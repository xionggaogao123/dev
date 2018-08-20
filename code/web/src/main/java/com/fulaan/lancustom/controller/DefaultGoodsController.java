package com.fulaan.lancustom.controller;

import com.fulaan.lancustom.dto.MonetaryGoodsDto;
import com.fulaan.lancustom.service.MonetaryGoodsService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
 * @Date: 2018/8/7 10:59
 * @Description:
 */
@Api("小兰客服")
@Controller
@RequestMapping("/jxmapi/goods")
public class DefaultGoodsController {
    @Autowired
    private MonetaryGoodsService service;

    @ApiOperation(value = "商品列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "商品列表", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMonetaryGoodsList")
    @ResponseBody
    public RespObj getMonetaryGoodsList(String name,
                                        @RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            List<MonetaryGoodsDto> list = service.getMonetaryGoodsList(name, page, pageSize);
            map.put("list", list);
            int count = service.getMonetaryGoodsCount(name);
            map.put("count", count);
            obj.setMessage(map);
            obj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }

    @ApiOperation(value = "商品详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "商品详情", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMonetaryGoodsDetail")
    @ResponseBody
    public RespObj getMonetaryGoodsDetail(String goodId) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            MonetaryGoodsDto dto = service.getMonetaryGoodsDetail(goodId);
            map.put("goods", dto);
            obj.setMessage(map);
            obj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }
}
