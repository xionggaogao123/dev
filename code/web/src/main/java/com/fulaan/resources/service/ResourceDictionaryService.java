package com.fulaan.resources.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.resources.ResourceDictionaryDao;
import com.pojo.resources.ResourceDictionaryEntry;

@Service
public class ResourceDictionaryService {
	private ResourceDictionaryDao resourceDictionaryDao = new ResourceDictionaryDao();
	

	/**
	 * 增加
	 * @param e
	 * @return
	 */
	public ObjectId addResourceDictionaryEntry(ResourceDictionaryEntry e)
	{
		return resourceDictionaryDao.addResourceDictionaryEntry(e);
	}
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(ObjectId id)
	{
		return resourceDictionaryDao.getResourceDictionaryEntry(id);
	}
	/**
	 * 
	 * @param type 类型
	 * @param code 代码
	 * @param level 级别
	 */
	public ResourceDictionaryEntry getResourceDictionaryEntry(int type,String name,ObjectId pid)
	{
		return resourceDictionaryDao.getResourceDictionaryEntry(type, name,pid);
	}

	/**
	 * 根据类型查询
	 * @param type
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(type);
	}
	
	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(ObjectId parentId,int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(parentId, type);
	}
	
	/**
	 * 根据父节点集合查询子节点
	 * @param parentId
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrysByParents(Collection<ObjectId> parentIds,int type)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrysByParents(parentIds,type,null);
	}
	
	/**
	 * 根据科目查询题目类型
	 * @param subject
	 * @return
	 */
	public List<ResourceDictionaryEntry> getItemTypeBySubject(List<ObjectId> subject)
	{
		 return resourceDictionaryDao.getItemTypeBySubject(subject);
	}
	
	/**
	 * 根据ID集合查询
	 * @param ids
	 * @return
	 */
	public List<ResourceDictionaryEntry> getResourceDictionaryEntrys(Collection<ObjectId> ids)
	{
		 return resourceDictionaryDao.getResourceDictionaryEntrys(ids);
	}

	public List<ObjectId> getResourceDictionaryEntryIds(String typeId, int typeInt) {
		List<ObjectId> verIds=new ArrayList<ObjectId>();
		if(null!=typeId&&!"".equals(typeId)) {
			List<ResourceDictionaryEntry> list = getItemTypeBySubject(new ObjectId(typeId), typeInt, new BasicDBObject("ty", 1));
			if (list != null && list.size() > 0) {
				for (ResourceDictionaryEntry entry : list) {
					verIds.add(entry.getID());
				}
			}
		}
		return verIds;
	}

	/**
	 * 根据父节点查询子节点
	 * @param subject
	 * @return
	 */
	public List<ResourceDictionaryEntry> getItemTypeBySubject(ObjectId subject, int typeInt, DBObject fields) {
		List<ResourceDictionaryEntry> list = resourceDictionaryDao.getItemTypeBySubject(subject, typeInt, fields);
		return list;
	}
}
