package com.oc.hawk.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class NumberUtils {
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";


    public static boolean isMobile(String phone) {
        if (phone == null || phone.length() == 0) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }
}
