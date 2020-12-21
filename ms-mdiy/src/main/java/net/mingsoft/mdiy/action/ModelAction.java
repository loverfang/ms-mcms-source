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


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.base.util.JSONObject;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.entity.ModelEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * 自定义模型管理控制层
 * @author SMILE
 * 创建日期：2019-11-7 10:03:44<br/>
 * 历史修订：<br/>
 */
@Api(value = "自定义模型接口")
@Controller("mdiyModelAction")
@RequestMapping("/${ms.manager.path}/mdiy/model")
public class ModelAction extends net.mingsoft.mdiy.action.BaseAction{
	
	private final static String TYPE="model";
	/**
	 * 注入自定义模型业务层
	 */	
	@Autowired
	private IModelBiz modelBiz;

	/**
	* 寻找文件正则
	*/
	private  Pattern  filePattern = Pattern.compile("(src|href)=\"(upload/.*?(png|jpg|gif))");
	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/mdiy/model/index";
	}
	
	/**
	 * 查询自定义模型列表
	 * @param model 自定义模型实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelName 模型名称<br/>
	 * modelTableName 模型表名<br/>
	 * modelAppId 应用编号<br/>
	 * modelJson json<br/>
	 * id 编号<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * modelName: 模型名称<br/>
	 * modelTableName: 模型表名<br/>
	 * modelAppId: 应用编号<br/>
	 * modelJson: json<br/>
	 * id: 编号<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@ApiOperation(value = "查询自定义模型列表接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "modelName", value = "模型名称", required =false,paramType="query"),
    	@ApiImplicitParam(name = "modelTableName", value = "模型表名", required =false,paramType="query"),
    	@ApiImplicitParam(name = "modelAppId", value = "应用编号", required =false,paramType="query"),
    	@ApiImplicitParam(name = "modelJson", value = "json", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
    	@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
	@GetMapping("/list")
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model,BindingResult result) {
		modelEntity.setModelAppId(BasicUtil.getAppId());
		modelEntity.setModelCustomType(TYPE);
		BasicUtil.startPage();
		List modelList = modelBiz.query(modelEntity);
		return ResultData.build().success(new EUListBean(modelList,(int)BasicUtil.endPage(modelList).getTotal()));
	}
	
	/**
	 * 返回编辑界面model_form
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute ModelEntity modelEntity,HttpServletResponse response,HttpServletRequest request,ModelMap modelMap){
		if(modelEntity.getId()!=null){
			BaseEntity _modelEntity = modelBiz.getEntity(Integer.parseInt(modelEntity.getId()));
            modelMap.addAttribute("modelEntity",_modelEntity);
		}
		return "/mdiy/model/form";
	}


	/**
	 * 返回自定义模型数据
	 */
	@GetMapping("/data")
	@ResponseBody
	public ResultData data(Integer modelId,String linkId,HttpServletResponse response,HttpServletRequest request,ModelMap modelMap){
		ModelEntity model = (ModelEntity)modelBiz.getEntity(modelId);
		Map data=null;
		if(ObjectUtil.isNotNull(model)){
			List<Map> listMap = (List<Map>)modelBiz.excuteSql(StrUtil.format("select * from {} where link_id = '{}' limit 0,1",model.getModelTableName(),linkId));
			if(listMap.size()>0){
				data=new HashMap();
				for (Object o : listMap.get(0).keySet()) {
					data.put(getCamelCaseString(o.toString(),false),listMap.get(0).get(o));
				}
			}
		}
		return ResultData.build().success(data);
	}

	private  String getCamelCaseString(String inputString,
											boolean firstCharacterUppercase) {
		StringBuilder sb = new StringBuilder();

		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);

			switch (c) {
				case '_':
				case '-':
				case '@':
				case '$':
				case '#':
				case ' ':
				case '/':
				case '&':
					if (sb.length() > 0) {
						nextUpperCase = true;
					}
					break;

				default:
					if (nextUpperCase) {
						sb.append(Character.toUpperCase(c));
						nextUpperCase = false;
					} else {
						sb.append(Character.toLowerCase(c));
					}
					break;
			}
		}

		if (firstCharacterUppercase) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	@LogAnn(title = "保存模型",businessType= BusinessTypeEnum.INSERT)
	@PostMapping("/data/save")
	@ResponseBody
	public ResultData save(Integer modelId,String linkId,HttpServletResponse response,HttpServletRequest request,ModelMap modelMap){
		ModelEntity model = (ModelEntity)modelBiz.getEntity(modelId);
		if(ObjectUtil.isNotNull(model)&&StringUtils.isNotBlank(linkId)){
			Map<String, Object> requestMap = BasicUtil.assemblyRequestMap();
			Map fieldMap= model.getFieldMap();
			HashMap<String, Object> fields = new HashMap<>();
			//拼接字段
			for (String s : requestMap.keySet()) {
				//判断是否存在此字段
				if(fieldMap.containsKey(s)){
					fields.put(fieldMap.get(s).toString(),requestMap.get(s));
				}
			}
			fields.put("link_id",linkId);
			modelBiz.insertBySQL(model.getModelTableName(),fields);
			return ResultData.build().success();
		}
		return ResultData.build().error();
	}

	@LogAnn(title = "更新分类",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/data/update")
	@ResponseBody
	public ResultData update(Integer modelId,String linkId,HttpServletResponse response,HttpServletRequest request,ModelMap modelMap){
		ModelEntity model = (ModelEntity)modelBiz.getEntity(modelId);
		Map data=null;
		//效验参数
		if(ObjectUtil.isNotNull(model)&&StringUtils.isNotBlank(linkId)){
			Map<String, Object> requestMap = BasicUtil.assemblyRequestMap();
			Map fieldMap= model.getFieldMap();
			HashMap<String, Object> fields = new HashMap<>();
			//拼接字段
			for (String s : requestMap.keySet()) {
				//判断是否存在此字段
				if(fieldMap.containsKey(s)){
					fields.put(fieldMap.get(s).toString(),requestMap.get(s));
				}
			}
			Map where = new HashMap();
			where.put("link_id",linkId);
			//更新
			modelBiz.updateBySQL(model.getModelTableName(),fields,where);
			return ResultData.build().success();
		}
		return ResultData.build().error();
	}




	/**
	 * 获取自定义模型
	 * @param modelEntity 自定义模型实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelName 模型名称<br/>
	 * modelTableName 模型表名<br/>
	 * modelAppId 应用编号<br/>
	 * modelJson json<br/>
	 * id 编号<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * modelName: 模型名称<br/>
	 * modelTableName: 模型表名<br/>
	 * modelAppId: 应用编号<br/>
	 * modelJson: json<br/>
	 * id: 编号<br/>
	 * }</dd><br/>
	 */
	@ApiOperation(value = "获取自定义模型列表接口")
    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap modelMap){
		if(modelEntity.getId()==null) {
			return null;
		}
		modelEntity.setModelCustomType(TYPE);
		modelEntity.setModelAppId(BasicUtil.getAppId());
		ModelEntity _model = (ModelEntity)modelBiz.getEntity(Integer.parseInt(modelEntity.getId()));
		return ResultData.build().success(_model);
	}

	@ApiOperation(value = "导入")
	@ApiImplicitParams({
            @ApiImplicitParam(name = "modelJson", value = "json", required =true,paramType="query"),
	})
	@LogAnn(title = "导入分类",businessType= BusinessTypeEnum.INSERT)
	@PostMapping("/importJson")
	@ResponseBody
	@RequiresPermissions("mdiy:model:importJson")
	public ResultData importJson(@ModelAttribute @ApiIgnore ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request,BindingResult result) {
		String model="mdiy_model_";
		//验证json的值是否合法
		if(StringUtil.isBlank(modelEntity.getModelJson())){
			return ResultData.build().error(getResString("err.empty", this.getResString("model.json")));
		}
		Map map = new HashMap();
		try{
			map = com.alibaba.fastjson.JSONObject.parseObject(modelEntity.getModelJson(), Map.class);
		}catch (Exception e){
			return ResultData.build().error(getResString("err.error", this.getResString("model.json")));
		}
		ModelEntity _modelEntity=new ModelEntity();
		//在表名前面拼接前缀
		String tableName=model+map.get("tableName");
		_modelEntity.setModelTableName(tableName);
		_modelEntity.setModelAppId(BasicUtil.getAppId());
		modelEntity.setModelCustomType(TYPE);
		//判断表名是否存在
		if(ObjectUtil.isNotNull(modelBiz.getEntity(_modelEntity))){
			return ResultData.build().error(getResString("err.exist", this.getResString("table.name")));
		}



		Map json= new HashMap();
		json.put("html",map.get("html"));
		json.put("script",map.get("script"));
		//创建表
		modelBiz.excuteSql(map.get("sql").toString().replace("{model}",model));
		//添加关联id
		modelBiz.excuteSql(StrUtil.format("ALTER TABLE `{}` add COLUMN `{}` int NOT NULL FIRST",tableName,"link_id"));
		modelEntity.setModelField(map.get("field").toString());
		modelEntity.setModelTableName(tableName);
		modelEntity.setModelName(map.get("title").toString());
		modelEntity.setModelAppId(BasicUtil.getAppId());
		modelEntity.setModelJson(JSONObject.toJSONString(json));
		//保存自定义模型实体
		modelBiz.saveEntity(modelEntity);
		return ResultData.build().success(modelEntity);
	}


	/**
	 * @param models 自定义模型实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * id:多个id直接用逗号隔开,例如id=1,2,3,4
	 * 批量删除自定义模型
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@ApiOperation(value = "批量删除自定义模型列表接口")
	@LogAnn(title = "批量删除自定义模型列表接口",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mdiy:model:del")
	public ResultData delete(@RequestBody List<ModelEntity> models,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[models.size()];
		for(int i = 0;i<models.size();i++){
			ids[i] =Integer.parseInt(models.get(i).getId()) ;
		}
		modelBiz.delete(ids);
		return ResultData.build().success();
	}


}
