package com.pojo.emarket;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 商品信息
 * <pre>
 * collectionName:goods
 * </pre>
 * <pre>
 * {
       subs:科目ID；对应SubjectType
       ccgts:年级；对应GradeType
       [
         
       ]
       ty:种类；对 应课程，已购课程
       st:状态；对应GoodsState
       nm:商品名称
       desc:商品介绍
       tdesc:老师介绍
       ow:拥有者ID
       rid:关联ID;当是课程的时候，是课程ID
       rgid:关联商品ID
       pr:价格
       sc:卖出个数
       lut:最后更新时间
       comc:评论个数；
       coms: 参见Comment
       io:是否打开
       [
             * {
			 *  ui:用户ID
			 *  com:评论内容
			 *  t:时间
			 * }
       ]
       et:有效时间 // long
 	   enddt:截止日期  //long
 * }
 * </pre>
 * @author fourer
 */
public class GoodsEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7964374225292256093L;

	
	public GoodsEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public GoodsEntry(List<Integer> subjects, List<Integer> cloudClassGradeTypes,
			int goodsType, int goodsState, ObjectId owner, String name,ObjectId relateId,ObjectId relateGoodId,
			double price, int sellCount, long expireTime,String desc,long enddate,int isopen) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("subs", MongoUtils.convert(subjects))
		.append("ccgts", MongoUtils.convert(cloudClassGradeTypes))
		.append("ty", goodsType)
		.append("st", goodsState)
		.append("ow", owner)
		.append("nm", name)
		.append("rid", relateId)
		.append("rgid", relateGoodId)
		.append("pr", price)
		.append("sc", sellCount)
		.append("lut", System.currentTimeMillis())
		.append("comc", Constant.ZERO)
		.append("coms", new BasicDBList())
		.append("et", expireTime)
        .append("desc",desc).append("tdesc", null)
				.append("enddt",enddate)
				.append("io",isopen);
		setBaseEntry(dbo);
	}
	
	public long getExpireTime() {
		return getSimpleLongValue("et");
	}
	public void setExpireTime(long expireTime) {
		setSimpleValue("et", expireTime);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public List<Integer> getSubjects() {

        List<Integer> retList =new ArrayList<Integer>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("subs");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((Integer)o);
            }
        }
        return retList;
	}
	public void setSubjects(List<Integer> subjects) {
		setSimpleValue("subs", subjects);
	}
	
	public List<Integer> getCloudClassGradeTypes() {
		List<Integer> retList =new ArrayList<Integer>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ccgts");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((Integer)o);
			}
		}
		return retList;
	}
	
	public void setCloudClassGradeTypes(List<Integer> ccgts) {
		setSimpleValue("ccgts", ccgts);
	}
	
	public int getGoodsType() {
		return getSimpleIntegerValue("ty");
	}
	public void setGoodsType(int goodsType) {
		setSimpleValue("ty", goodsType);
	}
	
	
	public List<Comment> getGoodsCommentList() {
		List<Comment> retList =new ArrayList<Comment>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("coms");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new Comment((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setGoodsCommentList(List<Comment> goodsCommentList) {
		
		List<DBObject> list =MongoUtils.fetchDBObjectList(goodsCommentList);
		setSimpleValue("coms", MongoUtils.convert(list));
	}


	public int getGoodsState() {
		return getSimpleIntegerValue("st");
	}
	public void setGoodsState(int goodsState) {
		setSimpleValue("st", goodsState);
	}
	public long getEnddate() {
		return getSimpleLongValue("enddt");
	}
	public void setEnddate(long enddate) {
		setSimpleValue("enddt",enddate);
	}
	public ObjectId getOwner() {
		return getSimpleObjecIDValue("ow");
	}
	public void setOwner(ObjectId owner) {
		setSimpleValue("ow", owner);
	}
	public ObjectId getRelateId() {
		return getSimpleObjecIDValue("rid");
	}
	public void setRelateId(ObjectId relateId) {
		setSimpleValue("rid", relateId);
	}
	public ObjectId getRelateGoodId() {
		return getSimpleObjecIDValue("rgid");
	}
	public void setRelateGoodId(ObjectId relateGoodId) {
		setSimpleValue("rgid", relateGoodId);
	}
	public double getPrice() {
		return getSimpleDoubleValue("pr");
	}
	public void setPrice(double price) {
		setSimpleValue("pr", price);
	}
	public int getSellCount() {
		return getSimpleIntegerValue("sc");
	}
	public void setSellCount(int sellCount) {
		setSimpleValue("sc", sellCount);
	}
	public long getLastUpdateTime() {
		return getSimpleLongValue("lut");
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		setSimpleValue("lut", lastUpdateTime);
	}
	public int getCommentCount() {
		return getSimpleIntegerValue("comc");
	}
	public void setCommentCount(int commentCount) {
		setSimpleValue("comc", commentCount);
	}


    public String getDesc() {
        return getSimpleStringValue("desc");
    }
    public void setDesc(String desc) {
        setSimpleValue("desc", desc);
    }

	public int getIsopen() {
		return getSimpleIntegerValue("io");
	}

	public void setIsopen(int isopen) {
		setSimpleValue("io",isopen);
	}

    public String getTeacherDesc() {
        return getSimpleStringValue("tdesc");
    }
    public void setTeacerDesc(String desc) {
        setSimpleValue("tdesc", desc);
    }
}
