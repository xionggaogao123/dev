package com.pojo.excellentCourses;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 * 精品课程表
 * id               id                        id
 * userId           开课用户                  uid
 * subjectId        学科id                    sid
 * subjectName      学科名称                  snm
 * userName         自定义开课者姓名          unm
 * cover            封面                      img
 * title            课程题目                  tit
 * target           课程目标                  tar
 * communityIdList  选择的社群list            cis  (List<ObjectId>)
 * allClassCount    总课时                    acc
 * createTime       创建时间                  ctm
 * startTime        开始时间                  stm
 * endTime          结束时间                  etm
 * oldPrice         期望总收益                opc
 * newPrice         系统总收益                npc
 * studentNumber    上课人数                  stn
 * status           审批状态                  sta      0  编辑     1 审批中    2 通过        3 拒绝
 * type             课程状态                  typ      0  正常     1 结束      2 未开始      3 进行中
 *
 */
public class ExcellentCoursesEntry extends BaseDBObject {

    public ExcellentCoursesEntry(){

    }

    public ExcellentCoursesEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public ExcellentCoursesEntry(
            ObjectId userId,
            ObjectId subjectId,
            String subjectName,
            String userName,
            String cover,
            String title,
            String target,
            int allClassCount,
            List<ObjectId> communityIdList,
            long startTime,
            long endTime,
            int oldPrice,
            int newPrice,
            int studentNumber,
            int status,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("sid", subjectId)
                .append("snm", subjectName)
                .append("unm", userName)
                .append("cov", cover)
                .append("tit", title)
                .append("tar", target)
                .append("acc", allClassCount)
                .append("clt", communityIdList)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("opc", oldPrice)
                .append("npc", newPrice)
                .append("stn", studentNumber)
                .append("sta", status)
                .append("typ",type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ExcellentCoursesEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId subjectId,
            String subjectName,
            String userName,
            String cover,
            String title,
            String target,
            int allClassCount,
            List<ObjectId> communityIdList,
            long startTime,
            long endTime,
            int oldPrice,
            int newPrice,
            int studentNumber,
            int status,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("sid", subjectId)
                .append("snm", subjectName)
                .append("unm", userName)
                .append("cov", cover)
                .append("tit", title)
                .append("tar", target)
                .append("acc", allClassCount)
                .append("clt", communityIdList)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("opc", oldPrice)
                .append("npc", newPrice)
                .append("stn", studentNumber)
                .append("sta", status)
                .append("typ",type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public String getUserName(){
        return getSimpleStringValue("unm");
    }
    public void setUserName(String userName){
        setSimpleValue("unm", userName);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }
    public String getSubjectName(){
        return getSimpleStringValue("snm");
    }
    public void setSubjectName(String subjectName){
        setSimpleValue("snm", subjectName);
    }
    public String getCover(){
        return getSimpleStringValue("cov");
    }
    public void setCover(String cover){
        setSimpleValue("cov", cover);
    }
    public String getTitle(){
        return getSimpleStringValue("tit");
    }
    public void setTitle(String title){
        setSimpleValue("tit", title);
    }
    public String getTarget(){
        return getSimpleStringValue("tar");
    }
    public void setTarget(String target){
        setSimpleValue("tar", target);
    }

    public int getAllClassCount(){
        return getSimpleIntegerValue("acc");
    }

    public void setAllClassCount(int allClassCount){
        setSimpleValue("acc",allClassCount);
    }

    public long getStartTime(){
        return getSimpleLongValue("stm");
    }

    public void setStartTime(long startTime){
        setSimpleValue("stm",startTime);
    }
    public long getEndTime(){
        return getSimpleLongValue("etm");
    }

    public void setEndTime(long endTime){
        setSimpleValue("etm",endTime);
    }
    public void setCommunityIdList(List<ObjectId> communityIdList){
        setSimpleValue("clt", MongoUtils.convert(communityIdList));
    }

    public List<ObjectId> getCommunityIdList(){
        ArrayList<ObjectId> communityIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                communityIdList.add((ObjectId)obj);
            }
        }
        return communityIdList;
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }


    public int getOldPrice(){
        return getSimpleIntegerValue("opc");
    }

    public void setOldPrice(int oldPrice){
        setSimpleValue("opc",oldPrice);
    }

    public int getNewPrice(){
        return getSimpleIntegerValue("npc");
    }

    public void setNewPrice(int newPrice){
        setSimpleValue("npc",newPrice);
    }
    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public int getStudentNumber(){
        return getSimpleIntegerValue("stn");
    }

    public void setStudentNumber(int studentNumber){
        setSimpleValue("stn",studentNumber);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}