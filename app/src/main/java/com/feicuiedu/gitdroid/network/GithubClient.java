package com.feicuiedu.gitdroid.network;

import com.feicuiedu.gitdroid.hotUser.HotUserResult;
import com.feicuiedu.gitdroid.hotRepo.RepoResult;
import com.feicuiedu.gitdroid.login.TokenInterceptor;
import com.feicuiedu.gitdroid.login.model.AccessToken;
import com.feicuiedu.gitdroid.login.model.User;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * Created by gqq on 2016/12/8.
 */

// 网络请求的初始化Retrofit
public class GithubClient implements GithubApi{

    private static GithubClient mGithubClient;
    private final GithubApi mGithubApi;

    public static synchronized GithubClient getInstance() {
        if (mGithubClient == null) {
            mGithubClient = new GithubClient();
        }
        return mGithubClient;
    }

    private GithubClient(){

        // 初始化拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 初始化OkhttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(interceptor)
                .build();

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        // 实现Api接口请求
        mGithubApi = retrofit.create(GithubApi.class);
    }

    @Override
    public Call<AccessToken> getOAuthToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("code") String code) {
        return mGithubApi.getOAuthToken(clientId, clientSecret, code);
    }

    @Override
    public Call<User> getUser() {
        return mGithubApi.getUser();
    }

    @Override
    public Call<RepoResult> searchRepos(@Query("q") String q, @Query("page") int page) {
        return mGithubApi.searchRepos(q, page);
    }

    @Override
    public Call<HotUserResult> serachUser(@Query("q") String query, @Query("page") int pageId) {
        return mGithubApi.serachUser(query,pageId);
    }
}
