package com.fulaan.testDomainHandle.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.testDomainHandle.service.TestDomainHandleService;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/testDomainHandle")
public class TestDomainHandleController extends BaseController {
    private static final Logger logger = Logger.getLogger(TestDomainHandleController.class);

    @Autowired
    private TestDomainHandleService testDomainHandleService;

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("/homePage")
    public String homePage(Map<String,Object> model) {
        return "testDomainHandle/homePage";
    }

    @ResponseBody
    @RequestMapping(value = "/handleTableTestDomain")
    public String handleTableTestDomain(
            @RequestParam String dbName,
            @RequestParam String tableName,
            @RequestParam String selField,
            @RequestParam String childField
    ) {
        RespObj resultObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            logger.debug("七牛-测试域名替换处理:");
            logger.debug("时间："+ DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            logger.debug("处理人："+getSessionValue().getUserName());
            logger.debug("处理人Id："+getUserId().toString());
            testDomainHandleService.handleTableTestDomain(dbName, tableName, selField, childField);
            logger.debug("操作成功!");
            resultObj.setMessage("操作成功!");
        }catch (Exception e){
            resultObj = new RespObj(Constant.FAILD_CODE);
            logger.debug("操作失败!");
            resultObj.setMessage("操作失败!");
        }
        return JSON.toJSONString(resultObj);
    }
}
