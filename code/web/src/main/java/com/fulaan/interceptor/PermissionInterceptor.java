package com.fulaan.interceptor;

import com.fulaan.base.BaseController;
import com.fulaan.annotation.UserPermissions;
import com.fulaan.annotation.UserRoles;
import com.pojo.app.SessionValue;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.UnLoginException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

public class PermissionInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(PermissionInterceptor.class);
    private static String FORMATAT = ",{0},";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod method = (HandlerMethod) handler;
        UserPermissions up = method.getMethodAnnotation(UserPermissions.class);
        UserRoles ur = method.getMethodAnnotation(UserRoles.class);

        if (null != up || null != ur) {
            SessionValue sv = (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);

            if (null == sv)
                throw new UnLoginException();

            if (null != up) {
                String permissionFormat = MessageFormat.format(FORMATAT, up.value());

                String removePermission = sv.getUserRemovePermission();
                if (StringUtils.isNotBlank(removePermission)) {
                    if (removePermission.contains(permissionFormat)) {
                        throw new PermissionUnallowedException(handler.toString());
                    }
                }

                String havePermission = sv.getUserPermission();
                if (StringUtils.isNotBlank(havePermission)) {
                    if (havePermission.contains(permissionFormat)) {
                        return true;
                    }
                }
            }


            if (null != ur) {
                int userRole = sv.getUserRole();
                logger.info(userRole + "======role");
                UserRole[] removeRoles = ur.noValue();
                if (removeRoles.length > Constant.ZERO) {
                    boolean isIn = UserRole.isInRoles(userRole, removeRoles);
                    if (isIn)
                        throw new PermissionUnallowedException(handler.toString());
                }
                UserRole[] allowRoles = ur.value();
                if (allowRoles.length > Constant.ZERO) {
                    boolean isIn = UserRole.isInRoles(userRole, allowRoles);
                    if (!isIn)
                        throw new PermissionUnallowedException(handler.toString());
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }


}
