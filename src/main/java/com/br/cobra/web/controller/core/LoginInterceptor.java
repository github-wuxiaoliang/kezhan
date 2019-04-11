package com.br.cobra.web.controller.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.br.cobra.common.util.HttpClientUtil;
import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.model.UserInfoDto;

/**
 * 登录校验
 * @author chun
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        // 将登录用户信息放入到model中
        if(response.isCommitted()){
            return;
        }
        UserInfoDto user = (UserInfoDto) request.getSession().getAttribute(Constants.SESSION_USER_INFO);
        if(user != null && modelAndView != null && modelAndView.getModel() != null
                && !modelAndView.getViewName().startsWith("redirect:")){
            modelAndView.getModel().put("user", user);
            modelAndView.getModel().put("ctx",request.getContextPath());
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String url = PropertiesUtil.getStringValue("internal.cas.url");
        
        HttpSession session = request.getSession(true);
        String requestUrl = request.getRequestURI();
        
        if(requestUrl.contains("user/logout.do")){
            return true;
        }
        
        String ticket = request.getParameter("ticket");
        UserInfoDto userInfo = null;
        if(StringUtils.isNotBlank(ticket)){
            userInfo = getUserInfoByCas(ticket);
            if(userInfo != null){
                // 保存用户信息
                session.setAttribute(Constants.SESSION_USER_INFO, userInfo);
            }
        }
        userInfo = (UserInfoDto) session.getAttribute(Constants.SESSION_USER_INFO);
        
        // 用户无登录
        if(userInfo == null){
            // 401 跳转
            response.sendRedirect(url);
            return false;
        }
        
        return true;
    }
    
    private UserInfoDto getUserInfoByCas(String token){
        String url = PropertiesUtil.getStringValue("internal.cas.login.url");
        Map<String, String> params = new HashMap<String, String>();
        params.put("ticket", token);
        try {
            String resultJson = HttpClientUtil.simpleGetInvoke(url, params);
            JSONObject data = JSONObject.parseObject(resultJson);
            if("success".equals(data.getString("status"))){
                UserInfoDto userInfoDto = new UserInfoDto(data);
                return userInfoDto;
            }
        } catch(Exception e){
            LOGGER.info("获取登录cas用户失败."+e.getMessage(),e);
            return null;
        }
        return null;
    }

}
