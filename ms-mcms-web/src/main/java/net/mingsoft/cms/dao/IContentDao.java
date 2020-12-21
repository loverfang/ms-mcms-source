package net.mingsoft.cms.dao;

import net.mingsoft.base.dao.IBaseDao;
import java.util.*;

import net.mingsoft.cms.bean.CategoryBean;
import net.mingsoft.cms.bean.ContentBean;
import org.apache.ibatis.annotations.Param;

/**
 * 文章持久层
 * @author 铭飞开发团队
 * 创建日期：2019-11-28 15:12:32<br/>
 * 历史修订：<br/>
 */
public interface IContentDao extends IBaseDao {

    /**
     * 查询文章编号集合
     * @contentBean
     * @return
     */
    public List<CategoryBean> queryIdsByCategoryIdForParser(ContentBean contentBean);

    /**
     * 根据查询文章实体总数
     *
     * @param tableName
     *            :自定义生成的表名
     * @param map
     *            key:字段名 value:List 字段的各种判断值 list[0]:是否为自定义字段 list[1]:是否为整形
     *            list[2]:是否是等值查询 list[3]:字段的值
     * @return 文章实体总数
     */
    int getSearchCount(@Param("tableName") String tableName, @Param("diyList") List diyList,@Param("map") Map<String, Object> map,
                       @Param("websiteId") int websiteId, @Param("ids") String ids);

    /**
     * 分类编号删除文章
     * @param ids
     */
    void deleteEntityByCategoryIds(@Param("ids") int[] ids);
}