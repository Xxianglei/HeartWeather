package com.sparklyys.yweather.Toast;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.sparklyys.yweather.R;



/**
 * Created by sxt on 2015.11.27
 */
public class ToastUtils {
    private static Toast toast;
    private static TextView textView;
    /**
     * 自定义样式的吐司
     * <p/>
     * 静态toast 只创建一个toast实例 可以实时显示弹出的内容
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {

        if (toast == null) { // 1. 创建前 2.消失后toast为null
            // 获取打气筒
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //创建视图
            View view = inflater.inflate(R.layout.toast, null);
            textView = (TextView) view.findViewById(R.id.tv_toast_text);
            //创建土司
            toast = new Toast(context);
            //设置居中方式  默认在底部
            //toast.setGravity(Gravity.CENTER, 0, 0);//如果不设置剧中方式,使用系统默认的吐司位置
            //设置土司的持续时长
            toast.setDuration(Toast.LENGTH_SHORT);
            //设置土司的背景View
            toast.setView(view);
        }
        //设置土司的显示额内容
        textView.setText(text);
        toast.show();
    }
}
