package com.pojo.emarket;

import com.pojo.app.IdValuePair;
import com.pojo.lesson.LessonEntry;


/**
 * 商品DTO
 * @author fourer
 *
 */
public class GoodsDTO {

	private String id;
	private String image;
	private double price;
	private String name;
	private IdValuePair user;
	private IdValuePair school;
	private int videoCount;
	private int docCount;
	private int questionCount;
	private String lessonid;
	private int isopen;
	
	
	public GoodsDTO(GoodsEntry ge,LessonEntry le)
	{
		this.id=ge.getID().toString();
		this.image=le.getImgUrl();
		this.price=ge.getPrice();
		this.name=ge.getName();
		this.docCount = le.getDocumentCount();
		this.videoCount = le.getVideoCount();
		this.questionCount = le.getExerciseCount();
		this.user=new IdValuePair(ge.getOwner(),"");
		this.lessonid = le.getID().toString();
		this.isopen = ge.getIsopen();
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IdValuePair getUser() {
		return user;
	}
	public void setUser(IdValuePair user) {
		this.user = user;
	}
	public IdValuePair getSchool() {
		return school;
	}
	public void setSchool(IdValuePair school) {
		this.school = school;
	}
	public int getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}
	public int getDocCount() {
		return docCount;
	}
	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}
	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	public String getLessonid() {
		return lessonid;
	}
	public void setLessonid(String lessonid) {
		this.lessonid = lessonid;
	}
	public int getIsopen() {
		return isopen;
	}

	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}
	
	
	
	
}
