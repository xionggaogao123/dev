package com.db.resources;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 资源字典操作类
 * index:ty_pid
 *       {"ty":1,"pid":1}
 * @author fourer
 *
 */
public class ResourceDictionaryDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addResourceDictionaryEntry(ResourceDictionaryEntry e)
    {
        save(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, e.getBaseEntry());
        return e.getID();
    }
    /**
     * 详情
     * @param id
     * @return
     */
    public ResourceDictionaryEntry getResourceDictionaryEntry(ObjectId id)
    {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo=findOne(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResourceDictionaryEntry((BasicDBObject)dbo);
        }
        return null;
    }




    /**
     *
     * @param type 类型
     * @param name 代码
     * @param pid 级别
     */
    public ResourceDictionaryEntry getResourceDictionaryEntry(int type, String name, ObjectId pid)
    {
        DBObject query =new BasicDBObject("ty",type).append("nm", name);
        if(null!=pid)
        {
            query.put("pid", pid);
        }
        DBObject dbo=findOne(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResourceDictionaryEntry((BasicDBObject)dbo);
        }
        return null;
    }


    /**
     * @param type 类型
     */
    public ResourceDictionaryEntry getResourceDictionaryEntry(int type, long sort)
    {
        DBObject query =new BasicDBObject("ty",type).append("st", sort);
        DBObject dbo=findOne(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResourceDictionaryEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(int type)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("ty",type);
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据父节点查询子节点
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId, int type)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("pid",parentId);
        if(type>-1)
        {
            query.put("ty", type);
        }
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据父节点查询子节点
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId, List<Integer> types)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        BasicDBObject query =new BasicDBObject("ty", new BasicDBObject(Constant.MONGO_IN, types));
        query.append("pinfos.id", parentId);
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据父节点查询子节点
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId, List<ObjectId> parentIds, List<Integer> types)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        BasicDBObject query =new BasicDBObject("ty", new BasicDBObject(Constant.MONGO_IN, types));
        query.append("pinfos.id", parentId);
        query.append("pinfos.id", new BasicDBObject(Constant.MONGO_IN, parentIds));
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    /**
     * 根据父节点查询子节点
     * @param parentId
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId,int type,int skip,int limit)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("pid",parentId);
        if(type>-1)
        {
            query.put("ty", type);
        }
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1),skip,limit);
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    /**
     * 根据父节点集合查询子节点
     * @param parentIds
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrysByParents(Collection<ObjectId> parentIds, int type, DBObject fields)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,parentIds));
        if(type!= Constant.NEGATIVE_ONE)
        {
            query.put("ty", type);
        }
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, fields,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据科目查询题目类型
     * @param subject
     * @return
     */
    public List<ResourceDictionaryEntry> getItemTypeBySubject(List<ObjectId> subject)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("pinfos.id",new BasicDBObject(Constant.MONGO_IN,subject));
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据科目查询题目类型
     * @param subject
     * @return
     */
    public List<ResourceDictionaryEntry> getItemTypeBySubject(ObjectId subject, int type, DBObject fields)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("pinfos.id",subject);
        if(type!= Constant.NEGATIVE_ONE)
        {
            query.put("ty", type);
        }
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, fields, new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据ID集合查询
     * @param ids
     * @return
     */
    public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(Collection<ObjectId> ids)
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }



    /**
     * 根据ID集合查询，返回map
     * @param ids
     * @return
     */
    public Map<ObjectId, ResourceDictionaryEntry> getResourceDictionaryEntryMap(Collection<ObjectId> ids)
    {
        Map<ObjectId, ResourceDictionaryEntry> retMap =new HashMap<ObjectId, ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1));
        if(null!=list && list.size()>0)
        {
            ResourceDictionaryEntry e;
            for(DBObject dbo:list)
            {
                e=new ResourceDictionaryEntry((BasicDBObject)dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 增量更新数量
     * @param id
     * @param inc
     */
    public void update(ObjectId id,int inc)
    {
        DBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("c",inc));
        update(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, updateValue);
    }


    @Deprecated
    public ResourceDictionaryEntry getResourceDictionaryEntryBySort(Long sort,int ty )
    {
        DBObject query =new BasicDBObject("st",sort).append("ty", ty);
        DBObject dbo=findOne(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResourceDictionaryEntry((BasicDBObject)dbo);
        }
        return null;
    }


    @Deprecated
    public List<ResourceDictionaryEntry> getResourceDictionaryEntry(int ty )
    {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject().append("ty", ty);
        List<DBObject> dbos=find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbos)
        {
            for(DBObject dbo:dbos)
            {
                retList.add (new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    //得到全部
    public List<ResourceDictionaryEntry> getAll(int skip,int limit){
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject();
        List<DBObject> list =find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS,new BasicDBObject("st",1),skip,limit);
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ResourceDictionaryEntry> getResourceDictionaryEntryList(int type, String gradeName, ObjectId pid) {
        List<ResourceDictionaryEntry> retList =new ArrayList<ResourceDictionaryEntry>();
        DBObject query =new BasicDBObject("ty",type);
        if(gradeName != "" &&!"*".equals(gradeName)) {
            query.put("nm", MongoUtils.buildRegex(gradeName));
        }
        if(null!=pid)
        {
            query.put("pinfos.id", pid);
        }
        List<DBObject> dbos=find(MongoFacroty.getResDB(), Constant.COLLECTION_RESOURCES_DICTIONARY, query, Constant.FIELDS);
        if(null!=dbos)
        {
            for(DBObject dbo:dbos)
            {
                retList.add (new ResourceDictionaryEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
}
