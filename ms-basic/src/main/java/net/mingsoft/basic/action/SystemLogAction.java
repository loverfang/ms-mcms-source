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
package net.mingsoft.basic.action;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.biz.ISystemLogBiz;
import net.mingsoft.basic.entity.SystemLogEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;













/**
 * 系统日志管理控制层
 * @author 铭飞开发团队
 * 创建日期：2019-11-21 11:34:52<br/>
 * 历史修订：<br/>
 */
@Api(value = "系统日志接口")
@Controller
@RequestMapping("/${ms.manager.path}/basic/systemLog")
public class SystemLogAction extends net.mingsoft.basic.action.BaseAction{


	/**
	 * 注入系统日志业务层
	 */
	@Autowired
	private ISystemLogBiz systemLogBiz;

	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/basic/system-log/index";
	}


	@ApiOperation(value = "查询系统日志列表接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "param", value = "请求参数", required =false,paramType="query"),
    	@ApiImplicitParam(name = "result", value = "返回参数", required =false,paramType="query"),
    	@ApiImplicitParam(name = "errorMsg", value = "错误消息", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
    	@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
	@RequestMapping(value ="/list",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore SystemLogEntity systemLog, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model, BindingResult result) {
		systemLog.setAppId(BasicUtil.getAppId());
		BasicUtil.startPage();
		List systemLogList = systemLogBiz.query(systemLog);
		return ResultData.build().success(new EUListBean(systemLogList,(int)BasicUtil.endPage(systemLogList).getTotal()));
	}

	/**
	 * 返回编辑界面systemLog_form
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute SystemLogEntity systemLog,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(systemLog.getId()!=null){
			BaseEntity systemLogEntity = systemLogBiz.getEntity(Integer.parseInt(systemLog.getId()));
			model.addAttribute("systemLogEntity",systemLogEntity);
		}
		return "/basic/system-log/form";
	}


	@ApiOperation(value = "获取系统日志列表接口")
    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore SystemLogEntity systemLog,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(systemLog.getId()==null) {
			return null;
		}
		SystemLogEntity _systemLog = (SystemLogEntity)systemLogBiz.getEntity(Integer.parseInt(systemLog.getId()));
		return ResultData.build().success(_systemLog);
	}





}
