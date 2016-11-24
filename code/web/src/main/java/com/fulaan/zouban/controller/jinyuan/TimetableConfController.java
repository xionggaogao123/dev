package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.UserGroupInfo;
import com.pojo.user.UserRole;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by admin on 2016/9/20.
 */
@Controller
@RequestMapping("/zouban/timetableConf")
public class TimetableConfController extends BaseController {

    @Autowired
    private PaikeService paikeService;
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private SchoolService schoolService;


    /**
     * 删除班级事务(事务维度（班级）)
     *
     * @param term
     * @param gradeId
     * @param classId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/removeEventClass", method = RequestMethod.POST)
    @ResponseBody
    public RespObj removeEventClass(String term, @ObjectIdType ObjectId gradeId, String classId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        timetableConfService.removeEventClass(term, gradeId, classId, x, y);
        respObj.setCode(RespObj.SUCCESS.code);
        return respObj;
    }


    /**
     * 新增班级事务(事务维度（班级）)
     *
     * @param term
     * @param gradeId
     * @param classId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/addEventClassList", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addClassEventList(String term, @ObjectIdType ObjectId gradeId, String classId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        timetableConfService.addClassEventList(term, gradeId, classId, x, y);
        int state = zoubanStateService.getZoubanState(term, getSessionValue().getSchoolId(), gradeId.toString());
        if (state > 4) {
            if (!timetableConfService.judgementState(term, gradeId.toString())) {
                zoubanStateService.setZoubanState(term, gradeId.toString(), 4);
            }
        }

        respObj.setCode(RespObj.SUCCESS.code);
        return respObj;
    }

    /**
     * 新增班级事务(事务维度（全校以及年级）)
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/addClassEventList", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addClassEventList(String term,
                                     String gradeId,
                                     int x,
                                     int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        timetableConfService.addSchoolClassEventList(term, gradeId, x, y, getSchoolId().toString());
        respObj.setCode(RespObj.SUCCESS.code);
        return respObj;
    }

    /**
     * 删除班级事务
     *
     * @param term
     * @param gradeId
     * @param
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeClassEvent")
    @ResponseBody
    public RespObj removeClassEvent(String term,
                                    String gradeId,
                                    int x,
                                    int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            if (("All").equals(gradeId)) {
                List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
                for (GradeView gradeView : gradeList) {
                    timetableConfService.removeClassEvent(term, gradeView.getId(), x, y);
                }
            } else {
                timetableConfService.removeClassEvent(term, gradeId, x, y);
            }
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }

    /**
     * 获取班级维度的班级无可事务
     *
     * @param gradeId
     * @param term
     * @param classId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/classTimeTableEvent", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getClassTimeTableEvent(String gradeId, String term, String classId) {
        TimetableConfDTO timetableConf = timetableConfService.getClassEventTimetable(term, gradeId, classId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timetableConf", timetableConf);
        return result;
    }

    /**
     * 获取课表配置
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/getTimetableConf", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTimetableConf(String term) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        TimetableConfDTO timetableConf = new TimetableConfDTO();
        for (GradeView gradeView : gradeList) {
            timetableConf = timetableConfService.getTimetableConf(term, new ObjectId(gradeView.getId()));
            break;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timetableConf", timetableConf);
        return result;
    }

    /**
     * 获取课表配置以及班级事务（事务维度全校及年级）
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/timetableConf", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTimetableConf(String gradeId, String term) {

        TimetableConfDTO timetableConf = timetableConfService.getTimetableConfDTO(gradeId, term, getSchoolId().toString());

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timetableConf", timetableConf);
        return result;
    }

    /**
     * 课表配置是否锁定
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/isLocked")
    @ResponseBody
    public Map<String, Object> isLocked(String term, String gradeId) {
        boolean lock = timetableConfService.isLocked(term, gradeId);
        Map<String, Object> result = new HashMap<String, Object>();
        if (lock) {
            result.put("lock", "LOCK");
        } else {
            result.put("lock", "UNLOCK");
        }
        return result;
    }

    /**
     * 锁定课表配置
     *
     * @param timetableConfDTO
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/lock")
    @ResponseBody
    public RespObj lock(@RequestBody TimetableConfDTO timetableConfDTO) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            timetableConfService.lockTimetableConf(getSchoolId(), timetableConfDTO);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }

    /**
     * 解锁课表配置
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/unlock")
    @ResponseBody
    public RespObj unlock(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            timetableConfService.unlock(term, gradeId);
            //取消发布
            timeTableService.cancelPublishCourse(term, getSchoolId(), new ObjectId(gradeId));
            //设置进度
            zoubanStateService.setZoubanState(term, gradeId, 4);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    //=======================================================不可排课事务设置==============================================

    /**
     * 获取不可排课事务列表
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getEventList")
    @ResponseBody
    public Map<String, Object> getEventList(String term) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        List<EventDTO> eventDTOList = new ArrayList<EventDTO>();
        for (GradeView gradeView : gradeList) {
            eventDTOList = timetableConfService.getEventList(term, gradeView.getId());
            break;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("eventList", eventDTOList);
        return result;
    }


    /**
     * 获取学科老师列表
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/subjectTeacher")
    @ResponseBody
    public Map<String, Object> subjectTeacher(String term) {
        List<SubjectTeacher> list = timetableConfService.getSubjectTeacher(term, getSchoolId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("subjectTeacherList", list);
        return result;
    }

    /**
     * 获取事务详情
     *
     * @param term
     * @param eventId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/eventDetail")
    @ResponseBody
    public Map<String, Object> eventDetail(String term, String eventId) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        EventDTO eventDTO = new EventDTO();
        for (GradeView gradeView : gradeList) {
            eventDTO = timetableConfService.getEventDetail(term, gradeView.getId(), eventId);
            break;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("eventDetail", eventDTO);
        return result;
    }


    /**
     * 新增/更新事务
     *
     * @param term
     * @param eventDTO
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addOrUpdateEvent")
    @ResponseBody
    public RespObj updateEvent(String term, @RequestBody EventDTO eventDTO) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        try {
            List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
            for (GradeView gradeView : gradeList) {
                if (eventDTO.getId() != null && !eventDTO.getId().equals("")) {
                    timetableConfService.updateEvent(term, gradeView.getId(), eventDTO);
                } else {
                    timetableConfService.addEvent(term, gradeView.getId(), eventDTO);
                }
                int state = zoubanStateService.getZoubanState(term, getSessionValue().getSchoolId(), gradeView.getId());
                if (state > 4) {
                    if (!timetableConfService.judgementState(term, gradeView.getId())) {
                        //取消发布
                        timeTableService.cancelPublishCourse(term, getSchoolId(), new ObjectId(gradeView.getId()));
                        zoubanStateService.setZoubanState(term, gradeView.getId(), 4);
                    }
                }
            }
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 删除事务
     *
     * @param term
     * @param eventId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeEvent")
    @ResponseBody
    public RespObj removeEvent(String term, String eventId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        try {
            TimetableConfEntry.Event event = timetableConfService.getConfEvent(term, new ObjectId(eventId));
            if (event == null) {
                throw new Exception("事务不存在");
            }

            timetableConfService.removeEvent(term, event, getSchoolId().toString());
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取学科和老师
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getSubjectTeachers")
    @ResponseBody
    public Map<String, Object> getSubjectUsers() {
        //学科
        List<UserGroupInfo<IdNameValuePairDTO>> subjectTeacherList = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = paikeService.findTeacherListBySchool(new ObjectId(getSessionValue().getSchoolId()));
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entryMaps : retMap.entrySet()) {
            subjectTeacherList.add(new UserGroupInfo<IdNameValuePairDTO>(entryMaps.getKey(), new ArrayList<IdNameValuePairDTO>(entryMaps.getValue())));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("subTeachers", subjectTeacherList);
        return result;
    }

    /**
     * 获取某个时间点的所有事务
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/eventDetailList")
    @ResponseBody
    public Map<String, Object> getEventDetailList(String term, String gradeId, int x, int y) {
        List<EventDetailDTO> eventDetailDTOs = timetableConfService.eventDetailList(term, gradeId, x, y);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("eventDetailList", eventDetailDTOs);
        return result;
    }



}
