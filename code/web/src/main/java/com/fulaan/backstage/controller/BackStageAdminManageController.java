package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.service.BackStageAdminManageService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by taotao.chan on 2018年8月22日16:37:19
 */
@Api(value = "后台管理类")
@Controller
@RequestMapping("/web/backstageadminmanage")
public class BackStageAdminManageController extends BaseController {
    @Autowired
    private BackStageAdminManageService backStageAdminManageService;


    
    /**
     * 新增 修改（有Id） 操作
     * 后台添加管理员
     * param userId Id(主键Id修改时才有) roleId(from jxm_role_jurisdiction_setting 表)
     */
    @ApiOperation(value = "后台添加管理员", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveAdminJurisdiction")
    @ResponseBody
    public String saveAdminJurisdiction(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String msg = backStageAdminManageService.saveAdminJurisdiction(map);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台添加管理员错误!");
        }
        return JSON.toJSONString(respObj);
    }


    @ApiOperation(value = "后台管理员注销", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delAdminJurisdiction")
    @ResponseBody
    public String delAdminJurisdiction(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String msg = backStageAdminManageService.delAdminJurisdiction(map);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台添加管理员错误!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 后台管理员初始化角色筛选列表
     * @return
     */
    @ApiOperation(value = "角色筛选列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getRolePropertyInfo")
    @ResponseBody
    public RespObj getRolePropertyInfo(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            JSONArray jsonArray = backStageAdminManageService.getRolePropertyInfo();
            respObj.setMessage(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("角色筛选列表查询错误!");
        }
        return respObj;
    }


    /**
     * 后台管理员列表查询
     * @param map
     * 入参有 page pageSize roleProperty（角色属性分组筛选) userInfo(userId或者userName)
     * @return
     */
    @ApiOperation(value = "后台管理员列表查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAdminJurisdiction")
    @ResponseBody
    public RespObj getAdminJurisdiction(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> msg = backStageAdminManageService.getAdminJurisdiction(map);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台管理员列表查询错误!");
        }
        return respObj;
    }

}
