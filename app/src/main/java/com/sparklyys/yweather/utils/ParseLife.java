package com.sparklyys.yweather.utils;

import com.sparklyys.yweather.model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 丽丽超可爱 on 2017/8/2.
 */

public class ParseLife {
    // 生活指数
    public Weather_model getLifeInfomation(String jsonString) throws Exception {

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultJsonArray = jsonObject.getJSONArray("HeWeather5");
        JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);
        JSONObject suggestion = jsonObject1.getJSONObject("suggestion");

        JSONObject car_washing= suggestion.getJSONObject("cw");

        Weather_model life = new Weather_model();
        life.setCar(car_washing.getString("brf"));
        life.setDetails(car_washing.getString("txt"));

        JSONObject dressing= suggestion.getJSONObject("drsg");
        life.setChuan(dressing.getString("brf"));
        life.setDetails2(dressing.getString("txt"));

        JSONObject flu= suggestion.getJSONObject("flu");
        life.setGan(flu.getString("brf"));
        life.setDetails3(flu.getString("txt"));

        JSONObject sport= suggestion.getJSONObject("sport");
        life.setYun(sport.getString("brf"));
        life.setDetails4(sport.getString("txt"));
//  旅游改 紫外线
        JSONObject travel= suggestion.getJSONObject("uv");
        life.setTour(travel.getString("brf"));
        life.setDetails5(travel.getString("txt"));
//  带伞改 舒适度
        JSONObject umbrella= suggestion.getJSONObject("comf");
        life.setSan(umbrella.getString("brf"));
        life.setDetails6(umbrella.getString("txt"));
        return life;
    }
}
