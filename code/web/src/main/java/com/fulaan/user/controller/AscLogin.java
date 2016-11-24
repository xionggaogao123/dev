package com.fulaan.user.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.utils.HttpXmlClient;
import com.fulaan.utils.HttpXmlClient.HttpFulaanCookie;
import com.sys.constants.Constant;

@Controller
@RequestMapping("/asc")
public class AscLogin extends BaseController {

	@SessionNeedless
	@RequestMapping("/login")
	public String ascLogin(HttpServletResponse response,HttpServletRequest request)
	{
		Map<String, String> params = new HashMap<String, String>();  
		params.put("tbUserName", "乌市教育局");
		params.put("tbUserPwd", "wsjyj");
		params.put("__VIEWSTATE", "/wEPDwUKMTEzODU1MTU2Ng9kFgICAw9kFgICAQ9kFgQCCQ8PZBYCHgZvbmxvYWQFR3RvcC50b3BGcmFtZS5kb2N1bWVudC5nZXRFbGVtZW50QnlJZCgncGVyc2VuSW5mb3InKS5ocmVmPSdqYXZhc2NyaXB0OjsnZAILDw8WAh4HVmlzaWJsZWdkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAgUJaWJ0bkxvZ2luBQdpYnRuUmVn/lFKKkSMQPjc2dKEuUIWRRoyJ0Q=");
		params.put("__EVENTVALIDATION", "/wEWBQLS9Oa9DQLyj/OQAgKVqs78BwKBo5SvBQKcscC4BhU5e5zO2q516OaO+EX6bwTrjioz");
		params.put("ibtnLogin.x", "20");
		params.put("ibtnLogin.y", "12");
		List<HttpFulaanCookie>	cks = HttpXmlClient.post("http://xjwlmqtest1.jintizi.com/zyzx/user_win.aspx", params);   
		
		
		System.out.println(cks);
		if(null!=cks && cks.size()>0)
		{
			for(HttpFulaanCookie ck:cks)
			{
				Cookie userKeycookie = new Cookie(ck.getName(),ck.getValue());
	
				userKeycookie.setPath(Constant.BASE_PATH);
				userKeycookie.setDomain(".jintizi.com");
				userKeycookie.isHttpOnly();
				response.addCookie(userKeycookie);
			}
		}
		
		return "asc/asc";
	
	}
	
	
	
}
