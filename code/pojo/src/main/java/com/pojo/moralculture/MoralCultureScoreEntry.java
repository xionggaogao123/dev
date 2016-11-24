package com.pojo.moralculture;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 德育项目成绩
 * <pre>
 * collectionName:moralCultureScore
 * </pre>
 * <pre>
 {
     ui:用户id
     si:学校ID
     gid:年级ID
     cid:班级ID
     sei:学期id
     mcsis[{

     }]成绩
     st:是否删除
 }
 * </pre>
 * @author jing.guo
 */
public class MoralCultureScoreEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936856358592747487L;

    public MoralCultureScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MoralCultureScoreEntry(
            ObjectId userId,
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            String semesterId,
            List<MoralCultureScoreInfo> mcsiList
    ) {
        this(
                userId,
                schoolId,
                gradeId,
                classId,
                semesterId,
                mcsiList,
                DeleteState.NORMAL
        );
    }

    public MoralCultureScoreEntry(
            ObjectId userId,
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            String semesterId,
            List<MoralCultureScoreInfo> mcsiList,
            DeleteState ds
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ui",userId)
                .append("si", schoolId)
                .append("gid", gradeId)
                .append("cid", classId)
                .append("sei", semesterId)
                .append("mcsis", MongoUtils.convert(MongoUtils.fetchDBObjectList(mcsiList)))
                .append("st", ds.getState());
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid", classId);
    }

    public String getSemesterId() {
        return getSimpleStringValue("sei");
    }

    public void setSemesterId(String semesterId) {
        setSimpleValue("sei", semesterId);
    }

    public List<MoralCultureScoreInfo> getMcsiList() {
        List<MoralCultureScoreInfo> retList =new ArrayList<MoralCultureScoreInfo>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("mcsis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new MoralCultureScoreInfo((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setMcsiList(List<MoralCultureScoreInfo> mcsiList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(mcsiList);
        setSimpleValue("mcsis", MongoUtils.convert(list));
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
