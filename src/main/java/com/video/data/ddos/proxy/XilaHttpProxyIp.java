package com.video.data.ddos.proxy;

import com.video.data.model.IpInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class XilaHttpProxyIp extends BaseProxyIp {

    public List<IpInfoModel> parse(String content) {
        List<IpInfoModel> ipList = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByClass("fl-table").eq(0);
        Elements tableElements = elements.select("tbody").select("tr");
        for (Element tr : tableElements) {
            Elements tdElement = tr.select("td");
            IpInfoModel ipInfoModel = new IpInfoModel();
            ipInfoModel.setScheme("HTTP");
            ipInfoModel.setIp(tdElement.eq(0).text().split(":")[0]);
            ipInfoModel.setProt(tdElement.eq(0).text().split(":")[1]);
            ipInfoModel.setSource("西拉HTTP");
            ipInfoModel.setDisconnectTimes(0);
            ipList.add(ipInfoModel);
        }
        return ipList;
    }

    public String getUrl(int page) {
        return "http://www.nimadaili.com/http/" + page + "/";
    }

    public int getMaxPage() {
        return 500;
    }

    public String getProxyName() {
        return "西拉HTTP代理";
    }

}
