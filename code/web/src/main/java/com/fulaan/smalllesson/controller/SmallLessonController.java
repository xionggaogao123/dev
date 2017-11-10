package com.fulaan.smalllesson.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.smalllesson.dto.LessonAnswerDTO;
import com.fulaan.smalllesson.dto.SmallLessonDTO;
import com.fulaan.smalllesson.service.SmallLessonService;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/27.
 */
@Controller
@RequestMapping(value="/smallLesson")
@Api(value = "复兰小课堂的所有接口")
public class SmallLessonController extends BaseController {
    @Autowired
    private SmallLessonService smallLessonService;

    /**
     * 扫描二维码进入课程
     */


    /**
     * 输入二维码进入课程
     *
     */

    /**
     * 添加课程（点击开课）
     */
    @SessionNeedless
    @ApiOperation(value = " 添加课程（点击开课）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addLessonEntry")
    @ResponseBody
    public String addLessonEntry(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId,
                                 @ApiParam(name = "userName", required = true, value = "用户姓名") @RequestParam("userName") String userName){

        RespObj respObj=null;
        try {

            SmallLessonDTO str = smallLessonService.addLessonEntry(userId,userName);
            if(str == null){
                respObj = RespObj.FAILD;
                respObj.setErrorMessage("正在上课中!");
            }else{
                respObj = RespObj.SUCCESS;
                respObj.setMessage(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage(" 添加课程（点击开课）失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 下课(修改课程)
     */
    @SessionNeedless
    @ApiOperation(value = " 添加课程（点击开课）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updLessonEntry")
    @ResponseBody
    public String updLessonEntry(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId,
                                 @ApiParam(name = "time", required = true, value = "持续时间") @RequestParam("time") int time){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.updLessonEntry(userId,time);
            respObj.setMessage("下课(修改课程)成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("下课(修改课程)失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 加入课程 （学生扫描进入）
     */
    @SessionNeedless
    @ApiOperation(value = "加入课程 （学生扫描进入）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addStuEntry/{teacherId}")
    @ResponseBody
    public String addStuEntry(@ApiParam(name = "teacherId", required = true, value = "老师id") @PathVariable(value = "teacherId") String teacherId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            ObjectId userId = getUserId();
            Map<String,Object> str = smallLessonService.addStuEntry(userId, getSessionValue().getUserName(), new ObjectId(teacherId));
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("加入课程 （学生扫描进入）失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 加入课程 （学生输入码进入）
     */
    @ApiOperation(value = "加入课程 （学生扫描进入）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addStuEntryByCode")
    @ResponseBody
    public String addStuEntryByCode(@ApiParam(name = "code", required = true, value = "课程id") @RequestParam(value = "code") String code){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            ObjectId userId = getUserId();
            Map<String,Object> str = smallLessonService.addStuEntryByCode(userId, getSessionValue().getUserName(), code);
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("加入课程 （学生扫描进入）失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前用户的课程列表（倒序）
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "查找当前用户的课程列表（倒序）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectLessonList")
    @ResponseBody
    public String selectLessonList(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId,
                                   @ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                   @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = smallLessonService.getLessonList(new ObjectId(userId), page, pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前用户的课程列表（倒序）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前课程的活跃列表（倒序）
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "查找当前课程的活跃列表（倒序）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectUserResultList")
    @ResponseBody
    public String selectUserResultList(@ApiParam(name = "lessonId", required = true, value = "课程id") @RequestParam("userId") String lessonId,
                                   @ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                   @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = smallLessonService.getUserResultList(new ObjectId(lessonId), page, pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前课程的活跃列表（倒序）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 答题情况列表（倒序）
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "答题情况列表（倒序）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectAnswerList")
    @ResponseBody
    public String selectAnswerList(@ApiParam(name = "lessonId", required = true, value = "课程id") @RequestParam("userId") String lessonId,
                                       @ApiParam(name = "number",required = true,value = "答题次数") @RequestParam("number") int number,
                                       @ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                       @ApiParam(name = "type", required = true, value = "type") @RequestParam("type") int type,
                                       @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = smallLessonService.selectAnswerList(new ObjectId(lessonId), number, page, pageSize,type);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("答题情况列表（倒序）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加活跃用户idlist
     */
    @SessionNeedless
    @ApiOperation(value = "添加活跃用户idlist", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addUserResult")
    @ResponseBody
    public String addUserResult(@ApiParam(name = "userIds", required = true, value = "用户id") @RequestParam("userIds") List<String> userIds,
                                @ApiParam(name = "lessonId", required = true, value = "课程记录id") @RequestParam("lessonId") String lessonId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.addUserResult(userIds,new ObjectId(lessonId));
            respObj.setMessage("添加活跃用户idlist成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加活跃用户idlist失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 批量添加答题实体类列表
     */
    @SessionNeedless
    @ApiOperation(value = "批量添加答题实体类列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addAnswerList")
    @ResponseBody
    public String addAnswerList(@ApiParam(name = "answerList", required = true, value = "答题列表") @RequestParam("answerList") List<LessonAnswerDTO> answerList){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.addAnswerList(answerList);
            respObj.setMessage("批量添加答题实体类列表成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("批量添加答题实体类列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 批量添加答题实体类列表-新
     */
    @SessionNeedless
    @ApiOperation(value = "批量添加答题实体类列表-新", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addNewAnswerList")
    @ResponseBody
    public String addNewAnswerList(@ApiParam(name = "answerList", required = true, value = "答题列表") @RequestBody LessonAnswerDTO answerList){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<LessonAnswerDTO> dto = answerList.getList();
            if(dto.size()>0){
                smallLessonService.addAnswerList(dto);
            }
            respObj.setMessage("批量添加答题实体类列表成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("批量添加答题实体类列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 修改课程名
     */
    @SessionNeedless
    @ApiOperation(value = "修改课程名", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateLessonName")
    @ResponseBody
    public String updateLessonName(@ApiParam(name = "lessonId", required = true, value = "课程id") @RequestParam("userId") String lessonId,
                                   @ApiParam(name = "name",required = true,value = "课程名") @RequestParam("name") String name){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
           smallLessonService.updateLessonName(new ObjectId(lessonId), name);
            respObj.setMessage("修改课程名成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改课程名失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 删除课程
     */
    @SessionNeedless
    @ApiOperation(value = "删除课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delLessonEntry")
    @ResponseBody
    public String delLessonEntry(@ApiParam(name = "lessonId", required = true, value = "课程id") @RequestParam("lessonId") String lessonId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.delLessonEntry(new ObjectId(lessonId));
            respObj.setMessage("删除课程成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除课程失败");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 用户的登陆基本信息展示
     *
     */
    @SessionNeedless
    @ApiOperation(value = "用户的登陆基本信息展示", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public String getUserInfo(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> map = smallLessonService.getUserInfo(new ObjectId(userId));
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除课程失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 一分钟调用接口
     *
     */
    @SessionNeedless
    @ApiOperation(value = "一分钟调用接口", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getTimeLoading")
    @ResponseBody
    public String getTimeLoading(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.getTimeLoading(new ObjectId(userId));
            respObj.setMessage("成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除课程失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 一分钟调用接口
     *
     */
    @SessionNeedless
    @ApiOperation(value = "一分钟调用接口", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateUserAvatar")
    @ResponseBody
    public String updateUserAvatar(@ApiParam(name = "userId", required = true, value = "用户id") @RequestParam("userId") String userId){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            smallLessonService.getTimeLoading(new ObjectId(userId));
            respObj.setMessage("成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除课程失败");
        }
        return JSON.toJSONString(respObj);
    }


}
