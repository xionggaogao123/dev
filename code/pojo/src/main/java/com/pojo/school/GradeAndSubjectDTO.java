package com.pojo.school;

import java.util.ArrayList;
import java.util.List;


import com.pojo.app.SimpleDTO;

public class GradeAndSubjectDTO {

	private SimpleDTO grade;
	private List<SimpleDTO> subjects=new ArrayList<SimpleDTO>();
	
	
	
	
	public GradeAndSubjectDTO(SimpleDTO grade) {
		super();
		this.grade = grade;
	}
	public SimpleDTO getGrade() {
		return grade;
	}
	public void setGrade(SimpleDTO grade) {
		this.grade = grade;
	}
	public List<SimpleDTO> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<SimpleDTO> subjects) {
		this.subjects = subjects;
	}
	
	public void addSubject(SimpleDTO dto)
	{
		this.subjects.add(dto);
	}
	
	
}
