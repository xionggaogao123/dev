package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.wrongquestion.TestTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/12.
 */
public class TestTypeDao extends BaseDao {
    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addTestTypeEntry(TestTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_TYPE, e.getBaseEntry());
        return e.getID();
    }
}
