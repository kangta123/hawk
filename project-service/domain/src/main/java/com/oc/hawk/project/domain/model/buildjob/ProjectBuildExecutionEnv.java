package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.ddd.DomainValueObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@DomainValueObject
public class ProjectBuildExecutionEnv {
    private Map<String, Object> env;

    public ProjectBuildExecutionEnv(String env) {
        if (StringUtils.isNotEmpty(env)) {
            this.env = JsonUtils.json2Map(env);
        }
    }

    public ProjectBuildExecutionEnv(Map<String, Object> env) {
        this.env = env;
    }

    public Map<String, Object> env() {
        return this.env;
    }

    public String envStr() {
        return JsonUtils.object2Json(env);
    }
}
