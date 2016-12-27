package com.feicuiedu.gitdroid.hotRepo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.gitdroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gqq on 2016/12/1.
 */

// 热门仓库(最热门)
public class HotRepoFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_repo, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * 1. 要切换Fragment，所以要创建一个适配器，设置给ViewPager
         * 2. 完善Adapter
         * 3. 完善标题的展示和联动
         *
         */
        HotRepoAdapter adapter = new HotRepoAdapter(getChildFragmentManager(),getContext());

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);// 绑定ViewPager
    }
}
