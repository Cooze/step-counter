package org.cooze.stepcounter.record.zk;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-21
 **/
public class ZkClientFactory implements PooledObjectFactory<CuratorFramework> {

    private ZkClientConfig clientConfig;

    public ZkClientFactory(ZkClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public PooledObject<CuratorFramework> makeObject() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1500, this.clientConfig.getMaxRetries() == 0 ? 3 : this.clientConfig.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .namespace(this.clientConfig.getNamespace())
                .connectString(this.clientConfig.getHosts())
                .sessionTimeoutMs(this.clientConfig.getSessionTimeoutMs())
                .connectionTimeoutMs(this.clientConfig.getConnectionTimeoutMs())
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<CuratorFramework> pooledObject) throws Exception {
        CuratorFramework client = pooledObject.getObject();
        if (client != null) {
            client.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<CuratorFramework> pooledObject) {
        switch (pooledObject.getObject().getState()) {
            case LATENT:
                return false;
            case STARTED:
                return true;
            case STOPPED:
                return false;
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<CuratorFramework> pooledObject) throws Exception {
        pooledObject.getObject().getState();
    }

    @Override
    public void passivateObject(PooledObject<CuratorFramework> pooledObject) throws Exception {

    }
}
