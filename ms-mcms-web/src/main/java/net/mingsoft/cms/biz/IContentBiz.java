package net.mingsoft.cms.biz;

import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.cms.bean.CategoryBean;
import net.mingsoft.cms.bean.ContentBean;
import net.mingsoft.mdiy.entity.ModelEntity;

import java.util.List;
import java.util.Map;


/**
 * 文章业务
 * @author 铭飞开发团队
 * 创建日期：2019-11-28 15:12:32<br/>
 * 历史修订：<br/>
 */
public interface IContentBiz extends IBaseBiz {

    /**
     * 根据文章属性查询
     * @param contentBean
     * @return
     */
    List<CategoryBean> queryIdsByCategoryIdForParser(ContentBean contentBean);



    int getSearchCount(ModelEntity contentModel, List diyList, Map whereMap, int appId, String categoryIds);
}