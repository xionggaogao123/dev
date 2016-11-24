package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.NameValuePair;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;


/**
 * 商品信息
 * <pre>
 * collectionName:egoods
 * </pre>
 * <pre>
 * {
      nm:名称
      int:介绍
      pr:价格(分)
      dpr:折扣价（分）
      sim:推荐图片
      pin:品牌
      ims:小图片，用户商品介绍
      [
       {
        id:
        v:
       }
      ]
      hts:商品详情
      st:状态：0正常销售 1已经下架 2未发布
      sc:销量
      ks:种类列表 参见Kind
        [
		      {
			 *  id:
			 *  nm：名字
			 *  ls:规格列表
			 *  [
			 *   {
			 *    id:
			 *    v:
			 *   }
			 *  ]
			 * }
        ]
      cs:Comment summary
      pl:popular level 人气（点击量）
      gcl:goodsCategoryList  产品一级分类
      lgcl:levelGoodsCategoryList 产品二级分类
 	  grcl:gradeCategoryList 年级分类
      bc: 课外阅读类书籍分类
      eoff:经验值最多抵用价格（分）
      voff:抵用券最多抵用价格（分）
      etid:expressTemplate id 运费模板id

 * }
 * </pre>
 * @author fourer
 */
public class EGoodsEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3570862972266115779L;

	public EGoodsEntry(){}

	public EGoodsEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public String getPinpai() {
		return getSimpleStringValue("pin");
	}

	public void setPinpai(String pinpai) {
		setSimpleValue("pin", pinpai);
	}

	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public String getIntroduce() {
		return getSimpleStringValue("int");
	}
	public void setIntroduce(String introduce) {
		setSimpleValue("int", introduce);
	}
	public int getPr() {
		return getSimpleIntegerValue("pr");
	}
	public void setPr(int pr) {
		setSimpleValue("pr", pr);
	}
	public String getSuggestImage(){
		return getSimpleStringValue("sim");
	}
	public void setSuggestImage(String image){
		setSimpleValue("sim", image);
	}
	public List<IdValuePair> getIms() {
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
	public void setIms(List<IdValuePair> ims) {
		List<DBObject> ls =MongoUtils.fetchDBObjectList(ims);
		setSimpleValue("ims", MongoUtils.convert(ls));
	}
	public String getHtmls() {
		return getSimpleStringValue("hts");
	}
	public void setHtmls(String htmls) {
		setSimpleValue("hts", htmls);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public int getActivity() {
		if(getBaseEntry().containsField("actvt")){
			return getSimpleIntegerValue("actvt");
		}else{
			return -1;
		}
	}
	public void setActivity(int activity) {setSimpleValue("actvt", activity);}
	public int getGroupPurchase(){
		if(getBaseEntry().containsField("gpc")){
			return getSimpleIntegerValue("gpc");
		}else{
			return -1;
		}
	}

	public void setGroupPurchase(int groupPurchase) {setSimpleValue("gpc", groupPurchase);}

	//爆款
	public int getMaxOut(){
		if(getBaseEntry().containsField("mxt")){
			return getSimpleIntegerValue("mxt");
		}else{
			return -1;
		}
	}

	public void setMaxOut(int maxOut) {setSimpleValue("mxt", maxOut);}
	public int getSellCount() {
		return getSimpleIntegerValue("sc");
	}
	public void setSellCount(int sellCount) {
		setSimpleValue("sc", sellCount);
	}
	public List<Kind> getKindList() {
		List<Kind> retList =new ArrayList<Kind>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ks");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new Kind((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setKindList(List<Kind> kindList) {
		List<DBObject> ls =MongoUtils.fetchDBObjectList(kindList);
		setSimpleValue("ks", MongoUtils.convert(ls));
	}

	public List<NameValuePair> getCommentSummary(){
		List<NameValuePair> commentSummary = new ArrayList<NameValuePair>();
		BasicDBList list = (BasicDBList)getSimpleObjectValue("cs");
		if(null != list){
			for(Object o : list){
				commentSummary.add(new NameValuePair((BasicDBObject)o));
			}
		}
		return commentSummary;
	}

	public void setCommentSummary(List<NameValuePair> commentSummary){
		setSimpleValue("cs",MongoUtils.convert(MongoUtils.fetchDBObjectList(commentSummary)));
	}

	public int getPopularLevel(){
		return getSimpleIntegerValue("pl");
	}

	public void setPopularLevel(int level){
		setSimpleValue("pl", level);
	}

	public List<ObjectId> getGoodsCategoryList(){
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(getBaseEntry().containsField("gcl")) {
			BasicDBList list = (BasicDBList) getSimpleObjectValue("gcl");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					retList.add((ObjectId) o);
				}
			}
		}
		return retList;
	}

	public void setGoodsCategoryList(List<ObjectId> goodsCategoryList){
		setSimpleValue("gcl", MongoUtils.convert(goodsCategoryList));
	}

	public List<ObjectId> getLevelGoodsCategoryList(){
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(getBaseEntry().containsField("lgcl")) {
			BasicDBList list = (BasicDBList) getSimpleObjectValue("lgcl");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					retList.add((ObjectId) o);
				}
			}
		}
		return retList;
	}

	public void setLevelGoodsCategoryList(List<ObjectId> levelGoodsCategoryList){
		setSimpleValue("lgcl", MongoUtils.convert(levelGoodsCategoryList));
	}

	public List<ObjectId> getGradeCategoryList(){
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(getBaseEntry().containsField("grcl")) {
			BasicDBList list = (BasicDBList) getSimpleObjectValue("grcl");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					retList.add((ObjectId) o);
				}
			}
		}
		return retList;
	}

	public void setGradeCategoryList(List<ObjectId> gradeCategoryList){
		setSimpleValue("grcl",MongoUtils.convert(gradeCategoryList));
	}

	public int getBookCategory(){
		if(getBaseEntry().containsField("bc")){
			return getSimpleIntegerValue("bc");
		}
		return 0;
	}

	public void setBookCategory(int bookCategory){
		setSimpleValue("bc",bookCategory);
	}

	public int getDiscountPrice(){
		if(getBaseEntry().containsField("dpr")){
			return getSimpleIntegerValue("dpr");
		}
		return getSimpleIntegerValue("pr");
	}

	public void setDiscountPrice(int discountPrice){
		setSimpleValue("dpr", discountPrice);
	}

	public int getExperienceOff(){
		return getSimpleIntegerValueDef("eoff", 0);
	}

	public void setExperienceOff(int experienceOff){
		setSimpleValue("eoff", experienceOff);
	}

	public int getVoucherOff(){
		return getSimpleIntegerValueDef("voff", 0);
	}

	public void setVoucherOff(int voucherOff){
		setSimpleValue("voff", voucherOff);
	}

	public ObjectId getExpTempId(){
		ObjectId etid = null;
		if(getBaseEntry().containsField("etid")){
			etid = getSimpleObjecIDValue("etid");
		}

		return etid;
	}

	public void setExpTempId(ObjectId expTempId){
		setSimpleValue("etid",expTempId);
	}

	/**
	 * 种类规则表 
	 * @author fourer
	 * {
	 *  id:
	 *  
	 *  nm：名字
	 *  ls:规格列表
	 *  [
	 *   {
	 *    id:
	 *    v:
	 *   }
	 *  ]
	 * }
	 */
	public static class Kind extends BaseDBObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -580185485475369231L;

		
		public Kind(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		public Kind(ObjectId id,String name,List<Spec> pairs) {
			List<DBObject> ls =MongoUtils.fetchDBObjectList(pairs);
			BasicDBObject baseDBObject =new BasicDBObject()
			.append("id", id)
			.append("nm", name)
			.append("ls", MongoUtils.convert(ls));
			setBaseEntry(baseDBObject);
		}
		
		public ObjectId getId() {
			return getSimpleObjecIDValue("id");
		}
		public void setId(ObjectId id) {
			setSimpleValue("id", id);
		}
		public String getName() {
			return getSimpleStringValue("nm");
		}
		public void setName(String name) {
			setSimpleValue("nm", name);
		}
		public List<Spec> getList() {
			List<Spec> retList =new ArrayList<Spec>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("ls");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new Spec((BasicDBObject)o));
				}
			}
			return retList;
		}
		public void setList(List<Spec> list) {
			List<DBObject> ls =MongoUtils.fetchDBObjectList(list);
			setSimpleValue("ls", MongoUtils.convert(ls));
		}

		/**
		 * 按老数据格式取得数据，只为转换数据使用
		 * @return
		 */
		@Deprecated
		public List<IdValuePair> getIVList() {
			List<IdValuePair> retList =new ArrayList<IdValuePair>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("ls");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new IdValuePair((BasicDBObject)o));
				}
			}
			return retList;
		}
	}

	public static class Spec extends BaseDBObject{

		public Spec(){}

		public Spec(BasicDBObject baseEntry){
			setBaseEntry(baseEntry);
		}

		public Spec(ObjectId id, String name, int price){
			BasicDBObject baseEntry = new BasicDBObject()
					.append("id", id)
					.append("nm", name)
					.append("pr", price);
			setBaseEntry(baseEntry);
		}

		public ObjectId getSpecId(){
			return getSimpleObjecIDValue("id");
		}

		public void setSpecId(ObjectId id){
			setSimpleValue("id", id);
		}

		public String getSpecName(){
			return getSimpleStringValue("nm");
		}

		public void setSpecName(String name){
			setSimpleValue("nm", name);
		}

		public int getSpecPrice(){
			return getSimpleIntegerValue("pr");
		}

		public void setSpecPrice(int price){
			setSimpleValue("pr", price);
		}

	}
	
}
