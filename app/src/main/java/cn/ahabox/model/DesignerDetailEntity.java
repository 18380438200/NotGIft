package cn.ahabox.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by libo on 2016/3/1.
 *
 * 设计师详情类实体类
 */
public class DesignerDetailEntity implements Serializable{

    /**
     * title : 宋洋
     * products : [{"id":6,"cover":"http://demo.assets.img.feiliwu.com.cn/33c465583a9c9036c42c4c8603a752e2.png?v=20160126115312","status":0,"name":"精品戒指8号新","price":"0.01","favorites":181},{"id":22,"cover":"http://demo.assets.img.feiliwu.com.cn/016a5b3fcae98d85f15d518daa5d3833.jpg?v=20160126115312","status":0,"name":"acs水杯","price":"1","favorites":65},{"id":17,"cover":"http://demo.assets.img.feiliwu.com.cn/904289b41c3c15f42f3ad9a589ab50b8.jpg?v=20160126115312","status":0,"name":"dfssdf","price":"7","favorites":189},{"id":14,"cover":"http://demo.assets.img.feiliwu.com.cn/a86b8cfff612ab457cb823fa1403ae0d.jpg?v=20160126115312","status":0,"name":"艺术家商品","price":"10","favorites":103},{"id":12,"cover":"http://demo.assets.img.feiliwu.com.cn/e3d583c43e361a15d05a30e39066ea38.jpg?v=20160126115312","status":0,"name":"0元测试","price":"0","favorites":141}]
     * cover : http://demo.assets.img.feiliwu.com.cn/044085e7e265009db62a8426db6dac89.jpg
     * name : 宋洋美术·徐帆
     * voice_url : http://demo.assets.msg.feiliwu.com.cn/7330314cf2c7dfcca2d5cb42ba29f47f.mp3?e=1456829150&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:BypX0ieV9pVExkBAvSYC-hYR2qE=
     * describe : 个人简介：
     1983年生于北京的职业艺术家。2005年毕业于清华大学美术学院油画系，师从陈丹青、石冲，忻东旺先生。参与2015中英文化交流年“小羊肖恩”的创作。

     创作背景：
     以“寓言”形式来表现人性，试图以“情节”让绘画中呈现一种时间的流动性，以一种泛白的、简化的、充满实验气息的色彩，表现出一种复杂的情绪：时空错乱、荒诞，却又真实。

     艺术风格：
     徐帆更是一个艺术家，而非市场明星。他的沉默让喜欢热闹的人觉得索然无味，与许多偏于思想的“当代”艺术家不同，徐帆迷恋绘画、热爱绘画，他有着扎实的造型写实能力，并且不局限于油画语言。为求语言上的突破，他潜心研究过国画、版画、坦培拉技法。他坚持以绘画的方式介入现实社会，同时淋漓尽致的展现他的绘画语言。
     * thumbnail_url : http://demo.assets.msg.feiliwu.com.cn/86972e9444fa04867c8be6f29dac7c27.mp4?vframe/jpg/offset/4/w/480/h/360&e=1456829150&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:AASPDWgecXZZPq8bJkGIXxKnd2U=
     * video_url : http://demo.assets.msg.feiliwu.com.cn/86972e9444fa04867c8be6f29dac7c27.mp4?e=1456829150&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:abQAALba-plC8S8CRjz8vO1RedE=
     * images : [{"url":"","src":"http://demo.assets.img.feiliwu.com.cn/d4a1bf0a9add70157e398d6038880813.jpg"},{"url":"http://www.feiliwu.com.cn/products/10","src":"http://demo.assets.img.feiliwu.com.cn/d957b548b685b26608bf8ccdfbf1e633.jpg"}]
     */

    private String title;
    private String cover;
    private String name;
    private String voice_url;
    private String describe;
    private String thumbnail_url;
    private String video_url;
    private boolean is_follow;
    /**
     * id : 6
     * cover : http://demo.assets.img.feiliwu.com.cn/33c465583a9c9036c42c4c8603a752e2.png?v=20160126115312
     * status : 0
     * name : 精品戒指8号新
     * price : 0.01
     * favorites : 181
     */
    private List<ProductsEntity> products;
    /**
     * url :
     * src : http://demo.assets.img.feiliwu.com.cn/d4a1bf0a9add70157e398d6038880813.jpg
     */

    private List<ImagesEntity> images;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setProducts(List<ProductsEntity> products) {
        this.products = products;
    }

    public void setImages(List<ImagesEntity> images) {
        this.images = images;
    }

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getName() {
        return name;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public String getDescribe() {
        return describe;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public List<ProductsEntity> getProducts() {
        return products;
    }

    public List<ImagesEntity> getImages() {
        return images;
    }

    public static class ProductsEntity implements Serializable{
        private int id;
        private String cover;
        private int status;
        private String name;
        private String price;
        private int favorites;
        private boolean is_favorited;

        public void setId(int id) {
            this.id = id;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setFavorites(int favorites) {
            this.favorites = favorites;
        }

        public int getId() {
            return id;
        }

        public String getCover() {
            return cover;
        }

        public int getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public int getFavorites() {
            return favorites;
        }

        public boolean getIs_favorited() {
            return is_favorited;
        }

        public void setIs_favorited(boolean is_favorited) {
            this.is_favorited = is_favorited;
        }

    }

    public static class ImagesEntity {
        private String url;
        private String src;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getUrl() {
            return url;
        }

        public String getSrc() {
            return src;
        }
    }
}
