package com.fulaan.registration.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.registration.SubQualityDao;
import com.pojo.registration.SubQualityEntry;

/**
 * 素质教育子项目Service
 * @author cxy
 * 2015-11-25 15:23:08
 */
@Service
public class SubQualityService {
	
	private SubQualityDao subQualityDao = new SubQualityDao();
	
	/**
     * 添加素质教育项目
     * @param e
     * @return
     */
    public ObjectId addSubQualityEntry(SubQualityEntry e){
        return subQualityDao.addSubQualityEntry(e);
    }
    
    /**
     * 删除一条素质教育项目
     * @param id
     */
    public void deleteSubQualityEntry(ObjectId id){
    	subQualityDao.deleteSubQualityEntry(id);
    }
    
    /**
	 * 根据ID更新一条素质教育项目信息
	 */
	public void updateSubQualityEntry(ObjectId id,String name,String requirement){
		subQualityDao.updateSubQualityEntry(id, name, requirement);
	}
	
	/**
     * 通过schoolId查询素质教育项目信息
     * @param schoolId
     */
    public List<SubQualityEntry> querySubQualityList(ObjectId parentId) {
        return subQualityDao.querySubQualityList(parentId);
    }
}
