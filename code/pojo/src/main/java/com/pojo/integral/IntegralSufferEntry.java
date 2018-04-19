package com.pojo.integral;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-13.
 * 新-----积分经验值表
 * id                                  id
 * userId          uid                用户id
 * score           sco                积分
 * suffer          suf                经验值
 * sign            sig                签到数
 * oldSuffer       ols                昨日积分
 * olsScore        olo                昨日经验值
 */
public class IntegralSufferEntry extends BaseDBObject {
    public IntegralSufferEntry(){

    }

    public IntegralSufferEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public IntegralSufferEntry(
            ObjectId userId,
            int score,
            int suffer,
            int sign,
            int oldSuffer,
            int oldScore
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid",userId)
                .append("sco", score)
                .append("suf",suffer)
                .append("sig", sign)
                .append("ols",oldSuffer)
                .append("olo",oldScore)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    //修改构造
    public IntegralSufferEntry(
            ObjectId id,
            ObjectId userId,
            int score,
            int suffer,
            int sign,
            int oldSuffer,
            int oldScore
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("sco", score)
                .append("suf",suffer)
                .append("sig", sign)
                .append("ols", oldSuffer)
                .append("olo",oldScore)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getScore(){
        return getSimpleIntegerValue("sco");
    }

    public void setScore(int score){
        setSimpleValue("sco",score);
    }

    public int getSuffer(){
        return getSimpleIntegerValue("suf");
    }

    public void setSuffer(int suffer){
        setSimpleValue("suf",suffer);
    }

    public int getSign(){
        return getSimpleIntegerValue("sig");
    }

    public void setSign(int sign){
        setSimpleValue("sig",sign);
    }

    public int getOldScore(){
        return getSimpleIntegerValue("olo");
    }

    public void setOldScore(int oldScore){
        setSimpleValue("olo",oldScore);
    }

    public int getOldSuffer(){
        return getSimpleIntegerValue("ols");
    }

    public void setOldSuffer(int oldSuffer){
        setSimpleValue("ols",oldSuffer);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
