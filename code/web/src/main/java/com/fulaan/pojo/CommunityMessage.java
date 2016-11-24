package com.fulaan.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/10/25.
 * Message
 */
public class CommunityMessage {

  private String communityId;
  private String title;
  private String content;
  private int type;
  private String shareUrl;
  private String shareImage;
  private String shareTitle;
  private String sharePrice;
  private List<Attachement> attachements = new ArrayList<Attachement>();
  private List<Attachement> vedios = new ArrayList<Attachement>();
  private List<Attachement> images = new ArrayList<Attachement>();

  public String getCommunityId() {
    return communityId;
  }

  public void setCommunityId(String communityId) {
    this.communityId = communityId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  public String getShareImage() {
    return shareImage;
  }

  public void setShareImage(String shareImage) {
    this.shareImage = shareImage;
  }

  public String getShareTitle() {
    return shareTitle;
  }

  public void setShareTitle(String shareTitle) {
    this.shareTitle = shareTitle;
  }

  public String getSharePrice() {
    return sharePrice;
  }

  public void setSharePrice(String sharePrice) {
    this.sharePrice = sharePrice;
  }

  public List<Attachement> getAttachements() {
    return attachements;
  }

  public void setAttachements(List<Attachement> attachements) {
    this.attachements = attachements;
  }

  public List<Attachement> getVedios() {
    return vedios;
  }

  public void setVedios(List<Attachement> vedios) {
    this.vedios = vedios;
  }

  public List<Attachement> getImages() {
    return images;
  }

  public void setImages(List<Attachement> images) {
    this.images = images;
  }
}
