package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.service.BackStageRepairManageService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年10月9日10:54:51
 */
@Api(value = "后台管理类-维修管理")
@Controller
@RequestMapping("/web/backstagerepair")
public class BackStageRepairManageController extends BaseController {
    @Autowired
    private BackStageRepairManageService backStageRepairManageService;

    /**
     * 维修管理-获取手机型号
     * @return
     */
    @ApiOperation(value = "维修管理-获取手机型号", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getPhoneModel")
    @ResponseBody
    public RespObj getPhoneModel() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<String> result = backStageRepairManageService.getPhoneModel();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 维修管理-获取当前型号库存手机颜色
     * @return
     */
    @ApiOperation(value = "发货管理-获取当前型号库存手机颜色", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCurrentModelColor")
    @ResponseBody
    public RespObj getCurrentModelColor(@ApiParam(name = "phoneModel", required = false, value = "phoneModel") @RequestParam(value = "phoneModel", defaultValue = "") String phoneModel) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<String> result = backStageRepairManageService.getCurrentModelColor(phoneModel);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 维修管理-新增
     * @param map
     * @return
     */
    @ApiOperation(value = "维修管理-新增", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addRepairManage")
    @ResponseBody
    public RespObj addRepairManage(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageRepairManageService.addRepairManage(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 维修管理-列表数据
     * @return
     */
    @ApiOperation(value = "维修管理-列表数据", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getRepairManageList")
    @ResponseBody
    public RespObj getRepairManageList(
            @ApiParam(name = "page", required = true, value = "page") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ApiParam(name = "inputParams", required = false, value = "inputParams") @RequestParam(value = "inputParams", defaultValue = "") String inputParams,
            @ApiParam(name = "year", required = false, value = "year") @RequestParam(value = "year", defaultValue = "") String year,
            @ApiParam(name = "month", required = false, value = "month") @RequestParam(value = "month", defaultValue = "") String month,
            @ApiParam(name = "isr", required = true, value = "isr") @RequestParam(value = "isr", defaultValue = "0") int isr
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageRepairManageService.getRepairManageList(page, pageSize, inputParams, year, month, isr);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 维修管理-修改
     * @param map
     * @return
     */
    @ApiOperation(value = "维修管理-修改", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateRepairManage")
    @ResponseBody
    public RespObj updateRepairManage(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageRepairManageService.updateRepairManage(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 维修管理-维修完成操作
     * @param map
     * @return
     */
    @ApiOperation(value = "维修管理-维修完成操作", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/completeRepairManage")
    @ResponseBody
    public RespObj completeRepairManage(@RequestParam Map<String, Object> map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<String> result = backStageRepairManageService.completeRepairManage(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
