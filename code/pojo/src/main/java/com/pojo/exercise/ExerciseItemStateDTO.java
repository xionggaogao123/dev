package com.pojo.exercise;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * 测试题目以及答题统计情况情况
 */
public class ExerciseItemStateDTO extends ExerciseMixItemInfoDTO {

    private static final  NumberFormat nt = NumberFormat.getPercentInstance();
	
	static
	{
		 nt.setMinimumFractionDigits(0);
	}
	
	
	//todo
	public ExerciseItemStateDTO() {
	}
	
	
	public ExerciseItemStateDTO(ExerciseMixItem e) {
		super(e);
		if(this.getType()==ExerciseItemType.SINGLECHOICE.getType())
		{
			
			this.answerMap.put("A", 0);
//			this.answerMap.put("B", 0);
//			this.answerMap.put("C", 0);
//			this.answerMap.put("D", 0);
			
			
			if(this.getSelectCount()==Constant.TWO)
			{
				this.answerMap.put("B", 0);
			}
			if(this.getSelectCount()==Constant.THREE)
			{
				this.answerMap.put("B", 0);
				this.answerMap.put("C", 0);
			}
			if(this.getSelectCount()==Constant.FOUR)
			{
				this.answerMap.put("B", 0);
				this.answerMap.put("C", 0);
				this.answerMap.put("D", 0);
			}
			if(this.getSelectCount()==Constant.FIVE)
			{
				this.answerMap.put("B", 0);
				this.answerMap.put("C", 0);
				this.answerMap.put("D", 0);
				this.answerMap.put("E", 0);
			}
			if(this.getSelectCount()==Constant.SIX)
			{
				this.answerMap.put("B", 0);
				this.answerMap.put("C", 0);
				this.answerMap.put("D", 0);
				this.answerMap.put("E", 0);
				this.answerMap.put("F", 0);
			}
			
			if(this.getSelectCount()==Constant.SEVEN)
			{
				this.answerMap.put("B", 0);
				this.answerMap.put("C", 0);
				this.answerMap.put("D", 0);
				this.answerMap.put("E", 0);
				this.answerMap.put("F", 0);
				this.answerMap.put("G", 0);
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			/**
			
			if(this.getSelectCount()==Constant.FIVE)
				this.answerMap.put("E", 0);
			if(this.getSelectCount()==Constant.SIX)
				this.answerMap.put("F", 0);
			if(this.getSelectCount()==Constant.SEVEN)
				this.answerMap.put("G", 0);
				**/
		}
		if(this.getType()==ExerciseItemType.TRUE_OR_FALSE.getType())
		{
			this.answerMap.put("1", 0);
			this.answerMap.put("0", 0);
		}
		if(this.getType()==ExerciseItemType.GAP.getType())
		{
			this.answerMap.put(String.valueOf(e.getScore()), 0);
			this.answerMap.put("0.0", 0);
		}
		
	}
	
	//答题人数
	private int answerCount;
	//对的人数
	private int rightCount;
	//已经获得分数的人数
	private int scoreCount;
	//答案情况
	private Map<String, Integer>  answerMap =new HashMap<String, Integer>();
	//用户答案
	private Map<String, UserItemAnswerDTO>  userMap =new HashMap<String, UserItemAnswerDTO>();
	//未提交用户
	private List<UserItemAnswerDTO> unCommitted = new ArrayList<UserItemAnswerDTO>();


	public int getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}
	public int getRightCount() {
		return rightCount;
	}
	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}
	public int getScoreCount() {
		return scoreCount;
	}
	public void setScoreCount(int scoreCount) {
		this.scoreCount = scoreCount;
	}


	public int getWrongCount() {
		return answerCount-rightCount;
	}
	public void setWrongCount(int rightCount) {
		//this.rightCount = rightCount;
	}
	
	
	
	public Map<String, Integer> getAnswerMap() {
		return answerMap;
	}
	public void setAnswerMap(Map<String, Integer> answerMap) {
		this.answerMap = answerMap;
	}
	
	
	public List<AnswerCount> getAnswerCounts()
	{
		List<ExerciseItemStateDTO.AnswerCount> retList =new ArrayList<ExerciseItemStateDTO.AnswerCount>();
		for(Map.Entry<String, Integer> entry:answerMap.entrySet())
		{
			ExerciseItemStateDTO.AnswerCount ac =new AnswerCount();
			ac.setAnswer(entry.getKey());
			ac.setCount(entry.getValue());
			retList.add(ac);
		}
	
		Collections.sort(retList, new Comparator<ExerciseItemStateDTO.AnswerCount>() {
			@Override
			public int compare(ExerciseItemStateDTO.AnswerCount arg0, ExerciseItemStateDTO.AnswerCount arg1) {
				return arg0.getAnswer().compareTo(arg1.getAnswer());
			}
		});
		
		return retList;
	}
	
	
	public void increaseRight()
	{
		this.rightCount=this.rightCount+1;
	}
	
	public void increaseAnswerCount()
	{
		this.answerCount=this.answerCount+1;
	}
	
	public void increaseScoreCount()
	{
		this.scoreCount=this.scoreCount+1;
	}
	
	
	public void increaseAnswer(String answer)
	{
		if(answerMap.containsKey(answer))
		{
			answerMap.put(answer, answerMap.get(answer)+1);
		}
		else
		{
			answerMap.put(answer, 1);
		}
	}
	
	
	public void addUserAnswer(String user,UserItemAnswerDTO answer)
	{
		userMap.put(user, answer);
	}
	
	public List<UserItemAnswerDTO> getUserAnswerList() {
		return new ArrayList<ExerciseItemStateDTO.UserItemAnswerDTO>(userMap.values());
	}
	public void setUserAnswerList(List<UserItemAnswerDTO> userAnswerList) {
		//this.userAnswerList = userAnswerList;
	}

	public List<UserItemAnswerDTO> getRightUserAnswerList(){
		List<UserItemAnswerDTO> userItemAnswerDTOs = getUserAnswerList();
		List<UserItemAnswerDTO> rightUserItemAnswerDTOs = new ArrayList<UserItemAnswerDTO>();
		for(UserItemAnswerDTO userItemAnswerDTO : userItemAnswerDTOs){
			if(userItemAnswerDTO.getRight() == 1){
				rightUserItemAnswerDTOs.add(userItemAnswerDTO);
			}
		}
		return rightUserItemAnswerDTOs;
	}


	public List<UserItemAnswerDTO> getWrongUserAnswerList(){
		List<UserItemAnswerDTO> userItemAnswerDTOs = getUserAnswerList();
		List<UserItemAnswerDTO> rightUserItemAnswerDTOs = new ArrayList<UserItemAnswerDTO>();
		for(UserItemAnswerDTO userItemAnswerDTO : userItemAnswerDTOs){
			if(userItemAnswerDTO.getRight() == 0){
				rightUserItemAnswerDTOs.add(userItemAnswerDTO);
			}
		}
		return rightUserItemAnswerDTOs;
	}

	public List<UserItemAnswerDTO> getUnCommitted() {
		return unCommitted;
	}

	public void setUnCommitted(List<UserItemAnswerDTO> unCommitted) {
		this.unCommitted = unCommitted;
	}

	/**
	 * 得分率
	 * @return
	 */
	public String getRate()
	{
		if(this.getType()==Constant.FIVE) 
		{
			return Constant.EMPTY;
		}
		if(this.rightCount==Constant.ZERO)
		{
		   return nt.format(Constant.DEFAULT_VALUE_DOUBLE);
		}
	    return nt.format((double)this.rightCount/this.answerCount);
	}
	public double getCssWidth(String key)
	{
		if(!answerMap.containsKey(key))
		{
			return 0;
		}

		int keyValue =answerMap.get(key);
		if(this.answerCount == 0)
			return 0;
		return 100.0*keyValue/this.answerCount;
	}

	public double getCssWidthForPanDuan(String key) {
		int keyValue;
		if(key.equals("1")){
			keyValue = getRightCount();
		} else {
			keyValue = getWrongCount();
		}
		if(this.answerCount == 0)
			return 0;
		return 100.0*keyValue/this.answerCount;
	}

	/**
	 * 
	 */
	
	public int getRight() {
		if(this.getType()==ExerciseItemType.SINGLECHOICE.getType())
		{
			if(answerMap.containsKey("1"))
			{
			  return answerMap.get("1");
			}
		}
		return Constant.ZERO;
	}
	
	public int getWrong() {
		if(this.getType()==ExerciseItemType.SINGLECHOICE.getType())
		{
			if(answerMap.containsKey("0"))
			{
			  return answerMap.get("0");
			}
		}
		return Constant.ZERO;
	}

	@Override
	public String toString() {
		return "ExerciseItemStateDTO [answerCount=" + answerCount
				+ ", rightCount=" + rightCount + ", answerMap=" + answerMap
				+ ", userMap=" + userMap + ","+super.toString()+"]";
	}

	/**
	 * 用户答题
	 * @author fourer
	 *
	 */
	public static class UserItemAnswerDTO
	{
		private String id;
		private String uid;
		private String name;
		private String answer;
		private Double score;
		private int right; //1 正确 0错误
		private List<IdValuePairDTO> imageList=new ArrayList<IdValuePairDTO>(Constant.FIVE);
		private String time;
		private String avatar;
		//todo
		public UserItemAnswerDTO()
		{
			
		}
		
		public UserItemAnswerDTO(ExerciseAnswerEntry e)
		{
			this.id=e.getID().toString();
			this.uid=e.getUserId().toString();
			this.answer=e.getUserAnswer();
			this.right=e.getIsRight();
			if(e.getUserScore()>0)
			{
			  this.score=e.getUserScore();
			}
			List<IdValuePair> list =e.getImages();
			for(IdValuePair ip:list)
			{
				imageList.add(new IdValuePairDTO(ip));
			}
			this.time = DateTimeUtils.convert(new ObjectId(id).getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
		}



		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getUid() {
			return uid;
		}


		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getTime(){return time;}
		public void setTime(String time){this.time = time;}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		public int getRight() {
			return right;
		}
		public void setRight(int right) {
			this.right = right;
		}

		public List<IdValuePairDTO> getImageList() {
			return imageList;
		}

		public void setImageList(List<IdValuePairDTO> imageList) {
			this.imageList = imageList;
		}

		public Double getScore() {
			return score;
		}

		public void setScore(Double score) {
			this.score = score;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "UserItemAnswerDTO [id=" + id + ", uid=" + uid + ", name="
					+ name + ", answer=" + answer + ", score=" + score
					+ ", right=" + right + ", imageList=" + imageList + "]";
		}
		
	}
	
	
	public static class AnswerCount
	{
		private String answer;
		private int count;
		
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
		
	}
}
