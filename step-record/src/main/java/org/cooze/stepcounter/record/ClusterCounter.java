package org.cooze.stepcounter.record;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.core.utils.GzipUtil;
import org.cooze.stepcounter.extend.utils.SpiLoadUtil;
import org.cooze.stepcounter.record.utils.CounterUtils;
import org.cooze.stepcounter.record.utils.SpringContextUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-02
 **/
public class ClusterCounter {


    private String schemaName;
    private long time;
    private String timeUnit;

    public ClusterCounter() {
    }

    public ClusterCounter(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void init() {
        switch (timeUnit) {
            case "s":
            case "S":
                this.time = this.time * 1000;
                break;
            case "m":
            case "M":
                this.time = this.time * 1000 * 60;
                break;
            case "h":
            case "H":
                this.time = this.time * 1000 * 60 * 60;
                break;
        }
        new Timer().schedule(new TimeTaskImpl(), 0, this.time);
    }


    class TimeTaskImpl extends TimerTask {

        @Override
        public void run() {
            DataStoreZkTemplate template = SpringContextUtils.getBean(DataStoreZkTemplate.class);
            if (template == null) {
                return;
            }
            template.setClusterLock(schemaName);
            List<byte[]> schemaDatas = template.listDatas(schemaName);
            if (schemaDatas == null || schemaDatas.isEmpty()) {
                template.releaseClusterLock(schemaName);
                return;
            }
            List<TransportSchema> list = new ArrayList<>();
            for (byte[] buf : schemaDatas) {
                Type type = new TypeToken<List<TransportSchema>>() {
                }.getType();
                list.addAll(new Gson().fromJson(new String(GzipUtil.unGzip(buf), Charset.forName("utf-8")), type));
            }
            Map<String, List<NameValue<Long>>> countValueMap = new HashMap<>();
            CounterUtils.count(countValueMap, list);
            List<TransportSchema> resultDataList = CounterUtils.countMapToSchema(countValueMap, schemaName);
            try {
                if (resultDataList != null && !resultDataList.isEmpty()) {
                    SpiLoadUtil.execution(resultDataList);
                }
                template.removeClusterData(schemaName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            template.releaseClusterLock(schemaName);
        }
    }

}
