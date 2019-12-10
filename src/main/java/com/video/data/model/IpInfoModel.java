package com.video.data.model;

public class IpInfoModel {

    private String scheme;

    private String ip;

    private String prot;

    private String source;

    private Integer disconnectTimes = 0;

    private Integer availableTimes = 0;

    private String lastUseTime = "未使用";

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDisconnectTimes() {
        return disconnectTimes;
    }

    public void setDisconnectTimes(Integer disconnectTimes) {
        synchronized (this.disconnectTimes) {
            this.disconnectTimes = disconnectTimes;
        }
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public Integer getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(Integer availableTimes) {
        synchronized (this.availableTimes) {
            this.availableTimes = availableTimes;
        }
    }

    public String getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(String lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getIpInfo() {
        return scheme + ":" + ip + ":" + prot + ":" + source + ":连接失败次数:" + disconnectTimes + ":连接成功次数:" + availableTimes;
    }
}
