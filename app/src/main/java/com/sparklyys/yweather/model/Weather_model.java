package com.sparklyys.yweather.model;

/**
 * Created by SparkklyYS on 2017/7/20.
 * Weather bean包含天气预报的各项信息
 */

public class Weather_model {
    private String name ;//城市名称
    private String Country;         //城市所在国家
    private String WeatherText;     //天气描述
    private int WeatherCode;     //天气现象代码
    private String temperature;     //温度
    private String feels_like;      //体感温度
    private String pressure;//气压
    private double humidity;        //相对湿度
    private double visibility;      //可见度
    private String wind_direction;  //风向
    private String wind_direction_degree;   //风向角度
    private String wind_speed;      //风速
    private String wind_scale;      //风力等级
    private   int code;
    private  String push;
    private int pm;
    private String sunset;
    private String sunrise;
    private String air;

    private String car;
    private String tour;
    private String san;
    private String chuan;
    private String details2;
    private String details3;
    private String details4;
    private String details5;
    private String details6;
    private String details;
    private String gan;
    private String yun;

    private String date;            //时间
    private String text_day;        //天气情况
    private String high_temp;       //最高温度
    private String low_temp;        //最低温度

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText_day() {
        return text_day;
    }

    public void setText_day(String text_day) {
        this.text_day = text_day;
    }

    public String getHigh_temp() {
        return high_temp;
    }

    public void setHigh_temp(String high_temp) {
        this.high_temp = high_temp;
    }

    public String getLow_temp() {
        return low_temp;
    }

    public void setLow_temp(String low_temp) {
        this.low_temp = low_temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getWeatherText() {
        return WeatherText;
    }

    public void setWeatherText(String weatherText) {
        WeatherText = weatherText;
    }

    public int getWeatherCode() {
        return WeatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        WeatherCode = weatherCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_direction_degree() {
        return wind_direction_degree;
    }

    public void setWind_direction_degree(String wind_direction_degree) {
        this.wind_direction_degree = wind_direction_degree;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getWind_scale() {
        return wind_scale;
    }

    public void setWind_scale(String wind_scale) {
        this.wind_scale = wind_scale;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public String getChuan() {
        return chuan;
    }

    public void setChuan(String chuan) {
        this.chuan = chuan;
    }

    public String getGan() {
        return gan;
    }

    public void setGan(String gan) {
        this.gan = gan;
    }

    public String getYun() {
        return yun;
    }

    public void setYun(String yun) {
        this.yun = yun;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails2() {
        return details2;
    }

    public void setDetails2(String details2) {
        this.details2 = details2;
    }

    public String getDetails3() {
        return details3;
    }

    public void setDetails3(String details3) {
        this.details3 = details3;
    }

    public String getDetails4() {
        return details4;
    }

    public void setDetails4(String details4) {
        this.details4 = details4;
    }

    public String getDetails5() {
        return details5;
    }

    public void setDetails5(String details5) {
        this.details5 = details5;
    }

    public String getDetails6() {
        return details6;
    }

    public void setDetails6(String details6) {
        this.details6 = details6;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }
}
