package com.pojo.comment;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 复杂评价中的星级，依附于ComplexCommentEntry
 * {
 *  星级评价名称 : na(name)
 *  星级 : sl(starLevel) (1~5)int
 * }
 * </pre>
 * @author cxy
 */
public class StarLevel extends BaseDBObject{
	
	public StarLevel(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public StarLevel(String name,String starLevel) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("na", name)
									 .append("sl", starLevel);
		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("na");
	}
	
	public void setName(String name) {
		setSimpleValue("na", name);
	}
	
	 public String getStarLevel(){
		 return getSimpleStringValue("sl");
	 }
	 public void setStarLevel(int starLevel){
		 setSimpleValue("sl",starLevel);
	 }
}
