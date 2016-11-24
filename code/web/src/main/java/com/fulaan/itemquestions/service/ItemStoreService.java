package com.fulaan.itemquestions.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;







import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.questions.ItemStoreDao;
import com.mongodb.BasicDBObject;
import com.pojo.questions.ItemProperty;
import com.pojo.questions.ItemStoreEntry;
import com.pojo.repertory.CoursewareEntry;

/**
 * 
 * @author Zoukai
 *
 */
@Service
public class ItemStoreService {

	ItemStoreDao isd=new ItemStoreDao();
	

	//添加新题
	public int addItem(ItemStoreEntry ques){
		int result =0;
		if(ques != null){
			isd.addQuestions(ques);
		}else{
			result=1;
		}
		return result;

	}
	/**
	 * 根据id查询
	 */
	public ItemStoreEntry getItemStoreEntry(ObjectId id)
	{
		return isd.getItemStoreEntry(id);
	}
	
	/**
	 * 根据数据字典ID在教材版本中查询未入库问题
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<ItemStoreEntry> getItemNotSavedByIdInVersion(String resourceDictionaryId,String questionTopic){
		return isd.getItemNotSavedByIdInVersion(resourceDictionaryId,questionTopic);
	}
	
	
	/**
	 * 根据数据字典ID在教材版本中查询已入库问题
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<ItemStoreEntry> getItemSavedByIdInVersion(String resourceDictionaryId,String questionTopic){
		return isd.getItemSavedByIdInVersion(resourceDictionaryId,questionTopic);
	}
	
	
	/**
	 * 根据数据字典ID在知识点中查询未入库问题
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<ItemStoreEntry> getItemNotSavedByIdInKnowledge(String resourceDictionaryId,String questionTopic){
		return isd.getItemNotSavedByIdInKnowledge(resourceDictionaryId,questionTopic);
	}
	
	
	/**
	 * 根据数据字典ID在知识点中查询已入库问题
	 * @param resourceDictionaryId
	 * @param propertyType
	 * @return
	 */
	public List<ItemStoreEntry> getItemSavedByIdInKnowledge(String resourceDictionaryId,String questionTopic){
		return isd.getItemSavedByIdInKnowledge(resourceDictionaryId,questionTopic);
	}
	
	/**
	 * 删除一个问题
	 * @param id
	 */
	public void deleteItem(ObjectId id){
		isd.removeItem(id);
	}
	
	 //更新题目
	public String updateItem(ObjectId id,String questionTopic,String contentOfQuestion,String rightAnswer,
			String answerAnalysis,List<ItemProperty> properties){
		isd.updateItem(id, questionTopic, contentOfQuestion, rightAnswer, answerAnalysis, properties);
		return "success";
	}
}
