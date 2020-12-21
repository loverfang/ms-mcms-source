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
package net.mingsoft.mdiy.action;

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
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.ITagBiz;
import net.mingsoft.mdiy.entity.TagEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 标签管理控制层
 * @author 铭飞开发团队
 * 创建日期：2018-10-24 8:44:34<br/>
 * 历史修订：<br/>
 */
@Api(value = "标签管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/tag")
public class TagAction extends BaseAction{
	
	
	/**
	 * 注入标签业务层
	 */	
	@Autowired
	private ITagBiz tagBiz;
	
	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){
		return "/mdiy/tag/index";
	}
	
	/**
	 * 查询标签列表
	 * @param tag 标签实体
	 * <i>tag参数包含字段信息参考：</i><br/>
	 * id 暂无描述<br/>
	 * tagName 标签名称<br/>
	 * tagType 标签类型<br/>
	 * tagDescription 描述<br/>
	 * tagSql sql语句,支持多条，多条用隔开<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * id: 暂无描述<br/>
	 * tagName: 标签名称<br/>
	 * tagType: 标签类型<br/>
	 * tagDescription: 描述<br/>
	 * tagSql: sql语句,支持多条，多条用隔开<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@ApiOperation(value = "标签列表")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "tagName", value = "模块编号", required =false,paramType="query"),
    	@ApiImplicitParam(name = "tagType", value = "自定义页面绑定模板的路径", required = false,paramType="query"),
    	@ApiImplicitParam(name = "tagDescription", value = "自定义页面标题", required = false,paramType="query"),
    })
	@GetMapping("/list")
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore TagEntity tag, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model, BindingResult result) {
		BasicUtil.startPage();
		List tagList = tagBiz.query(tag);
		return ResultData.build().success(new EUListBean(tagList,(int)BasicUtil.endPage(tagList).getTotal()));
	}
	
	/**
	 * 返回编辑界面tag_form
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute TagEntity tag,HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){
		if(tag.getId()!=null){
			BaseEntity tagEntity = tagBiz.getEntity(Integer.parseInt(tag.getId()));			
			model.addAttribute("tagEntity",tagEntity);
		}
		return "/mdiy/tag/form";
	}
	
	/**
	 * 获取标签
	 * @param tag 标签实体
	 * <i>tag参数包含字段信息参考：</i><br/>
	 * id 暂无描述<br/>
	 * tagName 标签名称<br/>
	 * tagType 标签类型<br/>
	 * tagDescription 描述<br/>
	 * tagSql sql语句,支持多条，多条用隔开<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * id: 暂无描述<br/>
	 * tagName: 标签名称<br/>
	 * tagType: 标签类型<br/>
	 * tagDescription: 描述<br/>
	 * tagSql: sql语句,支持多条，多条用隔开<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "获取标签详情")
	@ApiImplicitParam(name = "id", value = "标签编号", required = true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore TagEntity tag,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(tag.getId()==null) {
			return null;
		}
		TagEntity _tag = (TagEntity)tagBiz.getEntity(Integer.parseInt(tag.getId()));
		return ResultData.build().success(_tag);
	}
	
	/**
	 * 保存标签实体
	 * @param tag 标签实体
	 * <i>tag参数包含字段信息参考：</i><br/>
	 * id 暂无描述<br/>
	 * tagName 标签名称<br/>
	 * tagType 标签类型<br/>
	 * tagDescription 描述<br/>
	 * tagSql sql语句,支持多条，多条用隔开<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * id: 暂无描述<br/>
	 * tagName: 标签名称<br/>
	 * tagType: 标签类型<br/>
	 * tagDescription: 描述<br/>
	 * tagSql: sql语句,支持多条，多条用隔开<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "保存标签")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "tagName", value = "模块编号", required =true,paramType="query"),
    	@ApiImplicitParam(name = "tagDescription", value = "自定义页面标题", required = true,paramType="query"),
    	@ApiImplicitParam(name = "tagType", value = "自定义页面绑定模板的路径", required = false,paramType="query"),
    })
	@LogAnn(title = "保存标签",businessType= BusinessTypeEnum.INSERT)
	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("mdiy:tag:save")
	public ResultData save(@ModelAttribute @ApiIgnore TagEntity tag, HttpServletResponse response, HttpServletRequest request,BindingResult result) {
		//验证标签名称的值是否合法			
		if(StringUtils.isBlank(tag.getTagName())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.name")));
		}
		if(!StringUtil.checkLength(tag.getTagName()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.name"), "1", "255"));
		}
		//验证描述的值是否合法			
		if(StringUtils.isBlank(tag.getTagDescription())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.description")));
		}
		if(!StringUtil.checkLength(tag.getTagDescription()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.description"), "1", "255"));
		}

		tagBiz.saveEntity(tag);
		return ResultData.build().success(tag);
	}
	
	/**
	 * @param tag 标签实体
	 * <i>tag参数包含字段信息参考：</i><br/>
	 * id:多个id直接用逗号隔开,例如id=1,2,3,4
	 * 批量删除标签
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@ApiOperation(value = "批量删除标签")
	@LogAnn(title = "批量删除标签",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mdiy:tag:del")
	public ResultData delete(@RequestBody @ApiIgnore List<TagEntity> tags,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[tags.size()];
		for(int i = 0;i<tags.size();i++){
			ids[i] =Integer.parseInt(tags.get(i).getId()) ;
		}
		tagBiz.delete(ids);
		return ResultData.build().success();
	}

	/** 
	 * 更新标签信息标签
	 * @param tag 标签实体
	 * <i>tag参数包含字段信息参考：</i><br/>
	 * id 暂无描述<br/>
	 * tagName 标签名称<br/>
	 * tagType 标签类型<br/>
	 * tagDescription 描述<br/>
	 * tagSql sql语句,支持多条，多条用隔开<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * id: 暂无描述<br/>
	 * tagName: 标签名称<br/>
	 * tagType: 标签类型<br/>
	 * tagDescription: 描述<br/>
	 * tagSql: sql语句,支持多条，多条用隔开<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "更新标签信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "标签编号", required =true,paramType="query"),
    	@ApiImplicitParam(name = "tagName", value = "模块编号", required =true,paramType="query"),
    	@ApiImplicitParam(name = "tagDescription", value = "自定义页面标题", required = true,paramType="query"),
    	@ApiImplicitParam(name = "tagType", value = "自定义页面绑定模板的路径", required = false,paramType="query"),
    })
	@LogAnn(title = "更新标签信息",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/update")
	@ResponseBody	
	@RequiresPermissions("mdiy:tag:update") 
	public ResultData update(@ModelAttribute @ApiIgnore TagEntity tag, HttpServletResponse response,
			HttpServletRequest request) {
		//验证暂无描述的值是否合法			
		if(StringUtils.isBlank(tag.getId())){
			return ResultData.build().error(getResString("err.empty", this.getResString("id")));
		}
		if(!StringUtil.checkLength(tag.getId()+"", 1, 11)){
			return ResultData.build().error(getResString("err.length", this.getResString("id"), "1", "11"));
		}
		//验证标签名称的值是否合法			
		if(StringUtils.isBlank(tag.getTagName())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.name")));
		}
		if(!StringUtil.checkLength(tag.getTagName()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.name"), "1", "255"));
		}
		//验证描述的值是否合法			
		if(StringUtils.isBlank(tag.getTagDescription())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.description")));
		}
		if(!StringUtil.checkLength(tag.getTagDescription()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.description"), "1", "255"));
		}
		tagBiz.updateEntity(tag);
		return ResultData.build().success(tag);
	}
	
		
}
