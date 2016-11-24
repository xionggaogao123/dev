package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.zouban.*;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.dto.XuanKeDTO;
import com.fulaan.zouban.dto.XuanKeSubjectDtailDTO;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserEntry;
import com.pojo.zouban.*;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wang_xinxin on 2015/9/23.
 */

@Service
public class XuanKeService {

    private ClassDao classDao = new ClassDao();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private StudentXuankeDao studentXuankeDao = new StudentXuankeDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private ScoreDao scoreDao = new ScoreDao();

    @Autowired
    private UserService userService = new UserService();
    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private TimeTableService timetableService;

    /**
     * 选课配置
     *
     * @param term
     * @param gradeId
     * @param schoolId
     * @return
     */
    public XuankeConfEntry findXuanKeConfEntry(String term, String gradeId, String schoolId) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        if (xuankeConfEntry == null) {
            xuankeConfEntry = new XuankeConfEntry(new ObjectId(schoolId), term, new ObjectId(gradeId));
            xuanKeConfDao.addXuanKeConf(xuankeConfEntry);
        }
        return xuankeConfEntry;
    }


    /**
     * 走班选课列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public XuanKeDTO findXuanKeConf(String term, String gradeId, int type, String schoolId) {
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        if (xuankeEntry != null) {
            List<SubjectConfEntry> subjectConflist = subjectConfDao.findSubjectConf(xuankeEntry.getID(), type);
            List<SubjectConfDTO> subConfList = new ArrayList<SubjectConfDTO>();
            Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);
            for (SubjectConfEntry subject : subjectConflist) {
                subConfList.add(new SubjectConfDTO(subject, subjectMap.get(subject.getSubjectId()).getName()));
            }

            //按照学科等级考人数多少排序
            Collections.sort(subConfList, new Comparator<SubjectConfDTO>() {
                @Override
                public int compare(SubjectConfDTO o1, SubjectConfDTO o2) {
                    return o2.getAdvUserCount() - o1.getAdvUserCount();
                }
            });

            int count = studentXuankeDao.findStudentChooseByType(xuankeEntry.getID(), 0);
            int choosecount = studentXuankeDao.findStudentChooseByType(xuankeEntry.getID(), 1);
            XuanKeDTO xuankeDto = new XuanKeDTO(xuankeEntry);
            xuankeDto.setXuankecount(count + "/" + (count + choosecount));
            xuankeDto.setSubConfList(subConfList);
            return xuankeDto;
        } else {//没有配置则默认创建一个配置
            XuankeConfEntry xuankeConfEntry = new XuankeConfEntry(new ObjectId(schoolId), term, new ObjectId(gradeId));
            xuanKeConfDao.addXuanKeConf(xuankeConfEntry);
            return new XuanKeDTO(xuankeConfEntry);
        }
    }


    /**
     * 公布/取消公布
     *
     * @param xuanKeId
     */
    public int release(String xuanKeId) {
        XuankeConfEntry xuanKeConfEntry = xuanKeConfDao.findXuanKeConfByXuanKeId(new ObjectId(xuanKeId));
        if (xuanKeConfEntry.getIsRelease() == 0) {
            String term = xuanKeConfEntry.getTerm();
            ObjectId gradeId = xuanKeConfEntry.getGradeId();
            //删除学生选课结果
            studentXuankeDao.removeStudentChooseEntry(new ObjectId(xuanKeId));
            //更新学生列表
            updateStudentList(new ObjectId(xuanKeId), gradeId);
            //清空学生等级考成绩（分层）
            scoreDao.clearStudentScore(term, gradeId);
            //清空走班课
            zouBanCourseDao.removeCourseByType(term, gradeId, ZoubanType.ZOUBAN.getType());
            //清空课表
            timetableService.deleteAllCourse(term, gradeId.toString());

            List<ObjectId> subjectObjIds = new ArrayList<ObjectId>();
            List<SubjectConfEntry> subjectConfList = subjectConfDao.findSubjectConf(xuanKeConfEntry.getID(), ZoubanType.ZOUBAN.getType());
            if (subjectConfList != null && subjectConfList.size() != 0) {
                for (SubjectConfEntry subject : subjectConfList) {
                    subjectObjIds.add(subject.getSubjectId());
                }
            }
            //清空学科配置中的学生列表
            subjectConfDao.removeXuankeStudent(xuanKeConfEntry.getID(), subjectObjIds);
            xuanKeConfDao.isRelease(new ObjectId(xuanKeId), 1);
            return 1;
        } else {
            studentXuankeDao.removeStudentChooseEntry(new ObjectId(xuanKeId));
            xuanKeConfDao.isRelease(new ObjectId(xuanKeId), 0);
            zoubanStateService.setZoubanState(xuanKeConfEntry.getTerm(), xuanKeConfEntry.getGradeId().toString(), 2);
            zoubanStateService.setZoubanSubState(xuanKeConfEntry.getTerm(), xuanKeConfEntry.getGradeId().toString(), 3);
            return 0;
        }
    }

    /**
     * 更新选课学生列表
     * @param xuanKeId
     * @param gradeId
     */
    public void updateStudentList(ObjectId xuanKeId, ObjectId gradeId) {
        List<ObjectId> gradeids = new ArrayList<ObjectId>();
        gradeids.add(gradeId);
        List<ClassEntry> classlist = classDao.findClassEntryByGradeIds(gradeids);

        for (ClassEntry cls : classlist) {
            List<ObjectId> studentIdList = cls.getStudents();
            if (studentIdList != null && studentIdList.size() != 0) {
                Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(studentIdList, new BasicDBObject("nm", 1).append("sex", 1));
                for (ObjectId uid : cls.getStudents()) {
                    StudentChooseEntry studentChooseEntry = new StudentChooseEntry(xuanKeId, uid,
                            new ArrayList<ObjectId>(), new ArrayList<ObjectId>(), userMap.get(uid).getUserName(), cls.getID(), 0);
                    studentXuankeDao.addStudentChooseEntry(studentChooseEntry);
                }
            }
        }
    }


    /**
     * 选课记录更新
     *
     */
    public ObjectId updateXuanKeConf(String term, String gradeId, String startDate, String endDate, String schoolId) {
        final ObjectId gid = new ObjectId(gradeId);
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConf(term, gid);
        ObjectId xuankeId = null;
        if (xuankeEntry != null) {
            xuankeId = xuankeEntry.getID();
            xuanKeConfDao.updateXuanKeConf(xuankeEntry.getID(), DateTimeUtils.stringToDate(startDate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM).getTime(), DateTimeUtils.stringToDate(endDate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM).getTime());
        } else {
            long startTime = DateTimeUtils.stringToDate(startDate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM).getTime();
            long endTime = DateTimeUtils.stringToDate(endDate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM).getTime();
            XuankeConfEntry xuankeConfEntry = new XuankeConfEntry(new ObjectId(schoolId), term, gid, startTime, endTime);
            xuankeId = xuanKeConfDao.addXuanKeConf(xuankeConfEntry);
            updateStudentList(xuankeId, gid);
        }
        return xuankeId;
    }


    /**
     * 选课明细
     *
     * @param xuanKeId
     * @param subjectId
     * @param classId
     */
    public Map xuanKeSubjectDetail(String xuanKeId, String subjectId, String classId, String schoolId, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<XuanKeSubjectDtailDTO> stulist = new ArrayList<XuanKeSubjectDtailDTO>();
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConfByXuanKeId(new ObjectId(xuanKeId));
        List<SubjectConfEntry> subjectConflist = subjectConfDao.findStudentCourseList(xuanKeId, subjectId, classId, type);

        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        if (subjectConflist != null && subjectConflist.size() != 0) {
            for (SubjectConfEntry subjectConf : subjectConflist) {
                List<IdValuePair> advlist = subjectConf.getAdvUsers();
                List<IdValuePair> simlist = subjectConf.getSimUsers();
                if (advlist != null && advlist.size() != 0) {
                    for (IdValuePair pair : advlist) {
                        XuanKeSubjectDtailDTO detail = new XuanKeSubjectDtailDTO();
                        if (org.apache.commons.lang.StringUtils.isBlank(classId)) {
                            detail.setUserid(pair.getId().toString());
                            detail.setClassId(pair.getValue().toString());
                            detail.setType(1);
                            detail.setSubjectId(subjectConf.getSubjectId().toString());
                            stulist.add(detail);
                            classIds.add(new ObjectId(pair.getValue().toString()));
                            userIds.add(pair.getId());
                        } else if (classId.equals(pair.getValue().toString())) {
                            detail.setUserid(pair.getId().toString());
                            detail.setClassId(pair.getValue().toString());
                            detail.setType(1);
                            detail.setSubjectId(subjectConf.getSubjectId().toString());
                            stulist.add(detail);
                            classIds.add(new ObjectId(pair.getValue().toString()));
                            userIds.add(pair.getId());
                        }
                    }
                }
                if (simlist != null && simlist.size() != 0) {
                    for (IdValuePair pair : simlist) {
                        XuanKeSubjectDtailDTO detail = new XuanKeSubjectDtailDTO();
                        if (org.apache.commons.lang.StringUtils.isBlank(classId)) {
                            detail.setUserid(pair.getId().toString());
                            detail.setClassId(pair.getValue().toString());
                            detail.setSubjectId(subjectConf.getSubjectId().toString());
                            detail.setType(2);
                            stulist.add(detail);
                            classIds.add(new ObjectId(pair.getValue().toString()));
                            userIds.add(pair.getId());
                        } else if (classId.equals(pair.getValue().toString())) {
                            detail.setUserid(pair.getId().toString());
                            detail.setClassId(pair.getValue().toString());
                            detail.setSubjectId(subjectConf.getSubjectId().toString());
                            detail.setType(2);
                            stulist.add(detail);
                            classIds.add(new ObjectId(pair.getValue().toString()));
                            userIds.add(pair.getId());
                        }
                    }
                }
            }
            Map<ObjectId, UserEntry> userMap = null;
            Map<ObjectId, ClassEntry> classMap = null;
            if (userIds != null && userIds.size() != 0) {
                List<ObjectId> listTemp = new ArrayList<ObjectId>();
                Iterator<ObjectId> it = userIds.iterator();
                while (it.hasNext()) {
                    ObjectId a = it.next();
                    if (listTemp.contains(a)) {
                        it.remove();
                    } else {
                        listTemp.add(a);
                    }
                }
                userMap = userService.getUserEntryMap(userIds, new BasicDBObject("nm", 1).append("sex", 1));

            }
            if (classIds != null && classIds.size() != 0) {
                List<ObjectId> listTemp = new ArrayList<ObjectId>();
                Iterator<ObjectId> it = classIds.iterator();
                while (it.hasNext()) {
                    ObjectId a = it.next();
                    if (listTemp.contains(a)) {
                        it.remove();
                    } else {
                        listTemp.add(a);
                    }
                }
                classMap = classService.getClassEntryMap(classIds, new BasicDBObject("nm", 1));
            }
            Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);
            for (XuanKeSubjectDtailDTO dto : stulist) {
                UserEntry user = userMap.get(new ObjectId(dto.getUserid()));
                dto.setUsername(user.getUserName());
                dto.setSex(dto.getSex());
                if (dto.getType() == 1) {
                    dto.setCourseName(subjectMap.get(new ObjectId(dto.getSubjectId())).getName() + "-等级考");
                } else {
                    dto.setCourseName(subjectMap.get(new ObjectId(dto.getSubjectId())).getName() + "-合格考");
                }
                dto.setClassName(classMap.get(new ObjectId(dto.getClassId())).getName());

            }
        }
        map.put("xuanke", new XuanKeDTO(xuankeEntry));
        map.put("sublist", stulist);
        return map;
    }

    /**
     * 选课进度列表
     *
     * @param xuanKeId
     * @param classId
     * @param choose
     * @param userName
     * @return
     */
    public List<XuanKeSubjectDtailDTO> studentXuanKeList(String xuanKeId, String classId, int choose, String userName, String schoolId) {
        List<StudentChooseEntry> chooseEntries = new ArrayList<StudentChooseEntry>();
        List<StudentChooseEntry> chooselist = studentXuankeDao.studentXuanKeList(new ObjectId(xuanKeId), new ObjectId(classId), choose, userName);
        List<ObjectId> uIds = new ArrayList<ObjectId>();
        for (StudentChooseEntry studentChooseEntry : chooselist) {
            uIds.add(studentChooseEntry.getUserId());
        }
        List<IdNameDTO> idNameDTOs = userService.findUserIdNameByIds(uIds);
        for (int i = 0; i < chooselist.size(); i++) {
            StudentChooseEntry studentChooseEntry = chooselist.get(i);
            for (IdNameDTO idNameDTO : idNameDTOs) {
                if (idNameDTO.getId().equals(studentChooseEntry.getUserId().toString())) {
                    studentChooseEntry.setUserName(idNameDTOs.get(chooselist.size() - i - 1).getName());
                    break;
                }
            }

            chooseEntries.add(studentChooseEntry);
        }
        return setStudentDetail(chooseEntries, schoolId);
    }

    /**
     * 详细
     *
     * @param chooselist
     * @param schoolId
     * @return
     */
    public List<XuanKeSubjectDtailDTO> setStudentDetail(List<StudentChooseEntry> chooselist, String schoolId) {
        List<XuanKeSubjectDtailDTO> detailist = new ArrayList<XuanKeSubjectDtailDTO>();
        XuanKeSubjectDtailDTO xuanKeSubjectDtailDTO = null;
        if (chooselist != null && chooselist.size() != 0) {
            List<ObjectId> classIds = new ArrayList<ObjectId>();
            Map<ObjectId, ClassEntry> classMap = null;
            List<ClassEntry> classlist = classDao.findClassInfoBySchoolId(new ObjectId(schoolId), null);
            for (ClassEntry classty : classlist) {
                classIds.add(classty.getID());
            }
            classMap = classService.getClassEntryMap(classIds, new BasicDBObject("nm", 1));
            Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);
            for (StudentChooseEntry chooseEntry : chooselist) {
                xuanKeSubjectDtailDTO = new XuanKeSubjectDtailDTO();
                xuanKeSubjectDtailDTO.setUsername(chooseEntry.getUserName());
                xuanKeSubjectDtailDTO.setUserid(chooseEntry.getUserId().toString());
                xuanKeSubjectDtailDTO.setClassId(chooseEntry.getClassId().toString());
                xuanKeSubjectDtailDTO.setClassName(classMap.get(chooseEntry.getClassId()).getName());
                xuanKeSubjectDtailDTO.setAdvName("");
                if (chooseEntry.getAdvancelist() != null && chooseEntry.getAdvancelist().size() != 0) {
                    String subject = "";
                    for (ObjectId sub : chooseEntry.getAdvancelist()) {
                        subject += subjectMap.get(sub).getName() + "、";
                    }
                    xuanKeSubjectDtailDTO.setAdvName(subject.substring(0, subject.length() - 1));
                }
                xuanKeSubjectDtailDTO.setSimName("");
                if (chooseEntry.getSimplelist() != null && chooseEntry.getSimplelist().size() != 0) {
                    String subject = "";
                    for (ObjectId sub : chooseEntry.getSimplelist()) {
                        subject += subjectMap.get(sub).getName() + "、";
                    }
                    xuanKeSubjectDtailDTO.setSimName(subject.substring(0, subject.length() - 1));
                }
                detailist.add(xuanKeSubjectDtailDTO);
            }
        }
        return detailist;
    }


    /**
     * 更新选课说明
     *
     * @param xuankeId
     * @param info
     */
    public void updateXuanKeInfo(ObjectId xuankeId, String info) {
        xuanKeConfDao.updateXuanKeInfo(xuankeId, info);
    }


    /**
     * 是否公布
     *
     * @param xuankeId
     * @return
     */
    public boolean isRelease(ObjectId xuankeId) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConfByXuanKeId(xuankeId);
        if (xuankeConfEntry != null) {
            return xuankeConfEntry.getIsRelease() == 1;
        } else {
            return false;
        }
    }


}
