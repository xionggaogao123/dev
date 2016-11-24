package com.pojo.businessactivity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 火热活动
 * <pre>
 * collectionName:fieryactivitys
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  con:内容
 *  bt:开始时间
 *  et:结束时间
 *  rl:活动角色
 *  te:即时生效  0：生效，1：未生效
 *  pif:活动广告图片
 *  pin:活动广告图片名称
 *  ppif:手机端活动广告图片
 *  ppin:手机端活动广告图片名称
 *  aty:活动类型
 *  if:活动是否结束
 *  ct:创建时间
 *  cb:创建人
 *  st:是否删除
 *  df:文档地址
 *  [
 *   {
 *    id:
 *    nm:
 *    v:
 *   }
 *  ]
 * }
 * ia : 活动是否是全部可看，1：是，0：否
 * eis : 当ia==0时，可以看到活动的教育局
 * </pre>
 * @author guojing on 2015/7/30.
 */
public class FieryActivityEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = -8455687526184995858L;

    public FieryActivityEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public FieryActivityEntry(String name, String content,String startDate,String endDate,
                              int checkRole, int takeEffect, String picFile, String picName,
                              String phonePicFile,String phonePicName,int type,int isFinish,
                              Long createTime, ObjectId createBy, List<IdNameValuePair> docFile,
                              int isAll, List<ObjectId> eduIds)
    {
        super();
        DateTimeUtils time=new DateTimeUtils();
        long beginTime=0l;
        if(startDate!=null&&!"".equals(startDate)){
            beginTime=time.getStrToLongTime(startDate+" 00:00:00");
        }
        long endTime=0l;
        if(endDate!=null&&!"".equals(endDate)){
            endTime=time.getStrToLongTime(endDate+" 23:59:59");
        }
        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", name)
                .append("con", content)
                .append("bt", beginTime)
                .append("et", endTime)
                .append("rl", checkRole)
                .append("te", takeEffect)
                .append("pif", picFile)
                .append("pin", picName)
                .append("ppif", phonePicFile)
                .append("ppin", phonePicName)
                .append("aty", type)
                .append("if", isFinish)
                .append("ct", createTime)
                .append("cb", createBy)
                .append("st", DeleteState.NORMAL.getState())
                .append("df", MongoUtils.convert(MongoUtils.fetchDBObjectList(docFile)))
                .append("ia", isAll)
                .append("eis", eduIds);
        setBaseEntry(baseEntry);
    }

    public FieryActivityEntry(String name, String content,String startDate,String endDate,
                              int checkRole, int takeEffect, String picFile, String picName,
                              String phonePicFile,String phonePicName,List<IdNameValuePair> docFile,
                              int isAll, List<ObjectId> eduIds)
    {
        super();
        DateTimeUtils time=new DateTimeUtils();
        long beginTime=0l;
        if(startDate!=null&&!"".equals(startDate)){
            beginTime=time.getStrToLongTime(startDate+" 00:00:00");
        }
        long endTime=0l;
        if(endDate!=null&&!"".equals(endDate)){
            endTime=time.getStrToLongTime(endDate+" 23:59:59");
        }
        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", name)
                .append("con", content)
                .append("bt", beginTime)
                .append("et", endTime)
                .append("rl", checkRole)
                .append("te", takeEffect)
                .append("pif", picFile)
                .append("pin", picName)
                .append("ppif", phonePicFile)
                .append("ppin", phonePicName)
                .append("st", DeleteState.NORMAL.getState())
                .append("df", MongoUtils.convert(MongoUtils.fetchDBObjectList(docFile)))
                .append("ia", isAll)
                .append("eis", eduIds);
        setBaseEntry(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getContent() {
        return getSimpleStringValue("con");
    }

    public void setContent(String content) {
        setSimpleValue("con", content);
    }

    public Long getBeginTime() {
        return getSimpleLongValue("bt");
    }

    public void setBeginTime(Long beginTime) {
        setSimpleValue("bt", beginTime);
    }

    public Long getEndTime() {
        return getSimpleLongValue("et");
    }

    public void setEndTime(Long endTime) {
        setSimpleValue("et", endTime);
    }

    public int getCheckRole() {
        return getSimpleIntegerValue("rl");
    }

    public void setCheckRole(int checkRole) {
        setSimpleValue("rl", checkRole);
    }

    public int getTakeEffect() {
        return getSimpleIntegerValue("te");
    }

    public void setTakeEffect(int takeEffect) {
        setSimpleValue("te", takeEffect);
    }

    public String getPicFile() {
        return getSimpleStringValue("pif");
    }

    public void setPicFile(String picFile) {
        setSimpleValue("pif", picFile);
    }

    public String getPicName() {
        return getSimpleStringValue("pin");
    }

    public void setPicName(String picName) {
        setSimpleValue("pin", picName);
    }


    public String getPhonePicFile() {
        return getSimpleStringValue("ppif");
    }

    public void setPhonePicFile(String phonePicFile) {
        setSimpleValue("ppif", phonePicFile);
    }

    public String getPhonePicName() {
        return getSimpleStringValue("ppin");
    }

    public void setPhonePicName(String phonePicName) {
        setSimpleValue("ppin", phonePicName);
    }

    public int getActivityType() {
        return getSimpleIntegerValue("aty");
    }

    public void setActivityType(int activityType) {
        setSimpleValue("aty", activityType);
    }

    public int getIsFinish() {
        return getSimpleIntegerValue("if");
    }

    public void setIsFinish(int isFinish) {
        setSimpleValue("if", isFinish);
    }

    public Long getCreateTime() {
        return getSimpleLongValue("ct");
    }

    public void setCreateTime(Long createTime) {
        setSimpleValue("ct", createTime);
    }

    public ObjectId getCreateBy() {
        return getSimpleObjecIDValue("cb");
    }

    public void setCreateBy(ObjectId createBy) {
        setSimpleValue("cb", createBy);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }

    public List<IdNameValuePair> getDocFile() {
        List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("df");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setDocFile(List<IdNameValuePair> df) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(df);
        setSimpleValue("df", MongoUtils.convert(list));
    }

    public int getIsAll() {
        return getSimpleIntegerValueDef("ia",1);
    }

    public void setIsAll(int isAll) {
        setSimpleValue("ia", isAll);
    }

    public List<ObjectId> getEduIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("eis");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setEduIds(List<ObjectId> eduIds) {
        setSimpleValue("eis", MongoUtils.convert(eduIds));
    }

}
