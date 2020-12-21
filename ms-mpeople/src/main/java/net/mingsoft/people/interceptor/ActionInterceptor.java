/**
The MIT License (MIT) * Copyright (c) 2016 铭飞科技

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
 *//**
The MIT License (MIT) * Copyright (c) 2016 铭飞科技(mingsoft.net)

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

package net.mingsoft.people.interceptor;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import net.mingsoft.basic.constant.ErrorCodeEnum;

import org.apache.commons.lang3.StringUtils;

import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.exception.BusinessException;
import net.mingsoft.basic.interceptor.BaseInterceptor;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.constant.e.CookieConstEnum;
import net.mingsoft.people.entity.PeopleEntity;

/**
 *
 * 铭飞MS平台－会员拦截
 * @author 铭飞开发团队
 * @version
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
public class ActionInterceptor extends BaseInterceptor {

	private String loginUrl;
	private String mobileLoginUrl;

	public ActionInterceptor(){

	}
	public ActionInterceptor(String loginUrl,String mobileLoginUrl){
		this.loginUrl = loginUrl;
		this.mobileLoginUrl = mobileLoginUrl;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//如果是移动端请求使用移动端你的登录地址
		if(BasicUtil.isMobileDevice()){
			loginUrl = mobileLoginUrl;
		}
		if (ObjectUtil.isNull(BasicUtil.getSession(net.mingsoft.people.constant.e.SessionConstEnum.PEOPLE_SESSION))) {
			//判断是否为ajax请求，默认不是
			if(StringUtils.isBlank(loginUrl) || !StringUtils.isBlank(request.getHeader("x-requested-with")) && request.getHeader("x-requested-with").equals("XMLHttpRequest")){
				throw new BusinessException(ErrorCodeEnum.CLIENT_UNAUTHORIZED.toString(),"登录失效");
			} else {
				String login = URLDecoder.decode(loginUrl,Const.UTF8);
				String backUrl = BasicUtil.getUrl()+request.getServletPath();
				if(request.getQueryString()!=null) {
					backUrl +="?"+request.getQueryString();
				}
				if(login.indexOf("?") > 0) {
					login = login+"&url="+URLEncoder.encode(backUrl,Const.UTF8);
				} else {
					login = login+"?url="+URLEncoder.encode(backUrl,Const.UTF8);
				}
				response.sendRedirect(login);
				return false;
			}

		} else {
			return true;
		}
	}
}
