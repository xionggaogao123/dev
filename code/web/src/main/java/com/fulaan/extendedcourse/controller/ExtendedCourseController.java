package com.fulaan.extendedcourse.controller;

import com.fulaan.base.BaseController;
import com.fulaan.extendedcourse.dto.ExtendedCourseDTO;
import com.fulaan.extendedcourse.service.ExtendedCourseService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by James on 2018-12-06.
 */
@Api(value="拓展课")
@Controller
@RequestMapping("/jxmapi/extendedcourse")
public class ExtendedCourseController extends BaseController {

    @Autowired
    private ExtendedCourseService extendedCourseService;

    private static final Logger logger = Logger.getLogger(ExtendedCourseController.class);

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
            Map<String,Object> result = extendedCourseService.selectExtendedCourseList(new ObjectId(communityId),getUserId(),keyword,status,page,pageSize);
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
     * 查询所有课程（学生）
     */
    @ApiOperation(value = "查询课程列表（学生）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectExtendedCourseListFromSon")
    @ResponseBody
    public RespObj selectExtendedCourseListFromSon(@RequestParam(value="communityId") String communityId,
                                            @RequestParam(value="keyword") String keyword,
                                            @RequestParam(value="status",defaultValue = "0") int status,//0  全部   1  报名中   2  学习中   3 已学完
                                            @RequestParam(value="page") int page,
                                            @RequestParam(value="pageSize") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectExtendedCourseListFromSon(new ObjectId(communityId), getUserId(), keyword, status, page, pageSize);
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
     * 查询报名名单（老师）
     */
    @ApiOperation(value = "查询报名名单（老师）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectStudentList")
    @ResponseBody
    public RespObj selectStudentList(@RequestParam(value="communityId") String communityId,
                                     @RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectStudentList(new ObjectId(communityId),getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询课程详情（家长）
     */
    @ApiOperation(value = "查询课程详情（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectCourseDesc")
    @ResponseBody
    public RespObj selectCourseDesc(@RequestParam(value="id") String id,
                                    @RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = extendedCourseService.selectCourseDesc(getUserId(), new ObjectId(id),new ObjectId(communityId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 查询课程详情（孩子）
     */
    @ApiOperation(value = "查询课程详情（孩子）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectCourseDescFromSon")
    @ResponseBody
    public RespObj selectCourseDescFromSon(@RequestParam(value="id") String id,
                                           @RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            ExtendedCourseDTO result = extendedCourseService.selectCourseDescFromSon(getUserId(), new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 报名、抢课(家长)
     */
    @ApiOperation(value = "报名、抢课(家长)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/applyCourse")
    @ResponseBody
    public RespObj applyCourse(@RequestParam(value="id") String id,
                                @RequestParam(value="sonIds") String sonIds,
                                @RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.applyCourse(new ObjectId(communityId),getUserId(), new ObjectId(id),sonIds);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 报名、抢课(孩子)
     */
    @ApiOperation(value = "报名、抢课(孩子)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/applyCourseForSon")
    @ResponseBody
    public RespObj applyCourseForSon(@RequestParam(value="id") String id,
                               @RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.applyCourseForSon(new ObjectId(communityId), getUserId(), new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    /**
     * 取消报名（家长）
     */
    @ApiOperation(value = "取消报名（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteMyApply")
    @ResponseBody
    public RespObj deleteMyApply(@RequestParam(value="communityId") String communityId,
                               @RequestParam(value="sonId") String sonId,
                                     @RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteMyApply(new ObjectId(id), new ObjectId(communityId),getUserId(), new ObjectId(sonId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("取消成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 取消报名（孩子）
     */
    @ApiOperation(value = "取消报名（孩子）", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteMyApplyForSon")
    @ResponseBody
    public RespObj deleteMyApplyForSon(@RequestParam(value="communityId") String communityId,
                                 @RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteMyApplyForSon(new ObjectId(id), new ObjectId(communityId), getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("取消成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "添加新的孩子（限定）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/registerUser")
    @ResponseBody
    public RespObj registerUser(@RequestParam(value="userName") String userName,
                                @RequestParam(value="communityId") String communityId,
                                HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            String phoneNumber = "12345678900";
            //限定注册用户
            String userId=extendedCourseService.registerAvailableUser(request, userName, phoneNumber, Constant.TWO, userName, getUserId(),new ObjectId(communityId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(userId);
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



}
