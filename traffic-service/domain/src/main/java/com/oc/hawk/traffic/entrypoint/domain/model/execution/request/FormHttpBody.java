package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import java.util.Map;

public class FormHttpBody extends HttpBody {

    public FormHttpBody(String body) {
        super(body);
    }

    @Override
    public String getData() {
        return (String) body;
    }

}
