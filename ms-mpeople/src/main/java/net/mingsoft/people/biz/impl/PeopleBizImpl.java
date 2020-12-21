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

import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.dao.IPeopleDao;
import net.mingsoft.people.entity.PeopleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 用户业务层实现类
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Service("peopleBiz")
@Transactional
public class PeopleBizImpl  extends BaseBizImpl implements IPeopleBiz{

	/**
	 * 用户持久化层
	 */
	@Autowired
	private IPeopleDao peopleDao;
	
	/**
	 * 获取peopleDao
	 */
	@Override
	protected IBaseDao getDao() {
		return peopleDao;
	}

	/**
	 * 用户有子类增加
	 */
	@Override
	public int savePeople(PeopleEntity people) {
		peopleDao.saveEntity(people);
	    return saveEntity(people);
	}
	
	/**
	 * 根据用户ID进行用户实体的更新，用于有子类的更新操作
	 * @param entity
	 */	
	@Override
	public void updatePeople(PeopleEntity people) {
		//修改子类子类时，people中没有参数，保证执行updateSQL语句是一定正确
		people.setPeopleAppId(BasicUtil.getAppId());
		peopleDao.updateEntity(people);
		updateEntity(people);
	}	
	
	/**
	 * 用户删除
	 */
	@Override
	public void deletePeople(int id) {
		peopleDao.deleteEntity(id);
		deleteEntity(id);
	}	
	
	/**
	 * 根据用户用户名查询用户实体</br>
	 * @param userName 用户名(注:手机号,邮箱,用户名称都可作为用户名登录)
	 * @param appId 应用Id
	 * @return 查询到的用户实体
	 */
	public PeopleEntity getEntityByUserName(String userName,int appId){
		return this.peopleDao.getEntityByUserName(userName,appId);
	}	
	
	/**
	 * 根据AppId查询用户列表并进行分页
	 * @param appId 应用Id
	 * @param page 分页
	 * @return 用户集合
	 */
	public List<PeopleEntity> queryPageListByAppId(int appId){
		return this.peopleDao.queryPageListByAppId(appId);
	}
	
	/**
	 * 根据应用ID查询用户总数
	 * @param appId 应用ID
	 * @return 用户总数
	 */
	public int queryCountByAppId(int appId){
		return this.peopleDao.getCount(appId,null);
	}

	@Override
	public PeopleEntity getEntityByCode(String userName, String peopleCode,int appId) {
		// TODO Auto-generated method stub
		return this.peopleDao.getEntityByCode(userName, peopleCode, appId);
	}

	@Override
	public int getCountByDate(String peopleDateTime, Integer appId) {
		// TODO Auto-generated method stub
		Map where = new HashMap();
		where.put("peopleDateTime", peopleDateTime);
		return this.peopleDao.getCount(appId, where);
	}
	
	@Override
	public void deletePeople(int[] peopleIds) {
		if(peopleIds==null){
			return;
		}
		this.peopleDao.deletePeoples(peopleIds);
		delete(peopleIds);
	}
	
	@Override
	public List<PeopleEntity> queryByAppIdAndMap(int appId, Map whereMap) {
		// TODO Auto-generated method stub
		return peopleDao.queryByAppIdAndMap(appId, whereMap);
	}

	@Override
	public int getCountByAppIdAndMap(int appId, Map whereMap) {
		// TODO Auto-generated method stub
		return peopleDao.getCountByAppIdAndMap(appId, whereMap);
	}

	@Override
	public PeopleEntity getByPeople(PeopleEntity people,int appId) {
		// TODO Auto-generated method stub
		return peopleDao.getByPeople(people, appId);
	}

	@Override
	public PeopleEntity getEntityByMailOrPhone(String userName, int appId) {
		// TODO Auto-generated method stub
		return peopleDao.getEntityByMailOrPhone(userName, appId);
	}

	@Override
	public List<PeopleEntity> query(int appId, Map where) {
		// TODO Auto-generated method stub
		return peopleDao.query(appId,where);
	}

	
	
	
}
