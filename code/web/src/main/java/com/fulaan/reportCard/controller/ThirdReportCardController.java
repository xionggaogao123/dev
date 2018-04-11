package com.fulaan.reportCard.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.reportCard.dto.*;
import com.fulaan.reportCard.service.ReportCardService;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/30.
 */
@Controller
@RequestMapping(value="/reportCard")
@Api(value = "成绩单的所有接口")
public class ThirdReportCardController extends BaseController{

    @Autowired
    private ReportCardService reportCardService;


    @ApiOperation(value = "保存考试信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveGroupExamDetail")
    @ResponseBody
    public RespObj saveGroupExamDetail(@RequestBody ExamGroupDTO examGroupDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO groupExamDetailDTO=examGroupDTO.buildDTO();
            String result = reportCardService.saveGroupExamDetail(groupExamDetailDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取老师发送的成绩单列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMySendGroupExam")
    @ResponseBody
    public RespObj getMySendGroupExam(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamDetailDTO> groupExamDetailDTOs=reportCardService.getMySendGroupExamDetailDTOs(
                   subjectId,examType,status,getUserId(),page,pageSize
            );
            int count=reportCardService.countMySendGroupExamDetailDTOs(subjectId,examType,status,getUserId());
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list",groupExamDetailDTOs);
            retMap.put("count",count);
            retMap.put("page",page);
            retMap.put("pageSize",pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @ApiOperation(value = "获取学生接收的成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentReceiveExams")
    @ResponseBody
    public RespObj getStudentReceiveExams(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamDetailDTO> groupExamDetailDTOs=reportCardService.getReceiveExams(
                    subjectId,examType,status,getUserId(),page,pageSize
            );
            int count=reportCardService.countReceiveExams(subjectId,examType,status,getUserId());
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list",groupExamDetailDTOs);
            retMap.put("count",count);
            retMap.put("page",page);
            retMap.put("pageSize",pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }




    @ApiOperation(value = "获取家长接收的成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getParentReceiveExams")
    @ResponseBody
    public RespObj getParentReceiveExams(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=reportCardService.getParentReceivedGroupExamDetailDTOs(
                    subjectId,examType,status,getUserId(),page,pageSize
            );
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "家长签字功能", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/pushSign")
    @ResponseBody
    public RespObj pushSign(@ObjectIdType ObjectId groupExamDetailId,
                            @ObjectIdType ObjectId userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.pushSign(groupExamDetailId,userId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("签字成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "删除成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除成绩单已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/removeGroupExam")
    @ResponseBody
    public RespObj removeGroupExam(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.removeGroupExamDetailEntry(groupExamDetailId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成绩单成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "发送成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除成绩单已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/sendGroupExam")
    @ResponseBody
    public RespObj sendGroupExam(@ObjectIdType ObjectId groupExamDetailId, String showType){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.sendGroupExam(groupExamDetailId, showType);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("发送成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "查询录入成绩的学生名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchRecordStudentScores")
    @ResponseBody
    public RespObj searchRecordStudentScores(@ObjectIdType ObjectId examGroupDetailId,
                                             @RequestParam(required = false,defaultValue = "-1")int score,
                                             @RequestParam(required = false,defaultValue = "-1")int scoreLevel,
                                             @RequestParam(required = false,defaultValue = "1")int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamUserRecordDTO> examScoreDTOs=reportCardService.searchRecordStudentScores(examGroupDetailId,score,scoreLevel,type);
            respObj.setMessage(examScoreDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取该考试的版本号", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getExamGroupVersion")
    @ResponseBody
    public RespObj getExamGroupVersion(@ObjectIdType ObjectId examGroupDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamVersionDTO groupExamVersionDTO=reportCardService.getExamGroupVersion(examGroupDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(groupExamVersionDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "保存或编辑成绩列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveRecordExamScore")
    @ResponseBody
    public RespObj saveRecordExamScore(@RequestBody ExamGroupScoreDTO examGroupScoreDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamVersionDTO groupExamVersionDTO=reportCardService.getExamGroupVersion(
                    new ObjectId(examGroupScoreDTO.getGroupExamDetailId()));
            if(groupExamVersionDTO.getVersion()!=examGroupScoreDTO.getVersion()) {
                List<GroupExamUserRecordDTO> examScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
                for (ExamGroupUserScoreDTO userScoreDTO : examGroupScoreDTO.getExamGroupUserScoreDTOs()) {
                    examScoreDTOs.add(userScoreDTO.buildDTO());
                }
                reportCardService.saveRecordExamScore(examScoreDTOs, examGroupScoreDTO.getStatus(), examGroupScoreDTO.getIsSend());
                reportCardService.updateVersion(new ObjectId(examGroupScoreDTO.getGroupExamDetailId()),
                        examGroupScoreDTO.getVersion());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("保存或编辑成绩成功!");
            }else{
                respObj.setErrorMessage("不是最新的版本");
            }
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询详情信息
     * @param groupExamDetailId
     * @return
     */
    @ApiOperation(value = "获取该条成绩单的详情信息(针对老师)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getTeacherGroupExamDetail")
    @ResponseBody
    public RespObj getTeacherGroupExamDetail(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO detailDTO=reportCardService.
                    getTeacherGroupExamDetail(groupExamDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询详情信息
     * @param singleId
     * @return
     */
    @ApiOperation(value = "获取该条成绩单的详情信息(针对家长或学生)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getGroupExamDetail")
    @ResponseBody
    public RespObj getGroupExamDetail(@ObjectIdType ObjectId singleId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO detailDTO=reportCardService.
                    getGroupExamDetail(singleId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取所有的考试类型", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getExamTypeList")
    @ResponseBody
    public RespObj getExamTypeList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<ExamTypeDTO> examTypeDTOs=reportCardService.getExamTypeDTOs();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(examTypeDTOs);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @ApiOperation(value = "导出模板", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportTemplate/{examGroupDetailId}")
    @ResponseBody
    public void exportTemplate(@PathVariable @ObjectIdType ObjectId examGroupDetailId,
                               HttpServletResponse response,
                               HttpServletRequest request){
        try {
            reportCardService.exportTemplate(request,examGroupDetailId,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "导入模板", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "导入模板已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/importTemplate")
    @ResponseBody
    public RespObj importTemplate(HttpServletRequest request)throws Exception{
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        String groupExamId = request.getParameter("groupExamId");
        MultipartRequest multipartRequest = (MultipartRequest) request;
        try {
            MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
            for (List<MultipartFile> multipartFiles : fileMap.values()) {
                for(MultipartFile file:multipartFiles) {
                    System.out.println("----" + file.getOriginalFilename());
                    reportCardService.importTemplate(groupExamId,file.getInputStream());
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

    @ApiOperation(value = "导出模板", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportUserControl/{communityId}")
    @ResponseBody
    public void exportUserControl(@PathVariable @ObjectIdType ObjectId communityId,
                                  HttpServletResponse response,
                                  HttpServletRequest request){
        try {
            reportCardService.exportUserControl(request,communityId,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "导入模板", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "导入模板已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/importUserControl")
    @ResponseBody
    public RespObj importUserControl(MultipartRequest request)throws Exception{
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            MultiValueMap<String, MultipartFile> fileMap = request.getMultiFileMap();
            for (List<MultipartFile> multipartFiles : fileMap.values()) {
                for(MultipartFile file:multipartFiles) {
                    System.out.println("----" + file.getOriginalFilename());
                    String fileName=file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
                    reportCardService.importUserControl(file.getInputStream(),fileName);
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
