package com.pojo.controlphone;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区推荐应用表
 * Created by James on 2017/11/6.
 Id
 communityId           	    社区id                   cid
 communityName              社区名称                 cnm
 userId                     用户id                   uid
 appIdList           		appId集合                alt
 */
public class ControlAppEntry extends BaseDBObject {

    public ControlAppEntry(){

    }
    public ControlAppEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlAppEntry(
            ObjectId communityId,
            String communityName,
            ObjectId userId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("cid", communityId)
                .append("cnm",communityName)
                .append("uid",userId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlAppEntry(
            ObjectId id,
            ObjectId communityId,
            String communityName,
            ObjectId userId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("cid", communityId)
                .append("cnm",communityName)
                .append("uid",userId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public String getCommunityName(){
        return getSimpleStringValue("cnm");
    }

    public void setCommunityName(String communityName){
        setSimpleValue("cnm",communityName);
    }


    public void setAppIdList(List<ObjectId> appIdList){
        setSimpleValue("alt", MongoUtils.convert(appIdList));
    }

    public List<ObjectId> getAppIdList(){
        ArrayList<ObjectId> appIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("alt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                appIdList.add((ObjectId)obj);
            }
        }
        return appIdList;
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
