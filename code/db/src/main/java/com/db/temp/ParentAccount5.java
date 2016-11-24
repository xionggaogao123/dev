package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.user.UserDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 数据还原
 * @author fourer
 *
 */
public class ParentAccount5 {

	private static ObjectId maxId =new ObjectId("5715e9dc0cf2f633eef5e3f6");
	
	public static void main(String[] args) throws IOException {
		
		
		//System.out.println(DateTimeUtils.convert(new ObjectId("5715cf4b0cf20bb2e89a2082").getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
		
		
		File classLog =new File("/home/classo425w.txt");
		classLog.createNewFile();
		
		UserDao userDao =new UserDao();
		
		int skip=0;
		int limit=500;
		
		
		for(int i=0;i<20*100;i++)
		{
			skip=i*500;
			System.out.println("skip:"+skip);
			List<DBObject> dbos=getUUsersUsers(skip,limit);
			
			if(null==dbos || dbos.size()==0)
			{
				break;
			}
			
			for(DBObject dbo:dbos)
			{
				try
				{
							  Object cidObj=dbo.get("cid");
							  if(null==cidObj)
							  {
								  UserEntry ue =new UserEntry(dbo);
								  ue.setLetter(PinYin2Abbreviation.cn2py(ue.getUserName()));
								  userDao.addUserEntry(ue); 
								  continue;
							  }
							  
							  if(!(cidObj instanceof BasicDBList))
							  {
								  UserEntry ue =new UserEntry(dbo);
								  ue.setLetter(PinYin2Abbreviation.cn2py(ue.getUserName()));
								  userDao.addUserEntry(ue); 
								  continue;
							  }
								
								BasicDBList list =(BasicDBList)dbo.get("cid");
								
								if(null==list || list.size()==0)
								{
									dbo.put("cid", null);
								}
								else if(list.size()==1) //家长
								{
									if(dbo.get("nm").toString().indexOf("爸爸")>0) //爸爸
									{
										dbo.put("nm", dbo.get("nm").toString().replace("爸爸", "家长"));
										dbo.put("cid", list.get(0));
									}
									else if(dbo.get("nm").toString().indexOf("家长")>0)
									{
										dbo.put("cid", list.get(0));
									}
									else //妈妈丢弃
									{
										continue;
									}
								}
								else if(list.size()==2) //儿子
								{
									dbo.put("cid", list.get(0));
								}
								else
								{
									
									System.out.println(dbo);
									FileUtils.write(classLog, dbo.toString(),true);
									FileUtils.write(classLog, "\r\n",true);
								}
								
								
								UserEntry ue =new UserEntry(dbo);
								ue.setLetter(PinYin2Abbreviation.cn2py(ue.getUserName()));
								userDao.addUserEntry(ue);
				}catch(Exception ex)
				{
					FileUtils.write(classLog, "出错"+dbo.toString(),true);
					FileUtils.write(classLog, "\r\n",true);
				}
			}
		}
	}
	
	
	
	
	private static List<DBObject> getUUsersUsers(int skip,int limit)
	{
		BasicDBObject query =new BasicDBObject("_id",new BasicDBObject(Constant.MONGO_GT,maxId));
		List<DBObject> l=MongoFacroty.getAppDB().getCollection("uuser2").find(query,Constant.FIELDS).sort(Constant.MONGO_SORTBY_DESC).skip(skip).limit(limit).toArray();
		return l;
	}
	
	
	private static DBObject getParents(ObjectId pid)
	{
		BasicDBObject query =new BasicDBObject("_id",pid);
		DBObject l=MongoFacroty.getAppDB().getCollection("uuser2").findOne(query,Constant.FIELDS);
		return l;
	}
	
	
}
