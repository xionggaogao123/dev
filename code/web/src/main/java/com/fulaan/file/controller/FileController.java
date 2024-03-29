package com.fulaan.file.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.file.service.FileService;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caocui on 2015/8/25.
 */
@Api(value="file1")
@Controller
@RequestMapping("/file1")
public class FileController extends BaseController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "文件上传", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap.put("data", fileService.uploadFile(request, this.getUserId(), "file"));
            resultMap.put("code", "200");
        } catch (IOException e) {
            resultMap.put("code", "500");
            resultMap.put("data", "上传文件失败");
            e.printStackTrace();
        } catch (IllegalParamException e) {
            resultMap.put("code", "500");
            resultMap.put("data", "上传文件失败");
            e.printStackTrace();
        } finally {
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.write(JSON.toJSONString(resultMap));
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "文件上传", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        try {
            fileService.downFile(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @return
     */
    @ApiOperation(value = "文件上传", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/getResourceUrl")
    @ResponseBody
    public String getResourceUrl(@RequestParam(value = "resourceId", defaultValue = "") String resourceId) {
        Map<String, String> resultMap = new HashMap<String, String>();
        return JSON.toJSONString(resultMap);
    }
}
