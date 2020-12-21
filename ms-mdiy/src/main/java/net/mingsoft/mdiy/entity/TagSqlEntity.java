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

import com.fasterxml.jackson.annotation.JsonFormat;

import net.mingsoft.base.entity.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

 /**
 * 标签对应多个sql语句实体
 * @author 铭飞开发团队
 * 创建日期：2018-11-26 11:42:01<br/>
 * 历史修订：<br/>
 */
public class TagSqlEntity extends BaseEntity {

	private static final long serialVersionUID = 1543203721085L;
	
	/**
	 * 自定义标签编号
	 */
	private Integer tagId; 
	/**
	 * 自定义sql支持ftl写法
	 */
	private String tagSql; 
	/**
	 * 排序升序
	 */
	private Integer sort;
		

	/**
	 * 设置自定义标签编号
	 */
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	/**
	 * 获取自定义标签编号
	 */
	public Integer getTagId() {
		return this.tagId;
	}
	/**
	 * 设置自定义sql支持ftl写法
	 */
	public void setTagSql(String tagSql) {
		this.tagSql = tagSql;
	}

	/**
	 * 获取自定义sql支持ftl写法
	 */
	public String getTagSql() {
		return this.tagSql;
	}
	/**
	 * 设置排序升序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取排序升序
	 */
	public Integer getSort() {
		return this.sort;
	}
}
