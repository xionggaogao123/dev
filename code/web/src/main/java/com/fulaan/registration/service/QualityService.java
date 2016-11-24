package com.fulaan.registration.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.registration.QualityDao;
import com.pojo.registration.QualityEntry;

/**
 * 素质教育项目Service
 * @author cxy
 * 2015-11-25 15:20:44
 */
@Service
public class QualityService {
	
	private QualityDao qualityDao = new QualityDao();
	
	/**
     * 添加素质教育项目
     * @param e
     * @return
     */
    public ObjectId addQualityEntry(QualityEntry e){
        return qualityDao.addQualityEntry(e);
    }
    
    /**
     * 删除一条素质教育项目
     * @param id
     */
    public void deleteQualityEntry(ObjectId id){
    	qualityDao.deleteQualityEntry(id);
    }
    
    /**
	 * 根据ID更新一条素质教育项目信息
	 */
	public void updateQualityEntry(ObjectId id,String name){
		qualityDao.updateQualityEntry(id, name);
		
	}
	
	/**
     * 通过schoolId查询素质教育项目信息
     * @param schoolId
     */
    public List<QualityEntry> queryQuality(ObjectId schoolId) {
       return qualityDao.queryQuality(schoolId);
    }
    
}
