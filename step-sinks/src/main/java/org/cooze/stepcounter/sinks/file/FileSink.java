package org.cooze.stepcounter.sinks.file;


import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.extend.CounterSink;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-03
 **/
public class FileSink implements CounterSink {

    private Log2jTemplate template;

    private static final String token = "\t|\t";


    public FileSink() throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("FileSinkConfig.properties"));
        Log2jContext log2jContext = new Log2jContext();
        log2jContext.setFileName(properties.getProperty("file.sink.filename"));
        log2jContext.setFilePath(properties.getProperty("file.sink.filepath"));
        log2jContext.setLoggerSize(properties.getProperty("file.sink.fileSize"));
        template = new Log2jTemplate(log2jContext);
    }

    @Override
    public void store(List<TransportSchema> resultDataList) {
        StringBuilder stringBuilder = new StringBuilder();

        int maxLen = 0;

        for (int i = 0, size = resultDataList.size(); i < size; i++) {
            TransportSchema schema = resultDataList.get(i);
            List<NameValue<String>> keys = schema.getBody().getKeyFields();
            List<NameValue<Long>> counts = schema.getBody().getCountFields();
            for (int ind = 0, len = keys.size(); ind < len; ind++) {

                int kl = stringLength(keys.get(ind).getName());
                int vl = stringLength(keys.get(ind).getValue());
                if (kl > maxLen) {
                    maxLen = kl;
                }
                if (vl > maxLen) {
                    maxLen = vl;
                }
            }
            for (int ind = 0, len = counts.size(); ind < len; ind++) {
                int kl = stringLength(counts.get(ind).getName());
                int vl = stringLength(String.valueOf(counts.get(ind).getValue()));
                if (kl > maxLen) {
                    maxLen = kl;
                }
                if (vl > maxLen) {
                    maxLen = vl;
                }
            }
        }
        TransportSchema transportSchema = resultDataList.get(0);
        stringBuilder.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------\n")
                .append(transportSchema.getHead().getSchemaName()).append("\t|\ttime frame = ").append(transportSchema.getHead().getTimeFrame()).append("\t|\ttime=").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        List<NameValue<String>> keyFields = transportSchema.getBody().getKeyFields();
        List<NameValue<Long>> countFields = transportSchema.getBody().getCountFields();
        for (int i = 0, size = keyFields.size(); i < size; i++) {
            stringBuilder.append(assembly(keyFields.get(i).getName(), maxLen)).append(token);
        }
        for (int i = 0, size = countFields.size(); i < size; i++) {
            stringBuilder.append(assembly(countFields.get(i).getName(), maxLen)).append(token);
        }
        stringBuilder.append("\n");
        for (int i = 0, size = resultDataList.size(); i < size; i++) {
            TransportSchema schema = resultDataList.get(i);
            List<NameValue<String>> keys = schema.getBody().getKeyFields();
            List<NameValue<Long>> counts = schema.getBody().getCountFields();
            for (int ind = 0, len = keys.size(); ind < len; ind++) {

                stringBuilder.append(assembly(keys.get(ind).getValue(), maxLen)).append(token);
            }
            for (int ind = 0, len = counts.size(); ind < len; ind++) {
                stringBuilder.append(assembly(String.valueOf(counts.get(ind).getValue()), maxLen)).append(token);
            }
            stringBuilder.append("\n");
        }
        template.writeLog(stringBuilder.toString());
    }


    private static String assembly(String value, int maxlen) {
        int vl = stringLength(value);
        int len = maxlen - vl;
        if (len <= 0) {
            return value;
        }
        StringBuilder sb = new StringBuilder(value);
        for (int i = 0; i < len; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static int stringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

}
