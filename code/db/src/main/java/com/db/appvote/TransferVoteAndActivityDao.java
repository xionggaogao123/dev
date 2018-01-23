package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appvote.TransferVoteAndActivityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/22.
 */
public class TransferVoteAndActivityDao extends BaseDao{

    public void saveEntry(TransferVoteAndActivityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_TRANSFER_VOTE_AND_ACTIVITY,entry.getBaseEntry());
    }


    public void removeEntry(ObjectId transferId){
        BasicDBObject query = new BasicDBObject()
                .append("tfi",transferId);
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_JXM_TRANSFER_VOTE_AND_ACTIVITY,query);
    }


    public TransferVoteAndActivityEntry getEntryByTransferId(ObjectId transferId){
        BasicDBObject query = new BasicDBObject()
                .append("tfi",transferId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_TRANSFER_VOTE_AND_ACTIVITY,
                Constant.FIELDS,query);
        if(null!=dbObject){
            return new TransferVoteAndActivityEntry(dbObject);
        }else{
            return null;
        }
    }


}
