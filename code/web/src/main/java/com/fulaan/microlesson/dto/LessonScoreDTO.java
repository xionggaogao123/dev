package com.fulaan.microlesson.dto;

import com.pojo.microlesson.LessonScoreEntry;
import com.pojo.microlesson.ScoreTypeEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/28.
 */
public class LessonScoreDTO implements Comparable<LessonScoreDTO>{

    private String lessonid;

    private String matchid;

    private String lessonname;

    private int allscore;

    private Float average;

    private int sort;

    private List<String> scores;

    public String getLessonid() {
        return lessonid;
    }

    public String getMatchid() {
        return matchid;
    }

    public String getLessonname() {
        return lessonname;
    }

    public int getAllscore() {
        return allscore;
    }

    public Float getAverage() {
        return average;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }

    public void setAllscore(int allscore) {
        this.allscore = allscore;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<String> getScores() {
        return scores;
    }

    public void setScores(List<String> scores) {
        this.scores = scores;
    }

//    public LessonScoreEntry buildLessonScore(ObjectId lessonid,ObjectId matchid,String lessonname,int allscore,double average,int sort) {
//
//        LessonScoreEntry lessonscore = new LessonScoreEntry(lessonid,matchid,lessonname,allscore,average,sort);
//        return lessonscore;
//    }

    public LessonScoreDTO(LessonScoreEntry s,List<String> namelist) {
        this.lessonid = s.getLessonid().toString();
        this.allscore = s.getAllScore();
        this.average = (float)s.getAverage();
        this.lessonname = s.getLessonname();
        this.sort = s.getSort();
        List<String> scorelist = new ArrayList<String>();
        if (namelist!=null && namelist.size()!=0) {
            if (s.getScoreTypeList()!=null && s.getScoreTypeList().size()!=0) {
                for (String name : namelist) {
                    String num = "0";
                    for (ScoreTypeEntry scoreTypeEntry : s.getScoreTypeList()) {
                        if (name.equals(scoreTypeEntry.getName())) {
                            num = String.valueOf(scoreTypeEntry.getScore());
                        }
                    }
                    scorelist.add(num);
                }
            } else {
                for (String name : namelist) {
                    scorelist.add("0");
                }
            }
        }
        this.scores = scorelist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonScoreDTO)) return false;

        LessonScoreDTO that = (LessonScoreDTO) o;

        if (Double.compare(that.average, average) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(average);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public int compareTo(LessonScoreDTO o) {
        return this.average.compareTo(o.average);
    }
}
