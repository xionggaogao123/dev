package com.fulaan.learningcenter.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.school.LeagueDao;
import com.mongodb.DBObject;
import com.pojo.school.LeagueEnrty;
import org.springframework.stereotype.Service;

/**
 * 联盟
 * @author fourer
 *
 */
@Service
public class LeagueService {

	private LeagueDao dao =new LeagueDao();
	
	/**
	 * 按照学校ID查询
	 * @param schoolId
	 * @return
	 */
	public List<LeagueEnrty> getLeagueEnrtys(ObjectId schoolId,DBObject fields)
	{
		return dao.getLeagueEnrtys(schoolId, fields);
	}
}
