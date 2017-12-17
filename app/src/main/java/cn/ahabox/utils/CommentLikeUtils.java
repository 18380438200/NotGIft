package cn.ahabox.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2016/7/8.
 *
 * 评论点赞功能
 */
public class CommentLikeUtils {
    private Context context;
    private int likeNum;
    private DialogUtils dialogUtils;
    private DataParserImpl dataParser;
    private boolean isLike;

    public CommentLikeUtils(Context context,DialogUtils dialogUtils,DataParserImpl dataParser,int likeNum,boolean isLike){
        this.context = context;
        this.dialogUtils = dialogUtils;
        this.dataParser = dataParser;
        this.likeNum = likeNum;
        this.isLike = isLike;
    }

    public void showLikeBg(ImageView ivLike){
        if(isLike){
            ivLike.setBackgroundResource(R.mipmap.like);
        }else{
            ivLike.setBackgroundResource(R.mipmap.dislike);
        }
    }

    public void showLike(TextView tvLikeNum){
        //点赞数量
        if(likeNum == 0){
            tvLikeNum.setVisibility(View.GONE);
        }else{
            tvLikeNum.setVisibility(View.VISIBLE);
        }
        tvLikeNum.setText("" + likeNum);
    }

    public void setEvent(final int commentId,final ImageView ivLike, final TextView tvLikeNum){
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞之前先判断是否已经登录
                App.gotoLogin(context, new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        if (isLike) {
                            disLike(commentId, ivLike,tvLikeNum);
                        } else {
                            like(commentId, ivLike,tvLikeNum);
                        }
                    }
                });
            }
        });
    }

    /**
     * 点赞
     */
    public void like(int commentId, final ImageView ivLike, final TextView tvLikeNum){
        dialogUtils.progressDialog();
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        ivLike.setBackgroundResource(R.mipmap.like);
                        Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                        tvLikeNum.setText("" + ++likeNum);
                        tvLikeNum.setVisibility(View.VISIBLE);
                        isLike = !isLike;
                    } else {
                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseLike(commentId);
    }

    /**
     * 取消点赞
     * @param commentId
     * @param ivLike
     */
    public void disLike(int commentId, final ImageView ivLike, final TextView tvLikeNum){
        dialogUtils.progressDialog();
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        ivLike.setBackgroundResource(R.mipmap.dislike);
                        Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                        if (--likeNum == 0) {
                            tvLikeNum.setVisibility(View.GONE);
                        } else {
                            tvLikeNum.setText("" + likeNum);
                        }
                        isLike = !isLike;
                    } else {
                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseDislike(commentId);
    }

}
