package org.cooze.stepcounter.record.utils;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public final class SpringContextUtils {

    public static <T> T getBean(Class<T> clazz) {
        return ApplicationContextHolder.getContext().getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return ApplicationContextHolder.getContext().getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return ApplicationContextHolder.getContext().getBean(beanName, clazz);
    }

}
