package com.fulaan.indexpage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.indexpage.service.IndexPageService;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2017/9/28.
 */
@Controller
@RequestMapping("/web/pageIndex")
@Api(value="大人端首页加载")
public class IndexPageController extends BaseController {
    @Autowired
    private IndexPageService indexPageService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "首页加载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getIndexList")
    @ResponseBody
    public String getIndexList(@ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                               @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            ObjectId userId = getUserId();
            CommunityDTO fulanDto = communityService.getCommunityByName("复兰大学");
            if (null == userId && null != fulanDto) {

            } else {
                if (null != fulanDto) {
                    //加入复兰大学
                    joinFulaanCommunity(getUserId(), new ObjectId(fulanDto.getGroupId()),new ObjectId(fulanDto.getId()));
                }
            }
            Map<String,Object> mlist =  indexPageService.getIndexList(getUserId(), page, pageSize);
            respObj.setMessage(mlist);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("首页加载失败");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 加入社区但是不加入环信群组---这里只有复兰社区调用
     * 加入复兰社区--- 复兰社区很特殊，特殊对待
     *
     * @param userId
     * @param communityId
     * @return
     */
    private void joinFulaanCommunity(ObjectId userId, ObjectId groupId,ObjectId communityId) {

        //type=1时，处理的是复兰社区
        if (memberService.isGroupMember(groupId, userId)) {
            return;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {

        } else {
            //新人
            communityService.pushToUser(communityId, userId, 3);
            memberService.saveMember(userId, groupId, 0);
        }
    }
    @ApiOperation(value = "添加首页list", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addIndexList")
    @ResponseBody
    public String addIndexList(@ApiParam(name = "contactId", required = true, value = "关联对象id") @RequestParam("contactId") String contactId,
                               @ApiParam(name = "type", required = true, value = "类型") @RequestParam("type") int type,
                               @ApiParam(name = "communityId", required = true, value = "社区id") @RequestParam("communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            indexPageService.addIndexPage(communityId, contactId, type,getUserId());
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改课程名失败");
        }
        return JSON.toJSONString(respObj);
    }



    @ApiOperation(value = "获取往期热点话题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getHotTopicList")
    @ResponseBody
    public String getHotTopicList(@ApiParam(name = "page", required = true, value = "page") @RequestParam("page") int page,
                                  @ApiParam(name = "pageSize", required = true, value = "pageSize") @RequestParam("pageSize") int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> map = indexPageService.getHotTopicList2(getUserId(),page, pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取往期热点话题失败");
        }
        return JSON.toJSONString(respObj);
    }

}
