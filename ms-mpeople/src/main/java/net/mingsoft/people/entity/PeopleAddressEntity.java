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
 */package net.mingsoft.people.entity;

import net.mingsoft.base.constant.e.BaseEnum;
import net.mingsoft.base.entity.BaseEntity;

/**
 * 
 * 用户收货地址实体类
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
public class PeopleAddressEntity extends BaseEntity{

	/**
	 * 用户收货地址自增长Id
	 */
	int peopleAddressId;
	
	/**
	 * 对应用户基础信息拓展表的id
	 */
	int peopleAddressPeopleId;
	
	/**
	 * 用户收货人姓名
	 */
	String peopleAddressConsigneeName;
	
	/**
	 * 收货人所在的省
	 */
	String peopleAddressProvince;
	
	/**
	 * 收货人所在的省
	 */
	long peopleAddressProvinceId;
	
	/**
	 * 收货人所在的市
	 */
	String peopleAddressCity;
	
	long peopleAddressCityId;
	
	/**
	 * 收货人所在区
	 */
	String peopleAddressDistrict;
	long peopleAddressDistrictId;
	
	/**
	 * 街道
	 */
	String peopleAddressStreet;
	
	long peopleAddressStreetId;
	
	/**
	 * 收货人的详细收货地址
	 */
	String peopleAddressAddress;
	
	/**
	 * 收货人邮箱
	 */
	String peopleAddressMail;
	
	/**
	 * 收货人手机
	 */
	String peopleAddressPhone;
	
	/**
	 * 是否是收货人最终收货地址。0代表是，1代表不是，默认为0
	 */
	int peopleAddressDefault;

	/**
	 * 对应的站点id
	 */
	int peopleAddressAppId;

	public int getPeopleAddressId() {
		return peopleAddressId;
	}

	public void setPeopleAddressId(int peopleAddressId) {
		this.peopleAddressId = peopleAddressId;
	}

	public int getPeopleAddressPeopleId() {
		return peopleAddressPeopleId;
	}

	public void setPeopleAddressPeopleId(int peopleAddressPeopleId) {
		this.peopleAddressPeopleId = peopleAddressPeopleId;
	}

	public String getPeopleAddressConsigneeName() {
		return peopleAddressConsigneeName;
	}

	public void setPeopleAddressConsigneeName(String peopleAddressConsigneeName) {
		this.peopleAddressConsigneeName = peopleAddressConsigneeName;
	}

	public String getPeopleAddressProvince() {
		return peopleAddressProvince;
	}

	public void setPeopleAddressProvince(String peopleAddressProvince) {
		this.peopleAddressProvince = peopleAddressProvince;
	}

	public String getPeopleAddressCity() {
		return peopleAddressCity;
	}

	public void setPeopleAddressCity(String peopleAddressCity) {
		this.peopleAddressCity = peopleAddressCity;
	}

	public String getPeopleAddressDistrict() {
		return peopleAddressDistrict;
	}

	public void setPeopleAddressDistrict(String peopleAddressDistrict) {
		this.peopleAddressDistrict = peopleAddressDistrict;
	}

	public String getPeopleAddressAddress() {
		return peopleAddressAddress;
	}

	public void setPeopleAddressAddress(String peopleAddressAddress) {
		this.peopleAddressAddress = peopleAddressAddress;
	}

	public String getPeopleAddressMail() {
		return peopleAddressMail;
	}

	public void setPeopleAddressMail(String peopleAddressMail) {
		this.peopleAddressMail = peopleAddressMail;
	}

	public String getPeopleAddressPhone() {
		return peopleAddressPhone;
	}

	public void setPeopleAddressPhone(String peopleAddressPhone) {
		this.peopleAddressPhone = peopleAddressPhone;
	}

	public int getPeopleAddressDefault() {
		return peopleAddressDefault;
	}
	
	/**
	 * 推荐使用枚举类形参方法，此方法过时
	 * @param peopleAddressDefault
	 */
	@Deprecated
	public void setPeopleAddressDefault(int peopleAddressDefault) {
		this.peopleAddressDefault = peopleAddressDefault;
	}
	/**
	 * 枚举类形参方法
	 * @param peopleAddressDefault
	 */
	public void setPeopleAddressDefault(BaseEnum peopleAddressDefault) {
		this.peopleAddressDefault = peopleAddressDefault.toInt();
	}

	public int getPeopleAddressAppId() {
		return peopleAddressAppId;
	}

	public void setPeopleAddressAppId(int peopleAddressAppId) {
		this.peopleAddressAppId = peopleAddressAppId;
	}

	public String getPeopleAddressStreet() {
		return peopleAddressStreet;
	}

	public void setPeopleAddressStreet(String peopleAddressStreet) {
		this.peopleAddressStreet = peopleAddressStreet;
	}

	public long getPeopleAddressProvinceId() {
		return peopleAddressProvinceId;
	}

	public void setPeopleAddressProvinceId(long peopleAddressProvinceId) {
		this.peopleAddressProvinceId = peopleAddressProvinceId;
	}

	public long getPeopleAddressCityId() {
		return peopleAddressCityId;
	}

	public void setPeopleAddressCityId(long peopleAddressCityId) {
		this.peopleAddressCityId = peopleAddressCityId;
	}

	public long getPeopleAddressDistrictId() {
		return peopleAddressDistrictId;
	}

	public void setPeopleAddressDistrictId(long peopleAddressDistrictId) {
		this.peopleAddressDistrictId = peopleAddressDistrictId;
	}

	public long getPeopleAddressStreetId() {
		return peopleAddressStreetId;
	}

	public void setPeopleAddressStreetId(long peopleAddressStreetId) {
		this.peopleAddressStreetId = peopleAddressStreetId;
	}
	
	

}
