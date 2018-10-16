package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.dto.StorageManageDto;
import com.fulaan.backstage.service.BackStageAdminManageService;
import com.fulaan.backstage.service.BackStageStorageManageService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Created by taotao.chan on 2018年9月17日14:32:36
 */
@Api(value = "后台管理类-库存管理")
@Controller
@RequestMapping("/web/backstagestoragemanage")
public class BackStageStorageManageController extends BaseController {
    @Autowired
    private BackStageStorageManageService backStageStorageManageService;

    /**
     * 库存批量导入模板
     * @param response
     * @param request
     */
    @ApiOperation(value = "库存批量导入模板", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportStorageTemplate")
    @ResponseBody
    public void exportStorageTemplate(HttpServletResponse response,
                               HttpServletRequest request) {
        try {
            backStageStorageManageService.exportStorageTemplate(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导入模板", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/importStorageTemplate")
    @ResponseBody
    public RespObj importStorageTemplate(HttpServletRequest request) throws Exception {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        MultipartRequest multipartRequest = (MultipartRequest) request;
        String result ="导入模板失败";
        try {
            MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
            for (List<MultipartFile> multipartFiles : fileMap.values()) {
                for (MultipartFile file : multipartFiles) {
                    System.out.println("----" + file.getOriginalFilename());
                    result = backStageStorageManageService.importStorageTemplate(file.getInputStream(),request);
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
     * 库存查询下拉条件
     * @return
     */
    @ApiOperation(value = "库存查询下拉条件", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStorageOption")
    @ResponseBody
    public RespObj getStorageOption() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageStorageManageService.getStorageOption();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 库存Table数据查询
     * @param page
     * @param pageSize
     * @param imeiNo
     * @param storageStatus
     * @param useStatus
     * @param year
     * @param month
     * @return
     */
    @ApiOperation(value = "库存查询", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStorageInfoList")
    @ResponseBody
    public RespObj getStorageInfoList(
            @ApiParam(name = "page", required = true, value = "page") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ApiParam(name = "imeiNo", required = false, value = "imeiNo") @RequestParam(value = "imeiNo", defaultValue = "") String imeiNo,
            @ApiParam(name = "storageStatus", required = false, value = "storageStatus") @RequestParam(value = "storageStatus", defaultValue = "") String storageStatus,
            @ApiParam(name = "useStatus", required = false, value = "useStatus") @RequestParam(value = "useStatus", defaultValue = "") String useStatus,
            @ApiParam(name = "year", required = false, value = "year") @RequestParam(value = "year", defaultValue = "") String year,
            @ApiParam(name = "month", required = false, value = "month") @RequestParam(value = "month", defaultValue = "") String month
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageStorageManageService.getStorageInfoList(page, pageSize, imeiNo, storageStatus, useStatus, year, month);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 根据唯一索引更新
     * @param map
     * @return
     */
    @ApiOperation(value = "根据唯一索引更新", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateStorageInfoById")
    @ResponseBody
    public RespObj updateStorageInfoById(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageStorageManageService.updateStorageInfoById(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 根据唯一索引集合更新
     * @param map
     * @return
     */
    @ApiOperation(value = "根据唯一索引集合更新", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateStorageInfoByIds")
    @ResponseBody
    public RespObj updateStorageInfoByIds(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageStorageManageService.updateStorageInfoByIds(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 库存管理-单个冻结
     * @param map
     * @return
     */
    @ApiOperation(value = "库存管理-单个冻结", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/freezeStorageInfoById")
    @ResponseBody
    public RespObj freezeStorageInfoById(@RequestParam Map<String, Object> map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageStorageManageService.freezeStorageInfoById(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 库存管理-批量冻结
     * @param map
     * @return
     */
    @ApiOperation(value = "库存管理-批量冻结", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/freezeStorageInfoByIds")
    @ResponseBody
    public RespObj freezeStorageInfoByIds(@RequestParam Map<String, Object> map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String result = backStageStorageManageService.freezeStorageInfoByIds(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
