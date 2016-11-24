package com.pojo.examscoreanalysis;

/**
 * Created by fl on 2016/8/23.
 */
public class ZongFenSummaryDTO {

    private String className;

    private int count_10;

    private double rate_10;

    private int count_50;

    private double rate_50;

    private int count_100;

    private double rate_100;

    private int count_200;

    private double rate_200;

    private double yswAvg;

    private int yswGradeRank;

    private double allAvg;

    private int allGradeRank;

    public ZongFenSummaryDTO(){}

    public ZongFenSummaryDTO(String className, int count_100, int count_10, int count_200, int count_50,
                             double rate_100, double rate_10, double rate_200, double rate_50,
                             double allAvg, int allGradeRank, double yswAvg, int yswGradeRank) {
        this.className = className;
        this.count_100 = count_100;
        this.count_10 = count_10;
        this.count_200 = count_200;
        this.count_50 = count_50;
        this.rate_100 = rate_100;
        this.rate_10 = rate_10;
        this.rate_200 = rate_200;
        this.rate_50 = rate_50;
        this.allAvg = allAvg;
        this.allGradeRank = allGradeRank;
        this.yswAvg = yswAvg;
        this.yswGradeRank = yswGradeRank;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCount_100() {
        return count_100;
    }

    public void setCount_100(int count_100) {
        this.count_100 = count_100;
    }

    public int getCount_10() {
        return count_10;
    }

    public void setCount_10(int count_10) {
        this.count_10 = count_10;
    }

    public int getCount_200() {
        return count_200;
    }

    public void setCount_200(int count_200) {
        this.count_200 = count_200;
    }

    public int getCount_50() {
        return count_50;
    }

    public void setCount_50(int count_50) {
        this.count_50 = count_50;
    }

    public double getRate_100() {
        return rate_100;
    }

    public void setRate_100(double rate_100) {
        this.rate_100 = rate_100;
    }

    public double getRate_10() {
        return rate_10;
    }

    public void setRate_10(double rate_10) {
        this.rate_10 = rate_10;
    }

    public double getRate_200() {
        return rate_200;
    }

    public void setRate_200(double rate_200) {
        this.rate_200 = rate_200;
    }

    public double getRate_50() {
        return rate_50;
    }

    public void setRate_50(double rate_50) {
        this.rate_50 = rate_50;
    }

    public double getAllAvg() {
        return allAvg;
    }

    public void setAllAvg(double allAvg) {
        this.allAvg = allAvg;
    }

    public int getAllGradeRank() {
        return allGradeRank;
    }

    public void setAllGradeRank(int allGradeRank) {
        this.allGradeRank = allGradeRank;
    }

    public double getYswAvg() {
        return yswAvg;
    }

    public void setYswAvg(double yswAvg) {
        this.yswAvg = yswAvg;
    }

    public int getYswGradeRank() {
        return yswGradeRank;
    }

    public void setYswGradeRank(int yswGradeRank) {
        this.yswGradeRank = yswGradeRank;
    }
}
