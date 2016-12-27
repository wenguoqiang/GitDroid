package com.feicuiedu.gitdroid.login;

import com.feicuiedu.gitdroid.Utils.LogUtils;
import com.feicuiedu.gitdroid.login.model.UserRepo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gqq on 2016/12/8.
 */
// 自定义拦截器的作用：为了将Token值添加到所有的retrofit请求的请求头信息里面
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        // 1. 获取拦截的请求
        Request request = chain.request();

        // 2. 重新构建新的请求
        Request.Builder builder = request.newBuilder();

        // 如果已授权拿到token
        if (UserRepo.hasAccessToken()) {

            // 3. 在新的请求builder里面添加token的头信息
            builder.header("Authorization", "token " + UserRepo.getAccessToken());
        }
        /**
         *  以上的操作不明白可参考请求构建的过程
         *  Request request1 = new Request.Builder().build();
         */
       // 4. 继续执行请求得到响应
        Response response = chain.proceed(builder.build());

        // 5. 成功，直接返回
        if (response.isSuccessful()){
            return response;
        }

        // 附：处理一下其他情况：API文档里面有介绍
        if (response.code()==401|| response.code()==403){
            LogUtils.e(response.body().string());
            throw new IOException("未经授权的！");
        }else {
            throw new IOException("响应码："+response.code());
        }
    }
}
