package com.pojo.appmarket;

import com.mongodb.BasicDBList;
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
 * Created by scott on 2017/10/10.
 */
public class AppDetailEntry extends BaseDBObject{

    public AppDetailEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppDetailEntry(ObjectId id,
                          String appPackageName,
                          String logo,
                          int type,
                          long size,
                          List<AttachmentEntry> imageList,
                          String version,
                          String description,
                          String appName,
                          String url){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("apn",appPackageName)
                .append("lg",logo)
                .append("ty",type)
                .append("sz",size)
                .append("ur",url)
                .append("an",appName)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vs",version)
                .append("des",description)
                .append("ti",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public AppDetailEntry(String appPackageName,
                          String logo,
                          int type,
                          long size,
                          List<AttachmentEntry> imageList,
                          String version,
                          String description,
                          String appName,
                          String url){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("apn",appPackageName)
                .append("lg",logo)
                .append("ty",type)
                .append("sz",size)
                .append("ur",url)
                .append("an",appName)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vs",version)
                .append("des",description)
                .append("ti",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setAppName(String appName){
        setSimpleValue("an",appName);
    }

    public String getAppName(){
        return getSimpleStringValue("an");
    }

    public void setUrl(String url){
        setSimpleValue("ur",url);
    }


    public String getUrl(){
        return getSimpleStringValue("ur");
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }

    public String getVersion(){
        return getSimpleStringValue("vs");
    }

    public void setVersion(String version){
        setSimpleValue("vs",version);
    }


    public void setImageList(List<AttachmentEntry> imageList){
        setSimpleValue("il",MongoUtils.fetchDBObjectList(imageList));
    }

    public List<AttachmentEntry> getImageList(){
        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        BasicDBList list=getDbList("il");
        for(Object o:list){
            attachmentEntries.add(new AttachmentEntry((DBObject)o));
        }
        return attachmentEntries;
    }

    public long getSize(){
        return getSimpleLongValue("sz");
    }

    public void setSize(long size){
        setSimpleValue("sz",size);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public String getLogo(){
        return getSimpleStringValue("lg");
    }

    public void setLogo(String logo){
        setSimpleValue("lg",logo);
    }

    public String getAppPackageName(){
        return getSimpleStringValue("apn");
    }

    public void setAppPackageName(String appPackageName){
        setSimpleValue("apn",appPackageName);
    }


}
