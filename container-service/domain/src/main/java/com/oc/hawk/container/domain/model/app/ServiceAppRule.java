package com.oc.hawk.container.domain.model.app;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class ServiceAppRule implements Comparable<ServiceAppRule> {
    private static final String DEFAULT_WEIGHT = "100";

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceApp app;

    private ServiceAppRuleRoute route;

    private boolean enabled;
    private int order;

    @Override
    public int compareTo(ServiceAppRule o) {
        return Integer.compare(this.order, o.order);
    }

    public void exchangeOrder(ServiceAppRule target) {
        int order = this.order;
        this.order = target.order;
        target.order = order;
    }

}
