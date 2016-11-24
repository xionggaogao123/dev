package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 分层数据
 * fc:分层  A,B,C...Z
 * score:成绩段
 * users:学生列表 users
 * Created by wang_xinxin on 2015/10/14.
 */
public class FenCengItem extends BaseDBObject {

    public FenCengItem(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public FenCengItem(String fenCeng,String score,List<ObjectId> users) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("fc", fenCeng)
                .append("score",score)
                .append("users", MongoUtils.convert(users));
        setBaseEntry(baseEntry);
    }

    public String getFenCeng() {
        return getSimpleStringValue("fc");
    }

    public void setFenCeng(String fenCeng) {
        setSimpleValue("fc",fenCeng);
    }

    public String getScore() {
        return getSimpleStringValue("score");
    }

    public void setScore(String score) {
        setSimpleValue("score",score);
    }

    public List<ObjectId> getUsers() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("users");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setUsers(List<ObjectId> users) {
        setSimpleValue("users", MongoUtils.convert(users));
    }
}
