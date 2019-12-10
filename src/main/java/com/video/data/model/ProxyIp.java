package com.video.data.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyIp {

    private List<IpInfoModel> proxyIps;

    private Long requestErrorTimes = 0L;

    private Long requestTimes = 0L;

    private Long ipErrorNumber = 0L;

    private Long totalTimes = 0L;

    private String startTime = "";

    private Map<String, String> activePorxy = new HashMap<>();

    private List<String> proxyList = new ArrayList<>();

    public List<IpInfoModel> getProxyIps() {
        synchronized (proxyIps) {
            return proxyIps;
        }
    }

    public void setProxyIps(List<IpInfoModel> proxyIps) {
        this.proxyIps = proxyIps;
    }

    public Long getRequestErrorTimes() {
        return requestErrorTimes;
    }

    public void setRequestErrorTimes(Long requestErrorTimes) {
        synchronized (this.requestErrorTimes) {
            this.requestErrorTimes = requestErrorTimes;
        }
    }

    public Long getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(Long requestTimes) {
        synchronized (this.requestTimes) {
            this.requestTimes = requestTimes;
        }
    }

    public Long getIpErrorNumber() {
        return ipErrorNumber;
    }

    public void setIpErrorNumber(Long ipErrorNumber) {
        synchronized (this.ipErrorNumber) {
            this.ipErrorNumber = ipErrorNumber;
        }
    }

    public Long getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Long totalTimes) {
        synchronized (this.totalTimes) {
            this.totalTimes = totalTimes;
        }
    }

    public List<String> getProxyList() {
        return proxyList;
    }

    public void setProxyList(List<String> proxyList) {
        this.proxyList = proxyList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Map<String, String> getActivePorxy() {
        return activePorxy;
    }

    public void setActivePorxy(Map<String, String> activePorxy) {
        synchronized (this.activePorxy) {
            this.activePorxy = activePorxy;
        }
    }
}
