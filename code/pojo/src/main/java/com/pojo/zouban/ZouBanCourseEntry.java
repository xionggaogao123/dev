package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 走班课程
 * {
 * sid:学校id---------------->schoolId
 * te:学期------------------->term
 * gid:年级id---------------->gradeId
 *
 *
 * //教学班基础数据
 * subid:学科id-------------->subjectId
 * lscnt:课时------------>lessonCount
 * tid:老师id------------>teacherId
 * tnm:老师姓名----------->teacherName
 * crid:上课地址--------->classRoomId
 * stus:学生list---------->studentList
 * clsnm:教学班名称--------------->className
 * //分组走班分班数
 * clsnb:分班数------------>classNumber
 *
 * //教学班类型
 * type:课程类型----------->type  （参见ZoubanType）
 *
 * grpid:分段id-------------->groupId （几个行政班分在一段， 可以减少对老师的需求，减少跨年级上课老师）
 * cid[]:行政班id------------------->classIdList (走班课关联若干个；非走班课关联一个；体育课分男女班，关联两个；分组走班课关联若干个；单双周课关联一个；)
 * group:分组-------------->group  //分组.不同于分段(几门走班课分为一组，在同一时间上课，走班课/体育课/分组走班课/单双周课 均有分组，非走班无分组)
 *
 * //拓展课专用字段，
 * max :人数上限------------>max 主要用于拓展课人数上限
 * time:上课时间列表[]------>timeList 主要用于记录拓展课的上课时间
 *
 * //走班课新增
 * lv:等级考/合格考----------> level 1：等级考 2：合格考
 *
 * //分组走班/单双周专用
 * scid:subjectConfId 学科Id
 *
 * }
 * Created by wang_xinxin on 2015/9/21.
 */
public class ZouBanCourseEntry extends BaseDBObject implements Cloneable {

    public ZouBanCourseEntry() {
    }

    public ZouBanCourseEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, List<ObjectId> classIdList, ObjectId groupId, String className,
                             int lessonCount, ObjectId teacherId, String teacherName, ObjectId classRoomId, List<ObjectId> studentList, int type, ObjectId group,
                             int max, List<PointEntry> pointEntries, int level,int classNumber,ObjectId subjectConfId) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("subid", subjectId)
                .append("te", term)
                .append("gid", gradeId)
                .append("grpid", groupId)
                .append("cid", MongoUtils.convert(classIdList))
                .append("clsnm", className)
                .append("lscnt", lessonCount)
                .append("tid", teacherId)
                .append("tnm", teacherName)
                .append("crid", classRoomId)
                .append("stus", MongoUtils.convert(studentList))
                .append("type", type)
                .append("group", group)
                .append("max", max)
                .append("time", MongoUtils.convert(MongoUtils.fetchDBObjectList(pointEntries)))
                .append("lv", level)
                .append("clsnb",classNumber)
                .append("scid",subjectConfId);
        setBaseEntry(baseEntry);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, ObjectId groupId,
                             String className, int lessonCount, List<ObjectId> studentList, ObjectId group, int level) {
        this(schoolId, subjectId, term, gradeId, null, groupId, className, lessonCount,
                null, null, null, studentList, ZoubanType.ZOUBAN.getType(), group, 0, null, level,0,null);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, List<ObjectId> classIdList, ObjectId groupId,
                             String className, int lessonCount, List<ObjectId> studentList, ObjectId group, int type) {
        this(schoolId, subjectId, term, gradeId, classIdList, groupId, className, lessonCount,
                null, null, null, studentList, type, group, 0, null, 0,0,null);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, List<ObjectId> classIdList, ObjectId groupId,
                             String className, int lessonCount, ObjectId teacherId, String teacherName, ObjectId classRoomId,
                             List<ObjectId> studentList, int type, ObjectId group) {
        this(schoolId, subjectId, term, gradeId, classIdList, groupId, className, lessonCount,
                teacherId, teacherName, classRoomId, studentList, type, group, 0, null, 0,0,null);
    }

    public ZouBanCourseEntry(ObjectId schoolId, String term, ObjectId gradeId, ObjectId grpId, ObjectId subjectId, String className,
                             ObjectId teacherId, String teacherName, ObjectId classroomId, int lessonCount, ObjectId group, int level) {
        this(schoolId, subjectId, term, gradeId, null, grpId, className, lessonCount,
                teacherId, teacherName, classroomId, null, ZoubanType.ZOUBAN.getType(), group, 0, null, level,0,null);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, List<ObjectId> classIdList, ObjectId groupId,
                             String className, int lessonCount,
                              int type, ObjectId group,int classNumber,ObjectId subjectConfId) {
        this(schoolId, subjectId, term, gradeId, classIdList, groupId, className, lessonCount,
                    null,null, null, null, type, group, 0, null, 0,classNumber,subjectConfId);
    }

    public ZouBanCourseEntry(ObjectId schoolId, ObjectId subjectId, String term, ObjectId gradeId, List<ObjectId> classIdList, ObjectId groupId,
                             String className, int lessonCount,
                             int type, ObjectId group,int classNumber,ObjectId classRoomId,List<ObjectId> studentList,ObjectId subjectConfId) {
        this(schoolId, subjectId, term, gradeId, classIdList, groupId, className, lessonCount,
                null,null, classRoomId, studentList, type, group, 0, null, 0,classNumber,subjectConfId);
    }


    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getSubjectConfId() {
        return getSimpleObjecIDValue("scid");
    }

    public void setSubjectConfId(ObjectId subjectConfId) {
        setSimpleValue("scid", subjectConfId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subid", subjectId);
    }

    public String getTerm() {
        return getSimpleStringValue("te");
    }

    public void setTerm(String team) {
        setSimpleValue("te", team);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getGroupId() {
        return getSimpleObjecIDValue("grpid");
    }

    public void setGroupId(ObjectId groupId) {
        setSimpleValue("grpid", groupId);
    }

    public String getClassName() {
        return getSimpleStringValue("clsnm");
    }

    public void setClassName(String className) {
        setSimpleValue("clsnm", className);
    }

    public int getLessonCount() {
        return getSimpleIntegerValue("lscnt");
    }

    public void setLessonCount(int lessonCount) {
        setSimpleValue("lscnt", lessonCount);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public ObjectId getClassRoomId() {
        return getSimpleObjecIDValue("crid");
    }

    public void setClassRoomId(ObjectId classRoomId) {
        setSimpleValue("crid", classRoomId);
    }

    public int getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(int type) {
        setSimpleValue("type", type);
    }

    public List<ObjectId> getStudentList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("stus");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add((ObjectId) o);
            }
        }
        return retList;
    }

    public void setStudentList(List<ObjectId> studentList) {
        setSimpleValue("stus", MongoUtils.convert(studentList));
    }

    public String getTeacherName() {
        return getSimpleStringValue("tnm");
    }

    public void setTeacherName(String name) {
        setSimpleValue("tnm", name);
    }

    public List<ObjectId> getClassId() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cid");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add((ObjectId) o);
            }
        }
        return retList;
    }

    public void setClassId(List<ObjectId> classIdList) {
        setSimpleValue("cid", MongoUtils.convert(classIdList));
    }

    public ObjectId getGroup() {
        if (this.getBaseEntry().containsField("group"))
            return getSimpleObjecIDValue("group");
        else
            return null;
    }

    public void setGroup(ObjectId group) {
        setSimpleValue("group", group);
    }

    public int getMax() {
        if (this.getBaseEntry().containsField("max")) {
            return getSimpleIntegerValue("max");
        }
        return 0;
    }

    public void setMax(int max) {
        setSimpleValue("max", max);
    }

    public List<PointEntry> getPointEntry() {
        List<PointEntry> pointEntries = new ArrayList<PointEntry>();
        if (this.getBaseEntry().containsField("time")) {
            BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("time");
            if (basicDBList != null && !basicDBList.isEmpty()) {
                for (Object o : basicDBList) {
                    pointEntries.add(new PointEntry((BasicDBObject) o));
                }
            }
        }
        return pointEntries;
    }

    public void setPointEntry(List<PointEntry> pointEntry) {
        setSimpleValue("time", MongoUtils.convert(MongoUtils.fetchDBObjectList(pointEntry)));
    }

    public int getLevel(){
        if (getBaseEntry().containsField("lv")) {
            return getSimpleIntegerValue("lv");
        } else {
            return 0;
        }
    }

    public void setLevel(int level) {
        setSimpleValue("lv", level);
    }

    public int getClassNumber(){
        if (getBaseEntry().containsField("clsnb")) {
            return getSimpleIntegerValueDef("clsnb",0);
        } else {
            return 0;
        }
    }

    public void setClassNumber(int classNumber){
        setSimpleValue("clsnb",classNumber);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
