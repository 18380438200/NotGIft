package cn.ahabox.interfaces;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by libo on 2015/12/8.
 *
 * 适配器模式的回调接口
 */
public interface IcallBack {

    void callBack(List list);

    void callBack(List list, boolean charge);

    void callBack(String str);

    void callBack(byte[] bytes);

    void callBack(JSONObject obj);

    void confirmHandle();

    void callBack(int progress);

    void callBack(float num);

    void callBack(boolean boo);

    /** 可变长int参数 */
    void callBack(int page,int subPage);

    /** 可变长字符串参数 */
    void callBack(String... args);
}
