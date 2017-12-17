package cn.ahabox.model;

/**
 * Created by libo on 2016/4/5.
 *
 * 未送出礼物实体类
 */
public class WaitSendEntity {

    /**
     * code : 1604054574271514
     * cover : http://demo.assets.img.feiliwu.com.cn/1aeb3ad652ea7edfdd4acf6031e0b664.jpg?v=20160303114906
     * product_name : shuibei3
     * price : 0.01
     * on_sale : false
     * pay_at : 1459841932
     * refund_limit_days : 7
     */
    private String code;
    private String cover;
    private String product_name;
    private String price;
    private boolean on_sale;
    private int pay_at;
    private int refund_limit_days;

    public void setCode(String code) {
        this.code = code;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public void setPay_at(int pay_at) {
        this.pay_at = pay_at;
    }

    public void setRefund_limit_days(int refund_limit_days) {
        this.refund_limit_days = refund_limit_days;
    }

    public String getCode() {
        return code;
    }

    public String getCover() {
        return cover;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getPrice() {
        return price;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public int getPay_at() {
        return pay_at;
    }

    public int getRefund_limit_days() {
        return refund_limit_days;
    }
}
