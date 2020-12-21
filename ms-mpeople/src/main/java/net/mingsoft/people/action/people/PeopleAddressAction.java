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

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.action.BaseAction;
import net.mingsoft.people.biz.IPeopleAddressBiz;
import net.mingsoft.people.entity.PeopleAddressEntity;
import net.mingsoft.people.entity.PeopleEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * 普通用户收货地址信息控制层(外部请求接口)
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Api(value = "普通用户收货地址信息接口")
@Controller("peopleAddress")
@RequestMapping("/people/address")
public class PeopleAddressAction extends BaseAction {
	/**
	 * 注入用户收货地址业务层
	 */
	@Autowired
	private IPeopleAddressBiz peopleAddressBiz;

    @ApiOperation(value = "用户收货地址列表接口")
	@GetMapping("/list")
	public void list(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {
		// 通过session得到用户实体
		PeopleEntity people = this.getPeopleBySession();
		// 通过用户id和站点id查询用户收货地址列表
		peopleAddress.setPeopleAddressAppId(BasicUtil.getAppId());
		peopleAddress.setPeopleAddressPeopleId(people.getPeopleId());
		List list = peopleAddressBiz.query(peopleAddress);
		this.outJson(response, JSONArray.toJSONString(list));
	}

	@ApiOperation(value = "保存用户收货地址接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "peopleAddressConsigneeName", value = "用户收货人姓名", required =true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressProvince", value = "收货人所在的省", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressProvinceId", value = "收货人所在的省编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressCity", value = "收货人所在的市", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressCityId", value = "收货人所在的市编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDistrict", value = "收货人所在区", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDistrictId", value = "收货人所在区编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressStreet", value = "街道", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressStreetId", value = "街道编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressAddress", value = "收货人的详细收货地址", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressMail", value = "收货人邮箱", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressPhone", value = "收货人手机", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDefault", value = "是否是收货人最终收货地址。0代表是，1代表不是，默认为0", required = false,paramType="query")
    })
	@PostMapping(value="/save")
	public void save(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {

		// 通过session得到用户实体
		PeopleEntity peopleEntity = this.getPeopleBySession();
		// 判断用户信息是否为空
		if (peopleAddress == null) {
			this.outJson(response,null, false, this.getResString("people.msg.null.error"),
					this.getResString("people.msg.null.error"));
			return;
		}
		// 验证手机号
		if (StringUtils.isBlank(peopleAddress.getPeopleAddressPhone())) {
			this.outJson(response,null, false,
					this.getResString("people.msg.phone.error", net.mingsoft.people.constant.Const.RESOURCES));
			return;
		}
		// 验证邮箱
		if (!StringUtils.isBlank(peopleAddress.getPeopleAddressMail())) {
			if (!Validator.isEmail(peopleAddress.getPeopleAddressMail())) {
				this.outJson(response,null, false,
						this.getResString("people.msg.mail.error", net.mingsoft.people.constant.Const.RESOURCES));
				return;
			}
		}
		// 判断省是否为空
		if (StringUtils.isBlank(peopleAddress.getPeopleAddressProvince())) {
			this.outJson(response,null, false, this.getResString("people.user.msg.null.error"));
			return;
		}
		// 注入用户id
		peopleAddress.setPeopleAddressPeopleId(peopleEntity.getPeopleId());
		// 注入站点id
		peopleAddress.setPeopleAddressAppId(BasicUtil.getAppId());
		// 进行保存
		peopleAddressBiz.saveEntity(peopleAddress);
		this.outJson(response, null, true, JSONObject.toJSONString(peopleAddress));
	}

	@ApiOperation(value = "更新用户收货地址接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleAddressId", value = "用户收货地址编号", required =true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressConsigneeName", value = "用户收货人姓名", required =true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressProvince", value = "收货人所在的省", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressProvinceId", value = "收货人所在的省编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressCity", value = "收货人所在的市", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressCityId", value = "收货人所在的市编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDistrict", value = "收货人所在区", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDistrictId", value = "收货人所在区编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressStreet", value = "街道", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressStreetId", value = "街道编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressAddress", value = "收货人的详细收货地址", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressMail", value = "收货人邮箱", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressPhone", value = "收货人手机", required = true,paramType="query"),
    	@ApiImplicitParam(name = "peopleAddressDefault", value = "是否是收货人最终收货地址。0代表是，1代表不是，默认为0", required = false,paramType="query")
    })
	@PostMapping(value="/update")
	public void update(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {
		// 通过session得到用户实体
		PeopleEntity people = this.getPeopleBySession();
		peopleAddress.setPeopleAddressPeopleId(people.getPeopleId());
		PeopleAddressEntity address = (PeopleAddressEntity) peopleAddressBiz.getEntity(peopleAddress);
		if (people.getPeopleId() != address.getPeopleAddressPeopleId()) {
			this.outJson(response, false);
			return;
		}
		// 判断用户信息是否为空
		if (StringUtils.isBlank(peopleAddress.getPeopleAddressProvince())
				|| StringUtils.isBlank(peopleAddress.getPeopleAddressAddress())) {
			this.outJson(response,null, false,
					this.getResString("people.address", net.mingsoft.people.constant.Const.RESOURCES));
			return;
		}
		// 验证手机号
		if (StringUtils.isBlank(peopleAddress.getPeopleAddressPhone())) {
			this.outJson(response,null, false,
					this.getResString("people.msg.phone.error", net.mingsoft.people.constant.Const.RESOURCES));
			return;
		}
		peopleAddress.setPeopleAddressPeopleId(people.getPeopleId());
		peopleAddress.setPeopleAddressAppId(BasicUtil.getAppId());
		// 更新用户地址
		peopleAddressBiz.updateEntity(peopleAddress);
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "设置默认地址接口")
	@ApiImplicitParam(name = "peopleAddressId", value = "用户收货地址编号", required = true,paramType="query")
	@PostMapping("/setDefault")
	public void setDefault(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {
		// 通过session得到用户实体
		PeopleEntity people = this.getPeopleBySession();
		// 将获取用户 PeopleAddressDefault 值还原为1，更新设为用户默认地址
		peopleAddress.setPeopleAddressPeopleId(people.getPeopleId());
		peopleAddress.setPeopleAddressAppId(BasicUtil.getAppId());
		// 更新用户地址
		peopleAddressBiz.setDefault(peopleAddress);
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "根据收货地址编号删除收货地址信息")
	@ApiImplicitParam(name = "peopleAddressId", value = "用户收货地址编号", required = true,paramType="query")
	@PostMapping("/delete")
	public void delete(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {
		// 根据收货地址id删除收货信息
		peopleAddress.setPeopleAddressPeopleId(this.getPeopleBySession().getPeopleId());
		peopleAddress.setPeopleAddressAppId(BasicUtil.getAppId());
		peopleAddressBiz.deleteEntity(peopleAddress);
		this.outJson(response, null, true);
	}

	@ApiOperation(value = "获取收货地址详情接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleAddressId", value = "用户收货地址编号", required = true,paramType="query"),
	})
	@GetMapping("/get")
	public void get(@ModelAttribute @ApiIgnore PeopleAddressEntity peopleAddress, HttpServletRequest request,
			HttpServletResponse response) {
		int peopleId = this.getPeopleBySession().getPeopleId();
		// 通过用户地址id查询用户地址实体
		peopleAddress.setPeopleAddressPeopleId(peopleId);
		PeopleAddressEntity address = (PeopleAddressEntity) peopleAddressBiz.getEntity(peopleAddress);
		if (peopleId != address.getPeopleAddressPeopleId()) {
			this.outJson(response, false);
			return;
		}
		this.outJson(response, JSONObject.toJSONString(address));
	}
}
