package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainEntity;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@DomainEntity
public class EntryPointHistory {
	
	private EntryPointHistoryID historyId;
	private Long start;
	private Long end;
	private EntryPointConfigID configId;
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	
}
