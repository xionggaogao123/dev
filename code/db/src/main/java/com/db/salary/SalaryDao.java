package com.db.salary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.AggregationOutput;
import com.pojo.exam.ExamEntry;
import com.pojo.salary.*;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;

/**
 * 工资管理
 *
 * @author yang.ling
 */
public class SalaryDao extends BaseDao {

    /**
     * 添加单条薪资
     *
     * @param e
     * @return
     */
    public ObjectId add(SalaryEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 批量增加薪资
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, list);
    }


    /**
     * 更新工资数据字段值
     *
     * @param id
     * @param itemName
     * @param m
     */
    public void update(String id, String itemName, double m) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id))
                .append("money.in", itemName);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("money.$.m", Math.abs(m)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, updateValue);

    }

    /**
     * 根据薪资编码获取薪资信息
     *
     * @param id
     * @return
     */
    public SalaryEntry getSalaryDto(final String id) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY,
                new BasicDBObject().append(Constant.ID, new ObjectId(id)), Constant.FIELDS);
        return null != dbObject ? new SalaryEntry((BasicDBObject) dbObject) : null;
    }

    /**
     * 修改实发工资信息
     *
     * @param ss
     * @param ms
     * @param as
     */
    public void updateRealSalary(final String id, final double ss, final double ms, final double as,final String remark) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ss", ss).append("ms", ms).append("as", as).append("rmk",remark));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, updateValue);
    }

    /**
     * 修改工资条的所有信息
     *
     * @param salaryDto
     */
    public void updateAllSalaryItem(SalaryDto salaryDto) {
        BasicDBObject query;
        BasicDBObject basicDBObject;
        for (SalaryItemDto itemDto : salaryDto.getMoney()) {
            query = new BasicDBObject(Constant.ID, new ObjectId(salaryDto.getId())).append("money.in", itemDto.getItemName());
            basicDBObject = new BasicDBObject("money.$.m", Math.abs(itemDto.getM()));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, new BasicDBObject(Constant.MONGO_SET, basicDBObject));
        }
    }


    /**
     * 删除工资文档
     *
     * @param id
     */
    public void delete(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query);
    }


    /**
     * 查询工资数据
     *
     * @param query
     * @param fields
     * @return
     */
    public List<SalaryEntry> getSalaryEntryList(DBObject query, DBObject fields) {
        List<SalaryEntry> salaryList = new ArrayList<SalaryEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, fields,
                new BasicDBObject("unm", Constant.ASC).append("ym", Constant.DESC).append("n", Constant.DESC));
        if (null != list && !list.isEmpty()) {
            SalaryEntry e;
            for (DBObject dbo : list) {
                e = new SalaryEntry((BasicDBObject) dbo);
                salaryList.add(e);
            }
        }
        return salaryList;
    }

    /**
     * 查询工资数据
     *
     * @param query
     * @param fields
     * @return
     */
    public List<SalaryEntry> getSalaryEntryList(DBObject query,int skip,int limit, DBObject fields) {
        List<SalaryEntry> salaryList = new ArrayList<SalaryEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, fields,
                new BasicDBObject("unm", Constant.ASC).append("ym", Constant.DESC).append("n", Constant.DESC), skip, limit);
        if (null != list && !list.isEmpty()) {
            SalaryEntry e;
            for (DBObject dbo : list) {
                e = new SalaryEntry((BasicDBObject) dbo);
                salaryList.add(e);
            }
        }
        return salaryList;
    }

    /**
     * 修改某次工资表的名称
     *
     * @param schoolId
     * @param year
     * @param month
     * @param num
     * @param name
     */
    public void renameSalary(final ObjectId schoolId, final int year, final int month, final int num, final String name) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY,
                new BasicDBObject("sid", schoolId).append("y", year).append("m", month).append("n", num),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("na", name)));
    }

    /**
     * 删除指定工资表
     *
     * @param schoolId
     * @param year
     * @param month
     * @param num
     */
    public void deleteSalary(final ObjectId schoolId, final int year, final int month, final int num) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY,
                new BasicDBObject("sid", schoolId).append("y", year).append("m", month).append("n", num),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("dl", 1)));
    }

    /**
     * 获取学校中指定年月的工资发放次数列表
     *
     * @param schoolId
     * @param year
     * @param month
     */
    public Object[] getSalaryTimes(String schoolId, int year, int month) {
        DBObject dbObject = new BasicDBObject(Constant.MONGO_MATCH,
                new BasicDBObject("sid", new ObjectId(schoolId)).append("y", year).append("m", month).append("dl", 0));
        BasicDBObject group = new BasicDBObject("n", "$n");
        DBObject groupBy = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject(Constant.ID, group));
        try {
            AggregationOutput output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, dbObject, groupBy);
            List<Integer> index = new ArrayList<Integer>(Constant.SIX);
            BasicDBObject result;
            for (DBObject obj : output.results()) {
                result = (BasicDBObject) obj;
                index.add(((BasicDBObject) result.get(Constant.ID)).getInt("n"));
            }
            Collections.sort(index);
            return index.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[0];
    }


    /**
     * @param schoolId
     * @param year
     * @param month
     * @return
     */
    public int countSalaryTimes(String schoolId, int year, int month) {
        DBObject dbObject = new BasicDBObject(Constant.MONGO_MATCH,
                new BasicDBObject("sid", new ObjectId(schoolId)).append("y", year).append("m", month));
        BasicDBObject group = new BasicDBObject("n", "$n");
        DBObject groupBy = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject(Constant.ID, group));
        try {
            AggregationOutput output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, dbObject, groupBy);
            return ((List) output.results()).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    
    /**
     * 
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public Set<ObjectId> getSalaryEntrySchoolIdSet( ){
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY,new BasicDBObject(), new BasicDBObject("sid",1));
        Set<ObjectId> set =new HashSet<ObjectId>();
        for(DBObject dbObject:list){
        	SalaryEntry schoolEntry=new SalaryEntry((BasicDBObject)dbObject);
        	set.add(schoolEntry.getSchoolId());
        }
        return set;
    }

    /**
     *
     * @param itemName
     * @param type
     * @param schoolId
     * @return
     */
    public int selSalaryItem(String itemName, String type, String schoolId) {
        BasicDBObject query =new BasicDBObject("in",itemName).append("it",type).append("sid",new ObjectId(schoolId));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY_ITEM,query);

    }

    /**
     *
     * @param query
     * @return
     */
    public int getSalaryCount(BasicDBObject query) {
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_SALARY,query);
    }

    /**
     *
     * @param id
     * @param m
     */
    public void updateRemark(ObjectId id, String m) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("rmk", m));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SALARY, query, updateValue);
    }
}
