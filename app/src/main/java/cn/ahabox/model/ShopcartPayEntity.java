package cn.ahabox.model;

/**
 * Created by libo on 2016/9/20.
 *
 * 购物车支付商品列表实体类
 */
public class ShopcartPayEntity {

    /**
     * product_name : 宋洋限量版水杯这个水杯不错哟，赶紧买赶紧买
     * product_price : 9.9
     * quantity : 2
     * cover_picture_display : http://demo.assets.img.feiliwu.com.cn/0e355afe7d803cdf65b87132accc40a6.png?v=20160920104959
     */
    private String product_name;
    private String product_price;
    private int quantity;
    private String cover_picture_display;

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCover_picture_display(String cover_picture_display) {
        this.cover_picture_display = cover_picture_display;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCover_picture_display() {
        return cover_picture_display;
    }
}
