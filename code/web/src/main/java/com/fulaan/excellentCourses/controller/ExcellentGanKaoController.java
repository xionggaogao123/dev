package com.fulaan.excellentCourses.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.excellentCourses.service.ExcellentGanKaoService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2018-09-13.
 *  赶考网流程
 */
@Api(value="赶考网直播课程")
@Controller
@RequestMapping("/jxmapi/gankao")
public class ExcellentGanKaoController extends BaseController {
    @Autowired
    private ExcellentGanKaoService excellentGanKaoService;

    /**
     *  赶考网去上课
     */
    @ApiOperation(value = "赶考网去上课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/gotoNewClass")
    @ResponseBody
    public String gotoNewClass(@ApiParam(name = "id", required = false, value = "id") @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dto = excellentGanKaoService.gotoNewClass(new ObjectId(id), getUserId());
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

}
