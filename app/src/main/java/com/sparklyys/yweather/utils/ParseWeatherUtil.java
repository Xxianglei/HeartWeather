package com.sparklyys.yweather.utils;

import android.util.Log;

import com.sparklyys.yweather.model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SparklyYS on 2017/7/21.
 * 解析返回的JSON字符串工具类
 * 解析实时天气
 * 解析天气列表
 */

public class ParseWeatherUtil {

    public List<Weather_model> getInfomation(String jsonString) throws Exception {
        List<Weather_model> WeatherList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultJsonArray = jsonObject.getJSONArray("results");
        JSONObject newJsonObject = resultJsonArray.getJSONObject(0);
        JSONArray jsonArray = newJsonObject.getJSONArray("daily");

        Log.i("num", String.valueOf(jsonArray.length()));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            Weather_model newWeather = new Weather_model();
            newWeather.setDate(jsonObject1.getString("date"));
            newWeather.setText_day(jsonObject1.getString("text_day"));
            newWeather.setWeatherCode(jsonObject1.getInt("code_day"));
            newWeather.setLow_temp(jsonObject1.getString("low"));
            newWeather.setHigh_temp(jsonObject1.getString("high"));
            WeatherList.add(newWeather);
        }
        return WeatherList;
    }
}
