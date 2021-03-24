package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Table(name = "traffic_entrypoint_trace")
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
        historyPo.setTraceId(history.getTraceId());
        historyPo.setConfigId(history.getConfigId());
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

}
