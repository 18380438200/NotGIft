package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/4/10.
 *
 * 创建订单返回结果实体类
 */
public class CreateOrderEntity implements Serializable{

    /**
     * code : 6293a334509a0e489f02bbc051803799
     * quantity : 3
     * can_use_promo_code : true
     * total_amount : 26580
     * product_price : 8860
     */
    private String code;
    private int quantity;
    private boolean can_use_promo_code;
    private int total_amount;
    private int product_price;
    private String cover_url;
    private String produce_name;
    private String realPrice;
    private String realTotalamount;

    public void setCode(String code) {
        this.code = code;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCan_use_promo_code(boolean can_use_promo_code) {
        this.can_use_promo_code = can_use_promo_code;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getRealTotalamount() {
        return realTotalamount;
    }

    public void setRealTotalamount(String realTotalamount) {
        this.realTotalamount = realTotalamount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getCode() {
        return code;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isCan_use_promo_code() {
        return can_use_promo_code;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public int getProduct_price() {
        return product_price;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getProduce_name() {
        return produce_name;
    }

    public void setProduce_name(String produce_name) {
        this.produce_name = produce_name;
    }
}
