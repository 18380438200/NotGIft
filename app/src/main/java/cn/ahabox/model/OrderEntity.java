package cn.ahabox.model;

/**
 * Created by libo on 2016/3/31.
 *
 * 订单实体类
 */
public class OrderEntity {

    /**
     * code : 1603314531633073
     * cover : http://demo.assets.img.feiliwu.com.cn/d46eef59dab57adfb761b82e3ca212ff.jpg?v=20160303114906
     * product_name : 商品笔记测试商品1
     * price : 0.02
     * pay_at : 1459400518
     */
    private String code;
    private String cover;
    private String product_name;
    private String price;
    private int pay_at;

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

    public void setPay_at(int pay_at) {
        this.pay_at = pay_at;
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

    public int getPay_at() {
        return pay_at;
    }
}
