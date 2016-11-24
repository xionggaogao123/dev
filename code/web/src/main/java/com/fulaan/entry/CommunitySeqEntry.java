package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by jerry on 2016/10/26.
 * Seq
 * ty:类型------0：定义阈值 1:序列值
 * ran : 一个随机值
 * seq：序列----阀值
 */
public class CommunitySeqEntry extends BaseDBObject {

  public CommunitySeqEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  public CommunitySeqEntry(int type,long communitySeq) {
    BasicDBObject dbo = new BasicDBObject()
            .append("ty",type)
            .append("seq", communitySeq)
            .append("ran",Math.random())
            .append("r", 0);
    setBaseEntry(dbo);
  }

  public long getSeq(){
    return getSimpleLongValue("seq");
  }

  public long getType(){
    return getSimpleIntegerValue("ty");
  }
}
