package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.IdValuePair;
import com.pojo.school.LeagueEnrty;

public class LeagueDaoTest {

	private LeagueDao dao =new LeagueDao();
	
	@Test
	public void add()
	{
		List<IdValuePair> schools =new ArrayList<IdValuePair>();
		schools.add(new IdValuePair(new ObjectId(),"清华"));
		LeagueEnrty e =new LeagueEnrty("k6kt", new ObjectId(), schools);
		dao.add(e);
	}
	
	@Test
	public void operSchool()
	{
		dao.operSchool(new ObjectId("551cb18963e7a469d22ddaba"), new IdValuePair(new ObjectId("551cb18963e7a469d22ddab8"),"清华"), 0);
	}
}
