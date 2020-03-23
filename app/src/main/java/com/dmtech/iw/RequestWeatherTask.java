package com.dmtech.iw;

import android.os.AsyncTask;
import android.util.Log;

import com.dmtech.iw.http.HttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestWeatherTask extends AsyncTask<Void, Void, List<String>> {

    private List<String> locationIds;

    public RequestWeatherTask(String[] locations) {
        locationIds = Arrays.asList(locations);
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> results = new ArrayList<>();

        for (int i = 0; i < locationIds.size(); i++) {
            String id = locationIds.get(i);
            String url = HttpHelper.getUrl(id);
            Log.d("iWeather", "request url: " + url);
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                if (response != null) {
                    results.add(i, response.body().string());
                } else {
                    results.add(i, "");
                }
            } catch (IOException e) {
                e.printStackTrace();
                results.add(i, "");
            }
        }
        return results;
    }


    @Override
    protected void onPostExecute(List<String> results) {
        for (String s : results) {
            Log.d("iWeather", "result: " + s);
        }
    }
}
