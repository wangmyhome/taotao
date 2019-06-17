package com.taotao.web.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.com.utils.CookieUtils;
import com.taotao.sso.query.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";
    
    public static final String LOGIN_URL = "http://sso.taotao.com/user/login.html";
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        UserThreadLocal.set(null);//清空当前线程中的User对象
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            //未登录状态
            response.sendRedirect(LOGIN_URL);
            System.out.println("处于未登录状态1");
            return false;
        }
        User user = this.userService.queryUserByToken(token);
        if(null == user){
            //未登录状态
            response.sendRedirect(LOGIN_URL);
            System.out.println("处于未登录状态2");
            return false;
        }
        //处于登录状态
        UserThreadLocal.set(user);//将User对象放入UserThreadLocal中
        return true;
        
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {

    }

}
