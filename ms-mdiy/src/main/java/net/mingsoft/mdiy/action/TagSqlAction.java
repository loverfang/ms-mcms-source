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
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.ITagSqlBiz;
import net.mingsoft.mdiy.entity.TagSqlEntity;
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
 * 标签对应多个sql语句管理控制层
 * @author 铭飞开发团队
 * 创建日期：2018-11-26 11:42:01<br/>
 * 历史修订：<br/>
 */
@Api(value = "标签对应多个sql语句管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/tagSql")
public class TagSqlAction extends net.mingsoft.mdiy.action.BaseAction{
	
	
	/**
	 * 注入标签对应多个sql语句业务层
	 */	
	@Autowired
	private ITagSqlBiz tagSqlBiz;
	
	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){
		model.addAttribute("tagId", BasicUtil.getInt("id"));
		return "/mdiy/tag_sql/index";
	}
	
	/**
	 * 查询标签对应多个sql语句列表
	 * @param tagSql 标签对应多个sql语句实体
	 * <i>tagSql参数包含字段信息参考：</i><br/>
	 * tagId 自定义标签编号<br/>
	 * tagSql 自定义sql支持ftl写法<br/>
	 * sort 排序升序<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * tagId: 自定义标签编号<br/>
	 * tagSql: 自定义sql支持ftl写法<br/>
	 * sort: 排序升序<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@ApiOperation(value = "查询标签对应多个sql语句列表")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "tagId", value = "自定义标签编号", required = false,paramType="query"),
    	@ApiImplicitParam(name = "tagSql", value = "自定义sql支持ftl写法", required = false,paramType="query"),
    	@ApiImplicitParam(name = "sort", value = "排序升序", required = false,paramType="query")
    })
	@GetMapping("/list")
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore TagSqlEntity tagSql, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model, BindingResult result) {
		BasicUtil.startPage();
		List tagSqlList = tagSqlBiz.query(tagSql);
		return ResultData.build().success(new EUListBean(tagSqlList,(int)BasicUtil.endPage(tagSqlList).getTotal()));
	}
	
	
	/**
	 * 保存标签对应多个sql语句实体
	 * @param tagSql 标签对应多个sql语句实体
	 * <i>tagSql参数包含字段信息参考：</i><br/>
	 * tagId 自定义标签编号<br/>
	 * tagSql 自定义sql支持ftl写法<br/>
	 * sort 排序升序<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * tagId: 自定义标签编号<br/>
	 * tagSql: 自定义sql支持ftl写法<br/>
	 * sort: 排序升序<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "保存标签对应多个sql语句")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tagSql", value = "自定义sql支持ftl写法", required = true,paramType="query"),
    	@ApiImplicitParam(name = "sort", value = "排序升序", required = true,paramType="query"),
    	@ApiImplicitParam(name = "tagId", value = "自定义标签编号", required = false,paramType="query"),
    })
	@LogAnn(title = "保存标签对应多个sql语句",businessType= BusinessTypeEnum.INSERT)
	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("mdiy:tagSql:save")
	public ResultData save(@ModelAttribute @ApiIgnore TagSqlEntity tagSql, HttpServletResponse response, HttpServletRequest request,BindingResult result) {
		//验证自定义sql支持ftl写法的值是否合法			
		if(StringUtils.isBlank(tagSql.getTagSql())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.sql")));
		}
		if(!StringUtil.checkLength(tagSql.getTagSql()+"", 1, 1000)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.sql"), "1", "1000"));
		}
		//验证排序升序的值是否合法			
		if(tagSql.getSort()==null){
			return ResultData.build().error(getResString("err.empty", this.getResString("sort")));
		}
		if(!StringUtil.checkLength(tagSql.getSort()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("sort"), "1", "255"));
		}
		tagSqlBiz.saveEntity(tagSql);
		return ResultData.build().success(tagSql);
	}
	

	/**
	 * 
	 * @param tagSql 标签对应多个sql语句实体
	 * <i>tagSql参数包含字段信息参考：</i><br/>
	 * id:多个id直接用逗号隔开,例如id=1,2,3,4
	 * 批量删除标签属性
	 * tagId 自定义标签编号<br/>
	 * tagSql 自定义sql支持ftl写法<br/>
	 * sort 排序升序<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * tagId: 自定义标签编号<br/>
	 * tagSql: 自定义sql支持ftl写法<br/>
	 * sort: 排序升序<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "批量删除标签属性")
	@LogAnn(title = "批量删除标签属性",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mdiy:tagSql:del")
	public ResultData delete(@RequestBody List<TagSqlEntity> tagSqls,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[tagSqls.size()];
		for(int i = 0;i<tagSqls.size();i++){
			ids[i] =Integer.parseInt(tagSqls.get(i).getId()) ;
		}
		tagSqlBiz.delete(ids);
		return ResultData.build().success();
	}
	

	/**
	 * 更新标签对应多个sql语句实体
	 * @param tagSql 标签对应多个sql语句实体
	 * <i>tagSql参数包含字段信息参考：</i><br/>
	 * id:多个id直接用逗号隔开,例如id=1,2,3,4
	 * 批量删除标签属性
	 * tagId 自定义标签编号<br/>
	 * tagSql 自定义sql支持ftl写法<br/>
	 * sort 排序升序<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * tagId: 自定义标签编号<br/>
	 * tagSql: 自定义sql支持ftl写法<br/>
	 * sort: 排序升序<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "更新标签对应多个sql语句")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "标签对应多个sql语句编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "tagId", value = "自定义标签编号", required = true,paramType="query"),
    	@ApiImplicitParam(name = "tagSql", value = "自定义sql支持ftl写法", required = true,paramType="query"),
    	@ApiImplicitParam(name = "sort", value = "排序升序", required = true,paramType="query")
    })
	@LogAnn(title = "更新标签对应多个sql语句",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/update")
	@ResponseBody	
	@RequiresPermissions("mdiy:tagSql:update") 
	public ResultData update(@ModelAttribute @ApiIgnore TagSqlEntity tagSql, HttpServletResponse response,
			HttpServletRequest request) {
		//验证id,id的值是否合法			
		if(StringUtils.isBlank(tagSql.getId())){
			return ResultData.build().error(getResString("err.empty", this.getResString("id")));
		}
		if(!StringUtil.checkLength(tagSql.getId()+"", 1, 11)){
			return ResultData.build().error(getResString("err.length", this.getResString("id"), "1", "11"));
		}
		//验证暂无描述的值是否合法			
		if(StringUtils.isBlank(tagSql.getTagId()+"")){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.id")));
		}
		if(!StringUtil.checkLength(tagSql.getTagId()+"", 1, 11)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.id"), "1", "11"));
		}
		//验证自定义sql支持ftl写法的值是否合法			
		if(StringUtils.isBlank(tagSql.getTagSql())){
			return ResultData.build().error(getResString("err.empty", this.getResString("tag.sql")));
		}
		if(!StringUtil.checkLength(tagSql.getSort()+"", 1, 255)){
			return ResultData.build().error(getResString("err.length", this.getResString("tag.description"), "1", "255"));
		}
		tagSqlBiz.updateEntity(tagSql);
		return ResultData.build().success(tagSql);
	}

	/**
	 * 获取标签对应多个sql语句实体
	 * @param tagSql 标签对应多个sql语句实体
	 * <i>tagSql参数包含字段信息参考：</i><br/>
	 * id:多个id直接用逗号隔开,例如id=1,2,3,4
	 * 批量删除标签属性
	 * tagId 自定义标签编号<br/>
	 * tagSql 自定义sql支持ftl写法<br/>
	 * sort 排序升序<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * tagId: 自定义标签编号<br/>
	 * tagSql: 自定义sql支持ftl写法<br/>
	 * sort: 排序升序<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "获取标签对应多个sql语句详情")
	@ApiImplicitParam(name = "id", value = "标签对应多个sql语句编号", required = true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore TagSqlEntity tagSql,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(tagSql.getId()==null) {
			return ResultData.build().error();
		}
		TagSqlEntity _tagSql = (TagSqlEntity)tagSqlBiz.getEntity(Integer.parseInt(tagSql.getId()));
		return ResultData.build().success(_tagSql);
	}
}
