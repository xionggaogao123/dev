package com.pojo.indicator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拓展课评论
 * <pre>
 * collectionName:evaluateResult
 * </pre>
 * <pre>
 * {
 *  sid: 学校id
 *  aid: 活动ID(拓展课活动ID)
 *  cfid: 评价人id
 *  ctid: 被评价人
 *  tag: 评价标签
 *  apid:副本使用记录Id
 *  stid: 本次评价使用的评价副本Id（无副本时:指标体系id）
 *  crd:创建时间
 *  zbs:{
 *          zbid:指标id
 *          ty: 1根节点 2 叶子节点
 *          zbpid:父节点
 *          zbps：[{ id :唯一性标示 }]:父节点集合
 *          lel:级别
 *          sty: 打分种类
 *          ssc: 分值
 *  } 指标评价集合
 *  ir:是否有效 0 有效 1 失效
 * }
 * </pre>
 * Created by guojing on 2016/10/31.
 */
public class InterestEvaluateEntry extends BaseDBObject {
    private static final long serialVersionUID = 7933557028492747487L;

    public InterestEvaluateEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public InterestEvaluateEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InterestEvaluateEntry(
            ObjectId schoolId,
            ObjectId activityId,
            int termType,
            ObjectId commonFromId,
            ObjectId commonToId,
            String tag,
            String describe,
            ObjectId appliedId,
            ObjectId snapshotId,
            List<InterestEvaluate> zhiBiaoList
    ){
        super();
        List<DBObject> zhiBiaos = MongoUtils.fetchDBObjectList(zhiBiaoList);
        BasicDBObject dbo =new BasicDBObject()
                .append("sid", schoolId)
                .append("aid", activityId)
                .append("tty", termType)
                .append("cfid", commonFromId)
                .append("ctid", commonToId)
                .append("tag", tag)
                .append("des", describe)
                .append("apid", appliedId)
                .append("stid", snapshotId)
                .append("crd", new Date().getTime())
                .append("zbs", MongoUtils.convert(zhiBiaos))
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getActivityId() {
        return getSimpleObjecIDValue("aid");
    }

    public void setActivityId(ObjectId activityId) {
        setSimpleValue("aid", activityId);
    }

    public int getTermType() {
        return getSimpleIntegerValue("tty");
    }

    public void setTermType(int termType) {
        setSimpleValue("tty", termType);
    }

    public ObjectId getCommonFromId() {
        return getSimpleObjecIDValue("cfid");
    }

    public void setCommonFromId(ObjectId commonFromId) {
        setSimpleValue("cfid", commonFromId);
    }

    public ObjectId getCommonToId() {
        return getSimpleObjecIDValue("ctid");
    }

    public void setCommonToId(ObjectId commonToId) {
        setSimpleValue("ctid", commonToId);
    }

    public String getTag() {
        return getSimpleStringValue("tag");
    }

    public void setTag(String tag) {
        setSimpleValue("tag", tag);
    }

    public String getDescribe() {
        return getSimpleStringValueDef("des","");
    }

    public void setDescribe(String describe) {
        setSimpleValue("des", describe);
    }

    public ObjectId getAppliedId() {
        return getSimpleObjecIDValue("apid");
    }

    public void setAppliedId(ObjectId appliedId) {
        setSimpleValue("apid", appliedId);
    }

    public ObjectId getSnapshotId() {
        return getSimpleObjecIDValue("stid");
    }

    public void setSnapshotId(ObjectId snapshotId) {
        setSimpleValue("stid", snapshotId);
    }

    public long getCreateDate() {
        return getSimpleLongValueDef("crd",0l);
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("crd", createDate);
    }

    public List<InterestEvaluate> getZhiBiaos() {
        List<InterestEvaluate> retList =new ArrayList<InterestEvaluate>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("zbs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new InterestEvaluate((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setZhiBiaos(List<InterestEvaluate> zhiBiaoList) {
        List<DBObject> zhiBiaos = MongoUtils.fetchDBObjectList(zhiBiaoList);
        setSimpleValue("zbs", MongoUtils.convert(zhiBiaos));
    }

    public int getIsValid() {
        return getSimpleIntegerValueDef("ir",0);
    }

    public void setIsValid(int isValid) {
        setSimpleValue("ir", isValid);
    }

}
