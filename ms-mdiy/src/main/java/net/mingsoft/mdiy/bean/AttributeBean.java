package net.mingsoft.mdiy.bean;


/**
 * 列表标签对应属性类
 * @author 铭飞开源团队
 * @date 2020年9月1日
 */
public class AttributeBean {

    /**
     * 类型flag
     */
    private String flag;

    /**
     * 类型noflag
     */
    private String noflag;

    /**
     * 排序字段
     */
    private String orderby;

    /**
     * 排序类型
     */
    private String order;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNoflag() {
        return noflag;
    }

    public void setNoflag(String noflag) {
        this.noflag = noflag;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
