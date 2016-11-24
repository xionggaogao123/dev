package com.fulaan.dto;

import com.pojo.forum.FPostDTO;
import com.pojo.forum.FReplyDTO;
import com.pojo.user.UserInfoDTO;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/9/18.
 * 消息 DTO
 */
public class MessageDTO {

  private ObjectId id;
  private int type;
  private String content;
  private long time;
  private UserInfoDTO user;
  private FPostDTO post;
  private FReplyDTO reply;

  public String getId() {
    return id.toString();
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public com.pojo.user.UserInfoDTO getUser() {
    return user;
  }

  public void setUser(com.pojo.user.UserInfoDTO user) {
    this.user = user;
  }

  public com.pojo.forum.FPostDTO getPost() {
    return post;
  }

  public void setPost(com.pojo.forum.FPostDTO post) {
    this.post = post;
  }

  public com.pojo.forum.FReplyDTO getReply() {
    return reply;
  }

  public void setReply(com.pojo.forum.FReplyDTO reply) {
    this.reply = reply;
  }
}
