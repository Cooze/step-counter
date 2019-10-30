package org.cooze.stepcounter.receiver.utils;


import org.cooze.stepcounter.core.SchemaContainer;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportBody;
import org.cooze.stepcounter.core.protocol.TransportHead;
import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.core.utils.MatchFileUtils;
import org.cooze.stepcounter.core.utils.XmlUtils;
import org.cooze.stepcounter.record.*;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-09
 **/
public final class SchemaXmlUtils {
    private static Pattern pattern = Pattern.compile("^(\\d+)([s|S|m|M|h|H])$");

    public static void readSchemaXmlFromClasspath(String nodeName, Map<String, SchemaDataContainerEvent> schemaContainerEvent) {
        try {
            // init schema from xml
            List<String> resourcesFile = MatchFileUtils.matchFile(Thread.currentThread().getClass().getResource("/schema").getPath());

            if (resourcesFile == null) {
                throw new RuntimeException("schema 不存在！");
            }
            for (String resource : resourcesFile) {
                String schemaName = XmlUtils.readSchemaName(new FileInputStream(resource));
                String schemaTimeFrame = XmlUtils.readSchemaTimeFrame(new FileInputStream(resource));
                Matcher matcher = pattern.matcher(schemaTimeFrame);
                if (!matcher.find()) {
                    throw new Exception(schemaName + "，TimeFrame不能为空.");
                }
                Set<String> countFields = XmlUtils.readCountFields(new FileInputStream(resource));
                Set<String> keyFields = XmlUtils.readKeyFields(new FileInputStream(resource));
                SchemaContainer.instance().putSchema(buildTransportSchema(schemaName, schemaTimeFrame, keyFields, countFields));
                schemaContainerEvent.put(schemaName, new SchemaDataContainerEvent(new SchemaDataContainer(ContainerStatus.count), new SchemaDataContainer(ContainerStatus.save)));
                new DataCounter(nodeName, schemaName, schemaContainerEvent.get(schemaName));
                ClusterCounter clusterCounter = new ClusterCounter(schemaName);
                long time = Long.parseLong(matcher.group(1));
                String timeUnit = matcher.group(2);
                clusterCounter.setTime(time);
                clusterCounter.setTimeUnit(timeUnit);
                clusterCounter.init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TransportSchema buildTransportSchema(String schemaName, String schemaTimeFrame, Set<String> keyFields, Set<String> countFields) {
        TransportSchema transportSchema = new TransportSchema();
        TransportHead head = new TransportHead(schemaName);
        head.setTimeFrame(schemaTimeFrame);
        transportSchema.setHead(head);
        TransportBody.BodyBuilder bodyBuilder = TransportBody.BodyBuilder.newInstance();
        keyFields.forEach(k -> {
            NameValue<String> keyField = new NameValue<>();
            keyField.setName(k);
            bodyBuilder.appendKeyField(keyField);
        });
        countFields.forEach(c -> {
            NameValue<Long> count = new NameValue<>();
            count.setName(c);
            count.setValue(0L);
            bodyBuilder.appendCountField(count);
        });
        transportSchema.setBody(bodyBuilder.build());

        return transportSchema;
    }
}
