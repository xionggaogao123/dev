package com.db.emarket;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.emarket.GoodsType;
import com.pojo.emarket.OrderEntry;
import com.pojo.emarket.OrderState;
import com.pojo.emarket.OrderType;
import com.pojo.emarket.PayType;

public class OrderDaoTest {

	private OrderDao dao =new OrderDao();
	
	
	@Test
	public void addOrderEntry()
	{
//		List<Integer> list =new ArrayList<Integer>();
//		list.add(1);
//		list.add(2);
//		
//		IdValuePair userInfo =new IdValuePair(new ObjectId(), "张山");
//		IdValuePair goods =new IdValuePair(new ObjectId(), "学好物理");
		
		IdValuePair userInfo = new IdValuePair(new ObjectId("552e05fdf6f27442d6b52f88"), "enan");
		IdValuePair goodsInfo = new IdValuePair(new ObjectId("55404ee7a345c806bf9e34fe"), "实数");
		IdValuePair ownerInfo = new IdValuePair(new ObjectId("5548c3d27f724d97e0ee92a5"), "siri");
		OrderEntry entry = new OrderEntry("",-1, null, userInfo, GoodsType.LESSON, goodsInfo, PayType.NONE, 12, OrderType.EXCELLENTLESSON, ownerInfo,0);
		dao.addOrderEntry(entry);
		//int subject, List<Integer> cloudClassGradeTypes,IdValuePair userInfo,GoodsType type, IdValuePair goodsInfo,PayType paytype
//		OrderEntry e =new OrderEntry(1, list, userInfo,GoodsType.LESSON, goods, PayType.ALIPAY);
//		dao.addOrderEntry(e);
	}
	
	
	@Test
	public void updateState()
	{
		//dao.updateState(new ObjectId("5518b79063e79a922d4c87fb"), OrderState.ROVOKE);
		
		System.out.println(dao.getUserOrderList(-1, -1, new ObjectId("552e05fdf6f27442d6b52f87"), null, null, null, 0, 5));
		
	}
	
	@Test 
	public void getOrder(){
		List<OrderEntry> list = dao.getUserOrderlist(new ObjectId("5548c3d27f724d97e0ee92a5"), null, Long.parseLong("1430988255701"), Long.parseLong("1430988255707"), null, null, 0, 20);
		System.out.println(list);
	}
	
}
