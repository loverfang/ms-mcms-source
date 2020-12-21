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


import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.constant.Const;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.entity.ModelEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2014-7-14<br/>
 * 历史修订：<br/>
 */
@Api("主界面控制层")
@Controller
@RequestMapping("/${ms.manager.path}")
public class MainAction extends BaseAction {

	/**
	 * 模块业务层
	 */
	@Autowired
	private IModelBiz modelBiz;

	/**
	 * 管理员业务层
	 */
	@Autowired
	private IManagerBiz managerBiz;


	@Value("${ms.manager.path}")
	private String managerPath;


	/**
	 * 加载后台主界面，并查询数据
	 * @param request 请求对象
	 * @return  主界面地址
	 */
	@ApiOperation(value = "加载后台主界面，并查询数据")
	@GetMapping(value = {"/index","/"})
	public String index(HttpServletRequest request) {

		ManagerSessionEntity managerSession =  getManagerBySession();
		List<BaseEntity> modelList = new ArrayList<BaseEntity>();
		ModelEntity model = new ModelEntity();
		modelList = modelBiz.queryModelByRoleId(managerSession.getManagerRoleID());
		//如果ischild有值，则不显示
		List<BaseEntity> _modelList = new ArrayList<BaseEntity>();
		for(int i=0;i<modelList.size();i++){
			ModelEntity _model = (ModelEntity) modelList.get(i);
			if(StringUtil.isBlank(_model.getIsChild())){
				_modelList.add(_model);
			}
		}
		request.setAttribute("managerSession", managerSession);
		request.setAttribute("modelList", JSONObject.toJSONString(modelList));
		int managerId = managerSession.getManagerId();
		request.setAttribute("client", BasicUtil.getApp().getAppUrl()+"/"+managerPath);
		return "/index";
	}

	@GetMapping("/main")
	public String main(HttpServletRequest request) {
		return "/main";
	}

	@GetMapping("/rf")
	@ResponseBody
	public void rf(HttpServletRequest request) {
	}

	/**
	 * 查询该父模块下的子模块
	 * @param modelId 模块ID
	 * @param request 请求对象
	 * @return 子模块列表map集合
	 */
	@ApiOperation(value = "查询该父模块下的子模块")
	@ApiImplicitParam(name = "modelId", value = "模块编号", required = true,paramType="path")
	@GetMapping(value = "/{modelId}/queryListByModelId")
	@ResponseBody
	public ResultData queryListByModelId(@PathVariable @ApiIgnore int modelId, HttpServletRequest request) {
		Map modelMap = new HashMap();
		List<BaseEntity> modelList = null;
		ManagerSessionEntity managerSession =  getManagerBySession();
		ModelEntity model = new ModelEntity();
		if (isSystemManager() && modelId == Const.DEFAULT_CMS_MODEL_ID) { // 若为系统管理员且操作CMS模块
			model.setModelManagerId(Const.DEFAULT_SYSTEM_MANGER_ROLE_ID);
			model.setModelId(modelId);
			modelList = modelBiz.query(model);
		} else if (isSystemManager()) { // 若为系统管理员且非操作CMS模块
			model.setModelModelId(modelId);
			modelList = modelBiz.query(model);
		} else { // 其他管理员
			modelList = modelBiz.queryModelByRoleId(managerSession.getManagerRoleID());
			for (int i = 0; i < modelList.size(); i++) {
				ModelEntity _model = (ModelEntity) modelList.get(i);
				if (_model.getModelModelId() != modelId) {
					modelList.remove(i);
					i--;
				}
			}
		}
		modelMap.put("modelList", modelList);
		return ResultData.build().success(modelMap);
	}

	/**
	 * 修改登录密码
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	@ApiOperation(value = "修改登录密码")
	@LogAnn(title = "修改登录密码",businessType= BusinessTypeEnum.OTHER)
	@PostMapping("/editPassword")
	@ResponseBody
	public ResultData editPassword(HttpServletResponse response, HttpServletRequest request) {
		//获取管理员信息
		ManagerEntity manager =  this.getManagerBySession();
		return ResultData.build().success(manager.getManagerName());
	}

	/**
	 * 修改登录密码，若不填写密码则表示不修改
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	@ApiOperation(value = "修改登录密码，若不填写密码则表示不修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "oldManagerPassword", value = "旧密码", required = true,paramType="query"),
		@ApiImplicitParam(name = "newManagerPassword", value = "新密码", required = true,paramType="query"),
	})
	@LogAnn(title = "修改登录密码",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/updatePassword")
	@ResponseBody
	public ResultData updatePassword( HttpServletResponse response,HttpServletRequest request) {
		//获取旧的密码,MD5加密
		String oldManagerPassword = SecureUtil.md5(request.getParameter("oldManagerPassword"));
		//获取新的密码
		String newManagerPassword = request.getParameter("newManagerPassword");
		//获取管理员信息
		ManagerEntity manager = this.getManagerBySession();
		// 判断新密码和旧密码是否为空
		if (StringUtil.isBlank(newManagerPassword) || StringUtil.isBlank(oldManagerPassword)) {
			return ResultData.build().error(getResString("err.empty", this.getResString("managerPassword")));
		}

		//判断旧的密码是否正确
		if(!oldManagerPassword.equals(manager.getManagerPassword())){
			return ResultData.build().error(getResString("err.oldPassword", this.getResString("managerPassword")));
		}
		// 判断新密码长度
		if (!StringUtil.checkLength(newManagerPassword, 6, 30)) {
			return ResultData.build().error(getResString("err.length", this.getResString("managerPassword"), "6", "30"));
		}
		//更改密码
		manager.setManagerPassword(SecureUtil.md5(newManagerPassword));
		//更新
		managerBiz.updateUserPasswordByUserName(manager);
		return ResultData.build().success();
	}

	/**
	 * 退出系统
	 * @param request 请求对象
	 * @return true退出成功
	 */
	@ApiOperation(value = "退出系统")
	@GetMapping("/loginOut")
	@ResponseBody
	public ResultData loginOut(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return ResultData.build().success();
	}

	/**
	 * 加载UI页面
	 * @param request
	 * @return UI页面地址
	 */
	@ApiOperation(value = "加载UI页面")
	@GetMapping("/ui")
	public String ui(HttpServletRequest request) {
		return "/ui";
	}

	/**
	 * 加载UI列表界面
	 * @param request
	 * @return 列表界面地址
	 */
	@ApiOperation(value = "加载UI列表界面")
	@GetMapping("/ui/list")
	public String list(HttpServletRequest request) {
		return "/ui/list";
	}

	/**
	 * 加载UI的表单页面
	 * @param request
	 * @return 表单页面地址
	 */
	@ApiOperation(value = "加载UI的表单页面")
	@GetMapping("/ui/form")
	public String form(HttpServletRequest request) {
		return "/ui/from";
	}
}
