package net.mingsoft.basic.biz.impl;

import com.alibaba.fastjson.JSON;
import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.biz.ISystemConfigBiz;
import net.mingsoft.basic.dao.ISystemConfigDao;
import net.mingsoft.basic.entity.SystemConfigEntity;
import net.mingsoft.basic.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service("systemConfigBiz")
@Transactional
public class SystemConfigBizImpl extends BaseBizImpl implements ISystemConfigBiz {

    @Autowired
    private ISystemConfigDao systemConfigDao;

    @Override
    protected IBaseDao getDao() {
        return systemConfigDao;
    }

    @Override
    public List<SystemConfigEntity> queryList(Map<String, Object> params) {
        String paramKey = (String) params.get("paramKey");
        return systemConfigDao.query(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        systemConfigDao.updateValueByKey(key, value);
    }

    @Override
    public String getValue(String key) {
        SystemConfigEntity config = systemConfigDao.queryByKey(key);
        return config == null ? null : config.getParamValue();
    }

    @Override
    public SystemConfigEntity getSysConfig(String key) {
        return systemConfigDao.queryByKey(key);
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
            throw new BusinessException("获取参数失败");
        }
    }

}
