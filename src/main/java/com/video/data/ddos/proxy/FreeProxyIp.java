package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FreeProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        List<IpInfoModel> ipList = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByClass("DataGrid").eq(0).select("tbody").select("tr");
        for (Element element : elements) {
            Elements tdElement = element.select("td");
            if(!"IP地址".equals(tdElement.get(0).text())) {
                IpInfoModel ipInfoModel = new IpInfoModel();
                ipInfoModel.setScheme(tdElement.eq(2).text());
                ipInfoModel.setIp(tdElement.eq(0).text());
                ipInfoModel.setProt(tdElement.get(1).text());
                ipInfoModel.setSource("free");
                ipInfoModel.setDisconnectTimes(0);
                ipList.add(ipInfoModel);
            }
        }
        return ipList;
    }

    public String getUrl(int page) {
        return "http://freeproxylists.net/zh/?page=" + page;
    }

    public int getMaxPage() {
        return 1800;
    }

    public String getProxyName() {
        return "free proxy lists代理";
    }
}
