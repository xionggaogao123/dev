package com.db.user;

import com.pojo.user.UserEntry;
import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserExperienceLogDaoTest {

	private UserExperienceLogDao dao =new UserExperienceLogDao();
	
	@Test
	public void addUserExperienceLog()
	{
		List<ExperienceLog> list =new ArrayList<ExperienceLog>();
        ExperienceLog experienceLog=new ExperienceLog("观看云课程",2,new Date().getTime(),1,new ObjectId());//(String des, int experience, long time,ObjectId relariveId)
        list.add(experienceLog);
		UserExperienceLogEntry e =new UserExperienceLogEntry(new ObjectId("551d02739aedfa2625fd4cf4"), list);
		dao.addUserExperienceLog(e);
	}

	@Test
	public void addExperienceLogEntry()
	{
		//for(int i=0;i<50;i++)
		{
            ExperienceLog e =new ExperienceLog("观看课程视频",2,new Date().getTime(),2, new ObjectId("556bd1d639f1feece403cdf9"));
		    dao.addExperienceLogEntry(new ObjectId("551d02739aedfa2625fd4cf4"), e);;
		}
	}

    @Test
    public void countUserExp() throws IllegalParamException
    {
        int count=dao.countUserExp(new ObjectId("551d02739aedfa2625fd4cf4"), "556bd1d639f1feece403cdf9",2 );
        System.out.println(count);

        UserDao userDao=new UserDao();
        UserEntry userEntry = userDao.getUserEntry(new ObjectId("551d02739aedfa2625fd4cf4"), Constant.FIELDS);
        int experience = userEntry.getExperiencevalue();
        experience=experience+2;
        userDao.update(new ObjectId("551d02739aedfa2625fd4cf4"),"exp", experience,true);
    }
    @Test
    public void userExpCount(){
        int count=dao.userExpCount(new ObjectId("551d02739aedfa2625fd4cf4"));
        System.out.println(count);
    }
	
	@Test
	public void getUserExperienceLogEntry()
	{
		UserExperienceLogEntry e=dao.getUserExperienceLogEntry(new ObjectId("551e32f49aed693504b265ba"), 0, 3);
		System.out.println(e.getBaseEntry());
	}

    @Test
    public void testOne(){
        long currTime=1l;
        //检查是否当天第一次
        currTime= DateTimeUtils.getDayMinTime(new Date().getTime());
        List<ExperienceLog> expList=dao.getExperienceLogList(new ObjectId("551d02739aedfa2625fd4cf4"), currTime);
        int dayTotalExp=0;
        for(ExperienceLog expLog:expList){
            dayTotalExp+= expLog.getExperience();
        }
        System.out.println(dayTotalExp);
    }
}
