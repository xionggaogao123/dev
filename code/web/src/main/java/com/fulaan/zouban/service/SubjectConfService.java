package com.fulaan.zouban.service;

import com.db.classroom.ClassroomDao;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.db.zouban.SubjectConfDao;
import com.db.zouban.TimeTableDao;
import com.db.zouban.XuanKeConfDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.*;
import com.mongodb.BasicDBObject;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Subject;
import com.pojo.user.UserEntry;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangkaidong on 2016/9/30.
 * <p/>
 * 学科配置Service
 */
@Service
public class SubjectConfService {
    ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    SubjectConfDao subjectConfDao = new SubjectConfDao();
    UserDao userDao = new UserDao();
    ClassroomDao classroomDao = new ClassroomDao();
    ClassDao classDao = new ClassDao();
    TimeTableDao timeTableDao = new TimeTableDao();


    @Autowired
    ClassService classService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    ZoubanStateService zoubanStateService;
    @Autowired
    PaikeService paikeService;
    @Autowired
    private EventConflictService eventConflictService;

    /**
     * 获取学科配置
     *
     * @param subjectConfId
     * @return
     */
    public SubjectConfDTO findSubjectConf(String subjectConfId) {
        SubjectConfEntry subjectConfEntry = subjectConfDao.findSubjectConfEntry(new ObjectId(subjectConfId));
        return new SubjectConfDTO(subjectConfEntry, "");
    }


    /**
     * 学科配置&学生选课列表
     *
     * @param term
     * @param gradeId
     * @param type
     * @param schoolId
     * @return
     */
    public List<SubjectConfDTO> getSubjectConfList(String term, String gradeId, int type, String schoolId) {
        List<SubjectConfDTO> subConfList = new ArrayList<SubjectConfDTO>();
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        if (xuankeEntry != null) {
            subConfList.addAll(findSubjectConfList(schoolId, gradeId, type, xuankeEntry.getID()));
        }

        return subConfList;
    }


    /**
     * 学科配置列表
     *
     * @param schoolId
     * @param gradeId
     * @param type
     * @param xuankeId
     * @return
     */
    public List<SubjectConfDTO> findSubjectConfList(String schoolId, String gradeId, int type, ObjectId xuankeId) {
        List<SubjectConfDTO> subConfList = new ArrayList<SubjectConfDTO>();

        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);


        if (type == ZoubanType.ZOUBAN.getType() || type == ZoubanType.FEIZOUBAN.getType()) {
            List<SubjectConfEntry> entryList = subjectConfDao.findSubjectConf(xuankeId, type);

            for (SubjectConfEntry subjectConfEntry : entryList) {
                if (subjectMap.containsKey(subjectConfEntry.getSubjectId())) {
                    Subject subject1 = subjectMap.get(subjectConfEntry.getSubjectId());
                    subConfList.add(new SubjectConfDTO(subjectConfEntry, subject1.getName()));
                }
            }
        } else if (type == ZoubanType.GROUPZOUBAN.getType()) {
            List<SubjectConfEntry> entryList = subjectConfDao.findSubjectConf(xuankeId, type);
            for (SubjectConfEntry subject : entryList) {
                if (subjectMap.containsKey(subject.getSubjectId())) {
                    SubjectConfDTO subjectConfDTO = new SubjectConfDTO(subject, subjectMap.get(subject.getSubjectId()).getName());
                    List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
                    StringBuffer str = new StringBuffer();
                    for (String classId : subjectConfDTO.getClassList()) {
                        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                            if (classId.equals(classInfoDTO.getId().toString())) {
                                str.append(classInfoDTO.getClassName() + "/");
                                break;
                            }
                        }
                    }
                    subjectConfDTO.setClassGroupName(str.toString().substring(0, str.lastIndexOf("/")));
                    subConfList.add(subjectConfDTO);
                }
            }
        }

        return subConfList;
    }


    /**
     * 新增或更新走班课学科配置
     *
     * @param subjectConfDTO
     */
    public void addOrUpdateSubConf(SubjectConfDTO subjectConfDTO, ObjectId schoolId) throws Exception {
        String term = subjectConfDTO.getTerm();
        String gradeId = subjectConfDTO.getGradeId();

        if (StringUtils.isEmpty(subjectConfDTO.getSubjectConfId())) {//新增
            int count = subjectConfDao.checkSubjectConf(subjectConfDTO.getXuankeId(), subjectConfDTO.getSubjectId(), subjectConfDTO.getType());
            if (count > 0) {
                throw new RuntimeException("学科已存在，不能重复添加");
            }

            if (subjectConfDTO.getType() == ZoubanType.ZOUBAN.getType()) {
                XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
                //取消发布
                xuanKeConfDao.isRelease(xuankeConfEntry.getID(), 0);
                //进度设置到第二步
                zoubanStateService.setZoubanState(term, gradeId.toString(), 2);
                //删除走班课
                zouBanCourseDao.removeCourseByType(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());
            } else if (subjectConfDTO.getType() == ZoubanType.FEIZOUBAN.getType()) {
                //添加非走班课
                addFeiZoubanCourseEntry(subjectConfDTO, schoolId);
            }
            subjectConfDao.addSubjectConf(subjectConfDTO.exportEntry());
        } else {
            //更新课时，如果课时减少，弹出相应的课程；如果课时增加则不做处理

            //更新学科配置中的课时
            subjectConfDao.updateSubjectConf(new ObjectId(subjectConfDTO.getSubjectConfId()),
                    subjectConfDTO.getAdvanceTime(), subjectConfDTO.getSimpleTime());

            //查询相应的教学班
            List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId),
                    null, new ObjectId(subjectConfDTO.getSubjectId()), subjectConfDTO.getType());

            //调整课表中对应的课
            if (subjectConfDTO.getType() == ZoubanType.ZOUBAN.getType()) {
                for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                    if (zouBanCourseEntry.getLevel() == 1 &&
                            zouBanCourseEntry.getLessonCount() > subjectConfDTO.getAdvanceTime()) {//等级考
                        timeTableDao.removeCourseById(term, null, zouBanCourseEntry.getID());
                    }
                    if (zouBanCourseEntry.getLevel() == 2 &&
                            zouBanCourseEntry.getLessonCount() > subjectConfDTO.getSimpleTime()) {//合格考
                        timeTableDao.removeCourseById(term, null, zouBanCourseEntry.getID());
                    }
                }

            } else if (subjectConfDTO.getType() == ZoubanType.FEIZOUBAN.getType()) {
                for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                    if(zouBanCourseEntry.getLessonCount() > subjectConfDTO.getAdvanceTime()) {
                        timeTableDao.removeCourseById(term, zouBanCourseEntry.getClassId().get(0), zouBanCourseEntry.getID());
                    }
                }
            }

            //更新教学班课时
            zouBanCourseDao.updateZoubanCourse(subjectConfDTO.getTerm(), new ObjectId(subjectConfDTO.getGradeId()),
                    new ObjectId(subjectConfDTO.getSubjectId()), subjectConfDTO.getType(), subjectConfDTO.getAdvanceTime());
        }
    }

    private void addFeiZoubanCourseEntry(SubjectConfDTO subjectConfDTO, ObjectId schoolId) throws Exception {
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(new ObjectId(subjectConfDTO.getGradeId()));

        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();

        for (ClassEntry cls : classEntries) {
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            classIdList.add(cls.getID());
            ObjectId subjectId = new ObjectId(subjectConfDTO.getSubjectId());
            ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(schoolId, subjectId,
                    subjectConfDTO.getTerm(), new ObjectId(subjectConfDTO.getGradeId()), classIdList, null,
                    subjectConfDTO.getSubjectName(), subjectConfDTO.getAdvanceTime(),
                    cls.getStudents(), null, subjectConfDTO.getType());

            //todo 查询绑定的班级
            ClassroomEntry classRoomEntry = getClassRoom(cls.getID());
            if (classRoomEntry == null) {
                throw new Exception("请先设置教室");
            } else {
                zouBanCourseEntry.setClassRoomId(classRoomEntry.getID());
            }

            zouBanCourseEntryList.add(zouBanCourseEntry);
        }

        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
        }

    }

    public ClassroomEntry getClassRoom(ObjectId id) {
        return classroomDao.findClassroomByClassId(id);
    }


    /**
     * 删除选课学科
     *
     * @param subjectConfId
     */
    public void deleteSubjectConf(String subjectConfId) {
        SubjectConfEntry subjectConfEntry = subjectConfDao.findSubjectConfEntry(new ObjectId(subjectConfId));
        //选课id
        ObjectId xuankeId = subjectConfEntry.getXuanKeId();
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConfByXuanKeId(xuankeId);
        String term = xuankeConfEntry.getTerm();
        ObjectId gradeId = xuankeConfEntry.getGradeId();

        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseListBySubjectId(term, gradeId, null, subjectConfEntry.getSubjectId(), subjectConfEntry.getType());


        if (subjectConfEntry.getType() == ZoubanType.FEIZOUBAN.getType()) {
            //删除教学班
            zouBanCourseDao.removeCourseBySubjectId(term, gradeId, ZoubanType.FEIZOUBAN.getType(), subjectConfEntry.getSubjectId(), null);
        } else if (subjectConfEntry.getType() == ZoubanType.ZOUBAN.getType()) {
            zouBanCourseDao.removeCourseByType(term, gradeId, ZoubanType.ZOUBAN.getType());
            //取消发布
            xuanKeConfDao.isRelease(xuankeId, 0);
            //进度设置到第二步
            zoubanStateService.setZoubanState(term, gradeId.toString(), 2);
        }

        //清空课表
        if (subjectConfEntry.getType() == ZoubanType.ZOUBAN.getType()) {
            paikeService.clearTimetableCourse(term, gradeId.toString(), 1, null);
        } else if (subjectConfEntry.getType() == ZoubanType.FEIZOUBAN.getType()) {
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                timeTableDao.removeCourseById(term, zouBanCourseEntry.getClassId().get(0), zouBanCourseEntry.getID());
            }
        }

        subjectConfDao.deleteSubjectConf(new ObjectId(subjectConfId));
    }


    //--------------------------------------------------分组走班课设置--------------------------------------------------------

    public ZouBanCourseEntry getZouBanCourseEntry(ObjectId id) {
        return zouBanCourseDao.getCourseInfoById(id);
    }

    /**
     * 获取分组走班分配学生的行政班列表
     *
     * @param zouBanCourseEntry
     * @return
     */
    public List<IdNameDTO> getIdNameDTO(ZouBanCourseEntry zouBanCourseEntry) {
        List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
        List<ObjectId> objectIds = zouBanCourseEntry.getClassId();
        for (ObjectId objectId : objectIds) {
            IdNameDTO idNameDTO = new IdNameDTO();
            idNameDTO.setId(objectId.toString());
            ClassEntry classEntry = classService.getClassEntryById(objectId, Constant.FIELDS);
            idNameDTO.setName(classEntry.getName());
            idNameDTOs.add(idNameDTO);
        }
        return idNameDTOs;
    }


    public void addZoubanList(List<ZouBanCourseEntry> zouBanCourseEntries) {
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
        }

    }

    public List<ZouBanCourseEntry> getZouBanCourseEntry(String group, String term, String gradeId) {
        return zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), new ObjectId(group), null, ZoubanType.GROUPZOUBAN.getType());
    }

    /**
     * 编辑分组走班老师数据查询
     *
     * @param group
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZoubanGroupDTO> getEditFZZBTeacherList(String group, String term, String gradeId) {
        List<ZouBanCourseEntry> zouBanCourseEntries;
        List<ZoubanGroupDTO> zoubanGroupDTOs = new ArrayList<ZoubanGroupDTO>();
        zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), new ObjectId(group), null, ZoubanType.GROUPZOUBAN.getType());
        if (null != zoubanGroupDTOs) {
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                ZoubanGroupDTO zoubanGroupDTO = new ZoubanGroupDTO();
                zoubanGroupDTO.setTeId(new ObjectId().toString());
                if (StringUtils.isNotBlank(zouBanCourseEntry.getTeacherName())) {
                    zoubanGroupDTO.setTeacherName(zouBanCourseEntry.getTeacherName());
                } else {
                    zoubanGroupDTO.setTeacherName("");
                }
                if (null != zouBanCourseEntry.getTeacherId()) {
                    zoubanGroupDTO.setTeacherId(zouBanCourseEntry.getTeacherId().toString());
                } else {
                    zoubanGroupDTO.setTeacherId("");
                }
                zoubanGroupDTO.setClassName(zouBanCourseEntry.getClassName());
                if (null != zouBanCourseEntry.getClassRoomId()) {
                    zoubanGroupDTO.setClassRoomId(zouBanCourseEntry.getClassRoomId().toString());
                } else {
                    zoubanGroupDTO.setClassRoomId("");
                }
                zoubanGroupDTO.setZoubanCourseId(zouBanCourseEntry.getID().toString());
                zoubanGroupDTO.setGroup(zouBanCourseEntry.getGroup().toString());
                zoubanGroupDTO.setCrId(new ObjectId().toString());
                zoubanGroupDTOs.add(zoubanGroupDTO);
            }
        }
        return zoubanGroupDTOs;
    }


    //查询分组走板设置老师设置列表
    public List<ZouBanCourseDTO> getFZZBTeacherList(String term, String gradeId, int type, String subjectId) {
        List<ZouBanCourseEntry> zouBanCourseEntries;
        if ("All".equals(subjectId)) {
            zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), null, null, type);
        } else {
            zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), null, new ObjectId(subjectId), type);
        }
        List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();

        List<ZouBanCourseDTO> retData = new ArrayList<ZouBanCourseDTO>();
        Map<ObjectId, Integer> map = new HashMap<ObjectId, Integer>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            if (map.containsKey(zouBanCourseEntry.getGroup())) {
                int count = map.get(zouBanCourseEntry.getGroup());
                count++;
                map.put(zouBanCourseEntry.getGroup(), count);
            } else {
                map.put(zouBanCourseEntry.getGroup(), 1);
            }
            ZouBanCourseDTO zouBanCourseDTO = new ZouBanCourseDTO(zouBanCourseEntry);
            if (StringUtils.isNotBlank(zouBanCourseDTO.getClassRoomId())) {
                ClassroomEntry classRoomEntry = classroomService.findEntryById(new ObjectId(zouBanCourseDTO.getClassRoomId()));
                zouBanCourseDTO.setClassRoom(classRoomEntry.getRoomName());
            }
            zouBanCourseDTOs.add(zouBanCourseDTO);
        }

        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
            zouBanCourseDTO.setClassNumber(map.get(new ObjectId(zouBanCourseDTO.getGroupStr())));
            retData.add(zouBanCourseDTO);
        }

        return retData;
    }


    /**
     * 删除分组走班(通过subjectConfid)
     *
     * @param subjectConfId
     * @param gradeId
     * @param term
     * @param subjectId
     */
    public void removeFZZB(String subjectConfId, String gradeId, String term, String subjectId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListBySubConfigId(term, new ObjectId(gradeId), null, new ObjectId(subjectConfId),
                ZoubanType.GROUPZOUBAN.getType());
        List<ObjectId> zouBanIds = new ArrayList<ObjectId>();
        List<ObjectId> classIdLists = null;
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            zouBanIds.add(zouBanCourseEntry.getID());
            if(classIdLists == null){
                classIdLists = zouBanCourseEntry.getClassId();
            }
        }
        if(zouBanIds.size()>0){
            timeTableDao.removeCourseByIds(term, classIdLists, zouBanIds);
            //删除分组走班学科信息
            subjectConfDao.deleteSubjectConf(new ObjectId(subjectConfId));
            //删除分组走班教学班信息
            zouBanCourseDao.removeCourseBySubjectId(term, new ObjectId(gradeId),
                    ZoubanType.GROUPZOUBAN.getType(), new ObjectId(subjectId), new ObjectId(subjectConfId));
            //通过被删除的课程idList删除冲突
            eventConflictService.removeConflictByCourseIdList(zouBanIds);
        }else{
            subjectConfDao.deleteSubjectConf(new ObjectId(subjectConfId));
            //删除分组走班教学班信息
            zouBanCourseDao.removeCourseBySubjectId(term, new ObjectId(gradeId),
                    ZoubanType.GROUPZOUBAN.getType(), new ObjectId(subjectId), new ObjectId(subjectConfId));
        }


    }

    /**
     * 删除分组走班(通过课程id)
     * @param term
     * @param courseId
     */
    public void removeFZBKTeacher(String term, String courseId){
        if(null!=courseId){
            ZouBanCourseEntry  zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));
            List<ObjectId> classIdLists = zouBanCourseEntry.getClassId();
            //根据groupid和type找到同组的其他课程
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByGroup(term, zouBanCourseEntry.getGroup(), zouBanCourseEntry.getType());
            List<ObjectId> newCourseIdList = new ArrayList<ObjectId>();
            List<ObjectId> oldCourseIdList = new ArrayList<ObjectId>();
            for(ZouBanCourseEntry  entry : zouBanCourseEntries){
                if(!courseId.equals(entry.getID().toString())){
                    newCourseIdList.add(entry.getID());
                }
                oldCourseIdList.add(entry.getID());
            }
            if(newCourseIdList.size()==0){//表示这个组的课程删完了，直接移除
                timeTableDao.removeCourseByIds(term, classIdLists, oldCourseIdList);
                ObjectId scid = new ObjectId();
                scid = zouBanCourseEntry.getSubjectConfId();
                //删除分组走班学科信息
                subjectConfDao.deleteSubjectConf(scid);
            }else{
                //得到删除课程涉及到的课表
                List<TimeTableEntry> timeTableEntryList =  timeTableDao.findTimeTableList(term, classIdLists, 0);
                for(TimeTableEntry timeTableEntry : timeTableEntryList){
                    List<CourseItem> courseItems = timeTableEntry.getCourseList();
                    for(int i=0; i<courseItems.size(); i++) {
                        CourseItem courseItem = courseItems.get(i);
                        List<ObjectId> courseIds = courseItem.getCourse();
                        if (compare(courseIds, oldCourseIdList)) {//如果相等说明是一条记录，把老的修改成新的（去掉删除的之后）
                            courseItem.setCourse(newCourseIdList);
                        }
                    }
                    timeTableDao.addTimeTable(timeTableEntry);//把新的放进去

                }
            }


            //删除分组走班课教学班信息
            List<ObjectId> ids = new ArrayList<ObjectId>();
            ids.add(new ObjectId(courseId));
            zouBanCourseDao.removeCourseByIds(ids);
            //删除冲突
            eventConflictService.removeConflictByCourseIdList(ids);
        }

    }

    /**
     * compare
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b){
        if(a.size() != b.size()){
            return false;
        }else{
            Collections.sort(a);
            Collections.sort(b);
            for(int i=0;i<a.size();i++){
                if(!a.get(i).toString().equals(b.get(i).toString())){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 新增分组走班
     *
     * @param schoolId
     * @param gradeId
     * @param term
     * @param classIds
     * @param lessonCount
     * @param classNumber
     * @param subjectId
     * @param subjectName
     */
    public void addFZZB(String subjectConfId, String schoolId, String gradeId, String term, String classIds, int lessonCount,
                        int classNumber, String subjectId, String subjectName, String xuankeId) {
        //行政班id
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        List<String> temp = new ArrayList<String>();
        if (classIds.indexOf(",") > -1) {
            String[] classIdArr = classIds.split(",");
            for (String classId : classIdArr) {
                classIdList.add(new ObjectId(classId));
                temp.add(classId);
            }
        } else {
            classIdList.add(new ObjectId(classIds));
            temp.add(classIds);
        }
        if (StringUtils.isNotBlank(subjectConfId)) {//如果subjectConfId不为空说明是修改操作。当前只能修改课时。修改课时时移除课表中相关课
            SubjectConfEntry  subjectConfEntry = subjectConfDao.findSubjectConfEntry(new ObjectId(subjectConfId));
            int oldCount = subjectConfEntry.getAdvanceTime();
            subjectConfDao.updateSubjectConf(subjectConfEntry.getID(),lessonCount,subjectConfEntry.getSimpleTime());
            zouBanCourseDao.updateZoubanCourseBySubConfId(term, new ObjectId(gradeId), new ObjectId(subjectConfId), ZoubanType.GROUPZOUBAN.getType(), lessonCount);
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListBySubConfigId(term, new ObjectId(gradeId), null, new ObjectId(subjectConfId),
                    ZoubanType.GROUPZOUBAN.getType());
            List<ObjectId> zouBanIds = new ArrayList<ObjectId>();
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                zouBanIds.add(zouBanCourseEntry.getID());
            }
            if(oldCount>lessonCount){//课时变少了
                //删除分组走班教学班
                timeTableDao.removeCourseByIds(term, classIdList, zouBanIds);
                eventConflictService.removeConflictByCourseIdList(zouBanIds);
            }
        }else{
            SubjectConfDTO subjectConfDTO = new SubjectConfDTO();
            subjectConfDTO.setSubjectId(subjectId);
            subjectConfDTO.setSubjectName(subjectName);
            subjectConfDTO.setTerm(term);
            subjectConfDTO.setType(ZoubanType.GROUPZOUBAN.getType());
            subjectConfDTO.setGradeId(gradeId);
            subjectConfDTO.setXuankeId(xuankeId);
            subjectConfDTO.setAdvanceTime(lessonCount);
            subjectConfDTO.setSimpleTime(0);
            subjectConfDTO.setIfFengCeng(0);
            subjectConfDTO.setClassNumber(classNumber);
            subjectConfDTO.setClassList(temp);
            //分组走班学科设置
            ObjectId subjectConfObId = subjectConfDao.addSubjectConf(subjectConfDTO.exportEntry());
            //分组
            ObjectId group = new ObjectId();
            for (int i = 0; i < classNumber; i++) {
                ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), term, new ObjectId(gradeId),
                        classIdList, null, subjectName + "走班" + (i + 1), lessonCount, ZoubanType.GROUPZOUBAN.getType(), group, classNumber, subjectConfObId
                );
                zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
            }
        }
    }

    /**
     * 修改分组课程的具体内容（老师，教室，课程名称）
     * @param zoubanGroupDTOs
     */
    public void updateFZZBConfig(List<ZoubanGroupDTO> zoubanGroupDTOs){
        boolean removeTimeTable = false;
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        String term="";
        for(ZoubanGroupDTO zoubanGroupDTO : zoubanGroupDTOs) {
            ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(zoubanGroupDTO.getZoubanCourseId()));
            courseIds.add(zouBanCourseEntry.getID());
            //如果原来的老师id或者教室id发生变化那么就要移除课表信息，如果仅仅是课程名字变了，课表信息不变
            if(zouBanCourseEntry.getTeacherId()!=null){//当原来的老师id不为空的时候，说明是修改设置
                String teacherId = zouBanCourseEntry.getTeacherId().toString();
                String classRoomId = zouBanCourseEntry.getClassRoomId().toString();
                if (!(teacherId.equals(zoubanGroupDTO.getTeacherId()) && classRoomId.equals(zoubanGroupDTO.getClassRoomId()))) {
                    removeTimeTable = true;
                    classIdList = zouBanCourseEntry.getClassId();
                    term = zouBanCourseEntry.getTerm();
                }
            }
            //修改课程信息
            zouBanCourseEntry.setTeacherId(new ObjectId(zoubanGroupDTO.getTeacherId()));
            zouBanCourseEntry.setTeacherName(zoubanGroupDTO.getTeacherName());
            zouBanCourseEntry.setClassRoomId(new ObjectId(zoubanGroupDTO.getClassRoomId()));
            zouBanCourseEntry.setClassName(zoubanGroupDTO.getClassName());
            zouBanCourseDao.updateZoubanCourse(zouBanCourseEntry);

        }
        if(removeTimeTable){
            //删除课表相关课
            timeTableDao.removeCourseByIds(term, classIdList, courseIds);
            //删除冲突
            eventConflictService.removeConflictByCourseIdList(courseIds);
        }
    }
    //--------------------------------------------------单双周设置--------------------------------------------------------

    /**
     * 处理单双周课
     *
     * @param map
     * @param zouBanCourseEntryList
     * @param subjectMap
     * @param classInfoDTOList
     */
    public void DSZKSubject(Map<ObjectId, ZoubanOddEvenDTO> map, List<ZouBanCourseEntry> zouBanCourseEntryList,
                            Map<ObjectId, String> subjectMap, List<ClassInfoDTO> classInfoDTOList) {

        ZouBanCourseEntry zouBanCourseEntry1 = zouBanCourseEntryList.get(0);
        ZouBanCourseEntry zouBanCourseEntry2 = zouBanCourseEntryList.get(1);
        if (zouBanCourseEntry1.getClassName().contains("单")) {
            ZoubanOddEvenDTO zoubanOddEvenDTO = new ZoubanOddEvenDTO();
            List<ObjectId> classId = zouBanCourseEntry1.getClassId();
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                if (classId.get(0).toString().equals(classInfoDTO.getId())) {
                    zoubanOddEvenDTO.setClassName(classInfoDTO.getClassName());
                    break;
                }
            }
            zoubanOddEvenDTO.setGroup(zouBanCourseEntry1.getGroup().toString());
            zoubanOddEvenDTO.setZoubanOddEvenId(zouBanCourseEntry1.getID().toString() + "," + zouBanCourseEntry2.getID().toString());
            zoubanOddEvenDTO.setSubjectName(subjectMap.get(zouBanCourseEntry1.getSubjectId()) + "/" +
                    subjectMap.get(zouBanCourseEntry2.getSubjectId()));
            zoubanOddEvenDTO.setSubjectId(zouBanCourseEntry1.getSubjectId().toString() + "," + zouBanCourseEntry2.getSubjectId().toString());
            if (null != zouBanCourseEntry1.getTeacherId()) {
                zoubanOddEvenDTO.setTeacherName(zouBanCourseEntry1.getTeacherName() + "/" + zouBanCourseEntry2.getTeacherName());
                zoubanOddEvenDTO.setTeacherId(zouBanCourseEntry1.getTeacherId().toString() + "," + zouBanCourseEntry2.getTeacherId().toString());
            }
            map.put(zouBanCourseEntry1.getGroup(), zoubanOddEvenDTO);
        } else {
            ZoubanOddEvenDTO zoubanOddEvenDTO = new ZoubanOddEvenDTO();
            List<ObjectId> classId = zouBanCourseEntry1.getClassId();
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                if (classId.get(0).toString().equals(classInfoDTO.getId())) {
                    zoubanOddEvenDTO.setClassName(classInfoDTO.getClassName());
                    break;
                }
            }
            zoubanOddEvenDTO.setGroup(zouBanCourseEntry2.getGroup().toString());
            zoubanOddEvenDTO.setZoubanOddEvenId(zouBanCourseEntry2.getID().toString() + "," + zouBanCourseEntry1.getID().toString());
            zoubanOddEvenDTO.setSubjectName(subjectMap.get(zouBanCourseEntry2.getSubjectId()) + "/" +
                    subjectMap.get(zouBanCourseEntry1.getSubjectId()));
            zoubanOddEvenDTO.setSubjectId(zouBanCourseEntry2.getSubjectId().toString() + "," + zouBanCourseEntry1.getSubjectId().toString());
            if (null != zouBanCourseEntry2.getTeacherId()) {
                zoubanOddEvenDTO.setTeacherName(zouBanCourseEntry2.getTeacherName() + "/" + zouBanCourseEntry1.getTeacherName());
                zoubanOddEvenDTO.setTeacherId(zouBanCourseEntry2.getTeacherId().toString() + "," + zouBanCourseEntry1.getTeacherId().toString());
            }
            map.put(zouBanCourseEntry2.getGroup(), zoubanOddEvenDTO);
        }
    }

    /**
     * 查询单双周课老师设置列表
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    public List<ZoubanOddEvenDTO> getDSZKTeacherList(String term, String gradeId, int type, String schoolId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), null, null, type);
        List<ZoubanOddEvenDTO> zouBanCourseDTOs = new ArrayList<ZoubanOddEvenDTO>();

        Map<ObjectId, ZoubanOddEvenDTO> map = new HashMap<ObjectId, ZoubanOddEvenDTO>();

        Map<ObjectId, List<ZouBanCourseEntry>> listHashMap = new HashMap<ObjectId, List<ZouBanCourseEntry>>();

        List<SubjectView> subjectList = schoolService.findSubjectListBySchoolIdAndGradeId(schoolId, gradeId);

        Map<ObjectId, String> subjNameMap = new HashMap<ObjectId, String>();

        for (SubjectView sv : subjectList) {
            subjNameMap.put(new ObjectId(sv.getId()), sv.getName());
        }



        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);

        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            if (listHashMap.containsKey(zouBanCourseEntry.getGroup())) {
                List<ZouBanCourseEntry> zouBanCourseEntryList = listHashMap.get(zouBanCourseEntry.getGroup());
                zouBanCourseEntryList.add(zouBanCourseEntry);
                listHashMap.put(zouBanCourseEntry.getGroup(), zouBanCourseEntryList);
            } else {
                List<ZouBanCourseEntry> zouBanCourseEntries1 = new ArrayList<ZouBanCourseEntry>();
                zouBanCourseEntries1.add(zouBanCourseEntry);
                listHashMap.put(zouBanCourseEntry.getGroup(), zouBanCourseEntries1);
            }
        }

        for (Map.Entry<ObjectId, List<ZouBanCourseEntry>> entry : listHashMap.entrySet()) {
            List<ZouBanCourseEntry> zouBanCourseEntryList = entry.getValue();
            DSZKSubject(map, zouBanCourseEntryList, subjNameMap, classInfoDTOList);
        }


        Map<String, List<ZoubanOddEvenDTO>> map1 = new HashMap<String, List<ZoubanOddEvenDTO>>();
        for (Map.Entry<ObjectId, ZoubanOddEvenDTO> entry : map.entrySet()) {
            if (map1.containsKey(entry.getValue().getSubjectName())) {
                List<ZoubanOddEvenDTO> list = map1.get(entry.getValue().getSubjectName());

                list.add(entry.getValue());
                map1.put(entry.getValue().getSubjectName(), list);
            } else {
                List<ZoubanOddEvenDTO> item = new ArrayList<ZoubanOddEvenDTO>();
                item.add(entry.getValue());
                map1.put(entry.getValue().getSubjectName(), item);
            }
        }

        for (Map.Entry<String, List<ZoubanOddEvenDTO>> entry : map1.entrySet()) {
            List<ZoubanOddEvenDTO> item = entry.getValue();
            Collections.sort(item, new Comparator<ZoubanOddEvenDTO>() {
                @Override
                public int compare(ZoubanOddEvenDTO o1, ZoubanOddEvenDTO o2) {
                    try {
                        String name1 = o1.getClassName();
                        String name2 = o2.getClassName();
                        String n1 = "";
                        String n2 = "";

                        if (name1.contains("(")) {
                            n1 = name1.substring(name1.indexOf("(") + 1, name1.lastIndexOf(")"));
                        } else {
                            n1 = name1.substring(name1.indexOf("（") + 1, name1.lastIndexOf("）"));
                        }
                        if (name2.contains("(")) {
                            n2 = name2.substring(name2.indexOf("(") + 1, name2.lastIndexOf(")"));
                        } else {
                            n2 = name2.substring(name2.indexOf("（") + 1, name2.lastIndexOf("）"));
                        }
                        return Integer.parseInt(n1) - Integer.parseInt(n2);
                    } catch (Exception e) {

                    }
                    return 0;
                }
            });
            zouBanCourseDTOs.addAll(item);
        }

        return zouBanCourseDTOs;
    }

    /**
     * 删除单双周或者分组走班的课程
     *
     * @param term
     * @param gradeId
     * @param subjectId
     * @param type
     */
    public void removeTimetable(String term, String gradeId, String subjectId, int type) {
        //先清空单双周对应的课表信息
        //查询该科目对应的课程信息
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), null, new ObjectId(subjectId), type);
        //获取全年级课表
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);
        List<ObjectId> zouBanIds = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            zouBanIds.add(zouBanCourseEntry.getID());
        }
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            for (CourseItem courseItem : timeTableEntry.getCourseList()) {
                if (courseItem.getType() == type) {
                    List<ObjectId> courseIds = courseItem.getCourse();
                    for (ObjectId courseId : courseIds) {
                        if (zouBanIds.contains(courseId)) {
                            //删除课程信息
                            timeTableDao.removeCourse(term, timeTableEntry.getClassId(), 1, courseItem);
                        }
                    }
                }
            }
        }
    }


    /**
     * 删除单双周课
     *
     * @param oddEvenId
     * @param gradeId
     * @param term
     * @param subjectId
     */
    public void removeDSZK(String oddEvenId, String gradeId, String term, String subjectId) {
        String[] subjectConfIds = oddEvenId.split(",");
        String[] subjectIds = subjectId.split(",");
        for(String item:subjectIds){
            removeTimetable(term,gradeId,item,ZoubanType.ODDEVEN.getType());
        }


        //删除单双周科目配置
        for (String subjectConfId : subjectConfIds) {
            subjectConfDao.deleteSubjectConf(new ObjectId(subjectConfId));
        }

        //删除对应的courseId课程信息
        //删除这些课程对应的该年级课表
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        for (String item : subjectIds) {
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId),
                    null, new ObjectId(item), ZoubanType.ODDEVEN.getType());
            if (null != zouBanCourseEntries) {
                zouBanCourseEntryList.addAll(zouBanCourseEntries);
            }
        }

        List<String> ids = new ArrayList<String>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            ids.add(zouBanCourseEntry.getID().toString());
        }
        //删除课表
        removeTimetableCourse(ids, term, gradeId);

        //删除单双周教学班信息
        for (String item : subjectIds) {
            for (String subjectConfId : subjectConfIds) {
                zouBanCourseDao.removeCourseBySubjectId(term, new ObjectId(gradeId),
                        ZoubanType.ODDEVEN.getType(), new ObjectId(item), new ObjectId(subjectConfId));
            }
        }

    }

    public void removeDSZKTeacher(String zoubanOddEvenId, String term, String gradeId) {
        String[] ids = zoubanOddEvenId.split(",");
        List<String> oIds = new ArrayList<String>();
        Set<ObjectId> idList = new HashSet<ObjectId>();
        for (String item : ids) {
            idList.add(new ObjectId(item));
            oIds.add(item);
        }
        zouBanCourseDao.removeCourseByIds(idList);
        //删除这些课程对应的该年级课表
        removeTimetableCourse(oIds, term, gradeId);
    }

    /**
     * 删除这些课程对应的该年级课表
     *
     * @param courseIdsStrList
     * @param term
     * @param gradeId
     */
    public void removeTimetableCourse(List<String> courseIdsStrList, String term, String gradeId) {
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for (String id : courseIdsStrList) {
            courseIds.add(new ObjectId(id));
        }
        //获取全年级课表
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            List<CourseItem> courseItems = timeTableEntry.getCourseList();
            for (CourseItem courseItem : courseItems) {
                if (courseItem.getType() == ZoubanType.ODDEVEN.getType() ||
                        courseItem.getType() == ZoubanType.FEIZOUBAN.getType() ||
                        courseItem.getType() == ZoubanType.GROUPZOUBAN.getType()) {
                    List<ObjectId> teIds = courseItem.getCourse();
                    for (ObjectId tid : teIds) {
                        if (courseIds.contains(tid)) {
                            //删除课程信息
                            timeTableDao.removeCourse(term, timeTableEntry.getClassId(), 1, courseItem);
                        }
                    }
                }
            }
        }
    }


    public void addDSZKTeacher(String zoubanEvenId, String teacherId, String teacherName) {
        String[] zoubanId = zoubanEvenId.split(",");
        String[] teas = teacherId.split(",");
        String[] tn = teacherName.split(",");
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(zoubanId[0]));
        ZouBanCourseEntry zouBanCourseEntry1 = zouBanCourseDao.getCourseInfoById(new ObjectId(zoubanId[1]));
        zouBanCourseEntry.setTeacherId(new ObjectId(teas[0]));
        zouBanCourseEntry.setTeacherName(tn[0]);
        zouBanCourseEntry1.setTeacherId(new ObjectId(teas[1]));
        zouBanCourseEntry1.setTeacherName(tn[1]);
        zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
        zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry1);
        List<ObjectId> couList = new ArrayList<ObjectId>();
        couList.add(new ObjectId(zoubanId[0]));
        couList.add(new ObjectId(zoubanId[1]));
        timeTableDao.removeCourseByIds(zouBanCourseEntry.getTerm(), zouBanCourseEntry.getClassId(), couList);
        //通过被删除的课程idList删除冲突
        eventConflictService.removeConflictByCourseIdList(couList);
    }


    /**
     * 添加单双周课设置
     *
     * @param oddEvenId
     * @param schoolId
     * @param gradeId
     * @param term
     * @param lessonCount
     * @param subjectId
     * @param xuankeId
     */
    public void addDSZK(String oddEvenId, String schoolId, String gradeId, String term, int lessonCount,
                        String subjectId, String xuankeId) throws Exception {
        List<String> str = new ArrayList<String>();
        String[] str1 = subjectId.split(",");
        for (String item : str1) {
            str.add(item);
        }
        SubjectConfDTO subjectConfDTO = new SubjectConfDTO();
        SubjectConfDTO subjectConfDTO1 = new SubjectConfDTO();
        if (StringUtils.isNotBlank(oddEvenId)) {
            String[] s = oddEvenId.split(",");
            subjectConfDTO.setSubjectConfId(s[0]);
            subjectConfDTO1.setSubjectConfId(s[1]);

            List<String> t = new ArrayList<String>();
            SubjectConfEntry subjectConfEntry = subjectConfDao.findSubjectConfEntry(new ObjectId(s[0]));
            SubjectConfEntry subjectConfEntry1 = subjectConfDao.findSubjectConfEntry(new ObjectId(s[1]));
            t.add(subjectConfEntry.getSubjectId().toString());
            t.add(subjectConfEntry1.getSubjectId().toString());
            //删除分组走班教学班
            for (String item : t) {
                for (String mytem : s) {
                    zouBanCourseDao.removeCourseBySubjectId(term, new ObjectId(gradeId),
                            ZoubanType.ODDEVEN.getType(), new ObjectId(item), new ObjectId(mytem));
                }
            }
        }

        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        Map<ObjectId, ObjectId> classroomMap = new HashMap<ObjectId, ObjectId>();

        for (ClassEntry classEntry : classEntries) {
            try {
                classroomMap.put(classEntry.getID(), getClassRoom(classEntry.getID()).getID());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("本年级教室未添加，请添加教室");
            }
        }


        subjectConfDTO.setSubjectId(str.get(0));
        subjectConfDTO.setTerm(term);
        subjectConfDTO.setType(ZoubanType.ODDEVEN.getType());
        subjectConfDTO.setGradeId(gradeId);
        subjectConfDTO.setXuankeId(xuankeId);
        subjectConfDTO.setAdvanceTime(lessonCount);
        subjectConfDTO.setSimpleTime(0);
        subjectConfDTO.setIfFengCeng(0);
        subjectConfDTO.setClassNumber(0);
        subjectConfDTO.setOddEvenType(1);//单周课
        subjectConfDTO.setClassList(new ArrayList<String>());

        subjectConfDTO1.setSubjectId(str.get(1));
        subjectConfDTO1.setTerm(term);
        subjectConfDTO1.setType(ZoubanType.ODDEVEN.getType());
        subjectConfDTO1.setGradeId(gradeId);
        subjectConfDTO1.setXuankeId(xuankeId);
        subjectConfDTO1.setAdvanceTime(lessonCount);
        subjectConfDTO1.setSimpleTime(0);
        subjectConfDTO1.setIfFengCeng(0);
        subjectConfDTO1.setClassNumber(0);
        subjectConfDTO1.setOddEvenType(2);//双周课
        subjectConfDTO1.setClassList(new ArrayList<String>());


        ObjectId evenOID = new ObjectId(str.get(0));
        ObjectId oddOID = new ObjectId(str.get(1));

        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId.toString());


        String evenName = subjectMap.get(evenOID).getName() + "（单）";
        String oddName = subjectMap.get(oddOID).getName() + "（双）";


        //分组走班学科设置
        ObjectId eid = subjectConfDao.addSubjectConf(subjectConfDTO.exportEntry());
        ObjectId oid = subjectConfDao.addSubjectConf(subjectConfDTO1.exportEntry());
        for (ClassEntry cls : classEntries) {
            try {
                //行政班id
                ObjectId group = new ObjectId();
                List<ObjectId> classIdList = new ArrayList<ObjectId>();
                classIdList.add(cls.getID());

                ZouBanCourseEntry zouBanCourseEntry1 = new ZouBanCourseEntry(new ObjectId(schoolId), oddOID, term, new ObjectId(gradeId),
                        classIdList, null, oddName, 1, ZoubanType.ODDEVEN.getType(), group, 0, classroomMap.get(cls.getID()), cls.getStudents(), oid);

                ZouBanCourseEntry zouBanCourseEntry2 = new ZouBanCourseEntry(new ObjectId(schoolId), evenOID, term, new ObjectId(gradeId),
                        classIdList, null, evenName, 1, ZoubanType.ODDEVEN.getType(), group, 0, classroomMap.get(cls.getID()), cls.getStudents(), eid);

                zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry1);
                zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry2);
            } catch (Exception e) {
                subjectConfDao.deleteSubjectConf(oid);
                subjectConfDao.deleteSubjectConf(eid);
            }
        }
    }

    /**
     * 查询单双周学科配置
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    public List<OddEvenDTO> getDSZKSubjectList(String term, String gradeId, int type, String schoolId) {
        List<OddEvenDTO> oddEvenDTOs = new ArrayList<OddEvenDTO>();
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);

        if (xuankeEntry != null) {
            List<SubjectConfEntry> entryList = subjectConfDao.findSubjectConf(xuankeEntry.getID(), type);
            if (null != entryList && !entryList.isEmpty()) {
                for (int i = 0; i < entryList.size() - 1; i += 2) {
                    OddEvenDTO oddEvenDTO = new OddEvenDTO();

                    if (subjectMap.containsKey(entryList.get(i).getSubjectId()) &&
                            subjectMap.containsKey(entryList.get(i + 1).getSubjectId())) {
                        String subjectName = subjectMap.get(entryList.get(i).getSubjectId()).getName();
                        String subjectName1 = subjectMap.get(entryList.get(i + 1).getSubjectId()).getName();
                        oddEvenDTO.setSubjectId(entryList.get(i).getSubjectId() + "," +
                                entryList.get(i + 1).getSubjectId());
                        oddEvenDTO.setSubjectName(subjectName + "/" + subjectName1);
                        oddEvenDTO.setOddEvenId(entryList.get(i).getID().toString() + "," +
                                entryList.get(i + 1).getID().toString());
                        oddEvenDTOs.add(oddEvenDTO);
                    }
                }
            }
        }
        return oddEvenDTOs;
    }


    //--------------------------------------------------体育课设置--------------------------------------------------------

    /**
     * 新增体育课
     *
     * @param schoolId
     * @param gradeId
     * @param term
     * @param classIds
     * @param className
     * @param lessonCount
     * @param teacherMId
     * @param teacherFId
     * @param teacherMName
     * @param teacherFName
     */
    public void addPECourse(String schoolId, String gradeId, String term, String classIds, String className,
                            int lessonCount, String teacherMId, String teacherFId, String teacherMName, String teacherFName) {
        //体育课学科id
        List<SubjectView> subjectList = schoolService.findSubjectList(schoolId);
        String subjectId = "";
        for (SubjectView subjectView : subjectList) {
            if (subjectView.getName().contains("体育")) {
                subjectId = subjectView.getId();
                break;
            }
        }
        //行政班id
        String[] classIdArr = classIds.split(",");
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        for (String classId : classIdArr) {
            classIdList.add(new ObjectId(classId));
        }
        //学生列表
        List<ObjectId> studentListM = new ArrayList<ObjectId>();//男生
        List<ObjectId> studentListF = new ArrayList<ObjectId>();//女生
        for (ObjectId classId : classIdList) {
            List<ObjectId> stuIdList = classService.findStuByClassId(classId);
            List<UserEntry> stuEntryList = userDao.getUserEntryList(stuIdList, new BasicDBObject("sex", 1));
            for (UserEntry student : stuEntryList) {
                if (student.getSex() == 1) {
                    studentListM.add(student.getID());
                } else {
                    studentListF.add(student.getID());
                }
            }
        }

        //男生班和女生班是一组
        ObjectId group = new ObjectId();
        //男生班
        ZouBanCourseEntry zouBanCourseEntry1 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), term, new ObjectId(gradeId),
                classIdList, null, className + "-男", lessonCount, new ObjectId(teacherMId), teacherMName, null, studentListM, 4, group);
        zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry1);
        //女生班
        ZouBanCourseEntry zouBanCourseEntry2 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), term, new ObjectId(gradeId),
                classIdList, null, className + "-女", lessonCount, new ObjectId(teacherFId), teacherFName, null, studentListF, 4, group);
        zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry2);
    }

    /**
     * 更新体育课
     *
     * @param mClassId
     * @param fClassId
     * @param className
     * @param adminClassId
     * @param mTeacherId
     * @param fTeacherId
     * @param lessonCount
     */
    public void updatePECourse(String term, String mClassId, String fClassId, String className, String adminClassId,
                               String mTeacherId, String fTeacherId, String mTeacherName, String fTeacherName, int lessonCount) {
        //行政班id
        String[] classIdArr = adminClassId.split(",");
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        for (String classId : classIdArr) {
            classIdList.add(new ObjectId(classId));
        }
        //学生列表
        List<ObjectId> studentListM = new ArrayList<ObjectId>();//男生
        List<ObjectId> studentListF = new ArrayList<ObjectId>();//女生
        for (ObjectId classId : classIdList) {
            List<ObjectId> stuIdList = classService.findStuByClassId(classId);
            List<UserEntry> stuEntryList = userDao.getUserEntryList(stuIdList, new BasicDBObject("sex", 1));
            for (UserEntry student : stuEntryList) {
                if (student.getSex() == 1) {
                    studentListM.add(student.getID());
                } else {
                    studentListF.add(student.getID());
                }
            }
        }
        int oldCount = 0;
        List<ObjectId> couIds = new ArrayList<ObjectId>();
        couIds.add(new ObjectId(mClassId));
        couIds.add(new ObjectId(fClassId));
        List<ZouBanCourseEntry> courseList = zouBanCourseDao.findCourseListByIds(couIds);
        if(courseList.size()>0){
            ZouBanCourseEntry course = courseList.get(0);
            oldCount = course.getLessonCount();
        }
        zouBanCourseDao.updatePECourse(new ObjectId(mClassId), classIdList, className + "-男", new ObjectId(mTeacherId), mTeacherName, studentListM, lessonCount);
        zouBanCourseDao.updatePECourse(new ObjectId(fClassId), classIdList, className + "-女", new ObjectId(fTeacherId), fTeacherName, studentListF, lessonCount);
        if(oldCount>lessonCount){
            //清空课表中的发生改变的体育课
            timeTableDao.removeCourseByIds(term, classIdList, couIds);
            //删除冲突
            eventConflictService.removeConflictByCourseIdList(couIds);
        }
    }

    /**
     * 根据年级获取体育课
     *
     * @param gradeId
     * @param term
     * @return
     */
    public List<PECourseDTO> getPECourseList(String gradeId, String term) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), 4);
        Map<ObjectId, List<ZouBanCourseEntry>> byGroup = new HashMap<ObjectId, List<ZouBanCourseEntry>>();

        for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
            List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
            list.add(entry);
            if (byGroup.get(entry.getGroup()) != null) {
                list.addAll(byGroup.get(entry.getGroup()));
            }
            byGroup.put(entry.getGroup(), list);
        }

        List<PECourseDTO> peCourseDTOList = new ArrayList<PECourseDTO>();

        for (Map.Entry entry : byGroup.entrySet()) {
            List<ZouBanCourseEntry> courseEntryList = (ArrayList<ZouBanCourseEntry>) entry.getValue();
            ZouBanCourseEntry mEntry = null;
            ZouBanCourseEntry fEntry = null;
            for (ZouBanCourseEntry entry1 : courseEntryList) {
                if (entry1.getClassName().contains("男")) {
                    mEntry = entry1;
                } else {
                    fEntry = entry1;
                }
            }

            PECourseDTO peCourseDTO = new PECourseDTO();
            peCourseDTO.setGroupClassName(fEntry.getClassName().substring(0, fEntry.getClassName().lastIndexOf("-")));

            List<ObjectId> classIds = courseEntryList.get(0).getClassId();

            String adminClassName = "";
            String adminClassIds = "";
            for (ObjectId classId : classIds) {
                ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classId.toString());
                adminClassIds += classInfoDTO.getId() + ",";
                adminClassName += classInfoDTO.getClassName() + "、";
            }

            peCourseDTO.setAdminClassName(adminClassName.substring(0, adminClassName.length() - 1));
            peCourseDTO.setAdminClassId(adminClassIds.substring(0, adminClassIds.length() - 1));
            peCourseDTO.setLessonCount(mEntry.getLessonCount());
            peCourseDTO.setfClassId(fEntry.getID().toString());
            peCourseDTO.setfClassName(fEntry.getClassName());
            peCourseDTO.setfTeacherId(fEntry.getTeacherId().toString());
            peCourseDTO.setfTeacherName(fEntry.getTeacherName());
            peCourseDTO.setfCountStr(fEntry.getStudentList().size() + "(女)");
            peCourseDTO.setmClassId(mEntry.getID().toString());
            peCourseDTO.setmClassName(mEntry.getClassName());
            peCourseDTO.setmTeacherId(mEntry.getTeacherId().toString());
            peCourseDTO.setmTeacherName(mEntry.getTeacherName());
            peCourseDTO.setmCountStr(mEntry.getStudentList().size() + "(男)");

            peCourseDTOList.add(peCourseDTO);
        }

        return peCourseDTOList;
    }

    /**
     * 删除体育课
     *
     * @param mClassId
     * @param fClassId
     */
    public void deletePECourse(String term, String mClassId, String fClassId) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(mClassId));
        List<ObjectId> classIdList = zouBanCourseEntry.getClassId();
        zouBanCourseDao.removeCourseById(new ObjectId(mClassId));
        zouBanCourseDao.removeCourseById(new ObjectId(fClassId));
        List<ObjectId> couIds = new ArrayList<ObjectId>();
        couIds.add(new ObjectId(mClassId));
        couIds.add(new ObjectId(fClassId));
        //清空课表中的发生改变的体育课
        timeTableDao.removeCourseByIds(term, classIdList, couIds);
        //通过被删除的课程idList删除冲突
        eventConflictService.removeConflictByCourseIdList(couIds);
    }
}
