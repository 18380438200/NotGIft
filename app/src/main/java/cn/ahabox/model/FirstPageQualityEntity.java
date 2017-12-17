package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/2/29.
 *
 * 首页精品推荐
 */
public class FirstPageQualityEntity implements Serializable {

    /**
     * type : 2
     * product_id : 1
     * cover_picture_url : http://7xj34m.com2.z0.glb.qiniucdn.com/a85c61ff000177e620883530f40e8594.png?v=20160121200334
     * content : 手工台湾原创黑陶杯／中国台湾hmm
     * title : 最高级别的迷你上的玩艺术品 ￥650
     * message_url : http://7xj34o.com2.z0.glb.qiniucdn.com/4b42037171279223870243c9be038611.mp3?e=1456729473&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:qIx80MU6P00f6BVLhvtVXqwtFs0=
     */

    private int type;
    private String product_id;
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
