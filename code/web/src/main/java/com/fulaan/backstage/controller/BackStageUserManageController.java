package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.backstage.service.BackStageUserManageService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年8月30日09:32:46
 */
@Api(value = "后台用户管理类")
@Controller
@RequestMapping("/web/backstageusermanage")
public class BackStageUserManageController extends BaseController {
    @Autowired
    private BackStageUserManageService backStageUserManageService;

    /**
     * 后台用户管理角色筛选
     * @return
     */
    @ApiOperation(value = "后台用户管理角色筛选", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserRoleOption")
    @ResponseBody
    public RespObj getUserRoleOption() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            JSONArray jsonArray = backStageUserManageService.getUserRoleOption();
            respObj.setMessage(jsonArray);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("用户管理角色筛选查询错误!");
        }
        return respObj;
    }

    /**
     * 后台用户管理查询
     * @return
     */
    @ApiOperation(value = "后台用户管理查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserListByRole")
    @ResponseBody
    public RespObj getUserListByRole(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<UserManageResultDTO> userManageResultDTOS = backStageUserManageService.getUserListByRole(map);
            respObj.setMessage(userManageResultDTOS);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台用户管理查询错误!");
        }
        return respObj;
    }

}
