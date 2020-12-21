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

package net.mingsoft.basic.action.web;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.action.BaseAction;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.biz.IRoleBiz;
import net.mingsoft.basic.constant.Const;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.AppEntity;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.entity.RoleEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 *
 * @ClassName:  LoginAction
 * @Description:TODO(登录的基础应用层)
 * @author: 铭飞开发团队
 * @date:   2015年1月27日 下午3:21:47
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
@Api("登录的基础应用层接口")
@Controller
@RequestMapping("/${ms.manager.path}")
public class LoginAction extends BaseAction {

	@Value("${ms.manager.path}")
	private String managerPath;
	/**
	 * 管理员业务层
	 */
	@Autowired
	private IManagerBiz managerBiz;

	/**
	 * 角色业务request层
	 */
	@Autowired
	private IRoleBiz roleBiz;

	/**
	 * 站点业务层
	 */
	@Autowired
	private IAppBiz appBiz;


	/**
	 * 加载管理员登录界面
	 *
	 * @param request
	 *            请求对象
	 * @return 管理员登录界面地址
	 */
	@ApiOperation(value = "加载管理员登录界面")
	@SuppressWarnings("resource")
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		if (BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION)!=null) {
			return "redirect:"+managerPath+"/index.do";
		}
		// 根据请求地址来显示标题
		AppEntity app = BasicUtil.getApp();
		// 判断应用实体是否存在
		if (app != null) {
			// 检测应用是否有自定义界面b
			if (!StringUtil.isBlank(app.getAppLoginPage())) {
				LOG.debug("跳转自定义登录界面");
				return "redirect:" + app.getAppLoginPage();
			}

		} else {
			File file = new File(BasicUtil.getRealPath( "WEB-INF/ms.install"));
			//存在安装文件
			if (file.exists()) {
				String defaultId = FileReader.create(new File(BasicUtil.getRealPath( "WEB-INF/ms.install"))).readString();
				if (!StringUtils.isEmpty(defaultId)) {
					app = (AppEntity) appBiz.getEntity(Integer.parseInt(defaultId));
					app.setAppUrl(this.getUrl(request));
					appBiz.updateEntity(app);
					FileWriter.create(new File(BasicUtil.getRealPath( "WEB-INF/ms.install.bak"))).write(defaultId);
					file.delete();
				}
			}

		}
		request.setAttribute("app", app);
		return "/login";
	}

	/**
	 * 验证登录
	 *
	 * @param manager
	 *            管理员实体
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	@ApiOperation(value = "验证登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "managerName", value = "帐号", required = true,paramType="query"),
			@ApiImplicitParam(name = "managerPassword", value = "密码", required = true,paramType="query"),
	})
	@PostMapping(value="/checkLogin")
	public void checkLogin(@ModelAttribute @ApiIgnore ManagerEntity manager, HttpServletRequest request,
						   HttpServletResponse response) {
		AppEntity urlWebsite = appBiz.getByUrl(this.getDomain(request)); // 根据url地址获取站点信息，主要是区分管理员对那些网站有权限
		if (urlWebsite == null) {
			this.outJson(response, null, false, this.getResString("err.not.exist",this.getResString("app"),"!请尝试去文件 WEB-INF/ms.install.bak 后缀bak"));
			return;
		}
		//验证码
		if (!(checkRandCode())) {
			outJson(response, null, false,getResString("err.error", new String[] { getResString("rand.code") }));
			return;
		}

		// 根据账号获取当前管理员信息
		ManagerEntity newManager = new ManagerEntity();
		newManager.setManagerName(manager.getManagerName());
		ManagerEntity _manager = (ManagerEntity) managerBiz.getEntity(newManager);

		if (_manager == null || StringUtils.isEmpty(manager.getManagerName())) {
			// 系统不存在此用户
			this.outJson(response, null, false, this.getResString("err.nameEmpty"));
		} else {
			// 判断当前用户输入的密码是否正确
			if (SecureUtil.md5(manager.getManagerPassword()).equals(_manager.getManagerPassword())) {
				// 创建管理员session对象
				ManagerSessionEntity managerSession = new ManagerSessionEntity();
				AppEntity website = new AppEntity();
				// 获取管理员所在的角色
				RoleEntity role = (RoleEntity) roleBiz.getEntity(_manager.getManagerRoleID());
				website = (AppEntity) appBiz.getEntity(role.getAppId());
				// 判断当前登录管理员是否为该网站的系统管理员，如果是，如果不是则判断是否为超级管理员
				if ((website != null && urlWebsite.getAppId() == website.getAppId() && _manager.getManagerRoleID() > Const.DEFAULT_SYSTEM_MANGER_ROLE_ID) || (role.getAppId()==BasicUtil.getAppId())) {
					if(website==null){
						website = BasicUtil.getApp();
					}
					List childManagerList = managerBiz.queryAllChildManager(managerSession.getManagerId());
					managerSession.setBasicId(website.getAppId());
					managerSession.setManagerParentID(role.getRoleManagerId());
					managerSession.setManagerChildIDs(childManagerList);
					managerSession.setStyle(website.getAppStyle());
					// 压入管理员seesion
					BasicUtil.setSession(SessionConstEnum.MANAGER_SESSION, managerSession);
				} else {
					if (!(_manager.getManagerRoleID() == Const.DEFAULT_SYSTEM_MANGER_ROLE_ID)) {
						LOG.debug("roleId: "+_manager.getManagerRoleID());
						this.outJson(response, null, false, this.getResString("err.not.exist",this.getResString("manager")));
					} else {
						BasicUtil.setSession(SessionConstEnum.MANAGER_SESSION, managerSession);
					}
				}
				BeanUtils.copyProperties(_manager, managerSession);

				Subject subject = SecurityUtils.getSubject();
				UsernamePasswordToken upt = new UsernamePasswordToken(managerSession.getManagerName(),managerSession.getManagerPassword());
				subject.login(upt);
				this.outJson(response, null, true, null);
			} else {
				// 密码错误
				this.outJson(response, null, false, this.getResString("err.password"));
			}
		}
	}

}
