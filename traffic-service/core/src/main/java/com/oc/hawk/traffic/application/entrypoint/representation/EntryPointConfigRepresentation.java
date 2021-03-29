package com.oc.hawk.traffic.application.entrypoint.representation;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceDetailDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceListItemDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.ExecuteResponseDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceNodeDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceResponseDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserEntryPointDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserGroupDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntryPointConfigRepresentation {

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
        userApiDTO.setProjectId(String.valueOf(config.getHttpResource().getTarget().getProjectId()));
        return userApiDTO;
    }

    public List<UserGroupEntryPointDTO> toUserGroupEntryPointDTOList(List<EntryPointConfig> entryPointList, List<EntryPointConfigGroup> entryPointGroupList) {

        Map<EntryPointGroupID, List<EntryPointConfig>> entryPointMap = entryPointList.stream().collect(Collectors.groupingBy(EntryPointConfig::getGroupId));

        Map<EntryPointGroupID, String> entryPointGroupMap = entryPointGroupList.stream().collect(Collectors.toMap(EntryPointConfigGroup::getGroupId, EntryPointConfigGroup::getGroupName, (oldValue, newValue) -> newValue));
        Map<Long, String> groupIdMap = new HashMap<>();
        entryPointGroupMap.entrySet().forEach(entry -> {
            groupIdMap.put(entry.getKey().getId(), entry.getValue());
        });

        List<UserGroupEntryPointDTO> userGroupApiDTOList = new ArrayList<UserGroupEntryPointDTO>();
        entryPointMap.forEach((key, value) -> {
            UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
            userGroupApiDTO.setGroupId(key.getId());

            String groupName = groupIdMap.get(key.getId());
            userGroupApiDTO.setGroupName(groupName);

            List<UserEntryPointDTO> userApiDTOList = new ArrayList<>();
            for (EntryPointConfig config : value) {
                userApiDTOList.add(toUserEntryPointDTO(config, key.getId()));
            }
            userGroupApiDTO.setApiList(userApiDTOList);
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
    
    public List<TraceDetailDTO> toTraceDetailList(List<Trace> traceList) {
        List<TraceDetailDTO> list = new ArrayList<>();
        for(Trace trace : traceList) {
            TraceDetailDTO dto = toTraceDetailDTO(trace);
            list.add(dto);
        }
        return list;
    }
    
    public TraceDetailDTO toTraceDetailDTO(Trace trace){
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
            ZoneId zoneId = ZoneId.of(ZoneId.SHORT_IDS.get("PST"));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zoneId);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String localTime = df.format(localDateTime);
            traceListItemDTO.setRequestTime(localTime);
            traceListItemDTO.setExecuteTime(item.getLatency());
            return traceListItemDTO;
        }).collect(Collectors.toList());
        
        dto.setTraceListItemDTO(traceListItemList);
        return dto;
    }
    
}
