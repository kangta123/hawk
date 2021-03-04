package com.oc.hawk.transfer.entrypoint.domain.model.request;

import com.oc.hawk.ddd.DomainEntity;

@DomainEntity
public abstract class HttpBody {

	Object body;
	
	public HttpBody(Object body) {
		this.body = body;
	}
	
	public abstract Object getData();
}