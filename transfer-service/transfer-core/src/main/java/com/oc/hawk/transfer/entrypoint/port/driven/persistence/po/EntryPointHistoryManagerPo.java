package com.oc.hawk.transfer.entrypoint.port.driven.persistence.po;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHistory;
import com.oc.hawk.transfer.entrypoint.domain.model.request.JsonHttpBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "transfer_endpoint_request_history")
@Entity
@DynamicUpdate
public class EntryPointHistoryManagerPo extends BaseEntity{
	
	private String host;
	private String path;
	private String method;
	private String requestId;
	private Long traceId;
	private String requestBody;
	private String requestHeaders;
	private String responseStatus;
	private String responseBody;
	private String responseHeaders;
	private Long start;
	private Long end;
	private Timestamp startTime;
	private Timestamp createTime;
	private Timestamp updateTime;
	
	public static EntryPointHistoryManagerPo createBy(EntryPointHistory history) {
		EntryPointHistoryManagerPo historyPo = new EntryPointHistoryManagerPo();
		historyPo.setStart(history.getStart());
		historyPo.setStartTime(new Timestamp(history.getStart()));
		historyPo.setEnd(history.getEnd());
		historyPo.setHost(history.getHttpRequest().getHost());
		historyPo.setMethod(history.getHttpRequest().getRequestMethod().name());
		historyPo.setPath(history.getHttpRequest().getRequestAddr());
		historyPo.setRequestBody(((JsonHttpBody)history.getHttpRequest().getRequestBody()).getData());
		historyPo.setRequestHeaders(JsonUtils.object2Json(history.getHttpRequest().getHttpHeader().getHeaderMap()));
		historyPo.setRequestId(String.valueOf(history.getHttpRequest().getRequestId().getId()));
		historyPo.setResponseStatus(history.getHttpResponse().getResponseCode());
		historyPo.setResponseHeaders(JsonUtils.object2Json(history.getHttpResponse().getResponseHeader().getResponeseHeader()));
		historyPo.setCreateTime(new Timestamp(new Date().getTime()));
		historyPo.setUpdateTime(new Timestamp(new Date().getTime()));
		return historyPo;
	}
	
}
