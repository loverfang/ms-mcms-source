package net.mingsoft.basic.biz;

import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.basic.entity.SystemConfigEntity;

import java.util.List;
import java.util.Map;

public interface ISystemConfigBiz extends IBaseBiz {

    List<SystemConfigEntity> queryList(Map<String, Object> params);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 根据key，获取配置的value值
     * @param key           key
     */
    String getValue(String key);

    /**
     * 根据key，获取配置的SysConfig信息
     *
     * @param key           key
     */
    SystemConfigEntity getSysConfig(String key);

    /**
     * 根据key，获取value的Object对象
     * @param key    key
     * @param clazz  Object对象
     */
    <T> T getConfigObject(String key, Class<T> clazz);
}
