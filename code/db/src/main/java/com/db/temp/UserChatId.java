package com.db.temp;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.exceptions.IllegalParamException;

public class UserChatId {

	
	
	
	//572089a563e763a5913163a1
	public static void main(String[] args) {
		
		
		System.out.println(new ObjectId().toString());
		
		
		UserDao dao =new UserDao();
		
		int skip=0;
		
		for(int i=0;i<10*120;i++)
		{
			skip=i*1000;
			
			System.out.println("skip="+skip);
			
			List<UserEntry> list=dao.searchUserByChatid(0,1000);
			
			if(null==list && list.size()==0)
			{
				break;
			}
			
			for(UserEntry u:list)
			{
				try {
					dao.update(u.getID(), "chatid", u.getID().toString(), false);
				} catch (IllegalParamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
