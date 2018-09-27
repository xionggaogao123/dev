package com.fulaan.excellentCourses.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.excellentCourses.dto.CCLoginDTO;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.excellentCourses.dto.HourResultDTO;
import com.fulaan.excellentCourses.service.CoursesRoomService;
import com.fulaan.excellentCourses.service.ExcellentCoursesService;
import com.fulaan.pojo.User;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by James on 2018-04-26.
 */
@Api(value="web精品课")
@Controller
@RequestMapping("/web/excellentCourses")
public class WebExcellentCoursesController extends BaseController {
    @Autowired
    private ExcellentCoursesService excellentCoursesService;
    @Autowired
    private CoursesRoomService coursesRoomService;


    /**
     * 添加课程
     */
    @ApiOperation(value = "添加课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addExcellentCourses")
    @ResponseBody
    public String addExcellentCourses(@RequestBody ExcellentCoursesDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result= excellentCoursesService.addEntry(dto,getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 我的课程（分页）
     */
    @ApiOperation(value = "添加课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyExcellentCourses")
    @ResponseBody
    public String getMyExcellentCourses(@ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                        @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyExcellentCourses(getUserId(),page,pageSize);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  去上课
     */
    @ApiOperation(value = "去上课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/gotoClass")
    @ResponseBody
    public String gotoClass(@ApiParam(name = "id", required = false, value = "id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object>  dto = excellentCoursesService.gotoClass(new ObjectId(id),getUserId());
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  去上课
     */
    @SessionNeedless
    @ApiOperation(value = "去上课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/gotoWebClass")
    @ResponseBody
    public String gotoWebClass(@ApiParam(name = "id", required = false, value = "id") @RequestParam(value="id",defaultValue = "") String id,
                               @ApiParam(name = "userId", required = false, value = "userId") @RequestParam(value="userId",defaultValue = "") String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object>  dto = excellentCoursesService.gotoClass(new ObjectId(id),new ObjectId(userId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 我所有报名的课程（分页）
     */
    @ApiOperation(value = "我所有报名的课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyAllBuyExcellentCourses")
    @ResponseBody
    public String getMyAllBuyExcellentCourses(@ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "") int page,
                                              @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyAllBuyExcellentCourses(getUserId(),page,pageSize);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生我所有报名的课程（分页）
     */
    @SessionNeedless
    @ApiOperation(value = "我所有报名的课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentBuyExcellentCourses")
    @ResponseBody
    public RespObj getStudentBuyExcellentCourses(
                                              @ApiParam(name = "userId", required = false, value = "userId") @RequestParam(value="userId",defaultValue = "") String userId,
                                              @ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "") int page,
                                              @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getStudentBuyExcellentCourses(new ObjectId(userId), page, pageSize);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程失败!");
        }
        return respObj;
    }

    /**
     * 课程详情
     */
    @ApiOperation(value = "课程详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyDetails")
    @ResponseBody
    public String getMyDetails(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyDetails(new ObjectId(id));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程详情失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 课程详情（打包课）
     */
    @ApiOperation(value = "课程详情（打包课）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyNewDetails")
    @ResponseBody
    public String getMyNewDetails(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyNewDetails(new ObjectId(id), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程详情失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 课程详情
     */
    @ApiOperation(value = "课程详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyOwnDetails")
    @ResponseBody
    public String getMyOwnDetails(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyOwnDetails(new ObjectId(id),getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程详情失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生课程详情
     */
    @SessionNeedless
    @ApiOperation(value = "学生课程详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentDetails")
    @ResponseBody
    public RespObj getStudentDetails(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                     @ApiParam(name = "userId", required = true, value = "userId") @RequestParam("userId") String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.getMyOwnDetails(new ObjectId(id),new ObjectId(userId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程详情失败!");
        }
        return respObj;
    }

    /**
     * 获取所有报名人数
     */
    @ApiOperation(value = "获取所有报名人数", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCoursesPersionNum")
    @ResponseBody
    public String getCoursesPersionNum(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<User> result = excellentCoursesService.getCoursesPersionNum(new ObjectId(id));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取所有报名人数失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 删除课程
     */
    @ApiOperation(value = "删除课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteCourses")
    @ResponseBody
    public String deleteCourses(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = excellentCoursesService.deleteCourses(new ObjectId(id), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 是否可编辑
     */
    @ApiOperation(value = "是否可编辑", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/booleanUpdateCourses")
    @ResponseBody
    public String booleanUpdateCourses(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.booleanUpdateCourses(new ObjectId(id),getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("判断是否可编辑失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 批量保存课时
     */
    @ApiOperation(value = "批量保存课时", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addHourClassEntry")
    @ResponseBody
    public String addHourClassEntry(@RequestBody HourResultDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result= excellentCoursesService.addEntry(dto, getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 新批量保存课时
     */
    @ApiOperation(value = "新批量保存课时", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addNewHourClassEntry")
    @ResponseBody
    public String addNewHourClassEntry(@RequestBody HourResultDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result= excellentCoursesService.addNewEntry(dto, getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 完成申请
     */
    @ApiOperation(value = "完成申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/finishEntry")
    @ResponseBody
    public String finishEntry(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.finishEntry(new ObjectId(id),getUserId());
            respObj.setMessage("完成申请");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("完成申请!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 解析批量设置课时
     */
    @ApiOperation(value = "完成申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/piList")
    @ResponseBody
    public RespObj piList(@ApiParam(name = "start", required = true, value = "start") @RequestParam("start") String start,
                         @ApiParam(name = "end", required = true, value = "end") @RequestParam("end") String end,
                         @ApiParam(name = "week", required = true, value = "week") @RequestParam("week") String weekList){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<String> str = new ArrayList<String>();
            List<Long> numberList = new ArrayList<Long>();
            respObj.setCode(Constant.SUCCESS_CODE);
            long startNum = DateTimeUtils.getStrToLongTime(start, "yyyy-MM-dd");
            long endNum = DateTimeUtils.getStrToLongTime(end, "yyyy-MM-dd");

            if(startNum>endNum){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("结束时间应大于开始时间！");
            }
            //管控时间
            Date date = new Date(startNum);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            boolean flage = true;
            long ssstime = startNum;
            String[] strings = weekList.split(",");
            for(int j = 0; j < strings.length;j++){
                int week = Integer.parseInt(strings[j]);
                int i = 0;
                if(w< week){
                    startNum = startNum + (week-w)*24*60*60*1000;
                }else if(w==week){

                }else{//3   1
                    startNum = startNum + (7-w+week)*24*60*60*1000;
                }
                if(endNum>=startNum){
                    numberList.add(startNum);
                }else{
                    flage = false;
                }
                while(flage){
                    i++;
                    startNum += 7*24*60*60*1000;
                    if(endNum>=startNum){
                       // str.add(DateTimeUtils.getLongToStrTimeTwo(startNum).substring(0,11));
                        numberList.add(startNum);
                    }else{
                        flage = false;
                    }
                    if(i>100){
                        flage = false;
                    }
                }
                startNum = ssstime;
                flage = true;
            }
            if(numberList.size()==0){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("无符合的开课日期！");
            }
            //排序
            Collections.sort(numberList);
            for(Long lo  :numberList){
                str.add(DateTimeUtils.getLongToStrTimeTwo(lo).substring(0,11));
            }
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("解析批量设置课时失败!");
        }
        return respObj;
    }

    /**
     * 重新开课
     */
    @ApiOperation(value = "重新开课", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/newOpen")
    @ResponseBody
    public RespObj newOpen(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String str = excellentCoursesService.newOpen(getUserId(), new ObjectId(id));
            if(str.equals("")){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("课程已被删除！");
            }else{
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(str);
            }
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 查询所有用户的提现申请
     */
    @ApiOperation(value = "查询所有用户的提现申请", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectAllUserMoney")
    @ResponseBody
    public RespObj selectAllUserMoney(@ApiParam(name = "type", required = true, value = "1 申请中  2 已通过  3 已拒绝") @RequestParam("type") int type,
            @ApiParam(name = "jiaId", required = true, value = "家校美id") @RequestParam(value="jiaId",defaultValue = "") String jiaId,
            @ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> str = excellentCoursesService.selectAllUserMoney(jiaId, type,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(str);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 通过提现
     */
    @ApiOperation(value = "通过提现", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/agreeUserMoney")
    @ResponseBody
    public RespObj agreeUserMoney(@ApiParam(name = "id", required = true, value = "记录id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            excellentCoursesService.agreeUserMoney(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("通过提现成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 拒绝提现
     */
    @ApiOperation(value = "拒绝提现", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteUserMoney")
    @ResponseBody
    public RespObj deleteUserMoney(@ApiParam(name = "id", required = true, value = "记录id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            excellentCoursesService.deleteUserMoney(new ObjectId(id), getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("拒绝提现成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 查看账户记录
     */
    @ApiOperation(value = "查看账户记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectUserList")
    @ResponseBody
    public RespObj selectUserList(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam(value="userId",defaultValue = "") String userId,
                                  @ApiParam(name = "contactId", required = true, value = "关联id") @RequestParam(value="contactId",defaultValue = "") String contactId,
                                  @ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "1") int page,
    @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = excellentCoursesService.selectUserList(new ObjectId(userId),contactId,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 设为轮播（取消轮播）
     */
    @ApiOperation(value = "设为轮播", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/setLun")
    @ResponseBody
    public RespObj setLun(@ApiParam(name = "id", required = true, value = "id") @RequestParam(value="id",defaultValue = "") String id,
                          @ApiParam(name = "top", required = true, value = "top") @RequestParam(value="top",defaultValue = "") int top){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            excellentCoursesService.setLun(new ObjectId(id),getUserId(),top);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设为轮播成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询轮播列表
     */
    @ApiOperation(value = "查询轮播列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getLunList")
    @ResponseBody
    public RespObj getLunList(@ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "1") int page,
                          @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = excellentCoursesService.getOldLunList(page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /****************************************  cc直播相关  start  *****************************************/
    /**
     * 创建直播间回调(提供给cc直播进行接口验证)
     *
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping(value="/openOldBackCreate",method = RequestMethod.POST)
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> openOldBackCreate( @RequestBody CCLoginDTO dto){
        Map<String,Object> map = coursesRoomService.openBackCreate(dto);
        return map;
    }

    /**
     * 创建直播间回调(提供给cc直播进行接口验证)
     *
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping(value="/openBackCreate")
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> openBackCreate(String userid,String roomid,String viewername,String viewertoken,String viewercustomua,String liveid,String recordid){
        CCLoginDTO dto = new CCLoginDTO(userid,roomid,viewername,viewertoken,viewercustomua,liveid,recordid);
        Map<String,Object> map = coursesRoomService.openBackCreate(dto);
        return map;
    }

    /**
     * 老师开课回调
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/endBackCreate")
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> endBackCreate(){
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,String> room = new HashMap<String, String>();
        room.put("id","");
        room.put("publishUrl","");
        try{


        }catch (Exception e){
            map.put("result","FAIL");
            map.put("room",room);
        }
        return map;
    }

    /**
     * 老师结束课程回调
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/backCreate")
    @ResponseBody
    @SessionNeedless
    public Map<String,Object> backCreate(){
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,String> room = new HashMap<String, String>();
        room.put("id","");
        room.put("publishUrl","");
        try{


        }catch (Exception e){
            map.put("result","FAIL");
            map.put("room",room);
        }
        return map;
    }

    /**
     * 老师自动登陆
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/teacherLogin")
    @ResponseBody
    public RespObj teacherLogin(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            /*https://view.csslcloud.net/api/view/lecturer?roomid=xxx&userid=xxx&publishname=xxx&publishpassword=xxx*/
            String result = excellentCoursesService.teacherLogin(getUserId(),new ObjectId(id));
            if(result.equals("")){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("时间已过了");
            }else{
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 新老师自动登陆
     */
    @ApiOperation(value = "提供给cc直播进行接口验证", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/teacherNewLogin")
    @ResponseBody
    public RespObj teacherNewLogin(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            /*https://view.csslcloud.net/api/view/lecturer?roomid=xxx&userid=xxx&publishname=xxx&publishpassword=xxx*/
            String result = excellentCoursesService.teacherNewLogin(getUserId(),new ObjectId(id));
            if(result.equals("")){
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage("时间已过了");
            }else{
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /****************************************  cc直播相关  end  *****************************************/

    /**
     *  消费记录（家长）
     */
    @ApiOperation(value = "消费记录（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/accountMyList")
    @ResponseBody
    public String accountMyList(@ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "") int  page,
                              @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "") int  pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object>  dto = excellentCoursesService.accountMyList(page, pageSize, getUserId());
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 后台直接购课
     */
    @ApiOperation(value = "购买课节（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/buyOwnClassList")
    @ResponseBody
    public String buyOwnClassList(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                    @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId,
                                    @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds,
                                    HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String ipconfig = "";
            if (request.getHeader("x-forwarded-for") == null) {
                ipconfig =  request.getRemoteAddr();
            }else{
                ipconfig =  request.getHeader("x-forwarded-for");
            }
            String result = excellentCoursesService.buyOwnClassList(new ObjectId(id), getUserId(), classIds, new ObjectId(sonId), ipconfig);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 新后台直接购课
     */
    @ApiOperation(value = "购买课节（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/buyNewOwnClassList")
    @ResponseBody
    public String buyNewOwnClassList(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                  @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId,
                                  @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds,
                                  @ApiParam(name = "role", required = true, value = "role") @RequestParam("role") int role,
                                  @ApiParam(name = "money", required = true, value = "money") @RequestParam("money") int money,
                                  HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String ipconfig = "";
            if (request.getHeader("x-forwarded-for") == null) {
                ipconfig =  request.getRemoteAddr();
            }else{
                ipconfig =  request.getHeader("x-forwarded-for");
            }
            String result = excellentCoursesService.buyNewOwnClassList(new ObjectId(id), getUserId(), classIds, new ObjectId(sonId), ipconfig,role,money);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }
    
    /**
     * 简洁查询余额（家长）
     */
    @ApiOperation(value = "简洁查询余额", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyAccount")
    @ResponseBody
    public String getMyAccount(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            double result = excellentCoursesService.getMyAccount(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }
    
    /**
     *  修改支付宝账户
     */
    @ApiOperation(value = "修改支付宝账户", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateAccount")
    @ResponseBody
    public String updateAccount(@ApiParam(name = "accountName", required = true, value = "accountName") @RequestParam("accountName") String accountName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateAccount(accountName,getUserId());
            respObj.setMessage("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询用户推送社群
     */
    @ApiOperation(value = "查询用户推送社群", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommunityList")
    @ResponseBody
    public String getCommunityList(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<CommunityDTO> result = excellentCoursesService.getCommunityList(new ObjectId(id), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 修改推送社群
     */
    @ApiOperation(value = "修改推送社群", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateCommunityList")
    @ResponseBody
    public String updateCommunityList(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id,
                                      @ApiParam(name = "communityList", required = true, value = "communityList") @RequestParam String communityList){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String message = excellentCoursesService.updateCommunityList(new ObjectId(id), communityList);
            respObj.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 查询某个用户的上课信息
     */
    @ApiOperation(value = "查询某个用户的上课信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectUserClassDesc")
    @ResponseBody
    public String selectUserClassDesc(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id,
                                      @ApiParam(name = "userId", required = true, value = "userId") @RequestParam String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> message= excellentCoursesService.selectSimpleList(new ObjectId(id), new ObjectId(userId));
            respObj.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 删除课程小节
     */
    @ApiOperation(value = "删除课程小节", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteClass")
    @ResponseBody
    public String deleteClass(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String message = excellentCoursesService.deleteClass(new ObjectId(id),getUserId());
            respObj.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修改总价
     */
    @ApiOperation(value = "修改总价", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateAllPrice")
    @ResponseBody
    public String updateAllPrice(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id,
                                 @ApiParam(name = "price", required = true, value = "price") @RequestParam double price){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateAllPrice(new ObjectId(id),price , getUserId());
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 修改/增加课程小结
     */
    @ApiOperation(value = "修改/增加课程小结", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateOneClass")
    @ResponseBody
    public String updateOneClass(@ApiParam(name = "dto", required = true, value = "dto") HourClassDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateOneClass(new ObjectId(dto.getParentId()),dto ,getUserId(),dto.getStatus());
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 批量修改时间
     */
    @ApiOperation(value = "批量修改时间", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateClassTime")
    @ResponseBody
    public String updateClassTime(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id,
                                  @ApiParam(name = "ids", required = true, value = "ids") @RequestParam String ids){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateClassTime(getUserId(), new ObjectId(id), ids);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 批量修改老师
     */
    @ApiOperation(value = "批量修改老师", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateClassTeacher")
    @ResponseBody
    public String updateClassTeacher(@ApiParam(name = "id", required = true, value = "id") @RequestParam String id,
                                     @ApiParam(name = "ids", required = true, value = "ids") @RequestParam String ids,
                                     @ApiParam(name = "ownId", required = true, value = "ownId") @RequestParam String ownId,
                                     @ApiParam(name = "ownName", required = true, value = "ownName") @RequestParam String ownName,
                                     @ApiParam(name = "subjectName", required = true, value = "subjectName") @RequestParam String subjectName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateClassTeacher(getUserId(), new ObjectId(id),ids,new ObjectId(ownId),ownName,subjectName);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

}
