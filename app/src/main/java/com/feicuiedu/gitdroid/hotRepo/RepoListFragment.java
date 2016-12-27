package com.feicuiedu.gitdroid.hotRepo;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by gqq on 2016/12/2.
 */

public class RepoListFragment extends Fragment implements RepoListView {

    private static final String KEY_LANGUAGE = "key_language";

    @BindView(R.id.lvRepos)
    ListView mLvRepos;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    @BindView(R.id.errorView)
    TextView mErrorView;
    private RepoListAdapter mAdapter;
    private RepoListPresenter mRepoListPresenter;

    private ActivityUtils mActivityUtils;

    /**
     * 仓库列表页面：不同语言的仓库列表数据
     * 在这个页面里面展示的是不同语言的仓库列表：根据不同的语言去请求不同的数据
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);

        mRepoListPresenter = new RepoListPresenter(getLanguage(),this);
        mActivityUtils = new ActivityUtils(this);

        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 不建议在构造方法里面传递数据，所以可以提供一个创建方法，进行传递
     */
    public static RepoListFragment getInstance(Language language) {
        // 创建一个RepoListFragment
        RepoListFragment repoListFragment = new RepoListFragment();
        // 传递的数据
        Bundle bundle = new Bundle();
        // 传递的对象，注意：Language一定要实现序列化
        bundle.putSerializable(KEY_LANGUAGE, language);
        repoListFragment.setArguments(bundle);
        return repoListFragment;
    }

    // 拿到传递过来的数据
    private Language getLanguage() {
        return (Language) getArguments().getSerializable(KEY_LANGUAGE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RepoListAdapter();
        mLvRepos.setAdapter(mAdapter);

        //判断有没有数据，没有数据的话，自动刷新
        if (mAdapter.getCount()==0){
            mPtrClassicFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 自动刷新
                    mPtrClassicFrameLayout.autoRefresh();
                }
            }, 200);
        }

        initRefresh();

    }

    // 初始化刷新和加载
    private void initRefresh() {

        // 刷新间隔比较短，不触发刷新
        mPtrClassicFrameLayout.setLastUpdateTimeRelateObject(this);

        // 关闭视图的时间
        mPtrClassicFrameLayout.setDurationToClose(1500);

        // 设置头布局
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("I LIKE ANDROID");
        header.setPadding(0,30,0,30);
        mPtrClassicFrameLayout.setHeaderView(header);
        mPtrClassicFrameLayout.addPtrUIHandler(header);

        // 设置背景
        mPtrClassicFrameLayout.setBackgroundResource(R.color.colorRefresh);


        // 设置监听：我们使用的是既有刷新又有加载的
        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {

            // 上拉加载开始的方法
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                // 去进行加载更多，通过业务类来进行
                mRepoListPresenter.loadMore();
            }

            // 下拉刷新开始的方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 去刷新数据，通过业务类来进行
                mRepoListPresenter.refreshdata();
            }
        });
    }

    @Override
    public void addRefreshData(List<Repo> repos) {
        mAdapter.clear();
        mAdapter.addAll(repos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addLoadMore(List<Repo> repos) {
        mAdapter.addAll(repos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void stopMoreData() {
        mPtrClassicFrameLayout.refreshComplete();// 刷新停止（加载停止）
    }

    @Override
    public void showEmptyView() {
        mPtrClassicFrameLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        mPtrClassicFrameLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }
}
