package com.feicuiedu.gitdroid.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gqq on 2016/12/8.
 */

// 请求token得到的响应数据模型
public class AccessToken {

    /**
     * access_token : e72e16c7e42f292c6912e7710c838347ae178b4a
     * scope : repo,gist
     * token_type : bearer
     */

    @SerializedName("access_token")
    private String accessToken;
    private String scope;
    @SerializedName("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
