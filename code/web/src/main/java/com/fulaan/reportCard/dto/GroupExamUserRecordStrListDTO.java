package com.fulaan.reportCard.dto;

import java.util.ArrayList;
import java.util.List;

import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordStrListDTO {

    private String id;
    private String groupExamDetailId;
    private String userId;
    private List<String> score = new ArrayList<String>();
    private List<String> scoreRep = new ArrayList<String>();
    private List<Integer> scoreLevel = new ArrayList<Integer>();
    private String userName;
    private String userNumber;
    private String mainUserId;
    private String groupId;
    private String communityId;
    private int rank;
    private List<String> rankList = new ArrayList<String>();
    private String examType;
    private List<String> subjectId = new ArrayList<String>();
    private List<String> subjectName = new ArrayList<String>();
    //班级和成绩排名抬头
    private List<String> subjectNameWithDd = new ArrayList<String>();
    //班级和成绩排名具体成绩
    private List<String> scoreDd = new ArrayList<String>();
    //是否缺考
    private boolean flg = false;
    //是否选中
    private boolean flgChoose = false;
    //是否自己小孩
    private boolean ownChild = false;
    


    public GroupExamUserRecordStrListDTO(){

    }
    
    public GroupExamUserRecordStrListDTO(GroupExamUserRecordDTO g){
        this.id = g.getId();
        this.groupExamDetailId = g.getGroupExamDetailId();
        this.userId = g.getUserId();
        String[] scoreArry = g.getScoreStr().split(",");
        String[] scoreLevelArry = g.getScoreLevelStr().split(",");
        String[] subjectIdArry = g.getSubjectId().split(",");
        String[] rankArry = g.getRankStr().split(",");
        for (String s : scoreArry) {
            if (s.equals("-1")) {
                this.score.add("缺");
            } else if (s.equals("-2")) {
                this.score.add("");
            } else {
                this.score.add(s);
            }
        }
        
        for (String s : scoreArry) {
            if (s.equals("-1")) {
                this.flg = true;
                break;
            }
        }
    
        for (String s : scoreLevelArry) {
            this.scoreLevel.add(Integer.valueOf(s));
        }
   
        for (String s : subjectIdArry) {
            this.subjectId.add(s);
        }
        this.userName = g.getUserName();
        this.userNumber = g.getUserNumber();
        this.mainUserId = g.getMainUserId();
        this.groupId = g.getGroupId();
        this.communityId = g.getCommunityId();
        this.rank = g.getRank();
        this.examType = g.getExamType();
        for (String s : rankArry) {
            this.rankList.add(s);
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupExamDetailId() {
        return groupExamDetailId;
    }

    public void setGroupExamDetailId(String groupExamDetailId) {
        this.groupExamDetailId = groupExamDetailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getScore() {
        return score;
    }

    public void setScore(List<String> score) {
        this.score = score;
    }

    public List<Integer> getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(List<Integer> scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public List<String> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<String> subjectId) {
        this.subjectId = subjectId;
    }

    public List<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(List<String> subjectName) {
        this.subjectName = subjectName;
    }

    public boolean isFlg() {
        return flg;
    }

    public void setFlg(boolean flg) {
        this.flg = flg;
    }

    public boolean isFlgChoose() {
        return flgChoose;
    }

    public void setFlgChoose(boolean flgChoose) {
        this.flgChoose = flgChoose;
    }

    public List<String> getSubjectNameWithDd() {
        return subjectNameWithDd;
    }

    public void setSubjectNameWithDd(List<String> subjectNameWithDd) {
        this.subjectNameWithDd = subjectNameWithDd;
    }

    public List<String> getScoreDd() {
        return scoreDd;
    }

    public void setScoreDd(List<String> scoreDd) {
        this.scoreDd = scoreDd;
    }

    public List<String> getRankList() {
        return rankList;
    }

    public void setRankList(List<String> rankList) {
        this.rankList = rankList;
    }

    public boolean isOwnChild() {
        return ownChild;
    }

    public void setOwnChild(boolean ownChild) {
        this.ownChild = ownChild;
    }

    public List<String> getScoreRep() {
        return scoreRep;
    }

    public void setScoreRep(List<String> scoreRep) {
        this.scoreRep = scoreRep;
    }



    

    
    
    
    
    
}
