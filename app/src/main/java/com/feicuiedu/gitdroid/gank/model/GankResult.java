package com.feicuiedu.gitdroid.gank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 123 on 2016/9/1.
 */
public class GankResult {
    /*
    "category": [
        "Android",
        "\u4f11\u606f\u89c6\u9891",
        "\u798f\u5229",
        "iOS"
        ],
    "error": false,
    "results": {
     */

    private List<String> category;

    private boolean error;

    @SerializedName("results")
    private Result result;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result{

        @SerializedName("Android")
        private List<GankItem> androidItems;

        public List<GankItem> getAndroidItems() {
            return androidItems;
        }
    }
}
