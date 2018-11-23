package com.fulaan.backstage.controller;

import com.fulaan.backstage.service.BackStageOutStorageFollowService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年10月11日15:27:24
 */
@Api(value = "后台管理类-出库跟踪")
@Controller
@RequestMapping("/jxmapi/backstageoutstoragefollow")
public class DefaultOutStorageFollowController extends BaseController {
    @Autowired
    private BackStageOutStorageFollowService backStageOutStorageFollowService;



    /**
     * 出库跟踪-注册绑定IMEI和账号
     * @param mobile
     * @param imeiNo
     * @return
     */
    @ApiOperation(value = "出库跟踪-注册绑定IMEI和账号", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateOutStorageFollowUserInfo")
    @ResponseBody
    public RespObj updateOutStorageFollowUserInfo(@RequestParam("mobile") String mobile,
                                                  @RequestParam("imeiNo") String imeiNo) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageOutStorageFollowService.updateOutStorageFollowUserInfo(mobile, imeiNo);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
