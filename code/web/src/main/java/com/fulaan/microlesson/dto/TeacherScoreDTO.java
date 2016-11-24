package com.fulaan.microlesson.dto;

import com.pojo.microlesson.ScoreTypeEntry;
import com.pojo.microlesson.TeacherScoreEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2015/8/28.
 */
public class TeacherScoreDTO {

    public TeacherScoreDTO() {

    }

    private int score;

    private String comment;

    private String lessonid;

    private String username;

    private String[] scoretypelist;

    private List<ScoreDTO> scoreDTOList;

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getScoretypelist() {
        return scoretypelist;
    }

    public void setScoretypelist(String[] scoretypelist) {
        this.scoretypelist = scoretypelist;
    }

    public TeacherScoreEntry buildTeacherScore(ObjectId userid,String username,Map<String,Object> model) {
        List<ScoreTypeEntry> scoretypes = new ArrayList<ScoreTypeEntry>();
        String[] scoreTypeList = this.getScoretypelist();
        int sc = this.score;
        List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
        if (scoreTypeList!=null && scoreTypeList.length!=0) {
            sc = 0;
            for (String type : scoreTypeList) {
                if (!StringUtils.isEmpty(type)) {
                    ScoreDTO scoreDTO = new ScoreDTO();
                    ScoreTypeEntry pair = new ScoreTypeEntry(type.split(";")[0],Integer.valueOf(type.split(";")[1]));
                    scoretypes.add(pair);
                    sc += Integer.valueOf(type.split(";")[1]);
                    scoreDTO.setName(type.split(";")[0]);
                    scoreDTO.setScore(type.split(";")[1]);
                    scoreDTOs.add(scoreDTO);
                }
            }
        }
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setName("总分");
        scoreDTO.setScore(String.valueOf(sc));
        scoreDTOs.add(scoreDTO);
        model.put("scorelist",scoreDTOs);
        TeacherScoreEntry teachersocre = new TeacherScoreEntry(userid,username,sc,this.comment,new ObjectId(this.getLessonid()),scoretypes);
        return teachersocre;
    }

    public TeacherScoreDTO (TeacherScoreEntry e) {
        this.score = e.getScore();
        this.comment = e.getComment();
        this.lessonid = e.getLessonid().toString();
        this.username = e.getSusername();
        List<ScoreDTO> scoreList = new ArrayList<ScoreDTO>();
        if (e.getScoreTypeList()!=null && e.getScoreTypeList().size()!=0) {
            ScoreDTO s = new ScoreDTO();
            int num = 0;
            for (ScoreTypeEntry scoreTypeEntry : e.getScoreTypeList()) {
                s = new ScoreDTO();
                s.setName(scoreTypeEntry.getName());
                s.setScore(String.valueOf(scoreTypeEntry.getScore()));
                scoreList.add(s);
                num += scoreTypeEntry.getScore();
            }
            s = new ScoreDTO();
            s.setName("总分");
            s.setScore(String.valueOf(num));
            scoreList.add(s);
        } else {
            ScoreDTO s = new ScoreDTO();
            s.setName("总分");
            s.setScore(String.valueOf(e.getScore()));
            scoreList.add(s);
        }
        this.scoreDTOList = scoreList;
    }

    public List<ScoreDTO> getScoreDTOList() {
        return scoreDTOList;
    }

    public void setScoreDTOList(List<ScoreDTO> scoreDTOList) {
        this.scoreDTOList = scoreDTOList;
    }
}
