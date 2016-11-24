package com.pojo.exercise;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.pojo.exercise.ExerciseItemEntry.Item;
import com.sys.constants.Constant;


/**
 * 测试题目DTO，包括考试，练习等
 * @author fourer
 *
 */
public class ExerciseMixItemInfoDTO {

	private String id;
	private String name;
	private String documentId;
	private ObjectId itemId;
	private String bigTitleId; //大题题号
	private String titleId;
	private int type;
	private double score;
	private String answer;
	private int spendTime;
	private int selectCount;
	private String opts;
	
	//todo
	public ExerciseMixItemInfoDTO()
	{
		
	}
	
	public ExerciseMixItemInfoDTO(ExerciseMixItem e)
	{
		this.id=e.getItemId().toString();
		this.bigTitleId=e.getBigTitleId();
		this.documentId=e.getDocumentId().toString();
		this.titleId=e.getTitleId();
		this.type=e.getType();
		this.score=e.getScore();
		this.answer=e.getAnswers();
		this.selectCount=e.getSelectCount();
	}
	
	public Item covertToItem()
	{
		List<String> list =null;
		if(StringUtils.isNotBlank(opts))
		{
			String[] optArr=StringUtils.split(opts);
			list =Arrays.asList(optArr);
		}
		Item item =new Item(titleId, type, score, answer,selectCount,list);
		return item;
	}
	
	
	public ExerciseItemEntry covertToExerciseItemEntry()
	{
		String[] tits=this.titleId.split(Constant.SEPARATE_LINE);
		ExerciseItemEntry e=new ExerciseItemEntry(new ObjectId(this.documentId),name, tits[0], spendTime);
		return e;
	}
	
	public String getDocumentId() {
		return documentId;
	}


	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}


	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getSpendTime() {
		return spendTime;
	}
	public void setSpendTime(int spendTime) {
		this.spendTime = spendTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
	}

	public ObjectId getItemId() {
		return itemId;
	}

	public void setItemId(ObjectId itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpts() {
		return opts;
	}

	public void setOpts(String opts) {
		this.opts = opts;
	}

	public String getBigTitleId() {
		return bigTitleId;
	}

	public void setBigTitleId(String bigTitleId) {
		this.bigTitleId = bigTitleId;
	}

	@Override
	public String toString() {
		return "ExerciseMixItemInfoDTO [id=" + id + ", name=" + name
				+ ", documentId=" + documentId + ", itemId=" + itemId
				+ ", bigTitleId=" + bigTitleId + ", titleId=" + titleId
				+ ", type=" + type + ", score=" + score + ", answer=" + answer
				+ ", spendTime=" + spendTime + ", selectCount=" + selectCount
				+ ", opts=" + opts + "]";
	}
	
	
	
	
	
}
