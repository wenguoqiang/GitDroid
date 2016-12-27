package com.feicuiedu.gitdroid.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.feicuiedu.gitdroid.R;
import com.feicuiedu.gitdroid.Utils.ActivityUtils;
import com.feicuiedu.gitdroid.favorite.FavoriteFragment;
import com.feicuiedu.gitdroid.gank.GankFragment;
import com.feicuiedu.gitdroid.hotRepo.HotRepoFragment;
import com.feicuiedu.gitdroid.hotUser.HotUserFragment;
import com.feicuiedu.gitdroid.login.model.UserRepo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigationView)
    NavigationView mNavigationView;// 侧滑菜单
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;// 抽屉效果(侧滑，里面包括两个布局：1. 主页面显示，2. 侧滑的布局，一般结合NavigationView使用)
    private Button mBtnLogin;
    private ImageView mIvIcon;
    private ActivityUtils mActivityUtils;

    //切换的Fragment
    private HotRepoFragment mHotRepoFragment;
    private GankFragment mGankFragment;
    private HotUserFragment hotUserFragment;
    private FavoriteFragment favoriteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置当前视图(更改了当前视图内容,将导致onContentChanged方法触发)
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mActivityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

        /**
         * 需要处理的视图
         * 1. toolbar：主题是没有ActionBar，所以展示的时候toolbar作为actionBar展示
         * 2. DrawerLayout：
         * 3. NavigationView
         */

        //设置ActionBar
        setSupportActionBar(mToolbar);

        // 设置监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();// 同步状态
        // 设置DrawerLayout的侧滑监听
        mDrawerLayout.addDrawerListener(toggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        mBtnLogin = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id.btnLogin);
        mIvIcon = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id.ivIcon);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityUtils.startActivity(LoginActivity.class);
                finish();
            }
        });

        // 在主页面，默认显示的是热门仓库Fragment
        mHotRepoFragment = new HotRepoFragment();
        replaceFragment(mHotRepoFragment);
    }

    /**
     * 1. 创建Fragment
     * 2. 切换Fragment:提供一个方法，根据传入的Fragment来进行切换
     * 3. 展示：1. 默认展示 2. 切换时
     *
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    //主要做了我们基本登录信息的改变
    @Override
    protected void onStart() {
        super.onStart();
        // 判断用户信息是不是为空
        if (UserRepo.isEmpty()){
            mBtnLogin.setText(R.string.login_github);
            return;
        }
        // 改成切换账号
        mBtnLogin.setText(R.string.switch_account);
        // 设置toolbar的标题
        getSupportActionBar().setTitle(UserRepo.getUser().getLogin());
        // 设置头像信息
        /**
         * Picasso进行图片的展示
         * 1. 添加依赖：compile 'com.squareup.picasso:picasso:2.5.2'
         * 2. 使用
         */
        Picasso.with(this)
                .load(UserRepo.getUser().getAvatar())// 加载图片
                .into(mIvIcon);// 作用到谁身上

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.isChecked()){
            item.setChecked(false);
        }

        // TODO: 2016/12/1 我的收藏页面切换视图

        switch (item.getItemId()){

            // 最热门
            case R.id.github_hot_repo:
                if (mHotRepoFragment.isAdded()){
                    replaceFragment(mHotRepoFragment);
                }
                break;
            // 开发者
            case R.id.github_hot_coder:
                if (hotUserFragment == null) hotUserFragment = new HotUserFragment();
                if (!hotUserFragment.isAdded()) {
                    replaceFragment(hotUserFragment);
                }
                break;
            // 我的收藏
            case R.id.arsenal_my_repo:
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                }
                if (!favoriteFragment.isAdded()) {
                    replaceFragment(favoriteFragment);
                }
                break;
            // 每日干货
            case R.id.tips_daily:
                if (mGankFragment == null) {
                    mGankFragment = new GankFragment();
                }
                if(!mGankFragment.isAdded()){
                    replaceFragment(mGankFragment);
                }
                break;
        }

        // 选择某一项之后，切换Fragment，关闭抽屉
        mDrawerLayout.closeDrawer(GravityCompat.START);

        // 返回true,代表该菜单项已经被选择
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
