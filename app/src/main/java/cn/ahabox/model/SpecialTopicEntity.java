package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/1.
 *
 * 专题实体类
 */
public class SpecialTopicEntity implements Serializable {

    /**
     * id : 40
     * show_picture : http://demo.assets.img.feiliwu.com.cn/ab03d60e7061001734cce368fd9c39c5.png
     * show_describe : {"title":"aaa","content":"bbbb"}
     * voice_url : http://demo.assets.msg.feiliwu.com.cn/e9fbb7ad14c9ee1b35d820ef1571e0e3.mp3?e=1457360695&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:OhO3tJ02lszIMprHHjLG0exLRF8=
     * url : http://demo.feiliwu.com.cn/topic_pages/28fbd0
     */

    private int id;
    private String show_picture;
    /**
     * title : aaa
     * content : bbbb
     */

    private ShowDescribeEntity show_describe;
    private String voice_url;
    private String url;

    public void setId(int id) {
        this.id = id;
    }

    public void setShow_picture(String show_picture) {
        this.show_picture = show_picture;
    }

    public void setShow_describe(ShowDescribeEntity show_describe) {
        this.show_describe = show_describe;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getShow_picture() {
        return show_picture;
    }

    public ShowDescribeEntity getShow_describe() {
        return show_describe;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public String getUrl() {
        return url;
    }

    public static class ShowDescribeEntity {
        private String title;
        private String content;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}
