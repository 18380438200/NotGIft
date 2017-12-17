package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/1.
 *
 * 品牌汇实体类
 */
public class BrandEntity implements Serializable{

    /**
     * id : 1
     * name : 宋洋美术·徐帆
     * thumbnail_url : http://demo.assets.img.feiliwu.com.cn/a34590d02f0d67a48427c5df6aeea377.jpg
     * introduce : asdasdwdqewe
     */
    private int id;
    private String name;
    private String thumbnail_url;
    private String introduce;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getIntroduce() {
        return introduce;
    }
}
