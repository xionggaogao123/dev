package com.fulaan.appvote.controller;

import com.fulaan.appvote.dto.AppVoteDTO;
import com.fulaan.appvote.service.AppVoteService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
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
public class AppVoteController extends BaseController{

    @Autowired
    private AppVoteService appVoteService;


    @RequestMapping("/saveAppVote")
    @ResponseBody
    public RespObj saveAppVote(@RequestBody AppVoteDTO appVoteDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appVoteService.saveAppVote(appVoteDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存成功");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

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
