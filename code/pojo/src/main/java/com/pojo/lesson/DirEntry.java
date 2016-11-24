package com.pojo.lesson;

import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 目录
 * <pre>
 * collectionName:dirs
 * </pre>
 * <pre>
 * {
 *  ow:拥有者;老师ID,班级课程ID,学校ID
 *  dn:名称
 *  pi:父目录ID
 *  so:排序代码，仅仅用于同级地域排序
 *  ty:种类 ;参见DirType
 * }
 * </pre>
 * @author fourer
 */
public class DirEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4897892438074548729L;

	public DirEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	/**
	 * 
	 * @param owerId
	 * @param dirName
	 * @param parentId
	 * @param sort
	 * @param type
	 */
	public DirEntry(ObjectId owerId, String dirName, ObjectId parentId,
			int sort,DirType ty) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("ow", owerId)
		.append("dn", dirName)
		.append("pi", parentId)
		.append("so", sort)
		.append("ty", ty.getType())
		;
		setBaseEntry(dbo);
	}
	
	public ObjectId getOwerId() {
		return getSimpleObjecIDValue("ow");
	}
	public void setOwerId(ObjectId owerId) {
		setSimpleValue("ow", owerId);
	}
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public String getDirName() {
		return getSimpleStringValue("dn");
	}
	public void setDirName(String dirName) {
		setSimpleValue("dn", dirName);
	}
	public ObjectId getParentId() {
		return getSimpleObjecIDValue("pi");
	}
	public void setParentId(ObjectId parentId) {
		setSimpleValue("pi", parentId);
	}

	public int getSort() {
		return getSimpleIntegerValue("so");
	}
	public void setSort(int sort) {
		setSimpleValue("so", sort);
	}
}
