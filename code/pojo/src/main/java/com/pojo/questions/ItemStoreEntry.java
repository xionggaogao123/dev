package com.pojo.questions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 
 * @author zoukai
 *			2015-09-14
 *			题库Entry类
 *			
 *			题型	   : qt(questionTopic)
 *			题目内容     : coq(contentOfQuestion)
 *			正确答案     : ria(rightAnswer)
 *			答案解析     : ani(answerAnalysis)
 *			上传时间     : pti(publishTime)
 *			创建人         : pun（publishUserName）
 *			创建人ID : pid(publishId)
 *			属性             ：  pops(properties)  参见ItemProperty
 *	[
        {
		   tcv(teachingVersion):教材版本属性信息 
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		  
		  kpn(knowledgePoint) : 综合知识点属性信息 
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		   
		}
 * 删除标志位  : ir (isRemoved) 0没有删除 1已经删除
 * 入库标志位  : is (isSaved)  0审核未通过 1通过审核 2待审核
 */
public class ItemStoreEntry extends BaseDBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ItemStoreEntry(){
		super();
	}
	
	public ItemStoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
	
	public ItemStoreEntry(String questionTopic,String contentOfQuestion,String rightAnswer,
			String answerAnalysis,long publishTime,String publishUserName,ObjectId publishId,List<ItemProperty> properties,int isSaved){
		super();
		
		BasicDBObject baseEntry=new BasicDBObject().append("qt",questionTopic)
				   								   .append("coq",contentOfQuestion)
				   								   .append("ria",rightAnswer)
				   								   .append("ani",answerAnalysis)
				   								   .append("pti",new Date().getTime())
				   								   .append("pun",publishUserName)
				   								   .append("pid",publishId)
				   								   .append("pops",MongoUtils.convert(MongoUtils.fetchDBObjectList(properties)))
				   								   .append("is", isSaved)
				   								   .append("ir",Constant.ZERO);
		
							setBaseEntry(baseEntry);
	}
	
	public String getquestionTopic(){
		return getSimpleStringValue("qt");
		
	}
	
	public void setquestionTopic(String questionTopic){
		setSimpleValue("qt",questionTopic);
	}
	
	public String getcontentOfQuestion(){
		return getSimpleStringValue("coq");
	}
	
	public void setcontentOfQuestion(String contentOfQuestion){
		setSimpleValue("coq",contentOfQuestion);
	}
	
	public String getrightAnswer(){
		return getSimpleStringValue("ria");
	}
	
	public void setrightAnswer(String rightAnswer){
		setSimpleValue("ria",rightAnswer);
	}
	
	public String getanswerAnalysis(){
		return getSimpleStringValue("ani");
	}
	
	public void setanswerAnalysis(String answerAnalysis){
		setSimpleValue("ani",answerAnalysis);
	}
	
	public Long getPublishTime(){
		return getSimpleLongValue("pti");
	}
	
	public void setPublishTime(){
		setSimpleValue("pti",new Date().getTime());
	}
	
	public String getpublishUserName(){
		return getSimpleStringValue("pun");
	}
	
	public void setpublishUserName(String publishUserName){
		setSimpleValue("pun",publishUserName);
	}
	
	public ObjectId getpublishId(){
		return getSimpleObjecIDValue("pid");
	}
	
	public void setpublishId(ObjectId publishId){
		setSimpleValue("pid",publishId);
	}
	
	public List<ItemProperty> getproperties(){
		List<ItemProperty> resultList =new ArrayList<ItemProperty>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("pops");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new ItemProperty((BasicDBObject)o));
			}
		}
		return resultList;
	}
	
	public void setproperties(List<ItemProperty> properties){
		List<DBObject> list=MongoUtils.fetchDBObjectList(properties);
		setSimpleValue("pops",MongoUtils.convert(list));
	}
	
	public int getIsSaved(){
		return getSimpleIntegerValue("is");
	}
	
	public void setIsSaved(int isSaved){
		setSimpleValue("is",isSaved);
	}
	



}
