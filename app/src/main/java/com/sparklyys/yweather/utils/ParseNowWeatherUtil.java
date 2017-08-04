package com.sparklyys.yweather.utils;

import com.sparklyys.yweather.model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SparklyYS on 2017/7/20.
 * 解析返回的JSON字符串工具类
 * 解析实时天气
 */
public class ParseNowWeatherUtil {

    /**
     * @param jsonString
     * @return
     * @throws Exception
     */
    public Weather_model getInfomation(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultJsonArray = jsonObject.getJSONArray("HeWeather5");
        JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);
        JSONObject location = jsonObject1.getJSONObject("basic");
        JSONObject update=location.getJSONObject("update");

        JSONObject now = jsonObject1.getJSONObject("now");

        JSONObject wind=now.getJSONObject("wind");

        Weather_model nowWeather = new Weather_model();
        nowWeather.setName(location.getString("city"));
        nowWeather.setCountry(location.getString("cnty"));
        JSONObject cond=now.getJSONObject("cond");
        nowWeather.setWeatherText(cond.getString("txt"));
        nowWeather.setWeatherCode(cond.getInt("code"));
        nowWeather.setPush(update.getString("loc"));
        nowWeather.setTemperature(now.getString("tmp"));
        nowWeather.setFeels_like(now.getString("fl"));
        nowWeather.setPressure(now.getString("pres"));
        nowWeather.setVisibility(now.getDouble("vis"));

        nowWeather.setWind_direction(wind.getString("dir"));
        nowWeather.setWind_direction_degree(wind.getString("deg"));
        nowWeather.setWind_speed(wind.getString("spd"));
        nowWeather.setWind_scale(wind.getString("sc"));

        return nowWeather;
    }

}
