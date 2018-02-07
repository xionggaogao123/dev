package com.pojo.jiaschool;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/6.
 *  Id
 schoolId        	         学校id                   sid
 userId                     用户id                   uid
 appIdList           		appId集合                alt
 */
public class SchoolAppEntry extends BaseDBObject {
    public SchoolAppEntry(){

    }
    public SchoolAppEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public SchoolAppEntry(
            ObjectId schoolId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SchoolAppEntry(
            ObjectId id,
            ObjectId schoolId,
            List<ObjectId> appIdList
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("sid", schoolId)
                .append("alt", appIdList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
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
