package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/13.
 * 记录投票贴
 * {
 *     vid : voteId 投票贴Id
 *     uid : userId 用户Id
 *     nb : number 选中的序号
 *
 * }
 */
public class FVoteEntry extends BaseDBObject {

    public FVoteEntry(){}

    public FVoteEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public FVoteEntry(ObjectId voteId,ObjectId userId,int number){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("vid",voteId)
                .append("uid",userId)
                .append("nb",number);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getVoteId(){
        return getSimpleObjecIDValue("vid");
    }

    public void setVoteId(ObjectId voteId){
        setSimpleValue("vid",voteId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getNumber(){
        return getSimpleIntegerValue("nb");
    }

    public void setNumber(int number){
        setSimpleValue("nb",number);
    }
}
