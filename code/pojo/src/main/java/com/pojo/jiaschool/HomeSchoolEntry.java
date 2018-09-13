package com.pojo.jiaschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/1.
 *
 *   sty:学校类型 ,详见SchoolType
 *   nm:学校名称
 *   enm:学校英文名称
 *   int:简介
 *   det:详情
 *   dom:主页，域名
 *   pc:邮编
 *   logo: logo图像地址;长方形图片，供k6kt使用;
 *   tp:电话
 *   so:仅仅用于排序
 *   inp:初始密码
 *   pr:省级行政区域
 *   add:地址
 *   ====新增====
 *   city:市级行政区域
 *   creationDate:创建时间
 *   schoolParagraph:学段 幼儿园 小学 初中等（可以多选）
 */

/**
 * SchoolType
 * 现在学校类型存的是 10教育局 20公立学校 30私立学校 40培训学校
 * 以前学校类型存的内容现在存在学段里面
 */
public class HomeSchoolEntry extends BaseDBObject {
    public HomeSchoolEntry(){

    }

    public HomeSchoolEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    //添加构造
    public HomeSchoolEntry(
            int schoolType,
            String name,
            String englishName,
            String introduce,
            String detail,
            String domain,
            String postCode,
            String telephone,
            int sort,
            String initialPassword,
            String province,
            String address
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("sty", schoolType)
                .append("nm", name)
                .append("enm", englishName)
                .append("int", introduce)
                .append("det", detail)
                .append("dom", domain)
                .append("pc", postCode)
                .append("tp", telephone)
                .append("so", sort)
                .append("inp", initialPassword)
                .append("pr", province)
                .append("add", address)
                .append("logo", "http://7xiclj.com1.z0.glb.clouddn.com/5a2674e3b0573026f932759b.png")
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public HomeSchoolEntry(
            ObjectId id,
            int schoolType,
            String name,
            String englishName,
            String introduce,
            String detail,
            String domain,
            String postCode,
            String telephone,
            int sort,
            String initialPassword,
            String province,
            String address
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("sty", schoolType)
                .append("nm", name)
                .append("enm", englishName)
                .append("int", introduce)
                .append("det", detail)
                .append("dom", domain)
                .append("pc", postCode)
                .append("tp", telephone)
                .append("so", sort)
                .append("inp", initialPassword)
                .append("pr", province)
                .append("add", address)
                .append("logo", "http://7xiclj.com1.z0.glb.clouddn.com/5a2674e3b0573026f932759b.png")
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //运营管理添加构造
    public HomeSchoolEntry(
            int schoolType,
            String name,
            int sort,
            String province,
            String address,
            String city,
            String creationDate,
            List<String> schoolParagraph//学段
    ) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sty", schoolType)
                .append("nm", name)
                .append("enm", "")
                .append("int", "")
                .append("det", "")
                .append("dom", "")
                .append("pc", "")
                .append("tp", "")
                .append("so", sort)
                .append("inp", "")
                .append("pr", province)
                .append("add", address)
                .append("city", city)
                .append("creationDate", creationDate)
                .append("schoolParagraph", schoolParagraph)
                .append("logo", "http://7xiclj.com1.z0.glb.clouddn.com/5a2674e3b0573026f932759b.png")
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //运营管理修改构造
    public HomeSchoolEntry(
            ObjectId id,
            int schoolType,
            String name,
            int sort,
            String province,
            String address,
            String city,
            String creationDate,
            List<String> schoolParagraph//学段
    ) {
        BasicDBObject dbObject = new BasicDBObject()
                .append(Constant.ID, id)
                .append("sty", schoolType)
                .append("nm", name)
//                .append("enm", "")
//                .append("int", "")
//                .append("det", "")
//                .append("dom", "")
//                .append("pc", "")
//                .append("tp", "")
                .append("so", sort)
//                .append("inp", "")
                .append("pr", province)
                .append("add", address)
                .append("city", city)
                .append("creationDate", creationDate)
                .append("schoolParagraph", schoolParagraph)
                .append("logo", "http://7xiclj.com1.z0.glb.clouddn.com/5a2674e3b0573026f932759b.png")
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public String getLogo() {
        return getSimpleStringValue("logo");
    }
    public void setLogo(String logo) {
        setSimpleValue("logo", logo);
    }
    public int getSchoolType() {
        return getSimpleIntegerValue("sty");
    }
    public void setSchoolType(int schoolType) {
        setSimpleValue("sty", schoolType);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }
    public String getEnglishName() {
        return getSimpleStringValue("enm");
    }
    public void setEnglishName(String englishName) {
        setSimpleValue("enm", englishName);
    }
    public String getIntroduce() {
        return getSimpleStringValue("int");
    }
    public void setIntroduce(String introduce) {
        setSimpleValue("int", introduce);
    }


    public String getDetail() {
        return getSimpleStringValue("det");
    }
    public void setDetail(String detail) {
        setSimpleValue("det", detail);
    }



    public String getDomain() {
        return getSimpleStringValue("dom");
    }
    public void setDomain(String domain) {
        setSimpleValue("dom", domain);
    }
    public String getPostCode() {
        return getSimpleStringValue("pc");
    }
    public void setPostCode(String postCode) {
        setSimpleValue("pc", postCode);
    }
    public String getTelephone() {
        return getSimpleStringValue("tp");
    }
    public void setTelephone(String telephone) {
        setSimpleValue("tp", telephone);
    }


    public int getSort() {
        return getSimpleIntegerValue("so");
    }
    public void setSort(int sort) {
        setSimpleValue("so", sort);
    }
    public String getInitialPassword() {
        return getSimpleStringValue("inp");
    }
    public void setInitialPassword(String initialPassword) {
        setSimpleValue("inp", initialPassword);
    }

    public String getProvince() {
        return getSimpleStringValue("pr");
    }
    public void setProvince(ObjectId province) {
        setSimpleValue("pr", province);
    }
    public String getAddress() {
        return getSimpleStringValue("add");
    }
    public void setAddress(String address) {
        setSimpleValue("add", address);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public String getCity(){
        return getSimpleStringValue("city");
    }
    public String getCreationDate(){
        return getSimpleStringValue("creationDate");
    }

    public List<String> getSchoolParagraph(){
        return (ArrayList)getSimpleObjectValue("schoolParagraph");
    }
}
