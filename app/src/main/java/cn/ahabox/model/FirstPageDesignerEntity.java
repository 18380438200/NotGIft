package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/2/29.
 *
 * 首页设计师实体类
 */
public class FirstPageDesignerEntity implements Serializable {

    /**
     * type : 1
     * url : http://192.168.1.203:3000/child_categories/6
     * cover_picture_url : http://7xj34m.com2.z0.glb.qiniucdn.com/8d8524ff4b37218e7591ba50fb3ed315.png?v=20160121200334
     * content :
     * title :
     * message_url : http://7xj34o.com2.z0.glb.qiniucdn.com/2806cadd56b83563926909801c5b1ab1.mp3?e=1456729473&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:Mg0muU4M5AYjLNYhZBzWxvqyGm8=
     */

    private int type;
    private String url;
    private String cover_picture_url;
    private String content;
    private String title;
    private String message_url;

    public void setType(int type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCover_picture_url(String cover_picture_url) {
        this.cover_picture_url = cover_picture_url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage_url(String message_url) {
        this.message_url = message_url;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getCover_picture_url() {
        return cover_picture_url;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage_url() {
        return message_url;
    }
}
