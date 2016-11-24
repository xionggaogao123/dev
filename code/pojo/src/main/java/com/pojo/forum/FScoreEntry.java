package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/8/16.
 */
public class FScoreEntry  extends BaseDBObject {
    public FScoreEntry(){}

    public FScoreEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public void setTime(Long  time){
        setSimpleValue("ti",time);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setScoreOrigin(String scoreOrigin){
        setSimpleValue("sco",scoreOrigin);
    }

    public String getScoreOrigin(){
        return getSimpleStringValue("sco");
    }

    public void setPersonId(ObjectId personId){
        setSimpleValue("pid",personId);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setScore(int score){
        setSimpleValue("sc",score);
    }

    public int getScore(){
        return getSimpleIntegerValue("sc");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setOperation(String operation){
        setSimpleValue("opr",operation);
    }

    public String getOperation(){
        return getSimpleStringValue("opr");
    }
}
