package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.exercise.ExerciseItemEntry.Item;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

/**
 * 练习题目DTO，包括考试题目等
 * @author fourer
 *
 */
public class ExerciseItemDTO {

	private String docId;
	private String name;
	private String titleId;
	private int time;
	private String lesson;
	private List<ItemDTO> list = new ArrayList<ExerciseItemDTO.ItemDTO>();
    
	
	
	public ExerciseItemDTO() {
		super();
	}


	public ExerciseItemDTO(ExerciseItemEntry e)
	{
		this.docId=e.getID().toString();
		this.name=e.getName();
		this.titleId=e.getTitleId();
		this.time=e.getSpendTime();
		for(Item item:e.getItemList())
		{
			list.add(new ItemDTO(item));
		}
	}
	
	
	public ExerciseItemEntry convert() throws Exception
	{
		if(null==this.docId||!ObjectId.isValid(this.docId))
			throw new IllegalParamException();
		ExerciseItemEntry e=new ExerciseItemEntry(new ObjectId(this.docId), name, titleId, time);
		List<Item> itemList =new ArrayList<ExerciseItemEntry.Item>();
		for(ItemDTO dto:this.list)
		{
			Item item =new Item(dto.getTitleId(), dto.getItemType(), dto.getScore(), dto.getRightAnswer(), dto.getSelectCount(), dto.getOption());
			itemList.add(item);
		}
		e.setItemList(itemList);
		return e;
	}
	
	
	public static ExerciseItemDTO create()
	{
		List<Item> items =new ArrayList<ExerciseItemEntry.Item>();
		
		List<String> option =new ArrayList<String>();
		option.add("A");
		option.add("B");
		option.add("C");
		option.add("D");
		//Item item =new Item("1", 1, 2D, "A", 1, option);
		Item item =new Item("1", 1, 2D, "A", 4, option);
		items.add(item);
		
		
		ExerciseItemEntry e=new ExerciseItemEntry(new ObjectId(), "", "1", 45,items);
		e.setID(new ObjectId());
		ExerciseItemDTO dto =new ExerciseItemDTO(e);
		
		return dto;
	}
	
	
	
	public String getLesson() {
		return lesson;
	}


	public void setLesson(String lesson) {
		this.lesson = lesson;
	}


	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public List<ItemDTO> getList() {
		return list;
	}

	public void setList(List<ItemDTO> list) {
		this.list = list;
	}
	public double getTotalScore() {
		double d=0;
		for(ItemDTO item:list)
		{
			d+=item.getScore();
		}
		return d;
	}

	public void setTotalScore(double totalScore) {
		//this.totalScore = totalScore;
	}


	


	@Override
	public String toString() {
		return "ExerciseItemDTO [docId=" + docId + ", name=" + name
				+ ", titleId=" + titleId + ", time=" + time + ", list=" + list
				+ "]";
	}





	/**
	 * 小题
	 * @author fourer
	 *
	 */
	public static class ItemDTO
	{
		private String id;
		private String titleId;
		private double score;
		private int itemType;
		private List<String> option;
		private String rightAnswer;
		private int selectCount;
		
		
		
		public ItemDTO() {
			super();
		}


		public ItemDTO (Item item)
		{
			this.id=item.getId().toString();
			this.titleId=item.getTitleId();
			this.score=item.getScore();
			this.itemType=ExerciseItemType.getExerciseItemType(item.getType()).getType();
			//this.option=item.getOptions();
			if(itemType==1)
			{
				List<String> option =new ArrayList<String>();
				//option.addAll(default_option);
				
				option.add("A");
				if(item.getSelectCount()==Constant.TWO)
				{
					option.add("B");
				}
				
				if(item.getSelectCount()==Constant.THREE)
				{
					option.add("B");
					option.add("C");
				}if(item.getSelectCount()==Constant.FOUR)
				{
					option.add("B");
					option.add("C");
					option.add("D");
				}
				
				if(item.getSelectCount()==Constant.FIVE)
				{
					option.add("B");
					option.add("C");
					option.add("D");
					option.add("E");
				}
				if(item.getSelectCount()==Constant.SIX)
				{
					option.add("B");
					option.add("C");
					option.add("D");
					option.add("E");
					option.add("F");
				}
				if(item.getSelectCount()==Constant.SEVEN)
				{
					option.add("B");
					option.add("C");
					option.add("D");
					option.add("E");
					option.add("F");
					option.add("G");
				}
				this.option=option;
			}
			this.selectCount=item.getSelectCount();
			this.rightAnswer=item.getAnswers();
		}
		
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitleId() {
			return titleId;
		}
		public void setTitleId(String titleId) {
			this.titleId = titleId;
		}
		public double getScore() {
			return score;
		}
		public void setScore(double score) {
			this.score = score;
		}
		public int getItemType() {
			return itemType;
		}
		public void setItemType(int itemType) {
			this.itemType = itemType;
		}
		public List<String> getOption() {
			return option;
		}
		public void setOption(List<String> option) {
			this.option = option;
		}
		public String getRightAnswer() {
			return rightAnswer;
		}
		public void setRightAnswer(String rightAnswer) {
			this.rightAnswer = rightAnswer;
		}


		public int getSelectCount() {
			return selectCount;
		}


		public void setSelectCount(int selectCount) {
			this.selectCount = selectCount;
		}


		@Override
		public String toString() {
			return "ItemDTO [id=" + id + ", titleId=" + titleId + ", score="
					+ score + ", itemType=" + itemType + ", option=" + option
					+ ", rightAnswer=" + rightAnswer + ", selectCount="
					+ selectCount + "]";
		}
		
		
	}
}
