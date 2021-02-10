package com.co.hawk.transfer.entrypoint.domain.model.request;

import java.util.Objects;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@DomainEntity
@Getter
@Builder
public class HttpRequest{
	
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

	
	public void addHttpHeader(HttpHeader httpHeader) {
        if (Objects.isNull(this.httpHeader)) {
            this.httpHeader = httpHeader;
        }
    }
}
