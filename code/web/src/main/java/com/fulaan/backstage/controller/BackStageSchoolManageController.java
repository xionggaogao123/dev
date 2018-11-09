package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fulaan.backstage.dto.SchoolControlTimeDTO;
import com.fulaan.backstage.service.BackStageAdminManageService;
import com.fulaan.backstage.service.BackStageSchoolManageService;
import com.fulaan.base.BaseController;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
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

import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年9月10日11:08:38
 */
@Api(value = "后台管理类")
@Controller
@RequestMapping("/web/backstageschoolmanage")
public class BackStageSchoolManageController extends BaseController {

    @Autowired
    private BackStageSchoolManageService backStageSchoolManageService;

    @Autowired
    private UserService userService;

    /**
     * 多条件查询学校列表
     */
    @ApiOperation(value = "多条件查询学校列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getSchoolList")
    @ResponseBody
    public String getSchoolList(@ApiParam(name="schoolType",required = false,value="学校类型") @RequestParam(value="schoolType",defaultValue = "0") int schoolType,
                                @ApiParam(name="provincesName",required = false,value="学校所属省") @RequestParam(value="provincesName",defaultValue = "") String provincesName,
                                @ApiParam(name="cityName",required = false,value="学校所属市") @RequestParam(value="cityName",defaultValue = "") String cityName,
                                @ApiParam(name="areaName",required = false,value="学校所属市") @RequestParam(value="areaName",defaultValue = "") String areaName,
                                @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize,
                                @ApiParam(name="schoolName",required = false,value="学校名") @RequestParam(value="schoolName",defaultValue = "") String schoolName){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String, Object> dtos = backStageSchoolManageService.getSchoolList(schoolType, provincesName, cityName, areaName, page, pageSize, schoolName);
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
    @RequestMapping("/saveNewSchoolEntry")
    @ResponseBody
    public String saveNewSchoolEntry(@ApiParam(name="dto",required = true,value="学校对象") @RequestBody HomeSchoolDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String str = backStageSchoolManageService.saveNewSchoolEntry(dto);
            respObj.setMessage(str);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("保存学校信息失败!");
        }
        return JSON.toJSONString(respObj);
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
            Map<String,Object> map = backStageSchoolManageService.getPersonListBySchoolId(new ObjectId(schoolId), page, pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询学校下的所有用户角色列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  查询用户信息
     * @return
     */
    @ApiOperation(value = "查询用户信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getUserInfoByUserId")
    @ResponseBody
    public RespObj getUserInfoByUserId(@ApiParam(name="userId",required = true,value="userId") String userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
//            UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(userId);
            UserEntry userEntry = userService.findByGenerateCode(userId);
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(userEntry);
            respObj.setMessage(userDetailInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询用户信息失败!");
        }
        return respObj;
    }

    /**
     *  运营管理添加学校管理员
     * @return
     */
    @ApiOperation(value = "添加学校管理员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/savePersonToSchool")
    @ResponseBody
    public RespObj addPersonToSchool(@ApiParam(name="schoolId",required = true,value="schoolId") String schoolId,
                                    @ApiParam(name="userId",required = true,value="userId") String userId,
                                    @ApiParam(name="name",required = true,value="name") @RequestParam(value="name",defaultValue = "") String name,
                                    @ApiParam(name="type",required = true,value="type") @RequestParam(value="type",defaultValue = "-10") int type,
                                    @ApiParam(name="role",required = true,value="role") @RequestParam(value="role",defaultValue = "-10") int role,
                                    @ApiParam(name="role",required = true,value="roleId") @RequestParam(value="roleId",defaultValue = "") String roleId
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageSchoolManageService.savePersonToSchool(getUserId(),new ObjectId(userId), new ObjectId(schoolId), name, type,role ,roleId);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加学校管理员失败!");
        }
        return respObj;
    }

    /**
     *  删除学校管理员
     * @return
     */
    @ApiOperation(value = "删除学校管理员", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/deletePersonToSchool")
    @ResponseBody
    public RespObj deletePersonToSchool(@ApiParam(name="id",required = true,value="id") String id
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            backStageSchoolManageService.deletePersonToSchool(getUserId(), new ObjectId(id));
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除学校管理员失败!");
        }
        return respObj;
    }

    /**
     *  查询学校下的社群列表
     * @return
     */
    @ApiOperation(value = "查询学校下的社群列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getCommunityListBySchoolId")
    @ResponseBody
    public String getCommunityListBySchoolId(@ApiParam(name="schoolId",required = false,value="schoolId") @RequestParam(value="schoolId",defaultValue = "") String schoolId,
                                             @ApiParam(name="communityName",required = false,value="communityName") @RequestParam(value="communityName",defaultValue = "") String communityName,
                                             @ApiParam(name="page",required = true,value="page") @RequestParam(value="page",defaultValue = "1") int page,
                                             @ApiParam(name="pageSize",required = true,value="pageSize") @RequestParam(value="pageSize",defaultValue = "5") int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //当从学校管理点进去 会有schoolId，
            // 当从社群管理点进去，则没有，取当前管理员属于哪个学校
            if (schoolId == "" || schoolId == null || "undefined".equals(schoolId)){
                schoolId = backStageSchoolManageService.getSchoolIdByManageUid(getUserId());
//                schoolId = backStageSchoolManageService.getSchoolIdByManageUid(new ObjectId("5ad453a43d4df940950b99e2"));
            }
            Map<String,Object> map = backStageSchoolManageService.getCommunityListBySchoolId(new ObjectId(schoolId),communityName,page,pageSize);
            map.put("schoolId",schoolId);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询学校下的社群列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 使用情况统计
     * 认证老师的数据统计
     */
    @ApiOperation(value = "认证老师的数据统计", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getTeacherList")
    @ResponseBody
    public String getTeacherList(@ApiParam(name = "schoolId", required = true, value = "schoolId") @RequestParam(value = "schoolId") String schoolId,
                                 @ApiParam(name = "communityId", required = true, value = "communityId") @RequestParam(value = "communityId") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> list = backStageSchoolManageService.getTeacherList(communityId ,new ObjectId(schoolId));
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
     * 校管控设置保存
     */
    @ApiOperation(value = "校管控设置保存", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveSchoolControlSetting")
    @ResponseBody
    public String saveSchoolControlSetting(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = backStageSchoolManageService.saveSchoolControlSetting(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 校管控列表查询
     * 查询当前学校的管控设置列表展示
     */
    @ApiOperation(value = "校管控列表查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getEachSchoolControlSettingList")
    @ResponseBody
    public RespObj getEachSchoolControlSettingList(
            @ApiParam(name = "schoolId", required = true, value = "schoolId") @RequestParam(value = "schoolId",defaultValue = "") String schoolId
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<SchoolControlTimeDTO> result = backStageSchoolManageService.getEachSchoolControlSettingList(new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作失败");
        }
        return respObj;
    }

    /**
     * 校管控列表查询
     * 系统默认管控设置列表展示
     */
    @ApiOperation(value = "系统默认校管控列表查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getDefaultSchoolControlSettingList")
    @ResponseBody
    public RespObj getDefaultSchoolControlSettingList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<SchoolControlTimeDTO> result = backStageSchoolManageService.getDefaultSchoolControlSettingList();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作失败");
        }
        return respObj;
    }

    /**
     * 校管控设置删除
     */
    @ApiOperation(value = "校管控设置删除", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delSchoolControlSetting")
    @ResponseBody
    public String delSchoolControlSetting(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = backStageSchoolManageService.delSchoolControlSetting(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修复存在学校没有默认管控时间
     */
    @ApiOperation(value = "修复存在学校没有默认管控时间", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/fixOldSchoolControlTimeSetting")
    @ResponseBody
    public String fixOldSchoolControlTimeSetting(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = backStageSchoolManageService.fixOldSchoolControlTimeSetting();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作失败");
        }
        return JSON.toJSONString(respObj);
    }

}
