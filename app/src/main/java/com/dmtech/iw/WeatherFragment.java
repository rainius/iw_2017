package com.dmtech.iw;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmtech.iw.entity.Daily_forecast;
import com.dmtech.iw.entity.HeWeather6;
import com.dmtech.iw.entity.Weather;
import com.dmtech.iw.http.HttpHelper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String mName;
    // 当前气温
    private TextView mCurTempView;
    // 更新时间
    private TextView mUpdateTimeView;
    // 最高气温
    private TextView mMaxTempView;
    // 最低气温
    private TextView mMinTempView;
    // 当前天气情况图标
    private ImageView mConditionIcon;
    // 当前天气情况文字描述
    private TextView mConditionView;
    // 天气背景图
    private ImageView mBackground;

    // 天气数据对象
    private Weather mWeather;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name name of fragment.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String name) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public static WeatherFragment newInstance(Weather weather) {
        WeatherFragment fragment = new WeatherFragment();
        fragment.updateWeather(weather);
        return fragment;
    }

    /**
     * 获取天气实体对象
     * @return 当前Fragment持有的天气实体对象
     */
    public Weather getWeather() {
        return mWeather;
    }

    private void updateWeather(Weather weather) {
        mWeather = weather;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mCurTempView = view.findViewById(R.id.tv_cur_temp);
        mUpdateTimeView = view.findViewById(R.id.tv_update_time);
        mMaxTempView = view.findViewById(R.id.tv_max_temp);
        mMinTempView = view.findViewById(R.id.tv_min_temp);
        mConditionIcon = view.findViewById(R.id.ic_condition);
        mConditionView = view.findViewById(R.id.tv_condition);
        mBackground = view.findViewById(R.id.iv_condition_bg);

        bindWeatherToViews();

        // 自定义字体
        Typeface typeface = Typeface.createFromAsset(
                getActivity().getAssets(),
                "HelveticaNeue-UltraLight.otf");
        mCurTempView.setTypeface(typeface);

        ConstraintLayout infoContainer = view.findViewById(R.id.weather_info_container);
        infoContainer.setPadding(0, 0, 0, getVirtualBarHeight(getActivity()));

        return view;
    }

    // 将天气数据绑定到UI
    private void bindWeatherToViews() {

        if (mWeather == null) {
            return;
        }

        HeWeather6 hw6 = mWeather.getHeWeather6().get(0);
        mUpdateTimeView.setText(hw6.getUpdate().getLoc());
        mCurTempView.setText(hw6.getNow().getTmp() + "°");

        Daily_forecast df = hw6.getDaily_forecast().get(0);
        mMaxTempView.setText(df.getTmp_max() + "℃");
        mMinTempView.setText(df.getTmp_min() + "℃");

        mConditionView.setText(hw6.getNow().getCond_txt());

        Glide.with(this)
                .load(HttpHelper.getIconUrl(hw6.getNow().getCond_code()))
                .into(mConditionIcon);

        Glide.with(this)
                .load(HttpHelper.getBackgroundUrl(hw6.getNow().getCond_code()))
                .into(mBackground);

    }


    public String getName() {
        return mName;
    }

    // 获取虚拟按键栏高度
    private int getVirtualBarHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        }

        int realHeight = dm.heightPixels;
        // 计算虚拟按键栏高度
        int virtualbarHeight = realHeight - height;

        if (virtualbarHeight < 0) {
            virtualbarHeight = 0;
        }

        return virtualbarHeight;
    }

}
