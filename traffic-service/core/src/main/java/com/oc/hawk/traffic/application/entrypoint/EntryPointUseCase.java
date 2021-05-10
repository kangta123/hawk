package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.traffic.application.entrypoint.representation.EntryPointConfigRepresentation;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ContainerFacade;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.facade.FileFacade;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.SpanContext;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointConfigExecutor;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointConfigGroups;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointGroupImportance;
import com.oc.hawk.traffic.entrypoint.domain.service.TrafficTraces;
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
    private final ProjectFacade projectFacade;
    private final ContainerFacade containerFacade;
    
    private final EntryPointResourceRepository entryPointResourceRepository;

    /**
     * 查询可见分组及接口
     */
    public List<UserGroupEntryPointDTO> queryGroupAndApiList() {
        List<EntryPointConfigGroup> entryPointGroupList
            = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
        List<EntryPointConfig> entryPointVisibilyList
            = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibilitiy(entryPointGroupList);
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
        EntryPointConfigID entryPointConfigId = entryPointConfigRepository.save(apiConfig);
        apiConfig.updateConfigId(entryPointConfigId);
        entryPointResourceRepository.addConfig(apiConfig);
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
    public List<UserGroupEntryPointDTO> queryApiByResource(String key) {
        List<EntryPointConfig> entryPointConfigList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointByKey(key);  
        List<EntryPointConfigGroup> entryPointGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getUserGroupList(entryPointConfigList);
        List<UserGroupEntryPointDTO> userGroupEntryPointList = entryPointConfigRepresentation.toUserGroupEntryPointDTOList(entryPointConfigList, entryPointGroupList);
        return userGroupEntryPointList;
    }

    /**
     * 单个接口信息查询
     */
    public EntryPointDTO queryApiInfo(Long id) {
        EntryPointConfig config = entryPointConfigRepository.byId(new EntryPointConfigID(id));
        if (Objects.isNull(config)) {
            return new EntryPointDTO();
        }
        return entryPointConfigRepresentation.toUserEntryPointDTO(config, config.getGroupId().getId());
    }

    /**
     * 导入接口
     */
    @Transactional(rollbackFor = Exception.class)
    public void importGroup(Long groupId,ImportGroupDTO importGroupDTO) {
        EntryPointConfigGroup group = entryPointConfigRepository.byId(new EntryPointGroupID(groupId));
        List<EntryPointConfig> entryPointList = entryPointConfigFactory.create(group,importGroupDTO.getRequests());
        new EntryPointGroupImportance(entryPointConfigRepository,entryPointResourceRepository).importPostmanJson(group.getGroupId(), entryPointList);
    }

    /**
     * 执行接口
     */
    public ExecuteResponseDTO execute(ExecuteCommand executeCommand) {
        EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(executeCommand.getEntryPointId()));
        if(Objects.nonNull(executeCommand.getProjectId())) {
            entryPointConfig.updateProjectId(executeCommand.getProjectId());
            entryPointConfigRepository.save(entryPointConfig);
        }
        
        HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig, executeCommand);
        
        HttpResponse httpResponse = new EntryPointConfigExecutor(entryPointExcutor).execute(httpRequest);
        
        ExecuteResponseDTO excuteResponseDTO = entryPointConfigRepresentation.toExecuteResponseDTO(httpResponse);
        return excuteResponseDTO;
    }
    
    /**
     * 链路信息查询
     */
    public TraceItemPageDTO queryTraceInfoList(Integer page,Integer size,String key){
    	List<Trace> traceList = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryTraceInfoList(page,size,key);
    	return entryPointConfigRepresentation.toTraceDetailList(traceList,size);
    }
    
    /**
     * 根据id删除Api
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntryPoint(Long id) {
        EntryPointConfig config = entryPointConfigRepository.byId(new EntryPointConfigID(id));
        if(Objects.isNull(config)) {
           return ;
        }
        new EntryPointConfigGroups(entryPointConfigRepository).deleteEntryPoint(new EntryPointConfigID(id));
        entryPointResourceRepository.deleteConfig(config);
    }
    
    /**
     * 根据链路id查询单个请求历史详情
     */
    public TraceDetailDTO queryTrafficTraceInfo(EntryPointConfigID entryPointId,TraceId traceId) {
        Trace trace = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryTrafficTraceInfo(entryPointId,traceId);
        return entryPointConfigRepresentation.toTraceDetailDTO(trace);
    }
    
    /**
     * 链路节点列表查询
     */
    public List<TraceNodeDTO> queryTraceNodeList(String spanId){
        Trace traceParam = Trace.builder().spanContext(new SpanContext(spanId,null,null, null)).build();
        List<Trace> traceList = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryTraceNodeList(traceParam);
        return entryPointConfigRepresentation.toTreeNodeDTOList(traceList);
    }
    
    /**
     * 接口历史链路信息查询
     */
    public TraceResponseDTO queryTrafficTraceList(Integer page,Integer size,Long entryPointId) {
        EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(entryPointId));
        List<Trace> traceList = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryTrafficTraceList(page,size,entryPointConfig);
        Long countNum = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryTrafficTraceCount(entryPointConfig);
        return entryPointConfigRepresentation.toTraceResponseDTO(traceList,countNum);
    }
    
    /**
     * 获取下载文件
     */
    public byte[] getFile(String fileName) {
        log.info("download file {}", fileName);
        return fileFacade.getDownloadFile(fileName);
    }
    
    /**
     * 启动加载接口配置信息
     */
    public void loadEntryPointConfigData() {
        log.info("load entrypoint config data.");
        List<EntryPointConfig> entryPointConfigList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointConfigList();
        entryPointResourceRepository.deleteAllEntryPointResource(entryPointConfigList);
        entryPointResourceRepository.loadEntryPointResource(entryPointConfigList);
    }
    
    /**
     * 根据id删除组
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntryPointGroup(Long id) {
        EntryPointConfigGroup group = entryPointConfigRepository.byId(new EntryPointGroupID(id));
        //groupId不存在,不能删除.
        if(Objects.isNull(group)) {
           return ;
        }
        List<EntryPointConfig> entryPointConfigList = entryPointConfigRepository.byGroupIdList(List.of(group.getGroupId()));
        //该组下存在api,不能删除.
        if(Objects.nonNull(entryPointConfigList) && !entryPointConfigList.isEmpty()) {
            throw new AppBusinessException("分组下包含api不能删除");
        }
        new EntryPointConfigGroups(entryPointConfigRepository).deleteEntryPointGroup(new EntryPointGroupID(id));
    }
    
    /**
     * 链路详情查询
     */
    public TraceDetailDTO queryApiTraceDetail(String spanId){
        Trace traceParam = Trace.builder().spanContext(new SpanContext(spanId,null,null, null)).build();
        Trace trace = new TrafficTraces(entryPointConfigRepository,entryPointResourceRepository).queryApiTraceDetail(traceParam);
        return entryPointConfigRepresentation.toTraceDetailDTO(trace);
    }

}
