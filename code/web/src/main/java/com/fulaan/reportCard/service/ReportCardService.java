package com.fulaan.reportCard.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppNoticeDao;
import com.db.reportCard.*;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.dto.VideoDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.reportCard.dto.*;
import com.fulaan.user.service.TestTable;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HSSFUtils;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.reportCard.*;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.QiniuFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/9/30.
 */
@Service
public class ReportCardService {

    @Autowired
    private UserService userService;
    @Autowired
    private RedDotService redDotService;

    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();
    private RecordScoreEvaluateDao recordScoreEvaluateDao = new RecordScoreEvaluateDao();
    private RecordLevelEvaluateDao recordLevelEvaluateDao = new RecordLevelEvaluateDao();
    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    private CommunityDao communityDao = new CommunityDao();
    private VirtualUserDao virtualUserDao = new VirtualUserDao();

    private ExamTypeDao examTypeDao = new ExamTypeDao();

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();

    private GroupExamVersionDao groupExamVersionDao = new GroupExamVersionDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private WebHomePageDao webHomePageDao = new WebHomePageDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private MemberDao memberDao = new MemberDao();

    private ReportCardSignDao reportCardSignDao = new ReportCardSignDao();

    private VirtualCommunityDao virtualCommunityDao = new VirtualCommunityDao();

    private IndexPageDao indexPageDao = new IndexPageDao();


    public static void main(String[] args) throws Exception {
        ReportCardService reportCardService = new ReportCardService();
        /**
         GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
         detailDTO.setGroupId("59c32cc6670ab23fb82dc4ae");
         detailDTO.setCommunityId("59c32cc5670ab23fb82dc4ac");
         detailDTO.setExamType("59e415a2bf2e7917683d7354");
         detailDTO.setRecordScoreType(2);
         detailDTO.setExamName("单周测试");
         detailDTO.setSubjectId("59b5fc0fbf2e791bb445cdb7");
         detailDTO.setMaxScore(-1);
         detailDTO.setQualifyScore(-1);
         detailDTO.setExcellentScore(-1);
         detailDTO.setExamStrTime("2017-10-20");
         ObjectId userId=new ObjectId("59c32c8c670ab23fb82dc49a");
         reportCardService.saveGroupExamDetail(detailDTO,userId);
         **/
        /**-----------添加学生成绩---------**/
        /**
         List<GroupExamUserRecordDTO> examGroupUserScoreDTOs=new ArrayList<GroupExamUserRecordDTO>();
         GroupExamUserRecordDTO dto1=new GroupExamUserRecordDTO();
         dto1.setId("59e6c86a267564078c9d07be");
         dto1.setScore(-1);
         dto1.setScoreLevel(99);
         dto1.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto1);
         GroupExamUserRecordDTO dto2=new GroupExamUserRecordDTO();
         dto2.setId("59e6c86a267564078c9d07bf");
         dto2.setScore(-1);
         dto2.setScoreLevel(98);
         dto2.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto2);
         GroupExamUserRecordDTO dto3=new GroupExamUserRecordDTO();
         dto3.setId("59e6c86a267564078c9d07c0");
         dto3.setScore(-1);
         dto3.setScoreLevel(90);
         dto3.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto3);
         GroupExamUserRecordDTO dto4=new GroupExamUserRecordDTO();
         dto4.setId("59e6c86a267564078c9d07c1");
         dto4.setScore(-1);
         dto4.setScoreLevel(93);
         dto4.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto4);
         GroupExamUserRecordDTO dto5=new GroupExamUserRecordDTO();
         dto5.setId("59e6c86a267564078c9d07c2");
         dto5.setScore(-1);
         dto5.setScoreLevel(96);
         dto5.setGroupExamDetailId("59e6c86a267564078c9d07bd");
         examGroupUserScoreDTOs.add(dto5);
         int status=2;
         reportCardService.saveRecordExamScore(examGroupUserScoreDTOs,status);
         **/
        /**----------生成版本号--------------**/
        /**
         GroupExamVersionEntry versionEntry=new GroupExamVersionEntry(new ObjectId("59e6c86a267564078c9d07bd"),1);
         GroupExamVersionDao groupExamVersionDao=new GroupExamVersionDao();
         groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
         **/
    }


    /**
     * 签字的功能
     *
     * @param groupExamDetailId
     * @param userId
     */
    public void pushSign(ObjectId groupExamDetailId,
                         ObjectId userId,
                         ObjectId mainUserId
    ) {
        GroupExamUserRecordEntry recordEntry = groupExamUserRecordDao.getUserRecordEntry(groupExamDetailId, userId);
        if (null == recordEntry) {
            groupExamUserRecordDao.pushSign(groupExamDetailId, userId);
            groupExamDetailDao.updateSignedCount(groupExamDetailId);
            GroupExamUserRecordEntry userRecordEntry=groupExamUserRecordDao.getExamUserRecordEntry(groupExamDetailId,userId);
            if(null!=userRecordEntry) {
                webHomePageDao.updateContactStatus(userRecordEntry.getID(), Constant.THREE, Constant.THREE);
                reportCardSignDao.updateTypeByRecordId(userRecordEntry.getID());
            }

        }
    }

    /**
     * 删除成绩单
     *
     * @param id
     */
    public void removeGroupExamDetailEntry(ObjectId id, ObjectId userId) throws Exception {
        GroupExamDetailEntry entry = groupExamDetailDao.getGroupExamDetailEntry(id);
        if (null != entry) {
            if (null != entry.getUserId() && null != userId &&
                    entry.getUserId().toString().equals(userId.toString())) {
                long current=System.currentTimeMillis();
                if(entry.getSubmitTime() >current-24*60*60*1000) {
                    groupExamDetailDao.removeGroupExamDetailEntry(id);
                    groupExamUserRecordDao.updateGroupExamDetailStatus(id, Constant.ONE);
                    webHomePageDao.removeContactList(id);
                    webHomePageDao.removeReportCard(id);
                }else{
                    throw new Exception("已过有效时间!");
                }
            } else {
                throw new Exception("你没有权限删除成绩单！");
            }
        }

    }

    public Map<String,Object> searchReportCardSignList(ObjectId groupExamDetailId){
        Map<String,Object> result = new HashMap<String,Object>();
        List<User> sign = new ArrayList<User>();
        List<User> unSign = new ArrayList<User>();
        List<ReportCardSignEntry> signEntries = reportCardSignDao.getEntries(groupExamDetailId);
        Map<ObjectId,List<Integer>> userTypeMap = new HashMap<ObjectId, List<Integer>>();
        Map<ObjectId,Long> signTime = new HashMap<ObjectId, Long>();
        for(ReportCardSignEntry signEntry:signEntries){
            if(null!=signEntry.getParentId()) {
                signTime.put(signEntry.getParentId(), signEntry.getSignTime());
                if (null != userTypeMap.get(signEntry.getParentId())) {
                    List<Integer> types = userTypeMap.get(signEntry.getParentId());
                    types.add(signEntry.getType());
                    userTypeMap.put(signEntry.getParentId(), types);
                } else {
                    List<Integer> types = new ArrayList<Integer>();
                    types.add(signEntry.getType());
                    userTypeMap.put(signEntry.getParentId(), types);
                }
            }
        }
        Set<ObjectId> signIds = new HashSet<ObjectId>();
        Set<ObjectId> unSignIds = new HashSet<ObjectId>();
        for(Map.Entry<ObjectId,List<Integer>> item:userTypeMap.entrySet()){
            ObjectId mainUserId = item.getKey();
            List<Integer> types = item.getValue();
            boolean flag=true;
            for(int type:types){
                if(type!=Constant.THREE){
                    flag=false;
                }
            }
            if(flag){
                signIds.add(mainUserId);
            }else {
                unSignIds.add(mainUserId);
            }
        }
        if(signIds.size()>0) {
            setSignValues(signIds,signTime,sign,Constant.ONE);
        }
        if(unSignIds.size()>0){
            setSignValues(unSignIds,signTime,unSign,Constant.TWO);
        }
        result.put("SignList",sign);
        result.put("SignListNum",sign.size());
        result.put("UnSignList",unSign);
        result.put("UnSignListNum",unSign.size());
        return result;
    }

    public void setSignValues(Set<ObjectId> userIds,Map<ObjectId,Long> signTime,
                              List<User> users,int type){
        Map<ObjectId, UserEntry> signUserEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(Map.Entry<ObjectId, UserEntry> userItem:signUserEntryMap.entrySet()){
            UserEntry userEntry=userItem.getValue();
            String time=Constant.EMPTY;
            if(type==Constant.ONE){
                time=DateTimeUtils.getLongToStrTimeTwo(signTime.get(userItem.getKey()));
            }
            User user=new User(userEntry.getUserName(),
                    userEntry.getNickName(),userEntry.getID().toString(),
                    AvatarUtils.getAvatar2(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                    userEntry.getSex(), time);
            users.add(user);
        }
    }


    public void sendGroupExam(ObjectId groupExamDetailId) {
        groupExamDetailDao.updateGroupExamDetailEntry(groupExamDetailId, Constant.TWO);
        groupExamUserRecordDao.updateGroupExamDetailStatus(groupExamDetailId, Constant.TWO);
        webHomePageDao.updateContactStatus(groupExamDetailId, Constant.FIVE, Constant.TWO);
        webHomePageDao.updateReportCardStatus(groupExamDetailId,Constant.THREE, Constant.TWO);
        GroupExamDetailEntry entry = groupExamDetailDao.getEntryById(groupExamDetailId);
        //添加红点
        redDotService.addThirdList(entry.getID(),entry.getCommunityId(), entry.getUserId(), ApplyTypeEn.repordcard.getType());
    }

    public GroupExamVersionDTO getExamGroupVersion(ObjectId groupExamDetailId) throws Exception {
        GroupExamVersionEntry entry = groupExamVersionDao.getVersionByGroupExamDetailId(groupExamDetailId);
        if (null != entry) {
            return new GroupExamVersionDTO(entry);
        } else {
            throw new Exception("传入的考试参数有误");
        }
    }


    public List<GroupExamUserRecordDTO> searchRecordStudentScores(ObjectId groupExamDetailId, int score, int scoreLevel, int type) {
        List<GroupExamUserRecordDTO> recordExamScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        int maxScoreLevel = 100;
        if (scoreLevel >= RecordLevelEnum.AP.getLevelScore()) {
            maxScoreLevel = 100;
        } else if (scoreLevel >= RecordLevelEnum.BP.getLevelScore()) {
            maxScoreLevel = 97;
        } else if (scoreLevel >= RecordLevelEnum.CP.getLevelScore()) {
            maxScoreLevel = 94;
        } else if (scoreLevel >= RecordLevelEnum.DP.getLevelScore()) {
            maxScoreLevel = 91;
        }
        final List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId, score, maxScoreLevel, scoreLevel, type);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            userIds.add(recordEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        Map<ObjectId, NewVersionCommunityBindEntry> bindUserMap = new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
            bindUserMap = newVersionCommunityBindDao.getUserEntryMapByCondition(
                    recordEntries.get(0).getCommunityId(), new ArrayList<ObjectId>(userIds));
        }
        boolean flag = false;
        for (GroupExamUserRecordEntry recordEntry : recordEntries) {
            GroupExamUserRecordDTO userRecordDTO = new GroupExamUserRecordDTO(recordEntry);
            NewVersionCommunityBindEntry
                    entry = bindUserMap.get(recordEntry.getUserId());
            if (null == entry) {
                flag = true;
            } else {
                userRecordDTO.setUserNumber(entry.getNumber());
                if (StringUtils.isNotBlank(entry.getThirdName())) {
                    userRecordDTO.setUserName(entry.getThirdName());
                } else {
                    flag = true;
                }
            }
            if (flag) {
                UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                if(null != userEntry){
                    userRecordDTO.setUserName(
                            StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                }
                flag = false;
            }
            VirtualUserEntry virtualUserEntry = virtualUserDao.getVirtualUserByUserId(recordEntry.getUserId());
            if(null != virtualUserEntry){
                userRecordDTO.setUserNumber(virtualUserEntry.getUserNumber());
                userRecordDTO.setUserName(virtualUserEntry.getUserName());
            }
            recordExamScoreDTOs.add(userRecordDTO);
        }
        Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
            @Override
            public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                int result=0;
                if(StringUtils.isNotEmpty(o1.getUserNumber())&&
                        StringUtils.isNotEmpty(o1.getUserName())) {
                    result = getCompareResult(o1.getUserNumber(), o2.getUserNumber());
                    if (result == 0) {
                        result = getCompareResult(o1.getUserName(), o2.getUserName());
                    }
                }
                return result;
            }
        });
        return recordExamScoreDTOs;
    }


    public int getCompareResult(String itemOne, String itemTwo) {
        int result = 0;
        if (itemOne.length() > itemTwo.length()) {
            result = -1;
        } else if (itemOne.length() < itemTwo.length()) {
            result = 1;
        } else {
            int length = itemOne.length();
            for (int i = 0; i < length; i++) {
                if (itemOne.charAt(i) > itemTwo.charAt(i)) {
                    result = -1;
                } else if (itemOne.charAt(i) < itemTwo.charAt(i)) {
                    result = 1;
                }
            }
        }
        return result;
    }

    public int countReceiveExams(
            String subjectId, String examType, int status,
            ObjectId userId
    ) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        return groupExamUserRecordDao.countStudentReceivedEntries(suId, examTypeId, status, userId);
    }

    /**
     * 获取我接受的成绩单列表(学生)
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamDetailDTO> getReceiveExams(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize
    ) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getStudentReceivedEntries(
                suId, examTypeId, status, userId, page, pageSize
        );
        return getGroupExamDetailDtos(recordEntries);
    }

    public List<GroupExamDetailDTO> getGroupExamDetailDtos(List<GroupExamUserRecordEntry> recordEntries) {
        List<GroupExamDetailDTO> groupExamDetailDTOs = new ArrayList<GroupExamDetailDTO>();
        if (recordEntries.size() > 0) {
            Set<ObjectId> groupExamIds = new HashSet<ObjectId>();
            Set<ObjectId> uIds = new HashSet<ObjectId>();
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            Set<ObjectId> subjectIds = new HashSet<ObjectId>();
            Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry
                    entry : recordEntries) {
                groupExamIds.add(entry.getGroupExamDetailId());
                uIds.add(entry.getUserId());
                communityIds.add(entry.getCommunityId());
                subjectIds.add(entry.getSubjectId());
                if (null != entry.getExamType()) {
                    examTypeIds.add(entry.getExamType());
                }
            }
            Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(uIds, Constant.FIELDS);
            Map<ObjectId, VirtualUserEntry> virtualUserEntryMap = virtualUserDao.getVirtualUserMap(new ArrayList<ObjectId>(uIds));
            Map<ObjectId, GroupExamDetailEntry> examDetailEntryMap = groupExamDetailDao.getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
            Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
            Map<ObjectId, ExamTypeEntry> examTypeEntryMap = new HashMap<ObjectId, ExamTypeEntry>();
            if (examTypeIds.size() > 0) {
                examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
            }
            List<ObjectId> mainUserIds = new ArrayList<ObjectId>();
            Map<ObjectId, UserEntry> mainUserEntryMap = new HashMap<ObjectId, UserEntry>();
            for (Map.Entry<ObjectId, GroupExamDetailEntry>
                    detailItem : examDetailEntryMap.entrySet()) {
                mainUserIds.add(detailItem.getValue().getUserId());
            }
            if (mainUserIds.size() > 0) {
                mainUserEntryMap = userService.getUserEntryMap(mainUserIds, Constant.FIELDS);
            }
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                ObjectId groupExamDetailId = recordEntry.getGroupExamDetailId();
                GroupExamDetailEntry detailEntry = examDetailEntryMap.get(groupExamDetailId);
                if (null != detailEntry) {
                    GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(detailEntry);
                    UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                    VirtualUserEntry virtualUserEntry = virtualUserEntryMap.get(recordEntry.getUserId());
                    if (null != userEntry) {
                        detailDTO.setChildUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                    }else if(null!=virtualUserEntry){
                        detailDTO.setChildUserName(StringUtils.isNotBlank(virtualUserEntry.getUserName())?virtualUserEntry.getUserName():Constant.EMPTY);
                    }
                    detailDTO.setChildUserId(recordEntry.getUserId().toString());
                    CommunityEntry communityEntry = communityEntryMap.get(recordEntry.getCommunityId());
                    if (null != communityEntry) {
                        detailDTO.setGroupName(communityEntry.getCommunityName());
                    }
                    SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(recordEntry.getSubjectId());
                    if (null != subjectClassEntry) {
                        detailDTO.setSubjectName(subjectClassEntry.getName());
                    }
                    if (null != recordEntry.getExamType()) {
                        ExamTypeEntry examTypeEntry = examTypeEntryMap.get(recordEntry.getExamType());
                        if (null != examTypeEntry) {
                            detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                        }
                    }
                    detailDTO.setScore(recordEntry.getScore());
                    detailDTO.setScoreLevel(recordEntry.getScoreLevel());
                    UserEntry mainUserEntry = mainUserEntryMap.get(detailEntry.getUserId());
                    if (null != mainUserEntry) {
                        detailDTO.setUserName(StringUtils.isNotEmpty(mainUserEntry.getNickName())?mainUserEntry.getNickName():mainUserEntry.getUserName());
                    }
                    detailDTO.setStatus(recordEntry.getStatus());
                    detailDTO.setSingleScoreId(recordEntry.getID().toString());
                    groupExamDetailDTOs.add(detailDTO);
                }
            }
        }
        return groupExamDetailDTOs;
    }


    /**
     * 获取我接受的成绩单列表(家长)
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> getParentReceivedGroupExamDetailDTOs(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getEntriesByMainUserId(userId);
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            userIds.add(bindEntry.getUserId());
        }
        List<GroupExamUserRecordEntry> userRecordEntries = groupExamUserRecordDao.getParentReceivedEntries(
                suId, examTypeId, status, userId, new ArrayList<ObjectId>(userIds), page, pageSize
        );
        int count = groupExamUserRecordDao.countParentReceivedEntries(suId, examTypeId, status, userId, new ArrayList<ObjectId>(userIds));
        List<GroupExamDetailDTO> groupExamDetailDTOs = getGroupExamDetailDtos(userRecordEntries);
        retMap.put("list", groupExamDetailDTOs);
        retMap.put("count", count);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.repordcard.getType());
        return retMap;
    }


    public int countMySendGroupExamDetailDTOs(String subjectId, String examType, int status,
                                              ObjectId userId) {
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        return groupExamDetailDao.countMySendGroupExamDetailEntries(suId, examTypeId, status, userId);
    }

    /**
     * 获取我发出的成绩单列表
     *
     * @param userId
     * @return
     */
    public List<GroupExamDetailDTO> getMySendGroupExamDetailDTOs(
            String subjectId, String examType, int status,
            ObjectId userId, int page, int pageSize) {
        List<GroupExamDetailDTO> groupExamDetailDTOs = new ArrayList<GroupExamDetailDTO>();
        ObjectId suId = StringUtils.isNotBlank(subjectId) ? new ObjectId(subjectId) : null;
        ObjectId examTypeId = StringUtils.isNotBlank(examType) ? new ObjectId(examType) : null;
        List<GroupExamDetailEntry> entries = groupExamDetailDao.getMySendGroupExamDetailEntries(
                suId, examTypeId, status, userId,
                page, pageSize);
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        Set<ObjectId> examTypeIds = new HashSet<ObjectId>();
        Set<ObjectId> subjectIds = new HashSet<ObjectId>();
        for (GroupExamDetailEntry examDetailEntry : entries) {
            communityIds.add(examDetailEntry.getCommunityId());
            if (null != examDetailEntry.getExamType()) {
                examTypeIds.add(examDetailEntry.getExamType());
            }
            subjectIds.add(examDetailEntry.getSubjectId());
        }
        Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
        Map<ObjectId, ExamTypeEntry> examTypeEntryMap = new HashMap<ObjectId, ExamTypeEntry>();
        if (examTypeIds.size() > 0) {
            examTypeEntryMap = examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
        }
        Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(new ArrayList<ObjectId>(subjectIds));
        for (GroupExamDetailEntry examDetailEntry : entries) {
            GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(examDetailEntry);
            detailDTO.setUnSignCount(detailDTO.getSignCount() - detailDTO.getSignedCount());
            CommunityEntry communityEntry = communityEntryMap.get(examDetailEntry.getCommunityId());
            if (null != communityEntry) {
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            if (null != examDetailEntry.getExamType()) {
                ExamTypeEntry examTypeEntry = examTypeEntryMap.get(examDetailEntry.getExamType());
                if (null != examTypeEntry) {
                    detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
                }
            }
            SubjectClassEntry subjectClassEntry = subjectClassEntryMap.get(examDetailEntry.getSubjectId());
            if (null != subjectClassEntry) {
                detailDTO.setSubjectName(subjectClassEntry.getName());
            }
            if (examDetailEntry.getRecordScoreType() == Constant.ONE) {
                RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(examDetailEntry.getID());
                if (null != evaluateEntry) {
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                }
            } else {
                RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(examDetailEntry.getID());
                if (null != levelEvaluateEntry) {
                    detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                }
            }
            groupExamDetailDTOs.add(detailDTO);
        }
        return groupExamDetailDTOs;
    }

    /**
     * 保存新建的考试
     *
     * @param dto
     * @param userId
     * @throws Exception
     */
    public String saveGroupExamDetail(GroupExamDetailDTO dto, ObjectId userId) throws Exception {

        String id = dto.getId();
        dto.setUserId(userId.toString());
        dto.setSignedCount(Constant.ZERO);
        dto.setSignCount(Constant.ZERO);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long examTime = dateFormat.parse(dto.getExamStrTime()).getTime();
        dto.setExamTime(examTime);
        if (StringUtils.isEmpty(id)) {
            String communityId = dto.getCommunityId();
            Set<ObjectId> userIds = new HashSet<ObjectId>();
            Map<ObjectId,ObjectId> userMainIds=new HashMap<ObjectId, ObjectId>();
            List<NewVersionCommunityBindEntry> entries
                    = newVersionCommunityBindDao.getStudentIdListByCommunityId(new ObjectId(communityId));
            for (NewVersionCommunityBindEntry bindEntry : entries) {
//                userIds.add(bindEntry.getUserId());
                userMainIds.put(bindEntry.getUserId(),bindEntry.getMainUserId());
            }
            List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
            for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
                userIds.add(virtualUserEntry.getUserId());
            }


            ObjectId groupExamDetailId = groupExamDetailDao.saveGroupExamDetailEntry(dto.buildEntry());
            List<GroupExamUserRecordEntry> userRecordEntries = new ArrayList<GroupExamUserRecordEntry>();
            for (ObjectId uId : userIds) {
                userRecordEntries.add(new GroupExamUserRecordEntry(
                        groupExamDetailId,
                        userId,
                        uId,
                        StringUtils.isNotEmpty(dto.getGroupId()) ? new ObjectId(dto.getGroupId()) : null,
                        StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,
                        StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        -1D,
                        -1,
                        0,
                        Constant.ZERO
                ));
            }
            WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                    StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                    groupExamDetailId, StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                    null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,Constant.ZERO
            );
            webHomePageDao.saveWebHomeEntry(homePageEntry);
            for (GroupExamUserRecordEntry userRecordEntry : userRecordEntries) {
                ObjectId recordId = groupExamUserRecordDao.saveGroupExamUserRecord(userRecordEntry);
                WebHomePageEntry pageEntry = new WebHomePageEntry(Constant.THREE, userId,
                        userRecordEntry.getCommunityId(),
                        recordId, userRecordEntry.getSubjectId(),
                        userRecordEntry.getUserId(), groupExamDetailId, userRecordEntry.getExamType(),Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(pageEntry);
                ObjectId mainUserId=userMainIds.get(userRecordEntry.getUserId());
                ReportCardSignEntry signEntry =new  ReportCardSignEntry(mainUserId,groupExamDetailId,
                        recordId,Constant.ZERO,System.currentTimeMillis());
                reportCardSignDao.saveEntry(signEntry);
            }
            groupExamDetailDao.updateSignCount(groupExamDetailId, userIds.size());
            GroupExamVersionEntry versionEntry = new GroupExamVersionEntry(groupExamDetailId, 1L);
            groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
            return groupExamDetailId.toString();
        } else {
            GroupExamDetailEntry oldEntry = groupExamDetailDao.getGroupExamDetailEntry(new ObjectId(id));
            if (null != oldEntry) {
                GroupExamDetailEntry entry = dto.buildEntry();
                entry.setID(new ObjectId(id));
                entry.setSignCount(oldEntry.getSignCount());
                entry.setSignedCount(oldEntry.getSignedCount());
                groupExamDetailDao.saveGroupExamDetailEntry(entry);

                //查询该考试信息
                WebHomePageEntry homePageEntry = new WebHomePageEntry(Constant.FIVE, userId,
                        StringUtils.isNotEmpty(dto.getCommunityId()) ? new ObjectId(dto.getCommunityId()) : null,
                        new ObjectId(id), StringUtils.isNotEmpty(dto.getSubjectId()) ? new ObjectId(dto.getSubjectId()) : null,
                        null, null, StringUtils.isNotEmpty(dto.getExamType()) ? new ObjectId(dto.getExamType()) : null,Constant.ZERO
                );
                WebHomePageEntry pageEntry = webHomePageDao.getWebHomePageEntry(new ObjectId(id));
                if (null != pageEntry) {
                    homePageEntry.setID(pageEntry.getID());
                    homePageEntry.setStatus(oldEntry.getStatus());
                }
                webHomePageDao.saveWebHomeEntry(homePageEntry);
            }
            return id;
        }
    }

    public void updateVersion(ObjectId groupExamDetailId,
                              long version) {
        groupExamVersionDao.updateVersionByGroupExamDetailId(groupExamDetailId, version);
    }

    public void increaseVersion(ObjectId groupExamDetailId) {
        groupExamVersionDao.increaseVersion(groupExamDetailId);
    }

    /**
     * 保存成绩列表
     *
     * @param examScoreDTOs
     */
    public void saveRecordExamScore(List<GroupExamUserRecordDTO> examScoreDTOs, int status) {
        if (examScoreDTOs.size() > 0) {
            String groupExamDetailId = examScoreDTOs.get(0).getGroupExamDetailId();
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScore(new ObjectId(dto.getId()),
                        dto.getScore(), dto.getScoreLevel(), dto.getRank());
            }
            List<GroupExamUserRecordEntry> recordEntries = groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamDetailId), -1, -1, -1, 1);
            examScoreDTOs.clear();
            for (GroupExamUserRecordEntry entry : recordEntries) {
                examScoreDTOs.add(new GroupExamUserRecordDTO(entry));
            }
            GroupExamDetailEntry detailEntry = groupExamDetailDao.getEntryById(new ObjectId(groupExamDetailId));
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                Collections.sort(examScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() == o2.getScore()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(examScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if (o1.getScoreLevel() > o2.getScoreLevel()) {
                            return -1;
                        } else if (o1.getScoreLevel() == o2.getScoreLevel()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            int rank = 1;
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                dto.setRank(rank);
                rank++;
            }
            for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                groupExamUserRecordDao.updateGroupExamUserRecordScore(new ObjectId(dto.getId()),
                        dto.getScore(), dto.getScoreLevel(), dto.getRank());
                webHomePageDao.updateContactStatus(new ObjectId(dto.getId()), Constant.THREE, status);
            }
            //数据分析:分为两种
            //一是分值分析 二是等第分析
            groupExamDetailDao.updateGroupExamDetailEntry(new ObjectId(groupExamDetailId), status);
            webHomePageDao.updateContactStatus(new ObjectId(groupExamDetailId), Constant.FIVE, status);
            //更新状态
            groupExamUserRecordDao.updateGroupExamDetailStatus(new ObjectId(groupExamDetailId), status);

            //添加红点
            if(status==2){
                redDotService.addThirdList(detailEntry.getID(),detailEntry.getCommunityId(), detailEntry.getUserId(), ApplyTypeEn.repordcard.getType());
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                double qualifyScore = detailEntry.getQualifyScore();
                double excellentScore = detailEntry.getExcellentScore();
                //所有有分值的总人数
                int totalCount = 0;
                double totalScore = 0D;
                //合格人数
                int qualifyCount = 0;
                //优秀人数
                int excellentCount = 0;
                //不及格人数
                int unQualifyCount = 0;
                double maxScore = 0;
                if (examScoreDTOs.get(0).getScore() != -1D) {
                    maxScore = examScoreDTOs.get(0).getScore();
                }
                double minScore = 0;
                if (examScoreDTOs.get(examScoreDTOs.size() - 1).getScore() != -1D) {
                    minScore = examScoreDTOs.get(examScoreDTOs.size() - 1).getScore();
                }
                for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                    double score = dto.getScore();
                    if (score != -1) {
                        totalCount++;
                        totalScore += score;
                        if (score >= qualifyScore) {
                            qualifyCount++;
                        } else {
                            unQualifyCount++;
                        }
                        if (score >= excellentScore) {
                            excellentCount++;
                        }
                    }
                }
                if (totalCount != 0) {
                    double avgScore = divide(totalScore, (double) totalCount, 1);
                    double excellentPercent = divide(mul((double) excellentCount, 100D), (double) totalCount, 1);
                    double qualifyPercent = divide(mul((double) qualifyCount, 100D), (double) totalCount, 1);
                    double unQualifyPercent = divide(mul((double) unQualifyCount, 100D), (double) totalCount, 1);
                    RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(new ObjectId(groupExamDetailId));
                    RecordScoreEvaluateEntry entry = new RecordScoreEvaluateEntry(new ObjectId(groupExamDetailId),
                            excellentPercent, qualifyPercent, unQualifyPercent, avgScore, maxScore, minScore);
                    if (null != evaluateEntry) {
                        entry.setID(evaluateEntry.getID());
                    }
                    recordScoreEvaluateDao.saveRecordScoreEvaluateEntry(entry);
                }
            } else {
                int totalCount = 0;
                int aCount = 0;
                int bCount = 0;
                int cCount = 0;
                int dCount = 0;
                for (GroupExamUserRecordDTO dto : examScoreDTOs) {
                    int scoreLevel = dto.getScoreLevel();
                    if (scoreLevel != -1) {
                        totalCount++;
                        if (scoreLevel >= RecordLevelEnum.AP.getLevelScore()) {
                            aCount++;
                        } else if (RecordLevelEnum.AP.getLevelScore() >
                                scoreLevel && scoreLevel >= RecordLevelEnum.BP.getLevelScore()) {
                            bCount++;
                        } else if (scoreLevel < RecordLevelEnum.BP.getLevelScore()
                                && scoreLevel >= RecordLevelEnum.CP.getLevelScore()) {
                            cCount++;
                        } else if (scoreLevel < RecordLevelEnum.CP.getLevelScore()
                                && scoreLevel >= RecordLevelEnum.DP.getLevelScore()) {
                            dCount++;
                        }
                    }
                }
                if (totalCount != 0) {
                    double aPercent = divide(mul((double) aCount, 100D), (double) totalCount, 1);
                    double bPercent = divide(mul((double) bCount, 100D), (double) totalCount, 1);
                    double cPercent = divide(mul((double) cCount, 100D), (double) totalCount, 1);
                    double dPercent = divide(mul((double) dCount, 100D), (double) totalCount, 1);

                    RecordLevelEvaluateEntry entry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(new ObjectId(groupExamDetailId));
                    RecordLevelEvaluateEntry evaluateEntry = new RecordLevelEvaluateEntry(
                            new ObjectId(groupExamDetailId), aPercent, bPercent, cPercent, dPercent);
                    if (null != entry) {
                        evaluateEntry.setID(entry.getID());
                    }
                    recordLevelEvaluateDao.saveRecordLevelEvaluate(evaluateEntry);
                }
            }
        }
    }

    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public GroupExamDetailDTO getGroupExamDetail(ObjectId singleId) {
        GroupExamDetailDTO detailDTO = new GroupExamDetailDTO();
        GroupExamUserRecordEntry examUserRecordEntry = groupExamUserRecordDao.getGroupExamUserRecordEntry(singleId);
        if (null != examUserRecordEntry) {
            ObjectId groupExamDetailId = examUserRecordEntry.getGroupExamDetailId();
            detailDTO = getTeacherGroupExamDetail(groupExamDetailId);
            detailDTO.setSingleScoreId(singleId.toString());
            detailDTO.setStatus(examUserRecordEntry.getStatus());
            detailDTO.setChildUserId(examUserRecordEntry.getUserId().toString());
           // NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao.getEntry(examUserRecordEntry.getCommunityId(), examUserRecordEntry.getUserId());
            UserEntry userEntry = userService.findById(examUserRecordEntry.getUserId());
            VirtualUserEntry virtualUserEntry = virtualUserDao.getVirtualUserByUserId(examUserRecordEntry.getUserId());
           // if (bindEntry != null && bindEntry.getThirdName() != null && !bindEntry.getThirdName().equals("")) {
            //    detailDTO.setChildUserName(bindEntry.getThirdName());
            //} else {
            if (null != userEntry) {
                detailDTO.setChildUserName(StringUtils.isNotBlank(userEntry.getNickName()) ?
                                userEntry.getNickName() : userEntry.getUserName()
                );
            }else if(null!=virtualUserEntry){
                detailDTO.setChildUserName(virtualUserEntry.getUserName());
            }
           // }
            if (detailDTO.getRecordScoreType() == Constant.ONE) {
                detailDTO.setScore(examUserRecordEntry.getScore());
                detailDTO.setSingleRank(examUserRecordEntry.getRank());
            } else {
                detailDTO.setScoreLevel(examUserRecordEntry.getScoreLevel());
            }
        }
        return detailDTO;
    }

    public GroupExamDetailDTO getTeacherGroupExamDetail(ObjectId groupExamDetailId) {
        GroupExamDetailDTO detailDTO = new GroupExamDetailDTO();
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        if (null != detailEntry) {
            detailDTO = new GroupExamDetailDTO(detailEntry);
            SubjectClassEntry subjectClassEntry = subjectClassDao.getEntry(detailEntry.getSubjectId());
            if (null != subjectClassEntry) {
                detailDTO.setSubjectName(subjectClassEntry.getName());
            }
            UserEntry userEntry = userService.findById(detailEntry.getUserId());
            if (null != userEntry) {
                detailDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            }
            CommunityEntry communityEntry = communityDao.findByObjectId(detailEntry.getCommunityId());
            if (null != communityEntry) {
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            ExamTypeEntry examTypeEntry = examTypeDao.getEntry(detailEntry.getExamType());
            if (null != examTypeEntry) {
                detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
            }
            if (detailEntry.getRecordScoreType() == Constant.ONE) {
                RecordScoreEvaluateEntry evaluateEntry = recordScoreEvaluateDao.getEntryById(groupExamDetailId);
                if (null != evaluateEntry) {
                    detailDTO.setExcellentPercent(evaluateEntry.getExcellentPercent());
                    detailDTO.setQualifyPercent(evaluateEntry.getQualifyPercent());
                    detailDTO.setUnQualifyPercent(evaluateEntry.getUnQualifyPercent());
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                    detailDTO.setGroupMaxScore(evaluateEntry.getMaxScore());
                    detailDTO.setGroupMinScore(evaluateEntry.getMinScore());
                }
            } else {
                RecordLevelEvaluateEntry levelEvaluateEntry = recordLevelEvaluateDao.getRecordLevelEvaluateEntry(groupExamDetailId);
                if (null != levelEvaluateEntry) {
                    detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                    detailDTO.setbPercent(levelEvaluateEntry.getBpercent());
                    detailDTO.setcPercent(levelEvaluateEntry.getCpercent());
                    detailDTO.setdPercent(levelEvaluateEntry.getDpercent());
                }
            }
        }
        return detailDTO;
    }


    public List<ExamTypeDTO> getExamTypeDTOs() {
        List<ExamTypeDTO> examTypeDTOs = new ArrayList<ExamTypeDTO>();
        List<ExamTypeEntry> examTypeEntries = examTypeDao.getList();
        for (ExamTypeEntry examTypeEntry : examTypeEntries) {
            examTypeDTOs.add(new ExamTypeDTO(examTypeEntry));
        }
        return examTypeDTOs;
    }

    public Map<String, Object> searchUserList(ObjectId communityId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        List<VirtualUserEntry> virtualUserEntries = virtualUserDao.getAllVirtualUsers(communityId, page, pageSize);
        for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
            VirtualUserDTO dto = new VirtualUserDTO(virtualUserEntry);
            virtualUserDTOs.add(dto);
        }
        int count = virtualUserDao.countAllVirtualUsers(communityId);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", count);
        result.put("list", virtualUserDTOs);
        return result;
    }

    public Map<ObjectId, VirtualCommunityEntry> getVirtualCommunityMap(List<ObjectId> communityIds){
        return  virtualCommunityDao.getVirtualMap(communityIds);
    }

    public List<VirtualCommunityUserDTO> getRoleCommunities(ObjectId userId) {
        List<VirtualCommunityUserDTO> virtualCommunityUserDTOs
                = new ArrayList<VirtualCommunityUserDTO>();
        List<ObjectId> groupIds = memberDao.getManagerGroupIdsByUserId(userId);
        List<CommunityEntry> entries = communityDao.getCommunityEntriesByGroupIds(groupIds);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (CommunityEntry communityEntry : entries) {
            communityIds.add(communityEntry.getID());
        }
        Map<ObjectId, VirtualCommunityEntry> map = virtualCommunityDao.getVirtualMap(communityIds);
        for (CommunityEntry communityEntry : entries) {
            VirtualCommunityUserDTO userDTO = new VirtualCommunityUserDTO();
            userDTO.setCommunityId(communityEntry.getID().toString());
            userDTO.setCommunityName(communityEntry.getCommunityName());
            userDTO.setUserCount(Constant.ZERO);
            userDTO.setFileName(communityEntry.getCommunityName()+"学生名单");
            if (null != map.get(communityEntry.getID())) {
                userDTO.setUserCount(map.get(communityEntry.getID()).getUserCount());
                userDTO.setFileName(map.get(communityEntry.getID()).getFileName());
            }
            virtualCommunityUserDTOs.add(userDTO);
        }
        return virtualCommunityUserDTOs;
    }

    public void removeVirtualUserList(ObjectId communityId) {
        VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
        if(null!=virtualCommunityEntry){
            virtualCommunityEntry.setUserCount(Constant.ZERO);
            virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
        }
        virtualUserDao.removeOldData(communityId);
    }

    public void removeItemId(ObjectId itemId,ObjectId communityId) {
        VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
        if(null!=virtualCommunityEntry){
            virtualCommunityEntry.setUserCount(virtualCommunityEntry.getUserCount()-1);
            virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
        }
        virtualUserDao.removeItemById(itemId);
    }

    public void editVirtualUserItem(ObjectId itemId,
                                    String userName,
                                    String userNumber) {
//        virtualUserDao.editVirtualUserItem(itemId, userName, userNumber);
        VirtualUserEntry entry = virtualUserDao.findById(itemId);
        if(null!=entry){
            entry.setUserName(userName);
            entry.setUserNumber(userNumber);
            NewVersionCommunityBindEntry bindEntry = newVersionCommunityBindDao
                    .getVirtualBindEntry(entry.getCommunityId(),entry.getUserName());
            if(null!=bindEntry){
                entry.setUserId(bindEntry.getUserId());
            }
            virtualUserDao.saveVirualEntry(entry);
        }
    }

    public int importUserTemplate(InputStream inputStream, String communityId,String fileName) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        for (int j = 1; j <= rowNum; j++) {
            String userName =  getCellValue(sheet.getRow(j).getCell(0));
            String userNumber = getCellValue(sheet.getRow(j).getCell(1));
            if (StringUtils.isNotEmpty(userName)) {
                VirtualUserDTO v = new VirtualUserDTO();
                v.setCommunityId(communityId);
                v.setUserNumber(userNumber);
                v.setUserName(userName);
                virtualUserDTOs.add(v);
            }
        }
        return dealData(virtualUserDTOs,fileName);
    }

    public int judgeIsExistMatch(ObjectId communityId){
        int flag=1;
        VirtualCommunityEntry communityEntry =virtualCommunityDao.findntryByCommunityId(communityId);
        if(null==communityEntry){
            flag=0;
        }else{
            if(communityEntry.getUserCount()==Constant.ZERO){
                flag=0;
            }
        }
        return flag;
    }

    public void sendUnMatchNotice(String communityId,ObjectId userId)throws Exception{
        List<VirtualUserDTO> userDTOs = matchInputCount(communityId);
        if(userDTOs.size()>0){
            CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(communityId));
            UserEntry userEntry = userService.findById(userId);
            TestTable cg = new TestTable();
            ObjectId fileKey = new ObjectId();
            File outFile = File.createTempFile(fileKey.toString(), ".jpg");
            System.out.println(outFile.getAbsolutePath());
            System.out.println(outFile.getPath());
            cg.graphicsGeneration(outFile.getAbsolutePath(),userDTOs.size()+3,2,communityEntry.getCommunityName()+"未匹配学生名单",userDTOs);
            QiniuFileUtils.uploadFile(fileKey.toString() + ".jpg", new FileInputStream(outFile), QiniuFileUtils.TYPE_IMAGE);
            outFile.delete();
            String imagePath =  QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey.toString() + ".jpg");
            List<Attachement> imageList = new ArrayList<Attachement>();
            Attachement item =new Attachement();
            item.setFlnm(imagePath);
            item.setUrl(imagePath);
            item.setUploadUserId(userId.toString());
            imageList.add(item);
            AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                    "59dc8a68bf2e791a140769b4",
                    "其他",
                    communityEntry.getCommunityName()+"未匹配学生名单",
                    "未匹配名单通知列表",
                    communityEntry.getGroupId().toString(),
                    communityEntry.getID().toString(),
                    Constant.THREE,
                    new ArrayList<VideoDTO>(),
                    imageList,
                    new ArrayList<Attachement>(),
                    new ArrayList<Attachement>(),
                    communityEntry.getCommunityName(),
                    userEntry.getUserName());
            appNoticeDTO.setUserId(userId.toString());
            ObjectId appNoticeId=appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            IndexPageDTO dto1 = new IndexPageDTO();
            dto1.setType(CommunityType.appNotice.getType());
            dto1.setUserId(userId.toString());
            dto1.setCommunityId(communityEntry.getID().toString());
            dto1.setContactId(appNoticeId.toString());
            IndexPageEntry entry = dto1.buildAddEntry();
            indexPageDao.addEntry(entry);
            objectIdList.add(new ObjectId(communityEntry.getID().toString()));
            //1:家长2:学生3:家长，学生
            redDotService.addEntryList(objectIdList,userId, ApplyTypeEn.notice.getType(),Constant.THREE);
            redDotService.addOtherEntryList(objectIdList,userId, ApplyTypeEn.daynotice.getType(),Constant.THREE);
        }
    }

    public List<VirtualUserDTO> matchInputCount(String communityId)throws Exception{
        Map<String, ObjectId> userBindMap = new HashMap<String, ObjectId>();
        List<VirtualUserDTO> dtos = new ArrayList<VirtualUserDTO>();
        List<NewVersionCommunityBindEntry>
                bindEntries = newVersionCommunityBindDao.getStudentIdListByCommunityId(new ObjectId(communityId));
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            if (StringUtils.isNotEmpty(bindEntry.getThirdName())) {
                userBindMap.put(bindEntry.getThirdName(), bindEntry.getUserId());
            }
        }
        List<VirtualUserEntry> virtualUserEntries=virtualUserDao.getAllVirtualUsers(new ObjectId(communityId));
        for (VirtualUserEntry virtualUserEntry : virtualUserEntries) {
            String userName = virtualUserEntry.getUserName();
            if(null!=userBindMap.get(userName)){
                virtualUserEntry.setUserId(userBindMap.get(userName));
                virtualUserDao.saveVirualEntry(virtualUserEntry);
            }else{
                dtos.add(new VirtualUserDTO(virtualUserEntry));
            }
        }
        if(virtualUserEntries.size()==Constant.ZERO){
            throw new Exception("请先导入学生名单再进行匹配");
        }
        return dtos;
    }

    public int dealData(List<VirtualUserDTO> virtualUserDTOs,String fileName) {
        int count=0;
        if (virtualUserDTOs.size() > 0) {
            ObjectId communityId = new ObjectId(virtualUserDTOs.get(0).getCommunityId());
            List<NewVersionCommunityBindEntry>
                    bindEntries = newVersionCommunityBindDao.getStudentIdListByCommunityId(communityId);
            Map<String, ObjectId> userIds = new HashMap<String, ObjectId>();
            for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
                if (StringUtils.isNotEmpty(bindEntry.getThirdName())) {
                    userIds.put(bindEntry.getThirdName(), bindEntry.getUserId());
                }
            }
            List<VirtualUserEntry> entries = new ArrayList<VirtualUserEntry>();
            for (VirtualUserDTO virtualUserDTO : virtualUserDTOs) {
                String userName = virtualUserDTO.getUserName();
                if (null != userIds.get(userName)) {
                    VirtualUserEntry userEntry = new VirtualUserEntry(communityId, virtualUserDTO.getUserNumber(),
                            userIds.get(userName), virtualUserDTO.getUserName());
                    entries.add(userEntry);
                } else {
                    count++;
                    VirtualUserEntry userEntry = new VirtualUserEntry(communityId, virtualUserDTO.getUserNumber(),
                            new ObjectId(), virtualUserDTO.getUserName());
                    entries.add(userEntry);
                }
            }
            if (entries.size() > 0) {
                VirtualCommunityEntry virtualCommunityEntry=virtualCommunityDao.findntryByCommunityId(communityId);
                if(null!=virtualCommunityEntry){
                    virtualCommunityEntry.setUserCount(entries.size());
                    virtualCommunityEntry.setFileName(fileName);
                    virtualCommunityDao.saveVirtualCommunity(virtualCommunityEntry);
                }else{
                    VirtualCommunityEntry entry=new VirtualCommunityEntry(communityId,entries.size(),fileName);
                    virtualCommunityDao.saveVirtualCommunity(entry);
                }
                virtualUserDao.removeOldData(communityId);
                virtualUserDao.saveEntries(entries);
            }
        }
        return count;
    }

    public void importUserControl(InputStream inputStream,String fileName) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<VirtualUserDTO> virtualUserDTOs = new ArrayList<VirtualUserDTO>();
        for (int j = 1; j <= rowNum; j++) {
            String communityId = getCellValue(sheet.getRow(j).getCell(0));
            String userName =  getCellValue(sheet.getRow(j).getCell(1));
            String userNumber =  getCellValue(sheet.getRow(j).getCell(2));
            if (StringUtils.isNotEmpty(communityId) &&
                    StringUtils.isNotEmpty(userName)) {
                VirtualUserDTO v = new VirtualUserDTO();
                v.setCommunityId(communityId);
                v.setUserName(userName);
                v.setUserNumber(userNumber);
                virtualUserDTOs.add(v);
            }
        }
        dealData(virtualUserDTOs,fileName);
    }

    private String getCellValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC: {
                    short format = cell.getCellStyle().getDataFormat();
                    if(format == 14 || format == 31 || format == 57 || format == 58){   //excel中的时间格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellvalue = sdf.format(date);
                    }
                    // 判断当前的cell是否为Date
                    else if (HSSFDateUtil.isCellDateFormatted(cell)) {  //先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
                        // 如果是Date类型则，取得该Cell的Date值           // 对2014-02-02格式识别不出是日期格式
                        Date date = cell.getDateCellValue();
                        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue= formater.format(date);
                    } else { // 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = NumberToTextConverter.toText(cell.getNumericCellValue());

                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue().replaceAll("'", "''");
                    break;
                case  HSSFCell.CELL_TYPE_BLANK:
                    cellvalue = null;
                    break;
                // 默认的Cell值
                default:{
                    cellvalue = " ";
                }
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public void exportUserTemplate(HttpServletResponse response) {
        String sheetName = "学生录入模板";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");

        cell = row.createCell(1);
        cell.setCellValue("用户学号");

        String fileName = sheetName + ".xls";
        HSSFUtils.exportExcel(response, wb, fileName);
    }

    public void exportUserControl(ObjectId communityId, HttpServletResponse response) {
        List<VirtualUserEntry> virtualUserEntries = virtualUserDao.getAllVirtualUsers(communityId);
        String sheetName = "学生录入模板";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("社区Id");

        cell = row.createCell(1);
        cell.setCellValue("用户名");

        cell = row.createCell(2);
        cell.setCellValue("用户学号");

        int rowLine = 1;

        HSSFRow rowItem;
        HSSFCell cellItem;

        for (VirtualUserEntry userEntry : virtualUserEntries) {
            rowItem = sheet.createRow(rowLine);

            cellItem = rowItem.createCell(0);
            cellItem.setCellValue(userEntry.getCommunityId().toString());

            cellItem = rowItem.createCell(1);
            cellItem.setCellValue(userEntry.getUserName());

            cellItem = rowItem.createCell(2);
            cellItem.setCellValue(userEntry.getUserNumber());
            rowLine++;
        }
        String fileName = sheetName + ".xls";
        HSSFUtils.exportExcel(response, wb, fileName);
    }


    public void exportTemplate(ObjectId examGroupDetailId, HttpServletResponse response) {
        List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScores(examGroupDetailId, -1, -1, 1);
        GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        if (null != detailEntry) {
            String sheetName = detailEntry.getExamName() + "录入模板";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFRow row = sheet.createRow(0);


            HSSFCell cell = row.createCell(0);
            cell.setCellValue("关键字Id");

            cell = row.createCell(1);
            cell.setCellValue("考试Id");

            cell = row.createCell(2);
            cell.setCellValue("用户姓名");

            cell = row.createCell(3);
            cell.setCellValue("用户学号");

            cell = row.createCell(4);
            cell.setCellValue("等第分值");

            cell = row.createCell(5);
            cell.setCellValue("考试分值");

            int rowLine = 1;

            HSSFRow rowItem;
            HSSFCell cellItem;
            for (GroupExamUserRecordDTO recordDTO : recordDTOs) {

                rowItem = sheet.createRow(rowLine);

                cellItem = rowItem.createCell(0);
                cellItem.setCellValue(recordDTO.getId());

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue(recordDTO.getGroupExamDetailId());

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue(recordDTO.getUserName());

                cellItem = rowItem.createCell(3);
                cellItem.setCellValue(recordDTO.getUserNumber());

                if (detailEntry.getRecordScoreType() == Constant.ONE) {
                    cellItem = rowItem.createCell(4);
                    cellItem.setCellValue(-1);

                    cellItem = rowItem.createCell(5);
                    cellItem.setCellValue("");
                } else {
                    cellItem = rowItem.createCell(4);
                    cellItem.setCellValue("");

                    cellItem = rowItem.createCell(5);
                    cellItem.setCellValue(-1);
                }
                rowLine++;
            }

            String fileName = sheetName + ".xls";
            HSSFUtils.exportExcel(response, wb, fileName);
        }
    }


    public void importTemplate(InputStream inputStream) throws Exception {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));
        int rowNum = sheet.getLastRowNum();
        List<GroupExamUserRecordDTO> examScoreDTOs = new ArrayList<GroupExamUserRecordDTO>();
        for (int j = 1; j <= rowNum; j++) {
            GroupExamUserRecordDTO item = new GroupExamUserRecordDTO();
            String id = sheet.getRow(j).getCell(0).getStringCellValue();
            String groupExamDetailId = sheet.getRow(j).getCell(1).getStringCellValue();
            item.setGroupExamDetailId(groupExamDetailId);
            item.setId(id);
            HSSFCell cell = sheet.getRow(j).getCell(5);
            double score = getValue(cell);
            item.setScore(score);
            HSSFCell hssfCell = sheet.getRow(j).getCell(4);
            int scoreLevel = (new Double(getValue(hssfCell))).intValue();
            item.setScoreLevel(scoreLevel);
            item.setRank(Constant.ZERO);
            examScoreDTOs.add(item);
        }
        if (examScoreDTOs.size() > 0) {
            saveRecordExamScore(examScoreDTOs, Constant.ZERO);
            increaseVersion(new ObjectId(examScoreDTOs.get(0).getGroupExamDetailId()));
        }
    }

    public double getValue(HSSFCell cell) {
        double cellValue = -1;
        if (cell != null) {
            String vvv = getStringCellValue(cell);
            if (StringUtils.isNotBlank(vvv)) {
                cellValue = getValueByPrint(vvv);
            }
        }
        return cellValue;
    }

    /**
     * A+:100 A:99 A-:98 B+:97 B:96 B-:95 C+:94 C:93 C-:92 D+:91 D:90 D-:89
     */
    public double getValueByPrint(String levelScore) {
        double cellValue = -1;
        if (levelScore.equals("A+")) {
            cellValue = 100;
        } else if (levelScore.equals("A")) {
            cellValue = 99;
        } else if (levelScore.equals("A-")) {
            cellValue = 98;
        } else if (levelScore.equals("B+")) {
            cellValue = 97;
        } else if (levelScore.equals("B")) {
            cellValue = 96;
        } else if (levelScore.equals("B-")) {
            cellValue = 95;
        } else if (levelScore.equals("B-")) {
            cellValue = 95;
        } else if (levelScore.equals("C+")) {
            cellValue = 94;
        } else if (levelScore.equals("C")) {
            cellValue = 93;
        } else if (levelScore.equals("C-")) {
            cellValue = 92;
        } else if (levelScore.equals("D+")) {
            cellValue = 91;
        } else if (levelScore.equals("D")) {
            cellValue = 90;
        } else if (levelScore.equals("D-")) {
            cellValue = 89;
        } else{
            cellValue = Double.valueOf(levelScore);
        }
        return cellValue;
    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) return Constant.EMPTY;
        String strCell;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }

        return org.apache.commons.lang.StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }



    public void generateReportCardSign(){
        int page=1;
        int pageSize=200;
        boolean flag=true;
        reportCardSignDao.removeOldData();
        while(flag){
            List<GroupExamUserRecordEntry> entries = groupExamUserRecordDao.getEntriesByStatus(page,pageSize);
            if(entries.size()>0){
                Map<ObjectId,ObjectId> groupCommunityMap=new HashMap<ObjectId, ObjectId>();
                Map<ObjectId,List<GroupExamUserRecordEntry>> recordMap = new HashMap<ObjectId, List<GroupExamUserRecordEntry>>();
                for(GroupExamUserRecordEntry recordEntry:entries){
                     ObjectId groupExamDetailId=recordEntry.getGroupExamDetailId();
                     groupCommunityMap.put(groupExamDetailId,recordEntry.getCommunityId());
                     if(null!=recordMap.get(groupExamDetailId)){
                         List<GroupExamUserRecordEntry> userRecordEntries =recordMap.get(groupExamDetailId);
                         userRecordEntries.add(recordEntry);
                         recordMap.put(groupExamDetailId,userRecordEntries);
                     }else{
                         List<GroupExamUserRecordEntry> userRecordEntries =new ArrayList<GroupExamUserRecordEntry>();
                         userRecordEntries.add(recordEntry);
                         recordMap.put(groupExamDetailId,userRecordEntries);
                     }
                }
                for(Map.Entry<ObjectId,List<GroupExamUserRecordEntry>> item:recordMap.entrySet()){
                    ObjectId groupExamDetailId=item.getKey();
                    ObjectId communityId=groupCommunityMap.get(groupExamDetailId);
                    List<GroupExamUserRecordEntry> userRecordEntries=item.getValue();
                    List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao
                            .getStudentIdListByCommunityId(communityId);
                    Map<ObjectId,ObjectId> bindUserMap = new HashMap<ObjectId, ObjectId>();
                    for(NewVersionCommunityBindEntry bindEntry:bindEntries){
                        bindUserMap.put(bindEntry.getUserId(),bindEntry.getMainUserId());
                    }
                    for(GroupExamUserRecordEntry recordEntry:userRecordEntries){
                        ObjectId userId=recordEntry.getUserId();
                        if(null!=bindUserMap.get(userId)){
                            ObjectId mainUserId=bindUserMap.get(userId);
                            int type=Constant.ZERO;
                            if(recordEntry.getStatus()==Constant.THREE){
                                type=Constant.THREE;
                            }
                            ReportCardSignEntry signEntry = new ReportCardSignEntry(
                                    mainUserId,
                                    groupExamDetailId,
                                    recordEntry.getID(),
                                    type,
                                    recordEntry.getID().getTime()
                                    );
                            reportCardSignDao.saveEntry(signEntry);
                        }
                    }
                }

            }else{
                flag=false;
            }
            page++;
        }
    }


}
