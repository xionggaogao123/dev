package com.fulaan.indexpage.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.reportCard.*;
import com.db.user.GenerateUserCodeDao;
import com.db.user.UserDao;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.indexpage.dto.WebHomePageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.appnotice.GenerateUserCodeEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
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

    private GenerateUserCodeDao generateUserCodeDao = new GenerateUserCodeDao();

    private VirtualAndUserDao virtualAndUserDao = new VirtualAndUserDao();

    private VirtualUserDao virtualUserDao = new VirtualUserDao();

    private RedDotService redDotService  = new RedDotService();


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


    public void generateUserCode(){
        long start=generateUserCodeDao.findEntryByLast();
        int count=1;
        while (count<=100000){
            long seqId=start+count;
            GenerateUserCodeEntry codeEntry=new GenerateUserCodeEntry(seqId);
            generateUserCodeDao.saveEntry(codeEntry);
            count++;
        }
    }


    public String getSeqId(){
        return String.valueOf(generateUserCodeDao.getCodeEntry().getSeqId());
    }


    /**
     * type：webHomePageEntry中的type
     */
    public void dataMapping(){
        int pageSize=200;
        List<Integer> types=new ArrayList<Integer>();
        types.add(Constant.FIVE);
        types.add(Constant.THREE);
        for(int type:types) {
            int page = 1;
            boolean flag = true;
            //在处理数据之前先删除老数据
            webHomePageDao.removeOldDataByType(type);
            if (type == Constant.FIVE) {
                while (flag) {
                    List<GroupExamDetailEntry> entries = groupExamDetailDao.getMappingDatas(page, pageSize);
                    if (entries.size() > 0) {
                        for (GroupExamDetailEntry entry : entries) {
                            WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.FIVE,
                                    entry.getUserId(),
                                    entry.getCommunityId(),
                                    entry.getID(), entry.getSubjectId(),
                                    null, null, entry.getExamType(), Constant.ZERO
                            );
                            if (entry.getStatus() == Constant.ONE) {
                                homePageEntry.setRemove(Constant.ONE);
                            } else {
                                homePageEntry.setStatus(entry.getStatus());
                            }
                            webHomePageDao.saveWebHomeEntry(homePageEntry);
                        }
                    } else {
                        flag = false;
                    }
                    page++;
                }
            } else if (type == Constant.THREE) {
                while (flag) {
                    List<GroupExamUserRecordEntry> entries = groupExamUserRecordDao.getMappingDatas(page, pageSize);
                    if(entries.size()>0){
                        for(GroupExamUserRecordEntry userRecordEntry:entries){
                            WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.THREE,
                                    userRecordEntry.getMainUserId(),
                                    userRecordEntry.getCommunityId(),
                                    userRecordEntry.getID(), userRecordEntry.getSubjectId(),
                                    userRecordEntry.getUserId(), userRecordEntry.getGroupExamDetailId(),
                                    userRecordEntry.getExamType(),Constant.ZERO
                            );
                            if (userRecordEntry.getStatus() == Constant.ONE) {
                                homePageEntry.setRemove(Constant.ONE);
                            } else {
                                homePageEntry.setStatus(userRecordEntry.getStatus());
                            }
                            webHomePageDao.saveWebHomeEntry(homePageEntry);
                        }
                    }else{
                        flag=false;
                    }
                    page++;
                }
            }
        }

    }

    //由绑定孩子列表获得虚拟孩子列表
    public List<ObjectId> getMyChildList(List<ObjectId> objectIds){
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        Set<ObjectId> set = new HashSet<ObjectId>();
        List<ObjectId> objectIdList1 = virtualAndUserDao.getEntryListByCommunityId(objectIds);
        set.addAll(objectIdList1);
        set.addAll(objectIds);
        objectIdList.addAll(set);
        return objectIdList;
    }
    public Map<String,Object> gatherReportCardList(String sId,String examType,
                                                   int status,
                                                   ObjectId userId,int page,int pageSize){
        Map<String,Object> result=new HashMap<String,Object>();
        ObjectId subjectId = ObjectId.isValid(sId) ? new ObjectId(sId) : null;
        ObjectId examTypeId= ObjectId.isValid(examType) ? new ObjectId(examType) : null;
        List<ObjectId> receiveIds2 = new ArrayList<ObjectId>();
        List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            receiveIds2.add(bindEntry.getUserId());
        }
        List<ObjectId> receiveIds = this.getMyChildList(receiveIds2);
        List<WebHomePageEntry> entries = webHomePageDao.gatherReportCardList(receiveIds,
                examTypeId, subjectId, status,userId, page, pageSize);
        int count=webHomePageDao.countGatherReports(receiveIds,
                examTypeId, subjectId, status,userId);
        List<GroupExamDetailDTO> detailDTOs=new ArrayList<GroupExamDetailDTO>();
        getGatherReportCardList(detailDTOs, entries);
        result.put("list", detailDTOs);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);

        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.repordcard.getType());
        return result;
    }


    public Map<String, Object> getGatherEntries(ObjectId userId, int type, String subjectId, String communityId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<WebHomePageDTO> webHomePageDTOs = new ArrayList<WebHomePageDTO>();
        List<ObjectId> receiveIds2 = new ArrayList<ObjectId>();
        if (type == Constant.NEGATIVE_ONE || type == Constant.THREE) {
            List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
            for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
                receiveIds2.add(bindEntry.getUserId());
            }
        }
        List<ObjectId> receiveIds = this.getMyChildList(receiveIds2);
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
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.repordcard.getType());
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

    public void getGatherReportCardList(List<GroupExamDetailDTO> detailDTOs,List<WebHomePageEntry> entries){
        List<ObjectId> reportCardIds = new ArrayList<ObjectId>();
        List<ObjectId> reportCardSendIds = new ArrayList<ObjectId>();
        for (WebHomePageEntry webHomePageEntry : entries) {
            if (webHomePageEntry.getType() == Constant.THREE) {
                reportCardIds.add(webHomePageEntry.getContactId());
            } else if (webHomePageEntry.getType() == Constant.FIVE) {
                reportCardSendIds.add(webHomePageEntry.getContactId());
            }
        }

        if (reportCardIds.size() > 0) {
            List<GroupExamUserRecordEntry> userRecordEntries = groupExamUserRecordDao.getGroupExamUserRecordsByIds(reportCardIds);
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
            Set<ObjectId> groupExamIds = new HashSet<ObjectId>();
            Set<ObjectId> childUserIds=new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry groupExamUserRecordEntry : userRecordEntries) {
                groupExamIds.add(groupExamUserRecordEntry.getGroupExamDetailId());
                childUserIds.add(groupExamUserRecordEntry.getUserId());
            }
            Map<ObjectId, GroupExamDetailEntry> groupExamDetailEntryMap = groupExamDetailDao
                    .getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            initAllAvailableIds(communityIds,userIds,subjectIds,examTypeIds,groupExamDetailEntryMap);
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, UserEntry> childUserEntryMap = userDao.getUserEntryMap(childUserIds, Constant.FIELDS);
            Map<ObjectId, VirtualUserEntry> virtualUserEntryMap = virtualUserDao.getVirtualUserMap(new ArrayList<ObjectId>(childUserIds));
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                GroupExamDetailEntry detailEntry = groupExamDetailEntryMap.get(userRecordEntry.getGroupExamDetailId());
                if (null != detailEntry) {
                    GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(detailEntry);
                    detailDTO.setOwner(true);
                    //展示类型 个人还是全班
                    detailDTO.setShowType(detailEntry.getShowType());
                    UserEntry userEntry = childUserEntryMap.get(userRecordEntry.getUserId());
                    VirtualUserEntry virtualUserEntry = virtualUserEntryMap.get(userRecordEntry.getUserId());
                    if (null != userEntry) {
                        detailDTO.setChildUserName(
                                org.apache.commons.lang3.StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                    }else if(null !=virtualUserEntry){
                        detailDTO.setChildUserName(virtualUserEntry.getUserName());
                    }
                    detailDTO.setChildUserId(userRecordEntry.getUserId().toString());
                    CommunityEntry communityEntry = communityEntryMap.get(userRecordEntry.getCommunityId());
                    if (null != communityEntry) {
                        detailDTO.setGroupName(communityEntry.getCommunityName());
                    }
                    SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(userRecordEntry.getSubjectId());
                    if (null != subjectClassEntry) {
                        detailDTO.setSubjectName(subjectClassEntry.getName());
                    }
                    if (null != userRecordEntry.getExamType()) {
                        ExamTypeEntry examTypeEntry = examTypeEntryMap.get(userRecordEntry.getExamType());
                        if (null != examTypeEntry) {
                            detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                        }
                    }
                    detailDTO.setScore(userRecordEntry.getScore());
                    detailDTO.setScoreLevel(userRecordEntry.getScoreLevel());
                    UserEntry mainUserEntry = mainUserEntryMap.get(detailEntry.getUserId());
                    if (null != mainUserEntry) {
                        detailDTO.setUserName(org.apache.commons.lang3.StringUtils.isNotBlank(mainUserEntry.getNickName())?mainUserEntry.getNickName():mainUserEntry.getUserName());
                    }
                    detailDTO.setStatus(userRecordEntry.getStatus());
                    detailDTO.setSingleScoreId(userRecordEntry.getID().toString());
                    detailDTOs.add(detailDTO);
                }
            }
        }

        if(reportCardSendIds.size()>0){
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
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
                GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(detailEntry);
                detailDTO.setOwner(false);
                detailDTO.setUnSignCount(detailDTO.getSignCount() - detailDTO.getSignedCount());
                UserEntry mainUserEntry = userEntryMap.get(detailEntry.getUserId());
                if (null != mainUserEntry) {
                    detailDTO.setUserName(mainUserEntry.getUserName());
                }
                CommunityEntry communityEntry = communityEntryMap.get(detailEntry.getCommunityId());
                if (null != communityEntry) {
                    detailDTO.setGroupName(communityEntry.getCommunityName());
                }
                if (null != detailEntry.getExamType()) {
                    ExamTypeEntry examTypeEntry = examTypeEntryMap.get(detailEntry.getExamType());
                    if (null != examTypeEntry) {
                        detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                    }
                }
                SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(detailEntry.getSubjectId());
                if (null != subjectClassEntry) {
                    detailDTO.setSubjectName(subjectClassEntry.getName());
                }
                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(detailEntry.getID());
                    if (null != evaluateEntry) {
                        detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                    }
                } else {
                    RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(detailEntry.getID());
                    if (null != levelEvaluateEntry) {
                        detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                    }
                }
                detailDTOs.add(detailDTO);
            }
        }
        Collections.sort(detailDTOs, new Comparator<GroupExamDetailDTO>() {
            @Override
            public int compare(GroupExamDetailDTO o1, GroupExamDetailDTO o2) {
                int result=0;
                if(o1.getSubmitTime()>o2.getSubmitTime()){
                    result=-1;
                }else if(o1.getSubmitTime()<o2.getSubmitTime()) {
                    result=1;
                }
                return result;
            }
        });
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
            Set<ObjectId> childUserIds=new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry groupExamUserRecordEntry : userRecordEntries) {
                groupExamIds.add(groupExamUserRecordEntry.getGroupExamDetailId());
                childUserIds.add(groupExamUserRecordEntry.getUserId());
            }
            Map<ObjectId, GroupExamDetailEntry> groupExamDetailEntryMap = groupExamDetailDao
                    .getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            initAllAvailableIds(communityIds,userIds,subjectIds,examTypeIds,groupExamDetailEntryMap);
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao
                    .findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            Map<ObjectId, UserEntry> childUserEntryMap = userDao.getUserEntryMap(childUserIds, Constant.FIELDS);
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                GroupExamDetailEntry detailEntry = groupExamDetailEntryMap.get(userRecordEntry.getGroupExamDetailId());
                int status=Constant.ZERO;
                if(null!=reportCardStatus.get(userRecordEntry.getID())){
                    status=reportCardStatus.get(userRecordEntry.getID());
                }
                boolean owner=false;
                if (null != detailEntry) {
                    ObjectId childUserId=userRecordEntry.getUserId();
                    String childUserName=Constant.EMPTY;
                    UserEntry userEntry=childUserEntryMap.get(childUserId);
                    if(null!=userEntry){
                        childUserName= org.apache.commons.lang3.StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                    }
                    setWebHomeData(userRecordEntry.getID(),detailEntry,communityEntryMap,userEntryMap,subjectClassEntryMap,
                            examTypeEntryMap,webHomePageDTOs,status,owner,childUserId.toString(),childUserName
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
                setWebHomeData(detailEntry.getID(),detailEntry,communityEntryMap,
                        userEntryMap,subjectClassEntryMap,
                        examTypeEntryMap,webHomePageDTOs,status,true,Constant.EMPTY,Constant.EMPTY);
            }
        }
        Collections.sort(webHomePageDTOs, new Comparator<WebHomePageDTO>() {
            @Override
            public int compare(WebHomePageDTO o1, WebHomePageDTO o2) {
                int result=0;
                if(o1.getSubmitTime()>o2.getSubmitTime()){
                    result=-1;
                }else if(o1.getSubmitTime()<o2.getSubmitTime()) {
                    result=1;
                }
                return result;
            }
        });
    }

    public void setWebHomeData(ObjectId id,GroupExamDetailEntry detailEntry,Map<ObjectId, CommunityEntry> communityEntryMap,
                               Map<ObjectId, UserEntry> userEntryMap,Map<ObjectId, SubjectClassEntry> subjectClassEntryMap,
                               Map<ObjectId, ExamTypeEntry> examTypeEntryMap,
                               List<WebHomePageDTO> webHomePageDTOs,
                               int status,
                               boolean isOwner,
                               String childUserId,
                               String childUserName
                               ){
        WebHomePageDTO webHomePageDTO = new WebHomePageDTO(detailEntry);
        webHomePageDTO.setStatus(status);
        webHomePageDTO.setId(detailEntry.getID().toString());
        webHomePageDTO.setType(Constant.TWO);
        webHomePageDTO.setOwner(isOwner);
        webHomePageDTO.setContactId(id.toString());
        webHomePageDTO.setChildUserId(childUserId);
        webHomePageDTO.setChildUserName(childUserName);
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
