package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/16.
 *
 * 地址实体类
 */
public class AddressEntity implements Serializable{

    /**
     * id : 83
     * name : 李博
     * province : 110000
     * city : 010000
     * county : 050000
     * street : 锐创国际中心
     * postalcode : 000022
     * phone : 18201497620
     * display_str : ,,;锐创国际中心，用于给客户看的地址，代码地址是用与服务器交互的地址标志
     */
    private int id;
    private String name;
    private String province;
    private String city;
    private String county;
    private String street;
    private String postalcode;
    private String phone;
    private String display_str;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisplay_str() {
        return display_str;
    }
}
