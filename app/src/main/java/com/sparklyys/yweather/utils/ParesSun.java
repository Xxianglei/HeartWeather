package com.sparklyys.yweather.utils;

import com.sparklyys.yweather.model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 丽丽超可爱 on 2017/8/2.
 */

public class ParesSun {
    // 获取 日出日落
    public Weather_model sunInfomation(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultJsonArray = jsonObject.getJSONArray("results");
        JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);

        JSONArray sunshuzu = jsonObject1.getJSONArray("sun");
        JSONObject jsonObject2=sunshuzu.getJSONObject(0);
        Weather_model sun = new Weather_model();
        sun.setSunrise(jsonObject2.getString("sunrise"));
        sun.setSunset(jsonObject2.getString("sunset"));
        return sun;
    }
}
