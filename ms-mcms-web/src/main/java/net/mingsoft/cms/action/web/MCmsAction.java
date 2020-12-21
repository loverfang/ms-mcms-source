/**
 The MIT License (MIT) * Copyright (c) 2016 铭飞科技(mingsoft.net)

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

package net.mingsoft.cms.action.web;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateNotFoundException;
import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.cms.bean.CategoryBean;
import net.mingsoft.cms.bean.ContentBean;
import net.mingsoft.cms.biz.ICategoryBiz;
import net.mingsoft.cms.biz.IContentBiz;
import net.mingsoft.cms.entity.CategoryEntity;
import net.mingsoft.cms.entity.ContentEntity;
import net.mingsoft.cms.util.CmsParserUtil;
import net.mingsoft.mdiy.bean.AttributeBean;
import net.mingsoft.mdiy.bean.PageBean;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.biz.IPageBiz;
import net.mingsoft.mdiy.entity.ModelEntity;
import net.mingsoft.mdiy.util.ParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态生成页面，需要后台配置自定义页数据
 *
 * @author 铭飞开源团队
 * @date 2018年12月17日
 */
@Controller("dynamicPageAction")
@RequestMapping("/mcms")
public class MCmsAction extends net.mingsoft.cms.action.BaseAction {

	/**
	 * 自定义页面业务层
	 */
	@Autowired
	private IPageBiz pageBiz;

	/**
	 * 文章管理业务处理层
	 */
	@Autowired
	private IContentBiz contentBiz;

	/**
	 * 栏目业务层
	 */
	@Autowired
	private ICategoryBiz categoryBiz;



	/**
	 * 搜索标签;
	 */
	public static final String SEARCH = "search";

	/**
	 * 自定义模型
	 */
	@Autowired
	private IModelBiz modelBiz;


	/**
	 * 动态列表页
	 */
	@GetMapping("/index.do")
	public void index(HttpServletRequest req, HttpServletResponse resp) {
		Map map = BasicUtil.assemblyRequestMap();
		map.forEach((k,v)->{
            map.put(k,v.toString().replaceAll("('|\"|\\\\)","\\\\$1"));
		});
		map.put(ParserUtil.URL, BasicUtil.getUrl());
		//动态解析
		map.put(ParserUtil.IS_DO,true);
		//设置动态请求的模块路径
		map.put(ParserUtil.MODEL_NAME, "mcms");
		//解析后的内容
		String content = "";
		try {
			//根据模板路径，参数生成
			content = CmsParserUtil.generate(ParserUtil.INDEX+ParserUtil.HTM_SUFFIX, map);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.outString(resp, content);
	}

	/**
	 * 动态列表页
	 * @param req
	 * @param resp
	 */
	@GetMapping("/list.do")
	public void list(HttpServletRequest req, HttpServletResponse resp) {
		Map map = BasicUtil.assemblyRequestMap();
		map.forEach((k,v)->{
			map.put(k,v.toString().replaceAll("('|\"|\\\\)","\\\\$1"));
		});
		//获取栏目编号
		int typeId = BasicUtil.getInt(ParserUtil.TYPE_ID,0);
		int size = BasicUtil.getInt(ParserUtil.SIZE,10);
		ContentBean contentBean = new ContentBean();
		contentBean.setContentCategoryId(String.valueOf(typeId));
		//获取文章总数
		List<CategoryBean> columnArticles = contentBiz.queryIdsByCategoryIdForParser(contentBean);
		//判断栏目下是否有文章
		if(columnArticles.size()==0){
			this.outJson(resp, false);
		}
		//设置分页类
		PageBean page = new PageBean();
		int total = PageUtil.totalPage(columnArticles.size(), size);
		map.put(ParserUtil.COLUMN, columnArticles.get(0));
		//获取总数
		page.setTotal(total);
		//设置栏目编号
		map.put(ParserUtil.TYPE_ID, typeId);
		//设置列表当前页
		map.put(ParserUtil.PAGE_NO, BasicUtil.getInt(ParserUtil.PAGE_NO,1));

		map.put(ParserUtil.URL, BasicUtil.getUrl());
		map.put(ParserUtil.PAGE, page);
		//动态解析
		map.put(ParserUtil.IS_DO,true);
		//设置动态请求的模块路径
		map.put(ParserUtil.MODEL_NAME, "mcms");
		//解析后的内容
		String content = "";
		try {
			//根据模板路径，参数生成
			content = CmsParserUtil.generate(columnArticles.get(0).getCategoryListUrl(),map);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.outString(resp, content);
	}

	/**
	 * 动态详情页
	 * @param id 文章编号
	 */
	@GetMapping("/view.do")
	public void view(String orderby,String order,HttpServletRequest req, HttpServletResponse resp) {
		//参数文章编号
		ContentEntity article = (ContentEntity) contentBiz.getEntity(BasicUtil.getInt(ParserUtil.ID));
		if(ObjectUtil.isNull(article)){
			this.outJson(resp, null,false,getResString("err.empty", this.getResString("id")));
			return;
		}
		if(StringUtils.isNotBlank(order)){
			//防注入
			if(!order.toLowerCase().equals("asc")&&!order.toLowerCase().equals("desc")){
				this.outJson(resp, null,false,getResString("err.error", this.getResString("order")));
				return;
			}
		}

		orderby= orderby.replaceAll("('|\"|\\\\)","\\\\$1");
		PageBean page = new PageBean();
		//用于详情上下页获取当前文章列表对应的分类，根据文章查询只能获取自身分类
		String typeId = BasicUtil.getString(ParserUtil.TYPE_ID,article.getContentCategoryId());
		//根据文章编号查询栏目详情模版
		CategoryEntity column = (CategoryEntity) categoryBiz.getEntity(Integer.parseInt(typeId));
		//解析后的内容
		String content = "";
		Map map = BasicUtil.assemblyRequestMap();
		map.forEach((k,v)->{
			//sql注入过滤
			map.put(k,v.toString().replaceAll("('|\"|\\\\)","\\\\$1"));
		});
		//动态解析
		map.put(ParserUtil.IS_DO,true);
		//设置栏目编号
		map.put(ParserUtil.TYPE_ID, typeId);
		//设置动态请求的模块路径
		map.put(ParserUtil.MODEL_NAME, "mcms");
		map.put(ParserUtil.URL, BasicUtil.getUrl());
		map.put(ParserUtil.PAGE, page);
		map.put(ParserUtil.ID, article.getId());
		ContentBean contentBean = new ContentBean();
		contentBean.setContentCategoryId(String.valueOf(typeId));
		contentBean.setOrderBy(orderby);
		contentBean.setOrder(order);
		List<CategoryBean> articleIdList = contentBiz.queryIdsByCategoryIdForParser(contentBean);
		Map<Object, Object> contentModelMap = new HashMap<Object, Object>();
		ModelEntity contentModel = null;
		for (int artId = 0; artId < articleIdList.size();) {
			//如果不是当前文章则跳过
			if(articleIdList.get(artId).getArticleId() != Integer.parseInt(article.getId())){
				artId++;
				continue;
			}
			// 文章的栏目路径
			String categoryParentId = articleIdList.get(artId).getId() ;
			if(StringUtils.isNotBlank(articleIdList.get(artId).getCategoryParentId())){
				categoryParentId += ','+articleIdList.get(artId).getCategoryParentId();
			}
			// 文章的栏目模型编号
			String columnContentModelId = articleIdList.get(artId).getMdiyModelId();
			Map<String, Object> parserParams = new HashMap<String, Object>();
			parserParams.put(ParserUtil.COLUMN, articleIdList.get(artId));
			// 判断当前栏目是否有自定义模型
			if ( StringUtils.isNotBlank(columnContentModelId) && Integer.parseInt(columnContentModelId)>0) {
				// 通过当前栏目的模型编号获取，自定义模型表名
				if (contentModelMap.containsKey(columnContentModelId)) {
					parserParams.put(ParserUtil.TABLE_NAME, contentModel.getModelTableName());
				} else {
					// 通过栏目模型编号获取自定义模型实体
					 contentModel=(ModelEntity)modelBiz.getEntity(Integer.parseInt(columnContentModelId));
					// 将自定义模型编号设置为key值
					contentModelMap.put(columnContentModelId, contentModel.getModelTableName());
					parserParams.put(ParserUtil.TABLE_NAME, contentModel.getModelTableName());
				}
			}
			// 第一篇文章没有上一篇
			if (artId > 0) {
				CategoryBean preCaBean = articleIdList.get(artId - 1);
					page.setPreId(preCaBean.getArticleId());
			}
			// 最后一篇文章没有下一篇
			if (artId + 1 < articleIdList.size()) {
				CategoryBean nextCaBean = articleIdList.get(artId + 1);
					page.setNextId(nextCaBean.getArticleId());
			}
			break;
		}
		try {
			//根据模板路径，参数生成
			content = CmsParserUtil.generate(column.getCategoryUrl(), map);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.outString(resp, content);
	}



	/**
	 * 实现前端页面的文章搜索
	 *
	 * @param request
	 *            搜索id
	 * @param response
	 */
	@RequestMapping(value = "search")
	@ResponseBody
	public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String, Object> map = new HashMap<>();
		// 读取请求字段
		Map<String, Object> field =  BasicUtil.assemblyRequestMap();
		// 自定义字段集合
		Map<String, String> diyFieldName = new HashMap<String, String>();
		CategoryEntity column = null; // 当前栏目
		ModelEntity contentModel = null; // 栏目对应模型
		List<DiyModelMap> fieldValueList = new ArrayList<DiyModelMap>(); // 栏目对应字段的值
		int typeId = 0;
		String categoryIds = BasicUtil.getString("categoryId");
		//当传递了栏目编号，但不是栏目集合
		if(!StringUtil.isBlank(categoryIds) && !categoryIds.contains(",")){
			typeId = Integer.parseInt(categoryIds);
		}
		String url = BasicUtil.getUrl();
		//记录自定义模型字段名
		List filedStr = new ArrayList<>();
		//根据栏目确定自定义模型
		if(typeId>0){
			column = (CategoryEntity) categoryBiz.getEntity(Integer.parseInt(typeId+""));
			// 获取表单类型的id
			if (column != null&&ObjectUtil.isNotNull(column.getMdiyModelId())) {
				contentModel = (ModelEntity) modelBiz.getEntity(Integer.parseInt(column.getMdiyModelId()));
				if (contentModel != null) {
					Map<String,String> fieldMap = contentModel.getFieldMap();
					for (String s : fieldMap.keySet()) {
						filedStr.add(fieldMap.get(s));
					}
					map.put(ParserUtil.TABLE_NAME, contentModel.getModelTableName());
				}
			}
			map.put(ParserUtil.COLUMN, column);
		}

		// 遍历取字段集合
		if (field != null) {
			for (Map.Entry<String, Object> entry : field.entrySet()) {
				if (entry != null) {
					if (ObjectUtil.isNull(entry.getValue())) {
						continue;
					}
                    String value = entry.getValue().toString().replaceAll("('|\"|\\\\)","\\\\$1"); // 处理由get方法请求中文乱码问题
                    value=clearXss(value);
					if (request.getMethod().equals(RequestMethod.GET)) { // 如果是get方法需要将请求地址参数转码
						try {
							value = new String(value.getBytes("ISO-8859-1"), Const.UTF8);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					// 保存至自定义字段集合
						if (!StringUtil.isBlank(value)) {
							diyFieldName.put(entry.getKey(), value);
							//判断请求中的是否是自定义模型中的字段
							if(filedStr.contains(entry.getKey())){
								//设置自定义模型字段和值
								DiyModelMap diyMap = new DiyModelMap();
								diyMap.setKey(entry.getKey());
								diyMap.setValue(value);
								fieldValueList.add(diyMap);
							}
						}

				}
			}
		}

		//添加自定义模型的字段和值
		if(fieldValueList.size()>0){
			map.put("diyModel", fieldValueList);
		}

		//设置分页类
		PageBean page = new PageBean();
		Map<String, Object> searchMap = field;
		StringBuilder urlParams=new StringBuilder();
		searchMap.forEach((k,v)->{
			//sql注入过滤
            if(v!=null){
                searchMap.put(k,v.toString().replaceAll("('|\"|\\\\)","\\\\$1"));
                searchMap.put(k,clearXss(searchMap.get(k).toString()));
                if(!ParserUtil.SIZE.equals(k)&&!ParserUtil.PAGE_NO.equals(k)){
                    urlParams.append(k).append("=").append(searchMap.get(k)).append("&");
                }
            }
		});

		//查询数量
		int count= contentBiz.getSearchCount(contentModel,fieldValueList,searchMap,BasicUtil.getAppId(),categoryIds);
		map.put(ParserUtil.URL, url);
		map.put(SEARCH, searchMap);
		map.put(ParserUtil.APP_ID, BasicUtil.getAppId());
		map.put(ParserUtil.PAGE, page);
		map.put(ParserUtil.HTML, ParserUtil.HTML);
		//动态解析
		map.put(ParserUtil.IS_DO,false);
		//设置动态请求的模块路径
		map.put(ParserUtil.MODEL_NAME, "mcms");
		searchMap.put(ParserUtil.PAGE_NO, 0);
		AttributeBean attributeBean = new AttributeBean();
		ParserUtil.read(SEARCH+ParserUtil.HTM_SUFFIX,map, page,attributeBean);
		int total = PageUtil.totalPage(count, page.getSize());
		int pageNo = BasicUtil.getInt(ParserUtil.PAGE_NO, 1);
		if(pageNo >= total && total!=0) {
			pageNo = total;
		}
		//获取总数
		page.setTotal(total);

		page.setPageNo(pageNo);

		//设置分页的统一链接
		 url = url +request.getServletPath() +"?" + urlParams;
		String pageNoStr = ParserUtil.SIZE+"="+page.getSize()+"&"+ParserUtil.PAGE_NO+"=";
		//下一页
		String nextUrl = url + pageNoStr+((pageNo+1 > total)?total:pageNo+1);
		//首页
		String indexUrl = url + pageNoStr + 1;
		//尾页
		String lastUrl = url + pageNoStr + total;
		//上一页 当前页为1时，上一页就是1
		String preUrl = url + pageNoStr + ((pageNo==1) ? 1:pageNo-1);

		page.setIndexUrl(indexUrl);
		page.setNextUrl(nextUrl);
		page.setPreUrl(preUrl);
		page.setLastUrl(lastUrl);

		//解析后的内容
		String content = "";
		try {
			//根据模板路径，参数生成
			content = CmsParserUtil.generate(SEARCH+ParserUtil.HTM_SUFFIX,map);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.outString(response, content);
	}

	// 清除路径中的转义字符
	private String clearXss(String value) {

		if (value == null || "".equals(value)) {
			return value;
		}

		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replace("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
				"\"\"");
		value = value.replace("script", "");

		return value;
	}

	/**
	 * 存储自定义模型字段和接口参数
	 * @author 铭飞开源团队
	 * @date 2019年3月5日
	 */
	public class DiyModelMap {
		String key;
		Object value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}




}
