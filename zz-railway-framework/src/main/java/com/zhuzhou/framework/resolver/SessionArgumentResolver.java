package com.zhuzhou.framework.resolver;

import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.utils.stomp.ConstUtils;
import com.zhuzhou.framework.utils.stomp.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求参数解析器
 * @author cjh
 */
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否是支持解析的参数
     * @param parameter
     * @return
     */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(OAuth.class) != null;
	}

    /**
     * 解析参数
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
		try {
			Subject subject = SecurityUtils.getSubject();
			return subject.getPrincipals().getPrimaryPrincipal();
		} catch (Exception e) {
			throw new ApplicationException(ErrorResponseMsgEnum.TOKEN_EXPIRE);
		}
	}
}

