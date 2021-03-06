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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dmtech.iw.entity.HeWeather6;
import com.dmtech.iw.entity.Weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RequestWeatherTask.Callback {

    private static final String[] LOCATION_IDS = {
            "CN101010800",
            "CN101131012",
            "CN101310304",
            "US3290097",
            "AU2147714"
    };

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private WeatherPagerAdapter mPagerAdapter;

    private RelativeLayout mLoadingView;

    private List<WeatherFragment> mFragments = new ArrayList<>();

    private void fillTestFragments() {
        mFragments.add(WeatherFragment.newInstance("北京"));
        mFragments.add(WeatherFragment.newInstance("武汉"));
        mFragments.add(WeatherFragment.newInstance("伦敦"));
        mFragments.add(WeatherFragment.newInstance("东京"));
        mFragments.add(WeatherFragment.newInstance("西雅图"));

        for (WeatherFragment f : mFragments) {
            Log.d("iWeather",
                    "添加城市: " + f.getArguments().getString(WeatherFragment.ARG_NAME));
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("iWeather", "onPageSelected: " + position);
            updateTitle();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);

        mToolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mDrawer=findViewById(R.id.drawer);
        //创建抽屉按钮
        mDrawerToggle=new ActionBarDrawerToggle(this,
                mDrawer,
                mToolbar,
                R.string.open,
                R.string.close);

        mDrawer.addDrawerListener(mDrawerToggle);

        //填充测试数据，将来删除
        //fillTestFragments();

        mViewPager = findViewById(R.id.viewpager);
        mPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), 1);
        mPagerAdapter.setFragments(mFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mLoadingView = findViewById(R.id.loading_weather_container);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        WeatherFragment f = mFragments.get(mViewPager.getCurrentItem());
//        String title = f.getArguments().getString(WeatherFragment.ARG_NAME);
//        mToolbar.setTitle(title);
//        mToolbar.setSubtitle(title);

        RequestWeatherTask task = new RequestWeatherTask(Arrays.asList(LOCATION_IDS));
        task.setCallback(this);
        task.execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.menu_item_add) {
            Toast.makeText(this, "增加新位置", Toast.LENGTH_SHORT).show();
            Log.d("iWeather", "增加新位置");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPreExecute() {
        // 开始加载数据，显示等待视图
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(List<Weather> weathers) {
        Log.d("iWeather", "收到天气数据对象：" + weathers.size());

        mFragments.clear();
        for (int i = 0; i < weathers.size(); i++) {
            Weather weather = weathers.get(i);
            WeatherFragment wf = WeatherFragment.newInstance(weather);
            mFragments.add(wf);
        }

        // 刷新ViewPager
        mPagerAdapter.setFragments(mFragments);
        mPagerAdapter.notifyDataSetChanged();

        // 更新标题文字(主标题：具体位置，副标题：上级区域+国家)
        updateTitle();

        // 加载完成，隐藏等待视图
        mLoadingView.setVisibility(View.GONE);
    }

    private void updateTitle() {
        // 当前显示哪个页面？
        int pos = mViewPager.getCurrentItem();
        WeatherFragment wf = mFragments.get(pos);
        HeWeather6 hw6 = wf.getWeather().getHeWeather6().get(0);
        String title = hw6.getBasic().getLocation();
        String subtitle = hw6.getBasic().getAdmin_area() + ", " + hw6.getBasic().getCnty();
        mToolbar.setTitle(title);
        mToolbar.setSubtitle(subtitle);
    }
}
