package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.backstage.dto.RoleJurisdictionSettingDto;
import com.fulaan.backstage.dto.UserRoleJurisdictionDto;
import com.fulaan.backstage.service.BackStageManageService;
import com.fulaan.backstage.service.BackStageRoleManageService;
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
 * Created by taotao.chan on 2018年8月22日16:37:19
 */
@Api(value = "后台角色管理类")
@Controller
@RequestMapping("/web/backstagerolemanage")
public class BackStageRoleManageController extends BaseController {
    @Autowired
    private BackStageRoleManageService backStageRoleManageService;


    /**
     * 新增 修改 逻辑删除
     * 后台设置角色权限
     *
     */
    @ApiOperation(value = "后台设置角色权限", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveRoleJurisdiction")
    @ResponseBody
    public String saveRoleJurisdiction(@RequestBody Map map){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            map.put("userId",getUserId().toString());
            String msg = backStageRoleManageService.saveRoleJurisdiction(map);
            Map<String,Object> rMap = new HashMap<String,Object>();
            rMap.put("id",msg);
            respObj.setMessage(rMap);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置角色权限错误!");
        }
        return JSON.toJSONString(respObj);
    }


    @ApiOperation(value = "后台角色权限列表查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getRoleJurisdictionList")
    @ResponseBody
    public RespObj getRoleJurisdictionList(@RequestBody Map map){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<RoleJurisdictionSettingDto> msg = backStageRoleManageService.getRoleJurisdictionList(map);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("角色权限查询错误!");
        }
        return respObj;
    }

}
