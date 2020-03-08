package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<WeatherFragment> mFragments = new ArrayList<>();

    private ViewPager mPager;
    private WeatherPagerAdapter mAdapter;

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
           Log.d("iWeather", "onPageScrolled");
        }

        @Override
        public void onPageSelected(int position) {
            Log.d("iWeather", "onPageSelected: " + position);
            WeatherFragment wf = mFragments.get(position);
            wf.updateNameView();
            mToolbar.setTitle(wf.getName());
            mToolbar.setSubtitle(wf.getName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
           Log.d("iWeather", "onPageScrollStateChanged");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);

        // 用Toolbar代替Actionbar
        mToolbar = findViewById(R.id.main_toolbar);
        this.setSupportActionBar(mToolbar);

        mDrawer = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawer, mToolbar, R.string.open, R.string.close);

        mDrawer.addDrawerListener(mDrawerToggle);

        // 仅测试用，后期删除
        fillTestFragment();

        mPager = findViewById(R.id.viewpager);
        mAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), 1);
        mAdapter.setFragments(mFragments);
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WeatherFragment wf = mFragments.get(mPager.getCurrentItem());
        wf.updateNameView();
        mToolbar.setTitle(wf.getArguments().getString(WeatherFragment.ARG_NAME));
        mToolbar.setSubtitle(wf.getArguments().getString(WeatherFragment.ARG_NAME));
    }

    // 仅前期测试用
    private void fillTestFragment() {
        mFragments.add(WeatherFragment.newInstance("北京"));
        mFragments.add(WeatherFragment.newInstance("武汉"));
        mFragments.add(WeatherFragment.newInstance("伦敦"));
        mFragments.add(WeatherFragment.newInstance("东京"));
        mFragments.add(WeatherFragment.newInstance("西雅图"));

        for (WeatherFragment f : mFragments) {
            Log.d("iWeather", "已添加：" + f.getArguments().getString(WeatherFragment.ARG_NAME));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //依据菜单资源文件描述创建菜单对象
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 使抽屉按钮状态与抽屉视图的开闭状态一致
        mDrawerToggle.syncState();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.menu_item_add) {
            Toast.makeText(this, "增加新位置", Toast.LENGTH_SHORT).show();
            Log.d("iWeather", "增加新位置");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
