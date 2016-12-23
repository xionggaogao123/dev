package com.fulaan.util;

import com.fulaan.mall.service.EGoodsService;
import com.fulaan.pojo.ProductModel;
import com.fulaan.util.crawl.CrawlData;
import com.pojo.ebusiness.EGoodsDTO;
import com.pojo.parentChild.ParentChildActivityEntry;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/26.
 */
public class URLParseUtil {

    private static EGoodsService eGoodsService = new EGoodsService();

    public static boolean ParseHDBData(HttpClient client, String url,List<ParentChildActivityEntry> entries,String cityName,ObjectId regionId,int expense,
                                       String startTime, CrawlData.Record record,int number) throws Exception {
        HttpResponse response = HttpUtils.getCrawlHtml(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        if (StatusCode == 200) {
            String entity = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(response.getEntity());
            return  JdCrawlingUtil.ParseData(entity,entries,cityName,regionId,expense,startTime,record,number);
        } else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
            return false;
        }
    }

    public static ProductModel urlParser(HttpClient client, String url) throws Exception {
        //获取网站响应的html，这里调用了HTTPUtils类
        ProductModel productModel = new ProductModel();
        HttpResponse response = HttpUtils.getCrawlHtml(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        if (StatusCode == 200) {
            String entity = EntityUtils.toString(response.getEntity(), "utf-8");
            if (url.contains("item.jd.com")) {
                productModel = JdCrawlingUtil.getJdData(entity);
            } else if (url.contains("product.dangdang.com")) {
                productModel = JdCrawlingUtil.getDangData(entity);
            } else if (url.contains("fulaan.com")) {
                String productId = url.substring(url.indexOf("=") + 1, url.length());
                EGoodsDTO eGoodsDTO = eGoodsService.detail(new ObjectId(productId));
                productModel = JdCrawlingUtil.setModel((String) eGoodsDTO.getImages().get(0).getValue(), eGoodsDTO.getGoodsName(), eGoodsDTO.getDiscountPrice() + "");
            } else if (url.contains("m.jd.com")) {
                productModel = JdCrawlingUtil.getJDH5(entity);
            } else if (url.contains("item.taobao.com")) {
                productModel = JdCrawlingUtil.getTaoBaoData(entity);
            } else if (url.contains("hdb.com")){
                //爬取互动吧数据

            }

            EntityUtils.consume(response.getEntity());
        } else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        productModel.setUrl(url);
        return productModel;
    }
}
