package com.oc.hawk.monitor.domain.measurement.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MetricUnitUtils {
    public static int toInt(String value) {
        return Math.round(Float.parseFloat(value));
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double format(String unit, double value) {
        BigDecimal valueDecimal = new BigDecimal(value);
        switch (unit) {
            case BYTE_UNIT:
                return valueDecimal.divide(BYTE, 2, RoundingMode.HALF_UP).doubleValue();
            case KB_UNIT:
                return valueDecimal.divide(KB, 2, RoundingMode.HALF_UP).doubleValue();
            case MB_UNIT:
                return valueDecimal.divide(MB, 2, RoundingMode.HALF_UP).doubleValue();
            case GB_UNIT:
                return valueDecimal.divide(GB, 2, RoundingMode.HALF_UP).doubleValue();
            case TB_UNIT:
                return valueDecimal.divide(TB, 2, RoundingMode.HALF_UP).doubleValue();
            case MILLISECOND_UNIT:
                return valueDecimal.divide(MILLISECOND, 2, RoundingMode.HALF_UP).doubleValue();
            case SECOND_UNIT:
                return valueDecimal.divide(SECOND, 2, RoundingMode.HALF_UP).doubleValue();
            case MINUTE_UNIT:
                return valueDecimal.divide(MINUTE, 2, RoundingMode.HALF_UP).doubleValue();
            case HOUR_UNIT:
                return valueDecimal.divide(HOUR, 2, RoundingMode.HALF_UP).doubleValue();
            case DAY_UNIT:
                return valueDecimal.divide(DAY, 2, RoundingMode.HALF_UP).doubleValue();
            case PERCENT_UNIT:
                return valueDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            default:
                return value;
        }
    }

    private static final String PERCENT_UNIT = "%";

    private static final String BYTE_UNIT = "byte";

    private static final BigDecimal BYTE = new BigDecimal(1);

    private static final String KB_UNIT = "kb";

    private static final BigDecimal KB = new BigDecimal(1024).multiply(BYTE);

    private static final String MB_UNIT = "mb";

    private static final BigDecimal MB = KB.multiply(KB);

    private static final String GB_UNIT = "gb";

    private static final BigDecimal GB = MB.multiply(KB);

    private static final String TB_UNIT = "tb";

    private static final BigDecimal TB = GB.multiply(KB);

    private static final String MILLISECOND_UNIT = "ms";

    private static final BigDecimal MILLISECOND = new BigDecimal(1).divide(new BigDecimal(1000)).setScale(3);

    private static final String SECOND_UNIT = "s";

    private static final BigDecimal SECOND = new BigDecimal(1);

    private static final String MINUTE_UNIT = "m";

    private static final BigDecimal MINUTE = SECOND.multiply(new BigDecimal(60));

    private static final String HOUR_UNIT = "h";

    private static final BigDecimal HOUR = MINUTE.multiply(new BigDecimal(60));

    private static final String DAY_UNIT = "d";

    private static final BigDecimal DAY = HOUR.multiply(new BigDecimal(24));


}
