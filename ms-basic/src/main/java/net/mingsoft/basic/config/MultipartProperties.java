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
/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mingsoft.basic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.MultipartConfigElement;

/**
 * 自动注入上传配置
 * @author huise
 */
@ConfigurationProperties(prefix = "ms.upload.multipart", ignoreUnknownFields = false)
public class MultipartProperties {
	/**
	 * 文件大小
	 */
	private long maxFileSize = 1024;
	/**
	 * 最大请求大小
	 */
	private long maxRequestSize = 10240;
	/**
	 * 开启延时加载
	 */
	private boolean resolveLazily = false;
	/**
	 * 文件编码
	 */
	private String defaultEncoding =  "ISO-8859-1";
	/**
	 * 文件临时存放目录
	 */
	private Resource uploadTempDir = null;
	/**
	 * 临时文件大小
	 */
	private int maxInMemorySize = 4096;



	public long getMaxFileSize() {
		return maxFileSize*1000;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getMaxRequestSize() {
	    if(maxRequestSize>0){
            return maxRequestSize*1000;
        }else {
	        return maxRequestSize;
        }
	}

	public void setMaxRequestSize(long maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}


	public boolean isResolveLazily() {
		return resolveLazily;
	}

	public void setResolveLazily(boolean resolveLazily) {
		this.resolveLazily = resolveLazily;
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public Resource getUploadTempDir() {
		return uploadTempDir;
	}

	public void setUploadTempDir(Resource uploadTempDir) {
		this.uploadTempDir = uploadTempDir;
	}

	public int getMaxInMemorySize() {
		return maxInMemorySize;
	}

	public void setMaxInMemorySize(int maxInMemorySize) {
		this.maxInMemorySize = maxInMemorySize;
	}
}
