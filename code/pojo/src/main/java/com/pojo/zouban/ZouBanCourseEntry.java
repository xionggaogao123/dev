package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 走班课程
 * {
 * sid:学校id---------------->schoolId
 * subid:学科id-------------->subjectId
 * te:学期------------------->term
 * gid:年级id---------------->gradeId
 * grpid:分段id-------------->groupId
 * v:课程名称--------------->className
 * cid:班级id
 * lscnt:课时------------>lessonCount
 * tid:老师id------------>teacherId
 * tnm:老师姓名-----------》teacherName
 * crid:上课地址--------->classRoomId
 * stus:学生list---------->studentlist
 * type:课程类型----------->type  1走班 2非走班 3小走班 4体育走班 5其他走班 6 兴趣班
 * group:编组-------------->group  //格致中学--分组.不同于分段(几门走班课分为一组，在同一时间上课)
 * max :人数上限------------>maxpeople 主要用于拓展课人数上限
 * time:上课时间列表[]------>timneList 主要用于记录拓展课的上课时间
 *
 *
 * }
 * Created by wang_xinxin on 2015/9/21.
 */
public class ZouBanCourseEntry extends BaseDBObject implements Cloneable{

    public ZouBanCourseEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ZouBanCourseEntry(ObjectId schoolId,ObjectId subjectId,String team,ObjectId gradeId,ObjectId classId,ObjectId groupId,String className,
                             int lessonCount,List<ObjectId> studentList,int type) {
        this(schoolId,subjectId,team,gradeId,classId,groupId,className,lessonCount,null,null,null,studentList,type,null);
    }

    public ZouBanCourseEntry(ObjectId schoolId,ObjectId subjectId,String team,ObjectId gradeId,ObjectId classId,ObjectId groupId,String className,
                             int lessonCount,ObjectId teacherId,String teacherName,ObjectId classRoomId,List<ObjectId> studentList,int type,ObjectId group) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("sid",schoolId)
                .append("subid",subjectId)
                .append("te", team)
                .append("gid",gradeId)
                .append("grpid",groupId)
                .append("cid",classId)
                .append("clsnm", className)
                .append("lscnt",lessonCount)
                .append("tid",teacherId)
                .append("tnm",teacherName)
                .append("crid",classRoomId)
                .append("stus", MongoUtils.convert(studentList))
                .append("type",type)
                .append("group",group)
                .append("max",0)
                .append("time",MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<PointEntry>())));
        setBaseEntry(baseEntry);
    }
    public ZouBanCourseEntry(ObjectId schoolId,ObjectId subjectId,String team,ObjectId gradeId,ObjectId classId,ObjectId groupId,String className,
                             int lessonCount,ObjectId teacherId,String teacherName,ObjectId classRoomId,List<ObjectId> studentList,int type,ObjectId group,
                             int max,List<PointEntry> pointEntries) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("sid",schoolId)
                .append("subid",subjectId)
                .append("te", team)
                .append("gid",gradeId)
                .append("grpid",groupId)
                .append("cid",classId)
                .append("clsnm", className)
                .append("lscnt",lessonCount)
                .append("tid",teacherId)
                .append("tnm",teacherName)
                .append("crid",classRoomId)
                .append("stus", MongoUtils.convert(studentList))
                .append("type",type)
                .append("group",group)
                .append("max",max)
                .append("time",MongoUtils.convert(MongoUtils.fetchDBObjectList(pointEntries)));
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSubjectId()
    {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId)
    {
        setSimpleValue("subid",subjectId);
    }

    public String getTeam() {
        return getSimpleStringValue("te");
    }

    public void setTeam(String team) {
        setSimpleValue("te",team);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid",gradeId);
    }

    public ObjectId getGroupId() {
        return getSimpleObjecIDValue("grpid");
    }

    public void setGroupId(ObjectId groupId) {
        setSimpleValue("grpid",groupId);
    }

    public String getClassName() {
        return  getSimpleStringValue("clsnm");
    }

    public void setClassName(String className) {
        setSimpleValue("clsnm",className);
    }

    public int getLessonCount() {
        return getSimpleIntegerValue("lscnt");
    }

    public void setLessonCount(int lessonCount) {
        setSimpleValue("lscnt",lessonCount);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid",teacherId);
    }

    public ObjectId getClassRoomId() {
        return getSimpleObjecIDValue("crid");
    }

    public void setClassRoomId(ObjectId classRoomId) {
        setSimpleValue("crid",classRoomId);
    }

    public int getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(int type) {
        setSimpleValue("type",type);
    }

    public List<ObjectId> getStudentList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("stus");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setStudentList(List<ObjectId> studentList) {
        setSimpleValue("stus", MongoUtils.convert(studentList));
    }
    public String getTeacherName()
    {
        return getSimpleStringValue("tnm");
    }
    public void setTeacherName(String name)
    {
        setSimpleValue("tnm",name);
    }

    public ObjectId getClassId() {
        return  getSimpleObjecIDValue("cid");
    }
    public void setClassId(ObjectId classId) {
        setSimpleValue("cid",classId);
    }

    public ObjectId getGroup()
    {
        if(this.getBaseEntry().containsField("group"))
            return getSimpleObjecIDValue("group");
        else
            return null;
    }
    public void setGroup(ObjectId group)
    {
        setSimpleValue("group",group);
    }

    public int getMax()
    {
        if(this.getBaseEntry().containsField("max"))
        {
            return getSimpleIntegerValue("max");
        }
        return 0;
    }
    public void setMax(int max)
    {
        setSimpleValue("max",max);
    }

    public List<PointEntry> getPointEntry()
    {
        List<PointEntry> pointEntries=new ArrayList<PointEntry>();
        if(this.getBaseEntry().containsField("time"))
        {
            BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("time");
            if(basicDBList!=null&&!basicDBList.isEmpty())
            {
                for (Object o:basicDBList)
                {
                    pointEntries.add(new PointEntry((BasicDBObject)o));
                }
            }
        }
        return pointEntries;
    }
    public void setPointEntry(List<PointEntry> pointEntry)
    {
        setSimpleValue("time",MongoUtils.convert(MongoUtils.fetchDBObjectList(pointEntry)));
    }

    /*public List<ObjectId> getClassIds()
    {
        List<ObjectId> classIds=new ArrayList<ObjectId>();
        if(this.getBaseEntry().containsField("cids"))
        {
            BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("cids");
            if(basicDBList!=null&&!basicDBList.isEmpty())
            {
                for (Object o:basicDBList)
                {
                    classIds.add((ObjectId)o);
                }
            }
        }
        return classIds;
    }
    public void setClassIds(List<ObjectId> classIds)
    {
        setSimpleValue("cids",MongoUtils.convert(classIds));
    }*/
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
