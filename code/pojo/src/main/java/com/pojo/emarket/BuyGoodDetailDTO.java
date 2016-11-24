package com.pojo.emarket;

import java.util.List;

import com.pojo.lesson.LessonEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;

/**
 * 购买商品明细
 * @author admin
 *
 */
public class BuyGoodDetailDTO {

	private String id;
	private String courseName;
	private String courseContent;
	private String imageUrl;
	private String teacherId;
	private int videoCount;
	private int docCount;
	private int exerciseCount;
	private long expireTime;
	private String teacherName;
	private String teacherNname;
	private String teacherIntroduce;
	private String teacherInt;
	private double price;
	private String profileBrief;
	private String maxImageUrl;
	private int userId;
	private String schoolName;
	private int isopen;
	private int count;
	private int isWordExercise;
	private List<GoodsDTO> hotList;
	
	public BuyGoodDetailDTO(){}
	
	public BuyGoodDetailDTO(GoodsEntry ge, UserEntry ue, LessonEntry le, SchoolEntry se) {
		this.id = ge.getID().toString();
		this.courseName = ge.getName();
		this.courseContent = ge.getDesc();
		this.imageUrl = le.getImgUrl();
		this.teacherId = ue.getID().toString();
		this.videoCount = le.getVideoCount();
		this.docCount = le.getDocumentCount();
		this.exerciseCount = le.getExerciseCount();
		this.expireTime = ge.getExpireTime();
		this.teacherName = ue.getUserName();
		this.teacherNname = ue.getNickName();
		this.teacherInt = ue.getIntroduce();
		this.price = ge.getPrice();
		this.schoolName = se.getName();
		this.count = ge.getSellCount();
		this.teacherIntroduce = ue.getIntroduce();
		this.isopen = ge.getIsopen();
		this.maxImageUrl = AvatarUtils.getAvatar(ue.getAvatar(), 3);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseContent() {
		return courseContent;
	}
	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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
		return exerciseCount;
	}
	public void setQuestionCount(int exerciseCount) {
		this.exerciseCount = exerciseCount;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherNname() {
		return teacherNname;
	}
	public void setTeacherNname(String teacherNname) {
		this.teacherNname = teacherNname;
	}
	public String getTeacherInt() {
		return teacherInt;
	}
	public void setTeacherInt(String teacherInt) {
		this.teacherInt = teacherInt;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getProfileBrief() {
		return profileBrief;
	}
	public void setProfileBrief(String profileBrief) {
		this.profileBrief = profileBrief;
	}
	public String getMaxImageUrl() {
		return maxImageUrl;
	}
	public void setMaxImageUrl(String maxImageUrl) {
		this.maxImageUrl = maxImageUrl;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public int getIsopen() {
		return isopen;
	}
	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getIsWordExercise() {
		return isWordExercise;
	}
	public void setIsWordExercise(int isWordExercise) {
		this.isWordExercise = isWordExercise;
	}
	public List<GoodsDTO> getHotList() {
		return hotList;
	}
	public void setHotList(List<GoodsDTO> hotList) {
		this.hotList = hotList;
	}
	public String getTeacherIntroduce() {
		return teacherIntroduce;
	}
	public void setTeacherIntroduce(String teacherIntroduce) {
		this.teacherIntroduce = teacherIntroduce;
	}
	
}
