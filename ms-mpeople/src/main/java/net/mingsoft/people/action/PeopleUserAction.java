package net.mingsoft.people.action;

import cn.hutool.crypto.digest.DigestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.FileUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.people.bean.PeopleBean;
import net.mingsoft.people.biz.IPeopleBiz;
import net.mingsoft.people.biz.IPeopleUserBiz;
import net.mingsoft.people.entity.PeopleEntity;
import net.mingsoft.people.entity.PeopleUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
	
/**
 * 用户基础信息表管理控制层
 * @author 伍晶晶
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
@Api(value = "用户基础信息接口")
@Controller
@RequestMapping("/${ms.manager.path}/people/peopleUser")
public class PeopleUserAction extends net.mingsoft.people.action.BaseAction{
	
	/**
	 * 注入用户基础信息表业务层
	 */	
	@Autowired
	private IPeopleUserBiz peopleUserBiz;
	
	/**
	 * 注入用户基础信息控制层
	 */
	@Autowired
	private IPeopleBiz peopleBiz;
	
	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/people/user/index";
	}

    @ApiOperation(value = "获取用户基础详情接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "puPeopleId", value = "用户ID关联people表的（people_id）", required = true,paramType="query"),
        @ApiImplicitParam(name = "puRealName", value = "用户真实名称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAddress", value = "用户地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puIcon", value = "用户头像图标地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puNickname", value = "用户昵称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puSex", value = "用户性别(0.未知、1.男、2.女)", required = false,paramType="query"),
        @ApiImplicitParam(name = "puBirthday", value = "用户出生年月日", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCard", value = "身份证", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAppId", value = "用户所属应用ID", required = false,paramType="query"),
        @ApiImplicitParam(name = "puProvince", value = "省", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCity", value = "城市", required = false,paramType="query"),
        @ApiImplicitParam(name = "puDistrict", value = "区", required = false,paramType="query"),
        @ApiImplicitParam(name = "puStreet", value = "街道", required = false,paramType="query")
    })
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(peopleUser.getPuPeopleId()<=0) {
            return ResultData.build().error(getResString("err.error", this.getResString("pu.people.id")));
        }
        PeopleUserEntity _peopleUser = (PeopleUserEntity)peopleUserBiz.getEntity(peopleUser.getPuPeopleId());
        return ResultData.build().success(_peopleUser);
    }

	@ApiOperation(value = "用户基础信息列表接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "peopleName", value = "用户账号", required =false,paramType="query"),
    	@ApiImplicitParam(name = "puNickname", value = "用户昵称", required = false,paramType="query"),
    	@ApiImplicitParam(name = "peopleState", value = "审核状态", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puRealName", value = "真实姓名", required = false,paramType="query"),
    	@ApiImplicitParam(name = "peopleDateTimes", value = "注册时间范围，如2019-01-23至2019-01-24", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puPeopleId", value = "用户ID关联people表的（people_id）", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puAddress", value = "用户地址", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puIcon", value = "用户头像图标地址", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puSex", value = "用户性别(0.未知、1.男、2.女)", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puBirthday", value = "用户出生年月日", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puCard", value = "身份证", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puAppId", value = "用户所属应用ID", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puProvince", value = "省", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puCity", value = "城市", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puDistrict", value = "区", required = false,paramType="query"),
    	@ApiImplicitParam(name = "puStreet", value = "街道", required = false,paramType="query")

    })
	@GetMapping("/list")
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore PeopleBean peopleUser, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
		if(peopleUser == null){
			peopleUser = new PeopleBean();
		}
		peopleUser.setPeopleAppId(BasicUtil.getAppId());
		BasicUtil.startPage();
		List<PeopleBean> peopleUserList = peopleUserBiz.query(peopleUser);
		EUListBean list = new EUListBean(peopleUserList,(int)BasicUtil.endPage(peopleUserList).getTotal());
		return ResultData.build().success(list);
	}

	/**
	 * 返回编辑界面peopleUser_form
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute PeopleUserEntity peopleUser,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(peopleUser.getPuPeopleId() != null){
			BaseEntity peopleUserEntity = peopleUserBiz.getEntity(peopleUser.getPuPeopleId());
			model.addAttribute("peopleUserEntity",peopleUserEntity);
		}
		return "/people/user/form";
	}

	@ApiOperation(value = "保存用户基础信息接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peopleName", value = "登录账号", required = true,paramType="query"),
		@ApiImplicitParam(name = "peoplePassword", value = "用户密码", required = true,paramType="query"),
        @ApiImplicitParam(name = "puPeopleId", value = "用户ID关联people表的（people_id）", required = false,paramType="query"),
        @ApiImplicitParam(name = "puRealName", value = "用户真实名称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAddress", value = "用户地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puIcon", value = "用户头像图标地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puNickname", value = "用户昵称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puSex", value = "用户性别(0.未知、1.男、2.女)", required = false,paramType="query"),
        @ApiImplicitParam(name = "puBirthday", value = "用户出生年月日", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCard", value = "身份证", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAppId", value = "用户所属应用ID", required = false,paramType="query"),
        @ApiImplicitParam(name = "puProvince", value = "省", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCity", value = "城市", required = false,paramType="query"),
        @ApiImplicitParam(name = "puDistrict", value = "区", required = false,paramType="query"),
        @ApiImplicitParam(name = "puStreet", value = "街道", required = false,paramType="query")
    })
	@LogAnn(title = "保存用户基础信息接口",businessType= BusinessTypeEnum.INSERT)
	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("people:user:save")
	public ResultData save(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser, HttpServletResponse response, HttpServletRequest request) {
		//验证用户真实名称的值是否合法			
		if(!StringUtil.checkLength(peopleUser.getPuRealName()+"", 0, 50)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.real.name"), "0", "50"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuAddress()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.address"), "0", "200"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuIcon()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.icon"), "0", "200"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuNickname()+"", 0, 50)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.nickname"), "0", "50"));
		}
		//验证用户性别(0.未知、1.男、2.女)的值是否合法			
		if(!StringUtil.checkLength(peopleUser.getPuSex()+"", 0, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.sex"), "0", "10"));
		}
		//验证身份证的值是否合法			
		if(!StringUtil.checkLength(peopleUser.getPuCard()+"", 0, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.card"), "0", "255"));
		}
		if(!StringUtils.isBlank(DigestUtil.md5Hex(peopleUser.getPeoplePassword()))){
			//设置用户密码
			peopleUser.setPeoplePassword(DigestUtil.md5Hex(peopleUser.getPeoplePassword()));
		}
		//验证用户输入的信息是否合法
		if(!this.checkPeople(peopleUser, request, response)){
			return ResultData.build().error();

		}
		peopleUser.setPeopleDateTime(new Date());
		peopleUser.setPeopleAppId(BasicUtil.getAppId());
		peopleUserBiz.savePeople(peopleUser);
		return ResultData.build().success(peopleUser);
	}

	@ApiOperation(value = "批量删除用户接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "puPeopleId", value = "多个puPeopleId直接用逗号隔开,例如puPeopleId=1,2,3,4", required = true,paramType="query")
    })
	@LogAnn(title = "批量删除用户接口",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("people:user:del")
	public ResultData delete(@RequestBody List<PeopleUserEntity> peopleUsers,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[peopleUsers.size()];
		for(int i = 0;i<peopleUsers.size();i++){
			ids[i] = peopleUsers.get(i).getPuPeopleId();
		}
		FileUtil.del(peopleUsers);
		peopleUserBiz.deletePeople(ids);
		return ResultData.build().success();
	}

	@ApiOperation(value = "修改用户基础信息接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "peoplePassword", value = "用户密码", required = false,paramType="query"),
        @ApiImplicitParam(name = "puRealName", value = "用户真实名称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAddress", value = "用户地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puIcon", value = "用户头像图标地址", required = false,paramType="query"),
        @ApiImplicitParam(name = "puNickname", value = "用户昵称", required = false,paramType="query"),
        @ApiImplicitParam(name = "puSex", value = "用户性别(0.未知、1.男、2.女)", required = false,paramType="query"),
        @ApiImplicitParam(name = "puBirthday", value = "用户出生年月日", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCard", value = "身份证", required = false,paramType="query"),
        @ApiImplicitParam(name = "puAppId", value = "用户所属应用ID", required = false,paramType="query"),
        @ApiImplicitParam(name = "puProvince", value = "省", required = false,paramType="query"),
        @ApiImplicitParam(name = "puCity", value = "城市", required = false,paramType="query"),
        @ApiImplicitParam(name = "puDistrict", value = "区", required = false,paramType="query"),
        @ApiImplicitParam(name = "puStreet", value = "街道", required = false,paramType="query")
    })
	@LogAnn(title = "修改用户基础信息接口",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/update")
	@ResponseBody	 
	@RequiresPermissions("people:user:update")
	public ResultData update(@ModelAttribute @ApiIgnore PeopleUserEntity peopleUser, HttpServletResponse response,
			HttpServletRequest request) {
		if(!StringUtil.checkLength(peopleUser.getPuRealName()+"", 0, 50)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.real.name"), "0", "50"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuAddress()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.address"), "0", "200"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuIcon()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.icon"), "0", "200"));
		}
		if(!StringUtil.checkLength(peopleUser.getPuNickname()+"", 0, 50)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.nickname"), "0", "50"));
		}
		//验证用户性别(0.未知、1.男、2.女)的值是否合法			
		if(!StringUtil.checkLength(peopleUser.getPuSex()+"", 0, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.sex"), "0", "10"));
		}
		//验证身份证的值是否合法			
		if(!StringUtil.checkLength(peopleUser.getPuCard()+"", 0, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("pu.card"), "0", "255"));
		}
		if(!this.checkUpdatePeople(peopleUser, request, response)){
			return ResultData.build().error();
		}
		//判断用户密码是否为空，如果不为空则进行密码的更新
		if(!StringUtils.isBlank(peopleUser.getPeoplePassword())){
			//设置用户密码
			peopleUser.setPeoplePassword(DigestUtil.md5Hex(peopleUser.getPeoplePassword()));
		}
		peopleUser.setPeopleId(peopleUser.getPuPeopleId());
		peopleUserBiz.updatePeople(peopleUser);
		return ResultData.build().success(peopleUser);
	}
	
	/**
	 * 验证更新用户信息是判断用户输入的信息是否合法
	 * @param peopleUser  用户实体
	 * @param request http请求对象
	 * @param response http响应对象
	 */
	public boolean checkUpdatePeople(PeopleUserEntity peopleUser,HttpServletRequest request,HttpServletResponse response){
		
		//获取更改前的用户
		PeopleUserEntity oldPeopleUser = (PeopleUserEntity) peopleUserBiz.getEntity(peopleUser.getPuPeopleId());
		//获取应用id
		int appId = BasicUtil.getAppId();
		//如果填写了邮箱，则验证邮箱格式是否正确
		if (!StringUtils.isBlank(peopleUser.getPeopleMail()) && !StringUtil.isEmail(peopleUser.getPeopleMail())) {
			ResultData.build().error(this.getResString("people.msg.mail.format.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
		//验证用户名不能为空
		if(StringUtils.isBlank(peopleUser.getPeopleName())){
			ResultData.build().error(this.getResString("people.msg.name.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
				
		//如果填写了手机号码，则验证手机号码填写是否正确
		if (!StringUtils.isBlank(peopleUser.getPeoplePhone()) && !StringUtil.isMobile(peopleUser.getPeoplePhone())) {
			ResultData.build().error(this.getResString("people.msg.phone.format.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
				
				
		//当用户名进行修改时验证用户名是否是唯一的
		if (!StringUtils.isBlank(peopleUser.getPeopleName())) {
			// 验证手机号是否唯一
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(peopleUser.getPeopleName(), appId);
			//判断之前是否已经存在用户名，如果不存在，则判断是否存在重名，如果存在,判断用户是否更改用户名如果更改则判断新更改的用户名是否已经存在
			//判断填写的用户名和之前用户名是否相同，如果不相同
			if(StringUtils.isBlank(oldPeopleUser.getPeopleName())){
				if (peoplePhone != null) {
					ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
					return false;
				}
			}else{
				if(!oldPeopleUser.getPeopleName().equals(peopleUser.getPeopleName())){
					if (peoplePhone != null) {
							ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
						return false;
					}
				}
			}
					
		}
		if(!StringUtils.isBlank(peopleUser.getPeoplePhone())){
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(peopleUser.getPeoplePhone(), appId);
			//判断之前是否已经存在手机号，如果不存在，则判断是否存在重名，如果存在,判断用户是否更改手机号如果更改则判断新更改的手机号是否已经存在
			//判断填写的手机号和之前手机号是否相同，如果不相同
			if(StringUtils.isBlank(oldPeopleUser.getPeoplePhone())){
				if (peoplePhone != null) {
					ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
					return false;
				}
			}else{
				if(!oldPeopleUser.getPeoplePhone().equals(peopleUser.getPeoplePhone())){
					if (peoplePhone != null) {
						ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
						return false;
					}
				}
			}
		}
		//验证邮箱的唯一性
		if(!StringUtils.isBlank(peopleUser.getPeopleMail())){
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(peopleUser.getPeopleMail(), appId);
			//判断之前是否已经存在手机号，如果不存在，则判断是否存在重名，如果存在,判断用户是否更改手机号如果更改则判断新更改的手机号是否已经存在
			//判断填写的手机号和之前手机号是否相同，如果不相同
			if(StringUtils.isBlank(oldPeopleUser.getPeopleMail())){
				if (peoplePhone != null) {
					ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
					return false;
				}
			}else{
				if(!oldPeopleUser.getPeopleMail().equals(peopleUser.getPeopleMail())){
					if (peoplePhone != null) {
						ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
						return false;
					}
				}
			}
		}
		//验证用户身份证号码
		return true;
	}
	
	/**
	 * 验证保存用户时输入的信息是否合法
	 * @param peopleUser  用户实体
	 * @param request http请求对象
	 * @param response http响应对象
	 */
	public boolean checkPeople(PeopleUserEntity peopleUser,HttpServletRequest request,HttpServletResponse response){
		//获取应用id
		int appId = BasicUtil.getAppId();
		//如果填写了邮箱，则验证邮箱格式是否正确
		if (!StringUtils.isBlank(peopleUser.getPeopleMail()) && !StringUtil.isEmail(peopleUser.getPeopleMail())) {
			ResultData.build().error(this.getResString("people.msg.mail.format.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
		//验证用户名不能为空
		if(StringUtils.isBlank(peopleUser.getPeopleName())){
			ResultData.build().error(this.getResString("people.msg.name.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
		//如果填写了手机号码，则验证手机号码填写是否正确
		if (!StringUtils.isBlank(peopleUser.getPeoplePhone()) && !StringUtil.isMobile(peopleUser.getPeoplePhone())) {
			ResultData.build().error(this.getResString("people.msg.phone.format.error", net.mingsoft.people.constant.Const.RESOURCES));
			return false;
		}
		
		
		//验证用户名是否是唯一的
		if (!StringUtils.isBlank(peopleUser.getPeopleName())) {
			// 验证手机号是否唯一
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(peopleUser.getPeopleName(), appId);
			if (peoplePhone != null) {
				ResultData.build().error(this.getResString("people.register.msg.name.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
				return false;
			}
		}
		if (!StringUtils.isBlank(peopleUser.getPeoplePhone())) {
			// 验证手机号是否唯一
			PeopleEntity peoplePhone = this.peopleBiz.getEntityByUserName(peopleUser.getPeoplePhone(), appId);
			if (peoplePhone != null) {
				ResultData.build().error(this.getResString("people.register.msg.phone.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
				return false;
			}
		}
		if (!StringUtils.isBlank(peopleUser.getPeopleMail())) {
			// 验证邮箱是否唯一
			PeopleEntity peopleMail = this.peopleBiz.getEntityByUserName(peopleUser.getPeopleMail(), appId);
			if (peopleMail != null) {
				ResultData.build().error(this.getResString("people.register.msg.mail.repeat.error", net.mingsoft.people.constant.Const.RESOURCES));
				return false;
			}
		}
		
		return true;
	}

    @ApiOperation(value = "获取用户详细信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "peopleId", value = "用户id", required =true,paramType="query")
    })
	@Deprecated
	@GetMapping("/getEntity")
    @ResponseBody
	public ResultData getEntity(String peopleId,HttpServletRequest request,HttpServletResponse response){
		if(StringUtils.isBlank(peopleId) || !StringUtil.isInteger(peopleId)){
			return ResultData.build().error();
		}
		PeopleUserEntity peopleUser = (PeopleUserEntity) this.peopleUserBiz.getEntity(Integer.parseInt(peopleId));
		if(peopleUser == null){
			return ResultData.build().error();
		}
		return ResultData.build().success(peopleUser);
	}
}