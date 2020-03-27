package com.dmtech.iw.http;

public class HttpHelper {
    //https://free-api.heweather.net/s6/weather?location=CN101010800&key=d2ae781d61744d65a2ef2156eef2cb64
    private static final String URL_FORMAT = "https://free-api.heweather.net/s6/weather?location=%s&key=%s";
    private static final String API_KEY = "d2ae781d61744d65a2ef2156eef2cb64";

    /**
     * 为指定位置生成获取天气数据的URL
     * @param locationId 位置的ID
     * @return 返回locationId对应位置的URL
     */
    public static String getUrl(String locationId) {
        String url = String.format(URL_FORMAT, locationId, API_KEY);
        return url;
    }
}
