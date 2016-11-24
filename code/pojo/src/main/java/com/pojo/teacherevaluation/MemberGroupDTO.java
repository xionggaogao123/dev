package com.pojo.teacherevaluation;

import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by fl on 2016/4/20.
 */
public class MemberGroupDTO {

    private String id;
    private String schoolId;
    private String year;
    private String name;
    private List<IdValuePairDTO1> leaders = new ArrayList<IdValuePairDTO1>();
    private List<IdValuePairDTO1> members = new ArrayList<IdValuePairDTO1>();
    private List<TeacherGroupDTO> teacherGroupDTOs = new ArrayList<TeacherGroupDTO>();
    private int state;//老师的报名状态  1已报名 -1未报名  0未在候选老师列表
    private int timeState;//本次考核时间状态，-1考核未开始  0考核进行中  1考核已结束

    public MemberGroupDTO(){}

    public MemberGroupDTO(String id, String schoolId, String year, String name, int state, int timeState) {
        this.id = id;
        this.schoolId = schoolId;
        this.year = year;
        this.name = name;
        this.state = state;
        this.timeState = timeState;
    }

    public MemberGroupDTO(MemberGroupEntry entry, Map<ObjectId, UserEntry> userEntryMap){
        id = entry.getID()==null ? "" : entry.getID().toString();
        schoolId = entry.getSchoolId().toString();
        year = entry.getYear();
        name = entry.getName();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTimeState() {
        return timeState;
    }

    public void setTimeState(int timeState) {
        this.timeState = timeState;
    }

    /**
     *
     */
    public static class TeacherGroupDTO{
        private String groupId;
        private String groupName;
        private int num; //优秀人数
        private int liangNum; //良好人数
        private List<IdNameValuePairDTO> teachers = new ArrayList<IdNameValuePairDTO>();

        public TeacherGroupDTO(){}

        public TeacherGroupDTO(MemberGroupEntry.TeacherGroup teacherGroup, Map<ObjectId, UserEntry> userEntryMap){
            groupId = teacherGroup.getGroupId().toString();
            groupName = teacherGroup.getGroupName();
            num = teacherGroup.getNum();
            liangNum = teacherGroup.getLiangNum();
            List<IdValuePair> groupTeachers = teacherGroup.getGroupTeachers();
            Collections.sort(groupTeachers, new Comparator<IdValuePair>() {
                @Override
                public int compare(IdValuePair pair1, IdValuePair pair2) {
                    int value1 = (Integer)pair1.getValue();
                    int value2 = (Integer)pair2.getValue();
                    return value2 - value1;
                }
            });

            for(IdValuePair teacher : groupTeachers){
                UserEntry userEntry = userEntryMap.get(teacher.getId());
                teachers.add(new IdNameValuePairDTO(teacher.getId().toString(), userEntry==null ? "" : userEntry.getUserName(), teacher.getValue()));
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

        public List<IdNameValuePairDTO> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<IdNameValuePairDTO> teachers) {
            this.teachers = teachers;
        }
    }


}
