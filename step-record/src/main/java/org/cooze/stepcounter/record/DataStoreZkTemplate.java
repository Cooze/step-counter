package org.cooze.stepcounter.record;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.cooze.stepcounter.record.zk.ZkClientPool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-21
 **/
public class DataStoreZkTemplate {

    private static final String PATH_SEPARATE = "/";
    private static final String CLUSTER_LOCK = "_cluster_lock_";
    private static final String CLUSTER_LOCK_ALL = "_cluster_lock_all_";
    private static final String NODE_LOCK = "_node_lock_";
    private static final String DATA_NODE = "datas";

    public static class LockNode {
        private CuratorFramework client;
        private InterProcessMutex interProcessMutex;

        public LockNode(CuratorFramework client, InterProcessMutex interProcessMutex) {
            this.client = client;
            this.interProcessMutex = interProcessMutex;
        }

        public CuratorFramework getClient() {
            return client;
        }

        public void setClient(CuratorFramework client) {
            this.client = client;
        }

        public InterProcessMutex getInterProcessMutex() {
            return interProcessMutex;
        }

        public void setInterProcessMutex(InterProcessMutex interProcessMutex) {
            this.interProcessMutex = interProcessMutex;
        }
    }

    private ThreadLocal<LockNode> lockTread = new ThreadLocal<>();

    private ThreadLocal<String> ClusterLockThreadLocal = new ThreadLocal<>();

    private ZkClientPool zkClientPool;

    public DataStoreZkTemplate(ZkClientPool zkClientPool) {
        this.zkClientPool = zkClientPool;
    }

    public DataStoreZkTemplate() {
    }

    public ZkClientPool getZkClientPool() {
        return zkClientPool;
    }

    public void setZkClientPool(ZkClientPool zkClientPool) {
        this.zkClientPool = zkClientPool;
    }


    public boolean trySelfLock(String schema, String nodeName) {
        CuratorFramework client = null;
        try {
            client = this.zkClientPool.getClient();
            lockTread.set(new LockNode(client, new InterProcessMutex(client,
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(NODE_LOCK)
                            .append(PATH_SEPARATE)
                            .append(nodeName)
                            .toString())));
            return lockTread.get().getInterProcessMutex().acquire(10, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void releaseSelfLock() {
        try {
            if (lockTread.get() == null) {
                return;
            }
            if (lockTread.get().getInterProcessMutex().isAcquiredInThisProcess()) {
                lockTread.get().getInterProcessMutex().release();
                zkClientPool.returnClient(lockTread.get().client);
                lockTread.remove();
            }else {
                zkClientPool.returnClient(lockTread.get().client);
                lockTread.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String path) throws Exception {
        CuratorFramework client = null;
        try {
            client = this.zkClientPool.getClient();
            Stat stat = client.checkExists().creatingParentContainersIfNeeded().forPath(path);
            return stat != null;
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }


    public void setClusterLock(String schema) {
        CuratorFramework client = null;
        try {
            client = this.zkClientPool.getClient();
            ClusterLockThreadLocal.set(schema);
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(CLUSTER_LOCK)
                            .append(PATH_SEPARATE)
                            .append(CLUSTER_LOCK_ALL)
                            .toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }

    public boolean clusterHasLock(String schema) {
        try {
            return isExist(new StringBuilder(PATH_SEPARATE)
                    .append(schema)
                    .append(PATH_SEPARATE)
                    .append(CLUSTER_LOCK)
                    .append(PATH_SEPARATE)
                    .append(CLUSTER_LOCK_ALL)
                    .toString());
        } catch (Exception e) {
            return false;
        }
    }

    public void releaseClusterLock(String schema) {
        CuratorFramework client = null;
        try {
            client = this.zkClientPool.getClient();
            if (clusterHasLock(schema)) {
                client.delete().forPath(new StringBuilder(PATH_SEPARATE)
                        .append(schema)
                        .append(PATH_SEPARATE)
                        .append(CLUSTER_LOCK)
                        .append(PATH_SEPARATE)
                        .append(CLUSTER_LOCK_ALL)
                        .toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ClusterLockThreadLocal.remove();
            this.zkClientPool.returnClient(client);
        }
    }

    public void releaseClusterLock() {
        try {
            String schema = ClusterLockThreadLocal.get();
            releaseClusterLock(schema);
            ClusterLockThreadLocal.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putData(String schema, String node, byte[] data) {
        CuratorFramework client = null;
        try {
            client = this.zkClientPool.getClient();
            String path = new StringBuilder(
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(DATA_NODE)
                            .append(PATH_SEPARATE)
                            .append(node)
            ).toString();
            if (isExist(path)) {
                client.setData().forPath(path, data);
                return;
            }
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }

    public void removeDataNode(String schema, String node) {
        CuratorFramework client = null;
        try {
            String path = new StringBuilder(
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(DATA_NODE)
                            .append(PATH_SEPARATE)
                            .append(node)
            ).toString();
            if (!isExist(path)) {
                return;
            }
            client = this.zkClientPool.getClient();
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }

    public void removeClusterData(String schema) {
        CuratorFramework client = null;
        try {
            String path = new StringBuilder(
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(DATA_NODE)
            ).toString();
            if (!isExist(path)) {
                return;
            }
            client = this.zkClientPool.getClient();
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }

    public byte[] getData(String schema, String node) {
        CuratorFramework client = null;
        try {
            String path = new StringBuilder(
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(DATA_NODE)
                            .append(PATH_SEPARATE)
                            .append(node)
            ).toString();
            if (!isExist(path)) {
                return null;
            }
            client = this.zkClientPool.getClient();
            return client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }

    public List<byte[]> listDatas(String schema) {
        CuratorFramework client = null;
        List<byte[]> list = new LinkedList<>();
        try {

            String path = new StringBuilder(
                    new StringBuilder(PATH_SEPARATE)
                            .append(schema)
                            .append(PATH_SEPARATE)
                            .append(DATA_NODE)
            ).toString();
            if (!isExist(path)) {
                return list;
            }
            client = this.zkClientPool.getClient();
            List<String> paths = client.getChildren().forPath(path);

            for (String p : paths) {
                list.add(client.getData().forPath(new StringBuilder(
                        new StringBuilder(path)
                                .append(PATH_SEPARATE)
                                .append(p)
                ).toString()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            this.zkClientPool.returnClient(client);
        }
    }


}
