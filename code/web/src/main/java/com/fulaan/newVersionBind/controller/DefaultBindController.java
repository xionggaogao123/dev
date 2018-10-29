package com.fulaan.newVersionBind.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.newVersionBind.dto.*;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.operation.service.AppCommentService;
import com.fulaan.service.MemberService;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.pojo.app.SessionValue;
import com.pojo.user.TeacherSubjectBindDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
 * Created by scott on 2017/9/5.
 */
@Api(value = "app家长孩子绑定逻辑")
@Controller
@RequestMapping("/jxmapi/bind")
public class DefaultBindController extends BaseController {


    private static final Logger logger = Logger.getLogger(DefaultBindController.class);

    @Autowired
    private NewVersionBindService newVersionBindService;

    @Autowired
    private AppCommentService appCommentService;
    @Autowired
    private MemberService memberService;


    /**
     * 完善绑定的信息
     * @param bindId
     * @param sex
     * @param birthDate
     * @param provinceName
     * @param regionName
     * @param regionAreaName
     * @param avatar
     * @param schoolName
     * @param gradeType
     * @return
     */
    @ApiOperation(value = "完善绑定的信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/supplementNewVersionInfo")
    @ResponseBody
    public RespObj supplementNewVersionInfo(
            @ApiParam(name = "bindId", required = true, value = "绑定Id") @ObjectIdType ObjectId bindId,
            int sex,String birthDate,
            String provinceName,
            String regionName,
            String regionAreaName,
            String avatar,
            String schoolName,
            String nickName,
            int relation,
            int gradeType
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.supplementNewVersionInfo(bindId, sex, birthDate, provinceName, regionName, regionAreaName, schoolName, avatar, gradeType,
                    nickName, relation);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("完善信息成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param bindId
     * @param sex
     * @param birthDate
     * @param personalSignature
     * @param avatar
     * @param nickName
     * @return
     */
    @ApiOperation(value = "完善绑定的信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveBindUserDetail")
    @ResponseBody
    public RespObj saveBindUserDetail(
            @ApiParam(name = "bindId", required = true, value = "绑定Id") @ObjectIdType ObjectId bindId,
            @RequestParam(required = false,defaultValue = "-1") int sex,
            @RequestParam(required = false,defaultValue = "")String birthDate,
            @RequestParam(required = false,defaultValue = "")String personalSignature,
            @RequestParam(required = false,defaultValue = "")String avatar,
            @RequestParam(required = false,defaultValue = "")String nickName
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            SessionValue sv = getSessionValue();
            sv.setAvatar(avatar + "?v=1");
            sv.setUserName(nickName);
            String userKey = CacheHandler.getUserKey(sv.getId());
            CacheHandler.cacheSessionValue(userKey,
                    sv, Constant.SECONDS_IN_HALF_YEAR);
            memberService.updateAllAvatar(new ObjectId(sv.getId()),avatar);
            newVersionBindService.saveBindUserDetail(bindId, sex, birthDate, avatar,
                    nickName, personalSignature);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("完善信息成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 填写信息绑定接口
     * @param userId
     * @param nickName
     * @param relation
     * @return
     */
    @ApiOperation(value = "填写信息绑定接口", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveNewVersionEntry")
    @ResponseBody
    public RespObj saveNewVersionEntry(
            @ApiParam(name = "userId", required = true, value = "userId") @ObjectIdType ObjectId userId,
            String nickName,
            String avatar,
            int relation){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            //移交课程
            newVersionBindService.yijiao(userId,getUserId());
            newVersionBindService.saveNewVersionBindRelationEntry(getUserId(),userId, relation,
                    nickName,avatar);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 填写信息绑定接口
     * @param userId
     * @return
     */
    @ApiOperation(value = "添加孩子真实姓名", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addTrueName")
    @ResponseBody
    public RespObj addTrueName(
            @ApiParam(name = "userId", required = true, value = "userId") @ObjectIdType ObjectId userId,
            String name){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{

            newVersionBindService.addTrueName(userId,name);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获取某个家长的绑定的所有的学生列表
     * @return
     */
    @ApiOperation(value = "获取某个家长的绑定的所有的学生列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getBindDtos")
    @ResponseBody
    public RespObj getNewVersionBindDtos(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionBindRelationDTO> dtoList=newVersionBindService.getNewVersionBindDtos(getUserId(),null);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获取某个家长的绑定(包括分享)的所有的学生列表
     * @return
     */
    @ApiOperation(value = "获取某个家长的绑定的所有的学生列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getNewBindDtos")
    @ResponseBody
    public RespObj getThreeVersionBindDtos(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> dtoList=newVersionBindService.getThreeVersionBindDtos(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage("查询管控孩子失败！");
        }
        return respObj;
    }

    /**
     * 特殊列表
     * @return
     */
    @ApiOperation(value = "获取某个家长的绑定的所有的学生列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMyBindDtos")
    @ResponseBody
    public RespObj getMyNewVersionBindDtos(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> dtoList=newVersionBindService.getMyNewVersionBindDtos(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param communityId
     * @return
     */
    @ApiOperation(value = "获取某个家长的绑定的某个社区下的孩子列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getCommunityBindEntries")
    @ResponseBody
    public RespObj getCommunityBindEntries(@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionCommunityBindDTO> dtoList=newVersionBindService.getCommunityBindEntries(getUserId(),communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "查找登录状态的绑定孩子列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/searchLoginChildren")
    @ResponseBody
    public RespObj searchLoginChildren(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<UserLoginStatus> dtoList=newVersionBindService.searchLoginChildren(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @return
     */
    @ApiOperation(value = "获取", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getBindStudent")
    @ResponseBody
    public RespObj getNewVersionBindStudent(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            NewVersionBindRelationDTO dto=newVersionBindService.getNewVersionBindStudent(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dto);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "保存", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addEntry")
    @ResponseBody
    public RespObj saveNewVersionEntry(@RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveNewVersionEntry(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 绑定社区下的某个家长与某些学生
     * @param userIds
     * @param communityId
     * @return
     */
    @ApiOperation(value = "绑定社区下的某个家长与某些学生", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addCommunityBindEntry")
    @ResponseBody
    public RespObj addCommunityBindEntry(String userIds,
                                         @ApiParam(name = "communityId", required = true, value = "communityId") @ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.addCommunityBindEntry(userIds, communityId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 处理老数据
     */
    @ApiOperation(value = "处理老数据", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editCommunityBindEntry")
    @ResponseBody
    public RespObj editCommunityBindEntry(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.editCommunityBindEntry();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }
    /**
     * 学生端支持扫码入群
     * @param communityId
     * @return
     */
    @ApiOperation(value = "学生端支持扫码入群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saoCommunityBindEntry")
    @ResponseBody
    public RespObj saoCommunityBindEntry(@ApiParam(name = "communityId", required = true, value = "communityId") @ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = newVersionBindService.saoCommunityBindEntry(getUserId(), communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 添加虚拟学生
     * @param thirdName
     * @param number
     * @param communityId
     * @return
     */
    @ApiOperation(value = "绑定社区下的虚拟学生", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addBindVirtualCommunity")
    @ResponseBody
    public RespObj addBindVirtualCommunity(String thirdName,String number, @ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.addBindVirtualCommunity(thirdName,communityId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定社区下的虚拟学生成功!");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 修改虚拟学生
     * @param thirdName
     * @param number
     * @param bindId
     * @return
     */
    @ApiOperation(value = "绑定社区下的虚拟学生", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateBindVirtualCommunity")
    @ResponseBody
    public RespObj updateBindVirtualCommunity(String thirdName,String number, @ObjectIdType ObjectId bindId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.updateBindVirtualCommunity(thirdName,number,bindId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定社区下的虚拟学生成功!");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 多选添加虚拟学生
     * @param thirdName
     * @param number
     * @param communityIds
     * @returnOe
     */
    @ApiOperation(value = "多选绑定社区下的虚拟学生", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addMoreBindVirtualCommunity")
    @ResponseBody
    public RespObj addMoreBindVirtualCommunity(@RequestParam(value="thirdName") String thirdName,@RequestParam(value="number") String number,  @RequestParam(value="communityIds") String communityIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            if(communityIds!=null){
                String[] strings =communityIds.split(",");
                for(String str:strings){
                    if (StringUtils.isNotBlank(number)) {
                        newVersionBindService.addBindVirtualCommunityCopy(thirdName.trim(), number, new ObjectId(str), getUserId());
                    } else {
                        newVersionBindService.addBindVirtualCommunity(thirdName.trim(),new ObjectId(str),getUserId());
                    }
                    
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("添加成功!");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @param parentId
     * @param studentId
     * @return
     */
    @ApiOperation(value = "个人中心解除绑定", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delEntry")
    @ResponseBody
    public RespObj delNewVersionEntry(@ApiParam(name = "parentId", required = true, value = "父母id") @RequestParam("parentId") String parentId,@ApiParam(name = "studentId", required = true, value = "学生id") @RequestParam("studentId") String studentId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.delNewVersionEntry(new ObjectId(parentId), new ObjectId(studentId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("解除绑定成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 包含虚拟学生
     * @param communityId
     * @return
     */
    @ApiOperation(value = "获取某个社区下绑定的孩子有哪些", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getCommunityBindStudentList")
    @ResponseBody
    public RespObj getCommunityBindStudentList(@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionBindRelationDTO> dtoList=newVersionBindService.getCommunityBindStudentList(getUserId(),communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询全部包含虚拟学生
     * @return
     */
    @ApiOperation(value = "获取某个社区下绑定的孩子有哪些", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getAllCommunityBindStudentList")
    @ResponseBody
    public RespObj getAllCommunityBindStudentList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionBindRelationDTO> dtoList=newVersionBindService.getAllCommunityBindStudentList(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param communityId
     * @return
     */
    @ApiOperation(value = "获取某个社区下绑定的孩子有哪些", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getCommunityBindList")
    @ResponseBody
    public RespObj getCommunityBindList(@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionBindRelationDTO> dtoList=newVersionBindService.getNewVersionBindDtos(getUserId(),communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    /**
     *
     * @param bindDTO
     * @return
     */
    @ApiOperation(value = "绑定该老师学科信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/bindTeacherSubject")
    @ResponseBody
    public RespObj bindTeacherSubject(@RequestBody TeacherSubjectBindDTO bindDTO){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveTeacherSubjectBind(bindDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定该老师学科信息成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    /**
     *
     * @param communityId
     * @param userId
     * @param studentNumber
     * @return
     */
    @ApiOperation(value = "编辑学生学号信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editStudentNumber")
    @ResponseBody
    public RespObj editStudentNumber(@ObjectIdType ObjectId communityId,
                                     @ObjectIdType ObjectId userId,
                                     String studentNumber){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.updateNumber(communityId, getUserId(), userId, studentNumber);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("编辑学生学号信息成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "编辑学生学号信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editStudentNumberAndThirdName")
    @ResponseBody
    public RespObj editStudentNumberAndThirdName(@ObjectIdType ObjectId communityId,
                                     @ObjectIdType ObjectId userId,
                                     String thirdName,
                                     @ObjectIdType ObjectId bindId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.updateStudentNumberAndThirdName(communityId, getUserId(), userId, thirdName.trim(),bindId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("编辑学生学号和姓名信息成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 多选绑定
     */
    @ApiOperation(value = "多选绑定编辑学生学号信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editMoreStudentNumberAndThirdName")
    @ResponseBody
    public RespObj editMoreStudentNumberAndThirdName(String communityIds,
                                                 @ObjectIdType ObjectId userId,
                                                 String thirdName,
                                                 String studentNumber,
                                                 String bindId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            if(communityIds !=null){
                String[] strings = communityIds.split(",");
                for(String str: strings){
                    if (StringUtils.isNotBlank(studentNumber)) {
                        newVersionBindService.updateStudentNumberAndThirdNameCopy(new ObjectId(str), getUserId(), userId, studentNumber, thirdName);
                    } else {
                        newVersionBindService.updateStudentNumberAndThirdName(new ObjectId(str), getUserId(), userId, thirdName,new ObjectId(bindId));
                    }
                    
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("编辑成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * @param communityId
     * @param userId
     * @param thirdName
     * @return
     */
    @ApiOperation(value = "编辑学生姓名信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/editStudentThirdName")
    @ResponseBody
    public RespObj editStudentThirdName(@ObjectIdType ObjectId communityId,
                                     @ObjectIdType ObjectId userId,
                                     String thirdName){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.updateThirdName(communityId, getUserId(), userId, thirdName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("编辑学生姓名信息！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param subjectIds
     * @return
     */
    @ApiOperation(value = "保存老师与学科的绑定", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveNewVersionSubject")
    @ResponseBody
    public RespObj saveNewVersionSubject(String subjectIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveNewVersionSubject(subjectIds,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存老师与学科的绑定！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @return
     */
    @ApiOperation(value = "获取老师的绑定的所有的学科列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getTeacherBindSubjectList")
    @ResponseBody
    public RespObj getTeacherBindSubjectList(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<SubjectClassDTO> subjectClassDTOs=appCommentService.selectTeacherSubjectList2(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(subjectClassDTOs);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**---------解除绑定关系------------**/
    /**
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "解除绑定关系", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/relieveBindRelation")
    @ResponseBody
    public RespObj relieveBindRelation(@RequestBody BindChildrenDTO dto){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            newVersionBindService.relieveBindRelation(getUserId(), dto.getChildren());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("手机管控解除成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 填写孩子手机号
     * @return
     */
    @ApiOperation(value = "填写孩子手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/makeOutRelation")
    @ResponseBody
    public RespObj makeOutRelation(String userKey){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            newVersionBindService.makeOutRelation(getUserId(), userKey);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("填写孩子手机号成功");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 移交关联关系
     * @param relationDTO
     * @return
     */
    @ApiOperation(value = "移交绑定关系", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/transferBindRelation")
    @ResponseBody
    public RespObj transferBindRelation(@RequestBody TransferUserRelationDTO relationDTO){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result=newVersionBindService.transferBindRelation(getUserId(),relationDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 再次改版老师选定哪些孩子移交
     * @param userIds
     * @return
     */
    @ApiOperation(value = "选定孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectUnbindChild")
    @ResponseBody
    public RespObj selectUnbindChild(String userIds,@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        newVersionBindService.selectUnbindChild(userIds,getUserId(),communityId);
        respObj.setMessage("选定孩子成功");
        return respObj;
    }


    /**
     * 查询老师选定孩子的列表
     * @return
     */
    @ApiOperation(value = "查询老师选定孩子的列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/searchUnbindChildren")
    @ResponseBody
    public RespObj searchUnbindChildren(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<RecordUserUnbindDTO> dtos = newVersionBindService.searchUnbindChildren(getUserId());
        respObj.setMessage(dtos);
        return respObj;
    }

    /**
     * 删除选定孩子
     * @param unBindId
     * @return
     */
    @ApiOperation(value = "删除选定孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeUnbindChild")
    @ResponseBody
    public RespObj removeUnbindChild(@ObjectIdType ObjectId unBindId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        newVersionBindService.removeUnbindChild(unBindId);
        respObj.setMessage("删除选定孩子成功");
        return respObj;
    }

    /**
     * 删除选定孩子
     * @param importId
     * @return
     */
    @ApiOperation(value = "删除家长接收的孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeParentImport")
    @ResponseBody
    public RespObj removeParentImport(@ObjectIdType ObjectId importId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        newVersionBindService.removeParentImport(importId);
        respObj.setMessage("删除家长接收的孩子成功");
        return respObj;
    }


    /**
     * 家长填写要接收的孩子
     * @param userKey
     * @param nickName
     * @return
     */
    @ApiOperation(value = "家长填写要接收的孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/makeOutParentReceiveChildren")
    @ResponseBody
    public RespObj makeOutParentReceiveChildren(String userKey,String nickName){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            newVersionBindService.makeOutParentReceiveChildren(userKey, nickName,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("家长填写要接收的孩子成功");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 家长查询要接收的孩子列表
     * @return
     */
    @ApiOperation(value = "家长查询要接收的孩子列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/searchReceiveChildren")
    @ResponseBody
    public RespObj searchReceiveChildren(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<RecordParentImportDTO> dtos = newVersionBindService.searchReceiveChildren(getUserId());
        respObj.setMessage(dtos);
        return respObj;
    }


    /**
     *
     * @param receiveKeyId
     * @return
     */
    @ApiOperation(value = "家长接收孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/receiveChildren")
    @ResponseBody
    public RespObj receiveChildren(@ObjectIdType ObjectId receiveKeyId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.receiveChildren(receiveKeyId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("家长接收孩子成功");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param bindId
     * @param provinceName
     * @param regionName
     * @param regionAreaName
     * @param schoolName
     * @param nickName
     * @param relation
     * @param gradeType
     * @return
     */
    @ApiOperation(value = "完善绑定的信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/completeBindInfo")
    @ResponseBody
    public RespObj completeBindInfo(
            @ApiParam(name = "bindId", required = true, value = "绑定Id") @ObjectIdType ObjectId bindId,
            @RequestParam(required = false,defaultValue = "") String provinceName,
            @RequestParam(required = false,defaultValue = "") String regionName,
            @RequestParam(required = false,defaultValue = "") String regionAreaName,
            @RequestParam(required = false,defaultValue = "") String schoolName,
            @RequestParam(required = false,defaultValue = "") String nickName,
            @RequestParam(required = false,defaultValue = "-1") int relation,
            @RequestParam(required = false,defaultValue = "-1") int gradeType
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.completeBindInfo(bindId, provinceName, regionName, regionAreaName, schoolName, gradeType,
                    nickName, relation);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("完善信息成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * \
     * @return
     */
    @ApiOperation(value = "获取填写的孩子手机号列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getMakeOutList")
    @ResponseBody
    public RespObj getMakeOutList(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<MakeOutUserRealationDTO> dtos=newVersionBindService.getMakeOutList(getUserId());
        respObj.setMessage(dtos);
        return respObj;
    }


    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除手机号item", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeMakeOutItem")
    @ResponseBody
    public RespObj removeMakeOutItem(@ObjectIdType ObjectId id){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        newVersionBindService.removeByItemId(id);
        respObj.setMessage("删除手机号成功");
        return respObj;
    }


    /**
     *
     * @param userIds
     * @param communityIds
     * @return
     */
    @ApiOperation(value = "改版绑定孩子", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/bindCommunity")
    @ResponseBody
    public RespObj bindCommunity(String userIds,String communityIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.bindCommunity(userIds,communityIds,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定成功");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param userId
     * @param communityIds
     * @return
     */
    @ApiOperation(value = "解除孩子的绑定关系", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("relieveCommunityBindRelation")
    @ResponseBody
    public RespObj relieveCommunityBindRelation(@ObjectIdType ObjectId userId,String communityIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            newVersionBindService.relieveCommunityBindRelation(userId, communityIds);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 查询孩子所在的所有社区
     * @return
     */
    @ApiOperation(value = "查询孩子所在的所有社区", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("getUserBelongCommunities")
    @ResponseBody
    public RespObj getUserBelongCommunities(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<GroupOfCommunityDTO> dtos = newVersionBindService.getUserBelongCommunities(getUserId());
        respObj.setMessage(dtos);
        return respObj;
    }


    /**
     * 设置老数据家长和学生自动成为好友
     * @return
     */
    @ApiOperation(value = "设置老数据家长和学生自动成为好友", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("setParentAndChildrenAutoFriend")
    @ResponseBody
    public RespObj setParentAndChildrenAutoFriend(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        newVersionBindService.setParentAndChildrenAutoFriend();
        respObj.setMessage("设置老数据家长和学生自动成为好友成功");
        return respObj;
    }


}
