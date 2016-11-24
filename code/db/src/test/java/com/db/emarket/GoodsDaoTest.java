package com.db.emarket;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.emarket.Comment;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.GoodsState;
import com.pojo.emarket.GoodsType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

public class GoodsDaoTest {

	GoodsDao dao =new GoodsDao();
	
	@Test
	public void addGoodsEntry()
	{
		List<Integer> list =new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		
//		GoodsEntry e =new GoodsEntry(1, list, 1, 1, new ObjectId(),"", new ObjectId("54cdf090f6f27442d6bce6aa"), 512, 12, 1);
//		int subject, List<Integer> cloudClassGradeTypes,
//		int goodsType, int goodsState, ObjectId owner, String name,ObjectId relateId,
//		int price, int sellCount, int expireTime
		//GoodsEntry e = new GoodsEntry(-1, list, GoodsType.LESSON.getType(), 1, new ObjectId("552e05fdf6f27442d6b52f87"), "这是商品名称3", new ObjectId("54cdf090f6f27442d6bce6aa"), 56, 1, 2);
		//dao.addGoodsEntry(e);
	}
	
	
	
	
	@Test
	public void addComment()
	{
//		Comment comment =new Comment(new ObjectId(), "ttt");
//		dao.addComment(new ObjectId("5514bd3563e76f4fcc0a02f0"), comment);
		
		//GoodsEntry e=dao.getGoodsEntry(new ObjectId("5514bd3563e76f4fcc0a02f0"),new BasicDBObject().append("coms", -1));
		//System.out.println(e.getBaseEntry());
		dao.deleteComment(new ObjectId("5514bd3563e76f4fcc0a02f0"));
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	public void update()
	{
		FieldValuePair pair =new FieldValuePair("sc", 34);
		FieldValuePair pair1 =new FieldValuePair("pr", 34);
		dao.update(new ObjectId("5512640463e72ae6048a6e0d"), pair,pair1);
	}
	
	@Test
	public void getGoodsList()
	{
		try {
			//sub, gradeType, gradeType, state,name, owner, new BasicDBObject("coms",Constant.ZERO), null, skip, limit
			List<GoodsEntry> list=	dao.getGoodsList(-1, null, -1, GoodsState.SHELVE.getType(), "",null,new ObjectId("552e05fdf6f27442d6b52f87"), new BasicDBObject("coms",Constant.ZERO), null, 0, 5,null,1);
			System.out.println(list);
		} catch (IllegalParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
