package com.feicuiedu.gitdroid.login.model;

/**
 * Created by gqq on 2016/12/8.
 */

// 主要是为了存储Token和请求的用户信息
public class UserRepo {

    private static String accessToken;

    private static User user;

    // 判断一下当前有没有Token
    public static boolean hasAccessToken(){
        return accessToken !=null;
    }

    // 当前是不是为空
    public static boolean isEmpty(){
        return accessToken==null || user ==null;
    }

    // 清空信息
    public static void clear(){
        accessToken = null;
        user = null;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        UserRepo.accessToken = accessToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserRepo.user = user;
    }
}
