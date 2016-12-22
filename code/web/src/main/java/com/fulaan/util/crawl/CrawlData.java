package com.fulaan.util.crawl;

import com.db.parentChild.ParentChildActivityDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.parentChild.dto.ParentChildActivityDTO;
import com.fulaan.train.service.RegionService;
import com.fulaan.util.URLParseUtil;
import com.pojo.parentChild.ParentChildActivityEntry;
import com.pojo.train.RegionEntry;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
public class CrawlData {


    public static void main(String[] args) throws Exception{
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();
        String startTime="2016-12-24";
        RegionService regionService=new RegionService();
        ParentChildActivityDao parentChildActivityDao=new ParentChildActivityDao();
        RegionEntry regionEntry=regionService.getRegionEntry("上海市");
        List<Integer> expenses=new ArrayList<Integer>();
        expenses.add(0);
        expenses.add(1);
        String city="shanghai";
        int number=1;
        boolean flag=true;
        List<ParentChildActivityEntry> entries=new ArrayList<ParentChildActivityEntry>();
        for(Integer expense:expenses) {
            while(flag) {
                String url = "http://www.hdb.com/find/" + city + "-flzj-fy" + expense + "-p" + number + "?start_time=" + startTime;
                String cityName="上海";
                ObjectId regionId=regionEntry.getID();
                flag=URLParseUtil.ParseHDBData(client, url, entries,cityName,regionId,expense,startTime);
                number++;
            }
            parentChildActivityDao.batchAddData(entries);
            entries.clear();
            flag=true;
            number=1;
        }

    }
    public static void getDatasByClass()
    {
        Rule rule = new Rule(
                "http://www1.sxcredit.gov.cn/public/infocomquery.do?method=publicIndexQuery",
                new String[] { "query.enterprisename","query.registationnumber" }, new String[] { "兴网","" },
                "cont_right", Rule.CLASS, Rule.POST);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
    }


    public static void getDatasByCssQuery()
    {
        Rule rule = new Rule("http://www.11315.com/search",
                new String[] { "name" }, new String[] { "兴网" },
                "div.g-mn div.con-model", Rule.SELECTION, Rule.GET);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
    }

    public static void printf(List<LinkTypeData> datas)
    {
        for (LinkTypeData data : datas)
        {
            System.out.println(data.getLinkText());
            System.out.println(data.getLinkHref());
            System.out.println("***********************************");
        }

    }
}
