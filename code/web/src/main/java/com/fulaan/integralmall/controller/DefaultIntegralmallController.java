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
import com.fulaan.integralmall.dto.AddressDto;
import com.fulaan.integralmall.dto.GoodsDto;
import com.fulaan.integralmall.dto.IntegralmallHomeDto;
import com.fulaan.integralmall.dto.OrderDto;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.integralmall.service.IntegralmallService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value="/jxmapi/integralmall")
@Api(value = "积分商城的所有接口")
public class DefaultIntegralmallController extends BaseController{

    @Autowired
    private IntegralSufferService integralSufferService;
    
    @Autowired
    private IntegralmallService integralmallService;
    
    @ApiOperation(value = "积分商城首页", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "积分商城首页",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getIntegralmallHomePage")
    @ResponseBody
    public RespObj getIntegralmallHomePage(@RequestParam(required = false, defaultValue = "1")int page,
                                           @RequestParam(required = false, defaultValue = "10")int pageSize){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            IntegralmallHomeDto dto = integralmallService.getIntegralmallHome(page, pageSize, getUserId());
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list",dto);
            retMap.put("count", integralmallService.getIntegralmallHomeNum());
            retMap.put("page",page);
            retMap.put("pageSize",pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "商品详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "商品详情",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/goodDetail")
    @ResponseBody
    public RespObj goodDetail(@ObjectIdType ObjectId goodId){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            GoodsDto goodsDto = integralmallService.getGoodById(goodId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(goodsDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "兑换记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "兑换记录",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/exchangeRecord")
    @ResponseBody
    public RespObj exchangeRecord(@RequestParam(required = false, defaultValue = "1")int page,
                                           @RequestParam(required = false, defaultValue = "10")int pageSize){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<GoodsDto> goodsL = integralmallService.getExchangeRecord(page, pageSize, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(goodsL);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "兑换记录带总数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "兑换记录带总数",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/exchangeRecordTotal")
    @ResponseBody
    public RespObj exchangeRecordTotal(@RequestParam(required = false, defaultValue = "1")int page,
                                           @RequestParam(required = false, defaultValue = "10")int pageSize){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<GoodsDto> goodsL = integralmallService.getExchangeRecord(page, pageSize, getUserId());
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", goodsL);
            retMap.put("count", integralmallService.getExchangeRecordTotal(getUserId()));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "申述提交", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "申述提交",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/stateSave")
    @ResponseBody
    public RespObj stateSave(@ObjectIdType ObjectId orderId,String stateReason){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.stateSave(orderId, stateReason);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("申述提交成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    } 
    
    @ApiOperation(value = "确认订单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "确认订单",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/orderConfirm")
    @ResponseBody
    public RespObj orderConfirm(@ObjectIdType ObjectId goodId){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            OrderDto orderDto = integralmallService.orderConfirm(goodId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(orderDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    } 
    
    @ApiOperation(value = "编辑收货地址", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "编辑收货地址",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/editAddress")
    @ResponseBody
    public RespObj editAddress(){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            AddressDto addressDto = integralmallService.editAddress(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(addressDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    } 
    
    @ApiOperation(value = "保存收货地址", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存收货地址",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveAddress")
    @ResponseBody
    public RespObj saveAddress(@RequestBody AddressDto addressDto){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.saveAddress(addressDto, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    } 
    
    @ApiOperation(value = "提交订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "提交订单",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveOrder")
    @ResponseBody
    public RespObj saveOrder(Integer goodNum, @ObjectIdType ObjectId goodId, @ObjectIdType ObjectId addressId,Integer costScore){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            integralmallService.saveOrder(goodNum, goodId, addressId,getUserId(),costScore);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(costScore);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    } 
    
    @ApiOperation(value = "物流信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "物流信息",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/wuLiuInfo")
    @ResponseBody
    public RespObj wuLiuInfo(@ObjectIdType ObjectId orderId){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            WuliuInfoDto wuliuInfoDto = integralmallService.wuLiuInfo(orderId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(wuliuInfoDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    
    
}
