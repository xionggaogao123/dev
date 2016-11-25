package com.fulaan.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.dto.CommunityDTO;
import com.fulaan.dto.CommunityDetailDTO;
import com.fulaan.dto.GroupDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.pojo.GroupAnnounceDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.GroupService;
import com.fulaan.service.MemberService;
import com.fulaan.util.DateUtils;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    /**
     * 群组二维码入口
     *
     * @param request
     * @param response
     */
    @RequestMapping("/group/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj groupHandle(@PathVariable @ObjectIdType ObjectId id, HttpServletRequest request, HttpServletResponse response) {


        if (getUserId() == null) {

            try {
                response.sendRedirect("http://a.app.qq.com/o/simple.jsp?pkgname=com.fulan.mall");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GroupDTO groupDTO = groupService.findByObjectId(id);
        CommunityDTO community = communityService.getByGroupId(groupDTO.getEmChatId());

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
    public void personHandle(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping("/community/{communityId}")
    @SessionNeedless
    @ResponseBody
    public RespObj communityHandle(@PathVariable @ObjectIdType ObjectId communityId, HttpServletResponse response) {


        if (getUserId() == null) {

            try {
                response.sendRedirect("http://a.app.qq.com/o/simple.jsp?pkgname=com.fulan.mall");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }


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
