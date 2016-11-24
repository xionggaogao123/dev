package com.fulaan.groupdiscussion.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.groupdiscussion.dto.GroupChatDTO;
import com.fulaan.groupdiscussion.dto.GroupDiscussionDTO;
import com.fulaan.groupdiscussion.dto.GroupFileDTO;
import com.fulaan.groupdiscussion.dto.GroupUserDTO;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.groupdiscussion.service.GroupDiscussionService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.IdValuePair;
import com.pojo.groups.GroupsChatEntry;
import com.pojo.groups.GroupsEntry;
import com.pojo.groups.GroupsFileEntry;
import com.pojo.groups.GroupsUser;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by fourer on 15-2-26.
 */
@Controller
@RequestMapping("groupchat")
public class GroupDiscussionController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupDiscussionController.class);

    @Autowired
    private GroupDiscussionService groupDiscussionService;

    @Autowired
    private UserService userService;

    @Autowired
    private EaseMobService easeMobService;

    @Autowired
    private ExperienceService experienceService;

    /**
     * 查询所有列表
     *
     * @param
     * @return
     */
    @RequestMapping("/selGroup")
    @ResponseBody
    public List<GroupDiscussionDTO> selGroup() {
        String chatid = getSessionValue().getChatid();
        UserEntry user = userService.searchUserChatId(chatid);
        List<IdValuePair> groupInfoList = user.getGroupInfoList();
        List<GroupDiscussionDTO> groups = new ArrayList<GroupDiscussionDTO>();
        GroupDiscussionDTO group = null;
        if (groupInfoList!=null && groupInfoList.size()!=0) {
            for (IdValuePair idvalue : groupInfoList) {
                group = new GroupDiscussionDTO();
                group.setRoomid(idvalue.getBaseEntry().getString("id"));
                GroupsEntry groupentry = groupDiscussionService.selSingleGroup(idvalue.getBaseEntry().getString("id"));
                group.setGroupname(groupentry.getGroupname());
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * 新建群组
     *
     * @param group 群组的各项属性
     * @return
     */
    @RequestMapping("/addGroup")
    @ResponseBody
    public Map<String, Object> addGroup(GroupDiscussionDTO group) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            group.setMaingroup(getSessionValue().getChatid());
            GroupsEntry groupsEntry = group.buildGroupsEntry();
            groupDiscussionService.addGroup(groupsEntry);

            if (UserRole.isTeacher(getSessionValue().getUserRole())) {
                ExpLogType expLogType = ExpLogType.LAUNCH_GROUP_CHAT;
                if (experienceService.updateScore(getUserId().toString(), expLogType, groupsEntry.getID().toString())) {
                    result.put("scoreMsg", expLogType.getDesc());
                    result.put("score", expLogType.getExp());
                }
            }

            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    /**
     * 更新群组
     *
     * @param group 群组的各项属性
     * @return
     */
    @RequestMapping("/updateGroup")
    @ResponseBody
    public Map<String, Object> updateGroup(GroupDiscussionDTO group) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            group.setMaingroup(getSessionValue().getChatid());
            GroupsEntry groupsEntry = group.buildGroupsEntry();
            groupDiscussionService.updateGroup(groupsEntry, group.getUseridAry());
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }
    /**
     * 更新群名称
     *
     * @param group
     * @return
     */
    @RequestMapping("/updateGroupName")
    @ResponseBody
    public Map<String, Object> updateGroupName(GroupDiscussionDTO group) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            group.setMaingroup(getSessionValue().getChatid());
            GroupsEntry groupsEntry = group.buildGroupsEntry();
            groupDiscussionService.updateGroupName(groupsEntry);
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

//    /**
//     * 删除群成员
//     *
//     * @param group
//     * @return
//     */
//    @RequestMapping("/deleteGroupUser")
//    @ResponseBody
//    public Map<String, Object> deleteGroupUser(GroupDiscussionDTO group) {
//        Map<String, Object> result = new HashMap<String, Object>();
//        try {
//            group.setMaingroup(getSessionValue().getId());
//            GroupsEntry groupsEntry = group.buildGroupsEntry();
//            groupDiscussionService.deleteGroupUser(new ObjectId(group.getId()), groupsEntry);
//            result.put("resultCode", 0);
//        } catch (Exception e) {
//            result.put("resultCode", 1);
//        }
//        return result;
//    }

    /**
     * 添加群成员
     *
     * @param group
     * @return
     */
    @RequestMapping("/addGroupUser")
    @ResponseBody
    public Map<String, Object> addGroupUser(GroupDiscussionDTO group) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            group.setMaingroup(getSessionValue().getChatid());
            GroupsEntry groupsEntry = group.buildGroupsEntry();
            groupDiscussionService.addGroupUser(new ObjectId(group.getId()), groupsEntry);
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    //app接口 end

    /**
     * 添加成员
     *
     * @param userid
     * @param roomid
     * @param id
     * @return
     */
    @RequestMapping("/addGroupMember")
    @ResponseBody
    public Map<String, Object> addGroupMember(@RequestParam("userid") String userid, @RequestParam("roomid") String roomid, @RequestParam("roomid") String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            groupDiscussionService.addGroupMember(userid, roomid, id);
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

//    /**
//     * 删除成员
//     *
//     * @param userid
//     * @param roomid
//     * @return
//     */
//    @RequestMapping("/delGroupMember")
//    @ResponseBody
//    public Map<String, Object> delGroupMember(@RequestParam("userid") String userid, @RequestParam("roomid") String roomid, @RequestParam("id") String id) {
//        Map<String, Object> result = new HashMap<String, Object>();
//        try {
//            groupDiscussionService.delGroupMember(userid, roomid, id);
//            result.put("resultCode", 0);
//        } catch (Exception e) {
//            result.put("resultCode", 1);
//        }
//        return result;
//    }

    /**
     * 删除群组
     *
     * @param roomid
     * @param type
     * @return
     */
    @RequestMapping("/deleteGroup")
    @ResponseBody
    public Map<String, Object> deleteGroup(@RequestParam("roomid") String roomid, int type) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            groupDiscussionService.deleteGroup(roomid, type, getSessionValue().getChatid());
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping(value = "/upload", produces = "text/html; charset=utf-8")
    @ResponseBody
    public List<String> uploadFile(MultipartRequest request, @RequestParam("roomid") String roomid) {
        List<String> fileUrls = new ArrayList<String>();
        GroupsFileEntry groupFile = null;
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }

//                File dir = new File(randomPath);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                File f = new File(dir, file.getOriginalFilename());
//                file.transferTo(f);
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,examName));
                groupFile = new GroupsFileEntry(roomid, QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,examName),getSessionValue().getChatid(), file.getOriginalFilename(), System.currentTimeMillis(), 0, (int) file.getSize() / 1024,0);
                groupDiscussionService.insertGroupFile(groupFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrls;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    @RequestMapping("/download")
    @ResponseBody
    public Map download(String fileid, HttpServletRequest request,
                           HttpServletResponse response) throws UnsupportedEncodingException {
//    	Map<String, Object> result = new HashMap<>();
//        GroupsFileEntry groupfile = groupDiscussionService.selGroupFile(new ObjectId(fileid));
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment;fileName="
//                + java.net.URLEncoder.encode(groupfile.getFilename(), "UTF-8"));
//        File f = new File("C:\\fulaan\\" + groupfile.getFilepath().substring(0, 33));
//        f.mkdirs();
//        BufferedOutputStream bos = null;
//        try {
//            InputStream inputStream = new FileInputStream(new File(groupfile.getFilepath()));
////            bos = new BufferedOutputStream(new FileOutputStream(f.getPath()+groupfile.getFilePath().substring(33)));
//            OutputStream os = response.getOutputStream();
//            byte[] b = new byte[2048];
//            int length;
//            while ((length = inputStream.read(b)) > 0) {
//                os.write(b, 0, length);
//            }
            groupDiscussionService.updateGroupFile(new ObjectId(fileid));
            // 这里主要关闭。
//            os.close();
//
//            inputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
//        Map<String,Object> filepath = new HashMap<String, Object>();
//        filepath.put("filepath",QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT,groupfile.getFilepath()));
        return null;
    }

    /**
     * 查询文件列表
     *
     * @param roomid
     * @param pageable
     * @return
     */
    @RequestMapping("/selFileList")
    @ResponseBody
    public Page<GroupFileDTO> selFileList(@RequestParam("roomid") String roomid, Pageable pageable) {
        return groupDiscussionService.selGroupFileList(roomid, pageable);
    }

    /**
     * 删除群文件
     *
     * @param id
     * @return
     */
    @RequestMapping("/deletefile")
    @ResponseBody
    public Map<String, Object> deletefile(@RequestParam("id") String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            groupDiscussionService.deleteGroupFile(id);
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    /**
     * 发送消息
     *
     * @param groupChat
     * @return
     */
    @RequestMapping("send")
    @ResponseBody
    public Map<String, Object> sendMessage(GroupChatDTO groupChat) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            groupChat.setGroupUserid(getSessionValue().getChatid());
            GroupsChatEntry chat = groupChat.buildGroupChat();
            groupDiscussionService.addGroupChat(chat);
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    /**
     * 是否是群主
     *
     * @param roomid
     * @return
     */
    @RequestMapping("isGroupMain")
    @ResponseBody
    public Map<String, Object> isGroupMain(@RequestParam("roomid") String roomid) {
        Map<String, Object> result = new HashMap<String, Object>();
        GroupsEntry group = groupDiscussionService.selSingleGroup(roomid);
        List<GroupsUser> userList = group.getGroupsUserList();
        int count = 0;
        List<GroupUserDTO> gorupUserList = new ArrayList<GroupUserDTO>();
        GroupUserDTO groupuser = null;
        if (userList != null && userList.size() != 0) {
            for (GroupsUser userview : userList) {
                groupuser = new GroupUserDTO();
                groupuser.setUserid(userview.getUserid().toString());
                groupuser.setMaingroup(group.getUserid().toString());
                UserEntry user = userService.searchUserChatId(userview.getUserid());
                if (user!=null) {
                    groupuser.setUserName(user.getUserName());
                    groupuser.setMaxImageURL(AvatarUtils.getAvatar(user.getAvatar(), 3));
                } else {
                    System.out.print(userview.getUserid());
                }
                if (group.getUserid().equals(userview.getUserid())) {
                    groupuser.setIsgroupmain(true);
                }
                gorupUserList.add(groupuser);
            }
            count = userList.size() - 1;
        }
        result.put("groupmemberList", gorupUserList);
        result.put("count", count);
        result.put("isgroupmain", groupDiscussionService.isGroupMain(roomid, getSessionValue().getChatid()));
        return result;
    }

    /**
     * 收到消息更新状态
     *
     * @param refGroupUser
     * @return
     */
    @RequestMapping("updateStatus")
    @ResponseBody
    public Map<String, Object> updateStatus(GroupUserDTO refGroupUser) {
        Map<String, Object> result = new HashMap<String, Object>();
        refGroupUser.setUserid(getSessionValue().getChatid());
        GroupsUser groupuser = new GroupsUser(
                refGroupUser.getUserid(),
                1,
                new Date().getTime()/1000

        );
        try {
            groupDiscussionService.updateStatus(groupuser,refGroupUser.getRoomid());
            result.put("resultCode", 0);
        } catch (Exception e) {
            result.put("resultCode", 1);
        }
        return result;
    }

    /**
     * 初始化聊天消息
     *
     * @return
     */
    @RequestMapping("selChatList")
    @ResponseBody
    public List<GroupDiscussionDTO> selChatList() {
        UserEntry user = userService.searchUserChatId(getSessionValue().getChatid());
        List<IdValuePair> groupinfolist = user.getGroupInfoList();
        List<GroupDiscussionDTO> grouplist = new ArrayList<GroupDiscussionDTO>();
        GroupDiscussionDTO groups = new GroupDiscussionDTO();
        for (IdValuePair vpair : groupinfolist) {
            GroupsEntry groupsentry = groupDiscussionService.selectGroupByRoomid(vpair.getBaseEntry().getString("id"));
            Map<String, Object> result = groupDiscussionService.selChatList(groupsentry.getID(), getSessionValue().getChatid());
            if (result.get("groupchatList") != null) {
                groups.setGroupchatList((List<GroupChatDTO>) result.get("groupchatList"));
            }
            groups.setNotviewcount(Integer.parseInt(result.get("notviewCount").toString()));
            groups.setFlag(Boolean.valueOf(result.get("flag").toString()));
            grouplist.add(groups);
        }
        return grouplist;
    }

    /**
     * 聊天历史消息
     * @param groupchatdto
     * @param pageable
     * @return
     */
    @RequestMapping("selHistoryChatList")
    @ResponseBody
    public Page<GroupChatDTO> selHistoryChatList(GroupChatDTO groupchatdto,  Pageable pageable) throws ResultTooManyException {
        return groupDiscussionService.selHistoryChatList(groupchatdto, pageable);
    }


    @RequestMapping("index")
    public String chatIndex(Map<String, Object> model) {
        model.put("appKey", easeMobService.getAppKey());
        return "groupchat/chatIndex";
    }
    
    
//public int j = 0;
//    @RequestMapping("registChatid")
//    @ResponseBody
//    public Map<String, Object>  registChatid() {
//        Map map = new HashMap();
//        List<String> userlist = groupDiscussionService.selUserBySchoolid("55934c15f6f28b7261c19c98");
//        try {
//                for (int i=0;i<300;i++) {
//                    if (!StringUtils.isEmpty(userlist.get(i))) {
//                        easeMobService.createNewUser(userlist.get(i));
//                    }
//                }
//            j++;
//            map.put("result","success"+j);
//        } catch(Exception e) {
//            map.put("result","failed"+j);
//        }
//        return map;
//    }

//    @RequestMapping("index2")
//    public String chatIndex2() {
//        return "groupchat/Index";
//    }
    }
