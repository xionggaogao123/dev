package com.fulaan.calendar.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.calendar.service.EventService;
import com.fulaan.calendar.service.LoopEventService;
import com.fulaan.homeschool.service.NoticeService;
import com.fulaan.user.service.UserService;
import com.google.common.base.Joiner;
import com.pojo.calendar.Event;
import com.pojo.calendar.EventAndCountDTO;
import com.pojo.calendar.EventDTO;
import com.pojo.calendar.EventLoopType;
import com.pojo.calendar.LoopEvent;
import com.pojo.calendar.MiddleDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;

/**
 * 日历controoller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(CalendarController.class);

	/**
	 * 天的最多循环次数
	 */
	private static final int DAY_MAX_LOOP_COUNT = 365 * 5;
	/**
	 * 周的最大循环次数
	 */
	private static final int WEEK_MAX_LOOP_COUNT = 5 * 12 * 5;
	/**
	 * 月的最大循环次数
	 */
	private static final int MONTH_MAX_LOOP_COUNT = 12 * 5;
	/**
	 * 年的最大循环次数
	 */
	private static final int YEAR_MAX_LOOP_COUNT = 5;
	/**
	 * 页面最大宽度
	 */
	private static final int MAX_PAGE_CSS_WIDTH = 810;
	/**
	 * 周历最大宽度
	 */
	private static final int WEEK_MAX_PAGE_CSS_WIDTH = 115;
	
	/**
	 * 页面最大高度
	 */
	private static final int MAX_PAGE_CSS_HEIGHT = 66 * 24;
	/**
	 * 最小日历高度
	 */
	private static final int MIN_CALENDAR_HEIGHT = 20;


	private EventService eventService=new EventService();
	private LoopEventService loopEventService=new LoopEventService();
	private NoticeService noticeService =new NoticeService();
	private UserService userService =new UserService();
	/**
	 * 加載天日历页面
	 * 
	 * @param session
	 * @param date
	 *            2015-01-12
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/day")
	public String loadDayPage(
			@RequestParam(required = false) String date, Model model)
			throws Exception {
		try {
			String dateStr = StringUtils.isBlank(date) ? DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD): date;
			long beginTime = DateTimeUtils.stringToDate(dateStr,
					DateTimeUtils.DATE_YYYY_MM_DD).getTime();
			long endTime = beginTime + Constant.MS_IN_DAY - 1L;
			model.addAttribute("bt", beginTime);
			model.addAttribute("et", endTime);
			model.addAttribute("dateStr", dateStr);
		} catch (Exception ex) {
			logger.error("", ex);
			throw ex;
		}
		return "calendar/day_calender";
	}

	/**
	 * 获得天历数据
	 * 
	 * @param session
	 * @param day
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/day/data")
	@ResponseBody
	public List<EventDTO> getEventDTOList( String day)
			throws Exception {

		String dateStr = StringUtils.isBlank(day) ? DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD): day;
		long beginTime = DateTimeUtils.stringToDate(dateStr,
				DateTimeUtils.DATE_YYYY_MM_DD).getTime();
		long endTime = beginTime + Constant.MS_IN_DAY - 1L;

		List<EventDTO> list = new ArrayList<EventDTO>();
		try {
			list = getEventDTOList(getUserId(), beginTime, endTime);
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

	/**
	 * 加載周历页面
	 * 
	 * @param session
	 * @param date
	 *            2015-01-12
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/week")
	public String loadWeekPage(
			@RequestParam(required = false) String date, Model model)
			throws Exception {
		try {
			Long[] times=DateTimeUtils.getWeekMinAndMaxTime(date);
			model.addAttribute("bt", DateTimeUtils.convert(times[Constant.ZERO],DateTimeUtils.DATE_YYYY_MM_DD));
			model.addAttribute("et", DateTimeUtils.convert(times[Constant.ONE],DateTimeUtils.DATE_YYYY_MM_DD));
		} catch (Exception ex) {
			logger.error("", ex);
			throw ex;
		}
		return "calendar/week_calender";
	}

	/**
	 * 获得周历数据
	 * 
	 * @param session
	 * @param day
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/week/data")
	@ResponseBody
	public Map<String,List<EventDTO>>  getWeekEventDTOList( String bt,String et)
			throws Exception {
		long beginTime=DateTimeUtils.stringToDate(bt,DateTimeUtils.DATE_YYYY_MM_DD).getTime();
		long endTime=DateTimeUtils.stringToDate(et,DateTimeUtils.DATE_YYYY_MM_DD).getTime()+Constant.MS_IN_DAY-1;
		Map<String,List<EventDTO>> map=	getEventDTOMap(getUserId(), beginTime, endTime);
		return map;
	}
	
	/**
	 * 月历
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/month")
	public String loadMonthCalendar(
			@RequestParam(required = false) String date, Model model)
			throws Exception {
		try {
			String dateStr = StringUtils.isBlank(date) ? DateTimeUtils
					.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM) : date;
			model.addAttribute("dateStr", dateStr);
		} catch (Exception ex) {
			logger.error("", ex);
			throw ex;
		}
		return "calendar/month_calender";
	}

	/**
	 * 月历数据
	 * @param session
	 * @param date
	 * @param isAllEvent 是否需要全部事件，0不需要 1需要
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/month/data")
	@ResponseBody
	public List<EventAndCountDTO> getMonthEventsData(@RequestParam(required=false) String date,@RequestParam(required=false,defaultValue="0") int isAllEvent) throws Exception {
		List<EventAndCountDTO> retList =new ArrayList<EventAndCountDTO>();
		try {
			Long[] times = DateTimeUtils.getMonthMinAndMaxTime(date);
			List<EventDTO> list = new ArrayList<EventDTO>();
			try {
				list = getEventDTOList(getUserId(), times[0], times[1]);
			} catch (Exception e) {
				logger.error("", e);
			}
			Map<String,EventAndCountDTO> map =new HashMap<String, EventAndCountDTO>();
		
			String time;
			EventAndCountDTO eventAndCountDTO;
			if(null!=list && list.size()>0)
			{
				for(EventDTO dto:list)
				{
					time =DateTimeUtils.convert(dto.getOrigBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD);
					if(!map.containsKey(time))
					{
						eventAndCountDTO=new EventAndCountDTO(time,dto);
						retList.add(eventAndCountDTO);
						map.put(time, eventAndCountDTO );
					}
					else
					{
						if(isAllEvent==0)
						{
							if(map.get(time).getList().size()==1)
							{
								map.get(time).getList().add(dto);
							}
							else
							{
								map.get(time).increaseCount();
							}
						}
						else
						{
							map.get(time).getList().add(dto);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return retList;
	}
	
	/**
	 * 月历数据,只查找哪天有事件
	 * 
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/month/data1")
	@ResponseBody
	public String getMonthEventsData1(@RequestParam(required=false) String date,@RequestParam(required=false) Integer userId) throws Exception {
//		ObjectId uid=getUserId();
//    	String cacheKey=null ;
//    	
//		Set<String> hashSet =new HashSet<String>();
//		try {
//			ObjectId uId =getUserId();
//			Long[] times = DateTimeUtils.getMonthMinAndMaxTime(date);
//			String yearMonth=DateTimeUtils.convert(times[1], DateTimeUtils.DATE_YYYY_MM);
//			
//			cacheKey=CacheHandler.getKeyValue(CacheHandler.CACHE_USER_CALENDAR,String.valueOf(uid),yearMonth);
//			String value=CacheHandler.getStringValue(cacheKey);
//	    	if(null!=value)
//	    	{
//	    		return value;
//	    	}
//			List<EventDTO> list = new ArrayList<EventDTO>();
//			try {
//				list = getEventDTOList(uId, times[0], times[1]);
//			} catch (Exception e) {
//				logger.error("", e);
//			}
//		
//			String time;
//			
//			if(null!=list && list.size()>0)
//			{
//				for(EventDTO dto:list)
//				{
//					time =DateTimeUtils.convert(dto.getOrigBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD);
//					
//					String str=time.substring(Constant.EIGHT);
//					if(str.startsWith("0"))
//					{
//						str=str.substring(Constant.ONE);
//					}
//					if(!hashSet.contains(str))
//					{
//						hashSet.add(str);
//					}
//				}
//			}
//			
//		} catch (Exception ex) {
//			logger.error("", ex);
//		}
//		
//		String retStr=Joiner.on(Constant.COMMA).join(hashSet);
//		CacheHandler.cache(cacheKey, retStr, Constant.FIVE_HUNDRED);
		return "";
	}
	/**
	 * 周历列表
	 * 
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public String loadWeekListCalendar(@RequestParam(required=false) String date, Model model) throws Exception {
		try {
			Long[] times=DateTimeUtils.getWeekMinAndMaxTime(date);
			model.addAttribute("bt", DateTimeUtils.convert(times[Constant.ZERO],DateTimeUtils.DATE_YYYY_MM_DD));
			model.addAttribute("et", DateTimeUtils.convert(times[Constant.ONE],DateTimeUtils.DATE_YYYY_MM_DD));
		} catch (Exception ex) {
			logger.error("", ex);
			throw ex;
		}
		return "calendar/list_calender";
	}
	
	/**
	 * 列表数据
	 * 
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/list/data")
	@ResponseBody
	public List<EventAndCountDTO> getListEventsData(String bt,String et) throws Exception {
		
		List<EventAndCountDTO> retList =new ArrayList<EventAndCountDTO>();
		try {
			long beginTime=DateTimeUtils.stringToDate(bt,DateTimeUtils.DATE_YYYY_MM_DD).getTime();
			long endTime=DateTimeUtils.stringToDate(et,DateTimeUtils.DATE_YYYY_MM_DD).getTime()+Constant.MS_IN_DAY-1000;
			List<EventDTO> list = new ArrayList<EventDTO>();
			try {
				list = getEventDTOList(getUserId(), beginTime, endTime);
			} catch (Exception e) {
				logger.error("", e);
			}
			Map<String,EventAndCountDTO> map =new HashMap<String, EventAndCountDTO>();
			String time;
			EventAndCountDTO eventAndCountDTO;
			if(null!=list && list.size()>0)
			{
				for(EventDTO dto:list)
				{
					time=DateTimeUtils.convert(dto.getOrigBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD);
					if(!map.containsKey(time))
					{
						eventAndCountDTO=new EventAndCountDTO(time,dto);
						retList.add(eventAndCountDTO);
						map.put(time, eventAndCountDTO );
					}
					else
					{
					  map.get(time).getList().add(dto);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return retList;
	}
	
	/**
	 * 得到详情
	 * 
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	@ResponseBody
	public RespObj getEvent(String id) {
		RespObj obj = RespObj.FAILD;

		if (null== id || !ObjectId.isValid(id)) {
			obj.setMessage("非法参数");
			return obj;
		}

		Event e = eventService.getEvent(getUserId(), new ObjectId(id));
		if (null == e) {
			e = loopEventService.getLoopEvent(getUserId(), new ObjectId(id));
		}
		if(null==e)
		{
			e=noticeService.getNoticeEvent(new ObjectId(id));
		}
		if (null == e) {
			logger.error("Can not find event;The id:" + id + "; the user is "
					+ getUserId());
			obj.setMessage("没有找到该日历");
			return obj;
		}
		/**
		 * 辨别是不是循环事件
		 */
		int isl = 0;
		if (e instanceof LoopEvent) {
			isl = 1;
		}
		e.getBaseEntry().append("isl", isl);
		/**
		 * 适配移动端接口
		 */
		e.getBaseEntry().append("strId",e.getID().toString());
		obj = new RespObj(Constant.SUCCESS_CODE, e.getBaseEntry());
		return obj;
	}

	/**
	 * 添加一个事件
	 * 
	 * @param session
	 * @param type
	 * @param title
	 * @param content
	 * @param beginTime
	 * @param endTime
	 * @param lp
	 * @param dv
	 * @param edt
	 * @param edv
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public RespObj addEvent( String type, String title,
			String content, String beginTime, String endTime, String lp,
			String dv, String edt, String edv, String operId)

	{
		RespObj obj = RespObj.FAILD;

		int intType;
		try {
			intType = Integer.parseInt(type);
			if (intType != Constant.ONE && intType != Constant.TWO) {
				throw new Exception();
			}
		} catch (Exception ex) {
			obj.message = "参数错误";
			return obj;
		}

		if (StringUtils.isBlank(title) && StringUtils.isBlank(content)) {
			obj.message = "内容为空";
			return obj;
		}

		if (title.length() > 30) {
			obj.message = "标题不超过30字";
			return obj;
		}

		long lBeginTime;
		long lEndTime;
		try {
			lBeginTime = Long.valueOf(beginTime);
			lEndTime = Long.valueOf(endTime);
			if (lBeginTime >= lEndTime) {
				throw new Exception();
			}
		} catch (Exception ex) {
			obj.message = "日历时间错误";
			return obj;
		}
		// -1 表示不循环
		int intLp;
		try {
			intLp = Integer.parseInt(lp);
			if (intLp != Constant.ONE && intLp != Constant.TWO
					&& intLp != Constant.THREE && intLp != Constant.FOUR
					&& intLp != -1) {
				throw new Exception();
			}
			if(intLp>0)
			{
				if(!DateTimeUtils.convert(lBeginTime,DateTimeUtils.DATE_YYYY_MM_DD).equals(DateTimeUtils.convert(lEndTime,DateTimeUtils.DATE_YYYY_MM_DD)))
				{
					obj.message = "抱歉,跨天日历不可以重复哦！";
					return obj;
				}
			}
			
		} catch (Exception ex) {
			obj.message = "参数错误";
			return obj;
		}

		EventLoopType elp = null;

		if (intLp > Constant.ZERO) {
			int intDv;
			try {
				intDv = Integer.parseInt(dv);
			} catch (Exception ex) {
				obj.message = "参数错误";
				return obj;
			}

			// String edt,String edv
			int intEdt;
			long longEdv = -1L;

			try {
				intEdt = Integer.valueOf(edt);
				if (intEdt != Constant.ZERO && intEdt != Constant.ONE
						&& intEdt != Constant.TWO) {
					throw new Exception();
				}
				if (intEdt == Constant.ZERO) {
					longEdv = 0L;
				}
				if (intEdt == Constant.ONE || intEdt == Constant.TWO) {
					longEdv = Long.valueOf(edv);
				}
			} catch (Exception ex) {
				obj.message = "参数错误";
				return obj;
			}
			elp = new EventLoopType(intLp, intDv, intEdt, longEdv);
		}


		ObjectId ui =getUserId();

		if (StringUtils.isNotBlank(operId)) {
			if (null==operId || !ObjectId.isValid(operId)) {
				obj.message = "参数错误";
				return obj;
			}
			loopEventService.remove(ui, new ObjectId(operId));
			eventService.removeEvent(ui, new ObjectId(operId));
		}
		
		
		
		ObjectId insert=null;
		if (null != elp) // 循环事件 
		{
			LoopEvent e = new LoopEvent(ui, intType, title, content,
					lBeginTime, lEndTime, elp);
			logger.info(e.getBaseEntry());
			insert=loopEventService.addLoopEvent(e);
		} else {
			Event e = new Event(ui, intType, title, content,
					lBeginTime, lEndTime);
			logger.info(e.getBaseEntry());
			insert=eventService.addEvent(e);
		}
		obj =new RespObj(Constant.SUCCESS_CODE, insert.toString());
		return obj;
	}
	/**
	 * 删除事件
	 * 
	 * @param session
	 * @param operId
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public RespObj delEvent(@ObjectIdType ObjectId operId) {
		logger.info("the user delete event;user=" + getUserId() + " ;event="
				+ operId);
		eventService.removeEvent(getUserId(), operId);
		return RespObj.SUCCESS;
	}

	/**
	 * 删除循环事件
	 * 
	 * @param session
	 * @param operId
	 * @return
	 */
	@RequestMapping("/del1")
	@ResponseBody
	public RespObj delEvent( String operId, int type,
			String bt) {
		RespObj obj = RespObj.FAILD;
		if (null==operId || !ObjectId.isValid(operId)) {
			obj.message = "参数错误";
			return obj;
		}
		
		logger.info("the user delete loop event;user=" +getUserId()
				+ " ;event=" + operId);
		long beginTime = DateTimeUtils.stringToDate(bt,
				DateTimeUtils.DATE_YYYY_MM_DD).getTime()+Constant.MS_IN_30_MINUTE; //删除当天的事件 
	
		LoopEvent	e = loopEventService.getLoopEvent(getUserId(), new ObjectId(operId));
		// *  ret:-1不删除 0 只删除当天 1 删除当天即以后
		  //  reb:操作时间 ,默认为“” 删除的时候是2255555555L值
		if(null!=e)
		{
			
			if(type==0)
			{
				loopEventService.addDeleteDate(bt, getUserId(), new ObjectId(operId));
			}
			else
			{
				loopEventService.updateRemoves(getUserId(), new ObjectId(operId),beginTime);
			}
		}
		else
		{
			eventService.removeEvent(getUserId(), new ObjectId(operId));
		}
		
		return RespObj.SUCCESS;
	}

	
	/**
	 * 得到Event list,并且转化为EventDTO map ,key:2014-01-14
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private Map<String,List<EventDTO>> getEventDTOMap(ObjectId userId, long beginTime,long endTime) throws Exception
	{
		Map<String,List<EventDTO>> retMap=new HashMap<String, List<EventDTO>>();
		Map<String,List<Event>> eventMap=new HashMap<String, List<Event>>();
		List<Event> eventList = getEventList(userId,beginTime,endTime);
		String date;
		for(Event e:eventList)
		{
			date=DateTimeUtils.convert(e.getBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD);
			if(!eventMap.containsKey(date))
			{
				eventMap.put(date, new ArrayList<Event>() );
			}
			eventMap.get(date).add(e);
		}
		
		for(Map.Entry<String,List<Event>> entry:eventMap.entrySet())
		{
			if(entry.getValue().size()>0)
			{
			  retMap.put(entry.getKey(), toEventDTOsForDayShows(entry.getValue(),true));
			}
		}
		return retMap;
	}
	/**
	 * 得到Event list,并且转化为EventDTO list
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private List<EventDTO> getEventDTOList(ObjectId userId, long beginTime,
			long endTime) throws Exception {
		List<Event> eventList = getEventList(userId,beginTime,endTime);
		return toEventDTOsForDayShows(eventList,false);
	}
	
	/**
	 * 得到Event list
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Event> getEventList(ObjectId userId, long beginTime,
			long endTime)
	{
		List<Event> retList =new ArrayList<Event>();
		List<Event> eventList = eventService.getEventList(userId, beginTime,endTime);
		
		try{
			ObjectId childId=null;
			if(UserRole.isParent(getSessionValue().getUserRole()))
			{
				UserEntry ue=userService.searchUserId(getUserId());
				childId=ue.getConnectIds().get(0);
			}
		 List<Event> noticeEventList=noticeService.getNoticeEvent(getSessionValue().getUserRole(), userId,new ObjectId(getSessionValue().getSchoolId()) ,childId, beginTime, endTime, Constant.ZERO, Constant.HUNDRED);
		 eventList.addAll(noticeEventList);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		
		retList.addAll(convertOneDayEvent(eventList,beginTime,endTime));
		List<LoopEvent> loopEventList = loopEventService.getLoopEventList(
				userId, endTime);
		if (!loopEventList.isEmpty()) {
			List<Event> eventList2 = getEvent(loopEventList, beginTime, endTime);
			
			if (!eventList2.isEmpty()) {
				//因为循环时间不垮天，没必要转化
				//retList.addAll(convertOneDayEvent(eventList2,beginTime,endTime));
				retList.addAll(eventList2);
			}
		}
		return retList;
	}
	/**
	 * 将跨天的时间转变成单天的事件
	 * @param eventList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Event> convertOneDayEvent(List<Event> eventList, long beginTime,long endTime)
	{
		List<Event> retList =new ArrayList<Event>();
		for(Event event:eventList)
		{
			Event newEvent;
			long beg=event.getBeginTime();
			long end=DateTimeUtils.getDayMaxTime(beg);
			
			if(event.getEndTime()<end) //不是跨天的事件
			{
				 retList.add(event);
			}
			else
			{
				while(true)
				{
					if(end>=event.getEndTime())
					{
						end=event.getEndTime();
						
						newEvent =new Event(event.getUid(), event.getType(), event.getTitle(), event.getContent(), beg, event.getEndTime()) ;
						newEvent.setID(new ObjectId(new Date(beg)));
						newEvent.getBaseEntry().append("orgId", event.getID());
						if(isRequireEvent(beginTime, endTime, beg, end))
						{
						   retList.add(newEvent);
						}
						break;
					}
					else
					{
						newEvent =new Event(event.getUid(), event.getType(), event.getTitle(), event.getContent(), beg, end) ;
						newEvent.setID(new ObjectId(new Date(beg)));
						newEvent.getBaseEntry().append("orgId", event.getID());
						if(isRequireEvent(beginTime, endTime, beg, end))
						{
						   retList.add(newEvent);
						}
						beg =end+1000;
						end=beg+Constant.MS_IN_DAY-1000;
						if(beg>=endTime)
						{
							break;
						}
					}
				}
			}
		}
		return retList;
	}
	/**
	 * 转变为每天显示的日历，并且计算页面css位置
	 * 
	 * @param isWeek
	 * @param list
	 * @return
	 */
	private List<EventDTO> toEventDTOsForDayShows(List<Event> list,boolean isWeek) {
		List<EventDTO> dtoList = new ArrayList<EventDTO>();

		/**
		 * 先按照时间先后顺序排序
		 */
		Collections.sort(list, new Comparator<Event>() {
			@Override
			public int compare(Event arg0, Event arg1) {
				long a = 0;
				try {
					a = arg0.getID().getTime()
							- arg1.getID().getTime();
				} catch (Exception ex) {
				}
				if(a>0) return Constant.ONE;
				if(a<0) return Constant.NEGATIVE_ONE;
				return Constant.ZERO;
			}
		});

		List<MiddleDTO> middleDTOList = new ArrayList<MiddleDTO>();

		Map<ObjectId, MiddleDTO> middleDTOMap = new HashMap<ObjectId, MiddleDTO>();
		/**
		 * key:为新加入的 value:受到影响的
		 */
		Map<ObjectId, ObjectId> yingxiangMap = new HashMap<ObjectId, ObjectId>();

		MiddleDTO lastInsertDTO = null;

		MiddleDTO middleDTO = null;

		long lastBeginTime;
		long lastEndTime;
		ObjectId lastId;

		long thisBeginTime;
		int newCount;
		int newLeft;
		ObjectId newLastId;
		for (Event e : list) {
			middleDTO = new MiddleDTO(e);
			middleDTOMap.put(e.getID(), middleDTO);
			middleDTOList.add(middleDTO);

			if (null == lastInsertDTO)// 第一次放入
			{
				lastInsertDTO = middleDTO;
			} else // 一天多个事件，
			{
				lastBeginTime = lastInsertDTO.getEvent().getBeginTime();
				lastEndTime = lastInsertDTO.getEvent().getEndTime();
				thisBeginTime = e.getBeginTime();

				if (thisBeginTime >= lastBeginTime
						&& thisBeginTime < lastEndTime)// 有交叉
				{

					lastId = lastInsertDTO.getEvent().getID();
					newCount = middleDTOMap.get(lastId).getCount() + 1;
					newLeft = middleDTOMap.get(lastId).getLeft() + 1;

					middleDTO.setCount(newCount);
					middleDTO.setLeft(newLeft);

					lastInsertDTO.setCount(newCount);

					newLastId = lastId;
					while (true) {
						newLastId = yingxiangMap.get(newLastId);
						if (null == newLastId) {
							break;
						}
						middleDTOMap.get(newLastId).setCount(newCount);
					}
					yingxiangMap.put(e.getID(), lastId);
				}
				lastInsertDTO = middleDTO;
			}
		}

		EventDTO eventDTO;

		for (MiddleDTO dto : middleDTOList) {
			eventDTO = new EventDTO(dto.getEvent());
			eventDTO.setTop(getTop(dto.getEvent().getBeginTime()));
			eventDTO.setHeight(getHeight(dto.getEvent().getBeginTime(), dto
					.getEvent().getEndTime()));
			if(!isWeek) //天历
			{
				 eventDTO.setWidth(getWidth(dto.getCount()));
			     eventDTO.setLeft(getLeft(dto.getCount(), dto.getLeft()));
			}
			else //周历
			{
				 eventDTO.setWidth(getWeekWidth(dto.getCount()));
				 eventDTO.setLeft(getWeekLeft(dto.getCount(), dto.getLeft()));
			}
			dtoList.add(eventDTO);
		}
		return dtoList;
	}

	/**
	 * 得到top值
	 * 
	 * @param beginTime
	 * @return
	 */
	private int getTop(long beginTime) {
		long minTime = DateTimeUtils.getDayMinTime(beginTime);
		Double top = (beginTime - minTime)
				/ Double.valueOf(String.valueOf(Constant.MS_IN_DAY))
				* MAX_PAGE_CSS_HEIGHT;
		return top.intValue();
	}

	/**
	 * 得到高度
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private int getHeight(long beginTime, long endTime) {
		long maxThisDay = DateTimeUtils.getDayMaxTime(beginTime);
		if (endTime > maxThisDay) {
			endTime = maxThisDay;
		}
		Double value = ((endTime - beginTime) / Double.valueOf(String
				.valueOf(Constant.MS_IN_DAY))) * MAX_PAGE_CSS_HEIGHT;
		int h = value.intValue();
		return h < MIN_CALENDAR_HEIGHT ? MIN_CALENDAR_HEIGHT : h;

	}

	/**
	 * 得到宽度
	 * 
	 * @param count
	 * @return
	 */
	private int getWidth(int count) {
		return MAX_PAGE_CSS_WIDTH / count;
	}
	
	/**
	 * 得到宽度
	 * 
	 * @param count
	 * @return
	 */
	private int getWeekWidth(int count) {
		return WEEK_MAX_PAGE_CSS_WIDTH / count;
	}

	/**
	 * 得到left
	 * 
	 * @param count
	 * @param left
	 * @return
	 */
	private int getLeft(int count, int left) {
		return getWidth(count) * left + 92;
	}

	/**
	 * 得到left
	 * 
	 * @param count
	 * @param left
	 * @return
	 */
	private int getWeekLeft(int count, int left) {
		return getWeekWidth(count) * left ;
	}

	private List<Event> getEvent(List<LoopEvent> LoopEventList,
			long queryMinTime, long queryMaxTime) {
		List<Event> retList = new ArrayList<Event>();

		int loopDays = 1;
		for (LoopEvent le : LoopEventList) {
			if (le.getBeginTime() > queryMaxTime) {
				continue;
			}
			if (isRequireEvent(queryMinTime, queryMaxTime, le.getBeginTime(),
					le.getEndTime())) {
				
				if(!le.getDeleteDates().contains(DateTimeUtils.convert(le.getBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD)))
				{
					
					long deleteTime =le.getRemoveBeginTime();
					
					if(deleteTime>0)
					{
						if(!DateTimeUtils.convert(le.getBeginTime(),DateTimeUtils.DATE_YYYY_MM_DD).equals(DateTimeUtils.convert(deleteTime,DateTimeUtils.DATE_YYYY_MM_DD)))
						{
							 retList.add(le);
						}
					}else
					{
				      retList.add(le);
					}
				}
			}

			EventLoopType elt = le.getLoopType();
			int loopEndType = elt.getEndType();
			loopDays = elt.getDayValue();
			if (elt.getLoopType() == Constant.ONE) // 以天来循环
			{
				if (elt.getDayValue() > 0) // 每隔几天就重复
				{
					if (loopEndType == 0) // 一直重复
					{
						loopEvent(DAY_MAX_LOOP_COUNT, le, loopDays, retList,
								queryMinTime, queryMaxTime, null, false);
					}
					if (loopEndType == 1) // 循环几次
					{
						int loops = Long.valueOf(elt.getEndValue()).intValue();
						loopEvent(loops, le, loopDays, retList, queryMinTime,
								queryMaxTime, null, false);
					}
					if (loopEndType == 2) // 具体日期
					{
						long loopEndTime = elt.getEndValue();
						loopEvent(DAY_MAX_LOOP_COUNT, le, loopDays, retList,
								queryMinTime, queryMaxTime, loopEndTime, false);
					}
				} else // 每个工作日重复
				{
					if (loopEndType == 0) // 一直重复
					{
						loopEvent(DAY_MAX_LOOP_COUNT, le, loopDays, retList,
								queryMinTime, queryMaxTime, null, true);
					}
					if (loopEndType == 1) // 循环几次
					{
						int loops = Long.valueOf(elt.getEndValue()).intValue();
						loopEvent(loops, le, loopDays, retList, queryMinTime,
								queryMaxTime, null, true);
					}
					if (loopEndType == 2) // 具体日期
					{
						long loopEndTime = elt.getEndValue();
						loopEvent(DAY_MAX_LOOP_COUNT, le, loopDays, retList,
								queryMinTime, queryMaxTime, loopEndTime, true);
					}
				}
			}

			/**
			 * lp:频率 1日 2周 3月 4年 dv:当选择为日时， 0表示每个工作日重复 3：表示每3天重复 edt:何时结束 0一直持续
			 * 1 循环几次后结束 2 具体日期结束 edv:0,5,122222222552(long)
			 */
			if (elt.getLoopType() == Constant.TWO
					|| elt.getLoopType() == Constant.THREE
					|| elt.getLoopType() == Constant.FOUR) {
				int type = elt.getLoopType();
				int maxLoopCount = WEEK_MAX_LOOP_COUNT;
				if (type == Constant.THREE)
					maxLoopCount = MONTH_MAX_LOOP_COUNT;
				if (type == Constant.FOUR)
					maxLoopCount = YEAR_MAX_LOOP_COUNT;
				if (loopEndType == 0) // 一直重复
				{
					loopEvent(maxLoopCount, le, retList, queryMinTime,
							queryMaxTime, null, type);
				}
				if (loopEndType == 1) // 循环几次
				{
					int loops = Long.valueOf(elt.getEndValue()).intValue();
					loopEvent(loops, le, retList, queryMinTime, queryMaxTime,
							null, type);
				}
				if (loopEndType == 2) // 具体日期
				{
					long loopEndTime = elt.getEndValue();
					loopEvent(maxLoopCount, le, retList, queryMinTime,
							queryMaxTime, loopEndTime, type);
				}
			}
		}
		return retList;
	}

	/**
	 * 当循环力度是天时调用
	 * 
	 * @param loops
	 * @param le
	 * @param loopDays
	 * @param retList
	 * @param queryMinTime
	 * @param queryMaxTime
	 * @param loopEndTime
	 * @param isQueryWorkDate
	 */
	private void loopEvent(int loops, LoopEvent le, int loopDays,
			List<Event> retList, long queryMinTime, long queryMaxTime,
			Long loopEndTime, boolean isQueryWorkDate) {
		
		if(loopDays==0)
			loopDays=1;
		
	
		Set<String> deleteSet =le.getDeleteDates();
		long removeBeginTime =le.getRemoveBeginTime();
		
		String thisDay;
		
		for (int i = 1; i <= loops; i++) {
			
			long newBeginTime = le.getBeginTime() + Constant.MS_IN_DAY
					* loopDays * (i );
			long newEndTime = le.getEndTime() + Constant.MS_IN_DAY * loopDays
					* (i);

			if (newBeginTime > queryMaxTime) {
				break;
			}
			if (null != loopEndTime) {
				if (newBeginTime > loopEndTime) {
					break;
				}
			}
			
			if (isRequireEvent(queryMinTime, queryMaxTime, newBeginTime,
					newEndTime)) {
				
				if(removeBeginTime>0 && newBeginTime >=removeBeginTime) //本天即以后
				{
				   break;
				}
				thisDay =DateTimeUtils.convert(newBeginTime,DateTimeUtils.DATE_YYYY_MM_DD);
				if(deleteSet.contains(thisDay))
				{
					continue;
				}
				
				if (isQueryWorkDate) {
					if (DateTimeUtils.isWorkDate(newBeginTime)) {
						retList.add(le.toEvent(le.getID(), newBeginTime,
								newEndTime));
					}
				} else {
					retList.add(le.toEvent(le.getID(), newBeginTime, newEndTime));
				}
			}
		}
	}

	/**
	 * 当循环力度是周，月，年时调用
	 * 
	 * @param loops
	 * @param le
	 * @param retList
	 * @param queryMinTime
	 * @param queryMaxTime
	 * @param loopEndTime
	 * @param type
	 *            2：周 3月 4年
	 */
	private void loopEvent(int loops, LoopEvent le, List<Event> retList,
			long queryMinTime, long queryMaxTime, Long loopEndTime, int type) {

		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(new Date(le.getBeginTime()));

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(new Date(le.getEndTime()));

		Set<String> deleteSet =le.getDeleteDates();
		long removeBeginTime =le.getRemoveBeginTime();

		String thisDay;
		long newBeginTime;
		long newEndTime;
		for (int i = 1; i <= loops; i++) {
			if (type == 2) // 周
			{
				beginCal.add(Calendar.DATE, Constant.SEVEN);
				endCal.add(Calendar.DATE, Constant.SEVEN);
			}
			if (type == 3) // 月
			{
				beginCal.add(Calendar.MONTH, Constant.ONE);
				endCal.add(Calendar.MONTH, Constant.ONE);
			}
			if (type == 4) // 年
			{
				beginCal.add(Calendar.YEAR, Constant.ONE);
				endCal.add(Calendar.YEAR, Constant.ONE);

			}
			newBeginTime = beginCal.getTimeInMillis();
			newEndTime = endCal.getTimeInMillis();

			if (newBeginTime > queryMaxTime) {
				break;
			}
			if (null != loopEndTime) {
				if (newBeginTime > loopEndTime) {
					break;
				}
			}
			
			if(removeBeginTime>0 && newBeginTime >=removeBeginTime) //本天即以后
			{
			   break;
			}
			thisDay =DateTimeUtils.convert(newBeginTime,DateTimeUtils.DATE_YYYY_MM_DD);
			if(deleteSet.contains(thisDay))
			{
				continue;
			}
			
			if (isRequireEvent(queryMinTime, queryMaxTime, newBeginTime,
					newEndTime)) {
				retList.add(le.toEvent(le.getID(), newBeginTime, newEndTime));
			}
		}
	}
	/**
	 * 判断时间是否符合要求
	 * 
	 * @return
	 */
	private boolean isRequireEvent(long queryMinTime, long queryMaxTime,
			long eventBeginTime, long eventEndTime) {
		if (eventBeginTime > queryMaxTime)
			return false;
		if (queryMinTime > eventEndTime)
			return false;
		return true;
	}

}
