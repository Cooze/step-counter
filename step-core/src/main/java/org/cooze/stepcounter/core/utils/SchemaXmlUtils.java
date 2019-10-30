package org.cooze.stepcounter.core.utils;


import org.cooze.stepcounter.core.SchemaContainer;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportBody;
import org.cooze.stepcounter.core.protocol.TransportHead;
import org.cooze.stepcounter.core.protocol.TransportSchema;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-09
 **/
public final class SchemaXmlUtils {

    public static void readSchemaXmlFromClasspath(String filepath) {
        try {
            // init schema from xml
            List<String> resourcesFile = null;
            boolean isJar = true;
            try {
                File rootFile = new File(Thread.currentThread().getClass().getResource(filepath).getFile());
                resourcesFile = MatchFileUtils.listJarResources(rootFile.getAbsolutePath().split("!")[0].split(":")[1], filepath.startsWith("/") ? filepath.substring(1, filepath.length()) : filepath);
            } catch (Exception e) {
            }
            if (resourcesFile == null) {
                isJar = false;
                resourcesFile = MatchFileUtils.matchFile(Thread.currentThread().getClass().getResource(filepath).getPath());
            }
            if (resourcesFile == null) {
                throw new RuntimeException("schema 不存在！");
            }

            if (isJar) {
                for (String resource : resourcesFile) {
                    String schemaName = XmlUtils.readSchemaName(Thread.currentThread().getClass().getResourceAsStream(resource));
                    String schemaTimeFrame = XmlUtils.readSchemaTimeFrame(Thread.currentThread().getClass().getResourceAsStream(resource));
                    Set<String> countFields = XmlUtils.readCountFields(Thread.currentThread().getClass().getResourceAsStream(resource));
                    Set<String> keyFields = XmlUtils.readKeyFields(Thread.currentThread().getClass().getResourceAsStream(resource));
                    SchemaContainer.instance().putSchema(buildTransportSchema(schemaName, schemaTimeFrame, keyFields, countFields));
                }
            } else {
                for (String resource : resourcesFile) {
                    String schemaName = XmlUtils.readSchemaName(new FileInputStream(resource));
                    String schemaTimeFrame = XmlUtils.readSchemaTimeFrame(new FileInputStream(resource));
                    Set<String> countFields = XmlUtils.readCountFields(new FileInputStream(resource));
                    Set<String> keyFields = XmlUtils.readKeyFields(new FileInputStream(resource));
                    SchemaContainer.instance().putSchema(buildTransportSchema(schemaName, schemaTimeFrame, keyFields, countFields));
                }
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
