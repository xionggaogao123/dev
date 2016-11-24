package com.pojo.ebusiness;


import com.pojo.app.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/1/13.
 */
public class EGoodsDTO {

    private String goodsId;
    private String goodsName;
    private String introduction;
    private int price;
    private String suggestImage;
    private List<IdValuePairDTO1> images = new ArrayList<IdValuePairDTO1>();
    private String html;
    private int state;
    private int sellCount;
    private List<KindDTO> kindDTOList = new ArrayList<KindDTO>();
    private List<NameValuePairDTO> commentSummary = new ArrayList<NameValuePairDTO>();
    private int popularLevel;
    private String brand;
    private List<String> goodsCategoryList = new ArrayList<String>();
    private List<String> levelGoodsCategoryList = new ArrayList<String>();
    private List<String> gradeCategoryList = new ArrayList<String>();
    private int bookCategory;
    private int discountPrice;
    private int experienceOff;
    private int voucherOff;
    private String expressTemplateId;
    private int activity;
    private int groupPurchase;
    private int maxOut;

    public EGoodsDTO(){}

    public EGoodsDTO(EGoodsEntry eGoodsEntry){
        this.goodsId = eGoodsEntry.getID().toString();
        this.goodsName = eGoodsEntry.getName();
        this.introduction = eGoodsEntry.getIntroduce();
        this.price = eGoodsEntry.getPr();
        this.suggestImage = eGoodsEntry.getSuggestImage();
        List<IdValuePair> valuePairs = eGoodsEntry.getIms();
        if(null != valuePairs){
            for(IdValuePair pair : valuePairs){
                this.images.add(new IdValuePairDTO1(pair));
            }
        }
        this.html = eGoodsEntry.getHtmls();
        this.state = eGoodsEntry.getState();
        this.sellCount = eGoodsEntry.getSellCount();
        List<EGoodsEntry.Kind> kinds = eGoodsEntry.getKindList();
        if(null != kinds){
            for(EGoodsEntry.Kind kind : kinds){
                this.kindDTOList.add(new KindDTO(kind));
            }
        }
        List<NameValuePair> cs = eGoodsEntry.getCommentSummary();
        if(null != cs){
            for(NameValuePair nameValuePair : cs){
                this.commentSummary.add(new NameValuePairDTO(nameValuePair));
            }
        }
        this.popularLevel = eGoodsEntry.getPopularLevel();
        this.brand = eGoodsEntry.getPinpai();
        for(ObjectId id : eGoodsEntry.getGoodsCategoryList()){
            this.goodsCategoryList.add(id.toString());
        }
        for(ObjectId id : eGoodsEntry.getLevelGoodsCategoryList()){
            this.levelGoodsCategoryList.add(id.toString());
        }
        for(ObjectId id : eGoodsEntry.getGradeCategoryList()){
            this.gradeCategoryList.add(id.toString());
        }
        this.bookCategory = eGoodsEntry.getBookCategory();
        this.discountPrice = eGoodsEntry.getDiscountPrice();
        this.experienceOff = eGoodsEntry.getExperienceOff();
        this.voucherOff = eGoodsEntry.getVoucherOff();

        if(eGoodsEntry.getExpTempId() != null){
            this.expressTemplateId = eGoodsEntry.getExpTempId().toString();
        }else {
            this.expressTemplateId = "";
        }
        this.activity=eGoodsEntry.getActivity();
        this.groupPurchase=eGoodsEntry.getGroupPurchase();
        this.maxOut=eGoodsEntry.getMaxOut();
    }

    public EGoodsEntry exportEntry(){
        EGoodsEntry eGoodsEntry = new EGoodsEntry();
        if(!goodsId.equals("")){
            eGoodsEntry.setID(new ObjectId(goodsId));
        }
        eGoodsEntry.setName(goodsName);
        eGoodsEntry.setPinpai(brand);
        eGoodsEntry.setIntroduce(introduction);
        eGoodsEntry.setPr(price);
        eGoodsEntry.setSuggestImage(suggestImage);
        List<IdValuePair> imgs = new ArrayList<IdValuePair>();
        if(null != images){
            for(IdValuePairDTO1 img : images){
                imgs.add(new IdValuePair(new ObjectId(img.getId()), img.getValue()));
            }
        }
        eGoodsEntry.setIms(imgs);
        eGoodsEntry.setHtmls(html);
        eGoodsEntry.setState(state);
        eGoodsEntry.setActivity(activity);
        eGoodsEntry.setGroupPurchase(groupPurchase);
        eGoodsEntry.setMaxOut(maxOut);
        eGoodsEntry.setSellCount(sellCount);
        eGoodsEntry.setPopularLevel(popularLevel);
        List<EGoodsEntry.Kind> kindList = new ArrayList<EGoodsEntry.Kind>();
        if(null != kindDTOList){
            for(KindDTO kindDTO : kindDTOList){
                kindList.add(kindDTO.exportEntry());
            }
        }
        eGoodsEntry.setKindList(kindList);
        List<NameValuePair> commentSummaryList = new ArrayList<NameValuePair>();
        if(null != commentSummary){
            for(NameValuePairDTO cs : commentSummary){
                commentSummaryList.add(new NameValuePair(cs.getName(), cs.getValue()));
            }
        }
        eGoodsEntry.setCommentSummary(commentSummaryList);
        List<ObjectId> goodsCategorys = new ArrayList<ObjectId>();
        if(goodsCategoryList.size() > 0){
            for(String id : goodsCategoryList){
                goodsCategorys.add(new ObjectId(id));
            }
        }
        eGoodsEntry.setGoodsCategoryList(goodsCategorys);
        List<ObjectId> levelGoodsCategorys = new ArrayList<ObjectId>();
        if(levelGoodsCategoryList.size() > 0){
            for(String id : levelGoodsCategoryList){
                levelGoodsCategorys.add(new ObjectId(id));
            }
        }
        eGoodsEntry.setLevelGoodsCategoryList(levelGoodsCategorys);
        List<ObjectId> gradeCategories = new ArrayList<ObjectId>();
        if(gradeCategoryList.size() > 0){
            for(String id : gradeCategoryList){
                gradeCategories.add(new ObjectId(id));
            }
        }
        eGoodsEntry.setGradeCategoryList(gradeCategories);

        eGoodsEntry.setBookCategory(bookCategory);
        eGoodsEntry.setDiscountPrice(discountPrice);
        eGoodsEntry.setExperienceOff(experienceOff);
        eGoodsEntry.setVoucherOff(voucherOff);

        eGoodsEntry.setExpTempId(new ObjectId(expressTemplateId));
        return eGoodsEntry;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        goodsName = goodsName.replace("\"","");
        goodsName = goodsName.replaceAll("[\n\r]"," ");
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSuggestImage() {
        return suggestImage;
    }

    public void setSuggestImage(String suggestImage) {
        this.suggestImage = suggestImage;
    }

    public List<IdValuePairDTO1> getImages() {
        return images;
    }

    public void setImages(List<IdValuePairDTO1> images) {
        this.images = images;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    public List<KindDTO> getKindDTOList() {
        return kindDTOList;
    }

    public void setKindDTOList(List<KindDTO> kindDTOList) {
        this.kindDTOList = kindDTOList;
    }

    public List<NameValuePairDTO> getCommentSummary() {
        return commentSummary;
    }

    public void setCommentSummary(List<NameValuePairDTO> commentSummary) {
        this.commentSummary = commentSummary;
    }

    public int getPopularLevel() {
        return popularLevel;
    }

    public void setPopularLevel(int popularLevel) {
        this.popularLevel = popularLevel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getGoodsCategoryList() {
        return goodsCategoryList;
    }

    public void setGoodsCategoryList(List<String> goodsCategoryList) {
        this.goodsCategoryList = goodsCategoryList;
    }

    public List<String> getLevelGoodsCategoryList() {
        return levelGoodsCategoryList;
    }

    public void setLevelGoodsCategoryList(List<String> levelGoodsCategoryList) {
        this.levelGoodsCategoryList = levelGoodsCategoryList;
    }
    public List<String> getGradeCategoryList() {
        return gradeCategoryList;
    }

    public void setGradeCategoryList(List<String> gradeCategoryList) {
        this.gradeCategoryList = gradeCategoryList;
    }

    public int getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(int bookCategory) {
        this.bookCategory = bookCategory;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getExperienceOff() {
        return experienceOff;
    }

    public void setExperienceOff(int experienceOff) {
        this.experienceOff = experienceOff;
    }

    public int getVoucherOff() {
        return voucherOff;
    }

    public void setVoucherOff(int voucherOff) {
        this.voucherOff = voucherOff;
    }

    public String getExpressTemplateId() {
        return expressTemplateId;
    }

    public void setExpressTemplateId(String expressTemplateId) {
        this.expressTemplateId = expressTemplateId;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getGroupPurchase() {
        return groupPurchase;
    }

    public void setGroupPurchase(int groupPurchase) {
        this.groupPurchase = groupPurchase;
    }

    public int getMaxOut() {
        return maxOut;
    }

    public void setMaxOut(int maxOut) {
        this.maxOut = maxOut;
    }
}
