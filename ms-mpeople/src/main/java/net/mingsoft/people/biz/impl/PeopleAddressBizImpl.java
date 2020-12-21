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
 */package net.mingsoft.people.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.people.biz.IPeopleAddressBiz;
import net.mingsoft.people.constant.e.PeopleAddressEnum;
import net.mingsoft.people.dao.IPeopleAddressDao;
import net.mingsoft.people.entity.PeopleAddressEntity;

/**
 * 
 * 用户收货地址业务处理层
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Service("peopleAddressBizImpl")
public class PeopleAddressBizImpl extends BaseBizImpl implements IPeopleAddressBiz{
	
	/**
	 * 注入用户收货地址持久层
	 */
	@Autowired
	private IPeopleAddressDao peopleAddressDao;
	
	/**
	 * 获取peopleAddressDao
	 */
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return peopleAddressDao;
	}

	@Override
	public void setDefault(PeopleAddressEntity peopleAddress) {
		peopleAddressDao.setDefault(peopleAddress);
	}
}
