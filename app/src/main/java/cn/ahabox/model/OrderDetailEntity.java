package cn.ahabox.model;

/**
 * Created by libo on 2016/3/31.
 *
 * 订单详情实体类
 */
public class OrderDetailEntity {

    /**
     * code : 1603313217174637
     * product_id : 30
     * status : wait_ship
     * address : 四川省,成都市,金牛区;好尴尬
     * phone : 18201497620
     * user_name : 李博
     * product_name : shuibei3
     * postalcode : 610036
     * address_input_type : know_address
     * promo_price : null
     * cover : http://demo.assets.img.feiliwu.com.cn/1aeb3ad652ea7edfdd4acf6031e0b664.jpg?v=20160303114906
     * price : 0.01
     * created_at : 1459410360
     * pay_at : 1459410370
     * send_link_at : 0
     * confirm_at : 1459410471
     * shipped_at : 0
     * signed_at : 0
     * promo_code : null
     * voice_url : http://demo.assets.voice.tar.feiliwu.com.cn/2c6bf7aff861d809495d876b65f89638.mp3?e=1459416425&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:29Y_zR2m94-e5uuLKp9TkbQ3774=
     * sharelink_title : 帅哥 为您挑选了一份礼物，点击领取
     * sharelink_content : 赶快接收我送的礼物，填完地址就等着收快递吧！
     * on_sale : false
     * refund_status : null
     * nature:common
     */
    private String code;
    private int product_id;
    private String status;
    private String address;
    private String phone;
    private String user_name;
    private int message_id;
    private String product_name;
    private String postalcode;
    private String address_input_type;
    private Object promo_price;
    private String cover;
    private String price;
    private int created_at;
    private int pay_at;
    private int send_link_at;
    private int confirm_at;
    private int shipped_at;
    private int signed_at;
    private Object promo_code;
    private String voice_url;
    private String sharelink_title;
    private String sharelink_content;
    private boolean on_sale;
    private String refund_status;
    private String nature;

    public void setCode(String code) {
        this.code = code;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public void setAddress_input_type(String address_input_type) {
        this.address_input_type = address_input_type;
    }

    public void setPromo_price(Object promo_price) {
        this.promo_price = promo_price;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public void setPay_at(int pay_at) {
        this.pay_at = pay_at;
    }

    public void setSend_link_at(int send_link_at) {
        this.send_link_at = send_link_at;
    }

    public void setConfirm_at(int confirm_at) {
        this.confirm_at = confirm_at;
    }

    public void setShipped_at(int shipped_at) {
        this.shipped_at = shipped_at;
    }

    public void setSigned_at(int signed_at) {
        this.signed_at = signed_at;
    }

    public void setPromo_code(Object promo_code) {
        this.promo_code = promo_code;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public void setSharelink_title(String sharelink_title) {
        this.sharelink_title = sharelink_title;
    }

    public void setSharelink_content(String sharelink_content) {
        this.sharelink_content = sharelink_content;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCode() {
        return code;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getAddress_input_type() {
        return address_input_type;
    }

    public Object getPromo_price() {
        return promo_price;
    }

    public String getCover() {
        return cover;
    }

    public String getPrice() {
        return price;
    }

    public int getCreated_at() {
        return created_at;
    }

    public int getPay_at() {
        return pay_at;
    }

    public int getSend_link_at() {
        return send_link_at;
    }

    public int getConfirm_at() {
        return confirm_at;
    }

    public int getShipped_at() {
        return shipped_at;
    }

    public int getSigned_at() {
        return signed_at;
    }

    public Object getPromo_code() {
        return promo_code;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public String getSharelink_title() {
        return sharelink_title;
    }

    public String getSharelink_content() {
        return sharelink_content;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public String getRefund_status() {
        return refund_status;
    }
}
