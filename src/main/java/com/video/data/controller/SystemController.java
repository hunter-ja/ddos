package com.video.data.controller;

import com.video.data.model.IpInfoModel;
import com.video.data.model.ProxyIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SystemController {

    @Autowired
    ProxyIp proxyIp;

    @RequestMapping("/ip/list")
    public String ipDetail() {
        List<IpInfoModel> list = proxyIp.getProxyIps();
        StringBuilder sb = new StringBuilder();
        sb.append("<meta http-equiv=\"refresh\" content=\"1\">");
        sb.append("当前可用IP：").append(list.size()).append(" 个").append("</br>");
        sb.append("<style>");
        sb.append("td {border:1px solid #eee;padding:10px;}");
        sb.append("</style>");
        sb.append("<table><thead><tr><td>协议</td><td>IP</td><td>端口</td>" +
                "<td>来源</td><td>连接失败次数</td><td>连接成功次数</td><td>最后使用时间</td></tr></thead>");
        sb.append("<tbody>");
        for (IpInfoModel ip : list) {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(ip.getScheme());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getIp());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getProt());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getSource());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getDisconnectTimes());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getAvailableTimes());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(ip.getLastUseTime());
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    @RequestMapping("/status")
    public String status() {
        StringBuilder proxyList = new StringBuilder();
        for (String proxy : proxyIp.getProxyList()) {
            proxyList.append(proxy).append(" ,");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<meta http-equiv=\"refresh\" content=\"1\">");
        sb.append("<style>");
        sb.append("td {border:1px solid #eee;padding:10px;}");
        sb.append("</style>");
        sb.append("<table><tbody>");
        sb.append("<tr><td style='text-align:right;'>系统启动时间:</td><td>")
                .append(proxyIp.getStartTime()).append("</td></tr>");
        sb.append("<tr><td style='text-align:right;'>成功访问目标次数:</td><td>").
                append(proxyIp.getRequestTimes()).append("次</td></tr>");
        sb.append("<tr><td style='text-align:right;'>代理连接不上次数:</td><td>")
                .append(proxyIp.getIpErrorNumber()).append("次</td></tr>");
        sb.append("<tr><td style='text-align:right;'>目标网站500错误次数:</td><td>")
                .append(proxyIp.getRequestErrorTimes()).append("次</td></tr>");
        sb.append("<tr><td style='text-align:right;'>攻击目标网站次数:</td><td>")
                .append(proxyIp.getTotalTimes()).append("次</td></tr>");
        sb.append("<tr><td style='text-align:right;'>已启用代理:</td><td>[")
                .append(proxyList.toString()).append("]</td></tr>");
        for (Map.Entry entry : proxyIp.getActivePorxy().entrySet()) {
            sb.append("<tr><td style='text-align:right;'>").append(entry.getKey())
                    .append("：</td><td>最后执行时间：")
                    .append(entry.getValue()).append("</td></tr>");

        }

        return sb.toString();
    }

}
