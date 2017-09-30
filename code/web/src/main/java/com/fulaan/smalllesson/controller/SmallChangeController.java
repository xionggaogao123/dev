package com.fulaan.smalllesson.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by James on 2017/9/30.
 */
@Controller
@RequestMapping("/changeLesson")
@Api(value="跳转页面")
public class SmallChangeController extends BaseController {

    @ApiOperation(value = "注册界面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/index")
    @SessionNeedless
    public String index(@ApiParam(name="userId",required = true,value="用户id") @RequestParam(value="userId") String userId,
                        @ApiParam(name="userName",required = true,value="用户姓名") @RequestParam(value="userName") String userName,
                        Map<String,Object> model){
        model.put("userId",userId);
        model.put("userName",userName);
        return "/fulanlesson/index";
    }



}
