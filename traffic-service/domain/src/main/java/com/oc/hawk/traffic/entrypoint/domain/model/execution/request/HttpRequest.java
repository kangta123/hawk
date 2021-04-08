package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@DomainEntity
@Getter
@Builder
public class HttpRequest {

    private final HttpRequestID requestId;
    /**
     * 请求方法
     */
    private final HttpRequestMethod requestMethod;
    /**
     * 请求表单
     */
    private final HttpRequestParam requestParam;
    /**
     * 请求url
     */
    private final HttpUriParam httpUriParam;
    /**
     * 请求body
     */
    private final HttpBody requestBody;
    /**
     * 超时时间
     */
    private final Long timeOut;
    /**
     * 实例名
     */
    private final String instanceName;
    /**
     * 请求地址
     */
    private final String requestAddr;
    /**
     * 请求host
     */
    private final String host;
    /**
     * 实例Id
     */
    private final String instanceId;
    /**
     * 请求头
     */
    private HttpHeader httpHeader;

    public void addHttpHeader(HttpHeader httpHeader) {
        if (Objects.isNull(this.httpHeader)) {
            this.httpHeader = httpHeader;
        }
    }

    public String getHttpRequestUrl() {
        return new StringBuilder().append("http://")
            .append(this.instanceName)
            .append(":8080")
            .append(this.requestAddr)
            .toString();
    }
}
