package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.CertificateEntry;
import com.pojo.teachermanage.EducationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class CertificateDao extends BaseDao {
    /**
     * 增加证书信息
     *
     * @param e
     * @return
     */
    public ObjectId addCertificateEntry(CertificateEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CERTIFICATE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除证书信息
     * @param userId
     */
    public void removeCertificateEntry(ObjectId userId) {
        DBObject query =new BasicDBObject("ui",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CERTIFICATE, query);
    }

    /**
     * 获取证书信息
     * @param userId
     * @param fields
     * @return
     */
    public List<CertificateEntry> getCertificateList(ObjectId userId,DBObject fields) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CERTIFICATE, query, fields);
        List<CertificateEntry> certificateEntryArrayList=new ArrayList<CertificateEntry>();
        for(DBObject dbObject:list){
            CertificateEntry certificateEntry=new CertificateEntry((BasicDBObject)dbObject);
            certificateEntryArrayList.add(certificateEntry);
        }
        return certificateEntryArrayList;
    }
}
