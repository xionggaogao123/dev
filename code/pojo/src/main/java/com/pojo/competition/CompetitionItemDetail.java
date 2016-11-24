package com.pojo.competition;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *评比项目明细
 *
 *评比项目Id : itdid(itemDetailId)
 *项目明细名称 : itdn(itemDetailName)
 * 项目明细说明 : itd(itemDetail)
 * Created by guojing on 2016/2/29.
 */
public class CompetitionItemDetail extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public CompetitionItemDetail(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public CompetitionItemDetail(String itemDetailId, String itemDetailName,String itemDetail) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("itdid", new ObjectId(itemDetailId))
                .append("itdn", itemDetailName)
                .append("itd", itemDetail);
        setBaseEntry(baseEntry);
    }

    public CompetitionItemDetail(String itemDetailName,String itemDetail) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("itdn", itemDetailName)
                .append("itd", itemDetail);
        setBaseEntry(baseEntry);
    }


    public ObjectId getItemDetailId() {
        return getSimpleObjecIDValue("itdid");
    }

    public void setItemDetailId(ObjectId itemDetailId) {
        setSimpleValue("itdid", itemDetailId);
    }

    public String getItemDetailName() {
        return getSimpleStringValue("itdn");
    }

    public void setItemDetailName(String itemDetailName) {
        setSimpleValue("itdn",itemDetailName);
    }

    public String getItemDetail() {
        return getSimpleStringValue("itd");
    }

    public void setItemDetail(String itemDetail) {
        setSimpleValue("itd",itemDetail);
    }
}
