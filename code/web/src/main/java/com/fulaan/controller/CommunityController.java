package com.fulaan.controller;


import com.easemob.server.EaseMobAPI;
import com.easemob.server.comm.wrapper.ResponseWrapper;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.cache.RedisUtils;
import com.fulaan.dto.*;
import com.fulaan.pojo.CommunityMessage;
import com.fulaan.pojo.PageModel;
import com.fulaan.pojo.ProductModel;
import com.fulaan.service.*;
import com.fulaan.user.service.UserService;
import com.fulaan.util.GetImage;
import com.fulaan.util.QRUtils;
import com.fulaan.util.URLParseUtil;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.pojo.activity.FriendApply;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.fcommunity.ConcernEntry;
import com.pojo.fcommunity.PartInContentEntry;
import com.pojo.fcommunity.RemarkEntry;
import com.pojo.user.*;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jerry on 2016/10/24.
 * 社区Controller
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private FriendApplyService friendApplyService;
    @Autowired
    private ConcernService concernService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private EmService emService;
    @Autowired
    private CommunitySystemInfoService communitySystemInfoService;

    public static final String suffix = "/static/images/community/upload.png";

    @RequestMapping("/create")
    @ResponseBody
    public RespObj createCommunity(HttpServletRequest request,
                                   String name,
                                   @RequestParam(required = false, defaultValue = "") String desc,
                                   @RequestParam(required = false, defaultValue = "") String logo,
                                   @RequestParam(required = false, defaultValue = "0") int open,
                                   @RequestParam(required = false, defaultValue = "") String userIds) throws Exception {

        //先判断该社区名称是否使用过
        boolean flag = communityService.judgeCommunityCreate(name);
        if (flag) {
            return RespObj.FAILD("该社区名称已使用过！");
        }
        ObjectId uid = getUserId();

        ObjectId communityId = new ObjectId();

        String qrUrl = QRUtils.getCommunityQrUrl(communityId);
        if (qrUrl == null) {
            return RespObj.FAILD("无法生成二维码");
        }
        String seqId = communityService.getSeq();

        if (StringUtils.isBlank(seqId)) {
            return RespObj.FAILD("社区序列值不够");
        }

        if (StringUtils.isBlank(logo)) {
            String prev = "http://www.fulaan.com";
            logo = prev + suffix;
        }

        ObjectId commId = communityService.createCommunity(communityId, uid, name, desc, logo, qrUrl, seqId, open);
        communityService.pushToUser(commId, uid, 2);

        CommunityDTO communityDTO = communityService.findByObjectId(commId);
        //创建社区系统消息通知
        communitySystemInfoService.saveOrupdateEntry(getUserId(),getUserId(),"社长",4,commId);
        communitySystemInfoService.saveOrupdateEntry(getUserId(),getUserId(),"社长",5,commId);
        if (StringUtils.isNotBlank(userIds)) {
            GroupDTO groupDTO = groupService.findByObjectId(new ObjectId(communityDTO.getGroupId()));
            String[] userList = userIds.split(",");

            for (String userId : userList) {

                if(emService.addUserToEmGroup(groupDTO.getEmChatId(),new ObjectId(userId))){
                    memberService.saveMember(new ObjectId(userId),new ObjectId(groupDTO.getId()),0);
                    communityService.pushToUser(communityId, new ObjectId(userId), 1);
                }

            }
        }

        int memberCount = memberService.countMember(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setMemberCount(memberCount);

        List<MemberDTO> members = memberService.getMembers(new ObjectId(communityDTO.getGroupId()), 20);
        communityDTO.setMembers(members);

        MemberDTO mine = memberService.getUser(new ObjectId(communityDTO.getGroupId()), getUserId());
        communityDTO.setMine(mine);

        String headImage = groupService.getHeadImage(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setHeadImage(headImage);

        return RespObj.SUCCESS(communityDTO);
    }


    @RequestMapping("/create2")
    @ResponseBody
    public RespObj createCommunity2(HttpServletRequest request,
                                    String name,
                                    @RequestParam(required = false, defaultValue = "") String desc,
                                    @RequestParam(required = false, defaultValue = "") String logo,
                                    @RequestParam(required = false, defaultValue = "0") int open,
                                    @RequestParam(required = false, defaultValue = "") String userIds) throws Exception {

        //先判断该社区名称是否使用过
        boolean flag = communityService.judgeCommunityCreate(name);
        if (flag) {
            CommunityDTO communityDTO = new CommunityDTO();
            communityDTO.setErrorMsg("该社区名称已使用过！");
            return RespObj.FAILD(communityDTO);
        }
        ObjectId uid = getUserId();

        ObjectId communityId = new ObjectId();

        String qrUrl = QRUtils.getCommunityQrUrl(communityId);
        String seqId = communityService.getSeq();

        if (StringUtils.isBlank(seqId)) {
            CommunityDTO communityDTO = new CommunityDTO();
            communityDTO.setErrorMsg("社区序列值不够");
            return RespObj.FAILD(communityDTO);
        }

        if (StringUtils.isBlank(logo)) {
            String prev = "http://www.fulaan.com";
            logo = prev + suffix;
        }

        ObjectId commId = communityService.createCommunity(communityId, uid, name, desc, logo, qrUrl, seqId, open);
        communityService.pushToUser(commId, uid, 2);

        CommunityDTO communityDTO = communityService.findByObjectId(commId);


        if (StringUtils.isNotBlank(userIds)) {
            GroupDTO groupDTO = groupService.findByObjectId(new ObjectId(communityDTO.getGroupId()));
            String[] userList = userIds.split(",");

            for (String userId : userList) {

                if(emService.addUserToEmGroup(groupDTO.getEmChatId(),new ObjectId(userId))){
                    memberService.saveMember(new ObjectId(userId),new ObjectId(groupDTO.getId()),0);
                    communityService.pushToUser(communityId, new ObjectId(userId), 1);
                }

            }
        }

        int memberCount = memberService.countMember(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setMemberCount(memberCount);

        List<MemberDTO> members = memberService.getMembers(new ObjectId(communityDTO.getGroupId()), 20);
        communityDTO.setMembers(members);

        MemberDTO mine = memberService.getUser(new ObjectId(communityDTO.getGroupId()), getUserId());
        communityDTO.setMine(mine);

        String headImage = groupService.getHeadImage(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setHeadImage(headImage);

        return RespObj.SUCCESS(communityDTO);
    }


    /**
     * 获取社区详情
     *
     * @param id 社区id
     * @return
     */
    @RequestMapping(value = "/{id}")
    @SessionNeedless
    @ResponseBody
    public RespObj get(@PathVariable @ObjectIdType ObjectId id) {

        ObjectId groupId = communityService.getGroupId(id);
        CommunityDTO communityDTO = communityService.findByObjectId(id);
        List<MemberDTO> members = memberService.getMembers(groupId, 12);
        if (null != getUserId()) {
            MemberDTO mine = memberService.getUser(groupId, getUserId());
            communityDTO.setMine(mine);
        }
        communityDTO.setMembers(members);
        return RespObj.SUCCESS(communityDTO);
    }

    /**
     * 上传图片
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    @ResponseBody
    @SessionNeedless
    public RespObj uploadImage(MultipartRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();

            List<String> list = new ArrayList<String>(fileMap.keySet());
            if (list.size() == 0) {
                obj.setMessage("未上传图片");
                return obj;
            }
            ObjectId id = new ObjectId();
            MultipartFile file = fileMap.get(list.get(0));
            String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileKey = id.toString() + Constant.POINT + extensionName;
            QiniuFileUtils.uploadFile(fileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
            String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
            FileUploadDTO dto = new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
            fileInfos.add(dto);
            obj = new RespObj(Constant.SUCCESS_CODE, fileInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 邀请人员加入社区
     * 这里的逻辑是先
     *
     * @param communityId
     * @param userId
     * @return
     */
    @RequestMapping("/inviteMember")
    @ResponseBody
    public RespObj inviteMember(@ObjectIdType ObjectId communityId,
                                @ObjectIdType ObjectId userId) {
        ObjectId groupId = communityService.getGroupId(communityId);
        //判断该用户是否加过该社区
        if (memberService.isGroupMember(groupId, userId)) {
            return RespObj.FAILD("该用户已经是该社区成员了");
        } else {
            if (memberService.isBeforeMember(groupId, userId)) {
                memberService.updateMember(groupId, userId, 0);
            } else {
                memberService.saveMember(userId, groupId, 0);
            }
            communityService.pushToUser(communityId, userId, 1);
            return RespObj.SUCCESS("加人成员成功");
        }
    }

    /**
     * 社长移除 成员
     *
     * @param communityId
     * @param userId
     * @return
     */
    @RequestMapping("/removeMember")
    @ResponseBody
    public RespObj removeMember(@ObjectIdType ObjectId communityId,
                                @ObjectIdType ObjectId userId) {
        CommunityDTO communityDTO = communityService.findByObjectId(communityId);
        String groupId = communityDTO.getEmChatId();
        ResponseWrapper responseWrapper = EaseMobAPI.removeSingleUserFromChatGroup(groupId, userId.toString());
        if (responseWrapper.getResponseStatus() != 200) {
            return RespObj.FAILD("环信出错!");
        }
        memberService.deleteMember(new ObjectId(communityDTO.getGroupId()), userId);
        communityService.pullFromUser(new ObjectId(communityDTO.getId()), userId);
        return RespObj.SUCCESS("删除成员成功");
    }

    /**
     * 获得我的社区
     *
     * @return
     */
    @RequestMapping("/myCommunitys")
    @ResponseBody
    @SessionNeedless
    public RespObj getMyCommunitys() {
        try {
            ObjectId userId = getUserId();
            List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
            CommunityDTO fulanDto = communityService.getDefaultDto("复兰社区");
            if (null == userId) {
                if (null != fulanDto) {
                    communityDTOList.add(fulanDto);
                }
                return RespObj.SUCCESS(communityDTOList);
            } else {
                //登录状态
                //判断是否为复兰社区会员
                if (null != fulanDto) {
                    if (!memberService.isGroupMember(new ObjectId(fulanDto.getGroupId()), getUserId())) {
                        //加入复兰社区
                        joinCommunityWithEm(getUserId(), new ObjectId(fulanDto.getId()), 1);
                        communityService.pushToUser(new ObjectId(fulanDto.getId()), getUserId(), 2);
                    }
                }

                communityDTOList = communityService.getCommunitys(userId, 1, 9);

                return RespObj.SUCCESS(communityDTOList);
            }
        } catch (Exception e) {
            return RespObj.FAILD(e.getMessage());
        }
    }

    /**
     * 获取热门社区
     *
     * @return
     */
    @RequestMapping("/hotCommunitys")
    @ResponseBody
    @SessionNeedless
    public RespObj hotCommunitys() {
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(communityService.getOpenCommunityS(userId));
    }

    /**
     * 更新 name,desc,logo
     *
     * @param communityId
     * @param name
     * @param desc
     * @param logo
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public RespObj updateCommunity(@ObjectIdType ObjectId communityId,
                                   @RequestParam(required = false, defaultValue = "") String name,
                                   @RequestParam(required = false, defaultValue = "") String desc,
                                   @RequestParam(required = false, defaultValue = "") String logo,
                                   @RequestParam(required = false, defaultValue = "0") int open) {
        communityService.updateCommunity(communityId, name, desc, logo, open);
        return RespObj.SUCCESS;
    }

    /**
     * 新建消息
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/newMessage")
    @ResponseBody
    public RespObj newMessage(HttpServletRequest request, @RequestBody CommunityMessage message) {
        ObjectId uid = getUserId();
        communityService.saveMessage(uid, message);
        return RespObj.SUCCESS;
    }

    /**
     * 获取最新的消息
     *
     * @param page
     * @param pageSize
     * @param order
     * @return
     */
    @RequestMapping("/news")
    @ResponseBody
    @SessionNeedless
    public RespObj news(@RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(communityService.getNews(new ArrayList<ObjectId>(), page, pageSize, order, userId, 2));
    }

    /**
     * 获取当前页面中的最新社区详情数据
     *
     * @param communityId
     * @param page
     * @param pageSize
     * @param order
     * @return
     */
    @RequestMapping("/getCurrDetail")
    @ResponseBody
    @SessionNeedless
    public RespObj news(@ObjectIdType ObjectId communityId,
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        objectIds.add(communityId);
        return RespObj.SUCCESS(communityService.getNews(objectIds, page, pageSize, order, userId, 1));
    }

    /**
     * 消息
     *
     * @param communityId
     * @param page
     * @param pageSize
     * @param order
     * @return
     */
    @RequestMapping("/message")
    @ResponseBody
    public RespObj messages(@ObjectIdType ObjectId communityId,
                            @RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "10") int pageSize,
                            @RequestParam(required = false, defaultValue = "-1") int order) {
        return RespObj.SUCCESS(communityService.getMessages(communityId, page, pageSize, order));
    }

    /**
     * 参与社区活动接口
     *
     * @param communityDetailId
     * @return
     */
    @RequestMapping("/enterCommunityDetail")
    @ResponseBody
    public RespObj enterCommunityDetail(@ObjectIdType ObjectId communityId,
                                        @ObjectIdType ObjectId communityDetailId,
                                        @RequestParam(defaultValue = "1") int join,
                                        @RequestParam(defaultValue = "", required = false) String msg) {
        ObjectId userId = getUserId();
        if (join == 1) {
            CommunityDetailDTO communityDetailDTO = communityService.findDetailByObjectId(communityDetailId);
            List<String> partInList = communityDetailDTO.getPartInList();
            boolean flag = false;
            for (String item : partInList) {
                if (item.equals(userId.toString())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                return RespObj.FAILD("你已经参与了");
            }
            communityService.enterCommunityDetail(communityDetailId, userId);

            communityService.saveReplyDetailText(communityId, communityDetailId, userId, msg, 2);
        } else {
            communityService.cancelCommunityDetail(communityDetailId, userId);
            communityService.deleteReplyDetailText(communityDetailId, userId);
            return RespObj.SUCCESS("取消报名");
        }
        return RespObj.SUCCESS("报名成功");
    }

    /**
     * 参加活动的名单
     *
     * @param communityDetailId
     * @return
     */
    @RequestMapping("/joinActivitySheet")
    @ResponseBody
    public RespObj joinActivitySheet(@ObjectIdType ObjectId communityDetailId) {
        CommunityDetailDTO communityDetailDTO = communityService.findDetailByObjectId(communityDetailId);
        List<String> partInList = communityDetailDTO.getPartInList();
        List<User> users = new ArrayList<User>();
        for (String id : partInList) {
            UserDetailInfoDTO user = userService.getUserInfoById(id);
            User user1 = new User();
            user1.setImg(user.getImgUrl());
            user1.setName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
            user1.setId(user.getId());
            users.add(user1);
            PartInContentDTO partInContentDTO = communityService.getPartInContent(communityDetailId, new ObjectId(user.getId()));
            if (partInContentDTO != null)
                user1.setContent(partInContentDTO.getInformation());
        }
        return RespObj.SUCCESS(users);
    }

    /**
     * 详情
     *
     * @param communityDetailId
     * @return
     */
    @RequestMapping("/isEnterCommunityDetail")
    @ResponseBody
    public RespObj isEnterCommunityDetail(@ObjectIdType ObjectId communityDetailId) {
        ObjectId userId = getUserId();
        CommunityDetailDTO communityDetailDTO = communityService.findDetailByObjectId(communityDetailId);
        List<String> partInList = communityDetailDTO.getPartInList();
        boolean flag = false;
        for (String item : partInList) {
            if (item.equals(userId.toString())) {
                flag = true;
                break;
            }
        }
        return RespObj.SUCCESS(flag);
    }

    @RequestMapping("/join")
    @ResponseBody
    public RespObj joinCommunity(@ObjectIdType ObjectId communityId) {
        ObjectId userId = getUserId();
        if (joinCommunity(userId, communityId, 0)) {
            return RespObj.SUCCESS("操作成功!");
        } else {
            return RespObj.FAILD("该用户已经是该社区成员了");
        }

    }

    /**
     * 加入社区
     *
     * @param userId
     * @param communityId
     * @return
     */
    public boolean joinCommunity(ObjectId userId, ObjectId communityId, int type) {

        ObjectId groupId = communityService.getGroupId(communityId);
        GroupDTO groupDTO = groupService.findByObjectId(groupId);
        //type=1时，处理的是复兰社区
        if (type == 1) {
            if (memberService.isGroupMember(groupId, userId)) {
                return false;
            }
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {

            if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                memberService.updateMember(groupId, userId, 0);
                //设置先前该用户所发表的数据
                communityService.setPartIncontentStatus(communityId, userId, 0);
                communityService.pushToUser(communityId, userId, 1);
            }

        } else {

            if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                memberService.saveMember(groupId, userId, 0);
                communityService.pushToUser(communityId, userId, 1);
            }
        }
        return true;
    }

    /**
     * 加入社区但是不加入环信群组---这里只有复兰社区调用
     *
     * @param userId
     * @param communityId
     * @param type
     * @return
     */
    public boolean joinCommunityWithEm(ObjectId userId, ObjectId communityId, int type) {

        ObjectId groupId = communityService.getGroupId(communityId);
        //type=1时，处理的是复兰社区
        if (memberService.isGroupMember(groupId, userId)) {
            return false;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {
            memberService.updateMember(groupId, userId, 0);
            //设置先前该用户所发表的数据
            communityService.setPartIncontentStatus(communityId, userId, 0);
        } else {
            //新人
            communityService.pushToUser(communityId, userId, 1);
            memberService.saveMember(groupId, userId, 0);
        }
        return true;
    }


    /**
     * 退出的逻辑
     * 1.判断是否是群主
     * 2.退出环信
     *
     * @param communityId
     * @return
     */
    @RequestMapping("/quit")
    @ResponseBody
    public RespObj quitCommunity(@ObjectIdType ObjectId communityId) {

        ObjectId groupId = communityService.getGroupId(communityId);
        String emChatId = groupService.getEmchatIdByGroupId(groupId);
        ObjectId userId = getUserId();

        if (memberService.isHead(groupId, userId)) {
            return RespObj.FAILD("群主暂时无法退出");
        }else if(memberService.isManager(groupId,userId)){
            //当是副社长退出时
            List<ObjectId> objectIds=communityService.getAllMemberIds(groupId);
            communitySystemInfoService.addBatchData(userId,objectIds,"副社长",1,communityId);
        }

        if (emService.removeUserFromEmGroup(emChatId, userId)) {
            communityService.pullFromUser(communityId, userId);
            memberService.deleteMember(groupId, userId);
            //设置先前该用户所发表的数据为废弃掉的
            communityService.setPartIncontentStatus(communityId, userId, 1);
        }
        return RespObj.SUCCESS("操作成功!");
    }


    @RequestMapping("/search")
    @ResponseBody
    public RespObj searchCommunity(String relax) {
        return RespObj.SUCCESS(communityService.search(relax, getUserId()));
    }

    @RequestMapping("/communityPublish")
    @SessionNeedless
    @LoginInfo
    public String communityPublish(Map<String, Object> model) {
        String communityId = getRequest().getParameter("communityId");
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        ObjectId userId = getUserId();
        if (null != userId) {
            if (memberService.isManager(groupId, userId)) {
                model.put("operation", 1);
            } else {
                model.put("operation", 0);
            }
        }
        model.put("communityId", communityId);
        return "/community/communityPublish";
    }

    @RequestMapping("/communityAllType")
    @SessionNeedless
    @LoginInfo
    public String communityAllType(Map<String, Object> model) {
        return "/community/communityAllType";
    }

    @RequestMapping("/communityDetail")
    @SessionNeedless
    @LoginInfo
    public String communityMessage(@ObjectIdType ObjectId detailId, Map<String, Object> model) throws Exception{
        try {
            ObjectId uid = getUserId();
            CommunityDetailDTO detail = communityService.findDetailByObjectId(detailId);
            List<CommunityDTO> communitys = new ArrayList<CommunityDTO>();
            model.put("operation", false);
            //已读
            //判断是否已读，未读则添加进去
            if (null != uid) {
                boolean flag = true;
                List<String> unReadList = detail.getUnReadList();
                for (String item : unReadList) {
                    if (item.equals(uid.toString())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    communityService.pushRead(detailId, getUserId());
                }
                communitys = communityService.getCommunitys(uid, 1, 9);
                ObjectId groupId = communityService.getGroupId(new ObjectId(detail.getCommunityId()));
                if (memberService.isManager(groupId, getUserId())) {
                    model.put("operation", true);
                }
            } else {
                CommunityDTO fulanDto = communityService.getDefaultDto("复兰社区");
                if (null != fulanDto) {
                    communitys.add(fulanDto);
                }
            }
            model.put("detail", detail);
            model.put("detailId", detailId.toString());
            model.put("communitys", communitys);
            return "community/communityDetail";
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @RequestMapping("/myMessages")
    @ResponseBody
    public RespObj myMessages(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(communityService.getMyMessages(userId, page, pageSize, order));
    }

    @RequestMapping("/typeMessages")
    @ResponseBody
    @SessionNeedless
    public RespObj typeMessages(@RequestParam @ObjectIdType ObjectId communityId,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "4") int pageSize,
                                @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        objectIds.add(communityId);
        return RespObj.SUCCESS(communityService.getNews(objectIds, page, pageSize, order, userId, 1));
    }

    @RequestMapping("/getMessage")
    @ResponseBody
    @SessionNeedless
    public RespObj getMessage(@RequestParam @ObjectIdType ObjectId communityId,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "4") int pageSize,
                              @RequestParam(required = false, defaultValue = "-1") int order,
                              @RequestParam(required = false, defaultValue = "1") int type) {
        return RespObj.SUCCESS(communityService.getMessages(communityId, page, pageSize, order, type));
    }

    @RequestMapping("/getAllTypeMessage")
    @ResponseBody
    @SessionNeedless
    public RespObj getAllTypeMessage(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "4") int pageSize,
                                     @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        if (null == userId) {
            CommunityDTO fulanDTO = communityService.getDefaultDto("复兰社区");
            if (fulanDTO != null && fulanDTO.getId() != null) {
                List<ObjectId> cmmIds = new ArrayList<ObjectId>();
                cmmIds.add(new ObjectId(fulanDTO.getId()));
                return RespObj.SUCCESS(communityService.getNews(cmmIds, page, pageSize, order, userId, 1));
            }
            return RespObj.FAILD("没有数据");
        } else {
            List<ObjectId> communityIds = new ArrayList<ObjectId>();
            List<CommunityDTO> communityDTOList = communityService.getCommunitys(userId, -1, 0);
            for (CommunityDTO communityDTO : communityDTOList) {
                communityIds.add(new ObjectId(communityDTO.getId()));
            }
            return RespObj.SUCCESS(communityService.getNews(communityIds, page, pageSize, order, userId, 2));
        }
    }


    @RequestMapping("/judgeMember")
    @ResponseBody
    public RespObj judgeMember(@ObjectIdType ObjectId communityId) {
        ObjectId groupId = communityService.getGroupId(communityId);
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(memberService.isGroupMember(groupId, userId));
    }

    /**
     * 分享链接
     *
     * @param url
     * @return
     * @throws Exception
     */
    @RequestMapping("/shareUrl")
    @SessionNeedless
    @ResponseBody
    public RespObj getInfoByUrl(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            //抓取的数据
            ProductModel productModel = URLParseUtil.UrlParser(client, url);
            if (StringUtils.isBlank(productModel.getImageUrl())) {
                return RespObj.FAILD("解析不了该链接或者该链接无效");
            }
            return RespObj.SUCCESS(productModel);
        } catch (Exception e) {
            return RespObj.FAILD("解析不了该链接或者该链接无效");
        }
    }

    /**
     * 消息详情
     *
     * @param detailId
     * @return
     */
    @RequestMapping("/messageDetail")
    @ResponseBody
    @SessionNeedless
    public RespObj messageDetail(@ObjectIdType ObjectId detailId) {
        return RespObj.SUCCESS(communityService.findDetailByObjectId(detailId));
    }


    /**
     * 判断当前用户是否是该社区成员
     *
     * @param communityId
     * @return
     */
    @RequestMapping("/judge")
    @ResponseBody
    public RespObj judge(@ObjectIdType ObjectId communityId) {
        ObjectId userId = getUserId();
        ObjectId groupId = communityService.getGroupId(communityId);
        return RespObj.SUCCESS(memberService.isGroupMember(groupId, userId));
    }

    /**
     * 社区成员列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/communityMember")
    @LoginInfo
    public String getCommunityMember(Map<String, Object> model) {
        String communityId = getRequest().getParameter("communityId");
        model.put("communityId", communityId);

        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        if (memberService.isManager(groupId, getUserId())) {
            model.put("operation", 1);
        }
        return "/community/communityMember";
    }

    /**
     * 获取社区成员列表
     *
     * @param page
     * @param pageSize
     * @param communityId
     * @return
     */
    @RequestMapping("/getMemberList")
    @ResponseBody
    @SessionNeedless
    public RespObj getMemberList(@RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                 @ObjectIdType ObjectId communityId) {
        PageModel<MemberDTO> members = communityService.getMemberList(page, pageSize, getUserId(), communityId);
        return RespObj.SUCCESS(members);
    }

    /**
     * 获取我的玩伴列表信息
     *
     * @return
     */
    @RequestMapping("/getMyPartners")
    @ResponseBody
    @SessionNeedless
    public RespObj getMyPartners() {
        List<MemberDTO> members = communityService.getMyPartners(getUserId());
        return RespObj.SUCCESS(members);
    }


    /**
     * 设置备注名
     *
     * @param remarkId
     * @param endUserId
     * @param remark
     * @return
     */
    @RequestMapping("/setRemark")
    @ResponseBody
    public RespObj setRemark(String remarkId, String endUserId, String remark) {
        ObjectId userId = getUserId();
        if (StringUtils.isNotBlank(remarkId)) {
            communityService.updateRemark(new ObjectId(remarkId), remark);
        } else {
            RemarkEntry entry = new RemarkEntry(userId, new ObjectId(endUserId), remark);
            communityService.saveRemark(entry);
        }
        return RespObj.SUCCESS;
    }

    @RequestMapping("/communityMessageList")
    @SessionNeedless
    @LoginInfo
    public String communityMessageList(Map<String, Object> model) {
        String communityId = getRequest().getParameter("communityId");
        int type = Integer.parseInt(getRequest().getParameter("type"));
        model.put("communityId", communityId);
        model.put("type", type);
        return "/community/communityMessageList";
    }

    /**
     * 删除选中列表
     */
    @RequestMapping("/deleteMembers")
    @ResponseBody
    public RespObj deleteMembers(@ObjectIdType ObjectId communityId, String memberUserId, String memberId) {
        List<ObjectId> objectIds = getMembersId(memberId);
        List<ObjectId> userIds = getMembersId(memberUserId);
        CommunityDTO community = communityService.findByObjectId(communityId);

        ObjectId groupId = communityService.getGroupId(communityId);

        for (ObjectId userId : userIds) {
            if (!memberService.isManager(groupId, userId)) {
                communityService.deleteMembers(objectIds);
                return RespObj.SUCCESS;
            } else {
                return RespObj.FAILD("不能删除该社区社长");
            }
        }
        return RespObj.FAILD;
    }


    /**
     * 查询出副社长人数
     */
    @RequestMapping("/countSecondMember")
    @ResponseBody
    public RespObj countSecondMember(@ObjectIdType ObjectId communityId) {
        int count = communityService.countSecondMember(communityId);
        return RespObj.SUCCESS(count);
    }

    /**
     * 判断权限
     * 判断是否是管理员-包括社长，副社长
     *
     * @param communityId
     * @return
     */
    @RequestMapping("/judgeOperation")
    @ResponseBody
    public RespObj judgeOperation(@ObjectIdType ObjectId communityId) {
        ObjectId groupId = communityService.getGroupId(communityId);
        return RespObj.SUCCESS(memberService.isManager(groupId, getUserId()));
    }

    /**
     * 设为副社长
     * 1.判断是否是社长
     * 2.从userId里面剔除自己
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/setSecondMembers")
    @ResponseBody
    public RespObj setSecondMembers(@ObjectIdType ObjectId communityId, String memberUserId, String memberId, int role) {
        List<ObjectId> objectIds = getMembersId(memberId);
        List<ObjectId> userIds = getMembersId(memberUserId);
        ObjectId userId = getUserId();
        ObjectId groupId = communityService.getGroupId(communityId);

        if (!memberService.isHead(groupId, userId)) {
            return RespObj.FAILD("您没有权限");
        }

        CommunityDTO communityDTO = communityService.findByObjectId(communityId);

        if (userIds.contains(new ObjectId(communityDTO.getOwerId()))) {
            return RespObj.FAILD("不能设置社长为副社长");
        }
        communityService.setSecondMembers(objectIds, role);
        return RespObj.SUCCESS("操作成功");
    }


    /**
     * 参与人数
     *
     * @param detailId
     * @return
     */
    @RequestMapping("/partInUsers")
    @ResponseBody
    @SessionNeedless
    public RespObj partInUsers(@ObjectIdType ObjectId detailId) {
        CommunityDetailDTO communityDetailDTO = communityService.findDetailByObjectId(detailId);
        List<String> partInList = communityDetailDTO.getPartInList();
        List<User> users = new ArrayList<User>();
        for (String id : partInList) {
            UserDetailInfoDTO user = userService.getUserInfoById(id);
            User user1 = new User();
            user1.setImg(user.getImgUrl());
            user1.setName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
            user1.setId(user.getId());
            PartInContentDTO partInContentDTO = communityService.getPartInContent(detailId, new ObjectId(user.getId()));
            if (partInContentDTO != null) {
                user1.setContent(partInContentDTO.getInformation());
                user1.setTime(DateTimeUtils.convert(new ObjectId(partInContentDTO.getPartInContentId()).getTime(),
                        DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
            }

            users.add(user1);
        }
        return RespObj.SUCCESS(users);
    }

    public List<User> getPartInData(ObjectId detailId) {
        CommunityDetailDTO communityDetailDTO = communityService.findDetailByObjectId(detailId);
        List<String> partInList = communityDetailDTO.getPartInList();
        List<User> users = new ArrayList<User>();
        for (String id : partInList) {
            UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(id);
            User user1 = new User();
            user1.setName(StringUtils.isNotBlank(userDetailInfoDTO.getNickName()) ? userDetailInfoDTO.getNickName() : userDetailInfoDTO.getUserName());
            PartInContentDTO partInContentDTO = communityService.getPartInContent(detailId, new ObjectId(userDetailInfoDTO.getId()));
            if (partInContentDTO != null) {
                user1.setContent(partInContentDTO.getInformation());
                user1.setTime(DateTimeUtils.convert(new ObjectId(partInContentDTO.getPartInContentId()).getTime(),
                        DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
            }

            users.add(user1);
        }
        return users;
    }

    /**
     * 导出用户报名数据
     *
     * @param detailId
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalParamException
     */
    @RequestMapping("/exportPartInData")
    @ResponseBody
    public void exportPartInData(@ObjectIdType ObjectId detailId, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {

        List<User> users = getPartInData(detailId);
        String parentPath = request.getServletContext().getRealPath("/upload") + "/partInData/";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String suffix = new ObjectId().toString() + ".xls";
        File file = new File(parentPath, suffix);
        if (!file.exists()) {
            file.createNewFile();
        }
        exportData(file, users);
        response.setContentType("application/x-download");// 设置response内容的类型
        response.setHeader("Content-disposition", "attachment; filename=" + new String(suffix.getBytes("gb2312"), "iso8859-1"));// 设置头部信息

        InputStream inputStream = new FileInputStream(file);
        OutputStream outs = response.getOutputStream();// 获取文件输出IO流

        IOUtils.copy(inputStream, outs);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outs);
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportData(File file, List<User> users) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle style = wb.createCellStyle();
        //设置背景色
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillBackgroundColor(HSSFColor.BLUE.index);
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("报名列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名字");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("报名时间");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("报名备注");
        cell.setCellStyle(style);
        int page = 0;
        for (int i = 0; i < users.size(); i++) {
            User dto = users.get(i);
            page++;
            row = sheet.createRow(page);
            cell = row.createCell(0);
            cell.setCellValue(dto.getName());
            cell = row.createCell(1);
            cell.setCellValue(dto.getTime());
            cell = row.createCell(2);
            cell.setCellValue(dto.getContent());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            try {
                out.write(content);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    public class User {
        private String img;
        private String name;
        private String id;
        private String content;
        private String time;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }


    @RequestMapping("/replyToDetail")
    @ResponseBody
    public RespObj replyToDetail(@ObjectIdType ObjectId detailId,
                                 @ObjectIdType ObjectId communityId, int type,
                                 @RequestParam(defaultValue = "", required = false) String text,
                                 @RequestParam(defaultValue = "", required = false) String images,
                                 @RequestParam(defaultValue = "", required = false) String vedios,
                                 @RequestParam(defaultValue = "", required = false) String attachements) {

        ObjectId userId = getUserId();
        if (StringUtils.isNotBlank(text)) {
            communityService.saveReplyDetailText(communityId, detailId, userId, text, type);
        }
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 获取列表
     *
     * @param communityDetailId
     * @param page
     * @param pageSize
     * @param order
     * @return
     */
    @RequestMapping("/partInContent")
    @ResponseBody
    @SessionNeedless
    public RespObj partInContent(@ObjectIdType ObjectId communityDetailId,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(communityService.getPartInContent(userId, communityDetailId, page, pageSize));
    }


    /**
     * 判断是否为社长
     *
     * @param communityId
     * @return
     */
    @RequestMapping("/judgeManager")
    @ResponseBody
    public RespObj judgeCommunityManager(@ObjectIdType ObjectId communityId) {
        ObjectId groupId = communityService.getGroupId(communityId);
        return RespObj.SUCCESS(memberService.isHead(groupId, getUserId()));
    }

    /**
     * 对一条回复进行点赞
     *
     * @param partInContentId
     * @param zan
     * @return
     */
    @RequestMapping("/zanToPartInContent")
    @ResponseBody
    public RespObj zanToPartInContent(@ObjectIdType ObjectId partInContentId,
                                      @RequestParam(defaultValue = "1", required = false) int zan) {
        Boolean message = communityService.ZanToPartInContent(partInContentId, getUserId(), zan);
        return RespObj.SUCCESS(message);
    }


    /**
     * 获取用户信息
     *
     * @param regular
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public RespObj getUserInfo(@RequestParam(defaultValue = "", required = false) String regular,
                               @RequestParam(defaultValue = "1", required = false) int page,
                               @RequestParam(defaultValue = "5", required = false) int pageSize) throws Exception {
        try {
            int count = 1;
            List<IdNameValuePairDTO> dtos = new ArrayList<IdNameValuePairDTO>();
            if (ObjectId.isValid(regular)) {
                UserEntry userEntry = userService.find(new ObjectId(regular));
                if (null == userEntry) {
                    return RespObj.FAILD("查找不到该用户！");
                } else {
                    dtos.add(getDto(userEntry));
                }
            } else {
                //用户名精确查找
                List<UserEntry> userEntryList = userService.getInfoByName("nm", regular);
                if (userEntryList.size() > 0) {
                    dtos = getUserInfo(userEntryList);
                    count = dtos.size();
                } else {
                    //昵称精确查找
                    List<UserEntry> userEntryList1 = userService.getInfoByName("nnm", regular);
                    if (userEntryList1.size() > 0) {
                        dtos = getUserInfo(userEntryList1);
                        count = dtos.size();
                    } else {
                        //判断是否存储了Id,存储时间为5分钟
                        String field1 = "nm";
                        String filed2 = "nnm";
                        ObjectId userId = getUserId();
                        String key = userId.toString() + "$" + regular;
                        Map map = RedisUtils.getMap(key);
                        String lastId = "";
                        if (null != map && map.size() != 0) {
                            lastId = (String) map.get("lastId");
                        }
                        UserEntry userEntry = userService.getUserInfoEntry(regular);
                        if (null != userEntry) {
                            List<UserEntry> userEntries = userService.getUserList(field1, regular, page, pageSize, lastId);
                            dtos = getUserInfo(userEntries);
                            if (null != map && map.size() != 0) {
                                count = Integer.parseInt((String) map.get("count"));
                            } else {
                                count = userService.countUserList(field1, regular);
                            }
                        } else {
                            List<UserEntry> userEntries = userService.getUserList(filed2, regular, page, pageSize, lastId);
                            dtos = getUserInfo(userEntries);
                            if (null != map && map.size() != 0) {
                                count = Integer.parseInt((String) map.get("count"));
                            } else {
                                count = userService.countUserList(filed2, regular);
                            }
                        }
                        //存储key-value
                        Map hashMap = new HashMap();
                        if (dtos.size() > 0) {
                            hashMap.put("lastId", dtos.get(dtos.size() - 1).getId());
                            hashMap.put("count", count + "");
                        }
                        if (null != hashMap && hashMap.size() > 0) {
                            RedisUtils.cacheMap(key, hashMap, Constant.SESSION_FIVE_MINUTE);
                        }
                    }
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", dtos);
            map.put("count", count);
            return RespObj.SUCCESS(map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("传递的参数不符合标准！");
        }
    }

    public List<IdNameValuePairDTO> getUserInfo(List<UserEntry> userEntries) {
        List<IdNameValuePairDTO> idNameValuePairDTOs = new ArrayList<IdNameValuePairDTO>();
        for (UserEntry item : userEntries) {
            idNameValuePairDTOs.add(getDto(item));
        }
        return idNameValuePairDTOs;
    }

    public IdNameValuePairDTO getDto(UserEntry userEntry) {
        IdNameValuePairDTO dto = new IdNameValuePairDTO();
        dto.setId(userEntry.getID().toString());
        dto.setValue(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        dto.setName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        return dto;
    }


    /**
     * 提交作业
     *
     * @param communityDetailId
     * @param content
     * @param images
     * @param attacheMents
     * @return
     */
    @RequestMapping("/submitHWork")
    @ResponseBody
    public RespObj submitHWork(@ObjectIdType ObjectId communityId,
                               @ObjectIdType ObjectId communityDetailId, int type,
                               @RequestParam(required = false, defaultValue = "") String content,
                               @RequestParam(required = false, defaultValue = "") String images,
                               @RequestParam(required = false, defaultValue = "") String attacheMents) {
        ObjectId uid = getUserId();
        communityService.saveHomeWork(communityId, communityDetailId, uid, content, images, attacheMents, type);
        return RespObj.SUCCESS;
    }

    /**
     * 分享图片视频
     *
     * @param communityDetailId
     * @param content
     * @param images
     * @param vedios
     * @return
     */
    @RequestMapping("/shareImages")
    @ResponseBody
    public RespObj shareImages(@ObjectIdType ObjectId communityId,
                               @ObjectIdType ObjectId communityDetailId, int type,
                               @RequestParam(required = false, defaultValue = "") String content,
                               @RequestParam(required = false, defaultValue = "") String images,
                               @RequestParam(required = false, defaultValue = "") String vedios) {
        ObjectId uid = getUserId();
        communityService.saveCommunityShare(communityId, communityDetailId, uid, content, images, vedios, type);
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 分享链接
     *
     * @param communityDetailId
     * @param shareUrl
     * @return
     */
    @RequestMapping("/shareUrls")
    @ResponseBody
    public RespObj shareUrl(@ObjectIdType ObjectId communityId,
                            @ObjectIdType ObjectId communityDetailId,
                            @RequestParam(defaultValue = "", required = false) String shareCommend,
                            @RequestParam(defaultValue = "", required = false) String shareUrl) {

        try {
            HttpClient client = new DefaultHttpClient();
            //抓取的数据
            ProductModel productModel = URLParseUtil.UrlParser(client, shareUrl);
            if (StringUtils.isBlank(productModel.getImageUrl())) {
                return RespObj.FAILD("解析不了该链接或者该链接无效");
            }
            ObjectId uid = getUserId();
            communityService.saveCommunityShare(communityId, communityDetailId, uid, productModel, shareCommend, 6);
            return RespObj.SUCCESS("操作成功");
        } catch (Exception e) {
            return RespObj.FAILD("解析不了该链接或者该链接无效");
        }
    }


    @RequestMapping("/recommend")
    @ResponseBody
    public RespObj recommend(@ObjectIdType ObjectId communityId,
                             @ObjectIdType ObjectId communityDetailId, int type,
                             @RequestParam(defaultValue = "", required = false) String shareCommend,
                             @RequestParam(defaultValue = "", required = false) String shareUrl,
                             @RequestParam(defaultValue = "", required = false) String shareImage,
                             @RequestParam(defaultValue = "", required = false) String description,
                             @RequestParam(defaultValue = "", required = false) String sharePrice) {
        ObjectId uid = getUserId();
        communityService.saveCommunityRecommend(communityId, communityDetailId, uid, shareUrl, shareImage, description, sharePrice, shareCommend, type);
        return RespObj.SUCCESS("推荐学习用品成功！");
    }


    /**
     * 群主 邀请多人进社区
     * 逻辑：
     * 1.加入社区
     * 2.push id到实体中
     * 3.添加到环信组
     * 3.添加进讨论组
     * 4.push 组id 到实体中
     *
     * @param communityId
     * @param userIds
     * @return
     */
    @RequestMapping("/inviteMembers")
    @ResponseBody
    public RespObj inviteMembers(@ObjectIdType ObjectId communityId,
                                 String userIds) {
        ObjectId groupId = communityService.getGroupId(communityId);
        String emChatId = groupService.getEmchatIdByGroupId(groupId);
        List<ObjectId> userIdList = getMembersId(userIds);

        for (ObjectId userId : userIdList) {

            if(!memberService.isGroupMember(groupId,userId)) {
                if (memberService.isBeforeMember(groupId, userId)) {

                    if(emService.addUserToEmGroup(emChatId,userId)) {
                        memberService.updateMember(groupId,userId,0);
                    }
                } else {
                    if(emService.addUserToEmGroup(emChatId,userId)) {
                        memberService.saveMember(userId, groupId);
                    }

                }
            }
        }
        return RespObj.SUCCESS("操作成功");
    }

    private List<ObjectId> getMembersId(String memberId) {
        String[] members = memberId.split(",");
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (String item : members) {
            objectIds.add(new ObjectId(item));
        }
        return objectIds;
    }

    /**
     * 查询玩伴通知列表（分页）
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getFriendApplys")
    @ResponseBody
    public RespObj getFriendApplys(@RequestParam(defaultValue = "1", required = false) int page,
                                   @RequestParam(defaultValue = "10", required = false) int pageSize) {
        ObjectId userId = getUserId();
        PageModel<FriendApply> pageModel = new PageModel<FriendApply>();
        pageModel.setResult(friendApplyService.findFriendApplys(userId.toString(), 0, page, pageSize));
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(friendApplyService.countNoResponseReply(userId.toString()));
        return RespObj.SUCCESS(pageModel);
    }

    /**
     * 查询我关注的人列表（分页）
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getConcernList")
    @ResponseBody
    public RespObj getConcernList(@RequestParam(defaultValue = "1", required = false) int page,
                                  @RequestParam(defaultValue = "10", required = false) int pageSize) {
        ObjectId userId = getUserId();
        PageModel<ConcernDTO> pageModel = new PageModel<ConcernDTO>();
        pageModel.setResult(concernService.getConcernByUserId(userId, page, pageSize));
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(concernService.countConcernList(userId));
        return RespObj.SUCCESS(pageModel);
    }

    @RequestMapping("/playmateNotice")
    @LoginInfo
    public String playmateNotice(Map<String, Object> model) {
        model.put("menuItem", 1);
        return "/community/playmateNotice";
    }

    @RequestMapping("/mySystemInfo")
    @LoginInfo
    public String mySystemInfo(Map<String, Object> model) {
        model.put("menuItem", 3);
        return "/community/playmateNotice";
    }


    @RequestMapping("/myPartners")
    @LoginInfo
    public String myPartners(Map<String, Object> model) {
        model.put("menuItem", 2);
        return "/community/playmateNotice";
    }


    @RequestMapping("/friendList")
    @LoginInfo
    public String getFriendList(Map<String, Object> model) {
        return "forum/friend_list";
    }

    /**
     * (取消)关注某人
     */
    @RequestMapping("/concernPerson")
    @ResponseBody
    public RespObj setConcernPerson(@ObjectIdType ObjectId concernId, int remove) {
        ConcernEntry concernEntry = concernService.getConcernData(getUserId(), concernId, -1);
        if (null != concernEntry) {
            concernService.setConcernData(getUserId(), concernId, remove);
        } else {
            concernService.pushConcern(getUserId(), concernId);
        }

        return RespObj.SUCCESS(remove);
    }

    /**
     * 取消关注
     *
     * @param concernId
     * @return
     */
    @RequestMapping("/pullConcern")
    @ResponseBody
    public RespObj pullConcern(@ObjectIdType ObjectId concernId) {
        concernService.pullConcern(concernId);
        return RespObj.SUCCESS;
    }


    @RequestMapping("/communitySet")
    @LoginInfo
    public String communitySet() {
        return "/community/communitySet";
    }


    /**
     * 判断取的名称是否唯一
     *
     * @return
     */
    @RequestMapping("/judgeCreate")
    @ResponseBody
    public RespObj judgeCreateCommunity(String communityName) {
        return RespObj.SUCCESS(communityService.judgeCommunityCreate(communityName));
    }

    /**
     * 编辑社区时判断是否修改了社区名称
     *
     * @param communityName
     * @param id
     * @return
     */
    @RequestMapping("/judgeCommunityName")
    @ResponseBody
    public RespObj judgeCommunityName(String communityName, @ObjectIdType ObjectId id) {
        return RespObj.SUCCESS(communityService.judgeCommunityName(communityName, id));
    }


    @RequestMapping("/userData")
    public String redirectUser(Map<String, Object> model) {
        ObjectId userId = getUserId();
        UserEntry userEntry1 = userService.find(userId);
        model.put("applyName", StringUtils.isNotBlank(userEntry1.getNickName()) ? userEntry1.getNickName() : userEntry1.getUserName());
        String personId = getRequest().getParameter("userId");
        UserEntry userEntry = userService.find(new ObjectId(personId));
        model.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        model.put("nickName", StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        //获取自己的标签信息
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        List<String> tags = new ArrayList<String>();
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
            tags.add(userTagEntry.getTag());
        }
        model.put("tags", tags);
        model.put("personId", personId);
        model.put("communityNames", communityService.generateCommunityNames(new ObjectId(personId)));
        //判断是否为好友
        boolean isFriend = friendService.isFriend(personId, userId.toString());
        if (isFriend) {
            model.put("friend", "玩伴");
        } else {
            List<FriendApply> friendApplyList = friendApplyService.findApplyBySponsorIdAndRespondentId(userId.toString(), personId);
            if (null != friendApplyList && !friendApplyList.isEmpty()) {
                model.put("friend", "等回复");
            } else {
                model.put("friend", "加玩伴");
            }
        }
        return "/community/communityUser";
    }

    @RequestMapping("/pushUserTag")
    @ResponseBody
    public RespObj pushUserTag(@RequestBody UserTag userTag) {
        ObjectId userId = getUserId();
        userService.pushUserTag(userId, userTag.getCode(), userTag.getTag());
        return RespObj.SUCCESS;
    }

    @RequestMapping("/pullUserTag")
    @ResponseBody
    public RespObj pullUserTag(int code) {
        ObjectId userId = getUserId();
        userService.pullUserTag(userId, code);
        return RespObj.SUCCESS;
    }

    /**
     * 判断该用户是否包含该标签
     *
     * @param code
     * @return
     */
    @RequestMapping("/judgeTag")
    @ResponseBody
    public RespObj judgeTag(int code) {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.find(userId);
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        boolean flag = true;
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
            if (userTagEntry.getCode() == code) {
                flag = false;
                break;
            }
        }
        return RespObj.SUCCESS(flag);
    }

    /**
     * 获取我的标签
     *
     * @return
     */
    @RequestMapping("/getMyTags")
    @ResponseBody
    public RespObj getMyTags() {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.find(userId);
        List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
        List<String> tags = new ArrayList<String>();
        for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
            tags.add(userTagEntry.getTag());
        }
        return RespObj.SUCCESS(tags);
    }

    /**
     * 生成 seq
     *
     * @return
     */
    @RequestMapping("/generateSeq")
    @ResponseBody
    @SessionNeedless
    public RespObj generateSeq() {
        communityService.generateSeq();
        return RespObj.SUCCESS;
    }

    /**
     * 重新生成 二维码
     *
     * @param searchId
     * @return
     */
    @RequestMapping("/generateQrUrl")
    @ResponseBody
    @SessionNeedless
    public RespObj generateQrUrl(String searchId) {
        communityService.generateQrUrl(searchId);
        return RespObj.SUCCESS;
    }

    /**
     * 重置logo
     *
     * @return
     */
    @RequestMapping("/resetLogo")
    @ResponseBody
    public RespObj resetLogo() {
        List<CommunityDTO> communityDTOs = communityService.findAllCommunity();
        String logo = "http://www.fulaan.com/static/images/community/upload.png";
        for (CommunityDTO communityDTO : communityDTOs) {
            if (communityDTO.getLogo().contains("k6kt")) {
                communityService.resetLogo(communityDTO.getId(), logo);
            }
        }
        return RespObj.SUCCESS("success");
    }


    /**
     * 通过url获取下载图片到本地
     *
     * @param url
     * @param request
     * @return
     */
    @RequestMapping("/getImage")
    @ResponseBody
    public RespObj getImage(String url, HttpServletRequest request) throws IOException {
        String parentPath = request.getServletContext().getRealPath("/");
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String imageUrl = new ObjectId().toString() + Constant.POINT + FilenameUtils.getExtension(url);
        String nativeUrl = parentPath + imageUrl;
        File file = new File(nativeUrl);
        if (!file.exists()) {
            file.createNewFile();
        }

        byte[] btImg = GetImage.getImageFromNetByUrl(url);
        if (null != btImg && btImg.length > 0) {
            FileOutputStream fops = new FileOutputStream(file);
            try {
                fops.write(btImg);
            } finally {
                fops.flush();
                fops.close();
            }
            return RespObj.SUCCESS(imageUrl);
        } else {
            return RespObj.FAILD("没有从该连接获得内容");
        }
    }

    /**
     * 注册环信服务
     *
     * @return
     */
    @RequestMapping("/registerEmService")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj registerEmService() {
        userService.registerEmChatService();
        return RespObj.SUCCESS("success");
    }

    /* 在七牛上下载压缩过的图片，放在本地服务器上
    *
    * @param req
    * @param imageUrl //原始图片地址（本地）
    * @return
    */
    @RequestMapping("/getQiuNiuImage")
    @ResponseBody
    public Map<String, Object> getQiuNiuImage(HttpServletRequest req, String imageUrl) {
        Map<String, Object> model = new HashMap<String, Object>();
        String parentPath = req.getServletContext().getRealPath("/upload") + "/qiuNiu/";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String filekey = new ObjectId().toString() + Constant.POINT + FilenameUtils.getExtension(imageUrl);
        String imageName = parentPath + filekey;
        File file = new File(imageName);
        if (!file.exists()) {//文件不存在
            try {
                file.createNewFile();
                String type = "?imageView/2/w/580";//压缩样式
                String qnUrl = imageUrl + type;//对应七牛地址
                URL url = new URL(qnUrl);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                dataInputStream.close();
                fileOutputStream.close();
                model.put("code", 200);
                model.put("url", "/upload/qiuNiu/" + filekey);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {//存在直接返回
            model.put("code", 200);
            model.put("url", "/upload/qiuNiu/" + filekey);
        }
        return model;
    }

    /**
     * 保存涂鸦图片 - web端
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveEditedImage")
    public void saveEditedImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String filekey = "qiuNiu-" + new ObjectId().toString() + ".png";
        String parentPath = request.getServletContext().getRealPath("/upload") + "/qiuNiu/";
        File parentFile = new File(parentPath);
        File attachFile = new File(parentFile, filekey);
        try {
            ServletInputStream inputStream = request.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, attachFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw  ex;
        }
        String[] partInContentId = request.getParameter("partInContentId").split("-");
        String fileKey = new ObjectId().toString() + Constant.POINT + "png";
        QiniuFileUtils.uploadFile(fileKey, new FileInputStream(attachFile), QiniuFileUtils.TYPE_IMAGE);
        String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
        communityService.updateImage(new ObjectId(partInContentId[0]), path, "http://" + partInContentId[1]);

        PartInContentEntry partInContentEntry=communityService.findPartIncontById(new ObjectId(partInContentId[0]));
        try {
            attachFile.delete();
            response.sendRedirect("/community/communityDetail.do?detailId="+partInContentEntry.getDetailId());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 保存涂鸦 - 图片
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveTuYaImage")
    @SessionNeedless
    @ResponseBody
    public RespObj saveEditedImage(@ObjectIdType ObjectId partInContentId, String oldImagePath, @RequestParam("file") MultipartFile multipartFile) throws Exception {

        String fileKey = new ObjectId().toString() + ".jpg";
        QiniuFileUtils.uploadFile(fileKey, multipartFile.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
        String newImagePath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
        communityService.updateImage(partInContentId, newImagePath, oldImagePath);
        return RespObj.SUCCESS("保存成功!");
    }


    /**
     * 批阅
     *
     * @param contentId
     * @return
     */
    @RequestMapping("/mark")
    @ResponseBody
    public RespObj setMark(@ObjectIdType ObjectId contentId) {
        PartInContentEntry partInContentEntry = communityService.findPartIncontById(contentId);
        ObjectId groupId = communityService.getGroupId(partInContentEntry.getCommunityId());
        ObjectId userId = getUserId();
        if (memberService.isManager(groupId, userId)) {
            partInContentEntry.setMark(1);
            communityService.savePartIncontent(partInContentEntry);
            return RespObj.SUCCESS;
        } else {
            return RespObj.FAILD("你不是副社长或社长，不能批阅作业");
        }
    }


    /**
     * 删除消息
     * @param detailId
     * @return
     */
    @RequestMapping("/removeDetailById")
    @ResponseBody
    public RespObj removeDetailById(@ObjectIdType ObjectId detailId){
        communityService.removeCommunityDetailById(detailId);
        return RespObj.SUCCESS;
    }

    /**
     * 获取系统消息列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getCommunitySysInfo")
    @ResponseBody
    public Map<String,Object> getCommunitySysInfo(@RequestParam(required = false,defaultValue = "1")int page,
                                       @RequestParam(required = false,defaultValue = "10")int pageSize){
        Map<String,Object> map=new HashMap<String,Object>();
        ObjectId userId=getUserId();
        List<CommunitySystemInfoDTO> dtos=communitySystemInfoService.findInfoByUserIdAndType(userId,page,pageSize);
        int count=communitySystemInfoService.countEntriesByUserIdAndType(userId);
        map.put("list",dtos);
        map.put("count",count);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return map;
    }


}