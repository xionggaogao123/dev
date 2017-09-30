package com.fulaan.reportCard.service;

import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.reportCard.GroupExamDetailDao;
import com.db.reportCard.RecordExamScoreDao;
import com.db.reportCard.RecordLevelEvaluateDao;
import com.db.reportCard.RecordScoreEvaluateDao;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.fulaan.reportCard.dto.RecordExamScoreDTO;
import com.fulaan.reportCard.dto.RecordLevelEnum;
import com.fulaan.reportCard.dto.UserRecordDTO;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.pojo.reportCard.RecordExamScoreEntry;
import com.pojo.reportCard.RecordLevelEvaluateEntry;
import com.pojo.reportCard.RecordScoreEvaluateEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/9/30.
 */
@Service
public class ReportCardService {

    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();
    private RecordExamScoreDao recordExamScoreDao=new RecordExamScoreDao();
    private RecordScoreEvaluateDao recordScoreEvaluateDao=new RecordScoreEvaluateDao();
    private RecordLevelEvaluateDao recordLevelEvaluateDao=new RecordLevelEvaluateDao();
    private NewVersionCommunityBindDao newVersionCommunityBindDao=new NewVersionCommunityBindDao();


    /**
     * 签字的功能
     * @param id
     * @param userId
     */
    public void pushSign(ObjectId id,
                         ObjectId userId
                         ){
        groupExamDetailDao.pushSign(id, userId);
    }


    /**
     * 获取我接受的成绩单列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public  List<GroupExamDetailDTO> getMyReceivedGroupExamDetailDTOs(
            ObjectId userId,int page,int pageSize){
        List<GroupExamDetailDTO> groupExamDetailDTOs=new ArrayList<GroupExamDetailDTO>();
        List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao
                .getEntriesByMainUserId(userId);
        List<ObjectId> communityIds=new ArrayList<ObjectId>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:bindEntries){
            communityIds.add(bindEntry.getCommunityId());
            userIds.add(bindEntry.getUserId());
        }
        List<GroupExamDetailEntry> entries=groupExamDetailDao.getMyReceivedGroupExamDetailEntries(userId,
                communityIds,userIds,
                page,pageSize);
        for(GroupExamDetailEntry examDetailEntry:entries){
            groupExamDetailDTOs.add(new GroupExamDetailDTO(examDetailEntry));
        }
        return groupExamDetailDTOs;
    }
    /**
     * 获取我发出的成绩单列表
     * @param userId
     * @return
     */
    public List<GroupExamDetailDTO> getMySendGroupExamDetailDTOs(
            ObjectId userId,int page,int pageSize){
        List<GroupExamDetailDTO> groupExamDetailDTOs=new ArrayList<GroupExamDetailDTO>();
        List<GroupExamDetailEntry> entries=groupExamDetailDao.getMySendGroupExamDetailEntries(userId,
                page,pageSize);
        for(GroupExamDetailEntry examDetailEntry:entries){
            GroupExamDetailDTO detailDTO=new GroupExamDetailDTO(examDetailEntry);
            List<UserRecordDTO> userRecordDTOs=detailDTO.getUserRecordDTOs();
            int signCount=0;
            int signedCount=0;
            for(UserRecordDTO dto:userRecordDTOs){
                if(dto.getStatus()==Constant.ZERO
                        ){
                    signCount++;
                }
                if(dto.getStatus()==Constant.TWO){
                    signCount++;
                    signedCount++;
                }
            }
            int unSignCount=signCount-signedCount;
            detailDTO.setSignCount(signCount);
            detailDTO.setUnSignCount(unSignCount);
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
                =newVersionCommunityBindDao.getAllStudentBindEntries(new ObjectId(communityId));
        Set<ObjectId> userIds=new HashSet<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:entries){
            userIds.add(bindEntry.getUserId());
        }
        List<UserRecordDTO> recordDTOs=new ArrayList<UserRecordDTO>();
        for(ObjectId uuId:userIds){
            recordDTOs.add(new UserRecordDTO(uuId.toString()));
        }
        dto.setUserRecordDTOs(recordDTOs);
        dto.setUserId(userId.toString());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        long examTime=dateFormat.parse(dto.getExamStrTime()).getTime();
        dto.setExamTime(examTime);
        groupExamDetailDao.saveGroupExamDetailEntry(dto.buildEntry());
    }

    /**
     * 保存成绩列表
     * @param examScoreDTOs
     */
    public void  saveRecordExamScore(List<RecordExamScoreDTO> examScoreDTOs,int status){
        if(examScoreDTOs.size()>0) {
            String groupExamDetailId = examScoreDTOs.get(0).getGroupExamDetailId();
            //删除以前的成绩
            recordExamScoreDao.removeAllRecordExamScore(new ObjectId(groupExamDetailId));
            List<RecordExamScoreEntry> entries=new ArrayList<RecordExamScoreEntry>();
            for(RecordExamScoreDTO dto:examScoreDTOs){
                entries.add(dto.buildEntry());
            }
            //保存进去
            recordExamScoreDao.saveEntries(entries);
            //数据分析:分为两种
            //一是分值分析 二是等第分析
            groupExamDetailDao.updateGroupExamDetailEntry(new ObjectId(groupExamDetailId),status);
            GroupExamDetailEntry detailEntry=groupExamDetailDao.getEntryById(new ObjectId(groupExamDetailId));
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
                Collections.sort(examScoreDTOs, new Comparator<RecordExamScoreDTO>() {
                    @Override
                    public int compare(RecordExamScoreDTO o1, RecordExamScoreDTO o2) {
                        if(o1.getScore()>o2.getScore()){
                            return 1;
                        }else if(o1.getScore()==o2.getScore()){
                            return 0;
                        }else{
                           return -1;
                        }
                    }
                });
                double maxScore=examScoreDTOs.get(0).getScore();
                double minScore=examScoreDTOs.get(examScoreDTOs.size()-1).getScore();
                for(RecordExamScoreDTO dto:examScoreDTOs){
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
                for(RecordExamScoreDTO dto:examScoreDTOs){
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

}
