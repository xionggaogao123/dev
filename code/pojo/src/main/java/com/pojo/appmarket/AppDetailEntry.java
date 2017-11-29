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
 *whiteOrBlack: 管控是是否禁用（黑名单）
 *type 0 系统 1 复兰 2 第三方
 */
public class AppDetailEntry extends BaseDBObject{

    public AppDetailEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppDetailEntry(ObjectId id,
                          String appPackageName,
                          String logo,
                          int type,
                          long appSize,
                          int versionCode,
                          int isControl,
                          int whiteOrBlack,
                          String size,
                          List<AttachmentEntry> imageList,
                          String versionName,
                          String description,
                          String appName,
                          String url,
                          String fileKey){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("apn",appPackageName)
                .append("lg",logo)
                .append("ty",type)
                .append("asz",appSize)
                .append("isc",isControl)
                .append("wob",whiteOrBlack)
                .append("ord",1)
                .append("sz", size)
                .append("ur",url)
                .append("an",appName)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vs",versionName)
                .append("vc",versionCode)
                .append("des",description)
                .append("fk",fileKey)
                .append("ti",System.currentTimeMillis())
                .append("upt",System.currentTimeMillis())
                .append("iu",Constant.ZERO)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public AppDetailEntry(String appPackageName,
                          String logo,
                          int type,
                          long appSize,
                          int versionCode,
                          int isControl,
                          int whiteOrBlack,
                          String size,
                          List<AttachmentEntry> imageList,
                          String versionName,
                          String description,
                          String appName,
                          String url,
                          String fileKey){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("apn",appPackageName)
                .append("lg", logo)
                .append("ty", type)
                .append("asz",appSize)
                .append("isc",isControl)
                .append("wob",whiteOrBlack)
                .append("ord", 1)
                .append("sz", size)
                .append("ur",url)
                .append("an",appName)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vs",versionName)
                .append("vc",versionCode)
                .append("des",description)
                .append("ti",System.currentTimeMillis())
                .append("upt",System.currentTimeMillis())
                .append("iu",Constant.ZERO)
                .append("fk",fileKey)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setFileKey(String fileKey){
        setSimpleValue("fk",fileKey);
    }

    public String getFileKey(){
        return getSimpleStringValue("fk");
    }

    public long getUpdateTime(){
        return getSimpleLongValue("upt");
    }

    public void setUpdateTime(long updateTime){
        setSimpleValue("upt",updateTime);
    }

    public int getIsUpdated(){
        return getSimpleIntegerValue("iu");
    }

    public void setIsUpdated(int isUpdated){
        setSimpleValue("iu",isUpdated);
    }

    public void setVersionCode(int versionCode){
        setSimpleValue("vc",versionCode);
    }

    public int getVersionCode(){
        return getSimpleIntegerValue("vc");
    }

    public void setAppSize(long appSize){
        setSimpleValue("asz",appSize);
    }

    public long getAppSize(){
        return getSimpleIntegerValue("asz");
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

    public String getVersionName(){
        return getSimpleStringValue("vs");
    }

    public void setVersionName(String versionName){
        setSimpleValue("vs",versionName);
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

    public String getSize(){
        return getSimpleStringValue("sz");
    }

    public void setSize(String size){
        setSimpleValue("sz",size);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getOrder(){
        return getSimpleIntegerValueDef("ord",1);
    }

    public void setOrder(int order){
        setSimpleValue("ord",order);
    }

    public int getIsControl(){
        return getSimpleIntegerValue("isc");
    }

    public void setIsControl(int isControl){
        setSimpleValue("isc",isControl);
    }
    public int getWhiteOrBlack(){
        return getSimpleIntegerValue("wob");
    }

    public void setWhiteOrBlack(int whiteOrBlack){
        setSimpleValue("wob",whiteOrBlack);
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
