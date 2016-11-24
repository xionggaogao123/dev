package com.db.temp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.db.itempool.ItemPoolDao;
import com.db.questions.ItemStoreDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.itempool.ItemPoolEntry;
import com.pojo.questions.ItemProperty;
import com.pojo.questions.ItemStoreEntry;
import com.pojo.questions.PropertiesObj;
import com.pojo.resources.ResourceDictionaryEntry;
import com.sys.utils.MD5Utils;


public class ItemPoolToItemStore {

	
	private static ResourceDictionaryDao resourceDictionaryDao =new ResourceDictionaryDao();
	
	private static ItemStoreDao itemStoreDao =new ItemStoreDao();
	
	private static Set<String> hashSet =new HashSet<String>();
	
	public static void main(String[] args) throws Exception {
		
		//目录
		ResourceDictionaryEntry defaultmulu=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf("101102101101106"), 6);
		
		ResourceDictionaryEntry defaultzhishidian=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf("101102101101"), 8);
		
		ItemPoolDao itemPooldao =new ItemPoolDao();
		
		int skip=0;
		int limit=200;
		int error=0;
		String md5="";
		ItemStoreEntry ise=null;
		while(true)
		{
			System.out.println("skip="+skip);
			List<ItemPoolEntry> list=itemPooldao.getItemPoolEntry(skip, limit);
			
			 if(null==list || list.isEmpty())
			 {
				  break;
			 }
			
			for(ItemPoolEntry ipe:list)
			{
				
				md5=MD5Utils.getMD5(ipe.getQuestion());
				
				if(hashSet.contains(md5))
				{
					continue;
				}
				
				hashSet.add(md5);
				
				try
				{
				   ise=buildItemStoreEntry(ipe,defaultmulu,defaultzhishidian);
				   
				   if(null!=ise)
				   {
				     itemStoreDao.addQuestions(ise);
				   }
				}catch(Exception ex)
				{
					error=error+1;
				}
			}	
			skip=skip+200;
		}
		System.out.println("error="+error);
		
		
	}
	
	public static ItemStoreEntry buildItemStoreEntry(ItemPoolEntry e,ResourceDictionaryEntry defaultmulu,ResourceDictionaryEntry defaultzhishidian)
	{
		
		String questionTopic="";
		ResourceDictionaryEntry rde=resourceDictionaryDao.getResourceDictionaryEntry(e.getItemType());
		if(rde.getName().equals("选择题"))
		{
			questionTopic="选择题";
		}else if(rde.getName().equals("判断题"))
		{
			questionTopic="判断题";
		}else if(rde.getName().equals("填空题"))
		{
			questionTopic="填空题";
		}else
		{
			questionTopic="主观题";
		}
		
		String rightAnswer=e.getAnswer();
		
		if(rde.getName().equals("选择题"))
		{
			rightAnswer=e.getItem().getAnswer();
		}
		
		 List<PropertiesObj> versionList =new ArrayList<PropertiesObj>();
		 List<PropertiesObj> knowledgeList =new ArrayList<PropertiesObj>();
		 
		 ObjectId kwId=null;
		 ObjectId banbenId=null;
		 
		 if(e.getScList().size()!=0)
		 {
			 kwId=e.getScList().get(0);
		 }
		 
		 
		 if(e.getPsbList().size()!=0)
		 {
			 banbenId=e.getPsbList().get(0);
		 }
		
		 
		 
		 if(null==kwId )
		 {
			 kwId=defaultzhishidian.getID();
		 }
		 
		 
		 if(null==banbenId)
		 {
			 banbenId=defaultmulu.getID();
		 }
		 
		 
		 if(null==kwId ||  null==banbenId)
		 {
			throw new NullPointerException();
		 }
		 
		 //知识点
		 ResourceDictionaryEntry kwRe= resourceDictionaryDao.getResourceDictionaryEntry(kwId);
		 
		 if(null!=kwRe && kwRe.getParentInfos().size()!=0)
		 {
			 for(IdValuePair idv:kwRe.getParentInfos())
			 {
				 knowledgeList.add(new PropertiesObj(idv.getId().toString(), idv.getValue().toString()));
			 }
			 knowledgeList.add(new PropertiesObj(kwRe.getID().toString(), kwRe.getName().toString()) );
		 }
		 
		 
		 //章节
        ResourceDictionaryEntry psbRe= resourceDictionaryDao.getResourceDictionaryEntry(banbenId);
		 
		 if(null!=psbRe && psbRe.getParentInfos().size()!=0)
		 {
			 for(IdValuePair idv:psbRe.getParentInfos())
			 {
				 versionList.add(new PropertiesObj(idv.getId().toString(), idv.getValue().toString()));
			 }
			 versionList.add(new PropertiesObj(psbRe.getID().toString(), psbRe.getName().toString()) );
		 }
		 
		 ItemProperty pp =new ItemProperty(versionList,knowledgeList);
		
		 List<ItemProperty> aaalist =new ArrayList<ItemProperty>();
		 
		 aaalist.add(pp);
		
	
		 ItemStoreEntry ee =new ItemStoreEntry(questionTopic, e.getQuestion(),rightAnswer , e.getParseAnser(), System.currentTimeMillis(), "复兰教育局", new ObjectId("561888560cf26d224f4917d7"), aaalist, 1);
	     return ee;
	
	}
}
