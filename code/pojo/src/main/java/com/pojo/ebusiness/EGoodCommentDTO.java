package com.pojo.ebusiness;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO1;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/1/14.
 */
public class EGoodCommentDTO {
    private String eCommentId;
    private String userId;
    private String goodsId;
    private String orderId;
    private String content;
    private int score;
    private List<IdValuePairDTO1> images = new ArrayList<IdValuePairDTO1>();

    //=======================页面展示=======================
    private String userName;
    private String userAvatar;
    private List<KindDTO> kindDTOList = new ArrayList<KindDTO>();
    private String date;

    public EGoodCommentDTO(){}

    public EGoodCommentDTO(EGoodsCommentEntry entry){
        this.eCommentId = entry.getID().toString();
        this.userId = entry.getUserId().toString();
        this.goodsId = entry.getID().toString();
        this.orderId = entry.getOrderId().toString();
        this.content = entry.getContent();
        this.score = entry.getScore();
        List<IdValuePair> pairs = entry.getImageList();
        if(null != pairs){
            for(IdValuePair pair : pairs){
                this.images.add(new IdValuePairDTO1(pair));
            }
        }
    }

    public EGoodsCommentEntry exportEntry(){
        EGoodsCommentEntry entry = new EGoodsCommentEntry();
        entry.setUserId(new ObjectId(this.userId));
        entry.seteGoodsId(new ObjectId(this.goodsId));
        entry.setOrderId(new ObjectId(this.orderId));
        entry.setContent(this.content);
        entry.setScore(this.score);
        List<IdValuePair> imgs = new ArrayList<IdValuePair>();
        if(this.images.size() > 0){
            for(IdValuePairDTO1 img : images){
                imgs.add(new IdValuePair(new ObjectId(img.getId()), img.getValue()));
            }
        }
        entry.setImageList(imgs);
        return entry;
    }

    public String geteCommentId() {
        return eCommentId;
    }

    public void seteCommentId(String eCommentId) {
        this.eCommentId = eCommentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<IdValuePairDTO1> getImages() {
        return images;
    }

    public void setImages(List<IdValuePairDTO1> images) {
        this.images = images;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<KindDTO> getKindDTOList() {
        return kindDTOList;
    }

    public void setKindDTOList(List<KindDTO> kindDTOList) {
        this.kindDTOList = kindDTOList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
