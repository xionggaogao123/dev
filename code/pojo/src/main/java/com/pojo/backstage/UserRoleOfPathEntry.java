package com.pojo.backstage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/29.
 */
public class UserRoleOfPathEntry extends BaseDBObject{

    public UserRoleOfPathEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public UserRoleOfPathEntry(List<String> paths,
                                int role){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("rl",role)
                .append("ps", MongoUtils.convert(paths))
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setPaths(List<String> paths){
        setSimpleValue("ps",paths);
    }

    public List<String> getPaths(){
        List<String> paths=new ArrayList<String>();
        BasicDBList list=(BasicDBList)getSimpleObjectValue("ps");
        if(null!=list&&!list.isEmpty()){
            for(Object o:list){
                paths.add((String)o);
            }
        }
        return paths;
    }

    public void setRole(int role){
        setSimpleValue("rl",role);
    }

    public int getRole(){
        return getSimpleIntegerValue("rl");
    }
}
