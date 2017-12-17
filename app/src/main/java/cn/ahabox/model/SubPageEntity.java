package cn.ahabox.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by libo on 2016/2/29.
 *
 * 设计师、艺术家、心享物、定制实体类
 */
public class SubPageEntity implements Serializable {

    /**
     * id : 2
     * name : 孙荷芳
     * products : [{"id":14,"cover":"http://7xj34m.com2.z0.glb.qiniucdn.com/2df935e056fb134a0fd6903ba0f8b787.jpg?v=20160121200334"},{"id":59,"cover":"http://7xj34m.com2.z0.glb.qiniucdn.com/0113435c7da1f1533e34c110aae10210.jpg?v=20160121200334"},{"id":65,"cover":"http://7xj34m.com2.z0.glb.qiniucdn.com/72e1e0e144a5f39ed132f8caf89ad834.jpg?v=20160121200334"}]
     * voice_url : http://7xj34o.com2.z0.glb.qiniucdn.com/4b42037171279223870243c9be038611.mp3?e=1456749425&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:G1ZEwUqAqToCpZJbbm3I9Ue4_f0=
     * cover : http://7xj34m.com2.z0.glb.qiniucdn.com/f1ce751b51505e4675c70240c88ec545.jpg
     * describe : 孙荷芳
     孙荷芳


     孙荷芳孙荷芳孙荷芳孙荷芳孙荷芳荷芳孙荷芳荷芳孙荷芳, 孙荷芳孙荷芳孙荷芳孙...
     */

    private int id;
    private String name;
    private String voice_url;
    private String cover;
    private String describe;
    /**
     * id : 14
     * cover : http://7xj34m.com2.z0.glb.qiniucdn.com/2df935e056fb134a0fd6903ba0f8b787.jpg?v=20160121200334
     */

    private List<ProductsEntity> products;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setProducts(List<ProductsEntity> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public String getCover() {
        return cover;
    }

    public String getDescribe() {
        return describe;
    }

    public List<ProductsEntity> getProducts() {
        return products;
    }

    public static class ProductsEntity {
        private int id;
        private String cover;

        public void setId(int id) {
            this.id = id;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public String getCover() {
            return cover;
        }
    }
}
