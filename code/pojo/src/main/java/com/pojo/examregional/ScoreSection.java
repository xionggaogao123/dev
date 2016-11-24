package com.pojo.examregional;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**分数段
 * Created by fl on 2015/11/3.
 * bgn:开始分数
 * end:结束分数
 * num:人数
 */
public class ScoreSection extends BaseDBObject {
    public ScoreSection(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public ScoreSection(int beginScore, int endScore){
        BasicDBObject dbObject = new BasicDBObject()
                .append("bgn", beginScore)
                .append("end", endScore)
                .append("num", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public int getBeginScore(){
        return getSimpleIntegerValue("bgn");
    }

    public void setBeginScore(int beginScore){
        setSimpleValue("bgn", beginScore);
    }

    public int getEndScore(){
        return getSimpleIntegerValue("end");
    }

    public void setEndScore(int endScore){
        setSimpleValue("end", endScore);
    }

    public int getNum(){
        return getSimpleIntegerValue("num");
    }

    public void setNum(int num){
        setSimpleValue("num", num);
    }
}
