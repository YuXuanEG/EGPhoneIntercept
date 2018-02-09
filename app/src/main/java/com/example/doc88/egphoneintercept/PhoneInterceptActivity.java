package com.example.doc88.egphoneintercept;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PhoneInterceptActivity extends AppCompatActivity {
    private EditText et_ipnumber;
    private SharedPreferences sp;
    private OutcallReceiver outcallReceiver;

    private String permissions[] = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_intercept);

        et_ipnumber = (EditText) findViewById(R.id.et_ipnumber);
        //创建SharedPreferences对象
        sp = getSharedPreferences("config", MODE_PRIVATE);

        //动态注册拦截广播
//        outcallReceiver = new OutcallReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.intent.action.PHONE_STATE");
//        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//        registerReceiver(outcallReceiver,intentFilter);

        requestEGPermission(permissions);
    }

    public void click(View view) {
        String number = et_ipnumber.getText().toString().trim();
        switch (view.getId()) {
            case R.id.button:
                //创建Editor对象，保存用户输入的拦截号码
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("number", number);
                editor.commit();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_call:
                //申请打电话权限
                requestEGPermission(new String[]{Manifest.permission.CALL_PHONE});
                //拨号
                //方法一，直接intent调启
//                callMethod1(number);
                //方法二，利用反射拨号
                callMethod2(number);

                break;
        }

    }

    private void callMethod2(String number) {

        try {
            // 首先拿到TelephonyManager
            TelephonyManager telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;

            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);

            // 再通过ITelephony对象去反射里面的call方法，并传入包名和需要拨打的电话号码
            Method mt = obj.getClass().getMethod("call", new Class[]{String.class, String.class});
            //允许访问私有方法
            mt.setAccessible(true);
            mt.invoke(obj, new Object[]{getPackageName() + "", number});

//            Toast.makeText(this, "拨打电话！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callMethod1(String number) {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        intentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestEGPermission(new String[]{Manifest.permission.CALL_PHONE});
            return;
        }
        startActivity(intentCall);
    }

    private void requestEGPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> toApplyList = new ArrayList<String>();

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                    toApplyList.add(perm);
                    //进入到这里代表没有权限.
                }
            }
            String tmpList[] = new String[toApplyList.size()];
            if (!toApplyList.isEmpty()) {
                requestPermissions(toApplyList.toArray(tmpList), 123);
            }else {
                //有权限，随意do
            }
        }else {
            //低于android6.0在manifest中申请，直接do
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //已获取权限

                    //响应事件
                }else{
                    //权限被拒绝


//                    new SimpleBaseDialog(this, "需要开启权限后才能使用", "去设置", "取消", new SimpleClickListener() {
//                        @Override
//                        public void ok() {
//                            Intent intent=CMethod.getAppDetailSettingIntent(PulishWriteActivity.this);
//                            startActivity(intent);
//                        }
//                        @Override
//                        public void cancle() {
//
//                        }
//                    }).show();
                    M_ConfirmDialog.Builder alert = new M_ConfirmDialog.Builder(this);
                    alert.setTitle("提示")
                            .setMessage("确定从文辑中移除该文档吗？")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButtonColor(R.color.colorAccent)
                            .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButtonColor(R.color.colorPrimary);
                    alert.create().show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (outcallReceiver != null) {
//            unregisterReceiver(outcallReceiver);
//        }
    }
}
