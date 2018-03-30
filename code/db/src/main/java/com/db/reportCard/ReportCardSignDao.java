package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.ReportCardSignEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/13.
 */
public class ReportCardSignDao extends BaseDao{

    public void saveEntry(ReportCardSignEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_SIGN,entry.getBaseEntry());
    }

    public void saveEntries(List<ReportCardSignEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_SIGN, MongoUtils.fetchDBObjectList(entries));
    }

    public void updateTypeByRecordId(ObjectId userRecordId,ObjectId mainUserId){
        BasicDBObject query =new BasicDBObject()
                .append("uri",userRecordId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ty",Constant.THREE)
                .append("sti",System.currentTimeMillis()).append("pid",mainUserId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_SIGN,query,updateValue);
    }


    public void removeOldData(){
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_SIGN,new BasicDBObject());
    }

    public List<ReportCardSignEntry> getEntries(ObjectId groupExamDetailId){
        List<ReportCardSignEntry> entries = new ArrayList<ReportCardSignEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("gei",groupExamDetailId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_SIGN,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new ReportCardSignEntry(dbObject));
            }
        }
        return entries;
    }
}
