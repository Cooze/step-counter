package org.cooze.stepcounter.record.zk;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-21
 **/
public class ZkClientConfig {

    /**
     * zk 地址列表如果有多个用","分隔
     */
    private String hosts;

    /**
     * 明名空间
     */
    private String namespace;

    private int maxTotal;
    private int minIdle;

    /**
     * 会话超时时间
     */
    private int sessionTimeoutMs;

    /**
     * 客户端链接超时时间
     */
    private int connectionTimeoutMs;
    /**
     * 客户端链接重试次数
     */
    private int maxRetries;

    public ZkClientConfig(String hosts, String namespace, int sessionTimeoutMs,
                          int connectionTimeoutMs, int maxRetries, int maxTotal,
                          int minIdle) {
        this.hosts = hosts;
        this.namespace = namespace;
        this.sessionTimeoutMs = sessionTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.maxRetries = maxRetries;
        this.maxTotal = maxTotal;
        this.minIdle = minIdle;
    }

    public ZkClientConfig() {
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
