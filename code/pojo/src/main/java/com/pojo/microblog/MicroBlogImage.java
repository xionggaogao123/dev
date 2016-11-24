package com.pojo.microblog;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 微博图片，依附于MicroBlogEntry
 * <pre>
 * {
 *  id:标识ID
 *  path:路径
 * }
 * </pre>
 * @author fourer
 */
public class MicroBlogImage extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8918335070242826263L;

	
	public MicroBlogImage(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}

	public MicroBlogImage(String path) {
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
