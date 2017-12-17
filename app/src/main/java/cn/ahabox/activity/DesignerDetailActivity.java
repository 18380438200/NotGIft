package cn.ahabox.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.ProductFragment;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.utils.CommentsUtils;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.ListViewForScrollView;
import cn.ahabox.widget.PlayVideo;
import cn.ahabox.widget.ProgressVoice;

/**
 * Created by libo on 2015/1/19.
 * <p/>
 * 设计师详情
 */
public class DesignerDetailActivity extends BaseActivity {
    @Bind(R.id.ll_designer_expand)
    LinearLayout llDesignerExpand;
    @Bind(R.id.rl_designer_detail)
    RelativeLayout rlDesignerDetail;
    @Bind(R.id.tv_designer_expand)
    TextView tvDesignerExpand;
    @Bind(R.id.iv_designer_small)
    ImageView ivDesignerSmall;
    @Bind(R.id.iv_designer_open)
    ImageView ivDesignerOpen;
    @Bind(R.id.tv_designer_introduce)
    TextView tvDesignerIntroduce;
    @Bind(R.id.iv_designerdetail_cover)
    ImageView ivDesignerdetailCover;
    @Bind(R.id.tv_designerdetail_name)
    TextView tvDesignerdetailName;
    @Bind(R.id.iv_designerdetail_pic)
    ImageView ivDesignerdetailPic;
    @Bind(R.id.lv_designerdetail_pic)
    ListViewForScrollView lvDesignerdetail;
    @Bind(R.id.rl_video_container)
    RelativeLayout rlVideoContainer;
    @Bind(R.id.designer_header)
    CustomActionBar customActionBar;
    @Bind(R.id.srl_designerdetail)
    SwipeRefreshLayout srlDesignerdetail;
    @Bind(R.id.ib_designerdetail_focus)
    ImageButton ibDesignerdetailFocus;
    @Bind(R.id.ls_designerdetail_comments)
    ListViewForScrollView lsDesignerdetailComments;
    @Bind(R.id.tv_designerdetail_comment)
    TextView tvDesignerdetailComment;
    @Bind(R.id.et_designerdetail_content)
    EditText etDesignerdetailContent;
    @Bind(R.id.tv_designerdetail_more)
    TextView tvDesignerdetailMore;
    private PlayVideo playVideo;
    /**
     * 设计师介绍是否展开
     */
    private boolean isExpanded = true;
    private DesignerDetailEntity entity;
    private ProgressVoice progressVoice;
    /**
     * 是否关注
     */
    private boolean isFollowed = false;
    /**
     * 设计师文本介绍最大文字行数
     */
    private final int maxLines = 30;
    /**
     * 当前设计师详情id
     */
    private int designerId;
    private CommentsUtils commentsUtils;
    private DialogUtils dialogUtils;
    /** 评论列表数据 */
    private ArrayList commentsDatas = new ArrayList();
    /** 加载当前评论页数 */
    private int commentsPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_detail);
        ButterKnife.bind(this);

        initView();
        event();

    }

    private void initView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        dialogUtils = new DialogUtils(DesignerDetailActivity.this);
        designerId = getIntent().getIntExtra(Config.DESIGNER_ID_KEY, 1);

        commentsUtils = new CommentsUtils(this, designerId, etDesignerdetailContent, commentsDatas,lsDesignerdetailComments);

        commentsUtils.getCommentsList("child_category", designerId, commentsPage++, tvDesignerdetailMore);

        progressVoice = (ProgressVoice) findViewById(R.id.progress_voice);

        srlDesignerdetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                srlDesignerdetail.setVisibility(View.VISIBLE);
                srlDesignerdetail.setRefreshing(false);
                entity = JSON.parseObject(obj.toString(), DesignerDetailEntity.class);
                setDatas();
            }
        });
        dataParser.parseDesignerDetail(designerId);
    }

    private void setDatas() {
        handleFocus(entity.is_follow());

        if (null != entity.getCover()) {     //设计师图像和介绍语音
            ImageLoader.getInstance().displayImage(entity.getCover(), ivDesignerdetailCover, MyApp.getInstance().options);
            progressVoice.init(entity.getVoice_url());
        } else {
            ivDesignerdetailCover.setVisibility(View.GONE);
            progressVoice.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(entity.getThumbnail_url(), ivDesignerdetailPic, MyApp.getInstance().options);

        if (null != entity.getVideo_url()) {    //视频介绍内容
            playVideo = new PlayVideo(getApplicationContext(), entity.getVideo_url());
            rlVideoContainer.addView(playVideo.videoView);
        } else {
            rlVideoContainer.setVisibility(View.GONE);
        }

        if (null != entity.getName()) {   //设计师名称
            tvDesignerdetailName.setText(entity.getName());
        } else {
            tvDesignerdetailName.setVisibility(View.GONE);
        }

        if (null != entity.getDescribe()) {   //设计师描述内容
            tvDesignerIntroduce.setText(entity.getDescribe());
        } else {
            tvDesignerIntroduce.setVisibility(View.GONE);
            llDesignerExpand.setVisibility(View.GONE);
        }

        customActionBar.setTitle(entity.getTitle());

        CommonAdapter picAdapter = new CommonAdapter(getApplicationContext(), entity.getImages(), R.layout.item_adapte_imageview) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                DesignerDetailEntity.ImagesEntity entity1 = (DesignerDetailEntity.ImagesEntity) o;
                ImageView imageView = baseViewHolder.getView(R.id.imageview);
                ImageLoader.getInstance().displayImage(entity1.getSrc(), imageView, MyApp.getInstance().options);
            }
        };
        lvDesignerdetail.setAdapter(picAdapter);

        //将作品展示列表传给ProductFragment
        ArrayList productList = (ArrayList) entity.getProducts();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, ProductFragment.newInstance(productList)).commit();
    }

    private void event() {
        llDesignerExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent(isExpanded);
                isExpanded = !isExpanded;
            }
        });

        ibDesignerdetailFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.gotoLogin(DesignerDetailActivity.this, new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        handleFollow();
                    }
                });
            }
        });

    }

    @OnClick({R.id.tv_designerdetail_comment,R.id.tv_designerdetail_more})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_designerdetail_comment:
                checkComment();
                break;
            case R.id.tv_designerdetail_more:
                commentsUtils.getCommentsList("child_category", designerId, commentsPage++,tvDesignerdetailMore);
                break;
        }
    }

    /**
     * 检查评论字数是否符合条件
     */
    private void checkComment(){
        App.gotoLogin(getApplicationContext(), new CallBackimpl() {
            @Override
            public void confirmHandle() {
                String content = etDesignerdetailContent.getText().toString().trim();
                if(StrUtils.isStr(content)){
                    if(content.length() <= 120){   //评论字数不能大于120
                        commentsUtils.getResults("child_category_id", content);
                    }else{
                        showshortText("提交字数不能超过120");
                    }
                }else{
                    showshortText("提交内容不能为空");
                }

            }
        });
    }

    /**
     * 设计师关注操作
     */
    private void handleFocus(final boolean isFollowed) {
        this.isFollowed = isFollowed;
        if (isFollowed) {
            ibDesignerdetailFocus.setBackgroundResource(R.mipmap.focus_yes);
        }

    }

    /**
     * 根据当前状态
     */
    private void handleFollow() {
        HashMap params = new HashMap();
        params.put("child_category_id", designerId);
        String url;
        if (isFollowed) {
            url = Api.FOCUS_REMOVE;
        } else {
            url = Api.FOCUS_ADD;
        }
        Request.postAsync(url, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    if (obj.getInt("status") == Config.STATUS_SUCCESSED) {
                        if (isFollowed) {
                            ibDesignerdetailFocus.setBackgroundResource(R.mipmap.focus_no);
                        } else {
                            ibDesignerdetailFocus.setBackgroundResource(R.mipmap.focus_yes);
                        }
                        isFollowed = !isFollowed;
                    } else {
                        showshortText(obj.getString("errmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void switchContent(boolean isExpanded) {
        if (isExpanded) {
            //当前状态为展开
            rlDesignerDetail.setVisibility(View.VISIBLE);
            tvDesignerExpand.setText("收起内容");
            ivDesignerOpen.setVisibility(View.GONE);
            ivDesignerSmall.setVisibility(View.VISIBLE);
            tvDesignerIntroduce.setMaxLines(maxLines);
        } else {
            //当前状态为收起
            rlDesignerDetail.setVisibility(View.GONE);
            tvDesignerExpand.setText("展开内容");
            ivDesignerOpen.setVisibility(View.VISIBLE);
            ivDesignerSmall.setVisibility(View.GONE);
            tvDesignerIntroduce.setMaxLines(2);
        }
    }

    @Override
    protected void onDestroy() {   //退出当前页面
        super.onDestroy();
        if (null != MainActivity.mediaPlayer) {
            MainActivity.mediaPlayer.pause();
        }
        try {
            if (null != playVideo.mediaPlayer) {
                playVideo.mediaPlayer.release();
                playVideo.mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButterKnife.unbind(this);
    }
}
