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
 */package net.mingsoft.people.biz;

import net.mingsoft.people.entity.PeopleUserEntity;


/**
 * 
 * 
 * <p>
 * <b>铭飞MS平台</b>
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014 - 2015
 * </p>
 * 
 * <p>
 * Company:景德镇铭飞科技有限公司
 * </p>
 * 
 * @author 刘继平
 * 
 * @version 300-001-001
 * 
 * <p>
 * 版权所有 铭飞科技
 * </p>
 *  
 * <p>
 * Comments: 
 * </p>
 *  
 * <p>
 * Create Date:2014-9-4
 * </p>
 *
 * <p>
 * Modification history:
 * </p>
 */

/**
 * 
 * 用户信息业务层接口，继承IPeopleBiz接口
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
public interface IPeopleUserBiz extends IPeopleBiz {

	/**
	 * 用户信息实体保存</br>
	 * 只能有子类继承时调用的</br>
	 * @param peopleEntity 用户信息
	 * @return 新增成功后用户ID
	 */
	public int savePeopleUser(PeopleUserEntity peopleEntity);
	
	/**
	 * 更新用户信息,</br>
	 * 只能在有子类时调用</br>
	 * @param peopleEntity 用户信息
	 */
	public void updatePeopleUser(PeopleUserEntity peopleEntity);
	
	/**
	 * 删除用户信息</br>
	 * 只能在有子类时调用</br>
	 * @param peopleId 用户ID
	 */
	public void deletePeopleUser(int peopleId);
	
	/**
	 * 批量删除用户
	 * @param peopleIds 用户id集合
	 */
	public void deletePeopleUsers(int[] peopleIds);

	/**
	 * 通过实体获取详情
	 * @param peopleUser
	 */
	public PeopleUserEntity getByEntity(PeopleUserEntity peopleUser);
}
