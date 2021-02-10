package com.co.hawk.transfer.entrypoint.domain.model.request;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

@DomainEntity
@Getter
public class HttpBody {

	private String body;
	
	public HttpBody(String body) {
		this.body = body;
	}
	
}
