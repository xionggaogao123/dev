package com.pojo.fcommunity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/2.
 * <p>
 * zan:赞的个数
 * zali:点赞的列表
 */
public class PartInContentEntry extends BaseDBObject {

  public PartInContentEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  private PartInContentEntry() {

  }

  /**
   * 文本 ，图片 ，附件
   *
   * @param detailId
   * @param userId
   * @param information
   * @param imageList
   * @param attachmentList
   */
  public PartInContentEntry(ObjectId communityId,ObjectId detailId, ObjectId userId, String information, List<String> imageList,
                            List<AttachmentEntry> attachmentList,List<VideoEntry> videoEntries,int type) {
    this(communityId,detailId,userId,information,imageList,attachmentList,
            videoEntries,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,type,new ArrayList<ObjectId>());
  }

  /**
   *
   * @param detailId
   * @param userId
   * @param information
   * @param videoEntries
   * @param imageList
   */
  public PartInContentEntry(List<VideoEntry> videoEntries,ObjectId communityId,ObjectId detailId, ObjectId userId, String information, List<String> imageList,int type) {
    this(communityId,detailId,userId,information,imageList,new ArrayList<AttachmentEntry>(),
            videoEntries,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,type,new ArrayList<ObjectId>());
  }

  public PartInContentEntry(ObjectId communityId,ObjectId detailId, ObjectId userId,String information, List<String> imageList,
                            List<AttachmentEntry> attachmentList,List<VideoEntry> videoList,String shareUrl,String shareImage,
                            String shareTitle,String sharePrice,String shareCommend,int type,List<ObjectId> zanUserIds){
    BasicDBList attachmentDbList = new BasicDBList();
    for (AttachmentEntry attachmentEntry : attachmentList) {
      attachmentDbList.add(attachmentEntry.getBaseEntry());
    }
    BasicDBList videoDBList = new BasicDBList();
    for (VideoEntry videoEntry : videoList) {
      videoDBList.add(videoEntry.getBaseEntry());
    }
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("cmid",communityId)
            .append("cdid", detailId)
            .append("uid", userId)
            .append("inf", information)
            .append("iml", imageList)
            .append("atl", attachmentDbList)
            .append("vil", videoDBList)
            .append("url", shareUrl)
            .append("til", shareTitle)
            .append("img", shareImage)
            .append("pri", sharePrice)
            .append("scm",shareCommend)
            .append("ty",type)
            .append("zan", 0)
            .append("zali",zanUserIds)
            .append("r",0);
    setBaseEntry(basicDBObject);
  }


  public PartInContentEntry(ObjectId communityId,ObjectId detailId, ObjectId userId,
                            String shareUrl,String shareImage,
                            String shareTitle,String sharePrice,String shareCommend,int type){
    this(communityId,detailId,userId,Constant.EMPTY,new ArrayList<String>(),new ArrayList<AttachmentEntry>(),
            new ArrayList<VideoEntry>(),shareUrl,shareImage,shareTitle,sharePrice,shareCommend,type,new ArrayList<ObjectId>());
  }


  /**
   * 纯文本
   *
   * @param detailId
   * @param userId
   * @param information
   */
  public PartInContentEntry(ObjectId communityId,ObjectId detailId, ObjectId userId, String information,int type) {
       this(communityId,detailId,userId,information,new ArrayList<String>(),new ArrayList<AttachmentEntry>(),
               new ArrayList<VideoEntry>(),Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,Constant.EMPTY,type,new ArrayList<ObjectId>());
  }



  public ObjectId getCommunityId() {
    return getSimpleObjecIDValue("cmid");
  }

  public ObjectId getDetailId() {
    return getSimpleObjecIDValue("cdid");
  }

  public ObjectId getUserId() {
    return getSimpleObjecIDValue("uid");
  }

  public void setUserId(ObjectId userId) {
    setSimpleValue("uid", userId);
  }

  public String getInformation() {
    return getSimpleStringValue("inf");
  }

  public void setInformation(String information) {
    setSimpleValue("inf", information);
  }

  public int getZan() {
    if (getBaseEntry().containsKey("zan")) {
      return getSimpleIntegerValue("zan");
    }
    return 0;
  }

  public List<ObjectId> getZanList() {
    if (getBaseEntry().containsKey("zali")) {
      return (List<ObjectId>) getBaseEntry().get("zali");
    }
    return new ArrayList<ObjectId>();
  }

  public List<String> getImageList() {
    BasicDBList list = getDbList("iml");
    List<String> attachmentEntries = new ArrayList<String>();
    for (Object dbo : list) {
      attachmentEntries.add((String) dbo);
    }
    return attachmentEntries;
  }

  public List<VideoEntry> getVedioList() {
    BasicDBList list = getDbList("vil");
    List<VideoEntry> attachmentEntries = new ArrayList<VideoEntry>();
    for (Object dbo : list) {
      BasicDBObject dbObject = (BasicDBObject) dbo;
      attachmentEntries.add(new VideoEntry(dbObject));
    }
    return attachmentEntries;
  }

  public List<AttachmentEntry> getAttachementList() {
    BasicDBList list = getDbList("atl");
    List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
    for (Object dbo : list) {
      BasicDBObject dbObject = (BasicDBObject) dbo;
      attachmentEntries.add(new AttachmentEntry(dbObject));
    }
    return attachmentEntries;
  }

  public String getShareUrl(){
    return getField("url");
  }

  public String getShareImage(){
    return getField("img");
  }

  public String getShareTitle(){
    return getField("til");
  }

  public String getSharePrice(){
    return getField("pri");
  }

  public String getShareCommend(){
    return getField("scm");
  }

  public int getType(){
    return getSimpleIntegerValueDef("ty",0);
  }

  public int getRemove(){
    return getSimpleIntegerValueDef("ty",0);
  }

  public String getField(String field){
    if(getBaseEntry().containsField(field)){
      return getSimpleStringValue(field);
    }else{
      return Constant.EMPTY;
    }
  }

  public int getMark(){
    return getSimpleIntegerValueDef("mk",-1);
  }

  public void setMark(int mark){
    setSimpleValue("mk",mark);
  }


}
