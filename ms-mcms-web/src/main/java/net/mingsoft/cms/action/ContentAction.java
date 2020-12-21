package net.mingsoft.cms.action;

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
import net.mingsoft.cms.biz.IContentBiz;
import net.mingsoft.cms.entity.ContentEntity;
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
 * 文章管理控制层
 * @author 铭飞开发团队
 * 创建日期：2019-11-28 15:12:32<br/>
 * 历史修订：<br/>
 */
@Api(value = "文章接口")
@Controller("cmsContentAction")
@RequestMapping("/${ms.manager.path}/cms/content")
public class ContentAction extends BaseAction{
	
	
	/**
	 * 注入文章业务层
	 */	
	@Autowired
	private IContentBiz contentBiz;

	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/cms/content/index";
	}
	/**
	 * 返回主界面main
	 */
	@GetMapping("/main")
	public String main(HttpServletResponse response,HttpServletRequest request){
		return "/cms/content/main";
	}

	/**
	 * 查询文章列表
	 * @param content 文章实体
	 */
	@ApiOperation(value = "查询文章列表接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "contentTitle", value = "文章标题", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentCategoryId", value = "所属栏目", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentType", value = "文章类型", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDisplay", value = "是否显示", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentAuthor", value = "文章作者", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentSource", value = "文章来源", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDatetime", value = "发布时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentSort", value = "自定义顺序", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentImg", value = "文章缩略图", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDescription", value = "描述", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentKeyword", value = "关键字", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDetails", value = "文章内容", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentUrl", value = "文章跳转链接地址", required =false,paramType="query"),
    	@ApiImplicitParam(name = "appid", value = "文章管理的应用id", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
    	@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
	@RequestMapping("/list")
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore ContentEntity content,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model,BindingResult result) {
		content.setAppId(BasicUtil.getAppId());
		BasicUtil.startPage();
		List contentList = contentBiz.query(content);
		return ResultData.build().success(new EUListBean(contentList,(int)BasicUtil.endPage(contentList).getTotal()));
	}
	
	/**
	 * 返回编辑界面content_form
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute ContentEntity content,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(content.getId()!=null){
			BaseEntity contentEntity = contentBiz.getEntity(Integer.parseInt(content.getId()));			
			model.addAttribute("contentEntity",contentEntity);
		}
		model.addAttribute("appId",BasicUtil.getAppId());
		return "/cms/content/form";
	}

	/**
	 * 获取文章
	 * @param content 文章实体
	 */
	@ApiOperation(value = "获取文章列表接口")
    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore ContentEntity content,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(content.getId()==null) {
			return ResultData.build().error();
		}
		content.setAppId(BasicUtil.getAppId());
		ContentEntity _content = (ContentEntity)contentBiz.getEntity(Integer.parseInt(content.getId()));
		return ResultData.build().success(_content);
	}
	
	@ApiOperation(value = "保存文章列表接口")
	 @ApiImplicitParams({
    	@ApiImplicitParam(name = "contentTitle", value = "文章标题", required =true,paramType="query"),
		@ApiImplicitParam(name = "contentCategoryId", value = "所属栏目", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentType", value = "文章类型", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDisplay", value = "是否显示", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentAuthor", value = "文章作者", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentSource", value = "文章来源", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDatetime", value = "发布时间", required =true,paramType="query"),
		@ApiImplicitParam(name = "contentSort", value = "自定义顺序", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentImg", value = "文章缩略图", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDescription", value = "描述", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentKeyword", value = "关键字", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDetails", value = "文章内容", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentUrl", value = "文章跳转链接地址", required =false,paramType="query"),
		@ApiImplicitParam(name = "appid", value = "文章管理的应用id", required =false,paramType="query"),
		@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
		@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
		@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
	})

	/**
	* 保存文章
	* @param content 文章实体
	*/
	@PostMapping("/save")
	@ResponseBody
	@LogAnn(title = "保存文章", businessType = BusinessTypeEnum.INSERT)
	@RequiresPermissions("cms:content:save")
	public ResultData save(@ModelAttribute @ApiIgnore ContentEntity content, HttpServletResponse response, HttpServletRequest request) {
		//验证文章标题的值是否合法
		if(StringUtil.isBlank(content.getContentTitle())){
			return ResultData.build().error(getResString("err.empty", this.getResString("content.title")));
		}
		if(!StringUtil.checkLength(content.getContentTitle()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.title"), "0", "200"));
		}
		if(!StringUtil.checkLength(content.getContentAuthor()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.author"), "0", "200"));
		}
		if(!StringUtil.checkLength(content.getContentSource()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.source"), "0", "200"));
		}
		//验证发布时间的值是否合法
		if(StringUtil.isBlank(content.getContentDatetime())){
			return ResultData.build().error(getResString("err.empty", this.getResString("content.datetime")));
		}
		if(!StringUtil.checkLength(content.getContentUrl()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.url"), "0", "200"));
		}
		content.setAppId(BasicUtil.getAppId());
		contentBiz.saveEntity(content);
		return ResultData.build().success(content);
	}
	
	/**
	 * @param content 文章实体
	 */
	@ApiOperation(value = "批量删除文章列表接口")
	@PostMapping("/delete")
	@ResponseBody
	@LogAnn(title = "删除文章", businessType = BusinessTypeEnum.DELETE)
	@RequiresPermissions("cms:content:del")
	public ResultData delete(@RequestBody List<ContentEntity> contents,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[contents.size()];
		for(int i = 0;i<contents.size();i++){
			ids[i] =Integer.parseInt(contents.get(i).getId()) ;
		}
		contentBiz.delete(ids);
		return ResultData.build().success();
	}
	/**
	*	更新文章列表
	* @param content 文章实体
	*/
	 @ApiOperation(value = "更新文章列表接口")
	 @ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query"),
    	@ApiImplicitParam(name = "contentTitle", value = "文章标题", required =true,paramType="query"),
		@ApiImplicitParam(name = "contentCategoryId", value = "所属栏目", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentType", value = "文章类型", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDisplay", value = "是否显示", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentAuthor", value = "文章作者", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentSource", value = "文章来源", required =false,paramType="query"),
    	@ApiImplicitParam(name = "contentDatetime", value = "发布时间", required =true,paramType="query"),
		@ApiImplicitParam(name = "contentSort", value = "自定义顺序", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentImg", value = "文章缩略图", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDescription", value = "描述", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentKeyword", value = "关键字", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentDetails", value = "文章内容", required =false,paramType="query"),
		@ApiImplicitParam(name = "contentUrl", value = "文章跳转链接地址", required =false,paramType="query"),
		@ApiImplicitParam(name = "appid", value = "文章管理的应用id", required =false,paramType="query"),
		@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
		@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
		@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
	})
	@PostMapping("/update")
	@ResponseBody
	@LogAnn(title = "更新文章", businessType = BusinessTypeEnum.UPDATE)
	@RequiresPermissions("cms:content:update")
	public ResultData update(@ModelAttribute @ApiIgnore ContentEntity content, HttpServletResponse response,
			HttpServletRequest request) {
		//验证文章标题的值是否合法			
		if(StringUtil.isBlank(content.getContentTitle())){
			return ResultData.build().error(getResString("err.empty", this.getResString("content.title")));
		}
		if(!StringUtil.checkLength(content.getContentTitle()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.title"), "0", "200"));
		}
		if(!StringUtil.checkLength(content.getContentAuthor()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.author"), "0", "200"));
		}
		if(!StringUtil.checkLength(content.getContentSource()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.source"), "0", "200"));
		}
		//验证发布时间的值是否合法			
		if(StringUtil.isBlank(content.getContentDatetime())){
			return ResultData.build().error(getResString("err.empty", this.getResString("content.datetime")));
		}
		if(!StringUtil.checkLength(content.getContentUrl()+"", 0, 200)){
			return ResultData.build().error(getResString("err.length", this.getResString("content.url"), "0", "200"));
		}
		content.setAppId(BasicUtil.getAppId());
		contentBiz.updateEntity(content);
		return ResultData.build().success(content);
	}


		
}