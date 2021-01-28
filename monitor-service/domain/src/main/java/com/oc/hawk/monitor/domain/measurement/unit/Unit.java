package com.oc.hawk.monitor.domain.measurement.unit;

import java.text.DecimalFormat;

import static com.oc.hawk.monitor.domain.measurement.unit.UnitConstants.*;


/**
 * @author kangta123
 */
public enum Unit implements IUnits {

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
            return "TB";
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
            return "GB";
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
            return "MB";
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
            return "KB";
        }

    },
    BYTE {
        @Override
        public String format(long size, String pattern) {
            return format(size, getUnitSize(), "B", pattern);
        }

        @Override
        public long getUnitSize() {
            return B;
        }

        @Override
        public String toString() {
            return "B";
        }

    };

    String format(long size, long base, String unit, String pattern) {
        return new DecimalFormat(pattern).format(
            Long.valueOf(size).doubleValue() /
                Long.valueOf(base).doubleValue()
        ) + unit;
    }
}
