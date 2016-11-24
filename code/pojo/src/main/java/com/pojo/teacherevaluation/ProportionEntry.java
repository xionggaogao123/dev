package com.pojo.teacherevaluation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 评价比重  te_proportion
 * Created by fl on 2016/4/19.
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * lgp  领导小组比重  =  领导小组校领导比重 + 领导小组成员比重
 * ldp   领导小组校领导比重
 * gp   领导小组成员比重
 * hp   组内互评比重
 * lp   量化评分比重
 */
public class ProportionEntry extends BaseDBObject {

    public ProportionEntry(){}

    public ProportionEntry(ObjectId schoolId, String year){
        this(schoolId, year, 0, 0, 0, 0, 0);
    }

    public ProportionEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public ProportionEntry(ObjectId schoolId, String year, double leadGroupPro, double leaderPro, double groupPro, double huPingPro, double liangHuaPro){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("lgp", leadGroupPro)
                .append("ldp", leaderPro)
                .append("gp", groupPro)
                .append("hp", huPingPro)
                .append("lhp", liangHuaPro)
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("si", schoolId);
    }

    public String getYear(){
        return getSimpleStringValue("y");
    }

    public void setYear(String year){
        setSimpleValue("y", year);
    }

    public double getLeadGroupPro(){
        return getSimpleDoubleValue("lgp");
    }

    public void setLeadGroupPro(int pro){
        setSimpleValue("lgp", pro);
    }

    public double getLeaderPro(){
        return getSimpleDoubleValue("ldp");
    }

    public void setLeaderPro(int pro){
        setSimpleValue("ldp", pro);
    }

    public double getGroupPro(){
        return getSimpleDoubleValue("gp");
    }

    public void setGroupPro(int pro){
        setSimpleValue("gp", pro);
    }

    public double getHuPingPro(){
        return getSimpleDoubleValue("hp");
    }

    public void setHuPingPro(int pro){
        setSimpleValue("hp", pro);
    }

    public double getLiangHuaPro(){
        return getSimpleDoubleValue("lhp");
    }

    public void setLiangHuaPro(int pro){
        setSimpleValue("lhp", pro);
    }
}
