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

import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.entity.AppEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author by 铭飞开源团队
 * @Description TODO
 * @date 2020/1/10 8:39
 */
@Controller("indexAction")
@RequestMapping("index")
public class IndexAction {

    private static String  INDEX = "index.html", DEFAULT = "default.html";
    /**
     * 注入站点业务层
     */
    private IAppBiz appBiz;

    /**
     * 访问站点主页
     *
     * @param req
     *            HttpServletRequest对象
     * @param res
     *            HttpServletResponse 对象
     * @throws ServletException
     *             异常处理
     * @throws IOException
     *             异常处理
     */
    @ApiOperation(value = "访问站点主页")
    @RequestMapping
    public String index( HttpServletRequest req, HttpServletResponse res) throws IOException {
        // 获取用户所请求的域名地址
        appBiz = SpringUtil.getBean(IAppBiz.class);
        // 查询数据库获取域名对应Id
        int websiteId = 0;
        AppEntity website = appBiz.getByUrl(BasicUtil.getDomain());
        if (website != null) {
            websiteId = website.getAppId();
        } else {
            return "";
        }
        String path = "";

        if (!StringUtils.isEmpty(website.getAppMobileStyle())) {
            path = BasicUtil.isMobileDevice() ? "m" : ""; // 如果是手机访问就跳转到相应页面
        }

        String defaultHtmlPath = BasicUtil.getRealPath("html" + File.separator + websiteId + File.separator + path + File.separator + "default.html");
        File file = new File(defaultHtmlPath);
        String url = "html" + Const.SEPARATOR + websiteId + Const.SEPARATOR + path;
        String indexPosition = url + Const.SEPARATOR + INDEX;
        if (file.exists()) {
            indexPosition = url + Const.SEPARATOR + DEFAULT;
        }
        return "forward:"+indexPosition;
    }
}
