package com.fulaan.backstage.controller;

import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.fulaan.backstage.service.BackStageOutStorageFollowService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/web/backstageoutstoragefollow")
public class BackStageOutStorageFollowController extends BaseController {
    @Autowired
    private BackStageOutStorageFollowService backStageOutStorageFollowService;

    /**
     * 出库跟踪-导入用户信息模板下载
     * @param response
     * @param request
     */
    @ApiOperation(value = "出库跟踪-导入用户信息模板下载", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportOutStorageFollowUserTemplate")
    @ResponseBody
    public void exportOutStorageFollowUserTemplate(HttpServletResponse response,
                               HttpServletRequest request) {
        try {
            backStageOutStorageFollowService.exportOutStorageFollowUserTemplate(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出库跟踪-导入用户信息导入
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "出库跟踪-导入用户信息导入", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/importOutStorageFollowUser")
    @ResponseBody
    public RespObj importOutStorageFollowUser(HttpServletRequest request) throws Exception {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        MultipartRequest multipartRequest = (MultipartRequest) request;
        String result ="导入模板失败";
        try {
            MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
            for (List<MultipartFile> multipartFiles : fileMap.values()) {
                for (MultipartFile file : multipartFiles) {
                    System.out.println("----" + file.getOriginalFilename());
                    result = backStageOutStorageFollowService.importOutStorageFollowUser(file.getInputStream(),request);
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 出库跟踪-按项目查找
     * @param page
     * @param pageSize
     * @param inputParams
     * @param projectId
     * @return
     */
    @ApiOperation(value = "出库跟踪-按项目查找", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOutStorageListByProject")
    @ResponseBody
    public RespObj getOutStorageListByProject(
            @ApiParam(name = "page", required = true, value = "page") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ApiParam(name = "inputParams", required = false, value = "inputParams") @RequestParam(value = "inputParams", defaultValue = "") String inputParams,
            @ApiParam(name = "projectId", required = false, value = "projectId") @RequestParam(value = "projectId", defaultValue = "") String projectId
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> result = backStageOutStorageFollowService.getOutStorageListByProject(page, pageSize, inputParams, projectId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 出库跟踪-单个回收
     * @return
     */
    @ApiOperation(value = "出库跟踪-单个回收", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/singleRecycleInStorage")
    @ResponseBody
    public RespObj singleRecycleInStorage(@RequestParam Map<String, Object> params) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<String> result = backStageOutStorageFollowService.singleRecycleInStorage(params);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 出库跟踪-批量回收
     * @return
     */
    @ApiOperation(value = "出库跟踪-批量回收", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/batchRecycleInStorage")
    @ResponseBody
    public RespObj batchRecycleInStorage(@RequestParam Map<String, Object> params) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<String> result =backStageOutStorageFollowService.batchRecycleInStorage(params);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
