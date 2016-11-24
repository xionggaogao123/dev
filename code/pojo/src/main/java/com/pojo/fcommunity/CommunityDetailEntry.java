package com.pojo.fcommunity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by admin on 2016/10/24.
 * {
 * cmid:communityId 社区Id
 * cmuid:communityUserId 社区公告发布人Id
 * cmtl:communityTitle 社区内容标题
 * cmct:communityContent 社区内容
 * cmty:communityType 1:发公告 2：组织活动报名 3：火热分享 4：学习资料 5：布置作业 6：发布学习用品需求
 * unrl:unReadList 未读成员列表
 * pil:partInList 参与活动的人员列表
 * atl:attachmentList 附件列表
 * vil:voiceList 语音列表
 * iml:imageList 图片列表
 * picl:partInContentList 参与社区的发布的内容列表
 * {
 * uid:userId 参与人Id
 * inf:information 发表信息
 * iml:imageList 参与图片列表
 * atl:attachmentList 参与附件列表
 * vil:videoList 参与视频列表
 * {
 * vid:videoId 视频Id
 * vurl:videoUrl 视频路径
 * vimage:videoImage 视频封面
 * ti:创建时间
 * }
 * <p/>
 * }
 * //针对type=6学习用品
 * shul:shareUrl 分享地址
 * shim:shareImage 分享图片
 * shti:shareTitle 分享主题
 * shpr:sharePrice 分享价格
 * r:remove 0:未删除 1：已删除
 * <p/>
 * }
 */
public class CommunityDetailEntry extends BaseDBObject {

  public CommunityDetailEntry(DBObject dbObject) {
    setBaseEntry((BasicDBObject) dbObject);
  }

  public CommunityDetailEntry(ObjectId communityId, ObjectId communityUserId, String communityTitle,
                              String communityContent, int communityType,List<ObjectId> unReadList,
                              List<AttachmentEntry> attachmentList,
                              List<AttachmentEntry> voiceList, List<AttachmentEntry> imageList,
                              String shareUrl,String shareImage,String shareTitle,String sharePrice) {
    BasicDBList attachmentDbList = new BasicDBList();
    BasicDBList vedioDbList = new BasicDBList();
    BasicDBList imageDbList = new BasicDBList();
    for(AttachmentEntry attachmentEntry:attachmentList){
      attachmentDbList.add(attachmentEntry.getBaseEntry());
    }
    for(AttachmentEntry attachment:voiceList){
      vedioDbList.add(attachment.getBaseEntry());
    }
    for(AttachmentEntry attachment:imageList){
      imageDbList.add(attachment.getBaseEntry());
    }
    BasicDBObject basicDBObject = new BasicDBObject()
            .append("cmid", communityId)
            .append("cmuid", communityUserId)
            .append("cmtl", communityTitle)
            .append("cmct", communityContent)
            .append("cmty", communityType)
            .append("unrl",unReadList)
            .append("atl", attachmentDbList)
            .append("vil", vedioDbList)
            .append("iml", imageDbList)
            .append("shul",shareUrl)
            .append("shim",shareImage)
            .append("shti",shareTitle)
            .append("shpr",sharePrice)
            .append("ti", System.currentTimeMillis())
            .append("r", 0);
    setBaseEntry(basicDBObject);
  }

  public String getCommunityId() {
    return getSimpleStringValue("cmid");
  }

  public ObjectId getCommunityUserId() {
    return getSimpleObjecIDValue("cmuid");
  }

  public String getCommunityTitle() {
    return getSimpleStringValue("cmtl");
  }

  public String getCommunityContent() {
    return getSimpleStringValue("cmct");
  }

  public int getCommunityType() {
    return getSimpleIntegerValue("cmty");
  }

  public List<ObjectId> getUnReadList() {
    List<ObjectId> videoList = new ArrayList<ObjectId>();
    if (!getBaseEntry().containsField("unrl")) {
      return videoList;
    } else {
      BasicDBList list = (BasicDBList) getSimpleObjectValue("unrl");
      if (null != list && !list.isEmpty()) {
        for (Object o : list) {
          videoList.add((ObjectId) o);
        }
      }
      return videoList;
    }
  }

  public List<ObjectId> getPartInList() {
    List<ObjectId> videoList = new ArrayList<ObjectId>();
    if (!getBaseEntry().containsField("pil")) {
      return videoList;
    } else {
      BasicDBList list = (BasicDBList) getSimpleObjectValue("pil");
      if (null != list && !list.isEmpty()) {
        for (Object o : list) {
          videoList.add((ObjectId) o);
        }
      }
      return videoList;
    }
  }

  public List<AttachmentEntry> getAttachmentList() {
    return getAttachments("atl");
  }

  private List<AttachmentEntry> getAttachments(String field){
    List<AttachmentEntry> imageList = new ArrayList<AttachmentEntry>();
    if (!getBaseEntry().containsField(field)) {
      return imageList;
    } else {
      BasicDBList list = (BasicDBList) getSimpleObjectValue(field);
      for(Object o :list){
        AttachmentEntry attachmentEntry = new AttachmentEntry((BasicDBObject)o);
        imageList.add(attachmentEntry);
      }
      return imageList;
    }
  }

  public List<AttachmentEntry> getVoiceList() {
    return getAttachments("vil");
  }

  public List<AttachmentEntry> getImageList() {
    return getAttachments("iml");
  }

  public void setImageList(List<AttachmentEntry> imageList) {
    setList(imageList,"iml");
  }

  public long getCreateTime(){
    return getSimpleLongValue("ti");
  }


  public String getFiledData(String field){
    if(getBaseEntry().containsField(field)){
      return getSimpleStringValue(field);
    }else{
      return Constant.EMPTY;
    }
  }

  public String getShareUrl(){
    return getFiledData("shul");
  }

  public String getShareImage(){
    return getFiledData("shim");
  }

  public String getShareTitle(){
    return getFiledData("shti");
  }

  public String getSharePrice(){
    return getFiledData("shpr");
  }



  public int getRemove() {
    return getSimpleIntegerValue("r");
  }

  public void setRemove(int remove) {
    setSimpleValue("r", remove);
  }


  public void setList(Collection<? extends BaseDBObject> list, String field){
    List<DBObject> dbList = MongoUtils.fetchDBObjectList(list);
    setSimpleValue(field, MongoUtils.convert(dbList));
  }

}
