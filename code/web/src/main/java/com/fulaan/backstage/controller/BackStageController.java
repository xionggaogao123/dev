package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.appmarket.service.AppMarketService;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
            respObj.setCode(Constant.FAILD_CODE);
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
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置学生端应用回调时间失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 后台设置常用电话
     *
     */
    @ApiOperation(value = "后台设置常用电话", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addPhoneEntry")
    @ResponseBody
    public String addPhoneEntry(@ApiParam(name = "name", required = true, value = "名称") @RequestParam("name") String name,
                                @ApiParam(name = "phone", required = true, value = "电话") @RequestParam("phone") String phone){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageService.addPhoneEntry(name, phone);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置常用电话失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 后台设置默认管控时间选择表
     *
     */
    @ApiOperation(value = "后台设置默认管控时间选择表", httpMethod = "POST", produces = "application/json")
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
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台设置默认管控时间选择表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 设置默认常规管控时间
     * @return
     */
    @ApiOperation(value = "后台设置默认管控时间选择表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSchoolTime")
    @ResponseBody
    public String addSchoolTime(@ApiParam(name = "startTime", required = true, value = "开始时间") @RequestParam("startTime") String startTime,
                                @ApiParam(name = "endTime", required = true, value = "结束时间") @RequestParam("endTime") String endTime,
                                @ApiParam(name = "week", required = true, value = "星期") @RequestParam("week") int week){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            backStageService.addSchoolTime(startTime, endTime, week);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设置成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setErrorMessage("设置失败");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 设置默认特殊管控时间
     * @return
     */
    @ApiOperation(value = "后台设置默认管控时间选择表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addOtherSchoolTime")
    @ResponseBody
    public String addOtherSchoolTime(@ApiParam(name = "startTime", required = true, value = "开始时间") @RequestParam("startTime") String startTime,
                                @ApiParam(name = "endTime", required = true, value = "结束时间") @RequestParam("endTime") String endTime,
                                @ApiParam(name = "dateTime", required = true, value = "日期") @RequestParam("dateTime") String dateTime){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            backStageService.addOtherSchoolTime(startTime,endTime,dateTime);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设置成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设置失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 内容管理显示
     * @return
     */
    @ApiOperation(value = "内容管理显示", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectContentList")
    @ResponseBody
    public String selectContentList(@ApiParam(name = "isCheck", required = true, value = "是否审核") @RequestParam("isCheck") int isCheck,
                                     @ApiParam(name = "id", required = true, value = "数据id") @RequestParam(value = "id",defaultValue = "") String id,
                                     @ApiParam(name = "page", required = true, value = "页数") @RequestParam("page") int page,
                                     @ApiParam(name = "pageSize", required = true, value = "每条记录数") @RequestParam("pageSize") int pageSize){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map =  backStageService.selectContentList(isCheck,id,page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("内容管理显示失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 通过图片
     * @return
     */
    @ApiOperation(value = "通过图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/passContentEntry")
    @ResponseBody
    public String passContentEntry(@ApiParam(name = "id", required = true, value = "数据id") @RequestParam(value = "id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            backStageService.passContentEntry(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("通过成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("通过失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 删除
     * @return
     */
    @ApiOperation(value = "通过图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteContentEntry")
    @ResponseBody
    public String deleteContentEntry(@ApiParam(name = "id", required = true, value = "数据id") @RequestParam(value = "id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            backStageService.deleteContentEntry(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("通过成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("通过失败");
        }
        return JSON.toJSONString(respObj);
    }

    @RequestMapping("/setOrder/{apkId}/{order}")
    @ResponseBody
    public RespObj setOrder(@PathVariable @ObjectIdType ObjectId apkId,
                           @PathVariable int order){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        appMarketService.setOrder(apkId,order);
        respObj.setMessage("设置顺序成功");
        return respObj;
    }


    @RequestMapping("/deleteApk/{apkId}")
    @ResponseBody
    public RespObj deleteApk(@PathVariable @ObjectIdType ObjectId apkId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appMarketService.deleteApk(apkId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除apk文件成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "导入apk", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
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
