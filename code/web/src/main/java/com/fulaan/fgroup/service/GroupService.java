package com.fulaan.fgroup.service;

import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.groupchatrecord.GroupChatRecordDao;
import com.db.groupchatrecord.RecordChatPersonalDao;
import com.db.groupchatrecord.RecordTotalChatDao;
import com.easemob.server.comm.constant.MsgType;
import com.fulaan.dto.MemberDTO;
import com.fulaan.fgroup.dto.GroupChatRecordDTO;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.dto.RecordChatRelationDTO;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.fulaan.util.ImageUtils;
import com.fulaan.util.QRUtils;
import com.pojo.fcommunity.GroupEntry;
import com.pojo.groupchatrecord.GroupChatRecordEntry;
import com.pojo.groupchatrecord.RecordChatPersonalEntry;
import com.pojo.groupchatrecord.RecordTotalChatEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by jerry on 2016/11/1.
 */
@Service
public class GroupService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmService emService;

    private GroupDao groupDao = new GroupDao();
    private MemberDao memberDao = new MemberDao();

    private RecordChatPersonalDao recordChatPersonalDao = new RecordChatPersonalDao();

    private GroupChatRecordDao groupChatRecordDao = new GroupChatRecordDao();

    private RecordTotalChatDao recordTotalChatDao = new RecordTotalChatDao();


    public static void main(String[] args){
//        private String id;(传空字符串)
//        private String groupId;//群组Id（可选）
//        private String userId;//用户Id(传空字符串)
//        private String userName;//用户名(传空字符串)
//        private int type;//提交类型 1:文本 2:图片 3:视频 4:附件
//        private String content;//文本内容（可选）
//        private String fileUrl;//文件路径（可选）
//        private String imageUrl;//图片路径（可选）
//        private String timeStr;//上传时间，格式 HH:MM:SS(传空字符串)
//        private String submitDay;//上传时间，格式 yyyy-MM-dd(传空字符串)
//        private String receiveId;//接收人Id(可选)
//        private int chatType;//聊天类型 1:群聊天 2:单人聊
        String[] str=new String[]{"scott is very smart",
        "scott is very smarter than james",
        "james is a pig",
        "富豪哥好man","xu xia is a woman"};
        for(int i=0;i<=20;i++) {
            GroupChatRecordDTO dto = new GroupChatRecordDTO();
            dto.setId(Constant.EMPTY);
            dto.setGroupId(Constant.EMPTY);
            dto.setUserName(Constant.EMPTY);
            dto.setType(Constant.ONE);
            int remain=i%5;
            String content=str[remain];
            dto.setContent(content);
            dto.setFileUrl(Constant.EMPTY);
            dto.setImageUrl(Constant.EMPTY);
            dto.setTimeStr(Constant.EMPTY);
            dto.setSubmitDay(Constant.EMPTY);
            dto.setChatType(Constant.TWO);
            if(i<=10) {
                if(i%2==0){
                    dto.setReceiveId("5a17dc7d0a9d324986663cb7");
                    dto.setUserId("5a17dc8e0a9d324986663cb9");
                }else{
                    dto.setUserId("5a17dc7d0a9d324986663cb7");
                    dto.setReceiveId("5a17dc8e0a9d324986663cb9");
                }
            }else if(i<=14){
                dto.setUserId("5a17dc7d0a9d324986663cb7");
                dto.setReceiveId("5a17dc8e0a9d324986663cb9");
            }else{
                if(i%2==1){
                    dto.setReceiveId("5a17dc7d0a9d324986663cb7");
                    dto.setUserId("5a17dc8e0a9d324986663cb9");
                }else{
                    dto.setUserId("5a17dc7d0a9d324986663cb7");
                    dto.setReceiveId("5a17dc8e0a9d324986663cb9");
                }
            }
            saveTest(dto);
        }
    }

    public static void saveTest(GroupChatRecordDTO dto){
        RecordChatPersonalDao recordChatPersonalDao = new RecordChatPersonalDao();

        GroupChatRecordDao groupChatRecordDao = new GroupChatRecordDao();

        RecordTotalChatDao recordTotalChatDao = new RecordTotalChatDao();
        groupChatRecordDao.saveGroupRecordEntry(dto.buildEntry());
        if(dto.getChatType()== Constant.TWO
                &&StringUtils.isNotEmpty(dto.getReceiveId())){
            RecordChatPersonalEntry one=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getUserId()),
                    new ObjectId(dto.getReceiveId()));
            if(null==one){
                recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                        new ObjectId(dto.getUserId()),new ObjectId(dto.getReceiveId()),
                        Constant.TWO
                ));
                RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getUserId()));
                if(null!=chatEntry){
                    recordTotalChatDao.updateEntry(new ObjectId(dto.getUserId()));
                }else{
                    recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getUserId())));
                }
            }else{
                one.setUpdateTime(System.currentTimeMillis());
                recordChatPersonalDao.saveRecordEntry(one);
            }
            RecordChatPersonalEntry two=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getReceiveId()),
                    new ObjectId(dto.getUserId()));
            if(null==two){
                recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                        new ObjectId(dto.getReceiveId()),new ObjectId(dto.getUserId()),
                        Constant.TWO
                ));
                RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getReceiveId()));
                if(null!=chatEntry){
                    recordTotalChatDao.updateEntry(new ObjectId(dto.getReceiveId()));
                }else{
                    recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getReceiveId())));
                }
            }else{
                two.setUpdateTime(System.currentTimeMillis());
                recordChatPersonalDao.saveRecordEntry(two);
            }


        }else if(dto.getChatType()==Constant.ONE&&
                StringUtils.isNotEmpty(dto.getGroupId())){
            RecordChatPersonalEntry three=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getUserId()),
                    new ObjectId(dto.getGroupId()));
            if(null==three) {
                recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                        new ObjectId(dto.getUserId()), new ObjectId(dto.getGroupId()),
                        Constant.ONE
                ));
                RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getUserId()));
                if(null!=chatEntry){
                    recordTotalChatDao.updateEntry(new ObjectId(dto.getUserId()));
                }else{
                    recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getUserId())));
                }
            }else{
                three.setUpdateTime(System.currentTimeMillis());
                recordChatPersonalDao.saveRecordEntry(three);
            }
        }
    }

    public ObjectId createGroupWithCommunity(ObjectId communityId, ObjectId owerId, String emChatId, String name,
                                             String desc, String qrUrl) throws Exception {
        ObjectId groupId = new ObjectId();
        GroupEntry group = new GroupEntry(groupId, communityId, owerId, qrUrl, emChatId, name, desc);
        groupDao.add(group);
        memberService.saveMember(owerId, groupId, 2);
        return groupId;
    }

    public ObjectId createGroupWithoutCommunity(ObjectId owerId, String emChatId) {
        ObjectId groupId = new ObjectId();
        String qrUrl = QRUtils.getGroupQrUrl(groupId);
        GroupEntry group = new GroupEntry(groupId, owerId, qrUrl, emChatId);
        groupDao.add(group);
        memberService.saveMember(owerId, groupId, 2);
        return groupId;
    }

    public Map<ObjectId,GroupEntry> getGroupEntries(List<ObjectId> ids){
        return groupDao.getGroupEntries(ids);
    }

    /**
     * 获取讨论组成员列表
     *
     * @param chatId
     * @return
     */
    public ObjectId getGroupIdByChatId(String chatId) {
        return groupDao.getGroupIdByEmchatId(chatId);
    }

    /**
     * 获取未删除的讨论组成员列表
     *
     * @param chatId
     * @return
     */
    public ObjectId getMoreGroupIdByEmchatId(String chatId) {
        return groupDao.getMoreGroupIdByEmchatId(chatId);
    }

    public GroupEntry getGroupEntryByEmchatId(String chatId){
        return groupDao.getGroupEntryByEmchatId(chatId);
    }

    /**
     * findObjectId
     *
     * @param groupId
     * @return
     */
    public GroupDTO findById(ObjectId groupId,ObjectId userId) {
        GroupEntry groupEntry = groupDao.findByObjectId(groupId);
        List<MemberDTO> managers = memberService.getManagers(groupId);
        List<MemberDTO> members = memberService.getMembers(groupId, 20,userId);
        return new GroupDTO(groupEntry, members, managers);
    }

    public boolean findByObjectId(ObjectId groupId) {
        GroupEntry groupEntry = groupDao.findByObjectId(groupId);
        if(groupEntry.getCommunityId()==null){
            return false;
        }else{
            return true;
        }
    }

    public GroupDTO findByEmChatId(String emChatId,ObjectId userId) {
        GroupEntry groupEntry = groupDao.findByEmchatId(emChatId);
        if(groupEntry == null) return null;
        ObjectId groupId = groupEntry.getID();
        List<MemberDTO> managers = memberService.getManagers(groupId);
        List<MemberDTO> members = memberService.getMembers(groupId, 20,userId);
        return new GroupDTO(groupEntry, members, managers);
    }

    /**
     * 获取群聊头像
     *
     * @param groupId
     * @return
     */
    public String getHeadImage(ObjectId groupId) {
        return groupDao.getHeadImage(groupId);
    }

    /**
     * 更新群聊头像
     *
     * @param groupId
     * @return
     * @throws IOException
     * @throws IllegalParamException
     */
    @Async
    public void asyncUpdateHeadImage(ObjectId groupId) throws IOException, IllegalParamException {
        String url = generateHeadImage(groupId);
        groupDao.updateHeadImage(groupId, url);
    }

    /**
     * 更新群聊名称-按照用户名,的方式拼接
     *
     * @param groupId
     */
    @Async
    public void asyncUpdateGroupNameByMember(ObjectId groupId) {
        String name = generateGroupName(groupId);
        groupDao.updateGroupName(groupId, name);
    }

    /**
     * 更新 - 群聊名称
     *
     * @param groupId
     * @param groupName
     */
    @Async
    public void asyncUpdateGroupName(ObjectId groupId, String groupName) {
        groupDao.updateIsM(groupId, 1);
        groupDao.updateGroupName(groupId, groupName);
    }

    private String generateHeadImage(ObjectId groupId) throws IOException, IllegalParamException {
        List<MemberDTO> members = memberService.getMembers(groupId, 4,null);
        List<String> images = new ArrayList<String>();
        for (MemberDTO memberDTO : members) {
            if (StringUtils.isNotBlank(memberDTO.getAvator())) {
                images.add(memberDTO.getAvator());
            }
        }
        BufferedImage bufferedImage = ImageUtils.getCombinationOfhead(images);
        ObjectId fileKey = new ObjectId();
        File outFile = File.createTempFile(fileKey.toString(), ".jpg");
        ImageIO.write(bufferedImage, "JPG", outFile);
        QiniuFileUtils.uploadFile(fileKey.toString() + ".jpg", new FileInputStream(outFile), QiniuFileUtils.TYPE_IMAGE);
        outFile.delete();
        return QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey.toString() + ".jpg");
    }

    public List<GroupDTO> getChildrenGroups(ObjectId userId){
        List<GroupDTO> groupDTOs = new ArrayList<GroupDTO>();
        List<ObjectId> groupIds =memberDao.getGroupIdsByUserId(userId);

        List<GroupEntry> groupEntries =groupDao.findByIdList(groupIds);
        for(GroupEntry groupEntry:groupEntries){
            groupDTOs.add(new GroupDTO(groupEntry,new ArrayList<MemberDTO>(),new ArrayList<MemberDTO>()));
        }
        return groupDTOs;
    }


    private String generateGroupName(ObjectId groupId) {
        List<MemberDTO> members = memberService.getMembers(groupId, 3,null);
        String name = "";
        for (MemberDTO member : members) {
            name += member.getNickName() + ",";
        }
        if (name.contains(",")) {
            name = name.substring(0, name.lastIndexOf(","));
        }
        return name;
    }

    /**
     * 是否 - 是群聊成员
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isGroupMember(ObjectId groupId, ObjectId userId) {
        return memberDao.isMember(groupId, userId);
    }

    /**
     * 根据群组id获取环信id
     *
     * @param groupId
     * @return
     */
    public String getEmchatIdByGroupId(ObjectId groupId) {
        return groupDao.getEmchatIdByGroupId(groupId);
    }

    /**
     * 群组转让
     *
     * @param groupId
     * @param owerId
     */
    public void transferOwerId(ObjectId groupId, ObjectId owerId) {
        groupDao.updateOwerId(groupId, owerId);
    }

    /**
     * 删除群组
     *
     * @param groupId
     */
    public void deleteGroup(ObjectId groupId) {
        groupDao.delete(groupId);
    }


    public Map<String,Object> getChildrenRelation(ObjectId userId,
                                                    int page,
                                                    int pageSize){
        Map<String,Object> result = new HashMap<String, Object>();
        Set<ObjectId> groupIds = new HashSet<ObjectId>();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        List<RecordChatRelationDTO> dtos = new ArrayList<RecordChatRelationDTO>();
        List<RecordChatPersonalEntry> entries = recordChatPersonalDao.getChatEntries(userId,page,pageSize);
        for(RecordChatPersonalEntry entry:entries){
            if(entry.getChatType()==Constant.ONE){
                groupIds.add(entry.getReceiveId());
            }else{
                userIds.add(entry.getReceiveId());
            }
        }
        Map<ObjectId,GroupEntry> groupEntryMap = groupDao.getGroupEntries(new ArrayList<ObjectId>(groupIds));
        Map<ObjectId,UserEntry> userEntryMap =userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(RecordChatPersonalEntry entry:entries){
            RecordChatRelationDTO
                    dto =new RecordChatRelationDTO();
            dto.setChatType(entry.getChatType());
            if(entry.getChatType()==Constant.ONE){
                dto.setGroupId(entry.getReceiveId().toString());
                GroupEntry groupEntry = groupEntryMap.get(entry.getReceiveId());
                if(null!=groupEntry){
                    dto.setGroupName(groupEntry.getName());
                    dto.setImageUrl(groupEntry.getHeadImage());
                }
            }else{
                dto.setUserId(entry.getReceiveId().toString());
                UserEntry userEntry= userEntryMap.get(entry.getReceiveId());
                if(null!=userEntry){
                    dto.setUserName(userEntry.getUserName());
                    dto.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                }
            }
            dto.setTimeExpression(TimeChangeUtils.getChangeTime(entry.getUpdateTime()));
            dtos.add(dto);
        }
        int count=recordChatPersonalDao.countChatEntries(userId);
        result.put("list",dtos);
        result.put("count",count);
        result.put("page",page);
        result.put("pageSize",pageSize);
        return result;
    }

    public void saveGroupChatRecord(GroupChatRecordDTO dto)throws Exception{
         if(dto.getChatType()==Constant.ONE&&StringUtils.isNotEmpty(dto.getGroupId())){
             GroupEntry groupEntry = groupDao.findByEmchatId(dto.getGroupId());
             dto.setGroupId(groupEntry.getID().toString());
         }
         groupChatRecordDao.saveGroupRecordEntry(dto.buildEntry());
         if(dto.getChatType()== Constant.TWO
                 &&StringUtils.isNotEmpty(dto.getReceiveId())){
             RecordChatPersonalEntry one=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getUserId()),
                     new ObjectId(dto.getReceiveId()));
             if(null==one){
                 recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                         new ObjectId(dto.getUserId()),new ObjectId(dto.getReceiveId()),
                         Constant.TWO
                 ));
                 RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getUserId()));
                 if(null!=chatEntry){
                     recordTotalChatDao.updateEntry(new ObjectId(dto.getUserId()));
                 }else{
                     recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getUserId())));
                 }
             }else{
                 one.setUpdateTime(System.currentTimeMillis());
                 recordChatPersonalDao.saveRecordEntry(one);
             }
             RecordChatPersonalEntry two=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getReceiveId()),
                     new ObjectId(dto.getUserId()));
             if(null==two){
                 recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                         new ObjectId(dto.getReceiveId()),new ObjectId(dto.getUserId()),
                         Constant.TWO
                 ));
                 RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getReceiveId()));
                 if(null!=chatEntry){
                     recordTotalChatDao.updateEntry(new ObjectId(dto.getReceiveId()));
                 }else{
                     recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getReceiveId())));
                 }
             }else{
                 two.setUpdateTime(System.currentTimeMillis());
                 recordChatPersonalDao.saveRecordEntry(two);
             }


         }else if(dto.getChatType()==Constant.ONE&&
                 StringUtils.isNotEmpty(dto.getGroupId())){
             RecordChatPersonalEntry three=recordChatPersonalDao.getChatEntry(new ObjectId(dto.getUserId()),
                     new ObjectId(dto.getGroupId()));
             if(null==three) {
                 recordChatPersonalDao.saveRecordEntry(new RecordChatPersonalEntry(
                         new ObjectId(dto.getUserId()), new ObjectId(dto.getGroupId()),
                         Constant.ONE
                 ));
                 RecordTotalChatEntry chatEntry = recordTotalChatDao.getEntryByUserId(new ObjectId(dto.getUserId()));
                 if(null!=chatEntry){
                     recordTotalChatDao.updateEntry(new ObjectId(dto.getUserId()));
                 }else{
                     recordTotalChatDao.saveEntry(new RecordTotalChatEntry(new ObjectId(dto.getUserId())));
                 }
             }else{
                 three.setUpdateTime(System.currentTimeMillis());
                 recordChatPersonalDao.saveRecordEntry(three);
             }
         }
    }


    public Map<String,Object> getPersonalChatRecords(ObjectId userId,
                                                     ObjectId receiveId,
                                                     int page,
                                                     int pageSize){
        Map<String,Object> result = new HashMap<String,Object>();
        List<GroupChatRecordDTO> dtos = new ArrayList<GroupChatRecordDTO>();
        UserEntry userEntry = userService.findById(userId);
        UserEntry receiveEntry = userService.findById(receiveId);

        List<GroupChatRecordEntry> recordEntries = groupChatRecordDao.getPersonalChatRecords(userId,receiveId,page,pageSize);
        for(GroupChatRecordEntry entry:recordEntries){
            GroupChatRecordDTO dto = new GroupChatRecordDTO(entry);
            dto.setOwner(false);
            if(entry.getUserId().equals(userEntry.getID())){
                dto.setOwner(true);
                dto.setUserName(StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
            }else{
                dto.setUserName(StringUtils.isNotEmpty(receiveEntry.getNickName())?receiveEntry.getNickName():receiveEntry.getUserName());
                dto.setAvatar(AvatarUtils.getAvatar(receiveEntry.getAvatar(),receiveEntry.getRole(),receiveEntry.getSex()));
            }
            dtos.add(dto);
        }
        int count=groupChatRecordDao.countPersonalChatRecords(userId,receiveId);
        result.put("list",dtos);
        result.put("count",count);
        result.put("page",page);
        result.put("pageSize",pageSize);
        return result;
    }


    public Map<String,Object> getGroupChatRecords(ObjectId groupId,
                                                        ObjectId userId,
                                                        int page,
                                                        int pageSize){
        Map<String,Object> result= new HashMap<String,Object>();
        List<GroupChatRecordDTO> dtos = new ArrayList<GroupChatRecordDTO>();
//        MemberEntry memberEntry =memberDao.getUser(groupId,userId);
//        UserEntry userEntry = userService.findById(userId);
//        String userName;
//        if(null!=memberEntry){
//            userName=memberEntry.getNickName();
//        }else{
//            userName=StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
//        }

        long time =System.currentTimeMillis();

        long startTime = groupChatRecordDao.getStartTime(userId,groupId,time);

        long endTime = groupChatRecordDao.getEndTime(userId,groupId,time);

        List<GroupChatRecordEntry> recordEntries = groupChatRecordDao.getGroupChatRecords(groupId,page,pageSize,startTime,endTime);

        Set<ObjectId> userIds = new HashSet<ObjectId>();

        for(GroupChatRecordEntry entry:recordEntries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap = userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(GroupChatRecordEntry entry:recordEntries){
            GroupChatRecordDTO dto = new GroupChatRecordDTO(entry);
//            dto.setUserName(userName);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                dto.setUserName(StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            }
            dto.setOwner(false);
            if(entry.getUserId().toString().equals(userId.toString())) {
                dto.setOwner(true);
            }
            dtos.add(dto);
        }
        int count=groupChatRecordDao.countGroupChatRecords(userId,groupId);
        result.put("list",dtos);
        result.put("count",count);
        result.put("page",page);
        result.put("pageSize",pageSize);
        return result;
    }


    public Object getAddress(){
//        File file = new File();
        File file= new File("D:\\logo\\logo_1.png");
        Object obj=emService.uploadFile(file);
        return obj;
    }

    public boolean sendTestMessage(){
        List<String> targets = new ArrayList<String>();
        targets.add("37294088192001");
        //targets.add("5a4c874e3d4df91f36167b5c");
        String userId="5a00253d3d4df9241620d173";
        Map<String, String> ext = new HashMap<String, String>();
        Map<String, String> sendMessage = new HashMap<String, String>();
        //sendMessage.put("type", MsgType.IMG);
        //sendMessage.put("url", "https://a1.easemob.com/fulan/fulanmall/chatfiles/2b3ce640-0cb7-11e8-8a92-29b46c527a8a");
        //sendMessage.put("filename","operationBook.jpg");
        //sendMessage.put("secret","KzzmSgy3EeisbBEBikKn-2bhdi55QYWQdkgC8mYR_o3-LmTX");
        sendMessage.put("type", MsgType.TEXT);
        sendMessage.put("msg", "作业通知：\n 社长 张老师 发布了一条新作业 \n 请各位家长及时查看！");
        return emService.sendTextMessage("chatrooms", targets, userId, ext, sendMessage);
    }



}
