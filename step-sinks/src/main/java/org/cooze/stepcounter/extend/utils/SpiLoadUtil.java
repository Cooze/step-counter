package org.cooze.stepcounter.extend.utils;



import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.extend.CounterSink;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-03
 **/
public class SpiLoadUtil {

    public static void execution(List<TransportSchema> dataList) {
        ServiceLoader<CounterSink> load = ServiceLoader.load(CounterSink.class);
        Iterator<CounterSink> helloworlds = load.iterator();
        while (helloworlds.hasNext()) {
            helloworlds.next().store(dataList);
        }
    }
}
