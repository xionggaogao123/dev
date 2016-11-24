package com.fulaan.zouban.dto;

import com.pojo.zouban.PointEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/10/15.
 */
public class ZouBanCourseDTO {
    private String zbCourseId;
    private String courseName;
    private List<ObjectId> classIdList;
    private String className;
    private int group;
    private int count;
    private int lessonCount;
    private String teacherId;
    private String teacherName;
    private String classRoomId;
    private String classRoom;
    private String groupId;
    private String groupStr;
    private String subjectId;
    private String subjectName;
    private int check;
    private String term;
    private String schoolId;
    private String gradeId;
    private int max;
    private List<PointEntry> pointEntrylist;
    private int studentsCount;
    private List<ObjectId> studentList = new ArrayList<ObjectId>();
    private String groupName;
    private int classNumber;
    private String groupTypeId;

    public ZouBanCourseDTO() {}

    public ZouBanCourseDTO(ZouBanCourseEntry zouBanCourseEntry) {
        this.courseName = zouBanCourseEntry.getClassName();
        this.classIdList = zouBanCourseEntry.getClassId();
        this.className = zouBanCourseEntry.getClassName();
        
        
        if(null!=zouBanCourseEntry.getGroup())
        {
          this.groupStr = zouBanCourseEntry.getGroup().toString();
        }
        
        this.lessonCount = zouBanCourseEntry.getLessonCount();
        if(null!=zouBanCourseEntry.getTeacherId()){
            this.teacherId = zouBanCourseEntry.getTeacherId().toString();
        }
        this.teacherName = zouBanCourseEntry.getTeacherName();
        if (zouBanCourseEntry.getClassRoomId() != null) {
            this.classRoomId = zouBanCourseEntry.getClassRoomId().toString();
        } else {
            this.classRoomId = "";
        }
        this.zbCourseId = zouBanCourseEntry.getID().toString();
        this.subjectId = zouBanCourseEntry.getSubjectId().toString();
        this.max = zouBanCourseEntry.getMax();
        this.pointEntrylist = zouBanCourseEntry.getPointEntry();
        this.studentsCount = zouBanCourseEntry.getStudentList().size();
        if (zouBanCourseEntry.getGroupId() != null) {
            this.setGroupId(zouBanCourseEntry.getGroupId().toString());
        } else {
            this.setGroupId("");
            this.setGroupName("");
        }
        this.schoolId = zouBanCourseEntry.getSchoolId().toString();
        this.gradeId = zouBanCourseEntry.getGradeId().toString();
        this.term = zouBanCourseEntry.getTerm();
        this.classNumber=zouBanCourseEntry.getClassNumber();
        if(null!=zouBanCourseEntry.getGroup()){
            this.groupTypeId=zouBanCourseEntry.getGroup().toString();
        }
    }


    public ZouBanCourseEntry exportEntry() {
        ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry();
        if (zbCourseId != null) {
            zouBanCourseEntry.setID(new ObjectId(zbCourseId));
        }
        zouBanCourseEntry.setSchoolId(new ObjectId(schoolId));
        zouBanCourseEntry.setGradeId(new ObjectId(gradeId));
        zouBanCourseEntry.setTeacherId(new ObjectId(teacherId));
        zouBanCourseEntry.setTeacherName(teacherName);
        zouBanCourseEntry.setClassId(classIdList);
        zouBanCourseEntry.setClassName(className);
        zouBanCourseEntry.setGroupId(new ObjectId(groupId));
        zouBanCourseEntry.setClassRoomId(new ObjectId(classRoomId));
        zouBanCourseEntry.setMax(max);
        zouBanCourseEntry.setTerm(term);
        zouBanCourseEntry.setPointEntry(pointEntrylist);
        zouBanCourseEntry.setStudentList(studentList);
        zouBanCourseEntry.setLessonCount(lessonCount);
        zouBanCourseEntry.setClassNumber(classNumber);
        if(StringUtils.isNotBlank(groupTypeId)){
            zouBanCourseEntry.setGroup(new ObjectId(groupTypeId));
        }

        return zouBanCourseEntry;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getZbCourseId() {
        return zbCourseId;
    }

    public void setZbCourseId(String zbCourseId) {
        this.zbCourseId = zbCourseId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(String classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<ObjectId> getClassIdList() {
        return classIdList;
    }

    public void setClassIdList(List<ObjectId> classIds) {
        this.classIdList = classIds;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List<PointEntry> getPointEntrylist() {
        return pointEntrylist;
    }

    public void setPointEntrylist(List<PointEntry> pointEntrylist) {
        this.pointEntrylist = pointEntrylist;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public List<ObjectId> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<ObjectId> studentList) {
        this.studentList = studentList;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(String groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }
}
