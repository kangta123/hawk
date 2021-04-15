package com.oc.hawk.traffic.entrypoint.domain.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrafficTraceHeaderFilterConfig {
    
    private String requestKey;
    private String responseKey;
    
    public List<String> getRequestFilterKey(){
        if(StringUtils.isNotEmpty(this.requestKey)) {
            return Stream.of(this.requestKey.split(",")).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    public List<String> getResponseFilterKey(){
        if(StringUtils.isNotEmpty(this.responseKey)) {
            return Stream.of(this.responseKey.split(",")).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
}
