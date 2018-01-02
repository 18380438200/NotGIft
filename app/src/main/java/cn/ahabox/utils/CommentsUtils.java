package cn.ahabox.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.ahabox.adapter.CommentsAdapter;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CityEntity;
import cn.ahabox.model.CommentEntity;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2016/7/1.
 * <p>
 * 评论功能数据请求
 */
public class CommentsUtils {
    private static Context context;
    private CommentsAdapter adapter;
    private static DataParserImpl dataParser;
    private EditText editText;
    private int id;   //当前商品或者设计师id
    private static DialogUtils dialogUtils;
    private ArrayList commentsDatas;

    public CommentsUtils(Context context, int id, EditText editText, ArrayList commentsDatas, ListView listView) {
        this.context = context;
        this.id = id;
        this.editText = editText;
        this.commentsDatas = commentsDatas;
        dataParser = new DataParserImpl();
        dialogUtils = new DialogUtils(context);
        adapter = new CommentsAdapter(context, commentsDatas, dialogUtils, dataParser);
        listView.setAdapter(adapter.getAdapter());
    }

    /**
     * 获取评论列表
     *
     * @param kind
     * @param id
     * @param page
     */
    public void getCommentsList(String kind, int id, final int page, final View tvMore) {
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    boolean isEnd = obj.getBoolean("is_end");
                    JSONArray array = obj.getJSONArray("comments");     //加载更多数据将新数据添加进数据源中
                    List list = JSON.parseArray(array.toString(), CommentEntity.class);
                    adapter.addAll(list);
                    if (isEnd) {
                        tvMore.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseCommentsList(kind, id, page);
    }

    /**
     * 提交评论
     */
    public void getResults(String kind, final String content) {
        HashMap params = new HashMap();
        params.put("field", kind);
        params.put("value", id);
        params.put("content", content);
        dialogUtils.progressDialog();
        Request.postAsync(Api.MAKE_COMMENTS, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                makeCommentResult(str, content);
            }
        });
    }

    /**
     * 评论结果处理
     *
     * @param str
     */
    public void makeCommentResult(String str, String content) {
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            if (status == Config.STATUS_SUCCESSED) {
                Toast.makeText(context, context.getResources().getString(R.string.comment_success), Toast.LENGTH_SHORT).show();
                editText.setText("");    //评论成功清空编辑框并添加到第一项
                int createCommentId = obj.getJSONObject("data").getInt("comment_id");
                addComment(createCommentId, content);
            } else {
                Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加刚评论的项并刷新数据源
     *
     * @param commentId
     * @param content
     */
    private void addComment(int commentId, String content) {
        CommentEntity entity = new CommentEntity();
        entity.setId(commentId);
        entity.setAvatar_url(App.getUser().getAvatar_url());
        entity.setContent(content);
        entity.setUser_name(App.getUser().getNickname());
        entity.setUser_id(App.getUser().getId());
        commentsDatas.add(0, entity);
        adapter.getAdapter().notifyDataSetChanged();
    }

}
