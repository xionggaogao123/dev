package com.fulaan.reportCard.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.reportCard.*;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.fulaan.reportCard.dto.GroupExamUserRecordDTO;
import com.fulaan.reportCard.dto.RecordLevelEnum;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.reportCard.*;
import com.pojo.user.UserEntry;
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

    private GroupExamUserRecordDao groupExamUserRecordDao = new GroupExamUserRecordDao();


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


    public List<GroupExamUserRecordDTO> searchRecordStudentScores(ObjectId groupExamDetailId){
        List<GroupExamUserRecordDTO> recordExamScoreDTOs=new ArrayList<GroupExamUserRecordDTO>();
        List<GroupExamUserRecordEntry> recordEntries=groupExamUserRecordDao.getExamUserRecordEntries(groupExamDetailId);
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(GroupExamUserRecordEntry recordEntry:recordEntries){
            userIds.add(recordEntry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(GroupExamUserRecordEntry recordEntry:recordEntries){
            GroupExamUserRecordDTO userRecordDTO=new GroupExamUserRecordDTO(recordEntry);
            UserEntry userEntry=userEntryMap.get(recordEntry.getUserId());
            if(null!=userEntry){
                userRecordDTO.setUserName(userEntry.getUserName());
            }
            recordExamScoreDTOs.add(userRecordDTO);
        }
        return recordExamScoreDTOs;
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
                    detailDTO.setChildrenName(userEntry.getUserName());
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
        for(GroupExamDetailEntry examDetailEntry:entries){
            communityIds.add(examDetailEntry.getCommunityId());
        }
        Map<ObjectId, CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
        for(GroupExamDetailEntry examDetailEntry:entries){
            GroupExamDetailDTO detailDTO=new GroupExamDetailDTO(examDetailEntry);
            detailDTO.setUnSignCount(detailDTO.getSignCount()-detailDTO.getSignedCount());
            CommunityEntry communityEntry=communityEntryMap.get(examDetailEntry.getCommunityId());
            if (null!=communityEntry){
                detailDTO.setGroupName(communityEntry.getCommunityName());
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
                            return 1;
                        }else if(o1.getScore()==o2.getScore()){
                            return 0;
                        }else{
                            return -1;
                        }
                    }
                });
            }else{
                Collections.sort(examScoreDTOs, new Comparator<GroupExamUserRecordDTO>() {
                    @Override
                    public int compare(GroupExamUserRecordDTO o1, GroupExamUserRecordDTO o2) {
                        if(o1.getScoreLevel()>o2.getScoreLevel()){
                            return 1;
                        }else if(o1.getScoreLevel()==o2.getScoreLevel()){
                            return 0;
                        }else{
                            return -1;
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
                double avgScore=totalScore/totalCount;
                double excellentPercent=(double)excellentCount/(double)totalCount;
                double qualifyPercent=(double)qualifyCount/(double)totalCount;
                double unQualifyPercent=(double)unQualifyCount/(double)totalCount;
                RecordScoreEvaluateEntry entry=new RecordScoreEvaluateEntry(new ObjectId(groupExamDetailId),
                        excellentPercent, qualifyPercent, unQualifyPercent, avgScore, maxScore, minScore);
                recordScoreEvaluateDao.saveRecordScoreEvaluateEntry(entry);
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
                double aPercent=(double)aCount/(double)totalCount;
                double bPercent=(double)bCount/(double)totalCount;
                double cPercent=(double)cCount/(double)totalCount;
                double dPercent=(double)dCount/(double)totalCount;
                RecordLevelEvaluateEntry evaluateEntry=new RecordLevelEvaluateEntry(
                        new ObjectId(groupExamDetailId),aPercent,bPercent,cPercent,dPercent);
                recordLevelEvaluateDao.saveRecordLevelEvaluate(evaluateEntry);
            }
        }
    }

    public GroupExamDetailDTO getGroupExamDetail(ObjectId singleId){
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        GroupExamUserRecordEntry examUserRecordEntry=groupExamUserRecordDao.getGroupExamUserRecordEntry(singleId);
        if(null!=examUserRecordEntry){
            ObjectId groupExamDetailId=examUserRecordEntry.getGroupExamDetailId();
            detailDTO=getTeacherGroupExamDetail(groupExamDetailId);
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

}
