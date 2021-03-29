package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.application.entrypoint.representation.EntryPointConfigRepresentation;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.facade.FileFacade;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointConfigExecutor;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointConfigGroups;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointGroupImportance;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointTraces;
import com.oc.hawk.traffic.entrypoint.domain.service.excutor.EntryPointExcutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointUseCase {

    private final EntryPointConfigFactory entryPointConfigFactory;
    private final EntryPointConfigGroupFactory apiConfigGroupFactory;

    private final EntryPointConfigRepository entryPointConfigRepository;
    private final EntryPointConfigRepresentation entryPointConfigRepresentation;

    private final HttpRequestFactory httpRequestFactory;
    private final EntryPointExcutor entryPointExcutor;
    private final FileFacade fileFacade;
    

    /**
     * 查询可见分组及接口
     */
    public List<UserGroupEntryPointDTO> queryGroupAndApiList() {
        List<EntryPointConfigGroup> entryPointGroupList
            = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
        List<EntryPointConfig> entryPointVisibilyList
            = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibily(entryPointGroupList);
        return entryPointConfigRepresentation.toUserGroupEntryPointDTOList(entryPointVisibilyList, entryPointGroupList);
    }

    /**
     * 创建分组
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName) {
        EntryPointConfigGroup group = apiConfigGroupFactory.create(groupName);
        entryPointConfigRepository.save(group);
    }

    /**
     * 创建接口配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void createApi(CreateEntryPointCommand command) {
        log.info("create api with name:{},path:{},method:{}", command.getName(), command.getPath(), command.getMethod());
        EntryPointConfig apiConfig = entryPointConfigFactory.create(command);
        entryPointConfigRepository.save(apiConfig);
    }

    /**
     * 查询全部分组列表
     */
    public List<UserGroupDTO> queryUserAllGroupList() {
        List<EntryPointConfigGroup> allGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentAllGroupList();
        List<EntryPointConfigGroup> groupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
        return entryPointConfigRepresentation.toUserGroupDTO(allGroupList, groupList);
    }

    /**
     * 更新分组可见度
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserGroupVisibility(List<Long> groupIds) {
        List<EntryPointGroupID> entryPointGroupIdList = entryPointConfigFactory.createGroupInvisibility(groupIds);
        new EntryPointConfigGroups(entryPointConfigRepository).updateVisibility(entryPointGroupIdList, false);
    }

    /**
     * 根据关键字模块查询接口列表
     */
    public List<UserEntryPointDTO> queryApiByResource(String key) {
        List<EntryPointConfig> entryPointConfigList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointByKey(key);
        List<UserEntryPointDTO> userApiDTOList = entryPointConfigRepresentation.toUserApiDTOList(entryPointConfigList);
        return userApiDTOList;
    }

    /**
     * 单个接口信息查询
     */
    public UserEntryPointDTO queryApiInfo(Long id) {
        EntryPointConfig config = entryPointConfigRepository.byId(new EntryPointConfigID(id));
        if (Objects.isNull(config)) {
            return new UserEntryPointDTO();
        }
        return entryPointConfigRepresentation.toUserEntryPointDTO(config, config.getGroupId().getId());
    }

    /**
     * 导入接口
     */
    @Transactional(rollbackFor = Exception.class)
    public void importGroup(ImportGroupDTO importGroupDTO) {
        List<EntryPointConfig> entryPointList = entryPointConfigFactory.create(importGroupDTO.getRequests());
        EntryPointConfigGroup entryPointGroup = apiConfigGroupFactory.create(importGroupDTO.getName());
        new EntryPointGroupImportance(entryPointConfigRepository).importPostmanJson(entryPointGroup, entryPointList);
    }

    /**
     * 执行接口
     */
    public ExecuteResponseDTO execute(ExecuteCommand executeCommand) {
        EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(executeCommand.getEntryPointId()));
        HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig, executeCommand);
        
        HttpResponse httpResponse = new EntryPointConfigExecutor(entryPointExcutor).execute(httpRequest);
        
        ExecuteResponseDTO excuteResponseDTO = entryPointConfigRepresentation.toExecuteResponseDTO(httpResponse);
        return excuteResponseDTO;
    }
    
    /**
     * 链路信息查询
     */
    public List<TraceDetailDTO> queryTraceInfoList(Integer page,Integer size,String path,String instanceName){
    	List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(page,size,path,instanceName);
    	return entryPointConfigRepresentation.toTraceDetailList(traceList);
    }
    
    /**
     * 根据id删除Api
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntryPoint(Long id) {
        new EntryPointConfigGroups(entryPointConfigRepository).deleteEntryPoint(new EntryPointConfigID(id));
    }
    
    /**
     * 根据链路id查询单个请求历史详情
     */
    public TraceDetailDTO queryApiHistoryInfo(TraceId traceId) {
        Trace trace = new EntryPointTraces(entryPointConfigRepository).queryApiHistoryInfo(traceId);
        return entryPointConfigRepresentation.toTraceDetailDTO(trace);
    }
    
    /**
     * 链路节点列表查询
     */
    public List<TraceNodeDTO> queryTraceNodeList(String spanId){
        Trace traceParam = Trace.builder().spanId(spanId).build();
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceNodeList(traceParam);
        return entryPointConfigRepresentation.toTreeNodeDTOList(traceList);
    }
    
    /**
     * 接口历史链路信息查询
     */
    public TraceResponseDTO queryApiHistoryList(Integer page,Integer size,Long entryPointId) {
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryApiHistoryList(page,size,new EntryPointConfigID(entryPointId));
        Long countNum = new EntryPointTraces(entryPointConfigRepository).queryApiHistoryCount(new EntryPointConfigID(entryPointId));
        return entryPointConfigRepresentation.toTraceResponseDTO(traceList,countNum);
    }
    
    /**
     * 获取下载文件
     */
    public byte[] getDownloanFile() {
        return fileFacade.getDownloanFile();
    }
}
