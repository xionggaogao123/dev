package com.fulaan.count.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fulaan.base.BaseController;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.count.dto.TztbDto;
import com.fulaan.count.dto.XktDto;
import com.fulaan.count.dto.XqstDto;
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
    
    public RespObj jxmCount(String schooleId, String grade) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            JxmCountDto jxmCountDto = countService.jxmCount(schooleId, grade);
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
            List<SubjectClassDTO> list = countService.getSubjectClass();
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("查询学科失败!");

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
    public RespObj tczyList(String subjectId, String schooleId, String seTime, int page, int pageSize) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            Map<String, Object> l = countService.tczy(subjectId, schooleId, startTime,endTime,  page, pageSize);
            respObj.setMessage(l);
        } catch (Exception e) {
            Map<String, Object> l = countService.tczy(subjectId, schooleId, null,null,  page, pageSize);
            respObj.setMessage(l);
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "作业发布统计按班级", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/bjzyList")
    @ResponseBody
    public RespObj bjzyList(String communityId, String schooleId, String seTime, int page, int pageSize,  String grade) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            Map<String, Object> l = countService.bjzy(communityId,grade, schooleId, startTime,endTime,  page,  pageSize);
            respObj.setMessage(l);
        } catch (Exception e) {
           
            Map<String, Object> l = countService.bjzy(communityId,grade, schooleId, null,null,  page,  pageSize);
            respObj.setMessage(l);
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "通知统计图表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/tztb")
    @ResponseBody
    public RespObj tztb(String schooleId, String seTime) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        TztbDto l = new TztbDto();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.tztb(schooleId, startTime,endTime);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.tztb(schooleId, null,null);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "老师发布通知次数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/tzsub")
    @ResponseBody
    public RespObj tzsub(String subjectId, String schooleId, String seTime , int page, int pageSize) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            Map<String, Object> map = countService.tzsub(subjectId, schooleId,startTime,endTime, page,  pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            
            
            Map<String, Object> map = countService.tzsub(subjectId, schooleId,null,null,  page,  pageSize);
            respObj.setMessage(map);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "班级通知统计", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/tzcom")
    @ResponseBody
    public RespObj tzcom(String communityId, String schooleId, String seTime, int page, int pageSize,  String grade) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            Map<String, Object> map = countService.tzcom(communityId, grade,schooleId, startTime,endTime, page,  pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            
            
            Map<String, Object> map = countService.tzcom(communityId, grade, schooleId,null,null,  page,  pageSize);
            respObj.setMessage(map);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "兴趣社团图表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/xqsttb")
    @ResponseBody
    public RespObj xqsttb(String schooleId, String seTime) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        XqstDto l = new XqstDto();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.xqsttb(schooleId, startTime,endTime);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.xqsttb(schooleId, null,null);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "帖子发布数据统计", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/xqsttj")
    @ResponseBody
    public RespObj xqsttj(String schooleId, String seTime,int page, int pageSize) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> l = new HashMap<String, Object>();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.xqsttj(schooleId, startTime,endTime, page,  pageSize);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.xqsttj(schooleId, null,null, page, pageSize);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    
    @ApiOperation(value = "小课堂图表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/xkttb")
    @ResponseBody
    public RespObj xkttb(String schooleId, String seTime) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        XktDto l = new XktDto();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.xkttb(schooleId, startTime,endTime);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.xkttb(schooleId, null,null);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "小课堂班级统计", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/xktbj")
    @ResponseBody
    public RespObj xktbj(String schooleId, String seTime,int page, int pageSize, String grade) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> l = new HashMap<String, Object>();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.xktbj(schooleId,grade, startTime,endTime, page,  pageSize);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.xktbj(schooleId, grade,null,null, page,  pageSize);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "小课堂教师统计", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/xktjs")
    @ResponseBody
    public RespObj xktjs(String schooleId, String seTime,int page, int pageSize, String grade) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> l = new HashMap<String, Object>();
        try {
            
            String startTime = null;
            String endTime = null;
            List<String> list = JSON.parseObject(seTime, new TypeReference<List<String>>() {});
            startTime = list.get(0);
            endTime = list.get(1);
            
            l = countService.xktjs(schooleId, grade,startTime,endTime, page, pageSize);
            respObj.setMessage(l);
        } catch (Exception e) {
            
            try {
                l = countService.xktjs(schooleId,grade, null,null, page,  pageSize);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            respObj.setMessage(l);
      
        }
        
        return respObj;
    }
    
    @RequestMapping("/getDefaultDate")
    @ResponseBody
    public RespObj getDefaultDate() {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        Map<Integer, String> map = this.getTimePastSev();
        List<String> list = new ArrayList<String>();
        list.add(map.get(Constant.ZERO));
        list.add(map.get(Constant.ONE));
        respObj.setMessage(list);
        return respObj;
        
    }
    
    public Map<Integer, String> getTimePastSev() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        Date end = new Date();
        Long startTime = end.getTime() -  3600 * 1000 * 24 * 7;
        Date start = new Date(startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        map.put(0, startStr);
        map.put(1, endStr);
        return map;
    }
}
