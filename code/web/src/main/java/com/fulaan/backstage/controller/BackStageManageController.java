package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.appmarket.service.AppMarketService;
import com.fulaan.backstage.dto.JxmAppVersionDTO;
import com.fulaan.backstage.dto.UserLogResultDTO;
import com.fulaan.backstage.dto.UserRoleJurisdictionDto;
import com.fulaan.backstage.dto.UserRoleOfPathDTO;
import com.fulaan.backstage.service.BackStageManageService;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.base.BaseController;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.fulaan.controlphone.dto.ControlSetBackDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年8月22日16:37:19
 */
@Api(value = "后台管理类")
@Controller
@RequestMapping("/web/backstagemanage")
public class BackStageManageController extends BaseController {
    @Autowired
    private BackStageManageService backStageManageService;

    /**
     * 根据tab 页的类 查找 对应的路径或者 权限 树
     * map 传 tab 页的类 对应表里的字段class
     */

    @ApiOperation(value = "后台权限树获取", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getJurisdictionTree")
    @ResponseBody
    public RespObj getJurisdictionTree(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Object object = backStageManageService.getJurisdictionTree(map);
            respObj.setMessage(object);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置权限错误!");
        }
        return respObj;
    }

    /**
     * 新增 修改（有Id） 操作
     * 后台设置权限
     */
    @ApiOperation(value = "后台设置权限", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveJurisdiction")
    @ResponseBody
    public String saveJurisdiction(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            map.put("userId", getUserId().toString());
            String msg = backStageManageService.saveJurisdiction(map);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置权限错误!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 后台设置权限列表查询
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "后台设置权限查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getJurisdiction")
    @ResponseBody
    public RespObj getJurisdiction(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            respObj.setCode(Constant.SUCCESS_CODE);
            List<UserRoleJurisdictionDto> msg = backStageManageService.getJurisdiction(map);
//            resultMap.put("message", msg);
            respObj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置权限列表查询错误!");
        }
        return respObj;
    }

//    public static void main(String[] args) {
//        System.out.println(new ObjectId());
//    }

}
