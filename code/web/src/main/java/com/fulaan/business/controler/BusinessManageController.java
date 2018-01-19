package com.fulaan.business.controler;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.business.service.BusinessManageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2018/1/15.
 */
@Api(value="运营管理")
@Controller
@RequestMapping("/web/business")
public class BusinessManageController extends BaseController {
    @Autowired
    private BusinessManageService businessManageService;

    /**
     *  查询所有
     */
    @ApiOperation(value = "查询所有", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getEntryList")
    @ResponseBody
    public String getEntryList(@ApiParam(name="page",required = false,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                           @ApiParam(name="pageSize",required = false,value="pageSize") @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                           @ApiParam(name="jiaId",required = false,value="jiaId") @RequestParam(value="jiaId",defaultValue = "") String jiaId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos = businessManageService.getList(jiaId,page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询所有失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  处理老用户脚本
     */
    @ApiOperation(value = "处理老用户脚本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addOldEntry")
    @ResponseBody
    public String addOldEntry(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = System.currentTimeMillis();
            businessManageService.createBusinessManage();
            long time2 = System.currentTimeMillis();
            long during = time2-time;
            respObj.setMessage("处理花费时间为"+during+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("处理老用户脚本失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  更新脚本
     */
    @ApiOperation(value = "处理老用户脚本", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateOldEntry")
    @ResponseBody
    public String updateOldEntry(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = System.currentTimeMillis();
            businessManageService.checkBusinessManage2();
            long time2 = System.currentTimeMillis();
            long during = time2-time;
            respObj.setMessage("更新时间为"+during+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("更新脚本脚本失败!");
        }
        return JSON.toJSONString(respObj);
    }


}
