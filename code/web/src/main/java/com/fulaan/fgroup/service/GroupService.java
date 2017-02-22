package com.fulaan.fgroup.service;

import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.service.MemberService;
import com.fulaan.util.ImageUtils;
import com.fulaan.util.QRUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.GroupEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.QiniuFileUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/11/1.
 */
@Service
public class GroupService {

    @Autowired
    private MemberService memberService;

    private GroupDao groupDao = new GroupDao();
    private MemberDao memberDao = new MemberDao();

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

}
