package com.oc.hawk.transfer.entrypoint.domain.model.response;


import com.oc.hawk.ddd.DomainEntity;

import lombok.Builder;
import lombok.Getter;

@DomainEntity
@Getter
@Builder
public class HttpResponse {
	
	private ResponseID responseId;
	
	/**
	 * 响应头
	 */
	private HttpResponseHeader responseHeader;
	
	/**
	 * 响应体 
	 */
	private HttpResponseBody responseBody;
	
	/**
	 * 响应时间
	 */
	private Long responseTime;
	
	/**
	 * 响应Code
	 */
	private String responseCode;
	
	public void addResponseHeader(HttpResponseHeader header) {
        if (this.responseHeader == null) {
            this.responseHeader = header;
        }
    }
	
}
