package com.fulaan.version.service;

import com.db.version.AppVersionDao;
import com.pojo.version.AppVersionEntry;
import org.bson.types.ObjectId;

/**
 * AppVersion service
 * @author fourer
 *
 */
public class AppVersionService {

	private AppVersionDao dao =new AppVersionDao();
	
	public ObjectId addAppVersion(AppVersionEntry e)
	{
		return dao.addAppVersion(e);
	}
	
	public AppVersionEntry getRecentlyVersion(int client)
	{
		return dao.getRecentlyVersion(client);
	}
	
}
