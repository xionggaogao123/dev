package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 选课配置
 * {
 * xkid:选课id--------------->xuanKeId
 * uid:学生id---------------->studentId
 * advls:合格考科目------------------->advancelist
 * sipls:等级考科目---------------->simplelist
 * cid:班级id---------------->classId
 * um:学生名称----------------->userName
 * isx:是否选课----------------->isXuan
 * }
 * Created by wang_xinxin on 2015/9/24.
 */
public class StudentChooseEntry extends BaseDBObject {

    public StudentChooseEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public StudentChooseEntry() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("xkid", null)
                .append("uid", null)
                .append("advls", new BasicDBList())
                .append("sipls", new BasicDBList())
                .append("um","")
                .append("cid", null)
                .append("isx",0);
        setBaseEntry(basicDBObject);
    }


    public StudentChooseEntry(ObjectId xuanKeId,ObjectId userId,List<ObjectId> advancelist,List<ObjectId> simplelist,String userName,ObjectId classId,int isXuan) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("xkid",xuanKeId)
                .append("uid", userId)
                .append("advls", MongoUtils.convert(advancelist))
                .append("sipls", MongoUtils.convert(simplelist))
                .append("um", userName)
                .append("cid", classId)
                .append("isx",isXuan);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public List<ObjectId> getAdvancelist() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("advls");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setAdvancelist(List<ObjectId> advancelist) {
        setSimpleValue("advls", MongoUtils.convert(advancelist));
    }

    public List<ObjectId> getSimplelist() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("sipls");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setSimplelist(List<ObjectId> simplelist) {
        setSimpleValue("sipls", MongoUtils.convert(simplelist));
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid", classId);
    }

    public ObjectId getXuanKeId() {
        return getSimpleObjecIDValue("xkid");
    }

    public void setXuanKeId(ObjectId xuanKeId) {
        setSimpleValue("xkid", xuanKeId);
    }

    public String getUserName() {
        return getSimpleStringValue("um");
    }

    public void setUserName(String userName) {
        setSimpleValue("um", userName);
    }

    public int getIsXuan() {
        return getSimpleIntegerValue("isx");
    }

    public void setIsXuan(int isXuan) {
        setSimpleValue("isx", isXuan);
    }
}
