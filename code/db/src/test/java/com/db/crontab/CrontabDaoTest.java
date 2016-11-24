package com.db.crontab;

import com.mongodb.DBObject;
import com.pojo.crontab.CrontabEntry;
import org.junit.Test;

/**
 * Created by fl on 2016/3/18.
 */
public class CrontabDaoTest {
    private CrontabDao crontabDao = new CrontabDao();
    private String name = "interestclassnewterm";

    @Test
    public void testAdd(){
        CrontabEntry entry1 = crontabDao.getCrontabEntry(name);
        if(entry1 == null){
            CrontabEntry crontabEntry = new CrontabEntry("interestclassnewterm", 1);
            crontabDao.add(crontabEntry);
        }

    }

    @Test
    public void TestUpdate(){

        CrontabEntry entry1 = crontabDao.getCrontabEntry(name);
        CrontabEntry entry2 = crontabDao.getCrontabEntry(name);
        CrontabEntry dbObject1 = crontabDao.update(name, entry1.getVersion() + 1);
        CrontabEntry dbObject2 = crontabDao.update(name, entry2.getVersion() + 1);
        if(entry1.getVersion() == dbObject1.getVersion()){
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        if(entry2.getVersion() == dbObject2.getVersion()){
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
//        System.out.println(dbObject1.toString());
//        System.out.println(dbObject2.toString());
    }
}
