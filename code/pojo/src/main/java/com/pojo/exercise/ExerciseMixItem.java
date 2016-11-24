package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.base.BaseDBObject;
import com.pojo.exercise.ExerciseItemEntry.Item;
import com.sys.constants.Constant;

/**
 * 练习题目，包含大题和小题信息
 * {
 *  di:文档ID
 *  bi:大题题号
 *  iti:小题目ID
 *  tit:小题题号
 *  ty:类型；参见ExerciseItemType
 *  so:分值
 *  ans:正确答案
 *  sc:选项个数
 * }
 * @author fourer
 *
 */
public class ExerciseMixItem extends BaseDBObject  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExerciseMixItem() {
		super();
	}

	public ObjectId getDocumentId() {
		return getSimpleObjecIDValue("di");
	}
	public void setDocumentId(ObjectId documentId) {
		setSimpleValue("di", documentId);
	}
	public ObjectId getItemId() {
		return getSimpleObjecIDValue("iti");
	}
	public void setItemId(ObjectId itemId) {
		setSimpleValue("iti", itemId);
	}
	public String getTitleId() {
		return getSimpleStringValue("tit");
	}
	public void setTitleId(String titleId) {
		setSimpleValue("tit", titleId);
	}
	
	public String getBigTitleId() {
		return getSimpleStringValue("bi");
	}

	public void setBigTitleId(String bigTitleId) {
		setSimpleValue("bi", bigTitleId);
	}

	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public double getScore() {
		return getSimpleDoubleValue("so");
	}
	public void setScore(double score) {
		setSimpleValue("so", score);
	}
	public String getAnswers() {
		return getSimpleStringValue("ans");
	}
	public void setAnswers(String answers) {
		setSimpleValue("ans", answers);
	}
	
	
	
	public int getSelectCount() {
		return getSimpleIntegerValue("sc");
	}

	public void setSelectCount(int selectCount) {
		setSimpleValue("sc", selectCount);
	}

	public static List<ExerciseMixItem> build(ExerciseItemEntry entry)
	{
		List<ExerciseMixItem> retList =new ArrayList<ExerciseMixItem>();
		for(Item item:entry.getItemList())
		{
			ExerciseMixItem eit =new ExerciseMixItem();
			eit.setDocumentId(entry.getDocumentId());
			eit.setBigTitleId(entry.getTitleId());
			eit.setItemId(item.getId());
			eit.setTitleId(entry.getTitleId()+Constant.SEPARATE_LINE+item.getTitleId());
			eit.setType(item.getType());
			eit.setScore(item.getScore());
			eit.setAnswers(item.getAnswers());
			eit.setSelectCount(item.getSelectCount());
			retList.add(eit);
		}
		return retList;
	}
	
	public static ExerciseMixItem build1(ExerciseItemEntry entry)
	{
		ExerciseMixItem eit =new ExerciseMixItem();
		Item item =entry.getItemList().get(0);
		if(null!=item)
		{
			eit.setDocumentId(entry.getDocumentId());
			eit.setBigTitleId(entry.getTitleId());
			eit.setItemId(item.getId());
			eit.setTitleId(entry.getTitleId()+Constant.SEPARATE_LINE+item.getTitleId());
			eit.setType(item.getType());
			eit.setScore(item.getScore());
			eit.setAnswers(item.getAnswers());
			eit.setSelectCount(item.getSelectCount());
		}
		return eit;
	}
	
}
