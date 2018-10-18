package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.service.BackStageDeliveryManageService;
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
 * Created by taotao.chan on 2018年9月25日10:42:11
 */
@Api(value = "后台管理类-发货管理")
@Controller
@RequestMapping("/web/backstagedelivery")
public class BackStageDeliveryManageController extends BaseController {
    @Autowired
    private BackStageDeliveryManageService backStageDeliveryManageService;

    /**
     * 发货管理-发货状态下拉列表
     * @return
     */
    @ApiOperation(value = "发货管理-发货状态下拉列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getDeliveryOptionList")
    @ResponseBody
    public RespObj getProjectList() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            JSONArray jsonArray = backStageDeliveryManageService.getDeliveryOptionList();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(jsonArray);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 发货管理-列表数据
     * @return
     */
    @ApiOperation(value = "发货管理-列表数据", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getDeliveryInfoList")
    @ResponseBody
    public RespObj getDeliveryInfoList(
            @ApiParam(name = "page", required = true, value = "page") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ApiParam(name = "inputParams", required = false, value = "inputParams") @RequestParam(value = "inputParams", defaultValue = "") String inputParams,
            @ApiParam(name = "year", required = false, value = "year") @RequestParam(value = "year", defaultValue = "") String year,
            @ApiParam(name = "month", required = false, value = "month") @RequestParam(value = "month", defaultValue = "") String month,
            @ApiParam(name = "deliveryFlag", required = false, value = "deliveryFlag") @RequestParam(value = "deliveryFlag", defaultValue = "") String deliveryFlag
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageDeliveryManageService.getDeliveryInfoList(page, pageSize, inputParams, year, month,deliveryFlag);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 根据唯一索引更新物流信息
     * @param map
     * @return
     */
    @ApiOperation(value = "根据唯一索引更新", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateDeliveryLogisticsInfoById")
    @ResponseBody
    public RespObj updateDeliveryLogisticsInfoById(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageDeliveryManageService.updateDeliveryLogisticsInfoById(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 根据唯一索引集合更新物流信息
     * @param map
     * @return
     */
    @ApiOperation(value = "根据唯一索引集合更新", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateDeliveryLogisticsInfoByIds")
    @ResponseBody
    public RespObj updateDeliveryLogisticsInfoByIds(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageDeliveryManageService.updateDeliveryLogisticsInfoByIds(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 发货管理-获取当前型号库存手机颜色
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
            List<String> result = backStageDeliveryManageService.getCurrentModelColor(phoneModel);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 发货管理-配置手机检测可用
     * @return
     */
    @ApiOperation(value = "发货管理-配置手机检测可用", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/checkPhoneInfoValid")
    @ResponseBody
    public RespObj checkPhoneInfoValid(@ApiParam(name = "imeiNo", required = false, value = "imeiNo") @RequestParam(value = "imeiNo", defaultValue = "") String imeiNo,
                                       @ApiParam(name = "color", required = false, value = "color") @RequestParam(value = "color", defaultValue = "") String color
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            int result = backStageDeliveryManageService.checkPhoneInfoValid(imeiNo,color);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 发货管理-单个客户发货
     * parameter
     * imeiNo color excompanyNo expressNo inOutStorageId
     * @return
     */
    @ApiOperation(value = "发货管理-单个客户发货", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateSingleCustomerDelivery")
    @ResponseBody
    public RespObj updateSingleCustomerDelivery(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageDeliveryManageService.updateSingleCustomerDelivery(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
