package com.fulaan.meeting.srevice;

import com.db.meeting.MeetingDao;
import com.fulaan.calendar.service.EventService;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.meeting.dto.ChooseDTO;
import com.fulaan.meeting.dto.MeetingDTO;
import com.fulaan.meeting.dto.MessageDTO;
import com.fulaan.meeting.dto.VoteDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JsonUtil;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.app.SimpleDTO;
import com.pojo.calendar.Event;
import com.pojo.lesson.LessonWare;
import com.pojo.letter.LetterEntry;
import com.pojo.meeting.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by wang_xinxin on 2016/8/1.
 */

@Service
public class MeetingService {

    private static final Logger logger= Logger.getLogger(MeetingService.class);

    private MeetingDao meetingDao = new MeetingDao();

    private UserService userService = new UserService();

    @Autowired
    private EaseMobService easeMobService;

    private EventService eventService=new EventService();

    private LetterService letterService = new LetterService();
    /**
     * 
     * @param meetingDTO
     */
    public void AddMeetingInfo(MeetingDTO meetingDTO,ObjectId userId,ObjectId schoolId,String chatid,String username,String image,Map map) {
        int count = 0;
        if (meetingDTO.getModelType()==1) {
            count = meetingDao.checkModelNameCount(meetingDTO.getModelName(),null);
        }
        if (count==0) {
            if (StringUtils.isEmpty(meetingDTO.getId())||meetingDTO.getModelType()==1) {
                List<String> chatIds = new ArrayList<String>();
                List<String> userIds = new ArrayList<String>();
                userIds.add(userId.toString());
                if (meetingDTO.getUsers()!=null) {
                    for(int i=0;i<meetingDTO.getUsers().length;i++) {
                        if (!StringUtils.isEmpty(meetingDTO.getUsers()[i])) {
                            userIds.add(meetingDTO.getUsers()[i]);
                        }
                    }
                }
                HashSet<String> hs = new HashSet<String>(userIds);
                Iterator<String> iterator=hs.iterator();
                List<String> uids = new ArrayList<String>();
                while(iterator.hasNext()){
                    uids.add(iterator.next());
                }
                String group = "";
                List<UserDetailInfoDTO> userDetailInfos =  userService.findUserInfoByUserIds(uids);
                if (userDetailInfos!=null && userDetailInfos.size()!=0) {
                    for (UserDetailInfoDTO user : userDetailInfos) {
                        chatIds.add(user.getChatid());
                        Event e = new Event(new ObjectId(user.getId()), 2, meetingDTO.getName(), meetingDTO.getCause(),
                                DateTimeUtils.getStrToLongTime(meetingDTO.getStartTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM),
                                DateTimeUtils.getStrToLongTime(meetingDTO.getEndTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
                        eventService.addEvent(e);
                        List<ObjectId> receiverIds = new ArrayList<ObjectId>();
                        ArrayList<String> recipientIds = new ArrayList<String>();
                        recipientIds.add(user.getChatid());
                        receiverIds.add(new ObjectId(user.getId()));
                        LetterEntry letterEntry = new LetterEntry(userId,
                                meetingDTO.getStartTime()+"-"+meetingDTO.getEndTime()+"活动会务", receiverIds);
                        letterService.sendLetter(letterEntry);
                        easeMobService.sendMessage(chatid, recipientIds, meetingDTO.getStartTime()+"-"+meetingDTO.getEndTime()+"活动会务",username,image);
                        if (user.getId().equals(userId.toString())) {
                            group = user.getChatid();
                        }
                    }
                }
                Future future = easeMobService.createChatGroup(meetingDTO.getName(), "群组", group, chatIds);
                String jsonStr = "";
                String roomid = "";
                try {
                    jsonStr = (String) future.get();
                    Map<String, Object> result = JsonUtil.Json2Map(jsonStr);
                    LinkedHashMap<String,String> resultjson = (LinkedHashMap<String, String>) result.get("data");
                    roomid = resultjson.get("groupid");
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                } catch (ExecutionException e) {
                    logger.error(e.getMessage());
                }
                meetingDao.addMeetingInfo(meetingDTO.buildMeetingEntry(userId,schoolId,chatIds,roomid));
            } else {
                MeetingEntry meetingEntry = meetingDao.selMeetingDetail(new ObjectId(meetingDTO.getId()));
                List<String> chats = meetingEntry.getChatIds();
                meetingDao.updateMeetingInfo(meetingDTO.buildMeetingEntry(userId, schoolId, new ArrayList<String>(), ""), meetingDTO.getId());
            }
        }
        map.put("",count);
    }

    /**
     *
     * @param meetId
     * @return
     */
    public MeetingDTO selMeetingDetail(String meetId) {
        MeetingEntry meetingEntry = meetingDao.selMeetingDetail(new ObjectId(meetId));
        MeetingDTO meetingDTO = new MeetingDTO(meetingEntry);
        List<ObjectId> userIds = meetingEntry.getUserIds();
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(userIds);
        if (userDetailInfoDTOList!=null && userDetailInfoDTOList.size()!=0) {
            meetingDTO.setUserlist(userDetailInfoDTOList);
        }
        List<LessonWare> lessonWares = meetingEntry.getLessonWareList();
        List<SimpleDTO> coursewareList =new ArrayList<SimpleDTO>();
        if (lessonWares!=null && lessonWares.size()!=0) {
            for (LessonWare lessonWare : lessonWares) {
                SimpleDTO simpleDTO =new SimpleDTO(lessonWare);
                simpleDTO.setValue1(lessonWare.getFileType());
                coursewareList.add(simpleDTO);
            }
        }
        meetingDTO.setCoursewareList(coursewareList);
        List<IdValuePair> checkUserIds = meetingEntry.getCheckUsers();
        List<ObjectId> ulist = new ArrayList<ObjectId>();
        if (checkUserIds!=null && checkUserIds.size()!=0) {
            for (IdValuePair value : checkUserIds) {
                ulist.add(value.getId());
            }
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList2 = userService.findUserInfoByIds(ulist);
        if (userDetailInfoDTOList2!=null && userDetailInfoDTOList2.size()!=0) {
            meetingDTO.setCheckUserList(userDetailInfoDTOList2);
        }
        userIds.removeAll(ulist);
         List<UserDetailInfoDTO> userDetailInfoDTOList3 = userService.findUserInfoByIds(userIds);
        if (userDetailInfoDTOList3!=null && userDetailInfoDTOList3.size()!=0) {
            meetingDTO.setNoCheckUserList(userDetailInfoDTOList3);
        }
        List<VoteEntry> voteEntries = meetingDao.selVoteList(new ObjectId(meetId));
        List<VoteDTO> voteDTOs = new ArrayList<VoteDTO>();
        if (voteEntries!=null && voteEntries.size()!=0) {
            for(VoteEntry voteEntry : voteEntries) {
                VoteDTO voteDTO = new VoteDTO();
                voteDTO.setId(voteEntry.getID().toString());
                voteDTO.setName(voteEntry.getName());
                int num = 0;
                if (voteEntry.getChooseList()!=null && voteEntry.getChooseList().size()!=0) {
                    for (ChooseEntry choose : voteEntry.getChooseList()) {
                        num += choose.getUserIds().size();
                    }
                }
                voteDTO.setStatus(0);
                if (num==meetingEntry.getUserIds().size()) {
                    voteDTO.setStatus(1);
                }
                voteDTOs.add(voteDTO);
            }
        }
        meetingDTO.setVoteDTOList(voteDTOs);
        meetingDTO.setUserName(userService.searchUserId(meetingEntry.getUserId()).getUserName());
        List<IdValuePair> idValuePairs = meetingEntry.getApprovalUserIds();
        List<ObjectId> users = new ArrayList<ObjectId>();
        if (idValuePairs!=null &&idValuePairs.size()!=0) {
            for (IdValuePair idValue : idValuePairs) {
                users.add(idValue.getId());
            }
            meetingDTO.setSheHeUserList(userService.findUserInfoByIds(users));
        }
        List<UserDetailInfoDTO> userDetailInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        List<UserEntry> userEntryList = userService.getUserEntrysByChatids(meetingEntry.getChatIds(), new BasicDBObject("nm",1).append("chatid",1).append("avt",1));
        for (UserEntry user : userEntryList) {
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO();
            userDetailInfoDTO.setId(user.getID().toString());
            userDetailInfoDTO.setUserName(user.getUserName());
            userDetailInfoDTO.setChatid(user.getChatId());
            userDetailInfoDTO.setImgUrl(AvatarUtils.getAvatar(user.getAvatar(), 3));
            userDetailInfoDTOs.add(userDetailInfoDTO);
        }
        meetingDTO.setChatUsers(userDetailInfoDTOs);
        return meetingDTO;
    }

    /**
     *
     * @param schoolId
     * @return
     */
    public List<MeetingDTO> selModelList(ObjectId schoolId,ObjectId userId) {
        List<MeetingEntry> meetingEntryList = meetingDao.selModelList(schoolId, userId, new BasicDBObject("mnm", 1).append("modeltp", 1));
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        if (meetingEntryList!=null && meetingEntryList.size()!=0) {
            for (MeetingEntry meetingEntry : meetingEntryList) {
                MeetingDTO meetingDTO = new MeetingDTO();
                meetingDTO.setId(meetingEntry.getID().toString());
                meetingDTO.setModelName(meetingEntry.getModelName());
                meetingDTOs.add(meetingDTO);
            }
        }
        return meetingDTOs;
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<MeetingDTO> selMyMeetingList(String startTime,String endTime,int type,String keyword,ObjectId userId,int index) {
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startTime)) {
            String sdate = startTime + " " +"00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String edate = endTime + " " +"23:59";
            etime = DateTimeUtils.getStrToLongTime(edate,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        List<MeetingEntry> meetingEntryList = meetingDao.selMyMeetingList(stime,etime,type,keyword,userId,index, null);
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        if (meetingEntryList!=null && meetingEntryList.size()!=0) {
            for (MeetingEntry meetingEntry : meetingEntryList) {
                MeetingDTO meetingDTO = new MeetingDTO(meetingEntry);
                meetingDTO.setUserName(userService.searchUserId(meetingEntry.getUserId()).getUserName());
                meetingDTOs.add(meetingDTO);
            }
        }
        return meetingDTOs;

    }

    /**
     *
     * @param id
     */
    public void delMeetingInfo(ObjectId id) {
        meetingDao.delMeetingInfo(id);
    }

    /**
     *
     * @param modelName
     * @param id
     */
    public void updateMeetModel(String modelName, ObjectId id,Map map) {
        int count = meetingDao.checkModelNameCount(modelName,id);
        if (count==0) {
            meetingDao.updateMeetModel(id,modelName);
        }
        map.put("count",count);


    }

    /**
     *
     * @param userId
     * @param id
     */
    public void checkInMeeting(ObjectId userId, ObjectId id) {
       meetingDao.checkInMeeting(id,userId);
    }

    /**
     *
     * @param id
     * @param theme
     * @param answers
     */
    public void addVoteInfo(ObjectId id,ObjectId userId, String theme, String[] answers) {
        List<ChooseEntry> chooseEntryList = new ArrayList<ChooseEntry>();
        if (answers!=null && answers.length!=0) {
            for (int i=0;i<answers.length;i++) {
                if (!StringUtils.isEmpty(answers[i])) {
                    chooseEntryList.add(new ChooseEntry(answers[i],new ArrayList<ObjectId>()));
                }
            }
        }
        meetingDao.addVoteIfo(new VoteEntry(id,userId,System.currentTimeMillis(),theme,chooseEntryList));
    }

    /**
     *
     * @param id
     */
    public void removeVoteInfo(ObjectId id) {
        meetingDao.removeVoteInfo(id);
    }

    public void selVoteUserList(ObjectId id, String chooseId, Map map) {
        VoteEntry  voteEntry = meetingDao.selVoteInfo(id);
        List<ChooseEntry> chooseEntryList = voteEntry.getChooseList();
        if (chooseEntryList!=null && chooseEntryList.size()!=0) {
            for (ChooseEntry chooseEntry : chooseEntryList) {
                if (chooseEntry.getId().equals(new ObjectId(chooseId))) {
                    map.put("answer",chooseEntry.getAnswer());
                    if (chooseEntry.getUserIds()!=null && chooseEntry.getUserIds().size()!=0) {
                        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(chooseEntry.getUserIds());
                        map.put("users",userDetailInfoDTOList);
                    }

                }
            }
        }
    }

    /**
     *
     * @param id
     * @param userId
     * @param map
     */
    public void selVoteDetail(ObjectId id, ObjectId userId, Map map) {
        VoteEntry  voteEntry = meetingDao.selVoteInfo(id);
        List<ChooseEntry> chooseEntryList = voteEntry.getChooseList();
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(voteEntry.getID().toString());
        voteDTO.setName(voteEntry.getName());
        List<ChooseDTO> chooseDTOs = new ArrayList<ChooseDTO>();
        boolean flag = false;
        int num = 0;
        String chooseId = "";
        if (chooseEntryList!=null && chooseEntryList.size()!=0) {
            for (ChooseEntry chooseEntry : chooseEntryList) {
                ChooseDTO chooseDTO = new ChooseDTO();
                if (chooseEntry.getUserIds().contains(userId)) {
                    flag = true;
                    chooseId = chooseEntry.getId().toString();
                    chooseDTO.setChooseId(chooseId);
                }
                chooseDTO.setName(chooseEntry.getAnswer());
                chooseDTO.setId(chooseEntry.getId().toString());
                chooseDTO.setCount(chooseEntry.getUserIds().size());
                num += chooseEntry.getUserIds().size();
                chooseDTOs.add(chooseDTO);
            }
        }
        voteDTO.setChoose(chooseId);
        voteDTO.setChooseDTOList(chooseDTOs);
        voteDTO.setCount(num);
        voteDTO.setUserName(userService.searchUserId(voteEntry.getUserId()).getUserName());
        voteDTO.setTime(DateTimeUtils.getLongToStrTimeThree(voteEntry.getTime()));
        voteDTO.setFlag(flag);
        map.put("rows",voteDTO);
    }

    /**
     *
     * @param voteId
     * @param chooseId
     * @param userId
     */
    public void updateVoteInfo(String voteId, String chooseId, ObjectId userId) {
        meetingDao.updateVoteInfo(new ObjectId(voteId),new ObjectId(chooseId),userId);
    }

    /**
     *
     * @param meetId
     * @param flag
     * @param userId
     * @param appUserId
     * @param remark
     */
    public void submitShenHe(String meetId, int flag, ObjectId userId, String appUserId, String remark) {
        meetingDao.submitShenHe(new ObjectId(meetId),userId,appUserId,remark,flag);
    }

    /**
     *
     * @param id
     * @param lessonWare
     */
    public void updateMeetFile(ObjectId id, LessonWare lessonWare) {
        meetingDao.updateMeetFile(id,lessonWare);
    }

    /**
     *
     * @param userId
     * @param id
     */
    public void checkOutMeeting(ObjectId userId, ObjectId id) {
        meetingDao.checkOutMeeting(id, userId);
    }

    /**
     *
     * @param meetId
     * @param userId
     */
    public void endMeetingInfo(String meetId, ObjectId userId,ObjectId schoolId) {
        MeetingEntry meet = meetingDao.selMeetingDetail(new ObjectId(meetId));
        meetingDao.updateMeetingStatus(new ObjectId(meetId));
        meetingDao.endMeetingInfo(new MeetLogEntry(userId,schoolId,new ObjectId(meetId),meet.getName(),meet.getIssue(),meet.getOpenTime(),meet.getEndTime()));
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param keyword
     * @param userId
     * @return
     */
    public List<MeetingDTO> selMeetLogList(String startTime, String endTime, String keyword, ObjectId userId) {
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startTime)) {
            String sdate = startTime + " " +"00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String edate = endTime + " " +"23:59";
            etime = DateTimeUtils.getStrToLongTime(edate,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        List<MeetLogEntry> meetingEntryList = meetingDao.selMeetLogList(stime, etime, keyword, userId, null);
        List<MeetingDTO> meetingDTOs = new ArrayList<MeetingDTO>();
        if (meetingEntryList!=null && meetingEntryList.size()!=0) {
            for (MeetLogEntry meetingEntry : meetingEntryList) {
                MeetingDTO meetingDTO = new MeetingDTO();
                meetingDTO.setName(meetingEntry.getName());
                meetingDTO.setId(meetingEntry.getID().toString());
                meetingDTO.setMeetId(meetingEntry.getMeetId().toString());
                meetingDTO.setIssue(meetingEntry.getIssue());
                meetingDTO.setUserName(userService.searchUserId(meetingEntry.getUserId()).getUserName());
                meetingDTO.setTime(DateTimeUtils.getLongToStrTimeFive(meetingEntry.getOpenTime())+"-"+DateTimeUtils.getLongToStrTimeSIX(meetingEntry.getEndTime()));
                meetingDTO.setUserName(userService.searchUserId(meetingEntry.getUserId()).getUserName());
                meetingDTOs.add(meetingDTO);
            }
        }
        return meetingDTOs;
    }

    /**
     *
     * @param id
     */
    public void delMeetLog(String id) {
        meetingDao.delMeetLog(new ObjectId(id));
    }

    /**
     *
     * @param meetId
     * @param content
     */
    public void addMessage(String meetId, String content,ObjectId userId,ObjectId schoolId) {
        meetingDao.addMessage(new MessageEntry(new ObjectId(meetId),schoolId,userId,System.currentTimeMillis(),content));
    }

    /**
     *
     * @param meetId
     * @return
     */
    public List<MessageDTO> selChatLogList(ObjectId meetId) {
        List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
        List<MessageEntry> messageEntryList = meetingDao.selChatLogList(meetId);
        if (messageEntryList!=null && messageEntryList.size()!=0) {
            MeetingEntry meetingEntry = meetingDao.selMeetingDetail(meetId);
            Map<ObjectId,UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
            List<UserEntry> userEntryList = userService.getUserEntrysByChatids(meetingEntry.getChatIds(), new BasicDBObject("nm",1).append("chatid",1).append("avt",1));
            for (UserEntry user : userEntryList) {
                userEntryMap.put(user.getID(),user);
            }
            for (MessageEntry message : messageEntryList) {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setContent(message.getContent());
                messageDTO.setUserName(userEntryMap.get(message.getUserId()).getUserName());
                messageDTO.setTime(DateTimeUtils.getLongToStrTimeThree(message.getTime()));
                messageDTOs.add(messageDTO);
            }
        }
        return messageDTOs;
    }

    /**
     *
     * @param meetId
     * @param userId
     * @return
     */
    public boolean checkMeetingStatus(String meetId, ObjectId userId) {
        MeetingEntry meet = meetingDao.selMeetingDetail(new ObjectId(meetId));
        List<IdValuePair> valuePairs = meet.getCheckOutUsers();
        boolean flag = true;
        if (valuePairs!=null && valuePairs.size()!=0) {
            for (IdValuePair idValuePair : valuePairs) {
                if (userId.equals(idValuePair.getId())) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     *
     * @param meetId
     * @return
     */
    public boolean checkMeetingTime(String meetId) {
        MeetingEntry meetingEntry = meetingDao.selMeetingDetail(new ObjectId(meetId));
        if (meetingEntry.getStatus()==4) {
            return false;
        }
        if (System.currentTimeMillis()>meetingEntry.getEndTime()) {
            return false;
        }
        return true;
    }

    /**
     * 添加议题
     * @param meetId
     * @param content
     */
    public void addIssueList(String meetId, String content) {
        meetingDao.addIssueList(new ObjectId(meetId),content);
    }

    /**
     * 删除议题
     * @param issueId
     */
    public void delIssueInfo(String meetId,String issueId) {
        meetingDao.delIssueInfo(new ObjectId(meetId),new ObjectId(issueId));
    }
}
