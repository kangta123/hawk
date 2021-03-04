package com.oc.hawk.transfer.entrypoint.domain.model.request;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
	
	public Map<String,Object> getHttpUriVariables() {
		Map<String,Object> uriVariablesMap = new HashMap<String,Object>();
		this.params.forEach((key, value) -> {
			 uriVariablesMap.put(key, value);
		});
		return uriVariablesMap;
	}
}
