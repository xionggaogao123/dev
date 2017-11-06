package com.fulaan.controller;

import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
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
@Api(value="二维码入口")
@Controller
@RequestMapping("/jxmapi/qr")
public class DefaultQRController extends BaseController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    private NewVersionBindRelationDao newVersionBindRelationDao
            = new NewVersionBindRelationDao();

    private NewVersionUserRoleDao newVersionUserRoleDao
            =new NewVersionUserRoleDao();


    /**
     * 群组二维码入口
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "群组二维码入口", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/group/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj groupHandle(@ApiParam(name = "id", required = true, value = "id") @PathVariable @ObjectIdType ObjectId id, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (getUserId() == null) {
            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
            return null;
        }

        GroupDTO groupDTO = groupService.findById(id,getUserId());
        CommunityDTO community = communityService.getByEmChatId(groupDTO.getEmChatId());

        if (groupDTO.isBindCommunity() && community != null) {
            groupDTO.setSearchId(community.getSearchId());
            groupDTO.setHeadImage(community.getLogo());
            groupDTO.setName(community.getName());
        }

        MemberDTO head = memberService.getHead(new ObjectId(groupDTO.getId()));

        MemberDTO mine = memberService.getUser(id, getUserId());
        groupDTO.setCount(memberService.getMemberCount(id));
        groupDTO.setMine(mine);

        int count = memberService.getMemberCount(new ObjectId(groupDTO.getId()));
        boolean isJoin = memberService.isGroupMember(new ObjectId(groupDTO.getId()), getUserId());

        if (groupDTO.isBindCommunity() && community != null) {
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
     * @param response
     */
    @ApiOperation(value = "个人二维码入口", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/person/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj personHandle(@ApiParam(name = "id", required = true, value = "id") @PathVariable @ObjectIdType ObjectId id,
                                HttpServletResponse response) throws IOException {
        if (getUserId() == null) {
            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
            return null;
        }

        UserEntry userEntry = userService.findById(id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userEntry.getID().toString());
        map.put("nickName", StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        map.put("avator", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
        return RespObj.SUCCESS(map);
    }


    /**
     * 绑定接口
     * @param id
     * @param response
     * @return
     * @throws java.io.IOException
     */
    @ApiOperation(value = "绑定接口", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/personBind/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj personBindHandle(@ApiParam(name = "id", required = true, value = "id") @PathVariable @ObjectIdType ObjectId id,
                                HttpServletResponse response) throws IOException {
        if (getUserId() == null) {
            response.sendRedirect(Constant.COLLECTION_MALL_MARKET_URL);
            return null;
        }

        UserEntry userEntry = userService.findById(id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userEntry.getID().toString());
        map.put("nickName", StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        map.put("avator", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
        //加入逻辑（绑定逻辑）
        //判断该用户是否是未激活的学生且已经登录的用户是家长


        NewVersionBindRelationEntry entry = newVersionBindRelationDao.getBindEntry(id);
        /**
         * 0:已绑定
         * 1:扫描的是学生
         * 2:条件符合
         * 3:扫描二维码的是老用户
         * 4:被扫描二维码的是老用户
         *
         * **/
        if(null!=entry) {
            map.put("isBind","0");
        }else{
            NewVersionUserRoleEntry household=newVersionUserRoleDao.getEntry(getUserId());
            NewVersionUserRoleEntry userRoleEntry=newVersionUserRoleDao.getEntry(id);
            if(null==household){
                map.put("isBind","3");
            }else if(null==userRoleEntry){
                map.put("isBind","4");
            }else {
                if(userRoleEntry.getNewRole()==Constant.TWO){
                    map.put("isBind","0");
                }else {
                    if (household.getNewRole() == Constant.ONE ||
                            household.getNewRole() == Constant.TWO) {
                        map.put("isBind", "1");
                    } else {
                        map.put("isBind", "2");
                    }
                }
            }
        }
        map.put("phone",userEntry.getMobileNumber());

        //检查是否生成GenerateUserCode
//        if (StringUtils.isBlank(userEntry.getGenerateUserCode())) {
//            //若code为空，则生成code
//            String packageCode=ObjectIdPackageUtil.getPackage(userEntry.getID());
//            userEntry.setGenerateUserCode(packageCode);
//            userService.addUser(userEntry);
//            map.put("packageCode",packageCode);
//        }else{
//            map.put("packageCode",userEntry.getGenerateUserCode());
//        }
        return RespObj.SUCCESS(map);
    }
    @ApiOperation(value = "communityHandle", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/community/{communityId}")
    @SessionNeedless
    @ResponseBody
    public RespObj communityHandle(@ApiParam(name = "communityId", required = true, value = "communityId") @PathVariable @ObjectIdType ObjectId communityId,
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
        int count = memberService.getMemberCount(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setMemberCount(count);
        return RespObj.SUCCESS(communityDTO);
    }

}