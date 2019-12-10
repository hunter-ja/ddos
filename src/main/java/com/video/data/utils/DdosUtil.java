package com.video.data.utils;

import com.video.data.model.IpInfoModel;
import com.video.data.model.ProxyIp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DdosUtil {


    public static IpInfoModel selectOneIp() {
        ProxyIp proxyIpModel = SpringUtil.getBean(ProxyIp.class);
        List<IpInfoModel> ipList = proxyIpModel.getProxyIps();
        if(ipList.size() < 1) {
            return null;
        }
        int index = Integer.parseInt(String.valueOf(Math.round(Math.random() * (ipList.size() - 1))));
        IpInfoModel ip = ipList.get(index);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ip.setLastUseTime(sf.format(new Date()));
        return ip;
    }

    public static void remove(IpInfoModel ipInfoModel) {
        ProxyIp proxyIpModel = SpringUtil.getBean(ProxyIp.class);
        List<IpInfoModel> ipList = proxyIpModel.getProxyIps();
        ipList.remove(ipInfoModel);
    }

    public static void ipError(IpInfoModel ipInfo) {
        if(ipInfo.getDisconnectTimes() >= 5 && ipInfo.getAvailableTimes() < 1) {
            System.out.println(ipInfo.getIpInfo() + "代理无效，删除中");
            DdosUtil.remove(ipInfo);
        }else{
            ipInfo.setDisconnectTimes(ipInfo.getDisconnectTimes() + 1);
        }
    }

    public static void ipSuccess(IpInfoModel ipInfo) {
        ipInfo.setAvailableTimes(ipInfo.getAvailableTimes() + 1);
    }

}
