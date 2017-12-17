package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/2/29.
 *
 * 礼物推荐实体类
 */
public class RecommendEntity implements Serializable {

    /**
     * type : 2
     * product_id : 1
     * cover_picture_url : http://7xj34m.com2.z0.glb.qiniucdn.com/2d71f33343ddebfe56558d6b1e74bfc2.png?v=20160121200334
     * content :
     * title : 阿拉啦度
     * message_url : http://7xj34o.com2.z0.glb.qiniucdn.com/4b42037171279223870243c9be038611.mp3?e=1456729472&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:_4twkeSsonqeSIXahwlzSLgW9No=
     */

    private int type;
    private String product_id;
    private String url;
    private String cover_picture_url;
    private String content;
    private String title;
    private String message_url;

    public void setType(int type) {
        this.type = type;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setCover_picture_url(String cover_picture_url) {
        this.cover_picture_url = cover_picture_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getProduct_id() {
        return product_id;
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
