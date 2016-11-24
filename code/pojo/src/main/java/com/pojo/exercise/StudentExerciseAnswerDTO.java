package com.pojo.exercise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pojo.app.SimpleDTO;
import com.sys.constants.Constant;


/**
 * 学生试卷情况
 * @author fourer
 *
 */
public class StudentExerciseAnswerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1932173139844706272L;
	
	private String answerId;
	private String titleId;
	private String answer=Constant.EMPTY;
	private Double score;
	private int ty;
	private String type;
	private Double totalScore;
	
	
	private List<SimpleDTO> iamge =new ArrayList<SimpleDTO>();
	
	
	
	public StudentExerciseAnswerDTO(ExerciseMixItem e)
	{
		this.titleId=e.getTitleId();
		try
		{
			this.ty=e.getType();
	        this.type=ExerciseItemType.getExerciseItemType(e.getType()).getName();
	        if(this.ty==ExerciseItemType.SUBJECTIVE.getType())
	        {
	        	this.totalScore=e.getScore();
	        }
		}catch(Exception ex)
		{
			
		}
	}
	
	
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public String getAnswer() {
		
		if(this.ty==ExerciseItemType.TRUE_OR_FALSE.getType())
		{
			if(StringUtils.isBlank(this.answer))
			{
				return "未作答";
			}
			else
			{
				if(this.answer.equals("0"))
					return "错";
				else
					return "对";
			}
		}
		return StringUtils.isBlank(this.answer)?"":this.answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public List<SimpleDTO> getIamge() {
		return iamge;
	}
	public void setIamge(List<SimpleDTO> iamge) {
		this.iamge = iamge;
	}
	


	public String getAnswerId() {
		return answerId;
	}


	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getTy() {
		return ty;
	}


	public void setTy(int ty) {
		this.ty = ty;
	}
	
	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	
	public int getIsShow() {
		return (this.ty!=ExerciseItemType.SUBJECTIVE.getType() || StringUtils.isNotBlank(this.answerId))?Constant.ONE:Constant.ZERO;
	}

	@Override
	public String toString() {
		return "StudentExerciseAnswerDTO [answerId=" + answerId + ", titleId="
				+ titleId + ", answer=" + answer + ", score=" + score
				+ ", type=" + type + ", iamge=" + iamge + "]";
	}
	
	

}
