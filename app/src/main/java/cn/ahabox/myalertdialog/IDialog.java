package cn.ahabox.myalertdialog;

import cn.ahabox.interfaces.CallBackimpl;

/**
 * Created by libo on 2015/12/7.
 *
 * 自定义弹出框实现效果接口
 */
public interface IDialog {

    void progressDialog();

    void tipDialog(String title);

    void contentDialog(String strtitle, String content);

    void contentDialog(String strtitle, String content, String strnegetive, String strpositive, CallBackimpl callback);

    void errorDialog(String title);

    void successDialog(String title);

    void warnConfirmDialog(String title, String compliteTitle);

    void confirmCancelDialog(String strtitle, String strnegetive, String strpositive, CallBackimpl handle);

    void imageDialog(String title, int ImageId);

}
