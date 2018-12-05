package com.fulaan.count.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fulaan.base.BaseController;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.count.dto.TczyDto;
import com.fulaan.count.dto.ZytbDto;
import com.fulaan.count.service.CountService;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.operation.service.AppCommentService;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="后台统计")
@Controller
@RequestMapping("/web/count")
public class CountController extends BaseController {

    @Autowired
    private CountService countService;
    
    @Autowired
    private AppCommentService appCommentService;
    
    
    @ApiOperation(value = "获取学校列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleSchoolList")
    @ResponseBody
    public RespObj getSimpleSchoolList() {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            List<HomeSchoolDTO> list = countService.getSimpleSchoolList();
            respObj.setMessage(list);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "获取家校美统计详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/jxmCount")
    @ResponseBody
    
    public RespObj jxmCount(String schooleId) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            JxmCountDto jxmCountDto = countService.jxmCount(schooleId);
            respObj.setMessage(jxmCountDto);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
        }
        
        return respObj;
    }
    
    /**
     * 查询老师绑定的学科
     */
    @ApiOperation(value="查询老师绑定的学科",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectTeacherSubjectList")
    @ResponseBody
    public String selectTeacherSubjectList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<SubjectClassDTO> result = appCommentService.selectTeacherSubjectList(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("查询老师绑定的学科失败!");

        }
        return JSON.toJSONString(respObj);

    }
    
    @ApiOperation(value = "作业发布统计图表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/zytb")
    @ResponseBody
    public RespObj zytb(String seTime, String schooleId) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            String startTime = null;
            String endTime = null;
            
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            ZytbDto zytbDto = countService.zytb(schooleId, startTime, endTime);
            respObj.setMessage(zytbDto);
        } catch (Exception e) {
            ZytbDto zytbDto = countService.zytb(schooleId, null,null);
            respObj.setMessage(zytbDto);
            
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "作业发布统计按学科", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/tczyList")
    @ResponseBody
    public RespObj tczyList(String subjectId, String schooleId) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            List<TczyDto> l = countService.tczy(subjectId, schooleId);
            respObj.setMessage(l);
        } catch (Exception e) {
           
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("失败!");
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "作业发布统计按班级", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/bjzyList")
    @ResponseBody
    public RespObj bjzyList(String communityId, String schooleId) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            List<TczyDto> l = countService.bjzy(communityId, schooleId);
            respObj.setMessage(l);
        } catch (Exception e) {
           
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("失败!");
        }
        
        return respObj;
    }
}
