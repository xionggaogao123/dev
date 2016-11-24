package com.pojo.teacherevaluation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 评价比重  te_proportion
 * Created by fl on 2016/4/19.
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * evid  评价id  对应MemberGroupEntry _id  2016.7.29新增
 * lgp  领导小组比重  =  领导小组校领导比重 + 领导小组成员比重
 * ldp   领导小组校领导比重
 * gp   领导小组成员比重
 * hp   组内互评比重
 * ldmax 校领导分数去掉x个最高分  20160912新增 int 默认0
 * ldmin 校领导分数去掉x个最低分  20160912新增 int 默认0
 * gmax 领导小组成员分数去掉x个最高分  20160912新增 int 默认0
 * gmin 领导小组成员分数去掉x个最低分  20160912新增 int 默认0
 * hpmax 组内互评分数去掉x个最高分  20160912新增 int 默认0
 * hpmin 组内互评分数去掉x个最低分  20160912新增 int 默认0
 */
public class ProportionEntry extends BaseDBObject {

    public ProportionEntry(){}

    public ProportionEntry(ObjectId schoolId, String year, ObjectId evaluationId){
        this(schoolId, year, evaluationId, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public ProportionEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public ProportionEntry(ObjectId schoolId, String year, ObjectId evaluationId, double leadGroupPro, double leaderPro, double groupPro, double huPingPro,
                           int leaderMax, int leaderMin, int groupMax, int groupMin, int huPingMax, int huPingMin){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("evid", evaluationId)
                .append("lgp", leadGroupPro)
                .append("ldp", leaderPro)
                .append("gp", groupPro)
                .append("hp", huPingPro)
                .append("ldmax", leaderMax)
                .append("ldmin", leaderMin)
                .append("gmax", groupMax)
                .append("gmin", groupMin)
                .append("hpmax", huPingMax)
                .append("hpmin", huPingMin)
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

    public ObjectId getEvaluationId(){
        return getSimpleObjecIDValue("evid");
    }

    public void setEvaluationId(ObjectId evaluationId){
        setSimpleValue("evid", evaluationId);
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

    public int getLeaderMax() {
        return getSimpleIntegerValueDef("ldmax", 0);
    }

    public void setLeaderMax(int leaderMax) {
        setSimpleValue("ldmax", leaderMax);
    }

    public int getLeaderMin() {
        return getSimpleIntegerValueDef("ldmin", 0);
    }

    public void setLeaderMin(int leaderMin) {
        setSimpleValue("ldmin", leaderMin);
    }

    public int getGroupMax() {
        return getSimpleIntegerValueDef("gmax", 0);
    }

    public void setGroupMax(int groupMax) {
        setSimpleValue("gmax", groupMax);
    }

    public int getGroupMin() {
        return getSimpleIntegerValueDef("gmin", 0);
    }

    public void setGroupMin(int groupMin) {
        setSimpleValue("gmin", groupMin);
    }

    public int getHuPingMax() {
        return getSimpleIntegerValueDef("hpmax", 0);
    }

    public void setHuPingMax(int huPingMax) {
        setSimpleValue("hpmax", huPingMax);
    }

    public int getHuPingMin() {
        return getSimpleIntegerValueDef("hpmin", 0);
    }

    public void setHuPingMin(int huPingMin) {
        setSimpleValue("hpmin", huPingMin);
    }


}
