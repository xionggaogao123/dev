package com.fulaan.emarket.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fulaan.school.service.SchoolService;
import com.pojo.school.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.emarket.GoodsDao;
import com.fulaan.base.controller.BaseController;
import com.fulaan.emarket.service.GoodsService;
import com.fulaan.emarket.service.OrderService;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.app.SimpleDTO;
import com.pojo.emarket.BuyGoodDetailDTO;
import com.pojo.emarket.GoodsDTO;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.GoodsState;
import com.pojo.emarket.GoodsType;
import com.pojo.emarket.OrderDTO;
import com.pojo.emarket.OrderEntry;
import com.pojo.emarket.OrderState;
import com.pojo.emarket.OrderType;
import com.pojo.emarket.PayType;
import com.pojo.emarket.WithdrawCashInfo;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import sql.dataPojo.UserBalanceInfo;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

/**
 * Created by fourer on 15-2-26.de
 */

@Controller
@RequestMapping("/emarket")
public class EmarketController extends BaseController {
	private static final Logger logger = Logger.getLogger(EmarketController.class);
	
	private GoodsService goodsService = new GoodsService();
	private OrderService orderService = new OrderService();
	private UserService userService = new UserService();
	private SchoolService schoolService = new SchoolService();

	@RequestMapping("/test")
	public String test(){
		System.out.println("this is a test function.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return "/emarket/emarketGoods";
	}
	
	
	
	/**
	 * 获取详细信息
	 * 
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/buyLessonDetail")
	public String buyLessonDetail(@RequestParam("goodId") String goodId, Map<String, Object> model) {
		model.put("buyLessonInfo", goodsService.buyLessonDetail(goodId));
		return "/emarket/courseSaleInfo";
	}
	
	/**
	 * 生成课程订单
	 */
	@RequestMapping("/createLessonOrder")
	public @ResponseBody
	Map generateLessonOrders(@RequestParam(value = "lessonId") String lessonId,
			HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		try {
			//TODO
			String orderId = orderService.createOrder(lessonId,userId);
			map.put("resultCode", 0);
			map.put("orderId", orderId);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		return map;
	}

	
	@RequestMapping("/mainGoodsPage")
	public String mainGoodsPage(Map<String, Object> model){
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
        model.put("styStionList", schoolTypeList);
		return "/emarket/emarketGoods";
	}
	
	/**
	 * 学校种类
	 * @return
	 */
	@RequestMapping("/schooltypes")
	@ResponseBody
	public List<SimpleDTO> getSchoolTypes()
	{
		List<SimpleDTO> list =new ArrayList<SimpleDTO>();
		list.add(SchoolType.PRIMARY.toSimpleDTO());
		list.add(SchoolType.JUNIOR.toSimpleDTO());
		list.add(SchoolType.SENIOR.toSimpleDTO());
		return list;
	}
	
	/**
	 * 基本信息
	 * 
	 * @param stageIds
	 * @param subjectIds
	 * @return
	 */
	@RequestMapping("/baseList")
	public @ResponseBody
	Map selBaseList(@RequestParam("stageIds") String stageIds,
			@RequestParam("subjectIds") String subjectIds) {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] stages = null;
		if (!StringUtils.isEmpty(stageIds)) {
			stages = stageIds.split(",");
		}
		List<GradeType> gradetypelist = new ArrayList<GradeType>();
		List<SubjectType> subjecttypelist = new ArrayList<SubjectType>();
		if(stages==null || stages.length==0) {
			stages = new String[1];
			stages[0] = String.valueOf(SchoolType.PRIMARY.getType());
		}
		for (String stage : stages) {
			gradetypelist.addAll(GradeType.getGradeTypes(Integer.parseInt(stage),-1));
			List<SubjectType> subTypeList = SubjectType.getSubjectTypes(Integer.parseInt(stage));
			for (SubjectType subjectType : subTypeList) {
				if (!subjecttypelist.contains(subjectType)){
					subjecttypelist.add(subjectType);
				}
			}
		}
		List<SimpleDTO> gtList = new ArrayList<SimpleDTO>();
		List<SimpleDTO> stList = new ArrayList<SimpleDTO>();
		for (GradeType gradeType : gradetypelist) {
			gtList.add(gradeType.toSimpleDTO());
		}
		for (SubjectType subjectType : subjecttypelist) {
			stList.add(subjectType.toSimpleDTO());
		}
		map.put("subject", stList);
		map.put("grade", gtList);
		return map;
	}
	
	/**
	 * 获取教师自我介绍
	 * @return
	 */
	@RequestMapping("/introduction")
	public @ResponseBody
	Map selteacherIntroduction(HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		UserDetailInfoDTO user = userService.getUserInfoById(userId);
		String introduction = "";
		String teacherinfo = "";
		if (user != null) {
			introduction = user.getIntroduction();
			teacherinfo = schoolService.findAddressById(user.getSchoolID());
		}
		map.put("introduction", introduction);
		map.put("teacherinfo", teacherinfo);
		return map;
	}
	
	/**
	 * 获取精品课程,我的精品课程,已购买的精品课程
	 * 
	 * @param subjectid
	 * @param gradeid
	 * @param knowledgePointid
	 * @param schoolLevel
	 * @param pageSize
	 * @param keyword
	 * @param ismytype
	 * @param page
	 * @return
	 */
	@RequestMapping("/selLesson")
	public @ResponseBody
	Map selExcellentLesson(@RequestParam("subjectid") String subjectid,
			@RequestParam("gradeid") String gradeid,
			@RequestParam("knowledgePointid") String knowledgePointid,
			@RequestParam("schoolLevel") String schoolLevel,
			@RequestParam("keyword") String keyword,
			@RequestParam("ismytype") int ismytype,
			@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpSession session) {
		int schoolLevelid = 0;
		String userId = getSessionValue().getId();
		if (!StringUtils.isEmpty(schoolLevel)) {
			schoolLevelid = Integer.valueOf(schoolLevel);
		}
		int schoolLevelMax = -1;
		int schoolLevelMin = -1;
		int gradeId = -1;
		int knowledgePointId = -1;
		int subjectId = -1;
		List<Integer> grades =new ArrayList<Integer>();
		int schoolType = -1;
		if (StringUtils.isEmpty(gradeid)) {
//			if (schoolLevelid == 1) {
//				schoolType = 2;
//			} else if (schoolLevelid == 2) {
//				schoolType = 4;
//			} else if (schoolLevelid == 3) {
//				schoolType = 8;
//			}
			grades.addAll(GradeType.getGradeTypeIds(schoolLevelid, Constant.NEGATIVE_ONE));

		} else {
			grades.add(Integer.valueOf(gradeid));
		}
		if (!StringUtils.isEmpty(knowledgePointid)) {
			knowledgePointId = Integer.valueOf(knowledgePointid);
		}
		if (!StringUtils.isEmpty(subjectid)) {
			subjectId = Integer.valueOf(subjectid);
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		int skip;
		if(page < 1){
			skip = 0;
		}else{
			skip = (page - 1) * pageSize;
		}
		Map map = new HashMap();
		List<GoodsDTO> list = null;
		int count;
		try {
			// 获取已购买的课程
			if(ismytype == 2){
//				List<OrderEntry> orderList = orderService.getGoodsDTOList(subjectId, gradeId, new ObjectId(userId), GoodsType.LESSON, OrderState.COMPLETE, OrderType.EXCELLENTLESSON, skip, pageSize);
//				List<ObjectId> oiList = new ArrayList<ObjectId>();
//				for (OrderEntry e : orderList) {
//					oiList.add(e.getGoodsInfo().getId());
//				}
//				list = goodsService.getGoodsDTOList(oiList, GoodsType.LESSON.getType());
//				count = orderService.count(subjectId, gradeId, new ObjectId(userId), GoodsType.LESSON, OrderState.COMPLETE, OrderType.EXCELLENTLESSON);
				list = goodsService.getGoodsDTOList(subjectId, grades, 2, GoodsState.SHELVE.getType(), keyword, new ObjectId(userId), skip, pageSize,1,ismytype);
				count = goodsService.countGoods(subjectId, grades, 2, GoodsState.SHELVE.getType(), keyword, new ObjectId(userId),1,ismytype);
			// 获取我的精品课程
			}else if(ismytype == 1){
				list = goodsService.getGoodsDTOList(subjectId, grades, 1, GoodsState.SHELVE.getType(), keyword, new ObjectId(userId), page, pageSize,2,ismytype);
				count = goodsService.countGoods(subjectId, grades, 1, GoodsState.SHELVE.getType(), keyword, new ObjectId(userId),2,ismytype);
			// 获取所有的精品课程
			}else{
				list = goodsService.getGoodsDTOList(subjectId, grades, 1, GoodsState.SHELVE.getType(), keyword, null, page, pageSize,2,ismytype);
				count = goodsService.countGoods(subjectId, grades, 1, GoodsState.SHELVE.getType(), keyword, null,2,ismytype);
			}
			map.put("rows", list);
			map.put("total", count);
		} catch (IllegalParamException e) {
			e.printStackTrace();
		}
		map.put("page", page);
		map.put("pageSize", pageSize);
		
		return map;
	}
	
	/**
	 * 推送到精品课程
	 * 
	 * @param lessonId
	 * @param stageids
	 * @param subjectIds
	 * @param gradeIds
	 * @param examOutlineIds
	 * @param price
	 * @param expiretime
	 * @param lessonContent
	 * @return
	 */
	@RequestMapping("/addLesson")
	public @ResponseBody
	Map addOrUpdateExcellentLesson(@RequestParam("lessonId") String lessonId,
			@RequestParam("goodid") String goodid,
			@RequestParam("stageids") String stageids,
			@RequestParam("subjectIds") String subjectIds,
			@RequestParam("gradeIds") String gradeIds,
			@RequestParam("examOutlineIds") String examOutlineIds,
			@RequestParam("price") double price,
			@RequestParam("expiretime") int expiretime,
			@RequestParam("lessonContent") String lessonContent,
			@RequestParam("lessonName") String lessonName,
			@RequestParam("editType") int editType,
			@RequestParam("isopen") int isopen, HttpSession session) {
		Map map = new HashMap();
		String userId = getSessionValue().getId();

		try {
			String lessonid = goodsService.addOrUpdateMyExcellentLesson(lessonId,goodid, stageids, subjectIds, gradeIds, examOutlineIds,
				price, expiretime, lessonContent,
						lessonName, editType,
						isopen, userId);
			map.put("lessonId", lessonid);
			map.put("resultCode", 0);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}

		return map;
	}
	
	/**
	 * 获取我的账户订单
	 * 
	 * @return
	 */
	@RequestMapping("/myAccountOrder")
	public String selMyAccountOrder(Map<String, Object> model,
			HttpSession session) {
		String userId = getSessionValue().getId();
		model.put("balance", userService.getUserBalance(userId));
		return "/account/personalAccount";
	}
	
	/**
	 * 账户订单列表
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/accountOrderList")
	public @ResponseBody
	Map accountOrderList(@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, Map<String, Object> model,
			HttpSession session) {
		String userId = getSessionValue().getId();
		if (pageSize == 0) {
			pageSize = 10;
		}
		int skip;
		if(page < 1){
			skip = 0;
		}else{
			skip = (page - 1) * pageSize;
		}
		List<OrderEntry> list = orderService.getGoodsDTOList(-1, -1, new ObjectId(userId), null, OrderState.COMPLETE, OrderType.RECHARGE, skip, pageSize);
		List<OrderDTO> odList = new ArrayList<OrderDTO>();
		for (OrderEntry entry : list) {
			odList.add(new OrderDTO(entry,0));
		}
		int count = orderService.count(-1, -1, new ObjectId(userId), null,OrderState.COMPLETE, OrderType.RECHARGE);
		Map<String,Object> map=new HashMap<String,Object>();
    	map.put("total", count);
    	map.put("page", page);
    	map.put("pageSize", pageSize);
    	map.put("rows", odList);
		return map;
	}
	
	/**
	 * 获取我的余额
	 * 
	 * @return
	 */
	@RequestMapping("/myBalance")
	public String selMyAccountBalance(Map<String, Object> model,
			HttpSession session) {
		String userId = getSessionValue().getId();
		model.put("balance", userService.getUserBalance(userId));
		return "/account/recharge";
	}
	
	/**
	 * 生成充值订单
	 */
	@RequestMapping("/createRecharge")
	public String generateRechargeOrders(
			@RequestParam(value = "payType") int payType,
			@RequestParam(value = "rechargeMoney") double rechargeMoney,
			HttpServletRequest request, HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		String orderId = "";
		try {
			orderId = orderService.generateRechargeOrders(PayType.getPayType(payType), rechargeMoney, userId);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		request.setAttribute("orderid", orderId);
		//TODO
		return "forward:/pay/fzpaypal.do";
//		return "redirect:/emarket/myAccountOrder.do";
	}
	
	/**
	 * 获取我的订单
	 * 
	 * @return
	 */
	@RequestMapping("/myOrder")
	public String selMyOrder() {
		return "/account/myOrder";
	}
	
	/**
	 * 获取我的订单列表
	 * 
	 * @return
	 */
	@RequestMapping("/myOrderList")
	public @ResponseBody
	Map selMyOrderList(@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpSession session) {
		String userId = getSessionValue().getId();
		if (pageSize == 0) {
			pageSize = 10;
		}
		int skip;
		if(page < 1){
			skip = 0;
		}else{
			skip = (page - 1) * pageSize;
		}
		List<OrderEntry> list = orderService.getGoodsDTOList(-1, -1, new ObjectId(userId), null, null, OrderType.EXCELLENTLESSON, skip, pageSize);
		int count = orderService.count(-1, -1, new ObjectId(userId), null, null, OrderType.EXCELLENTLESSON);
		int payCount = orderService.count(-1, -1, new ObjectId(userId), null, OrderState.PAYED, OrderType.EXCELLENTLESSON);
		List<OrderDTO> odList = new ArrayList<OrderDTO>();
		for (OrderEntry entry : list) {
			odList.add(new OrderDTO(entry,0));
		}
		Map<String,Object> map=new HashMap<String,Object>();
    	map.put("total", count);
    	map.put("page", page);
    	map.put("pageSize", pageSize);
    	map.put("rows", odList);
    	map.put("paycount", payCount);
		return map;
	}
	
	/**
	 * 查询订单付钱
	 */
	@RequestMapping("/selLessonOrder")
	public String selLessonOrder(
			@RequestParam(value = "orderId") String orderId,
			Map<String, Object> model, HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		try {
			map = orderService.selLessonOrder(orderId, userId);
			model.put("orderId", map.get("orderId"));
			model.put("lessonName", map.get("lessonName"));
			model.put("createdate", map.get("createdate"));
			model.put("expiretime", map.get("expiretime"));
			model.put("price", map.get("price"));
			model.put("balance", map.get("balance"));
			model.put("resultCode", map.get("resultCode"));
		} catch (Exception e) {
			model.put("resultCode", 1);
		}
		return "/account/paystep";
	}
	
	/**
	 * 取消订单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	public @ResponseBody
	Map cancelOrder(@RequestParam(value = "id") String id) {
		Map map = new HashMap();
		try {
			orderService.cancelOrder(new ObjectId(id));
			map.put("resultCode", 0);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		return map;
	}
	
	/**
	 * 再次购买
	 * @param orderId
	 * @param session
	 * @return
	 */
	@RequestMapping("/rebuyLesson")
	public @ResponseBody
	Map rebuyLesson(@RequestParam("orderId") String orderId, HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		try {
			//TODO
//			String newOrderId = excellentLessonService.rebuyLesson(orderId,
//					user.getId());
			String newOrderId = orderService.rebuyLesson(orderId, userId);
			map.put("newOrderId", newOrderId);
			map.put("resultCode", 0);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		return map;
	}
	
	/**
	 * 提现
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/withDraw")
	public String withDraw(Map<String, Object> model, HttpSession session) {
		String userId = getSessionValue().getId();
		model.put("balance", userService.getUserBalance(userId));
		return "/account/withdraw";
	}

	/**
	 * 提现用户检查
	 * @param username
	 * @return
	 */
	@RequestMapping("/checkuser")
	public @ResponseBody Map checkuser(String username) {
		Map map = new HashMap();
		UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(getSessionValue().getId());
		if (userDetailInfoDTO.getNickName().contains(username)) {
			map.put("flag",true);
		} else {
			map.put("flag",false);
		}
		return map;
	}

	/**
	 * 提现，处理db数据
	 * @param paypalAccount
	 * @param cash
	 * @param password
	 * @param phone
	 * @param username
	 * @param openbank
	 * @param paytype
	 * @param session
	 * @return
	 */
	@RequestMapping("/withdrawCash")
	public @ResponseBody
	Map withdrawCash(@RequestParam(value = "paypalAccount") String paypalAccount,
					 @RequestParam(value = "cash") double cash,
					 @RequestParam(value = "password") String password,
					 @RequestParam(value = "phone") String phone,
					 @RequestParam(value = "username") String username,
					 @RequestParam(value = "openbank") String openbank,
					 @RequestParam(value = "paytype") int paytype,
					 @RequestParam(value="cardnum") String cardnum,
			HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		try {
			WithdrawCashInfo withdraw = new WithdrawCashInfo();
			withdraw.setCash(cash);
			withdraw.setOpenbank(openbank);
			withdraw.setPassword(password);
			withdraw.setUserid(userId);
			withdraw.setPaypalAccount(paypalAccount);
			withdraw.setPhone(phone);
			withdraw.setUsername(username);
			withdraw.setPaytype(paytype);
			withdraw.setCardnum(cardnum);
			map = goodsService.withdrawCash(withdraw);
		} catch (Exception e) {
			map.put("resultCode", 1);
			map.put("mesg", "提取余额失败，请重新提取！");
		}
		return map;
	}
	
	/**
	 * 获取我的过期课程
	 * 
	 * @return
	 */
	@RequestMapping("/myExpireLesson")
	public String selmyExpireLesson() {
		return "/account/overdue";
	}
	
	/**
	 * 获取我的过期课程列表
	 *
	 * @return
	 */
	@RequestMapping("/myExpireLessonList")
	public @ResponseBody
	Map selmyExpireLessonList(@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpSession session) {
		Map<String,Object> map = new HashMap<String, Object>();
		String userid = getSessionValue().getId();
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		int skip;
		if(page < 1){
			skip = 0;
		}else{
			skip = (page - 1) * pageSize;
		}
		List<OrderDTO> list = goodsService.selmyExpireLesson(userid,
				skip,pageSize);
		int count = goodsService.selMyExpireLessonCount(userid);
		map.put("rows", list);
		map.put("total", count);
		map.put("pageSize", pageSize);
		map.put("page", page);
		return map;
	}
	
	/**
	 * 精品课程首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/saleList")
	public String saleList(Map<String, Object> model) {
		return "/account/saleList";
	}
	
	/**
	 * 查询销售订单
	 * @param lessonname
	 * @param username
	 * @param starttime
	 * @param endtime
	 * @param paymentStatus
	 * @param page
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping("/saleListInfo")
	@ResponseBody
	public Map saleListInfo(@RequestParam("lessonname") String lessonname,
			@RequestParam("username") String username,
			@RequestParam("starttime") String starttime,
			@RequestParam("endtime") String endtime,
			@RequestParam("paymentStatus") int paymentStatus,
			@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = getSessionValue().getId();
		if (pageSize == 0) {
			pageSize = 20;
		}
		int skip;
		if(page < 1){
			skip = 0;
		}else{
			skip = (page - 1);
		}
		long start = 0, end = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(null != starttime && starttime.length() != 0){
				start = format.parse(starttime).getTime();
			}
			if(null != endtime && endtime.length() != 0){
				end = format.parse(endtime).getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OrderState state = null;
		if(0 != paymentStatus){
			state = OrderState.getOrderState(paymentStatus);
		}
		List<OrderDTO> list = orderService.getSaleOrderList(userId, lessonname, start, end, username, state, skip, pageSize);
		int count = orderService.count(userId, lessonname, start, end, username, state);
		map.put("total", count);
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("rows", list);
		return map;
	}

	/**
	 * 更新老师介绍
	 *
	 * @return
	 */
	@RequestMapping("/updateIntroduction")
	public @ResponseBody
	Map updateTeacherInfo(@RequestParam("introduce") String introduce,
						  HttpSession session) {
		Map map = new HashMap();
		String userId = getSessionValue().getId();
		try {
			userService.updateIntroduction(introduce, userId);
			map.put("resultCode", 0);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		return map;
	}

	/**
	 * 通过自己账户支付
	 *
	 * @param password
	 * @param orderId
	 * @return0
	 */
	@RequestMapping("/payLessonByBlance")
	public @ResponseBody
	Map payLessonByBlance(@RequestParam("password") String password,
						  @RequestParam("orderId") String orderId, HttpSession session) {
		String userId = getSessionValue().getId();
		Map map = new HashMap();
		try {
			map = orderService.payLessonByBlance(password, orderId,
					userId);
		} catch (Exception e) {
			map.put("resultCode", 1);
			map.put("mesg", "付款失败！");
		}

		return map;
	}

	/**
	 * 完成支付页面
	 *
	 * @param lessonName
	 * @param model
	 * @return
	 */
	@RequestMapping("/orderFinish")
	public String orderFinish(@RequestParam("lessonName") String lessonName,
							  Map<String, Object> model) {
		model.put("lessonName", lessonName);
		return "/account/payfinish";
	}

	/**
	 * 删除我的精品课程
	 *
	 * @param goodid
	 * @return
	 */
	@RequestMapping("/deleteMyExcellentLesson")
	public @ResponseBody
	Map deleteMyExcellentLesson(@RequestParam("goodid") String goodid) {
		Map map = new HashMap();
		try {
			goodsService.deleteGoods(goodid);
			map.put("resultCode", 0);
		} catch (Exception e) {
			map.put("resultCode", 1);
		}
		return map;
	}

	/**
	 * 获取课程详细信息
	 *
	 * @param goodid
	 * @return
	 */
	@RequestMapping("/selSingleLesson")
	public @ResponseBody
	Map selSingleLesson(@RequestParam("goodid") String goodid) {
		return goodsService.selSingleLessonDetail(goodid);
	}


	/**
	 * 在次推送课程
	 *
	 * @return
	 */
	@RequestMapping("/reAddLesson")
	public @ResponseBody
	Map reAddLesson(@RequestParam("lessonId") String lessonId) {
		Map map = new HashMap();
		GoodsEntry goods = goodsService.reAddLesson(lessonId);
		if (goods != null) {
			map.put("resultCode", 1);
		} else {
			map.put("resultCode", 0);
		}
		return map;

	}
}
