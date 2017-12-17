package cn.ahabox.model;

/**
 * Created by libo on 2016/4/1.
 *
 * 物流信息实体类
 */
public class ShipEntity {

    /**
     * remark : [测试路由]快件到达 【北京国风服务点】
     * accept_time : 2015.08.18 02:55
     * accept_address : 北京国风服务点
     */
    private String remark;
    private String accept_time;
    private String accept_address;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public void setAccept_address(String accept_address) {
        this.accept_address = accept_address;
    }

    public String getRemark() {
        return remark;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public String getAccept_address() {
        return accept_address;
    }
}
