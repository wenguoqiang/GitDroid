package com.feicuiedu.gitdroid.hotUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.gitdroid.R;
import com.feicuiedu.gitdroid.Utils.ActivityUtils;
import com.feicuiedu.gitdroid.login.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by gqq on 2016/12/13.
 */
public class HotUserFragment extends Fragment implements HotUserPresenter.HotUserView {

    @BindView(R.id.lvRepos)
    ListView lvUsers;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.errorView)
    TextView errorView;
    private HotUserAdapter adapter;

    private ActivityUtils activityUtils;
    private HotUserPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new HotUserPresenter(this);
        activityUtils = new ActivityUtils(this);
        adapter = new HotUserAdapter();
        lvUsers.setAdapter(adapter);
        // 初始PulltoRefresh控件
        initPullToRefresh();

        //如果没有数据，就自动刷新
        if (adapter.getCount() <= 0) {
            // 只是为了给UI线程让下步，不要在这里等着刷新数据
            ptrClassicFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrClassicFrameLayout.autoRefresh();
                }
            }, 200);
        }
    }

    private void initPullToRefresh() {
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setDurationToClose(2000);
        // 下拉刷新控件的监听(下拉时和上拉时会来回调的)
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadMore();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refresh();
            }
        });
    }

    @Override
    public void refreshData(List<User> list) {
        adapter.clear();
        adapter.addAll(list);
    }

    @Override
    public void showRefreshView() {
        ptrClassicFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void stopRefresh() {
        ptrClassicFrameLayout.refreshComplete();
    }

    @Override
    public void addLoadData(List<User> list) {
        adapter.addAll(list);
    }

    @Override
    public void showLoadView() {

    }

    @Override
    public void hideLoadView() {
        ptrClassicFrameLayout.refreshComplete();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
