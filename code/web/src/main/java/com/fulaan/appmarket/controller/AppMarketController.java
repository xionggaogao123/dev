package com.fulaan.appmarket.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.appmarket.dto.AppDetailCommentDTO;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.appmarket.service.AppMarketService;
import com.fulaan.base.BaseController;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/10/10.
 */
@Api(value = "应用市场相关接口")
@Controller
@RequestMapping("/appMarket")
public class AppMarketController extends BaseController{

    @Autowired
    private AppMarketService appMarketService;


    @ApiOperation(value = "网页端跳转到管理应用界面", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/index")
    public String redirectIndex(HttpServletRequest request, Map<String,Object> model){
        return "/appmarket/manageIndex";
    }


    @ApiOperation(value = "保存每个应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveAppDetail")
    @ResponseBody
    public RespObj saveAppDetail(@RequestBody AppDetailDTO appDetailDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appMarketService.saveAppDetail(appDetailDTO);
            respObj.setMessage("保存应用成功!");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "查询后台的所有应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAllAppDetails")
    @ResponseBody
    public RespObj getAllAppDetails(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppDetailDTO> appDetailDTOs=appMarketService.getAllAppDetails();
            respObj.setMessage(appDetailDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取评论列表以及评分比例", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommentList")
    @ResponseBody
    public RespObj getCommentList(
            @ObjectIdType ObjectId appDetailId,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appMarketService.getCommentList(appDetailId, page, pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "保存应用下的评论", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveAppDetailComment")
    @ResponseBody
    public RespObj saveAppDetailComment(@RequestBody AppDetailCommentDTO commentDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appMarketService.saveAppDetailComment(commentDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存应用下的评论成功!");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "根据查询条件查询应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchAppByCondition")
    @ResponseBody
    public RespObj searchAppByCondition(
            @RequestParam(required = false,defaultValue = "")String regular
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppDetailDTO> appDetailDTOs=appMarketService.getAppByCondition(regular);
            respObj.setMessage(appDetailDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


}