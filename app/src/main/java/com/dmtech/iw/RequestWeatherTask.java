package com.dmtech.iw;

import android.os.AsyncTask;
import android.util.Log;

import com.dmtech.iw.entity.Weathers;
import com.dmtech.iw.http.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestWeatherTask extends AsyncTask<Void, Void, List<String>> {

    public interface Callback {
        void onPostExecute(List<Weathers> weathers);
    }


    private List<String> locations;
    private Callback callback;

    public RequestWeatherTask(List<String> locations) {
        this.locations = locations;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPostExecute(List<String> results) {
        List<Weathers> weathers = new ArrayList<>();
        for (String s : results) {
            Log.d("iWeather", "Results: " + s);

            Gson gson = new Gson();
            Weathers w = gson.fromJson(s, Weathers.class);
            Log.d("iWeather", "天气：" + w.getHeWeather6().get(0).getBasic().getLocation());
            weathers.add(w);
        }

        if (callback != null) {
            callback.onPostExecute(weathers);
        }
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> results = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            // 生成URL
            String id = locations.get(i);
            String url = HttpHelper.getWeatherUrl(id);
            Log.d("iWeather", "URL: " + url);

            // okHttp访问URL
            OkHttpClient client = new OkHttpClient();
            // 生成访问url的请求对象
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).build();

            try {
                Response response = client.newCall(request).execute();
                if (response != null) {
                    results.add(i, response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
                results.add(i, "");
            }
        }
        return results;
    }


}
