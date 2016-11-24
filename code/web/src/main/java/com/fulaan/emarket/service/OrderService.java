package com.fulaan.emarket.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fulaan.utils.DoubleUtil;
import com.fulaan.utils.MD5Util;
import com.pojo.emarket.*;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;

import sql.dao.UserBalanceDao;
import sql.dataPojo.UserBalanceInfo;

import com.db.emarket.GoodsDao;
import com.db.emarket.OrderDao;
import com.db.lesson.LessonDao;
import com.db.user.UserDao;
import com.db.withdrawCash.WithdrawCashDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

/**
 * 订单服务类
 * @author fourer
 *
 */
public class OrderService {

	private OrderDao orderDao =new OrderDao();
	private UserDao userDao = new UserDao();
	private UserBalanceDao userBalanceDao = new UserBalanceDao();
	private GoodsDao goodsDao = new GoodsDao();
	private LessonDao lessonDao = new LessonDao();
    private WithdrawCashDao withdrawCashDao=new WithdrawCashDao();
	
	/**
	 * 根据订单id查询订单
	 * @param orderId
	 * @return
	 */
	public OrderEntry getOrderEntryById(String orderId){
		return orderDao.getOrderEntryByOrderId(new ObjectId(orderId));
	}

	/**
	 * 根据订单id查询订单
	 * @param orderId
	 * @return
	 */
	public OrderEntry getOrderEntryByOrderNum(String orderId){
		return orderDao.getOrderEntryByOrderNum(orderId);
	}
	
	/**
	 * 
	 * @param sub 科目ID
	 * @param gradeType 年级ID
	 * @param user 用户ID
	 * @param type 商品类型
	 * @param state 商品状态
	 * @return
	 */
	public int count(int sub, int gradeType,ObjectId user,GoodsType type,OrderState state,OrderType orderType)
	{
		return orderDao.count(sub, gradeType, user, type, state, orderType);
	}
	
	
	/**
	 * 
	 * @param sub 科目ID
	 * @param gradeType 年级ID
	 * @param user 用户ID
	 * @param type 商品类型
	 * @param state 商品状态
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<OrderEntry> getGoodsDTOList(int sub, int gradeType,ObjectId user,GoodsType type,OrderState state,OrderType orderType,int skip,int limit)
	{
		return orderDao.getUserOrderList(sub, gradeType, user, type, state, orderType, skip, limit);
	}
	
	/**
	 * 根据用户和商品ID查询
	 * @param user
	 * @param goodsId
	 * @return
	 */
	public OrderEntry getOrderEntry(ObjectId user,ObjectId goodsId)
	{
		return orderDao.getOrderEntry(user, goodsId);
	}
	
	/**
	 * 取消订单
	 * @param id 订单id
	 */
	@Transactional
	public void cancelOrder(ObjectId id) {
		orderDao.updateState(id, OrderState.ROVOKE);
	}
	
	/**
	 * 添加充值订单
	 * @return
	 */
	@Transactional
	public String generateRechargeOrders(PayType payType,double rechargeMoney,String userid) {
		// 生成订单编号
//		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
//		int r2 = (int) (Math.random() * (10));
//		int r3 = (int) (Math.random() * (10));
//		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		// 查询用户信息
		UserEntry userEntry = userDao.getUserEntry(new ObjectId(userid), new BasicDBObject("nm", 1));
		// 创建订单类 OrderEntry
		// 初始化订单类
//		String now = format.format(new Date());
		IdValuePair userInfo = new IdValuePair(new ObjectId(userid), userEntry.getUserName());
		OrderEntry e = new OrderEntry(getOrderNum(),-1, null, userInfo, GoodsType.ACCOUNT, new IdValuePair(new BasicDBObject()), OrderState.READY.getType(), System.currentTimeMillis(), payType, rechargeMoney, OrderType.RECHARGE, userInfo,0);
		orderDao.addOrderEntry(e);
		return e.getOrdernum();
	}
	
	/**
	 * 生成交易订单，更新用户账户余额，生成交易信息记录
	 * @param wdEntry
	 * @return
	 */
	@Transactional
	public Map withdrawCash(WithDrawEntry wdEntry) {
		String userid = wdEntry.getUserid();
		Map map = new HashMap();
		UserEntry e = userDao.getUserEntry(new ObjectId(userid), Constant.FIELDS);
		UserBalanceDTO ubInfo = userBalanceDao.getUserBalanceInfo(userid);
//		if (!ubInfo.getPassword().equals(MD5Util.getMD5String(wdEntry.getPassword()))) {
		if (!ubInfo.getPassword().equals(wdEntry.getPassword())) {
			map.put("resultCode", 1);
			map.put("mesg", "账户密码不正确，请重新输入！");
			return map;
		}
		if (ubInfo.getBalance()<wdEntry.getCash()) {
			map.put("resultCode", 1);
			map.put("mesg", "账户余额不足！");
			return map;
		}
		// 生成订单编号
		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
		int r2 = (int) (Math.random() * (10));
		int r3 = (int) (Math.random() * (10));
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		String now = format.format(new Date());
		String orderId = String.valueOf(now) + String.valueOf(r1) + String.valueOf(r2) + String.valueOf(r3);// 订单号
		OrderEntry orderEntry = orderDao.getOrderEntryByOrderId(new ObjectId(orderId));
		if (orderEntry != null) {
			withdrawCash(wdEntry);
		}
		orderEntry = new OrderEntry(new BasicDBObject());
		
		orderEntry.setPrice(wdEntry.getCash());
		IdValuePair userInfo = new IdValuePair(new ObjectId(userid), e.getUserName());
		orderEntry.setUserInfo(userInfo);
		orderEntry.setState(OrderState.READY.getType());
		orderEntry.setPayType(PayType.NONE.getType());
		orderEntry.setLastUpdateTime(new Date().getTime());
		orderDao.addOrderEntry(orderEntry);
		// 什么时候add，什么时候sub？？？
		userBalanceDao.addMoneyToBalance(userid, wdEntry.getCash());
        withdrawCashDao.addWithdrawCash(wdEntry);
//		WithdrawCashInfo cashInfo = new WithdrawCashInfo();
//		try {
		// TODO
			//withdrawCashDao.addWithdrawCash(withdraw);
//		} catch(Exception e) {
//			e.getStackTrace();
//		}
//		withdraw.setCash(withdraw.getCash());
//		withdraw.setUserid(withdraw.getUserid());
//		withdraw.setPaypalAccount(withdraw.getPaypalAccount());
		
		map.put("resultCode", 0);
		return map;
	}
	
	/**
	 * 获取订单信息
	 * @param orderId
	 * @param userid
	 * @return
	 */
	public Map selLessonOrder(String orderId,String userid) {
		Map map = new HashMap();
		OrderEntry orderEntry = orderDao.getOrderEntryByOrderId(new ObjectId(orderId));
//		//TODO START
//		if(null == orderEntry){
//			orderEntry = new OrderEntry(getOrderNum(),-1, null, new IdValuePair(new ObjectId(), "username"), GoodsType.LESSON, new IdValuePair(new ObjectId(), "test商品"), PayType.NONE, 2, OrderType.EXCELLENTLESSON, new IdValuePair(new ObjectId(), "ownername"));
//		}
//		//TODO END
		map.put("orderId", orderEntry.getOrdernum());
		if (orderEntry!=null) {
			map.put("lessonName", orderEntry.getGoodsInfo().getValue());
			map.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(orderEntry.getLastUpdateTime())));
			GoodsEntry goodsEntry = goodsDao.getGoodsEntry(orderEntry.getGoodsInfo().getId(), Constant.FIELDS);
//			//TODO START
//			if(goodsEntry == null){
//				goodsEntry = new GoodsEntry(null, null, 0, 0, null, null, null, 23, 3, 3,0);
//			}
//			//TODO END
			if (goodsEntry.getExpireTime()==0) {
				map.put("expiretime", "永久");
			} else {
				map.put("expiretime", goodsEntry.getExpireTime()+"个月");
			}
			map.put("price", goodsEntry.getPrice());
			map.put("balance", userBalanceDao.getUserBalanceInfo(userid).getBalance());
			map.put("resultCode", 0);
		} else {
			map.put("resultCode", 1);
		}
		return map;
	}
	
	/**
	 * 再次购买时生成订单
	 * @param orderId
	 * @param userid
	 * @return
	 */
	@Transactional
	public String rebuyLesson(String orderId,String userid) {
		OrderEntry orderEntry = orderDao.getOrderEntryByOrderId(new ObjectId(orderId));
		// 生成订单编号
//		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
//		int r2 = (int) (Math.random() * (10));
//		int r3 = (int) (Math.random() * (10));
//		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
//		String now = format.format(new Date());
//		String newOrder = String.valueOf(now) + String.valueOf(r1) + String.valueOf(r2) + String.valueOf(r3);// 订单号
//		order.setOrderId(newOrder);
//		order.setUserid(userid);
//		order.setPrice(orderInfo.getPrice());
//		order.setStatus(0);
//		order.setLessonId(orderInfo.getLessonId());
//		order.setSrclessonId(orderInfo.getSrclessonId());
//		order.setOrderType(orderInfo.getOrderType());
//		order.setPaymentStatus(0);
//		orderMapper.addOrders(order);
		//											int subject, 			List<Integer> cloudClassGradeTypes,	IdValuePair userInfo,		GoodsType type, 				IdValuePair goodsInfo,	PayType paytype,double price,OrderType orderType
		OrderEntry newOrderEntry = new OrderEntry(getOrderNum(),orderEntry.getSubject(),
												orderEntry.getCloudClassGradeTypes(), 
												orderEntry.getUserInfo(), 
												GoodsType.getGoodsType(orderEntry.getGoodsType()), 
												orderEntry.getGoodsInfo(), 
												PayType.getPayType(orderEntry.getPayType()), 
												orderEntry.getPrice(), 
												OrderType.getOrderType(orderEntry.getOrderType()),
												orderEntry.getOwnerInfo(),0);
//		orderEntry.setID(new ObjectId());
		return orderDao.addOrderEntry(newOrderEntry).toString();
	}
	
	 /**
	  * 创建订单
	  */
	@Transactional
	public String createOrder(String goodId,String userid){
		OrderEntry e = orderDao.getOrderEntry(new ObjectId(userid), new ObjectId(goodId));
		OrderEntry oEntry = null;
		if(null == e){
			GoodsEntry gEntry = goodsDao.getGoodsEntry(new ObjectId(goodId), Constant.FIELDS);
			LessonEntry lesson = lessonDao.getLessonEntry(gEntry.getRelateId(), null);
			LessonEntry lessonentry = new LessonEntry(lesson.getName(),lesson.getContent(),LessonType.getLessonType(lesson.getType()),lesson.getUserId(),null,lesson.getImgUrl(),lesson.getVideoIds(),lesson.getLastUpdateTime(),lesson.getVideoCount(),lesson.getDocumentCount(),lesson.getExerciseCount(),lesson.getLessonWareList(),lesson.getExercise(),lesson.getID(),lesson.getIsFromCloud());
			ObjectId lessonid= lessonDao.addLessonEntry(lessonentry);
			GoodsEntry goodentry = new GoodsEntry(gEntry.getSubjects(),gEntry.getCloudClassGradeTypes(),2,GoodsState.UNSHELVE.getType(),new ObjectId(userid),gEntry.getName(),lessonid,gEntry.getID(),gEntry.getPrice(),gEntry.getSellCount(),gEntry.getExpireTime(),gEntry.getDesc(),0,gEntry.getIsopen());
			ObjectId gid = goodsDao.addGoodsEntry(goodentry);
			int subject = 0;
			if (gEntry.getSubjects()!=null && gEntry.getSubjects().size()!=0) {
				subject = gEntry.getSubjects().get(0);
			}
			List<Integer> ccgts = gEntry.getCloudClassGradeTypes();
			UserEntry uEntry = userDao.getUserEntry(new ObjectId(userid), Constant.FIELDS);
			IdValuePair userInfo = new IdValuePair(new ObjectId(userid), uEntry.getNickName());
//			GoodsType type = GoodsType.getGoodsType(goodentry.getGoodsType());
			IdValuePair goodsInfo = new IdValuePair(gid, goodentry.getName());
			UserEntry ownerEntry = userDao.getUserEntry(gEntry.getOwner(), Constant.FIELDS);
			IdValuePair ownerInfo = new IdValuePair(gEntry.getOwner(), ownerEntry.getUserName());
			oEntry = new OrderEntry(getOrderNum(),subject, ccgts, userInfo, GoodsType.LESSON, goodsInfo, PayType.NONE, goodentry.getPrice(), OrderType.EXCELLENTLESSON, ownerInfo,0);
		}else{
			oEntry = new OrderEntry(e.getOrdernum(),e.getSubject(), e.getCloudClassGradeTypes(), e.getUserInfo(), GoodsType.LESSON, e.getGoodsInfo(), PayType.NONE, e.getPrice(), OrderType.getOrderType(e.getOrderType()), e.getOwnerInfo(),e.getExpireTime());
		}
		return orderDao.addOrderEntry(oEntry).toString();
	}

	/**
	 * 订单编号
	 * @return
	 */
	public String getOrderNum() {
		// 生成订单编号
		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
		int r2 = (int) (Math.random() * (10));
		int r3 = (int) (Math.random() * (10));
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		String now = format.format(new Date());
		String newOrder = String.valueOf(now) + String.valueOf(r1) + String.valueOf(r2) + String.valueOf(r3);// 订单号
		return newOrder;
	}
	
	/**
	 * 查询销售订单
	 * @param lessonName
	 * @param startTime
	 * @param endTime
	 * @param nickName
	 * @param state
	 * @return
	 */
	public List<OrderDTO> getSaleOrderList(String userId, String lessonName, long startTime, long endTime, String nickName, OrderState state, int skip, int limit){
		List<OrderEntry> list = orderDao.getUserOrderlist(new ObjectId(userId), lessonName, startTime, endTime, nickName, state, skip, limit);
		if(null != list && !list.isEmpty()){
			List<OrderDTO> dtoList = new ArrayList<OrderDTO>();
			for (OrderEntry entry : list) {
				dtoList.add(new OrderDTO(entry,0));
			}
			return dtoList;
		}
		return null;
	}
	
	/**
	 * 查询销售订单
	 * @param lessonName
	 * @param startTime
	 * @param endTime
	 * @param nickName
	 * @param state
	 * @return
	 */
	public int count(String userId, String lessonName, long startTime, long endTime, String nickName, OrderState state){
		return orderDao.count(new ObjectId(userId), lessonName, startTime, endTime, nickName, state);
	}

	/**
	 *
	 * @param password
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Map payLessonByBlance(String password, String orderId, String userId) {
		Map map = new HashMap();
		UserEntry user = userDao.getUserEntry(new ObjectId(userId), null);
		try {
			if (!(MD5Util.getMD5(password)).equals(user.getPassword())) {
				map.put("resultCode", 1);
				map.put("mesg", "支付密码不正确！");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OrderEntry order = orderDao.getOrderEntryByOrderNum(orderId);
		if (order.getState()==4) {
			map.put("resultCode", 1);
			map.put("mesg", "该订单已取消，请确认后支付！");
			return map;
		}
		if (order.getState()==2) {
			map.put("resultCode", 1);
			map.put("mesg", "该订单已支付！");
			return map;
		}
		if (DoubleUtil.subtract(userBalanceDao.getUserBalanceInfo(userId).getBalance(), order.getPrice())<0) {
			map.put("resultCode", 1);
			map.put("mesg", "账户余额不足，请及时充值！");
			return map;
		} else {
			ObjectId goodid = order.getGoodsInfo().getId();
			GoodsEntry goodentry = goodsDao.getGoodsEntry(goodid,null);
			long endtime = DateTimeUtils.getDate(new Date(),(int)goodentry.getExpireTime());
			if (goodentry.getEnddate()!=0) {
				if (goodentry.getEnddate()>System.currentTimeMillis()) {
					endtime = DateTimeUtils.getDate(new Date(goodentry.getEnddate()),(int)goodentry.getExpireTime());
				}
			}
			long expiretime = DateTimeUtils.getDate(new Date(),(int)goodentry.getExpireTime());
			if (order.getExpireTime()!=0) {
				if (order.getExpireTime()>System.currentTimeMillis()) {
					expiretime = DateTimeUtils.getDate(new Date(order.getExpireTime()), (int) goodentry.getExpireTime());
				}
			}
			goodsDao.updateExpiretime(goodid,endtime/1000);
			goodsDao.updateCount(goodentry.getRelateGoodId());
			orderDao.updateExpiretime(order.getID(),PayType.ONLINE.getType(),expiretime);
			userBalanceDao.subMoneyFromBalance(order.getUserInfo().getId().toString(),(-order.getPrice()));
			userBalanceDao.subMoneyFromBalance(order.getOwnerInfo().getId().toString(), (order.getPrice()*0.7));
			map.put("resultCode", 0);
			map.put("lessonId", goodentry.getRelateId());
		}

		return map;
	}

}
