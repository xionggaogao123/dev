package com.fulaan.appvote.controller;

import com.fulaan.appvote.dto.AppNewVoteDTO;
import com.fulaan.appvote.dto.AppVoteOptionDTO;
import com.fulaan.appvote.service.AppNewVoteService;
import com.fulaan.base.BaseController;
import com.fulaan.integral.service.IntegralSufferService;
import com.pojo.integral.IntegralType;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-10-29.
 */
@Api(value="新投票")
@Controller
@RequestMapping("/jxmapi/appnewvote")
public class AppNewVoteController extends BaseController {
    @Autowired
    private AppNewVoteService appNewVoteService;
    @Autowired
    private IntegralSufferService integralSufferService;

    private static final Logger logger = Logger.getLogger(AppNewVoteController.class);

    @ApiOperation(value = "保存投票记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveNewAppVote")
    @ResponseBody
    public RespObj saveNewAppVote(@RequestBody AppNewVoteDTO appNewVoteDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteDTO.setUserId(getUserId().toString());
            String score = appNewVoteService.saveNewAppVote(appNewVoteDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(score);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
            logger.error("error",e);
        }
        return respObj;
    }

    public static void main(String[] args){
        AppNewVoteService appNewVoteService = new AppNewVoteService();
        AppNewVoteDTO appNewVoteDTO = new AppNewVoteDTO();
        appNewVoteDTO.setTitle("自主投票");
        appNewVoteDTO.setContent("内容");
        appNewVoteDTO.setType(1);
        List<String> communityIds = new ArrayList<String>();
        communityIds.add("5bd94c821f7e9d1fccc1e0c9");
        appNewVoteDTO.setCommunityList(communityIds);
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(2);
        integers.add(3);
        appNewVoteDTO.setVoteTypeList(integers);
        appNewVoteDTO.setVoteCount(3);
        appNewVoteDTO.setSign(1);
        appNewVoteDTO.setOpen(1);
        appNewVoteDTO.setVoteStartTime("2018-10-31 14:00:00");
        appNewVoteDTO.setVoteEndTime("2018-11-08 14:00:00");
        appNewVoteDTO.setUrlType(0);
        appNewVoteDTO.setUserId("58f6bea2de04cb5a4bc72d38");
        appNewVoteService.saveNewAppVote(appNewVoteDTO,new ObjectId("58f6bea2de04cb5a4bc72d38"));
    }



    @ApiOperation(value = "家长查询投票列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getVoteList")
    @ResponseBody
    public RespObj getVoteList(@RequestParam(value="page",defaultValue = "1") int page,
                               @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                               @RequestParam(value="keyword",defaultValue = "") String keyword,
                               @RequestParam(value="communityId",defaultValue = "") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = appNewVoteService.getVoteList(getUserId(), communityId, keyword, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "学生查询投票列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getStudentVoteList")
    @ResponseBody
    public RespObj getStudentVoteList(@RequestParam(value="page",defaultValue = "1") int page,
                               @RequestParam(value="pageSize",defaultValue = "10") int pageSize,
                               @RequestParam(value="keyword",defaultValue = "") String keyword,
                               @RequestParam(value="communityId",defaultValue = "") String communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = appNewVoteService.getStudentVoteList(getUserId(), communityId, keyword, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }


    @ApiOperation(value = "查询投票详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getOneVote")
    @ResponseBody
    public RespObj getOneVote(@RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = appNewVoteService.getOneVote(getUserId(),new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }


    @ApiOperation(value = "投票", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/voteMyOption")
    @ResponseBody
    public RespObj voteMyOption( @RequestParam(value="id",defaultValue = "") String id,
                                 @RequestParam(value="optionIds",defaultValue = "") String optionIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteService.voteMyOption(getUserId(), new ObjectId(id),optionIds);
            int score = integralSufferService.addIntegral(getUserId(), IntegralType.vote,4,1);
            respObj.setMessage(score+"");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "报名", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/applyMyOption")
    @ResponseBody
    public RespObj applyMyOption( @RequestParam(value="id",defaultValue = "") String id,
                                  @RequestParam(value="description",defaultValue = "") String description){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteService.applyMyOption(getUserId(), new ObjectId(id),description);
            respObj.setMessage("报名成功");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "撤销报名", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteMyOption")
    @ResponseBody
    public RespObj deleteMyOption(@RequestParam(value="id",defaultValue = "") String id,@RequestParam(value="optionId",defaultValue = "") String optionId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteService.deleteMyOption(new ObjectId(id), new ObjectId(optionId),getUserId());
            respObj.setMessage("撤销成功");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "查询报名", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectMyOption")
    @ResponseBody
    public RespObj selectMyOption( @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            AppVoteOptionDTO appVoteOptionDTO = appNewVoteService.selectMyOption(getUserId(), new ObjectId(id));
            respObj.setMessage(appVoteOptionDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "更换选项", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateOption")
    @ResponseBody
    public RespObj updateOption( @RequestParam(value="id",defaultValue = "") String id,
                                 @RequestParam(value="selectOptionIds",defaultValue = "") String selectOptionIds){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteService.updateOption(new ObjectId(id), selectOptionIds);
            respObj.setMessage("保存成功");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "查询选项", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectOptionList")
    @ResponseBody
    public RespObj selectOptionList( @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> map = appNewVoteService.selectOptionList(getUserId(),new ObjectId(id));
            respObj.setMessage(map);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }


    @ApiOperation(value = "投票名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectUserList")
    @ResponseBody
    public RespObj selectUserList( @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> mapList =  appNewVoteService.selectUserList(new ObjectId(id));
            respObj.setMessage(mapList);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

    @ApiOperation(value = "删除投票", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteVote")
    @ResponseBody
    public RespObj deleteVote( @RequestParam(value="id",defaultValue = "") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appNewVoteService.deleteVote(new ObjectId(id),getUserId());
            respObj.setMessage("删除投票成功");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }

}
