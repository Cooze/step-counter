package org.cooze.stepcounter.sinks.file;

import org.apache.logging.log4j.Logger;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-09-03
 **/
public class Log2jTemplate {

    private Logger logger;

    public Log2jTemplate(Log2jContext log2jContext) {
        this.logger = log2jContext.loggerInit().getLogger(Log2jContext.APPEND_NAME);
    }

    public void writeLog(String msg) {
        this.logger.info(msg);
    }

}
