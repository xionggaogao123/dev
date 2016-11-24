package com.pojo.forum;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/13.
 * 邀请（统计邀请好友数）
 * {
 *     uid: userId 邀请人
 *     ct: count 邀请数
 *     ul: userList 被邀请人列表
 *
 * }
 */
public class FInvitationEntry extends BaseDBObject {

    public FInvitationEntry(){}

    public FInvitationEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public Long getCount(){
        return getSimpleLongValue("ct");
    }

    public void setCount(Long count){
        setSimpleValue("ct",count);
    }

    public List<ObjectId> getUserReplyList(){
        List<ObjectId> retList =new ArrayList<ObjectId>();
        if(getBaseEntry().containsField("url")) {
            BasicDBList list = (BasicDBList) getSimpleObjectValue("url");
            if (null != list && !list.isEmpty()) {
                for (Object o : list) {
                    retList.add((ObjectId) o);
                }
            }
        }
        return retList;
    }


    public void setUserReplyList(List<ObjectId> userReplyList){
        setSimpleValue("url", MongoUtils.convert(userReplyList));
    }
}
