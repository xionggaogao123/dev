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
 * collectionName:classOverallQualityScore
 * </pre>
 * <pre>
 {
 si:学校ID
 gi:年级ID
 ci:班级ID
 ui:用户ID
 ct:总形象币数量
 hct:隐藏总形象币数量
 coqs:分值信息 详见ClassOverallQualityScore
 }
 * Created by guojing on 2016/8/24.
 */
public class StuOverallQualityScoreEntry extends BaseDBObject {

    public StuOverallQualityScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public StuOverallQualityScoreEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            ObjectId userId,
            int currencyTotal,
            int hideCurrTotal,
            List<StuOverallQualityScore> soqsList
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("gi", gradeId)
                .append("ci", classId)
                .append("ui", userId)
                .append("ct", currencyTotal)
                .append("hct", hideCurrTotal)
                .append("soqs", MongoUtils.convert(MongoUtils.fetchDBObjectList(soqsList)));
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

    public int getCurrencyTotal() {
        return getSimpleIntegerValueDef("ct",0);
    }

    public void setCurrencyTotal(int currencyTotal) {
        setSimpleValue("ct", currencyTotal);
    }

    public int getHideCurrTotal() {
        return getSimpleIntegerValueDef("hct",0);
    }

    public void setHideCurrTotal(int hideCurrTotal) {
        setSimpleValue("hct", hideCurrTotal);
    }

    public List<StuOverallQualityScore> getSoqsList() {
        List<StuOverallQualityScore> retList =new ArrayList<StuOverallQualityScore>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("soqs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new StuOverallQualityScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setSoqsList(List<StuOverallQualityScore> soqsList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(soqsList);
        setSimpleValue("soqs", MongoUtils.convert(list));
    }
}
