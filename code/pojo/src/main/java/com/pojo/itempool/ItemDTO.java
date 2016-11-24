package com.pojo.itempool;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;





import com.pojo.app.IdNameValuePair;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.ItemPoolEntry.ObjectiveItem;
import com.pojo.itempool.StudentErrorItemEntry.Item;
import com.pojo.resources.ResourceDictionaryEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;




/**
 * 
 * 学生错题DTO
 * @author fourer
 *
 */
public class ItemDTO {

	private int totalCount;
	private int rightCount;
	private String id;
	private String userName;
	private String time;
	private String itemId; 
	private String source;
	private int intLevel;
	private String level;
	private int type;
	private String itemType;
	private String kwId;
	private String kw;
	private String bk;
	private String score;
	private String item;//题干
	private String myAnswer="";
	private String answer;
	private int answerNum;
	private String parse;
	private int isSaved;
	private String uploadTime;
	//是否正确
	private boolean right;
	//选择题的选项
	private String choice;
	private String image="";
	private int count;

	public ItemDTO(Item item,ItemPoolEntry e,ResourceDictionaryEntry pe )
	{
		this.id=item.getOriId().toString();
		this.time=DateTimeUtils.convert(item.getMaxTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
		int intLevel=e.getLevel();
		//1：较易 2：中 3：难
		if(Constant.ONE==intLevel)
		{
			this.level="容易";
		}
		if(Constant.TWO==intLevel)
		{
			this.level="较容易";
		}
		if(Constant.THREE==intLevel)
		{
			this.level="中等";
		}
		if(Constant.FOUR==intLevel)
		{
			this.level="较难";
		}
		if(Constant.FIVE==intLevel)
		{
			this.level="难";
		}
	
		ExerciseItemType type=ExerciseItemType.getExerciseItemType(e.getOrigType());
		this.type=type.getType();
		this.itemType=type.getName();
		if(null!=pe)
		{
		 this.kwId=pe.getID().toString();
		 this.kw=pe.getName();
		}
		this.score=String.valueOf(e.getScore());
		this.item=e.getQuestion();
		this.myAnswer=item.getMyAnswer();
		this.answer=e.getAnswer();
		this.parse=e.getParseAnser();
	
		this.count=item.getCount();
	}
	
	public ItemDTO(ItemPoolEntry e,ResourceDictionaryEntry kwPoint ,StudentExerciseEntry se)
	{
		if(null!=se)
		{
		this.totalCount=se.getTotalCount();
		this.rightCount=se.getRightCount();
		}
		this.id=e.getID().toString();
		int intLevel=e.getLevel();
		//1：较易 2：中 3：难
		if(Constant.ONE==intLevel)
		{
			this.level="容易";
		}
		if(Constant.TWO==intLevel)
		{
			this.level="较容易";
		}
		if(Constant.THREE==intLevel)
		{
			this.level="中等";
		}
		if(Constant.FOUR==intLevel)
		{
			this.level="较难";
		}
		if(Constant.FIVE==intLevel)
		{
			this.level="难";
		}
		ExerciseItemType type=ExerciseItemType.getExerciseItemType(e.getOrigType());
		this.type=type.getType();
		this.itemType=type.getName();
		if(null!=kwPoint)
		{
		  this.kwId=kwPoint.getID().toString();
		  this.kw=kwPoint.getName();
		}
		this.score=String.valueOf(e.getScore());
		this.item=e.getQuestion();
		this.answer=e.getAnswer();
		this.parse=e.getParseAnser();
		
		try
		{
			if(null!=se)
			{
			  IdNameValuePair pair= getItem(se,e.getID());
			  this.myAnswer=pair.getName();
		      right= isAnswerRight(e, pair.getName());
			}
		}catch(Exception ex)
		{}
		
		if(e.getOrigType()==ExerciseItemType.SINGLECHOICE.getType() || e.getOrigType()==ExerciseItemType.MULTICHOICE.getType())
		{
			choice="";
		}
	}
	
	
	public ItemDTO(ItemPoolEntry e,ResourceDictionaryEntry pe)
	{
		this.id=e.getID().toString();
		int intLevel=e.getLevel();
		//1：较易 2：中 3：难
		if(Constant.ONE==intLevel)
		{
			this.level="容易";
		}
		if(Constant.TWO==intLevel)
		{
			this.level="较容易";
		}
		if(Constant.THREE==intLevel)
		{
			this.level="中等";
		}
		if(Constant.FOUR==intLevel)
		{
			this.level="较难";
		}
		if(Constant.FIVE==intLevel)
		{
			this.level="难";
		}
		ExerciseItemType type=ExerciseItemType.getExerciseItemType(e.getOrigType());
		
	
		
		this.type=type.getType();
		this.itemType=type.getName();
		if(null!=pe)
		{
		  this.kwId=pe.getID().toString();
		  this.kw=pe.getName();
		}
		this.score=String.valueOf(e.getScore());
		this.item=e.getQuestion();
		this.answer=e.getAnswer();
		this.parse=e.getParseAnser();
		//if(e.getType()==ExerciseItemType.SINGLECHOICE.getType() || e.getType()==ExerciseItemType.MULTICHOICE.getType())
		{
			choice="";
		}
	}

	public ItemDTO(ItemPoolEntry e)
	{
		this.id=e.getID().toString();
		int intLevel=e.getLevel();
		this.intLevel=intLevel;
		//1：较易 2：中 3：难
		if(Constant.ONE==intLevel)
		{
			this.level="容易";
		}
		if(Constant.TWO==intLevel)
		{
			this.level="较容易";
		}
		if(Constant.THREE==intLevel)
		{
			this.level="中等";
		}
		if(Constant.FOUR==intLevel)
		{
			this.level="较难";
		}
		if(Constant.FIVE==intLevel)
		{
			this.level="难";
		}
		ExerciseItemType type= ExerciseItemType.getExerciseItemType(e.getOrigType());
		this.type=type.getType();
		this.itemType=type.getName();
		if(type.getType()==1){
			this.answerNum=e.getItem().getSelectCount();
		}else{
			this.answerNum=0;
		}
		this.score=String.valueOf(e.getScore());
		this.item=e.getQuestion();
		this.answer=e.getAnswer();
		this.parse=e.getParseAnser();
		//if(e.getType()==ExerciseItemType.SINGLECHOICE.getType() || e.getType()==ExerciseItemType.MULTICHOICE.getType())
		{
			choice="";
		}
	}

	public ItemDTO(){

	}
	
	/**
	 * 判断答题是否正确
	 * @param ipe
	 * @param answer
	 * @return
	 */
	public static Boolean isAnswerRight(ItemPoolEntry ipe,String answer)
	{
		//单选题
		if(ExerciseItemType.SINGLECHOICE.getType()==ipe.getOrigType()
				|| ExerciseItemType.TRUE_OR_FALSE.getType()==ipe.getOrigType()
						|| ExerciseItemType.GAP.getType()==ipe.getOrigType()
				)
		{
			ObjectiveItem item=ipe.getItem();
			if(null!=item && StringUtils.isNotBlank(item.getAnswer()))
			{
				return item.getAnswer().equalsIgnoreCase(answer);
			}
		}
		
		//主观题
		if(ExerciseItemType.SUBJECTIVE.getType()==ipe.getOrigType())
		{
			return false;
		}
		
		return false;
	}
	
	
	
	/**
	 * 得到一个题目的答题情况
	 * @param e
	 * @param itemid
	 * @return
	 */
	public static IdNameValuePair getItem(StudentExerciseEntry e,ObjectId itemid)
	{
		for(ExerciseItemType thisType:ExerciseItemType.values())
		{
			if(thisType==ExerciseItemType.MULTICHOICE)
				continue;
			if(null!=e.getList(thisType))
			{
				for(IdNameValuePair p:e.getList(thisType))
				{
					if( itemid.equals(p.getId()))
					{
						return p;
					}
				}
			}
		}
		return null;
	}
	
	
	
	public String getKwId() {
		return kwId;
	}

	public void setKwId(String kwId) {
		this.kwId = kwId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

//	public int getCount() {
//		return count;
//	}
//
//	public void setCount(int count) {
//		this.count = count;
//	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getMyAnswer() {
		return myAnswer;
	}

	public void setMyAnswer(String myAnswer) {
		this.myAnswer = myAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getParse() {
		return parse;
	}

	public void setParse(String parse) {
		this.parse = parse;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}


	public boolean getRight() {
		return right;
	}


	public void setRight(boolean right) {
		this.right = right;
	}

	


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBk() {
		return bk;
	}

	public void setBk(String bk) {
		this.bk = bk;
	}

	public int getIsSaved() {
		return isSaved;
	}

	public void setIsSaved(int isSaved) {
		this.isSaved = isSaved;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getIntLevel() {
		return intLevel;
	}

	public void setIntLevel(int intLevel) {
		this.intLevel = intLevel;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	@Override
	public String toString() {
		return "ItemDTO [totalCount=" + totalCount + ", rightCount="
				+ rightCount + ", id=" + id + ", time="
				+ time + ", itemId=" + itemId + ", source=" + source
				+ ", level=" + level + ", type=" + type + ", itemType="
				+ itemType + ", kwId=" + kwId + ", kw=" + kw + ", score="
				+ score + ", item=" + item + ", myAnswer=" + myAnswer
				+ ", answer=" + answer + ", parse=" + parse + ", right="
				+ right + ", choice=" + choice + "]";
	}
	
	
	
	
	
}
