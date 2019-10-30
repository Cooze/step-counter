package org.cooze.stepcounter.sinks.file;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;

import java.io.File;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-03
 **/
public class Log2jContext {
    public static final String APPEND_NAME = "LOGGER_TO_FILE";
    private String loggerSize;

    private String filePath;
    private String fileName;

    public Log2jContext() {
    }

    public String getLoggerSize() {
        return loggerSize;
    }

    public void setLoggerSize(String loggerSize) {
        this.loggerSize = loggerSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LoggerContext loggerInit() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.INFO);
        builder.setConfigurationName("RollingBuilder");
        // RollingFile file appender 只有RollingFile 才有压缩功能
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%msg");

        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", loggerSize));

        AppenderComponentBuilder appenderBuilder = builder.newAppender(APPEND_NAME, "RollingFile")
                .addAttribute("fileName", filePath + File.separator + fileName)
                .addAttribute("filePattern", filePath + File.separator + "history/" + fileName + "-history-%d{yyyy-MM-dd HH:mm:ss}.log.gz")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);
        // create the new logger
        builder.add(
                builder.newLogger(APPEND_NAME, Level.INFO)
                        .add(builder.newAppenderRef(APPEND_NAME))
                        .addAttribute("additivity", false));
        builder.add(builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef(APPEND_NAME).addAttribute("Level", Level.INFO))
        );
        Log4jContextFactory factory = new Log4jContextFactory();
        LogManager.setFactory(factory);
        return Configurator.initialize(builder.build());
    }
}
