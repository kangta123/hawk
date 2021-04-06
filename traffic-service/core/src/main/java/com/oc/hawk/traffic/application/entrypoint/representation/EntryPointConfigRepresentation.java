package com.oc.hawk.traffic.application.entrypoint.representation;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ContainerFacade;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointConfigRepresentation {

    private final ContainerFacade containerFacade;

    private final ProjectFacade projectFacade;

    public UserGroupEntryPointDTO toUserGroupEntryPointDTO(EntryPointConfigGroup group, List<EntryPointConfig> apiList) {
        UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
        userGroupApiDTO.setGroupId(group.getGroupId().getId());
        userGroupApiDTO.setGroupName(group.getGroupName());

        List<UserEntryPointDTO> userApiDTOList = new ArrayList<UserEntryPointDTO>();
        for (EntryPointConfig config : apiList) {
            UserEntryPointDTO userApiDTO = new UserEntryPointDTO();

            Long id = config.getConfigId().getId();
            userApiDTO.setId(id);

            String name = config.getDesign().getName();
            userApiDTO.setApiName(name);

            userApiDTOList.add(userApiDTO);
        }
        userGroupApiDTO.setApiList(userApiDTOList);
        return userGroupApiDTO;
    }

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

    public UserEntryPointDTO toUserEntryPointDTO(EntryPointConfig config, Long groupId) {
        UserEntryPointDTO userApiDTO = new UserEntryPointDTO();
        userApiDTO.setId(config.getConfigId().getId());
        userApiDTO.setGroupId(groupId);
        userApiDTO.setApiName(config.getDesign().getName());
        userApiDTO.setApiPath(config.getHttpResource().getPath().getPath());
        userApiDTO.setApiMethod(config.getHttpResource().getMethod().name());
        userApiDTO.setApiDesc(config.getDesign().getDesc());
        userApiDTO.setApp(config.getHttpResource().getTarget().getApp());
        Long projectId = config.getHttpResource().getTarget().getProjectId();
        userApiDTO.setProjectId(String.valueOf(projectId));
        ProjectDetailDTO projectDetailDTO = projectFacade.getProject(projectId);
        userApiDTO.setProjectName(projectDetailDTO.getName());
        return userApiDTO;
    }

    public List<UserGroupEntryPointDTO> toUserGroupEntryPointDTOList(List<EntryPointConfig> entryPointList, List<EntryPointConfigGroup> entryPointGroupList) {
        List<UserGroupEntryPointDTO> userGroupApiDTOList = new ArrayList<>();
        entryPointGroupList.forEach( obj -> {
            UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
            Long id = obj.getGroupId().getId();
            List<UserEntryPointDTO> userApiDTOList = new ArrayList<>();
            entryPointList.forEach(item -> {
                if(item.getGroupId().getId()==id) {
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

    public List<UserEntryPointDTO> toUserApiDTOList(List<EntryPointConfig> entryPointConfigList) {
        List<UserEntryPointDTO> userApiDTOList = entryPointConfigList.stream().map(obj -> toUserEntryPointDTO(obj, obj.getGroupId().getId())).collect(Collectors.toList());
        return userApiDTOList;
    }

    public ExecuteResponseDTO toExecuteResponseDTO(HttpResponse httpResponse) {
        ExecuteResponseDTO dto = new ExecuteResponseDTO();
        dto.setResponseCode(httpResponse.getResponseCode());
        dto.setResponseTime(httpResponse.getResponseTime());
        dto.setResponseBody(httpResponse.getResponseBody().getBody());
        dto.setResponseHeader(httpResponse.getResponseHeader().getResponeseHeader());
        return dto;
    }

    public List<TraceItemDTO> toTraceDetailList(List<Trace> traceList) {
        List<TraceItemDTO> list = new ArrayList<>();
        for (Trace trace : traceList) {
            TraceItemDTO dto = toTraceItemDTO(trace);
            list.add(dto);
        }
        return list;
    }

    public TraceDetailDTO toTraceDetailDTO(Trace trace) {
        TraceDetailDTO dto = new TraceDetailDTO();
        dto.setId(trace.getId().getId());
        dto.setHost(trace.getHost());
        dto.setPath(trace.getPath());
        dto.setMethod(trace.getMethod());
        dto.setDestAddr(trace.getDestAddr());
        dto.setRequestId(trace.getRequestId());
        dto.setSourceAddr(trace.getSourceAddr());
        dto.setDstWorkload(trace.getDstWorkload());
        dto.setDstNamespace(trace.getDstNamespace());
        dto.setLatency(trace.getLatency());
        dto.setProtocol(trace.getProtocol());
        dto.setSpanId(trace.getSpanId());
        dto.setParentSpanId(trace.getParentSpanId());
        dto.setTraceId(trace.getTraceId());
        dto.setRequestBody(trace.getRequestBody());
        dto.setRequestHeaders(JsonUtils.object2Json(trace.getRequestHeaders()));
        dto.setResponseCode(trace.getResponseCode());
        dto.setResponseBody(trace.getRequestBody());
        dto.setResponseHeaders(JsonUtils.object2Json(trace.getResponseHeaders()));
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
        //
        Map<String, List<TraceNodeDTO>> child = nodeList.stream().filter(node -> StringUtils.isNotBlank(node.getParentSpanId())).collect(Collectors.groupingBy(node -> node.getParentSpanId()));
        nodeList.forEach(node -> node.setChildNodeList(child.get(node.getSpanId())));
        return nodeList.stream().filter(node -> StringUtils.isBlank(node.getParentSpanId())).collect(Collectors.toList());
    }

    public TraceNodeDTO toTraceNodeDTO(Trace trace) {
        TraceNodeDTO traceNodeDTO = new TraceNodeDTO();
        traceNodeDTO.setTraceId(trace.getTraceId());
        traceNodeDTO.setSpanId(trace.getSpanId());
        traceNodeDTO.setParentSpanId(trace.getParentSpanId());
        traceNodeDTO.setInstanceName(trace.getDstWorkload());
        traceNodeDTO.setLatency(trace.getLatency());
        traceNodeDTO.setMethod(trace.getMethod());
        traceNodeDTO.setPath(trace.getPath());
        traceNodeDTO.setResponseCode(trace.getResponseCode());
        traceNodeDTO.setEntryPointName(trace.getEntryPointName());
        traceNodeDTO.setEntryPointId(trace.getEntryPointId());
        return traceNodeDTO;
    }

    public TraceResponseDTO toTraceResponseDTO(List<Trace> traceList,Long countNum) {
        TraceResponseDTO dto = new TraceResponseDTO();
        dto.setTotalSize(countNum);

        List<TraceListItemDTO> traceListItemList = traceList.stream().map(item -> {
            TraceListItemDTO traceListItemDTO = new TraceListItemDTO();
            traceListItemDTO.setId(item.getId().getId());

            Date date = new Date(item.getTimestamp());
            LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(date);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String localTime = localDateTime.format(df);
            traceListItemDTO.setRequestTime(localTime);
            traceListItemDTO.setExecuteTime(item.getLatency());
            traceListItemDTO.setResponseCode(item.getResponseCode());
            traceListItemDTO.setSpanId(item.getSpanId());
            return traceListItemDTO;
        }).collect(Collectors.toList());

        dto.setItems(traceListItemList);
        return dto;
    }

    public TraceItemDTO toTraceItemDTO(Trace trace) {
        TraceItemDTO dto = new TraceItemDTO();
        dto.setId(trace.getId().getId());
        dto.setPath(trace.getPath());
        dto.setMethod(trace.getMethod());
        dto.setDstWorkload(trace.getDstWorkload());
        dto.setLatency(trace.getLatency());
        dto.setSpanId(trace.getSpanId());
        dto.setResponseCode(trace.getResponseCode());
        Date date = new Date(trace.getTimestamp());
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(date);
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(DateUtils.HOUR_PATTERN);
        String startTime = fomatter.format(localDateTime);
        dto.setStartTime(startTime);
        dto.setEntryPointId(trace.getEntryPointId());
        dto.setEntryPointName(trace.getEntryPointName());
        return dto;
    }

}
