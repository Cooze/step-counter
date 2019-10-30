package org.cooze.stepcounter.record.utils;


import org.cooze.stepcounter.core.SchemaContainer;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportBody;
import org.cooze.stepcounter.core.protocol.TransportSchema;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-02
 **/
public class CounterUtils {

    public static Map<String, List<NameValue<Long>>> countTransportSchema(List<TransportSchema> transportSchemas, List<TransportSchema> zkTransportSchemaList) {
        Map<String, List<NameValue<Long>>> countValueMap = new HashMap<>();
        count(countValueMap, transportSchemas);
        count(countValueMap, zkTransportSchemaList);
        return countValueMap;
    }

    public static void count(Map<String, List<NameValue<Long>>> countValueMap, List<TransportSchema> transportSchemas) {
        if (transportSchemas == null || transportSchemas.isEmpty()) {
            return;
        }
        for (TransportSchema transportSchema : transportSchemas) {
            StringBuilder kfSb = new StringBuilder();
            for (NameValue<String> keyField : transportSchema.getBody().getKeyFields()) {
                if (keyField.getName() == null && keyField.getValue() != null) {
                    continue;
                }
                kfSb.append(keyField.getName()).append(",").append(keyField.getValue()).append("|");
            }
            if (kfSb.toString().equals("")) {
                continue;
            }
            String kf = kfSb.substring(0, kfSb.lastIndexOf("|"));
            if (countValueMap.get(kf) == null) {
                countValueMap.put(kf, transportSchema.getBody().getCountFields());
                continue;
            }
            List<NameValue<Long>> countValueBefore = countValueMap.get(kf);
            for (int i = 0, size = transportSchema.getBody().getCountFields().size(); i < size; i++) {
                int k = countValueBefore.indexOf(transportSchema.getBody().getCountFields().get(i));
                Long v = countValueBefore.get(k).getValue();
                countValueMap.get(kf).get(i).setValue((v == null ? 0 : v) + transportSchema.getBody().getCountFields().get(i).getValue());
            }
        }
    }

    public static List<TransportSchema> countMapToSchema(Map<String, List<NameValue<Long>>> countValueMap, String schemaName) {
        List<TransportSchema> list = new LinkedList<>();
        countValueMap.forEach((key, value) -> {
            if (key == null || "".equals(key)) {
                return;
            }

            TransportSchema transportSchema = new TransportSchema();
            transportSchema.setHead(SchemaContainer.instance().getSchema(schemaName).getHead());
            transportSchema.setBody(TransportBody.BodyBuilder.newInstance().build());
            for (String f : key.split("\\|")) {
                NameValue<String> fnv = new NameValue<>();
                String[] kv = f.split(",");
                fnv.setName(kv[0]);
                fnv.setValue(kv[1]);
                transportSchema.getBody().getKeyFields().add(fnv);

            }
            transportSchema.getBody().getCountFields().addAll(value);
            list.add(transportSchema);
        });
        return list;
    }
}
