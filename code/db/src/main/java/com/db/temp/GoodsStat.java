package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.db.lesson.LessonDao;
import com.pojo.lesson.LessonEntry;
import org.apache.commons.io.FileUtils;

import com.db.emarket.GoodsDao;
import com.db.emarket.OrderDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.app.IdValuePair;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.OrderEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

public class GoodsStat {

	
	
	public static void main(String[] args) throws IOException {
		
		File f =new File("/home/0826_goods.txt");
		f.createNewFile();
		
		GoodsDao gDao =new GoodsDao();
		UserDao uDao =new UserDao();
		SchoolDao sDao =new SchoolDao();
		OrderDao oDao =new OrderDao();
		LessonDao lessonDao = new LessonDao();
		
		List<GoodsEntry> list= gDao.getGoodsList();
		
		for(GoodsEntry ge:list)
		{
			StringBuilder b =new StringBuilder();
			b.append(DateTimeUtils.convert(ge.getID().getDate().getTime(), DateTimeUtils.DATE_YYYY_MM_DD)).append(",").append(ge.getName()).append(",");
			b.append(ge.getSellCount()).append(",");
			UserEntry ue =uDao.getUserEntry(ge.getOwner(), Constant.FIELDS);
			
			if(null!=ue)
			{
				try
				{
					b.append(ue.getUserName()).append(",");
					SchoolEntry se=sDao.getSchoolEntry(ue.getSchoolID(), Constant.FIELDS);
					b.append(se.getName()).append(",");
				}catch(Exception ex)
				{
					
				}
			}
			
			 FileUtils.write(f, "\r\n",true);
			 FileUtils.write(f, b.toString(),true);
			
			 
			 
			 
			 if(ge.getSellCount()>0)
				{
//					GoodsEntry entry = gDao.getGoodsEntry(ge.getRelateGoodId(), null);
					LessonEntry lessonEntry = lessonDao.getLessonEntry(ge.getRelateId(),null);
					List<OrderEntry> orders=oDao.getUserOrderlist(lessonEntry.getUserId(),ge.getName(),0,0,"",null,0,20000);
					
					StringBuilder b2=new StringBuilder();
					for(OrderEntry oe:orders)
					{
						 IdValuePair ip=oe.getUserInfo();
						 b2.append(ip.getValue().toString()).append(",");
					     UserEntry ue2 =uDao.getUserEntry(ip.getId(), Constant.FIELDS);
					     if(null!=ue2)
					     {
						   SchoolEntry se2=sDao.getSchoolEntry(ue2.getSchoolID(), Constant.FIELDS);
						   b2.append(se2.getName()).append(",");
					     }
					}
					
					
					
					FileUtils.write(f, "\r\n",true);
					 FileUtils.write(f, b2.toString(),true);
				}
			 
			 
			 
		}
	}
}
