package com.feicuiedu.gitdroid.hotRepo;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gqq on 2016/12/9.
 */

public class Language implements Serializable{

    private static List<Language> mLanguages;

    /**
     * path : java
     * name : Java
     */

    private String path;
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 我们所用的时候需要将语言集合给适配器使用，所以我们读取一下本地的这个文件
     * 并将读取出来的字符串Json，转换成集合
     * 所以对外提供一个方法，获取这个集合
      */
    public static List<Language> getLanguage(Context context){

        if (mLanguages!=null){
            return mLanguages;
        }
        try {
            // 拿到assets里面的文件
            InputStream inputStream = context.getAssets().open("langs.json");
            // 利用IO转换为字符串的第三方库，将IO转为json字符串
            String jsonString = IOUtils.toString(inputStream);
            Gson gson = new Gson();

            // 利用Gson解析将json字符串转换成集合
            mLanguages = gson.fromJson(jsonString, new TypeToken<List<Language>>() {
            }.getType());

            return mLanguages;

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
