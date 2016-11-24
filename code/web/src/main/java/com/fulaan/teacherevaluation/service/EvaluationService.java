package com.fulaan.teacherevaluation.service;

import com.db.teacherevaluation.EvaluationItemDao;
import com.db.teacherevaluation.MemberGroupDao;
import com.db.teacherevaluation.ProportionDao;
import com.db.teacherevaluation.SettingDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.teacherevaluation.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fl on 2016/7/29.
 */
@Service
public class EvaluationService {

    private MemberGroupDao memberGroupDao = new MemberGroupDao();

    private ProportionDao proportionDao = new ProportionDao();

    private SettingDao settingDao = new SettingDao();

    private EvaluationItemDao evaluationItemDao = new EvaluationItemDao();


    /**
     * 列表
     * @param schoolId
     * @param year
     * @return
     */
    public List<MemberGroupDTO> getEvaluations(ObjectId schoolId, String year, ObjectId userId, Boolean isHeadMaster){
        List<MemberGroupDTO> memberGroupDTOs = new ArrayList<MemberGroupDTO>();
        List<MemberGroupEntry> memberGroupEntries = memberGroupDao.getMemberGroupBySchoolIdAndYear(schoolId, year, Constant.FIELDS);
        for(MemberGroupEntry memberGroupEntry : memberGroupEntries){
            Integer state = isHasUser(memberGroupEntry, userId);
            if(state == null && isHeadMaster){
                state = 0;
            }
            if(state != null){
                SettingEntry settingEntry = settingDao.getSettingEntry(memberGroupEntry.getID());
                long now = System.currentTimeMillis();
                int timeState = now < settingEntry.getEvaluationTimeBegin() ? -1 : now < settingEntry.getEvaluationTimeEnd() ? 0 : 1;
                MemberGroupDTO memberGroupDTO = new MemberGroupDTO(memberGroupEntry.getID().toString(), memberGroupEntry.getSchoolId().toString(),
                        memberGroupEntry.getYear(), memberGroupEntry.getName(), state, timeState);
                memberGroupDTOs.add(memberGroupDTO);
            }
        }
        return memberGroupDTOs;
    }

    //返回null表示没有该用户  返回数字表示 1已报名 -1未报名  0考核领导或领导小组成员
    public Integer isHasUser(MemberGroupEntry memberGroupEntry, ObjectId userId){
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<IdValuePair> idValuePairs = teacherGroup.getGroupTeachers();
            for(IdValuePair idValuePair : idValuePairs){
                if(idValuePair.getId().equals(userId)){
                    return (Integer)idValuePair.getValue();
                }
            }
        }
        if(memberGroupEntry.getLeaders().contains(userId)){
            return 0;
        }
        if(memberGroupEntry.getMembers().contains(userId)){
            return 0;
        }

        return null;
    }

    /**
     * 新增
     * @param schoolId
     * @param year
     */
    public void addEvaluations(ObjectId schoolId, String year, String name){
        MemberGroupEntry memberGroupEntry = new MemberGroupEntry(schoolId, year, name);
        ObjectId evaluationId = memberGroupDao.saveMemberGroup(memberGroupEntry);

        ProportionEntry proportionEntry = new ProportionEntry(schoolId, year, evaluationId);
        proportionDao.addProportion(proportionEntry);

        SettingEntry settingEntry = new SettingEntry(schoolId, year, evaluationId);
        settingDao.addSetting(settingEntry);
    }

    /**
     * 更新
     * @param evaluationId
     * @param name
     */
    public void updateEvaluation(ObjectId evaluationId, String name){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        memberGroupEntry.setName(name);
        memberGroupDao.saveMemberGroup(memberGroupEntry);
    }

    /**
     * 逻辑删除
     * @param evaluationId
     */
    public void removeEvaluation(ObjectId evaluationId){
        memberGroupDao.removeMemberGroup(evaluationId);
    }

    public int sign(ObjectId evaluationId, ObjectId teacherId) throws Exception{
        //检查考核是否开始
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        long begin = settingEntry.getEvaluationTimeBegin();
        if(new Date().getTime() > begin){
            //考核已经开始不允许更改状态
            throw new Exception("考核已经开始，不允许变更报名状态");
        }
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<IdValuePair> teachers = teacherGroup.getGroupTeachers();
            for(IdValuePair teacher : teachers){
                if(teacher.getId().equals(teacherId)){
                    int state = (Integer)teacher.getValue();
                    state = state == 1 ? -1 : 1;
                    teacher.setValue(state);
                    memberGroupDao.saveMemberGroup(memberGroupEntry);
                    if(state == 1){
                        EvaluationItemEntry evaluationItemEntry = evaluationItemDao.getEvaluationItem(teacherId, evaluationId, new BasicDBObject("tid", 1));
                        if(evaluationItemEntry == null){
                            evaluationItemEntry = new EvaluationItemEntry(evaluationId, teacherId);
                            evaluationItemDao.addEvaluationItem(evaluationItemEntry);
                        }
                    } else {
                        evaluationItemDao.removeItem(evaluationId, teacherId);
                    }
                    return state;
                }
            }
        }
        throw new Exception("未参加本次考评");
    }

    /**
     * 获取还未到评价打分时间的评价项目
     * @param schoolId
     * @param userId
     * @return
     */
    public List<MemberGroupDTO> getUnBeginEvaluationsForTeacher(ObjectId schoolId, ObjectId userId){
        List<MemberGroupDTO> memberGroupDTOs = new ArrayList<MemberGroupDTO>();
        List<ObjectId> evaluationIds = settingDao.getEvaluationIdsByTime(schoolId, System.currentTimeMillis());
        List<MemberGroupEntry> memberGroupEntries = memberGroupDao.getMemberGroupByIds(evaluationIds, Constant.FIELDS);
        for(MemberGroupEntry memberGroupEntry : memberGroupEntries){
            Integer state = isHasUser(memberGroupEntry, userId);
            if(state != null && 1 == state){
                MemberGroupDTO memberGroupDTO = new MemberGroupDTO(memberGroupEntry.getID().toString(), memberGroupEntry.getSchoolId().toString(),
                        memberGroupEntry.getYear(), memberGroupEntry.getName(), state, -1);
                memberGroupDTOs.add(memberGroupDTO);
            }
        }
        return memberGroupDTOs;
    }

    /**
     * 获取还未到评价打分时间的评价项目
     * @param schoolId
     * @return
     */
    public List<MemberGroupDTO> getUnBeginEvaluationsForManager(ObjectId schoolId){
        List<MemberGroupDTO> memberGroupDTOs = new ArrayList<MemberGroupDTO>();
        List<ObjectId> evaluationIds = settingDao.getEvaluationIdsByTime(schoolId, System.currentTimeMillis());
        List<MemberGroupEntry> memberGroupEntries = memberGroupDao.getMemberGroupByIds(evaluationIds, Constant.FIELDS);
        for(MemberGroupEntry memberGroupEntry : memberGroupEntries){
            MemberGroupDTO memberGroupDTO = new MemberGroupDTO(memberGroupEntry.getID().toString(), memberGroupEntry.getSchoolId().toString(),
                    memberGroupEntry.getYear(), memberGroupEntry.getName(), 0, -1);
            memberGroupDTOs.add(memberGroupDTO);
        }
        return memberGroupDTOs;
    }



}
