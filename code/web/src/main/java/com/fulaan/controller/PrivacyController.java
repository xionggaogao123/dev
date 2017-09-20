package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jerry on 2016/9/22.
 * 关于我们
 */
@Api(value="关于我们")
@Controller
public class PrivacyController extends BaseController {

    @ApiOperation(value = "aboutUs", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/aboutus/k6kt")
    @SessionNeedless
    public String aboutUs() {
        return "/privacy/aboutUs";
    }

    @ApiOperation(value = "service", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/service/k6kt")
    @SessionNeedless
    public String service() {
        return "/privacy/service";
    }
    @ApiOperation(value = "privacy", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/privacy/k6kt")
    @SessionNeedless
    public String privacy() {
        return "/privacy/privacy";
    }
    @ApiOperation(value = "contactus", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/contactus/k6kt")
    @SessionNeedless
    public String contactus() {
        return "/privacy/contact";
    }
}
