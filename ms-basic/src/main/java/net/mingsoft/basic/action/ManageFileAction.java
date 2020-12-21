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
package net.mingsoft.basic.action;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 上传文件
 */
@Api("上传文件接口")
@Controller("ManageFileAction")
@RequestMapping("${ms.manager.path}/file")
public class ManageFileAction extends BaseFileAction {


	@Value("${ms.upload.denied}")
	private String uploadFileDenied;


	/**
	 * 处理post请求上传文件
	 * 可以自定义项目路径下任意文件夹
	 * @param req
	 *            HttpServletRequest对象
	 * @param res
	 *            HttpServletResponse 对象
	 * @throws ServletException
	 *             异常处理
	 * @throws IOException
	 *             异常处理
	 */
	@ApiOperation(value = "处理post请求上传文件")
	@LogAnn(title = "处理post请求上传文件",businessType= BusinessTypeEnum.OTHER)
	@PostMapping("/upload")
	@ResponseBody
	public String upload(Bean bean, boolean uploadFloderPath, HttpServletRequest req, HttpServletResponse res) throws IOException {
		//非法路径过滤
		if(checkUploadPath(bean)){
			this.outString(res,"");
		}

		Config config = new Config(bean.getUploadPath(),bean.getFile(),null,uploadFloderPath,bean.isRename());
		String path = this.upload(config);
		return path;
	}

	@ApiOperation(value = "处理post请求上传模板文件")
	@PostMapping("/uploadTemplate")
	@ResponseBody
	public String uploadTemplate(Bean bean, boolean uploadFloderPath, HttpServletRequest req, HttpServletResponse res) throws IOException {
		//非法路径过滤
		if(checkUploadPath(bean)){
			this.outString(res,"");
		}

		Config config = new Config(bean.getUploadPath(),bean.getFile(),null,uploadFloderPath,bean.isRename());
		String path = this.uploadTemplate(config);
		return path;
	}

	protected boolean checkUploadPath(Bean bean){
		return (bean.getUploadPath()!=null&&(bean.getUploadPath().contains("../")||bean.getUploadPath().contains("..\\")));
	}
}
