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
package net.mingsoft.basic.entity;

import net.mingsoft.base.entity.BaseEntity;
/**
* 系统日志实体
* @author 铭飞开发团队
* 创建日期：2019-11-20 10:54:49<br/>
* 历史修订：<br/>
*/
public class SystemLogEntity extends BaseEntity {

private static final long serialVersionUID = 1574218489983L;

	/**
	* 标题
	*/
	private String title;
	/**
	* IP
	*/
	private String ip;
	/**
	* 业务类型
	*/
	private String businessType;
	/**
	* 请求方法
	*/
	private String method;
	/**
	* 请求方式
	*/
	private String requestMethod;
	/**
	* 用户类型
	*/
	private String userType;
	/**
	* 操作人员
	*/
	private String user;
	/**
	* 请求地址
	*/
	private String url;
	/**
	* 请求地址
	*/
	private String location;
	/**
	* 请求参数
	*/
	private String param;
	/**
	* 返回参数
	*/
	private String result;
	/**
	* 请求状态
	*/
	private String status;
	/**
	* 错误消息
	*/
	private String errorMsg;

	/**
	 *  该角色的APPID
	 */
	private int appId;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	/**
	* 设置标题
	*/
	public void setTitle(String title) {
	this.title = title;
	}

	/**
	* 获取标题
	*/
	public String getTitle() {
	return this.title;
	}
	/**
	* 设置IP
	*/
	public void setIp(String ip) {
	this.ip = ip;
	}

	/**
	* 获取IP
	*/
	public String getIp() {
	return this.ip;
	}
	/**
	* 设置业务类型
	*/
	public void setBusinessType(String businessType) {
	this.businessType = businessType;
	}

	/**
	* 获取业务类型
	*/
	public String getBusinessType() {
	return this.businessType;
	}
	/**
	* 设置请求方法
	*/
	public void setMethod(String method) {
	this.method = method;
	}

	/**
	* 获取请求方法
	*/
	public String getMethod() {
	return this.method;
	}
	/**
	* 设置请求方式
	*/
	public void setRequestMethod(String requestMethod) {
	this.requestMethod = requestMethod;
	}

	/**
	* 获取请求方式
	*/
	public String getRequestMethod() {
	return this.requestMethod;
	}
	/**
	* 设置用户类型
	*/
	public void setUserType(String userType) {
	this.userType = userType;
	}

	/**
	* 获取用户类型
	*/
	public String getUserType() {
	return this.userType;
	}
	/**
	* 设置操作人员
	*/
	public void setUser(String user) {
	this.user = user;
	}

	/**
	* 获取操作人员
	*/
	public String getUser() {
	return this.user;
	}
	/**
	* 设置请求地址
	*/
	public void setUrl(String url) {
	this.url = url;
	}

	/**
	* 获取请求地址
	*/
	public String getUrl() {
	return this.url;
	}
	/**
	* 设置请求地址
	*/
	public void setLocation(String location) {
	this.location = location;
	}

	/**
	* 获取请求地址
	*/
	public String getLocation() {
	return this.location;
	}
	/**
	* 设置请求参数
	*/
	public void setParam(String param) {
	this.param = param;
	}

	/**
	* 获取请求参数
	*/
	public String getParam() {
	return this.param;
	}
	/**
	* 设置返回参数
	*/
	public void setResult(String result) {
	this.result = result;
	}

	/**
	* 获取返回参数
	*/
	public String getResult() {
	return this.result;
	}
	/**
	* 设置请求状态
	*/
	public void setStatus(String status) {
	this.status = status;
	}

	/**
	* 获取请求状态
	*/
	public String getStatus() {
	return this.status;
	}
	/**
	* 设置错误消息
	*/
	public void setErrorMsg(String errorMsg) {
	this.errorMsg = errorMsg;
	}

	/**
	* 获取错误消息
	*/
	public String getErrorMsg() {
	return this.errorMsg;
	}
}
