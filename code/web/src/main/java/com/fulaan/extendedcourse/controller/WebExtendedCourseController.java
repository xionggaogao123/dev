package com.fulaan.extendedcourse.controller;

import com.db.backstage.TeacherApproveDao;
import com.db.integral.IntegralSufferDao;
import com.db.user.UserDao;
import com.fulaan.base.BaseController;
import com.fulaan.dto.VideoDTO;
import com.fulaan.extendedcourse.dto.ExtendedCourseDTO;
import com.fulaan.extendedcourse.dto.ExtendedSchoolLabelDTO;
import com.fulaan.extendedcourse.dto.ExtendedSchoolTeacherDTO;
import com.fulaan.extendedcourse.service.ExtendedCourseService;
import com.fulaan.pojo.Attachement;
import com.fulaan.utils.HSSFUtils;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-12-07.
 */
@Api(value="拓展课")
@Controller
@RequestMapping("/web/extendedcourse")
public class WebExtendedCourseController extends BaseController {

    @Autowired
    private ExtendedCourseService extendedCourseService;

    private static final Logger logger = Logger.getLogger(ExtendedCourseController.class);
    /**
     * 学校开课类型设置
     */
    @ApiOperation(value = "设置学校开课类型", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveExtendedSchoolSetting")
    @ResponseBody
    public RespObj saveExtendedSchoolSetting(@RequestParam(value="schoolId") String schoolId,
                                             @RequestParam(value="courseType") int courseType){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.saveExtendedSchoolSettingEntry(new ObjectId(schoolId),courseType);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改开课类型失败");
        }
        return respObj;
    }

    /**
     * 获得学校的设置
     */
    @ApiOperation(value = "获得学校的设置", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getExtendedSchoolSetting")
    @ResponseBody
    public RespObj getExtendedSchoolSetting(@RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.getExtendedSchoolSetting(new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改开课类型失败");
        }
        return respObj;
    }

    /**
     * 删除课程标签
     */
    @ApiOperation(value = "删除课程标签", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteExtendedSchoolLabel")
    @ResponseBody
    public RespObj deleteExtendedSchoolLabel(@RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteExtendedSchoolLabel(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除课程标签失败");
        }
        return respObj;
    }

    /**
     * 删除课程老师
     */
    @ApiOperation(value = "删除课程老师", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteExtendedSchoolTeacher")
    @ResponseBody
    public RespObj deleteExtendedSchoolTeacher(@RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteExtendedSchoolTeacher(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除课程标签失败");
        }
        return respObj;
    }

    /**
     * 添加课程标签
     */
    @ApiOperation(value = "添加课程标签", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addExtendedSchoolLabel")
    @ResponseBody
    public RespObj addExtendedSchoolLabel(@RequestParam(value="name") String name,
                                          @RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.addExtendedSchoolLabel(new ObjectId(schoolId),getUserId(),name);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程标签失败");
        }
        return respObj;
    }

    /**
     * 添加课程老师
     */
    @ApiOperation(value = "添加课程老师", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addExtendedSchoolTeacher")
    @ResponseBody
    public RespObj addExtendedSchoolTeacher(@RequestParam(value="name") String name,
                                          @RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.addExtendedSchoolTeacher(new ObjectId(schoolId), getUserId(), name);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程标签失败");
        }
        return respObj;
    }


    /**
     * 查询课程标签列表
     */
    @ApiOperation(value = "查询课程标签列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectExtendedSchoolLabelList")
    @ResponseBody
    public RespObj selectExtendedSchoolLabelList(@RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<ExtendedSchoolLabelDTO> result = extendedCourseService.selectExtendedSchoolLabelList(new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程标签失败");
        }
        return respObj;
    }

    /**
     * 查询课程老师列表
     */
    @ApiOperation(value = "查询课程老师列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectExtendedSchoolTeacherList")
    @ResponseBody
    public RespObj selectExtendedSchoolTeacherList(@RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<ExtendedSchoolTeacherDTO> result = extendedCourseService.selectExtendedSchoolTeacherList(new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程标签失败");
        }
        return respObj;
    }


    /**
     * 新增拓展课
     */
    @ApiOperation(value = "新增拓展课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveExtendedCourse")
    @ResponseBody
    public RespObj saveExtendedCourse(@RequestBody ExtendedCourseDTO dto){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.saveExtendedCourse(dto,getUserId(),new ObjectId(dto.getSchoolId()));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("新增拓展课失败");
        }
        return respObj;
    }

    public static void main(String[] args){
        ExtendedCourseDTO dto = new ExtendedCourseDTO();
        dto.setCourseName("火速");
        dto.setDescription("美术拓展课");
        dto.setTypeId(null);
        dto.setTypeName("美术");
        dto.setApplyStartTime("2018-12-19 11:14:00");
        dto.setApplyEndTime("2018-12-20 11:30:00");
        dto.setVoteStartTime("2018-12-21 15:14:00");
        dto.setVoteEndTime("2018-12-30 15:14:00");
        dto.setWeek(2);
        dto.setType(2);
        dto.setLessonType(8);
        dto.setTeacherName("James老师");
        dto.setTypeId("");
        List<String> gradeList = new ArrayList<String>();
        gradeList.add("2");
        gradeList.add("3");
        dto.setGradeList(gradeList);
        dto.setUserAllNumber(20);
        dto.setClassUserNumber(0);
        dto.setRoomName("美术大教室");
        List<VideoDTO> videoList=new ArrayList<VideoDTO>();           //提交
        List<Attachement> imageList=new ArrayList<Attachement>();     //提交
        List<Attachement> attachements=new ArrayList<Attachement>();  //提交
        List<Attachement> voiceList=new ArrayList<Attachement>();     //提交
        dto.setVideoList(videoList);
        dto.setImageList(imageList);
        dto.setAttachements(attachements);
        dto.setVoiceList(voiceList);
        ExtendedCourseService extendedCourseService =  new ExtendedCourseService();
        extendedCourseService.saveExtendedCourse(dto,new ObjectId("58f6bea2de04cb5a4bc72d38"),new ObjectId("5c0f2dc41f7e9d303818dda9"));
    }

//    public static void main(String[] args){
//        TeacherApproveDao teacherApproveDao1 = new TeacherApproveDao();
//        IntegralSufferDao integralSufferDao = new IntegralSufferDao();
//        UserDao userDao1 = new UserDao();
//        List<TeacherApproveEntry> entrys = teacherApproveDao1.selectContentList("", 2, 1, 700);
//        List<ObjectId> objectIds = new ArrayList<ObjectId>();
//        for(TeacherApproveEntry teacherApproveEntry:entrys){
//            objectIds.add(teacherApproveEntry.getUserId());
//        }
//        Map<ObjectId, UserEntry> map = userDao1.getUserEntryMap(objectIds, Constant.FIELDS);
//        Map<ObjectId,Integer> integralSufferEntries = integralSufferDao.selectContentList(objectIds);
//        StringBuffer sb = new StringBuffer();
//        for(ObjectId oid : objectIds){
//            UserEntry userEntry = map.get(oid);
//            if(userEntry!=null){
//                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
//                int score = 0;
//                Integer integer = integralSufferEntries.get(oid);
//                if(integer!=null){
//                    score = integer;
//                }
//                sb.append("用户名："+name+"\t 家校美ID："+userEntry.getGenerateUserCode()+"  积分："+score+"\r\n");
//            }
//        }
//        try {
//            FileOutputStream bos = new FileOutputStream("D://大V积分.txt");
//            System.setOut(new PrintStream(bos));
//            System.out.println(sb.toString());
//        }catch ( Exception e){
//
//        }
//
//    }

    /**
     * 新增拓展课
     */
    @ApiOperation(value = "新增拓展课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getExcl")
    @ResponseBody
    public void getExcl(HttpServletResponse response,
                           HttpServletRequest request){
        TeacherApproveDao teacherApproveDao1 = new TeacherApproveDao();
        IntegralSufferDao integralSufferDao = new IntegralSufferDao();
        UserDao userDao1 = new UserDao();
        List<TeacherApproveEntry> entrys = teacherApproveDao1.selectContentList("", 2, 1, 700);
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for(TeacherApproveEntry teacherApproveEntry:entrys){
            objectIds.add(teacherApproveEntry.getUserId());
        }
        Map<ObjectId, UserEntry> map = userDao1.getUserEntryMap(objectIds, Constant.FIELDS);
        Map<ObjectId,Integer> integralSufferEntries = integralSufferDao.selectContentList(objectIds);
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet;
            String sheetName = "大V积分统计";

            try {
                sheet = wb.createSheet(sheetName);
            } catch (Exception e) {
                sheet = wb.createSheet("积分统计");
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            HSSFRow rowZero = sheet.createRow(0);
            HSSFCell cellZero = rowZero.createCell(0);
            long current = System.currentTimeMillis();

            cellZero.setCellValue("导出时间："+DateTimeUtils.getLongToStrTimeTwo(current));


            HSSFRow row = sheet.createRow(1);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("序号");

            cell = row.createCell(1);
            cell.setCellValue("用户名");

            cell = row.createCell(2);
            cell.setCellValue("家校美ID");

            cell = row.createCell(3);
            cell.setCellValue("积分");


            int rowLine = 2;

            HSSFRow rowItem;
            HSSFCell cellItem;
            int index = 0;
            for(ObjectId oid : objectIds) {
                UserEntry userEntry = map.get(oid);
                if (userEntry != null) {
                    String name = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                    int score = 0;
                    Integer integer = integralSufferEntries.get(oid);
                    if (integer != null) {
                        score = integer;
                    }
                    index++;
                    rowItem = sheet.createRow(rowLine);
                    cellItem = rowItem.createCell(0);
                    cellItem.setCellValue(index + "");

                    cellItem = rowItem.createCell(1);
                    cellItem.setCellValue(name);

                    cellItem = rowItem.createCell(2);
                    cellItem.setCellValue(userEntry.getGenerateUserCode());


                    cellItem = rowItem.createCell(3);
                    cellItem.setCellValue(score);

                    rowLine++;
                }
            }
            String fileName = sheetName + ".xls";
            String userAgent = request.getHeader("USER-AGENT");
            HSSFUtils.exportExcel(userAgent, response, wb, fileName);
        }catch ( Exception e){

        }
    }




    @ApiOperation(value = "查询社群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getCommunityList")
    @ResponseBody
    public RespObj getCommunityList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            //限定注册用户
            Map<String,Object> result =  extendedCourseService.getCommunityList(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }



    @ApiOperation(value = "添加新的孩子（限定）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/registerMoreUser")
    @ResponseBody
    public RespObj registerMoreUser(@RequestParam(value="userNames") String userNames,
                                    @RequestParam(value="communityId") String communityId,
                                    HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            String phoneNumber = "12345678900";
            //限定注册用户
            if(userNames==null){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("用户名不能为空！");
            }
            String[] strings = userNames.split("#%");
            if(strings.length>3){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("最多可添加3个孩子！");
            }
            String userId = "";
            for(String userName : strings){
                String uid =extendedCourseService.registerAvailableUser(request, userName, phoneNumber, Constant.TWO, userName, getUserId(),new ObjectId(communityId));
                userId = userId + uid + ",";
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(userId);
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询所有课程（app  家长、老师共用 三种状态）
     */
    @ApiOperation(value = "查询课程列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectExtendedCourseList")
    @ResponseBody
    public RespObj selectExtendedCourseList(@RequestParam(value="communityId") String communityId,
                                            @RequestParam(value="keyword",defaultValue = "") String keyword,
                                            @RequestParam(value="status",defaultValue = "0") int status,//0  全部   1  报名中   2  学习中   3 已学完
                                            @RequestParam(value="page",defaultValue = "1") int page,
                                            @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectWebExtendedCourseList(new ObjectId(communityId),getUserId(),keyword,status,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程列表失败");
        }
        return respObj;
    }


    /**
     * 获得老师权限及是否展示
     */
    @ApiOperation(value = "获得老师权限及是否展示", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getTeacherRole")
    @ResponseBody
    public RespObj getTeacherRole(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.getUserRole(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程列表失败");
        }
        return respObj;
    }


    /**
     * 查询所有课程（校领导）
     */
    @ApiOperation(value = "查询所有课程（校领导）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectAllExtendedCourseList")
    @ResponseBody
    public RespObj selectAllExtendedCourseList(@RequestParam(value="schoolId") String schoolId,
                                            @RequestParam(value="keyword",defaultValue = "") String keyword,
                                            @RequestParam(value="gradeId",defaultValue = "") String gradeId,
                                            @RequestParam(value="status",defaultValue = "0") int status,//0  全部   1  报名中   2  学习中   3 已学完
                                            @RequestParam(value="page",defaultValue = "1") int page,
                                            @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectAllWebExtendedCourseList(new ObjectId(schoolId),getUserId(),gradeId,keyword,status,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程列表失败");
        }
        return respObj;
    }

    /**
     * 删除课程（校领导）
     */
    @ApiOperation(value = "删除课程（校领导）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteCourse")
    @ResponseBody
    public RespObj deleteCourse(@RequestParam(value="schoolId") String schoolId,
                                               @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteCourse(new ObjectId(schoolId),getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除失败");
        }
        return respObj;
    }

    /**
     * 提前结束
     */
    @ApiOperation(value = "提前结束", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/endCourse")
    @ResponseBody
    public RespObj endCourse(@RequestParam(value="schoolId") String schoolId,
                                @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.endCourse(new ObjectId(schoolId), getUserId(), new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("提前结束成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("提前结束失败");
        }
        return respObj;
    }

    /**
     * 查看所有名单
     */
    @ApiOperation(value = "查看所有名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectAllUserList")
    @ResponseBody
    public RespObj selectAllUserList(@RequestParam(value="id",defaultValue = "") String id,
                                     @RequestParam(value="type",defaultValue = "") int type,
                                     @RequestParam(value="communityId",defaultValue = "") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectAllUserList(getUserId(), new ObjectId(id),communityId,type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查看所有名单失败");
        }
        return respObj;
    }


    /**
     * 打印所有名单
     */
    @ApiOperation(value = "打印所有名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getExclForSelectAllUserList")
    @ResponseBody
    public void getExclForSelectAllUserList(HttpServletResponse response,
                        HttpServletRequest request,@RequestParam(value="id",defaultValue = "") String id,
                        @RequestParam(value="type",defaultValue = "") int type,
                        @RequestParam(value="communityId",defaultValue = "") String communityId){
       try{
           extendedCourseService.getExclForSelectAllUserList(response, request, getUserId(), new ObjectId(id), communityId, type);
        }catch ( Exception e){

        }
    }

    /**
     * 查看详细名单
     */
    @ApiOperation(value = "查看详细名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectOneUserList")
    @ResponseBody
    public RespObj selectOneUserList(@RequestParam(value="id",defaultValue = "") String id,
                                     @RequestParam(value="communityId",defaultValue = "") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectOneUserList(getUserId(), new ObjectId(id), new ObjectId(communityId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查看详细名单失败");
        }
        return respObj;
    }

    /**
     * 编辑回显
     */
    @ApiOperation(value = "编辑回显", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editCourse")
    @ResponseBody
    public RespObj editCourse(@RequestParam(value="schoolId") String schoolId,
                              @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            ExtendedCourseDTO dto = extendedCourseService.editCourse(new ObjectId(schoolId), getUserId(), new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dto);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("编辑回显失败");
        }
        return respObj;
    }


    /**
     * 权限者调整报名名单
     */
    @ApiOperation(value = "权限者调整报名名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateUserList")
    @ResponseBody
    public RespObj updateUserList(@RequestParam(value="id",defaultValue = "") String id,
                                  @RequestParam(value="communityId",defaultValue = "") String communityId,
                                  @RequestParam(value="userIds",defaultValue = "") String userIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.updateUserList(new ObjectId(id), getUserId(),new ObjectId(communityId), userIds);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("权限者调整报名名单失败");
        }
        return respObj;
    }

    /**
     * 报名总览
     */
    @ApiOperation(value = "报名总览", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getAllCommunityAllUserList")
    @ResponseBody
    public RespObj getAllCommunityAllUserList(@RequestParam(value="status",defaultValue = "0") int status,
                                  @RequestParam(value="communityId",defaultValue = "") String communityId,
                                  @RequestParam(value="page",defaultValue = "1") int page,
                                  @RequestParam(value="pageSize",defaultValue = "1") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.getAllCommunityAllUserList(new ObjectId(communityId), status, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询报名总览失败");
        }
        return respObj;
    }

}
