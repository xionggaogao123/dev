package com.fulaan.jiaschool.dto;

import com.pojo.jiaschool.HomeSchoolEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by James on 2018/1/30.
 */
public class HomeSchoolDTO {

    private String id;
    private int schoolType;
    private String name;
    private String englishName;
    private String introduce;
    private String detail;
    private String domain;
    private String postCode;
    private String telephone;
    private int sort;
    private String initialPassword;
    private String province;
    private String address;
    private String logo;
    private int open;

    //运营管理
    private String city;
    private String creationDate;
    private String schoolParagraphStr;
    private List<String> schoolParagraph;
    private String communityCount;

    public HomeSchoolDTO(){

    }
    public HomeSchoolDTO(HomeSchoolEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.name = e.getName();
            this.schoolType = e.getSchoolType();
            this.englishName = e.getEnglishName();
            this.introduce = e.getIntroduce();
            this.detail = e.getDetail();
            this.domain = e.getDomain();
            this.postCode = e.getPostCode();
            this.telephone = e.getTelephone();
            this.sort = e.getSort();
            this.initialPassword = e.getInitialPassword();
            this.province = e.getProvince();
            this.address =e.getAddress();
            this.logo = e.getLogo();

            this.city = e.getCity();
            this.creationDate = e.getCreationDate();
            this.schoolParagraph = e.getSchoolParagraph();
        }else{
            new HomeSchoolDTO();
        }
    }

    public HomeSchoolEntry buildAddEntry(){
        HomeSchoolEntry openEntry =
                new HomeSchoolEntry(
                        this.schoolType,
                        this.name,
                        this.englishName,
                        this.introduce,
                        this.detail,
                        this.domain,
                        this.postCode,
                        this.telephone,
                        this.sort,
                        this.initialPassword,
                        this.province,
                        this.address
                );
        return openEntry;

    }
    public HomeSchoolEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        HomeSchoolEntry openEntry =
                new HomeSchoolEntry(
                        Id,
                        this.schoolType,
                        this.name,
                        this.englishName,
                        this.introduce,
                        this.detail,
                        this.domain,
                        this.postCode,
                        this.telephone,
                        this.sort,
                        this.initialPassword,
                        this.province,
                        this.address
                );
        return openEntry;

    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(int schoolType) {
        this.schoolType = schoolType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getSchoolParagraphStr() {
        return schoolParagraphStr;
    }

    public void setSchoolParagraphStr(String schoolParagraphStr) {
        this.schoolParagraphStr = schoolParagraphStr;
    }

    public List<String> getSchoolParagraph() {
        return schoolParagraph;
    }

    public void setSchoolParagraph(List<String> schoolParagraph) {
        this.schoolParagraph = schoolParagraph;
    }

    public String getCommunityCount() {
        return communityCount;
    }

    public void setCommunityCount(String communityCount) {
        this.communityCount = communityCount;
    }
}
