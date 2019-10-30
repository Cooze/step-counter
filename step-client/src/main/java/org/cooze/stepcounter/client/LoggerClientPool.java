package org.cooze.stepcounter.client;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.cooze.stepcounter.core.net.client.LoggerTcpClient;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-14
 **/
public class LoggerClientPool {

    private GenericObjectPool<LoggerTcpClient> loggerTcpClientPool;

    private static boolean DEFAULT_TEST_ON_BORROW_VALUE = Boolean.TRUE;
    private static boolean DEFAULT_TEST_ON_CREATE_VALUE = Boolean.TRUE;

    public LoggerClientPool(LoggerClientConfig clientConfig) {
        GenericObjectPoolConfig<LoggerTcpClient> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setTestOnBorrow(DEFAULT_TEST_ON_BORROW_VALUE);
        poolConfig.setTestOnCreate(DEFAULT_TEST_ON_CREATE_VALUE);
        poolConfig.setMaxTotal(clientConfig.getMaxTotal());
        poolConfig.setMinIdle(clientConfig.getMinIdle());
        //池化的对象的工厂类
        LoggerClientFactory loggerClientFactory = new LoggerClientFactory(clientConfig.getServers(), clientConfig.getConnTimeout());
        loggerTcpClientPool = new GenericObjectPool<>(loggerClientFactory, poolConfig);
    }

    public LoggerTcpClient getClient() throws Exception {
        return this.loggerTcpClientPool.borrowObject();
    }

    public void returnClient(LoggerTcpClient client) {
        this.loggerTcpClientPool.returnObject(client);
    }

    public void close() {
        this.loggerTcpClientPool.close();
    }

}
