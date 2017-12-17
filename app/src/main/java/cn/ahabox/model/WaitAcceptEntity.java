package cn.ahabox.model;

/**
 * Created by libo on 2016/4/5.
 *
 * Ta未接收的礼物实体类
 */
public class WaitAcceptEntity {

    /**
     * code : 1604058056537687
     * cover : http://demo.assets.img.feiliwu.com.cn/b58002dbb8513a92faf14ab571af0e5b.jpg?v=20160303114906
     * product_name : 水杯2
     * price : 0.01
     * send_link_at : 1459838756
     * expire_days : 2
     */
    private String code;
    private String cover;
    private String product_name;
    private String price;
    private int send_link_at;
    private int expire_days;

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

    public void setSend_link_at(int send_link_at) {
        this.send_link_at = send_link_at;
    }

    public void setExpire_days(int expire_days) {
        this.expire_days = expire_days;
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

    public int getSend_link_at() {
        return send_link_at;
    }

    public int getExpire_days() {
        return expire_days;
    }
}
