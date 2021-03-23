package com.oc.hawk.traffic.application.entrypoint.representation;

import com.oc.hawk.traffic.entrypoint.api.dto.ExecuteResponseDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserEntryPointDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserGroupDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
}
