package com.fulaan.quality.service;

import com.db.quality.QualityDao;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.fulaan.base.service.DirService;
import com.fulaan.learningcenter.service.ExerciseItemService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.quality.dto.TeacherQualityDTO;
import com.fulaan.smartcard.dto.ClassDTO;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.Quality.QualityEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonDTO;
import com.pojo.lesson.LessonEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.Grade;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wang_xinxin on 2016/10/24.
 */

@Service
public class TeacherQualityService {

    private UserDao userDao = new UserDao();

    private SchoolDao schoolDao = new SchoolDao();

    private ClassDao classDao = new ClassDao();

    private DirService dirService =new DirService();

    private LessonService lessonService =new LessonService();

    private ExerciseItemService exerciseItemService =new ExerciseItemService();

    private QualityDao qualityDao = new QualityDao();

    /**
     *
     * @param term
     * @param gradeId
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    public void selTeacherQualityList(ObjectId schoolId,String term, String gradeId, String userName, int page, int pageSize,Map<String,Object> model) {
        Map<ObjectId,UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        Map<ObjectId,Grade> gradeMap = new HashMap<ObjectId, Grade>();
        Map<ObjectId,List<ObjectId>> userGradeMap = new HashMap<ObjectId, List<ObjectId>>();
        List<ObjectId> user2 = new ArrayList<ObjectId>();
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolIdUserName(schoolId,userName,new BasicDBObject("nm",1));
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                userEntryMap.put(user.getID(),user);
                user2.add(user.getID());
            }
        }
        List<Grade> grades = schoolDao.findSchoolInfoBySchoolId(schoolId);
        if (grades!=null && grades.size()!=0) {
            for (Grade grade : grades) {
                gradeMap.put(grade.getGradeId(),grade);
            }
        }
        List<ObjectId> gradeList = new ArrayList<ObjectId>();
        if (!StringUtils.isEmpty(gradeId)) {
            gradeList.add(new ObjectId(gradeId));
        } else {
            if (grades!=null && grades.size()!=0) {
                for (Grade grade : grades) {
                    gradeList.add(grade.getGradeId());
                }
            }
        }
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeList);
        if (classEntryList!=null && classEntryList.size()!=0) {
            for (ClassEntry classEntry : classEntryList) {
                if (classEntry.getTeachers()!=null && classEntry.getTeachers().size()!=0) {
//                    userIds.addAll(classEntry.getTeachers());
                    for (ObjectId userId : classEntry.getTeachers()) {
                        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
                        if (user2.contains(userId)) {
                            if (userGradeMap!=null && userGradeMap.get(userId)!=null) {
                                gradeIds = userGradeMap.get(userId);
//                            gradeIds.add(classEntry.getGradeId());
                            }
                            userIds.add(userId);
                            gradeIds.add(classEntry.getGradeId());
                            userGradeMap.put(userId,gradeIds);
                        }
                    }
                }

            }
        }
        List<TeacherQualityDTO> teacherQualityDTOs = new ArrayList<TeacherQualityDTO>();
        List<LessonDTO> retList =new ArrayList<LessonDTO>();
        userIds = removeDuplicate(userIds);
        model.put("total", userIds.size());
        List<ObjectId> userIdList = listImitatePage(userIds,page,pageSize);
        Map<ObjectId,QualityEntry> qualityEntryMap = new HashMap<ObjectId, QualityEntry>();
        List<QualityEntry> qualityEntries = qualityDao.selQualityEntryByTeas(term, userIdList);
        if (qualityEntries!=null && qualityEntries.size()!=0) {
            for (QualityEntry quality : qualityEntries) {
                qualityEntryMap.put(quality.getTeacherId(),quality);
            }
        }
        if (userIdList!=null && userIdList.size()!=0) {
            for (ObjectId userId : userIdList) {
                retList =new ArrayList<LessonDTO>();
                TeacherQualityDTO teacherQualityDTO = new TeacherQualityDTO();
                teacherQualityDTO.setTeacherId(userId.toString());
                teacherQualityDTO.setTeacherName(userEntryMap.get(userId).getUserName());
                List<ObjectId> gradeList2 = userGradeMap.get(userId);
                String gradeName = "";
//                if (gradeList2!=null && gradeList2.size()!=0) {
//                    for (ObjectId gid : gradeList2) {
//                        gradeName += gradeMap.get(gid).getName()+" ";
//                    }
//                }
                if (classEntryList!=null && classEntryList.size()!=0) {
                    for (ClassEntry classEntry : classEntryList) {
                        if (classEntry.getTeachers()!=null && classEntry.getTeachers().size()!=0) {
                            for (ObjectId uid : classEntry.getTeachers()) {
                                if (uid.equals(userId)) {
                                    gradeName = gradeMap.get(classEntry.getGradeId()).getName();
                                }
                            }
                        }
                    }
                }
                teacherQualityDTO.setGradeName(gradeName);
                List<ObjectId> ownerList = new ArrayList<ObjectId>();
                ownerList.add(userId);
                List<DirEntry> dirList =dirService.getDirEntryList(ownerList,new BasicDBObject(Constant.ID,1), DirType.BACK_UP.getType());
                List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
                long startTime = DateTimeUtils.getStrToLongTime(term.substring(0, 4) + "/08/30 00:00", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
                long endTime = DateTimeUtils.getStrToLongTime((Integer.valueOf(term.substring(0,4))+1)+"/08/29 23:59",DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
                List<LessonEntry> lessonList = lessonService.getLessonEntryList(dirids,startTime,endTime, Constant.FIELDS);
                Map<ObjectId, LessonDTO> map = attachExeitemCount(lessonList);
                retList.addAll(map.values());
                int wareCnt = 0;
                int examCnt = 0;
                if (retList!=null && retList.size()!=0) {
                    for (LessonDTO lessonDTO : retList) {
                        wareCnt += lessonDTO.getDocumentCount();
                        examCnt += lessonDTO.getExerciseCount();
                    }
                }
                if (qualityEntryMap.get(userId)==null) {
                    teacherQualityDTO.setType(0);
                    teacherQualityDTO.setScore("");
                    teacherQualityDTO.setComment("");
                } else {
                    if (StringUtils.isEmpty(qualityEntryMap.get(userId).getComment())) {
                        teacherQualityDTO.setType(0);
                    } else {
                        teacherQualityDTO.setType(1);
                    }
                    teacherQualityDTO.setScore(qualityEntryMap.get(userId).getScore());
                    teacherQualityDTO.setComment(qualityEntryMap.get(userId).getComment());
                }
                teacherQualityDTO.setWareCnt(wareCnt);
                teacherQualityDTO.setExamCnt(examCnt);
                teacherQualityDTO.setLessonCnt(lessonService.count(dirids));
                teacherQualityDTOs.add(teacherQualityDTO);
            }
        }
        model.put("rows",teacherQualityDTOs);
    }

    /**
     * 为 LessonEntry list 添加练习数目
     * @param lessonList
     * @return
     */
    private Map<ObjectId, LessonDTO> attachExeitemCount(
            List<LessonEntry> lessonList) {
        Map<ObjectId, LessonDTO> map =new HashMap<ObjectId, LessonDTO>();
        LessonDTO dto;
        for (LessonEntry e : lessonList) {
            dto=new LessonDTO(e);
            if(null==e.getExercise())
            {
                map.put(new ObjectId(), dto);
            }
            else
            {
                map.put(e.getExercise(), dto);
            }
        }
        Map<ObjectId, Integer> countMap= exerciseItemService.statItemCount(map.keySet());
        if(countMap.size()>0)
        {
            for(Map.Entry<ObjectId, Integer> entry:countMap.entrySet())
            {
                dto=map.get(entry.getKey());
                if(null!=dto)
                {
                    dto.setExerciseCount(entry.getValue());
                }
            }
        }
        return map;
    }

    /**
     * List去重
     * @param list
     * @return
     */
    public List<ObjectId> removeDuplicate(List<ObjectId> list)   {
        HashSet h  =   new  HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 模拟对list分页查询
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    private List<ObjectId> listImitatePage(List<ObjectId> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<ObjectId> subList=new ArrayList<ObjectId>();
        if(list!=null&&list.size()>0) {
            if (m == 0) {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            } else {
                if (page == pageCount) {
                    subList = list.subList((page - 1) * pageSize, totalCount);
                } else {
                    subList = list.subList((page - 1) * pageSize, pageSize * (page));
                }
            }
        }
        return subList;
    }

    /**
     * 编辑评价，打分
     * @param team
     * @param comment
     * @param teacherId
     * @param score
     * @param type
     */
    public void addTeacherComment(String team, String comment, ObjectId teacherId, String score, int type) {
        QualityEntry qualityEntry = qualityDao.selQualityEntry(team, teacherId);
        if (qualityEntry==null) {
            qualityDao.addQualityEntry(new QualityEntry(team,teacherId,comment,score));
        } else {
            qualityDao.updateQualityEntry(team,type,teacherId,comment,score);
        }
    }

}
