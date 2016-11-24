package com.db.zouban;

import com.pojo.zouban.XuankeConfEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class XuanKeConfDaoTest {

    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    @Test
    public void testAddXuanKeConf() throws Exception {
        /*String dt1="2015/07/10 00:00:00";
        String dt2="2015/10/01 00:00:00";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt_from = null;
        Date dt_end = null;
        try {
            dt_from = sdf.parse(dt1);
            dt_end=sdf.parse(dt2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        long startTime = dt_from.getTime();
        long endTime = dt_end.getTime();
        XuankeConfEntry xuankeEntry=new XuankeConfEntry(new ObjectId("55934c14f6f28b7261c19c62"),
                "2015-2016学年第一学期",
                new ObjectId("560b52087f967eec518f136c"),
                3,3,startTime,endTime,0,0,0);
        xuanKeConfDao.addXuanKeConf(xuankeEntry);*/
    }
}