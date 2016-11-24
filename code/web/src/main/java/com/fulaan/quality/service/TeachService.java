package com.fulaan.quality.service;

import com.db.quality.TeachDao;
import com.fulaan.base.service.DirService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.quality.dto.TeacherCheckDTO;
import com.fulaan.quality.dto.TeacherPlainDTO;
import com.fulaan.quality.dto.TeacherProject;
import com.fulaan.quality.dto.TeacherProjectDTO;
import com.mongodb.BasicDBObject;
import com.pojo.Quality.TeachCheckEntry;
import com.pojo.Quality.TeachProjectEntry;
import com.pojo.Quality.TeacherPlainEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/11/10.
 */

@Service
public class TeachService {

    private TeachDao teachDao = new TeachDao();

    private DirService dirService =new DirService();

    private LessonService lessonService =new LessonService();

    /**
     *
     * @param term
     * @param userId
     * @param plainName
     * @return
     */
    public List<TeacherPlainDTO> selTeacherPlainList(String term,ObjectId userId,String plainName) {
        return null;
    }

    /**
     *
     * @param teacherPlainDTO
     */
    public void addUdpTeachPlan(TeacherPlainDTO teacherPlainDTO) {
        if (teacherPlainDTO.getType()==0) {
            teachDao.addTeacherPlainEntry(teacherPlainDTO.buildTeacherPlainEntry());
        } else if (teacherPlainDTO.getType()==1) {
            teachDao.updateTeacherPlainEntry(new ObjectId(teacherPlainDTO.getId()),teacherPlainDTO.buildTeacherPlainEntry());
        }

    }

    /**
     * 查询教学计划
     * @param term
     * @param planName
     * @param userId
     * @return
     */
    public List<TeacherPlainDTO> selTeachPlanList(String term, String planName, ObjectId userId) {
        List<TeacherPlainDTO> teacherPlainDTOs = new ArrayList<TeacherPlainDTO>();
        List<TeacherPlainEntry> teacherPlainEntryList = teachDao.selTeacherPlainEntryList(term,userId,planName);
        if (teacherPlainEntryList!=null && teacherPlainEntryList.size()!=0) {
            for (TeacherPlainEntry entry : teacherPlainEntryList) {
                teacherPlainDTOs.add(new TeacherPlainDTO(entry));
            }
        }
        return teacherPlainDTOs;
    }

    /**
     * 查询单个计划
     * @param id
     * @return
     */
    public TeacherPlainDTO selSinglePlan(ObjectId id) {
        TeacherPlainEntry teacherPlainEntry = teachDao.selTeacherPlainEntry(id);
        TeacherPlainDTO teacherPlainDTO = new TeacherPlainDTO();
        if (teacherPlainEntry!=null) {
            teacherPlainDTO = new TeacherPlainDTO(teacherPlainEntry);
        }
        return teacherPlainDTO;
    }

    /**
     * 删除计划
     * @param id
     * @param userId
     */
    public void delPlan(ObjectId id,ObjectId userId) {
        teachDao.delTeacherPlainEntry(id,userId);
    }

    /**
     *
     * @param term
     * @param subjectId
     * @param userId
     * @return
     */
    public void selSingleTeacherCheck(String term, String subjectId, String userId,Map map) {
        int count = teachDao.selTeachCheckCount(term, new ObjectId(subjectId), new ObjectId(userId));
        if (count == 0) {
            List<TeachProjectEntry> teachProjectList = new ArrayList<TeachProjectEntry>();
            for (String desc : TeacherProject.getTeacherProject()) {
                teachProjectList.add(new TeachProjectEntry(desc,0,"",""));
            }
            teachDao.addTeachCheckEntry(new TeachCheckEntry(term,new ObjectId(userId),new ObjectId(subjectId),teachProjectList));
        }
        TeachCheckEntry teachCheckEntry = teachDao.selTeachCheckEntry(term, new ObjectId(subjectId), new ObjectId(userId));
        map.put("time", DateTimeUtils.convert(teachCheckEntry.getLastTime(),DateTimeUtils.DATE_YYYY_MM_DD));
        int planCnt = teachDao.selTeacherPlainEntryList(term,new ObjectId(userId),"").size();
        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        ownerList.add(new ObjectId(userId));
        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), DirType.BACK_UP.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
        int lessonCnt = lessonService.count(dirids);
        List<TeacherProjectDTO> teacherProjectDTOs = new ArrayList<TeacherProjectDTO>();
        for (TeachProjectEntry entry : teachCheckEntry.getTeachProjectList()) {
            teacherProjectDTOs.add(new TeacherProjectDTO(entry,teachCheckEntry.getTeacherId(),planCnt,lessonCnt,term));
        }
        map.put("id",teachCheckEntry.getID().toString());
        map.put("rows",teacherProjectDTOs);

    }

    /**
     * 更新评分项的值
     * @param id
     * @param value
     * @param type
     */
    public void updateProjectValue(ObjectId id, String value, int type,String projectName) {
        teachDao.updateProjectValue(id,value,type,projectName);
    }
}
