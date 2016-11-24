package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 分层
 * {
 * xkid:选课id
 * grpid:分段id
 * scid:学科配置Id
 * type:类型  等级考1，合格考2
 * fcim:分层数据 fencengItem
 * }
 * Created by wang_xinxin on 2015/10/14.
 */
public class FenCengEntry extends BaseDBObject {

    public FenCengEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public FenCengEntry(ObjectId xuanKeId,ObjectId groupId,ObjectId subjectConfId,int type,List<FenCengItem> fenCengItemList) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("xkid", xuanKeId)
                .append("grpid",groupId)
                .append("scid", subjectConfId)
                .append("type",type)
                .append("fcim", MongoUtils.convert(MongoUtils.fetchDBObjectList(fenCengItemList)));
        setBaseEntry(baseEntry);
    }
    public ObjectId getXuanKeId() {
        return getSimpleObjecIDValue("xkid");
    }
    public void setXuanKeId(ObjectId xuanKeId) {
        setSimpleValue("xkid",xuanKeId);
    }
    public ObjectId getGroupId() {
        return getSimpleObjecIDValue("grpid");
    }
    public void setGroupId(ObjectId groupId) {
        setSimpleValue("grpid",groupId);
    }
    public ObjectId getSubjectConfId() {
        return getSimpleObjecIDValue("scid");
    }
    public void setSubjectConfId(ObjectId groupId) {
        setSimpleValue("scid",groupId);
    }
    public int getType() {
        return getSimpleIntegerValue("type");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }

    public List<FenCengItem> getFenCengItemList() {
        List<FenCengItem> retList =new ArrayList<FenCengItem>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("fcim");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new FenCengItem((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setFenCengItemList(List<FenCengItem> fenCengItemList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(fenCengItemList);
        setSimpleValue("fcim", MongoUtils.convert(list));

    }
}
