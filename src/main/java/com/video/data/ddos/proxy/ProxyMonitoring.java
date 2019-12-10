package com.video.data.ddos.proxy;

/**
 * 抓取代理IP
 */
public class ProxyMonitoring implements Runnable {

    @Override
    public void run() {
        System.out.println("代理IP抓取程序已启动");
        ThreadGroup threadGroup = new ThreadGroup("proxy");
        for (int i = 0; i < 5; i++) {
            new Thread(threadGroup, new XilaProxyIp()).start();
            new Thread(threadGroup, new XilaHttpProxyIp()).start();
            new Thread(threadGroup, new QiyunProxyIp()).start();
            new Thread(threadGroup, new KuaiProxyIp()).start();
            new Thread(threadGroup, new LiuliuProxyIp()).start();
            new Thread(threadGroup, new FreeProxyIp()).start();
        }
    }






}
