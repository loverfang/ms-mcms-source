package net.mingsoft.basic.biz;

import net.mingsoft.basic.entity.SystemConfigEntity;

public interface ISystemConfigBiz {
    /**
     * 保存配置信息
     */
    void saveConfig(SystemConfigEntity config);

    /**
     * 更新配置信息
     */
    void update(SystemConfigEntity config);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    void deleteBatch(Long[] ids);

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
