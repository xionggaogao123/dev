package com.fulaan.referenceData.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.referenceData.dto.ReferenceDataDTO;
import com.fulaan.referenceData.service.ReferenceDataService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2018-04-23.
 */
@Api(value="新参考资料")
@Controller
@RequestMapping("/jxmapi/referencedate")
public class ReferenceDataController extends BaseController {
    @Autowired
    private ReferenceDataService referenceDataService;
    
    @ApiOperation(value = "添加参考资料", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "添加参考资料成功",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/addReferenceDataEntry")
    @ResponseBody
    public RespObj addReferenceDataEntry(@RequestBody ReferenceDataDTO dto){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            dto.setUserId(getUserId().toString());
            String result = referenceDataService.addReferenceDataEntry(dto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
            if(result.contains("含")) {
                respObj.setCode(Constant.FAILD_CODE);
                respObj.setErrorMessage(result);
            }
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加参考资料失败");
        }
        return respObj;
    }

    @ApiOperation(value = "条件查询参考资料", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "条件查询参考资料成功",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/getReferenceData")
    @ResponseBody
    public RespObj getReferenceData(@ApiParam(name = "keyword", required = false, value = "关键字") @RequestParam("keyword") String keyword,
                                    @ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                    @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize,
                                    @ApiParam(name = "type", required = false, value = "type") @RequestParam("type") int type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = referenceDataService.getReferenceData(keyword,page,pageSize,type,getUserId(),1,"");
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询失败");
        }
        return respObj;
    }

    @ApiOperation(value = "转发", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "转发",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/transReferenceData")
    @ResponseBody
    public RespObj transReferenceData(@ApiParam(name = "id", required = false, value = "转发id") @RequestParam("id") String id,
                                    @ApiParam(name = "communityIds", required = true, value = "转发班级") @RequestParam("communityIds") String communityIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = referenceDataService.transReferenceData(new ObjectId(id), communityIds,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询失败");
        }
        return respObj;
    }

    @ApiOperation(value = "删除", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/delReferenceData")
    @ResponseBody
    public RespObj delReferenceData(@ApiParam(name = "id", required = false, value = "转发id") @RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            referenceDataService.delReferenceData(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除失败");
        }
        return respObj;
    }

    @ApiOperation(value = "学生端条件查询参考资料", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "条件查询参考资料成功",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/getStudentReferenceData")
    @ResponseBody
    public RespObj getStudentReferenceData(@ApiParam(name = "keyword", required = false, value = "关键字") @RequestParam(value="keyword",defaultValue = "") String keyword,
                                    @ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                    @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize,
                                    @ApiParam(name = "type", required = false, value = "type") @RequestParam(value="type",defaultValue = "0") int type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = referenceDataService.getStudentReferenceData(keyword,page,pageSize,type,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询失败");
        }
        return respObj;
    }

    @ApiOperation(value = "处理老数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "处理老数据",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 501, message = "已登出")})
    @RequestMapping("/updateEntry")
    @ResponseBody
    public RespObj updateEntry(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            referenceDataService.updateEntry();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("处理成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("处理失败");
        }
        return respObj;
    }

}
