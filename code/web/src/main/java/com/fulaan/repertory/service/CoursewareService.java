package com.fulaan.repertory.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.repertory.CoursewareDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.repertory.CoursewareEntry;
import com.pojo.repertory.RepertoryProperty;
import com.sys.constants.Constant;
/**
 * 资源管理-课件Service
 * @author cxy
 *
 */
@Service
public class CoursewareService {
	private CoursewareDao coursewareDao = new CoursewareDao();
	
	
	/**
	 * 新增一个课件
	 * @param e
	 * @return
	 */
	public ObjectId addCourseware(CoursewareEntry e){
		 return coursewareDao.addCourseware(e);
	}
	
	
	/**
	 * 根据数据字典ID在教材版本中查询未入库信息
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<CoursewareEntry> getCoursewaresNotSavedByIdInVersion(String resourceDictionaryId,ObjectId educationBureauId){
		return coursewareDao.getCoursewaresNotSavedByIdInVersion(resourceDictionaryId,educationBureauId);
	}
	
	/**
	 * 根据数据字典ID在教材版本中查询已入库信息
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<CoursewareEntry> getCoursewaresSavedByIdInVersion(String resourceDictionaryId,ObjectId educationBureauId){
		return coursewareDao.getCoursewaresSavedByIdInVersion(resourceDictionaryId,educationBureauId);
	}
	
	/**
	 * 根据数据字典ID在知识点中查询未入库信息
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<CoursewareEntry> getCoursewaresNotSavedByIdInKnowledge(String resourceDictionaryId,ObjectId educationBureauId){
		return coursewareDao.getCoursewaresNotSavedByIdInKnowledge(resourceDictionaryId,educationBureauId);
	}
	
	/**
	 * 根据数据字典ID在知识点中查询已入库信息
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<CoursewareEntry> getCoursewaresSavedByIdInKnowledge(String resourceDictionaryId,ObjectId educationBureauId){
		return coursewareDao.getCoursewaresSavedByIdInKnowledge(resourceDictionaryId,educationBureauId);
	}
	
	/**
	 * 删除一条
	 * @param id
	 */
	public void deleteCourseware(ObjectId id){
		coursewareDao.deleteCourseware(id);
	}
	
	/**
	 * 忽略一条
	 * @param id
	 */
	public void ignoreCourseware(ObjectId id){
		coursewareDao.ignoreCourseware(id);
	}
	
	/**
	 * 根据Id查询一个
	 * @param id
	 * @return
	 */
	public CoursewareEntry getCoursewareEntry(ObjectId id)
	{
		return coursewareDao.getCoursewareEntry(id);
	}
	
	/**
	 * 把一条改成入库状态
	 * @param id
	 */
	public void saveToGit(ObjectId id){
		coursewareDao.saveToGit(id);
	}
	/**
	 * 根据ID更新一条Courseware
	 */
	public void updateCourseware(ObjectId id,String coverId,ObjectId fileId,List<RepertoryProperty> properties){
		coursewareDao.updateCourseware(id, coverId, fileId, properties);
	}
	/**
	 * 根据传入的数据字典ID和属性分类种类查询前端需要的课件
	 * @return
	 */
	public List<CoursewareEntry> getCoursewaresForCloud(String resourceDictionaryId,String propertyType,ObjectId educationBureauId){
		return coursewareDao.getCoursewaresForCloud(resourceDictionaryId, propertyType, educationBureauId);
	}
}
