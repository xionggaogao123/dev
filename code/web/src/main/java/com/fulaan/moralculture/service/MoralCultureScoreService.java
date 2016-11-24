package com.fulaan.moralculture.service;

import com.db.moralculture.MoralCultureScoreDao;
import com.fulaan.moralculture.dto.MoralCultureDTO;
import com.fulaan.moralculture.dto.MoralCultureManageDTO;
import com.fulaan.moralculture.dto.MoralCultureScore;
import com.fulaan.user.service.UserService;
import com.mongodb.DBObject;
import com.pojo.moralculture.MoralCultureScoreEntry;
import com.pojo.moralculture.MoralCultureScoreInfo;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserDetailInfoDTO;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by guojing on 2015/7/2.
 */
@Service
public class MoralCultureScoreService {
    private static final Logger logger= Logger.getLogger(MoralCultureScoreService.class);

    private MoralCultureScoreDao moralCultureScoreDao = new MoralCultureScoreDao();

    @Autowired
    private UserService userService;
    /**
     * 学生添加德育项目成绩
     * @param mcDto
     * @return
     */
    public ObjectId addOrEditMoralCultureScoreInfo(MoralCultureDTO mcDto) {
        List<MoralCultureScoreInfo> mcsiList=new ArrayList<MoralCultureScoreInfo>();
        List<MoralCultureScore> list=mcDto.getMoralCultureScores();
        for(MoralCultureScore item:list){
            MoralCultureScoreInfo moralCultureScoreInfo=new MoralCultureScoreInfo(
                    new ObjectId(item.getProjectId()),
                    item.getProjectScore(),
                    item.getCreateTime().getTime(),
                    item.getUpdateTime().getTime()
            );
            mcsiList.add(moralCultureScoreInfo);
        }
        MoralCultureScoreEntry entry=moralCultureScoreDao.selMoralCultureScoreEntry(mcDto.getUserId(),mcDto.getGradeId(),mcDto.getClassId(),mcDto.getSemesterId());
        if(entry!=null){
            List<DBObject> sublist =new ArrayList<DBObject>();
            for(MoralCultureScoreInfo mcsi:mcsiList) {
                /*MoralCultureScoreEntry mcse=moralCultureScoreDao.selMoralCultureScoreEntry(mcDto.getUserId(),mcDto.getGradeId(),mcDto.getClassId(),mcDto.getSemesterId(), mcsi.getProjectId());
                if(mcse!=null){
                    String projectScore=mcse.getMcsiList().get(0).getProjectScore();
                    if(!mcsi.getProjectScore().equals(projectScore)){
                        moralCultureScoreDao.updMoralCultureScoreEntry(mcDto.getUserId(),mcDto.getGradeId(),mcDto.getClassId(),mcDto.getSemesterId(),mcsi);
                    }
                }else{
                    moralCultureScoreDao.addMoralCultureScoreEntry2(mcDto.getUserId(),mcDto.getGradeId(),mcDto.getClassId(),mcDto.getSemesterId(),mcsi);
                }*/
                sublist.add(mcsi.getBaseEntry());
            }
            moralCultureScoreDao.updMoralCultureScoreEntry(entry.getUserId(), entry.getGradeId(), entry.getClassId(), entry.getSemesterId(), sublist);
            return entry.getID();
        }else {
            entry = new MoralCultureScoreEntry(
                    new ObjectId(mcDto.getUserId()),
                    new ObjectId(mcDto.getSchoolId()),
                    new ObjectId(mcDto.getGradeId()),
                    new ObjectId(mcDto.getClassId()),
                    mcDto.getSemesterId(),
                    mcsiList
            );
            ObjectId mcseId=moralCultureScoreDao.addMoralCultureScoreEntry(entry);
            return mcseId;
        }

    }

    public MoralCultureDTO selPersonalMoralCultureScore(String userId,String gradeId,String classId,String semesterId,List<MoralCultureManageDTO> proList){
        MoralCultureDTO dto=new MoralCultureDTO();
        MoralCultureScoreEntry entry=moralCultureScoreDao.selMoralCultureScoreEntry(userId,gradeId,classId,semesterId);
        if(entry!=null) {
            dto.setId(entry.getID().toString());
            dto.setUserId(entry.getUserId().toString());
            dto.setSchoolId(entry.getSchoolId().toString());
            dto.setGradeId(entry.getGradeId().toString());
            dto.setClassId(entry.getClassId().toString());
            dto.setSemesterId(entry.getSemesterId());
            List<MoralCultureScore> scores=new ArrayList<MoralCultureScore>();
            List<MoralCultureScoreInfo> mcsiList=entry.getMcsiList();
            for(MoralCultureManageDTO mcmDto:proList){
                int index=0;
                for(MoralCultureScoreInfo item: mcsiList){
                    if(mcmDto.getId().equals(item.getProjectId().toString())){
                        MoralCultureScore score=new MoralCultureScore();
                        score.setProjectId(item.getProjectId().toString());
                        score.setProjectScore(item.getProjectScore());
                        scores.add(score);
                        mcsiList.remove(index);
                        break;
                    }
                    index++;
                }
            }
            if(proList!=null&&proList.size()>0){
                int addLength=proList.size()-scores.size();
                for(int j=0;j<addLength;j++){
                    MoralCultureScore score = new MoralCultureScore();
                    score.setProjectScore("");
                    scores.add(score);
                }
            }
            dto.setMoralCultureScores(scores);
        }
        return dto;
    }

    public List<MoralCultureScore> selClassAvgMoralCultureScore(String gradeId,String classId,String semesterId,List<MoralCultureManageDTO> proList) {
        List<MoralCultureScore> scoreList=new ArrayList<MoralCultureScore>();
        List<MoralCultureScoreEntry> mcseList=moralCultureScoreDao.selClassMoralCultureScore(gradeId, classId, semesterId);

        Map<String,List<MoralCultureScore>> map=new HashMap<String, List<MoralCultureScore>>();

        for (MoralCultureScoreEntry mcse : mcseList) {
            List<MoralCultureScoreInfo> mcsiList=mcse.getMcsiList();
            for(MoralCultureScoreInfo item: mcsiList){
                if(!"".equals(item.getProjectScore())) {
                    List<MoralCultureScore> scores=map.get(item.getProjectId().toString());
                    if(scores==null) {
                        scores=new ArrayList<MoralCultureScore>();
                    }
                    MoralCultureScore score = new MoralCultureScore();
                    score.setProjectId(item.getProjectId().toString());
                    score.setProjectScore(item.getProjectScore());
                    scores.add(score);

                    map.put(item.getProjectId().toString(), scores);

                }
            }
        }
        for(MoralCultureManageDTO mcmDto:proList) {
            MoralCultureScore score = new MoralCultureScore();
            score.setProjectId(mcmDto.getId());
            List<MoralCultureScore> scores=map.get(mcmDto.getId());
            if(scores==null) {
                score.setProjectScore("");
            }else{
                float totalScore=0f;
                for(MoralCultureScore mcs: scores){
                    Pattern pattern = Pattern.compile("^\\d+\\.\\d+|\\d+$");
                    if(pattern.matcher(mcs.getProjectScore()).matches()){
                        totalScore+=Float.parseFloat(mcs.getProjectScore());
                    }
                }
                if(scores.size()>0){
                    float avgScore=totalScore/scores.size();
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    String projectScore=fnum.format(avgScore);
                    score.setProjectScore(projectScore);
                }else{
                    score.setProjectScore("");
                }
            }
            scoreList.add(score);
        }
        if(proList!=null&&proList.size()>0){
            for(int j=0;j<proList.size()-scoreList.size();j++){
                MoralCultureScore score = new MoralCultureScore();
                score.setProjectScore("");
                scoreList.add(score);
            }
        }
        return scoreList;
    }

    public List<MoralCultureDTO> selClassAllStudentScore(ClassEntry classEntry,String semesterId,List<MoralCultureManageDTO> proList) {
        String gradeId=classEntry.getGradeId().toString();
        String classId=classEntry.getID().toString();
        //取得班级下的全部学生id
        List<ObjectId> stuIds=classEntry.getStudents();

        List<UserDetailInfoDTO> userInfos=userService.findUserInfoByIds(stuIds);

        Map<String,UserDetailInfoDTO> map=new HashMap<String, UserDetailInfoDTO>();
        for(UserDetailInfoDTO item:userInfos){
            map.put(item.getId(),item);
        }

        List<MoralCultureScoreEntry> mcseList=moralCultureScoreDao.selClassMoralCultureScore(gradeId, classId, semesterId);
        Map<ObjectId,MoralCultureScoreEntry> mcseMap=new HashMap<ObjectId, MoralCultureScoreEntry>();
        for(MoralCultureScoreEntry mcse: mcseList){
            mcseMap.put(mcse.getUserId(),mcse);
        }
        List<MoralCultureDTO> scoreList=new ArrayList<MoralCultureDTO>();
        //在结果集中取出数量是size条数据
        for( int i=0;i<stuIds.size();i++){
            MoralCultureDTO mcDto=new MoralCultureDTO();
            UserDetailInfoDTO user=map.get(stuIds.get(i).toString());
            mcDto.setUserId(user.getId());
            mcDto.setUserName(user.getUserName());
            mcDto.setSemesterId(semesterId);
            mcDto.setGradeId(gradeId);
            mcDto.setClassId(classId);

            MoralCultureScoreEntry mcse=mcseMap.get(stuIds.get(i));
            if(mcse!=null) {
                mcDto.setId(mcse.getID().toString());
                List<MoralCultureScore> scores = new ArrayList<MoralCultureScore>();
                List<MoralCultureScoreInfo> mcsiList = mcse.getMcsiList();
                for (MoralCultureManageDTO mcmDto : proList) {
                    int index = 0;
                    for (MoralCultureScoreInfo item : mcsiList) {
                        if (mcmDto.getId().equals(item.getProjectId().toString())) {
                            MoralCultureScore score = new MoralCultureScore();
                            score.setProjectId(item.getProjectId().toString());
                            score.setProjectScore(item.getProjectScore());
                            scores.add(score);
                            mcsiList.remove(index);
                            break;
                        }
                        index++;
                    }
                }
                if(proList!=null&&proList.size()>0){
                    int addLength=proList.size()-scores.size();
                    for(int j=0;j<addLength;j++){
                        MoralCultureScore score = new MoralCultureScore();
                        score.setProjectScore("");
                        scores.add(score);
                    }
                }

                mcDto.setMoralCultureScores(scores);
                mcDto.setState(1);
                mcDto.setStateDesc("已提交");
            }else{
                List<MoralCultureScore> scores = new ArrayList<MoralCultureScore>();
                for (MoralCultureManageDTO mcmDto : proList) {
                    MoralCultureScore score = new MoralCultureScore();
                    score.setProjectScore("");
                    scores.add(score);
                }
                mcDto.setMoralCultureScores(scores);
                mcDto.setState(0);
                mcDto.setStateDesc("未提交");
            }
            scoreList.add(mcDto);
        }
        return scoreList;
    }
}
