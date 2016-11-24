package com.pojo.examresult;

/**分数分布
 * Created by fl on 2015/6/18.
 */
public class ScoreDistributionDTO {

    String distribution;
    Integer num;

    public ScoreDistributionDTO(String distribution, Integer num) {
        this.distribution = distribution;
        this.num = num;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
