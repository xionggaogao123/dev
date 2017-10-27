package com.fulaan.questionbook.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.questionbook.dto.QuestionAdditionDTO;
import com.fulaan.questionbook.dto.QuestionBookDTO;
import com.fulaan.questionbook.service.QuestionBookService;
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
 * Created by James on 2017/9/30.
 */
@Api(value="错题本2.0")
@Controller
@RequestMapping("/questionBook")
public class QuestionBookController extends BaseController {
    @Autowired
    private QuestionBookService questionBookService;

    /**
     *  添加错题
     * @param dto
     * @param answerContent
     * @param answerList
     * @param analysisContent
     * @param analysisList
     * @return
     */
    @ApiOperation(value = "添加错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addQuestionBookEntry")
    @ResponseBody
    public String addQuestionBookEntry(@ApiParam(name="dto",required = true,value="作业对象")  QuestionBookDTO dto,
                                       @ApiParam(name="answerContent",required = true,value="答案内容") @RequestParam(value="answerContent",defaultValue = "") String answerContent,
                                       @ApiParam(name="answerList",required = true,value="答案图片") @RequestParam(value="answerList",defaultValue = "") List<String> answerList,
                                       @ApiParam(name="analysisContent",required = true,value="解析内容") @RequestParam(value="analysisContent",defaultValue = "") String analysisContent,
                                       @ApiParam(name="analysisList",required = true,value="解析图片") @RequestParam(value="analysisList",defaultValue = "") List<String> analysisList){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            String str = questionBookService.addQuestionBookEntry(dto,answerContent,answerList,analysisContent,analysisList);
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加错题失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  添加新错题
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加新错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addQuestionNewBookEntry")
    @ResponseBody
    public String addQuestionNewBookEntry(@ApiParam(name="dto",required = true,value="作业对象")  QuestionBookDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            String str = questionBookService.addQuestionNewBookEntry(dto);
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加错题失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  修改错题展示时间（1.7.30）
     * @return
     */
    @ApiOperation(value = "修改错题展示时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateQuestionBook")
    @ResponseBody
    public String updateQuestionBook(@ApiParam(name="questionId",required = true,value="错题id") @RequestParam(value="questionId") String questionId,
                                       @ApiParam(name="type",required = true,value="点击类型 1 我还是不会 2 我已经会了") @RequestParam(value="type") int type,
                                       @ApiParam(name="level",required = true,value="处于阶段") @RequestParam(value="level") int level){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            questionBookService.updateQuestionBook(new ObjectId(questionId),type,level);
            respObj.setMessage("修改错题成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改错题失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  删除已学会的错题
     * @return
     */
    @ApiOperation(value = "删除已学会的错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/delAlreadyEntry")
    @ResponseBody
    public String delAlreadyEntry(@ApiParam(name="questionId",required = true,value="错题id") @RequestParam(value="questionId") String questionId){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            questionBookService.delAlreadyEntry(new ObjectId(questionId));
            respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除错题失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 多条件组合查询列表
     */
    @ApiOperation(value = "多条件组合查询列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getQuestionList")
    @ResponseBody
    public String getQuestionList(@ApiParam(name="gradeId",required = false,value="年级id") @RequestParam(value="gradeId",defaultValue = "") String gradeId,
                                  @ApiParam(name="subjectId",required = false,value="学科id") @RequestParam(value="subjectId",defaultValue = "") String subjectId,
                                  @ApiParam(name="questionTypeId",required = false,value="错题类型id") @RequestParam(value="questionTypeId",defaultValue = "") String questionTypeId,
                                  @ApiParam(name="testId",required = false,value="测试类型") @RequestParam(value="testId",defaultValue = "") String testId,
                                  @ApiParam(name="type",required = true,value="是否学会") @RequestParam(value="type",defaultValue = "1") int type,
                                  @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                  @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize,
                                  @ApiParam(name="keyword",required = false,value="关键字") @RequestParam(value="keyword",defaultValue = "") String keyword){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = questionBookService.getQuestionList(gradeId, subjectId, questionTypeId, testId, type, page, pageSize, keyword,getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("多条件组合查询列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 今日复习
     */
    @ApiOperation(value = "今日复习", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getReviewList")
    @ResponseBody
    public String getReviewList(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<QuestionBookDTO> dtos = questionBookService.getReviewList(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("今日复习失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     *  查询单个错题
     */
    @ApiOperation(value = "查询单个错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getEntry")
    @ResponseBody
    public String getEntry(@ApiParam(name="id",required = false,value="错题id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = questionBookService.getEntry(new ObjectId(id));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查询单个错题失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  添加解析/答案/回答
     */
    @ApiOperation(value = "添加解析/答案/回答", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addAnswerEntry")
    @ResponseBody
    public String addAnswerEntry(@ApiParam(name="dto",required = true,value="解析对象") @RequestBody QuestionAdditionDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            questionBookService.addAnswerEntry(dto);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 修改错题
     */
    @ApiOperation(value = "修改错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateEntry")
    @ResponseBody
    public String updateEntry(@ApiParam(name="dto",required = true,value="解析对象") @RequestBody QuestionBookDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            questionBookService.updateEntry(dto);
            respObj.setMessage("修改错题成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改错题失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 修改单个解析/答案/回答
     */
    @ApiOperation(value = "修改解析/答案/回答", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateAnswerEntry")
    @ResponseBody
    public String updateAnswerEntry(@ApiParam(name="dto",required = true,value="解析对象") @RequestBody QuestionAdditionDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            questionBookService.updateAnswerEntry(dto);
            respObj.setMessage("修改解析/答案/回答成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改解析/答案/回答失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 我又不会了
     */
    @ApiOperation(value = "我又不会了", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/changeEntryState")
    @ResponseBody
    public String changeEntryState(@ApiParam(name="id",required = true,value="错题id") @RequestParam(value = "id",required = true) String id){
        RespObj respObj = null;
        try{
            respObj = RespObj.SUCCESS;
            questionBookService.changeEntryState(new ObjectId(id));
            respObj.setMessage("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("我又不会了失败!");
        }
        return JSON.toJSONString(respObj);
    }

}
