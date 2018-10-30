package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.appvote.AppNewVoteEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-10-29.
 */
public class AppNewVoteDao extends BaseDao {

    public void saveAppVote(AppNewVoteEntry appNewVoteEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_NEW_VOTE,appNewVoteEntry.getBaseEntry());
    }

}
