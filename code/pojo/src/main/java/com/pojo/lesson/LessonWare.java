package com.pojo.lesson;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 课程课件表；依附于LessonEntry
 * <pre>
 * {
 *  id:此课件ID
 *  fty:文件类型；FileType
 *  nm:名字
 *  pa:地址
 * }
 * </pre>
 * @author fourer
 */
public class LessonWare extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1758192933139800861L;
	public LessonWare(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public LessonWare(String type, String name, String path) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", new ObjectId())
		.append("fty", type)
		.append("nm", name)
		.append("pa", path);
		setBaseEntry(baseEntry);
	}
	
	
	public ObjectId getId() {
		return getSimpleObjecIDValue("id");
	}
	public void setId(ObjectId id) {
		setSimpleValue("id", id);
	}
	public String getFileType() {
		return getSimpleStringValue("fty");
	}
	public void setFileType(String fileType) {
		setSimpleValue("fty", fileType);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getPath() {
		return getSimpleStringValue("pa");
	}
	public void setPath(String path) {
		setSimpleValue("pa", path);
	}
}
