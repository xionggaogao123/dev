package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/10/21.
 * 社区Entry
 * <p/>
 * 家校互动
 * {
 * :communityName 社区名称
 * cmlg:communityLogo 社区logo
 * cmmes:communityMembers 社区成员列表
 * cmde:communityDescription 社区简介
 * cmco:communityQRCode 社区二维码
 * cmti:communityTime 创建社区时间
 * cmow:communityOwerId 创建者ID Mongo ID 类型
 * op:open 0:未公开 1:公开
 * r:remove 0：未删除 1：已删除
 *
 * -------------------------
 * grid: 群组id
 * emid: 环信id
 * <p/>
 * }
 */
public class CommunityEntry extends BaseDBObject {

  public CommunityEntry(){}

  public CommunityEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  public CommunityEntry(ObjectId _id, String searchId,ObjectId groupId,String emChatId, String name, String logo,String desc,
                        String qr, int open,ObjectId owerId) {
    BasicDBObject dbo = new BasicDBObject()
            .append("_id", _id)
            .append("cmid", searchId)
            .append("cmmn", name)
            .append("cmlg", logo)
            .append("cmde", desc)
            .append("cmco", qr)
            .append("grid",groupId)
            .append("cmti", System.currentTimeMillis())
            .append("cmow", owerId)
            .append("op",open)
            .append("emid",emChatId)
            .append("r", 0);
    setBaseEntry(dbo);
  }

  public int getOpen(){
    return getSimpleIntegerValue("op");
  }

  public Long getCommunityTime() {
    return getSimpleLongValueDef("cmti", 0L);
  }

  public String getSearchId() {
    return getSimpleStringValue("cmid");
  }

  public String getCommunityName() {
    return getSimpleStringValue("cmmn");
  }

  public String getCommunityLogo() {
    return getSimpleStringValue("cmlg");
  }

  public String getCommunityDescription() {
    return getSimpleStringValue("cmde");
  }

  public String getCommunityQRCode() {
    return getSimpleStringValue("cmco");
  }

  public ObjectId getOwerID() {
    return getSimpleObjecIDValue("cmow");
  }

  public int getRemove() {
    return getSimpleIntegerValue("r");
  }

  public void setRemove(int remove) {
    setSimpleValue("r", remove);
  }

  public String getGroupId() {
    return getSimpleStringValue("grid");
  }

  public String getEmChatId() {
    if(getBaseEntry().containsKey("emid")){
      return getSimpleStringValue("emid");
    }
    return "";
  }
}
