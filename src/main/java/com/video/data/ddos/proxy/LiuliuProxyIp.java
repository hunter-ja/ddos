package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class LiuliuProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        List<IpInfoModel> ipList = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementById("main").select("table").select("tbody").select("tr");
        for (Element element : elements) {
            Elements tdElement = element.select("td");
            if(!"ip".equals(tdElement.get(0).text())) {
                IpInfoModel ipInfoModel = new IpInfoModel();
                ipInfoModel.setScheme("HTTP");
                ipInfoModel.setIp(tdElement.eq(0).text());
                ipInfoModel.setProt(tdElement.get(1).text());
                ipInfoModel.setSource("66代理");
                ipInfoModel.setDisconnectTimes(0);
                ipList.add(ipInfoModel);
            }
        }
        return ipList;
    }

    public String getUrl(int page) {
        return "http://www.66ip.cn/_" + page + ".html";
    }

    public int getMaxPage() {
        return 1800;
    }

    public String getProxyName() {
        return "66代理";
    }
}
