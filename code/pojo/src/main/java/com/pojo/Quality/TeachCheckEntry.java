package com.pojo.Quality;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/10.
 */
public class TeachCheckEntry extends BaseDBObject {
    private static final long serialVersionUID = -3802617104052188060L;

    public TeachCheckEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public TeachCheckEntry(String term,ObjectId teacherId,ObjectId subjectId,List<TeachProjectEntry> teachProjectList) {
        super();
        List<DBObject> list = MongoUtils.fetchDBObjectList(teachProjectList);
        BasicDBObject dbo =new BasicDBObject()
                .append("term", term)
                .append("tid", teacherId)
                .append("sjid",subjectId)
                .append("ct", System.currentTimeMillis())
                .append("tps", MongoUtils.convert(list))
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getTerm() {
        return getSimpleStringValue("term");
    }
    public void setTerm(String term) {
        setSimpleValue("term", term);
    }
    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }
    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }
    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("sjid");
    }
    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("sjid", subjectId);
    }
    public long getLastTime() {
        return getSimpleLongValue("ct");
    }
    public void setLastTime(long lastTime) {
        setSimpleValue("ct",lastTime);
    }
    public List<TeachProjectEntry> getTeachProjectList() {
        List<TeachProjectEntry> teachProjectList =new ArrayList<TeachProjectEntry>();

        BasicDBList list =(BasicDBList)getSimpleObjectValue("tps");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                teachProjectList.add(new TeachProjectEntry((BasicDBObject)o));
            }
        }
        return teachProjectList;
    }

    public void setTeachProjectList(List<TeachProjectEntry> teachProjectList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(teachProjectList);
        setSimpleValue("tps",  MongoUtils.convert(list));
    }

}
