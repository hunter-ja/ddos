package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class KuaiProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        List<IpInfoModel> ipList = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementById("list").select("table").select("tbody").select("tr");
        for (Element element : elements) {
            Elements tdElement = element.select("td");
            IpInfoModel ipInfoModel = new IpInfoModel();
            ipInfoModel.setScheme(tdElement.eq(3).text());
            ipInfoModel.setIp(tdElement.eq(0).text());
            ipInfoModel.setProt(tdElement.eq(1).text());
            ipInfoModel.setSource("KuaiDaili");
            ipInfoModel.setDisconnectTimes(0);
            ipList.add(ipInfoModel);
        }
        return ipList;
    }

    public String getUrl(int page) {
        return "https://www.kuaidaili.com/free/inha/" + page;
    }

    public int getMaxPage() {
        return 3000;
    }

    public String getProxyName() {
        return "快代理";
    }
}
