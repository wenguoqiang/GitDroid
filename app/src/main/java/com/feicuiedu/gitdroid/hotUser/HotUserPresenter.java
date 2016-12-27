package com.feicuiedu.gitdroid.hotUser;

import com.feicuiedu.gitdroid.login.model.User;
import com.feicuiedu.gitdroid.network.GithubClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gqq on 2016/12/13.
 */

public class HotUserPresenter {
    // 视图接口
    private HotUserView hotUserView;
    private int nextPage = 1;
    private Call<HotUserResult> userCall;

    public HotUserPresenter(HotUserView hotUserView) {
        this.hotUserView = hotUserView;
    }

    /**
     * 执行刷新(获取最新数据)的业务，也就是让调用M层逻辑方法
     */
    public void refresh() {
        // 协调视图显示 .....
        hotUserView.showRefreshView();
        if (userCall != null) {
            userCall.cancel();
        }
        nextPage = 1;
        userCall = GithubClient.getInstance().serachUser("followers:>1000", nextPage);
        userCall.enqueue(refreshCallback);
    }

    public void loadMore() {
        hotUserView.showLoadView();
        if (userCall != null) {
            userCall.cancel();
        }
        userCall = GithubClient.getInstance().serachUser("followers:>1000", nextPage);
        userCall.enqueue(loadMoreCallback);
    }

    private Callback<HotUserResult> refreshCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {
            hotUserView.stopRefresh();
            // 200-299(201创建成功)
            if (response.isSuccessful()) {
                HotUserResult hotUserResult = response.body();
                if (hotUserResult == null) {
                    hotUserView.showMessage("结果为空");
                    return;
                }
                List<User> users = hotUserResult.getUsers();
                // 将结果(刷新获取到的用户列表信息数据),交付给视图去
                hotUserView.refreshData(users);
                nextPage = 2;
                return;
            }
            hotUserView.showMessage("失败:" + response.code());
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            hotUserView.stopRefresh();
            hotUserView.showMessage("刷新失败:" + t.getMessage());
        }
    };

    private Callback<HotUserResult> loadMoreCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {
            hotUserView.hideLoadView();
            //
            if (response.isSuccessful()) {
                HotUserResult hotUserResult = response.body();
                if (hotUserResult == null) {
                    hotUserView.showMessage("结果为空");
                    return;
                }
                List<User> users = hotUserResult.getUsers();
                // 加载更多..
                hotUserView.addLoadData(users);
                nextPage++;
                return;
            }
            hotUserView.showMessage("失败:" + response.code());
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            hotUserView.hideLoadView();
            hotUserView.showMessage("加载失败：" + t.getMessage());
        }
    };


    // 在这个处理过程中（开始逻辑，得到逻辑过程及结果反馈）
    // 视图应该干什么？？
    // 请和"视图那边" 定个接口出来，统一一下标准,才好干活

    public interface HotUserView {
        //下拉相关------------
        void refreshData(List<User> list); // 交付到视图去用显示的(listview)

        void showRefreshView();

        void stopRefresh();

        // 上拉相关------------
        void addLoadData(List<User> list);

        void showLoadView();

        void hideLoadView();

        // 通用(在业务过程重要提示，如error提示)
        void showMessage(String msg);
    }
}
