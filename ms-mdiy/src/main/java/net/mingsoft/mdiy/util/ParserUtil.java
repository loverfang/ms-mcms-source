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
package net.mingsoft.mdiy.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.mdiy.bean.AttributeBean;
import net.mingsoft.mdiy.bean.PageBean;
import net.mingsoft.mdiy.bean.TagSqlBean;
import net.mingsoft.mdiy.biz.ITagSqlBiz;
import net.mingsoft.mdiy.constant.e.TagTypeEnum;
import net.mingsoft.mdiy.tag.CustomTag;
import net.mingsoft.mdiy.tag.IncludeExTag;
import net.mingsoft.mdiy.tag.PageListTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ParserUtil {
    /*
     * log4j日志记录
     */
    protected final static Logger LOG = LoggerFactory.getLogger(ParserUtil.class);
    /**
     * 存放模版的文件夹
     */
    public static final String TEMPLATES = "templets";
    /**
     * 静态文件生成路径;例如：mcms/html/1
     */
    public static final String HTML = "html";
    /**
     * index
     */
    public static final String INDEX = "index";

    /**
     * 文件夹路径名;例如：1/58/71.html
     */
    public static final String HTML_SUFFIX = ".html";
    /**
     * 标签前缀
     */
    public static final String TAG_PREFIX = "ms_";
    /**
     * 生成的静态列表页面名;例如：list1.html
     */
    public static final String PAGE_LIST = "list-";
    /**
     * 模版文件后缀名;例如：index.html
     */
    public static final String HTM_SUFFIX = ".htm";

    /**
     * 是否是动态解析;true:动态、false：静态
     */
    public static final String IS_DO = "isDo";

    /**
     * 当前系统访问路径
     */
    public static final String URL = "url";

    /**
     * 栏目实体;
     */
    public static final String COLUMN = "column";

    /**
     * 文章编号
     */
    public static final String ID = "id";


    /**
     * 自定义模型表名;
     */
    public static final String TABLE_NAME = "tableName";

    /**
     * 模块路径;
     */
    public static final String MODEL_NAME = "modelName";

    /**
     * .do后缀
     */
    public static final String DO_SUFFIX = ".do";

    /**
     * 分页，提供給解析传递给sql解析使用
     */
    public static final String PAGE = "pageTag";

    /**
     * 当前页;
     */
    public static final String PAGE_NO = "pageNo";

    /**
     * 显示的条数;文章列表属性
     */
    public static final String SIZE = "size";

    /**
     * 栏目编号;原标签没有使用驼峰命名
     */
    public static final String TYPE_ID = "typeid";

    /**
     * 站点编号
     */
    public static final String APP_ID = "appId";

    /**
     * 单站点
     */
    public static boolean IS_SINGLE = true;

    public static Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);

    public static FileTemplateLoader ftl = null;
    public static StringTemplateLoader stringLoader;

    /**
     * 系统预设需要特殊条件的标签
     */
    public static List<String> systemTag=CollUtil.toList("field","pre","page","next");



    /**
     * 拼接模板文件路径
     *
     * @return
     */
    public static String buildTempletPath() {
        return ParserUtil.buildTempletPath(null);
    }

    /**
     * 拼接模板文件路径
     *
     * @return
     */
    public static String buildTempletPath(String path) {
        return BasicUtil.getRealTemplatePath(TEMPLATES) + File.separator + BasicUtil.getAppId() + File.separator
                + BasicUtil.getApp().getAppStyle() + (path != null ? (File.separator + path) : "");
    }


    /**
     * 拼接生成后的路径地址
     *
     * @return
     */
    public static String buildHtmlPath(String path) {
        return BasicUtil.getRealPath(HTML) + File.separator + BasicUtil.getAppId() + File.separator + path
                + HTML_SUFFIX;
    }



    /**
     * 根据模板路径，参数生成，主要提供给动态解析模板内容
     *
     * @param templatePath 模板路径
     * @param params       参数
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static String generate(String templatePath, Map params)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        //如果单站点，就废弃站点地址
        if (ParserUtil.IS_SINGLE) {
            params.put(URL, BasicUtil.getUrl());
        }
        //设置生成的路径
        params.put(HTML, HTML);
        //设置站点编号
        params.put(APP_ID, BasicUtil.getAppId());
        String read = read(templatePath, params);
        return read;
    }
    /**
     * 读取模板内容
     *
     * @param templatePath
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static String read(String templatePath, Map map)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        AttributeBean attributeBean  = new AttributeBean();
        return read(templatePath,map,null,attributeBean);
    }
    /**
     * 读取模板内容
     *
     * @param templatePath
     * @param isMobile
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static String read(String templatePath, boolean isMobile)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        String buildTempletPath = ParserUtil.buildTempletPath();
        if (ftl == null || !buildTempletPath.equals(ftl.baseDir.getPath())) {
            ftl = new FileTemplateLoader(new File(buildTempletPath));
            cfg.setNumberFormat("#");
            cfg.setTemplateLoader(ftl);
        }
        // 读取模板文件
        Template template = cfg.getTemplate((isMobile ? (BasicUtil.getApp().getAppMobileStyle() + File.separator) : "") + templatePath, Const.UTF8);
        // pc端内容
        StringWriter writer = new StringWriter();
        try {
            template.process(null, writer);
            return writer.toString();
        } catch (TemplateException e) {
            LOG.error("错误", e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 渲染模板
     *
     * @param templatePath 模板路径
     * @param map 传入参数
     * @param pageBean 分页bean
     * @param attributeBean 属性bean
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static String read(String templatePath, Map map, PageBean pageBean, AttributeBean attributeBean)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        String buildTempletPath = ParserUtil.buildTempletPath();
        if (ftl == null || !buildTempletPath.equals(ftl.baseDir.getPath())) {
            stringLoader = new StringTemplateLoader();
            ftl = new FileTemplateLoader(new File(buildTempletPath));
            MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[]{stringLoader,ftl});
            cfg.setNumberFormat("#");
            cfg.setTemplateLoader(multiTemplateLoader);
        }
        // 读取模板文件
        String temp =FileUtil.readString(FileUtil.file(buildTempletPath,templatePath),"utf-8");
        //替换标签
        temp = regReplace(temp);
        //添加自定义模板
        stringLoader.putTemplate("ms:custom:"+templatePath,temp);
        //获取自定义模板
        Template template = cfg.getTemplate("ms:custom:"+templatePath, Const.UTF8);
        //设置兼容模式
        cfg.setClassicCompatible(true);
        cfg.setSharedVariable(TAG_PREFIX+"includeEx", new IncludeExTag(buildTempletPath,stringLoader));

        ITagSqlBiz tagSqlBiz = SpringUtil.getBean(ITagSqlBiz.class);
        List<TagSqlBean> list = tagSqlBiz.queryAll();

        list.forEach(x -> {
            //添加自定义标签
            if (StrUtil.isNotBlank(x.getTagName())) {
                TagTypeEnum typeEnum = TagTypeEnum.get(x.getTagType());
                //列表标签
                if (typeEnum == TagTypeEnum.LIST) {
                    cfg.setSharedVariable(TAG_PREFIX+x.getTagName(), new CustomTag(map, x));
                }
                //分页标签
                if (typeEnum == TagTypeEnum.PAGE) {
                    cfg.setSharedVariable(TAG_PREFIX+x.getTagName(), new PageListTag(map, x,pageBean,attributeBean));
                }//其他内容标签
                else if(typeEnum==TagTypeEnum.SINGLE&&(!systemTag.contains(x.getTagName())
                        //文字内容需要id参数
                        ||(map.containsKey("id")&&x.getTagName().equals("field"))
                        //分页需要pageTag参数
                        ||(map.containsKey("pageTag")&&(x.getTagName().equals("pre")||x.getTagName().equals("next")||x.getTagName().equals("page")))
                )){
                    String sql = null;
                    try {
                        sql = rendering(map, x.getTagSql());
                        NamedParameterJdbcTemplate jdbc = SpringUtil.getBean(NamedParameterJdbcTemplate.class);
                       List list1 = jdbc.queryForList(sql, CollUtil.newHashMap());
                        map.put(x.getTagName(), list1.get(0));
                    } catch (IOException e) {

                    } catch (TemplateException e) {

                    }
                }
            }
        });

        // pc端内容
        StringWriter writer = new StringWriter();
        try {
            template.process(map, writer);
            return writer.toString();
        } catch (Exception e) {
            LOG.error("错误", e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 渲染模板方法
     *
     * @param root
     * @param content
     * @return
     */
    public static String rendering(Map root, String content) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", content);
        cfg.setNumberFormat("#");
        cfg.setTemplateLoader(stringLoader);
        Template template = cfg.getTemplate("template", "utf-8");
        StringWriter writer = new StringWriter();
        template.process(root, writer);
        return writer.toString();

    }

    /**
     * 旧标签正则替换处理
     * @param content
     * @return
     */
    public static String regReplace(String content){
        // 创建 Pattern 对象
        //替include标签
        content = content.replaceAll("<#include(.*)/>",  StrUtil.format( "<@{}includeEx template=$1/>",TAG_PREFIX));
        //两种情况<#include "head.htm" >与<#include "head.htm" /> 不会写正则 简单处理
        content = content.replaceAll("<#include(.*)>",  StrUtil.format( "<@{}includeEx template=$1/>",TAG_PREFIX));
        //替换全局标签{ms:global.name/}
        content = content.replaceAll("\\{ms:([^\\}]+)/\\}", "\\${$1}");
        //替换列表开头标签
        content = content.replaceAll("\\{ms:([^\\}]+)\\}", StrUtil.format( "<@{}$1>",TAG_PREFIX));
        //替换列表结束标签
        content = content.replaceAll("\\{/ms:([^\\}]+)\\}", StrUtil.format( "</@{}$1>",TAG_PREFIX));
        //替换内容标签 [field.*/]
        content = content.replaceAll("\\[([^\\]]+)/\\]", "\\${$1}");
        return content;
    }


}
