package com.feicuiedu.gitdroid.login;

import com.feicuiedu.gitdroid.login.model.AccessToken;
import com.feicuiedu.gitdroid.login.model.User;
import com.feicuiedu.gitdroid.login.model.UserRepo;
import com.feicuiedu.gitdroid.network.GithubApi;
import com.feicuiedu.gitdroid.network.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gqq on 2016/12/8.
 */

// 登录的业务类
public class LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    private Call<AccessToken> mTokenCall;
    private Call<User> mUserCall;

    public void login(String code) {
        /**
         * 1. 获取Token
         * 2. token获取用户信息
         */

        // 请求之前展示进度动画
        mLoginView.showProgress();

        // 取消掉重新请求
        if (mTokenCall != null) {
            mTokenCall.cancel();
        }

        // 获取token的请求
        mTokenCall = GithubClient.getInstance().getOAuthToken(
                GithubApi.CLIENT_ID,
                GithubApi.CLIENT_SECRET,
                code);
        mTokenCall.enqueue(tokenCallback);
    }

    // 获取token的callback
    private Callback<AccessToken> tokenCallback = new Callback<AccessToken>() {

        @Override
        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

            // 响应成功
            if (response.isSuccessful()){

                // 取出响应信息，得到Token值
                AccessToken accessToken = response.body();
                String token = accessToken.getAccessToken();

                // 存储Token值:方便使用
                UserRepo.setAccessToken(token);

                // 获取用户信息请求前展示进度动画效果
                mLoginView.showProgress();

                // 根据token获取用户信息
                mUserCall = GithubClient.getInstance().getUser();
                mUserCall.enqueue(mUserCallback);

            }
        }

        @Override
        public void onFailure(Call<AccessToken> call, Throwable t) {
            mLoginView.showMessage("请求失败"+t.getMessage());
            mLoginView.resetWeb();// 失败之后，重新加载页面，重新请求
            mLoginView.showProgress();// 重新请求，展示进度动画

        }
    };

    // 获取用户信息的Callback
    private Callback<User> mUserCallback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {

            // 请求成功
            if (response.isSuccessful()){

                // 响应的用户信息存储一下
                User user = response.body();
                UserRepo.setUser(user);

                // 提示登录成功
                mLoginView.showMessage("登录成功");

                // 跳转到主页面
                mLoginView.navigationToMain();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            mLoginView.showMessage("请求失败"+t.getMessage());
            mLoginView.resetWeb();// 重新请求
            mLoginView.showProgress();// 展示进度动画
        }
    };
}
