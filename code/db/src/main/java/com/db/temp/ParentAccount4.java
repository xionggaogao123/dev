package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;

public class ParentAccount4 {

	public static void main(String[] args) throws IOException {
		
		File classLog =new File("/home/classo425.txt");
		classLog.createNewFile();
		
		
		ClassDao classDao =new ClassDao();
		UserDao userDao =new UserDao();
		
		int skip=0;
		int limit=500;
		
		ObjectId thisObjId=null;
		int thisRole=0;
		UserEntry thisUser;
		
		for(int i=0;i<20*20;i++)
		{
			skip=i*500;
			System.out.println("users:"+skip);
			List<DBObject> dbos=getErrorUsers(skip,limit);
			
			if(null==dbos || dbos.size()==0)
			{
				break;
			}
			
			for(DBObject dbo:dbos)
			{
				thisObjId=(ObjectId)dbo.get("_id");
				thisRole=(Integer)dbo.get("r");
				thisUser=userDao.getUserEntry(thisObjId, Constant.MONGO_SORTBY_ASC);
				if( null == thisUser && thisRole!=4 )
				{
					if(thisRole==UserRole.TEACHER.getRole())
					{
						classDao.deleteTeacher(thisObjId);
					}
					
					if(thisRole==UserRole.STUDENT.getRole())
					{
						classDao.deleteStu(thisObjId);
					}
				}
			}
		}
		
		
		System.out.println("开始更新class 数据");
		
		for(int i=0;i<20;i++)
		{
			skip=i*500;
			List<ClassEntry> list =classDao.getAllEntrys(skip, limit);
			
			System.out.println("class:"+skip);
			
			
			if(null==list || list.size()==0)
			{
				break;
			}
			
			for(ClassEntry ce:list)
			{
				
				Map<ObjectId, UserEntry> map=userDao.getUserEntryMap(ce.getStudents(), Constant.MONGO_SORTBY_ASC);
				
				if(ce.getTotalStudent()!=map.size()) //需要处理
				{
					System.out.println("开始更新class:"+ce.getID().toString()+";count:"+ce.getTotalStudent());
					
					FileUtils.write(classLog,"开始更新class:"+ce.getID().toString()+";count:"+ce.getTotalStudent(),true);
					FileUtils.write(classLog, "\r\n",true);
					
					Set<ObjectId> existsSet =map.keySet();
					for(ObjectId stuid:ce.getStudents())
					{
						if(!existsSet.contains(stuid))
						{
							classDao.deleteStuById(ce.getID(), stuid);
						}
					}
					
					
					ClassEntry newCe=classDao.getClassEntryById(ce.getID(), Constant.FIELDS);
					classDao.updateStudentCount(ce.getID(), newCe.getStudents().size());
					
					FileUtils.write(classLog,"学生数量："+newCe.getStudents().size(),true);
					FileUtils.write(classLog, "\r\n",true);
				}
			}
		}
	}
	
	
	private static List<DBObject> getErrorUsers(int skip,int limit)
	{
		BasicDBObject query =new BasicDBObject("_id",new BasicDBObject(Constant.MONGO_GT,new ObjectId("5715e9dc0cf2f633eef5e3f6")));
		List<DBObject> l=MongoFacroty.getAppDB().getCollection("uuser").find(query,new BasicDBObject("_id",1).append("r", 1).append("ir", 1)).sort(Constant.MONGO_SORTBY_DESC).skip(skip).limit(limit).toArray();
		return l;
	}
	
}
