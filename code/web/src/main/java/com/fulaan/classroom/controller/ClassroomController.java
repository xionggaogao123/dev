package com.fulaan.classroom.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.classroom.ClassRoomDTO;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.service.PaikeService;
import com.mongodb.BasicDBObject;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by qiangm on 2015/10/23.
 */
@Controller
@RequestMapping("/classroom")
public class ClassroomController extends BaseController {

    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private PaikeService paikeService;

    /**
     * 获取教室列表----提供分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/findClassroomListPage")
    @ResponseBody
    public Map<String, Object> findClassroomList(int page, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        if (page < 0)
            page = 1;
        List<ClassRoomDTO> classroomEntryList = classroomService.findClassroomList(schoolId, page, pageSize);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rowCount", classroomService.count(schoolId));
        map.put("rows", classroomEntryList);
        return map;
    }

    /**
     * 获取教室列表----不提供分页
     *
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/findClassroomList")
    @ResponseBody
    public List<ClassRoomDTO> findClassroomList() {
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        List<ClassRoomDTO> classroomEntryList = classroomService.findClassroomList(schoolId);
        return classroomEntryList;
    }

    /**
     * 获取教室列表----不提供分页,当前时间点不被占用
     *
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/findFreeClassroomList")
    @ResponseBody
    public List<ClassRoomDTO> findFreeClassroomList(@RequestParam String year, @RequestParam String classroomId,
                                                    @RequestParam int x, @RequestParam int y) {
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        List<ClassRoomDTO> classroomEntryList = classroomService.findFreeClassroomList(schoolId, year, classroomId, x, y);

        return classroomEntryList;
    }

    /**
     * 获取教室列表----不提供分页,当前时间点不被占用,小走班使用
     *
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/findFreeClassroomListForXZB")
    @ResponseBody
    public List<ClassRoomDTO> findFreeClassroomListForXZB(@RequestParam String year, @RequestParam String classroomId,
                                                          @RequestParam int x, @RequestParam int y, @RequestParam String gradeId) {
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        List<ClassRoomDTO> classroomEntryList = classroomService.findFreeClassroomListForXZB(schoolId, gradeId, year, classroomId, x, y);

        return classroomEntryList;
    }

    /**
     * 添加教室
     *
     * @param name
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/addClassroom")
    @ResponseBody
    public RespObj addClassroom(String name, String classId) {
        RespObj respObj = RespObj.FAILD;
        try {
            classroomService.addClassRoom(getSchoolId(), name, classId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }

    /**
     * 更新教室
     *
     * @param classroomId
     * @param name
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/updateClassroom")
    @ResponseBody
    public RespObj updateClassroom(String classroomId, String name, String classId) {
        RespObj respObj = RespObj.FAILD;
        try {
            classroomService.updateClassroom(classroomId, name, classId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }

    /**
     * 删除教室
     *
     * @param classroomId
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/removeClassroom")
    @ResponseBody
    public RespObj removeClassroom(String classroomId) {
        RespObj respObj = RespObj.FAILD;
        try {
            classroomService.deleteClassRoom(classroomId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 判断教室名称是否被使用
     *
     * @param name
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/isExist")
    @ResponseBody
    public RespObj checkIfHave(String name) {
        RespObj respObj = RespObj.FAILD;
        if (classroomService.isExist(getSchoolId().toString(), name)) {
            respObj = RespObj.SUCCESS;
        }

        return respObj;
    }



    /**
     * 获取全校未设置班级
     *
     * @return
     */
    @RequestMapping("/findClassInfoBySchoolId")
    @ResponseBody
    public Map<String, Object> findClassInfoBySchoolId() {
        //全校所有班级
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoBySchoolId(getSchoolId().toString());
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(getSchoolId(), new BasicDBObject("grs", 1));
        ObjectId biye = null;
        for (Grade grade : schoolEntry.getGradeList()) {
            if (grade.getGradeType() == -1) {
                biye = grade.getGradeId();
            }
        }


        //获取全校的教室
        List<ClassRoomDTO> classroomEntryList = classroomService.findClassroomList(new ObjectId(getSessionValue().getSchoolId()));
        //已分配的班级
        List<String> classIdList = new ArrayList<String>();

        for (ClassRoomDTO classRoomDTO : classroomEntryList) {
            classIdList.add(classRoomDTO.getClassId());
        }

        Iterator<ClassInfoDTO> iterator = classInfoDTOList.iterator();
        while (iterator.hasNext()) {
            ClassInfoDTO classInfoDTO = iterator.next();

            if (biye != null && classInfoDTO.getGradeId().equals(biye.toString())) {//排除毕业班
                iterator.remove();
            } else {
                if (classIdList.contains(classInfoDTO.getId())) {//排除已分配班级
                    iterator.remove();
                }
            }
        }

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("classList", classInfoDTOList);
        return result;
    }

}
