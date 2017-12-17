package cn.ahabox.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.ImageViewAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.ProductDetailEntity;
import cn.ahabox.utils.MyLayoutManager;
import cn.ahabox.widget.PlayVideo;

/**
 * Created by libo on 2015/1/22.
 *
 * 图文详情
 */
public class PicDetailFragment extends Fragment {

    @Bind(R.id.rcv_picdetail)
    RecyclerView rcvPicdetail;
    /**
     * 图片列表
     */
    private ProductDetailEntity entity;
    @Bind(R.id.rl_video_container)
    RelativeLayout rlVideo;
    private static String ARG_PARAM1 = "params1";
    private PlayVideo playVideo;
    private ImageViewAdapter imageViewAdapter;

    public static PicDetailFragment newInstance(ProductDetailEntity entity) {
        Bundle args = new Bundle();
        PicDetailFragment fragment = new PicDetailFragment();
        args.putSerializable(ARG_PARAM1, entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (ProductDetailEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_detail, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        try {
            if (!entity.getVideo_url().equals("")) {
                playVideo = new PlayVideo(getActivity(), entity.getVideo_url());
                rlVideo.addView(playVideo.videoView);
            } else {
                rlVideo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //图文详情最长显示数
        if (entity.getDetail_pictures().size() > 9) {
            ArrayList pics = new ArrayList();
            for (int i = 0; i < 9; i++) {
                pics.add(entity.getDetail_pictures().get(i));
            }
            imageViewAdapter = new ImageViewAdapter(getActivity(), pics);
        } else {
            imageViewAdapter = new ImageViewAdapter(getActivity(), (ArrayList<String>) entity.getDetail_pictures());
        }
        rcvPicdetail.setLayoutManager(new MyLayoutManager(getActivity()));
        rcvPicdetail.setAdapter(imageViewAdapter);
    }

    @Override
    public void onDestroy() {    //退出停止视频播放
        super.onDestroy();
        try {
            if (null != playVideo) {
                playVideo.mediaPlayer.release();
                playVideo.mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
