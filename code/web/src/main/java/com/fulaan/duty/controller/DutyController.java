package com.fulaan.duty.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.duty.dto.*;
import com.fulaan.duty.service.DutyService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HttpXmlClient;
import com.fulaan.utils.LocalIp;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.WeekUtils;
import com.pojo.duty.DutySetEntry;
import com.pojo.duty.DutyUserEntry;
import com.pojo.lesson.LessonWare;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.SocketException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/6/28.
 */
@Controller
@RequestMapping("/duty")
public class DutyController extends BaseController {

    @Autowired
    private DutyService dutyService;

    @Autowired
    private UserService userService;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("/zhiban")
    public String zhiban(Model model) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int week = dutyService.getIndex();
        List<DutyProjectDTO> dutyProjectDTOs = dutyService.selProjectList(getSchoolId(),0,year,week);
        model.addAttribute("orgDutyProject",dutyProjectDTOs);
        if (dutyProjectDTOs!=null && dutyProjectDTOs.size()!=0) {
            List<DutyProjectDTO> dutyProjectDTOs1 = dutyService.selProjectByOrgId(dutyProjectDTOs.get(0).getId());
            model.addAttribute("dutyProject",dutyProjectDTOs1);
        }
        List<DutyProjectDTO> dutyProjectDTOs2 = dutyService.selProjectList(getSchoolId(),1,year,week);
        model.addAttribute("dutyProject2",dutyProjectDTOs2);
//        List<DutyTimeDTO> dutyTimeDTOList =  dutyService.selDutyTimeList(getSchoolId());
//        model.addAttribute("dutyTimes",dutyTimeDTOList);
        model.addAttribute("week",week);
        model.addAttribute("year",year);
        return "zhiban/zhiban";
    }

    @RequestMapping("/myzhiban")
    public String myzhiban() {
        return "zhiban/myzhiban";
    }

    @RequestMapping("/selDutySetInfo")
    @ResponseBody
    public Map selDutySetInfo(int type,int year,int week,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<String, Object>();
        dutyService.selDutySetInfo(getSchoolId(),getUserId(),map,type,year,week);
        return map;
    }

    /**
     *
     * @param type
     * @param num
     * @return
     */
    @RequestMapping("/updDutySetTime")
    @ResponseBody
    public Map updateDutySetTime(int type,int num,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.updateDutySetTime(getSchoolId(),type,num,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param userId
     * @return
     */
    @RequestMapping("/addDutyUser")
    @ResponseBody
    public Map addDutyUser(String userId,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addDutyUser(getSchoolId(),userId,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param userId
     * @return
     */
    @RequestMapping("/missDutyUser")
    @ResponseBody
    public Map missDutyUser(String userId,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.missDutyUser(getSchoolId(),userId,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dutyTimeId
     * @param dutyId
     * @param timeDesc
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/addOrUdpDutyTimeInfo")
    @ResponseBody
    public Map addOrUdpDutyTimeInfo(int year,int week,String dutyTimeId,String dutyId,String timeDesc,String startTime,String endTime) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addOrUdpDutyTimeInfo(week,year,getSchoolId(),dutyTimeId,dutyId, timeDesc, startTime,endTime);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dutyTimeId
     * @return
     */
    @RequestMapping("/delDutyTimeInfo")
    @ResponseBody
    public Map delDutyTimeInfo(String dutyTimeId,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.delDutyTimeInfo(dutyTimeId,year,week,getSchoolId());
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    @RequestMapping("/addOrUdpDutyProject")
    @ResponseBody
    public Map addOrUdpDutyProject(String dutyId,String orgDutyProjectId,String dutyProjectId,String content) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addOrUdpDutyProject(dutyId,orgDutyProjectId,dutyProjectId,content,map);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dutyProjectId
     * @return
     */
    @RequestMapping("/delDutyProject")
    @ResponseBody
    public Map delDutyProject(String dutyProjectId,String dutyOrgProjectId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.delDutyProject(dutyProjectId,dutyOrgProjectId,getSchoolId());
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
    @RequestMapping("/selDutyList")
    @ResponseBody
    public Map selDutyList(int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        dutyService.selDutyList(getSchoolId(),map,year,week);
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selSingleDuty")
    @ResponseBody
    public Map selSingleDuty(int type,String num,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        dutyService.selSingleDuty(type, getSchoolId(),map,num,year,week);
        List<DutyProjectDTO> dutyProjectDTOs = dutyService.selProjectList(getSchoolId(),0,year,week);
        map.put("orgDutyProject", dutyProjectDTOs);
        if (dutyProjectDTOs!=null && dutyProjectDTOs.size()!=0) {
            List<DutyProjectDTO> dutyProjectDTOs1 = dutyService.selProjectByOrgId(dutyProjectDTOs.get(0).getId());
            map.put("dutyProject", dutyProjectDTOs1);
        }
        return map;
    }


    /**
     *
     * @param modelName
     * @return
     */
    @RequestMapping("/addModel")
    @ResponseBody
    public Map addModel(int year,int week,String modelName) {
        Map<String,Object> map = new HashMap<String, Object>();
        int count = dutyService.checkModelNameCount(getSchoolId(),modelName,"");
        if (count!=0) {
            map.put("flag",false);
            map.put("mesg","模板名重复！");
            return map;
        }
        try {
            dutyService.addModel(getSchoolId(), modelName,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
            map.put("mesg","模板新建失败！");
        }
        return map;
    }

    /**
     *
     * @param modelId
     * @return
     */
    @RequestMapping("/useModel")
    @ResponseBody
    public Map useModel(String modelId,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.useModel(getSchoolId(), modelId,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param modelId
     * @param modelName
     * @return
     */
    @RequestMapping("/updateDutyModel")
    @ResponseBody
    public Map updateDutyModel(String modelId,String modelName) {
        Map<String,Object> map = new HashMap<String, Object>();
        int count = dutyService.checkModelNameCount(getSchoolId(),modelName,modelId);
        if (count!=0) {
            map.put("flag",false);
            map.put("mesg","模板名重复！");
            return map;
        }
        try {
            dutyService.updateDutyModel(modelId, modelName);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
            map.put("mesg","模板编辑失败！");
        }
        return map;
    }

    /**
     *
     * @param modelId
     * @return
     */
    @RequestMapping("/delDutyModel")
    @ResponseBody
    public Map delDutyModel(String modelId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.delDutyModel(modelId);
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
    @RequestMapping("/selDutyModelList")
    @ResponseBody
    public Map selDutyModelList() {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",dutyService.selDutyModelList(getSchoolId()));
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selProjectList")
    @ResponseBody
    public Map selProjectList(int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",dutyService.selProjectList(getSchoolId(),0,year,week));
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selDutyUsers")
    @ResponseBody
    public Map selDutyUsers() {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",userService.findTeacherInfoBySchoolId(getSessionValue().getSchoolId()));
        return map;
    }

    @RequestMapping("addOrUpdDutyUserInfo")
    @ResponseBody
    public Map addOrUpdDutyUserInfo(DutyDTO dutyDTO,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addOrUpdDutyUserInfo(dutyDTO, getSchoolId(),getUserId(),year,week);
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
    @RequestMapping("selMyDutyInfo")
    @ResponseBody
    public Map selMyDutyInfo() {
        Map<String,Object> map = new HashMap<String, Object>();
        dutyService.selMyDutyInfo(getSchoolId(),map,getUserId());
        return map;
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("selMyDutyHistory")
    @ResponseBody
    public Map selMyDutyHistory(int year,int month,int type,int curYear,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        dutyService.selMyDutyHistory(year,month,type,curYear,week,map,getSchoolId(),getUserId());
        return map;
    }

    /**
     * 申请换班详细
     * @param dutyId
     * @return
     */
    @RequestMapping("selDutyShiftDetail")
    @ResponseBody
    public Map selDutyShiftDetail(String dutyId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("cause", dutyService.selDutyShiftDetail(dutyId,getUserId()));
        return map;
    }

    /**
     *
     * @param dutyId
     * @param cause
     * @return
     */
    @RequestMapping("addDutyShiftInfo")
    @ResponseBody
    public Map addDutyShiftInfo(String dutyId,String cause,String timeDesc){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addDutyShiftInfo(dutyId,cause,getUserId(),timeDesc, getSchoolId());
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }


    /**
     * 签到
     * @param dutyId
     * @param type
     * @return
     */
    @RequestMapping("checkInOut")
    @ResponseBody
    public Map checkInOut(String dutyId,int type,int year,int week,HttpServletRequest request) throws SocketException{
        Map<String,Object> map = new HashMap<String, Object>();
        String ip = getIP(request);
        dutyService.checkInOut(dutyId,getUserId(),type,year,week,map,ip);
        return map;
    }

    /**
     *换班审核列表
     * @param startDate
     * @param endDate
     * @param name
     * @return
     */
    @RequestMapping("selShiftCheckList")
    @ResponseBody
    public Map selShiftCheckList(String startDate,String endDate,String name) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<DutyShiftDTO> dutyShiftDTOs = dutyService.selShiftCheckList(startDate, endDate, name, getSchoolId());
        map.put("rows", dutyShiftDTOs);
        return map;
    }

    /**
     *是否通过
     * @param dutyShiftId
     * @param type
     * @return
     */
    @RequestMapping("isShift")
    @ResponseBody
    public Map isTongGuo(String dutyShiftId,int type) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.isTongGuo(dutyShiftId,type);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *值班计划管理
     * @param startTime
     * @param endTime
     * @param name
     * @return
     */
    @RequestMapping("selDutyUserSarlaryList")
    @ResponseBody
    public Map selDutyUserSarlaryList(String startTime,String endTime,int type,int year,int week,String name,int page,int pageSize) {
        Map<String,Object> map = new HashMap<String, Object>();
        if (type==1) {
            startTime = DateTimeUtils.dateToStrLong(WeekUtils.getFirstDayOfWeek(year, week-1),DateTimeUtils.DATE_YYYY_MM_DD_N);
            endTime = DateTimeUtils.dateToStrLong(WeekUtils.getLastDayOfWeek(year, week-1),DateTimeUtils.DATE_YYYY_MM_DD_N);
        }
        map.put("rows", dutyService.selDutyUserSarlaryList(startTime,endTime,name,getSchoolId(),page,pageSize));
        map.put("total", dutyService.selDutyUserSarlaryCount(startTime,endTime,name,getSchoolId()));
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }



    /**
     *
     * @return
     */
    @RequestMapping("selAllDutyProject")
    @ResponseBody
    public Map selAllDutyProject(int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<DutyProjectDTO> dutyProjectDTOs2 = dutyService.selProjectList(getSchoolId(),1,year,week);
        map.put("rows", dutyProjectDTOs2);
        return map;
    }

    /**
     *
     * @param dutyUserId
     * @param sarlay
     * @return
     */
    @RequestMapping("updateSarlary")
    @ResponseBody
    public Map updateSarlary(String dutyUserId,double sarlay) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.updateSarlary(dutyUserId, sarlay);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     * 值班薪酬管理
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("selMySarlaryList")
    @ResponseBody
    public Map selMySarlaryList(int year,int month) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows", dutyService.selMySarlaryList(year, month,getUserId(), getSchoolId()));
        return map;
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param response
     * @return
     */
    @RequestMapping("exportDutyUserSarlaryList")
    @ResponseBody
    public boolean exportDutyUserSarlaryList(String startTime,String endTime,String name,HttpServletResponse response) {
        dutyService.exportDutyUserSarlaryList(startTime, endTime, name, getSchoolId(), response);
        return true;
    }

    /**
     *换班审核列表
     * @param startDate
     * @param endDate
     * @param name
     * @return
     */
    @RequestMapping("exportShiftCheckList")
    @ResponseBody
    public boolean exportShiftCheckList(String startDate,String endDate,String name,HttpServletResponse response) {
        dutyService.exportShiftCheckList(startDate, endDate, name, getSchoolId(),response);
        return true;
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
        dutyService.exportMySarlaryList(year, month, getUserId(), getSchoolId(),response);
        return true;
    }

    /**
     *
     * @param dutyShiftId
     * @param type
     * @return
     */
    @RequestMapping("updDutyShiftInfo")
    @ResponseBody
    public Map updDutyShiftInfo(String dutyShiftId,String userId,int type) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.updDutyShiftInfo(dutyShiftId,userId, type);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }
    /**
     * 上传附件
     *
     * @param req
     * @param file
     * @return
     */
    //技术原因，上传到upload/duty下
    @RequestMapping("/uploadattach")
    @ResponseBody
    public Map<String, Object> uploadAttachment(HttpServletRequest req, MultipartFile file) throws Exception{
//        String filekey = "duty-" + new ObjectId().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
//
//        String parentPath = req.getServletContext().getRealPath("/upload") + "/duty";
//        File parentFile = new File(parentPath);
//        if (!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//        req.getServletContext().getRealPath("/upload");
//        String urlPath = "/upload/duty/" + filekey;
//
//        File attachFile = new File(parentFile, filekey);
//        try {
//            //QiniuFileUtils.uploadFile(filekey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
//            FileUtils.copyInputStreamToFile(file.getInputStream(), attachFile);
//
//        } catch (Exception ioe) {
//
//        }
        Map<String, Object> model = new HashMap<String, Object>();
        String fileName= FilenameUtils.getName(file.getOriginalFilename());
        String examName =getFileName(file);
        RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
        if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
        {
            throw new FileUploadException();
        }
        String url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,examName);
        model.put("uploadType", 1);
        model.put("name",fileName);
        model.put("vurl",url);
        return model;
    }

    /**
     *
     * @param file
     * @return
     */
    private String getFileName(MultipartFile file){
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }
    /**
     *
     * @return
     * @throws SocketException
     */
    @RequestMapping("selLocalIp")
    @ResponseBody
    public Map selLocalIp(String dutyUserId) throws SocketException {
        Map<String, Object> model = new HashMap<String, Object>();
        DutyUserEntry dutyUser = dutyService.selDutyUserByDutyId(dutyUserId,getUserId());
        model.put("ip", dutyUser.getIp());
        model.put("files",dutyUser.getLessonWareList());
        model.put("content", dutyUser.getContent());
        return model;
    }

    /**
     *
     * @param dutyUserId
     * @param log
     * @param filepath
     * @return
     */
    @RequestMapping("addDutyLog")
    @ResponseBody
    public Map addDutyLog(String dutyUserId,String log,String[] filepath,String[] realName) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.addDutyLog(dutyUserId,getUserId(), log,filepath,realName);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dutyProjectId
     * @return
     */
    @RequestMapping("selProjectByOrgId")
    @ResponseBody
    public Map selProjectByOrgId(String dutyProjectId) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<DutyProjectDTO> dutyProjectDTOs1 = dutyService.selProjectByOrgId(dutyProjectId);
        map.put("dutyProject", dutyProjectDTOs1);
        return map;
    }


    /**
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param response
     * @return
     */
    @RequestMapping("exportDutyInfoList")
    @ResponseBody
    public boolean exportDutyInfoList(String startTime,String endTime,String name,HttpServletResponse response) {
         dutyService.exportDutyInfoList(startTime, endTime, name, getSchoolId(), response);
        return true;
    }

    /**
     * 值班统计
     * @param startTime
     * @param endTime
     * @param name
     * @param response
     * @return
     */
    @RequestMapping("exportDutyTotal")
    @ResponseBody
    public boolean exportDutyTotal(String startTime,String endTime,String name,HttpServletResponse response) {
        dutyService.exportDutyTotal(startTime, endTime, name, getSchoolId(), response);
        return true;
    }

    /**
     * 平安记录
     * @param startTime
     * @param endTime
     * @param name
     * @param response
     * @return
     */
    @RequestMapping("exportDutyLog")
    @ResponseBody
    public boolean exportDutyLog(String startTime,String endTime,String name,HttpServletResponse response) throws Exception{
        dutyService.exportDutyLog(startTime,endTime,name,getSchoolId(),response);
        return true;
    }

    /**
     * 设置ip
     * @param ip
     * @return
     */
    @RequestMapping("/updateDutySetIp")
    @ResponseBody
    public Map updateDutySetIp(String ip,int year,int week) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            dutyService.updateDutySetIp(getSchoolId(), ip,year,week);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
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

    /**
     * 编辑值班说明
     * @param explain
     * @return
     */
    @RequestMapping("addDutyExplain")
    @ResponseBody
    public Map addDutyExplain(int year,int week,String explain) {
        Map map = new HashMap();
        try {
            dutyService.addDutyExplain(getSchoolId(),year,week, explain);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }
}
