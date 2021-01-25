package com.oc.hawk.kubernetes.api.dto;

import com.google.common.collect.Maps;
import com.oc.hawk.kubernetes.api.constants.MatchString;
import lombok.Getter;

import java.util.Map;

@Getter
public class HawkHttpTrafficPolicyMatchDTO {
    private TrafficStringMatchDTO uri;
    private String method;
    private Map<String, TrafficStringMatchDTO> headers;
    private Map<String, TrafficStringMatchDTO> requestParams;

    public void addHeader(String mode, String key, String value) {
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        this.getHeaders().put(key, new TrafficStringMatchDTO(value, MatchString.valueOf(mode)));
    }

    public void setUri(String mode, String value) {
        this.uri = new TrafficStringMatchDTO(value, MatchString.valueOf(mode));
    }

    public void addParams(String mode, String key, String value) {
        if (requestParams == null) {
            requestParams = Maps.newHashMap();
        }
        this.getRequestParams().put(key, new TrafficStringMatchDTO(value, MatchString.valueOf(mode)));
    }

    public void addMethod(String value) {
        this.method = value;
    }
}
