package cn.ahabox.interfaces;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by libo on 2015/11/23.
 *
 * 接口层
 */
public interface Api {

    /** 服务器地址 */
    String SERVER_ADDRESS = "http://demo.feiliwu.com.cn/app_api/";

    /** 版本号V1 */
    String VERSION_CODE_V1 = "v1/";

    /** 软件最新版本号（测试地址） */
    String VERSIONCODE = SERVER_ADDRESS + VERSION_CODE_V1 + "app_versions/last";
    void parseVersion();

    /** 首页 */
    String FIRSTPAGE = SERVER_ADDRESS + VERSION_CODE_V1 + "products/homepage";
    /** 首页解析方法 */
    void parseFirstPage();

    /** 设计师、艺术家、心享物、定制 */
    String SUBPAGE = SERVER_ADDRESS + VERSION_CODE_V1 + "products/category?category_id=%d&page=%d";
    /** 设计师、艺术家、心享物、定制解析方法 */
    void parseSubPage(int id, int page);

    /** 专题 */
    String SPECIALTOPIC = SERVER_ADDRESS + VERSION_CODE_V1 + "products/topic_pages";
    /** 专题解析方法 */
    void parseTopic();

    /** 品牌汇 */
    String BRAND = SERVER_ADDRESS + VERSION_CODE_V1 + "child_categories";
    /** 品牌汇解析方法 */
    void parseBrand();

    /** 设计师、艺术家详情 */
    String DESIGNER_DETAIL = SERVER_ADDRESS + VERSION_CODE_V1 + "child_categories/%d";
    /** 设计师、艺术家详情解析方法 */
    void parseDesignerDetail(int id);

    /** 关注设计师 */
    String FOCUS_ADD = SERVER_ADDRESS + VERSION_CODE_V1 + "follows/add";

    /** 取消设计师关注 */
    String FOCUS_REMOVE = SERVER_ADDRESS + VERSION_CODE_V1 + "follows/remove";

    /** 设计师关注列表 */
    String DESIGNER_FOCUS = SERVER_ADDRESS + VERSION_CODE_V1 + "follows";
    /** 设计师关注列表解析 */
    void parseDesignerFocus();

    /** 商品详情 */
    String PRODUCT_DETAIL = SERVER_ADDRESS + VERSION_CODE_V1 + "products/%d";
    /** 商品详情解析方法 */
    void parseProductDetail(int id);

    /** 商品搜索 */
    String SEARCH = SERVER_ADDRESS + VERSION_CODE_V1 + "products/search?q=%s&page=%d";
    /** 商品搜索解析 */
    void parseSearch(String keyWords, int page);

    /** 快速选择商品类别标签 */
    String FASTCHOOSETAG = SERVER_ADDRESS + VERSION_CODE_V1 + "tags";
    /** 快速选择商品类别标签解析 */
    void parseProductTag();

    /** 通过商品类别id获取商品类别 */
    String TAGPRODUCTS = SERVER_ADDRESS + VERSION_CODE_V1 + "tags/%d?page=%d";
    /** 根据商品类别获取该类商品列表 */
    void parseTagProducts(int productTag, int page);

    /** 关键字热搜 */
    String KEYWORDS = SERVER_ADDRESS + VERSION_CODE_V1 + "products/keywords";
    void parseKeyWords();

    /** 获取验证码 */
    String CODES = SERVER_ADDRESS + VERSION_CODE_V1 + "users/send_verify_code";
    /** 获取验证码 */
    void parseCodes(Map<String, String> params);

    /** 验证验证码获取注册token */
    String VERIFICATION_CODES = SERVER_ADDRESS + VERSION_CODE_V1 + "users/mobile_verify";
    /** 验证验证码获取注册token */
    void parseTestCodes(Map<String, String> params);

    /** 注册提交密码和微信登录code */
    String POST_REGISTER = SERVER_ADDRESS + VERSION_CODE_V1 + "users/register";
    void parsePostRegister(HashMap params, HashMap headers);

    /** 修改密码 */
    String MODIFI_PASSWORD = SERVER_ADDRESS + VERSION_CODE_V1 + "users/modify_password";
    void parseModifyPassword(Map<String, String> params, Map<String, String> headers);

    /** 登录 */
    String LOGIN = SERVER_ADDRESS + VERSION_CODE_V1 + "users/login";
    /** 登录 */
    void parseLogin(Map<String, String> params);

    /** 微信登录 */
    String WXLOGIN = SERVER_ADDRESS + VERSION_CODE_V1 + "users/wx_login";
    void parseWxLogin(HashMap<String, String> params);

    /** 添加地址 */
    String ADD_ADDRESS = SERVER_ADDRESS + VERSION_CODE_V1 + "addresses";
    void parseAddAdd(HashMap<String, String> params);

    /** 修改地址 */
    String EDIT_ADDRESS = SERVER_ADDRESS + VERSION_CODE_V1 + "addresses/%d/modify";
    void parseEditAdd(HashMap<String, String> params, int id);

    /** 自填地址列表 */
    String MYADDRESS = SERVER_ADDRESS + VERSION_CODE_V1 + "addresses/my_addresses";
    void parseMyAddress();

    /** 曾经送过列表 */
    String ONCESEND = SERVER_ADDRESS + VERSION_CODE_V1 + "addresses/friend_addresses";
    void parseOnceSend();

    /** 删除收礼人地址 */
    String DEL_ADDRESS = SERVER_ADDRESS + VERSION_CODE_V1 + "addresses/%d/delete";
    void parseDelAddress(int id);

    /** 礼物收藏列表 */
    String COLLECTION_LIST = SERVER_ADDRESS + VERSION_CODE_V1 + "favorites";
    void parseCollList();

    /** 收藏商品 */
    String COLLECTION = SERVER_ADDRESS + VERSION_CODE_V1 + "favorites/add";

    /** 取消收藏礼物 */
    String REMOVE_COLL = SERVER_ADDRESS + VERSION_CODE_V1 + "favorites/remove";

    /** 获取语音上传 */
    String UPLOAD_TOKEN = SERVER_ADDRESS + VERSION_CODE_V1 + "attachments/voice_upload_token";
    void parseUploadToken();

    /** 语音列表获取 */
    String VOICE_LIST = SERVER_ADDRESS + VERSION_CODE_V1 + "messages";
    void parseVoiceList();

    /** 语音删除 */
    String VOICE_DELETE = SERVER_ADDRESS + VERSION_CODE_V1 + "messages/%d/remove";
    void parseVoiceDel(int voiceId);

    /** 语音改名 */
    String VOICE_RENAME = SERVER_ADDRESS + VERSION_CODE_V1 + "messages/%d/update_name";
    void parseVoiceRename(int voiceId, HashMap<String, String> params);

    /** 提交订单 */
    String PUT_ORDER = SERVER_ADDRESS + VERSION_CODE_V1 + "orders";

    /** 验证优惠码 */
    String PROMO_CODE = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/validate_promo_code?code=%s";
    void parsePromoCode(String id, String promocode);

    /** 生成预支付信息 */
    String PREPAY = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/pre_pay";

    /** 查询支付是否成功 */
    String PAY_SUCCESS = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/pay_success";
    void parsePaySuccess(String code);

    /** 确认送出礼物 */
    String SEND_OUT = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/send_out";

    /** 意见反馈 */
    String FEED_BACK = SERVER_ADDRESS + VERSION_CODE_V1 + "users/feedback";
    void parseFeedBack(HashMap params);

    /** 成交订单 */
    String TRANSAVTION_ORDER = SERVER_ADDRESS + VERSION_CODE_V1 + "orders";
    void parseOrders();

    /** 订单详情 */
    String ORDER_DETAIL = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s";
    void parseOrderDetail(String orderCode);

    /** 删除订单 */
    String ORDER_DEL = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/delete";
    void parseOrderDel(String orderId);

    /** 物流状态 */
    String SHIP_INFO = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/shipping_info";
    void parseShip(String code);

    /** 申请退款 */
    String REQUEST_REFUND = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/%s/request_refund";
    void parseRefund(String code, HashMap<String, String> params);

    /** 未送出礼物 */
    String WAIT_SEND = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/wait_send_list";
    void parseWaitSend();

    /** Ta未接收的礼物 */
    String WAIT_ACCEPT = SERVER_ADDRESS + VERSION_CODE_V1 + "orders/wait_accept_list";
    void parseWaitAccept();

    /** 评论列表 */
    String COMMENTS = SERVER_ADDRESS + VERSION_CODE_V1 + "comments?class_name=%s&id=%d&page=%d";
    void parseCommentsList(String className,int id,int page);

    /** 评论/回复 */
    String MAKE_COMMENTS = SERVER_ADDRESS + VERSION_CODE_V1 + "comments";

    /** 点赞 */
    String LIKE = SERVER_ADDRESS + VERSION_CODE_V1 + "comments/%d/like";
    void parseLike(int commentId);

    /** 取消点赞 */
    String DISLIKE = SERVER_ADDRESS + VERSION_CODE_V1 + "comments/%d/dislike";
    void parseDislike(int commentId);

    /** 评论删除 */
    String COMMENT_DEL = SERVER_ADDRESS + VERSION_CODE_V1 + "comments/%d/delete";
    void parseCommentDel(int commentId);

    /** 购物车列表 */
    String SHOP_CART = SERVER_ADDRESS + VERSION_CODE_V1 + "shop_carts";
    void parseShopcart();

    /** 购物车删除 */
    String SHOPCART_DEL = SERVER_ADDRESS + VERSION_CODE_V1 + "shop_carts/%d/delete";
    void parseShopcartDel(int productId);

    /** 购物车结算 */
    String SHOPCART_BILLING = SERVER_ADDRESS + VERSION_CODE_V1 + "shop_carts/confirm";

    /** 购物车支付 */
    String SHOPCART_PAY = SERVER_ADDRESS + VERSION_CODE_V1 + "shop_carts/pay?code=%s";
    void parseShopcartPay(String code);
}
