package com.fulaan.community;


import com.easemob.server.comm.constant.MsgType;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.RedisUtils;
import com.fulaan.community.dto.*;
import com.fulaan.communityValidate.dto.ValidateInfoDTO;
import com.fulaan.communityValidate.service.ValidateInfoService;
import com.fulaan.dto.*;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.forum.service.FVoteService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.operation.service.AppCommentService;
import com.fulaan.playmate.service.MateService;
import com.fulaan.pojo.CommunityMessage;
import com.fulaan.pojo.PageModel;
import com.fulaan.pojo.ProductModel;
import com.fulaan.pojo.Validate;
import com.fulaan.service.*;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import com.fulaan.util.GetImage;
import com.fulaan.util.QRUtils;
import com.fulaan.util.URLParseUtil;
import com.pojo.activity.FriendApply;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.Platform;
import com.pojo.fcommunity.*;
import com.pojo.forum.FVoteDTO;
import com.pojo.forum.FVoteEntry;
import com.pojo.user.*;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
import java.net.*;
import java.text.NumberFormat;
import java.util.*;


/**
 * Created by jerry on 2016/10/24.
 * 社区Controller
 */
@Api(value="社区Controller",hidden = true)
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
    @Autowired
    private MateService mateService;
    @Autowired
    private ValidateInfoService validateInfoService;
    @Autowired
    private FVoteService fVoteService;
    @Autowired
    private AppCommentService appCommentService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private LatestGroupDynamicService latestGroupDynamicService;

    public static final String suffix = "/static/images/community/upload.png";

    @RequestMapping("/create")
    @ResponseBody
    public RespObj createCommunity(String name,
                                   @RequestParam(required = false, defaultValue = "") StringBuffer desc,
                                   @RequestParam(required = false, defaultValue = "") StringBuffer logo,
                                   @RequestParam(required = false, defaultValue = "0") int open,
                                   @RequestParam(required = false, defaultValue = "") String userIds) throws Exception {
        Validate validate = detectCondition(name, desc, logo);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        ObjectId uid = getUserId();
        ObjectId communityId = new ObjectId();
        String qrUrl = QRUtils.getCommunityQrUrl(communityId);
        if (qrUrl == null) {
            return RespObj.FAILDWithErrorMsg("无法生成二维码");
        }
        String seqId = communityService.getSeq();
        if (StringUtils.isBlank(seqId)) {
            return RespObj.FAILDWithErrorMsg("社区序列值不够");
        }
        ObjectId commId = communityService.createCommunity(communityId, uid, name, desc.toString(), logo.toString(), qrUrl, seqId, open);
        if (commId == null) {
            return RespObj.FAILDWithErrorMsg("创建社区失败");
        }
        CommunityDTO communityDTO = communityService.findByObjectId(commId);
        //创建社区系统消息通知
        communitySystemInfoService.saveOrupdateEntry(getUserId(), getUserId(), "社长", 4, commId);
        communitySystemInfoService.saveOrupdateEntry(getUserId(), getUserId(), "社长", 5, commId);
        if (StringUtils.isNotBlank(userIds)) {
            GroupDTO groupDTO = groupService.findById(new ObjectId(communityDTO.getGroupId()),uid);
            List<ObjectId> userList = MongoUtils.convertObjectIds(userIds);
            for (ObjectId userId : userList) {
                if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                    memberService.saveMember(userId, new ObjectId(groupDTO.getId()), 0);
                    communityService.pushToUser(communityId, userId, 1);
                }
            }
        }
        int memberCount = memberService.getMemberCount(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setMemberCount(memberCount);
        List<MemberDTO> members = memberService.getMembers(new ObjectId(communityDTO.getGroupId()), 20,uid);
        communityDTO.setMembers(members);
        MemberDTO mine = memberService.getUser(new ObjectId(communityDTO.getGroupId()), getUserId());
        communityDTO.setMine(mine);
        String headImage = groupService.getHeadImage(new ObjectId(communityDTO.getGroupId()));
        communityDTO.setHeadImage(headImage);
        groupService.asyncUpdateHeadImage(new ObjectId(communityDTO.getGroupId()));
        return RespObj.SUCCESS(communityDTO);
    }

    private Validate detectCondition(String name, StringBuffer desc, StringBuffer logo) {
        Validate validate = new Validate();
        validate.setOk(false);
        //先判断社区名称是否是纯数字
        if (StringUtils.isNumeric(name)) {
            validate.setMessage("社区名称不能是纯数字!");
            return validate;
        }
        //先进行敏感词过滤
        List<String> words = userService.recordSensitiveWords(name);
        if (words.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("这些词语 ");
            for (String item : words) {
                buffer.append("“" + item + "”" + "、");
            }
            String str = buffer.toString().substring(0, buffer.toString().length() - 1);
            str = str + "是违禁词!";
            validate.setMessage(str);
            return validate;
        }
        String filterStr = userService.replaceSensitiveWord(desc.toString());
        desc.setLength(0);
        desc.append(filterStr);
        //先判断该社区名称是否使用过
        boolean flag = communityService.isCommunityNameUnique(name);
        if (!flag) {
            validate.setMessage("该社区名称已使用过！");
            return validate;
        }
        if (StringUtils.isBlank(logo) || suffix.equals(logo.toString())) {
            String prev = "http://www.fulaan.com";
            String preLogo = prev + suffix;
            logo.setLength(0);
            logo.append(preLogo);
        }
        validate.setOk(true);
        return validate;
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
        List<MemberDTO> members = memberService.getMembers(groupId, 12,getUserId());
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
     * 1.判断是否是成员
     * 2.加入环信
     * 3.判断是否以前加入过
     * 4.更新或保存成员
     * 5.更新数据
     * 6.push 到社区
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
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        //判断该用户是否加过该社区
        if (memberService.isGroupMember(groupId, userId)) {
            return RespObj.FAILD("该用户已经是该社区成员了");
        } else {
            if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                if (memberService.isBeforeMember(groupId, userId)) {
                    memberService.updateMember(groupId, userId, 0);
                    communityService.setPartIncontentStatus(communityId, userId, 0);
                } else {
                    memberService.saveMember(userId, groupId, 0);
                }
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
        String emChatId = communityDTO.getEmChatId();
        if (memberService.isHead(new ObjectId(communityDTO.getGroupId()), userId)) {
            return RespObj.FAILD("不能删除社长");
        }
        if (emService.removeUserFromEmGroup(emChatId, userId)) {
            memberService.deleteMember(new ObjectId(communityDTO.getGroupId()), userId);
            communityService.setPartIncontentStatus(new ObjectId(communityDTO.getGroupId()), userId, 1);
            communityService.pullFromUser(new ObjectId(communityDTO.getGroupId()), userId);
        }
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
    public RespObj getMyCommunitys(@RequestParam(defaultValue = "1", required = false) int page,
                                   @RequestParam(defaultValue = "100", required = false) int pageSize,
                                   @RequestParam(defaultValue = "app", required = false) String platform) {
        ObjectId userId = getUserId();
        List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
        if (null == userId && null != fulanDto) {
            communityDTOList.add(fulanDto);
            return RespObj.SUCCESS(communityDTOList);
        } else {
            if (null != fulanDto) {
                //加入复兰社区
                joinFulaanCommunity(getUserId(), new ObjectId(fulanDto.getId()));
            }
            communityDTOList = communityService.getCommunitys(userId, page, pageSize);
            if ("web".equals(platform)) {
                int count = communityService.countMycommunitys(userId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("list", communityDTOList);
                map.put("count", count);
                map.put("pageSize", pageSize);
                map.put("page", page);
                return RespObj.SUCCESS(map);
            } else {
                return RespObj.SUCCESS(communityDTOList);
            }
        }
    }
    /**
     * 获得我具有管理员权限的社区
     *
     * @return
     */
    @RequestMapping("/myRoleCommunitys")
    @ResponseBody
    @SessionNeedless
    public RespObj myRoleCommunitys(@RequestParam(defaultValue = "1", required = false) int page,
                                   @RequestParam(defaultValue = "100", required = false) int pageSize,
                                   @RequestParam(defaultValue = "app", required = false) String platform) {
        ObjectId userId = getUserId();
        List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
        if (null == userId && null != fulanDto) {
            communityDTOList.add(fulanDto);
            return RespObj.SUCCESS(communityDTOList);
        } else {
            if (null != fulanDto) {
                //加入复兰社区
                joinFulaanCommunity(getUserId(), new ObjectId(fulanDto.getId()));
            }
            List<ObjectId> mlist = appCommentService.getMyRoleList(userId);
            List<String> molist = new ArrayList<String>();
            for(ObjectId oid: mlist){
                molist.add(oid.toString());
            }
            communityDTOList = communityService.getCommunitys(userId, page, pageSize);
            List<CommunityDTO> communityDTOList2 = new ArrayList<CommunityDTO>();
            if(communityDTOList.size()>0){
                for(CommunityDTO dto3 : communityDTOList){
                    if(molist.contains(dto3.getId())){
                        communityDTOList2.add(dto3);
                    }
                }
            }
            if ("web".equals(platform)) {
                int count = communityService.countMycommunitys(userId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("list", communityDTOList2);
                map.put("count", count);
                map.put("pageSize", pageSize);
                map.put("page", page);
                return RespObj.SUCCESS(map);
            } else {
                return RespObj.SUCCESS(communityDTOList2);
            }
        }
    }


    @RequestMapping("/sortMyCommunities")
    @ResponseBody
    public RespObj sortMyCommunities(String params){
        ObjectId userId=getUserId();
        int max=0;
        //初始化状态
        communityService.updateInitSort(userId);
        Map<ObjectId,Integer> map=new HashMap<ObjectId, Integer>();
        List<ObjectId> communities=new ArrayList<ObjectId>();
        if(params.contains(",")){
            String[] param=params.split(",");
            for(String item:param){
                String[] temp=item.split("@");
                communities.add(new ObjectId(temp[0]));
                int customSort=Integer.parseInt(temp[1]);
                if(max<=customSort){
                    max=customSort;
                }
                map.put(new ObjectId(temp[0]),customSort);
            }
        }else{
            String[] temp=params.split("@");
            communities.add(new ObjectId(temp[0]));
            max=Integer.parseInt(temp[1]);
            map.put(new ObjectId(temp[0]),max);
        }

        RedisUtils.cacheString("customSort"+userId,String.valueOf(max),Constant.SECONDS_IN_HALF_YEAR);

        List<MineCommunityEntry> entries=new ArrayList<MineCommunityEntry>();
        Map<ObjectId,MineCommunityEntry> mineCommunityEntryMap=communityService.getMySortCommunities(userId,communities);
        for(Map.Entry<ObjectId,MineCommunityEntry> entry:mineCommunityEntryMap.entrySet()){
            MineCommunityEntry mineCommunityEntry=entry.getValue();
            Integer customSort=map.get(entry.getKey());
            mineCommunityEntry.setCustomSort(customSort);
            entries.add(mineCommunityEntry);
        }
//        communityService.batchSave(entries);
        for(MineCommunityEntry entry:entries){
            communityService.saveMimeEntry(entry);
        }
        return RespObj.SUCCESS;
    }

    /**
     * 获取热门社区
     *
     * @return
     */
    @RequestMapping("/hotCommunitys")
    @ResponseBody
    @SessionNeedless
    public RespObj hotCommunitys(@RequestParam(defaultValue = "1", required = false) int page,
                                 @RequestParam(defaultValue = "21", required = false) int pageSize,
                                 @RequestParam(defaultValue = "", required = false) String lastId) {
        ObjectId userId = getUserId();
        return RespObj.SUCCESS(communityService.getOpenCommunityS(userId, page, pageSize, lastId));
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
        if (StringUtils.isBlank(name) || StringUtils.isBlank(logo)) {
            return RespObj.FAILD("参数不合法");
        }
        CommunityDTO community = communityService.findByObjectId(communityId);
        if (name.equals(community.getName())) {
            communityService.updateCommunity(communityId, name, desc, logo, open);
        } else {
            if (communityService.isCommunityNameUnique(name)) {
                communityService.updateCommunity(communityId, name, desc, logo, open);
                groupService.asyncUpdateGroupName(new ObjectId(community.getGroupId()), name);
            } else {
                return RespObj.FAILD("该社区名称已经存在");
            }
        }
        return RespObj.SUCCESS;
    }

    /**
     * 最新一条动态消息
     * @param communityId
     * @return
     */
    @RequestMapping(value = "/getLatestGroupInfo")
    @ResponseBody
    public RespObj getLatestGroupInfo(@ObjectIdType ObjectId communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        String userId=getUserId().toString();
        LatestGroupDynamicEntry entry=latestGroupDynamicService.getLatestInfo(communityId);
        if(null!=entry){
            respObj.setCode(Constant.SUCCESS_CODE);
            LatestGroupDynamicDTO dto=new LatestGroupDynamicDTO(entry);
            List<String> readedList=dto.getReadedList();
            boolean flag=false;
            for(String uid:readedList){
                if(uid.equals(userId)){
                    flag=true;
                    break;
                }
            }
            if(flag){
                dto.setReaded(1);
            }else{
                dto.setReaded(0);
            }
            ObjectId  partInUserId=new ObjectId(dto.getUserId());
            RemarkEntry remarkEntry=communityService.getRemarkEntry(new ObjectId(userId),partInUserId);
            if(null!=remarkEntry){
                dto.setNickName(remarkEntry.getRemark());
            }else{
                ObjectId groupId = communityService.getGroupId(new ObjectId(dto.getCommunityId()));
                List<ObjectId> groupIds = new ArrayList<ObjectId>();
                groupIds.add(groupId);
                List<ObjectId> partInUserIds=new ArrayList<ObjectId>();
                partInUserIds.add(partInUserId);
                Map<String, MemberEntry> memberEntryMap = communityService.getMemberEntryMap(groupIds, partInUserIds);
                MemberEntry entry1 = memberEntryMap.get(groupId + "$" + partInUserId);
                UserEntry userEntry=userService.findById(partInUserId);
                String nickName=StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                if(null!=entry1){
                    if(StringUtils.isNotBlank(entry1.getNickName())){
                        dto.setNickName(entry1.getNickName());
                    }else{
                        dto.setNickName(nickName);
                    }
                }else{
                    dto.setNickName(nickName);
                }
            }
            respObj.setMessage(dto);
        }
        return respObj;
    }

    /**
     * 设置最新动态消息已读
     */
    @RequestMapping(value = "/pushLatestReadedInfo")
    @ResponseBody
    public RespObj pushLatestReadedInfo(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        ObjectId userId=getUserId();
        latestGroupDynamicService.pushRead(id,userId);
        return respObj;
    }

    /**
     * 新建消息
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/newMessage")
    @ResponseBody
    public RespObj newMessage(@RequestBody CommunityMessage message) {
        ObjectId uid = getUserId();
        try{
            ObjectId detailId=communityService.saveMessage(uid, message);
            int type=message.getType();
            String msg = "";
            if(type==1||type==5) {
                if (type == 1) {
                    msg = "发布了一条通知";
                } else {
                    msg = "发布了一条作业";
                }
                latestGroupDynamicService.saveLatestInfo(new ObjectId(message.getCommunityId()),detailId,msg,uid,type);
            }

//                Map<String, String> sendMessage = new HashMap<String, String>();
//                sendMessage.put("type", MsgType.TEXT);
//                sendMessage.put("msg", msg);
//                ObjectId groupId = communityService.getGroupId(new ObjectId(message.getCommunityId()));
//                List<ObjectId> groupIds = new ArrayList<ObjectId>();
//                groupIds.add(groupId);
//                List<ObjectId> partInUserIds=new ArrayList<ObjectId>();
//                partInUserIds.add(uid);
//                String emchatId=groupService.getEmchatIdByGroupId(groupId);
//                targets.add(emchatId);
//                Map<String, MemberEntry> memberEntryMap = communityService.getMemberEntryMap(groupIds, partInUserIds);
//                MemberEntry entry1 = memberEntryMap.get(groupId + "$" + uid);
//                String nickName="";
//                UserEntry userEntry=userService.findById(uid);
//                if (null != entry1) {
//                    if (StringUtils.isNotBlank(entry1.getNickName())) {
//                        nickName=entry1.getNickName();
//                    } else {
//                        nickName=StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
//                    }
//                } else {
//                    nickName=StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
//                }
//                Map<String, String> ext=new HashMap<String, String>();
//                ext.put("type",message.getType()+"");
//                ext.put("nickName",nickName);
//                ext.put("userId",uid.toString());
//                ext.put("communityId",message.getCommunityId());
//                emService.sendTextMessage("chatgroups", targets, uid.toString(), ext, sendMessage);
//            }
            return RespObj.SUCCESS;
        }catch (Exception e){
            return RespObj.FAILD("投票截止时间格式不规范");
        }

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
        //先进行敏感词过滤
        msg = userService.replaceSensitiveWord(msg);
        ObjectId userId = getUserId();
        if (join == 1) {
            CommunityDetailDTO communityDetailDTO = communityService.findDetailById(communityDetailId,userId);
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
        ObjectId userId=getUserId();
        CommunityDetailDTO communityDetailDTO = communityService.findDetailById(communityDetailId,userId);
        List<String> partInList = communityDetailDTO.getPartInList();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (String id : partInList) {
            userIds.add(new ObjectId(id));
        }
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityDetailDTO.getCommunityId()));
        List<ObjectId> groupIds = new ArrayList<ObjectId>();
        groupIds.add(groupId);
        List<ObjectId> partInUserIds=new ArrayList<ObjectId>(userIds);
        Map<String, MemberEntry> memberEntryMap = communityService.getMemberEntryMap(groupIds, partInUserIds);
        Map<ObjectId,RemarkEntry> remarkEntryMap=communityService.findRemarkEntries(userId,partInUserIds);
        List<User> users = new ArrayList<User>();
        for (String id : partInList) {
            UserDetailInfoDTO user = userService.getUserInfoById(id);
            User user1 = new User();
            user1.setImg(user.getImgUrl());
            MemberEntry entry1 = memberEntryMap.get(groupId + "$" + new ObjectId(id));
            if (null != entry1) {
                if (StringUtils.isNotBlank(entry1.getNickName())) {
                    user1.setName(entry1.getNickName());
                } else {
                    user1.setName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
                }
            } else {
                user1.setName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
            }
            if(null!=remarkEntryMap){
                RemarkEntry remarkEntry=remarkEntryMap.get(new ObjectId(id));
                if(null!=remarkEntry){
                    user1.setName(remarkEntry.getRemark());
                }
            }
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
        CommunityDetailDTO communityDetailDTO = communityService.findDetailById(communityDetailId,userId);
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

    /**
     * 查询最新谁申请加入私密社区的消息
     *
     * @param emChatId
     * @return
     */
    @RequestMapping("/getNewValidateInfo")
    @ResponseBody
    public RespObj getNewValidateInfo(String emChatId) {
        ObjectId userId=getUserId();
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        ValidateInfoEntry entry = validateInfoService.getNewsInfo(new ObjectId(groupDTO.getCommunityId()));
        if (null == entry) {
            return RespObj.FAILD("没有审核消息");
        } else {
            ValidateInfoDTO dto = new ValidateInfoDTO(entry);
            UserEntry userEntry = userService.findById(entry.getUserId());
            RemarkEntry remarkEntry=communityService.getRemarkEntry(getUserId(),entry.getUserId());
            if(null!=remarkEntry) {
                dto.setUserName(remarkEntry.getRemark());
            }else{
                dto.setUserName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            }
            return RespObj.SUCCESS(dto);
        }
    }

    /**
     * 处理验证申请信息
     *
     * @param reviewKeyId    (为了区分是否是再次申请的,若申请失败可以再申请一次，每次申请都会出现这个关键字Id,申请人和审核人的绑定关系)
     * @param userId         (申请人ID)
     * @param communityId    （社区Id）
     * @param approvedStatus (批准状态（0:已批准 1:未批准)
     * @return
     */
    @RequestMapping("/reviewApply")
    @ResponseBody
    public RespObj reviewApply(@ObjectIdType ObjectId reviewKeyId, @ObjectIdType ObjectId userId, @ObjectIdType ObjectId communityId, int approvedStatus) {
        ObjectId groupId = communityService.getGroupId(communityId);
        ObjectId reviewedId = getUserId();
        int authority = 1;
        String roleStr;
        if (memberService.isHead(groupId, reviewedId)) {
            roleStr = "社长";
        } else {
            roleStr = "副社长";
        }
        //先判断是否有权限
        if (!memberService.isManager(groupId, reviewedId)) {
            //更新权限状态
            validateInfoService.updateAuthority(reviewedId, communityId, authority);
            return RespObj.FAILD("该用户没有权限操作,请刷新页面!");
        } else {
            //先判断是否审核了
            ValidateInfoEntry validateInfoEntry = validateInfoService.getEntry(userId, reviewKeyId, communityId);
            if (null != validateInfoEntry) {
                if (validateInfoEntry.getStatus() == 0) {
                    //查询所有的管理员
                    List<ValidateInfoEntry> entries = validateInfoService.getEntries(userId, communityId, reviewKeyId);
                    for (ValidateInfoEntry entry : entries) {
                        entry.setStatus(1);
                        entry.setApprovedId(reviewedId);
                        entry.setRoleStr(roleStr);
                        entry.setApprovedStatus(approvedStatus);
                        validateInfoService.saveOrUpdate(entry);
                    }
//                    validateInfoService.batchSaveInfo(entries);
                    ValidateInfoEntry entry = validateInfoService.getApplyEntry(userId, reviewKeyId, communityId);
                    if (null != entry) {
                        entry.setReviewState(approvedStatus);
                        entry.setStatus(1);
                        validateInfoService.saveOrUpdate(entry);


                        //该用户加入社区
                        if (approvedStatus == 0) {
                            CommunityDTO dto = communityService.findByObjectId(communityId);
                            int saveState = 1;
                            if ("复兰社区".equals(dto.getName())) {
                                saveState = 3;
                            }
                            joinCommunity(userId, communityId, groupId, saveState);
                        }
                        return RespObj.SUCCESS("审核成功,请刷新页面!");
                    } else {
                        return RespObj.FAILD("该申请人信息不存在,请刷新页面!");
                    }
                } else {
                    return RespObj.FAILD("该用户信息已经审核了,请刷新页面!");
                }

            } else {
                return RespObj.FAILD("该审核信息不存在!");
            }
        }
    }


    /**
     * 获取验证消息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getValidateInfos")
    @ResponseBody
    public RespObj getValidateInfos(@RequestParam(defaultValue = "1", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId userId = getUserId();
        List<ValidateInfoDTO> list = validateInfoService.getValidateInfos(userId, page, pageSize);
        int count = validateInfoService.countValidateInfos(userId);
        map.put("list", list);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return RespObj.SUCCESS(map);
    }


    /**
     * 加入社区
     *
     * @param communityId
     * @return
     */
    @RequestMapping("/join")
    @ResponseBody
    public RespObj joinCommunity(@ObjectIdType ObjectId communityId,
                                 @RequestParam(defaultValue = "1", required = false) int type,
                                 @RequestParam(defaultValue = "", required = false) String msg) {
        CommunityDTO dto = communityService.findByObjectId(communityId);
        ObjectId userId = getUserId();
        ObjectId groupId = communityService.getGroupId(communityId);
        int saveState = 1;
        if ("复兰社区".equals(dto.getName())) {
            saveState = 3;
        }
        if (dto.getOpen() == 1) {
            if (joinCommunity(userId, communityId, groupId, saveState)) {
                return RespObj.SUCCESS("操作成功!");
            } else {
                return RespObj.FAILD("该用户已经是该社区成员了");
            }
        } else {
            //私密社区
            if (memberService.isGroupMember(groupId, userId)) {
                return RespObj.FAILD("该用户已经是该社区成员了");
            } else {
                UserEntry userEntry = userService.findById(userId);
                Map<String, String> ext = new HashMap<String, String>();
                String nickName = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                ext.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
                ext.put("nickName", nickName);
                ext.put("userId", userId.toString());
                ext.put("joinPrivate", "YES");
                List<MemberDTO> memberDTOs = memberService.getManagers(groupId);
                List<String> targets = new ArrayList<String>();
                for (MemberDTO memberDTO : memberDTOs) {
                    targets.add(memberDTO.getUserId());
                }
                String message;
                if (type == 1) {
                    message = "用户" + nickName + "请求加入" + dto.getName() + "（来自搜索ID）";
                } else {
                    message = "用户" + nickName + "请求加入" + dto.getName() + "（来自扫描二维码）";
                }
                Map<String, String> sendMessage = new HashMap<String, String>();
                sendMessage.put("type", MsgType.TEXT);
                sendMessage.put("msg", message);

                //申请加入私密社区
                boolean flag = validateInfoService.saveValidateInfos(userId, communityId, msg, type, memberDTOs);
                if (flag) {
                    if(emService.sendTextMessage("users", targets, userId.toString(), ext, sendMessage)){
                        return RespObj.FAILD("此群为私密社区群，请等待社长批准!");
                    }else{
                        return RespObj.FAILD("申请加入该私密社区失败!");
                    }
                } else {
                    return RespObj.FAILD("已经申请加入该私密社区了!");
                }
            }
        }

    }

    /**
     * 加入社区==同时加入环信组
     *
     * @param userId
     * @param communityId
     * @return
     */
    private boolean joinCommunity(ObjectId userId, ObjectId communityId, ObjectId groupId, int saveState) {
        GroupDTO groupDTO = groupService.findById(groupId,null);
        if (memberService.isGroupMember(groupId, userId)) {
            return false;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {
            if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                memberService.updateMember(groupId, userId, 0);
                //设置先前该用户所发表的数据
                communityService.setPartIncontentStatus(communityId, userId, 0);
                communityService.pushToUser(communityId, userId, saveState);
            }

        } else {
            if (emService.addUserToEmGroup(groupDTO.getEmChatId(), userId)) {
                memberService.saveMember(userId, groupId, 0);
                communityService.pushToUser(communityId, userId, saveState);
            }
        }
        return true;
    }

    /**
     * 加入社区但是不加入环信群组---这里只有复兰社区调用
     * 加入复兰社区--- 复兰社区很特殊，特殊对待
     *
     * @param userId
     * @param communityId
     * @return
     */
    private void joinFulaanCommunity(ObjectId userId, ObjectId communityId) {

        ObjectId groupId = communityService.getGroupId(communityId);
        //type=1时，处理的是复兰社区
        if (memberService.isGroupMember(groupId, userId)) {
            return;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {
            memberService.updateMember(groupId, userId, 0);
            communityService.pushToUser(communityId, userId, 3);
            //设置先前该用户所发表的数据
            communityService.setPartIncontentStatus(communityId, userId, 0);
        } else {
            //新人
            communityService.pushToUser(communityId, userId, 3);
            memberService.saveMember(userId, groupId, 0);
        }
    }

    @RequestMapping("/cleanPrior")
    @ResponseBody
    @SessionNeedless
    public RespObj cleanPrior(){
        communityService.cleanPrior();
        return RespObj.SUCCESS;
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
    public RespObj quit(@ObjectIdType ObjectId communityId) {

        ObjectId groupId = communityService.getGroupId(communityId);
        String emChatId = groupService.getEmchatIdByGroupId(groupId);
        ObjectId userId = getUserId();

        if (memberService.isHead(groupId, userId)) {
            return RespObj.FAILD("群主暂时无法退出");
        }

        if (emService.removeUserFromEmGroup(emChatId, userId)) {
            communityService.pullFromUser(communityId, userId);
            memberService.deleteMember(groupId, userId);
            //设置先前该用户所发表的数据为废弃掉的
            communityService.setPartIncontentStatus(communityId, userId, 1);

            if (memberService.isManager(groupId, userId)) { //发送退出消息
                //当是副社长退出时
                List<ObjectId> objectIds = communityService.getAllMemberIds(groupId);
                communitySystemInfoService.addBatchData(userId, objectIds, "副社长", 1, communityId);
            }
        }
        return RespObj.SUCCESS("操作成功!");
    }


    @RequestMapping("/search")
    @ResponseBody
    public RespObj searchCommunity(String relax) {
//        Map<String,Object> retMap=new HashMap<String,Object>();
        List<CommunityDTO> dtos=communityService.search(relax, getUserId());
//        List<CommunityService.UserSearchInfo> users=communityService.getUserSearchDtos(relax);
//        retMap.put("communities",dtos);
//        retMap.put("users",users);
        
        return RespObj.SUCCESS(dtos);
    }

    @RequestMapping("/searchCommunityAndUser")
    @ResponseBody
    public RespObj searchCommunityAndUser(String relax) {
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<CommunityDTO> dtos=communityService.search(relax, getUserId());
        List<CommunityService.UserSearchInfo> users=communityService.getUserSearchDtos(relax);
        retMap.put("communities",dtos);
        retMap.put("users",users);
        return RespObj.SUCCESS(retMap);
    }

    @RequestMapping("/communityPublish")
    @SessionNeedless
    @LoginInfo
    public String communityPublish(Map<String, Object> model) {
        String communityId = getRequest().getParameter("communityId");
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        if (null != getUserId()) {
            if (memberService.isManager(groupId, getUserId())) {
                model.put("operation", 1);
            } else {
                model.put("operation", 0);
            }
            MineCommunityEntry entry = communityService.getTopEntry(new ObjectId(communityId), getUserId());
            if (null != entry) {
                if (entry.getTop() == 0) {
                    model.put("top", 1);
                } else {
                    model.put("top", 0);
                }
            }
        }
        model.put("communityId", communityId);
        return "/community/communityPublish";
    }

    @RequestMapping("/communityAllType")
    @SessionNeedless
    @LoginInfo
    public String communityAllType(Map<String, Object> model,
                                   @RequestParam(value = "target", defaultValue = "") String target) {
        model.put("target", target);
        return "/community/communityAllType";
    }

    @RequestMapping("/communityDetail")
    @SessionNeedless
    @LoginInfo
    public String communityMessage(@ObjectIdType ObjectId detailId, Map<String, Object> model) throws Exception {
        try {
            model.put("isOwner",0);
            ObjectId uid = getUserId();
            CommunityDetailDTO detail = communityService.findDetailById(detailId,uid);
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
                if(uid.equals(new ObjectId(detail.getUserId()))){
                    model.put("isOwner",1);
                }

            } else {
                CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
                if (null != fulanDto) {
                    communitys.add(fulanDto);
                }
            }

            //判断是否是投票模块
            model.put("type",0);
            model.put("voteMaxCount",0);
            if(detail.getType()==7){
                model.put("type",1);
                model.put("voteType", 0);
                //判断当前用户是否已登录
                if(null!=uid){
                    //判断该用户是否已投票
                    FVoteEntry fVoteEntry = fVoteService.getFVote(detail.getId(), uid.toString());
                    if (null != fVoteEntry) {
                        //已经投票了
                        model.put("voteType",1);
                        model.put("voteMine",1);
                    }
                }

                List<String> voteOptions = new ArrayList<String>();
                List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                String voteContent=detail.getVoteContent();
                long voteDeadTime=detail.getVoteDeadTime();
                int voteMaxCount=detail.getVoteMaxCount();
                int voteUserCount = fVoteService.getFVoteCount(detail.getId());
                model.put("voteFlagType", detail.getVoteType());
                model.put("voteMaxCount",voteMaxCount);
                model.put("voteUserCount",voteUserCount);
                model.put("voteDeadFlag", 0);
                if(System.currentTimeMillis()>voteDeadTime){
                    model.put("voteDeadFlag", 1);
                }
                if (voteContent.contains("/n/r")) {
                    String[] votes = voteContent.split("/n/r");
                    for (String vote : votes) {
                        voteOptions.add(vote);
                    }
                    model.put("voteOptions", voteOptions);
                } else {
                    voteOptions.add(voteContent);
                    model.put("voteOptions", voteOptions);
                }
                if(voteMaxCount>1){
                    model.put("check",1);
                }
                List<FVoteDTO> fVoteEntryList = fVoteService.getFVoteList(detail.getId());
                int totalCount = fVoteEntryList.size();
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(0);

                StringBuffer buffer=new StringBuffer();

                for (int i = 0; i < voteOptions.size(); i++) {
                    int j = i + 1;
                    int count = 0;
                    List<ObjectId> voteUsersId=new ArrayList<ObjectId>();
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (FVoteDTO fVoteDTO : fVoteEntryList) {
                        int number = fVoteDTO.getNumber();
                        if (j == number) {
                            voteUsersId.add(new ObjectId(fVoteDTO.getUserId()));
                            count++;
                            if(null!=uid){
                                if(new ObjectId(fVoteDTO.getUserId()).equals(uid)){
                                    buffer.append(j+"、");
                                }
                            }
                        }
                    }
                    StringBuffer voteUserName=new StringBuffer();
                    Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(voteUsersId,Constant.FIELDS);
                    for(Map.Entry<ObjectId,UserEntry> entryEntry:userEntryMap.entrySet()){
                        UserEntry user=entryEntry.getValue();
                        String name=StringUtils.isNotBlank(user.getNickName())?user.getNickName():user.getUserName();
                        voteUserName.append(name);
                        voteUserName.append(",");
                    }
                    double pItem = (double) count / (double) totalCount;
                    map.put("voteItemStr", voteOptions.get(i));
                    map.put("voteItemCount", count);
                    if(StringUtils.isNotBlank(voteUserName.toString())){
                        map.put("voteUserName",voteUserName.toString().substring(0,voteUserName.toString().length()-1));
                    }else{
                        map.put("voteUserName","");
                    }

                    if(count==0){
                        map.put("voteItemPercent", "0%");
                    }else{
                        map.put("voteItemPercent", nt.format(pItem));
                    }

                    mapList.add(map);
                }
                String temp=buffer.toString();
                if(StringUtils.isNotBlank(temp)){
                    model.put("voteSelected",temp.substring(0,temp.length()-1));
                }

                model.put("voteMapList", mapList);
                //该帖投票人数
                int voteCount = fVoteService.getFVoteCount(detail.getId());
                model.put("voteCount", voteCount);
            }
            model.put("detail", detail);
            model.put("detailId", detailId.toString());
            model.put("communitys", communitys);
            return "community/communityDetail";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
        return getTypeInfo(communityId, page, pageSize, order);
    }


    @RequestMapping("/getTypeMessages")
    @ResponseBody
    @SessionNeedless
    public RespObj getTypeMessages(@RequestParam @ObjectIdType ObjectId communityId,
                                   @RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "4") int pageSize,
                                   @RequestParam(required = false, defaultValue = "-1") int order) {
        return getTypeInfo(communityId, page, pageSize, order);
    }


    private RespObj getTypeInfo(ObjectId communityId, int page, int pageSize, int order) {
        ObjectId userId = getUserId();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        objectIds.add(communityId);
        System.out.println(DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
        return RespObj.SUCCESS(communityService.getNews(objectIds, page, pageSize, order, userId, 1));
    }


    @RequestMapping("/getMessage")
    @ResponseBody
    @SessionNeedless
    public RespObj getMessage(@RequestParam @ObjectIdType ObjectId communityId,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "4") int pageSize,
                              @RequestParam(required = false, defaultValue = "1") int type) {
        Platform pf = getPlatform();
        boolean isApp = false;
        if (pf == Platform.Android || pf == Platform.IOS) {
            isApp = true;
        }
        return RespObj.SUCCESS(communityService.getMessages(communityId, page, pageSize, CommunityDetailType.getType(type), getUserId(), isApp));
    }

    @RequestMapping("/getAllTypeMessage")
    @ResponseBody
    @SessionNeedless
    public RespObj getAllTypeMessage(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "4") int pageSize,
                                     @RequestParam(required = false, defaultValue = "-1") int order) {
        ObjectId userId = getUserId();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        if (null == userId) {
            CommunityDTO fulanDTO = communityService.getCommunityByName("复兰社区");
            if (fulanDTO != null && fulanDTO.getId() != null) {
                communityIds.add(new ObjectId(fulanDTO.getId()));
                return RespObj.SUCCESS(communityService.getNews(communityIds, page, pageSize, order, null, 1));
            }
            return RespObj.FAILD("没有数据");
        } else {
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
            //创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient client = httpClientBuilder.build();
            //抓取的数据
            ProductModel productModel = URLParseUtil.urlParser(client, url);
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
        ObjectId userId=getUserId();
        CommunityDetailDTO detailDTO = communityService.findDetailById(detailId,userId);
        Platform pf = getPlatform();
        if (pf == Platform.Android || pf == Platform.IOS) {
            if (null != getUserId()) {
                boolean flag = true;
                List<String> unReadList = detailDTO.getUnReadList();
                for (String item : unReadList) {
                    if (item.equals(getUserId().toString())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    communityService.pushRead(detailId, getUserId());
                }
            }
        }
        return RespObj.SUCCESS(detailDTO);
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
        CommunityDTO community = communityService.findByObjectId(new ObjectId(communityId));
        model.put("communityId", communityId);
        model.put("searchId", community.getSearchId());

        model.put("menuItem", 0);
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        if (memberService.isManager(groupId, getUserId())) {
            model.put("operation", 1);
        }
        return "/community/communityMember";
    }

    @RequestMapping("/manageCurrentCommunity")
    @LoginInfo
    public String manageCurrentCommunity(Map<String, Object> model) {
        String communityId = getRequest().getParameter("communityId");
        CommunityDTO community = communityService.findByObjectId(new ObjectId(communityId));
        model.put("communityId", communityId);
        model.put("searchId", community.getSearchId());

        model.put("menuItem", 1);
        model.put("fuflaan", 0);
        if (StringUtils.isNotBlank(community.getName())) {
            if (community.getName().equals("复兰社区")) {
                model.put("fuflaan", 1);
            }
        }
        CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
        model.put("fulanId", fulanDto.getId());
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        if (memberService.isHead(groupId, getUserId())) {
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
     * @param remarkId  关键字Id
     * @param endUserId 被设置备注名的人的Id
     * @param remark    备注名
     * @return
     */
    @RequestMapping("/setRemark")
    @ResponseBody
    public RespObj setRemark(String remarkId,
                             String endUserId, String remark) {
        ObjectId userId = getUserId();
        if (StringUtils.isNotBlank(remarkId)) {
            communityService.updateRemark(new ObjectId(remarkId), remark);
        } else {
            RemarkEntry entry = new RemarkEntry(userId, new ObjectId(endUserId), remark);
            communityService.saveRemark(entry);
        }
        return RespObj.SUCCESS;
    }

    /**
     * 设置备注名
     *
     * @param endUserId 被设置备注名的人的Id
     * @param remark    备注名
     * @return
     */
    @RequestMapping("/setAppRemark")
    @ResponseBody
    public RespObj setAppRemark(String endUserId, String remark) {
        ObjectId userId = getUserId();
        RemarkEntry remarkEntry=communityService.getRemarkEntry(userId,new ObjectId(endUserId));
        if (null!=remarkEntry) {
            communityService.updateRemark(remarkEntry.getID(), remark);
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
        ObjectId groupId = communityService.getGroupId(new ObjectId(communityId));
        ObjectId userId = getUserId();
        if (null != userId) {
            if (memberService.isManager(groupId, userId)) {
                model.put("operation", 1);
            } else {
                model.put("operation", 0);
            }
        }

        return "/community/communityMessageList";
    }

    /**
     * 删除选中列表
     */
    @RequestMapping("/deleteMembers")
    @ResponseBody
    public RespObj deleteMembers(@ObjectIdType ObjectId communityId, String memberUserId) {
        List<ObjectId> userIds = MongoUtils.convertObjectIds(memberUserId);
        CommunityDTO community = communityService.findByObjectId(communityId);
        ObjectId groupId = communityService.getGroupId(communityId);
        for (ObjectId userId : userIds) {

            if (memberService.isHead(new ObjectId(community.getGroupId()), userId)) {
                return RespObj.FAILD("不能删除社长");
            }
            if (emService.removeUserFromEmGroup(community.getEmChatId(), userId)) {
                communityService.pullFromUser(communityId, userId);
                memberService.deleteMember(groupId, userId);
                //设置先前该用户所发表的数据为废弃掉的
                communityService.setPartIncontentStatus(communityId, userId, 1);
                if (memberService.isManager(groupId, userId)) { //发送退出消息
                    //当是副社长退出时
                    List<ObjectId> objectIds = communityService.getAllMemberIds(groupId);
                    communitySystemInfoService.addBatchData(userId, objectIds, "副社长", 1, communityId);
                }

            }
        }
        return RespObj.SUCCESS("删除成员成功！");
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
        List<ObjectId> objectIds = MongoUtils.convertObjectIds(memberId);
        List<ObjectId> userIds = MongoUtils.convertObjectIds(memberUserId);
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

        //更新申请状态
        for (ObjectId item : userIds) {
            if (role == 1) {
                validateInfoService.updateAuthority(item, communityId, 0);
            } else {
                validateInfoService.updateAuthority(item, communityId, 1);
            }
        }


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
        ObjectId userId=getUserId();
        CommunityDetailDTO communityDetailDTO = communityService.findDetailById(detailId,userId);
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
                user1.setTime(DateUtils.timeStampToStr(new ObjectId(partInContentDTO.getPartInContentId()).getTimestamp()));
            }

            users.add(user1);
        }
        return RespObj.SUCCESS(users);
    }

    public List<User> getPartInData(ObjectId detailId) {
        CommunityDetailDTO communityDetailDTO = communityService.findDetailById(detailId,null);
        List<String> partInList = communityDetailDTO.getPartInList();
        List<User> users = new ArrayList<User>();
        for (String id : partInList) {
            UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(id);
            User user1 = new User();
            user1.setName(StringUtils.isNotBlank(userDetailInfoDTO.getNickName()) ? userDetailInfoDTO.getNickName() : userDetailInfoDTO.getUserName());
            PartInContentDTO partInContentDTO = communityService.getPartInContent(detailId, new ObjectId(userDetailInfoDTO.getId()));
            if (partInContentDTO != null) {
                user1.setContent(partInContentDTO.getInformation());
                user1.setTime(DateUtils.timeStampToStr(new ObjectId(partInContentDTO.getPartInContentId()).getTimestamp()));
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
        //先进行敏感词过滤
        text = userService.replaceSensitiveWord(text);
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
        Boolean message = communityService.zanToPartInContent(partInContentId, getUserId(), zan);
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
            List<CommunityService.UserSearchInfo> dtos = new ArrayList<CommunityService.UserSearchInfo>();
            if (ObjectId.isValid(regular)) {
                UserEntry userEntry = userService.findById(new ObjectId(regular));
                if (null == userEntry) {
                    return RespObj.FAILD("查找不到该用户！");
                } else {
                    dtos.add(communityService.getDto(userEntry));
                }
            } else {
                //新产生的Id查找
                UserEntry userEntry1 = userService.findByGenerateCode(regular);
                if (null != userEntry1) {
                    dtos.add(communityService.getDto(userEntry1));
                }
                if (dtos.size() == 0) {
                    //用户名精确查找
                    List<UserEntry> userEntryList = userService.getInfoByName("nm", regular);
                    if (userEntryList.size() > 0) {
                        dtos = communityService.getUserInfo(userEntryList);
                        count = dtos.size();
                    } else {
                        //昵称精确查找
                        List<UserEntry> userEntryList1 = userService.getInfoByName("nnm", regular);
                        if (userEntryList1.size() > 0) {
                            dtos = communityService.getUserInfo(userEntryList1);
                            count = dtos.size();
                        } else {
                            //判断是否存储了Id,存储时间为5分钟
                            String field1 = "nm";
                            String filed2 = "nnm";
                            ObjectId userId = getUserId();
                            String key = userId.toString() + "$" + regular;
                            Map map = RedisUtils.getMap(key);
                            UserEntry userEntry = userService.getUserInfoEntry(regular);
                            if (null != userEntry) {
                                List<UserEntry> userEntries = userService.getUserList(field1, regular, page, pageSize);
                                dtos = communityService.getUserInfo(userEntries);
                                if (null != map && map.size() != 0) {
                                    count = Integer.parseInt((String) map.get("count"));
                                } else {
                                    count = userService.countUserList(field1, regular);
                                }
                            } else {
                                List<UserEntry> userEntries = userService.getUserList(filed2, regular, page, pageSize);
                                dtos = communityService.getUserInfo(userEntries);
                                if (null != map && map.size() != 0) {
                                    count = Integer.parseInt((String) map.get("count"));
                                } else {
                                    count = userService.countUserList(filed2, regular);
                                }
                            }
                            //存储key-value
                            Map hashMap = new HashMap();
                            if (dtos.size() > 0) {
                                hashMap.put("count", count + "");
                            }
                            if (null != hashMap && hashMap.size() > 0) {
                                RedisUtils.cacheMap(key, hashMap, Constant.SESSION_FIVE_MINUTE);
                            }
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
                               @RequestParam(required = false, defaultValue = "") String attacheMents,
                               @RequestParam(required = false, defaultValue = "") String videoList) {
        ObjectId uid = getUserId();
        communityService.saveHomeWork(communityId, communityDetailId, uid, content, images, attacheMents,videoList, type);
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

        ObjectId uid = getUserId();
        boolean isLegal = true;
        try {
            //先进行敏感词过滤
            shareCommend = userService.replaceSensitiveWord(shareCommend);
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient client = httpClientBuilder.build();
            //先判断是否是合法的路径
            if (!isLegalUrl(shareUrl)) {
                isLegal = false;
            }
            //抓取的数据
            ProductModel productModel = URLParseUtil.urlParser(client, shareUrl);
            if (StringUtils.isBlank(productModel.getImageUrl())) {
                isLegal = false;
                return RespObj.SUCCESS("操作成功");
            } else {
                if (isLegal) {
                    communityService.saveCommunityShare(communityId, communityDetailId, uid, productModel, shareCommend, 6);
                }
            }
            return RespObj.SUCCESS("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            isLegal = !isLegal;
            if (!isLegal) {
                return RespObj.SUCCESS("操作成功");
            } else {
                return RespObj.FAILD("访问不了此链接!");
            }
        } finally {
            if (!isLegal) {
                communityService.saveCommunityRecommend(communityId, communityDetailId, uid, shareUrl, "", "", "", shareCommend, 6);
            }
        }
    }

    private boolean isLegalUrl(String urlStr) {
        String urlStr1;
        String urlStr2;
        if (!urlStr.contains("http")) {
            urlStr1 = "http://" + urlStr;
            urlStr2 = "https://" + urlStr;
        } else {
            urlStr1 = urlStr;
            urlStr2 = urlStr;
        }
        boolean isUrl = false;
        try {
            URL url = new URL(urlStr1);
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isUrl = true;
            }
            con.disconnect();
            if (!urlStr1.equals(urlStr2)) {
                if (!isUrl) {
                    URL url2 = new URL(urlStr2);
                    URLConnection con2 = url2.openConnection();
                    String strFiled = con2.getHeaderField(0);
                    if (strFiled.indexOf("OK") > 0) {
                        isUrl = true;
                    }
                }
            }
            return isUrl;
        } catch (Exception ex) {
            return false;
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
        //先进行敏感词过滤
        shareCommend = userService.replaceSensitiveWord(shareCommend);
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
        List<ObjectId> userIdList = MongoUtils.convertObjectIds(userIds);
        for (ObjectId userId : userIdList) {
            if (!memberService.isGroupMember(groupId, userId)) {
                if (memberService.isBeforeMember(groupId, userId)) {
                    if (emService.addUserToEmGroup(emChatId, userId)) {
                        memberService.updateMember(groupId, userId, 0);
                        communityService.setPartIncontentStatus(communityId, userId, 0);
                        communityService.pushToUser(communityId, userId, 1);
                    }
                } else {
                    if (emService.addUserToEmGroup(emChatId, userId)) {
                        memberService.saveMember(userId, groupId);
                        communityService.pushToUser(communityId, userId, 1);
                    }
                }
            }
        }
        return RespObj.SUCCESS("操作成功");
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


    @RequestMapping("/searchHotCommunity")
    @LoginInfo
    public String searchHotCommunity() {
        return "/community/hotCommunity";
    }


    /**
     * 判断取的名称是否唯一
     *
     * @return
     */
    @RequestMapping("/judgeCreate")
    @ResponseBody
    public RespObj judgeCreateCommunity(String communityName) {
        return RespObj.SUCCESS(!communityService.isCommunityNameUnique(communityName));
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
    @SessionNeedless
    public String redirectUser(Map<String, Object> model) {
        ObjectId personId = new ObjectId(getRequest().getParameter("userId"));
        UserEntry userEntry = userService.findById(personId);

        model.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
        model.put("nickName", StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        if (getUserId() != null) {
            ObjectId userId = getUserId();
            UserEntry userEntry1 = userService.findById(userId);
            model.put("applyName", StringUtils.isNotBlank(userEntry1.getNickName()) ? userEntry1.getNickName() : userEntry1.getUserName());

            //获取自己的标签信息
            List<UserEntry.UserTagEntry> userTagEntries = userEntry.getUserTag();
            List<String> tags = new ArrayList<String>();
            for (UserEntry.UserTagEntry userTagEntry : userTagEntries) {
                tags.add(userTagEntry.getTag());
            }
            model.put("tags", tags);

            //判断是否为好友
            boolean isFriend = friendService.isFriend(personId.toString(), userId.toString());
            if (isFriend) {
                model.put("friend", "玩伴");
            } else {
                List<FriendApply> friendApplyList = friendApplyService.findApplyBySponsorIdAndRespondentId(userId.toString(), personId.toString());
                if (null != friendApplyList && !friendApplyList.isEmpty()) {
                    model.put("friend", "等回复");
                } else {
                    model.put("friend", "加玩伴");
                }
            }

        }
        model.put("personId", personId.toString());
        if (org.apache.commons.lang.StringUtils.isNotBlank(userEntry.getGenerateUserCode())) {
            model.put("packageCode", userEntry.getGenerateUserCode());
        } else {
            model.put("packageCode", personId.toString());
        }
        model.put("communityNames", communityService.generateCommunityNames(personId));

        return "/community/communityUser";
    }

    @RequestMapping("/pushUserTag")
    @ResponseBody
    public RespObj pushUserTag(@RequestBody UserTag userTag) {
        ObjectId userId = getUserId();
        userService.pushUserTag(userId, userTag.getCode(), userTag.getTag());
        mateService.pushUserTag(userId, userTag.getCode());
        return RespObj.SUCCESS;
    }

    @RequestMapping("/pullUserTag")
    @ResponseBody
    public RespObj pullUserTag(int code) {
        ObjectId userId = getUserId();
        userService.pullUserTag(userId, code);
        mateService.pullUserTag(userId, code);
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
        UserEntry userEntry = userService.findById(userId);
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
        UserEntry userEntry = userService.findById(userId);
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
                String type = "?imageMogr2/thumbnail/600x500";//压缩样式
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
     * 保存涂鸦 - 图片
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveTuYaImage", method = RequestMethod.POST)
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
     *
     * @param detailId
     * @return
     */
    @RequestMapping("/removeDetailById")
    @ResponseBody
    public RespObj removeDetailById(@ObjectIdType ObjectId detailId) {
        communityService.removeCommunityDetailById(detailId);
        return RespObj.SUCCESS;
    }


    /**
     * 记录删除消息的人员列表
     * @param detailId
     * @return
     */
    @RequestMapping("/recordDeleteUserIds")
    @ResponseBody
    public RespObj recordDeleteUserIds(@ObjectIdType ObjectId detailId) {
        communityService.recordDeleteUserIds(detailId,getUserId());
        return RespObj.SUCCESS;
    }


    /**
     * 获取系统消息列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getCommunitySysInfo")
    @ResponseBody
    public Map<String, Object> getCommunitySysInfo(@RequestParam(required = false, defaultValue = "1") int page,
                                                   @RequestParam(required = false, defaultValue = "20") int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId userId = getUserId();
        List<CommunitySystemInfoDTO> dtos = communitySystemInfoService.findInfoByUserIdAndType(userId, page, pageSize);
        int count = communitySystemInfoService.countEntriesByUserIdAndType(userId);
        //加载完数据设置已读
        communitySystemInfoService.setAllData(userId);
        map.put("list", dtos);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }


    /**
     * web端涂鸦控件
     *
     * @param base64ImgData
     * @param oldImage
     * @param partContentId
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/base64image")
    @ResponseBody
    public RespObj uploadBase64Image(String base64ImgData, String oldImage, String partContentId, HttpServletRequest req) throws Exception {
        String temp = base64ImgData.substring(base64ImgData.indexOf("base64") + 7, base64ImgData.length());
//        BASE64Decoder d = new BASE64Decoder();
//        byte[] bs = d.decodeBuffer(temp);
//        for (int i = 0; i < bs.length; ++i) {
//            if (bs[i] < 0) {// 调整异常数据
//                bs[i] += 256;
//            }
//        }
        Base64 base64 = new Base64();
        byte[] b = base64.decodeBase64(temp.getBytes());
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        String parentPath = req.getServletContext().getRealPath("/upload") + "/qiuNiu";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
//        String filekey = new ObjectId() + ".png";
        String fileName = new ObjectId() + ".jpeg";
        File attachFile = new File(parentFile, fileName);
        try {
            OutputStream out = new FileOutputStream(attachFile);
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
//        BufferedImage bi1 = ImageIO.read(bais);


//        ImageIO.write(bi1, "jpg", attachFile);

        String fileKey1 = new ObjectId().toString() + Constant.POINT + "png";
        QiniuFileUtils.uploadFile(fileKey1, new FileInputStream(attachFile), QiniuFileUtils.TYPE_IMAGE);
        String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey1);
        communityService.updateImage(new ObjectId(partContentId), path, "http://" + URLDecoder.decode(oldImage, "UTF-8"));
        return RespObj.SUCCESS;
    }


    /**
     * 保存涂鸦图片 - web端
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveEditedImage")
    @ResponseBody
    public RespObj saveEditedImage(HttpServletRequest request) throws Exception {
        String filekey = "qiuNiu-" + new ObjectId().toString() + ".png";
        String parentPath = request.getServletContext().getRealPath("/upload") + "/qiuNiu/";
        File parentFile = new File(parentPath);
        File attachFile = new File(parentFile, filekey);
        try {
            ServletInputStream inputStream = request.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, attachFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        String[] partInContentId = request.getParameter("partInContentId").split("-");
        String fileKey = new ObjectId().toString() + Constant.POINT + "png";
        QiniuFileUtils.uploadFile(fileKey, new FileInputStream(attachFile), QiniuFileUtils.TYPE_IMAGE);
        String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
        communityService.updateImage(new ObjectId(partInContentId[0]), path, "http://" + partInContentId[1]);
        try {
            attachFile.delete();
            return RespObj.SUCCESS;
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping("/getMyQRCode")
    @ResponseBody
    public RespObj getMyQRCode() {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.findById(userId);
        return getQRCode(userEntry,userId);
    }

    public RespObj getQRCode(UserEntry userEntry,ObjectId userId){
        if (StringUtils.isBlank(userEntry.getQRCode())) {
            String qrCode = QRUtils.getPersonQrUrl(userId);
            userEntry.setQRCode(qrCode);
            userService.addUser(userEntry);
            return RespObj.SUCCESS(qrCode);
        } else {
            return RespObj.SUCCESS(userEntry.getQRCode());
        }
    }

    public RespObj getQRBindCode(UserEntry userEntry,ObjectId userId){
        if (StringUtils.isBlank(userEntry.getQRCode())) {
            String qrCode = QRUtils.getPersonBindQrUrl(userId);
            userEntry.setQRBindCode(qrCode);
            userService.addUser(userEntry);
            return RespObj.SUCCESS(qrCode);
        } else {
            return RespObj.SUCCESS(userEntry.getQRBindCode());
        }
    }

    @RequestMapping("/getMyQRBindCodeByUserId")
    @ResponseBody
    @SessionNeedless
    public RespObj getMyQRBindCodeByUserId(@ObjectIdType  ObjectId userId) {
        UserEntry userEntry = userService.findById(userId);
        return getQRBindCode(userEntry,userId);
    }

    @RequestMapping("/getMyQRCodeByUserId")
    @ResponseBody
    @SessionNeedless
    public RespObj getMyQRCodeByUserId(@ObjectIdType  ObjectId userId) {
        UserEntry userEntry = userService.findById(userId);
        return getQRCode(userEntry,userId);
    }


    @RequestMapping("/getMyInfo")
    @ResponseBody
    public RespObj getMyInfo() {
        ObjectId userId = getUserId();
        Map<String, String> map = new HashMap<String, String>();
        UserEntry userEntry = userService.findById(userId);
        if (StringUtils.isNotBlank(userEntry.getGenerateUserCode())) {
            map.put("uid", userEntry.getGenerateUserCode());
        } else {
            map.put("uid", userEntry.getID().toString());
        }
        map.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
        if (StringUtils.isNotBlank(userEntry.getNickName())) {
            map.put("nickName", userEntry.getNickName());
        } else {
            map.put("nickName", userEntry.getUserName());
        }
        if (StringUtils.isBlank(userEntry.getQRCode())) {
            String qrCode = QRUtils.getPersonQrUrl(userId);
            userEntry.setQRCode(qrCode);
            userService.addUser(userEntry);
            map.put("qrCode", qrCode);

            return RespObj.SUCCESS(map);
        } else {
            map.put("qrCode", userEntry.getQRCode());
            return RespObj.SUCCESS(map);
        }

    }


    @RequestMapping("/updateCommunityPrio")
    @ResponseBody
    public RespObj updateCommunityPrio(@ObjectIdType ObjectId cmid, int prio) {
        communityService.updateCommunityPrio(cmid, prio);
        return RespObj.SUCCESS;
    }


    /**
     * 设置置顶
     *
     * @param communityId
     * @param top
     * @return
     */
    @RequestMapping("/updateCommunityTop")
    @ResponseBody
    public RespObj updateCommunityTop(@ObjectIdType ObjectId communityId, int top) {
        communityService.setTop(communityId, getUserId(), top);
        return RespObj.SUCCESS;
    }

    @RequestMapping("/setDefaultSort")
    @ResponseBody
    public RespObj setDefaultSort() {
        communityService.setDefaultSort();
        return RespObj.SUCCESS;
    }

    /**
     * 获取登录人所有的备注名
     *
     * @return
     */
    @RequestMapping("/getMyAllRemarks")
    @ResponseBody
    public RespObj getMyAllRemarks() {
        ObjectId userId = getUserId();
        List<RemarkDTO> remarkDTOs = communityService.getRemarkDtos(userId);
        return RespObj.SUCCESS(remarkDTOs);
    }


    /**
     * 置顶数据
     * @return
     */
    @RequestMapping("/updateCommunityDetailTop")
    @ResponseBody
    public RespObj updateCommunityDetailTop(@ObjectIdType ObjectId detailId,int top){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        communityService.updateCommunityDetailTop(detailId,top);
        return respObj;
    }


    /**
     * 点赞功能
     */
    @RequestMapping("/updateCommunityDetailZan")
    @ResponseBody
    public RespObj updateCommunityDetailZan(@ObjectIdType ObjectId detailId,int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        ObjectId userId=getUserId();
        CommunityDetailEntry entry=communityService.getEntryById(detailId);
        List<ObjectId> userIds=entry.getZanList();
        if(type==1){
            if(userIds.contains(userId)){
                respObj.setMessage("已经点过赞了");
            }else{
                respObj.setCode(Constant.SUCCESS_CODE);
                communityService.updateCommunityDetailZan(detailId,userId,type);
                respObj.setMessage("点赞成功");
            }
        }else{
            if(userIds.contains(userId)){
                respObj.setCode(Constant.SUCCESS_CODE);
                communityService.updateCommunityDetailZan(detailId,userId,type);
                respObj.setMessage("取消点赞成功");
            }else{
                respObj.setMessage("已经取消赞了,请刷新");
            }
        }
        return respObj;
    }


    /**
     * 删除回复
     */
    @RequestMapping("/removePartInContentInfo")
    @ResponseBody
    public RespObj removePartInContentInfo(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        communityService.removePartInContentInfo(id);
        respObj.setMessage("删除回复成功");
        return respObj;
    }


    /**
     * 新加功能(留言反馈)
     */
    @RequestMapping("/saveFeedbackContent")
    @ResponseBody
    public RespObj saveFeedbackContent(String content){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        ObjectId userId=getUserId();
        feedbackService.saveFeedBack(userId,content);
        respObj.setMessage("添加留言成功");
        return respObj;
    }

    /**
     * 分页获取留言列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getFeedbacks")
    @ResponseBody
    public RespObj getFeedbacks(@RequestParam(value="page",defaultValue = "1")int page,
                                @RequestParam(value="pageSize",defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        Map<String,Object> map=new HashMap<String,Object>();
        ObjectId userId=getUserId();
        List<FeedbackDTO> feedbackDTOs=feedbackService.getFeedbackDtos(page,pageSize,userId);
        int count=feedbackService.countFeedBack();
        map.put("list",feedbackDTOs);
        map.put("count",count);
        respObj.setMessage(map);
        return respObj;
    }

    /**
     * 删除留言反馈的信息
     * @param id
     * @return
     */
    @RequestMapping("/removeFeedBack")
    @ResponseBody
    public RespObj removeFeedBack(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        feedbackService.removeFeedBack(id);
        respObj.setMessage("删除留言成功");
        return respObj;
    }

    /**
     * 获取与我有关的模块列表信息
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    @RequestMapping("/getMyMessageByType")
    @ResponseBody
    @SessionNeedless
    public RespObj getMyMessageByType(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "4") int pageSize,
                                     @RequestParam(required = false, defaultValue = "-1") int type) {
        ObjectId userId = getUserId();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        if (null == userId) {
            CommunityDTO fulanDTO = communityService.getCommunityByName("复兰社区");
            if (fulanDTO != null && fulanDTO.getId() != null) {
                communityIds.add(new ObjectId(fulanDTO.getId()));
                return RespObj.SUCCESS(communityService.getMyMessageByType(communityIds,userId,type,page,pageSize));
            }
            return RespObj.FAILD("没有数据");
        } else {
            List<CommunityDTO> communityDTOList = communityService.getCommunitys(userId, -1, 0);
            for (CommunityDTO communityDTO : communityDTOList) {
                communityIds.add(new ObjectId(communityDTO.getId()));
            }
            return RespObj.SUCCESS(communityService.getMyMessageByType(communityIds,userId, type,page,pageSize));
        }
    }

}
