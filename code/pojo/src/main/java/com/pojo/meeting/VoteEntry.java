package com.pojo.meeting;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/8/13.
 */
public class VoteEntry  extends BaseDBObject {

    private static final long serialVersionUID = -567032340897885778L;

    public VoteEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public VoteEntry(ObjectId meetId,ObjectId userId,long time,String name,List<ChooseEntry> chooseList) {
        super();
        List<DBObject> list = MongoUtils.fetchDBObjectList(chooseList);
        BasicDBObject baseEntry =new BasicDBObject()
                .append("mid",meetId)
                .append("ui",userId)
                .append("dt",time)
                .append("nm", name)
                .append("csl", list);
        setBaseEntry(baseEntry);
    }

    public ObjectId getMeetId() {
        return getSimpleObjecIDValue("mid");
    }
    public void setMeetId(ObjectId meetId) {
        setSimpleValue("mid", meetId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public long getTime() {
        return getSimpleLongValue("dt");
    }
    public void setTime(long time) {
        setSimpleValue("dt",time);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }
    public List<ChooseEntry> getChooseList() {
        List<ChooseEntry> chooseList =new ArrayList<ChooseEntry>();

        BasicDBList list =(BasicDBList)getSimpleObjectValue("csl");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                chooseList.add(  new ChooseEntry((BasicDBObject)o));
            }
        }
        return chooseList;
    }

    public void setChooseList(List<ChooseEntry> chooseList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(chooseList);
        setSimpleValue("csl",  MongoUtils.convert(list));
    }
}
