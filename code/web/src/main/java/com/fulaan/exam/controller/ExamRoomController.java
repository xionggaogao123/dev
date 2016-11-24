package com.fulaan.exam.controller;

import java.util.HashMap;
import java.util.Map;

import com.fulaan.exam.service.ExamService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamRoomService;
import com.pojo.exam.ExamRoomEntry;
import com.sys.utils.RespObj;

/**
 * 考场资源controller
 *
 * @author cxy
 */
@Controller
@RequestMapping("/examRoom")
public class ExamRoomController extends BaseController {
    @Autowired
    private ExamRoomService examRoomService;

    @Autowired
    private ExamService examService;

    /**
     * 查询本校所有等级信息
     *
     * @return
     */

    @RequestMapping("/queryExamRoom")
    @ResponseBody
    public String queryLevel() {
        String schoolId = getSessionValue().getSchoolId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("examRooms", examRoomService.queryExamRoomsBySchoolId(new ObjectId(schoolId)));
        return JSON.toJSONString(map);
    }

    /**
     * 新增一条等级信息
     *
     * @return
     */

    @RequestMapping("/addExamRoom")
    @ResponseBody
    public String addExamRoom() {
        String schoolId = getSessionValue().getSchoolId();
        ObjectId id = examRoomService.addExamRoomEntry(new ExamRoomEntry(new ObjectId(schoolId)));
        Map<String, String> m = new HashMap<String, String>();
        m.put("examRoomId", id.toString());
        return JSON.toJSONString(m);
    }

    /**
     * 更新等级信息
     *
     * @return
     */
  
    @RequestMapping("/updateExamRoom")
    @ResponseBody
    public String updateExamRoom(@RequestParam(value = "examRoomNumber", defaultValue = "") String examRoomNumber,
                                 @RequestParam(value = "examRoomName", defaultValue = "0") String examRoomName,
                                 @RequestParam(value = "examRoomSitNumber", defaultValue = "0") String examRoomSitNumber,
                                 @RequestParam(value = "examRoomPostscript", defaultValue = "0") String examRoomPostscript,
                                 @RequestParam(value = "examRoomId", defaultValue = "") String examRoomId) {
        if ("".equals(examRoomId)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        examRoomService.updateExamRoom(new ObjectId(examRoomId), examRoomNumber, examRoomName, Integer.parseInt(examRoomSitNumber), examRoomPostscript);
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 删除一条等级
     *
     * @return
     */
  
    @RequestMapping("/deleteExamRoom")
    @ResponseBody
    public String deleteExamRoom(@RequestParam(value = "examRoomId", defaultValue = "") String examRoomId) {
        if ("".equals(examRoomId)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        examRoomService.deleteExamRoom(new ObjectId(examRoomId));
        return JSON.toJSONString(RespObj.SUCCESS);
    }
}
