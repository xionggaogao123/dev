package com.fulaan.reportCard.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.indexpage.service.WebHomePageService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.reportCard.dto.*;
import com.fulaan.reportCard.service.ReportCardService;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.pojo.integral.IntegralType;
import com.pojo.reportCard.GroupExamDetailEntry;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/30.
 */
@Controller
@RequestMapping(value="/jxmapi/reportCard")
@Api(value = "成绩单的所有接口")
public class DefaultReportCardController extends BaseController{

    @Autowired
    private ReportCardService reportCardService;

    @Autowired
    private WebHomePageService webHomePageService;

    private IntegralSufferService integralSufferService = new IntegralSufferService();



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
            String result = new ObjectId().toString();
            //先判断是否绑定了学生
            if(reportCardService.isHaveRecordEntries(groupExamDetailDTO.getCommunityId())) {
                result = reportCardService.saveGroupExamDetail(groupExamDetailDTO,getUserId());
            }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "判断有没有成绩单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/isHaveGroupExam")
    @ResponseBody
    public RespObj isHaveGroupExam(@ObjectIdType ObjectId examGroupDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailEntry  g = reportCardService.isHaveGroupExam(examGroupDetailId);
            respObj.setMessage(g);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
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
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param groupExamDetailId
     * @param userId
     * @return
     */
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
            respObj.setMessage("0");
            int score = integralSufferService.addIntegral(getUserId(), IntegralType.repordcard,3,2);
            respObj.setMessage(score + "");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @param groupExamDetailId
     * @return
     */
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
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @param groupExamDetailId
     * @return
     */
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
            //保存展示类型 个人或者全班
            //reportCardService.updateShowType(groupExamDetailId, showType);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("0");
            int score = integralSufferService.addIntegral(getUserId(), IntegralType.repordcard,1,1);
            respObj.setMessage(""+score);
        }catch (Exception e){
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
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "查询录入成绩的学生名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchRecordStudentScoresStr")
    @ResponseBody
    public RespObj searchRecordStudentScoresStr(@ObjectIdType ObjectId examGroupDetailId,
                                             @RequestParam(required = false,defaultValue = "-1")int score,
                                             @RequestParam(required = false,defaultValue = "-1")int scoreLevel,
                                             @RequestParam(required = false,defaultValue = "1")int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamUserRecordDTO> examScoreDTOs=reportCardService.searchRecordStudentScores(examGroupDetailId,score,scoreLevel,type);
            respObj.setMessage(this.trans(examScoreDTOs));
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    public List<GroupExamUserRecordStrDTO> trans(List<GroupExamUserRecordDTO> list) {
        List<GroupExamUserRecordStrDTO> l = new ArrayList<GroupExamUserRecordStrDTO>();
        for (GroupExamUserRecordDTO g : list) {
            GroupExamUserRecordStrDTO gs = new GroupExamUserRecordStrDTO(g);
            l.add(gs);
        }
        return l;
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
                reportCardService.updateShowType(new ObjectId(examGroupScoreDTO.getGroupExamDetailId()), examGroupScoreDTO.getShowType());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("0");
                if(examGroupScoreDTO.getIsSend()==0 && examGroupScoreDTO.getStatus()==2){
                    int score = integralSufferService.addIntegral(getUserId(), IntegralType.repordcard,1,1);
                    respObj.setMessage(""+score);
                }
            }else{
                respObj.setErrorMessage("不是最新的版本");
            }
        }catch (Exception e){
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
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "获取成绩和排行", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getGroupExamUserRecord")
    @ResponseBody
    public RespObj getGroupExamUserRecord(@ObjectIdType ObjectId groupExamDetailId) {
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
          
            List<GroupExamUserRecordDTO> detailDTO = reportCardService.getGroupExamUserRecord(groupExamDetailId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @return
     */
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
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param subjectId
     * @param examType
     * @param status
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "集合成绩单的列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/gatherReportCardList")
    @ResponseBody
    public RespObj gatherReportCardList(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap= webHomePageService.gatherReportCardList(
                    subjectId,examType,status,getUserId(),page,pageSize
            );
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "获取成绩单的签字和未签字列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchReportCardSignList")
    @ResponseBody
    public RespObj searchReportCardSignList(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String,Object> result=reportCardService.searchReportCardSignList(groupExamDetailId);
        respObj.setMessage(result);
        return respObj;
    }


    @ApiOperation(value = "app端判断是否需要上传学生并分配", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/judgeIsExistMatch")
    @ResponseBody
    public RespObj judgeIsExistMatch(@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            int flag=reportCardService.judgeIsExistMatch(communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(flag);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



}
