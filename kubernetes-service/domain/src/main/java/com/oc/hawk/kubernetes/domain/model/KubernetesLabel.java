package com.oc.hawk.kubernetes.domain.model;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class KubernetesLabel {

    public static Map<String, String> withBusinessService() {
        return withBusinessService(null, null, null);
    }

    public static Map<String, String> withBusinessService(Long projectId, String app, String version) {
        Map<String, String> labels = Maps.newHashMap();

        if (projectId != null) {
            labels.put(IServiceLabelConstants.LABEL_PROJECT, String.valueOf(projectId));
        }

        if (StringUtils.isNotEmpty(app)) {
            labels.put(IServiceLabelConstants.LABEL_APP, app);
        }
        if (StringUtils.isNotEmpty(version)) {
            labels.put(IServiceLabelConstants.LABEL_VERSION, version);
        }
        labels.put(IServiceLabelConstants.LABEL_CATALOG, IServiceLabelConstants.BUSINESS_SERVICE_CATALOG);
        return labels;
    }

}
