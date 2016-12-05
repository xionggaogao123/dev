package com.fulaan.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.dto.CommunityDTO;
import com.fulaan.dto.GroupDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.GroupService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jerry on 2016/10/25.
 * 二维码入口
 */
@Controller
@RequestMapping("/qr")
public class QRController extends BaseController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;



    /**
     * 群组二维码入口
     *
     * @param request
     * @param response
     */
    @RequestMapping("/group/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj groupHandle(@PathVariable @ObjectIdType ObjectId id, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (getUserId() == null) {
            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
            return null;
        }

        GroupDTO groupDTO = groupService.findByObjectId(id);
        CommunityDTO community = communityService.getByEmChatId(groupDTO.getEmChatId());

        if (groupDTO.isBindCommunity() && community != null) {
            groupDTO.setSearchId(community.getSearchId());
            groupDTO.setHeadImage(community.getLogo());
            groupDTO.setName(community.getName());
        }

        MemberDTO head = memberService.getHead(new ObjectId(groupDTO.getId()));

        MemberDTO mine = memberService.getUser(id, getUserId());
        groupDTO.setCount(memberService.countMember(id));
        groupDTO.setMine(mine);

        int count = memberService.countMember(new ObjectId(groupDTO.getId()));
        boolean isJoin = memberService.isGroupMember(new ObjectId(groupDTO.getId()), getUserId());

        if (groupDTO.isBindCommunity()) {
            CommunityDTO communityDTO = new CommunityDTO();
            communityDTO.setId(groupDTO.getId());
            communityDTO.setGroupId(groupDTO.getId());
            communityDTO.setHead(head);
            communityDTO.setHeadImage(groupDTO.getHeadImage());
            communityDTO.setLogo(community.getLogo());
            communityDTO.setName(groupDTO.getName());
            communityDTO.setCreateTime(DateUtils.timeStampToStr(new ObjectId(groupDTO.getId()).getTimestamp()));
            communityDTO.setMemberCount(count);
            communityDTO.setEmChatId(groupDTO.getEmChatId());
            communityDTO.setIsJoin(isJoin);
            return RespObj.SUCCESS(communityDTO);
        } else {

            CommunityDTO communityDTO = new CommunityDTO();
            communityDTO.setId(groupDTO.getId());
            communityDTO.setGroupId(groupDTO.getId());
            communityDTO.setHead(head);
            communityDTO.setLogo(groupDTO.getHeadImage());
            communityDTO.setHeadImage(groupDTO.getHeadImage());
            communityDTO.setName(groupDTO.getName());
            communityDTO.setCreateTime(DateUtils.timeStampToStr(new ObjectId(groupDTO.getId()).getTimestamp()));
            communityDTO.setMemberCount(count);
            communityDTO.setEmChatId(groupDTO.getEmChatId());
            communityDTO.setIsJoin(isJoin);
            return RespObj.SUCCESS(communityDTO);
        }
    }

    /**
     * 个人二维码入口
     *
     * @param request
     * @param response
     */
    @RequestMapping("/person/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj personHandle(@PathVariable @ObjectIdType ObjectId id,HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
//        if (getUserId() == null) {
//            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
//            return null;
//        }

        UserEntry userEntry=userService.find(id);
        Map<String,String> map=new HashMap<String,String>();
        map.put("userId",userEntry.getID().toString());
        map.put("nickName",StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
        map.put("avator", AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        return RespObj.SUCCESS(map);
    }

    @RequestMapping("/community/{communityId}")
    @SessionNeedless
    @ResponseBody
    public RespObj communityHandle(@PathVariable @ObjectIdType ObjectId communityId,
                                   HttpServletResponse response) throws IOException {
        if (getUserId() == null) {
            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
            return null;
        }
        CommunityDTO communityDTO = communityService.findByObjectId(communityId);
        if (communityDTO == null) {
            return RespObj.FAILD("没有此社区");
        }
        ObjectId userId = getUserId();
        if (null != userId) {
            if (memberService.isGroupMember(new ObjectId(communityDTO.getGroupId()), userId)) {
                communityDTO.setIsJoin(true);
            } else {
                communityDTO.setIsJoin(false);
            }
        }
        MemberDTO head = memberService.getHead(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setHead(head);
        int count = memberService.countMember(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setMemberCount(count);
        return RespObj.SUCCESS(communityDTO);
    }

}
