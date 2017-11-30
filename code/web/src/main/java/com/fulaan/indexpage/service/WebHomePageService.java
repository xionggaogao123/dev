package com.fulaan.indexpage.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.reportCard.GroupExamDetailDao;
import com.db.reportCard.GroupExamUserRecordDao;
import com.db.reportCard.RecordLevelEvaluateDao;
import com.db.reportCard.RecordScoreEvaluateDao;
import com.db.user.UserDao;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.indexpage.dto.WebHomePageDTO;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.reportCard.*;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/11/17.
 */
@Service
public class WebHomePageService {

    private WebHomePageDao webHomePageDao = new WebHomePageDao();

    private MemberDao memberDao = new MemberDao();

    private CommunityDao communityDao = new CommunityDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private AppCommentDao appCommentDao = new AppCommentDao();

    private UserDao userDao = new UserDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private ExamTypeDao examTypeDao = new ExamTypeDao();

    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();

    private RecordLevelEvaluateDao recordLevelEvaluateDao = new RecordLevelEvaluateDao();

    private RecordScoreEvaluateDao recordScoreEvaluateDao = new RecordScoreEvaluateDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();


    public Map<String, Long> setTime(int mode, String sTime, String eTime) throws Exception {
        Map<String, Long> retMap = new HashMap<String, Long>();
        long startTime = 0L;
        long endTime = 0L;
        if (mode == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime().getTime();
            endTime = System.currentTimeMillis();
        } else if (mode == 1) {
            endTime = System.currentTimeMillis();
            startTime = endTime - (60L * 60L * 1000L * 24L * 7L);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotEmpty(sTime)) {
                startTime = format.parse(sTime).getTime();
            }
            if (StringUtils.isNotEmpty(eTime)) {
                endTime = format.parse(eTime).getTime();
            }
            if (startTime > endTime) {
                throw new Exception("传入的时间结束时间不能小于开始时间");
            }
        }
        retMap.put("startTime", startTime);
        retMap.put("endTime", endTime);
        return retMap;
    }


    public Map<String, Object> getGatherEntries(ObjectId userId, int type, String subjectId, String communityId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<WebHomePageDTO> webHomePageDTOs = new ArrayList<WebHomePageDTO>();
        List<ObjectId> receiveIds = new ArrayList<ObjectId>();
        if (type == Constant.NEGATIVE_ONE || type == Constant.THREE) {
            List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
            for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
                receiveIds.add(bindEntry.getUserId());
            }
        }
        ObjectId sId = ObjectId.isValid(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId cId = ObjectId.isValid(communityId) ? new ObjectId(communityId) : null;
        List<WebHomePageEntry> entries = webHomePageDao.getGatherHomePageEntries(cId, receiveIds,
                type, sId, userId, page, pageSize
        );
        int count = webHomePageDao.countGatherHomePageEntries(cId, receiveIds,
                type, sId, userId);
        getDtosByEntries(webHomePageDTOs, entries);
        result.put("list", webHomePageDTOs);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * @param userId
     * @param type
     * @param subjectId
     * @param mode      0:今天 1:一周内 2:时间参数
     * @param status
     * @param sTime
     * @param eTime
     * @param page
     * @param pageSize
     * @throws Exception
     */
    public Map<String, Object> getMyReceivedEntries(ObjectId userId,
                                                    int type,
                                                    String subjectId,
                                                    int mode,
                                                    int status,
                                                    String sTime,
                                                    String eTime,
                                                    String communityId,
                                                    int page,
                                                    int pageSize) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<WebHomePageDTO> webHomePageDTOs = new ArrayList<WebHomePageDTO>();
        Map<String, Long> retMap = setTime(mode, sTime, eTime);
        long startTime = retMap.get("startTime");
        long endTime = retMap.get("endTime");
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        if (ObjectId.isValid(communityId)) {
            List<ObjectId> groupIds = memberDao.getGroupIdsByUserId(userId);
            List<CommunityEntry> communityEntries = communityDao.getCommunityEntriesByGroupIds(groupIds);
            for (CommunityEntry communityEntry : communityEntries) {
                communityIds.add(communityEntry.getID());
            }
        } else {
            communityIds.add(new ObjectId(communityId));
        }
        List<ObjectId> receiveIds = new ArrayList<ObjectId>();
        if (type == Constant.NEGATIVE_ONE || type == Constant.THREE) {
            List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
            for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
                receiveIds.add(bindEntry.getUserId());
            }
        }
        ObjectId sId = ObjectId.isValid(subjectId) ? new ObjectId(subjectId) : null;

        List<WebHomePageEntry> entries = webHomePageDao.getMyReceivedHomePageEntries(communityIds, receiveIds,
                type, sId, startTime, endTime, status, userId, page, pageSize
        );
        int count = webHomePageDao.countMyReceivedHomePageEntries(communityIds, receiveIds,
                type, sId, startTime, endTime, status, userId);
        getDtosByEntries(webHomePageDTOs, entries);
        result.put("list", webHomePageDTOs);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    public void getDtosByEntries(List<WebHomePageDTO> webHomePageDTOs, List<WebHomePageEntry> entries) {
        List<ObjectId> workIds = new ArrayList<ObjectId>();
        List<ObjectId> noticeIds = new ArrayList<ObjectId>();
        List<ObjectId> reportCardIds = new ArrayList<ObjectId>();
        List<ObjectId> reportCardSendIds = new ArrayList<ObjectId>();
        Map<ObjectId,Integer> workStatus=new HashMap<ObjectId, Integer>();
        Map<ObjectId,Integer> noticeStatus=new HashMap<ObjectId, Integer>();
        Map<ObjectId,Integer> reportCardStatus=new HashMap<ObjectId, Integer>();
        Map<ObjectId,Integer> reportCardSendStatus=new HashMap<ObjectId, Integer>();
        for (WebHomePageEntry webHomePageEntry : entries) {
            if (webHomePageEntry.getType() == Constant.ONE) {
                workIds.add(webHomePageEntry.getContactId());
                workStatus.put(webHomePageEntry.getContactId(),webHomePageEntry.getStatus());
            } else if (webHomePageEntry.getType() == Constant.TWO||
                    webHomePageEntry.getType() == Constant.FOUR) {
                noticeIds.add(webHomePageEntry.getContactId());
                noticeStatus.put(webHomePageEntry.getContactId(),webHomePageEntry.getStatus());
            } else if (webHomePageEntry.getType() == Constant.THREE) {
                reportCardIds.add(webHomePageEntry.getContactId());
                reportCardStatus.put(webHomePageEntry.getContactId(),webHomePageEntry.getStatus());
            } else if (webHomePageEntry.getType() == Constant.FIVE) {
                reportCardSendIds.add(webHomePageEntry.getContactId());
                reportCardSendStatus.put(webHomePageEntry.getContactId(),webHomePageEntry.getStatus());
            }
        }

        if (workIds.size() > 0) {
            List<AppCommentEntry> appCommentEntries = appCommentDao.getEntryListByIds(workIds);
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            for (AppCommentEntry commentEntry : appCommentEntries) {
                communityIds.add(commentEntry.getRecipientId());
                userIds.add(commentEntry.getAdminId());
                subjectIds.add(commentEntry.getSubjectId());
            }
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            for (AppCommentEntry commentEntry : appCommentEntries) {
                WebHomePageDTO webHomePageDTO = new WebHomePageDTO(commentEntry);
                webHomePageDTO.setType(Constant.ONE);
                webHomePageDTO.setTimeExpression(TimeChangeUtils.getChangeTime(commentEntry.getCreateTime()));
                CommunityEntry communityEntry = communityEntryMap.get(commentEntry.getRecipientId());
                if (null != communityEntry) {
                    webHomePageDTO.setGroupName(communityEntry.getCommunityName());
                }
                UserEntry userEntry = userEntryMap.get(commentEntry.getAdminId());
                if (null != userEntry) {
                    webHomePageDTO.setUserName(
                            StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                    webHomePageDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                }
                SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(commentEntry.getSubjectId());
                if (null != subjectClassEntry) {
                    webHomePageDTO.setSubjectName(subjectClassEntry.getName());
                }
                webHomePageDTOs.add(webHomePageDTO);
            }
        }

        if (noticeIds.size() > 0) {
            List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getAppNoticeEntriesByIds(noticeIds);
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            for (AppNoticeEntry appNoticeEntry : appNoticeEntries) {
                communityIds.add(appNoticeEntry.getCommunityId());
                userIds.add(appNoticeEntry.getUserId());
                subjectIds.add(appNoticeEntry.getSubjectId());
            }
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            for (AppNoticeEntry appNoticeEntry : appNoticeEntries) {
                WebHomePageDTO webHomePageDTO = new WebHomePageDTO(appNoticeEntry);
                webHomePageDTO.setType(Constant.TWO);
                webHomePageDTO.setTimeExpression(TimeChangeUtils.getChangeTime(appNoticeEntry.getSubmitTime()));
                CommunityEntry communityEntry = communityEntryMap.get(appNoticeEntry.getCommunityId());
                if (null != communityEntry) {
                    webHomePageDTO.setGroupName(communityEntry.getCommunityName());
                }
                UserEntry userEntry = userEntryMap.get(appNoticeEntry.getUserId());
                if (null != userEntry) {
                    webHomePageDTO.setUserName(
                            StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                    webHomePageDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                }
                SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(appNoticeEntry.getSubjectId());
                if (null != subjectClassEntry) {
                    webHomePageDTO.setSubjectName(subjectClassEntry.getName());
                }
                webHomePageDTOs.add(webHomePageDTO);
            }
        }

        if (reportCardIds.size() > 0) {
            List<GroupExamUserRecordEntry> userRecordEntries = groupExamUserRecordDao.getGroupExamUserRecordsByIds(reportCardIds);
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
            Set<ObjectId> groupExamIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry groupExamUserRecordEntry : userRecordEntries) {
                groupExamIds.add(groupExamUserRecordEntry.getGroupExamDetailId());
            }
            Map<ObjectId, GroupExamDetailEntry> groupExamDetailEntryMap = groupExamDetailDao
                    .getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            initAllAvailableIds(communityIds,userIds,subjectIds,examTypeIds,groupExamDetailEntryMap);
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                GroupExamDetailEntry detailEntry = groupExamDetailEntryMap.get(userRecordEntry.getGroupExamDetailId());
                int status=Constant.ZERO;
                if(null!=reportCardStatus.get(userRecordEntry.getID())){
                    status=reportCardStatus.get(userRecordEntry.getID());
                }
                if (null != detailEntry) {
                    setWebHomeData(detailEntry,communityEntryMap,userEntryMap,subjectClassEntryMap,
                            examTypeEntryMap,webHomePageDTOs,status
                            );
                }
            }
        }

        if(reportCardSendIds.size()>0){
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
            Map<ObjectId, GroupExamDetailEntry> groupExamDetailEntryMap = groupExamDetailDao
                    .getGroupExamDetailMap(reportCardSendIds);
            initAllAvailableIds(communityIds,userIds,subjectIds,examTypeIds,groupExamDetailEntryMap);
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            for (Map.Entry<ObjectId, GroupExamDetailEntry> item:groupExamDetailEntryMap.entrySet()) {
                GroupExamDetailEntry detailEntry = item.getValue();
                int status=Constant.ZERO;
                if(null!=reportCardSendStatus.get(item.getKey())){
                    status=reportCardSendStatus.get(item.getKey());
                }
                setWebHomeData(detailEntry,communityEntryMap,
                        userEntryMap,subjectClassEntryMap,
                        examTypeEntryMap,webHomePageDTOs,status);
            }
        }
    }

    public void setWebHomeData(GroupExamDetailEntry detailEntry,Map<ObjectId, CommunityEntry> communityEntryMap,
                               Map<ObjectId, UserEntry> userEntryMap,Map<ObjectId, SubjectClassEntry> subjectClassEntryMap,
                               Map<ObjectId, ExamTypeEntry> examTypeEntryMap,
                               List<WebHomePageDTO> webHomePageDTOs,
                               int status
                               ){
        WebHomePageDTO webHomePageDTO = new WebHomePageDTO(detailEntry);
        webHomePageDTO.setStatus(status);
        webHomePageDTO.setType(Constant.TWO);
        webHomePageDTO.setTimeExpression(TimeChangeUtils.getChangeTime(detailEntry.getSubmitTime()));
        CommunityEntry communityEntry = communityEntryMap.get(detailEntry.getCommunityId());
        if (null != communityEntry) {
            webHomePageDTO.setGroupName(communityEntry.getCommunityName());
        }
        if (null != detailEntry.getExamType()) {
            ExamTypeEntry examTypeEntry = examTypeEntryMap.get(detailEntry.getExamType());
            if (null != examTypeEntry) {
                webHomePageDTO.setExamTypeName(examTypeEntry.getExamTypeName());
            }
        }
        UserEntry userEntry = userEntryMap.get(detailEntry.getUserId());
        if (null != userEntry) {
            webHomePageDTO.setUserName(
                    StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            webHomePageDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
        }
        SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(detailEntry.getSubjectId());
        if (null != subjectClassEntry) {
            webHomePageDTO.setSubjectName(subjectClassEntry.getName());
        }

        if (detailEntry.getRecordScoreType() == Constant.ONE) {
            RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(detailEntry.getID());
            if (null != evaluateEntry) {
                webHomePageDTO.setAvgScore(evaluateEntry.getAvgScore());
            }
        } else {
            RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(detailEntry.getID());
            if (null != levelEvaluateEntry) {
                webHomePageDTO.setaPercent(levelEvaluateEntry.getApercent());
            }
        }
        webHomePageDTOs.add(webHomePageDTO);
    }


    public void initAllAvailableIds(Set<ObjectId> communityIds,
                                    Set<ObjectId> userIds,
                                    Set<ObjectId> subjectIds,
                                    Set<ObjectId> examTypeIds,
                                    Map<ObjectId, GroupExamDetailEntry> groupExamDetailEntryMap) {
        for (Map.Entry<ObjectId, GroupExamDetailEntry>
                item : groupExamDetailEntryMap.entrySet()) {
            GroupExamDetailEntry
                    detailEntry = item.getValue();
            communityIds.add(detailEntry.getCommunityId());
            userIds.add(detailEntry.getUserId());
            subjectIds.add(detailEntry.getSubjectId());
            if (null != detailEntry.getExamType()) {
                examTypeIds.add(detailEntry.getExamType());
            }
        }
    }


    /**
     * @param userId
     * @param type
     * @param subjectId
     * @param mode
     * @param status
     * @param sTime
     * @param eTime
     * @param communityId
     * @param page
     * @param pageSize
     * @throws Exception
     */
    public Map<String, Object> getMySendHomePageEntries(ObjectId userId,
                                                        int type,
                                                        String subjectId,
                                                        int mode,
                                                        int status,
                                                        String sTime,
                                                        String eTime,
                                                        String communityId,
                                                        int page,
                                                        int pageSize) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<WebHomePageDTO> webHomePageDTOs = new ArrayList<WebHomePageDTO>();
        ObjectId cId = ObjectId.isValid(communityId) ? new ObjectId(communityId) : null;
        Map<String, Long> retMap = setTime(mode, sTime, eTime);
        long startTime = retMap.get("startTime");
        long endTime = retMap.get("endTime");
        ObjectId sId = ObjectId.isValid(subjectId) ? new ObjectId(subjectId) : null;
        List<WebHomePageEntry> entries = webHomePageDao.getMySendHomePageEntries(cId,
                type, sId, startTime, endTime, status, userId, page, pageSize
        );
        int count = webHomePageDao.countMySendHomePageEntries(cId, type, sId, startTime, endTime, status, userId);
        getDtosByEntries(webHomePageDTOs, entries);
        result.put("list", webHomePageDTOs);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }


}
