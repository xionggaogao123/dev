package com.fulaan.excellentCourses.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.service.ExcellentCoursesService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-04-26.
 */
@Api(value="app精品课")
@Controller
@RequestMapping("/jxmapi/excellentCourses")
public class ExcellentCoursesController extends BaseController {
    @Autowired
    private ExcellentCoursesService excellentCoursesService;



    /****************************** 学生端 ********************************/
    /**
     * 我的课程
     */
    @ApiOperation(value = "我的课程", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyCoursesList")
    @ResponseBody
    public String getMyCoursesList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getMyCoursesList(getUserId());
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取我的课程失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 课节列表
     */
    @ApiOperation(value = "课节列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCoursesDesc")
    @ResponseBody
    public String getCoursesDesc(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getCoursesDesc(new ObjectId(id),getUserId());
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程简介失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 简介
     */
    @ApiOperation(value = "简介", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleDesc")
    @ResponseBody
    public String getSimpleDesc(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ExcellentCoursesDTO map = excellentCoursesService.getSimpleDesc(new ObjectId(id), getUserId());
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程简介失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 购买课节
     */
    @ApiOperation(value = "购买课节", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/buyClassList")
    @ResponseBody
    public String buyClassList(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                 @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = excellentCoursesService.buyClassList(new ObjectId(id),getUserId(),classIds);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("购买课节失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 我的收藏
     */
    @ApiOperation(value = "我的收藏", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/myCollectList")
    @ResponseBody
    public String myCollectList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.myCollectList(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询我的收藏失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  课程中心
     */
    @ApiOperation(value = "课程中心", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/myClassList")
    @ResponseBody
    public String myClassList(@ApiParam(name = "subjectId", required = false, value = "subjectId") @RequestParam(value="subjectId",defaultValue = "") String subjectId,
                               @ApiParam(name = "priceType", required = false, value = "priceType") @RequestParam(value="priceType",defaultValue = "1") int priceType,//1 正序  -1 倒叙
                                  @ApiParam(name = "persionType", required = false, value = "persionType") @RequestParam(value="persionType",defaultValue = "1") int persionType,
                                  @ApiParam(name = "timeType", required = false, value = "timeType") @RequestParam(value="timeType",defaultValue = "1") int timeType,
                                  @ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "1") int page,
                                  @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = excellentCoursesService.myClassList(getUserId(),subjectId, priceType, persionType, timeType, page, pageSize);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程中心失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  搜索课程中心
     */
    @ApiOperation(value = "搜索课程中心", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/myKeyClassList")
    @ResponseBody
    public String myKeyClassList(@ApiParam(name = "keyword", required = false, value = "keyword") @RequestParam(value="keyword",defaultValue = "") String keyword){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ExcellentCoursesDTO> result = excellentCoursesService.myKeyClassList(keyword);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("搜索课程中心失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  收藏
     */
    @ApiOperation(value = "收藏", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCollect")
    @ResponseBody
    public String addCollect(@ApiParam(name = "id", required = false, value = "id") @RequestParam(value="id",defaultValue = "") String id,
                              @ApiParam(name = "type", required = false, value = "0为取消 1为添加") @RequestParam(value="type",defaultValue = "1") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.addCollect(getUserId(), type, new ObjectId(id));
            if(type==0){
                respObj.setMessage("取消成功！");
            }else{
                respObj.setMessage("收藏成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询我的课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  获取最近要上的一节课
     */
    @ApiOperation(value = "获取最近要上的一节课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleCourses")
    @ResponseBody
    public String getSimpleCourses(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object>  dto = excellentCoursesService.getSimpleCourses(getUserId());
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询我的课程失败!");
        }
        return JSON.toJSONString(respObj);
    }
}
