package com.db.temp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.letter.LetterDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.letter.LetterEntry;
import com.pojo.letter.ReceiveInfo;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

/**
 * 
 * @author fourer
 *
 */
public class LetterState {

	public static void main(String[] args) throws ResultTooManyException {
		
		UserDao dao =new UserDao();
		
		LetterDao lDao =new LetterDao();
		SchoolDao schDao =new SchoolDao();
	    String users="K6KT小助手337,k6kt小助手85,k6kt小助手264,k6kt小助手1,k6kt小助手3,k6kt小助手61,k6kt小助手64,K6KT小助手184,K6KT小助手168,k6kt小助手4,K6KT小助手336,K6KT小助手321,k6kt小助手262,K6KT小助手345,k6kt小助手278,K6KT小助手40,K6KT小助手243,K6KT小助手158,K6KT小助手177,k6kt小助手261,K6KT小助手163";
	  
	    Map<ObjectId, UserEntry> userMap =new HashMap<ObjectId, UserEntry>();
	    
	    String[] ns =users.split(",");
	    
	    System.out.println("用户个数："+ns.length);
	    for(String n:ns)
	    {
	    	UserEntry ue=dao.getUserEntryByName(n.toLowerCase());
	    	if(null!=ue)
	    	{
	    		userMap.put(ue.getID(), ue);
	    	}
	    	System.out.println();
	    	System.out.println();
	    	System.out.println();
	    	System.out.println("+++++++++++++++++++++++++++++");
	    	System.out.println("名字："+n);
	    	List<LetterEntry> letters=lDao.getLetterEntryList(ue.getID(), -1, -1, null, -1, 0, 20, Constant.FIELDS);
	    	
	    	for(LetterEntry le:letters)
	    	{
	    		//特邀体验员
	    		if(le.getContent().indexOf("免费游迪士尼")==0)
	    		{
	    			List<ReceiveInfo> list=le.getReceiveList();
	    			//System.out.println("uid："+ue.getID());
	    			System.out.println("总数："+list.size());
	    			
	    			int count=0;
	    			for(ReceiveInfo ri:list)
	    			{
	    				if(ri.getState()==2 || ri.getState()==3)
	    				{
	    					try
	    					{
	    				        UserEntry ue1=	dao.getUserEntry(ri.getReceiverId(), Constant.FIELDS);
	    				        SchoolEntry ses =schDao.getSchoolEntry(ue1.getSchoolID(), Constant.FIELDS);
	    					    System.out.println(ue1.getUserName()+":"+ses.getName());
	    					    count=count+1;
	    					}catch(Exception ex)
	    					{
	    						
	    					}
	    				}
	    			}
	    			System.out.println("已读："+count);
	    			
	    		}
	    	}
	    }
	    
	    
	   
	  
	}
}
