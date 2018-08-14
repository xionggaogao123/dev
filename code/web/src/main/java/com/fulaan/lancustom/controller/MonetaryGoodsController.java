package com.fulaan.lancustom.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.lancustom.dto.MonetaryGoodsDto;
import com.fulaan.lancustom.service.MonetaryGoodsService;
import com.fulaan.newVersionBind.dto.MakeOutUserRealationDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Auther: taotao.chan
 * @Date: 2018/8/6 15:30
 * @Description: 商品数据后台管理
 */
@Api("小兰客服")
@Controller
@RequestMapping("/web/monetaryGoods")
public class MonetaryGoodsController {
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


    @ApiOperation(value = "保存商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新商品图片", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveGoodsInfo")
    @ResponseBody
    public RespObj saveGoodsInfo(@RequestBody MonetaryGoodsDto dto) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            service.saveGoodsInfo(dto);
            obj.setCode(Constant.SUCCESS_CODE);
            obj.setMessage("成功！");
        } catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }

    @ApiOperation(value = "删除商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除商品",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delMonetaryGoods")
    @ResponseBody
    public RespObj delMonetaryGoods(@ObjectIdType ObjectId goodId){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            service.delMonetaryGoods(goodId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
