package com.oc.hawk.transfer.entrypoint.domain.model.request;

import java.util.Objects;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@DomainEntity
@Getter
@Builder
public class HttpRequest{
	
	private HttpRequestID requestId;
	
	/**
	 * 请求头
	 */
	private HttpHeader httpHeader;
	
	/**
	 * 请求方法
	 */
	private HttpRequestMethod requestMethod;
	
	/**
	 * 请求表单
	 */
	private HttpRequestParam requestParam;
	
	/**
	 * 请求url
	 */
	private HttpUriParam httpUriParam;
	
	/**
	 * 请求body
	 */
	private HttpBody requestBody;
	
	/**
	 * 超时时间
	 */
	private Long timeOut;
	
	/**
	 * 实例名
	 */
	private String instanceName;
	
	/**
	 * 请求地址
	 */
	private String requestAddr;
	
	/**
	 * 请求host
	 */
	private String host;

	
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
