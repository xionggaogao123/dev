package com.db.emailmanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.emailmanage.EmailManageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2015/4/20.
 */
public class EmailManageDao extends BaseDao {

    /**
     * 添加日志信息
     *
     * @param e
     * @return
     */
    public ObjectId addEmailManageEntry(EmailManageEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EMAIL_MANAGE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据type查询
     *
     * @param type
     * @return
     */
    public EmailManageEntry getEmailManageEntry(int type) {
        DBObject query = new BasicDBObject("ty", type).append("fl", Constant.ONE);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EMAIL_MANAGE, query, Constant.FIELDS);
        if (null != dbo) {
            return new EmailManageEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public void updEmailManageEntry(EmailManageEntry e) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, e.getID());
        BasicDBObject updateValue = new BasicDBObject("ems", e.getEmails())
                .append("ty", e.getType())
                .append("udf", e.getUserDef())
                .append("soip", e.getServerOuterIp())
                .append("siip", e.getServerInnerIp());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EMAIL_MANAGE, query, update);
    }
}
