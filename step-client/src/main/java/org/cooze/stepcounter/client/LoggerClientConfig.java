package org.cooze.stepcounter.client;


import org.cooze.stepcounter.core.utils.SchemaXmlUtils;
import org.cooze.stepcounter.core.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-09
 **/
public final class LoggerClientConfig {

    public static final String LOGGER_CLIENT_SERVERS = "LoggerClient.servers";
    public static final String LOGGER_CLIENT_XMLSCHEMA_CLASSPATH = "LoggerClient.schemaXml.classpath";
    public static final String LOGGER_CLIENT_MAX_TOTAL = "LoggerClient.maxTotal";
    public static final String LOGGER_CLIENT_MAX_TOTAL_DEFAULT_VALUE = "10";
    public static final String LOGGER_CLIENT_MIN_IDLE = "LoggerClient.minIdle";
    public static final String LOGGER_CLIENT_MIN_IDLE_DEFAULT_VALUE = "5";
    public static final String LOGGER_CLIENT_CONN_TIMEOUT = "LoggerClient.connTimeout";
    public static final String LOGGER_CLIENT_CONN_TIMEOUT_DEFAULT_VALUE = "1000";

    public LoggerClientConfig(Map<String, String> config) {
        init(config);
    }

    public LoggerClientConfig(InputStream in) {
        init(in);
    }

    private static final class Holder {
        private static final Map<String, String> config = new HashMap<>();
    }

    public LoggerClientConfig init(Map<String, String> config) {
        if (config == null) {
            throw new RuntimeException("配置为空！");
        }
        Holder.config.putAll(config);
        SchemaXmlUtils.readSchemaXmlFromClasspath(Holder.config.get(LOGGER_CLIENT_XMLSCHEMA_CLASSPATH));
        return this;
    }

    public LoggerClientConfig init(InputStream in) {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
        String loggerCountServers = properties.getProperty(LOGGER_CLIENT_SERVERS);
        if (StringUtils.isEmpty(loggerCountServers)) {
            throw new RuntimeException("配置" + LOGGER_CLIENT_SERVERS + "为空！");
        }
        String loggerCountSchemaXmlClasspath = properties.getProperty(LOGGER_CLIENT_XMLSCHEMA_CLASSPATH);
        if (StringUtils.isEmpty(loggerCountSchemaXmlClasspath)) {
            throw new RuntimeException("配置" + LOGGER_CLIENT_XMLSCHEMA_CLASSPATH + "为空！");
        }

        String maxTotal = properties.getProperty(LOGGER_CLIENT_MAX_TOTAL, LOGGER_CLIENT_MAX_TOTAL_DEFAULT_VALUE);
        String minIdle = properties.getProperty(LOGGER_CLIENT_MIN_IDLE, LOGGER_CLIENT_MIN_IDLE_DEFAULT_VALUE);
        String connTimeout = properties.getProperty(LOGGER_CLIENT_CONN_TIMEOUT, LOGGER_CLIENT_CONN_TIMEOUT_DEFAULT_VALUE);
        Holder.config.put(LOGGER_CLIENT_SERVERS, loggerCountServers);
        Holder.config.put(LOGGER_CLIENT_XMLSCHEMA_CLASSPATH, loggerCountSchemaXmlClasspath);

        Holder.config.put(LOGGER_CLIENT_MAX_TOTAL, maxTotal);
        Holder.config.put(LOGGER_CLIENT_MIN_IDLE, minIdle);
        Holder.config.put(LOGGER_CLIENT_CONN_TIMEOUT, connTimeout);

        SchemaXmlUtils.readSchemaXmlFromClasspath(loggerCountSchemaXmlClasspath);
        return this;
    }

    public String getServers() {
        return Holder.config.get(LOGGER_CLIENT_SERVERS);
    }

    public int getMaxTotal() {
        String maxTotal = Holder.config.get(LOGGER_CLIENT_MAX_TOTAL);
        if (StringUtils.isEmpty(maxTotal)) {
            return 10;
        }
        return Integer.parseInt(Holder.config.get(LOGGER_CLIENT_MAX_TOTAL));
    }

    public int getMinIdle() {
        String minIdle = Holder.config.get(LOGGER_CLIENT_MIN_IDLE);
        if (StringUtils.isEmpty(minIdle)) {
            return 5;
        }
        return Integer.parseInt(Holder.config.get(LOGGER_CLIENT_MIN_IDLE));
    }

    public Long getConnTimeout() {
        String connTimeout = Holder.config.get(LOGGER_CLIENT_CONN_TIMEOUT);
        if (StringUtils.isEmpty(connTimeout)) {
            return null;
        }
        return Long.parseLong(connTimeout);
    }

    public String getXmlSchemaPath() {
        return Holder.config.get(LOGGER_CLIENT_XMLSCHEMA_CLASSPATH);
    }


}
