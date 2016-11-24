package com.pojo.competition;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 评比项目
 * @author cxy
 *唯一Id : itid(itemId)
 *项目名称 : itna(itemName)
 *说明 : itps(itemPostscript)
 *满分 : itfs(itemFullScore)
 */
public class CompetitionItem extends BaseDBObject{
	public CompetitionItem(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public CompetitionItem(String itemName,String itemPostscript, int itemFullScore) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("itid", new ObjectId())
		.append("itna", itemName)
		.append("itps", itemPostscript)
		.append("itfs", itemFullScore);
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getItemId() {
		return getSimpleObjecIDValue("itid");
	}
	public void setItemId(ObjectId itemId) {
		setSimpleValue("itid", itemId);
	}
	
	public String getItemName() {
		return getSimpleStringValue("itna");
	}
	public void setItemName(String itemName) {
		setSimpleValue("itna", itemName);
	}
	
	public String getItemPostscript() {
		return getSimpleStringValue("itps");
	}
	public void setItemPostscript(String itemPostscript) {
		setSimpleValue("itps", itemPostscript);
	}
	
	public int getItemFullScore() {
		return getSimpleIntegerValue("itfs");
	}

	public void setItemFullScore(String itemFullScore) {
		setSimpleValue("itfs", itemFullScore);
	}
	
}
