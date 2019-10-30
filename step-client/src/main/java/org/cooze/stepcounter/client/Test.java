package org.cooze.stepcounter.client;


import org.cooze.stepcounter.core.net.client.LoggerTcpClient;
import org.cooze.stepcounter.core.protocol.Message;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-16
 **/
public class Test {

    public static void main(String[] args) throws InterruptedException {

        LoggerClientConfig clientConfig = new LoggerClientConfig(Test.class.getResourceAsStream("/LoggerCount.properties"));
        LoggerClientPool clientPool = new LoggerClientPool(clientConfig);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                BuryingPoint.init("test1");
                if (i % 2 == 0) {
                    setKf("countName", "cn1");
                } else {
                    setKf("countName", "cn2");
                }
                setCp("c1");
                if (i % 2 == 0) {
                    setCp("c2");
                }
                LoggerTcpClient clientPoolClient = null;
                try {
                    clientPoolClient = clientPool.getClient();
                    clientPoolClient.send(new Message(BuryingPoint.build()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (clientPoolClient != null) {
                        clientPool.returnClient(clientPoolClient);
                    }
                    BuryingPoint.clean();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                BuryingPoint.init("test2");
                if (i % 2 == 0) {
                    setKf("1", "cn1");
                    setKf("2", "cn1");
                } else {
                    setKf("1", "cn2");
                    setKf("2", "cn2");
                }

                setCp("c1");
                setCp("c2");
                if (i % 2 == 0) {
                    setCp("c3");
                }
                LoggerTcpClient clientPoolClient = null;
                try {
                    clientPoolClient = clientPool.getClient();
                    clientPoolClient.send(new Message(BuryingPoint.build()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (clientPoolClient != null) {
                        clientPool.returnClient(clientPoolClient);
                    }
                    BuryingPoint.clean();
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                BuryingPoint.init("test3");
                if (i % 2 == 0) {
                    setKf("name", "张三");
                    setKf("age", "20");
                    setKf("sex", "男");
                } else {
                    setKf("name", "莉莉");
                    setKf("age", "18");
                    setKf("sex", "女");
                }
                setCp("number1");
                setCp("number2");
                LoggerTcpClient clientPoolClient = null;
                try {
                    clientPoolClient = clientPool.getClient();
                    clientPoolClient.send(new Message(BuryingPoint.build()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (clientPoolClient != null) {
                        clientPool.returnClient(clientPoolClient);
                    }
                    BuryingPoint.clean();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("close");
        clientPool.close();

    }

    public static void setCp(String cp) {
        BuryingPoint.setCountPoint(cp);
    }

    public static void setKf(String kf, String v) {
        BuryingPoint.setKeyField(kf, v);
    }


}
