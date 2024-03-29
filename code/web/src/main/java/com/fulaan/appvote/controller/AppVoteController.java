package com.fulaan.appvote.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.appvote.dto.AppVoteDTO;
import com.fulaan.appvote.service.AppVoteService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by scott on 2017/11/7.
 */
@Controller
@RequestMapping("/jxmapi/appVote")
public class AppVoteController extends BaseController{

    @Autowired
    private AppVoteService appVoteService;

    @ApiOperation(value = "保存投票记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveAppVote")
    @ResponseBody
    public RespObj saveAppVote(@RequestBody AppVoteDTO appVoteDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appVoteDTO.setUserId(getUserId().toString());
            String score = appVoteService.saveAppVote(appVoteDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(score);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 投票数据迁移
     * @return
     */
    @ApiOperation(value = "投票数据迁移", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/transferVote")
    @ResponseBody
    public RespObj transferVote(){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        appVoteService.transferVote();
        respObj.setMessage("投票数据迁移成功");
        return respObj;
    }

    /**
     * 活动数据迁移
     * @return
     */
    @ApiOperation(value = "活动数据迁移", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/transferActivity")
    @ResponseBody
    public RespObj transferActivity(){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        appVoteService.transferActivity();
        respObj.setMessage("活动数据迁移成功");
        return respObj;
    }


    @ApiOperation(value = "获取集合投票列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/gatherAppVotes")
    @ResponseBody
    public RespObj gatherAppVotes(@RequestParam(required = false,defaultValue = "1")int page,
                                  @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appVoteService.gatherAppVotes(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "删除并撤回投票信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeAppVote")
    @ResponseBody
    public RespObj removeAppVote(@ObjectIdType ObjectId appVoteId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        appVoteService.removeAppVote(appVoteId);
        respObj.setMessage("删除并撤回投票信息成功!");
        return respObj;
    }


    @ApiOperation(value = "获取我发送的投票", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMySendAppVote")
    @ResponseBody
    public RespObj getMySendAppVote(@RequestParam(required = false,defaultValue = "1")int page,
                                    @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appVoteService.getMySendAppVote(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "获取一个投票详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getOneAppVote")
    @ResponseBody
    public RespObj getOneAppVote(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            AppVoteDTO dto=appVoteService.getOneAppVote(getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dto);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }




    @ApiOperation(value = "获取我收到的投票", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMyReceivedAppVote")
    @ResponseBody
    public RespObj getMyReceivedAppVote(@RequestParam(required = false,defaultValue = "1")int page,
                                        @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appVoteService.getMyReceivedAppVote(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "获取学生收到的投票", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getStudentReceivedAppVotes")
    @ResponseBody
    public RespObj getStudentReceivedAppVotes(@RequestParam(required = false,defaultValue = "1")int page,
                                        @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appVoteService.getStudentReceivedAppVotes(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
