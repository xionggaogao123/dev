package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.appnotice.TransferCommunityRecordEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by scott on 2017/12/15.
 */
public class TransferCommunityRecordDao extends BaseDao{

    public void saveEntries(List<TransferCommunityRecordEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRANSFER_COMMUNITY_RECORD, MongoUtils.fetchDBObjectList(entries));
    }

    public void saveEntry(TransferCommunityRecordEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRANSFER_COMMUNITY_RECORD, entry.getBaseEntry());
    }
}
