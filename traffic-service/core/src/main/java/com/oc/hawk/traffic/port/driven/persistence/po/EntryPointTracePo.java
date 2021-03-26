package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Table(name = "traffic_trace")
@Entity
@DynamicUpdate
public class EntryPointTracePo extends BaseEntity {
	
    private String host;
    private String path;
    private String method;
    private String requestId;
    private String destAddr;
    private String sourceAddr;
    private String dstWorkload;
    private String dstNamespace;
    private Integer latency;
    private String protocol;
    private String spanId;
    private String parentSpanId;
    private String traceId;
    private Long configId;
    private String requestBody;
    private String requestHeaders;
    private String responseCode;
    private String responseBody;
    private String responseHeaders;
    private Timestamp startTime;
    private Timestamp createTime;
    

    public static EntryPointTracePo createBy(Trace history) {
        EntryPointTracePo historyPo = new EntryPointTracePo();
        historyPo.setHost(history.getHost());
        historyPo.setPath(history.getPath());
        historyPo.setMethod(history.getMethod());
        historyPo.setRequestId(history.getRequestId());
        historyPo.setDestAddr(history.getDestAddr());
        historyPo.setSourceAddr(history.getSourceAddr());
        historyPo.setDstNamespace(history.getDstNamespace());
        historyPo.setDstWorkload(history.getDstWorkload());
        historyPo.setLatency(history.getLatency());
        historyPo.setProtocol(history.getProtocol());
        historyPo.setSpanId(history.getSpanId());
        historyPo.setParentSpanId(history.getParentSpanId());
        historyPo.setTraceId(history.getTraceId());
        historyPo.setConfigId(history.getEntryPointId());
        historyPo.setRequestBody(history.getRequestBody());
        historyPo.setRequestHeaders(JsonUtils.object2Json(history.getRequestHeaders()));
        historyPo.setRequestId(history.getRequestId());
        historyPo.setResponseBody(history.getResponseBody());
        historyPo.setResponseHeaders(JsonUtils.object2Json(history.getResponseHeaders()));
        historyPo.setResponseCode(history.getResponseCode());
        historyPo.setStartTime(new Timestamp(history.getTimestamp()));
        historyPo.setCreateTime(new Timestamp(new Date().getTime()));
        return historyPo;
    }
    
    public Trace toTrace() {
        Map<String,Object> requestMap = JsonUtils.json2Map(requestHeaders);
        Map<String,String> requestHeadersMap = requestMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->(String)e.getValue()));
        Map<String,Object> responseMap = JsonUtils.json2Map(responseHeaders);
        Map<String,String> responseHeadersMap = responseMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->(String)e.getValue()));
        return Trace.builder()
                .id(new TraceId(getId()))
                .host(host)
                .path(path)
                .method(method)
                .requestId(requestId)
                .destAddr(destAddr)
                .sourceAddr(sourceAddr)
                .dstWorkload(dstWorkload)
                .dstNamespace(dstNamespace)
                .latency(latency)
                .protocol(protocol)
                .spanId(spanId)
                .parentSpanId(parentSpanId)
                .traceId(traceId)
                .entryPointId(configId)
                .requestBody(requestBody)
                .requestHeaders(requestHeadersMap)
                .responseCode(responseCode)
                .responseBody(responseBody)
                .responseHeaders(responseHeadersMap)
                .timestamp(startTime.getTime())
                .build();
    }

}
