package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
/**
 * 商品评论
 * <pre>
 * collectionName:ecomment
 * </pre>
 * <pre>
 * {
     ui:用户ID
     egi:商品ID
     oi:订单ID
     c:内容
     sc:评分
     ims:图片
     [
      {
       id:
       v:
      }
     ]
 * }
 * </pre>
 * @author fourer
 */
public class EGoodsCommentEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8084638979847114297L;
	
	public EGoodsCommentEntry(){}
	public EGoodsCommentEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	public EGoodsCommentEntry(ObjectId userId,ObjectId eGoodsId,ObjectId orderId, String content,int score,List<IdValuePair> images) {
		
		List<DBObject> ls =MongoUtils.fetchDBObjectList(images);
		BasicDBObject baseEntry=new BasicDBObject()
		.append("ui", userId)
		.append("egi", eGoodsId)
		.append("oi", orderId)
		.append("c", content)
		.append("sc", score)
		.append("ims", MongoUtils.convert(ls));
		setBaseEntry(baseEntry);
	}
	
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	public ObjectId geteGoodsId() {
		return getSimpleObjecIDValue("egi");
	}
	public void seteGoodsId(ObjectId eGoodsId) {
		setSimpleValue("egi", eGoodsId);
	}
	public ObjectId getOrderId(){
		if(getBaseEntry().containsField("oi")){
			return getSimpleObjecIDValue("oi");
		}
		return new ObjectId();
	}
	public void setOrderId(ObjectId orderId){
		setSimpleValue("oi", orderId);
	}
	public String getContent() {
		return getSimpleStringValue("c");
	}
	public void setContent(String content) {
		setSimpleValue("c", content);
	}
	public int getScore() {
		return getSimpleIntegerValue("sc");
	}
	public void setScore(int score) {
		setSimpleValue("sc", score);
	}
	public List<IdValuePair> getImageList() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ims");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setImageList(List<IdValuePair> imageList) {
		List<DBObject> ls =MongoUtils.fetchDBObjectList(imageList);
		setSimpleValue("ims", MongoUtils.convert(ls));
	}

}
