package com.oc.hawk.kubernetes.domain.model;

public interface IServiceLabelConstants {
    String LABEL_CATALOG = "service.catalog";
    String LABEL_PROJECT = "service.project";
    String LABEL_SERVICE_NAME = "SERVICE_NAME";

    String LABEL_APP = "app";
    String LABEL_VERSION = "version";

    String BUSINESS_SERVICE_CATALOG = "business";

    String ANNOTATION_PROMETHEUS_IO_PATH = "prometheus.io/path";
    String ANNOTATION_PROMETHEUS_IO_PORT = "prometheus.io/port";
    String ANNOTATION_PROMETHEUS_IO_SCRAPE = "prometheus.io/scrape";


}
