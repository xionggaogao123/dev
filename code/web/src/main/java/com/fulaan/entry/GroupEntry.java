package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/11/1.
 *
 * himg: 群聊头像
 * grct: groupCount 群聊个数
 * grnm: 群组名称
 * grde:
 * grqr: 二维码路径
 * grti：创建时间
 * grcd：环信id
 * grow：所有者
 *
 * cmid: 社区id
 * grlo: 群聊logo
 * grde: 群聊描述
 *
 * bcm:是否绑定社区 0:未绑定社区 1:绑定社区
 * ism: 是否改了名字 0：未改
 *
 */
public class GroupEntry extends BaseDBObject {

  public GroupEntry(DBObject dbo) {
    setBaseEntry((BasicDBObject) dbo);
  }

  /**
   * 单独的群聊
   * @param _id
   * @param owerId
   * @param qr
   * @param chatId
   */
  public GroupEntry(ObjectId _id,ObjectId owerId,String qr,String chatId) {
    BasicDBObject dbo = new BasicDBObject()
            .append(Constant.ID,_id)
            .append("grnm", "")
            .append("grlo", "")
            .append("grde", "")
            .append("grqr", qr)
            .append("grti", System.currentTimeMillis())
            .append("grcd", chatId)
            .append("grow", owerId)
            .append("himg","")
            .append("ism",0);
    setBaseEntry(dbo);
  }

  /**
   * 和社区绑定的群聊
   * @param _id
   * @param commid
   * @param owerId
   * @param qr
   * @param chatId
   */
  public GroupEntry(ObjectId _id,ObjectId commid,ObjectId owerId,String qr,String chatId,String grnm,String grdesc) {
    BasicDBObject dbo = new BasicDBObject()
            .append(Constant.ID,_id)
            .append("cmid",commid)
            .append("grnm", grnm)
            .append("grde", grdesc)
            .append("grqr", qr)
            .append("grti", System.currentTimeMillis())
            .append("grcd", chatId)
            .append("grow", owerId)
            .append("himg","")
            .append("ism",0);
    setBaseEntry(dbo);
  }

  public String getEmChatId(){
    if(getBaseEntry().containsKey("grcd")){
      return getSimpleStringValue("grcd");
    }
    return "";
  }

  public String getLogo() {
    if(getBaseEntry().containsKey("grlo")){
      return getSimpleStringValue("grlo");
    }
    return "";
  }

  public long getTime() {
    return getSimpleLongValue("grti");
  }


  public String getDesc() {
    if(getBaseEntry().containsKey("grde")){
      return getSimpleStringValue("grde");
    }
    return "";
  }


  public ObjectId getOwerId() {
    return getSimpleObjecIDValue("grow");
  }


  public String getName() {
    if(getBaseEntry().containsKey("grnm")){
      return getSimpleStringValue("grnm");
    }
    return "";
  }

  public String getQrUrl() {
    if(getBaseEntry().containsKey("grqr")){
      return getSimpleStringValue("grqr");
    }
    return "";
  }

  public String getHeadImage(){
    if(getBaseEntry().containsKey("himg")){
      return getSimpleStringValue("himg");
    }
    return "";
  }

  public ObjectId getCommunityId() {
    if(getBaseEntry().containsKey("cmid")){
      return getSimpleObjecIDValue("cmid");
    }
    return null;
  }

  public int getIsName(){
    return getSimpleIntegerValue("ism");
  }
}
