package com.oc.hawk.kubernetes.api.dto;

import com.oc.hawk.kubernetes.api.constants.MatchString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficStringMatchDTO {
    private String value;
    private MatchString match;
}
