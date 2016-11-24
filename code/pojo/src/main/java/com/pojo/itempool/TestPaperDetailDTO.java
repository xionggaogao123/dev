package com.pojo.itempool;

import java.util.ArrayList;
import java.util.List;

import com.pojo.exercise.ExerciseItemType;




/**
 * 老师组卷DTO
 * @author fourer
 *
 */
public class TestPaperDetailDTO extends TestPaperDTO{

	
	public TestPaperDetailDTO(TestPaperEntry e) {
		super(e);
		
		for (ExerciseItemType type : ExerciseItemType.values()) {

			if (ExerciseItemType.MULTICHOICE.equals(type)) {
				continue;
			}
			this.count += e.getQuestions(type).size();
		}
	}

    private int count;
	private String schoolName="";
	private String userName = "";
	private List<ItemDTO> chList =new ArrayList<ItemDTO>();
	private List<ItemDTO> tfList =new ArrayList<ItemDTO>();
	private List<ItemDTO> gapList =new ArrayList<ItemDTO>();
	private List<ItemDTO> subList =new ArrayList<ItemDTO>();
	
	
	
	public List<ItemDTO>  getList(ExerciseItemType type)
	{
		if(null==type)
			throw new  NullPointerException();
		if(ExerciseItemType.SINGLECHOICE.equals(type) || ExerciseItemType.MULTICHOICE.equals(type) )
		{
			return chList;
		}
		
		if(ExerciseItemType.TRUE_OR_FALSE.equals(type)  )
		{
			return tfList;
		}
		
		if(ExerciseItemType.GAP.equals(type)  )
		{
			return gapList;
		}
		
		return subList;
	}
	


	public int getTotal() {
		return this.count;
	}
	public void setTotal(int total) {
		this.count=total;
	}
	public int getChCount() {
		return chList.size();
	}
	public void setChCount(int chCount) {
		//this.chCount = chCount;
	}
	public int getTfCount() {
		return tfList.size();
	}
	public void setTfCount(int tfCount) {
		//this.tfCount = tfCount;
	}
	public int getGapCount() {
		return gapList.size();
	}
	public void setGapCount(int gapCount) {
		//this.gapCount = gapCount;
	}
	public int getSubCount() {
		return subList.size();
	}
	public void setSubCount(int subCount) {
		//this.subCount = subCount;
	}
	public List<ItemDTO> getChList() {
		return chList;
	}
	public void setChList(List<ItemDTO> chList) {
		this.chList = chList;
	}
	public List<ItemDTO> getTfList() {
		return tfList;
	}
	public void setTfList(List<ItemDTO> tfList) {
		this.tfList = tfList;
	}
	public List<ItemDTO> getGapList() {
		return gapList;
	}
	public void setGapList(List<ItemDTO> gapList) {
		this.gapList = gapList;
	}
	public List<ItemDTO> getSubList() {
		return subList;
	}
	public void setSubList(List<ItemDTO> subList) {
		this.subList = subList;
	}



	public String getSchoolName() {
		return schoolName;
	}



	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "TestPaperDetailDTO [count=" + count + ", schoolName="
				+ schoolName + ", chList=" + chList + ", tfList=" + tfList
				+ ", gapList=" + gapList + ", subList=" + subList + "]";
	}
	
	
	
}
