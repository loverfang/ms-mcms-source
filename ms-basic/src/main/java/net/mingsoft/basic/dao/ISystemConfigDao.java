package net.mingsoft.basic.dao;

import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.entity.SystemConfigEntity;

public interface ISystemConfigDao extends IBaseDao {

    void updateValueByKey(String key, String value);

    SystemConfigEntity queryByKey(String key);

}
