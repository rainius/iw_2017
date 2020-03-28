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
import com.dmtech.iw.entity.Weathers;
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
    private Weathers mWeather;
    private String mName;

    private TextView mCurTempView;
    private TextView mUpdateTimeView;
    private TextView mMaxTempView;
    private TextView mMinTempView;
    private ImageView mConditionIcon;
    private TextView mConditionView;
    private ImageView mConditionBg;

    private boolean mIsActivityCreated = false;


    public WeatherFragment() {
        // Required empty public constructor
    }

    public void updateWeather(Weathers weather) {
        this.mWeather = weather;
        weatherToView();
    }

    public Weathers getWeather() {
        return mWeather;
    }

    private void weatherToView() {

        if (mWeather == null || !mIsActivityCreated) {
            Log.d("WF", "weatherToView exit: " + (mWeather == null) + ", " + mIsActivityCreated);
            return;
        }

        Log.d("WF", "weatherToView");

        HeWeather6 w = mWeather.getHeWeather6().get(0);
        mUpdateTimeView.setText(w.getUpdate().getLoc());
        mCurTempView.setText(w.getNow().getTmp() + "°");

        Daily_forecast today = w.getDaily_forecast().get(0);
        mMaxTempView.setText(today.getTmp_max() + "℃");
        mMinTempView.setText(today.getTmp_min()+ "℃");

        mConditionView.setText(w.getNow().getCond_txt());

        String iconUrl = HttpHelper.getIconUrl(w.getNow().getCond_code());
        Log.d("iWeather", "icon: " + iconUrl);
        Glide.with(this).load(iconUrl).into(mConditionIcon);

        String bgUrl = HttpHelper.getBackgroundUrl(w.getNow().getCond_code());
        Log.d("iWeather", "bg: " + bgUrl);
        Glide.with(this)
             .load(bgUrl)
             .placeholder(R.drawable.bg_sample)
             .into(mConditionBg);

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

    public static WeatherFragment newInstance(Weathers weather) {
        WeatherFragment fragment = new WeatherFragment();
        fragment.updateWeather(weather);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }

        Log.d("WF", "onCreate: " + mName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        mUpdateTimeView = view.findViewById(R.id.tv_update_time);
        mMaxTempView = view.findViewById(R.id.tv_max_temp);
        mMinTempView = view.findViewById(R.id.tv_min_temp);
        mConditionIcon = view.findViewById(R.id.ic_condition);
        mConditionView = view.findViewById(R.id.tv_condition);
        mConditionBg = view.findViewById(R.id.iv_condition_bg);

        mCurTempView = view.findViewById(R.id.tv_cur_temp);
        // 自定义字体
        Typeface typeface = Typeface.createFromAsset(
                getActivity().getAssets(),
                "HelveticaNeue-UltraLight.otf");
        mCurTempView.setTypeface(typeface);

        ConstraintLayout infoContainer = view.findViewById(R.id.weather_info_container);
        infoContainer.setPadding(0, 0, 0, getVirtualBarHeight(getActivity()));
        Log.d("WF", "onCreateView: " + mName);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("WF", "onActivityCreated: " + mName);
        mIsActivityCreated = true;
        weatherToView();
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
