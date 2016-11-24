package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

public class MongoDBPressureTest {

	static List<String> list =new ArrayList<String>();
	public static void main(String[] args) throws IOException {
		
		
		list.addAll(FileUtils.readLines(new File("\\home\\ids.txt")));
		
		
		
		for(int i=0;i<700;i++)
		{
			Thread thread =new Thread(new Runnable() {
				@Override
				public void run() {
					
					long a=System.currentTimeMillis();
//					String nm="k6kt小助手";
//					MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_USER_NAME).find(new BasicDBObject("nm",MongoUtils.buildRegex(nm)),new BasicDBObject());
//					
				   List<DBObject> li=	MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_USER_NAME).find(new BasicDBObject("si",new ObjectId(list.get(RandomUtils.nextInt(500)))),new BasicDBObject("_id",1)).toArray();
					
				    if(null!=li && li.size()>0)
				    {
				    	for(int j=0;j<li.size();j++)
				    	{
				    		DBObject dbo=li.get(j);
				    	}
				    }
				    
//					List<DBObject>   li=	MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_USER_NAME).find(new BasicDBObject(),Constant.FIELDS).limit(200000).toArray();
//					
//				    if(null!=li && li.size()>0)
//				    {
//				    	for(int j=0;j<li.size();j++)
//				    	{
//				    		DBObject dbo=li.get(j);
//				    	}
//				    }
					long b=System.currentTimeMillis();
					System.out.println(Thread.currentThread().getName()+":"+(b-a)+":"+li.size());
					
					
				}
			});
			
			thread.setName("th"+i);
			thread.start();
		}
		
	}
}
