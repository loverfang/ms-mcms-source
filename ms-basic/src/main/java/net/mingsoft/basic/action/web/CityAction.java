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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.bean.CityBean;
import net.mingsoft.basic.biz.ICityBiz;
import net.mingsoft.basic.entity.CityEntity;
import net.mingsoft.basic.util.BasicUtil;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 省市县镇村数据管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：100<br/>
 * 创建日期：2017-7-27 14:47:29<br/>
 * 历史修订：<br/>
 */
@Api("省市县镇村数据管理接口")
@Controller("webCityAction")
@RequestMapping("/basic/city")
public class CityAction extends net.mingsoft.basic.action.BaseAction{

	/**
	 * 注入省市县镇村数据业务层
	 */
	@Autowired
	private ICityBiz cityBiz;


	@ApiOperation(value = "查询省市县镇村数据列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "provinceId", value = "省／直辖市／自治区级id", required = false,paramType="query"),
		@ApiImplicitParam(name = "provinceName", value = "省／直辖市／自治区级名称", required = false,paramType="query"),
		@ApiImplicitParam(name = "cityId", value = "市级id", required = false,paramType="query"),
		@ApiImplicitParam(name = "cityName", value = "市级名称", required = false,paramType="query"),
		@ApiImplicitParam(name = "cityPy", value = "城市拼音首字母", required = false,paramType="query"),
		@ApiImplicitParam(name = "countyId", value = "县／区级id", required = false,paramType="query"),
		@ApiImplicitParam(name = "countyName", value = "县／区级名称", required = false,paramType="query"),
		@ApiImplicitParam(name = "townId", value = "街道／镇级id", required = false,paramType="query"),
		@ApiImplicitParam(name = "townName", value = "街道／镇级名称", required = false,paramType="query"),
		@ApiImplicitParam(name = "villageId", value = "村委会id", required = false,paramType="query"),
		@ApiImplicitParam(name = "villageName", value = "村委会名称", required = false,paramType="query"),
	})
	@GetMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute @ApiIgnore CityEntity city,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		BasicUtil.startPage();
		List cityList = cityBiz.query(city);
		BasicUtil.endPage(cityList);
		this.outJson(response, JSONArray.toJSONStringWithDateFormat(cityList, "yyyy-MM-dd"));
	}


	@ApiOperation(value = "获取省市县镇村数据")
	@ApiImplicitParam(name = "id", value = "城市主键编号", required = true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute @ApiIgnore CityEntity city,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(StringUtils.isEmpty(city.getId())) {
			this.outJson(response, false, getResString("err.error", this.getResString("id")));
			return;
		}
		CityEntity _city = (CityEntity)cityBiz.getEntity(Integer.parseInt(city.getId()));
		this.outJson(response, _city);
	}


	@ApiOperation(value = "更新省市县镇村数据信息省市县镇村数据")
	@GetMapping("/query")
	@ResponseBody
	public void query(HttpServletResponse response,HttpServletRequest request) {
		int level = BasicUtil.getInt("level",3);//默认3级
		String type = BasicUtil.getString("type","tree"); //默认为树形结构
		List<CityBean> cityList = (List<CityBean>) cityBiz.queryForTree(level,type);
		this.outJson(response, JSONArray.toJSONString(cityList));
	}
	/**
	 * 根据拼音首字母查询城市
	 * @param response
	 * @param request
	 */
	@ApiOperation(value = "根据拼音首字母查询城市")
	@ApiImplicitParam(name = "cityPy", value = "城市拼音首字母", required = true,paramType="query")
	@GetMapping("/queryCityPy")
	@ResponseBody
	public void queryPy(HttpServletResponse response,HttpServletRequest request) {
		String cityPy = BasicUtil.getString("cityPy");
		CityEntity city = new CityEntity();
		city.setCityPy(cityPy);
		List<CityEntity> cityList = (List<CityEntity>) cityBiz.query(city);
		this.outJson(response, JSONArray.toJSONString(cityList));
	}

}
