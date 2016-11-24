package com.db.news;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.news.NewsColumnEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/7/2.
 */
public class NewsColumnDao extends BaseDao {
    /**
     * 添加栏目
     *
     * @param newsColumnEntry
     */
    public void addNewsColumn(NewsColumnEntry newsColumnEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, newsColumnEntry.getBaseEntry());
    }

    /**
     * 修改栏目
     *
     * @param objectId
     * @param columnName
     * @param dirName
     */
    public void updateColumn(ObjectId objectId, String columnName, String dirName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("cn", columnName).append("dir", dirName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, updateValue);
    }

    /**
     * 删除栏目
     *
     * @param objectId
     */
    public void deleteColumn(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query);
    }

    /**
     * 获取所有栏目，提供分页
     *
     * @param skip
     * @param pageSize
     * @return
     */
    public List<NewsColumnEntry> getNewsColumnList(int skip, int pageSize, String schoolId, String educationId) {
        BasicDBObject query = new BasicDBObject();
        if (schoolId != null && !"".equals(schoolId)) {
            query.append("sid", new ObjectId(schoolId));
        }
        if (educationId != null && !"".equals(educationId)) {
            query.append("eid", new ObjectId(educationId));
        }
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, pageSize);

        List<NewsColumnEntry> newsColumnEntryList = new ArrayList<NewsColumnEntry>();
        for (DBObject dbObject : dbObjects) {
            newsColumnEntryList.add(new NewsColumnEntry((BasicDBObject) dbObject));
        }
        return newsColumnEntryList;
    }

    /**
     * 获取所有栏目，不提供分页
     *
     * @return
     */
    public List<NewsColumnEntry> getNewsColumnList(String schoolId, String educationId) {
        BasicDBObject query = new BasicDBObject();
        if (schoolId != null && !"".equals(schoolId)) {
            query.append("sid", new ObjectId(schoolId));
        }
        if (educationId != null && !"".equals(educationId)) {
            query.append("eid", new ObjectId(educationId));
        }
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);

        List<NewsColumnEntry> newsColumnEntryList = new ArrayList<NewsColumnEntry>();
        for (DBObject dbObject : dbObjects) {
            newsColumnEntryList.add(new NewsColumnEntry((BasicDBObject) dbObject));
        }
        return newsColumnEntryList;
    }

    /**
     * 获取所有栏目数量
     *
     * @param schoolId
     * @return
     */
    public int countNewsColumn(String schoolId, String educationId) {
        BasicDBObject query = new BasicDBObject();
        if (schoolId != null && !"".equals(schoolId)) {
            query.append("sid", new ObjectId(schoolId));
        }
        if (educationId != null && !"".equals(educationId)) {
            query.append("eid", new ObjectId(educationId));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query);
    }

    /**
     * 获取一个栏目详细信息
     *
     * @param objectId
     * @return
     */
    public NewsColumnEntry findNewsColumnById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        if (dbObject != null) {
            return new NewsColumnEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 批量删除
     *
     * @param list
     */
    public void deleteManyColumn(List<ObjectId> list) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, list));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query);
    }

    /**
     * 检查新添栏目名是否有重复,无重复可添加返回true，有重复不可添加返回false
     */
    public int checkIfHave(String schoolId, String educationId, String columnName, String columnDir) {
        BasicDBObject query = new BasicDBObject();
        if (schoolId != null && !"".equals(schoolId)) {
            query.append("sid", new ObjectId(schoolId));
        }
        if (educationId != null && !"".equals(educationId)) {
            query.append("eid", new ObjectId(educationId));
        }
        BasicDBList dblist = new BasicDBList();
        dblist.add(new BasicDBObject("cn", columnName));
        dblist.add(new BasicDBObject("dir", columnDir));
        query.append(Constant.MONGO_OR, dblist);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        if (dbObject != null) {
            NewsColumnEntry entry = new NewsColumnEntry((BasicDBObject) dbObject);
            if (columnName.equals(entry.getColumnName())) {
                return 1;//名字重复
            } else {
                if (columnDir.equals(entry.getColumnDir())) {
                    return 2;//栏目重复
                }
                return 0;
            }
        } else {
            return 0;
        }
    }
    /*public int checkIfHave(ObjectId schoolId,String columnName,String columnDir)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("sid",schoolId);
        query.append("cn",columnName);
        int count=count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_COLUMN,query);
        if(count==0)//无重复
        {
            BasicDBObject query2=new BasicDBObject();
            query2.append("sid",schoolId);
            query2.append("dir",columnDir);
            int count2=count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEWS_COLUMN,query2);
            if(count2==0)
            {
                return 0;
            }
            else
                return 2;//栏目重复
        }
        else
            return 1;//名字重复
    }*/

    /**
     * 检查是否已经存在，用于修改栏目名排除相同
     *
     * @param schoolId
     * @param educationId
     * @param columnId
     * @param columnName
     * @return
     */
    public int checkIfHave(String schoolId, String educationId, String columnId, String columnName, String columnDir) {
        BasicDBObject query = new BasicDBObject();
        if (schoolId != null && !"".equals(schoolId)) {
            query.append("sid", new ObjectId(schoolId));
        }
        if (educationId != null && !"".equals(educationId)) {
            query.append("eid", new ObjectId(educationId));
        }
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NE, new ObjectId(columnId)));
        BasicDBList dblist = new BasicDBList();
        dblist.add(new BasicDBObject("cn", columnName));
        dblist.add(new BasicDBObject("dir", columnDir));
        query.append(Constant.MONGO_OR, dblist);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        if (dbObject == null) {//不存在
            return 0;
        } else {
            NewsColumnEntry entry = new NewsColumnEntry((BasicDBObject) dbObject);
            if (columnName.equals(entry.getColumnName())) {
                return 1;//名字重复
            } else if (columnDir.equals(entry.getColumnDir())) {
                return 2;//栏目重复
            }
            return 0;
        }
    }
    /*public int checkIfHave(ObjectId schoolId,String columnId,String columnName,String columnDir)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("sid",schoolId);
        //query.append("dir",columnDir);
        query.append("cn",columnName);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query,Constant.FIELDS);
        if(dbObject==null)//不存在
        {
            BasicDBObject query2=new BasicDBObject();
            query2.append("sid",schoolId);
            query2.append("dir",columnDir);
            DBObject dbObject2=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query2, Constant.FIELDS);
            if(dbObject2==null)
            {
                return 0;
            }
            else if(new NewsColumnEntry((BasicDBObject)dbObject2).getID().toString().equals(columnId))
            {
                return 0;
            }
            return 2;//目录重复
        }
        else if(new NewsColumnEntry((BasicDBObject)dbObject).getID().toString().equals(columnId))//名字相同
        {
            BasicDBObject query2=new BasicDBObject();
            query2.append("sid",schoolId);
            query2.append("dir",columnDir);
            DBObject dbObject2=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query2, Constant.FIELDS);
            if(dbObject2==null)
            {
                return 0;
            }
            else if(new NewsColumnEntry((BasicDBObject)dbObject2).getID().toString().equals(columnId))
            {
                return 0;
            }
            return 2;//目录重复
        }
        return 1;
    }*/

    /**
     * 根据栏目名获取栏目id
     *
     * @param name
     * @return
     */
    public ObjectId getColumnIdByName(String name) {
        BasicDBObject query = new BasicDBObject();
        query.append("cn", name);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        if (dbObject == null)//不存在
        {
            return null;
        } else {
            return new NewsColumnEntry((BasicDBObject) dbObject).getID();
        }
    }

    /**
     * 根据栏目名获取栏目ID
     *
     * @param schoolId
     * @param columnName
     * @return
     */
    public ObjectId getColumnIdByName(ObjectId schoolId, String columnName) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("cn", columnName);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        if (dbObject == null)//不存在
        {
            return null;
        } else {
            return new NewsColumnEntry((BasicDBObject) dbObject).getID();
        }
    }

    /**
     * 根据栏目ID获取栏目详细信息列表
     *
     * @param columns
     * @return
     */
    public List<NewsColumnEntry> getColumnByIds(List<ObjectId> columns) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, columns));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEWS_COLUMN, query, Constant.FIELDS);
        List<NewsColumnEntry> newsColumnEntryList = new ArrayList<NewsColumnEntry>();
        for (DBObject dbObject : dbObjectList) {
            newsColumnEntryList.add(new NewsColumnEntry((BasicDBObject) dbObject));
        }
        return newsColumnEntryList;
    }
}
