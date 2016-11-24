package com.pojo.teacherevaluation;

import com.pojo.app.IdValuePairDTO1;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/4/20.
 */
public class MemberGroupDTO {

    private String id;
    private String schoolId;
    private String year;
    private List<IdValuePairDTO1> leaders = new ArrayList<IdValuePairDTO1>();
    private List<IdValuePairDTO1> members = new ArrayList<IdValuePairDTO1>();
    private List<TeacherGroupDTO> teacherGroupDTOs = new ArrayList<TeacherGroupDTO>();

    public MemberGroupDTO(){}

    public MemberGroupDTO(MemberGroupEntry entry, Map<ObjectId, UserEntry> userEntryMap){
        id = entry.getID()==null ? "" : entry.getID().toString();
        schoolId = entry.getSchoolId().toString();
        year = entry.getYear();
        List<ObjectId> leaderIds = entry.getLeaders();
        for(ObjectId leaderId : leaderIds){
            UserEntry userEntry = userEntryMap.get(leaderId);
            leaders.add(new IdValuePairDTO1(leaderId.toString(), userEntry==null ? "" : userEntry.getUserName()));
        }
        List<ObjectId> memberIds = entry.getMembers();
        for(ObjectId member : memberIds){
            UserEntry userEntry = userEntryMap.get(member);
            members.add(new IdValuePairDTO1(member.toString(), userEntry==null ? "" : userEntry.getUserName()));
        }
        List<MemberGroupEntry.TeacherGroup> teacherGroups = entry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            teacherGroupDTOs.add(new TeacherGroupDTO(teacherGroup, userEntryMap));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<IdValuePairDTO1> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<IdValuePairDTO1> leaders) {
        this.leaders = leaders;
    }

    public List<IdValuePairDTO1> getMembers() {
        return members;
    }

    public void setMembers(List<IdValuePairDTO1> members) {
        this.members = members;
    }

    public List<TeacherGroupDTO> getTeacherGroupDTOs() {
        return teacherGroupDTOs;
    }

    public void setTeacherGroupDTOs(List<TeacherGroupDTO> teacherGroupDTOs) {
        this.teacherGroupDTOs = teacherGroupDTOs;
    }


    /**
     *
     */
    public static class TeacherGroupDTO{
        private String groupId;
        private String groupName;
        private int num; //优秀人数
        private int liangNum; //良好人数
        private List<IdValuePairDTO1> teachers = new ArrayList<IdValuePairDTO1>();

        public TeacherGroupDTO(){}

        public TeacherGroupDTO(MemberGroupEntry.TeacherGroup teacherGroup, Map<ObjectId, UserEntry> userEntryMap){
            groupId = teacherGroup.getGroupId().toString();
            groupName = teacherGroup.getGroupName();
            num = teacherGroup.getNum();
            liangNum = teacherGroup.getLiangNum();
            List<ObjectId> teacherIds = teacherGroup.getGroupTeachers();
            for(ObjectId teacherId : teacherIds){
                UserEntry userEntry = userEntryMap.get(teacherId);
                teachers.add(new IdValuePairDTO1(teacherId.toString(), userEntry==null ? "" : userEntry.getUserName()));
            }
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getLiangNum() {
            return liangNum;
        }

        public void setLiangNum(int liangNum) {
            this.liangNum = liangNum;
        }

        public List<IdValuePairDTO1> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<IdValuePairDTO1> teachers) {
            this.teachers = teachers;
        }
    }


}
