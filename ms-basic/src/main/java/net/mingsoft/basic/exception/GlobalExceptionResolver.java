/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.basic.exception;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import net.mingsoft.basic.constant.ErrorCodeEnum;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * 全局异常处理类
 * @author 铭飞开源团队-Administrator  
 * @date 2018年4月6日
 */
@ControllerAdvice
public class GlobalExceptionResolver extends DefaultHandlerExceptionResolver {
	
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * 全局异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response,Exception e) {
	    Map map = new HashMap();
		map.put("code", ErrorCodeEnum.SERVER_ERROR);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 全局异常 未找到类404
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ModelAndView handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response,Exception e) {
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_NOT_FIND);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 请求参数异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ModelAndView handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response,Exception e) {
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_NOT_FIND);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 请求方法类型错误
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ModelAndView handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response,Exception e) {
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_NOT_FIND);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 统一处理请求参数校验(实体对象传参)
	 *
	 * @param e BindException
	 * @return ResultResponse
	 */
	@ExceptionHandler(BindException.class)
	public ModelAndView validExceptionHandler(HttpServletRequest request, HttpServletResponse response,BindException e) {
		StringBuilder message = new StringBuilder();
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError error : fieldErrors) {
			message.append(error.getField()).append(error.getDefaultMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		Map map = new HashMap();
		map.put("cls", e.getStackTrace()[0]+""); //出错的类
		map.put("url", request.getServletPath()); //请求地址
		map.put("code", ErrorCodeEnum.CLIENT_REQUEST);
		map.put("result",false);
		map.put("msg", message.toString());
		map.put("exc", e.getClass()); //详细异常信息
		return getModelAndView(request, response, map,null);
	}

	/**
	 * 统一处理请求参数校验(普通传参)
	 *
	 * @param e ConstraintViolationException
	 * @return ResultResponse
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ModelAndView handleConstraintViolationException(HttpServletRequest request, HttpServletResponse response,ConstraintViolationException e) {
		StringBuilder message = new StringBuilder();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			Path path = violation.getPropertyPath();
			String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
			message.append(pathArr[1]).append(violation.getMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		Map map = new HashMap();
		map.put("msg", message.toString());
		map.put("code", ErrorCodeEnum.CLIENT_REQUEST);
		map.put("cls", e.getStackTrace()[0]+""); //出错的类
		map.put("url", request.getServletPath()); //请求地址

		map.put("exc", e.getClass()); //详细异常信息
		return getModelAndView(request, response, map,null);
	}


	/**
	 * shiro权限未授权异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = UnauthorizedException.class)
	public ModelAndView handleUnauthorizedException(HttpServletRequest request, HttpServletResponse response,UnauthorizedException e) {
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_UNAUTHORIZED);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 登录异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = AuthenticationException.class)
	public ModelAndView handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,AuthenticationException e) {
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_NOT_FIND);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * shiro权限错误
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = AuthorizationException.class)
	public ModelAndView handleAuthorizationException(HttpServletRequest request, HttpServletResponse response,AuthorizationException e){
		LOG.debug("AuthorizationException", e);
		Map map = new HashMap();
		map.put("cls", e.getStackTrace()[0]+""); //出错的类
		map.put("url", request.getServletPath()); //请求地址
		map.put("code", ErrorCodeEnum.CLIENT_NOT_FIND);
		map.put("msg",  e.getStackTrace());
		map.put("exc", e.getClass()); //详细异常信息
		return getModelAndView(request, response, map,null);
	}

	/**
	 * session失效异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = ExpiredSessionException.class)
	public ModelAndView handleExpiredSessionException(HttpServletRequest request, HttpServletResponse response,ExpiredSessionException e) {
		LOG.debug("ExpiredSessionException", e);
		Map map = new HashMap();
		map.put("code", ErrorCodeEnum.CLIENT_UNAUTHORIZED);
		return getModelAndView(request, response, map,e);
	}

	/**
	 * 返回异常信息处理
	 * @param request
	 * @param response
	 * @param map 异常信息：错误编码，异常类，请求地址，异常错误信息
	 * @param e
	 * @return
	 */
	private ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response, Map map,Exception e) {
		if(ObjectUtil.isNotNull(e)){
			LOG.debug("handleException", e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			map.put("cls", e.getStackTrace()[0]+""); //出错的类
			map.put("url", request.getServletPath()); //请求地址
			map.put("errMsg",  sw.toString());
			map.put("msg", e.getMessage());
			map.put("exc", e.getClass()); //详细异常信息
		}
		//去掉异常信息中的跨站脚本
		map.put("msg",Jsoup.clean(Optional.ofNullable(map.get("msg")).orElse("").toString(), "",new Whitelist(), new Document.OutputSettings().prettyPrint(false)));
		map.put("result",false);
		if (BasicUtil.isAjaxRequest(request)) {
			try {
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter writer = response.getWriter();
				writer.write(JSONObject.toJSONString(map));
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			return new ModelAndView("/error/index", map);
		}
		return null;
	}

}
