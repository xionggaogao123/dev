package com.fulaan.emarket.service;

import com.db.app.RegionDao;
import com.db.emarket.GoodsDao;
import com.db.emarket.OrderDao;
import com.db.lesson.DirDao;
import com.db.lesson.LessonDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.db.withdrawCash.WithdrawCashDao;
import com.fulaan.utils.CollectionUtil;
import com.fulaan.utils.MD5Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.app.RegionEntry;
import com.pojo.emarket.*;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.school.GradeType;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;

import sql.dao.UserBalanceDao;
import sql.dataPojo.UserBalanceInfo;

import javax.swing.text.AbstractDocument;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品service
 * @author fourer
 *
 */
public class GoodsService {
	
	private static final Logger logger =Logger.getLogger(GoodsService.class);
	
	private GoodsDao goodsDao =new GoodsDao();
	private LessonDao lessionDao =new LessonDao();
	private UserDao userDao =new UserDao();
	private SchoolDao schoolDao =new SchoolDao();
	private UserBalanceDao userBalanceDao = new UserBalanceDao();
	private OrderDao orderDao = new OrderDao();
	private DirDao dirDao = new DirDao();
    private WithdrawCashDao withdrawCashDao=new WithdrawCashDao();
	private RegionDao regionDao=new RegionDao();

	
	/**
	 * 添加一个商品
	 * @param e
	 */
	public ObjectId addGoods(GoodsEntry e)
	{
		return goodsDao.addGoodsEntry(e);
	}
	
	
	/**
	 * 得到商品数目
	 * @param sub 科目ID
	 * @param type 年级
	 * @param type 类型
	 * @param state 状态
	 * @param name 名称
	 * @param owner 拥有者
	 * @return
	 * @throws IllegalParamException 
	 */
	public int countGoods(int sub,List<Integer> grades,int type,int state, String name ,ObjectId owner,int goodtype,int ismytype) throws IllegalParamException
	{
		List<ObjectId> users = null;
		List<ObjectId> userids = new ArrayList<ObjectId>();
		if (!StringUtils.isEmpty(name)) {
			List<ObjectId> schoolids = schoolDao.getSchoolIdByNames(name);
			if (schoolids!=null && schoolids.size()!=0) {
				List<UserEntry> list = userDao.getTeacherEntryBySchoolIds(schoolids,null);
				users=MongoUtils.getFieldObjectIDs(list, "_id");
			}
			userids = userDao.findIdListByUserName(name);
		}
		int count = 0;
		if (goodtype==1) {
			count = goodsDao.countGoods(sub, grades, type, state,name,users, owner,userids,ismytype);
		} else if (goodtype==2) {
			List<GoodsEntry> ngoodsList = new ArrayList<GoodsEntry>();
			List<GoodsEntry> goodslist = goodsDao.getGoodsList(sub, grades, type, state, name,users, owner,Constant.FIELDS,null,userids,ismytype);
			for (GoodsEntry good : goodslist) {
				LessonEntry les = lessionDao.getLessonEntry(good.getRelateId(), Constant.FIELDS);
				if (les.getExerciseCount()!=0 || les.getVideoCount()!=0 || les.getDocumentCount()!= 0) {
					ngoodsList.add(good);
				}
			}
			count = ngoodsList.size();
		}
		return count;
	}
	
	
	/**
	 * 得到商品列表
	 * @param sub 科目ID
	 * @param grades 年级
	 * @param type 类型
	 * @param state 状态
	 * @param name 名称
	 * @param owner 拥有者
	 * @return
	 * @throws IllegalParamException 
	 */
	public List<GoodsDTO> getGoodsDTOList(int sub,List<Integer> grades,int type,int state,String name,ObjectId owner,int skip,int limit,int goodtype,int ismytype) throws IllegalParamException
	{
		List<GoodsDTO> retList =new ArrayList<GoodsDTO>();
		List<GoodsEntry> ngoodsList = new ArrayList<GoodsEntry>();
		List<GoodsEntry> goodsList = new ArrayList<GoodsEntry>();
		List<ObjectId> users = null;
		List<ObjectId> userids = new ArrayList<ObjectId>();
		if (!StringUtils.isEmpty(name)) {
			List<ObjectId> schoolids = schoolDao.getSchoolIdByNames(name);
			if (schoolids!=null && schoolids.size()!=0) {
				List<UserEntry> list = userDao.getTeacherEntryBySchoolIds(schoolids,null);
				users=MongoUtils.getFieldObjectIDs(list, "_id");
			}
			userids = userDao.findIdListByUserName(name);
		}

		if (goodtype==2) {
			goodsList=goodsDao.getGoodsList(sub, grades, type, state,name,users, owner, new BasicDBObject("coms",Constant.ZERO), null,userids,ismytype);
		} else {
			goodsList=goodsDao.getGoodsList(sub, grades, type, state,name,users, owner, new BasicDBObject("coms",Constant.ZERO), null,skip,limit,userids,ismytype);
		}

		if (goodtype==2 && goodsList!=null && goodsList.size()!=0) {
			List<ObjectId> relates = new ArrayList<ObjectId>();
			for (GoodsEntry good : goodsList) {
				relates.add(good.getRelateId());
//				LessonEntry les = lessionDao.getLessonEntry(good.getRelateId(), Constant.FIELDS);
			}
			List<LessonEntry> lessons = lessionDao.selLessonList(relates);
			for (GoodsEntry good : goodsList) {
				for (LessonEntry les : lessons) {
					if (good.getRelateId().equals(les.getID())) {
						if (les.getExerciseCount()!=0 || les.getVideoCount()!=0 || les.getDocumentCount()!= 0) {
							ngoodsList.add(good);
						}
					}

				}
			}
			Collections.reverse(ngoodsList);
			goodsList = listImitatePage(ngoodsList,skip,limit);
		}
		
		List<ObjectId> relatList =MongoUtils.getFieldObjectIDs(goodsList, "rid");
		List<ObjectId> userList =MongoUtils.getFieldObjectIDs(goodsList, "ow");
		
		GoodsDTO dto=null;
			Map<ObjectId, LessonEntry> lessionMap=lessionDao.getLessonEntryMapByIDs(relatList, new BasicDBObject("im",1).append("vc",1).append("dc",1).append("ec",1));
			Map<ObjectId, UserEntry> userMap=userDao.getUserEntryMap(userList,new BasicDBObject("nm",1).append("si", 1));
			
			LessonEntry le=null;
			UserEntry ue=null;
			SchoolEntry se;
			IdValuePair schoolPair =null;
			for(GoodsEntry e:goodsList)
			{
				try
				{
					le=lessionMap.get(e.getRelateId());
					ue=userMap.get(e.getOwner());
					if(null==le || null==ue)
					{
						continue;
					}
					SchoolEntry school = schoolDao.getSchoolEntry(ue.getSchoolID(),Constant.FIELDS);
					RegionEntry regionEntry=regionDao.getRegionById(school.getRegionId());
					RegionEntry region=regionDao.getRegionById(regionEntry.getParentId());
					String province = region.getName() == null ? "" : region
							.getName();
					String city = regionEntry.getName() == null ? "" : regionEntry.getName();
					String schoolname = school.getName() == null ? "" : school.getName();
					schoolPair=new IdValuePair(school.getID(), province + " " + city + " " + schoolname);
					dto=new GoodsDTO(e, le);
					dto.getUser().setValue(ue.getUserName());
					dto.setSchool(schoolPair);
					retList.add(dto);
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		
		return retList;
	}

	/**
	 * 模拟对list分页查询
	 * @param list
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<GoodsEntry> listImitatePage(List<GoodsEntry> list,int page,int pageSize) {
		int totalCount =list.size();
		int pageCount=0;
		int m=totalCount%pageSize;
		if(m>0){
			pageCount=totalCount/pageSize+1;
		} else {
			pageCount=totalCount/pageSize;
		}
		List<GoodsEntry> subList=new ArrayList<GoodsEntry>();
		if(list!=null&&list.size()>0) {
			if (m == 0) {
				subList = list.subList((page - 1) * pageSize, pageSize * (page));
			} else {
				if (page == pageCount) {
					subList = list.subList((page - 1) * pageSize, totalCount);
				} else {
					subList = list.subList((page - 1) * pageSize, pageSize * (page));
				}
			}
		}
		return subList;
	}
	
	
	
	/**
	 * 根据商品ID查询商品DTO
	 * @param goodsIds
	 * @param type
	 * @return
	 * @throws IllegalParamException
	 */
	public List<GoodsDTO> getGoodsDTOList(Collection<ObjectId> goodsIds,int type) throws IllegalParamException
	{
		List<GoodsDTO> retList =new ArrayList<GoodsDTO>();
		List<GoodsEntry> goodsList=goodsDao.getGoodsList(goodsIds, new BasicDBObject("coms",Constant.ZERO));
		List<ObjectId> relatList =MongoUtils.getFieldObjectIDs(goodsList, "rid");
		
		GoodsDTO dto=null;
		if(type==GoodsType.LESSON.getType())//课程
		{
			Map<ObjectId, LessonEntry> lessionMap=lessionDao.getLessonEntryMapByIDs(relatList, new BasicDBObject("im",1));
			LessonEntry le=null;
			for(GoodsEntry e:goodsList)
			{
				try
				{
					le=lessionMap.get(e.getRelateId());
					if(null==le )
					{
						continue;
					}
					dto=new GoodsDTO(e, le);
					retList.add(dto);
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		}
		return retList;
	}
	
	
	
	/**
	 * 商品详情
	 * @param id
	 * @return
	 */
	public GoodsEntry getGoodsEntry(ObjectId id)
	{
		return goodsDao.getGoodsEntry(id,new BasicDBObject("coms",Constant.ZERO));
	}
	
	
	/**
	 * 更新字段
	 * @param id
	 * @param pairs
	 */
	public void update(ObjectId id,FieldValuePair ...pairs)
	{
		goodsDao.update(id, pairs);
	}

	
	/**
	 * 添加一个商品评论
	 * @param goodsid
	 * @param comment
	 */
    public void addComment(ObjectId goodsid,Comment comment)
    {
    	GoodsEntry e=getGoodsEntry(goodsid);
    	if(e.getCommentCount()>=Constant.MAX_GOODS_COMMENT_INT)
    	{
    		goodsDao.deleteComment(goodsid);
    	}
    	goodsDao.addComment(goodsid, comment);
    }
	
	
	 /**
     *  得到用户学校信息 
     * @param users 必须包含_id和si两个字段
     * @param schoolFields 学校中的字段
     * @return key为用户的ID
     */
    private Map<ObjectId,SchoolEntry> getUserSchoolInfo(Collection<UserEntry> users,DBObject schoolFields)
    {
    	Map<ObjectId,SchoolEntry> retMap =new HashMap<ObjectId, SchoolEntry>();
    	
    	if(null!=users)
    	{
	    	List<ObjectId> schoolsId =MongoUtils.getFieldObjectIDs(users, "si");
	    	Map<ObjectId, SchoolEntry> schoolInfoMap=schoolDao.getSchoolMap(schoolsId, schoolFields);
	    	SchoolEntry se;
	    	for(UserEntry ue:users)
	    	{
	    		se=schoolInfoMap.get(ue.getSchoolID());
	    		if(null!=se)
	    		{
	    			retMap.put(ue.getID(), se);
	    		}
	    	}
    	}
    	return retMap;
    }
	
    /**
	  * 获取购买信息
	  * @param goodId
	  * @return
	  */
	 public BuyGoodDetailDTO buyLessonDetail(String goodId) {
		 GoodsEntry entry = goodsDao.getGoodsEntry(new ObjectId(goodId), Constant.FIELDS);
		 UserEntry uEntry = null;
		 SchoolEntry sEntry = null;
		 LessonEntry lEntry = null;
			 if(entry.getOwner() != null){
				 uEntry = userDao.getUserEntry(entry.getOwner(), Constant.FIELDS);
				 if(uEntry.getSchoolID() != null){
					 sEntry = schoolDao.getSchoolEntry(uEntry.getSchoolID(),Constant.FIELDS);
				 }
			 }
			 if(entry.getRelateId() != null){
				 lEntry = lessionDao.getLessonEntry(entry.getRelateId(), Constant.FIELDS);
			 }
			 List<GoodsDTO> hotList;
			 BuyGoodDetailDTO dto = new BuyGoodDetailDTO(entry, uEntry, lEntry, sEntry);
			 try {
				 hotList = getGoodsDTOList(-1, null, GoodsType.LESSON.getType(), GoodsState.SHELVE.getType(), null, null, 0, 4,1,1);
				 dto.setHotList(hotList);
			} catch (IllegalParamException e) {
				e.printStackTrace();
			}
		 return dto;
	 }
	 
	@Transactional
	public Map withdrawCash(WithdrawCashInfo withdraw) {
		Map map = new HashMap();
		UserEntry entry = userDao.getUserEntry(new ObjectId(withdraw.getUserid()), Constant.FIELDS);
		UserBalanceDTO info = userBalanceDao.getUserBalanceInfo(withdraw.getUserid());
		if (!entry.getNickName().contains(withdraw.getUsername())) {
			map.put("resultCode", 2);
			map.put("mesg", "*  收款人姓名与本用户不符");
			return map;
		}
		if (!entry.getPassword().equals(MD5Util.getMD5String(withdraw.getPassword()))) {
			map.put("resultCode", 1);
			map.put("mesg", "账户密码不正确，请重新输入！");
			return map;
		}
		if (info.getBalance()<withdraw.getCash()) {
			map.put("resultCode", 1);
			map.put("mesg", "账户余额不足！");
			return map;
		}
		IdValuePair userInfo = new IdValuePair(new ObjectId(withdraw.getUserid()), entry.getUserName());
		//(int subject, List<Integer> cloudClassGradeTypes, IdValuePair userInfo, GoodsType type, IdValuePair goodsInfo, int state, long lastUpdateTime, PayType paytype, double price, OrderType orderType, IdValuePair ownerInfo)
		// 生成订单编号
		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
		int r2 = (int) (Math.random() * (10));
		int r3 = (int) (Math.random() * (10));
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		String now = format.format(new Date());
		String newOrder = String.valueOf(now) + String.valueOf(r1) + String.valueOf(r2) + String.valueOf(r3);// 订单号
		OrderEntry e = new OrderEntry(newOrder,-1, null, userInfo, GoodsType.ACCOUNT, new IdValuePair(new BasicDBObject()), OrderState.COMPLETE.getType(), System.currentTimeMillis(), PayType.getPayType(withdraw.getPaytype()), -withdraw.getCash(), OrderType.RECHARGE, new IdValuePair(new BasicDBObject()),0);
		orderDao.addOrderEntry(e);
        userBalanceDao.subMoneyFromBalance(withdraw.getUserid(),withdraw.getCash()*(-1));
        withdrawCashDao.addWithdrawCash(withdraw.buildWithDrawEntry(newOrder));
        map.put("resultCode", 0);
		return map;
	}
	 
	
	@Transactional
	public String addOrUpdateMyExcellentLesson(String lessonId,String goodsid,String stageids,String subjectIds,String gradeIds,String examOutlineIds,double price,long expiretime,String lessonContent,String lessonName,int editType,int isopen,String userId) {
		LessonEntry lesson = null;
		GoodsEntry goodsentry = null;
		ObjectId goodId = null;
		// 开始创建一个新的商品
		String[] subs = subjectIds.split(",");
		String[] grades = gradeIds.split(",");
		List<Integer> subjects = new ArrayList<Integer>();
		List<Integer> cloudClassGradeTypes = new ArrayList<Integer>();
		for (String sub : subs) {
			subjects.add(Integer.parseInt(sub));
		}
		for (String grade : grades) {
			cloudClassGradeTypes.add(Integer.parseInt(grade));
		}
		if(editType == 1){
			lesson = lessionDao.getlesson(lessonId, Constant.FIELDS);
			if (lesson!=null) {
				goodsentry = goodsDao.getGoodsEntryBylessonId(lesson.getID());
				if (goodsentry != null) {
					goodsid = goodsentry.getID().toString();
				}
			}

		} else if(editType == 2){
			goodsentry = goodsDao.getGoodsEntry(new ObjectId(goodsid),Constant.FIELDS);
			lesson = lessionDao.getLessonEntry(goodsentry.getRelateId(), Constant.FIELDS);
		}
		if(goodsentry != null){
			goodsDao.updateGoodEntry(goodsentry.getRelateId(),goodsid,subjects,cloudClassGradeTypes,price,expiretime,lessonContent,isopen,userId);
		}else{
//			DirEntry dir = new DirEntry(new ObjectId(userId), null, null, -1, DirType.EMARKET);
//			ObjectId dirId = dirDao.addDirEntry(dir);
			if(editType == 3){
				lesson = new LessonEntry(lessonName, lessonContent, LessonType.EMARKET_LESSON, new ObjectId(userId), null, null, 0);
			} else {
				lesson = lessionDao.getLessonEntry( new ObjectId(lessonId),null);
				lesson.setSourceId(lesson.getID());
				lesson.setType(LessonType.EMARKET_LESSON.getType());
			}
			ObjectId lessonid = lessionDao.addLessonEntry(lesson);
//			DirEntry dir = new DirEntry(new ObjectId(userId),null,null,0,DirType.EMARKET);
//			dirDao.addDirEntry(dir);
			try {
				GoodsEntry good = new GoodsEntry(subjects, cloudClassGradeTypes, GoodsType.LESSON.getType(), GoodsState.SHELVE.getType(), new ObjectId(userId), lesson.getName(), lessonid,new ObjectId(), price, 0, expiretime,lessonContent,0,isopen);
				goodId = goodsDao.addGoodsEntry(good);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lesson.getID().toString();
	}

	/**
	 *
	 * @param goodid
	 */
	public void deleteGoods(String goodid) {
		goodsDao.deleteGoods(new ObjectId(goodid));

	}

	/**
	 * 查询单条课程信息
	 * @param goodid
	 * @return
	 */
	public Map selSingleLessonDetail(String goodid) {
		Map map = new HashMap();
		GoodsEntry goodentry = goodsDao.getGoodsEntry(new ObjectId(goodid), null);
		ObjectId lessonid = goodentry.getRelateId();
		LessonEntry lesson = lessionDao.getLessonEntry(lessonid, null);
		List<Integer> gradelist = goodentry.getCloudClassGradeTypes();
		List<Integer> subjectlist = goodentry.getSubjects();
		int[] stargArgs = new int[gradelist.size()];
		if (gradelist!=null && gradelist.size()!=0) {
			for (int i=0;i<gradelist.size();i++) {
				stargArgs[i] = GradeType.getGradeType(gradelist.get(i)).getSchoolType();
			}
		}
		if (stargArgs!=null && stargArgs.length!=0) {
			Integer [] targs =  CollectionUtil.getDistinct(stargArgs);
			map.put("stargArgs", targs);
		}
		map.put("subjectArgs", subjectlist.toArray());
		map.put("gradeArgs", gradelist.toArray());
		map.put("price",goodentry.getPrice());
		map.put("desc", goodentry.getDesc());
		map.put("isopen",goodentry.getIsopen());
		map.put("courseName",goodentry.getName());
//		map.put("lessonsInfo", lesson);
		return map;

	}

	/**
	 * 我的过期课程
	 * @param userid
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<OrderDTO> selmyExpireLesson(String userid, int skip,int limit) {
		List<OrderEntry> orderList = goodsDao.selmyExpireLesson(userid,skip,limit);
		List<OrderDTO> orderDTOList = new ArrayList<OrderDTO>();
		if (orderList!=null && orderList.size()!=0) {
			for (OrderEntry order : orderList) {
				GoodsEntry goods = goodsDao.getGoodsEntry(order.getGoodsInfo().getId(),Constant.FIELDS);
//				order.setExpireTime(goods.getExpireTime());
				orderDTOList.add(new OrderDTO(order,goods.getExpireTime()));
			}
		}
		return orderDTOList;
	}

	/**
	 * 数量
	 * @param userid
	 */
	public int selMyExpireLessonCount(String userid) {
		return goodsDao.selmyExpireLessonCount(userid);
	}

	public GoodsEntry reAddLesson(String lessonId) {
		LessonEntry lesson = lessionDao.getlesson(lessonId, Constant.FIELDS);
		GoodsEntry goods = null;
		if (lesson!=null) {
			goods = goodsDao.getGoodsEntryBylessonId(lesson.getID());
		}
		return goods;
	}
}
