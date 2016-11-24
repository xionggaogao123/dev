package com.fulaan.emarket.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.emarket.service.GoodsService;
import com.fulaan.emarket.service.OrderService;
import com.fulaan.learningcenter.service.LessonService;
import com.google.common.base.Splitter;
import com.pojo.app.FieldValuePair;
import com.pojo.emarket.Comment;
import com.pojo.emarket.GoodsDTO;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.GoodsState;
import com.pojo.emarket.GoodsType;
import com.pojo.emarket.OrderEntry;
import com.pojo.emarket.OrderState;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.school.GradeType;
import com.pojo.school.SubjectType;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
/**
 * 商品controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {

	private static final Logger logger =Logger.getLogger(GoodsController.class);
	
	private GoodsService goodsService =new GoodsService();
	private OrderService orderService =new OrderService();
	private LessonService lessonService =new LessonService();
	
	/**
	 * 得到商品数目
	 * @param sub 科目ID
	 * @param gradeType 年级
	 * @param type 类型
	 * @param state 状态
	 * @param name 名称
	 * @param owner 拥有者
	 * @return
	 * @throws IllegalParamException 
	 */
	
	@RequestMapping("/count")
	@ResponseBody
	public RespObj count(int sub,int gradeType,int type,int state, String name ,ObjectId owner) throws IllegalParamException
	{
		int count=goodsService.countGoods(sub, null, type, state, name, owner,1,1);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE, count);
		return obj;
	}
	
	
	/**
	 * 得到商品列表
	 * @param sub 科目ID
	 * @param gradeType 年级
	 * @param type 类型
	 * @param state 状态
	 * @param name 名称
	 * @param owner 拥有者
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<GoodsDTO> getGoodsDTOList(int sub,int gradeType,int type,int state,String name,ObjectId owner,int skip,int limit) throws IllegalParamException
	{
		return goodsService.getGoodsDTOList(sub, null, type, state, name, owner, skip, limit,1,1);
	}
	
	/**
	 * 删除该商品；逻辑删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RespObj deleteGoods(@ObjectIdType  ObjectId id)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		GoodsEntry ge=goodsService.getGoodsEntry(id);
		if(null==ge)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!ge.getOwner().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		logger.info("delete goods!user:"+getUserId()+";goods:"+ge.getBaseEntry());
		goodsService.update(id, new FieldValuePair("st",GoodsState.DELETE.getType()));
		return obj;
	}
	
	
	/**
	 * 得到已经买过商品的数量
	 * @param sub 科目ID
	 * @param gradeType 年级ID
	 * @param type 商品类型
	 * @param state 商品状态
	 * @return
	 */
	@RequestMapping("/payed/count")
	@ResponseBody
	public RespObj countPayedGoods(int sub,int gradeType,int type,int state)
	{
		int count =orderService.count(sub, gradeType, getUserId(), GoodsType.getGoodsType(type), OrderState.getOrderState(state), null);
		return new RespObj(Constant.SUCCESS_CODE, count);
	}
	
	/**
	 * 查询我购买的商品
	 * @param sub 科目ID
	 * @param gradeType 年级ID
	 * @param type 商品类型
	 * @param state 商品状态
	 * @param skip
	 * @param limit
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/payed/list")
	@ResponseBody
	public  List<GoodsDTO> payedGoodsList(int sub, int gradeType,int type,int state,int skip,int limit) throws IllegalParamException
	{
		List<OrderEntry> orderList=orderService.getGoodsDTOList(sub, gradeType, getUserId(), GoodsType.getGoodsType(type), OrderState.getOrderState(state), null, skip, limit);
		List<ObjectId> goodsId =MongoUtils.getFieldObjectIDs(orderList,"gi");
		return goodsService.getGoodsDTOList(goodsId, type);
	}
	
	/**
	 * 添加一个商品评论
	 * @param comment
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/comment")
	@ResponseBody
	public RespObj addComment(String comment,@ObjectIdType ObjectId goodsId)
	{
		RespObj obj= new RespObj(Constant.FAILD_CODE);
		if(!ValidationUtils.isRequestGoodsComment(comment))
		{
			obj.setMessage("评论不合法");
			return obj;
		}
		
		OrderEntry oe=orderService.getOrderEntry(getUserId(), goodsId);
		if(null==oe)
		{
			obj.setMessage("没有购买该商品，不能评论");
			return obj;
		}
		
		if(System.currentTimeMillis()-oe.getLastUpdateTime()>Constant.MS_SECONDS_IN_MONTH*6) //6个月内可以评论
		{
			obj.setMessage("时间已经6个月，不可以评论");
			return obj;
		}
		
		Comment commentEntry =new Comment(getUserId(), comment);
		goodsService.addComment(goodsId, commentEntry);
		return obj;
	}
	
	

	/**
	 * 需要老师权限
	 * 添加一个课程商品
	 * @param sub 科目信息 ;暂时只能选一门科目
	 * @param ccgts 年级信息
	 * @param price 价格
	 * @param lessonName 课程名称
	 * @param intro 简介
	 * @return
	 */
	public RespObj addLessonGoods(int sub,String ccgts,int price,String lessonName,String intro, int expireTime)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		SubjectType subject=SubjectType.getSubjectType(sub);
		if(null==subject)
		{
			obj.setMessage("科目错误");
			return obj;
		}
		List<Integer> gradeList =new ArrayList<Integer>();
		Iterable<String> result = Splitter.on(Constant.COMMA)
			       .omitEmptyStrings()
			       .split(ccgts); 
		for(String subStr:result)
		{
			if(null==GradeType.getGradeType(Integer.parseInt(subStr)))
			{
				obj.setMessage("年级参数错误");
				return obj;
			}
			gradeList.add(Integer.parseInt(subStr));
		}
		
		if(!ValidationUtils.isRequestLessonName(lessonName))
		{
			obj.setMessage("课程名错误");
			return obj;
		}
		
		if(!ValidationUtils.isRequestLessonIntro(intro))
		{
			obj.setMessage("课程介绍不合要求");
			return obj;
		}
		LessonEntry lessonentry =new LessonEntry(lessonName, intro, LessonType.EMARKET_LESSON, getUserId(), null, null,0);
		ObjectId lessionId=lessonService.addLessonEntry(lessonentry);
        List<Integer> subs = new ArrayList<Integer>();
        subs.add(subject.getId());
//		GoodsEntry ge =new GoodsEntry(subs, gradeList, GoodsType.LESSON.getType(), GoodsState.SHELVE.getType(), getUserId(), lessonName, lessionId, price, Constant.ZERO, expireTime,0);
//		ObjectId goodsId=goodsService.addGoods(ge);
//		obj =new RespObj(Constant.SUCCESS_CODE, goodsId.toString());
		return obj;
	}
	
	
	
	
	
}
