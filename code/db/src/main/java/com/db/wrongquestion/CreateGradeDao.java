package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.CreateGradeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/6.
 */
public class CreateGradeDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addSubjectEntry(CreateGradeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CREATE_GRADE, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询所有的年级列表
     */
    public List<CreateGradeEntry> getList(){
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CREATE_GRADE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<CreateGradeEntry> entryList = new ArrayList<CreateGradeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CreateGradeEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //单个年级（根据type）
    public CreateGradeEntry getEntryByType(int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("typ",type);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CREATE_GRADE, query, Constant.FIELDS);
        if (obj != null) {
            return new CreateGradeEntry((BasicDBObject) obj);
        }
        return null;
    }

}
