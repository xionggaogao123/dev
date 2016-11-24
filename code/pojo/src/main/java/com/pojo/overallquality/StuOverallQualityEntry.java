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
 * <pre>
 * collectionName:stuOverallQuality
 * </pre>
 * <pre>
 {
 si:学校ID
 gi:年级ID
 ci:班级ID
 ui:用户ID
 cd:日期
 oqis:分值信息 详见OverallQualityInfo
 }
 * Created by guojing on 2016/8/4.
 */
public class StuOverallQualityEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936856358592747487L;

    public StuOverallQualityEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public StuOverallQualityEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            ObjectId userId,
            long createDate,
            List<StuOverallQualityInfo> soqiList
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("gi", gradeId)
                .append("ci", classId)
                .append("ui", userId)
                .append("cd", createDate)
                .append("soqs", MongoUtils.convert(MongoUtils.fetchDBObjectList(soqiList)));
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

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public long getCreateDate() {
        return getSimpleLongValue("cd");
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("cd",createDate);
    }

    public List<StuOverallQualityInfo> getSoqsList() {
        List<StuOverallQualityInfo> retList =new ArrayList<StuOverallQualityInfo>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("soqs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new StuOverallQualityInfo((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setSoqsList(List<StuOverallQualityInfo> soqiList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(soqiList);
        setSimpleValue("soqs", MongoUtils.convert(list));
    }
}
