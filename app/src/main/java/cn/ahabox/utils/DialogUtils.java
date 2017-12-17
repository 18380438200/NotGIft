package cn.ahabox.utils;

import android.content.Context;
import android.os.CountDownTimer;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.myalertdialog.IDialog;
import cn.ahabox.myalertdialog.SweetAlertDialog;

/**
 * Created by libo on 2015/12/7.
 *
 * 自定义弹出框工具类
 */
public class DialogUtils implements IDialog {
    private Context context;
    private SweetAlertDialog progressDialog;

    public DialogUtils(Context context){
        this.context = context;
    }

    /**
     * 进度弹出框
     */
    @Override
    public void progressDialog(){
        final int[] i = {-1};
        progressDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在加载...");
        progressDialog.show();
        new CountDownTimer(800 * 7, 800) {
            @Override
            public void onTick(long millisUntilFinished) {
                i[0]++;
                switch (i[0]) {
                    case 0:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        progressDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                }

            }
            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();
    }

    public SweetAlertDialog getProgressDialog(){
        if (null != progressDialog)
            return progressDialog;
        return null;
    }

    /**
     * 普通提示弹出框
     */
    @Override
    public void tipDialog(String title){
        SweetAlertDialog dialog = new SweetAlertDialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitleText(title);
        dialog.show();
    }

    /**
     * 带有文本内容弹出框
     */
    @Override
    public void contentDialog(String title,String content){
        new SweetAlertDialog(context)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    /**
     * 错误提示弹出框
     */
    @Override
    public void errorDialog(String title){
        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .show();
    }

    /**
     * 成功提示弹出框
     */
    @Override
    public void successDialog(String title){
        new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .show();
    }

    /**
     * 警告提示弹出框
     */
    @Override
    public void warnConfirmDialog(String title, final String compliteTitle){
        new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setConfirmText("确认")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.setTitleText(compliteTitle)
                                .setConfirmText("完成")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

    /**
     * 确认或取消操作提示框
     */
    @Override
    public void confirmCancelDialog(String strtitle,String strnegetive,String strpositive, final CallBackimpl handle){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(strtitle)
                .setCancelText(strnegetive)
                .setConfirmText(strpositive)
                .showCancelButton(true)
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        handle.confirmHandle();
                    }
                })
                .show();
    }

    /**
     * 确认或取消操作都带回调
     */
    public void confirmCancelHandelDialog(String strtitle,String strnegetive,String strpositive, final CallBackimpl cancelHandle, final CallBackimpl handle){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(strtitle)
                .setCancelText(strnegetive)
                .setConfirmText(strpositive)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        cancelHandle.confirmHandle();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        handle.confirmHandle();
                    }
                })
                .show();
    }

    /**
     * 确认或取消操作提示框
     */
    @Override
    public void contentDialog(String strtitle,String content,String strnegetive,String strpositive,final CallBackimpl handle){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(strtitle)
                .setContentText(content)
                .setCancelText(strnegetive)
                .setConfirmText(strpositive)
                .showCancelButton(true)
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        handle.confirmHandle();
                    }
                })
                .show();
    }

    /**
     * 确认或取消操作提示框
     */
    public void contentCancleDialog(String strtitle,String content,String strnegetive,String strpositive, final CallBackimpl cancelHandle,final CallBackimpl handle){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(strtitle)
                .setContentText(content)
                .setCancelText(strnegetive)
                .setConfirmText(strpositive)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        cancelHandle.confirmHandle();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        handle.confirmHandle();
                    }
                })
                .show();
    }

    /**
     * 图片显示弹出框
     */
    @Override
    public void imageDialog(String title,int imageId){
        new SweetAlertDialog(context,SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setCustomImage(imageId)
                .show();
    }

}
