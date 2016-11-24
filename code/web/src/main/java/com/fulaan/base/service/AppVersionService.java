package com.fulaan.base.service;

import org.bson.types.ObjectId;

import com.db.app.AppVersionDao;
import com.pojo.app.AppVersionEntry;

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
