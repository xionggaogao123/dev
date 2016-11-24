package com.pojo.examregional;

/**等级段
 * Created by fl on 2016/6/16.
 */
public class GradeSection {
    private String name;//等级名称
    private int percent;//百分比 60 表示60%
    private double minScore;//等级最低分数 = 百分比 / 100 * 总分
    private int count;//等级人数
    private double rate;// 等级人数/总人数

    public GradeSection(String name, int percent, double minScore, int count, double rate) {
        this.name = name;
        this.percent = percent;
        this.minScore = minScore;
        this.count = count;
        this.rate = rate;
    }

    public void countInc(){
        count++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
