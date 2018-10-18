package com.fulaan.controlphone.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.backstage.dto.JxmAppVersionDTO;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.base.BaseController;
import com.fulaan.controlphone.dto.*;
import com.fulaan.controlphone.service.ControlPhoneService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.pojo.integral.IntegralType;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by James on 2017/11/3.
 */
@Controller
@RequestMapping("/jxmapi/controlphone")
@Api(value = "管控端手机")
public class ControlPhoneController extends BaseController {

    @Autowired
    private ControlPhoneService controlPhoneService;
    @Autowired
    private BackStageService backStageService;

    private static final Logger logger =Logger.getLogger(ControlPhoneController.class);

    @Autowired
    private IntegralSufferService integralSufferService;
    //管控电话
    /**
     * 添加管控手机号
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加管控手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addControlPhone")
    @ResponseBody
    public String addControlPhone(@ApiParam @RequestBody ControlPhoneDTO dto){
        //
        dto.setParentId(getUserId().toString());
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = controlPhoneService.addControlPhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加管控手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询手机list
     * @return
     */
    @ApiOperation(value = "查询手机list", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getControlPhoneList")
    @ResponseBody
    public String getControlPhoneList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ControlPhoneDTO> result = controlPhoneService.getControlPhoneList(getUserId(),new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询手机list失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 查询手机list
     * @return
     */
    @ApiOperation(value = "查询手机list", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShareControlPhoneList")
    @ResponseBody
    public String getShareControlPhoneList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            List<ControlPhoneDTO> result = controlPhoneService.getControlPhoneList(parentId,new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询手机list失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 删除手机号
     * @return
     */
    @ApiOperation(value = "删除手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delControlPhone")
    @ResponseBody
    public String delControlPhone(@ApiParam(name = "id", required = true, value = "手机号id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.delControlPhone(new ObjectId(id));
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修改手机号
     * @return
     */
    @ApiOperation(value = "修改手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateControlPhone")
    @ResponseBody
    public String updateControlPhone(@ApiParam(name = "id", required = true, value = "手机号id") @RequestParam("id") String id,
                                     @ApiParam(name = "name", required = true, value = "姓名") @RequestParam("name") String name,
                                     @ApiParam(name = "phone", required = true, value = "手机号") @RequestParam("phone") String phone){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.updateControlPhone(new ObjectId(id),name,phone);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 学生查询管控手机号（学生端)
     */
    @ApiOperation(value = "查询管控手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getPhoneListForStudent")
    @ResponseBody
    public String getPhoneListForStudent(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ControlPhoneDTO> result= controlPhoneService.getPhoneListForStudent(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询管控手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    //管控应用
    /**
     * 查询推送应用（家长端）
     *
     */
    @ApiOperation(value = "查询推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommunityAppList")
    @ResponseBody
    public String getCommunityAppList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<Map<String,Object>> result = controlPhoneService.getAppListForStudent(new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师查询可推送应用列表(搜索)
     */
    @ApiOperation(value = "老师查询可推送应用列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShouldAppList")
    @ResponseBody
    public String getShouldAppList(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId,
                                   @ApiParam(name = "keyword", required = true, value = "关键字") @RequestParam(value = "keyword",defaultValue = "") String keyword){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<AppDetailDTO> dtos = controlPhoneService.getShouldAppList(getUserId(),new ObjectId(communityId),keyword);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师查询可推送应用列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 家长查询可推送应用列表
     */
    @ApiOperation(value = "老师查询可推送应用列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getParentAppList")
    @ResponseBody
    public String getParentAppList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<AppDetailDTO> dtos = controlPhoneService.getParentAppList(getUserId(),new ObjectId(sonId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师查询可推送应用列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 家长查询可推送应用列表（搜索）
     */
    @ApiOperation(value = "家长查询可推送应用列表（搜索）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/seacherParentAppList")
    @ResponseBody
    public String seacherParentAppList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                       @ApiParam(name = "keyword", required = true, value = "关键字") @RequestParam("keyword") String keyword){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<AppDetailDTO> dtos = controlPhoneService.seacherParentAppList(getUserId(), new ObjectId(sonId), keyword);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("家长查询可推送应用列表（搜索）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师推送应用
     */
    @ApiOperation(value = "老师推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCommunityAppList")
    @ResponseBody
    public String addCommunityAppList(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId,
                                      @ApiParam(name = "appId", required = true, value = "推送应用id") @RequestParam("appId") String appId,
                                      @ApiParam(name = "type", required = true, value = "1卸载2推送") @RequestParam("type") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.addTeaCommunityAppList(getUserId(),new ObjectId(communityId), new ObjectId(appId),type);
            if(type==1){
                respObj.setMessage("卸载成功");
            }else{
                respObj.setMessage("推送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 家长推送应用
     */
    @ApiOperation(value = "家长推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addParentAppList")
    @ResponseBody
    public String addParentAppList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                   @ApiParam(name = "appId", required = true, value = "推送应用id") @RequestParam("appId") String appId,
                                   @ApiParam(name = "type", required = true, value = "1卸载2推送") @RequestParam("type") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.addParentAppList(getUserId(),new ObjectId(sonId),new ObjectId(appId),type);
            if(type==1){
                respObj.setMessage("卸载成功");
            }else{
                respObj.setMessage("推送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("家长推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }
    public static void main(String[] args){
        int i = 1;
       System.out.println(i);
    }
    /**
     * 学生获取推送应用
     */
    @ApiOperation(value = "学生获取推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAppListForStudent")
    @ResponseBody
    public String getAppListForStudent(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<AppDetailDTO> result = controlPhoneService.getCommunityAppList(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("学生获取推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 家长设置应用防沉迷时间
     *
     */
    @ApiOperation(value = "家长设置应用防沉迷时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addAppTimeEntry")
    @ResponseBody
    public String addAppTimeEntry(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                  @ApiParam(name = "time", required = true, value = "时间") @RequestParam("time") int time){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.addAppTimeEntry(new ObjectId(sonId),getUserId(),time);
            respObj.setMessage("家长设置应用防沉迷时间成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("家长设置应用防沉迷时间失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 定时接受孩子的应用使用情况
     */
    @ApiOperation(value = "定时接受孩子的应用使用情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/acceptOldAppResultList")
    @ResponseBody
    public String acceptOldAppResultList(@ApiParam(name = "dto", required = true, value = "应用使用情况list") @RequestBody ResultAppDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = controlPhoneService.acceptOldAppResultList(dto,getUserId());
            respObj.setMessage(time);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时接受孩子的应用使用情况失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * （新）定时接受孩子的应用使用情况
     */
    @ApiOperation(value = "定时接受孩子的应用使用情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/acceptAppResultList")
    @ResponseBody
    public String acceptAppResultList(@ApiParam(name = "dto", required = true, value = "应用使用情况list") @RequestBody ResultAppDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = controlPhoneService.acceptAppResultList(dto,getUserId());
            respObj.setMessage(time);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时接受孩子的应用使用情况失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * （新）定时接受孩子的应用使用情况
     */
    @ApiOperation(value = "定时接受孩子的应用使用情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/acceptNewAppResultList")
    @ResponseBody
    public String acceptNewAppResultList(@ApiParam(name = "dto", required = true, value = "应用使用情况list") @RequestBody ResultNewAppDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = controlPhoneService.acceptNewAppResultList(dto,getUserId());
            respObj.setMessage(time);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时接受孩子的应用使用情况失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * （新）定时接受孩子的应用使用情况
     */
    @ApiOperation(value = "定时接受孩子的应用使用情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/acceptFourAppResultList")
    @ResponseBody
    public String acceptFourAppResultList(@ApiParam(name = "dto", required = true, value = "应用使用情况list") @RequestBody ResultNewAppDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long time = controlPhoneService.acceptNewAppResultList(dto,getUserId());
            respObj.setMessage(time);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时接受孩子的应用使用情况失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 定时获取孩子的应用使用情况（家长）
     */
    @ApiOperation(value = "定时获取孩子的应用使用情况（家长）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/seacherAppResultList")
    @ResponseBody
    public String seacherAppResultList(@ApiParam(name = "sonId", required = true, value = "应用使用情况list") @RequestParam("sonId") String sonId,
                                       @ApiParam(name = "dateTime", required = true, value = "dateTime") @RequestParam("dateTime") String dateTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long dTm = 0l;
            if(dateTime != null && dateTime != ""){
                dTm = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
            }
            long current = System.currentTimeMillis();
            String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(current));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(dTm==strNum){
                if(hour<8){
                    dTm = dTm - 24*60*60*1000;
                }
            }
            Map<String,Object> map = controlPhoneService.seacherAppResultList(getUserId(),new ObjectId(sonId),dTm);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时获取孩子的应用使用情况（家长）失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 定时获取孩子黑名单应用使用情况（家长）
     */
    @ApiOperation(value = "定时获取孩子黑名单应用使用情况（家长）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/seacherBlackAppResultList")
    @ResponseBody
    public String seacherBlackAppResultList(@ApiParam(name = "sonId", required = true, value = "应用使用情况list") @RequestParam("sonId") String sonId,
                                       @ApiParam(name = "dateTime", required = true, value = "dateTime") @RequestParam("dateTime") String dateTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long dTm = 0l;
            if(dateTime != null && dateTime != ""){
                dTm = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
            }
            long current = System.currentTimeMillis();
            String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(current));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(dTm==strNum){
                if(hour<8){
                    dTm = dTm - 24*60*60*1000;
                }
            }
            Map<String,Object> map = controlPhoneService.seacherAppResultList3(getUserId(), new ObjectId(sonId), dTm);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时获取孩子的应用使用情况（家长）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 定时获取孩子黑名单应用使用情况（家长）
     */
    @ApiOperation(value = "定时获取孩子黑名单应用使用情况（家长）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/seacherFourAppResultList")
    @ResponseBody
    public String seacherFourAppResultList(@ApiParam(name = "sonId", required = true, value = "应用使用情况list") @RequestParam("sonId") String sonId,
                                            @ApiParam(name = "dateTime", required = true, value = "dateTime") @RequestParam("dateTime") String dateTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long dTm = 0l;
            if(dateTime != null && dateTime != ""){
                dTm = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
            }
            long current = System.currentTimeMillis();
            String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(current));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(dTm==strNum){
                if(hour<8){
                    dTm = dTm - 24*60*60*1000;
                }
            }
            Map<String,Object> map = controlPhoneService.seacherAppResultListFour(getUserId(), new ObjectId(sonId), dTm);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时获取孩子的应用使用情况（家长）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 定时获取孩子黑名单应用使用情况（家长）
     */
    @ApiOperation(value = "定时获取孩子黑名单应用使用情况（家长）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/seacherShareFourAppResultList")
    @ResponseBody
    public String seacherShareFourAppResultList(@ApiParam(name = "sonId", required = true, value = "应用使用情况list") @RequestParam("sonId") String sonId,
                                           @ApiParam(name = "dateTime", required = true, value = "dateTime") @RequestParam("dateTime") String dateTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            long dTm = 0l;
            if(dateTime != null && dateTime != ""){
                dTm = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
            }
            long current = System.currentTimeMillis();
            String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(current));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(dTm==strNum){
                if(hour<8){
                    dTm = dTm - 24*60*60*1000;
                }
            }
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            Map<String,Object> map = controlPhoneService.seacherAppResultListFour(parentId, new ObjectId(sonId), dTm);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时获取孩子的应用使用情况（家长）失败!");
        }
        return JSON.toJSONString(respObj);
    }

    //管控地图
    /**
     * 定时接受孩子的位置信息
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/acceptMapResult")
    @ResponseBody
    public String acceptMapResult(@ApiParam(name = "dto", required = true, value = "地图信息") @RequestBody ControlMapDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.acceptMapResult(dto,getUserId());
            respObj.setMessage("定时接受孩子的位置信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("定时接受孩子的位置信息失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 家长触发学生端上传定位信息
     */
    @ApiOperation(value = "家长触发学生端上传定位信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/loadStudentMap")
    @ResponseBody
    public String loadStudentMap(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.loadStudentMap(getUserId(), new ObjectId(sonId));
            respObj.setMessage("上传中");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("家长触发学生端上传定位信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 家长触发学生端上传定位信息
     */
    @ApiOperation(value = "家长触发学生端上传定位信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/loadShareStudentMap")
    @ResponseBody
    public String loadShareStudentMap(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            controlPhoneService.loadStudentMap(parentId, new ObjectId(sonId));
            respObj.setMessage("上传中");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("家长触发学生端上传定位信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取学生当前地图定位数据
     */
    @ApiOperation(value = "获取地图定位数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentMap")
    @ResponseBody
    public String getStudentMap(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ControlMapDTO controlMapDTO = controlPhoneService.getStudentMap(getUserId(), new ObjectId(sonId));
            respObj.setMessage(controlMapDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取学生当前地图定位数据失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取学生当前地图定位数据
     */
    @ApiOperation(value = "获取地图定位数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShareStudentMap")
    @ResponseBody
    public String getShareStudentMap(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            ControlMapDTO controlMapDTO = controlPhoneService.getStudentMap(parentId, new ObjectId(sonId));
            respObj.setMessage(controlMapDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取学生当前地图定位数据失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMapNow")
    @ResponseBody
    public String getMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dto = controlPhoneService.getMapNow(getUserId(), new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            logger.error("error",e);
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（改版家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewMapNow")
    @ResponseBody
    public String getNewMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dto = controlPhoneService.getNewMapNow(getUserId(), new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            logger.error("error",e);
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（改版家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getFourMapNow")
    @ResponseBody
    public String getFourMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dto = controlPhoneService.getFourMapNow(getUserId(), new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（改版家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getFiveMapNow")
    @ResponseBody
    public String getFiveMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dto = controlPhoneService.getFiveMapNow(getUserId(), new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（改版家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShareFourMapNow")
    @ResponseBody
    public String getShareFourMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            Map<String,Object> dto = controlPhoneService.getFourMapNow(parentId, new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息（改版家长首页）
     */
    @ApiOperation(value = "定时接受孩子的位置信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShareFiveMapNow")
    @ResponseBody
    public String getShareFiveMapNow(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId parentId = controlPhoneService.getMainUserId(new ObjectId(sonId));
            Map<String,Object> dto = controlPhoneService.getFiveMapNow(parentId, new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子绑定社群列表
     */
    @ApiOperation(value = "获取孩子绑定社群列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSonCommunityList")
    @ResponseBody
    public String getSonCommunityList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<GroupOfCommunityDTO> dto = controlPhoneService.getSonCommunityList(new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子绑定社群列表!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子绑定社群列表
     */
    @ApiOperation(value = "获取孩子绑定社群列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShareSonCommunityList")
    @ResponseBody
    public String getShareSonCommunityList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<GroupOfCommunityDTO> dto = controlPhoneService.getSonCommunityList(new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子绑定社群列表!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获取孩子地图信息记录（完整）
     */
    @ApiOperation(value = "获取孩子地图信息记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMapListEntry")
    @ResponseBody
    public String getMapListEntry(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                  @ApiParam(name = "startTime", required = true, value = "开始时间") @RequestParam("startTime") String startTime,
                                  @ApiParam(name = "endTime", required = true, value = "结束时间") @RequestParam("endTime") String endTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ControlMapDTO> dtos = controlPhoneService.getMapListEntry(getUserId(), new ObjectId(sonId), startTime, endTime);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子地图信息记录失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 获取孩子地图信息记录（简易）
     */
    @ApiOperation(value = "获取孩子地图信息记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleMapListEntry")
    @ResponseBody
    public String getSimpleMapListEntry(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                  @ApiParam(name = "dataTime", required = true, value = "日期") @RequestParam("dataTime") String dataTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ControlMapDTO> dtos = controlPhoneService.getSimpleMapListEntry(getUserId(), new ObjectId(sonId), dataTime);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子地图信息记录失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 获取消息记录
     */
    @ApiOperation(value = "获取消息记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSonMessage")
    @ResponseBody
    public String getSonMessage(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getSonMessage(getUserId(), new ObjectId(sonId),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取消息记录失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 孩子登录获取所有信息
     */
    @ApiOperation(value = "孩子登录获取所有信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAllMessageForSon")
    @ResponseBody
    public String getAllMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getAllMessageForSon(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("孩子登录获取所有信息失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 新孩子登录获取所有信息
     */
    @ApiOperation(value = "新孩子登录获取所有信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewAllMessageForSon")
    @ResponseBody
    public String getNewAllMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getNewAllSchoolMessageForSon(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("新孩子登录获取所有信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 分段管控孩子登录获取所有信息
     */
    @ApiOperation(value = "分段管控孩子登录获取所有信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCurrentAllMessageForSon")
    @ResponseBody
    public String getCurrentAllMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getCurrentAllSchoolMessageForSon(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("新孩子登录获取所有信息失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 孩子未登录获取默认信息
     */
    @SessionNeedless
    @ApiOperation(value = "孩子未登录获取默认信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleMessageForSon")
    @ResponseBody
    public String getSimpleMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getSimpleMessageForSon();
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("孩子登录获取所有信息失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 孩子新未登录获取默认信息
     */
    @SessionNeedless
    @ApiOperation(value = "孩子新未登录获取默认信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewSimpleMessageForSon")
    @ResponseBody
    public String getNewSimpleMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getNewSimpleMessageForSon();
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("孩子新未登录获取默认信息失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 孩子分段未登录获取默认信息
     */
    @SessionNeedless
    @ApiOperation(value = "孩子分段未登录获取默认信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCurrentSimpleMessageForSon")
    @ResponseBody
    public String getCurrentSimpleMessageForSon(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getCurrentSimpleMessageForSon();
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("孩子新未登录获取默认信息失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 老师首页加载基础信息
     */
    @ApiOperation(value = "老师首页加载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleMessageForTea")
    @ResponseBody
    public String getSimpleMessageForTea(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getSimpleMessageForTea(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 最新老师首页加载基础信息
     */
    @ApiOperation(value = "老师首页加载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewSimpleMessageForTea")
    @ResponseBody
    public String getNewSimpleMessageForTea(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getNewSimpleMessageForTea(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            logger.error("error",e);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 最新老师首页加载基础信息(分段管控)
     */
    @ApiOperation(value = "最新老师首页加载基础信息(分段管控)", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCurrentSimpleMessageForTea")
    @ResponseBody
    public String getCurrentSimpleMessageForTea(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlPhoneService.getCurrentSimpleMessageForTea(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            logger.error("error",e);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师首页加载
     */
    @ApiOperation(value = "老师首页加载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAllMessageForTea")
    @ResponseBody
    public String getAllMessageForTea(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //Map<String,Object> dtos= controlPhoneService.getAllMessageForTea(getUserId(),new ObjectId(communityId));旧
            Map<String,Object> dtos= controlPhoneService.getSchoolOneMessageForTea(getUserId(), new ObjectId(communityId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 老师首页加载(分段管控)
     */
    @ApiOperation(value = "老师首页加载(分段管控)", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCurentAllMessageForTea")
    @ResponseBody
    public String getCurentAllMessageForTea(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //Map<String,Object> dtos= controlPhoneService.getAllMessageForTea(getUserId(),new ObjectId(communityId));旧
            Map<String,Object> dtos= controlPhoneService.getNewSchoolOneMessageForTea(getUserId(), new ObjectId(communityId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 老师切换到管控
     * @return
     */
    @ApiOperation(value = "老师切换到管控", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteControlTime")
    @ResponseBody
    public String deleteControlTime(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.deleteControlTime(getUserId(),new ObjectId(communityId));
            respObj.setMessage("改变成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师切换到管控!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师设置自由时间
     * @return
     */
    @ApiOperation(value = "老师设置自由时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/setControlTime")
    @ResponseBody
    public String setControlTime(@ApiParam(name = "time", required = true, value = "时间") @RequestParam("time") int time,
                                 @ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlPhoneService.setControlTime(getUserId(),new ObjectId(communityId),time);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师设置自由时间!");
        }
        return JSON.toJSONString(respObj);
    }

    /************************ 系统设置********************************/
    /**
     * 后台管控时间的录入
     */
    @ApiOperation(value = "获取孩子地图信息记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addControlTime")
    @ResponseBody
    public String addControlTime(@RequestBody ControlSchoolTimeDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //Map<String,Object> dtos= controlPhoneService.getSonMessage(getUserId(), new ObjectId(sonId),page,pageSize);
            //respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子地图信息记录失败!");
        }
        return JSON.toJSONString(respObj);
    }




    /**======================实验demo========================*/
    /**
     * 添加管控手机号
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "添加管控手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSimpleControlPhone33")
    @ResponseBody
    public String addSimpleControlPhone(@ApiParam(name = "phone", required = true, value = "电话") @RequestParam("phone") String phone,
                                        @ApiParam(name = "name", required = true, value = "姓名") @RequestParam("name") String name){
        //
        ControlPhoneDTO dto = new ControlPhoneDTO();
        dto.setParentId("55934c26f6f28b7261c1bab0");//geng
        dto.setName(name);
        dto.setUserId("55934c26f6f28b7261c1bae1");//hao
        dto.setPhone(phone);
        dto.setType(2);
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = controlPhoneService.addControlPhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加管控手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师推送应用
     */
    @SessionNeedless
    @ApiOperation(value = "老师推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSimpleCommunityAppList33")
    @ResponseBody
    public String addSimpleCommunityAppList(@ApiParam(name = "code", required = true, value = "包名") @RequestParam("code") String code){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        ControlPhoneDTO dto = new ControlPhoneDTO();
        dto.setParentId("55934c26f6f28b7261c1bab0");//geng
        dto.setName(code);
        dto.setUserId("55934c26f6f28b7261c1bae1");//hao
        dto.setType(1);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = controlPhoneService.addControlSimplePhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生获包名
     */
    @SessionNeedless
    @ApiOperation(value = "学生获包名", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleAppList33")
    @ResponseBody
    public String getSimpleAppList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String result = controlPhoneService.getSimpleAppList(new ObjectId("55934c26f6f28b7261c1bae1"),1);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生获电话
     */
    @SessionNeedless
    @ApiOperation(value = "学生获电话", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimplePhoneList33")
    @ResponseBody
    public String getSimplePhoneList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<ControlPhoneDTO> result = controlPhoneService.getSimpleAppList2(new ObjectId("55934c26f6f28b7261c1bae1"),2);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生发送定位数据
     */
    @SessionNeedless
    @ApiOperation(value = "学生发送定位数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSimpleMapEntry33")
    @ResponseBody
    public String addSimpleMapEntry(@ApiParam(name = "longitude", required = true, value = "经度") @RequestParam("longitude") String longitude,
                                    @ApiParam(name = "latitude", required = true, value = "纬度") @RequestParam("latitude") String latitude){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ControlMapDTO dto = new ControlMapDTO();
            dto.setUserId("55934c26f6f28b7261c1bae1");
            dto.setLongitude(longitude);
            dto.setLatitude(latitude);
            String result = controlPhoneService.addSimpleMapEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 获得所有的复兰应用的版本号信息
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "获得所有的复兰应用的版本号信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAllAppVersion")
    @ResponseBody
    public String getAllAppVersion(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<JxmAppVersionDTO> dtos =  backStageService.getAllAppVersion();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得所有的复兰应用的版本号信息失败");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 处理老数据
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "处理老数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateAllAppVersion")
    @ResponseBody
    public String updateAllAppVersion(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            backStageService.updateAllAppVersion();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("处理老数据失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获得所有的复兰应用的版本号信息
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "获得所有的复兰应用的版本号信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewAllAppVersion")
    @ResponseBody
    public String getNewAllAppVersion(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<JxmAppVersionDTO> dtos =  backStageService.getAllAppVersion();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得所有的复兰应用的版本号信息失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 获得所有的复兰应用的版本号信息
     * @return
     */
    @SessionNeedless
    @ApiOperation(value = "获得所有的复兰应用的版本号信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMoreNewAllAppVersion")
    @ResponseBody
    public String getMoreNewAllAppVersion(@RequestParam(value = "type",defaultValue = "0")int type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<JxmAppVersionDTO> dtos =  backStageService.getNewAllAppVersion(type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得所有的复兰应用的版本号信息失败");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 获得所有的第三方应用
     * @return
     */
    @ApiOperation(value = "获得所有的第三方应用", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getThirdAppList")
    @ResponseBody
    public String getThirdAppList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<AppDetailDTO> dtos =  controlPhoneService.getThirdAppList(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得所有的第三方应用失败");
        }
        return JSON.toJSONString(respObj);
    }



    /**
     * 获得操作者的所有推送记录
     * @return
     */
    @ApiOperation(value = "获得操作者的所有推送记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserSendAppList")
    @ResponseBody
    public String getUserSendAppList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> dtos =  controlPhoneService.getUserSendAppList(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得操作者的所有推送记录失败");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 查询某个社区或孩子的推送列表
     * @return
     */
    @ApiOperation(value = "查询某个社区或孩子的推送列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOneAppList")
    @ResponseBody
    public String getOneAppList(@ApiParam(name = "contactId", required = true, value = "关联id") @RequestParam("contactId") String contactId,
                                @ApiParam(name = "type", required = true, value = "类型") @RequestParam("type") int type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<AppDetailDTO> dtos =  controlPhoneService.getOneAppList(getUserId(), new ObjectId(contactId),type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询某个社区或孩子的推送列表失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 推送应用
     * @return
     */
    @ApiOperation(value = "推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addAppToChildOrCommunity")
    @ResponseBody
    public String addAppToChildOrCommunity(@ApiParam(name = "contactId", required = true, value = "关联id") @RequestParam("contactId") String contactId,
                                @ApiParam(name = "type", required = true, value = "类型") @RequestParam("type") int type,
                                @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam("appId") String appId,
                                @ApiParam(name = "isCheck", required = true, value = "1 卸载 2 推送") @RequestParam("isCheck") int isCheck){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.addAppToChildOrCommunity(getUserId(), new ObjectId(contactId), type, new ObjectId(appId),isCheck);
            respObj.setCode(Constant.SUCCESS_CODE);
            if(isCheck==1){
                respObj.setMessage("卸载应用成功");
            }else{
                respObj.setMessage("推送应用成功");
            }

        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("推送应用失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 推送应用
     * @return
     */
    @ApiOperation(value = "推送应用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addNewAppToChildOrCommunity")
    @ResponseBody
    public String addNewAppToChildOrCommunity(@ApiParam(name = "contactId", required = true, value = "关联id") @RequestParam("contactId") String contactId,
                                           @ApiParam(name = "type", required = true, value = "类型") @RequestParam("type") int type,
                                           @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam("appId") String appId,
                                           @ApiParam(name = "isCheck", required = true, value = "1 卸载 2 推送") @RequestParam("isCheck") int isCheck){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            int status = controlPhoneService.addAppToChildOrCommunity(getUserId(), new ObjectId(contactId), type, new ObjectId(appId),isCheck);
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = new HashMap<String, Object>();
            if(isCheck==1){
                map.put("msg", "卸载应用成功");
            }else{
                map.put("msg","推送应用成功");
            }
            if(status!=0){
                int score = integralSufferService.addIntegral(getUserId(), IntegralType.find, 4, 1);
                map.put("score", score);
            }
            respObj.setMessage(map);
            if(status==0){
                respObj.setCode(Constant.FAILD_CODE);
                if(isCheck==1){
                    respObj.setErrorMessage("您的小孩已进入“校管控”，如需推送应用，请联系老师！");
                }else{
                    respObj.setErrorMessage("您的小孩已进入“校管控”，如需卸载应用，请联系老师！");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("推送应用失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询某个应用的总体情况
     * @return
     */
    @ApiOperation(value = "查询某个应用的总体情况", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectOneAppFromOwen")
    @ResponseBody
    public String selectOneAppFromOwen(@ApiParam(name = "appId", required = true, value = "应用id") @RequestParam("appId") String appId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = controlPhoneService.selectOneAppFromOwen(getUserId(), new ObjectId(appId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询某个应用的总体情况失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *
     * 给groupEntry中老数据加上逻辑删除字段
     */
    @ApiOperation(value = "修改无逻辑删除字段的对象", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addRemoveToGroupEntry")
    @ResponseBody
    public RespObj addRemoveToGroupEntry(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.addRemoveToGroupEntry();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改无逻辑删除字段的对象成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改无逻辑删除字段的对象失败");
        }
        return respObj;
    }

    @ApiOperation(value = "获取班级内的最新版本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommunityVersionList")
    @ResponseBody
    public RespObj getCommunityVersionList(@RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = controlPhoneService.getCommunityVersionList(new ObjectId(communityId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取班级内的最新版本信息失败");
        }
        return respObj;
    }

    @ApiOperation(value = "获取班级内的最新版本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewCommunityVersionList")
    @ResponseBody
    public RespObj getNewCommunityVersionList(@RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = controlPhoneService.getNewCommunityVersionList(new ObjectId(communityId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取班级内的最新版本信息失败");
        }
        return respObj;
    }

    @ApiOperation(value = "获取班级内的最新版本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getThreeCommunityVersionList")
    @ResponseBody
    public RespObj getThreeCommunityVersionList(@RequestParam(value="communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = controlPhoneService.getThreeCommunityVersionList(new ObjectId(communityId),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取班级内的最新版本信息失败");
        }
        return respObj;
    }


    @ApiOperation(value = "更新学生最新版本信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCommunityPersion")
    @ResponseBody
    public RespObj addCommunityPersion(@RequestParam(value="version") String version){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.addCommunityPersion(getUserId(),version);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("更新学生最新版本信息失败");
        }
        return respObj;
    }

    @ApiOperation(value = "删除冗余数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteAppResultPersion")
    @ResponseBody
    public RespObj deleteAppResultPersion(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.deleteAppResultPersion();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除冗余数据成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除冗余数据失败");
        }
        return respObj;
    }

    @ApiOperation(value = "最新的一条提交", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNewAppResultList")
    @ResponseBody
    public RespObj getNewAppResultList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<ControlAppResultDTO> dtos = controlPhoneService.getNewAppResultList(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取失败");
        }
        return respObj;
    }

    @ApiOperation(value = "获取本人的最新命令", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyNewVersion")
    @ResponseBody
    public RespObj getMyNewVersion(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String dtos = controlPhoneService.getMyNewVersion(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取获取本人的最新命令失败");
        }
        return respObj;
    }

    @ApiOperation(value = "设置共享用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addControlShareUser")
    @ResponseBody
    public RespObj addControlShareUser(@RequestParam(value="sonId") String sonId,
                                       @RequestParam(value="jiaId") String jiaId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.addControlShareUser(getUserId(),new ObjectId(sonId),jiaId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("分享成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "解除共享用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteControlShareUser")
    @ResponseBody
    public RespObj deleteControlShareUser(@RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            controlPhoneService.deleteControlShareUser(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("解除成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "查询共享用户", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectControlShareUser")
    @ResponseBody
    public RespObj selectControlShareUser(@RequestParam(value="sonId") String sonId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> mapList = controlPhoneService.selectControlShareUser(getUserId(), new ObjectId(sonId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(mapList);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询共享用户失败！");
        }
        return respObj;
    }
}
