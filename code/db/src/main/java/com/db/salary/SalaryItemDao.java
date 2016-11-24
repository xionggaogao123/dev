package com.db.salary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.salary.SalaryEntry;
import com.pojo.salary.SalaryItemEntry;
import com.pojo.salary.SalaryQueryDto;
import com.sys.constants.Constant;

/**
 * 工资项目管理
 *
 * @author yang.ling
 */
public class SalaryItemDao extends BaseDao {

    /**
     * 添加工资项目
     *
     * @param e
     * @return
     */
    public ObjectId add(SalaryItemEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 更新工资项目
     *
     * @param id
     * @param pairs
     */
    public String update(String id, BasicDBObject valueDBO) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, query, updateValue);
        return id;
    }


    /**
     * 删除工资项目
     *
     * @param id
     * @param teacherId
     */
    public String delete(String id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, query);
        return id;
    }


    /**
     * 分页查询工资项目
     *
     * @param fields
     * @return
     */
    public List<SalaryItemEntry> getSalaryItemEntryList(ObjectId schoolId,DBObject fields) {
        List<SalaryItemEntry> salaryList = new ArrayList<SalaryItemEntry>();
        BasicDBObject query = new BasicDBObject("sid",schoolId);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, query, fields, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            SalaryItemEntry e;
            for (DBObject dbo : list) {
                e = new SalaryItemEntry((BasicDBObject) dbo);
                salaryList.add(e);
            }
        }
        return salaryList;
    }
    

    /**
     * 分页查询工资项目
     *
     * @param fields
     * @return
     */
    @Deprecated
    public List<SalaryItemEntry> getSalaryItemEntryList() {
        List<SalaryItemEntry> salaryList = new ArrayList<SalaryItemEntry>();
        BasicDBObject query = new BasicDBObject();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            SalaryItemEntry e;
            for (DBObject dbo : list) {
                e = new SalaryItemEntry((BasicDBObject) dbo);
                salaryList.add(e);
            }
        }
        return salaryList;
    }
    
    
    
    /**
     * 删除工资项目
     *
     * @param id
     * @param teacherId
     */
    @Deprecated
    public void delete() {
        BasicDBObject query = new BasicDBObject();
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM, query);
    }
}
