package com.fulaan.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by James on 2017/10/12.
 * //对请求路径的全部拦截
 */
public class AllLogFilter implements Filter {

    private static final Logger logger =Logger.getLogger(AllLogFilter.class);
    private FilterConfig config;
    // 实现初始化方法
    public void init(FilterConfig config) {
        this.config = config;
    }
    // 实现销毁方法
    public void destroy() {
        this.config = null;
    }
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) {
        // 获取ServletContext 对象，用于记录日志
        ServletContext context = this.config.getServletContext();
        //Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        //Cookie[] cookies = request.getCookies();
        //HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        //Cookie[] cookies = httpServletRequest.getCookies();
        long before = System.currentTimeMillis();

        //System.out.println("开始过滤... ");
        // 将请求转换成HttpServletRequest 请求
        HttpServletRequest hrequest = (HttpServletRequest) request;
        //访问的用户IP地址
        String ipconfig = "";
        if (hrequest.getHeader("x-forwarded-for") == null) {
            ipconfig =  hrequest.getRemoteAddr();
        }else{
            ipconfig =  hrequest.getHeader("x-forwarded-for");
        }
        //cookie
        Cookie[] allCookies = hrequest.getCookies();
        //String cooks = Arrays.toString(allCookies);
        StringBuffer bs = new StringBuffer();
        if(allCookies != null){
            for(int i=0;i<allCookies.length;i++){
                Cookie temp = allCookies[i];
                bs.append(temp.getName() + "=" + temp.getValue() + " ");
            }
        }else{
                bs.append("null");
        }
        String cooks = bs.toString();
        //System.out.println(cooks);

        //参数列表
       // Enumeration enumeration = ((HttpServletRequest) request).getHeaderNames();

        Map<String ,String[]> map=request.getParameterMap();
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            stringBuffer.append(key + ":" + Arrays.toString(map.get(key))+"  ");
            //System.out.println("key= "+ key + " and value= " + map.get(key));
        }
        String  canList = stringBuffer.toString();
        //System.out.println(canList);
        try {

            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ip:" + cooks);
            if(!ipconfig.contains("192.168.1")){
                logger.error("error" ,e);
            }
        }

        long after = System.currentTimeMillis();
        String str = "";
        if((after - before)>1000){
            str="$132";
        }
        // 记录日志
        //context.log("过滤结束");
        // 再次记录日志
        //context.log("Filter已经截获到用户的请求的地址: " + hrequest.getServletPath()+"    请求被定位到" + ((HttpServletRequest) request).getRequestURI()+ "所花的时间为: " + (after - before));
        //log4j
        //logger.warn("Filter已经截获到用户的请求的地址: " + hrequest.getServletPath() + "    请求被定位到" + ((HttpServletRequest) request).getRequestURI() + "所花的时间为: " + (after - before));
        if(!hrequest.getServletPath().contains("entrance") && !ipconfig.contains("192.168.1") && !ipconfig.contains("127.0.0.1")){
            logger.warn(" %a - " + ipconfig
                    + "   %c - " + cooks
                    + "   %p - " + hrequest.getServletPath()
                    + "   %w - " + hrequest.getRequestURI()
                    + "   %t - " + (after - before)+"ms"
                    + "   %l - " + canList
                    + "timeOut:" + (after - before)
                    +"  " +str);
        }

        //"%a - " + ipconfig + "+++"
        //"   %c - " + Arrays.toString(cookies) + "+++"
        //"   %p - " + hrequest.getServletPath() + "+++"
        //"   %w - " + ((HttpServletRequest) request).getRequestURI() + "+++"
        //"   %t - " + (after - before)) + "+++"
        //"   %l - " + stringBuffer.toString()
    }
}
