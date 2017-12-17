package cn.ahabox.interfaces;

import org.json.JSONObject;
import java.util.List;

/**
 * Created by libo on 2015/12/8.
 *
 * 接口回调抽象类
 */
public abstract class CallBackimpl implements IcallBack{

    @Override
    public void callBack(List datas) {

    }

    @Override
    public void callBack(List list, boolean charge) {

    }

    @Override
    public void callBack(String str) {

    }

    @Override
    public void callBack(byte[] bytes) {

    }

    @Override
    public void confirmHandle() {

    }

    @Override
    public void callBack(int progress) {

    }

    @Override
    public void callBack(float num) {

    }

    @Override
    public void callBack(int page,int subpage) {

    }

    @Override
    public void callBack(boolean boo) {

    }

    @Override
    public void callBack(JSONObject obj) {

    }

    @Override
    public void callBack(String... args) {

    }
}
