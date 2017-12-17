package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.MainActivity;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.widget.FlowMenu;

public class ClassifyFragment extends Fragment {
    @Bind(R.id.rg_menubar)
    RadioGroup rgMenubar;
    @Bind(R.id.ll_stick_bottomline)
    LinearLayout llStickBottomline;
    private View view;
    private DesignerListFragment designerListFragment;
    private BrandFragment brandFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init(){
        new FlowMenu(getActivity(),view);
        for(int i=0;i<rgMenubar.getChildCount();i++){
            ((RadioButton)rgMenubar.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_color));
        }

        rgMenubar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View view = llStickBottomline.getChildAt(i);
                    RadioButton radioButton = ((RadioButton) rgMenubar.getChildAt(i));
                    if (radioButton.getId() == checkedId) {
                        setFragment(i);
                        view.setBackgroundColor(getResources().getColor(R.color.black));
                        radioButton.setTextColor(getResources().getColor(R.color.black));
                    } else {
                        view.setBackgroundColor(getResources().getColor(R.color.transparent));
                        radioButton.setTextColor(getResources().getColor(R.color.text_color));
                    }
                }
            }
        });
        //根据传入的当前选项初始化页面
        ((RadioButton) rgMenubar.getChildAt(0)).setChecked(true);

    }

    /**
     * 设置切换页面
     *
     * @param page
     */
    private void setFragment(int page) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (page) {
            case 0:
                if (null == designerListFragment) {
                    designerListFragment = DesignerListFragment.newInstance(3);
                    transaction.add(R.id.frame_classify_container, designerListFragment);
                } else {
                    transaction.show(designerListFragment);
                }
                break;
            case 1:
                if (null == brandFragment) {
                    brandFragment = new BrandFragment();
                    transaction.add(R.id.frame_classify_container, brandFragment);
                } else {
                    transaction.show(brandFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将其他fragment隐藏
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (null != designerListFragment) transaction.hide(designerListFragment);
        if (null != brandFragment) transaction.hide(brandFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(MainActivity.mediaPlayer.isPlaying()){
            MainActivity.mediaPlayer.pause();
        }
    }

}
