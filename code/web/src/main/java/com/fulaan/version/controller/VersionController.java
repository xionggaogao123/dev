package com.fulaan.version.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.version.service.AppVersionService;
import com.fulaan.version.dto.AppVersionDTO;
import com.pojo.version.AppVersionEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 版本号controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/jxmapi/version")
public class VersionController extends BaseController {

	private AppVersionService service =new AppVersionService();
	
	/**
	 * 最近的版本号
	 * @return
	 */
	@ApiOperation(value = "最近的版本号", httpMethod = "GET", produces = "application/json")
	@ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
	@SessionNeedless
	@RequestMapping(value="/recently",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public AppVersionDTO getAppVersionDTO( @RequestParam(defaultValue="android") String client )
	{
		int c= Constant.ZERO;
		if("android".equalsIgnoreCase(client))
		{
			c= Constant.ONE;
		}
		if("launch".equalsIgnoreCase(client))
		{
			c= Constant.TWO;
		}
		if("android_mall".equalsIgnoreCase(client))
		{
			c= Constant.THREE;
		}
		if("ios_mall".equalsIgnoreCase(client))
		{
			c= Constant.FOUR;
		}
		
		AppVersionEntry entry=service.getRecentlyVersion(c);
		if(null!=entry)
		{
			return new AppVersionDTO(entry.getVersion(),entry.getDes());
		}
		return null;
	}

	/**
	 * 添加版本号
	 * @return
	 */
	@ApiOperation(value = "添加版本号", httpMethod = "GET", produces = "application/json")
	@ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
	@SessionNeedless
	@RequestMapping(value="/addVersion",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public RespObj addVersion( @RequestParam(value =  "type") int type ,
									 @RequestParam(value="client") String client,@RequestParam(value="dec") String dec)
	{
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		try{
			AppVersionDTO appVersionDTO = new AppVersionDTO(type,client,dec);
			service.addAppVersion(appVersionDTO.buildEntry());
			respObj.setCode(Constant.SUCCESS_CODE);
			respObj.setMessage("添加成功！");
		}catch (Exception e){
			respObj.setCode(Constant.FAILD_CODE);
			respObj.setMessage("添加失败！");
		}
		return respObj;
	}

}
