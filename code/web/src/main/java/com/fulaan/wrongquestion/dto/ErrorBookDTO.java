package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.ErrorBookEntry;
import com.pojo.wrongquestion.ErrorBookEntry.AnswerExplain;
import com.pojo.wrongquestion.ErrorBookEntry.ErrorBookAttach;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *  错题本DTO
 *	added by xusy 2017-03-14
 */
public class ErrorBookDTO {
	
	private String id;
	
	private String userId;
	
	// 年级类型
	private int gradeType;
	
	private String gradeId;
	
	// 错题类型
	private int questionType;
	
	// 难度
	private int level; 
	
	// 错题来源
	private int source;
	
	private String subjectId;
	
	// 所属知识点列表
	private List<String> pointList;

	private String desc;
	
	private String answer;
	
	// 悬赏状态
	private int rewardStatus;
	
	// 答案解析
	private List<AnswerExplainDTO> explainList;
	
	private List<ErrorBookAttachDTO> attachmentList;
	
	private List<ErrorBookAttachDTO> topicAttachs = new ArrayList<ErrorBookAttachDTO>();

	public ErrorBookDTO() {}

	public ErrorBookDTO(ErrorBookEntry entry) {
		this.id = entry.getID().toString();
		this.userId = entry.getUserId().toString();
		this.gradeType = entry.getGradeType();
		this.gradeId = entry.getGradeId().toString();
		this.questionType = entry.getQuestionType();
		this.level = entry.getQuestionLevel();
		this.source = entry.getQuestionSource();
		this.subjectId = entry.getSubjectId().toString();
		this.rewardStatus = entry.getRewardStatus();

		List<ObjectId> pointIdList = entry.getKnowledgePointList();
		if(pointIdList != null
				&& !pointIdList.isEmpty()) {
			pointList = new ArrayList<String>();
			for(ObjectId objId : pointIdList) {
				pointList.add(objId.toString());
			}
		}

		this.desc = entry.getQuestionDesc();
		this.answer = entry.getQuestionAnswer();

		List<AnswerExplain> explainEntryList = entry.getAnswerExplainList();
		if(explainEntryList != null
				&& !explainEntryList.isEmpty()) {
			explainList = new ArrayList<AnswerExplainDTO>();
			for(AnswerExplain expl : explainEntryList) {
				explainList.add(new AnswerExplainDTO(expl));
			}
		}

		List<ErrorBookAttach> attachEntryList = entry.getAttachmentList();
		if(attachEntryList != null
				&& !attachEntryList.isEmpty()) {
			attachmentList = new ArrayList<ErrorBookAttachDTO>();
			for(ErrorBookAttach atta : attachEntryList) {
				attachmentList.add(new ErrorBookAttachDTO(atta));
			}
		}

	}

	public static class AnswerExplainDTO {

		private String id;

		private String explain;

		private List<String> attachIdList;

		private List<ErrorBookAttachDTO> explainAttachs = new ArrayList<ErrorBookAttachDTO>();
		
		public AnswerExplainDTO() {}
		
		public AnswerExplainDTO(AnswerExplain explainEntry) {
			this.id = explainEntry.getExplainId().toString();
			this.explain = explainEntry.getExplain();
			
			List<ObjectId> idList = explainEntry.getAttachIdList();
			if(idList != null 
					&& !idList.isEmpty()) {
				attachIdList = new ArrayList<String>();
				for(ObjectId id : idList) {
					attachIdList.add(id.toString());
				}
			}
		
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getExplain() {
			return explain;
		}

		public void setExplain(String explain) {
			this.explain = explain;
		}

		public List<String> getAttachIdList() {
			return attachIdList;
		}

		public void setAttachIdList(List<String> attachIdList) {
			this.attachIdList = attachIdList;
		}

		public List<ErrorBookAttachDTO> getExplainAttachs() {
			return explainAttachs;
		}

		public void setExplainAttachs(List<ErrorBookAttachDTO> explainAttachs) {
			this.explainAttachs = explainAttachs;
		}
		
	}
	
	public static class ErrorBookAttachDTO {
		
		private String id;
		
		private String name;
		
		private int type;
		
		private String fileUrl;
		
		private String imageUrl;
		
		private boolean isExplainFile;
		
		public ErrorBookAttachDTO() {}
		
		public ErrorBookAttachDTO(ErrorBookAttach attachEntry) {
			this.id = attachEntry.getErrorBookAttachId().toString();
			this.name = attachEntry.getAttachName();
			this.type = attachEntry.getAttachType();
			this.fileUrl = attachEntry.getFileUrl();
			this.imageUrl = attachEntry.getImageUrl();
			this.isExplainFile = attachEntry.getIsExplainFileFlag();
		}

		public ErrorBookAttach bulidEntry() {
			ObjectId objId = id == null ? new ObjectId() : new ObjectId(id);
			return new ErrorBookAttach(objId, name, type, fileUrl, imageUrl);
		}
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getFileUrl() {
			return fileUrl;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public boolean isExplainFile() {
			return isExplainFile;
		}

		public void setExplainFile(boolean isExplainFile) {
			this.isExplainFile = isExplainFile;
		}
		
	}
	
	public ErrorBookEntry bulidEntry() {
		ObjectId userId = new ObjectId(this.userId);
		ObjectId subject = new ObjectId(this.subjectId);
		ObjectId gradeId = new ObjectId(this.gradeId);
		
		List<ObjectId> pointIdList = new ArrayList<ObjectId>();
		if(pointList != null) {
			for(String str : pointList) {
				pointIdList.add(new ObjectId(str));
			}
		}
		
		List<ErrorBookAttach> pairList = new ArrayList<ErrorBookAttach>();
		if(attachmentList != null) {
			for(ErrorBookAttachDTO dto : attachmentList) {
				pairList.add(dto.bulidEntry());
			}
		}
		
		return new ErrorBookEntry(
				userId, 
				this.gradeType, 
				gradeId,
				this.questionType, 
				this.level, 
				subject, 
				pointIdList,
				desc,
				answer, 
				pairList);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public int getGradeType() {
		return gradeType;
	}

	public void setGradeType(int gradeType) {
		this.gradeType = gradeType;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public List<String> getPointList() {
		return pointList;
	}

	public void setPointList(List<String> pointList) {
		this.pointList = pointList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public int getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(int rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public List<AnswerExplainDTO> getExplainList() {
		return explainList;
	}

	public void setExplainList(List<AnswerExplainDTO> explainList) {
		this.explainList = explainList;
	}

	public List<ErrorBookAttachDTO> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<ErrorBookAttachDTO> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<ErrorBookAttachDTO> getTopicAttachs() {
		return topicAttachs;
	}

	public void setTopicAttachs(List<ErrorBookAttachDTO> topicAttachs) {
		this.topicAttachs = topicAttachs;
	}
	
}
