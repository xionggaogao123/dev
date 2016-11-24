package com.pojo.dangjian;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**党建资源表
 * Created by fl on 2016/3/23.
 * nm: 名称
 * srcs: 资源地址列表
 * di：资源目录
 * tm：学期
 * ui: 上传用户
 * st：状态  0正常 1已删除
 */
public class PartyResourceEntry extends BaseDBObject {

    public PartyResourceEntry(){}

    public PartyResourceEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public PartyResourceEntry(String name, List<IdNameValuePair> srcs, ObjectId directoryId, String term, ObjectId userId, int state){
        List<DBObject> srcDBList =MongoUtils.fetchDBObjectList(srcs);

        BasicDBObject baseEntry = new BasicDBObject()
                .append("nm", name)
                .append("srcs", MongoUtils.convert(srcDBList))
                .append("di", directoryId)
                .append("tm", term)
                .append("ui", userId)
                .append("st", state)
                ;
        setBaseEntry(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm", name);
    }

    public List<IdNameValuePair> getSrcs(){
        List<IdNameValuePair> srcs = new ArrayList<IdNameValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("srcs");
        if(list != null){
            for(Object o:list) {
                srcs.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return srcs;
    }

    public void setSrcs(List<IdNameValuePair> srcs){
        List<DBObject> srcDBList =MongoUtils.fetchDBObjectList(srcs);
        setSimpleValue("srcs", MongoUtils.convert(srcDBList));
    }

    public ObjectId getDirectoryId(){
        return getSimpleObjecIDValue("di");
    }

    public void setDirectoryId(ObjectId directoryId){
        setSimpleValue("di", directoryId);
    }

    public String getTerm(){
        return getSimpleStringValue("tm");
    }

    public void setTerm(String term){
        setSimpleValue("tm", term);
    }

    public int getState(){
        return getSimpleIntegerValue("st");
    }

    public void setState(int state){
        setSimpleValue("st", state);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("ui", userId);
    }
}
