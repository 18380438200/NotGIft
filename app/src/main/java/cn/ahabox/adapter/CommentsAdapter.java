package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CommentEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.CommentLikeUtils;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/7/1.
 *
 * 评论适配器
 */
public class CommentsAdapter extends MyBaseAdapter<CommentEntity> {
    private DialogUtils dialogUtils;
    private DataParserImpl dataParser;

    public CommentsAdapter(Context context, List<CommentEntity> datas,DialogUtils dialogUtils,DataParserImpl dataParser) {
        super(context, datas);
        this.dialogUtils = dialogUtils;
        this.dataParser = dataParser;
    }

    @Override
    public CommonAdapter<CommentEntity> createAdapter() {
        return new CommonAdapterPosition(context,datas, R.layout.item_comments) {

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, final int position) {
                final CommentEntity entity = (CommentEntity) o;
                ImageView ivAvater = baseViewHolder.getView(R.id.iv_comments_avater);
                TextView tvName = baseViewHolder.getView(R.id.tv_comments_name);
                TextView tvContent = baseViewHolder.getView(R.id.tv_comments_content);
                TextView tvTime = baseViewHolder.getView(R.id.tv_comments_time);
                LinearLayout llReplyName = baseViewHolder.getView(R.id.ll_comments_replay);
                final TextView tvReplyName = baseViewHolder.getView(R.id.tv_comments_replyName);
                final TextView tvReply = baseViewHolder.getView(R.id.tv_comments_reply);
                final EditText etReply = baseViewHolder.getView(R.id.et_designerdetail_content);
                TextView tvComment = baseViewHolder.getView(R.id.tv_designerdetail_comment);
                final ImageView ivLike = baseViewHolder.getView(R.id.iv_comments_like);
                TextView tvLikeNum = baseViewHolder.getView(R.id.tv_comments_num);
                final RelativeLayout rlReply = baseViewHolder.getView(R.id.rl_comments_reply);

                final boolean[] isShow = {false};   //是否显示回复输入框

                CommentLikeUtils commentLikeUtils = new CommentLikeUtils(context,dialogUtils,dataParser,entity.getLikes(),entity.is_like());
                commentLikeUtils.showLike(tvLikeNum);
                commentLikeUtils.showLikeBg(ivLike);
                commentLikeUtils.setEvent(entity.getId(),ivLike,tvLikeNum);

                ImageLoader.getInstance().displayImage(entity.getAvatar_url(), ivAvater, MyApp.circleOption);
                tvName.setText(entity.getUser_name());
                tvContent.setText(entity.getContent());

                //是否为回复项
                if(null != entity.getReply_user_name()){
                    llReplyName.setVisibility(View.VISIBLE);
                    tvReplyName.setText(entity.getReply_user_name());
                }

                //创建时间
                if(entity.getCreated_at() == 0){
                    tvTime.setText("刚刚");
                }else{
                    tvTime.setText(StrUtils.getdate(String.valueOf(entity.getCreated_at())));
                }

                //登录之后才会显示删除或回复功能
                if(null != App.getUser()){
                    if(entity.getUser_id() == App.getUser().getId()){
                        tvReply.setText("删除");
                        tvReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogUtils.confirmCancelDialog("是否删除", "取消", "确认", new CallBackimpl() {
                                    @Override
                                    public void confirmHandle() {
                                        commentDel(entity.getId(),position);
                                    }
                                });
                            }
                        });
                    }else{
                        tvReply.setText("回复");
                        tvReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isShow[0] = !isShow[0];
                                if (isShow[0]) {
                                    rlReply.setVisibility(View.VISIBLE);
                                    tvReply.setText(context.getResources().getString(R.string.hide));
                                } else {
                                    rlReply.setVisibility(View.GONE);
                                    tvReply.setText(context.getResources().getString(R.string.reply));
                                }

                            }
                        });
                    }
                }

                tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.gotoLogin(context, new CallBackimpl() {
                            @Override
                            public void confirmHandle() {
                                String content = etReply.getText().toString().trim();
                                if (null != content && content.length() != 0) {
                                    if (content.length() <= 120) {   //评论字数不能大于120
                                        reply(entity.getId(), content, rlReply);
                                    } else {
                                        Toast.makeText(context, "提交字数不能超过120", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "提交内容不能为空", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });

            }

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {}

        };
    }

    /**
     * 回复
     */
    public void reply(int replyId, final String content, final View container){
        HashMap params = new HashMap();
        params.put("reply_id", replyId);
        params.put("comment_content", content);
        Request.postAsync(Api.MAKE_COMMENTS, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        Toast.makeText(context, context.getResources().getString(R.string.comment_success), Toast.LENGTH_SHORT).show();
                        container.setVisibility(View.GONE);
                        int commentId = obj.getJSONObject("data").getInt("comment_id");
                        String replyName = obj.getJSONObject("data").getString("comment_reply_nickname");
                        addReply(commentId,content,replyName);
                    } else {
                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 添加刚评论的项并刷新数据源
     * @param commentId
     * @param content
     */
    private void addReply(int commentId,String content,String replyName){
        CommentEntity entity = new CommentEntity();
        entity.setId(commentId);
        entity.setAvatar_url(App.getUser().getAvatar_url());
        entity.setContent(content);
        entity.setReply_user_name(replyName);
        entity.setUser_name(App.getUser().getNickname());
        entity.setUser_id(App.getUser().getId());
        datas.add(0, entity);
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 删除评论
     * @param commentId
     */
    private void commentDel(int commentId, final int position){
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if(status == Config.STATUS_SUCCESSED){
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        datas.remove(datas.get(position));
                        getAdapter().notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,obj.getString("errmsg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseCommentDel(commentId);
    }

}
