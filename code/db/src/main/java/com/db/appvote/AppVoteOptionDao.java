package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.appvote.AppVoteOptionEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-10-29.
 */
public class AppVoteOptionDao extends BaseDao {
    public void saveAppVote(AppVoteOptionEntry appVoteOptionEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION,appVoteOptionEntry.getBaseEntry());
    }
}
