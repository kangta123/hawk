package com.oc.hawk.transfer.entrypoint.domain.model.request;

import java.util.Map;

import com.oc.hawk.ddd.DomainValueObject;

import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpRequestParam {

	private Map<String,String> params;
	
	public HttpRequestParam(Map<String,String> params) {
		this.params = params;
	}
	
	public boolean nonNull() {
		if(params!=null && !params.isEmpty()) {
			return true;
		}else{
			return false;
		}
	}
}
