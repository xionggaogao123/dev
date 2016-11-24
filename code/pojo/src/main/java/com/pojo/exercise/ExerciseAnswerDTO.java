package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.sys.constants.Constant;

/**
 * 学生答题DTO
 * @author fourer
 *
 */
public class ExerciseAnswerDTO {

	private String id;
	private String titleId;
	private int titleType;
	private double totalScore;
	private double score;
	private String userAnswer;
	private String rightAnswer;
	private List<IdValuePairDTO> imageList =new ArrayList<IdValuePairDTO>();
	
	
	public ExerciseAnswerDTO(ExerciseMixItem te, ExerciseAnswerEntry se)
	{
		this.id=te.getItemId().toString();
		this.titleId=te.getTitleId();
		this.titleType=te.getType();
		this.totalScore=te.getScore();
		this.rightAnswer=te.getAnswers();
		
		if(null!=se)
		{
			if(se.getUserScore()>=Constant.DEFAULT_VALUE_DOUBLE)
			{
				this.score=se.getUserScore();
			}
			
			this.userAnswer=se.getUserAnswer();
			
			
			for(IdValuePair p:se.getImages())
			{
				imageList.add(new IdValuePairDTO(p));
			}
		}
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
	public int getTitleType() {
		return titleType;
	}
	public void setTitleType(int titleType) {
		this.titleType = titleType;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getUserAnswer() {
		if(StringUtils.isNotBlank(userAnswer))
		{
			return this.userAnswer;
		}
		return Constant.EMPTY;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public String getRightAnswer() {
		if(StringUtils.isNotBlank(rightAnswer))
		{
			return this.rightAnswer;
		}
		return Constant.EMPTY;
	}
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public List<IdValuePairDTO> getImageList() {
		return imageList;
	}
	public void setImageList(List<IdValuePairDTO> imageList) {
		this.imageList = imageList;
	}
	
	
	
	
	
}
