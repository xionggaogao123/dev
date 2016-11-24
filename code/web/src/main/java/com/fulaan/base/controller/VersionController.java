package com.fulaan.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.service.AppVersionService;
import com.pojo.app.AppVersionEntry;
import com.pojo.app.AppVersionDTO;
import com.sys.constants.Constant;

/**
 * 版本号controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/version")
public class VersionController extends BaseController {

	private AppVersionService service =new AppVersionService();
	
	/**
	 * 最近的版本号
	 * @return
	 */
	@SessionNeedless
	@RequestMapping(value="/recently",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public AppVersionDTO getAppVersionDTO( @RequestParam(defaultValue="android") String client )
	{
		int c=Constant.ZERO;
		if("android".equalsIgnoreCase(client))
		{
			c=Constant.ONE;
		}
		if("launch".equalsIgnoreCase(client))
		{
			c=Constant.TWO;
		}
		if("android_mall".equalsIgnoreCase(client))
		{
			c=Constant.THREE;
		}
		if("ios_mall".equalsIgnoreCase(client))
		{
			c=Constant.FOUR;
		}
		
		AppVersionEntry entry=service.getRecentlyVersion(c);
		if(null!=entry)
		{
			return new AppVersionDTO(entry.getVersion(),entry.getDes());
		}
		return null;
	}

}
