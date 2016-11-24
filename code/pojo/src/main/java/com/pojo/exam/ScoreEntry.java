package com.pojo.exam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Caocui on 2015/7/22.
 * 考试信息Entry
 * 对应数据库集合 Constant.COLLECTION_SCORE="performance"
 * 考试编码:exId
 * 学生编码:stuId
 * 学生名称:stuNm
 * 班级编码:cid
 * 各学科成绩:sList
 * 班级名称:cna
 * 考场编号:rid
 * 学生考号:enm
 * 考场名称:rna
 * 考试成绩：es
 * 总成绩:suc
 * 考场编码:ern
 * 区域联考id:aid 2015/10/27 加入
 * 学校排名:sr
 * 区域排名:ar
 */
public class ScoreEntry extends BaseDBObject {
    public ScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ScoreEntry(ObjectId examId, ObjectId studentId, String studentName, ObjectId classId,
                      String className, ObjectId examRoomId, String examRoomName, List<SubjectScoreEntry> examScore,String examNum,String examRoomNumber,
                      ObjectId areaExamId, int schoolRanking, int aeraRanking) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("exId", examId)
                .append("stuId", studentId)
                .append("stuNm", studentName)
                .append("cId", classId)
                .append("cna", className)
                .append("rid", examRoomId)
                .append("rna", examRoomName)
                .append("enm", examNum)
                .append("suc", 0)
                .append("ern", examRoomNumber)
                .append("sList", MongoUtils.convert(MongoUtils.fetchDBObjectList(examScore)))
                .append("aid", areaExamId)
                .append("sr", schoolRanking)
                .append("ar", aeraRanking);
        setBaseEntry(baseEntry);
    }
    
    public ScoreEntry(ObjectId examId, ObjectId studentId, String studentName, ObjectId classId,
            String className, ObjectId examRoomId, String examRoomName, List<SubjectScoreEntry> examScore,String examNum,String examRoomNumber) {
    	super();
    	BasicDBObject baseEntry = new BasicDBObject()
    		.append("exId", examId)
      		.append("stuId", studentId)
      		.append("stuNm", studentName)
      		.append("cId", classId)
      		.append("cna", className)
      		.append("rid", examRoomId)
      		.append("rna", examRoomName)
      		.append("enm", examNum)
      		.append("suc", 0)
      		.append("ern", examRoomNumber)
      		.append("sList", MongoUtils.convert(MongoUtils.fetchDBObjectList(examScore)));
    	setBaseEntry(baseEntry);
    }

    public void setExamRoomNumber(String examRoomNumber) {
        setSimpleValue("ern", examRoomNumber);
    }

    public String getExamRoomNumber() {
        return getSimpleStringValue("ern");
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exId");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exId", examId);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stuId");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stuId", studentId);
    }

    public void setStudentName(String studentName) {
        setSimpleValue("stuNm", studentName);
    }

    public String getStudentName() {
        return getSimpleStringValue("stuNm");
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cId");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cId", classId);
    }

    public void setClassName(String className) {
        setSimpleValue("cna", className);
    }

    public String getClassName() {
        return getSimpleStringValue("cna");
    }

    public ObjectId getExamRoomId() {
        return getSimpleObjecIDValue("rid");
    }

    public void setExamRoomId(ObjectId examRoomId) {
        setSimpleValue("rid", examRoomId);
    }

    public void setExamRoomName(String examRoomName) {
        setSimpleValue("rna", examRoomName);
    }

    public String getExamRoomName() {
        return getSimpleStringValue("rna");
    }

    public String getExamNum() {
        return getSimpleStringValue("enm");
    }

    public void setExamNum(String examNum) {
        setSimpleValue("enm", examNum);
    }

    public double getScoreSum() {
        if(getBaseEntry().containsField("suc")){
            return getSimpleDoubleValue("suc");
        } else {
            return 0;
        }

    }

    public void setScoreSum(double scoreSum) {
        setSimpleValue("suc", scoreSum);
    }
//加入
    public ObjectId getAreaExamId() {
        return getSimpleObjecIDValue("aid");
    }

    public void setAreaExamId(ObjectId areaExamId) {
        setSimpleValue("aid", areaExamId);
    }
    
    public int getSchoolRanking(){
    	return getSimpleIntegerValue("sr");
    }
    
    public void setchoolRanking(int schoolRanking){
    	setSimpleValue("sr",schoolRanking);
    }
    
    public int getAeraRanking(){
    	return getSimpleIntegerValue("ar");
    }
    
    public void setAeraRanking(int aeraRanking){
    	setSimpleValue("ar",aeraRanking);
    }
    
    public List<SubjectScoreEntry> getExamScore() {
        BasicDBList list = (BasicDBList) getSimpleObjectValue("sList");
        List<SubjectScoreEntry> retList;
        if (null != list && !list.isEmpty()) {
            retList = new ArrayList<SubjectScoreEntry>(list.size());
            for (Object o : list) {
                retList.add(new SubjectScoreEntry((BasicDBObject) o));
            }
        } else {
            retList = Collections.EMPTY_LIST;
        }
        return retList;
    }

    public void setExamScore(List<IdValuePair> examScore) {
        this.setSimpleValue("sList", MongoUtils.convert(examScore));
    }
}
