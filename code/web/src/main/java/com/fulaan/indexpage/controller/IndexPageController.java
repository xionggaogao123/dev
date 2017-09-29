package com.fulaan.indexpage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by James on 2017/9/28.
 */
@Controller
@RequestMapping("/pageIndex")
@Api(value="大人端首页加载")
public class IndexPageController extends BaseController {

    @ApiOperation(value = "修改课程名", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getIndexList")
    @ResponseBody
    public String getIndexList(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
           // smallLessonService.updateLessonName(new ObjectId(lessonId), name);
            respObj.setMessage("修改课程名成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改课程名失败");
        }
        return JSON.toJSONString(respObj);
    }

}
