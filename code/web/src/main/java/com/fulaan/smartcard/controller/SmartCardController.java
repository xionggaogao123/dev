package com.fulaan.smartcard.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.smartcard.dto.DoorDTO;
import com.fulaan.smartcard.dto.KaoQinDTO;
import com.fulaan.smartcard.dto.KaoQinStateDTO;
import com.fulaan.smartcard.service.AsWebService;
import com.fulaan.smartcard.service.SmartCardService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SimpleDTO;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/5/27.
 */
@Controller
@RequestMapping("/smartCard")
public class SmartCardController extends BaseController {

    private static final Logger logger = Logger.getLogger(SmartCardController.class);

    @Autowired
    private SmartCardService smartCardService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;
    /**
     * 我的考勤
     * @param model
     * @return
     */
    @RequestMapping("/myKaoQin")
    public String myKaoQin(Map<String, Object> model) {
        String selDate= DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM);
        model.put("userId", getSessionValue().getId());
        model.put("userName",getSessionValue().getUserName());
        model.put("selDate",selDate);
        return "smartcard/mykaoqin";
    }

    /**
     * 查询考勤记录
     * @return Map
     */
    @RequestMapping("/searchKaoQinData")
    public @ResponseBody Map searchKaoQinData(
            @RequestParam(value="selDate", defaultValue = "")String selDate,
            @RequestParam(value="userId", defaultValue = "")String userId){
        Map map=new HashMap();
        if("".equals(selDate)){
            selDate=DateTimeUtils.getCurrDate();
        }
        if("".equals(userId)){
            userId=getSessionValue().getId();
        }
        ObjectId schoolId=getSchoolId();
        List<List<KaoQinDTO>> list=smartCardService.searchKaoQinData(selDate,userId,schoolId);
        map.put("list",list);
        return map;
    }

    /**
     * 学生考勤统计
     * @param model
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/stuKaoQin")
    public String stuKaoQin(Map<String, Object> model) {
        ObjectId teacherId=getUserId();
        ObjectId schoolId=getSchoolId();
        //获取年级的班级
        List<GradeView> gradeList=classService.getSimpleGradeViews(teacherId,schoolId);
        model.put("gradeList",gradeList);
        return "smartcard/stukaoqin";
    }

    /**
     * 查询某年级老师所带的班级
     * @return Map
     */
    @RequestMapping("/getTeacherClass")
    public @ResponseBody Map getTeacherClass(@RequestParam("gradeId") String gradeId){
        Map map=new HashMap();
        ObjectId teacherId=getUserId();
        List<ClassInfoDTO> list=classService.getGradeClassesInfo(new ObjectId(gradeId),teacherId);
        map.put("list",list);
        return map;
    }

    /**
     * 查询考勤记录
     * @return Map
     */
    @RequestMapping("/searchStuKaoQinData")
    public @ResponseBody Map searchStuKaoQinData(
            String type,
            @RequestParam("classId") String classId){
        Map map=new HashMap();
        int kaoQinTotal=smartCardService.getKaoQinTotal(type,2);
        List<KaoQinStateDTO> list=smartCardService.searchKaoQinData("stu",type,classId,"");
        KaoQinStateDTO kaoQinReta=smartCardService.getKaoQinReta(list,type);
        map.put("kaoQinTotal",kaoQinTotal);
        map.put("list",list);
        map.put("kaoQinReta",kaoQinReta);
        return map;
    }

    /**
     * 查询考勤记录
     * @return Map
     */
    @RequestMapping("/searchTeaKaoQinData")
    public @ResponseBody Map searchTeaKaoQinData(
            String type,
            @RequestParam("deptId") String deptId){
        Map map=new HashMap();
        int kaoQinTotal=smartCardService.getKaoQinTotal(type,2);
        List<KaoQinStateDTO> list=smartCardService.searchKaoQinData("tea",type,"",deptId);
        KaoQinStateDTO kaoQinReta=smartCardService.getKaoQinReta(list,type);
        map.put("kaoQinTotal",kaoQinTotal);
        map.put("list",list);
        map.put("kaoQinReta",kaoQinReta);
        return map;
    }

    /**
     * 用户考勤详情
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping("/userKaoQin")
    public String userKaoQin(String userId, Map<String, Object> model) {
        String selDate= DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM);
        UserEntry userEntry=userService.searchUserId(new ObjectId(userId));
        if(UserRole.isStudent(userEntry.getRole())){
            model.put("userRole", "1");
            model.put("userRoleDes", "学生");
            model.put("backUrl","/smartCard/stuKaoQin.do");
        }
        if(UserRole.isTeacher(userEntry.getRole())){
            model.put("userRole", "2");
            model.put("userRoleDes", "老师");
            model.put("backUrl","/smartCard/teaKaoQin.do");
        }
        model.put("userName", userEntry.getUserName());
        model.put("userId", userEntry.getID());
        model.put("selDate",selDate);
        return "smartcard/userkaoqin";
    }

    /**
     * 老师考勤统计
     * @param model
     * @return
     */
    @RequestMapping("/teaKaoQin")
    public String teaKaoQin(Map<String, Object> model) {
        if(UserRole.isHeadmaster(getSessionValue().getUserRole())){
            ObjectId schoolId=getSchoolId();
            List<SimpleDTO> list= schoolService.getDepartments(schoolId);
            model.put("list",list);
            return "smartcard/teakaoqin";
        }else{
            return "";
        }
    }
    /**
     * 学生考勤
     * @param model
     * @return
     */
    @RequestMapping("/gradeKaoQin")
    public String gradeKaoQin(Map<String, Object> model) {
        if(UserRole.isHeadmaster(getSessionValue().getUserRole())){
            return "smartcard/gradekaoqin";
        }else{
            return "";
        }
    }

    /**
     * 查询考勤记录
     * @return Map
     */
    @RequestMapping("/searchGradeKaoQinData")
    public @ResponseBody Map searchGradeKaoQinData(
            String type){
        ObjectId schoolId=getSchoolId();
        Map map=smartCardService.searchGradeKaoQinData(schoolId, type);
        return map;
    }

    /**
     * 学生考勤
     * @param model
     * @return
     */
    @RequestMapping("/classKaoQin")
    public String classKaoQin(String gradeId, String gradeName, Map<String, Object> model) {
        if(UserRole.isHeadmaster(getSessionValue().getUserRole())){
            model.put("gradeId",gradeId);
            model.put("gradeName",gradeName);
            return "smartcard/classkaoqin";
        }else{
            return "";
        }
    }

    /**
     * 查询考勤记录
     * @return Map
     */
    @RequestMapping("/searchClassKaoQinData")
    public @ResponseBody Map searchClassKaoQinData(
            String gradeId,
            String type){
        Map map=smartCardService.searchClassKaoQinData(gradeId, type);
        return map;
    }
    /**
     * 学生考勤
     * @param model
     * @return
     */
    @RequestMapping("/classStuKaoQin")
    public String classStuKaoQin(String classId, String className, String gradeId,String gradeName, Map<String, Object> model) {
        if(UserRole.isHeadmaster(getSessionValue().getUserRole())){
            model.put("classId",classId);
            model.put("className",className);
            model.put("gradeId",gradeId);
            model.put("gradeName",gradeName);
            return "smartcard/classstukaoqin";
        }else{
            return "";
        }
    }
    /**
     * 用户考勤详情
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping("/classStuKaoQinDetail")
    public String classStuKaoQinDetail(
            String userId,
            String gradeId,
            String gradeName,
            String classId,
            String className,
            Map<String, Object> model) {
        String selDate= DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM);
        UserEntry userEntry=userService.searchUserId(new ObjectId(userId));
        model.put("gradeId", gradeId);
        model.put("gradeName", gradeName);
        model.put("classId", classId);
        model.put("className", className);
        model.put("userName", userEntry.getUserName());
        model.put("userId", userEntry.getID());
        model.put("selDate",selDate);
        return "smartcard/stukaoqindetail";
    }

    /**
     * 用户门禁
     * @param model
     * @return
     */
    @RequestMapping("/userDoor")
    public String userDoor(
            Map<String, Object> model) {
        return "smartcard/userdoor";
    }

    /**
     * 门禁数据
     * @param page
     * @param pageSize
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    @RequestMapping("searchDoorData")
    public @ResponseBody Map<String,Object> searchDoorData(
            @RequestParam(value="name", defaultValue = "") String name,
            @RequestParam(value="selState", defaultValue = "") String selState,
            @RequestParam(value="doorNum", defaultValue = "") String doorNum,
            @RequestParam(value="dateStart", defaultValue = "") String dateStart,
            @RequestParam(value="dateEnd", defaultValue = "") String dateEnd,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
            ) throws ResultTooManyException {

        Map<String,Object> model = new HashMap<String,Object>();
        if (pageSize == 0) {
            pageSize = 20;
        }
        if(null!=dateStart&&!"".equals(dateStart)){
            dateStart=dateStart+" 00:00:00";
        }else{
            dateStart="";
        }
        if(null!=dateEnd&&!"".equals(dateEnd)) {
            dateEnd = dateEnd + " 23:59:59";
        }else{
            dateStart="";
        }
        int count = smartCardService.searchDoorDataListCount(name, selState, doorNum, dateStart, dateEnd);
        List<DoorDTO> list = smartCardService.searchDoorDataList(name, selState, doorNum, dateStart, dateEnd, page, pageSize);

        model.put("rows",list);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }


    /**
     * 生成用户考勤门禁数据
     * @param model
     * @return
     */
    @RequestMapping("/createUserKaoQinData")
    public String createUserKaoQinData(
            Map<String, Object> model) {
        AsWebService test = new AsWebService();
        DateTimeUtils time=new DateTimeUtils();
        Date prevDay=time.getPrevDay(new Date(),-1);
        String prevDayStr = time.getDateToStrTime(prevDay);
        String currDayStr = time.getCurrDate();
        test.createUserKaoQinData(prevDayStr,currDayStr);
        //test.createUserDoorData(prevDayStr,currDayStr);
        return "";
    }
}
