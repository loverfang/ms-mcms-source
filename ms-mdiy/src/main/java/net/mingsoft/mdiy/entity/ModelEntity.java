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
package net.mingsoft.mdiy.entity;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mingsoft.base.entity.BaseEntity;

import java.util.*;

/**
* 自定义模型实体
* @author SMILE
* 创建日期：2019-11-9 15:53:59<br/>
* 历史修订：<br/>
*/
public class ModelEntity extends BaseEntity {

private static final long serialVersionUID = 1573286039152L;

	/**
	* 模型名称
	*/
	private String modelName;
	/**
	* 模型表名
	*/
	private String modelTableName;
	/**
	* 应用编号
	*/
	private Integer modelAppId;
	/**
	* 类型
	*/
	private String modelType;
	/**
	* 下拉选择框
	*/
	private String modelCustomType;
	/**
	* json
	*/
	private String modelJson;
	/**
	* 自定义字段
	*/
	private String modelField;


	/**
	* 设置模型名称
	*/
	public void setModelName(String modelName) {
	this.modelName = modelName;
	}

	/**
	* 获取模型名称
	*/
	public String getModelName() {
	return this.modelName;
	}
	/**
	* 设置模型表名
	*/
	public void setModelTableName(String modelTableName) {
	this.modelTableName = modelTableName;
	}

	/**
	* 获取模型表名
	*/
	public String getModelTableName() {
	return this.modelTableName;
	}
	/**
	* 设置应用编号
	*/
	public void setModelAppId(Integer modelAppId) {
	this.modelAppId = modelAppId;
	}

	/**
	* 获取应用编号
	*/
	public Integer getModelAppId() {
	return this.modelAppId;
	}
	/**
	* 设置类型
	*/
	public void setModelType(String modelType) {
	this.modelType = modelType;
	}

	/**
	* 获取类型
	*/
	public String getModelType() {
	return this.modelType;
	}
	/**
	* 设置下拉选择框
	*/
	public void setModelCustomType(String modelCustomType) {
	this.modelCustomType = modelCustomType;
	}

	/**
	* 获取下拉选择框
	*/
	public String getModelCustomType() {
	return this.modelCustomType;
	}
	/**
	* 设置json
	*/
	public void setModelJson(String modelJson) {
	this.modelJson = modelJson;
	}

	/**
	* 获取json
	*/
	public String getModelJson() {
	return this.modelJson;
	}
	/**
	* 设置自定义字段
	*/
	public void setModelField(String modelField) {
	this.modelField = modelField;
	}

	/**
	* 获取自定义字段
	*/
	public String getModelField() {
	return this.modelField;
	}

	public Map getFieldMap(){
		Map map=new  HashMap();
		List<Map> list= JSONObject.parseArray(modelField,Map.class);
		if(ObjectUtil.isNotNull(list)){
			for (Map field : list) {
				map.put(field.get("model"),field.get("key"));
			}
		}
		return map;
	}
}
