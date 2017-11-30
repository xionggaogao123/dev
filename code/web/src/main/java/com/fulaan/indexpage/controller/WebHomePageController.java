package com.fulaan.indexpage.controller;

import com.fulaan.base.BaseController;
import com.fulaan.indexpage.service.WebHomePageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by scott on 2017/11/18.
 */
@Controller
@RequestMapping("/web/webHome")
public class WebHomePageController extends BaseController{

    @Autowired
    private WebHomePageService webHomePageService;

    /**
     *
     * @param page
     * @param pageSize
     * @param mode
     * @param type
     * @param subjectId
     * @param status
     * @param startTime
     * @param endTime
     * @param communityId
     * @return
     */
    @RequestMapping("/getMySendHomeEntries")
    @ResponseBody
    public RespObj getMySendHomeEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "0")int mode,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "-1")int status,
            @RequestParam(required = false,defaultValue = "")String startTime,
            @RequestParam(required = false,defaultValue = "")String endTime,
            @RequestParam(required = false,defaultValue = "")String communityId

    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getMySendHomePageEntries(getUserId(),type, subjectId, mode,
                    status, startTime, endTime, communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param page
     * @param pageSize
     * @param mode
     * @param type
     * @param subjectId
     * @param status
     * @param startTime
     * @param endTime
     * @param communityId
     * @return
     */
    @RequestMapping("/getMyReceiveHomeEntries")
    @ResponseBody
    public RespObj getMyReceiveHomeEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "0")int mode,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "-1")int status,
            @RequestParam(required = false,defaultValue = "")String startTime,
            @RequestParam(required = false,defaultValue = "")String endTime,
            @RequestParam(required = false,defaultValue = "")String communityId

    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getMyReceivedEntries(getUserId(),type, subjectId, mode,
                    status, startTime, endTime, communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param page
     * @param pageSize
     * @param type
     * @param subjectId
     * @param communityId
     * @return
     */
    @RequestMapping("/getGatherEntries")
    @ResponseBody
    public RespObj getGatherEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "")String communityId
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getGatherEntries(getUserId(),type,subjectId,communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



}
