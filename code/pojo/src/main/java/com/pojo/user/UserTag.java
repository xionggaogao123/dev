package com.pojo.user;

/**
 * Created by moslpc on 2016/11/7.
 */
public class UserTag {

  private int code;
  private String tag;

  public UserTag(int code,String tag){
    this.code=code;
    this.tag=tag;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}
