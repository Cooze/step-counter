package org.cooze.stepcounter.record;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.core.utils.GzipUtil;
import org.cooze.stepcounter.record.utils.CounterUtils;
import org.cooze.stepcounter.record.utils.SpringContextUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-07
 **/
public class DataCounter {
    private String schemaName;
    private String nodeName;
    private SchemaDataContainerEvent schemaDataContainerEvent;


    public DataCounter(String nodeName, String schemaName, SchemaDataContainerEvent schemaDataContainerEvent) {
        this.schemaName = schemaName;
        this.schemaDataContainerEvent = schemaDataContainerEvent;
        this.nodeName = nodeName;
        countExecution();
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    private void countExecution() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                DataStoreZkTemplate template = SpringContextUtils.getBean(DataStoreZkTemplate.class);
                if (template == null) {
                    System.out.println("template==null.");
                    return;
                }
                //集群锁则不处理
                boolean clusterHasLock = template.clusterHasLock(schemaName);
                if (clusterHasLock) {
                    return;
                }

                boolean selfLock = template.trySelfLock(schemaName, nodeName);
                //没获取锁则不处理
                if (!selfLock) {
                    return;
                }
                SchemaDataContainer container = schemaDataContainerEvent.countSchema();
                if (container.getContainer().size() == 0) {
                    //释放锁
                    template.releaseSelfLock();
                    return;
                }

                //从zk中取数据
                byte[] data = template.getData(schemaName, nodeName);
                List<TransportSchema> zkTransportSchemaList = null;
                if (data != null && data.length > 0) {
                    Type type = new TypeToken<List<TransportSchema>>() {
                    }.getType();

                    zkTransportSchemaList = new Gson().fromJson(new String(GzipUtil.unGzip(data), Charset.forName("utf-8")), type);
                }

                Map<String, List<NameValue<Long>>> countValueMap = CounterUtils.countTransportSchema(container.getContainer(), zkTransportSchemaList);
                List<TransportSchema> list = CounterUtils.countMapToSchema(countValueMap, schemaName);
                //数据保存至 zk
                if (!list.isEmpty()) {
                    template.putData(schemaName, nodeName, GzipUtil.gzip(new Gson().toJson(list).getBytes(Charset.forName("utf-8"))));
                }
                countValueMap.clear();
                schemaDataContainerEvent.clearCurrentContainer();
                //释放锁
                template.releaseSelfLock();
            }
        }, 0, 1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataCounter)) return false;
        DataCounter that = (DataCounter) o;
        return Objects.equals(schemaName, that.schemaName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName);
    }


}
