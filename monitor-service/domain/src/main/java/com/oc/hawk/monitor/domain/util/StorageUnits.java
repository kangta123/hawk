package com.oc.hawk.monitor.domain.util;


import java.text.DecimalFormat;

/**
 * @author kangta123
 */
public class StorageUnits {
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;

    enum Unit implements IUnits {

        TERA_BYTE {
            @Override
            public String format(long size, String pattern) {
                return format(size, getUnitSize(), "TB", pattern);
            }
            @Override
            public long getUnitSize() {
                return T;
            }
            @Override
            public String toString() {
                return "Terabytes";
            }
        },
        GIGA_BYTE {
            @Override
            public String format(long size, String pattern) {
                return format(size, getUnitSize(), "GB", pattern);
            }
            @Override
            public long getUnitSize() {
                return G;
            }
            @Override
            public String toString() {
                return "Gigabytes";
            }
        },
        MEGA_BYTE {
            @Override
            public String format(long size, String pattern) {
                return format(size, getUnitSize(), "MB", pattern);
            }
            @Override
            public long getUnitSize() {
                return M;
            }
            @Override
            public String toString() {
                return "Megabytes";
            }
        },
        KILO_BYTE {
            @Override
            public String format(long size, String pattern) {
                return format(size, getUnitSize(), "kB", pattern);
            }
            @Override
            public long getUnitSize() {
                return K;
            }
            @Override
            public String toString() {
                return "Kilobytes";
            }

        };

        String format(long size, long base, String unit, String pattern) {
            return new DecimalFormat(pattern).format(
                Long.valueOf(size).doubleValue() /
                    Long.valueOf(base).doubleValue()
            ) + unit;
        }
    }

    public static String format(long size, String pattern) {
        for(Unit unit : Unit.values()) {
            if(size >= unit.getUnitSize()) {
                return unit.format(size, pattern);
            }
        }
        return ("???(" + size + ")???");
    }

    public static String format(long size) {
        return format(size, "#,##0.#");
    }
}
