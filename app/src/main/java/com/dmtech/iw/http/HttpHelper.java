package com.dmtech.iw.http;

public class HttpHelper {
    // 和风天气API key
    private static final String API_KEY = "d2ae781d61744d65a2ef2156eef2cb64";
    //
    private static final String URL = "https://free-api.heweather.net/s6/weather";

    public static String getUrl(String locationId) {
        return URL + "?location=" + locationId + "&key=" + API_KEY;
    }
}
