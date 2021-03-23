package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import java.util.Map;

public class FormHttpBody extends HttpBody {

    public FormHttpBody(Map<String, String> body) {
        super(body);
    }

    @Override
    public Map<String, String> getData() {
        return (Map<String, String>) body;
    }

}
