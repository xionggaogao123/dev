package com.fulaan.util;

import com.fulaan.pojo.ProductModel;
import com.fulaan.util.crawl.CrawlData;
import com.pojo.parentChild.ParentChildActivityEntry;
import org.bson.types.ObjectId;
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

    public static boolean ParseData(String html, List<ParentChildActivityEntry> entries, String cityName, ObjectId regionId, int expense,
                                    String startTime,CrawlData.Record record,int number) {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        Element element = doc.getElementsByClass("find_main_ul").first();

        Elements elements = element.getElementsByTag("li");
        for (Element ele : elements) {
            Element element1 = ele.getElementsByTag("a").first();
            ParentChildActivityEntry entry = new ParentChildActivityEntry();
            entry.setRegionId(regionId);
            entry.setCity(cityName);
            entry.setExpense(expense);
            entry.setStartTime(startTime);
            if (null != element1) {
                entry.setActivityUrl(element1.attr("href"));
                Element element2 = element1.getElementsByClass("hd_pic").first();
                if (null != element2) {
                    entry.setActivityImage(element2.attr("data-src"));
                }
            } else {
                continue;
            }
            Element element2 = ele.getElementsByClass("find_main_div").first();
            String title = element2.getElementsByClass("find_main_title").first().child(0).getElementsByTag("h4").first().html();
            entry.setActivityName(title);
            String time = element2.getElementsByClass("find_main_time").first().getElementsByTag("p").first().html();
            entry.setActivityTime(time);
            Element address = element2.getElementsByClass("find_main_address").first();
            if (null != address) {
                String content = address.getElementsByTag("p").first().getElementsByTag("a").text();
                entry.setActivityContent(content);
            }

            Element element3 = element2.getElementsByClass("find_main_b").first().getElementsByClass("find_main_b_l").first();
            Element element4 = element3.getElementsByTag("a").first().getElementsByTag("img").first();
            if (null != element4) {
                entry.setActivityDescImage(element4.attr("src"));
            }
            String desc = element3.getElementsByTag("a").first().nextElementSibling().html();
            entry.setActivityDescription(desc);
            entries.add(entry);
        }

        if(number==1){
           if(entries.size()==0){
              return false;
           }else{
              record.setName(entries.get(0).getActivityName());
              record.setDesc(entries.get(0).getActivityDescription());
              record.setTime(entries.get(0).getActivityTime());
              return true;
           }
        }else{
            if(entries.size()==0){
                return false;
            }else{
                ParentChildActivityEntry entry=entries.get(0);
                String acName=entry.getActivityName();
                String ac=entry.getActivityDescription();
                String time=entry.getActivityTime();
                if(record.getName().equals(acName)&&record.getDesc().equals(ac)&&record.getTime().equals(time)){
                     entries.clear();
                     return false;
                }else{
                    return true;
                }
            }
        }
    }

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
//        Element ele1 = doc.getElementById("name");
//        Element ele2 = doc.getElementById("mini-jd-price");
        String url = ele.child(0).attr("data-origin");
        String decs = ele.child(0).attr("alt");
//        String price = ele2.html();
        return setModel("http:"+url, decs, "");
    }
}
