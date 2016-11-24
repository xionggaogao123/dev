package com.pojo.emarket;




public class CommentDTO {

	private String ui;
	private String comment;
	private long time;
	private String name;
	private String avatar;
	public CommentDTO(Comment c)
	{
		this.ui=c.getUi().toString();
		this.comment=c.getComment();
		this.time=c.getTime();
	}
	
	
	
	
	public String getUi() {
		return ui;
	}
	public void setUi(String ui) {
		this.ui = ui;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getAvatar() {
		return avatar;
	}




	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
	
	
}
