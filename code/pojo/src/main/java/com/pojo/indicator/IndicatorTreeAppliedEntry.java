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
 * <pre>
 * collectionName:indicatorTreeApplied
 * </pre>
 * <pre>
 * {
 *  nm: 评价名称
 *  sid: 学校id
 *  trid:指标体系id
 *  stid：本次评价使用的评价副本Id（无副本时:指标体系id）
 *  tty: 第几批次兴趣班标记
 *  egids: 被评价的群体
 *  evids： 评价者或者评价的群体
 *  evty： 评价者是个人还是群体
 *  ft： 评价的时间
 *  crid: 创建人Id
 *  crt: 创建时间
 *  upid:修改人Id
 *  upt:修改时间
 *  des:指标树描述
 *  ir:是否有效 0 有效 1 失效
 * }
 * </pre>
 * Created by guojing on 2016/11/14.
 */
public class IndicatorTreeAppliedEntry extends BaseDBObject {

    private static final long serialVersionUID = 7933557028492747487L;

    public IndicatorTreeAppliedEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public IndicatorTreeAppliedEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public IndicatorTreeAppliedEntry(
            String name,
            ObjectId schoolId,
            ObjectId treeId,
            ObjectId snapshotId,
            int termType,
            List<ObjectId> evaluateGroupIds,
            List<ObjectId> evaluaterIds,
            int evaluateType,
            long finishTime,
            ObjectId createrId,
            String describe
    ){
        super();
        long curTime = new Date().getTime();
        BasicDBObject dbo =new BasicDBObject()
                .append("nm", name)
                .append("sid", schoolId)
                .append("trid", treeId)
                .append("stid", snapshotId)
                .append("tty", termType)
                .append("egids", MongoUtils.convert(evaluateGroupIds))
                .append("evids", MongoUtils.convert(evaluaterIds))
                .append("evty", evaluateType)
                .append("ft", finishTime)
                .append("crid", createrId)
                .append("crt", curTime)
                .append("upid", createrId)
                .append("upt", curTime)
                .append("des", describe)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public IndicatorTreeAppliedEntry(
            String name,
            long finishTime,
            ObjectId updaterId,
            String describe
    ){
        super();
        long curTime = new Date().getTime();
        BasicDBObject dbo =new BasicDBObject()
                .append("nm", name)
                .append("ft", finishTime)
                .append("upid", updaterId)
                .append("upt", curTime)
                .append("des", describe);
        setBaseEntry(dbo);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getTreeId() {
        return getSimpleObjecIDValue("trid");
    }

    public void setTreeId(ObjectId treeId) {
        setSimpleValue("trid", treeId);
    }

    public ObjectId getSnapshotId() {
        return getSimpleObjecIDValue("stid");
    }

    public void setSnapshotId(ObjectId snapshotId) {
        setSimpleValue("stid", snapshotId);
    }

    public int getTermType() {
        return getSimpleIntegerValue("tty");
    }

    public void setTermType(int termType) {
        setSimpleValue("tty", termType);
    }

    public List<ObjectId> getEvaluateGroupIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("egids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;

    }

    public void setEvaluateGroupIds(List<ObjectId> evaluateGroupIds) {
        setSimpleValue("egids", MongoUtils.convert(evaluateGroupIds));
    }

    public List<ObjectId> getEvaluaterIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("evids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;

    }

    public void setEvaluaterIds(List<ObjectId> evaluaterIds) {
        setSimpleValue("evids", MongoUtils.convert(evaluaterIds));
    }

    public int getEvaluateType() {
        return getSimpleIntegerValue("evty");
    }

    public void setEvaluateType(int evaluateType) {
        setSimpleValue("evty", evaluateType);
    }

    public long getFinishTime() {
        return getSimpleLongValueDef("ft",0l);
    }

    public void setFinishTime(long finishTime) {
        setSimpleValue("ft", finishTime);
    }

    public ObjectId getCreaterId() {
        return getSimpleObjecIDValue("crid");
    }

    public void setCreaterId(ObjectId createrId) {
        setSimpleValue("crid", createrId);
    }

    public long getCreateDate() {
        return getSimpleLongValueDef("crt",0l);
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("crt", createDate);
    }

    public ObjectId getUpdaterId() {
        return getSimpleObjecIDValue("upid");
    }

    public void setUpdaterId(ObjectId updaterId) {
        setSimpleValue("upid", updaterId);
    }

    public long getUpdateDate() {
        return getSimpleLongValueDef("upt",0l);
    }

    public void setUpdateDate(long updateDate) {
        setSimpleValue("upt", updateDate);
    }

    public String getDescribe() {
        return getSimpleStringValue("des");
    }

    public void setDescribe(String describe) {
        setSimpleValue("des", describe);
    }

    public int getIsValid() {
        return getSimpleIntegerValueDef("ir",0);
    }

    public void setIsValid(int isValid) {
        setSimpleValue("ir", isValid);
    }

}
