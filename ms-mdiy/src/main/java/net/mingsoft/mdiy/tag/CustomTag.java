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

import cn.hutool.core.collection.CollUtil;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.mdiy.bean.TagSqlBean;
import net.mingsoft.mdiy.util.ParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huise
 * @Description: 自定义标签
 * @参考: https://www.cnblogs.com/zhangyadong/p/9681532.html
 * @Date: Create in 2020/06/23 9:16
 */

public class CustomTag implements TemplateDirectiveModel {
    protected static BeansWrapper build = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
    /*
     * log4j日志记录
     */
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 解析标签用的map
     */
    private Map data;
    /**
     * 标签sql模板
     */
    private TagSqlBean tag;
    /**
     * 传出的变量名
     */
    private String variableName;

    public CustomTag(Map data, TagSqlBean tag){
        this.data = data;
        this.tag = tag;
        this.variableName = "field";
    }

    public CustomTag(Map data, TagSqlBean tag, String variableName){
        this.data = data;
        this.tag = tag;
        this.variableName = variableName;
    }

    /**
     * @param environment 系统环境变量，通常用它来输出相关内容，如Writer out = env.getOut()。
     * @param params　页面上自定义标签传过来的对象，其key=自定义标签的参数名，value值是TemplateModel类型。
     * @param templateModels　循环变量:一个，可选。它给出了当前重复的次数，从1开始。
     * @param templateDirectiveBody　body: 用于处理自定义标签中的内容
     * @throws TemplateException　
     * @throws IOException
     */
    @Override
    public void execute(Environment environment,
                        Map params,
                        TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        HashMap<Object, Object> root = CollUtil.newHashMap();
        //历史变量
        //将外部传入的压入
        root.putAll(data);
        //将标签传入参数逐一的压入
        params.forEach((k,v)->{
            if(v instanceof TemplateModel){
                try {
                    root.put(k,build.unwrap((TemplateModel)v));
                } catch (TemplateModelException e) {
                    e.printStackTrace();
                    LOG.error("转换参数错误 key:{} -{}",k,e);
                }
            }
        });

        //压入了栏目则传入sql模板
        TemplateModel column = environment.getVariable(ParserUtil.COLUMN);
        if(column!=null){
            root.put(ParserUtil.COLUMN,build.unwrap(column));
        }

        String sql = ParserUtil.rendering(root, tag.getTagSql());
        NamedParameterJdbcTemplate jdbc = SpringUtil.getBean(NamedParameterJdbcTemplate.class);
        List _list = jdbc.queryForList(sql,CollUtil.newHashMap());
        TemplateModel oldVar = environment.getVariable(variableName);
        //渲染标签
        _list.forEach(x->{
            try {
                TemplateModel columnModel = build.wrap(x);
                //如果自定义了变量则赋值自定义变量
                if(templateModels.length>0){
                    templateModels[0] = columnModel;
                }else {
                    environment.setVariable(variableName, columnModel);
                }
                //压入 以便嵌套使用
                environment.setVariable(ParserUtil.COLUMN, columnModel);
                //渲染
                templateDirectiveBody.render(environment.getOut());
                //渲染完成还原变量
                environment.setVariable(ParserUtil.COLUMN,column);
                environment.setVariable(variableName,oldVar);

            } catch (TemplateModelException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
