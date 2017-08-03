package com.sparklyys.yweather;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.sparklyys.yweather.Toast.ToastUtils;
import com.sparklyys.yweather.ui.AAlertDialog;

import java.io.File;

/**
 * Created by 丽丽超可爱 on 2017/7/26.
 */

public class About extends Activity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.about);
        /* 创建菜单 */
        Button zhuye = (Button) findViewById(R.id.zhuye);
        Button fankui = (Button) findViewById(R.id.fankui);
        Button genxin = (Button) findViewById(R.id.genxin);
        Button fenxianng = (Button) findViewById(R.id.fenxiang);
        ImageButton imageButton = (ImageButton) findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(About.this, MainActivity.class);
                startActivity(intent);
            }
        });

        zhuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://github.com/Xxianglei/HeartWeather
                Uri uri=Uri.parse("https://github.com/Xxianglei/HeartWeather");   //指定网址
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);           //指定Action
                intent.setData(uri);                            //设置Uri
                startActivity(intent);        //启动Activit
        }
        });
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              new AAlertDialog(About.this).builder()
                      .setMsg("")
                      .setTitle("反馈信息")
                      .show();
            }
        });

        genxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new AAlertDialog(About.this).builder().setTitle("已经是最新版本^_^").show();
            }
        });
        fenxianng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
                Intent intent = new Intent(Intent.ACTION_SEND);
                File file=new File("app-debug.apk");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });
    }



}
