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
 * Created by James on 2017/11/30.
 */
public class ControlAppSystemEntry extends BaseDBObject {

    public ControlAppSystemEntry(){

    }
    public ControlAppSystemEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlAppSystemEntry(
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlAppSystemEntry(
            ObjectId id,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
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
