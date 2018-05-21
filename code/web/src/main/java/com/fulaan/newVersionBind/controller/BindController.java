package com.fulaan.newVersionBind.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.newVersionBind.dto.NewVersionBindRelationDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.service.AppCommentService;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.TeacherSubjectBindDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Created by scott on 2017/9/5.
 */
@Api(value = "app家长孩子绑定逻辑")
@Controller
@RequestMapping("/web/bind")
public class BindController extends BaseController {



    @Autowired
    private NewVersionBindService newVersionBindService;

    @Autowired
    private AppCommentService appCommentService;


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
            newVersionBindService.addCommunityBindEntry(userIds,communityId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
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
    *
    * @param parentId
    * @param studentId
    * @return
    */
   @ApiOperation(value = "个人中心解除绑定", httpMethod = "POST", produces = "application/json")
   @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
           @ApiResponse(code = 500, message = "服务器不能完成请求")})
   @RequestMapping("/delEntryByPhone")
   @ResponseBody
   public RespObj delNewVersionEntryByPhone(@ApiParam(name = "phone", required = true, value = "电话号码") @RequestParam("phone") String phone){
       RespObj respObj = new RespObj(Constant.FAILD_CODE);
       try{
           NewVersionBindRelationEntry n = newVersionBindService.getUserEntryByPhone(phone);

           newVersionBindService.delNewVersionEntry(n.getMainUserId(), n.getUserId());
           respObj.setCode(Constant.SUCCESS_CODE);
           respObj.setMessage("解除绑定成功!");
  
           
       }catch (Exception e){
           respObj.setMessage(e.getMessage());
       }
       return respObj;
   }
    
    


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

    /**
     * 多选添加虚拟学生
     * @param thirdName
     * @param number
     * @param communityIds
     * @return
     */
    @ApiOperation(value = "多选绑定社区下的虚拟学生", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addMoreBindVirtualCommunity")
    @ResponseBody
    public RespObj addMoreBindVirtualCommunity(@RequestParam(value="thirdName") String thirdName,@RequestParam(value="number") String number,@RequestParam(value="communityIds") String communityIds){
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

}
