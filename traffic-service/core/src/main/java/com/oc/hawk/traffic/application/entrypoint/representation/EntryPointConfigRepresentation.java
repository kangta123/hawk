package com.oc.hawk.traffic.application.entrypoint.representation;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointConfigRepresentation {
    
    private final ProjectFacade projectFacade;
    

    public List<UserGroupDTO> toUserGroupDTO(List<EntryPointConfigGroup> allGroupList, List<EntryPointConfigGroup> groupList) {
        List<UserGroupDTO> userGroupDTOList = new ArrayList<UserGroupDTO>();
        //转换ApiConfigGroup为groupId的list
        for (EntryPointConfigGroup group : allGroupList) {
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            Long groupId = group.getGroupId().getId();
            userGroupDTO.setGroupId(groupId);

            String groupName = group.getGroupName();
            userGroupDTO.setGroupName(groupName);

            List<Long> groupIdsList = groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList());

            if (groupIdsList.contains(groupId)) {
                userGroupDTO.setGroupStatus(1);
            } else {
                userGroupDTO.setGroupStatus(0);
            }
            userGroupDTOList.add(userGroupDTO);
        }
        return userGroupDTOList;
    }

    public EntryPointDTO toUserEntryPointDTO(EntryPointConfig config, Long groupId) {
        EntryPointDTO userApiDTO = new EntryPointDTO();
        userApiDTO.setId(config.getConfigId().getId());
        userApiDTO.setGroupId(groupId);
        userApiDTO.setApiName(config.getDescription().getName());
        userApiDTO.setApiPath(config.getHttpResource().getPath().getPath());
        userApiDTO.setApiMethod(config.getHttpResource().getMethod().name());
        userApiDTO.setApiDesc(config.getDescription().getDesc());
        Long projectId = config.getProjectId();
        if(Objects.nonNull(projectId)) {
            userApiDTO.setProjectId(String.valueOf(projectId));
            ProjectDetailDTO projectDetailDTO = projectFacade.getProject(projectId);
            userApiDTO.setProjectName(projectDetailDTO.getName());
        }
        return userApiDTO;
    }

    public List<UserGroupEntryPointDTO> toUserGroupEntryPointDTOList(List<EntryPointConfig> entryPointList, List<EntryPointConfigGroup> entryPointGroupList) {
        List<UserGroupEntryPointDTO> userGroupApiDTOList = new ArrayList<>();
        entryPointGroupList.forEach( obj -> {
            UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
            Long id = obj.getGroupId().getId();
            List<EntryPointDTO> userApiDTOList = new ArrayList<>();
            entryPointList.forEach(item -> {
                if(item.getGroupId().getId().equals(id)) {
                    userApiDTOList.add(toUserEntryPointDTO(item, item.getGroupId().getId()));
                    userGroupApiDTO.setApiList(userApiDTOList);
                }
            });
            String groupName = obj.getGroupName();
            userGroupApiDTO.setGroupId(id);
            userGroupApiDTO.setGroupName(groupName);
            userGroupApiDTOList.add(userGroupApiDTO);
        });
        return userGroupApiDTOList;
    }

    public ExecuteResponseDTO toExecuteResponseDTO(HttpResponse httpResponse) {
        ExecuteResponseDTO dto = new ExecuteResponseDTO();
        dto.setResponseCode(httpResponse.getResponseCode());
        dto.setResponseTime(httpResponse.getResponseTime());
        dto.setResponseBody(httpResponse.getResponseBody().getBody());
        dto.setResponseHeaders(httpResponse.getResponseHeader().getResponeseHeader());
        return dto;
    }
    
    public TraceItemPageDTO toTraceDetailList(List<Trace> traceList,Integer size) {

        final List<TraceItemDTO> list = traceList.stream().map(this::toTraceItemDTO).collect(Collectors.toList());

        TraceItemPageDTO itemPage = new TraceItemPageDTO();
        if(list.size() > size) {
            itemPage.setHasNext(true);
            itemPage.setItems(list.subList(0, size));
        }else{
            itemPage.setHasNext(false);
            itemPage.setItems(list);
        }
        return itemPage;
    }
    
    public TraceDetailDTO toTraceDetailDTO(Trace trace){
        TraceDetailDTO dto = new TraceDetailDTO();
        dto.setId(trace.getId().getId());
        dto.setHost(trace.getHost());
        dto.setPath(trace.getHttpResource().getPath().getPath());
        dto.setMethod(trace.getHttpResource().getMethod().name());
        dto.setDestAddr(trace.getDestination().getDestAddr());
        dto.setRequestId(trace.getRequestId());
        dto.setSourceAddr(trace.getSourceAddr());
        dto.setDstWorkload(trace.getDestination().getDstWorkload());
        dto.setDstNamespace(trace.getDestination().getDstNamespace());
        dto.setLatency(trace.getLatency().getTime());
        dto.setProtocol(trace.getProtocol());
        dto.setSpanId(trace.getSpanContext().getSpanId());
        dto.setParentSpanId(trace.getSpanContext().getParentSpanId());
        dto.setTraceId(trace.getSpanContext().getTraceId());
        dto.setRequestBody(trace.getRequestBody().getBody());
        dto.setRequestHeaders(trace.getRequestHeaders().getHeaderMap());
        dto.setResponseCode(trace.getResponseCode().getCode());
        dto.setResponseBody(trace.getResponseBody().getBody()); 
        dto.setResponseHeaders(trace.getResponseHeaders().getResponeseHeader());
        dto.setStartTime(trace.getTimestamp());
        dto.setEntryPointId(trace.getEntryPointId());
        dto.setEntryPointName(trace.getEntryPointName());
        return dto;
    }
    
    public List<TraceNodeDTO> toTreeNodeDTOList(List<Trace> traceList) {
        List<TraceNodeDTO> nodeList = new ArrayList<>();
        for(Trace trace : traceList) {
            TraceNodeDTO dto = toTraceNodeDTO(trace);
            nodeList.add(dto);
        }
        return nodeList;
    }
    
    private TraceNodeDTO toTraceNodeDTO(Trace trace) {
        TraceNodeDTO traceNodeDTO = new TraceNodeDTO();
        traceNodeDTO.setTraceId(trace.getSpanContext().getTraceId());
        traceNodeDTO.setSpanId(trace.getSpanContext().getSpanId());
        traceNodeDTO.setParentSpanId(trace.getSpanContext().getParentSpanId());
        traceNodeDTO.setInstanceName(trace.getDestination().getDstWorkload());
        traceNodeDTO.setLatency(trace.getLatency().getTime());
        traceNodeDTO.setMethod(trace.getHttpResource().getMethod().name());
        traceNodeDTO.setPath(trace.getHttpResource().getPath().getPath());
        traceNodeDTO.setResponseCode(trace.getResponseCode().getCode());
        traceNodeDTO.setEntryPointName(trace.getEntryPointName());
        traceNodeDTO.setEntryPointId(trace.getEntryPointId());
        traceNodeDTO.setStartTime(trace.getTimestamp());
        return traceNodeDTO;
    }
    
    public TraceResponseDTO toTraceResponseDTO(List<Trace> traceList,Long countNum) {
        TraceResponseDTO dto = new TraceResponseDTO();
        dto.setTotalSize(countNum);
        
        List<TraceListItemDTO> traceListItemList = traceList.stream().map(item -> {
            TraceListItemDTO traceListItemDTO = new TraceListItemDTO();
            traceListItemDTO.setId(item.getId().getId());
            traceListItemDTO.setStartTime(item.getTimestamp());
            traceListItemDTO.setLatency(item.getLatency().getTime());
            traceListItemDTO.setResponseCode(item.getResponseCode().getCode());
            traceListItemDTO.setSpanId(item.getSpanContext().getSpanId());
            return traceListItemDTO;
        }).collect(Collectors.toList());
        
        dto.setItems(traceListItemList);
        return dto;
    }
    
    private TraceItemDTO toTraceItemDTO(Trace trace) {
        TraceItemDTO dto = new TraceItemDTO();
        dto.setId(trace.getId().getId());
        dto.setPath(trace.getHttpResource().getPath().getPath());
        dto.setMethod(trace.getHttpResource().getMethod().name());
        dto.setDstWorkload(trace.getDestination().getDstWorkload());
        dto.setLatency(trace.getLatency().getTime());
        dto.setSpanId(trace.getSpanContext().getSpanId());
        dto.setResponseCode(trace.getResponseCode().getCode());
        dto.setStartTime(trace.getTimestamp());
        dto.setEntryPointId(trace.getEntryPointId());
        dto.setEntryPointName(trace.getEntryPointName());
        return dto;
    }
    
}
