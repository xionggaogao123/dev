package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.appmarket.service.AppMarketService;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.base.BaseController;
import com.fulaan.reportCard.service.ReportCardService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by James on 2017/11/18.
 */
@Api(value = "后台管理类")
@Controller
@RequestMapping("/web/backstage")
public class BackStageController extends BaseController {
    @Autowired
    private BackStageService backStageService;

    @Autowired
    private AppMarketService appMarketService;


    /**
     * 后台设置学生端地图回调时间
     *
     */
    @ApiOperation(value = "后台设置学生端回调时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSetBackEntry")
    @ResponseBody
    public String addSetBackEntry(@ApiParam(name = "time", required = true, value = "回调间隔") @RequestParam("time") int time){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageService.addBackTimeEntry(getUserId(),time);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage("后台设置学生端回调时间失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 后台设置学生端应用回调时间
     *
     */
    @ApiOperation(value = "后台设置学生端应用回调时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSetAppBackEntry")
    @ResponseBody
    public String addSetAppBackEntry(@ApiParam(name = "time", required = true, value = "回调间隔") @RequestParam("time") int time){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageService.addAppBackTimeEntry(getUserId(), time);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage("后台设置学生端应用回调时间失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 后台设置默认管控时间选择表
     *
     */
    @ApiOperation(value = "后台设置学生端应用回调时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSetTimeListEntry")
    @ResponseBody
    public String addSetTimeListEntry(@ApiParam(name = "time", required = true, value = "时间值") @RequestParam("time") int time){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageService.addSetTimeListEntry(getUserId(), time);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage("后台设置默认管控时间选择表失败!");
        }
        return JSON.toJSONString(respObj);
    }


    @RequestMapping("/importApkFile")
    @ResponseBody
    public RespObj importUserControl(HttpServletRequest servletRequest)throws Exception{
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        MultipartRequest request=(MultipartRequest)servletRequest;
        try {
            MultiValueMap<String, MultipartFile> fileMap = request.getMultiFileMap();
            for (List<MultipartFile> multipartFiles : fileMap.values()) {
                for(MultipartFile file:multipartFiles) {
                    System.out.println("----" + file.getOriginalFilename());
                    appMarketService.importApkFile(file,file.getInputStream(),file.getOriginalFilename());
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("导入模板成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }



}
