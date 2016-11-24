package com.fulaan.resources.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.resources.CloudResourceDao;
import com.pojo.resources.ResourceEntry;
/**
 * 
 * @author cxy
 *
 */
@Service
public class CloudResourceService {
	CloudResourceDao resourceDao = new CloudResourceDao();
	
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
		resourceDao.removeById(id);
	}
}
