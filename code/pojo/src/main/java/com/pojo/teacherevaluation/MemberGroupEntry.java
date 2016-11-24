package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

/**
 * 教师评价人员分组  te_memberGroup
 * Created by fl on 2016/4/18.
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * lds 考核小组领导   校领导
 * mes 考核小组成员   老师
 * tgs 被考核小组 List<TeacherGroup>
 */
public class MemberGroupEntry extends BaseDBObject {

    public MemberGroupEntry(){}

    public MemberGroupEntry(ObjectId schoolId, String year){
        this(schoolId, year, new ArrayList<ObjectId>(), new ArrayList<ObjectId>(), new ArrayList<TeacherGroup>());
    }

    public MemberGroupEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public MemberGroupEntry(ObjectId schoolId, String year, List<ObjectId> leaders, List<ObjectId> members, List<TeacherGroup> teacherGroupList){
        List<DBObject> teacherGroups = MongoUtils.fetchDBObjectList(teacherGroupList);
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("lds", leaders)
                .append("mes", members)
                .append("tgs", teacherGroups)
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("si", schoolId);
    }

    public String getYear(){
        return getSimpleStringValue("y");
    }

    public void setYear(String year){
        setSimpleValue("y", year);
    }

    public List<ObjectId> getLeaders(){
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("lds");
        if(null!=list && !list.isEmpty()) {
            for(Object o:list) {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setLeaders(List<ObjectId> leaders){
        setSimpleValue("lds", MongoUtils.convert(leaders));
    }

    public List<ObjectId> getMembers(){
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("mes");
        if(null!=list && !list.isEmpty()) {
            for(Object o:list) {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setMembers(List<ObjectId> members){
        setSimpleValue("mes", MongoUtils.convert(members));
    }

    public List<TeacherGroup> getTeacherGroups(){
        List<TeacherGroup> retList =new ArrayList<TeacherGroup>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("tgs");
        if(null!=list && !list.isEmpty()) {
            for(Object o:list) {
                retList.add(new TeacherGroup((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setTeacherGroups(List<TeacherGroup> teacherGroups){
        setSimpleValue("tgs", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherGroups)));
    }

    public void addTeacherToLeaders(List<ObjectId> teacherIds){
        List<ObjectId> leaders = getLeaders();
        leaders.addAll(teacherIds);
        setLeaders(leaders);
    }

    public void removeTeacherFromLeaders(ObjectId teacherId){
        List<ObjectId> leaders = getLeaders();
        leaders.remove(teacherId);
        setLeaders(leaders);
    }

    public void addTeacherToMembers(List<ObjectId> teacherIds){
        List<ObjectId> members = getMembers();
        members.addAll(teacherIds);
        setMembers(members);
    }

    public void removeTeacherFromMembers(ObjectId teacherId){
        List<ObjectId> members = getMembers();
        members.remove(teacherId);
        setMembers(members);
    }

    public void addTeacherToTeacherGroup(ObjectId groupId, List<ObjectId> teacherIds){
        List<MemberGroupEntry.TeacherGroup> teacherGroups = getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupId().equals(groupId)){
                teacherGroup.addTeachers(teacherIds);
                break;
            }
        }
    }

    public void removeTeacherFromTeacherGroup(ObjectId groupId, ObjectId teacherId){
        List<MemberGroupEntry.TeacherGroup> teacherGroups = getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupId().equals(groupId)){
                teacherGroup.removeTeacher(teacherId);
                break;
            }
        }
    }

    /**
     * 被考核老师分组
     * id 小组id
     * nm 小组名称
     * num 优秀人数
     * lnum 良好人数
     * teas 小组中老师id列表
     */
    public static class TeacherGroup extends BaseDBObject{
        public TeacherGroup(){}

        public TeacherGroup(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public TeacherGroup(ObjectId id, String name, int num, int lianghaoNum, List<ObjectId> groupTeachers){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id", id)
                    .append("nm", name)
                    .append("num", num)
                    .append("lnum", lianghaoNum)
                    .append("teas", MongoUtils.convert(groupTeachers))
                    ;
            setBaseEntry(baseEntry);
        }

        public TeacherGroup(String name){
            this(new ObjectId(), name, 0, 0, new ArrayList<ObjectId>());
        }

        public ObjectId getGroupId(){
            return getSimpleObjecIDValue("id");
        }

        public void setGroupId(ObjectId groupId){
            setSimpleValue("id", groupId);
        }

        public String getGroupName(){
            return getSimpleStringValue("nm");
        }

        public void setGroupName(String groupName){
            setSimpleValue("nm", groupName);
        }

        public int getNum(){
            return getSimpleIntegerValue("num");
        }

        public void setNum(int num){
            setSimpleValue("num", num);
        }

        public int getLiangNum(){
            return getSimpleIntegerValueDef("lnum", 0);
        }

        public void setLiangNum(int liangNum){
            setSimpleValue("lnum", liangNum);
        }
        public List<ObjectId> getGroupTeachers(){
            List<ObjectId> retList = new ArrayList<ObjectId>();
            BasicDBList basicDBList = (BasicDBList)getSimpleObjectValue("teas");
            if(null != basicDBList && !basicDBList.isEmpty()){
                for(Object o : basicDBList){
                    retList.add((ObjectId)o);
                }
            }
            return retList;
        }

        public void setGroupTeachers(List<ObjectId> groupTeachers){
            setSimpleValue("teas", MongoUtils.convert(groupTeachers));
        }

        public void addTeacher(ObjectId teacherId){
            List<ObjectId> teachers = getGroupTeachers();
            teachers.add(teacherId);
            setGroupTeachers(teachers);
        }

        public void addTeachers(List<ObjectId> teacherIds){
            List<ObjectId> teachers = getGroupTeachers();
            teachers.addAll(teacherIds);
            setGroupTeachers(teachers);
        }

        public void removeTeacher(ObjectId teacher){
            List<ObjectId> teachers = getGroupTeachers();
            teachers.remove(teacher);
            setGroupTeachers(teachers);
        }
    }
}
