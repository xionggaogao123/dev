package com.fulaan.school.navigation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.school.service.SchoolNavigationService;
import com.pojo.app.SessionValue;
import com.pojo.school.SchoolNavigationModuleDTO;
import com.pojo.school.SchoolNavigationModuleDTO.SchoolNavigationDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.serialize.ListObjectSerializable;

/**
 * 学校导航controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/school/navs/")
public class SchoolNavigationController extends BaseController {

	private static Logger logger =Logger.getLogger(SchoolNavigationController.class);
	
	private static SchoolNavigationService schoolNavigationService =new SchoolNavigationService();
	
	
	/**
	 * 加载学校导航
	 * @param type 1重新设定导航
	 * @return
	 */
	@RequestMapping("/load")
	@ResponseBody
	public List<SchoolNavigationModuleDTO> loadNavs(@RequestParam(required = false, defaultValue = "0")  int type )
	{
		SessionValue sv =getSessionValue();
		if(StringUtils.isBlank(sv.getSchoolId()))
		{
			return new ArrayList<SchoolNavigationModuleDTO>();
		}
		String key=MessageFormat.format(CacheHandler.K6KT_SCHOOL_ROLE_NAVS, sv.getSchoolId(),String.valueOf(sv.getUserRole()));
		ListObjectSerializable<SchoolNavigationModuleDTO> ser=new ListObjectSerializable<SchoolNavigationModuleDTO>();
		byte[] byteArr=CacheHandler.getBytesValue(key);
		if(null!=byteArr)
		{
			try
			{
					List<SchoolNavigationModuleDTO> list=ser.deserialize(byteArr);
					if(null!=list && list.size()>0)
					{
						if(0==type)
						{
						  return list;
						}
					}
			}catch(Exception ex)
			{
					logger.error("Nav error:", ex);
			}
		}
		List<Integer> roles=UserRole.getUserRoleIntList(sv.getUserRole());
		List<SchoolNavigationModuleDTO> dtoList=new ArrayList<SchoolNavigationModuleDTO>();
		try
		{
		  dtoList=schoolNavigationService.loadNavs(new ObjectId(sv.getSchoolId()), roles);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		
		if(dtoList.size()>0)
		{
			try
			{
			  byte[] bytes=ser.serialize(dtoList);
			  CacheHandler.cache(key, bytes, Constant.SECONDS_IN_DAY);
			}catch(Exception ex)
			{
				logger.error("Nav error:", ex);
			}
		}
		return dtoList;
	}
	
	
	
	/**
	 * 加载用户磁贴页面
	 * @return
	 */
	@RequestMapping("/citie")
	@ResponseBody
	public List<SchoolNavigationDTO> loadCiTie(@RequestParam(required = false, defaultValue = "0")  int type)
	{
		
		SessionValue sv =getSessionValue();
		if(StringUtils.isBlank(sv.getSchoolId()))
		{
			return new ArrayList<SchoolNavigationDTO>();
		}
		
		String key=MessageFormat.format(CacheHandler.K6KT_SCHOOL_ROLE_CITIE, sv.getSchoolId(),String.valueOf(sv.getUserRole()));
		ListObjectSerializable<SchoolNavigationDTO> ser=new ListObjectSerializable<SchoolNavigationDTO>();
		byte[] byteArr=CacheHandler.getBytesValue(key);
		if(null!=byteArr)
		{
			try
			{
					List<SchoolNavigationDTO> list=ser.deserialize(byteArr);
					if(null!=list && list.size()>0)
					{
						if(0==type)
						{
						  return list;
						}
					}
			}catch(Exception ex)
			{
					logger.error("Nav error:", ex);
			}
		}
		
		List<Integer> roles=UserRole.getUserRoleIntList(sv.getUserRole());
		List<SchoolNavigationDTO> dtoList=new ArrayList<SchoolNavigationDTO>();
		try
		{
		  dtoList=schoolNavigationService.loadCiTie(new ObjectId(sv.getSchoolId()), roles);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		logger.debug(dtoList);
		
		if(dtoList.size()>0)
		{
			try
			{
			  byte[] bytes=ser.serialize(dtoList);
			  CacheHandler.cache(key, bytes, Constant.SECONDS_IN_DAY);
			}catch(Exception ex)
			{
				logger.error("Nav error:", ex);
			}
		}
		return dtoList;
	}
	
	
	
}
