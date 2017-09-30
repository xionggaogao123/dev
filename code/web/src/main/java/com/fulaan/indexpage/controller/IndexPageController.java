package com.fulaan.indexpage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.indexpage.service.IndexPageService;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/28.
 */
@Controller
@RequestMapping("/pageIndex")
@Api(value="大人端首页加载")
public class IndexPageController extends BaseController {
    @Autowired
    private IndexPageService indexPageService;

    @ApiOperation(value = "修改课程名", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getIndexList")
    @ResponseBody
    public String getIndexList(@ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                               @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<Map<String,Object>> mlist =  indexPageService.getIndexList(getUserId(), page, pageSize);
            respObj.setMessage(mlist);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改课程名失败");
        }
        return JSON.toJSONString(respObj);
    }

}
