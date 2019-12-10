package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class QiyunProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        List<IpInfoModel> ipList = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByClass("layui-table").eq(0).select("tbody").select("tr");
        for (Element element : elements) {
            Elements tdElement = element.select("td");
            IpInfoModel ipInfoModel = new IpInfoModel();
            ipInfoModel.setScheme("HTTP");
            ipInfoModel.setIp(tdElement.eq(0).text());
            ipInfoModel.setProt(tdElement.get(1).text());
            ipInfoModel.setSource("89代理");
            ipInfoModel.setDisconnectTimes(0);
            ipList.add(ipInfoModel);
        }
        return ipList;
    }

    public String getUrl(int page) {
        return "http://www.89ip.cn/index_" + page + ".html";
    }

    public int getMaxPage() {
        return 70;
    }

    public String getProxyName() {
        return "89代理";
    }
}
