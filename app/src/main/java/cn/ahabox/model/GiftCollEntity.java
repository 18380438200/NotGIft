package cn.ahabox.model;

/**
 * Created by libo on 2016/3/16.
 *
 * 收藏礼物实体类
 */
public class GiftCollEntity {

    /**
     * cover : http://demo.assets.img.feiliwu.com.cn/0e355afe7d803cdf65b87132accc40a6.png?v=20160303114906
     * status : 0
     * price : 9.9
     * id : 1
     * name : 宋洋限量版水杯这个水杯不错哟，赶紧买赶紧买
     */
    private String cover;
    private int status;
    private String price;
    private int id;
    private String name;

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public int getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
