package com.example.doc88.egphoneintercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by EG on 2017/12/15.
 */

public class OutcallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取拨打的电话号码
        String outcallnumber=getResultData();
        //创建SharedPreferences对象，获取拦截号码
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String number =sp.getString("number","");
        //判断是否是拦截号码
        if (outcallnumber.equals(number)){
            //清除电话
            setResultData(null);

        }
    }
}
