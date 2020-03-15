package com.dmtech.iw;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

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

        TextView curTempView = view.findViewById(R.id.tv_cur_temp);
        curTempView.setPadding(0, 0, 0, getVirtualBarHeight(getActivity()));
        TextView updateTimeView = view.findViewById(R.id.tv_update_time);
        updateTimeView.setPadding(0, 0, 0, getVirtualBarHeight(getActivity()));

        return view;
    }

    public String getName() {
        return mName;
    }

    // 获得虚拟按键栏的高度
    private int getVirtualBarHeight(Context context) {
        // 取得全屏高度值
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        // 如果系统版本大于5.0，则获取除虚拟栏之外的高度
        // 系统版本如果在5.0之前，则不存在这个问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        // 二者相减得到虚拟栏高度
        int virtualbarHeight = realHeight - height;
        if (virtualbarHeight < 0) {
            virtualbarHeight = 0;
        }
        return virtualbarHeight;
    }
}
