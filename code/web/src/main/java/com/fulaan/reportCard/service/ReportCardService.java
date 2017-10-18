package com.fulaan.reportCard.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.reportCard.*;
import com.db.wrongquestion.ExamTypeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.reportCard.dto.*;
import com.fulaan.user.service.UserService;
import com.fulaan.wrongquestion.dto.ExamTypeDTO;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.reportCard.*;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/9/30.
 */
@Service
public class ReportCardService {

    @Autowired
    private UserService userService;

    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();
    private RecordScoreEvaluateDao recordScoreEvaluateDao=new RecordScoreEvaluateDao();
    private RecordLevelEvaluateDao recordLevelEvaluateDao=new RecordLevelEvaluateDao();
    private NewVersionCommunityBindDao newVersionCommunityBindDao=new NewVersionCommunityBindDao();
    private CommunityDao communityDao=new CommunityDao();

    private ExamTypeDao examTypeDao=new ExamTypeDao();

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();

    private GroupExamVersionDao groupExamVersionDao=new GroupExamVersionDao();

    private SubjectClassDao subjectClassDao=new SubjectClassDao();




    public static void main(String[] args)throws Exception{
        ReportCardService reportCardService=new ReportCardService();
        /**
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        detailDTO.setGroupId("59c32cc6670ab23fb82dc4ae");
        detailDTO.setCommunityId("59c32cc5670ab23fb82dc4ac");
        detailDTO.setExamType("59e415a2bf2e7917683d7354");
        detailDTO.setRecordScoreType(1);
        detailDTO.setExamName("测试第一周数据");
        detailDTO.setSubjectId("59b5fc0fbf2e791bb445cdb7");
        detailDTO.setMaxScore(100);
        detailDTO.setQualifyScore(60);
        detailDTO.setExcellentScore(80);
        detailDTO.setExamStrTime("2017-10-18");
        ObjectId userId=new ObjectId("59c32c8c670ab23fb82dc49a");
        reportCardService.saveGroupExamDetail(detailDTO,userId);
         **/
        /**-----------添加学生成绩---------**/
 /**
        List<GroupExamUserRecordDTO> examGroupUserScoreDTOs=new ArrayList<GroupExamUserRecordDTO>();
        GroupExamUserRecordDTO dto1=new GroupExamUserRecordDTO();
        dto1.setId("59e5669b2675642a3c1f736d");
        dto1.setScore(85);
        dto1.setScoreLevel(-1);
        dto1.setGroupExamDetailId("59e5669b2675642a3c1f736c");
        examGroupUserScoreDTOs.add(dto1);
        GroupExamUserRecordDTO dto2=new GroupExamUserRecordDTO();
        dto2.setId("59e5669b2675642a3c1f736e");
        dto2.setScore(75);
        dto2.setScoreLevel(-1);
        dto2.setGroupExamDetailId("59e5669b2675642a3c1f736c");
        examGroupUserScoreDTOs.add(dto2);
        GroupExamUserRecordDTO dto3=new GroupExamUserRecordDTO();
        dto3.setId("59e5669b2675642a3c1f736f");
        dto3.setScore(55);
        dto3.setScoreLevel(-1);
        dto3.setGroupExamDetailId("59e5669b2675642a3c1f736c");
        examGroupUserScoreDTOs.add(dto3);
        GroupExamUserRecordDTO dto4=new GroupExamUserRecordDTO();
        dto4.setId("59e5669b2675642a3c1f7370");
        dto4.setScore(40);
        dto4.setScoreLevel(-1);
        dto4.setGroupExamDetailId("59e5669b2675642a3c1f736c");
        examGroupUserScoreDTOs.add(dto4);
        GroupExamUserRecordDTO dto5=new GroupExamUserRecordDTO();
        dto5.setId("59e5669b2675642a3c1f7371");
        dto5.setScore(60);
        dto5.setScoreLevel(-1);
        dto5.setGroupExamDetailId("59e5669b2675642a3c1f736c");
        examGroupUserScoreDTOs.add(dto5);
        int status=2;
        reportCardService.saveRecordExamScore(examGroupUserScoreDTOs,status);
         **/
       /**----------生成版本号--------------**/
        GroupExamVersionEntry versionEntry=new GroupExamVersionEntry(new ObjectId("59e5669b2675642a3c1f736c"),1);
        GroupExamVersionDao groupExamVersionDao=new GroupExamVersionDao();
        groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
    }


    /**
     * 签字的功能
     * @param groupExamDetailId
     * @param userId
     */
    public void pushSign(ObjectId groupExamDetailId,
                         ObjectId userId
                         ){
        GroupExamUserRecordEntry recordEntry=groupExamUserRecordDao.getUserRecordEntry(groupExamDetailId, userId);
        if(null==recordEntry) {
            groupExamUserRecordDao.pushSign(groupExamDetailId, userId);
            groupExamDetailDao.updateSignedCount(groupExamDetailId);
        }
    }

    /**
     * 删除成绩单
     * @param id
     */
    public void removeGroupExamDetailEntry(ObjectId id){
        groupExamDetailDao.removeGroupExamDetailEntry(id);
        groupExamUserRecordDao.updateGroupExamDetailStatus(id,Constant.ONE);
    }


    public void sendGroupExam(ObjectId groupExamDetailId){
        groupExamDetailDao.updateGroupExamDetailEntry(groupExamDetailId,Constant.TWO);
        groupExamUserRecordDao.updateGroupExamDetailStatus(groupExamDetailId,Constant.TWO);
    }

    public GroupExamVersionDTO getExamGroupVersion(ObjectId groupExamDetailId)throws Exception{
        GroupExamVersionEntry entry=groupExamVersionDao.getVersionByGroupExamDetailId(groupExamDetailId);
        if(null!=entry){
            return new GroupExamVersionDTO(entry);
        }else{
            throw new Exception("传入的考试参数有误");
        }
    }


    public List<GroupExamUserRecordDTO> searchRecordStudentScores(ObjectId groupExamDetailId){
        List<GroupExamUserRecordDTO> recordExamScoreDTOs=new ArrayList<GroupExamUserRecordDTO>();
        final List<GroupExamUserRecordEntry> recordEntries=groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(GroupExamUserRecordEntry recordEntry:recordEntries){
            userIds.add(recordEntry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=new HashMap<ObjectId, UserEntry>();
        Map<ObjectId,NewVersionCommunityBindEntry> bindUserMap=new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        if(userIds.size()>0) {
            userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            bindUserMap=newVersionCommunityBindDao.getUserEntryMapByCondition(
                    recordEntries.get(0).getCommunityId(),new ArrayList<ObjectId>(userIds));
        }
        boolean flag=false;
        for(GroupExamUserRecordEntry recordEntry:recordEntries){
            GroupExamUserRecordDTO userRecordDTO=new GroupExamUserRecordDTO(recordEntry);
            NewVersionCommunityBindEntry
                    entry=bindUserMap.get(recordEntry.getUserId());
            if(null==entry) {
                flag=true;
            }else{
                userRecordDTO.setUserNumber(entry.getNumber());
                if(StringUtils.isNotBlank(entry.getThirdName())){
                    userRecordDTO.setUserName(entry.getThirdName());
                }else{
                    flag=true;
                }
            }
            if(flag){
                UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                if (null != userEntry) {
                    userRecordDTO.setUserName(
                            StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());

                }
                flag=false;
            }
            recordExamScoreDTOs.add(userRecordDTO);
        }
        Collections.sort(recordExamScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
            @Override
            public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                int result=getCompareResult(o1.getUserNumber(),o2.getUserNumber());
                if(result==0){
                    result=getCompareResult(o1.getUserName(),o2.getUserName());
                }
                return result;
            }
        });
        return recordExamScoreDTOs;
    }


    public int getCompareResult(String itemOne, String itemTwo){
        int result=0;
        if(itemOne.length()>itemTwo.length()){
            result=-1;
        }else if(itemOne.length()<itemTwo.length()){
            result=1;
        }else{
            int length=itemOne.length();
            for(int i=0;i<length;i++){
                if(itemOne.charAt(i)>itemTwo.charAt(i)){
                    result=-1;
                }else if(itemOne.charAt(i)<itemTwo.charAt(i)){
                    result=1;
                }
            }
        }
        return result;
    }


    /**
     * 获取我接受的成绩单列表(学生)
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamDetailDTO> getReceiveExams(
            String subjectId,String examType,int status,
            ObjectId userId,int page,int pageSize
    ){
        ObjectId suId= StringUtils.isNotBlank(subjectId)?new ObjectId(subjectId):null;
        ObjectId examTypeId= StringUtils.isNotBlank(examType)?new ObjectId(examType):null;
        List<GroupExamUserRecordEntry> recordEntries=groupExamUserRecordDao.getStudentReceivedEntries(
                suId,examTypeId,status,userId, page, pageSize
        );
        return getGroupExamDetailDtos(recordEntries);
    }

    public List<GroupExamDetailDTO> getGroupExamDetailDtos(List<GroupExamUserRecordEntry> recordEntries){
        List<GroupExamDetailDTO> groupExamDetailDTOs=new ArrayList<GroupExamDetailDTO>();
        if(recordEntries.size()>0) {
            Set<ObjectId> groupExamIds = new HashSet<ObjectId>();
            Set<ObjectId> uIds = new HashSet<ObjectId>();
            Set<ObjectId> communityIds = new HashSet<ObjectId>();
            for (GroupExamUserRecordEntry
                    entry : recordEntries) {
                groupExamIds.add(entry.getGroupExamDetailId());
                uIds.add(entry.getUserId());
                communityIds.add(entry.getCommunityId());
            }
            Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(uIds, Constant.FIELDS);
            Map<ObjectId, GroupExamDetailEntry> examDetailEntryMap = groupExamDetailDao.getGroupExamDetailMap(new ArrayList<ObjectId>(groupExamIds));
            Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));

            List<ObjectId> mainUserIds=new ArrayList<ObjectId>();
            Map<ObjectId,UserEntry> mainUserEntryMap=new HashMap<ObjectId, UserEntry>();
            for(Map.Entry<ObjectId, GroupExamDetailEntry>
                    detailItem:examDetailEntryMap.entrySet()){
                mainUserIds.add(detailItem.getValue().getUserId());
            }
            if(mainUserIds.size()>0){
                mainUserEntryMap=userService.getUserEntryMap(mainUserIds,Constant.FIELDS);
            }
            for (GroupExamUserRecordEntry recordEntry : recordEntries) {
                ObjectId groupExamDetailId = recordEntry.getGroupExamDetailId();
                GroupExamDetailEntry detailEntry = examDetailEntryMap.get(groupExamDetailId);
                if (null != detailEntry) {
                    GroupExamDetailDTO detailDTO = new GroupExamDetailDTO(detailEntry);
                    UserEntry userEntry = userEntryMap.get(recordEntry.getUserId());
                    detailDTO.setChildUserName(userEntry.getUserName());
                    detailDTO.setChildUserId(recordEntry.getUserId().toString());
                    CommunityEntry communityEntry=communityEntryMap.get(recordEntry.getCommunityId());
                    if (null!=communityEntry){
                        detailDTO.setGroupName(communityEntry.getCommunityName());
                    }
                    detailDTO.setScore(recordEntry.getScore());
                    detailDTO.setScoreLevel(recordEntry.getScoreLevel());
                    UserEntry mainUserEntry=mainUserEntryMap.get(detailEntry.getUserId());
                    if(null!=mainUserEntry){
                        detailDTO.setUserName(mainUserEntry.getUserName());
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
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamDetailDTO> getParentReceivedGroupExamDetailDTOs(
            String subjectId,String examType,int status,
            ObjectId userId,int page,int pageSize){
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        ObjectId suId= StringUtils.isNotBlank(subjectId)?new ObjectId(subjectId):null;
        ObjectId examTypeId= StringUtils.isNotBlank(examType)?new ObjectId(examType):null;
        List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getEntriesByMainUserId(userId);
        for(NewVersionCommunityBindEntry bindEntry:bindEntries){
            userIds.add(bindEntry.getUserId());
        }
        List<GroupExamUserRecordEntry> userRecordEntries=groupExamUserRecordDao.getParentReceivedEntries(
                suId,examTypeId,status,userId,new ArrayList<ObjectId>(userIds),page,pageSize
        );

        return getGroupExamDetailDtos(userRecordEntries);
    }


    /**
     * 获取我发出的成绩单列表
     * @param userId
     * @return
     */
    public List<GroupExamDetailDTO> getMySendGroupExamDetailDTOs(
            String subjectId,String examType,int status,
            ObjectId userId,int page,int pageSize){
        List<GroupExamDetailDTO> groupExamDetailDTOs=new ArrayList<GroupExamDetailDTO>();
        ObjectId suId= StringUtils.isNotBlank(subjectId)?new ObjectId(subjectId):null;
        ObjectId examTypeId=StringUtils.isNotBlank(examType)?new ObjectId(examType):null;
        List<GroupExamDetailEntry> entries=groupExamDetailDao.getMySendGroupExamDetailEntries(
                suId,examTypeId,status,userId,
                page,pageSize);
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        Set<ObjectId> examTypeIds=new HashSet<ObjectId>();
        for(GroupExamDetailEntry examDetailEntry:entries){
            communityIds.add(examDetailEntry.getCommunityId());
            examTypeIds.add(examDetailEntry.getExamType());
        }
        Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
        Map<ObjectId,ExamTypeEntry> examTypeEntryMap=examTypeDao.getExamTypeEntryMap(new ArrayList<ObjectId>(examTypeIds));
        for(GroupExamDetailEntry examDetailEntry:entries){
            GroupExamDetailDTO detailDTO=new GroupExamDetailDTO(examDetailEntry);
            detailDTO.setUnSignCount(detailDTO.getSignCount()-detailDTO.getSignedCount());
            CommunityEntry communityEntry=communityEntryMap.get(examDetailEntry.getCommunityId());
            if (null!=communityEntry){
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            ExamTypeEntry examTypeEntry=examTypeEntryMap.get(examDetailEntry.getExamType());
            if(null!=examTypeEntry){
                detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
            }
            if(examDetailEntry.getRecordScoreType()==Constant.ONE){
                RecordScoreEvaluateEntry evaluateEntry=recordScoreEvaluateDao.getEntryById(examDetailEntry.getID());
                if(null!=evaluateEntry){
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                }
            }else{
                RecordLevelEvaluateEntry levelEvaluateEntry=recordLevelEvaluateDao.getRecordLevelEvaluateEntry(examDetailEntry.getID());
                if(null!=levelEvaluateEntry){
                    detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                }
            }
            groupExamDetailDTOs.add(detailDTO);
        }
        return groupExamDetailDTOs;
    }

    /**
     * 保存新建的考试
     * @param dto
     * @param userId
     * @throws Exception
     */
    public void  saveGroupExamDetail(GroupExamDetailDTO dto, ObjectId userId)throws Exception{
        String communityId=dto.getCommunityId();
        List<NewVersionCommunityBindEntry> entries
                =newVersionCommunityBindDao.getStudentIdListByCommunityId(new ObjectId(communityId));
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:entries){
            userIds.add(bindEntry.getUserId());
        }
        dto.setUserId(userId.toString());
        dto.setSignedCount(Constant.ZERO);
        dto.setSignCount(Constant.ZERO);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        long examTime=dateFormat.parse(dto.getExamStrTime()).getTime();
        dto.setExamTime(examTime);
        ObjectId groupExamDetailId=groupExamDetailDao.saveGroupExamDetailEntry(dto.buildEntry());
        List<GroupExamUserRecordEntry> userRecordEntries=new ArrayList<GroupExamUserRecordEntry>();
        for(ObjectId uId:userIds){
            userRecordEntries.add(new GroupExamUserRecordEntry(
                    groupExamDetailId,
                    userId,
                    uId,
                    new ObjectId(dto.getGroupId()),
                    new ObjectId(dto.getExamType()),
                    new ObjectId(dto.getSubjectId()),
                    new ObjectId(dto.getCommunityId()),
                    -1D,
                    -1,
                    0,
                    Constant.ZERO
            ));
        }
        groupExamUserRecordDao.saveEntries(userRecordEntries);
        groupExamDetailDao.updateSignCount(groupExamDetailId,userIds.size());
        GroupExamVersionEntry versionEntry=new GroupExamVersionEntry(groupExamDetailId,1L);
        groupExamVersionDao.saveGroupExamVersionEntry(versionEntry);
    }

    public void updateVersion(ObjectId groupExamDetailId,
                              long version){
        groupExamVersionDao.updateVersionByGroupExamDetailId(groupExamDetailId,version);
    }

    /**
     * 保存成绩列表
     * @param examScoreDTOs
     */
    public void saveRecordExamScore(List<GroupExamUserRecordDTO> examScoreDTOs, int status){
        if(examScoreDTOs.size()>0) {
            String groupExamDetailId = examScoreDTOs.get(0).getGroupExamDetailId();
            for(GroupExamUserRecordDTO dto:examScoreDTOs){
                groupExamUserRecordDao.updateGroupExamUserRecordScore(new ObjectId(dto.getId()),
                        dto.getScore(),dto.getScoreLevel(),dto.getRank());
            }
            List<GroupExamUserRecordEntry> recordEntries=groupExamUserRecordDao.getExamUserRecordEntries(new ObjectId(groupExamDetailId));
            examScoreDTOs.clear();
            for(GroupExamUserRecordEntry entry:recordEntries){
                examScoreDTOs.add(new GroupExamUserRecordDTO(entry));
            }
            GroupExamDetailEntry detailEntry=groupExamDetailDao.getEntryById(new ObjectId(groupExamDetailId));
            if(detailEntry.getRecordScoreType()== Constant.ONE){
                Collections.sort(examScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if(o1.getScore()>o2.getScore()){
                            return -1;
                        }else if(o1.getScore()==o2.getScore()){
                            return 0;
                        }else{
                            return 1;
                        }
                    }
                });
            }else{
                Collections.sort(examScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if(o1.getScoreLevel()>o2.getScoreLevel()){
                            return -1;
                        }else if(o1.getScoreLevel()==o2.getScoreLevel()){
                            return 0;
                        }else{
                            return 1;
                        }
                    }
                });
            }
            int rank=1;
            for(GroupExamUserRecordDTO dto:examScoreDTOs){
                dto.setRank(rank);
                rank++;
            }
            for(GroupExamUserRecordDTO dto:examScoreDTOs){
                groupExamUserRecordDao.updateGroupExamUserRecordScore(new ObjectId(dto.getId()),
                        dto.getScore(),dto.getScoreLevel(),dto.getRank());
            }
            //数据分析:分为两种
            //一是分值分析 二是等第分析
            groupExamDetailDao.updateGroupExamDetailEntry(new ObjectId(groupExamDetailId),status);
            //更新状态
            groupExamUserRecordDao.updateGroupExamDetailStatus(new ObjectId(groupExamDetailId),status);
            if(detailEntry.getRecordScoreType()== Constant.ONE){
                double qualifyScore=detailEntry.getQualifyScore();
                double excellentScore=detailEntry.getExcellentScore();
                //所有有分值的总人数
                int totalCount=0;
                double totalScore=0D;
                //合格人数
                int qualifyCount=0;
                //优秀人数
                int excellentCount=0;
                //不及格人数
                int unQualifyCount=0;

                double maxScore=examScoreDTOs.get(0).getScore();
                double minScore=examScoreDTOs.get(examScoreDTOs.size()-1).getScore();
                for(GroupExamUserRecordDTO dto:examScoreDTOs){
                    double score=dto.getScore();
                    if(score!=-1){
                        totalCount++;
                        totalScore+=score;
                        if(score>=qualifyScore){
                            qualifyCount++;
                        }else{
                            unQualifyCount++;
                        }
                        if(score>excellentScore){
                            excellentCount++;
                        }
                    }
                }
                if(totalCount!=0) {
                    double avgScore = totalScore / totalCount;
                    double excellentPercent = (double) excellentCount / (double) totalCount;
                    double qualifyPercent = (double) qualifyCount / (double) totalCount;
                    double unQualifyPercent = (double) unQualifyCount / (double) totalCount;
                    RecordScoreEvaluateEntry entry = new RecordScoreEvaluateEntry(new ObjectId(groupExamDetailId),
                            excellentPercent, qualifyPercent, unQualifyPercent, avgScore, maxScore, minScore);
                    recordScoreEvaluateDao.saveRecordScoreEvaluateEntry(entry);
                }
            }else{
                int totalCount=0;
                int aCount=0;
                int bCount=0;
                int cCount=0;
                int dCount=0;
                for(GroupExamUserRecordDTO dto:examScoreDTOs){
                    int scoreLevel=dto.getScoreLevel();
                    if(scoreLevel!=-1){
                        totalCount++;
                        if(scoreLevel>= RecordLevelEnum.AP.getLevelScore()){
                            aCount++;
                        }else if(RecordLevelEnum.AP.getLevelScore()>
                                scoreLevel&&scoreLevel>=RecordLevelEnum.BP.getLevelScore()){
                            bCount++;
                        }else if(scoreLevel<RecordLevelEnum.BP.getLevelScore()
                                &&scoreLevel>=RecordLevelEnum.CP.getLevelScore()){
                            cCount++;
                        }else if(scoreLevel<RecordLevelEnum.CP.getLevelScore()
                                &&scoreLevel>=RecordLevelEnum.DP.getLevelScore()){
                            dCount++;
                        }
                    }
                }
                if(totalCount!=0) {
                    double aPercent = (double) aCount / (double) totalCount;
                    double bPercent = (double) bCount / (double) totalCount;
                    double cPercent = (double) cCount / (double) totalCount;
                    double dPercent = (double) dCount / (double) totalCount;
                    RecordLevelEvaluateEntry evaluateEntry = new RecordLevelEvaluateEntry(
                            new ObjectId(groupExamDetailId), aPercent, bPercent, cPercent, dPercent);
                    recordLevelEvaluateDao.saveRecordLevelEvaluate(evaluateEntry);
                }
            }
        }
    }

    public GroupExamDetailDTO getGroupExamDetail(ObjectId singleId){
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        GroupExamUserRecordEntry examUserRecordEntry=groupExamUserRecordDao.getGroupExamUserRecordEntry(singleId);
        if(null!=examUserRecordEntry){
            ObjectId groupExamDetailId=examUserRecordEntry.getGroupExamDetailId();
            detailDTO=getTeacherGroupExamDetail(groupExamDetailId);
            detailDTO.setSingleScoreId(singleId.toString());
            detailDTO.setChildUserId(examUserRecordEntry.getUserId().toString());
            UserEntry userEntry=userService.findById(examUserRecordEntry.getUserId());
            detailDTO.setChildUserName(StringUtils.isNotBlank(userEntry.getNickName())?
                    userEntry.getNickName():userEntry.getUserName()
            );
            if(detailDTO.getRecordScoreType()==Constant.ONE){
                detailDTO.setScore(examUserRecordEntry.getScore());
                detailDTO.setSingleRank(examUserRecordEntry.getRank());
            }else{
                detailDTO.setScoreLevel(examUserRecordEntry.getScoreLevel());
            }
        }
        return detailDTO;
    }

    public GroupExamDetailDTO getTeacherGroupExamDetail(ObjectId groupExamDetailId){
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        GroupExamDetailEntry detailEntry=groupExamDetailDao.getGroupExamDetailEntry(groupExamDetailId);
        if(null!=detailEntry){
            detailDTO=new GroupExamDetailDTO(detailEntry);
            SubjectClassEntry subjectClassEntry=subjectClassDao.getEntry(detailEntry.getSubjectId());
            if(null!=subjectClassEntry){
                detailDTO.setSubjectName(subjectClassEntry.getName());
            }
            UserEntry userEntry=userService.findById(detailEntry.getUserId());
            if(null!=userEntry){
                detailDTO.setUserName(StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            }
            CommunityEntry communityEntry=communityDao.findByObjectId(detailEntry.getCommunityId());
            if(null!=communityEntry){
                detailDTO.setGroupName(communityEntry.getCommunityName());
            }
            ExamTypeEntry examTypeEntry=examTypeDao.getEntry(detailEntry.getExamType());
            if(null!=examTypeEntry){
                detailDTO.setExamTypeName(examTypeEntry.getExamTypeName());
            }
            if(detailEntry.getRecordScoreType()==Constant.ONE){
                RecordScoreEvaluateEntry evaluateEntry=recordScoreEvaluateDao.getEntryById(groupExamDetailId);
                if(null!=evaluateEntry){
                    detailDTO.setExcellentPercent(evaluateEntry.getExcellentPercent());
                    detailDTO.setQualifyPercent(evaluateEntry.getQualifyPercent());
                    detailDTO.setUnQualifyPercent(evaluateEntry.getUnQualifyPercent());
                    detailDTO.setAvgScore(evaluateEntry.getAvgScore());
                    detailDTO.setGroupMaxScore(evaluateEntry.getMaxScore());
                }
            }else{
                RecordLevelEvaluateEntry levelEvaluateEntry=recordLevelEvaluateDao.getRecordLevelEvaluateEntry(groupExamDetailId);
                if(null!=levelEvaluateEntry){
                    detailDTO.setaPercent(levelEvaluateEntry.getApercent());
                    detailDTO.setbPercent(levelEvaluateEntry.getBpercent());
                    detailDTO.setcPercent(levelEvaluateEntry.getCpercent());
                    detailDTO.setdPercent(levelEvaluateEntry.getDpercent());
                }
            }
        }
        return detailDTO;
    }


    public List<ExamTypeDTO> getExamTypeDTOs(){
        List<ExamTypeDTO> examTypeDTOs=new ArrayList<ExamTypeDTO>();
        List<ExamTypeEntry> examTypeEntries=examTypeDao.getList();
        for(ExamTypeEntry examTypeEntry:examTypeEntries){
            examTypeDTOs.add(new ExamTypeDTO(examTypeEntry));
        }
        return examTypeDTOs;
    }

}
