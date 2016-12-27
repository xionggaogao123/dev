package com.fulaan.util.crawl;

import com.db.parentChild.ParentChildActivityDao;
import com.fulaan.train.service.RegionService;
import com.fulaan.util.URLParseUtil;
import com.pojo.parentChild.ParentChildActivityEntry;
import com.pojo.train.RegionEntry;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
public class CrawlData {


    private List<String> doCycleData() throws Exception{
        List<String> times=new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long nowTime=System.currentTimeMillis();
        String startTime=sdf.format(new Date(nowTime));
        times.add(startTime);
        for(int i=1;i<=30;i++){
            long time=i*24L*60L*60L*1000L+nowTime;
            String item=sdf.format(new Date(time));
            String[] str=item.split("-");
            StringBuffer buffer = new StringBuffer();
            buffer.append(str[0]);
            buffer.append("-");
            buffer.append(Integer.valueOf(str[1]));
            buffer.append("-");
            buffer.append(Integer.valueOf(str[2]));
            times.add(buffer.toString());
        }
      return times;
    }


    public  void crawlParentData() throws Exception{
        List<String> times=doCycleData();
        List<Integer> expenses = new ArrayList<Integer>();
        expenses.add(0);
        expenses.add(1);
        RegionService regionService = new RegionService();
        RegionEntry regionEntry = regionService.getRegionEntry("上海市");
        String city = "shanghai";
        boolean flag = true;
        int number = 1;
        for(String startTime:times) {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient client = httpClientBuilder.build();
            ParentChildActivityDao parentChildActivityDao = new ParentChildActivityDao();
            List<ParentChildActivityEntry> entries = new ArrayList<ParentChildActivityEntry>();
            //数据记录,防止循环爬取
            Record record = new Record();
            for (Integer expense : expenses) {
                while (flag) {
                    String url = "http://www.hdb.com/find/" + city + "-flzj-fy" + expense + "-p" + number + "?start_time=" + startTime;
                    String cityName = "上海";
                    ObjectId regionId = regionEntry.getID();
                    flag = URLParseUtil.ParseHDBData(client, url, entries, cityName, regionId, expense, startTime, record, number);
                    number++;
                    if (entries.size() > 0) {
                        //删除数据
                        parentChildActivityDao.removeData(startTime,expense);
                        parentChildActivityDao.batchAddData(entries);
                    }
                    entries.clear();
                }
                flag = true;
                number = 1;
            }
        }
    }

    public static void main(String[] args) throws Exception{
//        CrawlData crawlData=new CrawlData();
//        crawlData.crawlParentData();
//        cycleData();
//        todayData();
    }

    public static class Record{
        private String name;
        private String time;
        private String desc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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
