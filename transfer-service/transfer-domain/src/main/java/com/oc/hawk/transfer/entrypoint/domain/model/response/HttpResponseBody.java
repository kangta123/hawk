package com.oc.hawk.transfer.entrypoint.domain.model.response;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpResponseBody {
	
	private String body;
	
	public static HttpResponseBody createHttpResponseBody(String body) {
		return  HttpResponseBody.builder().body(body).build();
	}
	
}
