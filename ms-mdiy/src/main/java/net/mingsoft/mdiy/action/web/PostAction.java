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
package net.mingsoft.mdiy.action.web;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.action.BaseAction;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.biz.IPostBiz;
import net.mingsoft.mdiy.entity.PostEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用自定义表单
 *
 * @author 铭飞开发团队
 * @version 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@Api("通用自定义表单")
@Controller("webDiyPost")
@RequestMapping("/mdiy/post")
public class PostAction extends BaseAction {

    /**
     * 自定义表单业务处理层
     */

    @Autowired
	private IPostBiz postBiz;
    /**
     * 自定义模型业务处理层
     */

    @Autowired
	private IModelBiz modelBiz;

    /**
     * 保存
     *
     * @param idBase64 Base64编码数据
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     */
    @ApiOperation("保存")
    @ApiImplicitParam(name = "idBase64", value = "Base64编码数据", required = true, paramType = "path")
    @PostMapping("{idBase64}")
    @ResponseBody
    public void save(@PathVariable("idBase64") @ApiIgnore String idBase64, HttpServletRequest request, HttpServletResponse response) {
        String temp = this.decryptByAES(request, idBase64);
        //在进行自定义表单提交数据时是否需要提交验证码，默认是需要验证码
        //如果isCode为空获取，isCode=true，则进行验证码的验证
        if (request.getParameter("rand_code") != null) {
            if (!this.checkRandCode()) {
                this.outJson(response, null, false);
                return;
            }
        }
        //判断传入的加密数字是否能转换成整形
        if (!NumberUtils.isCreatable(temp)) {
            this.outJson(response, null, false);
            return;
        }
        //获取表单id
        int formId = Integer.parseInt(temp);
        postBiz.saveDiyFormData(formId,BasicUtil.assemblyRequestMap());
        this.outJson(response, true);

    }

    /**
     * 提供前端查询自定义表单提交数据
     *
     * @param idBase64 Base64编码数据
     * @param request
     * @param response
     */
    @ApiOperation(value = "提供前端查询自定义表单提交数据")
    @ApiImplicitParam(name = "idBase64", value = "Base64编码数据", required = true, paramType = "path")
    @GetMapping("{idBase64}/queryData")
    @ResponseBody
    public void queryData(@PathVariable("idBase64") @ApiIgnore String idBase64, HttpServletRequest request, HttpServletResponse response) {
        String temp = this.decryptByAES(request, idBase64);
		//判断传入的加密数字是否能转换成整形
		if (!NumberUtils.isCreatable(temp)) {
			this.outJson(response, null, false);
			return;
		}
		//获取表单id
		int formId = Integer.parseInt(temp);
        List list = postBiz.queryDiyFormData(formId,BasicUtil.assemblyRequestMap());
		if (ObjectUtil.isNotNull(list) ) {
            this.outJson(response, JSONObject.toJSONString(new EUListBean(list,(int)BasicUtil.endPage(list).getTotal())));
			return;
		}
        this.outJson(response, false);
    }   /**
     * 提供前端查询自定义表单提交数据
     *
     * @param idBase64 Base64编码数据
     * @param request
     * @param response
     */
    @ApiOperation(value = "查询自定义模型字段")
    @ApiImplicitParam(name = "idBase64", value = "Base64编码数据", required = true, paramType = "path")
    @GetMapping("{idBase64}/field")
    @ResponseBody
    public void field(@PathVariable("idBase64") @ApiIgnore String idBase64, HttpServletRequest request, HttpServletResponse response) {
        String temp = this.decryptByAES(request, idBase64);
		//判断传入的加密数字是否能转换成整形
		if (!NumberUtils.isCreatable(temp)) {
			this.outJson(response, null, false);
			return;
		}
		//获取表单id
		int formId = Integer.parseInt(temp);
        PostEntity postEntity = (PostEntity)modelBiz.getEntity(formId);
        if (ObjectUtil.isNotNull(postEntity)) {
            this.outJson(response, postEntity.getModelField());
			return;
		}
        this.outJson(response, false);
    }

}
