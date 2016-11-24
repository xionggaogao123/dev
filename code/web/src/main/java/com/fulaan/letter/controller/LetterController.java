package com.fulaan.letter.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.app.SessionValue;
import com.pojo.letter.LetterDTO;
import com.pojo.letter.LetterEntry;
import com.pojo.letter.LetterRecordEntry;
import com.pojo.letter.LetterRecordEntry.LetterRecordState;
import com.pojo.letter.LetterState;
import com.pojo.letter.LetterType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

/**
 * 信件controller
 *
 * @author fourer
 */
@Controller
@RequestMapping("/letter")
public class LetterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LetterController.class);

    private LetterService letterService = new LetterService();

    private UserService userService = new UserService();

    @Autowired
    private EaseMobService easeMobService;

    /**
     * 未读信件数量
     *
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public int getUnreadLetterCount() {
    	ObjectId uid=getUserId();
    	//String cacheKey =CacheHandler.getKeyString(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));
    	//String value=CacheHandler.getStringValue(cacheKey);
//    	if(StringUtils.isNotBlank(value))
//    	{
//    		return Integer.valueOf(value);
//    	}
        int count = letterService.getUnReadLetterCount(uid);
        //CacheHandler.cache(cacheKey, String.valueOf(count), Constant.SESSION_FIVE_MINUTE);
        return count;
    }

    /**
     * 获取列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> listLetters(Integer page, Integer size) throws Exception {
    	//ObjectId uid=getUserId();
    	//String cacheKey =CacheHandler.getKeyString(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));
    	//CacheHandler.cache(cacheKey, "0", Constant.SECONDS_IN_HOUR);
    	
        Map<String, Object> result = new HashMap<String, Object>();
        if (size == null) {
            size = 10;
        }

        String userId = getSessionValue().getId();
        // 传进来的page是从1开始的
        List<LetterRecordEntry> letterRecordEntryList = letterService.getLetterRecordList(userId, page - 1, size);


        //所有相关的信件的id
        Set<ObjectId> letterids = new HashSet<ObjectId>();
        //与当前用户关联的信件的所有用户id
        Set<ObjectId> userIdSet = new HashSet<ObjectId>();


        for (LetterRecordEntry e : letterRecordEntryList) {
            if(e.getUserId()!=null) {
                //如果当前用户是user
                if (e.getUserId().equals(getUserId())) {
                    userIdSet.add(e.getLetterUserId()); //添加对方id到待查id
                    letterids.add(e.getUserState().getId());//获取user的letterid
                } else {
                    userIdSet.add(e.getUserId());//添加id到待查id
                    letterids.add(e.getLetterUserState().getId());//获取对方的letterid
                }
            }
        }
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(userIdSet, Constant.FIELDS);
        Map<ObjectId, LetterEntry> letterEntryMap = letterService.getLetterEntryMap(letterids, Constant.FIELDS);

        List<LetterDTO> letterDTOList = new ArrayList<LetterDTO>();

        for (LetterRecordEntry e : letterRecordEntryList) {

            UserEntry userEntry;
            ObjectId ui;
            LetterRecordState letterRecordState;
            LetterEntry letterEntry;
            if(e.getUserId()!=null) {
                //如果是user
                if (e.getUserId().equals(getUserId())) {
                    ui = e.getLetterUserId();
                    letterRecordState = e.getUserState();
                } else {
                    ui = e.getUserId();
                    letterRecordState = e.getLetterUserState();
                }
                userEntry = userMap.get(ui);
                if (null == userEntry) {
                    logger.error("Can not find user;the id=" + ui);
                    continue;
                }

                letterEntry = letterEntryMap.get(letterRecordState.getId());
                if (letterEntry != null) {
                    LetterDTO letterDTO = new LetterDTO();
                    letterDTO.setLetterId(letterRecordState.getId().toString());
                    UserDetailInfoDTO userdto = userService.getUserInfoById(ui.toString());
                    letterDTO.setSenderchatid(userdto.getChatid());
                    letterDTO.setSenderId(ui.toString());
                    UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(userEntry);
                    letterDTO.setSenderName(userDetailInfoDTO.getNickName());
                    letterDTO.setUserImage(userDetailInfoDTO.getImgUrl());
                    letterDTO.setUnread(letterRecordState.getUnRead());

                    letterDTO.setSendingTime(new Date(letterEntry.getID().getTime()));
                    letterDTO.setLetterType(LetterType.getLetterType(letterEntry.getType()));
                    letterDTO.setContent(letterEntry.getContent());
                    letterDTOList.add(letterDTO);
                }
            }
        }

        result.put("rows", letterDTOList);


        //total ,page, pageSize
        result.put("total", letterService.countLatestLetterList(userId));
        result.put("page", page);
        result.put("pageSize", 10);


        return result;
    }

    /**
     * 得到与某个好友的所有对话
     *
     * @param replyId 好友的id
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping("/getreplyletters")
    @ResponseBody
    public Map<String, Object> getReplyLetters(String replyId,
                                               Integer page, Integer size) throws Exception {
        if (page == null) {
            page = 1;
        }
        page = page - 1;
        size = 10;
        Map<String, Object> result = new HashMap<String, Object>();


        List<LetterEntry> letterEntryList = letterService.getLetterListByPeerUserId(
                getSessionValue().getId(), replyId, page, size
        );

        List<LetterDTO> letterDTOList = new ArrayList<LetterDTO>();
        UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(replyId.toString());

        SessionValue userSessionValue = getSessionValue();


        //更新未读邮件为已读
        ObjectId uid=getUserId();
    	CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));

        for (LetterEntry letterEntry : letterEntryList) {
            LetterDTO letterDTO = new LetterDTO();
            letterDTO.setLetterId(letterEntry.getID().toString());
            letterDTO.setSenderId(letterEntry.getSenderId().toString());
            UserDetailInfoDTO userdto = userService.getUserInfoById(letterEntry.getSenderId().toString());
            letterDTO.setSenderchatid(userdto.getChatid());
            letterDTO.setContent(letterEntry.getContent());
            letterDTO.setSendingTime(new Date(letterEntry.getID().getTime()));
            letterDTO.setLetterState(LetterState.LETTER_READED);
            letterDTO.setLetterType(LetterType.COMMON_LETTER);
            if (letterEntry.getSenderId().toString().equals(userSessionValue.getId())) {

                letterDTO.setSenderName(userSessionValue.getRealName());
                letterDTO.setRecipient(replyId);
                letterDTO.setRecipientName(userDetailInfoDTO.getNickName());
                letterDTO.setUserImage(userSessionValue.getMaxAvatar());

            } else {

                letterService.readLetter(getUserId(), letterEntry);

                letterDTO.setSenderName(userDetailInfoDTO.getNickName());
                letterDTO.setRecipient(userSessionValue.getId());
                letterDTO.setRecipientName(userSessionValue.getRealName());
                letterDTO.setUserImage(userDetailInfoDTO.getImgUrl());

            }
            letterDTOList.add(letterDTO);


        }

        int totalLetter = letterService.countLetterListByPeerUserId(userSessionValue.getId(),
                replyId);
        result.put("total", totalLetter);
        result.put("page", page + 1);
        result.put("pageSize", size);
        result.put("rows", letterDTOList);
        result.put("recipient", userDetailInfoDTO.getNickName());


        return result;
    }


    /**
     * 回复私信
     *
     * @param recipient
     * @param message
     * @return
     */
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> replyLetter(String[] recipient, String message) {

        Map<String, Object> result = new HashMap<String, Object>();


        List<ObjectId> receiverIds = new ArrayList<ObjectId>();

        for (String aRecipient : recipient) {

            receiverIds.add(new ObjectId(aRecipient));
        }

        LetterEntry letterEntry = new LetterEntry(getUserId(),
                message, receiverIds);
        letterService.sendLetter(letterEntry);

        return result;
    }

    /**
     * 给其他人发私信
     *
     * @param recipient
     * @param message
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> addLetter(String recipient, String message,HttpServletRequest request) {
        //HttpSession session = request.getSession();

        String[] recvNames = recipient.split(";");

        List<String> recvList = new ArrayList<String>();
        for(String recvS:recvNames){
            recvList.add(recvS);
        }
        String clientType = request.getHeader("user-agent");
        boolean needSendToEaseMob = !clientType.contains("iOS") && !clientType.contains("Android");

        //
        List<ObjectId> recipientIdList=userService.getUserIdsByName(new ArrayList<String>(recvList));

        ArrayList<String> recipientIds = new ArrayList<String>();

        Map<String, Object> result = new HashMap<String, Object>();


        List<ObjectId> receiverIds = new ArrayList<ObjectId>();

        
        Map<ObjectId,UserEntry> userMap =userService.getUserEntryMap(recipientIdList, new BasicDBObject("chatid",1));
        
        for (ObjectId aRecipient : recipientIdList) {
            receiverIds.add(aRecipient);
            UserEntry user =userMap.get(aRecipient);
            if(null!=user)
            {
               recipientIds.add(String.valueOf(user.getChatId()));
            }
        }

        LetterEntry letterEntry = new LetterEntry(getUserId(),
                message, receiverIds);
        letterService.sendLetter(letterEntry);

        if (needSendToEaseMob) {//发送消息到环信
            easeMobService.sendMessage(getSessionValue().getChatid().toString(), recipientIds, message,getSessionValue().getUserName(),getSessionValue().getMaxAvatar());
        }


        return result;
    }


    /**
     *  清空当前用户所有私信 ,todo fourer 高效的删除某人的所有私信
     *
     * @return
     */
    @RequestMapping("/clear")
    @ResponseBody
    public Map<String, Object> clear() {
    	
        ObjectId uid=getUserId();
      	CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));
        Map<String, Object> result = new HashMap<String, Object>();

        return result;
    }

    /**
     * 接收者删除
     *
     * @param letterId
     * @return
     */
    @RequestMapping("/deletereply")
    @ResponseBody
    public Map<String, Object> deletereply(String letterId, String pairId) throws Exception {


    	
        ObjectId uid=getUserId();
      	CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));
      	
      	
        //letterService.d
        letterService.deleteLetter(getSessionValue().getId(), pairId, letterId);


        Map<String, Object> result = new HashMap<String, Object>();

        result.put("status", "ok");

        return result;
    }

    /**
     * 删除与一个用户的所有对话
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteallreply")
    @ResponseBody
    public Map<String, Object> deleteAllreply(String replyId) throws Exception {

    	
        ObjectId uid=getUserId();
      	CacheHandler.deleteKey(CacheHandler.CACHE_USER_LETTER_COUNT, String.valueOf(uid));

        letterService.deleteAllReply(getSessionValue().getId(), replyId);


        Map<String, Object> result = new HashMap<String, Object>();

        result.put("status", "ok");

        return result;
    }


}
