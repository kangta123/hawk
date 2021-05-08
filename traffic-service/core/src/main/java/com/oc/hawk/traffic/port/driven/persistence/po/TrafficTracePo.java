package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Table(name = "traffic_trace")
@Entity
@DynamicUpdate
public class TrafficTracePo extends BaseEntity {

    private String host;
    private String path;
    private String method;
    private String requestId;
    private String destAddr;
    private String sourceAddr;
    private String dstWorkload;
    private String dstNamespace;
    private Long latency;
    private String protocol;
    private String spanId;
    private String kind;
    private String parentSpanId;
    private String traceId;
    private String requestBody;
    private String requestHeaders;
    private String responseCode;
    private String responseBody;
    private String responseHeaders;
    private LocalDateTime startTime;
    private LocalDateTime createTime;

    public static TrafficTracePo createBy(Trace history) {
        TrafficTracePo historyPo = new TrafficTracePo();
        historyPo.setHost(history.getHost());
        historyPo.setPath(history.getHttpResource().getPath().getPath());
        historyPo.setMethod(history.getHttpResource().getMethod().name());
        historyPo.setRequestId(history.getRequestId());
        historyPo.setDestAddr(history.getDestination().getDestAddr());
        historyPo.setSourceAddr(history.getSourceAddr());
        historyPo.setDstNamespace(history.getDestination().getDstNamespace());
        historyPo.setDstWorkload(history.getDestination().getDstWorkload());
        historyPo.setLatency(history.getLatency().getLatency());
        historyPo.setProtocol(history.getProtocol());
        final SpanContext spanContext = history.getSpanContext();
        historyPo.setSpanId(spanContext.getSpanId());
        historyPo.setParentSpanId(spanContext.getParentSpanId());
        historyPo.setTraceId(spanContext.getTraceId());
        final Kind kind = spanContext.getKind();
        if (kind != null) {
            historyPo.setKind(String.valueOf(kind));
        }

        historyPo.setRequestBody(history.getRequestBody().getBody());
        historyPo.setRequestHeaders(JsonUtils.object2Json(history.getRequestHeaders().getHeaderMap()));
        historyPo.setRequestId(history.getRequestId());
        historyPo.setResponseBody(history.getResponseBody().getBody());
        historyPo.setResponseHeaders(JsonUtils.object2Json(history.getResponseHeaders().getResponeseHeader()));
        historyPo.setResponseCode(String.valueOf(history.getResponseCode().getCode()));
        Date historyDate = new Date(history.getTimestamp());
        historyPo.setStartTime(DateUtils.dateToLocalDateTime(historyDate));
        historyPo.setCreateTime(LocalDateTime.now());
        return historyPo;
    }

    public Trace toTrace() {
        Map<String, Object> requestMap = JsonUtils.json2Map(requestHeaders);
        Map<String, String> requestHeadersMap = requestMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
        Map<String, Object> responseMap = JsonUtils.json2Map(responseHeaders);
        Map<String, String> responseHeadersMap = responseMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
        return Trace.builder()
                .id(new TraceId(getId()))
                .host(host)
                .httpResource(new HttpResource(new HttpPath(path), HttpMethod.valueOf(method)))
                .destination(new Destination(dstWorkload, destAddr, dstNamespace))
                .requestId(requestId)
                .sourceAddr(sourceAddr)
                .latency(new Latency(latency))
                .protocol(protocol)
                .spanContext(new SpanContext(spanId, parentSpanId, traceId, kind))
                .requestBody(new HttpRequestBody(requestBody))
                .requestHeaders(new HttpRequestHeader(requestHeadersMap))
                .responseCode(new HttpResponseCode(Integer.parseInt(responseCode)))
                .responseBody(new HttpResponseBody(responseBody))
                .responseHeaders(new HttpResponseHeader(responseHeadersMap))
                .timestamp(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .build();
    }

}
