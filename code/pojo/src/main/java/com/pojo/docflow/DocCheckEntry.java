package com.pojo.docflow;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * {
 *     id:审阅id---->id
 *     uid:审阅人Id--->userId
 *     did:审阅人部门Id---->departmentId
 *     op:审核意见---->opinion -1未处理 0同意 1驳回 2发布 3废弃 4转发
 *     re:审核备注--->remark
 * }
 * Created by qiangm on qiangm.
 */
public class DocCheckEntry extends BaseDBObject {
    public DocCheckEntry(BasicDBObject baseEntry)
    {
        super(baseEntry);
    }

    public DocCheckEntry(ObjectId id,ObjectId userId,ObjectId departmentId,Integer opinion,String remark)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("id",id)
                .append("uid",userId)
                .append("did",departmentId)
                .append("op",opinion)
                .append("re", remark);
        setBaseEntry(dbo);
    }
    public DocCheckEntry()
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("id",null)
                .append("uid", null)
                .append("did",null)
                .append("op",-1)
                .append("re","");
        setBaseEntry(dbo);
    }
    public ObjectId getId()
    {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId Id)
    {
        setSimpleValue("id",Id);
    }
    public ObjectId getUserId()
    {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId)
    {
        setSimpleValue("uid",userId);
    }
    public ObjectId getDepartmentId()
    {
        return getSimpleObjecIDValue("did");
    }
    public void setDepartmentId(ObjectId departmentId)
    {
        setSimpleValue("did",departmentId);
    }
    public Integer getOPinion()
    {
        return getSimpleIntegerValue("op");
    }
    public void setOpinion(Integer opinion)
    {
        setSimpleValue("op",opinion);
    }
    public String getRemark()
    {
        return getSimpleStringValue("re");
    }
    public void setRemark(String remark)
    {
        setSimpleValue("re",remark);
    }
}
