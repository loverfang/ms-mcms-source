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
 */package net.mingsoft.people.action.people;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.IPageBiz;
import net.mingsoft.mdiy.entity.PageEntity;
import net.mingsoft.mdiy.util.ParserUtil;
import net.mingsoft.people.action.BaseAction;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.constant.e.CookieConstEnum;
import net.mingsoft.people.constant.e.PeopleEnum;
import net.mingsoft.people.constant.e.SessionConstEnum;
import net.mingsoft.people.entity.PeopleEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 * 用户基础信息表管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Api(value = "用户基础信息接口(需用户登录)")
@Controller("peopleMain")
@RequestMapping("/people")
public class PeopleAction extends BaseAction {
	/**
	 * 注入用户基础业务层
	 */
	@Autowired
	private IPeopleBiz peopleBiz;

	/**
	 * 自定义页面业务层
	 */
	@Autowired
	private IPageBiz pageBiz;

	@ApiOperation(value = "重置密码接口")
    @ApiImplicitParam(name = "peoplePassword", value = "用户密码", required = true,paramType="query")
	@PostMapping(value = "/resetPassword")
	@ResponseBody
	public void resetPassword(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证新密码的长度
		if (!StringUtil.checkLength(people.getPeoplePassword(), 6, 30)) {
			this.outJson(response, null, false,
					this.getResString("err.length", this.getResString("people.password"), "6", "20"));
			return;
		}
		if (StringUtils.isBlank(people.getPeoplePassword())) {
			// 用户或密码不能为空
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.password")));
			return;
		}
		// 获取用户session
		PeopleEntity _people = (PeopleEntity)peopleBiz.getEntity(this.getPeopleBySession().getPeopleId());
		// 将用户输入的原始密码用MD5加密再和数据库中的进行比对
		String peoplePassWord = DigestUtil.md5Hex(people.getPeoplePassword(), Const.UTF8);
		// 执行修改
		_people.setPeoplePassword(peoplePassWord);
		this.peopleBiz.updateEntity(_people);
		this.outJson(response, null, true);
	}

	@PostMapping(value = "/changePassword")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "用户密码", required = true,paramType="query"),
		@ApiImplicitParam(name = "peopleOldPassword", value = "用户旧密码", required = true,paramType="query")
	})
	@ResponseBody
	public void changePassword(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证码验证 验证码不为null 或 验证码不相等
		if (!checkRandCode()) {
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("rand.code")));
			return;
		}
		if (StringUtils.isBlank(people.getPeoplePassword())) {
			// 用户或密码不能为空
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.password")));
			return;
		}if (StringUtils.isBlank(people.getPeoplePassword())) {
			// 用户或密码不能为空
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.password")));
			return;
		}


		// 验证新密码的长度
		if (!StringUtil.checkLength(people.getPeoplePassword(), 6, 30)) {
			this.outJson(response, null, false,
					this.getResString("err.length", this.getResString("people.password"), "6", "20"));
			return;
		}

		// 验证新密码的合法：空格字符
		if (people.getPeoplePassword().contains(" ")) {
			this.outJson(response, null, false,
					 this.getResString("people.password") + this.getResString("people.space"));
			return;
		}

		// 获取用户session
		PeopleEntity _people =new PeopleEntity();
		_people.setPeopleId(getPeopleBySession().getPeopleId());
		PeopleEntity curPeople = (PeopleEntity)peopleBiz.getEntity(_people);
		if (!curPeople.isNewUser()&&StringUtils.isBlank(people.getPeopleOldPassword())) {
			// 用户或密码不能为空
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.old.password")));
			return;
		}
		if (!curPeople.isNewUser()&&!curPeople.getPeoplePassword().equals(SecureUtil.md5(people.getPeopleOldPassword()))) {
			// 用户或密码不能为空
			this.outJson(response, null, false,
					this.getResString("err.error", this.getResString("people.password")));
			return;
		}
		// 将用户输入的原始密码用MD5加密再和数据库中的进行比对
		String peoplePassWord = DigestUtil.md5Hex(people.getPeoplePassword(), Const.UTF8);
		// 执行修改
		curPeople.setPeoplePassword(peoplePassWord);
		this.peopleBiz.updateEntity(curPeople);
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "修改手机号接口")
	@PostMapping(value = "/changePhone")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "用户密码", required = true,paramType="query"),
		@ApiImplicitParam(name = "peopleCode", value = "手机验证码", required = true,paramType="query")
	})
	@ResponseBody
	public void changePhone(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取用户发送验证码session
		PeopleEntity _people = (PeopleEntity) BasicUtil.getSession( SessionConstEnum.SEND_CODE_SESSION);
		PeopleEntity peopleSession = this.getPeopleBySession();
		// 判断手机是否已经存在
		if (StringUtils.isBlank(people.getPeoplePhone())) {
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.phone")));
			return;
		}
		// 如果手机号码已经绑定过就需要验证手机短信吗
		if (_people.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()) {
			// 判断用户输入的验证是否正确
			if (!people.getPeopleCode().equals(_people.getPeopleCode())) {
				// 返回错误信息
				this.outJson(response, null, false,
						this.getResString("err.error", this.getResString("people.phone.code")));
				return;
			}
		}
		people.setPeopleId(peopleSession.getPeopleId());
		people.setPeoplePhone(people.getPeoplePhone());
		peopleBiz.updateEntity(people);
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "验证用户短信、邮箱验证码是否正确接口")
	@ApiImplicitParam(name = "peopleCode", value = "手机验证码", required = true,paramType="query")
	@PostMapping(value = "/checkPeopleCode")
	@ResponseBody
	public void checkPeopleCode(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取session中的用户实体
		PeopleEntity _people = this.getPeopleBySession();
		PeopleEntity _temp = peopleBiz.getByPeople(_people, BasicUtil.getAppId());
		if (people.getPeopleCode().equals(_temp.getPeopleCode())) {
			this.outJson(response, null, true);
		} else {
			this.outJson(response, null, false);
		}
	}

	@ApiOperation(value = "读取当前登录用户的基本信息 用户信息接口")
	@GetMapping("/info")
	@ResponseBody
	public void info(HttpServletRequest request, HttpServletResponse response) {
		// 得到登录后session中的用户实体值
		PeopleEntity people = (PeopleEntity) this.getPeopleBySession();
		// 返回用户信息
		this.outJson(response, people,"peopleOldPassword","peoplePassword");
	}

	@ApiOperation(value = "退出登录接口")
	@GetMapping(value = "/quit")
	@ResponseBody
	public void quit(HttpServletRequest request, HttpServletResponse response) {
		// 移除当前用户session
		this.removePeopleBySession(request);
		BasicUtil.setCookie( response, CookieConstEnum.PEOPLE_COOKIE, null);
		this.outJson(response, true);
	}

	@ApiOperation(value = "更新用户邮箱或手机号接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleMail", value = "用户邮箱", required = false,paramType="query"),
		@ApiImplicitParam(name = "peoplePhone", value = "手机号", required = false,paramType="query")
	})
	@PostMapping(value = "/update")
	public void update(@ModelAttribute @ApiIgnore PeopleEntity people, HttpServletRequest request, HttpServletResponse response) {
		if (people == null) {
			// 未填写信息返回错误信息
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people")));
			return;
		}
		int peopleId = this.getPeopleBySession().getPeopleId();
		PeopleEntity _people = (PeopleEntity)peopleBiz.getEntity(peopleId);

		if (_people.getPeopleMailCheck() == PeopleEnum.MAIL_CHECK.toInt()) {
			people.setPeopleMail(null);
		}
		if (_people.getPeoplePhoneCheck() == PeopleEnum.PHONE_CHECK.toInt()) {
			people.setPeoplePhone(null);
		}
		people.setPeopleName(null);
		people.setPeopleId(peopleId);
		this.peopleBiz.updateEntity(people);
		// 返回更新成功
		this.outJson(response, null, true, this.getResString("success"));
	}

	@ApiOperation(value = "前端会员中心所有页面都可以使用该方法 支持参数传递与解析，例如页面中有参数id=10 传递过来，跳转页面可以使用{id/}获取该参数")
	@ApiImplicitParam(name = "diy", value = "id", required = true,paramType="path")
	@GetMapping(value = "/{diy}")
	public void diy(@PathVariable(value = "diy") String diy, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> map = BasicUtil.assemblyRequestMap();
		//动态解析
		map.put(ParserUtil.IS_DO,false);
		//设置动态请求的模块路径
		map.put(ParserUtil.MODEL_NAME, "mcms");
		map.put(ParserUtil.URL, BasicUtil.getUrl());
		//解析后的内容
		String content = "";
		PageEntity page = new PageEntity();
		page.setPageKey("people/"+diy);
		//根据请求路径查询模版文件
		PageEntity _page = (PageEntity) pageBiz.getEntity(page);
		if(ObjectUtil.isNull(_page)){
			this.outJson(resp, false,getResString("templet.file"));
			return;
		}
		try {
			content = ParserUtil.generate(_page.getPagePath(), map);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.outString(resp, content);
	}

}
