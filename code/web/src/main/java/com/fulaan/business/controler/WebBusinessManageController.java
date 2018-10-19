package com.fulaan.business.controler;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.business.service.BusinessManageService;
import com.fulaan.controlphone.dto.CoursesBusinessDTO;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018/1/15.
 */
@Api(value="运营管理")
@Controller
@RequestMapping("/web/business")
public class WebBusinessManageController extends BaseController {
    @Autowired
    private BusinessManageService businessManageService;

    /**********************用户管理****************************/
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

    /**********************运营数据管理****************************/

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
            String result = businessManageService.addRoleUser(getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加管理员用户失败!");
        }
        return JSON.toJSONString(respObj);
    }
    
    /**
     * 删除管理员用户
     * @return
     */
    @ApiOperation(value = "删除管理员用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/delRoleUser")
    @ResponseBody
    public String delRoleUser(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String result = businessManageService.delRoleUser(getUserId(), new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除管理员用户失败!");
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
     * 获得用户所有的学校
     * @return
     */
    @ApiOperation(value = "获得用户所有的学校", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getMySchoolList")
    @ResponseBody
    public String getMySchoolList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            //todo
            List<HomeSchoolDTO> list = businessManageService.getMySchoolList(getUserId());
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
     * 查询所有的订单课程
     * @return
     */
    @ApiOperation(value = "查询所有的订单课程", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectAllOrderCourses")
    @ResponseBody
    public String selectAllOrderCourses(@ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                   @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                                   @ApiParam(name="name",required = false,value="name") @RequestParam(value="name",defaultValue = "") String name,
                                   @ApiParam(name="subjectId",required = false,value="subjectId") @RequestParam(value="subjectId",defaultValue = "") String subjectId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectAllOrderCourses(page,pageSize,name,subjectId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询所有的订单课程失败!");
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
     * 简洁详情
     * @return
     */
    @ApiOperation(value = "简洁详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectCoursesSimpleDetails")
    @ResponseBody
    public String selectCoursesSimpleDetails(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectCoursesSimpleDetails(new ObjectId(id));
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
     * 分页获取用户订单
     * @return
     */
    @ApiOperation(value = "分页获取用户订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectOrderPageList")
    @ResponseBody
    public String selectOrderPageList(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                      @ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                      @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectOrderPageList(new ObjectId(id), page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("分页获取用户订单失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 批量导出订单数据
     */
    @ApiOperation(value = "批量导出订单数据", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(
                         @ApiParam(name="id",required = false,value="id") @RequestParam(value="id") String id,
                            HttpServletResponse response,
                               HttpServletRequest request) {
        try {
            businessManageService.exportTemplate(request, response,new ObjectId(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  用户订单详情
     * @return
     */
    @ApiOperation(value = "用户订单详情", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectCoursesOrderDetails")
    @ResponseBody
    public String selectCoursesOrderDetails(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                            @ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId",defaultValue = "") String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectCoursesOrderDetails(new ObjectId(id), new ObjectId(userId));
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
     *  删除订单管理
     * @return
     */
    @ApiOperation(value = "删除订单管理", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectOrderDeleteList")
    @ResponseBody
    public String selectOrderDeleteList(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                        @ApiParam(name="status",required = false,value="status") @RequestParam(value="status",defaultValue = "1") int status,//1 删除 2 退出
                                        @ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                        @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectOrderDeleteList(new ObjectId(id),status, page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询删除订单管理失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 批量导出删除订单数据
     */
    @ApiOperation(value = "批量导出删除订单数据", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportTemplate2")
    @ResponseBody
    public void exportTemplate2(
            @ApiParam(name="id",required = false,value="id") @RequestParam(value="id") String id,
            @ApiParam(name="status",required = false,value="status") @RequestParam(value="status") int status,
            HttpServletResponse response,
            HttpServletRequest request) {
        try {
            businessManageService.exportTemplate2(request, response, new ObjectId(id),status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  退课查询
     * @return
     */
    @ApiOperation(value = "退课查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectCoursesTuiOrder")
    @ResponseBody
    public String selectCoursesTuiOrder(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                            @ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId",defaultValue = "") String userId,
                                            @ApiParam(name="ids",required = false,value="ids") @RequestParam(value="ids",defaultValue = "") String ids){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map =  businessManageService.selectCoursesTuiOrder(new ObjectId(id), new ObjectId(userId),ids);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("退课查询失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  退课
     * @return
     */
    @ApiOperation(value = "退课", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/tuiOrder")
    @ResponseBody
    public String tuiOrder(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                        @ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId",defaultValue = "") String userId,
                                        @ApiParam(name="ids",required = false,value="ids") @RequestParam(value="ids",defaultValue = "") String ids){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.tuiOrder(new ObjectId(id), new ObjectId(userId),ids,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("退课成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("超级详情失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  导入订单
     * @return
     */
    @ApiOperation(value = "导入订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/daoOrder")
    @ResponseBody
    public String daoOrder(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                           @ApiParam(name="roomId",required = false,value="roomId") @RequestParam(value="roomId",defaultValue = "") String roomId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String str = businessManageService.daoOrder(new ObjectId(id), roomId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(str);
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
     * 审批通过
     * @return
     */
    @ApiOperation(value = "审批通过", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/newFinish")
    @ResponseBody
    public String newFinish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                            @ApiParam(name="number",required = false,value="number") @RequestParam(value="number",defaultValue = "") String number){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String map =  businessManageService.newFinish(new ObjectId(id), number, getUserId());
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
     * 审批通过
     * @return
     */
    @ApiOperation(value = "审批通过", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/threeFinish")
    @ResponseBody
    public String threeFinish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                            @ApiParam(name="number",required = false,value="number") @RequestParam(value="number",defaultValue = "") String number){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String map =  businessManageService.newFinish(new ObjectId(id), number, getUserId());
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
     * 审批通过(附加信息)
     * @return
     */
    @ApiOperation(value = "附加信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/backFinish")
    @ResponseBody
    public String backFinish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                            @ApiParam(name="number",required = false,value="number") @RequestParam(value="number",defaultValue = "") String number,
                            @ApiParam(name="type",required = false,value="type") @RequestParam(value="type",defaultValue = "0") int type,
                            @ApiParam(name="classNumber",required = false,value="classNumber") @RequestParam(value="classNumber") String classNumber,
                            @ApiParam(name="sellName",required = false,value="sellName") @RequestParam(value="sellName") String sellName,
                            @ApiParam(name="province",required = false,value="province") @RequestParam(value="province") String province,
                            @ApiParam(name="city",required = false,value="city") @RequestParam(value="city") String city){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String map = businessManageService.threeFinish(new ObjectId(id), number, getUserId(),type);
            CoursesBusinessDTO dto = new CoursesBusinessDTO();
            dto.setClassNumber(classNumber);
            dto.setSellName(sellName);
            dto.setProvince(province);
            dto.setCity(city);
            dto.setContactId(id);
            dto.setType(Constant.ZERO);
            businessManageService.addCoursesBusinessEntry(dto);
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
     * 增加附加信息
     * @return
     */
    @ApiOperation(value = "增加附加信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addCommunityToFinish")
    @ResponseBody
    public String addCommunityToFinish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                             @ApiParam(name="communityId",required = false,value="communityId") @RequestParam(value="communityId",defaultValue = "") String communityId,
                             @ApiParam(name="emid",required = false,value="emid") @RequestParam(value="emid",defaultValue = "") String emid){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.addCommunityToFinish(new ObjectId(id),new ObjectId(communityId),emid);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("增加附加信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("增加附加信息失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 拒绝通过(附加信息)
     * @return
     */
    @ApiOperation(value = "附加信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/refuseFinish")
    @ResponseBody
    public String refuseFinish(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.refuseFinish(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("已拒绝");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("拒绝失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 直播课堂运营数据查询
     * @return
     */
    @ApiOperation(value = "直播课堂运营数据查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectCourseBusinessList")
    @ResponseBody
    public String selectCourseBusinessList(@ApiParam(name="businessName",required = false,value="id") @RequestParam(value="businessName",defaultValue = "") String businessName,
                                           @ApiParam(name="province",required = false,value="province") @RequestParam(value="province",defaultValue = "") String province,
                                           @ApiParam(name="city",required = false,value="city") @RequestParam(value="city",defaultValue = "") String city,
                                           @ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                           @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map = businessManageService.selectCourseBusinessList(businessName,province,city,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("直播课堂运营数据查询失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修改直播课堂助教
     * @return
     */
    @ApiOperation(value = "修改直播课堂助教", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateAss")
    @ResponseBody
    public String updateAss(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                           @ApiParam(name="name",required = false,value="name") @RequestParam(value="name",defaultValue = "") String name){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.updateAss(new ObjectId(id),name,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改失败!");
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

    /**
     * 强制删除订单
     * @return
     */
    @ApiOperation(value = "强制删除订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/deleteCoursesOrder")
    @ResponseBody
    public String deleteCoursesOrder(@ApiParam(name="id",required = false,value="id") @RequestParam(value="id",defaultValue = "") String id,
                                     @ApiParam(name="userId",required = false,value="userId") @RequestParam(value="userId",defaultValue = "") String userId,
                                     @ApiParam(name="jiaId",required = false,value="jiaId") @RequestParam(value="jiaId",defaultValue = "") String jiaId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {//double price,String orderId,int orderType,int orderType
            businessManageService.deleteCoursesOrder(new ObjectId(id), new ObjectId(userId),jiaId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");

        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("强制删除失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 版本获取
     */
    @SessionNeedless
    @ApiOperation(value = "版本获取", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getVersion")
    @ResponseBody
    public RespObj getVersion(@ApiParam(name="moduleName",required = false,value="moduleName") @RequestParam(value="moduleName",defaultValue = "") String moduleName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            int type = businessManageService.getVersion(moduleName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(type);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("版本获取失败!");
        }
        return respObj;
    }

    /**
     * 添加版本
     */
    @ApiOperation(value = "添加版本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addVersion")
    @ResponseBody
    public RespObj addVersion(@ApiParam(name="moduleName",required = false,value="moduleName") @RequestParam(value="moduleName",defaultValue = "") String moduleName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.addVersion(moduleName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加版本失败!");
        }
        return respObj;
    }


    /**
     * 修改版本
     */
    @ApiOperation(value = "修改版本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateVersion")
    @ResponseBody
    public RespObj updateVersion(@ApiParam(name="moduleName",required = false,value="moduleName") @RequestParam(value="moduleName",defaultValue = "") String moduleName,
                                @ApiParam(name="moduleType",required = false,value="moduleType") @RequestParam(value="moduleType",defaultValue = "") int moduleType){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.updateVersion(moduleName, moduleType);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改版本");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改版本失败!");
        }
        return respObj;
    }

    /**
     * 是否同意
     */
    @ApiOperation(value = "是否同意", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/booleanUserAgreement")
    @ResponseBody
    public RespObj booleanUserAgreement(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            boolean flag = businessManageService.booleanUserAgreement(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(flag);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取是否同意失败!");
        }
        return respObj;
    }

    /**
     * 同意用户协议
     */
    @ApiOperation(value = "同意用户协议", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/agreeUserAgreement")
    @ResponseBody
    public RespObj agreeUserAgreement(@ApiParam(name="type",required = false,value="type") @RequestParam(value="type",defaultValue = "") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.agreeUserAgreement(getUserId(), type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("同意成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("同意失败!");
        }
        return respObj;
    }


    /**
     * 查询每日订单
     */
    @ApiOperation(value = "查询每日订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getDayOrderList")
    @ResponseBody
    public RespObj getDayOrderList(@ApiParam(name="type",required = false,value="type") @RequestParam(value="type",defaultValue = "") int type,
                                   @ApiParam(name="startTime",required = false,value="startTime") @RequestParam(value="startTime",defaultValue = "") String startTime,
                                   @ApiParam(name="endTime",required = false,value="endTime") @RequestParam(value="endTime",defaultValue = "") String endTime,
                                   @ApiParam(name="schoolId",required = false,value="schoolId") @RequestParam(value="schoolId",defaultValue = "") String schoolId,
                                   @ApiParam(name="coursesId",required = false,value="coursesId") @RequestParam(value="coursesId",defaultValue = "") String coursesId,
                                   @ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "") int page,
                                   @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            businessManageService.getDayOrderList(getUserId(), type, startTime, endTime, schoolId, coursesId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("同意成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("同意失败!");
        }
        return respObj;
    }
}
