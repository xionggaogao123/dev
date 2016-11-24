package com.pojo.repertory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * @author cxy 
 * 2015-9-15 20:31:16
 * 资源-课件Entry类 
 * collectionName : coursware 
 * 上传用户ID : uid(userId)
 * 上传用户名  : una(userName)
 * 上传日期  	 : ts(timestamp)
 * 封面图片ID : coid(coverId)
 * 资源文件ID : fid(fileId)
 * 分类属性[] : props(properties) 参见RepertoryProperty
 * [
        {
		   ver(version) : 教材版本属性信息 PropertyObject
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		   kno(knowledge) : 知识点属性信息 PropertyObject
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		}
		.....
     ]
 * 删除标志位  : ir (isRemoved) 0没有删除 1已经删除
 * 入库标志位  : is (isSaved)  0没有入库 1已经入库
 * 忽略标志位 : iig(isIgnore) 0否,1是
 * 教育局ID  ： edid(educationBureauId)
 * 资源来源 : rf (resourceFrom)
 *         
 */
public class CoursewareEntry extends BaseDBObject{
	public CoursewareEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public CoursewareEntry(ObjectId userId,String userName,long timestamp,String coverId,ObjectId fileId,List<RepertoryProperty> properties,
									int isSaved,ObjectId educationBureauId,String resourceFrom) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("uid", userId)
													 .append("una", userName)
													 .append("ts", timestamp)
													 .append("coid", coverId)
													 .append("fid", fileId)
													 .append("rf", resourceFrom)
													 .append("props", MongoUtils.convert(MongoUtils.fetchDBObjectList(properties)))
													 .append("is", isSaved)
													 .append("ir",Constant.ZERO)
													 .append("iig",Constant.ZERO)
													 .append("edid",educationBureauId);

		setBaseEntry(baseEntry);
	}
	
	public String getUserId() {
		return getSimpleStringValue("uid");
	}
	public void setUserId(String userId) {
		setSimpleValue("uid", userId);
	}
	
	public String getUserName() {
		return getSimpleStringValue("una");
	}
	public void setUserName(String userName) {
		setSimpleValue("una", userName);
	}
	
	public long getTimestamp() {
		return getSimpleLongValue("ts");
	}
	public void setTimestamp(long timestamp) {
		setSimpleValue("ts", timestamp);
	}
	
	public String getCoverId() {
		return getSimpleStringValue("coid");
	}
	public void setCoverId(String coverId) {
		setSimpleValue("coid", coverId);
	}
	
	public ObjectId getFileId() {
		return getSimpleObjecIDValue("fid");
	}
	public void setFileId(String fileId) {
		setSimpleValue("fid", fileId);
	}
	
	public List<RepertoryProperty> getPropList() {
		List<RepertoryProperty> resultList =new ArrayList<RepertoryProperty>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("props");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new RepertoryProperty((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setPropList(Collection<RepertoryProperty> propList) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(propList);
		setSimpleValue("props", MongoUtils.convert(list));
	}
	
	public ObjectId getEducationBureauId() {
		return getSimpleObjecIDValue("edid");
	}
	public void setEducationBureauId(String educationBureauId) {
		setSimpleValue("edid", educationBureauId);
	}
	
	public String getResourceFrom() {
		return getSimpleStringValue("rf");
	}
	public void setResourceFrom(String resourceFrom) {
		setSimpleValue("rf", resourceFrom);
	}
	
	public int getIsIgnore() {
		return getSimpleIntegerValueDef("iig",0);
	}
	public void setIsIgnore(int isIgnore) {
		setSimpleValue("iig", isIgnore);
	}
}
