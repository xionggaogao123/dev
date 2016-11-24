package com.fulaan.webspider;


import com.db.WebSpiderSchool.WebSpiderDao;
import com.db.app.RegionDao;
import com.db.school.SchoolDao;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pojo.WebSpiderSchool.WebSpiderSchool;
import com.pojo.app.RegionEntry;
import com.pojo.school.SchoolEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作手册
 * 第一步：修改region表中巢湖市数据
 /webspider/updateChaohu.do

 第二步：增加亳州市
 /webspider/addBozhou.do

 第三步:增加县级行政单位
 /webspider/addRegion.do

 第四步：爬取数据，共13000余条数据，耗时四十分钟左右
 /webspider/spider.do

 第五步：修改爬取数据的schoolId,如果在k6kt中已经存在该学校，修改该学校的id为k6kt平台的id
 /webspider/updateSchoolId.do

 *
 * Created by qiangm on 2016/3/15.
 */
@Controller
@RequestMapping("/webspider")
public class WebSpider {
    private WebSpiderDao webSpiderDao = new WebSpiderDao();
    private RegionDao regionDao = new RegionDao();
    private SchoolDao schoolDao = new SchoolDao();
    private List<WebSpiderSchool> list = new ArrayList<WebSpiderSchool>();
    private Map<String, ObjectId> regionMap = getMapInfo();
    private ObjectId provinceId = new ObjectId("55934c13f6f28b7261c19c3d");//安徽省id

    @RequestMapping("/spider")
    @ResponseBody
    public Map<String, String> main() {
        int[] allId = new int[]{1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 3513, 3514,
                1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 3515,
                11148, 1149, 1150, 1151, 1152, 1153, 1154, 3516, 3517,
                1157, 1158, 1159, 1160, 1161, 1162, 3518,
                1165, 1166, 1167, 1168, 1169, 1170,
                1173, 1174, 1175, 1176,
                1179, 1180, 1181, 1182,
                1185, 1186, 1187, 1188, 1189, 1190, 1192, 1193, 1194, 1195, 3519,
                1198, 1199, 1200, 1201, 1202, 1203, 1204,
                1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214,
                1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 3564,
                1227, 1228, 1229, 1230, 1231,
                1234, 1235, 1236, 1237, 1238, 1239, 1240, 3520,
                1243, 1244, 1245, 1246,
                1249, 1250, 1251, 1252, 3521,
                1255, 1256, 1257, 1258, 1259, 1260, 1261
        };
        for (int i = 0; i < allId.length; i++) {
            Worm(allId[i]);
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getSchoolName().equals(list.get(i).getSchoolName())) {
                    list.remove(j);
                }
            }
        }
        webSpiderDao.addRegionEntrys(list);
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    public void Worm(int areaId) {
        String url = "http://www.ahedu.cn/eschool/index.php?m=School&c=Index&a=ajaxSchoolCommunitySearch";
        int skip = 0;
        int limit = 6;
        int count = 0;
        while (true) {
            skip = limit * (count);
            try {
                URL urlObj = new URL(url);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                // 设置边界
                String BOUNDARY = "----------" + System.currentTimeMillis();
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                conn.setRequestProperty("postman-token", "d89d0dc7-60cb-a9c4-7ddb-486b89da38d9");
                conn.setRequestProperty("cache-control", "no-cache");
                // 请求正文信息
                StringBuilder sb = new StringBuilder();
                sb.append("--"); // ////////必须多两道线
                sb.append(BOUNDARY);
                sb.append("\r\n");

                sb.append("Content-Disposition: form-data; name=\"areaId\";\r\n");
                sb.append("\r\n");
                sb.append(areaId + "\r\n");
                sb.append("--" + BOUNDARY);
                sb.append("\r\n");

                sb.append("Content-Disposition: form-data; name=\"schoolName\";\r\n");
                sb.append("\r\n");
                sb.append("" + "\r\n");
                sb.append("--" + BOUNDARY);
                sb.append("\r\n");

                sb.append("Content-Disposition: form-data; name=\"skip\";\r\n");
                sb.append("\r\n");
                sb.append(skip + "\r\n");
                sb.append("--" + BOUNDARY);
                sb.append("\r\n");

                sb.append("Content-Disposition: form-data; name=\"limit\";\r\n");
                sb.append("\r\n");
                sb.append(limit + "\r\n");
                sb.append("--" + BOUNDARY);
                sb.append("\r\n");

                byte[] head = sb.toString().getBytes("utf-8");

                // 获得输出流
                OutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(head);

                // 结尾部分
                byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
                out.write(foot);
                out.flush();
                out.close();

                // 定义BufferedReader输入流来读取URL的响应
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                conn.disconnect();

                JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
                JsonArray jsonArray = jsonparer.parse(buffer.toString()).getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        JsonObject json1 = jsonArray.get(i).getAsJsonObject().get("info").getAsJsonObject();
                        JsonObject school = json1.getAsJsonObject("school");
                        String schoolName = school.get("schoolName").getAsString();
                        int schoolType = school.get("schoolType").getAsInt();
                        String city = json1.get("city").getAsString();
                        String county = json1.get("county").getAsString();
                        String schoolIcons = json1.get("schoolIcons").getAsString();
                        JsonObject json2 = jsonArray.get(i).getAsJsonObject().get("num").getAsJsonObject();
                        int teacherNum = json2.get("teacherNum").getAsInt();
                        int classNum = json2.get("classNum").getAsInt();
                        schoolIcons = "http://www.ahedu.cn/eschool" + schoolIcons;
                        ObjectId cityId = regionMap.get(city);
                        ObjectId countyId = regionMap.get(county);
                        int newType=0;
                        if(schoolType==10||schoolType==14||schoolType==15||schoolType==16||schoolType==17)
                        {
                            newType=0;
                        }
                        else if(schoolType==6)
                        {
                            newType=12;
                        }
                        else if(schoolType==1)
                        {
                            newType=6;
                        }
                        else if(schoolType==7)
                        {
                            newType=1;
                        }
                        else if(schoolType==2)
                        {
                            newType=14;
                        }
                        else if(schoolType==8)
                        {
                            newType=2;
                        }
                        else if(schoolType==3)
                        {
                            newType=8;
                        }
                        else if(schoolType==5)
                        {
                            newType=2;
                        }
                        WebSpiderSchool webSpiderSchool = new WebSpiderSchool(schoolName, newType, provinceId, cityId, countyId, schoolIcons, classNum, teacherNum);
                        list.add(webSpiderSchool);
                    } catch (Exception e) {
                        //System.out.println(e);
                        return;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            count++;
        }
    }

    public void save(String schoolName, int type, ObjectId province, ObjectId city, ObjectId county, String icon, int classNum, int teacherNum) {
        icon = "http://www.ahedu.cn/eschool" + icon;
        WebSpiderSchool webSpiderSchool = new WebSpiderSchool(schoolName, type, province, city, county, icon, classNum, teacherNum);
        int count = webSpiderDao.count(schoolName, city);
        if (count == 0)
            webSpiderDao.save(webSpiderSchool);
    }

    //获取安徽各级行政单位map
    public Map<String, ObjectId> getMapInfo() {
        Map<String, ObjectId> map = new HashMap<String, ObjectId>();
        List<ObjectId> regionIds = new ArrayList<ObjectId>();
        regionIds.add(new ObjectId("55934c13f6f28b7261c19c3d"));
        regionIds.add(new ObjectId("55934c14f6f28b7261c19c5f"));
        regionIds.add(new ObjectId("55934c14f6f28b7261c19c6a"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c75"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c7e"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c85"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c89"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c8e"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c94"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c97"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c9d"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19ca0"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cab"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cb4"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cc2"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cc7"));
        regionIds.add(new ObjectId("56e8fa90d5bf30224ec20afb"));
        //List<RegionEntry> regionEntryList=regionDao.getRegionEntryListByPidOrId(regionIds);
        List<RegionEntry> regionEntryList = regionDao.getRegionEntryListByPid(regionIds);
        for (RegionEntry regionEntry : regionEntryList) {
            map.put(regionEntry.getName(), regionEntry.getID());
        }
        //System.out.println(map);
        return map;
    }

    /**
     * 增加县级行政单位
     */
    @RequestMapping("/addRegion")
    @ResponseBody
    public Map<String, String> addRegion() {
        ObjectId maanshan = new ObjectId("55934c14f6f28b7261c19c5f");
        ObjectId chuzhou = new ObjectId("55934c14f6f28b7261c19c6a");
        ObjectId huaibei = new ObjectId("55934c15f6f28b7261c19c75");
        ObjectId hefei = new ObjectId("55934c15f6f28b7261c19c7e");
        ObjectId tongling = new ObjectId("55934c15f6f28b7261c19c85");
        ObjectId anqing = new ObjectId("55934c15f6f28b7261c19c89");
        ObjectId xuancheng = new ObjectId("55934c15f6f28b7261c19c8e");
        ObjectId wuhu = new ObjectId("55934c15f6f28b7261c19c94");
        ObjectId fuyang = new ObjectId("55934c15f6f28b7261c19c97");
        ObjectId chizhou = new ObjectId("55934c15f6f28b7261c19c9d");
        ObjectId suzhou = new ObjectId("55934c15f6f28b7261c19ca0");
        ObjectId huainan = new ObjectId("55934c15f6f28b7261c19cab");
        ObjectId liuan = new ObjectId("55934c15f6f28b7261c19cb4");
        ObjectId bengbu = new ObjectId("55934c15f6f28b7261c19cc2");
        ObjectId huangshan = new ObjectId("55934c15f6f28b7261c19cc7");
        ObjectId bozhou = new ObjectId("56e8fa90d5bf30224ec20afb");
        String[] hefeiArr = new String[]{"瑶海区", "庐阳区", "蜀山区", "高新开发区", "包河区", "长丰县", "肥东县", "肥西县", "庐江县",/*"巢湖市",*/"经济开发区", "新站试验区"};
        int[] hefeiId = new int[]{1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 3513, 3514};
        String[] wuhuArr = new String[]{"镜湖区", "弋江区", "鸠江区", "三山区", "芜湖县", "繁昌县", "南陵县", "无为县", "经济开发区"};
        int[] wuhuId = new int[]{1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 3515};
        String[] bengbuArr = new String[]{"龙子湖区", "蚌山区", "禹会区", "淮上区", "怀远县", "五河县", "固镇县", "经济开发区", "高新开发区"};
        int[] bengbuId = new int[]{11148, 1149, 1150, 1151, 1152, 1153, 1154, 3516, 3517};
        String[] huainanArr = new String[]{"大通区", "田家庵区", "谢家集区", "八公山区", "潘集区", "凤台县", "毛集实验区"};
        int[] huainanId = new int[]{1157, 1158, 1159, 1160, 1161, 1162, 3518};
        String[] maanshanArr = new String[]{"花山区", "雨山区", "博望区", "当涂县", "含山县", "和县"};
        int[] maanshanId = new int[]{1165, 1166, 1167, 1168, 1169, 1170};
        String[] huaibeiArr = new String[]{"杜集区", "相山区", "烈山区", "濉溪县"};
        int[] huaibeiId = new int[]{1173, 1174, 1175, 1176};
        String[] tonglingArr = new String[]{"铜官山区", "狮子山区", "郊区", "铜陵县"};
        int[] tonglingId = new int[]{1179, 1180, 1181, 1182};
        String[] anqingArr = new String[]{"迎江区", "大观区", "宜秀区", "怀宁县", "枞阳县", "潜山县", "太湖县", "宿松县", "望江县", "岳西县", "桐城市", "经济开发区"};
        int[] anqingId = new int[]{1185, 1186, 1187, 1188, 1189, 1190, 1192, 1193, 1194, 1195, 3519};
        String[] huangshanArr = new String[]{"屯溪区", "黄山区", "徽州区", "歙县", "休宁县", "黟县", "祁门县"};
        int[] huangshanId = new int[]{1198, 1199, 1200, 1201, 1202, 1203, 1204};
        String[] chuzhouArr = new String[]{"琅琊区", "南谯区", "来安县", "全椒县", "定远县", "凤阳县", "天长市", "明光市"};
        int[] chuzhouId = new int[]{1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214};
        String[] fuyangArr = new String[]{"颍州区", "颍东区", "颍泉区", "临泉县", "太和县", "阜南县", "颍上县", "界首市", "经济技术开发区"};
        int[] fuyangId = new int[]{1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 3564};
        String[] suzhouArr = new String[]{"埇桥区", "砀山县", "萧县", "灵璧县", "泗县"};
        int[] suzhouId = new int[]{1227, 1228, 1229, 1230, 1231};
        String[] liuanArr = new String[]{"金安区", "裕安区", "寿县", "霍邱县", "舒城县", "金寨县", "霍山县", "叶集试验区"};
        int[] liuanId = new int[]{1234, 1235, 1236, 1237, 1238, 1239, 1240, 3520};
        String[] bozhouArr = new String[]{"谯城区", "涡阳县", "蒙城县", "利辛县"};
        int[] bozhouId = new int[]{1243, 1244, 1245, 1246};
        String[] chizhouArr = new String[]{"贵池区", "东至县", "石台县", "青阳县", "九华山风景区"};
        int[] chizhouId = new int[]{1249, 1250, 1251, 1252, 3521};
        String[] xuanchengArr = new String[]{"宣州区", "郎溪县", "广德县", "泾县", "绩溪县", "旌德县", "宁国市"};
        int[] xuanchengId = new int[]{1255, 1256, 1257, 1258, 1259, 1260, 1261};

        //合肥
        List<RegionEntry> list = new ArrayList<RegionEntry>();
        for (String str : hefeiArr) {
            RegionEntry regionEntry = new RegionEntry(4, hefei, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //芜湖
        list = new ArrayList<RegionEntry>();
        for (String str : wuhuArr) {
            RegionEntry regionEntry = new RegionEntry(4, wuhu, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //蚌埠
        list = new ArrayList<RegionEntry>();
        for (String str : bengbuArr) {
            RegionEntry regionEntry = new RegionEntry(4, bengbu, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //淮南
        list = new ArrayList<RegionEntry>();
        for (String str : huainanArr) {
            RegionEntry regionEntry = new RegionEntry(4, huainan, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //马鞍山
        list = new ArrayList<RegionEntry>();
        for (String str : maanshanArr) {
            RegionEntry regionEntry = new RegionEntry(4, maanshan, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //淮北
        list = new ArrayList<RegionEntry>();
        for (String str : huaibeiArr) {
            RegionEntry regionEntry = new RegionEntry(4, huaibei, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //铜陵
        list = new ArrayList<RegionEntry>();
        for (String str : tonglingArr) {
            RegionEntry regionEntry = new RegionEntry(4, tongling, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //安庆
        list = new ArrayList<RegionEntry>();
        for (String str : anqingArr) {
            RegionEntry regionEntry = new RegionEntry(4, anqing, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //黄山
        list = new ArrayList<RegionEntry>();
        for (String str : huangshanArr) {
            RegionEntry regionEntry = new RegionEntry(4, huangshan, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //滁州
        list = new ArrayList<RegionEntry>();
        for (String str : chuzhouArr) {
            RegionEntry regionEntry = new RegionEntry(4, chuzhou, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //阜阳
        list = new ArrayList<RegionEntry>();
        for (String str : fuyangArr) {
            RegionEntry regionEntry = new RegionEntry(4, fuyang, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //宿州
        list = new ArrayList<RegionEntry>();
        for (String str : suzhouArr) {
            RegionEntry regionEntry = new RegionEntry(4, suzhou, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //六安
        list = new ArrayList<RegionEntry>();
        for (String str : liuanArr) {
            RegionEntry regionEntry = new RegionEntry(4, liuan, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //亳州
        list = new ArrayList<RegionEntry>();
        for (String str : bozhouArr) {
            RegionEntry regionEntry = new RegionEntry(4, bozhou, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //池州
        list = new ArrayList<RegionEntry>();
        for (String str : chizhouArr) {
            RegionEntry regionEntry = new RegionEntry(4, chizhou, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        //宣城
        list = new ArrayList<RegionEntry>();
        for (String str : xuanchengArr) {
            RegionEntry regionEntry = new RegionEntry(4, xuancheng, str, 0);
            list.add(regionEntry);
        }
        regionDao.addRegionEntrys(list);
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    /**
     * 数据库无亳州，添加
     */
    @RequestMapping("/addBozhou")
    @ResponseBody
    public Map<String, String> addBozhou() {
        RegionEntry regionEntry = new RegionEntry(3, new ObjectId("55934c13f6f28b7261c19c3d"), "亳州市", 0);
        regionEntry.setID(new ObjectId("56e8fa90d5bf30224ec20afb"));
        regionDao.addRegionEntry(regionEntry);
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    /**
     * 数据库巢湖有错，修改
     */
    @RequestMapping("/updateChaohu")
    @ResponseBody
    public Map<String, String> updateChaohu() {
        regionDao.update(new ObjectId("55934c15f6f28b7261c19d6c"), 4, new ObjectId("55934c15f6f28b7261c19c7e"));
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    /**
     * 修改学校ID,K6KT平台已存在的学校使用k6kt的schoolId
     */
    @RequestMapping("/updateSchoolId")
    @ResponseBody
    public Map<String, String> setSchoolId() {
        List<ObjectId> regionIds = new ArrayList<ObjectId>();
        regionIds.add(new ObjectId("55934c13f6f28b7261c19c3d"));
        regionIds.add(new ObjectId("55934c14f6f28b7261c19c5f"));
        regionIds.add(new ObjectId("55934c14f6f28b7261c19c6a"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c75"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c7e"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c85"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c89"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c8e"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c94"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c97"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19c9d"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19ca0"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cab"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cb4"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cc2"));
        regionIds.add(new ObjectId("55934c15f6f28b7261c19cc7"));
        regionIds.add(new ObjectId("56e8fa90d5bf30224ec20afb"));
        List<SchoolEntry> schoolEntryList = schoolDao.getSchoolEntryByRegionIdList(regionIds);
        List<WebSpiderSchool> spiderSchools = webSpiderDao.findAll();
        for (WebSpiderSchool webSpiderSchool : spiderSchools) {
            //查询是否在school表中有存在
            boolean have = false;
            ObjectId newId = null;
            for (SchoolEntry s : schoolEntryList) {
                if(s.getDetail()!=null) {
                    if (s.getName().equals(webSpiderSchool.getSchoolName()) || s.getDetail().equals(webSpiderSchool.getSchoolName()) || s.getDetail().contains(webSpiderSchool.getSchoolName())) {
                        have = true;
                        newId = s.getID();
                        break;
                    }
                }
            }
            if (have) {
                //修改webspider中的字段值
                webSpiderDao.updateSchoolId(webSpiderSchool.getID(), newId);
                System.out.println(webSpiderSchool.getSchoolName());
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    @RequestMapping("/updateSchoolType")
    @ResponseBody
    public Map<String, String> updateSchoolType() {
        webSpiderDao.updateSchoolType(10, 0);
        webSpiderDao.updateSchoolType(14, 0);
        webSpiderDao.updateSchoolType(15, 0);
        webSpiderDao.updateSchoolType(16, 0);
        webSpiderDao.updateSchoolType(17, 0);
        //以上几个修改为其他学校

        webSpiderDao.updateSchoolType(6, 12);
        webSpiderDao.updateSchoolType(1, 6);
        webSpiderDao.updateSchoolType(7, 1);
        webSpiderDao.updateSchoolType(2, 14);
        webSpiderDao.updateSchoolType(8, 2);
        webSpiderDao.updateSchoolType(3, 8);
        webSpiderDao.updateSchoolType(5, 2);


        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "200");
        return map;
    }

    public void setShidianxiao()
    {
        List<String> leftSchool=new ArrayList<String>();
        String[] schoolList=new String[]{
                "安庆市第四中学",
                        "宣城市泾县稼祥中学",
                        "宣城市广德县桃州中学",
                        "马鞍山市薛镇初级中学",
                        "马鞍山市红星中学",
                        "宿州市第九中学",
                        "马鞍山二十二中",
                        "合肥市第四十八滨湖校区中学",
                        "淮北市实验学校",
                        "铜陵市实验高级中学",
                        "铜陵市实验小学",
                        "铜陵市第七中学",
                        "芜湖市育红小学",
                        "宣城市第二小学",
                        "安庆市桐城市实验中学",
                        "安庆市第二中学",
                        "合肥市望龙学校（小、初）",
                        "毫州市谯城中学",
                        "安徽省濉溪县濉溪初中",
                        "合肥市青年路小学",
                        "安徽省东至二中",
                        "合肥市少儿艺术学校",
                        "宿州一中",
                        "马鞍山市第二中学",
                        "池州市九华乡中心小学",
                        "九华镇中心小学师生（九华山中心学校分部）",
                        "池州市拥华小学（九华山中心小学分部）",
                        "合肥潜山路小学",
                        "宣城市宁国市实验教育集团津北分校",
                        "亳州师专实验小学",
                        "淮南市第十九中学",
                        "芜湖市繁昌县城关实验小学",
                        "池州市青阳县第四中学",
                        "池州市东至县至德小学",
                        "六安市霍邱县城关镇中心小学",
                        "淮北市第一中学",
                        "宿松县实验小学",
                        "合肥师范附属小学",
                        "阜阳市第三中学",
                        "明光市工人子弟小学",
                        "淮北市实验高级中学",
                        "淮南市师范附属小学",
                        "安庆市宿松中学",
                        "蚌埠市第二中学",
                        "宿州市第二中学",
                        "黄山市屯溪第一中学",
                        "滁州市紫薇小学",
                        "安庆华中路第二小学",
                        "黄山市祁门县第二中学",
                        "黄山市屯溪长干小学",
                        "阜阳市鼓楼中心校",
                        "芜湖市第十二中学",
                        "芜湖市田家炳实验中学",
                        "阜阳实验中学",
                        "霍山第二中学",
                        "淮北市第三实验小学",
                        "砀山县砀城第一小学",
                        "淮南第二中学",
                        "蚌埠市第六中学",
                        "安徽省滁州中学",
                        "芜湖市安徽师范大学附属小学",
                        "马鞍山市含山县环峰小学",
                        "马鞍山市山南小学",
                        "安庆市杨桥中学",
                        "亳州一中南校",
                        "六安市寿县安丰高级中学",
                        "宣城市第二中学",
                        "合肥市第一中学",
                        "蚌埠第二实验小学",
                        "滁州市第六中学",
                        "池州市东至县第三中学",
                        "宣城市实验教育集团实验小学",
                        "濉溪县口子实验学校",
                        "马鞍山市当涂县鸿志学校",
                        "马鞍山松源培正中学",
                        "合肥市瑶海实验小学",
                        "砀山县第五中学",
                        "美诚国际教育",
                        "濉溪县化学学科基地",
                        "芜湖市火龙岗中学",
                        "铜陵市人民小学",
                        "马鞍山市村钟小学",
                        "马鞍山市和县善厚镇初级中学",
                        "马鞍山市皖江职业教育中心",
                        "马鞍山市第八中学东校区",
                        "马鞍山市花园初级中学",
                        "马鞍山市成功学校",
                        "马鞍山市霍里中心小学",
                        "马鞍山市南山小学",
                        "马鞍山三姚小学",
                        "马鞍山二中实验学校小学",
                        "马鞍山二中实验学校初中",
                        "马鞍山湖东路第四小学",
                        "马鞍山市第八中学东校区",
                        "马鞍山师苑小学",
                        "马鞍山市实验中学",
                        "马鞍山市珍珠园小学",
                        "铜陵市中等职业技术教育中心",
                        "马鞍山市雨山实验学校（7-9年级）",
                        "马鞍山市育才小学",
                        "马鞍山市和县盛家口小学",
                        "马鞍山湖东路第二小学",
                        "马鞍山中加双语学校",
                        "马鞍山市龙泉中心学校百峰小学",
                        "马鞍山市龙泉中心学校",
                        "马鞍山市建中学校",
                        "马鞍山市第九中学",
                        "淮南市职业教育中心",
                        "马鞍山市含山县东关中心学校",
                        "马鞍山市龙泉中心学校团结小学",
                        "马鞍山市第十九中学",
                        "马鞍山湖东路第三小学",
                        "合肥市莲花小学",
                        "芜湖市繁昌县孙村镇赤沙初中",
                        "马鞍山市和县历阳镇第二小学",
                        "马鞍山西埠镇中心小学",
                        "马鞍山市梅山小学",
                        "和县第四中学",
                        "和县后港初级中学",
                        "马鞍山濮塘中心小学",
                        "马鞍山市含山县前进小学 ",
                        "马鞍山市新市中心学校石门分校",
                        "马鞍山市含山县环峰第三小学",
                        "马鞍山市师范学校附属小学",
                        "马鞍山市银塘中心小学",
                        "马鞍山市丹阳中心学校",
                        "马鞍山市幸福路小学",
                        "马鞍山市博望中心学校",
                        "和县历阳镇中心小学 ",
                        "蚌埠市固镇县濠城初级中学",
                        "阜阳市第二十一中学",
                        "阜阳市颍东区和谐路小学",
                        "淮南第二十三中学",
                        "安徽省合肥市六安路小学中国铁建国际城校区",
                        "马鞍山新建小学",
                        "马鞍山市新市初级中学",
                        "马鞍山市新市中心学校",
                        "马鞍山市外国语学校",
                        "马鞍山市雨山实验学校（1-6年级）",
                        "马鞍山市新博初级中学",
                        "马鞍山第一中学 ",
                        "马鞍山市丹阳中学",
                        "马鞍山市含山县环峰第四小学",
                        "合肥市和平小学",
                        "安徽省合肥市习友小学"
        };
        for(String str:schoolList)
        {
            int count=webSpiderDao.count(str);
            if(count==0)
            {
                //不存在，单独记录
                leftSchool.add(str);
            }
            else
            {
                webSpiderDao.setTestSchool(str);
            }
        }
        System.out.println(leftSchool);
    }

    public void addSchool()
    {
        //String schoolName,int type,ObjectId province,ObjectId city,ObjectId country,String icon,
        //int teacherNum,int classNum
        WebSpiderSchool school=new WebSpiderSchool("安庆市第四中学",12,provinceId,new ObjectId("55934c15f6f28b7261c19c89"),new ObjectId("56e8fbcfd5bf6a0f584283ce"),"",0,0,1);
        //webSpiderDao.save(school);
        school=new WebSpiderSchool("马鞍山二十二中",12,provinceId,new ObjectId("55934c14f6f28b7261c19c5f"),new ObjectId("56e8fbcfd5bf6a0f584283c1"),"",0,0,1);
        webSpiderDao.save(school);
    }
    public void setAllTest()
    {
        webSpiderDao.setAllTest();
    }
}
