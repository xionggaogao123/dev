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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 简易查询学校列表
     */
    @ApiOperation(value = "简易查询学校列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
        @RequestMapping("/getSimpleSchoolList")
    @ResponseBody
    public String getSimpleSchoolList(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<HomeSchoolDTO> dtos = homeSchoolService.getSimpleSchoolList(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("简易查询学校列表失败!");
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
    /**
     *  查询学校下的社群列表
     * @return
     */
    @ApiOperation(value = "查询学校下的社群列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getCommunityListBySchoolId")
    @ResponseBody
    public String getCommunityListBySchoolId(@ApiParam(name="schoolId",required = true,value="schoolId") String schoolId,
                                             @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                             @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize
                                             ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = homeSchoolService.getCommunityListBySchoolId(new ObjectId(schoolId),page,pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询学校下的社群列表失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 认证老师的数据统计
     */
    @ApiOperation(value = "上帝视角重置密码", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getTeacherList")
    @ResponseBody
    public String getTeacherList(@ApiParam(name = "schoolId", required = true, value = "schoolId") @RequestParam(value = "schoolId") String schoolId,
                                 @ApiParam(name = "communityId", required = true, value = "communityId") @RequestParam(value = "communityId") String communityId,
                                 @ApiParam(name = "startTime", required = true, value = "startTime") @RequestParam(value = "startTime") long startTime,
                                 @ApiParam(name = "endTime", required = true, value = "endTime") @RequestParam(value = "endTime") long endTime){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> list = homeSchoolService.getTeacherList(startTime, endTime,communityId ,new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(list);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("重置失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 导出大V数据
     * @param response
     */
    @ApiOperation(value = "导出大V数据", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(@ApiParam(name = "schoolId", required = true, value = "schoolId") @RequestParam(value = "schoolId") String schoolId,
                               @ApiParam(name = "communityId", required = true, value = "communityId") @RequestParam(value = "communityId") String communityId,
                               @ApiParam(name = "startTime", required = true, value = "startTime") @RequestParam(value = "startTime") long startTime,
                               @ApiParam(name = "endTime", required = true, value = "endTime") @RequestParam(value = "endTime") long endTime,
                               HttpServletResponse response,
                               HttpServletRequest request) {
        try {
            List<Map<String,Object>> list = homeSchoolService.getTeacherList(startTime, endTime,communityId ,new ObjectId(schoolId));
            homeSchoolService.exportTemplate(request,response,list,startTime,endTime,new ObjectId(schoolId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  查询学校下的所有用户角色列表
     * @return
     */
    @ApiOperation(value = "查询学校下的所有用户角色列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getPersonListBySchoolId")
    @ResponseBody
    public String getPersonListBySchoolId(@ApiParam(name="schoolId",required = true,value="schoolId") String schoolId,
                                             @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                             @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = homeSchoolService.getPersonListBySchoolId(new ObjectId(schoolId), page, pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询学校下的所有用户角色列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  添加学校管理员
     * @return
     */
    @ApiOperation(value = "添加学校管理员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addPersonToSchool")
    @ResponseBody
    public String addPersonToSchool(@ApiParam(name="schoolId",required = true,value="schoolId") String schoolId,
                                    @ApiParam(name="userId",required = true,value="userId") String userId,
                                    @ApiParam(name="name",required = true,value="name") @RequestParam(value="name",defaultValue = "") String name,
                                    @ApiParam(name="type",required = true,value="type") @RequestParam(value="type",defaultValue = "1") int type,
                                    @ApiParam(name="role",required = true,value="role") @RequestParam(value="role",defaultValue = "1") int role
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            homeSchoolService.addPersonToSchool(getUserId(),new ObjectId(userId), new ObjectId(schoolId), name, type, role);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加学校管理员失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  删除学校管理员
     * @return
     */
    @ApiOperation(value = "删除学校管理员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/deletePersonToSchool")
    @ResponseBody
    public String deletePersonToSchool(@ApiParam(name="id",required = true,value="id") String id
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            homeSchoolService.deletePersonToSchool(getUserId(), new ObjectId(id));
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除学校管理员失败!");
        }
        return JSON.toJSONString(respObj);
    }
}
