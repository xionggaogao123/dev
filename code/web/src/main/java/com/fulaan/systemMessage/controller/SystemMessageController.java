package com.fulaan.systemMessage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.WebAppCommentDTO;
import com.fulaan.systemMessage.dto.AppNewOperationDTO;
import com.fulaan.systemMessage.dto.SimpleUserDTO;
import com.fulaan.systemMessage.service.SystemMessageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
 * Created by James on 2018-05-22.
 */
@Controller
@RequestMapping("/web/systemMessage")
public class SystemMessageController extends BaseController {

    @Autowired
    private SystemMessageService systemMessageService;


    @ApiOperation(value = "发送特殊通知", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addEntry")
    @ResponseBody
    public String addEntry(@ApiParam @RequestBody WebAppCommentDTO wdto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            AppCommentDTO dto = wdto.getAppCommentDTO(wdto);
            ObjectId userId = new ObjectId("575e21be0cf2a633a9ff7b6b");
            dto.setAdminId(userId.toString());
            systemMessageService.addEntry(userId, dto);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送特殊通知失败");
        }
        return JSON.toJSONString(respObj);
    }


    @ApiOperation(value = "发送超级话题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addHotEntry")
    @ResponseBody
    public String addHotEntry(@ApiParam @RequestBody WebAppCommentDTO wdto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            AppCommentDTO dto = wdto.getAppCommentDTO(wdto);
            ObjectId userId = new ObjectId("575e21be0cf2a633a9ff7b6b");
            dto.setAdminId(userId.toString());
            systemMessageService.addHotEntry(userId, dto);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送特殊通知失败");
        }
        return JSON.toJSONString(respObj);
    }

    @ApiOperation(value = "设为精选留言", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addHotList")
    @ResponseBody
    public String addHotList(@ApiParam(name = "id", required = true, value = "评论id") @RequestParam("id") String id,
                             @ApiParam(name = "role", required = true, value = "role") @RequestParam("role") int role){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            systemMessageService.addHotList(new ObjectId(id), role);
            respObj.setMessage("设为精选留言成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("设为精选留言失败");
        }
        return JSON.toJSONString(respObj);
    }

    @ApiOperation(value = "滚屏展示", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addHotPing")
    @ResponseBody
    public String addHotPing(@ApiParam(name = "id", required = true, value = "话题id") @RequestParam("id") String id,
                             @ApiParam(name = "role", required = true, value = "role") @RequestParam("role") int role){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);

            systemMessageService.addHotPing(new ObjectId(id), role);
            respObj.setMessage("滚屏展示");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("滚屏展示失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 普通用户精选评论查询
     * @return
     */
    @ApiOperation(value = "精选评论查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOperationList")
    @ResponseBody
    public String getOperationList(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            int role = 2;//精选2
            Map<String,Object> dtos = systemMessageService.getOperationList(new ObjectId(id),role,getUserId(),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("根据作业id查找当前评论列表失败!");
        }
        return JSON.toJSONString(respObj);
    }
    
    /**
     * 用户留言查询
     * @return
     */
    @ApiOperation(value = "精选评论查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyOperationList")
    @ResponseBody
    public String getMyOperationList(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            int role = 2;//精选2
            Map<String,Object> dtos = systemMessageService.getMyOperationList(new ObjectId(id), role, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("根据作业id查找当前评论列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 所有评论查询
     * @return
     */
    @ApiOperation(value = "所有评论查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getAllOperationList")
    @ResponseBody
    public String getAllOperationList(@ApiParam(name = "id", required = true, value = "作业id") @RequestParam("id") String id,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            int role = 1;//非精选
            Map<String,Object> dtos = systemMessageService.getAllOperationList(new ObjectId(id),role, getUserId(),page,pageSize);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("根据作业id查找当前评论列表失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加一级评论
     * @return
     */
    @ApiOperation(value = "添加评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping("/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为作业id，role为2学生评论区，role为1家长评论区") @RequestBody AppNewOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            dto.setRole(1);
            systemMessageService.addOperationEntry(dto);
            respObj.setMessage("发表成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
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
    public String addSecondOperation(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为作业id，role为1家长评论区，role为2学生评论区") @RequestBody AppNewOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(2);
            String result = systemMessageService.addSecondOperation(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业二级评论失败!");

        }
        return JSON.toJSONString(respObj);

    }

    /**
     * 历史回顾
     * @return
     */
    @ApiOperation(value = "历史回顾", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getMyRoleCommunity")
    @ResponseBody
    public String getMyRoleCommunity(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //indexPageService.addIndexPage(communityId, contactId, type,getUserId());
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送特殊通知失败");
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
    public String delAppOperationEntry(@ApiParam(name = "id", required = true, value = "评论id") @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            systemMessageService.delAppOperationEntry(new ObjectId(id));
            respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除失败!");

        }


        return JSON.toJSONString(respObj);
    }

    @ApiOperation(value = "查询大V", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getBigList")
    @ResponseBody
    public RespObj getBigList(@ApiParam(value="communityIds")@RequestParam("communityIds") String communityIds){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<SimpleUserDTO> dtos =  systemMessageService.getBigList(communityIds, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询大V失败");
        }
        return respObj;
    }

    @ApiOperation(value = "发送消息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/sendMessage")
    @ResponseBody
    public RespObj sendMessage(@ApiParam(value="userIds")@RequestParam("userIds") String userIds,
                              @ApiParam(name="name",required = true,value = "名称")@RequestParam("name")String name,
                              @ApiParam(name="avatar",required = true,value = "头像")@RequestParam("avatar")String avatar,
                              @ApiParam(name="message",required = true,value = "消息")@RequestParam("message")String message){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            systemMessageService.sendText(name,avatar,getUserId(),userIds,message);
            respObj.setMessage("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送消息失败");
        }
        return respObj;
    }
    
    /**
     * 点赞
     * @return
     */
    @ApiOperation(value = "添加评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping("/updateZan")
    @ResponseBody
    public String updateZan( @RequestParam(value = "zan",defaultValue = "1") int zan,
                                     @RequestParam("id") String id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
           systemMessageService.updateZan(new ObjectId(id),getUserId(),zan);
            if(zan==0){
                respObj.setMessage("点赞成功");
            }else if(zan==1){
                respObj.setMessage("取消成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业评论失败!");
        }
        return JSON.toJSONString(respObj);
    }
}
