package org.cooze.stepcounter.receiver;


import org.cooze.stepcounter.core.net.server.TcpServer;
import org.cooze.stepcounter.record.utils.SpringContextUtils;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-09
 **/
public class Bootstrap {
    public static void main(String[] args) throws Exception {
        SpringContextUtils.getBean(TcpServer.class).start();
    }
}
