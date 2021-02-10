package com.co.hawk.transfer.entrypoint.domain.model.request;

import java.util.Map;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpUriParam {
	
	private Map<String,String> params;
	
	public HttpUriParam(Map<String,String> params) {
		this.params = params;
	}
	
}
