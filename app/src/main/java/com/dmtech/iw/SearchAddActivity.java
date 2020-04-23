package com.dmtech.iw;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dmtech.iw.entity.SearchInfos;
import com.dmtech.iw.entity.SearchResult;
import com.dmtech.iw.http.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mKeywordEdit;  // 搜索框
    private TextView mCancelBtn;    // 退出按钮
    private ListView mLocationList; // 待选地址列表

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

        mLocationList = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:   // 点击取消即退出页面
                finish();
                break;
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
            Log.d("iWeather", "result: " + s);
            // JSON -> Java Bean
            if (TextUtils.isEmpty(s)) {
                return;
            }

            Gson gson = new Gson();
            SearchResult searchResult = gson.fromJson(s, SearchResult.class);
            SearchInfos searchInfos = searchResult.getInfos().get(0);
            if ("ok".equalsIgnoreCase(searchInfos.getStatus())) {   //能够查询到结果才处理
                Log.d("iWeather", "search results: " + searchInfos.getBasics().size());
            }
        }
    }
}
