package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/16.
 *
 * 曾经送过地址实体类
 */
public class OtherAddressEntity implements Serializable{

    /**
     * id : 16
     * name : 爱嘎
     * phone : 16511112222
     * display_str : 北京市,市辖区,石景山区;这个安静爱哈哈
     */
    private int id;
    private String name;
    private String postalcode;
    private String phone;
    private String display_str;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDisplay_str(String display_str) {
        this.display_str = display_str;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisplay_str() {
        return display_str;
    }
}
