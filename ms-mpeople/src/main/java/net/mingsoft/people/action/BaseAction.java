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
 */package net.mingsoft.people.action;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.constant.e.SessionConstEnum;
import net.mingsoft.people.entity.PeopleEntity;

/**
 *
 * 基础类
 * @author 铭飞开发团队
 * @version
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
public class BaseAction extends net.mingsoft.mdiy.action.BaseAction {
	/**
	 * 读取国际化资源文件
	 * 
	 * @param key 键值
	 * @return字符串
	 */
	protected String getResString(String key) {
		return super.getResString(key, net.mingsoft.people.constant.Const.RESOURCES);
	}

	/**
	 * 读取国际化资源文件
	 * 
	 * @param key
	 *            键值
	 * @param fullStrs
	 *            需填充的值
	 * @return 字符串
	 */
	protected String getResString(String key, String... fullStrs) {
		return super.getResString(key, net.mingsoft.people.constant.Const.RESOURCES, fullStrs);
	}
	
	/**
	 * 获取用户session.没有返回null
	 */
	protected PeopleEntity getPeopleBySession() {
		Object obj = BasicUtil.getSession(SessionConstEnum.PEOPLE_SESSION);
		if (obj != null && obj instanceof PeopleEntity) {
			return (PeopleEntity) obj;
		} else if(obj instanceof String) {
			return JSONObject.parseObject(obj.toString(), PeopleEntity.class) ;
		}
		return null;
	}

	/**
	 * 设置用户session
	 * 
	 * @param request
	 * @param people
	 *            用户实体
	 */
	protected void setPeopleBySession(HttpServletRequest request, PeopleEntity people) {
		BasicUtil.setSession( SessionConstEnum.PEOPLE_SESSION, people);
	}

	/**
	 * 移除用户session
	 * 
	 * @param request
	 */
	protected void removePeopleBySession(HttpServletRequest request) {
		BasicUtil.removeSession(SessionConstEnum.PEOPLE_SESSION);
	}
	
	

}
