package com.pojo.app;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * app 版本信息
 * <pre>
 * collectionName:versions
 * </pre>
 * <pre>
 * {
 *  c:1为安卓    2平板
 *  v:
 *  des:
 * }
 * </pre>
 * @author fourer
 */
public class AppVersionEntry extends BaseDBObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6697293251091364249L;
	
	public AppVersionEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}


	public AppVersionEntry(int client,String version) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("c", client)
        .append("v", version)
        ;
        setBaseEntry(dbo);
	}
	
	
	public String getVersion() {
		return getSimpleStringValue("v");
	}
	public void setVersion(String version) {
		setSimpleValue("v", version);
	}
	public int getClient() {
		return getSimpleIntegerValue("c");
	}
	public void setClient(int client) {
		setSimpleValue("c", client);
	}
	
	

	public String getDes() {
		return getSimpleStringValue("des");
	}


	public void setDes(String des) {
		setSimpleValue("des", des);
	}
	
	
	
}
