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
package net.mingsoft.people.action;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.constant.e.PeopleEnum;
import net.mingsoft.people.entity.PeopleUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户基础信息表管理控制层
 * @author 铭飞开发团队
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Api(value = "用户基础接口")
@Controller
@RequestMapping("/${ms.manager.path}/people")
public class PeopleAction extends BaseAction{
	
	/**
	 * 注入用户控制层
	 */
	@Autowired
	private IPeopleBiz peopleBiz;

	@ApiOperation(value = "更新用户状态接口")
    @ApiImplicitParam(name = "peoples", value = "所选用户", required = true,paramType="query")
	@LogAnn(title = "更新用户状态接口",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/updateState")
    @ResponseBody
	public ResultData updateState(@RequestBody List<PeopleUserEntity> peoples, HttpServletRequest request, HttpServletResponse response){
		if(peoples.size() <= 0){
			return ResultData.build().error();
		}
		for(int i = 0;i < peoples.size(); i++){
			if(peoples.get(i).getPeopleState() == PeopleEnum.STATE_CHECK.toInt()){
				peoples.get(i).setPeopleStateEnum(PeopleEnum.STATE_NOT_CHECK);
			}else{
				peoples.get(i).setPeopleStateEnum(PeopleEnum.STATE_CHECK);
			}
			this.peopleBiz.updateEntity(peoples.get(i));
		}
		return ResultData.build().success();
	}
}
