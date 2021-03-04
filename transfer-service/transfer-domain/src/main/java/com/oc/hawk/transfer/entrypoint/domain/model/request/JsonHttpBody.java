package com.oc.hawk.transfer.entrypoint.domain.model.request;

public class JsonHttpBody extends HttpBody{

	public JsonHttpBody(String body) {
		super(body);
	}
	
	@Override
	public String getData() {
		return (String)body;
	}
}
