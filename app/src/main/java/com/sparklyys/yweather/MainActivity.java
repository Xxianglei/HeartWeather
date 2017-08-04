package com.sparklyys.yweather;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


import com.sparklyys.yweather.StepArcView.StepArcView;
import com.sparklyys.yweather.SunView.SunriseView;
import com.sparklyys.yweather.Toast.ToastUtils;
import com.sparklyys.yweather.model.Weather_model;
import com.sparklyys.yweather.utils.HttpDownloader;
import com.sparklyys.yweather.utils.NetUtil;
import com.sparklyys.yweather.utils.ParesSun;
import com.sparklyys.yweather.utils.ParseLife;
import com.sparklyys.yweather.utils.ParseNowWeatherUtil;
import com.sparklyys.yweather.utils.ParsePm;
import com.sparklyys.yweather.utils.ParseWeatherUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements AMapLocationListener, SwipeRefreshLayout.OnRefreshListener {

    //实时天气情况信息
    //AMap是地图对象
    private static final int REFRESH_COMPLETE = 0X110;
    private SwipeRefreshLayout mSwipeLayout;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器

    private Weather_model sun;
    private Weather_model life;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private List<Weather_model> WeatherList = new ArrayList<>();
    private String string;
    private SharedPreferences sharedPreferences;
    private int code;
    private TextView pressure1;
    private TextView winds;
    private TextView pressure;
    private TextView feels_like;
    private TextView visibility;
    private ImageView tianqi;
    public String city;
    private String city1 = "jilin";
    private String city2;
    private String city6;

    //天气信息控件
    //今天
    private TextView nowDateTime;
    private TextView nowWeatherInfo;
    private TextView nowTempRange;
    private ImageView onexiao;
    private ImageView twoxiao;
    private ImageView threexiao;
    private ImageView fourxiao;
    //明天
    private TextView twoDateTime;
    private TextView twoWeatherInfo;
    private TextView twoTempRange;

    //后天
    private TextView threeDateTime;
    private TextView threeWeatherInfo;
    private TextView threeTempRange;

    private TextView fourDateTime;
    private TextView fourWeatherInfo;
    private TextView fourTempRange;


    private Weather_model nowWeather;
    private Weather_model pm;
    private TextView pm1;

    //UI控件
    private Toolbar toolbar;              //工具栏
    private TextView title_temp;          //toolbar上显示的温度
    private TextView main_location;       //所在城市
    private TextView main_temp;           //所在城市的实时温度
    private TextView main_info;           //所在城市的实时天气描述
    private String string1;
    private String city5;
    private ImageButton location;
    private StringBuffer buffer;
    private StepArcView sv;
    private String imagePath;
    private int[] path = new int[]{R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four, R.mipmap.ll};
    //异步更新UI控件中的当前天气信息
    private Handler mmHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    location();
                    readBCperfernces();
                    saveBCperfernces( new Random().nextInt(path.length) );

                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };
    private TextView push;
    private ImageButton fenx;

    private void readBCperfernces() {

        SharedPreferences sharedPreferences = getSharedPreferences("BC",
                MODE_PRIVATE);
        int BC = sharedPreferences.getInt("BCG", 0);
        relativeLayout.setBackgroundResource(path[BC]);
    }

    private void saveBCperfernces(int shu) {
        SharedPreferences mySharedPreferences = getSharedPreferences("BC", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putInt("BCG",shu);
        editor.commit();
    }

    private ProgressDialog dialog;
    private WindPath mBigWindMill;
    private WindPath mSmallWindMill;
    private SunriseView mSunriseView;
    private TextView down1;
    private TextView up1;
    private TextView time;
    private TextView car1;
    private TextView car2;
    private TextView tour1;
    private TextView tour2;
    private TextView san1;
    private TextView san2;
    private TextView chuan1;
    private TextView chuan2;
    private TextView gan1;
    private TextView gan2;
    private TextView yun1;
    private TextView yun2;
    private RelativeLayout relativeLayout;


    public void onRefresh() {

        mmHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }


    private Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 0x12) {   //实况
                try {
                    String jsonString = (String) msg.obj;
                    ParseNowWeatherUtil parseNowWeatherUtil = new ParseNowWeatherUtil();
                    nowWeather = parseNowWeatherUtil.getInfomation(jsonString);
                    Log.i("天气", nowWeather.getName());
                    //先需要下载数据
                    saveperfernces(city, nowWeather.getTemperature().toString() + "°", nowWeather.getWeatherText().toString(), nowWeather.getTemperature().toString() + "°", "体感温度" + nowWeather.getFeels_like().toString() + "°", "能见度" + nowWeather.getVisibility() + "千米", " " + nowWeather.getWind_direction() + " " + nowWeather.getWind_scale() , nowWeather.getWeatherCode(), " 气压" + nowWeather.getPressure().toString() + "百帕",nowWeather.getPush().toString()+"发布");
                    readperfernces();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x11) {
                try {
                    String jsonString = (String) msg.obj;
                    ParseWeatherUtil parseWeatherUtil = new ParseWeatherUtil();
                    clearWeatherList();
                    WeatherList = parseWeatherUtil.getInfomation(jsonString);
                    //设置数据
                    setDatas();
                    readfore();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x13) {
                try {
                    String jsonString = (String) msg.obj;
                    ParsePm parsePm = new ParsePm();
                    pm = parsePm.getpmInfomation(jsonString);
                    Log.i("空气质量", pm.getAir());
                    //先需要下载数据
                    savePmperfernces(pm.getPm(), pm.getAir());
                    readPmperfernces();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //
            if (msg.what == 0x14) {
                try {
                    String jsonString = (String) msg.obj;
                    ParesSun paresSun = new ParesSun();
                    sun = paresSun.sunInfomation(jsonString);
                    Log.i("sun", sun.getSunrise());
                    //先需要下载数据
                    saveSunperfernces(sun.getSunrise(), sun.getSunset());
                    readSunperfernces();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (msg.what == 0x20) {

                relativeLayout.setBackgroundResource((path[msg.arg1]));
                super.handleMessage(msg);
            }


            if (msg.what == 0x15) {
                try {
                    String jsonString = (String) msg.obj;
                    ParseLife parseLife = new ParseLife();
                    life = parseLife.getLifeInfomation(jsonString);
                    Log.i("开车指数", life.getCar());
                    //先需要下载数据
                    saveLifeperfernces(life.getCar(), life.getDetails(), life.getChuan(), life.getDetails2(), life.getGan(), life.getDetails3(), life.getYun(), life.getDetails4(), life.getTour(), life.getDetails5(), life.getSan(), life.getDetails6());
                    readLifeperfernces();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void saveLifeperfernces(String car, String details, String chuan, String details2, String gan, String details3, String yun, String details4, String tour, String details5, String san, String details6) {

        SharedPreferences mySharedPreferences = getSharedPreferences("life", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("car", car);
        editor.putString("details", details);

        editor.putString("chuan", chuan);
        editor.putString("details2", details2);

        editor.putString("gan", gan);
        editor.putString("details3", details3);

        editor.putString("yun", yun);
        editor.putString("details4", details4);

        editor.putString("tour", tour);
        editor.putString("details5", details5);

        editor.putString("san", san);
        editor.putString("details6", details6);

        //提交当前数据
        editor.commit();

        Log.e("保存成功", car + details);

    }

    private void readLifeperfernces() {
        SharedPreferences sharedPreferences = getSharedPreferences("life",
                MODE_PRIVATE);

        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        String car = sharedPreferences.getString("car", "");
        String details = sharedPreferences.getString("details", "");
        car1.setText(car);
        car2.setText("今天" + details + "洗车");

        String chuan = sharedPreferences.getString("chuan", "");
        String details2 = sharedPreferences.getString("details2", "");
        chuan1.setText(chuan);
        chuan2.setText("今天天气" + details2);

        String gan = sharedPreferences.getString("gan", "");
        String details3 = sharedPreferences.getString("details3", "");
        gan1.setText(gan);
        gan2.setText("感冒" + details3);

        String yun = sharedPreferences.getString("yun", "");
        String details4 = sharedPreferences.getString("details4", "");
        yun1.setText(yun);
        yun2.setText("今天" + details4 + "运动");

        String tour = sharedPreferences.getString("tour", "");
        String details5 = sharedPreferences.getString("details5", "");
        tour1.setText(tour);
        tour2.setText("今天" + details5 + "旅行");

        String san = sharedPreferences.getString("san", "");
        String details6 = sharedPreferences.getString("details6", "");
        san1.setText(san);
        san2.setText(details6);
    }

    // 日出日落
    private void readSunperfernces() {
        SharedPreferences sharedPreferences = getSharedPreferences("sun",
                MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        String sunset = sharedPreferences.getString("sunset", "");
        String sunrise = sharedPreferences.getString("sunrise", "");
        up1.setText(sunrise);
        down1.setText(sunset);
    }

    private void saveSunperfernces(String sunrise, String sunset) {

        SharedPreferences mySharedPreferences = getSharedPreferences("sun", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("sunset", sunset);
        editor.putString("sunrise", sunrise);
        //提交当前数据
        editor.commit();
        Log.e("保存成功", sunset);
    }

    private void readPmperfernces() {
        SharedPreferences sharedPreferences = getSharedPreferences("pm",
                MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        String air = sharedPreferences.getString("AIR", "");
        int pm = sharedPreferences.getInt("PM", 0);
        pm1.setText("空气" + air);
        sv = (StepArcView) findViewById(R.id.sv);
        sv.setCurrentCount(200, pm, air);
        //  修改实时天气图标
    }

    private void savePmperfernces(int pm, String air) {

        SharedPreferences mySharedPreferences = getSharedPreferences("pm", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putInt("PM", pm);
        editor.putString("AIR", air);
        //提交当前数据
        editor.commit();
        Log.e("保存成功", air);


    }


    private class WeatherThread implements Runnable {

        private String city = "jilin";

        public WeatherThread(String city) {
            this.city = city;

        }

        public void run() {
            try {
                city1 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }

             // https://free-api.heweather.com/v5/now?city=jilin&key=f75021d48c674f89b3928c2524644ac8
            String address = "https://free-api.heweather.com/v5/now?city=" + city1 + "&key=f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            //打印weather info
            Log.w("cn", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x12;
            mHandler.sendMessage(message);
        }
    }

    private class WeatherpmThread implements Runnable {

        private String city = "jilin";

        public WeatherpmThread(String city) {
            this.city = city;

        }

        public void run() {
            try {
                city1 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            // 更换API https://free-api.heweather.com/v5/aqi?city=jilin&key=%20f75021d48c674f89b3928c2524644ac8
            // https://api.seniverse.com/v3/air/now.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&scope=city
            String address = "https://free-api.heweather.com/v5/aqi?city=" + city1 + "&key=%20f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            //打印weather info
            Log.w("cn", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x13;
            mHandler.sendMessage(message);
        }
    }

    private class WeatherInfoThread implements Runnable {

        private String city = "jilin";

        public WeatherInfoThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                city6 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city6);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            //https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            // https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            String address = "https://api.seniverse.com/v3/weather/daily.json?key=fvdzvvujhi7zevjg&location=" + city6 + "&language=zh-Hans&unit=c&start=0&days=5";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            //打印 weather Info
            Log.i("cn", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x11;
            mHandler.sendMessage(message);
        }
    }

    //  生活指数
    private class WeatherLifeThread implements Runnable {
        private String city = "jilin";

        public WeatherLifeThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                city6 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city6);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            // 修改指数API  https://free-api.heweather.com/v5/suggestion?city=jilin&key=%20f75021d48c674f89b3928c2524644ac8
            // https://api.seniverse.com/v3/life/suggestion.json?key=ftyaftrewoqijxd5&location=shanghai&language=zh-Hans
            //https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            // https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            String address = "https://free-api.heweather.com/v5/suggestion?city=" + city6 + "&key=%20f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            //打印 weather Info
            Log.i("cn", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x15;
            mHandler.sendMessage(message);
        }

    }
    // 日出日落

    private class WeathersunThread implements Runnable {
        private String city = "jilin";

        public WeathersunThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                city6 = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city6);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } // sun  https://free-api.heweather.com/v5/forecast?city=jilin&key=f75021d48c674f89b3928c2524644ac8
            //https://api.seniverse.com/v3/geo/sun.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&start=0&days=7
            //https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            // https://api.seniverse.com/v3/weather/daily.json?key=ftyaftrewoqijxd5&location=beijing&language=zh-Hans&unit=c&start=0&days=5
            String address = " https://free-api.heweather.com/v5/forecast?city=" + city6 + "&key=f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);

            //打印 weather Info
            Log.i("cn", jsonString);

            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x14;
            mHandler.sendMessage(message);
        }
    }

    private void readperfernces() {
        //  读取用于设置
        SharedPreferences sharedPreferences = getSharedPreferences("city",
                MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值  无返回none
        string1 = sharedPreferences.getString("CITY", "");
        String string2 = sharedPreferences.getString("MAIN_TEMP", "");
        String string3 = sharedPreferences.getString("MAIN_INFO", "");
        String string4 = sharedPreferences.getString("TITLE_TEMP", "");
        String string5 = sharedPreferences.getString("FEELS_LIKE", "");
        String string6 = sharedPreferences.getString("VISIBILITY", "");
        String string7 = sharedPreferences.getString("WINDS", "");
        int string8 = sharedPreferences.getInt("CODE", 0);
        String string10=sharedPreferences.getString("PUSH","");
        String string9 = sharedPreferences.getString("PRESSURE", "");


        visibility.setText(string6);
        feels_like.setText(string5);
        main_location.setText(string1);
        winds.setText(string7);
        pressure.setText(string9);
        title_temp.setText(string4);  //  标题栏温度
        main_temp.setText(string2);  //大温度
        main_info.setText(string3);  //优
        code = string8;
        push.setText(string10);

        tianqi(code);               //  修改实时天气图标
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //检查网络状态是否良好
        if (NetUtil.getNetWorkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("YWeather", "网络正常连接");
        } else {
            Log.d("YWeather", "网络挂了");
            ToastUtils.showToast(MainActivity.this, "请打开网络");
        }
        bindViews();
        settime();
        //  有数据  不会NULL
        //  运行定位
        location();
        startSunAnim(5, 19);
        readperfernces();
        readLifeperfernces();
        readPmperfernces();
        readSunperfernces();
        readBCperfernces();

        mBigWindMill = (WindPath) findViewById(R.id.id_wind);
        mSmallWindMill = (WindPath) findViewById(R.id.id_windsmall);
        mBigWindMill.startAnim();
        mSmallWindMill.startAnim();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(R.color.colorAccent, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //开始定位
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
                ToastUtils.showToast(getApplicationContext(), "已定位到当前城市：" + buffer.toString());
            }
        });


        fenx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshot();
                if (imagePath != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
                    File file = new File(imagePath);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));// 分享的内容
                    intent.setType("image/*");// 分享发送的数据类型
                    Intent chooser = Intent.createChooser(intent, "天气分享");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    }
                } else {
                    ToastUtils.showToast(MainActivity.this,"分享失败！");
                }
            }
        });
    }

    private void screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                imagePath = sdCardPath + File.separator + "screenshot.png";

                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
    }

    private void settime() {

        Calendar mCalendar = Calendar.getInstance();
        int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts = mCalendar.get(Calendar.MINUTE);
        //24小时制
        //  HOUR 12小时制
        if (mCalendar.get(Calendar.MINUTE) >= 10) {

            time.setText(mHour + ":" + mMinuts);
        } else {
            time.setText(mHour + ":" + "0" + mMinuts);
        }


    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(6000*10);
        //给对定位客户端象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //activity 再次回到前台
    protected void onResume() {
        super.onResume();
        // 修复页面返回后的空指针

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                //   aMapLocation.getCountry();//国家信息
                // aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                // aMapLocation.getDistrict();//城区信息
                //aMapLocation.getStreet();//街道信息
                // aMapLocation.getStreetNum();//街道门牌号信息
                //aMapLocation.getCityCode();//城市编码
                // aMapLocation.getAdCode();//地区编码
                // aMapLocation.getCity();//城市信息
                buffer = new StringBuffer();
                buffer.append(aMapLocation.getCity() + "");
                city = buffer.toString();
                Log.e("定位", "xx" + buffer.toString());
                city5 = buffer.toString();

                new Thread(new WeatherThread(buffer.toString())).start(); // 实况
                new Thread(new WeatherInfoThread(city5)).start(); //  预报
                new Thread(new WeatherpmThread(city5)).start();   // pm 25
                new Thread(new WeathersunThread(city5)).start();   // 日出日落
                new Thread(new WeatherLifeThread(city5)).start();   // 生活指数

                mLocationClient.stopLocation();//停止定位


            } else {
                //显示错误信息ErrCode是错误码，详见错误码表。errInfo是错误信息，
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                ToastUtils.showToast(getApplicationContext(), "获取定位失败");
            }
        }
    }

    private void processThread() {
        //构建一个下载进度条
        dialog = ProgressDialog.show(MainActivity.this, "", "Loading…");
        new Thread() {
            public void run() {
                //在新线程里执行长耗时方法
                longTimeMethod();
                //执行完毕后给handler发送一个空消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            dialog.dismiss();
        }
    };

    private void longTimeMethod() {

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void saveperfernces(String cityname, String main_temp, String main_info, String title_temp, String feels_like, String visibility, String winds, int code, String pressure,String push) {


        SharedPreferences mySharedPreferences = getSharedPreferences("city",
                MODE_PRIVATE);

        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("CITY", cityname);
        editor.putString("MAIN_TEMP", main_temp);
        editor.putString("MAIN_INFO", main_info);
        editor.putString("TITLE_TEMP", title_temp);
        editor.putString("FEELS_LIKE", feels_like);
        editor.putString("VISIBILITY", visibility);
        editor.putString("WINDS", winds);
        editor.putInt("CODE", code);
        editor.putString("PRESSURE", pressure);
        editor.putString("PUSH",push);
        //提交当前数据
        editor.commit();
        Log.e("保存成功", cityname);
        editor.commit();
    }

    //创建可供选择的菜单选项


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.city_search:
                LayoutInflater factory = LayoutInflater.from(this);
                final View view_search = factory.inflate(R.layout.view_search, null);
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.search)
                        .setView(view_search)
                        .setTitle("搜索城市")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //确认操作
                                EditText searchTextView = (EditText) view_search.findViewById(R.id.searchTextView);
                                //  获取输入文字
                                city = searchTextView.getText().toString();
                                processThread();
                                new Thread(new WeatherThread(city)).start();
                                city5 = city;
                                new Thread(new WeatherInfoThread(city5)).start();
                                new Thread(new WeatherpmThread(city5)).start();   // pm 25
                                new Thread(new WeathersunThread(city5)).start();   // 日出日落
                                new Thread(new WeatherLifeThread(city5)).start();   // 生活指数
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消操作
                                dialogInterface.dismiss();
                            }
                        }).create()
                        .show();
                break;
            case R.id.about_app:
                Intent intent2 = new Intent(MainActivity.this, About.class);
                startActivity(intent2);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 绑定UI控件
     */

    private void setDatas() {
        //   将预报的天气  保存一个偏好设置
        Weather_model newWeather;
        SharedPreferences mySharedPreferences = getSharedPreferences("fore",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 第一天
        newWeather = WeatherList.get(0);
        nowDateTime.setText("今天");
        int code1 = newWeather.getWeatherCode();
        onexiao(code1);
        nowWeatherInfo.setText(newWeather.getText_day());
        nowTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("oneday", nowDateTime.getText().toString());
        editor.putString("oneyu", nowWeatherInfo.getText().toString());
        editor.putInt("onecode", code1);
        editor.putString("onetem", nowTempRange.getText().toString());
        //  第二天
        newWeather = WeatherList.get(1);
        twoDateTime.setText("明天");
        int code2 = newWeather.getWeatherCode();
        twoxiao(code2);
        twoWeatherInfo.setText(newWeather.getText_day());
        twoTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("twoday", twoDateTime.getText().toString());
        editor.putString("twoyu", twoWeatherInfo.getText().toString());
        editor.putInt("twocode", code2);
        editor.putString("twotem", twoTempRange.getText().toString());
        // 第三天
        newWeather = WeatherList.get(2);
        threeDateTime.setText("后天");
        int code3 = newWeather.getWeatherCode();
        threexiao(code3);
        threeWeatherInfo.setText(newWeather.getText_day());
        threeTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("threeday", threeDateTime.getText().toString());
        editor.putString("threeyu", threeWeatherInfo.getText().toString());
        editor.putInt("threecode", code3);
        editor.putString("threetem", threeTempRange.getText().toString());

        //第四天
        newWeather = WeatherList.get(3);
        fourDateTime.setText("外天");
        int code4 = newWeather.getWeatherCode();
        fourxiao(code4);
        fourWeatherInfo.setText(newWeather.getText_day());
        fourTempRange.setText(newWeather.getLow_temp() + "~" + newWeather.getHigh_temp() + "°");
        editor.putString("fourday", fourDateTime.getText().toString());
        editor.putString("fouryu", fourWeatherInfo.getText().toString());
        editor.putInt("fourcode", code4);
        editor.putString("fourtem", fourTempRange.getText().toString());

        editor.commit();

    }

    public void readfore() {
        SharedPreferences sharedPreferences = getSharedPreferences("fore",
                MODE_PRIVATE);
        // 今天
        String string00 = sharedPreferences.getString("oneday", "");
        String string11 = sharedPreferences.getString("oneyu", "");
        String string22 = sharedPreferences.getString("onetem", "");
        int string88 = sharedPreferences.getInt("onecode", 0);
        nowDateTime.setText(string00);
        onexiao(string88);
        nowTempRange.setText(string22);
        nowWeatherInfo.setText(string11);
        // 明天
        String string000 = sharedPreferences.getString("twoday", "");
        String string111 = sharedPreferences.getString("twoyu", "");
        String string222 = sharedPreferences.getString("twotem", "");
        int string888 = sharedPreferences.getInt("twocode", 0);
        twoDateTime.setText(string000);
        twoxiao(string888);
        twoTempRange.setText(string222);
        twoWeatherInfo.setText(string111);
        // 后天
        String string0000 = sharedPreferences.getString("threeday", "");
        String string1111 = sharedPreferences.getString("threeyu", "");
        String string2222 = sharedPreferences.getString("threetem", "");
        int string8888 = sharedPreferences.getInt("threecode", 0);
        threeDateTime.setText(string0000);
        threexiao(string8888);
        threeTempRange.setText(string2222);
        threeWeatherInfo.setText(string1111);
        // 外天
        String string00000 = sharedPreferences.getString("fourday", "");
        String string11111 = sharedPreferences.getString("fouryu", "");
        String string22222 = sharedPreferences.getString("fourtem", "");
        int string88888 = sharedPreferences.getInt("fourcode", 0);
        fourDateTime.setText(string00000);
        fourxiao(string88888);
        fourTempRange.setText(string22222);
        fourWeatherInfo.setText(string11111);

    }

    public void startSunAnim(int sunrise, int sunset) {
        Calendar calendarNow = Calendar.getInstance();
        int hour = calendarNow.get(Calendar.HOUR_OF_DAY);
        if (hour < sunrise) {
            mSunriseView.sunAnim(0);
        } else if (hour > sunset) {
            mSunriseView.sunAnim(1);
        } else {
            mSunriseView.sunAnim(((float) hour - (float) sunrise) / ((float) sunset - (float) sunrise));
        }
    }

    public void bindViews() {
        fenx = (ImageButton)findViewById(R.id.share);
        push = (TextView)findViewById(R.id.push);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        time = (TextView) findViewById(R.id.time);
        up1 = (TextView) findViewById(R.id.up);
        down1 = (TextView) findViewById(R.id.down);
        mSunriseView = (SunriseView) findViewById(R.id.sun);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        pm1 = (TextView) findViewById(R.id.pm);
        location = (ImageButton) findViewById(R.id.location);
        tianqi = (ImageView) findViewById(R.id.imageView2);
        pressure = (TextView) findViewById(R.id.pressure);
        winds = (TextView) findViewById(R.id.winds);
        feels_like = (TextView) findViewById(R.id.feels_like);
        visibility = (TextView) findViewById(R.id.visibility);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        title_temp = (TextView) findViewById(R.id.title_temp);
        main_location = (TextView) findViewById(R.id.main_location);
        main_temp = (TextView) findViewById(R.id.main_tem);
        main_info = (TextView) findViewById(R.id.main_info);
        nowDateTime = (TextView) findViewById(R.id.weather_now).findViewById(R.id.datetime);
        nowWeatherInfo = (TextView) findViewById(R.id.weather_now).findViewById(R.id.weather_info);
        nowTempRange = (TextView) findViewById(R.id.weather_now).findViewById(R.id.range_temp);
        onexiao = (ImageView) findViewById(R.id.weather_now).findViewById(R.id.imageView);

        car1 = (TextView) findViewById(R.id.shenghuo1).findViewById(R.id.carwashing);
        car2 = (TextView) findViewById(R.id.shenghuo1).findViewById(R.id.carwashing1);

        tour1 = (TextView) findViewById(R.id.shenghuo2).findViewById(R.id.carwashing);
        tour2 = (TextView) findViewById(R.id.shenghuo2).findViewById(R.id.carwashing1);

        san1 = (TextView) findViewById(R.id.shenghuo3).findViewById(R.id.carwashing);
        san2 = (TextView) findViewById(R.id.shenghuo3).findViewById(R.id.carwashing1);

        chuan1 = (TextView) findViewById(R.id.shenghuo4).findViewById(R.id.carwashing);
        chuan2 = (TextView) findViewById(R.id.shenghuo4).findViewById(R.id.carwashing1);

        gan1 = (TextView) findViewById(R.id.shenghuo5).findViewById(R.id.carwashing);
        gan2 = (TextView) findViewById(R.id.shenghuo5).findViewById(R.id.carwashing1);

        yun1 = (TextView) findViewById(R.id.shenghuo6).findViewById(R.id.carwashing);
        yun2 = (TextView) findViewById(R.id.shenghuo6).findViewById(R.id.carwashing1);


        twoDateTime = (TextView) findViewById(R.id.weather_second).findViewById(R.id.datetime);
        twoWeatherInfo = (TextView) findViewById(R.id.weather_second).findViewById(R.id.weather_info);
        twoTempRange = (TextView) findViewById(R.id.weather_second).findViewById(R.id.range_temp);
        twoxiao = (ImageView) findViewById(R.id.weather_second).findViewById(R.id.imageView);

        threeDateTime = (TextView) findViewById(R.id.weather_third).findViewById(R.id.datetime);
        threeWeatherInfo = (TextView) findViewById(R.id.weather_third).findViewById(R.id.weather_info);
        threeTempRange = (TextView) findViewById(R.id.weather_third).findViewById(R.id.range_temp);
        threexiao = (ImageView) findViewById(R.id.weather_third).findViewById(R.id.imageView);

        fourDateTime = (TextView) findViewById(R.id.weather_four).findViewById(R.id.datetime);
        fourWeatherInfo = (TextView) findViewById(R.id.weather_four).findViewById(R.id.weather_info);
        fourTempRange = (TextView) findViewById(R.id.weather_four).findViewById(R.id.range_temp);
        fourxiao = (ImageView) findViewById(R.id.weather_four).findViewById(R.id.imageView);
        //设置自定义的toolbar为actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //隐藏label

    }

    public void tianqi(int code) {
        if (code ==100||code==900) {
            tianqi.setImageResource(R.mipmap.sunsun);
        } else if (code == 101 || code == 102 || code == 103) {
            tianqi.setImageResource(R.mipmap.duoyun);
        } else if (code == 104) {
            tianqi.setImageResource(R.mipmap.yingtian);
        } else if (code == 300||code==301||code==305||code==309) {
            tianqi.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 306) {
            tianqi.setImageResource(R.mipmap.zhongyu);
        } else if (code == 307 || code == 308|| code == 310|| code == 311|| code == 312) {
            tianqi.setImageResource(R.mipmap.dayu);
        } else if (code == 302 || code == 303 ) {
            tianqi.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 404||code==405||code==406||code==401||code==407) {
            tianqi.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 313||code==400) {
            tianqi.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 402|| code == 403 ) {
            tianqi.setImageResource(R.mipmap.daxue);
        } else if (code == 503 || code == 504 || code == 507 || code == 508) {
            tianqi.setImageResource(R.mipmap.shachen);
        } else if (code == 502) {
            tianqi.setImageResource(R.mipmap.gg);
        } else if (code == 501||code==500) {
            tianqi.setImageResource(R.mipmap.tt);
        } else if (code == 200||code==201||code == 202|| code == 203|| code == 204|| code == 205|| code == 206|| code == 207|| code == 208|| code == 209||code==210|| code == 211|| code == 212|| code == 213) {
            tianqi.setImageResource(R.mipmap.dafeng);
        } else if (code == 304) {
            tianqi.setImageResource(R.mipmap.leibing);
        }
    }

    private void threexiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            threexiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            threexiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            threexiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            threexiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 | code == 12) {
            threexiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            threexiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18|| code == 11 ) {
            threexiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            threexiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            threexiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            threexiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            threexiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            threexiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            threexiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            threexiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            threexiao.setImageResource(R.mipmap.leibing);
        }
    }


    private void onexiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            onexiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            onexiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            onexiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            onexiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14  || code == 12) {
            onexiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            onexiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18|| code == 11 ) {
            onexiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            onexiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            onexiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            onexiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            onexiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            onexiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            onexiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            onexiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            onexiao.setImageResource(R.mipmap.leibing);
        }
    }

    private void twoxiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            twoxiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            twoxiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            twoxiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            twoxiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14 || code == 12) {
            twoxiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            twoxiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18|| code == 11 ) {
            twoxiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            twoxiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            twoxiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            twoxiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            twoxiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            twoxiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            twoxiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            twoxiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            twoxiao.setImageResource(R.mipmap.leibing);
        }
    }

    private void fourxiao(int code) {
        if (code == 0 || code == 2 || code == 38) {
            fourxiao.setImageResource(R.mipmap.sunsun);
        } else if (code == 4 || code == 5 || code == 7) {
            fourxiao.setImageResource(R.mipmap.duoyun);
        } else if (code == 9) {
            fourxiao.setImageResource(R.mipmap.yingtian);
        } else if (code == 13) {
            fourxiao.setImageResource(R.mipmap.xiaoyu);
        } else if (code == 10 || code == 14  || code == 12) {
            fourxiao.setImageResource(R.mipmap.zhongyu);
        } else if (code == 15 || code == 19) {
            fourxiao.setImageResource(R.mipmap.dayu);
        } else if (code == 16 || code == 17 || code == 18|| code == 11 ) {
            fourxiao.setImageResource(R.mipmap.leizhenyu);
        } else if (code == 20) {
            fourxiao.setImageResource(R.mipmap.yujiaxue);
        } else if (code == 21 || code == 22) {
            fourxiao.setImageResource(R.mipmap.xiaoxue);
        } else if (code == 23 || code == 24 || code == 25 || code == 37) {
            fourxiao.setImageResource(R.mipmap.daxue);
        } else if (code == 26 || code == 27 || code == 28 || code == 29) {
            fourxiao.setImageResource(R.mipmap.shachen);
        } else if (code == 30) {
            fourxiao.setImageResource(R.mipmap.gg);
        } else if (code == 31) {
            fourxiao.setImageResource(R.mipmap.tt);
        } else if (code == 32 || code == 33) {
            fourxiao.setImageResource(R.mipmap.dafeng);
        } else if (code == 34 || code == 35 || code == 36) {
            fourxiao.setImageResource(R.mipmap.leibing);
        }
    }


    //清空天气列表中的数据
    private void clearWeatherList() {
        WeatherList.clear();
    }


}





