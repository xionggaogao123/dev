package com.db.temp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.itempool.ItemPoolDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.itempool.ItemPoolEntry;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.utils.MongoUtils;


public class ItempoolCountUpdate {

    static ItemPoolDao itemPoolDao = new ItemPoolDao();
    static ResourceDictionaryDao resourceDictionaryDao = new ResourceDictionaryDao();
    static Map<ObjectId, Integer> map = new HashMap<ObjectId, Integer>();

    public static void main(String[] args) {

//		
//		int skip=0;
//		int limit=200;
//		
//		while(true)
//		{
//			 System.out.println(skip);
//			 List<ItemPoolEntry> itemPoolEntry=itemPoolDao.getItemPoolEntry(skip, limit);
//			
//			 if(null==itemPoolEntry || itemPoolEntry.isEmpty())
//			 {
//				  break;
//			 }
//			 for(ItemPoolEntry se:itemPoolEntry)
//			 {
//				 try
//				 {
//					 //更新课程
//					 ObjectId id=se.getCourse();
//					 ResourceDictionaryEntry rde= resourceDictionaryDao.getResourceDictionaryEntry(id);
//					 updateRes( rde);
//					 
//					//更新知识点
//					 id=se.getClty();
//					 rde= resourceDictionaryDao.getResourceDictionaryEntry(id);
//					 updateRes1( rde);
//				 }catch(Exception ex)
//				 {
//					 
//				 }
//				 
//			 }
//			 skip=skip+200;
//		}
//		
//		for(Map.Entry<ObjectId, Integer> e:map.entrySet())
//		{
//			//resourceDictionaryDao.update(e.getKey(), e.getValue());
//		}


    }

    private static void updateRes(ResourceDictionaryEntry rde) {
        put(rde.getID());
        List<IdValuePair> idvList = rde.getParentInfos();
        List<ObjectId> idList = MongoUtils.getFieldObjectIDs(idvList, "id");
        if (idList.size() > 0) {
            for (ObjectId oid : idList) {
                put(oid);
            }
        }
    }


    private static void updateRes1(ResourceDictionaryEntry rde) {
        put(rde.getID());
        List<IdValuePair> idvList = rde.getParentInfos();
        List<ObjectId> idList = MongoUtils.getFieldObjectIDs(idvList, "id");
        if (idList.size() > 0) {
            for (int i = 2; i < idList.size(); i++) {
                put(idList.get(i));
            }
        }
    }


    private static void put(ObjectId id) {
        int c = 0;
        if (!map.containsKey(id)) {
            map.put(id, 0);
        } else {
            c = map.get(id);
            c = c + 1;
            map.put(id, c);
        }
    }
}
