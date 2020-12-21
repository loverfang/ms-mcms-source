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
 */
package net.mingsoft.people.action.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultJson;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.people.action.BaseAction;
import net.mingsoft.people.bean.PeopleLoginBean;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.biz.IPeopleUserBiz;
import net.mingsoft.people.constant.e.CookieConstEnum;
import net.mingsoft.people.constant.e.PeopleEnum;
import net.mingsoft.people.constant.e.SessionConstEnum;
import net.mingsoft.people.entity.PeopleEntity;
import net.mingsoft.people.entity.PeopleUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 铭飞会员模块,前端调用（不需要用户登录进行的操作）
 *
 * @author 铭飞开发团队
 * @version 版本号：0.0<br/>
 *          创建日期：2017-8-23 10:10:22<br/>
 *          历史修订：<br/>
 */
@Api("会员模块,前端调用（不需要用户登录进行的操作）")
@Controller("webPeople")
@RequestMapping("/")
public class PeopleAction extends BaseAction {

	/**
	 * 注入用户基础业务层
	 */
	@Autowired
	private IPeopleBiz peopleBiz;

	/**
	 * 注入用户基础业务层
	 */
	@Autowired
	private IPeopleUserBiz peopleUserBiz;

	@ApiOperation(value = "验证码验证,例如流程需要短信验证或邮箱验证，为有效防止恶意发送验证码。提供给ajax异步请求使用,注意：页面提交对验证码表单属性名称必须是rand_code，否则无效")
	@ApiImplicitParam(name = "rand_code", value = "验证码", required =true,paramType="query")
	@PostMapping(value = "/checkCode")
	@ResponseBody
	public void checkCode(HttpServletRequest request, HttpServletResponse response) {
		// 验证码验证 验证码不为null 或 验证码不相等
		if (!checkRandCode()) {
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("rand.code")));
			return;
		}
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "验证用户名、手机号、邮箱是否可用，同一时间只能判断一种，优先用户名称 ,只验证已绑定的用户,建议独立使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleMail", value = "用户邮箱", required =false,paramType="query"),
		@ApiImplicitParam(name = "peopleName", value = "登录帐号", required =false,paramType="query"),
		@ApiImplicitParam(name = "peoplePhone", value = "用户电话", required =false,paramType="query")
	})
	@PostMapping(value = "/check")
	@ResponseBody
	public void check(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request, HttpServletResponse response) {
		PeopleEntity _people = peopleBiz.getByPeople(people, BasicUtil.getAppId());
		if (_people != null) {
			this.outJson(response, true);
		} else {
			this.outJson(response, false);
		}
	}

	@ApiOperation(value = "登录验证,登录必须存在验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "用户密码", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleName", value = "登录帐号", required =true,paramType="query"),
		@ApiImplicitParam(name = "rand_code", value = "验证码", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleAutoLogin", value = " 大于0表示自动登录", required =false,paramType="query"),
	})
	@PostMapping(value = "/checkLogin")
	@ResponseBody
	public void checkLogin(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证码验证 验证码不为null 或 验证码不相等
		if (!this.checkRandCode()) {
			this.outJson(response, null, false, this.getResString("err.error", this.getResString("rand.code")));
			return;
		}
		// 用户名和密码不能为空
		if (StringUtils.isBlank(people.getPeopleName()) || StringUtils.isBlank(people.getPeoplePassword())) {
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("people")));
			return;
		}

		// 根据应用ID和用户名查询用户密码
		int appId = BasicUtil.getAppId();
		PeopleEntity peopleEntity = this.peopleBiz.getEntityByUserName(people.getPeopleName(), appId);
		if (peopleEntity == null) {
			// 用户名或密码错误
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("pepple.no.exist")));
			return;
		}

		// 将用户输入的密码用MD5加密再和数据库中的进行比对
		String peoplePassWord = SecureUtil.md5(people.getPeoplePassword());
		if (peoplePassWord.equals(peopleEntity.getPeoplePassword())) {
			// 登录成功,压入用户session
			this.setPeopleBySession(request, peopleEntity);
			PeopleLoginBean tempPeople = new PeopleLoginBean();
			tempPeople.setPeopleId(peopleEntity.getPeopleId());
			tempPeople.setPeopleName(peopleEntity.getPeopleName());
			tempPeople.setPeopleMail(peopleEntity.getPeopleMail());
			tempPeople.setPeopleState(peopleEntity.getPeopleState());
			tempPeople.setCookie(request.getHeader("cookie"));
			// 判断用户是否点击了自动登录
			if (people.getPeopleAutoLogin() > 0) {
				tempPeople.setPeoplePassword(people.getPeoplePassword());
				BasicUtil.setCookie( response, CookieConstEnum.PEOPLE_COOKIE, JSONObject.toJSONString(tempPeople),
						60 * 60 * 24 * people.getPeopleAutoLogin());
			}
			peopleEntity.setPeopleIp(BasicUtil.getIp());
			//保存用户ip
			peopleBiz.updateEntity(peopleEntity);
			this.outJson(response, null, true, JSONObject.toJSONString(tempPeople));

		} else {
			// 用户名或密码错误
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("pepple.no.exist")));
		}

	}

	@ApiOperation(value = "验证用户是否登录")
	@PostMapping(value = "/checkLoginStatus")
	@ResponseBody
	public void checkLoginStatus(HttpServletRequest request, HttpServletResponse response) {
		PeopleEntity people = this.getPeopleBySession();
		this.outJson(response, people == null ? false : true);
	}

	@ApiOperation(value = "验证重置密码过程中收到的验证码是否正确")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleCode", value = "用户随机验证码", required =true,paramType="query"),
		@ApiImplicitParam(name = "rand_code", value = "验证码", required =true,paramType="query")
	})
	@PostMapping(value = "/checkResetPasswordCode")
	@ResponseBody
	public void checkResetPasswordCode(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证码验证 验证码不为null 或 验证码不相等
		if (StringUtils.isBlank(this.getRandCode()) || !this.checkRandCode()) {
			this.outJson(response, null, false, this.getResString("err.error", this.getResString("rand.code")));
			return;
		}

		PeopleEntity _people = (PeopleEntity) BasicUtil.getSession( SessionConstEnum.PEOPLE_EXISTS_SESSION);
		if (_people == null) {
			// 用户不存在
			this.outJson(response, null, false,
					this.getResString("err.not.exist", this.getResString("people")));
			return;

		}

		LOG.debug(_people.getPeoplePhoneCheck() + ":" + PeopleEnum.PHONE_CHECK.toInt());
		LOG.debug(_people.getPeopleCode() + ":" + people.getPeopleCode());
		// 判断用户验证是否通过\判断用户输入对邮箱验证码是否与系统发送对一致\判断验证码对有效时间
		if (_people.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()
				&& _people.getPeopleCode().equals(people.getPeopleCode())) {
			BasicUtil.setSession( SessionConstEnum.PEOPLE_RESET_PASSWORD_SESSION, _people);
			this.outJson(response, null, true,
					this.getResString("success", this.getResString("people.get.password")));
		} else if (_people.getPeopleMailCheck() == PeopleEnum.MAIL_CHECK.toInt()
				&& _people.getPeopleCode().equals(people.getPeopleCode())) {
			BasicUtil.setSession( SessionConstEnum.PEOPLE_RESET_PASSWORD_SESSION, _people);
			this.outJson(response, null, true,
					this.getResString("success", this.getResString("people.get.password")));
		} else {
			this.outJson(response, null, false,
					this.getResString("fail", this.getResString("people.get.password")));
		}
	}

	@ApiOperation(value = "用户名、邮箱、手机号验证 ,用户重置密码必须使用该接口,适用场景:1、用户注册是对用户名、邮箱或手机号唯一性判断 2、用户取回密码是判断账号是否存在")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePhone", value = "用户电话", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleMail", value = "用户邮箱", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleName", value = "登录帐号", required =true,paramType="query"),
	})
	@PostMapping(value = "/isExists")
	@ResponseBody
	public void isExists(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug(JSONObject.toJSONString(people));
		if (StringUtils.isBlank(people.getPeopleName()) && StringUtils.isBlank(people.getPeoplePhone())
				&& StringUtils.isBlank(people.getPeopleMail())) {
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.name")));
			return;
		}

		// 获取应用ID
		int appId = BasicUtil.getAppId();
		people.setPeopleAppId(appId);
		// 如果接收到mail值，就给mail_check赋值1
		if (!StringUtils.isBlank(people.getPeopleMail())) {
			people.setPeopleMailCheck(PeopleEnum.MAIL_CHECK);
		}
		// 如果接收到phone值，就给phone_check赋值1
		if (!StringUtils.isBlank(people.getPeoplePhone())) {
			people.setPeoplePhoneCheck(PeopleEnum.PHONE_CHECK);
		}
		PeopleEntity _people = (PeopleEntity) this.peopleBiz.getEntity(people);
		if (_people != null) {
			BasicUtil.setSession( SessionConstEnum.PEOPLE_EXISTS_SESSION, _people);
			this.outJson(response, null, true);
			return;
		}
		this.outJson(response, null, false);
	}

	@ApiOperation(value = "用户注册,用户可以更具用名称、手机号、邮箱进行注册")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "登录密码", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleCode", value = "用户随机验证码", required =true,paramType="query"),
		@ApiImplicitParam(name = "peoplePhone", value = "用户电话", required =false,paramType="query"),
		@ApiImplicitParam(name = "peopleMail", value = "用户邮箱", required =false,paramType="query"),
		@ApiImplicitParam(name = "peopleName", value = "登录帐号", required =false,paramType="query"),
	})
	@PostMapping(value = "/register")
	@ResponseBody
	public void register(@ModelAttribute @ApiIgnore PeopleUserEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("people register");
		// 验证码验证 验证码不为null 或 验证码不相等
		if (!checkRandCode()) {
			this.outJson(response, null, false, this.getResString("err.error", this.getResString("rand.code")));
			return;
		}

		// 判断用户信息是否为空
		if (people == null) {
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people")));
			return;
		}

		int appId = BasicUtil.getAppId();
		// 如果用户名不为空表示使用的是账号注册方式
		if (!StringUtils.isBlank(people.getPeopleName())) {
			// 验证用户名
			if (StringUtils.isBlank(people.getPeopleName())) {
				this.outJson(response, null, false,
						this.getResString("err.empty", this.getResString("people.name")));
				return;
			}

			if (people.getPeopleName().contains(" ")) {
				this.outJson(response, null, false,
						this.getResString("people.name") + this.getResString("people.space"));
				return;
			}

			if (!StringUtil.checkLength(people.getPeopleName(), 3, 30)) {
				this.outJson(response, null, false,
						this.getResString("err.length", this.getResString("people.name"), "3", "30"));
				return;
			}

			// 判断用户名是否已经被注册
			PeopleEntity peopleName = this.peopleBiz.getEntityByUserName(people.getPeopleName(), appId);
			if (peopleName != null) {
				this.outJson(response, null, false,
						this.getResString("err.exist", this.getResString("people.name") + peopleName.getPeopleName()));
				return;
			}
		}

		if (!StringUtils.isBlank(people.getPeoplePhone())) {// 验证手机号
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(people.getPeoplePhone(), appId);
			if (peoplePhone != null && peoplePhone.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()) { // 已存在
				this.outJson(response, null, false,
						this.getResString("err.exist", this.getResString("people.phone")));
				return;
			} else {
				Object obj = BasicUtil.getSession( SessionConstEnum.SEND_CODE_SESSION);
				if (obj != null) {
					PeopleEntity _people = (PeopleEntity) obj;
					if (_people.getPeoplePhone().equals(people.getPeoplePhone())) {
						if (_people.getPeopleCode().equals(people.getPeopleCode())) {
							people.setPeoplePhoneCheck(PeopleEnum.PHONE_CHECK);
						} else {
							this.outJson(response, null, false,
									this.getResString("err.error", this.getResString("people.phone.code")));
							return;
						}
					}
				}
			}
		}

		if (!StringUtils.isBlank(people.getPeopleMail())) {// 验证邮箱
			// 检查邮箱格式是否含有空格
			if (people.getPeopleMail().contains(" ")) {
				this.outJson(response, null, false,
						this.getResString("people.mail") + this.getResString("people.space"));
				return;
			}
			PeopleEntity peopleMail = this.peopleBiz.getEntityByUserName(people.getPeopleMail(), appId);
			if (peopleMail != null && peopleMail.getPeopleMailCheck() == PeopleEnum.MAIL_CHECK.toInt()) {
				this.outJson(response, null, false,
						this.getResString("err.exist", this.getResString("people.mail")));
				return;
			} else {
				Object obj = BasicUtil.getSession( SessionConstEnum.SEND_CODE_SESSION);
				if (obj != null) {
					PeopleEntity _people = (PeopleEntity) obj;
					if (_people.getPeopleMail().equals(people.getPeopleMail())) {
						if (_people.getPeopleCode().equals(people.getPeopleCode())) {
							people.setPeopleMailCheck(PeopleEnum.MAIL_CHECK);
						} else {
							this.outJson(response, null, false,
									this.getResString("err.error", this.getResString("people.mail")));
							return;
						}
					}
				}
			}
		}

		// 密码
		if (StringUtils.isBlank(people.getPeoplePassword())) {
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.password")));
			return;
		}

		if (people.getPeoplePassword().contains(" ")) {
			this.outJson(response, null, false,
					this.getResString("people.password") + this.getResString("people.space"));
			return;
		}

		if (people.getPeoplePassword().length() < 6 || people.getPeoplePassword().length() > 30) {
			this.outJson(response, null, false,
					this.getResString("err.length", this.getResString("people.password"), "6", "30"));
			return;
		}
		//如果用户未设置昵称，则昵称默认为用户名
		if(StringUtils.isBlank(people.getPuNickname())){
			people.setPuNickname(people.getPeopleName());
		}
		// 将密码使用MD5加密
		people.setPeoplePassword(SecureUtil.md5(people.getPeoplePassword()));
		people.setPeopleAppId(appId);
		people.setPeopleDateTime(new Date());
		peopleUserBiz.savePeople(people);
		this.outJson(response, null, true,
				this.getResString("success", this.getResString("people.register")));
		LOG.debug("people register ok");
	}

	@ApiOperation(value = "用户重置密码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "登录密码", required =true,paramType="query"),
		@ApiImplicitParam(name = "rand_code", value = "验证码", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleCode", value = "用户随机验证码", required =true,paramType="query"),
	})
	@PostMapping(value = "/resetPassword")
	@ResponseBody
	public void resetPassword(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证码验证 验证码不为null 或 验证码不相等
		if (StringUtils.isBlank(this.getRandCode()) || !this.checkRandCode()) {
			this.outJson(response, null, false, this.getResString("err.error", this.getResString("rand.code")));
			return;
		}

		// 验证新密码的长度
		if (!StringUtil.checkLength(people.getPeoplePassword(), 3, 12)) {
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("people.password")));
			return;
		}

		PeopleEntity _people = (PeopleEntity) BasicUtil.getSession( SessionConstEnum.PEOPLE_RESET_PASSWORD_SESSION);

		if (_people == null) {
			// 用户不存在
			this.outJson(response, null, false,
					this.getResString("err.not.exist", this.getResString("people")));
			return;

		}

		// 判断用户验证是否通过\判断用户输入对邮箱验证码是否与系统发送对一致\判断验证码对有效时间
		if (_people.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()
				&& _people.getPeopleCode().equals(people.getPeopleCode())) {
			_people.setPeoplePassword(SecureUtil.md5(people.getPeoplePassword()));
			peopleBiz.updateEntity(_people);
			LOG.debug("更新密码成功");
			this.outJson(response, null, true,
					this.getResString("success", this.getResString("people.get.password")));
		} else if (_people.getPeopleMailCheck() == PeopleEnum.MAIL_CHECK.toInt()
				&& _people.getPeopleCode().equals(people.getPeopleCode())) {
			_people.setPeoplePassword(SecureUtil.md5(people.getPeoplePassword()));
			peopleBiz.updateEntity(_people);
			LOG.debug("更新密码成功");
			this.outJson(response, null, true,
					this.getResString("success", this.getResString("people.get.password")));
		} else {
			LOG.debug("更新密码失败");
			this.outJson(response, null, false,
					this.getResString("fail", this.getResString("people.get.password")));
		}
	}

	@ApiOperation(value = "自动登录")
	@PostMapping(value = "/autoLogin")
	@ResponseBody
	public void autoLogin(HttpServletRequest request, HttpServletResponse response) {

		// 获取页面上标记为PEOPLE_COOKIE的cookies值
		String cookie = BasicUtil.getCookie( CookieConstEnum.PEOPLE_COOKIE);
		if (StringUtils.isBlank(cookie)) {
			this.outJson(response, false);
			return;
		}

		PeopleEntity people = JSONObject.parseObject(cookie, PeopleEntity.class);
		// 查找到cookies里用户名对应的用户实体
		PeopleEntity peopleEntity = this.peopleBiz.getByPeople(people, BasicUtil.getAppId());
		if (peopleEntity != null) {
			// 登录成功,压入用户session
			setPeopleBySession(request, peopleEntity);
			this.outJson(response, true);
		} else {
			this.outJson(response, false);
		}

	}

	@ApiOperation(value = "用户发送验证码，可以通过邮箱或手机发送")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "receive", value = "接收地址，只能是邮箱或手机号，邮箱需要使用邮箱插件，手机号需要短信插件", required =true,paramType="query"),
		@ApiImplicitParam(name = "modelCode", value = "对应邮件插件的模块编号", required =true,paramType="query"),
		@ApiImplicitParam(name = "thrid", value = "默认sendcloud", required =true,paramType="query"),
		@ApiImplicitParam(name = "type", value = "类型", required =true,paramType="query"),
		@ApiImplicitParam(name = "isSession", value = "true启用session保存code,false 关联用户信息，true一般是当用户手机还不存在系统中时使用，", required =true,paramType="query"),
		@ApiImplicitParam(name = "peoplePhone", value = "用户电话", required =false,paramType="query"),
		@ApiImplicitParam(name = "peopleMail", value = "用户邮箱", required =false,paramType="query"),
		@ApiImplicitParam(name = "peopleName", value = "登录帐号", required =false,paramType="query"),
	})
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/sendCode")
	public void sendCode(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		String receive = request.getParameter("receive");
		String modelCode = request.getParameter("modelCode");
		String thrid = request.getParameter("thrid");
		String type = request.getParameter("type");
		boolean isSession = BasicUtil.getBoolean("isSession");

		if (StringUtils.isBlank(receive)) {
			this.outJson(response, null, false, this.getResString("err.empty", this.getResString("receive")));
			return;
		}
		if (StringUtils.isBlank(modelCode)) {
			this.outJson(response, null, false, this.getResString("err.empty", "modelCode"));
			return;
		}
		if (StringUtils.isBlank(type)) {
			this.outJson(response, null, false, this.getResString("err.empty", this.getResString("type")));
			return;
		}
		// 获取应用ID
		int appId = BasicUtil.getAppId();
		String peopleCode = StringUtil.randomNumber(6);
		// 解密得到的模块编码
		String _modelCode = this.encryptByAES(request, modelCode);
		this.LOG.debug("前端传的"+modelCode);
		this.LOG.debug("解密"+_modelCode);
		Map params = new HashMap();
		params.put("modelCode", _modelCode);
		params.put("receive", receive);
		params.put("thrid", thrid); // 使用第三方平台发送，确保用户能收到
		params.put("content", "{code:'" + peopleCode + "'}");
		params.put("type",type);
		if (isSession) { // 启用session
			if (!this.checkRandCode()) {
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("rand.code")));
				return;
			}

			Object obj = BasicUtil.getSession( SessionConstEnum.SEND_CODE_SESSION);
			if (obj != null) {
				PeopleEntity p = (PeopleEntity) obj;
				if (DateUtil.betweenMs(new Date(), (Date)p.getPeopleCodeSendDate()) == 60) {
					this.outJson(response, null, false, this.getResString("people.code.time.error"));
					return;

				}
			}

			PeopleEntity _people = new PeopleEntity();
			_people.setPeopleCode(peopleCode);
			_people.setPeopleCodeSendDate(Timestamp.valueOf(DateUtil.now()));
			_people.setPeopleMail(receive);
			_people.setPeoplePhone(receive);
			BasicUtil.setSession( SessionConstEnum.SEND_CODE_SESSION, _people);
			String contentt = HttpUtil.post(this.getUrl(request) + "/msend/send.do", params);
			ResultJson rs = JSONObject.parseObject(contentt, ResultJson.class);
			this.outJson(response, JSONObject.toJSONString(rs));
			LOG.debug("send " + receive + ":content " + peopleCode);
			return;
		}
		// 给people赋值（邮箱或电话）
		if (StringUtil.isMobile(receive)) {
			people.setPeoplePhone(receive);
		} else {
			people.setPeopleMail(receive);
		}
		// 判断是否接到用户名，应用于找回密码发送验证码
		if (StringUtils.isBlank(people.getPeopleName()) && this.getPeopleBySession() == null) {
			// 如果接收到mail值，就给mail_check赋值1
			if (!StringUtils.isBlank(people.getPeopleMail())) {
				people.setPeopleMailCheck(PeopleEnum.MAIL_CHECK);
			}
			// 如果接收到phone值，就给phone_check赋值1
			if (!StringUtils.isBlank(people.getPeoplePhone())) {
				people.setPeoplePhoneCheck(PeopleEnum.PHONE_CHECK);
			}
		}


		people.setPeopleAppId(appId);
		// 通过用户名地址和应用id得到用户实体
		PeopleEntity peopleEntity = (PeopleEntity) this.peopleBiz.getEntity(people);
		if (peopleEntity == null) {
			this.outJson(response, null, false,
					this.getResString("err.not.exist", this.getResString("people")));
			return;
		}
		if (peopleEntity.getPeopleUser() != null) {
			CodeBean code = new CodeBean();
			code.setCode(peopleCode);
			code.setUserName(peopleEntity.getPeopleUser().getPuNickname());
			params.put("content", JSONObject.toJSONString(code));
		}

		// 将生成的验证码加入用户实体
		peopleEntity.setPeopleCode(peopleCode);

		// 将当前时间转换为时间戳格式保存进people表
		peopleEntity.setPeopleCodeSendDate(Timestamp.valueOf(DateUtil.now()));
		// 更新该实体
		this.peopleBiz.updateEntity(peopleEntity);

		PeopleEntity _people = (PeopleEntity) BasicUtil.getSession( SessionConstEnum.PEOPLE_EXISTS_SESSION);
		if (_people != null) {
			BasicUtil.setSession( SessionConstEnum.PEOPLE_EXISTS_SESSION, peopleEntity);
		}
		String content = HttpUtil.post(this.getUrl(request) + "/msend/send.do", params);
		LOG.debug("content :" + content);
		ResultJson rs = JSONObject.parseObject(content, ResultJson.class);
		if(rs != null) {
			this.outJson(response, true);
		}

		LOG.debug("send " + receive + ":content " + peopleCode);
	}

	@ApiOperation(value = "验证用户输入的接收验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "receive", value = "接收地址，只能是邮箱或手机号，邮箱需要使用邮箱插件，手机号需要短信插件", required =true,paramType="query"),
		@ApiImplicitParam(name = "code", value = "接收到的验证码", required =true,paramType="query"),
	})
	@PostMapping(value = "/checkSendCode")
	public void checkSendCode(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		String code = request.getParameter("code");
		String receive = request.getParameter("receive");
		Boolean isMobile = Validator.isMobile(receive);
		Boolean isEmail = Validator.isEmail(receive);
		if (isMobile) {
			// 手机验证码
			if (StringUtils.isBlank(code)) {
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("people.phone.code")));
				return;
			}
			people.setPeoplePhone(receive);
		}
		if (isEmail) {
			// 邮箱验证码
			if (StringUtils.isBlank(code)) {
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("people.mail.code")));
				return;
			}
			people.setPeopleMail(receive);
		}
		// 获取应用ID
		int appId = BasicUtil.getAppId();
		people.setPeopleAppId(appId);
		// 根据邮箱地址查找用户实体
		PeopleEntity peopleEntity = (PeopleEntity) this.peopleBiz.getEntity(people);

		// 在注册流程，在发送验证码的时数据库可能还不存在用户信息
		if (BasicUtil.getSession(SessionConstEnum.SEND_CODE_SESSION) != null) {
			peopleEntity = (PeopleEntity) BasicUtil.getSession(SessionConstEnum.SEND_CODE_SESSION);
			// 判断用户输入的随机码是否正确
			if (!peopleEntity.getPeopleCode().equals(code)) {
				if (isMobile) {
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.phone.code")));
					return;
				}else{
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.mail.code")));
					return;
				}
			} else {
				this.outJson(response, null, true);
			}
		} else {
			if (isMobile) {
				// 如果用户已经绑定过手机直接返回错误
				if (peopleEntity.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()) {
					this.outJson(response, null, false);
					return;
				}
			} else {
				// 如果用户已经绑定过邮箱直接返回错误
				if (peopleEntity.getPeopleMailCheck() == PeopleEnum.MAIL_CHECK.toInt()) {
					this.outJson(response, null, false);
					return;
				}
			}

			// 得到发送验证码时间，并转换为String类型
			String date = peopleEntity.getPeopleCodeSendDate().toString();

			// 如果发送时间和当前时间只差大于30分钟，则返回false
			if (DateUtil.betweenMs(new Date(), DateUtil.parse(date)) > 60 * 60 * 24) {
				this.outJson(response, null, false, this.getResString("people.msg.code.error"));
				return;
			}

			// 判断用户输入的随机码是否正确
			if (!peopleEntity.getPeopleCode().equals(code)) {
				if (isMobile) {
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.phone.code")));
					return;
				}else{
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.mail.code")));
					return;
				}
			}

			// 将随机码在数据库中清空
			peopleEntity.setPeopleCode("");
			if (StringUtil.isMobile(receive)) {
				peopleEntity.setPeoplePhoneCheck(PeopleEnum.PHONE_CHECK);
			} else {
				peopleEntity.setPeopleMailCheck(PeopleEnum.MAIL_CHECK);
			}
			peopleBiz.updateEntity(peopleEntity);
			this.outJson(response, null, true);

		}

	}

	@ApiOperation(value = "解绑邮箱-> 验证用户输入的接收验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "接收到的验证码", required =true,paramType="query"),
		@ApiImplicitParam(name = "receive", value = "接收地址，只能是邮箱或手机号，邮箱需要使用邮箱插件，手机号需要短信插件", required =true,paramType="query")
	})
	@PostMapping(value = "/cancelBind")
	public void cancelBind(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		String code = request.getParameter("code");
		String receive = request.getParameter("receive");
		Boolean isMobile = Validator.isMobile(receive);
		Boolean isEmail = Validator.isEmail(receive);

		if (isMobile) {
			// 验证码
			if (StringUtils.isBlank(code)) {
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("people.phone.code")));
				return;
			}
			people.setPeoplePhone(receive);
		}
		if (isEmail) {
			// 验证码
			if (StringUtils.isBlank(code)) {
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("people.mail.code")));
				return;
			}
			people.setPeopleMail(receive);
		}
		// 获取应用ID
		int appId = BasicUtil.getAppId();
		people.setPeopleAppId(appId);
		// 根据用户名和邮箱或手机号查找用户实体
		PeopleEntity peopleEntity = (PeopleEntity) this.peopleBiz.getEntity(people);

		// 在注册流程，在发送验证码的时数据库可能还不存在用户信息
		if (BasicUtil.getSession(SessionConstEnum.SEND_CODE_SESSION) != null) {
			peopleEntity = (PeopleEntity) BasicUtil.getSession(SessionConstEnum.SEND_CODE_SESSION);
			// 判断用户输入的随机码是否正确
			if (!peopleEntity.getPeopleCode().equals(code)) {
				if (isMobile) {
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.phone.code")));
					return;
				}else{
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.mail.code")));
					return;
				}
			} else {
				this.outJson(response, null, true);
			}
		} else {
			if (isMobile) {
				// 如果用户未绑定过手机直接返回错误
				if (peopleEntity.getPeoplePhoneCheck() == PeopleEnum.PHONE_NO_CHECK.toInt()) {
					this.outJson(response, null, false);
					return;
				}
			} else {
				// 如果用户未绑定过邮箱直接返回错误
				if (peopleEntity.getPeopleMailCheck() == PeopleEnum.MAIL_NO_CHECK.toInt()) {
					this.outJson(response, null, false);
					return;
				}
			}

			// 得到发送验证码时间，并转换为String类型
			String date = peopleEntity.getPeopleCodeSendDate().toString();

			// 如果发送时间和当前时间只差大于30分钟，则返回false

			if (DateUtil.betweenMs(new Date(), DateUtil.parse(date)) > 60 * 60 * 24) {
				this.outJson(response, null, false, this.getResString("people.msg.code.error"));
				return;
			}

			// 判断用户输入的随机码是否正确
			if (!peopleEntity.getPeopleCode().equals(code)) {
				if (isMobile) {
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.phone.code")));
					return;
				}else{
					this.outJson(response, null, false,
							this.getResString("err.error", this.getResString("people.mail.code")));
					return;
				}
			}

			// 将随机码在数据库中清空
			peopleEntity.setPeopleCode("");
			if (StringUtil.isMobile(receive)) {
				peopleEntity.setPeoplePhoneCheck(PeopleEnum.PHONE_NO_CHECK);
			} else {
				peopleEntity.setPeopleMailCheck(PeopleEnum.MAIL_NO_CHECK);
			}
			peopleBiz.updateEntity(peopleEntity);
			this.outJson(response, null, true);

		}

	}

}

// 创建一个bean方便邮件发送，避免userName特色字符导致json格式转换失败
class CodeBean {
	String code;
	String userName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
