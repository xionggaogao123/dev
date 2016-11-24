package com.fulaan.teacherevaluation.service;

import com.db.teacherevaluation.*;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.*;
import com.pojo.teacherevaluation.*;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.*;

/**
 * Created by fl on 2016/4/22.
 */
@Service
public class EvaluationItemService {

    private EvaluationItemDao evaluationItemDao = new EvaluationItemDao();
    private UserDao userDao = new UserDao();
    private MemberGroupDao memberGroupDao = new MemberGroupDao();
    private SettingDao settingDao = new SettingDao();
    private ProportionDao proportionDao = new ProportionDao();
    private ElementDao elementDao = new ElementDao();

    @Autowired
    private EvaluationService evaluationService;

    /**
     * 得到教师评价里的角色
     * @param evaluationId
     * @param role
     * @param userId
     * @return
     */
    public Map<String, Integer> formateEvaluationRole(ObjectId evaluationId, int role, ObjectId userId){
        Map<String, Integer> model = new HashMap<String, Integer>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<ObjectId> leaders = memberGroupEntry.getLeaders();
        List<ObjectId> members = memberGroupEntry.getMembers();
        model.put("leader", leaders.contains(userId) ? 1 : 0);
        model.put("manager", UserRole.isManager(role) | UserRole.isHeadmaster(role) ? 1 : 0);
        model.put("member", members.contains(userId) ? 1 : 0);
        model.put("teacher", 0);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teachers = teacherGroup.getGroupTeacherIds();
            if(teachers.contains(userId)){
                model.put("teacher", 1);
                break;
            }
        }
        Integer state = evaluationService.isHasUser(memberGroupEntry, userId);
        if(null == state){
            state = 0;
        }
        model.put("state", state);
        return model;
    }

    public String getRule(ObjectId evaluationId){
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        return settingEntry.getRule();
    }

    /**
     * 获取老师个人陈述
     * @param teacherId
     * @param evaluationId
     * @return
     */
    public String getTeacherStatement(ObjectId teacherId, ObjectId evaluationId){
        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, new BasicDBObject("stat", 1));
        return evaluationItemEntry.getStatement();
    }

    /**
     * 更新老师个人陈述
     * @param teacherId
     * @param evaluationId
     * @param statement
     */
    public void updateTeacherStatement(ObjectId teacherId, ObjectId evaluationId, String statement){
        getEvaluationItemEntry(teacherId, evaluationId, new BasicDBObject("tid", 1));//防止没有EvaluationItemEntry
        evaluationItemDao.updateEvaluationItem(teacherId, evaluationId, new BasicDBObject("stat", statement));
    }

    private EvaluationItemEntry getEvaluationItemEntry(ObjectId teacherId, ObjectId evaluationId, DBObject fields){
        EvaluationItemEntry evaluationItemEntry = evaluationItemDao.getEvaluationItem(teacherId, evaluationId, fields);
        if(evaluationItemEntry == null){
            evaluationItemEntry = new EvaluationItemEntry(evaluationId, teacherId);
            evaluationItemDao.addEvaluationItem(evaluationItemEntry);
        }
        return evaluationItemEntry;
    }

    /**
     * 获取老师实证资料
     * @param teacherId
     * @param evaluationId
     * @return
     */
    public Map<String, Object> getTeacherEvidenceAndLiangHuaScore(ObjectId teacherId, ObjectId evaluationId){
        Map<String, Object> map = new HashMap<String, Object>();
        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, new BasicDBObject("evi", 1));
        map.put("evidence", evaluationItemEntry.getEvidence());
        return map;
    }

    /**
     * 得到老师陈述、实证资料和分数
     * @param teacherId
     * @param evaluationId
     * @param userId
     * @param isLeader
     * @return
     */
    public Map<String, Object> getTeacherStatementAndEvidenceAndScore(ObjectId teacherId, ObjectId evaluationId, ObjectId userId, Boolean isLeader){
        Map<String, Object> map = new HashMap<String, Object>();
        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, Constant.FIELDS);
        map.put("evidence", evaluationItemEntry.getEvidence());
        map.put("statement", evaluationItemEntry.getStatement());
        map.put("huPingScores", new ArrayList<IdValuePairDTO1>());
        map.put("kaoHeScores", new ArrayList<IdValuePairDTO1>());
        List<EvaluationItemEntry.ElementScore> huPingScore =  evaluationItemEntry.getHuPingScore();
        if(huPingScore.size() > 0){
            for(EvaluationItemEntry.ElementScore elementScore : huPingScore) {
                ObjectId tid = elementScore.getEvaluateTeacherId();
                if(tid.equals(userId)){
                    List<IdNameValuePairDTO> scores = changePairToDTO(elementScore.getElementScores());
                    map.put("huPingScores", scores);
                    break;
                }

            }
        }
        List<EvaluationItemEntry.ElementScore> kaoHeScore =  isLeader ? evaluationItemEntry.getLeaderScore() : evaluationItemEntry.getGroupScore();
        if(kaoHeScore.size() > 0){
            for(EvaluationItemEntry.ElementScore elementScore : kaoHeScore) {
                ObjectId tid = elementScore.getEvaluateTeacherId();
                if(tid.equals(userId)){
                    List<IdNameValuePairDTO> scores = changePairToDTO(elementScore.getElementScores());
                    map.put("kaoHeScores", scores);
                    break;
                }

            }
        }
        return map;
    }

    /**
     * 得到老师个人陈述、实证资料
     * @param teacherId
     * @param evaluationId
     * @return
     */
    public Map<String, Object> getTeacherStatementAndEvidence(ObjectId teacherId, ObjectId evaluationId){
        Map<String, Object> map = new HashMap<String, Object>();
        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, Constant.FIELDS);
        map.put("evidence", evaluationItemEntry.getEvidence());
        map.put("statement", evaluationItemEntry.getStatement());
        return map;
    }

    /**
     * 更新老师实证资料
     * @param teacherId
     * @param evaluationId
     * @param evidence
     */
    public void updateTeacherEvidence(ObjectId teacherId, ObjectId evaluationId, String evidence){
        getEvaluationItemEntry(teacherId, evaluationId, new BasicDBObject("tid", 1));//防止没有EvaluationItemEntry
        DBObject updateValue = new BasicDBObject().append("evi", evidence);
        evaluationItemDao.updateEvaluationItem(teacherId, evaluationId, updateValue);
    }



    private Map<String, Object> getMyGroupTeachersItemEntryExceptMine(ObjectId teacherId, ObjectId evaluationId){
        Map<String, Object> map = new HashMap<String, Object>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        map.put("groupEntry", memberGroupEntry);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<ObjectId> teachers = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teacherIds = teacherGroup.getGroupTeacherIds();
            if(teacherIds.contains(teacherId)){
                teacherIds.remove(teacherId);
                teachers.addAll(teacherIds);
                map.put("num", teacherGroup.getNum());
                map.put("lnum", teacherGroup.getLiangNum());
                break;
            }
        }
        List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(teachers, evaluationId, Constant.FIELDS);
        map.put("itemEntries", itemEntries);
        return map;
    }

    private double[] getExcellntAndGoodPercent(ObjectId evaluationId) throws Exception{
        double[] pers = new double[2];
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        List<SettingEntry.GradeSetting>  gradeSettings = settingEntry.getGradeSettings();
        if(gradeSettings.size() < 2){
            throw new Exception("管理员还未设置优秀、良好分数段");
        }
        pers[0] = gradeSettings.get(0).getBegin() / 100;
        pers[1] = gradeSettings.get(1).getBegin() / 100;
        return pers;
    }

    /**
     * 得到本组老师名单
     * @param evaluationId
     * @param userId
     * @return
     */
    public Map<String, Object> getMyGroupTeachers(ObjectId evaluationId, ObjectId userId){
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupTeacherIds().contains(userId)){
                model.put("groupName", teacherGroup.getGroupName());
                List<ObjectId> teacherIds = teacherGroup.getSignGroupTeacherIds();
                Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(teacherIds, new BasicDBObject("nm", 1));

                List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(teacherIds, evaluationId, Constant.FIELDS);
                Map<ObjectId, EvaluationItemEntry> evaluationItemEntryMap = new HashMap<ObjectId, EvaluationItemEntry>();
                for(EvaluationItemEntry itemEntry : itemEntries){
                    evaluationItemEntryMap.put(itemEntry.getTeacherId(), itemEntry);
                }

                for(ObjectId teacherId : teacherIds){
                    if(!teacherId.equals(userId)) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", teacherId.toString());
                        map.put("name", userMap.get(teacherId).getUserName());
                        map.put("state", 0);
                        EvaluationItemEntry evaluationItemEntry = evaluationItemEntryMap.get(teacherId);
                        if (evaluationItemEntry != null) {
                            int state = checkHuPingState(evaluationItemEntry, userId);
                            map.put("state", state);
                        }

                        list.add(map);
                    }
                }
            }
        }
        model.put("list", list);
        return model;
    }

    /**
     * 获取老师互评详情
     * @param teacherId 被评价的老师
     * @param evaluationId
     * @param type 1:：互评 2：领导 3：组员
     * @return
     */
    public List<Map<String, Object>> getTeacherElementScoreList(ObjectId teacherId, ObjectId evaluationId, int type){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, Constant.FIELDS);
        List<EvaluationItemEntry.ElementScore> elementScores = type==1 ? evaluationItemEntry.getHuPingScore() :
                type==2 ? evaluationItemEntry.getLeaderScore() : evaluationItemEntry.getGroupScore();
        if(elementScores.size() > 0){
            List<ObjectId> teacherIds = new ArrayList<ObjectId>();
            for(EvaluationItemEntry.ElementScore elementScore : elementScores) {
                teacherIds.add(elementScore.getEvaluateTeacherId());
            }
            Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(teacherIds, new BasicDBObject("nm", 1));
            for(EvaluationItemEntry.ElementScore elementScore : elementScores) {
                Map<String, Object> map = new HashMap<String, Object>();
                ObjectId tid = elementScore.getEvaluateTeacherId();
                map.put("tid", tid.toString());
                UserEntry userEntry = userEntryMap.get(tid);
                map.put("tname", userEntry==null ? "" : userEntry.getUserName());
                List<IdNameValuePairDTO> scores = changePairToDTO(elementScore.getElementScores());
                map.put("scores", scores);
                list.add(map);
            }
        }
        return list;
    }
    
    private List<IdNameValuePairDTO> changePairToDTO(List<IdNameValuePair> pairs){
        List<IdNameValuePairDTO> pairDTOs = new ArrayList<IdNameValuePairDTO>();
        for (IdNameValuePair pair : pairs){
            pairDTOs.add(new IdNameValuePairDTO(pair));
        }
        return pairDTOs;
    }

    /**
     * 检查互评和考核打分人数限制
     * @param teacherId
     * @param evaluationId
     * @param userId
     * @param type 1：互评  2：考核打分
     * @param evaluationItemEntry
     * @return
     */
    private String checkYouXiuLiangHaoNumLimit(ObjectId teacherId, ObjectId evaluationId,ObjectId userId, int type, EvaluationItemEntry evaluationItemEntry) throws Exception{
        Map<ObjectId, ElementEntry> elementMap = new HashMap<ObjectId, ElementEntry>();
        Map<ObjectId, Integer> elementNumMap = new HashMap<ObjectId, Integer>();
        Map<ObjectId, Integer> elementLiangNumMap = new HashMap<ObjectId, Integer>();
        List<ElementEntry> elementEntries = elementDao.getElements(evaluationId, 1);
        for(ElementEntry elementEntry : elementEntries){
            elementMap.put(elementEntry.getID(), elementEntry);
            elementNumMap.put(elementEntry.getID(), 0);
            elementLiangNumMap.put(elementEntry.getID(), 0);
        }
        double[] pres = getExcellntAndGoodPercent(evaluationId);

        Map<String, Object> info = getMyGroupTeachersItemEntryExceptMine(teacherId, evaluationId);

        MemberGroupEntry memberGroupEntry = (MemberGroupEntry)info.get("groupEntry");
        List<ObjectId> leaders = memberGroupEntry.getLeaders();
        Boolean isLeader = leaders.contains(userId);

        List<EvaluationItemEntry> itemEntries = (List<EvaluationItemEntry>)info.get("itemEntries");
        itemEntries.add(evaluationItemEntry);
        for(EvaluationItemEntry itemEntry : itemEntries){
            List<EvaluationItemEntry.ElementScore> elementScores = type==1 ? itemEntry.getHuPingScore() : isLeader ? itemEntry.getLeaderScore() : itemEntry.getGroupScore();
            for(EvaluationItemEntry.ElementScore elementScore : elementScores){
                if(elementScore.getEvaluateTeacherId().equals(userId)){
                    List<IdNameValuePair> pairs = elementScore.getElementScores();
                    for(IdNameValuePair pair : pairs){
                        ObjectId id = pair.getId();
                        ElementEntry elementEntry = elementMap.get(id);
                        if((Double)pair.getValue() >= elementEntry.getElementScore() * pres[0]){
                            elementNumMap.put(id, elementNumMap.get(id) + 1);
                        } else if((Double)pair.getValue() >= elementEntry.getElementScore() * pres[1]){
                            elementLiangNumMap.put(id, elementLiangNumMap.get(id) + 1);
                        }
                    }
                }

            }
        }
        String msg = "";
        int num = (Integer)info.get("num");
        int lnum = (Integer)info.get("lnum");
        for(ElementEntry elementEntry : elementEntries){
            ObjectId id = elementEntry.getID();
            if(elementNumMap.get(id) > num){
                msg += elementEntry.getName() + "优秀人数超过上限" + num + "\t";
            }
            if(elementLiangNumMap.get(id) > lnum){
                msg += elementEntry.getName() + "良好人数超过上限" + lnum + "\t";
            }
        }

        return msg;
    }

    /**
     * 打分
     * @param evaluationId
     * @param scores
     * @param userId
     * @param tijiao  0保存  1提交
     * @param mode 考核模式  1打分  2等级
     * @throws Exception
     */
    public void updateEvaluationScore(ObjectId evaluationId, String scores, ObjectId userId, int tijiao, int mode) throws Exception{
        if("".equals(scores)){
            throw new Exception("还未有老师报名，不能打分");
        }
        List<ElementEntry> elementEntries = elementDao.getElements(evaluationId, 1);
        String[] teacherScores = scores.split(";");
        for(String teacherScore : teacherScores){
            String[] info = teacherScore.split(",");
            ObjectId teacherId = new ObjectId(info[0]);
            List<IdNameValuePair> elementScores = formateElementScores(elementEntries, info, tijiao, mode);
            EvaluationItemEntry.ElementScore elementScore = new EvaluationItemEntry.ElementScore(userId, tijiao, elementScores);
            updateTeacherEvaluationScore(teacherId, evaluationId, elementScore, userId);
        }
    }


    private List<IdNameValuePair> formateElementScores(List<ElementEntry> elementEntries, String[] info, int tijiao, int mode) throws Exception{
        List<IdNameValuePair> elementScores = new ArrayList<IdNameValuePair>();
        if(mode == 1){
            for(int i=1; i<info.length; i++){
                double score = Double.parseDouble(info[i]);
                if(tijiao == 1 && score == 0){
                    throw new Exception("还未完成打分，请完成打分");
                }
                elementScores.add(new IdNameValuePair(elementEntries.get(i-1).getID(), "Z", score));
            }
        } else if(mode == 2){
            for(int i=1; i<info.length; i++){
                String grade = info[i];
                if(tijiao == 1 && "Z".equals(grade)){
                    throw new Exception("还未完成打分，请完成打分");
                }
                elementScores.add(new IdNameValuePair(elementEntries.get(i-1).getID(), grade, 0.0));
            }
        }

        return elementScores;
    }


    /**
     * 合并后的考核打分
     * @param teacherId
     * @param evaluationId
     * @param elementScore
     * @param userId
     * @throws Exception
     */
    private void updateTeacherEvaluationScore(ObjectId teacherId, ObjectId evaluationId, EvaluationItemEntry.ElementScore elementScore, ObjectId userId) throws Exception{
        if(teacherId.equals(userId)){
            throw new Exception("用户不能对自己打分");
        }
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        EvaluationItemEntry evaluationItemEntry = evaluationItemDao.getEvaluationItem(teacherId, evaluationId, Constant.FIELDS);
        Boolean isTheSameGroup = isTheSameGroup(memberGroupEntry, teacherId, userId);
        if(isTheSameGroup){//评价老师和被评价老师在同一小组
            updateHuPingScore(evaluationId, teacherId, userId, evaluationItemEntry, elementScore);
        }
        if(memberGroupEntry.getLeaders().contains(userId)){//评价老师是校领导
            updateLeaderScore(evaluationId, teacherId, userId, evaluationItemEntry, elementScore);
        }
        if(memberGroupEntry.getMembers().contains(userId)){//评价老师在领导小组
            updateLeadMemberScore(evaluationId, teacherId, userId, evaluationItemEntry, elementScore);
        }
    }

    /**
     * 检查评价老师和被评价老师是否在同一小组
     * @param memberGroupEntry
     * @param teacherId
     * @param userId
     * @return
     */
    private Boolean isTheSameGroup(MemberGroupEntry memberGroupEntry, ObjectId teacherId, ObjectId userId){
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teachers = teacherGroup.getGroupTeacherIds();
            if(teachers.contains(teacherId) && teachers.contains(userId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 更新互评成绩
     * @param evaluationId
     * @param teacherId
     * @param userId
     * @param evaluationItemEntry
     * @param elementScore
     * @throws Exception
     */
    private void updateHuPingScore(ObjectId evaluationId, ObjectId teacherId, ObjectId userId, EvaluationItemEntry evaluationItemEntry,
                                   EvaluationItemEntry.ElementScore elementScore) throws Exception{
        List<EvaluationItemEntry.ElementScore> currentScore = evaluationItemEntry.getHuPingScore();
        List<EvaluationItemEntry.ElementScore> scoresAfterAppend = new ArrayList<EvaluationItemEntry.ElementScore>();
        for(EvaluationItemEntry.ElementScore score : currentScore){
            if(!score.getEvaluateTeacherId().equals(userId)){
                scoresAfterAppend.add(score);
            }
        }
        scoresAfterAppend.add(elementScore);
        evaluationItemEntry.setHuPingScore(scoresAfterAppend);
        String msg = checkYouXiuLiangHaoNumLimit(teacherId, evaluationId, userId, 1, evaluationItemEntry);
        if(!msg.equals("")){
            throw new Exception(msg);
        }
        DBObject updateValue = new BasicDBObject("hps", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoresAfterAppend)));
        evaluationItemDao.updateEvaluationItem(teacherId, evaluationId, updateValue);
    }

    /**
     * 更新领导的打分
     * @param evaluationId
     * @param teacherId
     * @param userId
     * @param evaluationItemEntry
     * @param elementScore
     * @throws Exception
     */
    private void updateLeaderScore(ObjectId evaluationId, ObjectId teacherId, ObjectId userId, EvaluationItemEntry evaluationItemEntry,
                                   EvaluationItemEntry.ElementScore elementScore) throws Exception{
        List<EvaluationItemEntry.ElementScore> currentScore = evaluationItemEntry.getLeaderScore();
        List<EvaluationItemEntry.ElementScore> scoresAfterAppend = new ArrayList<EvaluationItemEntry.ElementScore>();
        for(EvaluationItemEntry.ElementScore score : currentScore){
            if(!score.getEvaluateTeacherId().equals(userId)){
                scoresAfterAppend.add(score);
            }
        }
        scoresAfterAppend.add(elementScore);
        evaluationItemEntry.setLeaderScore(scoresAfterAppend);
        String msg = checkYouXiuLiangHaoNumLimit(teacherId, evaluationId, userId, 1, evaluationItemEntry);
        if(!msg.equals("")){
            throw new Exception(msg);
        }
        DBObject updateValue = new BasicDBObject("lds", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoresAfterAppend)));
        evaluationItemDao.updateEvaluationItem(teacherId, evaluationId, updateValue);
    }

    /**
     * 更新领导小组的打分
     * @param evaluationId
     * @param teacherId
     * @param userId
     * @param evaluationItemEntry
     * @param elementScore
     * @throws Exception
     */
    private void updateLeadMemberScore(ObjectId evaluationId, ObjectId teacherId, ObjectId userId, EvaluationItemEntry evaluationItemEntry,
                                       EvaluationItemEntry.ElementScore elementScore) throws Exception{
        List<EvaluationItemEntry.ElementScore> currentScore = evaluationItemEntry.getGroupScore();
        List<EvaluationItemEntry.ElementScore> scoresAfterAppend = new ArrayList<EvaluationItemEntry.ElementScore>();
        for(EvaluationItemEntry.ElementScore score : currentScore){
            if(!score.getEvaluateTeacherId().equals(userId)){
                scoresAfterAppend.add(score);
            }
        }
        scoresAfterAppend.add(elementScore);
        evaluationItemEntry.setGroupScore(scoresAfterAppend);
        String msg = checkYouXiuLiangHaoNumLimit(teacherId, evaluationId, userId, 1, evaluationItemEntry);
        if(!msg.equals("")){
            throw new Exception(msg);
        }
        DBObject updateValue = new BasicDBObject("gps", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoresAfterAppend)));
        evaluationItemDao.updateEvaluationItem(teacherId, evaluationId, updateValue);
    }



    /**
     * 计算成绩和组内排名
     * @param evaluationId
     */
    public void calculateScoresAndRanking(ObjectId evaluationId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        ProportionEntry proportionEntry = proportionDao.getProportionEntryByEvalationId(evaluationId);
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        int mode = settingEntry.getMode();
        Map<String, Double> gradeScoreMap = new HashMap<String, Double>();
        List<NameValuePair> nameValuePairs = settingEntry.getModeGrades();
        for(NameValuePair nameValuePair : nameValuePairs){
            gradeScoreMap.put(nameValuePair.getName(), (Double)nameValuePair.getValue());
        }
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> groupTeacherIds = teacherGroup.getGroupTeacherIds();
            List<EvaluationItemEntry> groupItems = evaluationItemDao.getEvaluationItems(groupTeacherIds, evaluationId, Constant.FIELDS);
            List<RankingDTO> rankingDTOs = new ArrayList<RankingDTO>();
            Map<String, EvaluationItemEntry> teacherIdEvaluationItemEntryMap = new HashMap<String, EvaluationItemEntry>();
            for(EvaluationItemEntry evaluationItemEntry : groupItems){
                double finalGroupScore = calculateAverageElementScore(evaluationItemEntry.getGroupScore(), proportionEntry.getGroupMax(), proportionEntry.getGroupMin(), mode, gradeScoreMap) *
                        proportionEntry.getGroupPro() / 100;
                double finalHuPingScore = calculateAverageElementScore(evaluationItemEntry.getHuPingScore(), proportionEntry.getHuPingMax(), proportionEntry.getHuPingMin(), mode, gradeScoreMap) *
                        proportionEntry.getHuPingPro() / 100;
                double finalLeaderScore = calculateAverageElementScore(evaluationItemEntry.getLeaderScore(), proportionEntry.getLeaderMax(), proportionEntry.getLeaderMin(), mode, gradeScoreMap) *
                        proportionEntry.getLeaderPro() / 100;
                double finalScore = finalGroupScore + finalHuPingScore + finalLeaderScore;
                finalScore = Math.round(finalScore * 1000) / 1000.0;
                evaluationItemEntry.setFinalGroupScore(finalGroupScore);
                evaluationItemEntry.setFinalHuPingScore(finalHuPingScore);
                evaluationItemEntry.setFinalLeaderScore(finalLeaderScore);
                evaluationItemEntry.setFinalScore(finalScore);
                RankingDTO rankingDTO = new RankingDTO(evaluationItemEntry.getTeacherId().toString(), "", "", finalScore, 1);
                rankingDTOs.add(rankingDTO);
                teacherIdEvaluationItemEntryMap.put(evaluationItemEntry.getTeacherId().toString(), evaluationItemEntry);
            }

            Collections.sort(rankingDTOs);
            int rank = 1;
            double lastScore = 0;
            int index = 0;
            for(RankingDTO rankingDTO : rankingDTOs){
                index ++;
                double score = rankingDTO.getFinalStdScore();
                if(score >= lastScore){
                    rankingDTO.setRanking(rank);
                } else {
                    rank = index;
                    rankingDTO.setRanking(rank);
                }
                lastScore = score;
                EvaluationItemEntry evaluationItemEntry = teacherIdEvaluationItemEntryMap.get(rankingDTO.getTeacherId());
                evaluationItemEntry.setRank(rankingDTO.getRanking());
                evaluationItemDao.addEvaluationItem(evaluationItemEntry);
            }

        }


    }

    /**
     * 计算平均分，去掉x个最高分和x个最低分
     * @param elementScores
     * @param maxNum
     * @param minNum
     * @return
     */
    private double calculateAverageElementScore(List<EvaluationItemEntry.ElementScore> elementScores, int maxNum, int minNum, final int mode, final Map<String, Double> gradeScoreMap){
        //只保留提交的数据
        Iterator<EvaluationItemEntry.ElementScore> iterator = elementScores.iterator();
        while (iterator.hasNext()){
            EvaluationItemEntry.ElementScore elementScore = iterator.next();
            if(elementScore.getTijiao() != 1)
                iterator.remove();
        }
        int size = elementScores.size() - maxNum - minNum;
        if(size > 0){
            Collections.sort(elementScores, new Comparator<EvaluationItemEntry.ElementScore>() {
                @Override
                public int compare(EvaluationItemEntry.ElementScore score1, EvaluationItemEntry.ElementScore score2) {
                    Double totalScore1 = getTotalScore(score1, mode, gradeScoreMap);
                    Double totalScore2 = getTotalScore(score2, mode, gradeScoreMap);
                    return totalScore2.compareTo(totalScore1);
                }
            });
            double totalScore = 0;
            for(int i=maxNum; i<maxNum+size; i++){
                totalScore += getTotalScore(elementScores.get(i), mode, gradeScoreMap);
            }
            return totalScore / elementScores.size();
        }
        return 0;
    }

    private Double getTotalScore(EvaluationItemEntry.ElementScore elementScore, int mode, Map<String, Double> gradeScoreMap){
        if(1 == mode){
            return elementScore.getTotalScore();
        }
        double totalScore = 0;
        List<IdNameValuePair> scores = elementScore.getElementScores();
        for(IdNameValuePair score : scores){
            Double gradeScore = gradeScoreMap.get(score.getName());
            totalScore += gradeScore == null ? 0 :gradeScore;
        }
        return totalScore / scores.size();
    }



    public List<RankingDTO> getStdScoreRanking(ObjectId evaluationId, ObjectId userId){
        List<RankingDTO> rankingDTOs = new ArrayList<RankingDTO>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        if(memberGroupEntry == null){
            return rankingDTOs;
        }
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<ObjectId> allTeacherIds = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> groupTeacherIds = teacherGroup.getGroupTeacherIds();
            allTeacherIds.addAll(groupTeacherIds);
        }
        DBObject fields = new BasicDBObject("tid", 1).append("fs", 1).append("rk", 1);
        List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(evaluationId, fields);

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(allTeacherIds, new BasicDBObject("nm", 1));
        Map<ObjectId, MemberGroupEntry.TeacherGroup> userGroupMap = getTeacherIdGroupMap(teacherGroups);

        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        List<SettingEntry.GradeSetting> gradeSettings = settingEntry.getGradeSettings();

        List<ObjectId> leaders = memberGroupEntry.getLeaders();
        Boolean isLeader = leaders.contains(userId);
        for(EvaluationItemEntry entry : itemEntries){
            ObjectId teacherId = entry.getTeacherId();
            MemberGroupEntry.TeacherGroup teacherGroup = userGroupMap.get(teacherId);
            if(teacherGroup == null)
                continue;
            int limitRanking = teacherGroup.getNum() + teacherGroup.getLiangNum();
            if(!isLeader && entry.getRank() > limitRanking){
                continue;
            }
            UserEntry userEntry = userMap.get(teacherId);
            if(userEntry == null){
                continue;
            }
            String teacherName = userEntry.getUserName();

            String groupName = teacherGroup.getGroupName();
            double finalStdScore = Math.round(entry.getFinalScore() * 1000) / 1000.0;//改为展示原始分
            RankingDTO rankingDTO = new RankingDTO(teacherId.toString(), teacherName, groupName, finalStdScore, entry.getRank());
            rankingDTOs.add(rankingDTO);
        }
//        return rankingDTOs;
        return formateRankGradeName(evaluationId, rankingDTOs, userGroupMap);
    }

    private Map<ObjectId, MemberGroupEntry.TeacherGroup> getTeacherIdGroupMap(List<MemberGroupEntry.TeacherGroup> teacherGroups){
        Map<ObjectId, MemberGroupEntry.TeacherGroup> teacherIdGroupMap = new HashMap<ObjectId, MemberGroupEntry.TeacherGroup>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teacherIds = teacherGroup.getGroupTeacherIds();
            for(ObjectId teacherId : teacherIds){
                teacherIdGroupMap.put(teacherId, teacherGroup);
            }
        }
        return teacherIdGroupMap;
    }

    private List<RankingDTO> formateRankGradeName(ObjectId evaluationId, List<RankingDTO> rankingDTOs, Map<ObjectId, MemberGroupEntry.TeacherGroup> userGroupMap){
        List<RankingDTO> retList = new ArrayList<RankingDTO>();
        SettingEntry settingEntry = settingDao.getSettingEntry(evaluationId);
        List<SettingEntry.GradeSetting> gradeSettings = settingEntry.getGradeSettings();
        for(RankingDTO rankingDTO : rankingDTOs){
            if(rankingDTO.getFinalStdScore() > 0) {
                MemberGroupEntry.TeacherGroup teacherGroup = userGroupMap.get(new ObjectId(rankingDTO.getTeacherId()));
                int limitRanking = teacherGroup.getNum() + teacherGroup.getLiangNum();
                String gradeName = "";
                if(rankingDTO.getRanking() <= teacherGroup.getNum()){
                    gradeName = gradeSettings.get(0).getName();
                } else if(rankingDTO.getRanking() <= limitRanking){
                    gradeName = gradeSettings.get(1).getName();
                } else {
                    gradeName = gradeSettings.get(2).getName();
                }
                rankingDTO.setGradeName(gradeName);
                retList.add(rankingDTO);
            }
        }
        Collections.sort(retList, new Comparator<RankingDTO>() {
            @Override
            public int compare(RankingDTO o1, RankingDTO o2) {
            return Collator.getInstance(Locale.CHINESE).compare(o1.getGroupName(), o2.getGroupName());
            }
        });
        return retList;
    }


    public Map<String, Object> getTeacherElementScores(ObjectId teacherId, ObjectId evaluationId){
        ProportionEntry proportionEntry = proportionDao.getProportionEntryByEvalationId(evaluationId);
        List<ElementEntry> elementEntries = elementDao.getElements(evaluationId, 1);

        EvaluationItemEntry evaluationItemEntry = getEvaluationItemEntry(teacherId, evaluationId, Constant.FIELDS);
        List<EvaluationItemEntry.ElementScore> leadGroupScore =  evaluationItemEntry.getLeaderScore();
        leadGroupScore.addAll(evaluationItemEntry.getGroupScore());

        List<IdValuePairDTO1> group = calculateEachElementAvgScore(evaluationItemEntry.getGroupScore(), elementEntries);
        List<IdValuePairDTO1> leader = calculateEachElementAvgScore(evaluationItemEntry.getLeaderScore(), elementEntries);

        List<IdValuePairDTO1> huping = calculateEachElementAvgScore(evaluationItemEntry.getHuPingScore(), elementEntries);

        List<IdValuePairDTO1> total = new ArrayList<IdValuePairDTO1>();
        for(int i=0; i<huping.size(); i++){
            double leaderScore =(Double)leader.get(i).getValue();
            double groupScore = (Double)group.get(i).getValue();
            double hupingScore = (Double)huping.get(i).getValue();
            double totalScore = leaderScore * proportionEntry.getLeaderPro() + groupScore * proportionEntry.getGroupPro() + hupingScore * proportionEntry.getHuPingPro();
            totalScore = totalScore / 100;
            totalScore = Math.round(totalScore * 1000)/ 1000.0;
            IdValuePairDTO1 pairDTO = new IdValuePairDTO1(leader.get(i).getId(), totalScore);
            total.add(pairDTO);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("huping", huping);
        map.put("leader", leader);
        map.put("group", group);
        map.put("total", total);

        return map;
    }

    private List<IdValuePairDTO1> calculateEachElementAvgScore( List<EvaluationItemEntry.ElementScore> elementScores, List<ElementEntry> elementEntries){
        List<IdValuePairDTO1> list = new ArrayList<IdValuePairDTO1>();
        Map<ObjectId, Double> map = new LinkedHashMap<ObjectId, Double>();
        for(ElementEntry elementEntry : elementEntries){
            map.put(elementEntry.getID(), new Double(0));
        }
        int size = elementScores.size();
        if(size > 0){
            for(EvaluationItemEntry.ElementScore elementScore : elementScores){
                List<IdNameValuePair> pairs = elementScore.getElementScores();
                for(IdNameValuePair pair : pairs){
                    ObjectId id = pair.getId();
                    Double value = (Double)pair.getValue();
                    map.put(id, map.get(id) + value);
                }
            }

        }
        for (Map.Entry<ObjectId, Double> entry : map.entrySet()) {
            double value = entry.getValue() / size;
            value = Math.round(value * 1000) / 1000.0;
            IdValuePairDTO1 pairDTO = new IdValuePairDTO1(entry.getKey().toString(), value);
            list.add(pairDTO);
        }
        return list;
    }


    public List<Map<String, Object>> getMyScores(ObjectId schoolId, ObjectId userId){
        List<Map<String, Object>> model = new ArrayList<Map<String, Object>>();
        List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(schoolId, userId, new BasicDBObject());
        for(EvaluationItemEntry itemEntry : itemEntries){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", "");
            MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(itemEntry.getEvaluationId());
            List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
            for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
                List<ObjectId> teachers = teacherGroup.getGroupTeacherIds();
                if(teachers.contains(userId)){
                    map.put("groupName", teacherGroup.getGroupName());
                    break;
                }
            }

            map.put("year", memberGroupEntry.getYear() + "学年");
            map.put("stdScore", itemEntry.getFinalStdScore());
            map.put("rank", itemEntry.getRank());
            model.add(map);
        }
        return model;
    }

    /**
     * 教师小组教师列表及状态（状态类型由type指定）
     * @param evaluationId
     * @param groupId
     * @param userId
     * @param type  1:实证资料 2：校长查看老师互评 3：考核打分
     * @return
     */
    public List<Map<String, Object>> getGroupTeachersAndState(ObjectId evaluationId, ObjectId groupId, ObjectId userId, int type){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupId().equals(groupId)){
                List<ObjectId> teacherIds = teacherGroup.getSignGroupTeacherIds();
                Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(teacherIds, new BasicDBObject("nm", 1));

                List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(teacherIds, evaluationId, Constant.FIELDS);
                Map<ObjectId, EvaluationItemEntry> evaluationItemEntryMap = new HashMap<ObjectId, EvaluationItemEntry>();
                for(EvaluationItemEntry itemEntry : itemEntries){
                    evaluationItemEntryMap.put(itemEntry.getTeacherId(), itemEntry);
                }

                for(ObjectId teacherId : teacherIds){
                    UserEntry userEntry = userMap.get(teacherId);
                    if (userEntry == null) {
                        memberGroupEntry.removeTeacherFromTeacherGroup(groupId, teacherId);
                        memberGroupDao.saveMemberGroup(memberGroupEntry);
                        continue;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", teacherId.toString());
                    map.put("name", userEntry.getUserName());
                    map.put("state", 0);
                    EvaluationItemEntry evaluationItemEntry = evaluationItemEntryMap.get(teacherId);
                    if(evaluationItemEntry != null){
                        int state = 0;
                        if(type == 1){
                            state = checkEvidenceState(evaluationItemEntry);
                        } else if(type == 2){
                            state = checkHuPingStateForLeader(evaluationItemEntry);
                        } else if(type == 3){
                            state = checkKaoHeState(evaluationItemEntry, userId);
                        }
                        map.put("state", state);
                    }

                    list.add(map);
                }
            }
        }
        return list;
    }

    private int checkEvidenceState(EvaluationItemEntry evaluationItemEntry){
        if(evaluationItemEntry.getEvidence().equals("")){
            return 0;
        } else {
            return 1;
        }
    }

    private int checkHuPingState(EvaluationItemEntry evaluationItemEntry, ObjectId userId){
        List<EvaluationItemEntry.ElementScore> scores = evaluationItemEntry.getHuPingScore();
        if(scores.size() > 0){
            for(EvaluationItemEntry.ElementScore score : scores){
                if(score.getEvaluateTeacherId().equals(userId)){
                    return 1;
                }
            }
        }
        return 0;
    }

    private int checkHuPingStateForLeader(EvaluationItemEntry evaluationItemEntry){
        if(evaluationItemEntry.getHuPingScore().size() <= 0){
            return 0;
        } else {
            return 1;
        }
    }

    private int checkKaoHeState(EvaluationItemEntry evaluationItemEntry, ObjectId userId){
        List<EvaluationItemEntry.ElementScore> scores = evaluationItemEntry.getGroupScore();
        scores.addAll(evaluationItemEntry.getLeaderScore());
        if(scores.size() > 0){
            for(EvaluationItemEntry.ElementScore score : scores){
                if(score.getEvaluateTeacherId().equals(userId)){
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * 清空量化成绩、互评成绩、考核打分
     * @param evaluationId
     */
    public void restScores(ObjectId evaluationId){
        evaluationItemDao.restEvaluationItemScore(evaluationId);
    }

    public List<Map<String, Object>> checkIntegrity(ObjectId evaluationId){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teachers = teacherGroup.getGroupTeacherIds();
            List<ObjectId> signTeachers = teacherGroup.getSignGroupTeacherIds();
            List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(signTeachers, evaluationId, new BasicDBObject());
            Map<ObjectId, Integer> teacherCountMap = new HashMap<ObjectId, Integer>();
            for(EvaluationItemEntry itemEntry : itemEntries){
                List<EvaluationItemEntry.ElementScore> elementScores = itemEntry.getHuPingScore();
                Map<ObjectId, Boolean> teacherIdMap = new HashMap<ObjectId, Boolean>();//已打过分的老师
                teacherIdMap.put(itemEntry.getTeacherId(), true);
                for(EvaluationItemEntry.ElementScore elementScore : elementScores){
                    teacherIdMap.put(elementScore.getEvaluateTeacherId(), true);
                }
                for(ObjectId objectId : teachers){
                    Boolean flag = teacherIdMap.get(objectId);
                    if(flag == null){
                        teacherCountMap.put(objectId, teacherCountMap.get(objectId) == null ? 1 : teacherCountMap.get(objectId) + 1);
                    }
                }

            }
            for(Map.Entry<ObjectId, Integer> entry : teacherCountMap.entrySet()){
                ObjectId teacherId = entry.getKey();
                UserEntry userEntry = userDao.getUserEntry(teacherId, new BasicDBObject());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("step", "互评");
                map.put("teacherName", userEntry.getUserName());
                map.put("count", entry.getValue());
                list.add(map);
            }
        }

        List<ObjectId> members = memberGroupEntry.getMembers();
        List<ObjectId> allSignteachers = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            allSignteachers.addAll(teacherGroup.getSignGroupTeacherIds());
        }
        List<EvaluationItemEntry> itemEntries = evaluationItemDao.getEvaluationItems(allSignteachers, evaluationId, new BasicDBObject());
        Map<ObjectId, Integer> teacherNumMap = new HashMap<ObjectId, Integer>();
        for(EvaluationItemEntry itemEntry : itemEntries){
            List<EvaluationItemEntry.ElementScore> elementScores = itemEntry.getGroupScore();
            Map<ObjectId, Boolean> teacherIdMap = new HashMap<ObjectId, Boolean>();
            teacherIdMap.put(itemEntry.getTeacherId(), true);
            for(EvaluationItemEntry.ElementScore elementScore : elementScores){
                teacherIdMap.put(elementScore.getEvaluateTeacherId(), true);
            }

            for(ObjectId member : members){
                if(teacherIdMap.get(member) == null){
                    teacherNumMap.put(member, teacherNumMap.get(member) == null ? 1 : teacherNumMap.get(member) + 1);
                }
            }
            
        }

        for(Map.Entry<ObjectId, Integer> entry : teacherNumMap.entrySet()){
            ObjectId tId = entry.getKey();
            UserEntry userEntry = userDao.getUserEntry(tId, new BasicDBObject());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("step", "考核");
            map.put("teacherName", userEntry.getUserName());
            map.put("count", entry.getValue());
            list.add(map);
        }


        return list;
    }

    /**
     * 导出完整性检查结果
     * @param evaluationId
     * @param response
     */
    public void exportIntegrity(ObjectId evaluationId, HttpServletResponse response){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(memberGroupEntry.getName());
        List<Map<String, Object>> list = checkIntegrity(evaluationId);
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("打分项目");
        cell = row.createCell(1);
        cell.setCellValue("老师姓名");
        cell = row.createCell(2);
        cell.setCellValue("漏打人数");
        int rowNo = 1;
        for(Map<String, Object> map : list){
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellValue(map.get("step").toString());
            cell = row.createCell(1);
            cell.setCellValue(map.get("teacherName").toString());
            cell = row.createCell(2);
            cell.setCellValue(map.get("count").toString());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(memberGroupEntry.getName() + "排查情况.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 可用小组，考核小组领导及成员可以看到所有小组，普通老师只能看到自己所在小组
     * @param evaluationId
     * @param role
     * @param userId
     * @return
     */
    public List<Map<String, Object>> availableGroups(ObjectId evaluationId, int role, ObjectId userId){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        Map<String, Boolean> roleInfo = formateEvaluationRole(memberGroupEntry, userId);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        if(roleInfo.get("leader")|| roleInfo.get("member")){
            for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("groupId", teacherGroup.getGroupId().toString());
                map.put("groupName", teacherGroup.getGroupName());
                list.add(map);
            }
        } else if(roleInfo.get("teacher")){
            for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
                List<ObjectId> teacherIds = teacherGroup.getGroupTeacherIds();
                if(teacherIds.contains(userId)){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("groupId", teacherGroup.getGroupId().toString());
                    map.put("groupName", teacherGroup.getGroupName());
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 可用老师，小组中除自己以外的所有老师
     * @param evaluationId
     * @param groupId
     * @param userId
     */
    public List<EvaluationItemDTO> availableTeachers(ObjectId evaluationId, ObjectId groupId, ObjectId userId){
        MemberGroupEntry memberGroupEntry = memberGroupDao.getMemberGroup(evaluationId);
        List<ElementEntry> elementEntries = elementDao.getElements(evaluationId, 1);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            if(teacherGroup.getGroupId().equals(groupId)){
                teacherIds = teacherGroup.getSignGroupTeacherIds();
                teacherIds.remove(userId);
            }
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(teacherIds, new BasicDBObject("nm", 1));
        List<EvaluationItemDTO> evaluationItemDTOs = new ArrayList<EvaluationItemDTO>();
        List<EvaluationItemEntry> evaluationItemEntries = evaluationItemDao.getEvaluationItems(teacherIds, evaluationId, Constant.FIELDS);
        Map<String, Boolean> roleInfo = formateEvaluationRole(memberGroupEntry, userId);
        for(EvaluationItemEntry evaluationItemEntry : evaluationItemEntries){
            UserEntry userEntry = userEntryMap.get(evaluationItemEntry.getTeacherId());
            if(userEntry == null)
                continue;
            EvaluationItemDTO itemDTO = formateEvaluationItemDTO(evaluationItemEntry, userEntry, roleInfo, userId, elementEntries);
            evaluationItemDTOs.add(itemDTO);
        }
        return evaluationItemDTOs;
    }

    private EvaluationItemDTO formateEvaluationItemDTO(EvaluationItemEntry entry, UserEntry teacher, Map<String, Boolean> roleInfo, ObjectId userId, List<ElementEntry> elementEntries){
        EvaluationItemDTO itemDTO = new EvaluationItemDTO(entry);
        itemDTO.setTeacherName(teacher.getUserName());
        List<IdNameValuePairDTO> defaultPair = new ArrayList<IdNameValuePairDTO>();
        for(ElementEntry elementEntry : elementEntries){
            defaultPair.add(new IdNameValuePairDTO(elementEntry.getID().toString(), "Z", 0));
        }
        itemDTO.setElementScores(defaultPair);

        List<EvaluationItemEntry.ElementScore> elementScores = new ArrayList<EvaluationItemEntry.ElementScore>();
        if(roleInfo.get("teacher")){
            elementScores = entry.getHuPingScore();
        } else if(roleInfo.get("leader")){
            elementScores = entry.getLeaderScore();
        } else if(roleInfo.get("member")){
            elementScores = entry.getGroupScore();
        }
        for(EvaluationItemEntry.ElementScore elementScore : elementScores){
            if(elementScore.getEvaluateTeacherId().equals(userId)){
                List<IdNameValuePair> idValuePairs = elementScore.getElementScores();
                List<IdNameValuePairDTO> idValuePairDTO1s = new ArrayList<IdNameValuePairDTO>();
                for(IdNameValuePair idValuePair : idValuePairs){
                    idValuePairDTO1s.add(new IdNameValuePairDTO(idValuePair));
                }
                itemDTO.setElementScores(idValuePairDTO1s);
                break;
            }
        }
        return itemDTO;
    }

    private Map<String, Boolean> formateEvaluationRole(MemberGroupEntry memberGroupEntry, ObjectId userId){
        Map<String, Boolean> model = new HashMap<String, Boolean>();
        List<ObjectId> leaders = memberGroupEntry.getLeaders();
        List<ObjectId> members = memberGroupEntry.getMembers();
        model.put("leader", leaders.contains(userId));
        model.put("member", members.contains(userId));
        model.put("teacher", false);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> teachers = teacherGroup.getGroupTeacherIds();
            if(teachers.contains(userId)){
                model.put("teacher", true);
                break;
            }
        }
        return model;
    }




    //service结束
}
