package com.pojo.schoolsecurity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 微博图片，依附于SchoolSecurityEntry
 * <pre>
 * {
 *  id:标识ID
 *  path:路径
 * }
 * </pre>
 * @author jing.guo
 */
public class SchoolSecurityImage extends BaseDBObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 8918335074242826263L;


	public SchoolSecurityImage(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public SchoolSecurityImage(String path) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", new ObjectId()).append("path", path);
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getId() {
		return getSimpleObjecIDValue("id");
	}
	public void setId(ObjectId id) {
		setSimpleValue("id", id);
	}
	public String getPath() {
		return getSimpleStringValue("path");
	}
	public void setPath(String path) {
		setSimpleValue("path", path);
	}
	
}
