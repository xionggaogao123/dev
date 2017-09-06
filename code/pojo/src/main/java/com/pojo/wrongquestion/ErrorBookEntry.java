package com.pojo.wrongquestion;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * uid:用户id
 * gd:错题所属年级
 * gid:年级id
 * ty:题目类型
 * levl:题目难度  	1：易 2：较易 3：中 4 较难 5 难
 * sc:题目来源   	1:自己添加 	2：从题库中添加
 * sbj:题目所属科目
 * kps:知识点列表
 * desc:题目描述
 * an:题目答案
 * exs:{@link}ErrorBookEntry.AnswerExplain 题目解析list
 * atta:{@link}ErrorBookEntry.ErrorBookAttach 附件list
 * isgp:是否掌握 0：未掌握 1：掌握
 * ir:是否删除 0:未删除  1：删除
 * isrd：是否悬赏 0：未悬赏  1：已悬赏
 * 
 * added by xusy 2017-03-13
 */
public class ErrorBookEntry extends BaseDBObject {

	private static final long serialVersionUID = 2768021542212194996L;

	public ErrorBookEntry() {}
	
	public ErrorBookEntry(BasicDBObject entry) {
		setBaseEntry(entry);
	}
	
	public ErrorBookEntry(
			ObjectId userId,
			int gradeType,
			ObjectId gradeId,
			int questionType,
			int questionLevel,
			ObjectId subject,
			List<ObjectId> pointList,
			String questionDesc,
			String answer,
			List<ErrorBookAttach> attachmentList) {
		
		BasicDBObject entry = new BasicDBObject()
				.append("uid", userId)
				.append("gd", gradeType)
				.append("gid", gradeId)
				.append("ty", questionType)
				.append("levl", questionLevel)
				.append("sc", 1)
				.append("sbj", subject)
				.append("kps", pointList)
				.append("desc", questionDesc)
				.append("an", answer)
				.append("atta", MongoUtils.convert(MongoUtils.fetchDBObjectList(attachmentList)))
				.append("isgp", Constant.ZERO)
				.append("ir", Constant.ZERO)
				.append("isrd", Constant.ZERO);
		setBaseEntry(entry);
		
	}
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	
	public void setUserId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}
	
	public int getGradeType() {
		return getSimpleIntegerValue("gd");
	}
	
	public void setGradeType(int gradeType) {
		setSimpleValue("gd", gradeType);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	
	public int getQuestionType() {
		return getSimpleIntegerValue("ty");
	}
	
	public void setQuestionType(int type) {
		setSimpleValue("ty", type);
	}
	
	public int getQuestionLevel() {
		return getSimpleIntegerValue("levl");
	}
	
	public void setQustionLevel(int level) {
		setSimpleValue("levl", level);
	}
	
	public int getQuestionSource() {
		return getSimpleIntegerValue("sc");
	}
	
	public void setQuestionSource(int source) {
		setSimpleValue("sc", source);
	}
	
	public ObjectId getSubjectId() {
		return getSimpleObjecIDValue("sbj");
	}
	
	public void setSubjectId(ObjectId subjectId) {
		setSimpleValue("sbj", subjectId);
	}
	
	public List<ObjectId> getKnowledgePointList() {
		
		List<ObjectId> objectIdList = new ArrayList<ObjectId>();
		
		BasicDBList dbList = (BasicDBList) getSimpleObjectValue("kps");
		if(dbList != null 
				&& !dbList.isEmpty()) {
			for(Object obj : dbList) {
				objectIdList.add((ObjectId) obj);
			}
		}
		
		return objectIdList;
	}
	
	public void setKnowledgePointList(List<ObjectId> pointList) {
		setSimpleValue("kps", pointList);
	}
	
	public String getQuestionDesc() {
		return getSimpleStringValue("desc");
	}
	
	public void setQuestionDesc(String desc) {
		setSimpleValue("desc", desc);
	}
	
	public String getQuestionAnswer() {
		return getSimpleStringValue("an");
	}
	
	public void setQuestionAnswer(String answer) {
		setSimpleValue("an", answer);
	}
	
	public List<AnswerExplain> getAnswerExplainList() {
		
		List<AnswerExplain> idValueList = new ArrayList<AnswerExplain>();
		
		BasicDBList dbList = (BasicDBList) getSimpleObjectValue("exs");
		if(dbList != null 
				&& !dbList.isEmpty()) {
			for(Object obj : dbList) {
				idValueList.add(new AnswerExplain((BasicDBObject) obj));
			}
		}
		
		return idValueList;
	}
	
	public void setAnswerExplainList(List<AnswerExplain> explainList) {
		setSimpleValue("exs", MongoUtils.convert(MongoUtils.fetchDBObjectList(explainList)));
	}
	
	/**
	 * {
	 *   id:ObjectId解析id
	 *   epl:解析的具体内容
	 *   atids:该条解析包含的附件id列表  {@link}ErrorBookAttach 中id
	 * }
	 */
	public static class AnswerExplain extends BaseDBObject {
		
		private static final long serialVersionUID = -2445605999431171154L;

		public AnswerExplain() {}
		
		public AnswerExplain(BasicDBObject entry) {
			setBaseEntry(entry);
		}
		
		public AnswerExplain(
				ObjectId id, 
				String explain, 
				List<ObjectId> attachIdList) {
			BasicDBObject entry = new BasicDBObject()
					.append("id", id)
					.append("epl", explain)
					.append("atids", attachIdList);
			setBaseEntry(entry);
		}
		
		public ObjectId getExplainId() {
			return getSimpleObjecIDValue("id");
		}
		
		public void setExplainId(ObjectId id) {
			setSimpleValue("id", id);
		}
		
		public String getExplain() {
			return getSimpleStringValue("epl");
		}
		
		public void setExplain(String explain) {
			setSimpleValue("epl", explain);
		}
		
		public List<ObjectId> getAttachIdList() {
			
			List<ObjectId> idList = new ArrayList<ObjectId>();
			
			BasicDBList dbList = (BasicDBList) getSimpleObjectValue("atids");
			if(dbList != null 
					&& !dbList.isEmpty()) {
				for(Object obj : dbList) {
					idList.add((ObjectId) obj);
				}
			}
			
			return idList;
		}
		
		public void setAttachIdList(List<ObjectId> idList) {
			setSimpleValue("atids", idList);
		}
		
	}
	
	public List<ErrorBookAttach> getAttachmentList() {
		
		List<ErrorBookAttach> idvPairList = new ArrayList<ErrorBookAttach>();
		
		BasicDBList dbList = (BasicDBList) getSimpleObjectValue("atta");
		if(dbList != null 
				&& !dbList.isEmpty()) {
			for(Object obj : dbList) {
				idvPairList.add(new ErrorBookAttach((BasicDBObject) obj));
			}
		}
		
		return idvPairList;
	}
	
	public void setAttachmentList(List<ErrorBookAttach> pairList) {
		setSimpleValue("atta", MongoUtils.convert(MongoUtils.fetchDBObjectList(pairList)));
	}

	/**
	 * 错题本附件
	 * {
	 *   id：附件id
	 *   nm：附件名称
	 *   ty：附件类型
	 *   fu：附件url
	 *   iu：图片url
	 *   ief: 是否为解析附件
	 * }
	 */
	public static class ErrorBookAttach extends BaseDBObject {

		private static final long serialVersionUID = 9015867655927327138L;
		
		public ErrorBookAttach() {}
		
		public ErrorBookAttach(BasicDBObject entry) {
			setBaseEntry(entry);
		}
		
		public ErrorBookAttach(
				ObjectId id, 
				String fileName, 
				int type, 
				String fileUrl, 
				String imageUrl) {
			
			BasicDBObject entry = new BasicDBObject()
					.append("id", id)
					.append("nm", fileName)
					.append("ty", type)
					.append("fu", fileUrl)
					.append("iu", imageUrl)
					.append("ief", false);
			setBaseEntry(entry);
		}
		
		public ObjectId getErrorBookAttachId() {
			return getSimpleObjecIDValue("id");
		}
		
		public void setErrorBookAttachId(ObjectId id) {
			setSimpleValue("id", id);
		}
		
		public String getAttachName() {
			return getSimpleStringValue("nm");
		}
		
		public void setAttachName(String name) {
			setSimpleValue("nm", name);
		}
		
		public int getAttachType() {
			return getSimpleIntegerValue("ty");
		}
		
		public void setAttachType(int type) {
			setSimpleValue("ty", type);
		}
		
		public String getFileUrl() {
			return getSimpleStringValue("fu");
		}
		
		public void setFileUrl(String fileUrl) {
			setSimpleValue("fu", fileUrl);
		}
		
		public String getImageUrl() {
			return getSimpleStringValue("iu");
		}
		
		public void setImageUrl(String imageUrl) {
			setSimpleValue("iu", imageUrl);
		}
		
		public boolean getIsExplainFileFlag() {
			return getSimpleBoolean("ief");
		}
		
		public void setIsExplainFileFlag(boolean flag) {
			setSimpleValue("ief", flag);
		}
		
	}
	
	public int getGraspStatus() {
		return getSimpleIntegerValueDef("isgp", 0);
	}
	
	public void setGraspStatus(int status) {
		setSimpleValue("isgp", status);
	}
	
	public int getRemoveStatus() {
		return getSimpleIntegerValue("ir");
	}
	
	public void setRemoveStatus(int status) {
		setSimpleValue("ir", status);
	}
	
	public int getRewardStatus() {
		return getSimpleIntegerValueDef("isrd", 0);
	}
 	
	public void setRewardStatus(int status) {
		setSimpleValue("isrd", status);
	}
	
}
