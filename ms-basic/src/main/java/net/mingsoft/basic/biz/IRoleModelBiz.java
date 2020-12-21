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

package net.mingsoft.basic.biz;

import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.basic.entity.RoleModelEntity;

import java.util.List;

/**
 * 角色模块关联业务层接口
 * @author 张敏
 * @version 
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public interface IRoleModelBiz extends IBaseBiz{
	
	/**
	 * 保存该角色对应的模块集合
	 * @param roleModelList 集合
	 */
	public void saveEntity(List<RoleModelEntity> roleModelList);
	
	/**
	 * 更新该角色对应的模块集合
	 * @param roleModelList 集合
	 */
	public void updateEntity(List<RoleModelEntity> roleModelList);
	
	/**
	 * 通过角色获取所有关联的模块id
	 * @param roleId
	 */
	public List<RoleModelEntity> queryByRoleId(int roleId);

	public void deleteByRoleId(int roleId);


}
