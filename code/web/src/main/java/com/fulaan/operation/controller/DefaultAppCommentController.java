package com.fulaan.operation.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.service.AppCommentService;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
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
 * Created by James on 2017/8/25.
 */
@Api(value = "作业相关接口")
@Controller
@RequestMapping("/jxmapi/appOperation")
public class DefaultAppCommentController extends BaseController {

    @Autowired
    private AppCommentService appCommentService;
    /**
     * 添加作业
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加作业", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 401, message = "未授权客户机访问数据"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCommentEntry")
    @ResponseBody
    public String addCommentEntry(@ApiParam @RequestBody AppCommentDTO dto){
        dto.setAdminId(getUserId().toString());
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String result = appCommentService.addCommentEntry(dto,dto.getComList());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            if("推送失败".equals(e.getMessage())) {
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("推送失败");
            }else{
                respObj.setErrorMessage("添加作业失败!");
            }
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前老师今天发布的作业
     * @return
     */
  /*  @ApiOperation(value = "查找当前老师今天发布的作业", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectListByTeacherId")
    @ResponseBody
    public String selectListByTeacherId(){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppCommentDTO> dtos = appCommentService.selectListByTeacherId(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前老师今天发布的作业失败!");
        }
        return JSON.toJSONString(respObj);
    }*/

    /**
     * 查找当前家长收到的作业
     * @return
     */
   /* @ApiOperation(value = "查找当前家长收到的作业", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectListFromParent")
    @ResponseBody
    public String selectListFromParent(){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppCommentDTO> dtos = appCommentService.selectListFromParent(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前家长收到的作业失败!");
        }
        return JSON.toJSONString(respObj);
    }*/
    /**
     * 查找当前作业签到的家长名单
     * @return
     */
    @ApiOperation(value = "查找当前作业签到的家长名单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectRecordList")
    @ResponseBody
    public String selectRecordList(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = appCommentService.selectRecordList(new ObjectId(id));
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前作业签到的家长名单失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前作业提交的学生名单
     * @return
     */
    @ApiOperation(value = "查找当前作业提交的学生名单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectStudentLoad")
    @ResponseBody
    public String selectStudentLoad(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id,@ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,@ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = appCommentService.selectStudentLoad(new ObjectId(id), page, pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前作业提交的学生名单失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 是否签到
     * @return
     */
    @ApiOperation(value = "是否签到", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/isSign")
    @ResponseBody
    public String isSign(@ApiParam(name = "date", required = true, value = "日期（yyyy-MM-dd）") @RequestParam("date") String date){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            Map<String,Object> result = appCommentService.isSign(getUserId(),dateTime);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("是否签到!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 签到
     * @return
     */
    @ApiOperation(value = "签到", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/goSign")
    @ResponseBody
    public String goSign(@ApiParam(name = "qid", required = true, value = "签到id") @RequestParam("qid") String qid){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = appCommentService.goSign(new ObjectId(qid),getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("签到失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前当前月份用户发放作业情况列表
     * @return
     */
    @ApiOperation(value = "查找当前当前月份用户发放作业情况列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectResultList")
    @ResponseBody
    public String selectResultList(@ApiParam(name = "month", required = true, value = "月份（MM）") @RequestParam("month") int month){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<String> dtos = appCommentService.selectResultList(month, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前当前月份用户发放作业情况名单失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前当前月份学生收到作业情况列表
     * @return
     */
    @ApiOperation(value = "查找当前当前月份学生收到作业情况列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectStudentResultList")
    @ResponseBody
    public String selectStudentResultList(@ApiParam(name = "month", required = true, value = "月份（MM）") @RequestParam("month") int month){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<String> dtos = appCommentService.selectStudentResultList(month, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前当前月份用户发放作业情况名单失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前点击的事件老师/家长发放作业情况列表
     * @return
     */
    @ApiOperation(value = "查找当前点击的事件老师/家长发放作业情况列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectDateList")
    @ResponseBody
    public String selectDateList(@ApiParam(name = "date", required = true, value = "日期（yyyy-MM-dd）") @RequestParam("date") String date){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            Map<String,Object> dtos = appCommentService.selectDateList(dateTime, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前点击的事件老师失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前点击的事件学生收到作业情况列表
     * @return
     */
    @ApiOperation(value = "查找当前点击的事件学生收到作业情况列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStuLit")
    @ResponseBody
    public String getStuLit(@ApiParam(name = "date", required = true, value = "日期（yyyy-MM-dd）") @RequestParam("date") String date){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            List<AppCommentDTO> dtos = appCommentService.getStuLit(dateTime, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查找当前点击的事件学生收到作业情况列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 根据作业id查找当前评论列表(带作业详情)
     * @return
     */
    @ApiOperation(value = "根据作业id查找当前评论列表(带作业详情)", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOperationList")
    @ResponseBody
    public String getOperationList(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id,
                                   @ApiParam(name = "role", required = true, value = "角色区") @RequestParam("role") int role,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            int label = 1;
            Map<String,Object> dtos = appCommentService.getOperationList(new ObjectId(id),role,label,getUserId(),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("根据作业id查找当前评论列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 根据通知id查找当前评论列表(带通知详情)
     * @return
     */
    @ApiOperation(value = "根据作业id查找当前评论列表(带作业详情)", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getNoticeList")
    @ResponseBody
    public String getNoticeList(@ApiParam(name = "id", required = true, value = "通知id") @RequestParam("id") String id,@ApiParam(name = "role", required = true, value = "角色区") @RequestParam("role") int role,int page,int pageSize){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> dtos = appCommentService.getNoticeList(new ObjectId(id),role,getUserId(),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("根据作业id查找当前评论列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 添加学生作业
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加学生作业", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping("/addOperationEntryFromStrudent")
    @ResponseBody
    public String addOperationEntryFromStrudent(@ApiParam(value = "contactId为作业id，role为3学生提交") @RequestBody AppOperationDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appCommentService.addOperationEntryFromStrudent(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加学生作业失败!");

        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加作业评论
     * @return
     */
    @ApiOperation(value = "添加作业评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping("/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为作业id，role为2学生评论区，role为1家长评论区") @RequestBody AppOperationDTO dto){
   /* public String addOperationEntry(@ApiParam(name = "id", required = true, value = "通知id") @RequestParam("id") String contactId,
                                    @ApiParam(name = "role", required = true, value = "角色区") @RequestParam("role") int role,
                                    @ApiParam(name = "description", required = true, value = "角色区") @RequestParam("role") String description){*/
        RespObj respObj=null;
        try {
            //todo(角色判断)
            //AppOperationDTO dto = new AppOperationDTO();
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appCommentService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加作业评论失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加作业二级评论
     */
    @ApiOperation(value="添加二级回复",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/addSecondOperation")
    @ResponseBody
    public String addSecondOperation(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为作业id，role为1家长评论区，role为2学生评论区") @RequestBody AppOperationDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            dto.setLevel(2);
            String result = appCommentService.addSecondOperation(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加作业二级评论失败!");

        }
        return JSON.toJSONString(respObj);

    }
    /**
     * 查询二级评论列表
     */
    @ApiOperation(value="查询二级评论列表",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectSecondList")
    @ResponseBody
    public String selectSecondList(@ApiParam(name = "parentId", required = true, value = "一级评论id") @RequestParam("parentId") String parentId){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppOperationDTO> result = appCommentService.getSecondList(new ObjectId(parentId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("分页查询二级评论列表失败!");

        }
        return JSON.toJSONString(respObj);

    }
    /**
     * 删除作业
     */
    @ApiOperation(value="删除作业",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/delAppCommentEntry")
    @ResponseBody
    public String delAppCommentEntry(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
           appCommentService.delAppcommentEntry(new ObjectId(id),getUserId());
           respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除失败!");

        }


        return JSON.toJSONString(respObj);
    }



    /**
     * 删除评论
     */
    @ApiOperation(value="删除评论",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/delAppOperationEntry")
    @ResponseBody
    public String delAppOperationEntry(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id,
                                       @ApiParam(name = "pingId", required = true, value = "评论id") @RequestParam("pingId") String pingId,
                                       @ApiParam(name = "role",required = true,value= "role=1,家长讨论区，role=2学生提问区，role=3学生提交区")@RequestParam("role") int role){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            appCommentService.delAppOperationEntry(new ObjectId(id),new ObjectId(pingId),role);
            respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除失败!");

        }


        return JSON.toJSONString(respObj);
    }


    /**
     * 编辑作业后添加
     */
    @ApiOperation(value="编辑作业后添加",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/updateEntry")
    @ResponseBody
    public String updateEntry(@RequestBody AppCommentDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            appCommentService.updateEntry(dto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            if("推送失败".equals(e.getMessage())) {
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(e.getMessage());
            }else {
                respObj.setMessage("查询暂不发布的作业失败!");
            }
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询暂不发布的作业
     */
    @ApiOperation(value="查询作业详情",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectCommentDesc")
    @ResponseBody
    public String selectCommentDesc(@ApiParam(name="id",value="作业id") @RequestParam(value="id") String id){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            AppCommentDTO result = appCommentService.selectAppCommentEntry(new ObjectId(id));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("查询暂不发布的作业失败!");

        }
        return JSON.toJSONString(respObj);

    }

    /**
     * 查询老师绑定的学科
     */
    @ApiOperation(value="查询老师绑定的学科",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectTeacherSubjectList")
    @ResponseBody
    public String selectTeacherSubjectList(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<SubjectClassDTO> result = appCommentService.selectTeacherSubjectList(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("查询老师绑定的学科失败!");

        }
        return JSON.toJSONString(respObj);

    }




    //community/myCommunitys/page/pageSize/?platform=app
}
