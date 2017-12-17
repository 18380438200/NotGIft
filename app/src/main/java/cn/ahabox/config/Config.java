package cn.ahabox.config;

import android.os.Environment;

/**
 * Created by libo on 2015/11/24.
 *
 * 应用配置文件
 */
public class Config {

    /** 微信平台appId */
    public static final String APP_ID = "wx80c95ce838ce3ff7";

    /** 非礼物文件保存根目录 */
    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/feiliwu/";

    /** 共享参数存储文件名 */
    public static final String SHAREFRE_FILENAME = "firstInspf";

    /** 数据提交返回成功的标志 */
    public static final int STATUS_SUCCESSED = 0;

    /** 设计师详情传递参数 */
    public static final String DESIGNER_ID_KEY = "designerId";

    /** 商品详情传递参数 */
    public static final String PRODUCT_ID_KEY = "productId";

    /** 商品搜索关键字传递参数 */
    public static final String SEARCH_KEY = "searchId";

    /** webview加载url */
    public static final String WEBVIEW_URL = "webviewUrl";

    /** 收礼人地址跳转方向参数 */
    public static final String RECIPENTINFO_INTENT = "infointent";

    /** 注册类型 */
    public static final String REGISTER_TYPE = "registerType";

    /** 登录Token */
    public static final String SAVE_TOKEN = "saveToken";

    /** UserId */
    public static final String SAVE_UID = "saveUid";

    /** 用户名 */
    public static final String SAVE_USERNAME = "userName";

    /** 头像Url */
    public static final String SAVE_AVATER = "userAvater";

    /** 购买选择地址或管理地址 */
    public static final String ADDRESS_PARAMS = "address";

    /** 验证码验证类型 */
    public static final int TYPE_REGISTER = 0;

    public static final int TYPE_MODIFY = 1;

}
