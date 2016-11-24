package com.db.resources;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FileType;
import com.pojo.resources.ResourceEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;
/**
 * 资源操作类
 * @author fourer
 *
 */
public class ResourceDao extends BaseDao {

	
	public static final Integer[] VIDEO_TYPES=new Integer[]{
		                                                      FileType.FLASH.getType(),
		                                                      FileType.MP4.getType(),
		                                                      FileType.AVI.getType(),
		                                                      FileType.FLV.getType(),
		                                                      FileType.MKV.getType(),
		                                                      FileType.MOV.getType(),
		                                                      FileType.MPG.getType(),
		                                                      FileType.THREE_GP.getType(),
		                                                      FileType.RMVB.getType(),
		                                                      FileType.WMV.getType(),
		                                                      FileType.VOB.getType(),
		                                                      FileType.OTHER_VIDEO.getType()
		                                                      };
	
	
	public static final Integer[] WORD_TYPES=new Integer[]{
        FileType.DOC.getType(),
        FileType.DOCX.getType()
        };
	
	public static final Integer[] PPT_TYPES=new Integer[]{
        FileType.PPT.getType(),
        FileType.PPTX.getType()
        };
	
	
	
	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addResource(ResourceEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 更改字段值
	 * @param id
	 * @param field
	 * @param value
	 * @throws IllegalParamException
	 */
	public void update(ObjectId id,String field,Object value) throws IllegalParamException
	{
		if(field.equalsIgnoreCase("nm") || field.equalsIgnoreCase("lng") || field.equalsIgnoreCase("vsty"))
		{
			throw new IllegalParamException();
		}
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject(field,value));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, updateValue);
	}
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public ResourceEntry getResourceEntryById(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new ResourceEntry((BasicDBObject)dbo);
		}
		return null;
	}

    /**
     * 根据persistentid查询
     * @param persistentid
     * @return
     */
    public ResourceEntry getResourceEntryByPersistentId(String persistentid)
    {
        DBObject query =new BasicDBObject("pid",persistentid);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResourceEntry((BasicDBObject)dbo);
        }
        return null;
    }
	
	/**
	 * 根据ID集合查询，并且返回map形式
	 * @param col
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, ResourceEntry> getResourceEntryMap(Collection<ObjectId> col,DBObject fields)
	{
		Map<ObjectId, ResourceEntry> map =new HashMap<ObjectId, ResourceEntry>();
		DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,col));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, fields);
		if(null!=list && !list.isEmpty())
		{
			ResourceEntry e;
			for(DBObject dbo:list)
			{
				e=new ResourceEntry((BasicDBObject)dbo);
				map.put(e.getID(), e);
			}
		}
		return map;
	}
	

	/**
	 * 查找资源
	 * @param type 资源类型；不为-1时生效
	 * @param name
	 * @param userId
	 * @param schoolId
	 * @param scs 知识点
	 * @param psbs 章节
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<ResourceEntry> getResourceEntryList (ObjectId eduId, int type,String name,ObjectId userId,ObjectId schoolId,Collection<ObjectId> scs,Collection<ObjectId> psbs,int skip,int limit) throws ResultTooManyException
	{
		BasicDBObject query = buildQueryOne(eduId, type, name, userId, schoolId, scs,psbs);
		List<ResourceEntry> retList =new ArrayList<ResourceEntry>();
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC,skip,limit);
		if(null!=list && !list.isEmpty())
		{
			ResourceEntry e;
			for(DBObject dbo:list)
			{
				e=new ResourceEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	/**
	 * 查询数量
	 * @param type
	 * @param name
	 * @param userId
	 * @param schoolId
	 * @param scs
	 * @param psbs
	 * @return
	 * @throws ResultTooManyException
	 */
	public Long countResourceEntry (ObjectId eduId, int type,String name,ObjectId userId,ObjectId schoolId,Collection<ObjectId> scs,Collection<ObjectId> psbs) throws ResultTooManyException
	{
		BasicDBObject query = buildQueryOne(eduId, type, name, userId, schoolId, scs,psbs);
		long count=count(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query);
		return count;
	}
	
	/**
	 * 
	 * @param type 1 视频;2音频;3WORD;4PPT;5PDF;
	 * @param name
	 * @param userId
	 * @param schoolId
	 * @param scs
	 * @param psbs
	 * @return
	 * @throws ResultTooManyException
	 */
	private BasicDBObject buildQueryOne(ObjectId eduId, int type, String name, ObjectId userId,
			ObjectId schoolId, Collection<ObjectId> scs,
			Collection<ObjectId> psbs) throws ResultTooManyException {

		BasicDBObject allQuery =new BasicDBObject();
		BasicDBObject query =new BasicDBObject();
		BasicDBList bigList =new BasicDBList();
		BasicDBObject queryOne = new BasicDBObject("is", new BasicDBObject("$exists", 0));
		bigList.add(queryOne);
		query.append("ir",Constant.ZERO);
		query.append("is", Constant.ONE);
		if(null!=eduId)
		{
			BasicDBList list =new BasicDBList();
			BasicDBList list1 =new BasicDBList();
			BasicDBObject query1 =new BasicDBObject();

			BasicDBList list2 =new BasicDBList();
			BasicDBObject query2 =new BasicDBObject();

			list1.add(new BasicDBObject("ot", 1));
			list1.add(new BasicDBObject("edid", eduId));
			query1.append(Constant.MONGO_AND,list1);

			list2.add(new BasicDBObject("ot", 0));
			list2.add(new BasicDBObject("neids",new BasicDBObject(Constant.MONGO_NE, eduId)));
			query2.append(Constant.MONGO_AND,list2);
			list.add(query1);
			list.add(query2);

			query.append(Constant.MONGO_OR,list);
		}

		bigList.add(query);
		allQuery.append(Constant.MONGO_OR,bigList);

		if(StringUtils.isNotBlank(name))
		{
			allQuery.append("nm", MongoUtils.buildRegex(name));
		}
		else
		{
			if(type!=-1)
			{
				if(type==1 || type==2 )
				{
					allQuery.append("ty", new BasicDBObject(Constant.MONGO_IN,VIDEO_TYPES));
				}
				if(type==3 )
				{
					allQuery.append("ty", new BasicDBObject(Constant.MONGO_IN,WORD_TYPES));
				}
				if(type==4 )
				{
					allQuery.append("ty", new BasicDBObject(Constant.MONGO_IN,PPT_TYPES));
				}
				if(type==5 )
				{
					allQuery.append("ty", FileType.PDF.getType());
				}
			}
			
			if(null!=userId)
			{
				allQuery.append("ui", userId);
			}
			if(null!=schoolId)
			{
				allQuery.append("si", schoolId);
			}
			if(null!=scs && !scs.isEmpty())
			{
				allQuery.append("scs", new BasicDBObject(Constant.MONGO_IN,scs));
			}
			if(null!=psbs && !psbs.isEmpty())
			{
				allQuery.append("psbs", new BasicDBObject(Constant.MONGO_IN,psbs));
			}
		}

		return allQuery;
	}
	
	/**
	 * 增加推动或者观看次数
	 * @param id 
	 * @param type 0 观看此数 1 推送次数
	 */
	public void increaseNumber(ObjectId id,int type)
	{
		BasicDBObject query=new BasicDBObject(Constant.ID,id);
		String field="vn";
		if(type==Constant.ONE)
			field="pc";
		BasicDBObject update = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject(field, 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, update);
	}
	
	
	
	/**
	 * 更新一个资源所属的知识点和章节
	 * @param id
	 * @param scList 知识点
	 * @param psbList 章节
	 */
	public void updateResource(ObjectId id,List<ObjectId> scList,List<ObjectId> psbList)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("scs", MongoUtils.convert(scList)).append("psbs", MongoUtils.convert(psbList)));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, updateValue);
	}
	
	
	
	public void ignoreResource(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("iig", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, updateValue);
	}
	
	
	
	@Deprecated
	public List<ResourceEntry> getResourceEntryList (int skip,int limit) 
	{
		BasicDBObject query =new BasicDBObject("_id",new BasicDBObject(Constant.MONGO_GTE,new ObjectId("5745593c63e71fa0f16625f3")));
		List<ResourceEntry> retList =new ArrayList<ResourceEntry>();
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, new BasicDBObject("_id",1),Constant.MONGO_SORTBY_DESC);
		if(null!=list && !list.isEmpty())
		{
			ResourceEntry e;
			for(DBObject dbo:list)
			{
				e=new ResourceEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	
	/**
	 * 此方法只应用于导入资源
	 * @param id
	 */
	@Deprecated
	public void remove(ObjectId id)
	{
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, new BasicDBObject(Constant.ID,id));
	}
	
	/**
	 * 此方法只应用于导入资源
	 * @param skip
	 */
    @Deprecated
    public List<ResourceEntry> getResourceEntry(int skip,int limit ){
       // List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES,new BasicDBObject("ty",new BasicDBObject(Constant.MONGO_IN,VIDEO_TYPES)), Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
    	 List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES,new BasicDBObject(), Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
        List<ResourceEntry> schoolEntryList=new ArrayList<ResourceEntry>();
        for(DBObject dbObject:list){
        	ResourceEntry ResourceEntry=new ResourceEntry((BasicDBObject)dbObject);
        	schoolEntryList.add(ResourceEntry);
        }
        return schoolEntryList;
    }
    
    
    
    public long count()
    {
    	return count(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES,new BasicDBObject());
    }

	public int getCloudResourceCount(ObjectId eduId, String searchName, List<ObjectId> verIds, int isSaved) {
		BasicDBObject query = buildQuery(eduId, verIds, searchName,isSaved);

		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query);
	}

	private BasicDBObject buildQuery(ObjectId eduId, List<ObjectId> verIds, String searchName, int isSaved)  {
		BasicDBObject allQuery =new BasicDBObject();
		BasicDBObject query =new BasicDBObject();
		BasicDBList bigList =new BasicDBList();
		if(isSaved!=0) {
			BasicDBObject queryOne = new BasicDBObject("is", new BasicDBObject("$exists", 0));
			bigList.add(queryOne);
		}
		query.append("ir",Constant.ZERO);
		query.append("is", isSaved);

		if(null!=eduId)
		{
			BasicDBList list =new BasicDBList();
			BasicDBList list1 =new BasicDBList();
			BasicDBObject query1 =new BasicDBObject();

			BasicDBList list2 =new BasicDBList();
			BasicDBObject query2 =new BasicDBObject();

			list1.add(new BasicDBObject("ot", 1));
			list1.add(new BasicDBObject("edid", eduId));
			query1.append(Constant.MONGO_AND,list1);

			list2.add(new BasicDBObject("ot", 0));
			list2.add(new BasicDBObject("neids",new BasicDBObject(Constant.MONGO_NE, eduId)));
			query2.append(Constant.MONGO_AND,list2);
			list.add(query1);
			list.add(query2);

			query.append(Constant.MONGO_OR,list);
		}

		bigList.add(query);
		allQuery.append(Constant.MONGO_OR,bigList);

		if(StringUtils.isNotBlank(searchName))
		{
			allQuery.append("nm", MongoUtils.buildRegex(searchName));
		}else{
			if(null!=verIds && !verIds.isEmpty())
			{
				allQuery.append("psbs", new BasicDBObject(Constant.MONGO_IN,verIds));
			}
		}

		return allQuery;
	}

	public List<ResourceEntry> getCloudResourceList(ObjectId eduId, String searchName, List<ObjectId> verIds, int isSaved, int skip, int limit) {
		BasicDBObject query = buildQuery(eduId, verIds, searchName, isSaved);
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC,skip,limit);
		List<ResourceEntry> entryList=new ArrayList<ResourceEntry>();
		for(DBObject dbObject:list){
			ResourceEntry ResourceEntry=new ResourceEntry((BasicDBObject)dbObject);
			entryList.add(ResourceEntry);
		}
		return entryList;
	}

	public void deleteLessonWareEntry(ObjectId id) {
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, updateValue);
	}

	public void updLessonWareEntry(ResourceEntry e) {
		BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
		BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, update);
	}

	public void updateResourceEntry(ResourceEntry e) {
		BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
		BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_RESOURCES, query, update);
	}


}
