package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.resources.ResourceDao;
import com.db.resources.ResourceDictionaryDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.sys.constants.Constant;

public class ResourcefindNoVideo {

    public static void main(String[] args) throws IOException {


        List<DBObject> list = MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_RESOURCES).find(new BasicDBObject("_id", new BasicDBObject(Constant.MONGO_GTE, new ObjectId("573ea26363e702f38081546e")))).toArray();
        MongoFacroty.getAppDB().getCollection("res1").insert(list);

//		ResourceDao resourceDao =new ResourceDao();
//		ResourceDictionaryDao resourceDictionaryDao=new ResourceDictionaryDao();
//		List<ResourceEntry> list=resourceDao.getResourceEntry();
//		
//		 File file =new File("/home/micro0111a.txt");
//		 file.createNewFile();
//		
//		 for(ResourceEntry re:list)
//		 {
//			 try
//			 {
//				 String str=re.getID().toString()+"&A&Q";
//				 str+=re.getName()+"&A&Q";
//				 
//				ObjectId id= re.getPsbsList().get(0);
//				ResourceDictionaryEntry e=resourceDictionaryDao.getResourceDictionaryEntry(id);
//				if(null!=e)
//				{
//					for(IdValuePair idp:e.getParentInfos())
//					{
//						str+=idp.getValue().toString()+"&A&Q";
//					}
//					str+= e.getName().toString()+"&A&Q";
//				}
//				
//				 FileUtils.write(file, "\r\n",true);
//				 FileUtils.write(file, str,true);
//			 }catch(Exception ex)
//			 {
//			 }
//		 }


    }
}
