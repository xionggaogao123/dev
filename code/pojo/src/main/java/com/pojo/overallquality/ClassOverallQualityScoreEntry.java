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
 ts:总分
 coqs:分值信息 详见ClassOverallQualityScore
 }
 * Created by guojing on 2016/8/24.
 */
public class ClassOverallQualityScoreEntry extends BaseDBObject {

    public ClassOverallQualityScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassOverallQualityScoreEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            int totalScore,
            int baseCampCount,
            List<ClassOverallQualityScore> coqsList
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("gi", gradeId)
                .append("ci", classId)
                .append("ts", totalScore)
                .append("bcc", baseCampCount)
                .append("coqs", MongoUtils.convert(MongoUtils.fetchDBObjectList(coqsList)));
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

    public int getTotalScore() {
        return getSimpleIntegerValueDef("ts", 0);
    }

    public void setTotalScore(int totalScore) {
        setSimpleValue("ts",totalScore);
    }

    public int getBaseCampCount() {
        return getSimpleIntegerValueDef("bcc", 1);
    }

    public void setBaseCampCount(int baseCampCount) {
        setSimpleValue("bcc",baseCampCount);
    }

    public List<ClassOverallQualityScore> getCoqsList() {
        List<ClassOverallQualityScore> retList =new ArrayList<ClassOverallQualityScore>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("coqs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new ClassOverallQualityScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setCoqsList(List<ClassOverallQualityScore> coqsList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(coqsList);
        setSimpleValue("coqs", MongoUtils.convert(list));
    }

    public int getCastleCount(){
        return getSimpleIntegerValueDef("cc", 0);
    }

    public void setCastleCount(int castleCount){
        setSimpleValue("cc",castleCount);
    }

    public int getVillagerCount(){
        return getSimpleIntegerValueDef("vc", 0);
    }

    public void setVillagerCount(int villagerCount){
        setSimpleValue("vc",villagerCount);
    }

    public int getSoldiersCount(){
        return getSimpleIntegerValueDef("soc", 0);
    }

    public void setSoldiersCount(int soldiersCount){
        setSimpleValue("soc",soldiersCount);
    }
}
