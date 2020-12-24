package net.mingsoft.basic.biz.impl;

import com.alibaba.fastjson.JSON;
import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.basic.biz.ISystemConfigBiz;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

public class SystemConfigBizImpl extends BaseBizImpl implements ISystemConfigBiz {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String paramKey = (String) params.get("paramKey");

        IPage<SysConfigEntity> page = this.page(
                new Query<SysConfigEntity>().getPage(params),
                new QueryWrapper<SysConfigEntity>()
                        .like(StringUtils.isNotBlank(paramKey), "param_key", paramKey)
                        .eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Override
    public void saveConfig(SysConfigEntity config) {
        this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfigEntity config) {
        this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        baseMapper.updateValueByKey(key, value);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public String getValue(String key) {
        SysConfigEntity config = baseMapper.queryByKey(key);
        return config == null ? null : config.getParamValue();
    }

    @Override
    public SysConfigEntity getSysConfig(String key) {
        return baseMapper.queryByKey(key);
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);

        if (StringUtils.isNotBlank(value)) {
            value.replaceAll("\\\\","\\\\\\\\");
            return JSON.parseObject(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }
    }

}
