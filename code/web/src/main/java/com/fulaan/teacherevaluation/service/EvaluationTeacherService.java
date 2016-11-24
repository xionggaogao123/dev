package com.fulaan.teacherevaluation.service;

import com.db.teacherevaluation.EvaluationTeacherDao;
import com.db.teacherevaluation.MemberGroupDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.teacherevaluation.EvaluationTeacherDTO;
import com.pojo.teacherevaluation.EvaluationTeacherEntry;
import com.pojo.teacherevaluation.MemberGroupEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/9/12.
 */
@Service
public class EvaluationTeacherService {

    private EvaluationTeacherDao evaluationTeacherDao = new EvaluationTeacherDao();

    private UserDao userDao = new UserDao();

    private MemberGroupDao memberGroupDao = new MemberGroupDao();

    @Autowired
    private EvaluationItemService itemService;

    /**
     * 更新个人陈述
     * @param teacherId
     * @param statement
     */
    public void updateStatement(ObjectId teacherId, String statement){
        evaluationTeacherDao.updateStatement(teacherId, statement);
    }

    /**
     * 更新并推送个人陈述
     * @param teacherId
     * @param evaluationId
     * @param statement
     */
    public void updateAndPushStatement(ObjectId teacherId, ObjectId evaluationId, String statement){
        updateStatement(teacherId, statement);
        pushStatement(teacherId, evaluationId);
    }

    /**
     * 更新实证资料
     * @param teacherId
     * @param evidence
     */
    public void updateEvidence(ObjectId teacherId, String evidence){
        evaluationTeacherDao.updateEvidence(teacherId, evidence);
    }

    /**
     * 查询老师信息
     * @param teacherId
     * @return
     */
    public EvaluationTeacherDTO getEvaluationTeacherEntryByTeacherId(ObjectId teacherId, ObjectId schoolId){
        EvaluationTeacherEntry entry = evaluationTeacherDao.getEvaluationTeacherEntryByTeacherId(teacherId, Constant.FIELDS);
        if(entry == null){
            entry = new EvaluationTeacherEntry(schoolId, teacherId, "", "");
            evaluationTeacherDao.saveEvaluationTeacher(entry);
        }
        EvaluationTeacherDTO teacherDTO = new EvaluationTeacherDTO(entry);
        UserEntry userEntry = userDao.getUserEntry(teacherId, new BasicDBObject("nm", 1));
        teacherDTO.setTeacherName(userEntry.getUserName());
        return teacherDTO;
    }

    /**
     * 查询学校所有老师信息
     * @param schoolId
     * @return
     */
    public List<EvaluationTeacherDTO> getEvaluationTeacherEntryBySchoolId(ObjectId schoolId, int page, int pageSize){
        List<EvaluationTeacherDTO> list = new ArrayList<EvaluationTeacherDTO>();
        List<UserEntry> userEntries = userDao.getTeacherEntryBySchoolId(schoolId, (page - 1) * pageSize, pageSize, new BasicDBObject("nm", 1));
        List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(userEntries);
        List<EvaluationTeacherEntry> evaluationTeacherEntries = evaluationTeacherDao.getEvaluationTeacherEntryByTeacherIds(teacherIds, new BasicDBObject("evi", 1).append("tid", 1));
        Map<ObjectId, EvaluationTeacherEntry> teacherEntryMap = new HashMap<ObjectId, EvaluationTeacherEntry>();
        for(EvaluationTeacherEntry teacherEntry : evaluationTeacherEntries){
            teacherEntryMap.put(teacherEntry.getTeacherId(), teacherEntry);
        }
        for(UserEntry userEntry : userEntries){
            ObjectId teacherId = userEntry.getID();
            EvaluationTeacherEntry teacherEntry = teacherEntryMap.get(teacherId);
            if(teacherEntry == null){
                teacherEntry = new EvaluationTeacherEntry(schoolId, teacherId, "", "");
                evaluationTeacherDao.saveEvaluationTeacher(teacherEntry);
            }
            EvaluationTeacherDTO teacherDTO = new EvaluationTeacherDTO(teacherEntry);
            teacherDTO.setTeacherName(userEntry.getUserName());
            list.add(teacherDTO);
        }
        return list;
    }

    public int countEvaluationTeacherEntryBySchoolId(ObjectId schoolId){
        List<UserEntry> userEntries = userDao.getTeacherEntryBySchoolId(schoolId, new BasicDBObject("nm", 1));
        return userEntries.size();
    }

    /**
     * 推送个人陈述
     * @param teacherId
     * @param evaluationId
     */
    public void pushStatement(ObjectId teacherId, ObjectId evaluationId){
        EvaluationTeacherEntry entry = evaluationTeacherDao.getEvaluationTeacherEntryByTeacherId(teacherId, new BasicDBObject("stat", 1));
        itemService.updateTeacherStatement(teacherId, evaluationId, entry.getStatement());
    }

    /**
     * 推送实证资料
     * @param teacherId
     * @param evaluationId
     */
    public void pushEvidence(ObjectId teacherId, ObjectId evaluationId){
        EvaluationTeacherEntry entry = evaluationTeacherDao.getEvaluationTeacherEntryByTeacherId(teacherId, new BasicDBObject("evi", 1));
        itemService.updateTeacherEvidence(teacherId, evaluationId, entry.getEvidence());
    }

    public void pushAllEvidence(String teacherIds, ObjectId evaluationId) throws Exception{
        List<ObjectId> teachers = null;
        try {
            teachers = MongoUtils.convert(teacherIds);
        } catch (IllegalParamException e) {
            e.printStackTrace();
            throw new Exception("推送失败，教师标识有误");
        }
        for(ObjectId teacherId : teachers){
            pushEvidence(teacherId, evaluationId);
        }
    }

    /**
     * 根据考核项目查找
     * @param evaluationId
     * @param schoolId
     * @return
     */
    public List<EvaluationTeacherDTO> getEvaluationTeacherEntryByEvaluationId(ObjectId evaluationId, ObjectId schoolId){
        List<EvaluationTeacherDTO> list = new ArrayList<EvaluationTeacherDTO>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            teacherIds.addAll(teacherGroup.getSignGroupTeacherIds());
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(teacherIds, new BasicDBObject("nm", 1));
        List<EvaluationTeacherEntry> evaluationTeacherEntries = evaluationTeacherDao.getEvaluationTeacherEntryByTeacherIds(teacherIds, new BasicDBObject("evi", 1).append("tid", 1));
        Map<ObjectId, EvaluationTeacherEntry> teacherEntryMap = new HashMap<ObjectId, EvaluationTeacherEntry>();
        for(EvaluationTeacherEntry teacherEntry : evaluationTeacherEntries){
            teacherEntryMap.put(teacherEntry.getTeacherId(), teacherEntry);
        }
        for(UserEntry userEntry : userEntries){
            ObjectId teacherId = userEntry.getID();
            EvaluationTeacherEntry teacherEntry = teacherEntryMap.get(teacherId);
            if(teacherEntry == null){
                teacherEntry = new EvaluationTeacherEntry(schoolId, teacherId, "", "");
                evaluationTeacherDao.saveEvaluationTeacher(teacherEntry);
            }
            EvaluationTeacherDTO teacherDTO = new EvaluationTeacherDTO(teacherEntry);
            teacherDTO.setTeacherName(userEntry.getUserName());
            list.add(teacherDTO);
        }
        return list;
    }
}
