package com.fulaan.lancustom.controller;

import com.fulaan.base.BaseController;
import com.fulaan.lancustom.dto.MonetaryAddrDto;
import com.fulaan.lancustom.service.MonetaryAddrService;
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
 * @Auther: taotao.chan
 * @Date: 2018/8/7 11:16
 * @Description: 家校美物流地址维护
 */
@Api("小兰客服")
@Controller
@RequestMapping("/jxmapi/address")
public class DefaultAddressController extends BaseController {
    @Autowired
    private MonetaryAddrService service;

    @ApiOperation(value = "保存收货地址", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存收货地址",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveMonetaryAddr")
    @ResponseBody
    public RespObj saveAddress(@RequestBody MonetaryAddrDto monetaryAddrDto) {

        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            service.saveMonetaryAddr(monetaryAddrDto, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获取用户地址信息,目前设计就只能维护一个地址
     * @return
     */
    @ApiOperation(value = "获取收货地址", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "获取收货地址",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserAddrsList")
    @ResponseBody
    public RespObj getUserAddrsList() {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map= new HashMap<String,Object>();
            List<MonetaryAddrDto> list = service.getUserAddrsList(getUserId());
            map.put("addrList",list);
            obj.setMessage(map);
            obj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }

}
