package com.fulaan.preparation.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.preparation.PreparationDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.preparation.PreparationEntry;
import com.sys.constants.Constant;

/**
 * 集体备课Service
 * @author cxy
 *
 */
@Service
public class PreparationService {
	
	private PreparationDao preparationDao = new PreparationDao();
	
	/**
	 * 添加一条集体备课信息
	 * @param e
	 * @return
	 */
	public ObjectId addPreparationEntry(PreparationEntry e){
		return preparationDao.addPreparationEntry(e);
	}
	/**
     * 根据ID查询
     * @param userId
     * @return
     */
    public PreparationEntry getPreparationEntryById(ObjectId preparationId)
    {
        return preparationDao.getPreparationEntryById(preparationId);
    }
	/**
	 * 根据传入的数据字典ID和字段名查询集体备课List
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<PreparationEntry> getPreparationEntriesByResourceDictionaryId(String resourceDictionaryId,String columName,ObjectId ebeId){
		return preparationDao.getPreparationEntriesByResourceDictionaryId(resourceDictionaryId, columName,ebeId);
	}
	
	/**
	 * 查询所有的列表信息
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<PreparationEntry> getAllPreparationEntries(ObjectId ebeId){
		return preparationDao.getAllPreparationEntries(ebeId);
	}
	
	/**
     * 给某一个备课增加一个备选课件
     * @param userId
     * @return
     */
    public void addFileBackForPreparation(ObjectId preparationId,ObjectId fileId)
    {
    	preparationDao.addFileBackForPreparation(preparationId, fileId);
    }
    
    /**
     * 给某一个备课增加一个备选课件
     * @param userId
     * @return
     */
    public void addFileFromBack(ObjectId preparationId,ObjectId fileId)
    {
    	preparationDao.addFileForPreparation(preparationId, fileId);
    	preparationDao.delFileBackForPreparation(preparationId, fileId);
    }
    
    /**
     * 给某一个备课去除一个课件
     * @param userId
     * @return 
     */
    public void delFileForPreparation(ObjectId preparationId,ObjectId fileId)
    {
    	preparationDao.delFileForPreparation(preparationId, fileId);
    }
}
