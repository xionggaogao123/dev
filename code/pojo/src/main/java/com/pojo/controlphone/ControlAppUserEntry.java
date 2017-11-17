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
 * 家长推荐应用列表
 * Created by James on 2017/11/17.
 Id
 parentId           	    父id                    cid
 userId                      子id                    cnm
 appIdList           		appId集合               alt
 */
public class ControlAppUserEntry extends BaseDBObject {
    public ControlAppUserEntry(){

    }
    public ControlAppUserEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlAppUserEntry(
            ObjectId parentId,
            ObjectId userId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid",userId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlAppUserEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid",userId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
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
