package com.db.groupcollectmoney;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.groupcollectmoney.GroupUserExpenseEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by scott on 2018/1/15.
 */
public class GroupUserExpenseDao extends BaseDao{


    public  void saveEntry(GroupUserExpenseEntry expenseEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_USER_EXPENSE,expenseEntry.getBaseEntry());
    }


    public void  saveEntries(List<GroupUserExpenseEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_USER_EXPENSE, MongoUtils.fetchDBObjectList(entries));
    }





}
