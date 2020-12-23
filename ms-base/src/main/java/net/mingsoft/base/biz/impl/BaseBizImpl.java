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

package net.mingsoft.base.biz.impl;

import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.base.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:  BaseAction
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: 铭飞开发团队
 * @date:   2018年3月19日 下午3:28:27
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
public abstract class BaseBizImpl<E extends Serializable> implements IBaseBiz {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * 不需要重写此方法，自动会
	 *
	 * @return
	 */
	protected abstract IBaseDao<E> getDao();

	@Override
	public int saveEntity(BaseEntity entity) {
		return getDao().saveEntity(entity);
	}

	@Override
	public void deleteEntity(int id) {
		// TODO Auto-generated method stub
		getDao().deleteEntity(id);
	}

	@Override
	public void updateEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		getDao().updateEntity(entity);
	}

	@Override
	public List<E> queryAll() {
		// TODO Auto-generated method stub
		return getDao().queryAll();
	}

	@Override
	@Deprecated
	public int queryCount() {
		return getDao().queryCount();
	}

	@Override
	public BaseEntity getEntity(int id) {
		return getDao().getEntity(id);
	}

	@Override
	public List queryBySQL(String table, List fields, Map wheres, Integer begin, Integer end) {
		// TODO Auto-generated method stub
		return getDao().queryBySQL(table, fields, wheres, begin, end, null);
	}

	@Override
	public int countBySQL(String table, Map wheres) {
		// TODO Auto-generated method stub
		return getDao().countBySQL(table, wheres);
	}

	@Override
	public List queryBySQL(String table, List fields, Map wheres) {
		// TODO Auto-generated method stub
		return getDao().queryBySQL(table, fields, wheres, null, null, null);
	}

	@Override
	public void updateBySQL(String table, Map fields, Map wheres) {
		// TODO Auto-generated method stub
		getDao().updateBySQL(table, fields, wheres);
	}

	@Override
	public void deleteBySQL(String table, Map wheres) {
		// TODO Auto-generated method stub
		getDao().deleteBySQL(table, wheres);
	}

	@Override
	public void insertBySQL(String table, Map fields) {
		// TODO Auto-generated method stub
		getDao().insertBySQL(table, fields);
	}

	@Override
	public void createTable(String table, Map fileds) {
		// TODO Auto-generated method stub
		getDao().createTable(table, fileds);
	}


	@Override
	public void dropTable(String table) {
		// TODO Auto-generated method stub
		getDao().dropTable(table);
	}

	@Override
	public Object excuteSql(String sql) {
		// TODO Auto-generated method stub
		return getDao().excuteSql(sql);
	}

	@Override
	public void saveBatch(List list) {
		getDao().saveBatch(list);
	}

	@Override
	public void delete(int[] ids) {
		getDao().delete(ids);
	}

	@Override
	public void deleteEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		getDao().deleteByEntity(entity);
	}

	@Override
	public E getEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		return getDao().getByEntity(entity);
	}

	@Override
	public List<E> query(BaseEntity entity) {
		// TODO Auto-generated method stub
		return getDao().query(entity);
	}



}
