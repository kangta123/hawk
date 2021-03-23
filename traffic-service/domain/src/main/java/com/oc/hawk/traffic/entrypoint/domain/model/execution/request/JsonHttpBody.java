package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

public class JsonHttpBody extends HttpBody {

    public JsonHttpBody(String body) {
        super(body);
    }

    @Override
    public String getData() {
        return (String) body;
    }
}
