package com.fulaan.indexpage.controller;

import com.fulaan.indexpage.service.WebHomePageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by scott on 2017/11/18.
 */
@Controller
public class WebHomePageController {

    @Autowired
    private WebHomePageService webHomePageService;

    @RequestMapping("/getMySendHomeEntries")
    @ResponseBody
    public RespObj getMySendHomeEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "1")int mode

    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{

        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }

        return respObj;
    }

}
