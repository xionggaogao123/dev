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
     * 我的活跃课程
     */
    @ApiOperation(value = "我的活跃课程", httpMethod = "GET", produces = "application/json")
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
            respObj.setErrorMessage("获取我的活跃课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 家长孩子们的活跃课程
     */
    @ApiOperation(value = "家长孩子们的活跃课程", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyChildCoursesList")
    @ResponseBody
    public String getMyChildCoursesList(@ApiParam(name= "孩子id",required = true,value="")@RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getMyChildCoursesList(new ObjectId(sonId));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取活跃课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 我的过期课程
     */
    @ApiOperation(value = "我的过期课程", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyOldCoursesList")
    @ResponseBody
    public String getMyOldCoursesList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getOldMyCoursesList(getUserId());
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取我的课程失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 家长孩子们的过期课程
     */
    @ApiOperation(value = "家长孩子们的过期课程", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyChildOldCoursesList")
    @ResponseBody
    public String getMyChildOldCoursesList(@ApiParam(name= "孩子id",required = true,value="")@RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getMyChildOldCoursesList(new ObjectId(sonId));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程失败!");
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
     * 课节列表(家长)
     */
    @ApiOperation(value = "课节列表（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getChildCoursesDesc")
    @ResponseBody
    public String getChildCoursesDesc(@ApiParam(name = "id", required = true, value = "课程id") @RequestParam("id") String id,
                                      @ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getCoursesDesc(new ObjectId(id),new ObjectId(sonId));
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
     * 简介（家长）
     */
    @ApiOperation(value = "简介（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getChildSimpleDesc")
    @ResponseBody
    public String getChildSimpleDesc(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                     @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ExcellentCoursesDTO map = excellentCoursesService.getChildSimpleDesc(new ObjectId(id), getUserId(), new ObjectId(sonId));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程简介失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 首页简介（家长）
     */
    @ApiOperation(value = "简介（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOneSimpleDesc")
    @ResponseBody
    public String getOneSimpleDesc(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ExcellentCoursesDTO map = excellentCoursesService.getOneSimpleDesc(new ObjectId(id), getUserId());
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程简介失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 首页课节列表（家长）
     * @param id
     * @return
     */
    @ApiOperation(value = "课节列表（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOneCoursesDesc")
    @ResponseBody
    public String getOneCoursesDesc(@ApiParam(name = "id", required = true, value = "课程id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.getOneCoursesDesc(new ObjectId(id));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取课程简介失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 购买课（学生）
     */
    @ApiOperation(value = "购买课节（学生", httpMethod = "GET", produces = "application/json")
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
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 简洁查询余额（孩子）
     */
    @ApiOperation(value = "简洁查询余额", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAccount")
    @ResponseBody
    public String getAccount(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            int result = excellentCoursesService.getAccount(getUserId());
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
            int result = excellentCoursesService.getMyAccount(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 我的收藏（家长、孩子统一）
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
     *  课程中心(家长、孩子统一)
     */
    @ApiOperation(value = "课程中心", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/myClassList")
    @ResponseBody
    public String myClassList(@ApiParam(name = "subjectId", required = false, value = "subjectId") @RequestParam(value="subjectId",defaultValue = "") String subjectId,
                               @ApiParam(name = "priceType", required = false, value = "priceType") @RequestParam(value="priceType",defaultValue = "0") int priceType,//1 正序  -1 倒叙
                                  @ApiParam(name = "persionType", required = false, value = "persionType") @RequestParam(value="persionType",defaultValue = "0") int persionType,
                                  @ApiParam(name = "timeType", required = false, value = "timeType") @RequestParam(value="timeType",defaultValue = "0") int timeType,
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
     *  搜索课程中心（家长、孩子统一）
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
     *  收藏（家长、孩子统一）
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
                respObj.setMessage("取消收藏成功！");
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


    /**********************************充值相关********************************/

    /**
     *  充值
     */
    @ApiOperation(value = "充值", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/freeChong")
    @ResponseBody
    public String freeChong(@ApiParam(name = "number", required = false, value = "number") @RequestParam(value="number",defaultValue = "") int  number,
                            @ApiParam(name = "userName", required = false, value = "userName") @RequestParam(value="userName",defaultValue = "") String  userName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String  dto = excellentCoursesService.freeChong(getUserId(), number, userName);
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  消费记录 （孩子）
     */
    @ApiOperation(value = "消费记录（孩子）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/accountList")
    @ResponseBody
    public String accountList(@ApiParam(name = "page", required = false, value = "page") @RequestParam(value="page",defaultValue = "") int  page,
                            @ApiParam(name = "pageSize", required = false, value = "pageSize") @RequestParam(value="pageSize",defaultValue = "") int  pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object>  dto = excellentCoursesService.accountList(page, pageSize, getUserId());
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }


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
     * 购买课节(家长)
     */
    @ApiOperation(value = "购买课节（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/buyChildClassList")
    @ResponseBody
    public String buyChildClassList(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                    @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId,
                               @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = excellentCoursesService.buyChildClassList(new ObjectId(id),getUserId(),classIds,new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  退课查询
     */
    @ApiOperation(value = "简介（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteOrderSelectClass")
    @ResponseBody
    public String deleteOrderSelectClass(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                     @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.deleteOrderSelectClass(new ObjectId(id), getUserId(), new ObjectId(sonId));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  确定退款
     */
    @ApiOperation(value = "确定退款", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/refund")
    @ResponseBody
    public String refund(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                         @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId,
                                         @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds){
        // 为 以后兼容 部分退课，要求传递退课idList，目前暂不使用
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = excellentCoursesService.deleteOrderSelectClass(new ObjectId(id), getUserId(), new ObjectId(sonId));
            respObj.setMessage(map);
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
     *  提现页面
     */
    @ApiOperation(value = "提现页面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/goMyMoney")
    @ResponseBody
    public String goMyMoney(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
           //excellentCoursesService.updateAccount(accountName,getUserId());
            respObj.setMessage("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  授权孩子界面
     */
    @ApiOperation(value = "授权孩子界面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyRoleToSon")
    @ResponseBody
    public RespObj getMyRoleToSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<Map<String,Object>> mapList = excellentCoursesService.getMyRoleToSon(getUserId());
            respObj.setMessage(mapList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询失败！");
        }
        return respObj;
    }

    /**
     *  修改授权
     */
    @ApiOperation(value = "修改授权", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateMyRoleToSon")
    @ResponseBody
    public RespObj updateMyRoleToSon(@ApiParam(value="孩子id,孩子id",required = true,name="sonIds")@RequestParam(value="sonIds",required = true)String sonIds){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            excellentCoursesService.updateMyRoleToSon(getUserId(),sonIds);
            respObj.setMessage("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("授权失败！");
        }
        return respObj;
    }

}
