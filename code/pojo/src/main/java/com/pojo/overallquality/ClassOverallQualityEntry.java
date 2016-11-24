package com.pojo.overallquality;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *<pre>
 * collectionName:classOverallQuality
 * </pre>
 * <pre>
 {
 si:学校ID
 gi:年级ID
 ci:班级ID
 cd:日期
 oqis:分值信息 详见OverallQualityInfo
 }
 * Created by guojing on 2016/8/4.
 */
public class ClassOverallQualityEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936856358592747487L;

    public ClassOverallQualityEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassOverallQualityEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            long createDate,
            List<OverallQualityInfo> oqiList
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("gi", gradeId)
                .append("ci", classId)
                .append("cd", createDate)
                .append("oqis", MongoUtils.convert(MongoUtils.fetchDBObjectList(oqiList)));
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gi");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gi", gradeId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("ci");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("ci", classId);
    }

    public long getCreateDate() {
        return getSimpleLongValue("cd");
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("cd",createDate);
    }

    public List<OverallQualityInfo> getOqiList() {
        List<OverallQualityInfo> retList =new ArrayList<OverallQualityInfo>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("oqis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new OverallQualityInfo((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setOqiList(List<OverallQualityInfo> oqiList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(oqiList);
        setSimpleValue("oqis", MongoUtils.convert(list));
    }
}

