package com.oc.hawk.transfer.entrypoint.domain.model.request;

import java.util.Map;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class HttpHeader {
	
	private Map<String,String> headerMap;
	
	public HttpHeader(Map<String,String> headerMap) {
		this.headerMap = headerMap;
	}
	
	public boolean isJsonContentType() {
		String contentType = this.headerMap.get("Content-Type");
		if("application/json".equalsIgnoreCase(contentType)) {
			return true;
		}
		return false;
	}
	
	public boolean isFormContentType() {
		String contentType = this.headerMap.get("Content-Type");
		if("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
			return true;
		}
		return false;
	}
	
}