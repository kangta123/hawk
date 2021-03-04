package com.oc.hawk.transfer.entrypoint.domain.model.response;

import java.util.Map;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpResponseHeader {
	
	private Map<String,String> responeseHeader;
	
	public static HttpResponseHeader createHttpResponseHeader(Map<String,String> responseHeader) {
		return HttpResponseHeader.builder().responeseHeader(responseHeader).build();
	}
}