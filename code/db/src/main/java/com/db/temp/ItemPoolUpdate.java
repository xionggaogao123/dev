package com.db.temp;

import java.util.List;


import com.db.itempool.ItemPoolDao;
import com.mongodb.BasicDBList;
import com.pojo.itempool.ItemPoolEntry;
import com.sys.exceptions.IllegalParamException;

public class ItemPoolUpdate {

    public static void main(String[] args) throws IllegalParamException {

//	    ItemPoolDao dao =new ItemPoolDao();
//		int skip=0;
//		int limit=200;
//
//		while(true)
//		{
//			System.out.println("skip="+skip);
//			 List<ItemPoolEntry> schoolList=dao.getItemPoolEntry(skip, limit);
//
//			 if(null==schoolList || schoolList.isEmpty())
//			 {
//				  break;
//			 }
//
//			 for(ItemPoolEntry se:schoolList)
//			 {
//				 BasicDBList scsList =new BasicDBList();
//				 scsList.add(se.getBaseEntry().get("clty"));
//				 dao.update(se.getID(), "scs", scsList);
//
//
//
//				 BasicDBList psbsList =new BasicDBList();
//				 psbsList.add(se.getBaseEntry().get("psbs2"));
//				 dao.update(se.getID(), "psbs", psbsList);
//
//				 dao.update(se.getID(), "ows", new BasicDBList());
//			 }
//			 skip=skip+200;
//		}
    }
}
