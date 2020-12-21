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
 *
 */
package net.mingsoft.people.action.people;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.action.BaseAction;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.biz.IPeopleUserBiz;
import net.mingsoft.people.constant.e.PeopleEnum;
import net.mingsoft.people.entity.PeopleEntity;
import net.mingsoft.people.entity.PeopleUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 铭飞会员系统 详细的用户信息
 * @author 铭飞开发团队
 * @version
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Api(value = "详细用户信息接口")
@Controller("webPeopleUser")
@RequestMapping("/people/user")
public class PeopleUserAction extends BaseAction {

	/**
	 * 注入用户详细信息业务层
	 */
	@Autowired
	private IPeopleUserBiz peopleUserBiz;

	/**
	 * 注入用户业务层
	 */
	@Autowired
	private IPeopleBiz peopleBiz;

	@ApiOperation(value = "查询人员表详情接口")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "people", value = "当前用户", required =true,paramType="query"),
	})
	@GetMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute @ApiIgnore PeopleUserEntity people, HttpServletResponse response, HttpServletRequest request, ModelMap model, BindingResult result) {
		people.setPeopleAppId(BasicUtil.getAppId());
		BasicUtil.startPage();
		PeopleUserEntity _people = peopleUserBiz.getByEntity(people);
		this.outJson(response, _people ,"peoplePassword","peopleOldPassword");
	}

	@ApiOperation(value = "读取当前登录用户的基本信息接口")
	@GetMapping("/info")
	public void info(HttpServletRequest request, HttpServletResponse response) {
		// 获取用户session
		PeopleEntity people = this.getPeopleBySession();
		PeopleUserEntity peopleUser = (PeopleUserEntity) this.peopleUserBiz.getEntity(people.getPeopleId());
		if (peopleUser == null) {
			// 没用用户详细信息
			this.outJson(response, null, false);
			return;
		}
		// 返回用户详细信息
		this.outJson(response, peopleUser,"peopleOldPassword","peoplePassword");
	}

	@ApiOperation(value = "更新用户信息接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "puRealName", value = "用户真实名称", required =false,paramType="query"),
		@ApiImplicitParam(name = "puAddress", value = "用户地址", required =false,paramType="query"),
		@ApiImplicitParam(name = "puIcon", value = "用户头像图标地址", required =false,paramType="query"),
		@ApiImplicitParam(name = "puNickname", value = "用户昵称", required =false,paramType="query"),
		@ApiImplicitParam(name = "puSex", value = "用户性别(0.未知、1.男、2.女)", required =false,paramType="query"),
		@ApiImplicitParam(name = "puBirthday", value = "用户出生年月日", required =false,paramType="query"),
		@ApiImplicitParam(name = "puCard", value = "身份证", required =false,paramType="query"),
		@ApiImplicitParam(name = "puProvince", value = "省", required =false,paramType="query"),
		@ApiImplicitParam(name = "puCity", value = "城市", required =false,paramType="query"),
		@ApiImplicitParam(name = "puDistrict", value = "区", required =false,paramType="query"),
		@ApiImplicitParam(name = "puStreet", value = "街道", required =false,paramType="query"),
	})
	@PostMapping("/update")
	public void update(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser, HttpServletRequest request,
			HttpServletResponse response) {
		if (peopleUser == null) {
			// 未填写信息返回错误信息
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people")));
			return;
		}
		if(StringUtils.isNotEmpty(peopleUser.getPeopleMail())){
			PeopleEntity peopleByMail = peopleBiz.getEntityByMailOrPhone(peopleUser.getPeopleMail(), BasicUtil.getAppId());
			if(ObjectUtil.isNotNull(peopleByMail)){
				this.outJson(response, null, false, this.getResString("err.exist", this.getResString("people.mail")));
				return;
			}
		}
		int peopleId = this.getPeopleBySession().getPeopleId();
		peopleUser.setPeopleId(peopleId);
		peopleUser.setPuPeopleId(peopleId);
		//以免用户修改登录账号信息，邮箱、手机
		peopleUser.setPeoplePhone(null);
		peopleUser.setPeoplePhoneCheck(-1);
		peopleUser.setPeopleName(null);
		peopleUser.setPeopleMail(null);
		peopleUser.setPeopleMailCheck(-1);
		PeopleUserEntity pue = (PeopleUserEntity) this.peopleUserBiz.getEntity(peopleUser.getPeopleId());
		if (pue.getPuPeopleId() == 0) {
			this.peopleUserBiz.saveEntity(peopleUser);
		} else {
			peopleUserBiz.updatePeople(peopleUser);
		}
		// 返回更新成功
		this.outJson(response, null, true, this.getResString("success"));
	}
	@ApiOperation(value = "更新邮箱")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleMail", value = "邮箱", required =false,paramType="query"),
	})
	@PostMapping("/updateMail")
	public void updateMail(String peopleMail, HttpServletRequest request,
			HttpServletResponse response) {
		if (StrUtil.isBlank(peopleMail)) {
			// 未填写信息返回错误信息
			this.outJson(response, null, false,
					this.getResString("err.empty", this.getResString("people.mail")));
			return;
		}
		if(StringUtils.isNotEmpty(peopleMail)){
			PeopleEntity peopleByMail = peopleBiz.getEntityByMailOrPhone(peopleMail, BasicUtil.getAppId());
			if(ObjectUtil.isNotNull(peopleByMail)){
				this.outJson(response, null, false, this.getResString("err.exist", this.getResString("people.mail")));
				return;
			}
		}
		int peopleId = this.getPeopleBySession().getPeopleId();
		PeopleUserEntity peopleUser =new PeopleUserEntity();
		peopleUser.setPeopleId(peopleId);
		peopleUser.setPuPeopleId(peopleId);
		//以免用户修改登录账号信息，邮箱、手机
		peopleUser.setPeopleMail(peopleMail);
		peopleUser.setPeopleMailCheck(PeopleEnum.MAIL_CHECK);
		peopleBiz.updatePeople(peopleUser);
		// 返回更新成功
		this.outJson(response, null, true, this.getResString("success"));
	}

	/**
	 * 推荐使用update
	 */
	@Deprecated
	@ApiOperation(value = "保存用户头像(包含头像)接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "puIcon", value = "用户头像", required =true,paramType="query"),
		@ApiImplicitParam(name = "peopleMail", value = "用户手机", required =false,paramType="query"),
		@ApiImplicitParam(name = "peoplePhone", value = "用户邮箱", required =false,paramType="query")
	})
	@PostMapping("/saveUserIcon")
	public void saveUserIcon(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser, HttpServletRequest request,
			HttpServletResponse response) {
		if (peopleUser == null) {
			// 未填写信息返回错误信息
			this.outJson(response, null, false,
					this.getResString("people.user.msg.null.error", net.mingsoft.people.constant.Const.RESOURCES));
			return;
		}
		// 获取用户session
		PeopleEntity people = this.getPeopleBySession();
		String imgPath = peopleUser.getPuIcon().trim(); // 新图片路径
		if (!StringUtils.isBlank(imgPath)) {
			peopleUser.setPuIcon(imgPath);
		}

		peopleUser.setPeopleAppId(people.getPeopleAppId());
		peopleUser.setPeopleId(people.getPeopleId());
		this.peopleUserBiz.saveEntity(peopleUser);
		// 更新手机和电子邮件
		if (!StringUtils.isBlank(peopleUser.getPeopleMail())) {
			people.setPeopleMail(peopleUser.getPeopleMail());
		}
		if (!StringUtils.isBlank(peopleUser.getPeoplePhone())) {
			people.setPeoplePhone(peopleUser.getPeoplePhone());
		}
		this.peopleBiz.updateEntity(people);
		// 返回用户添加成功
		this.outJson(response, null, true,
				this.getResString("people.user.save.msg.success", net.mingsoft.people.constant.Const.RESOURCES));
	}

	/**
	 * 推荐使用update
	 */
	@Deprecated
	@ApiOperation(value = "更新用户信息(包含头像)接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "puIcon", value = "用户头像", required =false,paramType="query")
	})
	@PostMapping("/updateUserIcon")
	public void updateUserIcon(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser, HttpServletRequest request,
			HttpServletResponse response) {
		if (peopleUser == null) {
			// 未填写信息返回错误信息
			this.outJson(response, null, false,
					this.getResString("people.user.msg.null.error", net.mingsoft.people.constant.Const.RESOURCES));
			return;
		}
		// 获取用户session
		PeopleEntity people = this.getPeopleBySession();
		PeopleUserEntity oldPeopleUser = (PeopleUserEntity) peopleUserBiz.getEntity(people.getPeopleId());
		String imgPath = peopleUser.getPuIcon().trim();
		if (!StringUtils.isBlank(imgPath)) {
			oldPeopleUser.setPuIcon(imgPath);
		}

		this.peopleUserBiz.updatePeople(oldPeopleUser);
		// 返回更新成功
		this.outJson(response, null, true,
				this.getResString("people.user.update.msg.success", net.mingsoft.people.constant.Const.RESOURCES));
	}


}
