package com.dmtech.iw.http;

public class HttpHelper {
    //https://free-api.heweather.net/s6/weather?location=CN101010800&key=d2ae781d61744d65a2ef2156eef2cb64
    private static final String URL_FORMAT =
            "https://free-api.heweather.net/s6/weather?location=%s&key=%s&lang=zh";

    // 位置搜索URL模板
    private static final String SEARCH_LOCATION_URL_FORMAT =
            "https://search.heweather.net/find?location=%s&key=%s&lang=zh";

    private static final String ICON_URL_PREF = "http://jingzbit.cn/iweather/icons/";

    private static final String BACKGROUND_URL_PREF = "http://jingzbit.cn/iweather/img/weather_bg/";

    private static final String API_KEY = "d2ae781d61744d65a2ef2156eef2cb64";

    private static final String SUNNY = "sunny";
    private static final String CLOUDY = "cloudy";
    private static final String OVERCAST = "overcast";
    private static final String RAINY = "rainy";
    private static final String SNOWY = "snowy";


    /**
     * 为指定位置生成获取天气数据的URL
     * @param locationId 位置的ID
     * @return 返回locationId对应位置的URL
     */
    public static String getUrl(String locationId) {
        String url = String.format(URL_FORMAT, locationId, API_KEY);
        return url;
    }

    public static String getIconUrl(String conditionCode) {
        return ICON_URL_PREF + conditionCode + ".png";
    }

    /**
     * 根据天气代码获取对应的背景图
     * @param conditionCode 天气代码
     * @return 背景图URL
     */
    public static String getBackgroundUrl(String conditionCode) {
        // 1. 将天气代码映射到图片名字
        String name = "";
        char head = conditionCode.charAt(0);
        if (head == '1') {
            switch (conditionCode) {
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
            }
        } else if (head == '3') {
            name = RAINY;
        } else if (head == '4') {
            name = SNOWY;
        } else {
            name = SUNNY;
        }

        return BACKGROUND_URL_PREF + name + ".jpg";
    }

    public static String getSearchLocationUrl(String keyword) {
        return String.format(SEARCH_LOCATION_URL_FORMAT, keyword, API_KEY);
    }
}
