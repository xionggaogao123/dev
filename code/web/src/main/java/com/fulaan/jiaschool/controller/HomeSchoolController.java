package com.fulaan.jiaschool.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018/1/30.
 */
@Api(value="学校基础类")
@Controller
@RequestMapping("/web/homeschool")
public class HomeSchoolController extends BaseController {
    @Autowired
    private HomeSchoolService homeSchoolService;

    /**
     * 多条件查询学校列表
     */
    @ApiOperation(value = "多条件查询学校列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getSchoolList")
    @ResponseBody
    public String getSchoolList(@ApiParam(name="schoolType",required = true,value="学校类型") @RequestParam(value="schoolType",defaultValue = "0") int schoolType,
                                  @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                  @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize,
                                  @ApiParam(name="keyword",required = false,value="关键字") @RequestParam(value="keyword",defaultValue = "") String keyword){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos = homeSchoolService.getSchoolList(schoolType, page, pageSize, keyword);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("多条件查询学校列表失败!");
        }
        return JSON.toJSONString(respObj);
    }



    /**
     *  添加新的学校
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加新的学校", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addNewSchoolEntry")
    @ResponseBody
    public String addNewSchoolEntry(@ApiParam(name="dto",required = true,value="作业对象") HomeSchoolDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String str = homeSchoolService.addNewSchoolEntry(dto);
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加新的学校失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  删除学校
     * @return
     */
    @ApiOperation(value = "删除学校", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/delNewSchoolEntry")
    @ResponseBody
    public String delNewSchoolEntry(@ApiParam(name="id",required = true,value="id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            homeSchoolService.delNewSchoolEntry(new ObjectId(id), getUserId());
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  社区id查询
     * @return
     */
    @ApiOperation(value = "社区id查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/selectNewCommunityEntry")
    @ResponseBody
    public String selectNewCommunityEntry(@ApiParam(name="id",required = true,value="id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            CommunityDTO communityDTO = homeSchoolService.selectNewCommunityEntry(id);
            List<CommunityDTO> communityDTOs = new ArrayList<CommunityDTO>();
            if(communityDTO != null){
                communityDTOs.add(communityDTO);
            }
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("list",communityDTOs);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("社区id查询失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *  加入学校属性
     * @return
     */
    @ApiOperation(value = "社区id查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addSchoolSort")
    @ResponseBody
    public String addSchoolSort(@ApiParam(name="communityId",required = true,value="communityId") String communityId,
                                @ApiParam(name="schoolId",required = true,value="schoolId") String schoolId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            homeSchoolService.addSchoolSort(communityId,schoolId);
            respObj.setMessage("加入成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("加入失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  解除学校属性
     * @return
     */
    @ApiOperation(value = "社区id查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/delSchoolSort")
    @ResponseBody
    public String delSchoolSort(@ApiParam(name="communityId",required = true,value="communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            homeSchoolService.delSchoolSort(new ObjectId(communityId));
            respObj.setMessage("解除学校属性成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("解除学校属性失败!");
        }
        return JSON.toJSONString(respObj);
    }


}
