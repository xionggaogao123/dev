package com.fulaan.forum.service;

import com.db.forum.FInformationDao;
import com.db.forum.FPostDao;
import com.db.forum.FReplyDao;
import com.db.user.UserDao;
import com.fulaan.dto.MessageDTO;
import com.pojo.forum.*;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/6.
 */
@Service
public class FInformationService {

    private FInformationDao fInformationDao = new FInformationDao();

    private UserDao userDao = new UserDao();

    private FPostDao fPostDao = new FPostDao();

    private FReplyDao fReplyDao = new FReplyDao();

    /**
     * 删除消息
     */
    public void removeFInformation(ObjectId id) {
        fInformationDao.removeFInformation(id);
    }

    /**
     * 删除消息
     */
    public void remove(ObjectId userId, ObjectId personId) {
        fInformationDao.remove(userId, personId);
    }

    /**
     * 更新消息浏览
     *
     * @param userId
     * @param personId
     */
    public void updateScan(ObjectId userId, ObjectId personId, int type) {
        fInformationDao.updateScan(userId, personId, type);
    }

    /**
     * 新增消息
     *
     * @param userId
     * @param personId
     * @param type
     * @param content
     */
    public void addFInformation(ObjectId userId, String personId, int type, String content, int scan) {
        FInformationEntry fInformationEntry = new FInformationEntry(userId, new ObjectId(personId), type, System.currentTimeMillis(), content, scan);
        fInformationDao.addFInformation(fInformationEntry);
    }

    /**
     * 获取消息
     *
     * @param userId
     * @param type
     * @return
     */
    public List<FInformationDTO> getInformationFirst(String personId, ObjectId userId, int type) {

        List<FInformationEntry> fCollectionEntries = fInformationDao.getInformationFirst(userId, type);
        List<FInformationDTO> fInformationDTOList = new ArrayList<FInformationDTO>();
        if (null != fCollectionEntries) {
            for (FInformationEntry fInformationEntry : fCollectionEntries) {
                FInformationDTO fInformationDTO = new FInformationDTO(fInformationEntry);
                //判断是否在自己的维度
                if (fInformationEntry.getUserId().equals(new ObjectId(personId))) {
                    UserEntry userEntry = userDao.getUserEntry(fInformationEntry.getPersonId(), Constant.FIELDS);
                    if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
                        fInformationDTO.setAcceptName(userEntry.getNickName());
                    } else {
                        fInformationDTO.setAcceptName(userEntry.getUserName());
                    }
                    int count = fInformationDao.getFInformationCount(fInformationEntry.getPersonId(), fInformationEntry.getUserId());
                    fInformationDTO.setCount(count);
                    fInformationDTO.setAcceptType(1);
                } else {
                    int count = fInformationDao.getFInformationCount(fInformationEntry.getUserId(), fInformationEntry.getPersonId());
                    fInformationDTO.setAcceptName("您");
                    fInformationDTO.setCount(count);
                    fInformationDTO.setAcceptType(2);
                }

                UserEntry userEntry = userDao.getUserEntry(fInformationEntry.getUserId(), Constant.FIELDS);
                fInformationDTO.setImageSrc(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                long time = fInformationEntry.getTime();
                long nowTime = System.currentTimeMillis();
                long day = (nowTime - time) / (1000 * 60 * 60 * 24);
                fInformationDTO.setTime(day);
                if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
                    fInformationDTO.setNickName(userEntry.getNickName());
                } else {
                    fInformationDTO.setNickName(userEntry.getUserName());
                }
                fInformationDTOList.add(fInformationDTO);
            }
        }
        return fInformationDTOList;
    }

    public void saveReplyInfo(ObjectId uid, ObjectId psid, ObjectId ptid, ObjectId ryid) {
        fInformationDao.saveReplyInformation(uid, psid, ptid, ryid);
    }

    //获取未读消息
    public List<MessageDTO> getInformationUnRead(ObjectId userId) {

        List<FInformationEntry> fCollectionEntries = fInformationDao.getInformationUnRead(userId);
        List<MessageDTO> list = new ArrayList<MessageDTO>();
        for (FInformationEntry entry : fCollectionEntries) {
            if (entry.getType() == 2) { //点赞
                MessageDTO message = new MessageDTO();
                message.setId(entry.getID());
                message.setTime(entry.getTime());
                message.setType(entry.getType());
                FPostEntry postEntry = fPostDao.getFPostEntry(entry.getPostId());
                UserEntry user = userDao.getUserEntry(entry.getUserId(), Constant.FIELDS);
                if (user != null) {
                    message.setUser(new UserInfoDTO(user));
                }
                if (postEntry != null) {
                    message.setPost(new FPostDTO(postEntry));
                    list.add(message);
                }
            } else if (entry.getType() == 3) {
                MessageDTO message = new MessageDTO();
                message.setId(entry.getID());
                message.setTime(entry.getTime());
                message.setType(entry.getType());
                FPostEntry postEntry = fPostDao.getFPostEntry(entry.getPostId());
                UserEntry user = userDao.getUserEntry(entry.getUserId(), Constant.FIELDS);
                if (user != null) {
                    message.setUser(new UserInfoDTO(user));
                }
                FReplyEntry replyEntry = fReplyDao.getFReplyEntry(entry.getRyplyId());
                message.setReply(new FReplyDTO(replyEntry));
                if (postEntry != null) {
                    message.setPost(new FPostDTO(postEntry));
                    list.add(message);
                }
            }
        }
        return list;
    }

    public void clearReadedMessage(ObjectId userId) {
        fInformationDao.clearReadedMessage(userId);
    }

    /**
     * 获取消息
     **/
    public List<FInformationDTO> getInformation(int acceptType, ObjectId userId, ObjectId personId, int type) {

        List<FInformationEntry> fCollectionEntries = fInformationDao.getMessages(userId, personId, type);
        List<FInformationDTO> fInformationDTOList = new ArrayList<FInformationDTO>();
        if (null != fCollectionEntries) {
            for (FInformationEntry fInformationEntry : fCollectionEntries) {
                FInformationDTO fInformationDTO = new FInformationDTO(fInformationEntry);
                if (acceptType == 1) {
                    UserEntry userEntry = userDao.getUserEntry(fInformationEntry.getPersonId(), Constant.FIELDS);
                    if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
                        fInformationDTO.setAcceptName(userEntry.getNickName());
                    } else {
                        fInformationDTO.setAcceptName(userEntry.getUserName());
                    }
                    fInformationDTO.setAcceptType(1);
                } else {
                    fInformationDTO.setAcceptType(2);
                }
                UserEntry userEntry = userDao.getUserEntry(fInformationEntry.getUserId(), Constant.FIELDS);
                fInformationDTO.setImageSrc(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                long time = fInformationEntry.getTime();
                long nowTime = System.currentTimeMillis();
                long day = (nowTime - time) / (1000 * 60 * 60 * 24);
                if (null != userEntry.getNickName() && !"".equals(userEntry.getNickName())) {
                    fInformationDTO.setNickName(userEntry.getNickName());
                } else {
                    fInformationDTO.setNickName(userEntry.getUserName());
                }
                fInformationDTO.setTime(day);
                fInformationDTOList.add(fInformationDTO);
            }
        }
        return fInformationDTOList;
    }

    @Async
    public void sendSystemMessageToAllUser(String content) {
        List<ObjectId> users = userDao.getAllUserId();
        fInformationDao.sendSystemMessage(users, content);
    }

    public void sendSystemMessage(ObjectId uid, String content) {
        fInformationDao.sendSystemMessage(uid, content);
    }

    /**
     * 获取系统消息
     *
     * @param personId
     * @return
     */
    public List<FInformationDTO> getSystemInf(ObjectId personId) {
        List<FInformationEntry> fCollectionEntries = fInformationDao.getSystemInformation(personId);
        List<FInformationDTO> fInformationDTOList = new ArrayList<FInformationDTO>();
        if (null != fCollectionEntries) {
            for (FInformationEntry fInformationEntry : fCollectionEntries) {
                FInformationDTO fInformationDTO = FInformationDTO.systemMessage(fInformationEntry);
                UserEntry userEntry1 = userDao.getUserEntry(fInformationEntry.getPersonId(), Constant.FIELDS);
                long time = fInformationEntry.getTime();
                long nowTime = System.currentTimeMillis();
                long day = (nowTime - time) / (1000 * 60 * 60 * 24);
                if (null != userEntry1.getNickName() && !"".equals(userEntry1.getNickName())) {
                    fInformationDTO.setNickName(userEntry1.getNickName());
                } else {
                    fInformationDTO.setNickName(userEntry1.getUserName());
                }
                fInformationDTO.setTimeText(DateTimeUtils.convert(fInformationEntry.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
                fInformationDTO.setTime(day);
                fInformationDTOList.add(fInformationDTO);
            }
        }
        fInformationDao.clearReadedSysMessage(personId);
        return fInformationDTOList;
    }

    public int getUnReadSystemMsgCount(ObjectId personId) {
        return fInformationDao.getUnreadSystemCount(personId);
    }

    /**
     * 收到消息请求的数量
     **/
    public int getCount(ObjectId personId, int type, int scan) {
        return fInformationDao.getCount(personId, type, scan);
    }

}
