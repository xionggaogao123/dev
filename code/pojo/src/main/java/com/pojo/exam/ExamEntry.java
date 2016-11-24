package com.pojo.exam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by Caocui on 2015/7/22. 
 * 考试信息Entry
 * 对应数据库集合 Constant.COLLECTION_EXAM="examresult"
 * 考试名称:name
 * 考试时间:ed
 * 年级编码:gid
 * 年级名称:gna
 * 学校编码:sid
 * 学年:schY
 * 是否为年级考试:考试来源0：老师自建1：考务管理2：考试模块3：区域联考
 * 考试类型：et  [0:期中，1:期末，2:其他,3:区域联考]
 * 说明:rm
 * 考试科目列表:es
 * 考场位置是否已经完成编排:ac   0:未完成，1：已完成
 * 考场编排是否已经锁定:lk   0:未锁定，1：锁定
 * 使用考场:ue
 * 班级编码列表：cList
 * 科目编码列表：sList
 * 是否删除:df
 * 区域联考id: eid
 * 学段：sp "int:学段
	1：小学
	2：初中
	3：高中
	4：大学"
 */
public class ExamEntry extends BaseDBObject {
    /**
     * 已删除
     */
    public static final int FLAG_DELETED = 1;
    /**
     * 正常状态
     */
    public static final int FLAG_NOT_DELETE = 0;
    public ExamEntry(){
    	super();
    }

    public ExamEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ExamEntry(String name, long examDate, ObjectId gradeId, String gradeName, int examType, String remark,
                     List<ExamSubjectEntry> examSubjectEntries, int isArrangeComplate, int isLock,
                     ObjectId schoolId, String schoolYear, List<ObjectId> classList,List<ObjectId> subjectList) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("name", name)
                .append("ed", examDate)
                .append("date", DateTimeUtils.convert(examDate,DateTimeUtils.DATE_YYYY_MM_DD))
                .append("gId", gradeId)
                .append("gna", gradeName)
                .append("et", examType)
                .append("type", ExamDTO.examTypeNames[examType])
                .append("rm", remark)
                .append("ac", isArrangeComplate)
                .append("lk", isLock)
                .append("df", FLAG_NOT_DELETE)
                .append("sId", schoolId)
                .append("schY", schoolYear)
                .append("isGra", 1)//默认
                .append("cList", classList)
                .append("sList",subjectList)
                .append("ue", null)
                .append("es", MongoUtils.convert(MongoUtils.fetchDBObjectList(examSubjectEntries)));
        setBaseEntry(baseEntry);
    }
    
    public ExamEntry(String name, long examDate, ObjectId gradeId, String gradeName, int examType, String remark,
            List<ExamSubjectEntry> examSubjectEntries, int isArrangeComplate, int isLock,
            ObjectId schoolId, String schoolYear, int isGra, List<ObjectId> classList,List<ObjectId> subjectList,
            ObjectId examId ,int studentPeriod) {
    	super();
    	BasicDBObject baseEntry = new BasicDBObject()
    		.append("name", name)
    		.append("ed", examDate)
    		.append("date", DateTimeUtils.convert(examDate,DateTimeUtils.DATE_YYYY_MM_DD))
    		.append("gId", gradeId)
    		.append("gna", gradeName)
    		.append("et", examType)
    		.append("type", ExamDTO.examTypeNames[examType])
    		.append("rm", remark)
    		.append("ac", isArrangeComplate)
    		.append("lk", isLock)
    		.append("df", FLAG_NOT_DELETE)
    		.append("sId", schoolId)
    		.append("schY", schoolYear)
    		.append("isGra", isGra)//默认
    		.append("cList", classList)
    		.append("sList",subjectList)
    		.append("ue", null)
    		.append("es", MongoUtils.convert(MongoUtils.fetchDBObjectList(examSubjectEntries)))
    		.append("eId", examId)
    		.append("sp", studentPeriod);
    	setBaseEntry(baseEntry);
}
    
    public ExamEntry(String name, long examDate, ObjectId gradeId, String gradeName, int examType, String remark,
            List<ExamSubjectEntry> examSubjectEntries, int isArrangeComplate, int isLock,
            ObjectId schoolId, String schoolYear, int isGra, List<ObjectId> classList,List<ObjectId> subjectList) {
    	super();
    	BasicDBObject baseEntry = new BasicDBObject()
    		.append("name", name)
    		.append("ed", examDate)
    		.append("date", DateTimeUtils.convert(examDate,DateTimeUtils.DATE_YYYY_MM_DD))
    		.append("gId", gradeId)
    		.append("gna", gradeName)
    		.append("et", examType)
    		.append("type", ExamDTO.examTypeNames[examType])
    		.append("rm", remark)
    		.append("ac", isArrangeComplate)
    		.append("lk", isLock)
    		.append("df", FLAG_NOT_DELETE)
    		.append("sId", schoolId)
    		.append("schY", schoolYear)
    		.append("isGra", isGra)//默认
    		.append("cList", classList)
    		.append("sList",subjectList)
    		.append("ue", null)
    		.append("es", MongoUtils.convert(MongoUtils.fetchDBObjectList(examSubjectEntries)));
    	setBaseEntry(baseEntry);
}

    public Map<String, Map<String, Object>> getRoomUsed() {
        Map<String, Map<String, Object>> rooms = null;
        BasicDBList list = (BasicDBList) getSimpleObjectValue("ue");
        if (null != list && !list.isEmpty()) {
            Map<String, Object> usedRoom;
            rooms = new HashMap<String, Map<String, Object>>(list.size());
            BasicDBObject object;
            for (Object o : list) {
                usedRoom = new HashMap<String, Object>(Constant.THREE);
                object = (BasicDBObject) o;
                usedRoom.put("id", object.getString("rid"));
                usedRoom.put("usedSeat", object.getInt("an"));
                usedRoom.put("name", object.getString("rna"));
                rooms.put(object.getString("rid"), usedRoom);
            }
        }
        return rooms;
    }

    public List<ObjectId> getSubjectList() {
        return (List<ObjectId>) getSimpleObjectValue("sList");
    }

    public void setSubjectList(List<ObjectId> sList) {
        setSimpleValue("sList", sList);
    }

    public List<ObjectId> getCList() {
        return (List<ObjectId>) getSimpleObjectValue("cList");
    }

    public void setCList(List<ObjectId> cList) {
        setSimpleValue("cList", cList);
    }

    public int getIsGra() {
        return getSimpleIntegerValue("isGra");
    }

    public void setIsGra(int isGra) {
        setSimpleValue("isGra", isGra);
    }

    public String getSchoolYear() {
        return getSimpleStringValue("schY");
    }

    public void setSchoolYear(String schoolYear) {
        setSimpleValue("schY", schoolYear);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sId", schoolId);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String name) {
        setSimpleValue("name", name);
    }

    public long getExamDate() {
        return getSimpleLongValue("ed");
    }

    public void setExamDate(long examDate) {
        setSimpleValue("ed", examDate);
    }

    public String getGradeName() {
        return getSimpleStringValue("gna");
    }

    public void setGradeName(String gradeName) {
        setSimpleValue("gna", gradeName);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gId", gradeId);
    }

    public int getExamType() {
        return getSimpleIntegerValue("et");
    }

    public void setExamType(int examType) {
        setSimpleValue("et", examType);
    }

    public String getRemark() {
        return getSimpleStringValue("rm");
    }

    public void setRemark(String remark) {
        setSimpleValue("rm", remark);
    }

    public String getDeleteFlag() {
        return getSimpleStringValue("df");
    }

    public void setDeleteFlag(String deleteFlag) {
        setSimpleValue("df", deleteFlag);
    }

    public int isLock() {
        return getSimpleObjectValue("lk") == null ? 0 : getSimpleIntegerValue("lk");
    }

    public void setIsLock(int isLock) {
        setSimpleValue("lk", isLock);
    }

    public int isArrangeComplate() {
        return getSimpleObjectValue("ac") == null ? 0 : getSimpleIntegerValue("ac");
    }

    public void setIsArrangeComplate(int isArrangeComplate) {
        setSimpleValue("ac", isArrangeComplate);
    }

    public List<ExamSubjectEntry> getExamSubject() {
        BasicDBList list = (BasicDBList) getSimpleObjectValue("es");
        List<ExamSubjectEntry> retList;
        if (null != list && !list.isEmpty()) {
            retList = new ArrayList<ExamSubjectEntry>(list.size());
            for (Object o : list) {
                retList.add(new ExamSubjectEntry((BasicDBObject) o));
            }
        } else {
            retList = new ArrayList<ExamSubjectEntry>(0);
        }
        return retList;
    }

    public void setExamSubject(List<ExamSubjectEntry> examSubjectEntries) {
        setSimpleValue("es", MongoUtils.convert(MongoUtils.fetchDBObjectList(examSubjectEntries)));
    }
    
    public ObjectId getRegionalExamId() {
        return getSimpleObjecIDValue("eId");
    }

    public void setRegionalExamId(ObjectId regionalExamId) {
        setSimpleValue("eId", regionalExamId);
    }
    
}
