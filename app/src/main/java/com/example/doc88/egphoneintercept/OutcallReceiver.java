package com.example.doc88.egphoneintercept;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by EG on 2017/12/15.
 */

public class OutcallReceiver extends BroadcastReceiver {

    private static final String TAG = "EGLog";

    private static boolean incomingFlag = false;
    private static boolean outcomingFlag = false;

    private static String incoming_number = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //创建SharedPreferences对象，获取拦截号码
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String number =sp.getString("number","");



        //如果是拨打电话
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){

            incomingFlag = false;

            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            Log.d(TAG, "call OUT:"+phoneNumber);
            //判断是否是拦截号码
            if (phoneNumber.equals(number)){
                try {
                    // 自动挂断电话
                    // 首先拿到TelephonyManager
                    TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Class<TelephonyManager> c = TelephonyManager.class;

                    // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
                    Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                    //允许访问私有方法
                    mthEndCall.setAccessible(true);
                    final Object obj = mthEndCall.invoke(telMag, (Object[]) null);

                    // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
                    Method mt = obj.getClass().getMethod("endCall");
                    //允许访问私有方法
                    mt.setAccessible(true);
                    mt.invoke(obj);
//                    Toast.makeText(context, "挂断电话！", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //清除电话
                setResultData(null);
            }


        }else{

            //如果是来电

            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

            int state = tm.getCallState();
            Log.d(TAG,"电话状态"+state);

            switch (tm.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:

                    incomingFlag = true;//标识当前是来电

                    incoming_number = intent.getStringExtra("incoming_number");

                    Log.d(TAG, "RINGING :"+ incoming_number);
                    //判断是否是拦截号码
                    if (incoming_number.equals(number)){
                        try {
                            // 自动挂断电话
                            // 首先拿到TelephonyManager
                            TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                            Class<TelephonyManager> c = TelephonyManager.class;

                            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
                            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                            //允许访问私有方法
                            mthEndCall.setAccessible(true);
                            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);

                            // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
                            Method mt = obj.getClass().getMethod("endCall");
                            //允许访问私有方法
                            mt.setAccessible(true);
                            mt.invoke(obj);
//                            Toast.makeText(context, "挂断电话！", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //清除电话
                        setResultData(null);
                    }


                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //设备呼叫状态：摘机。 至少存在一个正在拨号，处于活动状态或处于保持状态的呼叫，并且没有呼叫正在振铃或等待。
                    outcomingFlag = true;//标识当前是去电


                    break;



                case TelephonyManager.CALL_STATE_IDLE:

                    if(incomingFlag||outcomingFlag){

                        Log.d(TAG, "incoming IDLE");

                    }

                    break;

            }

        }

    }
}
