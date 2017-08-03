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
        JSONArray resultJsonArray = jsonObject.getJSONArray("results");
        JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);
        JSONObject location = jsonObject1.getJSONObject("location");
        JSONObject now = jsonObject1.getJSONObject("now");
        Weather_model nowWeather = new Weather_model();
        nowWeather.setName(location.getString("name"));
        nowWeather.setCountry(location.getString("country"));
        nowWeather.setWeatherText(now.getString("text"));
        nowWeather.setWeatherCode(now.getInt("code"));
        nowWeather.setTemperature(now.getString("temperature"));
        nowWeather.setFeels_like(now.getString("feels_like"));
        nowWeather.setPressure(now.getString("pressure"));
        nowWeather.setVisibility(now.getDouble("visibility"));
        nowWeather.setWind_direction(now.getString("wind_direction"));
        nowWeather.setWind_direction_degree(now.getString("wind_direction_degree"));
        nowWeather.setWind_speed(now.getString("wind_speed"));
        nowWeather.setWind_scale(now.getString("wind_scale"));

        return nowWeather;
    }

}
