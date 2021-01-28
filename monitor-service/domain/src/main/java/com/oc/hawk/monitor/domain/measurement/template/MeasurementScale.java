package com.oc.hawk.monitor.domain.measurement.template;

import com.oc.hawk.monitor.domain.measurement.unit.*;

/**
 * 监控数据的度量方式
 *
 * @author kangta123
 */
public enum MeasurementScale {
    /**
     * 百分比。
     */
    percent,
    /**
     * 存储(字节)。
     */
    storage,
    /**
     * 次数
     */
    count,
    /**
     * 时间
     */
    time,
    /**
     * 比率(字节/秒)，用于网络、存储。
     */
    rate;


    public MeasurementUnit getUnit(double value) {
        switch (this) {
            case percent:
                return new PercentMeasurementUnit();
            case storage:
                return new StorageMeasurementUnit(value);
            case rate:
                return new RateStorageMeasurementUnit(value);
            case count:
                return new CountMeasurementUnit(value);
            case time:
                return new TimeMeasurementUnit(value);
            default:
                break;
        }
        return null;
    }
}
