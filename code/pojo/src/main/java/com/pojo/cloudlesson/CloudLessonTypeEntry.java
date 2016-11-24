package com.pojo.cloudlesson;


import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 云课程类别
 * <pre>
 * collectionName:cloudclasstype
 * </pre>
 * <pre>
 * {
 *  sty:学校类型；对应SchoolType
 *  sub:科目ID；对应SubjectType
 *  ccgt:云课程年级；对应GradeType
 *  nm:名字
 *  des:描述
 *  or:排序字段
 * }
 * </pre>
 * @author fourer
 */
public class CloudLessonTypeEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1307288140789644695L;

	public CloudLessonTypeEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public CloudLessonTypeEntry(int schoolType, int subjectId,
			int cloudClassGradeType, String name, String description, int order) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("sty", schoolType)
		.append("sub", subjectId)
		.append("ccgt", cloudClassGradeType)
		.append("nm", name)
		.append("des", description)
		.append("or", order);
		setBaseEntry(baseEntry);
	}
	public int getSchoolType() {
		return getSimpleIntegerValue("sty");
	}
	public void setSchoolType(int schoolType) {
		setSimpleValue("sty", schoolType);
	}
	public int getSubject() {
		return getSimpleIntegerValue("sub");
	}
	public void setSubject(int subjectId) {
		setSimpleValue("sub", subjectId);
	}
	public int getCloudClassGradeType() {
		return getSimpleIntegerValue("ccgt");
	}
	public void setCloudClassGradeType(int cloudClassGradeType) {
		setSimpleValue("ccgt",cloudClassGradeType);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getDescription() {
		return getSimpleStringValue("des");
	}
	public void setDescription(String description) {
		setSimpleValue("des", description);
	}
	public int getOrder() {
		return getSimpleIntegerValue("or");
	}
	public void setOrder(int order) {
		setSimpleValue("or", order);
	}
	
	
}
