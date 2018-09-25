package com.fulaan.backstage.controller;

import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.fulaan.backstage.dto.PhonesProjectDto;
import com.fulaan.backstage.service.BackStageInOutStorageService;
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
 * Created by taotao.chan on 2018年9月20日09:37:49
 */
@Api(value = "后台管理类-出库入库记录")
@Controller
@RequestMapping("/web/backstageinoutstorage")
public class BackStageInOutStorageController extends BaseController {
    @Autowired
    private BackStageInOutStorageService backStageInOutStorageService;


    /**
     * 库存管理-批量出库
     * @return
     */
    @ApiOperation(value = "库存管理-批量出库", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/batchOutStorage")
    @ResponseBody
    public RespObj batchOutStorage(@RequestParam Map<String, Object> params) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            backStageInOutStorageService.batchOutStorage(params);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 库存管理-项目列表
     * @return
     */
    @ApiOperation(value = "库存管理-项目列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getProjectList")
    @ResponseBody
    public RespObj getProjectList() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PhonesProjectDto> dtos = backStageInOutStorageService.getProjectList();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtos);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 库存管理-获取出库历史
     * @return
     */
    @ApiOperation(value = "库存管理-获取出库历史", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOutStorageHistoryList")
    @ResponseBody
    public RespObj getOutStorageHistoryList(
            @ApiParam(name = "page", required = true, value = "page") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ApiParam(name = "inputParams", required = false, value = "inputParams") @RequestParam(value = "inputParams", defaultValue = "") String inputParams,
            @ApiParam(name = "year", required = false, value = "year") @RequestParam(value = "year", defaultValue = "") String year,
            @ApiParam(name = "month", required = false, value = "month") @RequestParam(value = "month", defaultValue = "") String month
    ) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageInOutStorageService.getOutStorageHistoryList(page, pageSize, inputParams, year, month);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


}
