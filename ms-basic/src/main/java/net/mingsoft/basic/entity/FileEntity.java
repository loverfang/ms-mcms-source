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
package net.mingsoft.basic.entity;

import net.mingsoft.base.entity.BaseEntity;

 /**
 * 基础文件表实体
 * @author 铭飞开发团队
 * 创建日期：2018-12-29 9:18:56<br/>
 * 历史修订：<br/>
 */
public class FileEntity extends BaseEntity {

	private static final long serialVersionUID = 1546046336900L;
	
	/**
	 * 文件分类编号
	 */
	private Integer categoryId; 
	/**
	 * APP编号
	 */
	private Integer appId; 
	/**
	 * 文件名称
	 */
	private String fileName; 
	/**
	 * 文件链接
	 */
	private String fileUrl; 
	/**
	 * 文件大小
	 */
	private Integer fileSize; 
	/**
	 * 文件详情Json数据
	 */
	private String fileJson; 
	/**
	 * 文件类型：图片、音频、视频等
	 */
	private String fileType; 
	/**
	 * 子业务
	 */
	private String isChild; 
		
	/**
	 * 设置文件分类编号
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * 获取文件分类编号
	 */
	public Integer getCategoryId() {
		return this.categoryId;
	}
	/**
	 * 设置APP编号
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * 获取APP编号
	 */
	public Integer getAppId() {
		return this.appId;
	}
	/**
	 * 设置文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取文件名称
	 */
	public String getFileName() {
		return this.fileName;
	}
	/**
	 * 设置文件链接
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * 获取文件链接
	 */
	public String getFileUrl() {
		return this.fileUrl;
	}
	/**
	 * 设置文件大小
	 */
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 获取文件大小
	 */
	public Integer getFileSize() {
		return this.fileSize;
	}
	/**
	 * 设置文件详情Json数据
	 */
	public void setFileJson(String fileJson) {
		this.fileJson = fileJson;
	}

	/**
	 * 获取文件详情Json数据
	 */
	public String getFileJson() {
		return this.fileJson;
	}
	/**
	 * 设置文件类型：图片、音频、视频等
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取文件类型：图片、音频、视频等
	 */
	public String getFileType() {
		return this.fileType;
	}
	/**
	 * 设置子业务
	 */
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}

	/**
	 * 获取子业务
	 */
	public String getIsChild() {
		return this.isChild;
	}


}
