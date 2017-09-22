package com.pojo.appnotice;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/22.
 * {
 *
 * }
 */
public class AppNoticeEntry extends BaseDBObject{

    public AppNoticeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppNoticeEntry(
            ObjectId userId,
            String subject,
            String title,
            String content,
            List<ObjectId> communityIds,
            int watchPermission,
            List<AttachmentEntry> voiceList,
            List<AttachmentEntry> imageList){
       BasicDBObject basicDBObject=new BasicDBObject()
               .append("uid",userId)
               .append("su",subject)
               .append("tl",title)
               .append("ti",System.currentTimeMillis())
               .append("cn",content)
               .append("rl", MongoUtils.convert(new ArrayList<ObjectId>()))
               .append("cc", Constant.ZERO)
               .append("cms",communityIds)
               .append("wp",watchPermission)
               .append("vl",MongoUtils.fetchDBObjectList(voiceList))
               .append("il",MongoUtils.fetchDBObjectList(imageList))
               .append("ir",Constant.ZERO);
        setBaseEntry(basicDBObject);
    }



    public void setSubject(String subject){
        setSimpleValue("su",subject);
    }

    public String getSubject(){
        return getSimpleStringValue("su");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
}
