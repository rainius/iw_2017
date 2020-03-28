package com.dmtech.iw.http;

public class HttpHelper {
    //https://free-api.heweather.net/s6/weather?location=CN101010800&key=d2ae781d61744d65a2ef2156eef2cb64
    private static final String WEATHER_URL_FORMAT = "https://free-api.heweather.net/s6/weather?location=%s&key=%s&lang=zh";
    private static final String API_KEY = "d2ae781d61744d65a2ef2156eef2cb64";

    private static final String RES_URL_PREFIX = "http://jingzbit.cn/iweather/";

    private static final String CLOUDY = "cloudy";
    private static final String OVERCAST = "overcast";
    private static final String RAINY = "rainy";
    private static final String SNOWY = "snowy";
    private static final String SUNNY = "sunny";

    /**
     * 为指定位置生成获取天气数据的URL
     * @param locationId 位置的ID
     * @return 返回locationId对应位置的URL
     */
    public static String getWeatherUrl(String locationId) {
        String url = String.format(WEATHER_URL_FORMAT, locationId, API_KEY);
        return url;
    }

    public static String getIconUrl(String code) {
        return RES_URL_PREFIX + "icons/" + code + ".png";
    }


    public static String getBackgroundUrl(String condCode) {
        String name = "";
        char head = condCode.charAt(0);
        if (head == '1') {
            switch (condCode) {
                case "100":
                    name = SUNNY;
                    break;
                case "101":
                case "102":
                case "103":
                    name = CLOUDY;
                    break;
                case "104":
                    name = OVERCAST;
                    break;
            }
        } else if (head == '3') {
            name = RAINY;
        } else if (head == '4') {
            name = SNOWY;
        } else {
            name = SUNNY;
        }
        return RES_URL_PREFIX + "img/weather_bg/" + name + ".jpg";
    }
}
