package com.pojo.competition;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *评比项目明细
 *
 *评比Id : coid(comId)
 *评比项目Id : itid(itemId)
 *项目明细名称 : itdn(itemDetailName)
 * Created by guojing on 2016/2/29.
 */
public class CompetitionItemDetailEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936854568592747487L;

    public CompetitionItemDetailEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public CompetitionItemDetailEntry(
            ObjectId comId,
            ObjectId itemId,
            String itemDetailName)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("coid", comId)
                .append("itid", itemId)
                .append("itdn", itemDetailName)
                ;
        setBaseEntry(dbo);
    }

    public ObjectId getComId() {
        return getSimpleObjecIDValue("coid");
    }

    public void setComId(ObjectId comId) {
        setSimpleValue("coid", comId);
    }

    public ObjectId getItemId() {
        return getSimpleObjecIDValue("itid");
    }

    public void setItemId(ObjectId itemId) {
        setSimpleValue("itid", itemId);
    }

    public String getItemDetailName() {
        return getSimpleStringValue("itdn");
    }

    public void setItemDetailName(String itemDetailName) {
        setSimpleValue("itdn",itemDetailName);
    }
}
