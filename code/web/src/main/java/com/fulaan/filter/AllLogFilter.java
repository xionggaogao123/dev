package com.fulaan.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

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

        long before = System.currentTimeMillis();

        System.out.println("开始过滤... ");
        // 将请求转换成HttpServletRequest 请求
        HttpServletRequest hrequest = (HttpServletRequest) request;
        // tomcat记录日志
        context.log("Filter已经截获到用户的请求的地址: " + hrequest.getServletPath());
        //log4j
        logger.info("Filter已经截获到用户的请求的地址: " + hrequest.getServletPath());
        try {
            // Filter 只是链式处理，请求依然转发到目的地址。
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long after = System.currentTimeMillis();
        // 记录日志
        context.log("过滤结束");
        // 再次记录日志
        context.log(" 请求被定位到" + ((HttpServletRequest) request).getRequestURI()
                + "所花的时间为: " + (after - before));
        //log4j
        logger.info(" 请求被定位到" + ((HttpServletRequest) request).getRequestURI()
                + "所花的时间为: " + (after - before));
    }
}
