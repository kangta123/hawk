//package com.oc.hawk.monitor.domain.metric;
//
//import lombok.Getter;
//
//@Getter
//public enum MetricType {
//
//    CPU_USAGE("sum(irate(container_cpu_usage_seconds_total{name=~\"%s\\u002e.*\"}[1m])) without (cpu)", MetricGroup.SYSTEM,
//            MetricUnitType.Percent),
//    MEMORY_USAGE("container_memory_usage_bytes{name=~\"%s\\u002e.*\"}", MetricGroup.SYSTEM, MetricUnitType.Byte),
//    NETWORK_IN_USAGE("sum(irate(container_network_receive_bytes_total{name=~\"%s\\u002e.*\"}[1m])) without (interface)", MetricGroup.SYSTEM,
//            MetricUnitType.Flow),
//    NETWORK_OUT_USAGE("sum(irate(container_network_transmit_bytes_total{name=~\"%s\\u002e.*\"}[1m])) without (interface)", MetricGroup.SYSTEM,
//            MetricUnitType.Flow),
//    DISK_READ_USAGE("sum(irate(container_fs_reads_bytes_total{name=~\"%s\\u002e.*\"}[1m])) without (device)", MetricGroup.SYSTEM, MetricUnitType.Byte),
//    DISK_WRITE_USAGE("sum(irate(container_fs_writes_bytes_total{name=~\"%s\\u002e.*\"}[1m])) without (device)", MetricGroup.SYSTEM,
//            MetricUnitType.Byte),
//
//    TOMCAT_REQUEST_COUNT("tomcat_requestCount_total{app=\"%s\", job=\"jvm\"}", MetricGroup.TOMCAT, MetricUnitType.COUNT),
//
//    JVM_HEAP_USED_USAGE("jvm_memory_bytes_used{area=\"heap\", app=\"%s\", job=\"jvm\"}", MetricGroup.JVM, MetricUnitType.Byte),
//    JVM_NONE_HEAP_USED_USAGE("jvm_memory_bytes_used{area=\"nonheap\", app=\"%s\", job=\"jvm\"}", MetricGroup.JVM, MetricUnitType.Byte),
//    JVM_THREAD_COUNT("java_lang_Threading_ThreadCount{app=\"%s\", job=\"jvm\"}", MetricGroup.JVM, MetricUnitType.COUNT),
//    JVM_SURVIVOR_SPACE("jvm_memory_pool_bytes_used{app=\"%s\", job=\"jvm\", pool=\"Par Survivor Space\"}", MetricGroup.JVM, MetricUnitType.Byte),
//    JVM_CODE_CACHE("jvm_memory_pool_bytes_used{app=\"%s\", job=\"jvm\", pool=\"Code Cache\"}", MetricGroup.JVM, MetricUnitType.Byte),
//    JVM_CMS_OLD_GEN("jvm_memory_pool_bytes_used{app=\"%s\", job=\"jvm\", pool=\"CMS Old Gen\"}", MetricGroup.GC, MetricUnitType.Byte),
//    JVM_GC_PAR_NEW_DURATION("jvm_gc_collection_seconds_sum{app=\"%s\", gc=\"ParNew\"}", MetricGroup.GC, MetricUnitType.TimeMS),
//    JVM_GC_CMS_DURATION("jvm_gc_collection_seconds_sum{app=\"%s\", gc=\"ConcurrentMarkSweep\"}", MetricGroup.GC, MetricUnitType.TimeMS),
//
//    JVM_GC_CMS_COUNT("jvm_gc_collection_seconds_count{app=\"%s\", gc=\"ConcurrentMarkSweep\"}", MetricGroup.GC, MetricUnitType.COUNT),
//    JVM_GC_PAR_NEW_COUNT("jvm_gc_collection_seconds_count{app=\"%s\", gc=\"ParNew\"}", MetricGroup.GC, MetricUnitType.COUNT);
//
//    private String template;
//    private MetricUnitType unitType;
//    private MetricGroup group;
//
//    MetricType(String template, MetricGroup group, MetricUnitType unitType) {
//        this.template = template;
//        this.unitType = unitType;
//        this.group = group;
//    }
//
//
//    public String getMetric(String... params) {
//        return String.format(template, params);
//    }
//
//    public String format(String data) {
//        return this.unitType.format(data);
//    }
//}
