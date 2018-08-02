package com.fulaan.lancustom.controller;

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
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.lancustom.dto.MobileReturnDto;
import com.fulaan.lancustom.service.MobileReturnService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="小兰客服")
@Controller
@RequestMapping("/jxmapi/mobileReturn")
public class DefaultMobileReturnController extends BaseController{

    @Autowired
    private MobileReturnService mobileReturnService;
    
    
    @ApiOperation(value = "保存", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存问题",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/save")
    @ResponseBody
    public RespObj saveMobileReturn(@RequestBody MobileReturnDto dto) {
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            mobileReturnService.saveMobileReturn(dto, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "退换货列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "商品列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMobileReturn")
    @ResponseBody
    public RespObj getMobileReturn(
                                          @RequestParam(required = false, defaultValue = "1")int page,
                                          @RequestParam(required = false, defaultValue = "10")int pageSize) {
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<MobileReturnDto> list = mobileReturnService.getListAll(page, pageSize, getUserId());
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", mobileReturnService.getListCount(getUserId()));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            // TODO: handle exception
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
    public RespObj wuLiuInfo(@ObjectIdType ObjectId id){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            WuliuInfoDto wuliuInfoDto = mobileReturnService.wuLiuInfo(id, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(wuliuInfoDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
}
