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
package net.mingsoft.mdiy.tag;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.mingsoft.mdiy.bean.AttributeBean;
import net.mingsoft.mdiy.bean.PageBean;
import net.mingsoft.mdiy.bean.TagSqlBean;
import net.mingsoft.mdiy.util.ParserUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: huise
 * @Description: 自定义分页标签
 * @Date: Create in 2020/06/23 9:16
 */

public class PageListTag extends CustomTag {

    private PageBean pageBean;

    private AttributeBean attributeBean;

    public PageListTag(Map data, TagSqlBean tag,PageBean page){
        super(data,tag);
        this.pageBean = page;
    }
    public PageListTag(Map data, TagSqlBean tag, PageBean page, AttributeBean attributeBean){
        super(data,tag);
        this.pageBean = page;
        this.attributeBean = attributeBean;
    }

    @Override
    public void execute(Environment environment, Map params, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        boolean ispaging = false;
        //分页设置分页标签
        if(pageBean!=null&&params.containsKey("ispaging")){
             ispaging = (boolean)build.unwrap((TemplateModel) params.get("ispaging"), boolean.class);
             if(ispaging){
                 int size = 10;
                 if(params.containsKey(ParserUtil.SIZE)){
                     size = (int)build.unwrap((TemplateModel) params.get(ParserUtil.SIZE), int.class);
                 }
                 pageBean.setSize(size);
             }
        }
        //属性值
        if(params.containsKey("flag")){
            attributeBean.setFlag((String)build.unwrap((TemplateModel) params.get("flag"), String.class));
        }
        if(params.containsKey("noflag")){
            attributeBean.setNoflag((String)build.unwrap((TemplateModel) params.get("noflag"), String.class));
        }
        if(params.containsKey("orderby")){
            attributeBean.setOrderby((String)build.unwrap((TemplateModel) params.get("orderby"), String.class));
        }
        if(params.containsKey("order")){
            attributeBean.setOrder((String)build.unwrap((TemplateModel) params.get("order"), String.class));
        }

        super.execute(environment,params,templateModels,templateDirectiveBody);
    }



}
