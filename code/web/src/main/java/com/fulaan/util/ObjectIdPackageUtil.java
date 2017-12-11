package com.fulaan.util;

import com.db.indexPage.WebHomePageDao;
import com.db.user.GenerateUserCodeDao;
import com.pojo.appnotice.GenerateUserCodeEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2016/12/23.
 */
public class ObjectIdPackageUtil {
    public static void main(String[] args){
        ObjectId id=new ObjectId("579961cbde04cb783df3074f");
        System.out.println(id.toString());
        System.out.println(getPackage(id));
//        System.out.println(removePackage("1469669835"));
    }

    public static String getPackage(ObjectId id){
//        long time=System.currentTimeMillis();
//        long idTime=id.getTime();
//        long total=idTime+time;
//        return String.valueOf(total/1000L);
        GenerateUserCodeDao generateUserCodeDao=new GenerateUserCodeDao();
        String seqId;
        GenerateUserCodeEntry entry=generateUserCodeDao.getCodeEntry();
        if(null!=entry) {
            seqId=String.valueOf(entry.getSeqId());
        }else{
            GenerateUserCodeEntry lastEntry=generateUserCodeDao.findLastEntry();
            long sId=500500L;
            if(null!=lastEntry) {
                sId = lastEntry.getSeqId() + 1L;
                seqId = String.valueOf(sId);
            }else{
                seqId = String.valueOf(sId);
            }
            GenerateUserCodeEntry codeEntry = new GenerateUserCodeEntry(sId);
            codeEntry.setRemove(Constant.ONE);
            generateUserCodeDao.saveEntry(codeEntry);
        }
        return seqId;
    }

    public static ObjectId removePackage(String id){
        long dateTime=Long.parseLong(id);
        long time=dateTime*1000L;
        System.out.println(time);
        return new ObjectId(new Date(time));
    }
}
