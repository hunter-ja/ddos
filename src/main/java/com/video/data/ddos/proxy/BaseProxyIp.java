package com.video.data.ddos.proxy;

import com.video.data.model.HttpModel;
import com.video.data.model.IpInfoModel;
import com.video.data.model.ProxyIp;
import com.video.data.utils.DdosUtil;
import com.video.data.utils.HttpRequest;
import com.video.data.utils.SpringUtil;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseProxyIp implements Runnable {

    private List<IpInfoModel> ipList = SpringUtil.getBean(ProxyIp.class).getProxyIps();

    @Override
    public void run() {
        System.out.println(getProxyName() + "IP抓取程序已启动");
        Thread.currentThread().setName(getProxyName());
        ProxyIp proxyIp = SpringUtil.getBean(ProxyIp.class);
        if(!proxyIp.getProxyList().contains(getProxyName())) {
            proxyIp.getProxyList().add(getProxyName());
        }
        int page = 1;
        while (true) {
            if(page >= getMaxPage()) {
                page = 1;
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> activePorxy = proxyIp.getActivePorxy();
            try {
                activePorxy.put(getProxyName(), sf.format(new Date()));
                String url = getUrl(page);
                HttpModel httpModel = http(url);
                if(StringUtils.isEmpty(httpModel.getContent())) {
                    continue;
                }
                List<IpInfoModel> list = parse(httpModel.getContent());
                if(list != null) {
                    setIp(list);
                }
            }catch (Exception e) {
                activePorxy.put(getProxyName(), sf.format(new Date()) + ":出现异常" + e.getMessage());
            }
            page++;
        }
    }

    private void setIp(List<IpInfoModel> ips) {
        ipList.addAll(ips);
    }

    public List<IpInfoModel> parse(String content) {
        return null;
    }

    public HttpModel http(String url) {
        IpInfoModel ipInfo = DdosUtil.selectOneIp();
        HttpModel httpModel;
        if(ipInfo == null || "".equals(ipInfo.getIpInfo())) {
            httpModel = HttpRequest.sendGetRequest(url);
        }else{
            httpModel = HttpRequest.sendGetRequest(url, null, ipInfo.getIpInfo());
        }
        if(httpModel.getCode() == 0) {
            if(ipInfo != null) {
                DdosUtil.ipError(ipInfo);
            }
        }else if(httpModel.getCode() > 0) {
            if(ipInfo != null) {
                DdosUtil.ipSuccess(ipInfo);
            }
        }
        return httpModel;
    }

    public String getUrl(int page) {
        return "";
    }

    public int getMaxPage() {
        return 0;
    }

    public String getProxyName() {
        return "";
    }
}
