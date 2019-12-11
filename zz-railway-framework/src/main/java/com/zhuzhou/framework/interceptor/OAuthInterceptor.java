package com.zhuzhou.framework.interceptor;

import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ConstUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限过滤拦截器
 */
public class OAuthInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 权限过滤
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof HandlerMethod)){
			return true;
		}
		HandlerMethod method = (HandlerMethod) handler;
		OAuth oAuth = method.getMethodAnnotation(OAuth.class);
		if(oAuth == null || !oAuth.required()){
			return true;
		}
		if(request.getSession().getAttribute(ConstUtils.SESSION_USER) == null){
            request.getRequestDispatcher("/error/" + Result.NOT_OAUTH).forward(request, response);
			return false;
		}
		return true;
	}

}