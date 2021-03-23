package com.oc.hawk.traffic.entrypoint.domain.model.execution.response;


import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@DomainEntity
@Getter
@Builder
public class HttpResponse {

    private final ResponseID responseId;
    /**
     * 响应体
     */
    private final HttpResponseBody responseBody;
    /**
     * 响应时间
     */
    private final Long responseTime;
    /**
     * 响应Code
     */
    private final String responseCode;
    /**
     * 响应头
     */
    private HttpResponseHeader responseHeader;

    public void addResponseHeader(HttpResponseHeader header) {
        if (this.responseHeader == null) {
            this.responseHeader = header;
        }
    }

}
