package com.fulaan.util;

import com.fulaan.pojo.ProductModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/26.
 */
public class JdCrawlingUtil {

    public static ProductModel getJDH5(String html) {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);

        Element element = doc.getElementById("slide");
        Element element1 = element.child(0).child(0).child(0);
        Element element2 = doc.getElementsByClass("title-text").get(0);
        Element element3 = doc.getElementsByClass("big-price").first();
        String url = element1.attr("src");
        String decs = element2.text();
        String price = "￥" + element3.text();
        return setModel(url, decs, price);
    }

    public static ProductModel getTaoBaoData(String html) throws Exception {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的内容
        Element ele = doc.getElementById("J_ImgBooth");
        Element element = doc.getElementsByClass("tb-main-title").get(0);
        Element element1 = doc.getElementsByClass("tb-rmb-num").get(0);
        String url = ele.attr("src");
        String decs = element.html();
        String price = "￥" + element1.html();
        return setModel(url, decs, price);
    }

    public static ProductModel setModel(String url, String decs, String price) {
        ProductModel productModel = new ProductModel();
        productModel.setImageUrl(url);
        productModel.setProductDescription(decs);
        productModel.setProductPrice(price);
        //返回数据
        return productModel;
    }

    public static ProductModel getDangData(String html) throws Exception {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的内容
        Element ele = doc.getElementById("largePic");
        Element element = doc.getElementsByClass("name_info").get(0);
        Element element1 = doc.getElementById("dd-price");
        String url = ele.attr("src");
        String decs = element.child(0).html();
        String price = element1.text();
        return setModel(url, decs, price);
    }

    public static ProductModel getJdData(String html) throws Exception {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的内容
        Element ele = doc.getElementById("spec-n1");
        Element ele1 = doc.getElementById("name");
        Element ele2 = doc.getElementById("mini-jd-price");
        String url = ele.child(0).attr("src");
        String decs = ele1.child(0).html();
        String price = ele2.html();
        return setModel(url, decs, price);
    }
}
