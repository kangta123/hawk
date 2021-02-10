package com.co.hawk.transfer.entrypoint.domain.model.request;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpRequestMethod {
	
	private String requestMethod;
	
	public HttpRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
	
	public boolean isPost() {
		if("POST".equalsIgnoreCase(this.requestMethod)) {
			return true;
		}
		return false;
	}
	
	public boolean isGet() {
		if("GET".equalsIgnoreCase(this.requestMethod)) {
			return true;
		}
		return false;
	}
	
	public boolean isPut() {
		if("GET".equalsIgnoreCase(this.requestMethod)) {
			return true;
		}
		return false;
	}
	
	public boolean isDelete() {
		if("DELETE".equalsIgnoreCase(this.requestMethod)) {
			return true;
		}
		return false;
	}
	
}
