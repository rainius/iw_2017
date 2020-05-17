package com.dmtech.iw;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmtech.iw.entity.Basic;
import com.dmtech.iw.entity.SearchInfos;
import com.dmtech.iw.entity.SearchResult;
import com.dmtech.iw.http.HttpHelper;
import com.dmtech.iw.model.DaoSession;
import com.dmtech.iw.model.LocationModel;
import com.dmtech.iw.model.LocationModelDao;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import event.MessageEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAddActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText mKeywordEdit;  // 搜索框
    private TextView mCancelBtn;    // 退出按钮

    private ListView mLocationList; //待选地 址列表
    private ArrayAdapter mAdapter;

    private List<String> mLocationLabels;   // 位置名称列表
    private List<Basic> mLocations;         // 位置对象列表

    private Handler mHandler = new Handler();
    private Runnable mStartSearch = new Runnable() {
        @Override
        public void run() {
            Log.d("iWeather", "Start search: " + mKeywordEdit.getText().toString());
            SearchLocationsTask searchTask = new SearchLocationsTask();
            searchTask.execute();
        }
    };

    // 文本输入看守
    private TextWatcher mKeywordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("iWeather", "onTextChanged: " + s);
            // 控制流量
            mHandler.removeCallbacks(mStartSearch);               // 去掉没有执行的任务
            mHandler.postDelayed(mStartSearch, 800);    // 延迟800毫秒执行任务
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add);

        mKeywordEdit = findViewById(R.id.edit_keyword);
        // 设定文本输入看守
        mKeywordEdit.addTextChangedListener(mKeywordWatcher);

        mCancelBtn = findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(this);

        // 搜索结果列表、适配器及内容关联
        mLocationList = findViewById(R.id.listview);
        mLocationLabels = new ArrayList<>();
        mLocations = new ArrayList<>();

        mAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mLocationLabels);
        mLocationList.setAdapter(mAdapter);
        mLocationList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:   // 点击取消即退出页面
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("iWeather", "onItemClick: " + mLocationLabels.get(position));
        Basic location = mLocations.get(position);
        //将location对象转换为LocationModel对象以存储到数据库
        LocationModel model = LocationModel.fromBasic(location);
        Toast.makeText(this, model.getCid() + ":" + model.getLocation(), Toast.LENGTH_SHORT).show();
        // 保存model到数据库
        SaveLocationTask saveTask = new SaveLocationTask(model);
        saveTask.execute();
    }

    // 异步保存到数据库
    private class SaveLocationTask extends AsyncTask<Void, Void, LocationModel> {

        private LocationModel locationModel;

        public SaveLocationTask(LocationModel locationModel) {
            this.locationModel = locationModel;
        }

        @Override
        protected LocationModel doInBackground(Void... voids) {
            DaoSession session = ((iWeatherApp) getApplication()).getDaoSession();
            LocationModelDao dao = session.getLocationModelDao();
            dao.insert(locationModel);
            Log.d("iWeather", "Insert location: " + locationModel.getId());
            return locationModel;
        }

        @Override
        protected void onPostExecute(LocationModel locationModel) {
            EventBus.getDefault().post(new MessageEvent());
            finish();
        }
    }

    // 根据搜索框中输入的文字查询匹配的位置
    private class SearchLocationsTask extends AsyncTask<String, Void, String> {

        private String keyword;

        @Override
        protected void onPreExecute() {
            keyword = mKeywordEdit.getText().toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            // 未输入关键字
            if (TextUtils.isEmpty(keyword)) {
                return result;
            }

            // 根据keyword发起搜索请求
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            String url = HttpHelper.getSearchLocationUrl(keyword);
            Log.d("iWeather", "Search URL: " + url);

            Request request = builder.url(url).build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // 清空旧数据
            mLocationLabels.clear();
            mLocations.clear();

            Log.d("iWeather", "result: " + s);
            // JSON -> Java Bean
            if (TextUtils.isEmpty(s)) {
                mAdapter.notifyDataSetChanged();
                return;
            }

            Gson gson = new Gson();
            SearchResult searchResult = gson.fromJson(s, SearchResult.class);
            SearchInfos searchInfos = searchResult.getInfos().get(0);
            if ("ok".equalsIgnoreCase(searchInfos.getStatus())) {   //能够查询到结果才处理
                Log.d("iWeather", "search results: " + searchInfos.getBasics().size());
                // 为搜索结果分别生成显示标签
                for (Basic b : searchInfos.getBasics()) {
                    String label = b.getLocation() + ", " + b.getAdmin_area() + ", " + b.getCnty();
                    mLocationLabels.add(label);
                    mLocations.add(b);
                }
            }

            // 通知列表更新
            mAdapter.notifyDataSetChanged();
        }
    }

}
