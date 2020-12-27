package net.mingsoft.basic.entity;

public class SystemConfigEntity extends BaseEntity{
    /**
     * 参数名,不能为空
     */
    private String paramKey;
    /**
     * 参数值,不能为空
     */
    private String paramValue;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
