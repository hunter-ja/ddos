package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;

import java.util.ArrayList;
import java.util.List;

public class PayProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        String[] ipInfo = content.split(",")[3].replaceAll("\"", "").split(":");
        if(ipInfo.length < 4) {
            return null;
        }
        String ip = ipInfo[2];
        String port = ipInfo[3];
        List<IpInfoModel> ipList = new ArrayList<>();
        IpInfoModel ipInfoModel = new IpInfoModel();
        ipInfoModel.setScheme("HTTP");
        ipInfoModel.setIp(ip);
        ipInfoModel.setProt(port);
        ipInfoModel.setSource("付费代理");
        ipInfoModel.setDisconnectTimes(0);
        ipList.add(ipInfoModel);
        return ipList;
    }

    public String getUrl(int page) {
        return "http://ip.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=0&" +
                "fa=0&fetch_key=&groupid=0&qty=1&time=1&pro=&city=&port=1&format=json&ss=5&css=&ipport=1&et" +
                "=1&pi=1&co=1&dt=1&specialTxt=3&specialJson=&usertype=15";
    }

    public int getMaxPage() {
        return 1800;
    }

    public String getProxyName() {
        return "付费代理";
    }
}
