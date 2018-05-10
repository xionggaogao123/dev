package com.fulaan.integralmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.integralmall.dto.GoodsDto;
import com.fulaan.integralmall.dto.OrderDto;
import com.fulaan.integralmall.service.IntegralmallService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value="/web/integralmall")
@Api(value = "积分商城的所有接口")
public class IntegralmallController extends BaseController{

    @Autowired
    private IntegralSufferService integralSufferService;
    
    @Autowired
    private IntegralmallService integralmallService;
    
    @ApiOperation(value = "商品列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "商品列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getIntegralmallHomePage")
    @ResponseBody
    public RespObj getIntegralmallHomePage(@RequestParam(required = false, defaultValue = "1")int page,
                                           @RequestParam(required = false, defaultValue = "10")int pageSize){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<GoodsDto> list = integralmallService.getGoodsList(page, pageSize);
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", integralmallService.getIntegralmallHomeNum());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "保存商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "更新商品图片",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveGoods")
    @ResponseBody
    public RespObj saveGoods(@RequestBody GoodsDto goodsDto){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.saveGoods(goodsDto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "删除商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除商品",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delGoods")
    @ResponseBody
    public RespObj delGoods(@ObjectIdType ObjectId goodId){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.delGoods(goodId);
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
            integralmallService.delOrder(orderId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "订单列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "订单列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/orderList")
    @ResponseBody
    public RespObj orderList(@RequestParam(required = false, defaultValue = "1")int page,
                             @RequestParam(required = false, defaultValue = "10")int pageSize){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<OrderDto> list = integralmallService.getOrderList(page, pageSize);
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", integralmallService.getOrderListAll());
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
    @RequestMapping("/saveOrder")
    @ResponseBody
    public RespObj saveOrder(@RequestBody OrderDto orderDto){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.saveOrder(orderDto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
}
