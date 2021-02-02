package com.oc.hawk.base.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QueryUserDTO {
    private String key;
    private List<Long> ids;
    private Boolean departmentIgnore;
}
