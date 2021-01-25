package com.oc.hawk.monitor.constants;

public class MeasurementConstants {
    public static String TEMPLATE_CPU_USAGE = "sum(irate(container_cpu_usage_seconds_total{name=~\"%s\\u002e.*\"}[1m])) without (cpu)";
}
