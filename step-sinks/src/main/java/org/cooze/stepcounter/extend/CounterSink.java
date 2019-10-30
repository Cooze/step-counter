package org.cooze.stepcounter.extend;


import org.cooze.stepcounter.core.protocol.TransportSchema;

import java.util.List;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-03
 **/
public interface CounterSink {
    void store(List<TransportSchema> resultDataList);
}
