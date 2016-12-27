package com.feicuiedu.gitdroid.hotRepo;

import java.util.List;

/**
 * Created by gqq on 2016/12/9.
 */

public interface RepoListView {

    void addRefreshData(List<Repo> repos);// 设置刷新出来的数据

    void addLoadMore(List<Repo> repos);// 设置加载出来的数据

    void stopMoreData();//停止刷新

    void showEmptyView();// 显示空的视图

    void showErrorView();// 显示错误的视图

    void showMessage(String msg);// 弹吐司，显示信息
}
