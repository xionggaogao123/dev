package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 分段
 * {
 * xkid:选课id
 * group:分段
 * grpnm:分段名-----拓展课新加
 * cids:班级list
 * }
 * Created by wang_xinxin on 2015/10/9.
 */
public class ClassFengDuanEntry extends BaseDBObject {

    public ClassFengDuanEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassFengDuanEntry(ObjectId xuanKeId,int group,List<ObjectId> classIds) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("xkid",xuanKeId)
                .append("group", group)
                .append("cids", MongoUtils.convert(classIds));
        setBaseEntry(baseEntry);
    }
    public ClassFengDuanEntry(ObjectId xuanKeId,int group,String groupName,List<ObjectId> classIds) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("xkid",xuanKeId)
                .append("group", group)
                .append("grpnm",groupName)
                .append("cids", MongoUtils.convert(classIds));
        setBaseEntry(baseEntry);
    }

    public int getGroup() {
        return getSimpleIntegerValue("group");
    }

    public void setGroup(int group) {
        setSimpleValue("group",group);
    }

    public String getGroupName()
    {
        if(this.getBaseEntry().containsField("grpnm")) {
            return getSimpleStringValue("grpnm");
        }
        return "";
    }
    public void setGroupName(String groupName)
    {
        setSimpleValue("grpnm",groupName);
    }

    public ObjectId getXuanKeId() {
        return getSimpleObjecIDValue("xkid");
    }

    public void setXuanKeId(ObjectId xuanKeId) {
        setSimpleValue("xkid",xuanKeId);
    }

    public List<ObjectId> getClassIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setClassIds(List<ObjectId> classIds) {
        setSimpleValue("cids", MongoUtils.convert(classIds));
    }
}
