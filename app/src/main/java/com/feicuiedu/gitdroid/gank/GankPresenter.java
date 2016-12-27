package com.feicuiedu.gitdroid.gank;

import com.feicuiedu.gitdroid.gank.model.GankItem;
import com.feicuiedu.gitdroid.gank.model.GankResult;
import com.feicuiedu.gitdroid.gank.network.GankClient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 当前这个功能
 *
 * 做了一些什么样的业务 (调用了m层的数据处理, 在这里都由retrofit做完了)
 *
 * 得到业务的结果后，
 * 做了一些什么的工作去协调视图 (分别在什么情况下，应该显示什么视图)
 *
 *
 * 代码已PUSH
 *
 * https://github.com/gqq519/GitDroid.git
 *
 * 15:25继续
 *
 * Created by 123 on 2016/9/2.
 */
public class GankPresenter {

    private Call<GankResult> call;
    private GankView gankView;

    public GankPresenter(GankView gankView) {
        this.gankView = gankView;
    }

    public void getGanks(Date date) {
        //去做获取干货数据的操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 网络连接
        call = GankClient.getInstance().getDailyData(year, month, day);
        call.enqueue(callback);
    }

    //  (callbakc为网络连接模块的 业务接口, 也就是我们常说的回调)
    private Callback<GankResult> callback = new Callback<GankResult>() {
        @Override
        public void onResponse(
                Call<GankResult> call,
                Response<GankResult> response) {
            //请求结果
            GankResult gankResult = response.body();
            if (gankResult == null) {
                gankView.showMessage("数据是空的！");
                return;
            }
            // 什么情况下"协调"显示空视图????
            // 1. 是错误 code
            // 2. 不是错误,但无结果
            // 3. ...............结果里面，没有android类别
            // 4. .......................................，但里面没数据
            if (gankResult.isError()
                    || gankResult.getResult() == null
                    || gankResult.getResult().getAndroidItems() == null
                    || gankResult.getResult().getAndroidItems().isEmpty()) {
                gankView.showEmptyView();
                return;
            }
            // "协调"将获取到的数据交付到视图层去，让视图展示和改变
            List<GankItem> androidItems = gankResult.getResult().getAndroidItems();
            gankView.hideEmptyView();
            gankView.setData(androidItems);
        }

        @Override
        public void onFailure(Call<GankResult> call, Throwable t) {
            //请求失败
            gankView.showMessage("Error：" + t.getMessage());
        }
    };

    // 拿到业务处理的结果后
    // present和视图做衔接
    public interface GankView {
        /**
         * 1. 设置数据
         * 2. 显示空视图
         * 3. 显示信息
         * 4. 隐藏空视图
         */
        void setData(List<GankItem> list);

        void showEmptyView();

        void showMessage(String msg);

        void hideEmptyView();
    }
}
