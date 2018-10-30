package com.fulaan.appvote.controller;

import com.fulaan.appvote.dto.AppNewVoteDTO;
import com.fulaan.appvote.service.AppNewVoteService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by James on 2018-10-29.
 */
@Api(value="新投票")
@Controller
@RequestMapping("/jxmapi/appnewvote")
public class AppNewVoteController extends BaseController {
    @Autowired
    private AppNewVoteService appNewVoteService;


    @ApiOperation(value = "保存投票记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveNewAppVote")
    @ResponseBody
    public RespObj saveNewAppVote(@RequestBody AppNewVoteDTO appNewVoteDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteDTO.setUserId(getUserId().toString());
            String score = appNewVoteService.saveNewAppVote(appNewVoteDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(score);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
