package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.config.App;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.CommentsUtils;
import cn.ahabox.widget.ListViewForScrollView;

/**
 * Created by libo on 2016/7/8.
 *
 * 评论fragment
 */
public class CommentFragment extends Fragment {
    @Bind(R.id.et_designerdetail_content)
    EditText etDesignerdetailContent;
    @Bind(R.id.tv_designerdetail_more)
    TextView tvDesignerdetailMore;
    @Bind(R.id.ls_designerdetail_comments)
    ListViewForScrollView lsDesignerdetailComments;
    private static final String ARG_PARAM1 = "param1";

    private int productId;
    private CommentsUtils commentsUtils;
    /** 评论列表数据 */
    private ArrayList commentsDatas = new ArrayList();
    /** 加载当前评论页数 */
    private int commentsPage = 1;

    public static CommentFragment newInstance(int param1) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        commentsUtils = new CommentsUtils(getActivity(), productId, etDesignerdetailContent, commentsDatas,lsDesignerdetailComments);
        commentsUtils.getCommentsList("product", productId, commentsPage++, tvDesignerdetailMore);
    }

    @OnClick({R.id.tv_designerdetail_comment,R.id.tv_designerdetail_more})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_designerdetail_comment:
                checkComment();
                break;
            case R.id.tv_designerdetail_more:
                commentsUtils.getCommentsList("product", productId, commentsPage++,tvDesignerdetailMore);
                break;
        }
    }

    /**
     * 检查评论字数是否符合条件
     */
    private void checkComment(){
        App.gotoLogin(getActivity(), new CallBackimpl() {
            @Override
            public void confirmHandle() {
                String content = etDesignerdetailContent.getText().toString().trim();
                if(null != content && content.length() != 0){
                    if(content.length() <= 120){   //评论字数不能大于120
                        commentsUtils.getResults("product_id", content);
                    }else{
                        Toast.makeText(getActivity(),"提交字数不能超过120",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"提交内容不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
