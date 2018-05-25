package com.fulaan.business.controler;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.business.service.BusinessManageService;
import com.fulaan.pojo.User;
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
 * Created by James on 2018/1/15.
 */
@Api(value="运营管理")
@Controller
@RequestMapping("/web/business")
public class BusinessManageController extends BaseController {
    @Autowired
    private BusinessManageService businessManageService;

    /**
     *  查询所有
     */
    @ApiOperation(value = "查询所有", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getEntryList")
    @ResponseBody
    public String getEntryList(@ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                           @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                           @ApiParam(name="jiaId",required = false,value="jiaId") @RequestParam(value="jiaId",defaultValue = "") String jiaId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos = businessManageService.getList(jiaId,page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询所有失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  处理老用户脚本
     */
    @ApiOperation(value = "处理老用户脚本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addOldEntry")
    @ResponseBody
    public String addOldEntry(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = System.currentTimeMillis();
            businessManageService.createBusinessManage();
            long time2 = System.currentTimeMillis();
            long during = time2-time;
            respObj.setMessage("处理花费时间为"+during+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("处理老用户脚本失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  更新脚本
     */
    @ApiOperation(value = "处理老用户脚本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateOldEntry")
    @ResponseBody
    public String updateOldEntry(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = System.currentTimeMillis();
            businessManageService.checkBusinessManage2();
            long time2 = System.currentTimeMillis();
            long during = time2-time;
            respObj.setMessage("更新时间为"+during+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("更新脚本脚本失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 用户名和昵称查询
     * @return
     */
    @ApiOperation(value = "用户名和昵称查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectUser")
    @ResponseBody
    public String selectUser(@ApiParam(name="userName",required = false,value="userName") @RequestParam(value="userName",defaultValue = "1") String userName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<User> userList =  businessManageService.selectUser(userName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(userList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("用户名和昵称查询失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询所有管理员用户
     * @return
     */
    @ApiOperation(value = "查询所有管理员用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectRoleUser")
    @ResponseBody
    public String selectRoleUser(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<Map<String,Object>> userList =  businessManageService.selectRoleUser();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(userList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询所有管理员用户失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加管理员用户
     * @return
     */
    @ApiOperation(value = "添加管理员用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addRoleUser")
    @ResponseBody
    public String addRoleUser(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.addRoleUser(getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加管理员用户失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 权限列表
     * @return
     */
    @ApiOperation(value = "权限列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getRoleList")
    @ResponseBody
    public String getRoleList(@ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId") String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
           List<Map<String,Object>> list = businessManageService.getRoleList(new ObjectId(userId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("权限列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修改权限
     * @return
     */
    @ApiOperation(value = "修改权限", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateRole")
    @ResponseBody
    public String updateRole(@ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId") String userId,
                             @ApiParam(name="number",required = false,value="number") @RequestParam(value="number") String number){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.updateRole(getUserId(), new ObjectId(userId), number);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改权限失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询所有申请的课程
     * @return
     */
    @ApiOperation(value = "查询所有申请的课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectAllCourses")
    @ResponseBody
    public String selectAllCourses(@ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                                @ApiParam(name="name",required = false,value="name") @RequestParam(value="name",defaultValue = "") String name,
                                @ApiParam(name="subjectId",required = false,value="subjectId") @RequestParam(value="subjectId",defaultValue = "") String subjectId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectAllCourses(page,pageSize,name,subjectId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询所有申请的课程失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 超级详情
     * @return
     */
    @ApiOperation(value = "超级详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectCoursesDetails")
    @ResponseBody
    public String selectCoursesDetails(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectCoursesDetails(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("超级详情失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 审批通过
     * @return
     */
    @ApiOperation(value = "审批通过", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/finish")
    @ResponseBody
    public String finish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                         @ApiParam(name="number",required = false,value="number") @RequestParam(value="number",defaultValue = "") String number){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String map =  businessManageService.finish(new ObjectId(id), number, getUserId());
            if(map.equals("1")){
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("审核通过");
            }else{
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("审批通过失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 强制删除
     * @return
     */
    @ApiOperation(value = "强制删除", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/deleteCourses")
    @ResponseBody
    public String deleteCourses(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.deleteCourses(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");

        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("强制删除失败!");
        }
        return JSON.toJSONString(respObj);
    }

}
