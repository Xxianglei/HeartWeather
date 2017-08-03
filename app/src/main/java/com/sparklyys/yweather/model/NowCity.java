package com.sparklyys.yweather.model;

import android.app.Application;

/**
 * Created by 丽丽超可爱 on 2017/7/26.
 */

public class NowCity extends Application {


    private  String city;

    public  String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
