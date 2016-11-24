package com.pojo.competition;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 评比批次
 * @author cxy
 *唯一Id : bid(brtchId)
 *批次名称 : bna(brtchName)
 */
public class CompetitionBatch extends BaseDBObject{
	public CompetitionBatch(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public CompetitionBatch(String brtchName) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("bid", new ObjectId())
		.append("bna", brtchName);
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getBatchId() {
		return getSimpleObjecIDValue("bid");
	}
	public void setBatchId(ObjectId batchId) {
		setSimpleValue("bid", batchId);
	}
	
	public String getBatchName() {
		return getSimpleStringValue("bna");
	}
	public void setBatchName(String brtchName) {
		setSimpleValue("bna", brtchName);
	}
}
