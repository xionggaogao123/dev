package com.fulaan.lancustom.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.lancustom.dto.MonetaryOrdersDto;
import com.fulaan.lancustom.service.MonetaryOrdersService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/9 13:24
 * @Description: 订单物流信息管理
 */
@Api("小兰客服")
@RequestMapping("/web/monetaryOrders")
@Controller
public class MonetaryOrdersController {

    @Autowired
    private MonetaryOrdersService service;

    @ApiOperation(value = "订单列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "订单列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/orderList")
    @ResponseBody
    public RespObj orderList(String orderNo,
                             @RequestParam(required = false, defaultValue = "1")int page,
                             @RequestParam(required = false, defaultValue = "10")int pageSize){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<MonetaryOrdersDto> list = service.getOrderList(orderNo,page, pageSize);
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", service.getOrderListAll(orderNo));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "保存订单物流信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存订单物流信息",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveOrderLogisticsInfo")
    @ResponseBody
    public RespObj saveOrderLogisticsInfo(String orderId, String excompanyNo, String expressNo){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            service.saveOrderLogisticsInfo(orderId, excompanyNo, expressNo);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "删除订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除订单",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delOrder")
    @ResponseBody
    public RespObj delOrder(@ObjectIdType ObjectId orderId){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            service.delOrder(orderId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "更新订单状态", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除订单",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/handleState")
    @ResponseBody
    public RespObj handleState(@ObjectIdType ObjectId orderId, int isAcceptance){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            service.handleState(orderId, isAcceptance);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
