package com.fulaan.testDomainHandle.service;

import com.db.testDomainHandle.TestDomainHandleDao;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestDomainHandleService {

    private TestDomainHandleDao testDomainHandleDao =new TestDomainHandleDao();

    public void handleTableTestDomain(String dbName, String tableName, String selField, String childField) {
        Map<String, String> doMainMap = new HashMap<String, String>();
        doMainMap.put("items", "itempool.k6kt.com");
        doMainMap.put("docs", "doc.k6kt.com");
        doMainMap.put("videos", "video.k6kt.com");
        doMainMap.put("userVideos", "uservideo.k6kt.com");
        doMainMap.put("clouds", "cloud.k6kt.com");
        doMainMap.put("stores", "store.k6kt.com");
        doMainMap.put("statics", "static.k6kt.com");

        Map<String, List<String>> doMainListMap = new HashMap<String, List<String>>();
        List<String> items = new ArrayList<String>();
        items.add("7xj25c.com1.z0.glb.clouddn.com");
        doMainListMap.put("items", items);

        List<String> docs = new ArrayList<String>();
        docs.add("7xiclj.com1.z0.glb.clouddn.com");
        doMainListMap.put("docs", docs);

        List<String> videos = new ArrayList<String>();
        videos.add("7sbnym.com1.z0.glb.clouddn.com");
        videos.add("7sbnym.com2.z0.glb.clouddn.com");
        videos.add("7sbnym.com2.z0.glb.qiniucdn.com");
        videos.add("k6kt-video.qiniudn.com");
        doMainListMap.put("videos", videos);

        List<String> userVideos = new ArrayList<String>();
        userVideos.add("7sbow5.com2.z0.glb.qiniucdn.com");
        userVideos.add("7sbow5.com2.z0.glb.clouddn.com");
        userVideos.add("k6kt-hls.qiniudn.com");
        doMainListMap.put("userVideos", userVideos);

        List<String> clouds = new ArrayList<String>();
        clouds.add("7sbrbm.com1.z0.glb.clouddn.com");
        clouds.add("7sbrbm.com2.z0.glb.clouddn.com");
        clouds.add("7sbrbm.com2.z0.glb.qiniucdn.com");
        doMainListMap.put("clouds", clouds);

        List<String> stores = new ArrayList<String>();
        stores.add("7sbrbl.com1.z0.glb.clouddn.com");
        stores.add("7sbrbl.com2.z0.glb.clouddn.com");
        stores.add("7sbrbl.com2.z0.glb.qiniucdn.com");
        doMainListMap.put("stores", stores);

        List<String> statics = new ArrayList<String>();
        statics.add("7xo8rc.com1.z0.glb.clouddn.com");
        doMainListMap.put("statics", statics);

        int totalCount = testDomainHandleDao.getDBObjectCount(dbName, tableName, selField, childField);
        int pageSize = 10000;
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        System.out.println("开始更新");
        System.out.println("更新总数量"+totalCount+";更新总页数"+pageCount);
        for(int i = 1; i<=pageCount; i++) {
            int skip = i < 1 ? 0 : ((i - 1) * pageSize);
            List<DBObject> list = testDomainHandleDao.getDBObjectList(dbName, tableName, selField, childField, skip, pageSize);
            if (null != list && !list.isEmpty()) {
                String pubReUri = "";
                String pubDoMain = "";
                for (DBObject dbo : list) {
                    ObjectId id = new ObjectId(dbo.get("_id").toString());
                    Object fieldObj = dbo.get(selField);
                    if (fieldObj == null) {
                        continue;
                    }
                    if (fieldObj instanceof String) {
                        String value = (String) fieldObj;
                        if(!"".equals(pubReUri)&&!"".equals(pubDoMain)&&value.indexOf(pubReUri)>0){
                            value = value.replace(pubReUri, pubDoMain);
                            testDomainHandleDao.updField(dbName, tableName, id, selField, value);
                        }else {
                            for (Map.Entry<String, List<String>> entry : doMainListMap.entrySet()) {
                                boolean finished = false;
                                String doMain = doMainMap.get(entry.getKey());
                                for (String uri : entry.getValue()) {
                                    if (value.indexOf(uri) > 0) {
                                        value = value.replace(uri, doMain);
                                        testDomainHandleDao.updField(dbName, tableName, id, selField, value);
                                        pubReUri = uri;
                                        pubDoMain = doMain;
                                        finished = true;
                                        break;
                                    }
                                }
                                if (finished) {
                                    break;
                                }
                            }
                        }
                    } else {
                        List<DBObject> fieldObjs = fieldObj == null ? new ArrayList<DBObject>() : (List<DBObject>) fieldObj;
                        for(DBObject child :fieldObjs){
                            String value = (String)child.get(childField);
                            if(!"".equals(pubReUri)&&!"".equals(pubDoMain)&&value.indexOf(pubReUri)>0){
                                value = value.replace(pubReUri, pubDoMain);
                                child.put(childField, value);
                            }else {
                                for (Map.Entry<String, List<String>> entry : doMainListMap.entrySet()) {
                                    boolean finished = false;
                                    String doMain = doMainMap.get(entry.getKey());
                                    for (String uri : entry.getValue()) {
                                        if (value.indexOf(uri) > 0) {
                                            value = value.replace(uri, doMain);
                                            child.put(childField, value);
                                            pubReUri = uri;
                                            pubDoMain = doMain;
                                            finished = true;
                                            break;
                                        }
                                    }
                                    if (finished) {
                                        break;
                                    }
                                }
                            }
                        }
                        if(fieldObjs.size()>0) {
                            testDomainHandleDao.updField(dbName, tableName, id, selField, fieldObjs);
                        }
                    }
                }
            }
            if(i<pageCount) {
                System.out.println("剩余数量" + (totalCount - (pageSize * i)));
            }
        }
        System.out.println("结束更新");
    }

    public void handleTableTestDomain2(String dbName, String tableName, String selField, String childField) {
        int totalCount = testDomainHandleDao.getDBObjectCount(dbName, tableName, selField, childField);
        int pageSize = 10000;
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        System.out.println("开始更新");
        System.out.println("更新总数量"+totalCount+";更新总页数"+pageCount);
        for(int i = 1; i<=pageCount; i++) {
            int skip = i < 1 ? 0 : ((i - 1) * pageSize);
            List<DBObject> list = testDomainHandleDao.getDBObjectList(dbName, tableName, selField, childField, skip, pageSize);
            if (null != list && !list.isEmpty()) {
                String pubReUri = "cloud.k6kt.com";
                String pubDoMain = "video.k6kt.com";
                for (DBObject dbo : list) {
                    ObjectId id = new ObjectId(dbo.get("_id").toString());
                    Object fieldObj = dbo.get(selField);
                    if (fieldObj == null) {
                        continue;
                    }
                    if (fieldObj instanceof String) {
                        String value = (String) fieldObj;
                        value = value.replace(pubReUri, pubDoMain);
                        testDomainHandleDao.updField(dbName, tableName, id, selField, value);
                    }
                }
            }
            if(i<pageCount) {
                System.out.println("剩余数量" + (totalCount - (pageSize * i)));
            }
        }
        System.out.println("结束更新");
    }
}
