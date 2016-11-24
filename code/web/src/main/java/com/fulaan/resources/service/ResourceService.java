package com.fulaan.resources.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.resources.ResourceDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.resources.ResourceEntry;
import com.sys.constants.Constant;
/**
 * 
 * @author cxy
 *
 */
@Service
public class ResourceService {
	ResourceDao resourceDao = new ResourceDao();
	
	public ResourceEntry getResourceEntryById(ObjectId id){
		return resourceDao.getResourceEntryById(id);
	}
	
	/**
	 * 忽略一条
	 * @param id
	 */
	public void ignoreResource(ObjectId id){
		resourceDao.ignoreResource(id);
	}
	

	/**
	 * 删除一条
	 * @param id
	 */
	public void removeById(ObjectId id){
		//resourceDao.removeById(id);
	}

}
