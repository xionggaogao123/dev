package com.pojo.itempool;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 题库
 * <pre>
 * collectionName:itempool
 * </pre>
 * <pre>
 * {
 *  gtys:学段：对应的ipdictionary中的_id
 *  sty:学科；对应的ipdictionary中的_id
 *  lel:难易程度；1：易 2：较易 3：中 4 较难 5 难
 *  ty:题目类型；对应的ipdictionary中的_id
 *  oty:对应ExerciseItemType
 *  scs:知识点集合；对应的ipdictionary中的_id  ty=9
 *  [
 *  ] 
 *  psbs:单元；对应的ipdictionary中的_id ty=6
    [
    ]
    ows:拥有者
    [
    ]
 *  grs:关联到GradeType;这是为了学生或者老师知识点选题时精确选择题目
 *  [ 
 *   1,
 *   2,
 *  ]
 *  sco:分值；
 *  
 *  qu:题干
 *  pan:答案解析
 *  an:答案
 *  kg: 客观题相关字段 
 *  {
 *    an:客观题答案
 *    zc:组选个数
 *    sc:选项个数
 *  }
 * }
 * </pre>
 * @author fourer
 */
public class ItemPoolEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8593020010011695458L;
	
	
	public ItemPoolEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public ItemPoolEntry(){}

	
	/**
	 * 
	 * @param xueduan
	 * @param subject
	 * @param level
	 * @param type
	 * @param sco
	 * @param clty
	 * @param score
	 * @param question
	 * @param answer
	 * @param parseAnser
	 * @param item
	 * @param zhangjie1
	 * @param zhangjie2
	 * @param gradeList
	 * @param origItemType 
	 */
	public ItemPoolEntry(ObjectId xueduan, ObjectId subject,int level, ObjectId type,List<ObjectId> scs,
			 double score, String question, String answer,String parseAnser,
			ObjectiveItem item,List<ObjectId> psbs,List<Integer> gradeList,Integer origItemType) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("gtys", xueduan) //学段
		.append("sty", subject) //学科
		.append("lel", level) //难易程度
		.append("ty", type) //题目类型
		.append("scs", MongoUtils.convert(scs)) //只是面
		.append("sco", score) //分值
		.append("qu", question)
		.append("an", answer)
		.append("pan", parseAnser)
		.append("psbs", MongoUtils.convert(psbs)) //教材
		.append("grs", MongoUtils.convert(gradeList))
		.append("oty", origItemType)
		;
		if(null!=item)
		{
			baseEntry.append("kg", item.getBaseEntry());
		}
		else
		{
			baseEntry.append("kg", null);
		}
		
		setBaseEntry(baseEntry);
	}
	

	
	public List<ObjectId> getScList()
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("scs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setScList(List<ObjectId> scs)
	{
		setSimpleValue("scs", MongoUtils.convert(scs));
	}
	
	
	public List<ObjectId> getPsbList()
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("psbs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setPsbList(List<ObjectId> scs)
	{
		setSimpleValue("psbs", MongoUtils.convert(scs));
	}

	public List<Integer> getGradeList(){
		List<Integer> retList = new ArrayList<Integer>();
		BasicDBList list = (BasicDBList)getSimpleObjectValue("grs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((Integer)o);
			}
		}
		return retList;
	}

	public void setGradeList(List<Integer> gradeList){
		setSimpleValue("grs", MongoUtils.convert(gradeList));
	}
	
	public List<ObjectId> getOwnList()
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ows");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setOwnList(List<ObjectId> scs)
	{
		setSimpleValue("ows", MongoUtils.convert(scs));
	}
	
	
	public ObjectId getXueduan() {
		return getSimpleObjecIDValue("gtys");
	}
	public void setXueduan(ObjectId stage){
		setSimpleValue("gtys", stage);
	}
	public ObjectId getItemType() {
		return getSimpleObjecIDValue("ty");
	}
	
	
	public String getImage() {
		return getSimpleStringValue("img");
	}

	public void setImage(String image) {
		setSimpleValue("img", image);
	}

	
	public String getAnswer() {
		return getSimpleStringValue("an");
	}

	public void setAnswer(String answer) {
		setSimpleValue("an", answer);
	}


	public ObjectId getSubject() {
		return getSimpleObjecIDValue("sty");
	}

	public void setSubject(ObjectId subject) {
		setSimpleValue("sty",subject);
	}
	
	public int getLevel() {
		return getSimpleIntegerValue("lel");
	}

	public void setLevel(int level) {
		setSimpleValue("lel",level);
	}
	public int getOrigType() {
		return getSimpleIntegerValue("oty");
	}
	public void setOrigType(int origType){
		setSimpleValue("oty", origType);
	}

	

	//获得分值
	public double getScore() {
		return getSimpleDoubleValue("sco");
	}

	public void setScore(double score) {
		setSimpleValue("sco",score);
	}

	public String getQuestion() {
		return getSimpleStringValue("qu");
	}

	public void setQuestion(String question) {
		setSimpleValue("qu",question);
	}
	
	public String getParseAnser() {
		return getSimpleStringValue("pan");
	}

	public void setParseAnser(String parseAnser) {
		setSimpleValue("pan",parseAnser);
				
	}

	public ObjectiveItem getItem() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("kg");
		if(null!=dbo)
		{
			return new ObjectiveItem(dbo);
		}
		return null;
	}

	public void setItem(ObjectiveItem item) {
		setSimpleValue("kg",item.getBaseEntry());
	}

	/**
	 * 客观题相关字段
	 * {
     *     an:客观题答案
     *     zc:组选个数
     *     sc:选项个数
     *  }
	 * 
	 * @author fourer
	 *
	 */
	public  static class ObjectiveItem  extends BaseDBObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7937634428344660914L;
		
		
		public ObjectiveItem(BasicDBObject baseEntry) {
			super(baseEntry);
		}


		public ObjectiveItem(String answer, int zuSelectCount, int selectCount) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("an", answer)
			.append("zc", zuSelectCount)
			.append("sc", selectCount);
			setBaseEntry(dbo);
		}
		
		
		public String getAnswer() {
			return getSimpleStringValue("an");
		}
		public void setAnswer(String answer) {
			setSimpleValue("an", answer);
		}
		public int getZuSelectCount() {
			return getSimpleIntegerValue("zc");
		}
		public void setZuSelectCount(int zuSelectCount) {
			setSimpleValue("zc", zuSelectCount);
		}
		public int getSelectCount() {
			return getSimpleIntegerValue("sc");
		}
		public void setSelectCount(int selectCount) {
			setSimpleValue("sc", selectCount);
		}
		
	}
	

}
