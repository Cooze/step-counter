package org.cooze.stepcounter.record.zk;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.curator.framework.CuratorFramework;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-21
 **/
public class ZkClientPool {
    private GenericObjectPool<CuratorFramework> clientPool;

    public ZkClientPool(ZkClientConfig clientConfig) {
        GenericObjectPoolConfig<CuratorFramework> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setMaxTotal(clientConfig.getMaxTotal());
        poolConfig.setMinIdle(clientConfig.getMinIdle());
        clientPool = new GenericObjectPool<>(new ZkClientFactory(clientConfig), poolConfig);
    }

    /**
     * 链接返回给连接池
     *
     * @param client 客户端
     */
    public void returnClient(CuratorFramework client) {
        if (client == null) {
            return;
        }
        clientPool.returnObject(client);
    }

    /**
     * 返回链接
     *
     * @return 客户端链接
     */
    public CuratorFramework getClient() {
        try {
            return clientPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接池
     */
    public void close() {
        clientPool.close();
    }
}
