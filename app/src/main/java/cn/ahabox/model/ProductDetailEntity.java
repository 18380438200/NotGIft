package cn.ahabox.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by libo on 2016/3/2.
 *
 * 商品详情实体类
 */
public class ProductDetailEntity implements Serializable {

    /**
     * name : 【cup】宋洋限量版水杯这个水杯不错哟，赶紧买赶紧买
     * cover_pictures : ["http://demo.assets.img.feiliwu.com.cn/b250359814d8ce6c7aacc407489a49da.jpg?v=20160126115312","http://demo.assets.img.feiliwu.com.cn/e1dab391b6a8c6f1924f85e2d1b6f541.jpeg?v=20160126115312","http://demo.assets.img.feiliwu.com.cn/9df8edec47bf997ef9aedfdbeb4f4547.jpg?v=20160126115312"]
     * detail_pictures : ["http://demo.assets.img.feiliwu.com.cn/b250359814d8ce6c7aacc407489a49da.jpg?v=20160126115312","http://demo.assets.img.feiliwu.com.cn/e1dab391b6a8c6f1924f85e2d1b6f541.jpeg?v=20160126115312","http://demo.assets.img.feiliwu.com.cn/9df8edec47bf997ef9aedfdbeb4f4547.jpg?v=20160126115312"]
     * voice_url : http://demo.assets.msg.feiliwu.com.cn/05fda248e4ff29feae1162ade493a2ed.mp3?e=1456906385&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:9syKQWNOJXp_J2ozS4H7mLGve88=
     * video_url : http://demo.assets.msg.feiliwu.com.cn/e7599ab76465ae65425e1a4cc004d099.mp4?e=1456906385&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:7mOUyFDRnZV4Nop7C0cy4wfpT4M=
     * favorites : 110
     * price : 9.9
     * union_hash : [{"id":"self","nickname":"杯子"},{"id":3,"nickname":"茶具","first_cover_url":"http://demo.assets.img.feiliwu.com.cn/12dc065675c99d8e2587b0beb248db21.u=1610551270,4276602839?v=20150829121912?v=20150902193459?v=20150907094223?v=20150907094223?v=20160126115312"},{"id":5,"nickname":"戒指","first_cover_url":"http://demo.assets.img.feiliwu.com.cn/501fe972e2b8b7761c3a8e1e167ecb10.png?v=20160126115312"},{"id":8,"nickname":"教师节组合","first_cover_url":"http://demo.assets.img.feiliwu.com.cn/ad39a33534f65648a1c1f54c0b38b60d.png?v=20160126115312"}]
     * status : 0
     * special_service : ["http://demo.assets.wxsysicon.feiliwu.com.cn/system_products_show_bottom1.jpg?v=20160126115312","http://demo.assets.wxsysicon.feiliwu.com.cn/system_products_show_bottom2.jpg?v=20160126115312","http://demo.assets.wxsysicon.feiliwu.com.cn/system_products_show_bottom3.jpg?v=20160126115312","http://demo.assets.wxsysicon.feiliwu.com.cn/system_products_show_bottom4.jpg?v=20160126115312"]
     * child_category_id : 5
     * quantity : 10
     * summary : 非常精美的水杯
     * intro : [{"name":"退换货说明","name_en":" RETURN & EXCHANGE","summary":"下单成功后如无特别情况，我们不会再与您联系确认订单。\n订单一旦生成，如需取消或修改订单信息（地址，送达日期），请务必于预定收货日期的48小时之前提出。\n如有质量问题请在签收后24小时内联系客服。"},{"name":"运输说明","name_en":"DELIVERY","summary":"付款后24小时内发货，某些特殊商品付款后48小时内发货（双休日订单将在周一发货），香水、蜡烛、火柴等商品无法空运，运输时间无法预估，送达时间以物流实际配送为准，订购后请随时登录查询物流状态。 特别提醒：周末及法定节假日不发货。 \n"}]
     * is_presale : false
     * presale_start : null
     * presale_end : null
     * is_union : true
     * added_explain : (含礼物包装 顺丰速递)
     * img_suggest : []
     * nature : common
     * ship_time : 77
     * sale_mode : 销售模式，{"self_support"=> 自营, "replace_sale"=> 代销, "third_party_delivery"=> 第三方销售}
     * designer_thumbnail : http://demo.assets.img.feiliwu.com.cn/a34590d02f0d67a48427c5df6aeea377.jpg
     * designer_name : 宋洋美术·徐帆
     * designer_describe : 个人简介：
     1983年生于北京的职业艺术家。2005年毕业于清华大学美术学院油画系，师从陈丹青...
     */
    private String name;
    private String voice_url;
    private String video_url;
    private int favorites;
    private String price;
    private int status;
    private int child_category_id;
    private int quantity;
    private String summary;
    private boolean is_presale;
    private Timestamp presale_start;
    private Timestamp presale_end;
    private Timestamp time_now;
    private boolean is_favorited;
    private boolean is_union;
    private String added_explain;
    private String nature;
    private Object ship_time;
    private String designer_thumbnail;
    private String designer_name;
    private String designer_describe;
    private String sale_mode;
    private List<String> cover_pictures;
    private List<String> detail_pictures;
    /**
     * id : self
     * nickname : 杯子
     * first_cover_url :
     */
    private List<UnionHashEntity> union_hash;
    private List<String> special_service;
    /**
     * name : 退换货说明
     * name_en :  RETURN & EXCHANGE
     * summary : 下单成功后如无特别情况，我们不会再与您联系确认订单。
     订单一旦生成，如需取消或修改订单信息（地址，送达日期），请务必于预定收货日期的48小时之前提出。
     如有质量问题请在签收后24小时内联系客服。
     */

    private List<IntroEntity> intro;
    private List<?> img_suggest;

    public void setName(String name) {
        this.name = name;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setChild_category_id(int child_category_id) {
        this.child_category_id = child_category_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIs_presale(boolean is_presale) {
        this.is_presale = is_presale;
    }

    public void setPresale_start(Timestamp presale_start) {
        this.presale_start = presale_start;
    }

    public void setPresale_end(Timestamp presale_end) {
        this.presale_end = presale_end;
    }

    public boolean getIs_favorited() {
        return is_favorited;
    }

    public void setIs_favorited(boolean is_favorited) {
        this.is_favorited = is_favorited;
    }

    public void setIs_union(boolean is_union) {
        this.is_union = is_union;
    }

    public void setAdded_explain(String added_explain) {
        this.added_explain = added_explain;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setShip_time(Object ship_time) {
        this.ship_time = ship_time;
    }

    public void setDesigner_thumbnail(String designer_thumbnail) {
        this.designer_thumbnail = designer_thumbnail;
    }

    public void setDesigner_name(String designer_name) {
        this.designer_name = designer_name;
    }

    public void setDesigner_describe(String designer_describe) {
        this.designer_describe = designer_describe;
    }

    public void setCover_pictures(List<String> cover_pictures) {
        this.cover_pictures = cover_pictures;
    }

    public void setDetail_pictures(List<String> detail_pictures) {
        this.detail_pictures = detail_pictures;
    }

    public String getSale_mode() {
        return sale_mode;
    }

    public void setSale_mode(String sale_mode) {
        this.sale_mode = sale_mode;
    }

    public void setUnion_hash(List<UnionHashEntity> union_hash) {
        this.union_hash = union_hash;
    }

    public void setSpecial_service(List<String> special_service) {
        this.special_service = special_service;
    }

    public Timestamp getTime_now() {
        return time_now;
    }

    public void setTime_now(Timestamp time_now) {
        this.time_now = time_now;
    }

    public void setIntro(List<IntroEntity> intro) {
        this.intro = intro;
    }

    public void setImg_suggest(List<?> img_suggest) {
        this.img_suggest = img_suggest;
    }

    public String getName() {
        return name;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public int getFavorites() {
        return favorites;
    }

    public String getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public int getChild_category_id() {
        return child_category_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSummary() {
        return summary;
    }

    public boolean getIs_presale() {
        return is_presale;
    }

    public Timestamp getPresale_start() {
        return presale_start;
    }

    public Timestamp getPresale_end() {
        return presale_end;
    }

    public boolean getIs_union() {
        return is_union;
    }

    public String getAdded_explain() {
        return added_explain;
    }

    public String getNature() {
        return nature;
    }

    public Object getShip_time() {
        return ship_time;
    }

    public String getDesigner_thumbnail() {
        return designer_thumbnail;
    }

    public String getDesigner_name() {
        return designer_name;
    }

    public String getDesigner_describe() {
        return designer_describe;
    }

    public List<String> getCover_pictures() {
        return cover_pictures;
    }

    public List<String> getDetail_pictures() {
        return detail_pictures;
    }

    public List<UnionHashEntity> getUnion_hash() {
        return union_hash;
    }

    public List<String> getSpecial_service() {
        return special_service;
    }

    public List<IntroEntity> getIntro() {
        return intro;
    }

    public List<?> getImg_suggest() {
        return img_suggest;
    }

    public static class UnionHashEntity implements Serializable{
        private String id;
        private String nickname;
        private String first_cover_url;

        public String getFirst_cover_url() {
            return first_cover_url;
        }

        public void setFirst_cover_url(String first_cover_url) {
            this.first_cover_url = first_cover_url;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }
    }

    public static class IntroEntity implements Serializable{
        private String name;
        private String name_en;
        private String summary;

        public void setName(String name) {
            this.name = name;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getName() {
            return name;
        }

        public String getName_en() {
            return name_en;
        }

        public String getSummary() {
            return summary;
        }
    }
}
