package org.cooze.stepcounter.record.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public final class ApplicationContextHolder {
    private static class Holder {
        private static volatile ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{
                "classpath:/spring/application-*.xml"
        });
    }


    public static ApplicationContext getContext() {
        return Holder.applicationContext;
    }
}
