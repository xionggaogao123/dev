package com.pojo.competition;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 评比信息
 * <pre>
 * collectionName : competition
 * </pre>
 * <pre>
 * {
 * 学期 : tt (termType)
 * 评比名称 : cna (competitionName)
 * 说明 : cps (competitionPostscript)
 * 参与范围 : cra[] (competitionRange)年级ObjectId的数组
 * 评比项目 : cits (competitionItems)
 * 	competitionItem
 * 			[
 * 				唯一Id:itid(itemId)
 * 				项目名称:itna(itemName)
 * 				说明:itps(itemPostscript)
 * 				满分:itfs(itemFullScore)
 * 			] 
 * 批次数 : cbas(competitionBatches)
 * 	competitionBatch
 * 			[
 * 				唯一Id:bid(brtchId)
 * 				批次名称:bna(brtchName)
 * 			] 
 * 生成时间 ： cd (createDate)long
 * 删除标志位 : ir(isRemoved,0为未删除，1为已删除) 
 * 所属学校id : scid(schoolId)
 * 流動紅旗數量:(rfn)redFlagNum
 * @author cxy 
 */
public class CompetitionEntry extends BaseDBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7229883935641548521L;
	public CompetitionEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public CompetitionEntry(ObjectId shcoolId,String termType,String competitionName,String competitionPostscript,
					List<ObjectId> competitionRange,List<CompetitionItem> competitionItems,List<CompetitionBatch> competitionBatches,int redFlagNum) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
				.append("cd",new Date().getTime())
				.append("ir", Constant.ZERO)
				.append("tt", termType)
				.append("cna", competitionName)
				.append("cps", competitionPostscript)
				.append("cra", MongoUtils.convert(competitionRange))
				.append("cits", MongoUtils.convert(MongoUtils.fetchDBObjectList(competitionItems)))
				.append("cbas", MongoUtils.convert(MongoUtils.fetchDBObjectList(competitionBatches)))
				.append("rfn",redFlagNum);

		setBaseEntry(baseEntry);
	}
	
	// 默认未删除
	public int getIsRemove() {
		if (getBaseEntry().containsField("ir")) {
			return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}

	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
		
	public String getSchoolId() {
		return getSimpleStringValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
		
	public String getTermType() {
		return getSimpleStringValue("tt");
	}

	public void setTermType(String termType) {
		setSimpleValue("tt", termType);
	}
	
	public String getCompetitionName() {
		return getSimpleStringValue("cna");
	}

	public void setCompetitionName(String competitionName) {
		setSimpleValue("cna", competitionName);
	}
	
	public String getCompetitionPostscript() {
		return getSimpleStringValue("cps");
	}

	public void setCompetitionPostscript(String competitionPostscript) {
		setSimpleValue("cps", competitionPostscript);
	}
	
	public List<ObjectId> getCompetitionRange() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cra");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setCompetitionRange(List<ObjectId> competitionRange) {
		setSimpleValue("cra", MongoUtils.convert(competitionRange));
	}
	
	public List<CompetitionItem> getCompetitionItems() {
		List<CompetitionItem> gradeList =new ArrayList<CompetitionItem>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cits");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				gradeList.add(new CompetitionItem((BasicDBObject)o));
			}
		}
		return gradeList;
	}
	public void setCompetitionItems(Collection<CompetitionItem> competitionItems) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(competitionItems);
		setSimpleValue("cits", MongoUtils.convert(list));
	}
	
	public List<CompetitionBatch> getCompetitionBatches() {
		List<CompetitionBatch> gradeList =new ArrayList<CompetitionBatch>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cbas");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				gradeList.add(new CompetitionBatch((BasicDBObject)o));
			}
		}
		return gradeList;
	}
	public void setCompetitionBatches(Collection<CompetitionBatch> competitionBatches) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(competitionBatches);
		setSimpleValue("cbas", MongoUtils.convert(list));
	}

	public int getRedFlagNum() {
		return getSimpleIntegerValueDef("rfn", 0);
	}

	public void setRedFlagNum(int redFlagNum) {
		setSimpleValue("rfn", redFlagNum);
	}

}
