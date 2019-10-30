package org.cooze.stepcounter.client;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.cooze.stepcounter.core.net.client.LoggerTcpClient;

import java.util.Random;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-14
 **/
public class LoggerClientFactory implements PooledObjectFactory<LoggerTcpClient> {

    private Random random = new Random();
    private Long connTimeout;
    private String servers;

    private static final String SERVERS_SPLIT = ",";
    private static final String HOST_PORT_SPLIT = ":";
    private static final int HOST_INDEX = 0;
    private static final int PORT_INDEX = 1;
    private static final int DEFAULT_PING_TIMEOUT = 1000;

    public LoggerClientFactory(String servers, Long connTimeout) {
        this.servers = servers;
        this.connTimeout = connTimeout;
    }

    @Override
    public PooledObject<LoggerTcpClient> makeObject() throws Exception {
        String[] serverArr = servers.split(SERVERS_SPLIT);
        int index = random.nextInt(serverArr.length);
        String[] hostPort = serverArr[index].split(HOST_PORT_SPLIT);
        LoggerTcpClient client = new LoggerTcpClient(hostPort[HOST_INDEX], Integer.parseInt(hostPort[PORT_INDEX]), this.connTimeout);
        client.connect();
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<LoggerTcpClient> pooledObject) throws Exception {
        LoggerTcpClient client = pooledObject.getObject();
        if (client != null) {
            client.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<LoggerTcpClient> pooledObject) {
        try {
            return pooledObject.getObject().ping(DEFAULT_PING_TIMEOUT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<LoggerTcpClient> pooledObject) throws Exception {
        pooledObject.getObject().ping(DEFAULT_PING_TIMEOUT);
    }

    @Override
    public void passivateObject(PooledObject<LoggerTcpClient> pooledObject) throws Exception {

    }

}
