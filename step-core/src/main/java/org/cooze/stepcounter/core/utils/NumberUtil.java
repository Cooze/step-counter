package org.cooze.stepcounter.core.utils;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-09
 **/
public final class NumberUtil {

    public static boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
