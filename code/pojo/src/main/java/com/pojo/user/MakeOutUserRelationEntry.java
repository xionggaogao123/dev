package com.pojo.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/13.
 * 填写用户关系表
 */
public class MakeOutUserRelationEntry extends BaseDBObject{

    public MakeOutUserRelationEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public MakeOutUserRelationEntry(ObjectId parentId,
                                    List<String> userKeys){
        BasicDBObject basicDBObject  =new BasicDBObject()
                .append("pid",parentId)
                .append("uks", MongoUtils.convert(userKeys));
        setBaseEntry(basicDBObject);
    }

    public void setUserKeys(List<String> userKeys){
        setSimpleValue("uks",MongoUtils.convert(userKeys));
    }

    public List<String> getUserKeys(){
        List<String> userKeys=new ArrayList<String>();
        BasicDBList list=(BasicDBList)getSimpleObjectValue("uks");
        if(null!=list&&!list.isEmpty()){
            for(Object o:list){
                userKeys.add((String)o);
            }
        }
        return userKeys;
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
}
