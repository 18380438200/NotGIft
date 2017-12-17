package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/6/17.
 *
 * 设计师关注实体类
 */
public class DesignerFocusEntity implements Serializable{

    /**
     * id : 1
     * name : 宋洋
     * summary : 被媒体誉为“难以定义的艺术家”除油画创作,并涉及漫画、设计、写作、影像、音乐等。
     * thumbnail_url : http://demo.assets.img.feiliwu.com.cn/d6aad7aa5f224e316c62338808c87e50.png
     */
    private int id;
    private String name;
    private String summary;
    private String thumbnail_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}
