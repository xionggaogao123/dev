package com.fulaan.controlphone.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.base.BaseController;
import com.fulaan.controlphone.dto.ControlMapDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.fulaan.controlphone.dto.ResultAppDTO;
import com.fulaan.controlphone.service.ControlPhoneService;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/11/3.
 */
@Controller
@RequestMapping("/jxmapi/controlphone")
@Api(value = "管控端手机")
public class ControlPhoneController extends BaseController {

    @Autowired
    private ControlPhoneService controlPhoneService;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = controlPhoneService.addControlPhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlPhoneDTO> result = controlPhoneService.getControlPhoneList(getUserId(),new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.delControlPhone(new ObjectId(id));
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.updateControlPhone(new ObjectId(id),name,phone);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlPhoneDTO> result= controlPhoneService.getPhoneListForStudent(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<Map<String,Object>> result = controlPhoneService.getAppListForStudent(new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查询推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师查询可推送应用列表
     */
    @ApiOperation(value = "老师查询可推送应用列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getShouldAppList")
    @ResponseBody
    public String getShouldAppList(@ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppDetailDTO> dtos = controlPhoneService.getShouldAppList(new ObjectId(communityId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppDetailDTO> dtos = controlPhoneService.getParentAppList(getUserId(),new ObjectId(sonId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/seacherParentAppList")
    @ResponseBody
    public String seacherParentAppList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId,
                                       @ApiParam(name = "keyword", required = true, value = "关键字") @RequestParam("keyword") String keyword){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppDetailDTO> dtos = controlPhoneService.seacherParentAppList(getUserId(), new ObjectId(sonId),keyword);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("老师查询可推送应用列表失败!");
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
                                      @ApiParam(name = "appIds", required = true, value = "推送应用idList") @RequestParam("appIds") List<String> appIds){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.addCommunityAppList(new ObjectId(communityId), appIds);
            respObj.setMessage("老师推送应用成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.addParentAppList(getUserId(),new ObjectId(sonId),new ObjectId(appId),type);
            respObj.setMessage("家长推送应用成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppDetailDTO> result = controlPhoneService.getCommunityAppList(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
                                  @ApiParam(name = "time", required = true, value = "时间") @RequestParam("time") long time){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.addAppTimeEntry(new ObjectId(sonId),getUserId(),time);
            respObj.setMessage("家长设置应用防沉迷时间成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/acceptAppResultList")
    @ResponseBody
    public String acceptAppResultList(@ApiParam(name = "dto", required = true, value = "应用使用情况list") @RequestBody ResultAppDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.acceptAppResultList(dto,getUserId());
            respObj.setMessage("定时接受孩子的应用使用情况成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("定时接受孩子的应用使用情况失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.acceptMapResult(dto,getUserId());
            respObj.setMessage("定时接受孩子的位置信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("定时接受孩子的位置信息失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dto = controlPhoneService.getMapNow(getUserId(), new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("定时接受孩子的位置信息失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlMapDTO> dtos = controlPhoneService.getMapListEntry(getUserId(), new ObjectId(sonId), startTime, endTime);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlMapDTO> dtos = controlPhoneService.getSimpleMapListEntry(getUserId(), new ObjectId(sonId), dataTime);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("获取孩子地图信息记录失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 获取消息记录
     */
    @ApiOperation(value = "获取孩子地图信息记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSonMessage")
    @ResponseBody
    public String getSonMessage(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos= controlPhoneService.getSonMessage(getUserId(), new ObjectId(sonId),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("获取孩子地图信息记录失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos= controlPhoneService.getAllMessageForSon(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("孩子登录获取所有信息失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos= controlPhoneService.getSimpleMessageForTea(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos= controlPhoneService.getAllMessageForTea(getUserId(),new ObjectId(communityId));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("老师首页加载失败!");
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            //Map<String,Object> dtos= controlPhoneService.getSonMessage(getUserId(), new ObjectId(sonId),page,pageSize);
            //respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/addSimpleControlPhone")
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
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = controlPhoneService.addControlPhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/addSimpleCommunityAppList")
    @ResponseBody
    public String addSimpleCommunityAppList(@ApiParam(name = "code", required = true, value = "包名") @RequestParam("code") String code){
        RespObj respObj=null;
        ControlPhoneDTO dto = new ControlPhoneDTO();
        dto.setParentId("55934c26f6f28b7261c1bab0");//geng
        dto.setName(code);
        dto.setUserId("55934c26f6f28b7261c1bae1");//hao
        dto.setType(1);
        try {
            respObj = RespObj.SUCCESS;
            String result = controlPhoneService.addControlSimplePhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/getSimpleAppList")
    @ResponseBody
    public String getSimpleAppList(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = controlPhoneService.getSimpleAppList(new ObjectId("55934c26f6f28b7261c1bae1"),1);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/getSimplePhoneList")
    @ResponseBody
    public String getSimplePhoneList(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlPhoneDTO> result = controlPhoneService.getSimpleAppList2(new ObjectId("55934c26f6f28b7261c1bae1"),2);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
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
    @RequestMapping("/addSimpleMapEntry")
    @ResponseBody
    public String addSimpleMapEntry(@ApiParam(name = "longitude", required = true, value = "经度") @RequestParam("longitude") String longitude,
                                    @ApiParam(name = "latitude", required = true, value = "纬度") @RequestParam("latitude") String latitude){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            ControlMapDTO dto = new ControlMapDTO();
            dto.setUserId("55934c26f6f28b7261c1bae1");
            dto.setLongitude(longitude);
            dto.setLatitude(latitude);
            String result = controlPhoneService.addSimpleMapEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("老师推送应用失败!");
        }
        return JSON.toJSONString(respObj);
    }


}
