package org.cooze.stepcounter.core.utils;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/
public final class StringUtils {


    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }
}
