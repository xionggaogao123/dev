package com.fulaan.excellentCourses.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            String result= excellentCoursesService.addEntry(dto,getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("批量保存课时败!");
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

            if(startNum>=endNum){
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
     * 创建直播间回调(提供给cc直播进行接口验证)
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
}
