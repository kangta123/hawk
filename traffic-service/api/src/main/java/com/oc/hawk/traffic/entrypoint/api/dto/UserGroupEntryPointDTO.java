package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupEntryPointDTO {

    private Long groupId;
    private String groupName;
    private List<EntryPointDTO> apiList;

}
