package com.fulaan.salaryapp.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.salary.service.SalaryService;
import com.pojo.salary.SalaryDto;
import com.pojo.salary.SalaryItemDto;
import com.pojo.salary.SalaryItemEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import com.sys.utils.SalaryRespObj;

@Controller
@RequestMapping("/appsalary")
public class AppSalaryController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(AppSalaryController.class);

	@Autowired
	private SalaryService salaryService;

	/**
	 * 获取年份列表
	 */
	@SessionNeedless
	@RequestMapping("/getYearList")
	@ResponseBody
	public RespObj getYearList() {
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<String> yearList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		for (int i = year + 1; i > year - 10; i--) {
			yearList.add(i + Constant.EMPTY);
		}
		respObj.setMessage(yearList);
		return respObj;
	}

	
	/**
	 * 获取工资项目列表
	 */
	@SessionNeedless
	@RequestMapping("/getItermList")
	@ResponseBody
	public RespObj getSalaryItemList(@QueryParam("year") int year,
			@QueryParam("month") int month, @QueryParam("num") int num) {
		// ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<SalaryDto> listEntry = salaryService.findSalaryInfo(
				getSessionValue().getSchoolId(), year, month, num);
		if (null != listEntry && !listEntry.isEmpty()) {
			SalaryDto sd = listEntry.get(0);
			List<SalaryItemDto> sid = sd.getMoney();
			for (SalaryItemDto s : sid) {
				Map<String, String> map = new HashMap<String, String>();
				String name = s.getItemName();
				map.put("itemName", name);
				list.add(map);
			}
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		respObj.setMessage(list);
		return respObj;
	}

	/**
	 * 薪酬表格列表 根据年份，月份，发放次数，工资项目查询
	 */
	@SessionNeedless
	@RequestMapping("/getSalaryList")
	@ResponseBody
	public SalaryRespObj getSalaryList(@QueryParam("year") int year,
			@QueryParam("month") int month, @QueryParam("num") int num,
			@QueryParam("itemName") String itemName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		SalaryRespObj respObj = new SalaryRespObj(Constant.SUCCESS_CODE);
		if (logger.isDebugEnabled()) {
			logger.debug("try to query salary (y,m,c): " + year + ":" + month
					+ ":" + num);
		}

		List<Object> list = new ArrayList<Object>();
		List<SalaryDto> listEntry = salaryService.findSalaryInfo(
				getSessionValue().getSchoolId(), year, month, num);
		int total = listEntry.size();
		List<SalaryDto> pageList = (List<SalaryDto>) getListByPage(listEntry,
				pageNo, pageSize);
		Iterator<SalaryDto> it = pageList.iterator();
		while (it.hasNext()) {
			Map<String, Object> maps = new LinkedHashMap<String, Object>();
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			SalaryDto sd = it.next();
			String id = sd.getId();
			String name = sd.getUserName();
			double ss = sd.getSs();
			double as = sd.getAs();
			double ms = sd.getMs();
			maps.put("id", id);
			maps.put("name", name);
			List<SalaryItemDto> listMoney = sd.getMoney();
			Iterator<SalaryItemDto> itr = listMoney.iterator();
			while (itr.hasNext()) {
				SalaryItemDto sid = itr.next();
				String itemNames = sid.getItemName();
				if ("".equals(itemName) || itemName == null) {
					double m = sid.getM();
					map.put(itemNames, m);
				} else if (itemNames.equals(itemName)) {
					double m = sid.getM();
					map.put(itemName, m);
				}

			}
			map.put("ms", ms);
			map.put("ss", ss);
			map.put("as", as);
			maps.put("itemsList", map);
			list.add(maps);
		}
		respObj.setTotal(total);
		respObj.setMessage(list);
		return respObj;
	}

	/**
	 * 获取个人薪酬详情
	 */

	@SessionNeedless
	@RequestMapping("/getSelfDetail")
	@ResponseBody
	public RespObj getSelfSalaryDetail(@QueryParam("id") String id) {
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<Object> list = new ArrayList<Object>();
		List<SalaryItemDto> listMoney = new ArrayList<SalaryItemDto>();
		Map<String, Object> maps = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		SalaryDto sd = salaryService.getSalaryDto(id);
		String name = sd.getUserName();
		double ss = sd.getSs();
		double as = sd.getAs();
		double ms = sd.getMs();
		listMoney = sd.getMoney();
		maps.put("name", name);
		Iterator<SalaryItemDto> it = listMoney.iterator();

		while (it.hasNext()) {
			SalaryItemDto sid = it.next();
			String itemName = sid.getItemName();
			double m = sid.getM();
			map.put(itemName, m);
		}
		map.put("ms", ms);
		map.put("ss", ss);
		map.put("as", as);
		maps.put("itemsList", map);
		list.add(maps);
		respObj.setMessage(list);
		return respObj;
	}

	/**
	 * 添加工资项目记录数据
	 * 
	 * @param itemInfo
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addProject")
	@ResponseBody
	public RespObj addItemInfo(SalaryItemDto itemInfo) {
		RespObj result = new RespObj(Constant.FAILD_CODE);
		if (logger.isDebugEnabled()) {
			logger.debug("try to add salary item: " + itemInfo.toString());
		}
		itemInfo.setSchoolId(getSessionValue().getSchoolId());
		String itemId = salaryService.addSalaryItemInfo(itemInfo);
		if (itemId == null) {
			result.message = "添加工资项目失败，请稍后再试";
			if (logger.isDebugEnabled()) {
				logger.debug("add salary item failed: " + itemInfo.toString());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete salary item success: " + itemInfo.toString());
		}
		result.code = Constant.SUCCESS_CODE;
		result.message = "添加工资项目成功";
		return result;
	}

	/**
	 * 更新工资项目记录数据
	 * 
	 * @param itemInfo
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/updateProject")
	@ResponseBody
	public RespObj updateItemInfo(SalaryItemDto itemInfo) {
		RespObj result = new RespObj(Constant.FAILD_CODE);
		if (logger.isDebugEnabled()) {
			logger.debug("try to update salary item: " + itemInfo.toString());
		}
		String itemId = salaryService.updateSalaryItemInfo(itemInfo);
		if (itemId == null) {
			result.message = "修改工资项目失败，请稍后再试";
			if (logger.isDebugEnabled()) {
				logger.debug("update salary item failed: "
						+ itemInfo.toString());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("update salary item success: " + itemInfo.toString());
		}
		result.code = Constant.SUCCESS_CODE;
		result.message = "修改工资项目成功";
		return result;
	}

	/**
	 * 删除工资项目记录数据
	 * 
	 * @param itemInfo
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/deleteProject")
	@ResponseBody
	public RespObj deleteItemInfo(SalaryItemDto itemInfo) {
		RespObj result = new RespObj(Constant.FAILD_CODE);
		if (logger.isDebugEnabled()) {
			logger.debug("try to delete salary item: " + itemInfo.toString());
		}
		String itemId = salaryService.deleteSalaryItemInfo(itemInfo);
		if (itemId == null) {
			result.message = "删除工资项目失败，请稍后再试";
			if (logger.isDebugEnabled()) {
				logger.debug("delete salary item failed: "
						+ itemInfo.toString());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete salary item success: " + itemInfo.toString());
		}
		result.code = Constant.SUCCESS_CODE;
		result.message = "删除工资项目成功";
		return result;
	}

	/**
	 * 查询工资项目记录数据
	 * 
	 * @param itemInfo
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/projectList")
	@ResponseBody
	public String findItemInfo() {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		List<SalaryItemDto> list = salaryService.findSalaryItemInfo(schoolId);
		if (list == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("find salary itemlist failed");
			}
		}
		return JSON.toJSONString(list);
	}

	/**
	 * 获取区间工资信息
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/loadSalary")
	@ResponseBody
	public SalaryRespObj loadSalaryDetail(
			@RequestParam(value = "begin", defaultValue = "") int begin,
			@RequestParam(value = "end", defaultValue = "") int end,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		SalaryRespObj respObj = new SalaryRespObj(Constant.SUCCESS_CODE);
		List<SalaryDto> list = salaryService.findSalaryInfo(getSessionValue()
				.getId(), begin, end);
		@SuppressWarnings("unchecked")
		List<SalaryDto> pageList = (List<SalaryDto>) getListByPage(list,
				pageNo, pageSize);
		respObj.setTotal(list.size());
		respObj.setMessage(pageList);
		return respObj;
	}

	/**
	 * 分页
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,
			int pageNo, int pageSize) {
		List<Object> list = new ArrayList<Object>();
		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = (pageNo * pageSize) - 1;
		if (src.size() < pageNo * pageSize) {
			endIndex = src.size() - 1;
		}
		for (int i = startIndex; i < endIndex + 1; i++) {
			list.add(src.get(i));
		}
		return list;
	}

	/**
	 * 获取某月份的当前工资次数
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/getSalaryTimes")
	@ResponseBody
	public RespObj getSalaryTimes(Integer year, Integer month) {
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		respObj.setMessage(salaryService.getSalaryTimes(getSessionValue()
				.getSchoolId(), year, month));
		return respObj;
	}
}
