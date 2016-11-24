package com.fulaan.overtime.controller;

import com.fulaan.alipay.util.httpClient.HttpRequest;
import com.fulaan.base.controller.BaseController;
import com.fulaan.duty.dto.DutyModelDTO;
import com.fulaan.overtime.service.OverTimeService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HttpXmlClient;
import com.fulaan.utils.LocalIp;
import com.pojo.user.UserDetailInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/7/13.
 */
@Controller
@RequestMapping("/overTime")
public class OverTimeController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private OverTimeService overTimeService;

    @RequestMapping("/jiaban")
    public String jiaban(Model model) {
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findTeacherInfoBySchoolId(getSessionValue().getSchoolId());
        model.addAttribute("users",userDetailInfoDTOList);
        List<UserDetailInfoDTO> userDetailInfoDTOs = userService.getUserInfoBySchoolid(getSchoolId());
        model.addAttribute("headUsers",userDetailInfoDTOs);
        return "overtime/jiaban";
    }

    @RequestMapping("/myjiaban")
    public String myjiaban() {
        return "overtime/myjiaban";
    }

    /**
     *
     * @param jbUserId
     * @param date
     * @param startTime
     * @param endTime
     * @param cause
     * @param shUserId
     * @return
     */
    @RequestMapping("addJiaBanInfo")
    @ResponseBody
    public Map addJiaBanInfo(String overTimeId,String jbUserId,String date,String startTime,String endTime,String cause,String shUserId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.addJiaBanInfo(overTimeId,getSchoolId(), getUserId(), jbUserId, date, startTime, endTime, cause, shUserId,map);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param modelName
     * @param jbUserId
     * @param date
     * @param startTime
     * @param endTime
     * @param cause
     * @param shUserId
     * @return
     */
    @RequestMapping("saveModelInfo")
    @ResponseBody
    public Map saveModelInfo(String modelId,String modelName,String jbUserId,String date,String startTime,String endTime,String cause,String shUserId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.saveModelInfo(modelId,modelName, getSchoolId(), getUserId(), jbUserId, date, startTime, endTime, cause, shUserId,map);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param name
     * @return
     */
    @RequestMapping("selJiaBanList")
    @ResponseBody
    public Map selJiaBanList(String startDate,String endDate,String name,int type,int index) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",overTimeService.selJiaBanList(startDate,endDate,name,getUserId(),getSchoolId(),type,index));
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selTeacherJiaBanList")
    @ResponseBody
    public Map selTeacherJiaBanList() {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",overTimeService.selJiaBanList("", "", "", getUserId(), getSchoolId(), 2,0));
        return map;
    }


    /**
     *
     * @param overTimeId
     * @return
     */
    @RequestMapping("delJiaBanInfo")
    @ResponseBody
    public Map delJiaBanInfo(String overTimeId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.delJiaBanInfo(overTimeId);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param overTimeId
     * @return
     */
    @RequestMapping("submitJiaBan")
    @ResponseBody
    public Map submitJiaBan(String overTimeId,int type) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.submitJiaBan(overTimeId, type);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }


    /**
     *
     * @param overTimeId
     * @return
     */
    @RequestMapping("selSingleOverTime")
    @ResponseBody
    public Map selSingleOverTime(String overTimeId) {
        Map<String,Object> map = new HashMap<String, Object>();
        overTimeService.selSingleOverTime(overTimeId,map);
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selJiaBanModelList")
    @ResponseBody
    public Map selJiaBanModelList() {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",overTimeService.selJiaBanModelList(getUserId()));
        return map;
    }

    /**
     *
     * @param overTimeModelId
     * @return
     */
    @RequestMapping("delOverTimeModel")
    @ResponseBody
    public Map delOverTimeModel(String overTimeModelId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.delOverTimeModel(overTimeModelId);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param overTimeId
     * @param salary
     * @return
     */
    @RequestMapping("updateSalaryById")
    @ResponseBody
    public Map updateSalaryById(String overTimeId,double salary) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.updateSalaryById(overTimeId,salary);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param name
     * @param type
     * @param response
     * @return
     */
    @RequestMapping("exportJiaBanList")
    @ResponseBody
    public boolean exportJiaBanList(String startDate,String endDate,String name,int type,int index,HttpServletResponse response) {
        overTimeService.exportJiaBanList(startDate, endDate, name, getUserId(), getSchoolId(),type,index,response);
        return true;
    }

    /**
     *
     * @param overTimeId
     * @param type
     * @return
     */
    @RequestMapping("checkInOut")
    @ResponseBody
    public Map checkInOut(String overTimeId,int type,HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        String ip = getIP(request);
        overTimeService.checkInOut(overTimeId,type,map,ip,getSchoolId());
        return map;
    }

    /**
     *
     * @param overTimeId
     * @param log
     * @param filepath
     * @param realName
     * @return
     */
    @RequestMapping("addOverTimeLog")
    @ResponseBody
    public Map addOverTimeLog(String overTimeId,String log,String[] filepath,String[] realName) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.addOverTimeLog(overTimeId, log, filepath, realName);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param overTimeId
     * @param type
     * @return
     */
    @RequestMapping("updOverTimeType")
    @ResponseBody
    public Map updOverTimeType(String overTimeId,int type) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.updOverTimeType(overTimeId, type);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param overTimeId
     * @param userId
     * @return
     */
    @RequestMapping("updTongGuo")
    @ResponseBody
    public Map updTongGuo(String overTimeId,String userId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            overTimeService.updTongGuo(overTimeId, userId);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selModelList")
    @ResponseBody
    public Map selModelList(String overTimeModelId) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<DutyModelDTO> dutyModelDTOList = overTimeService.selJiaBanModelList(getUserId());
        map.put("rows",dutyModelDTOList);
        if (StringUtils.isEmpty(overTimeModelId)) {
            if (dutyModelDTOList!=null && dutyModelDTOList.size()!=0) {
                map.put("modeldto",dutyModelDTOList.get(0));
            }
        } else {
            for (DutyModelDTO dutyModelDTO : dutyModelDTOList) {
                if (overTimeModelId.equals(dutyModelDTO.getId())) {
                    map.put("modeldto",dutyModelDTO);
                }
            }
        }
        return map;
    }


    /**
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("selMyJiaBanSalary")
    @ResponseBody
    public Map selMyJiaBanSalary(int year,int month) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",overTimeService.selMyJiaBanSalary(year, month,getUserId()));
        return map;
    }

    /**
     *
     * @param year
     * @param month
     * @param response
     * @return
     */
    @RequestMapping("exportMySarlaryList")
    @ResponseBody
    public boolean exportMySarlaryList(int year,int month,HttpServletResponse response) {
        overTimeService.exportMySarlaryList(year, month, getUserId(), response);
        return true;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selMyOverTimeInfo")
    @ResponseBody
    public Map selMyOverTimeInfo() {
        Map<String,Object> map = new HashMap<String, Object>();
        overTimeService.selMyOverTimeInfo(getSchoolId(),map,getUserId());
        return map;
    }

    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
