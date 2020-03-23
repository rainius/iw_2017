package com.dmtech.iw;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


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
    private TextView mCurTempView;

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
        // 自定义字体
        Typeface typeface = Typeface.createFromAsset(
                getActivity().getAssets(),
                "HelveticaNeue-UltraLight.otf");
        mCurTempView.setTypeface(typeface);

        ConstraintLayout infoContainer = view.findViewById(R.id.weather_info_container);
        infoContainer.setPadding(0, 0, 0, getVirtualBarHeight(getActivity()));

        return view;
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
