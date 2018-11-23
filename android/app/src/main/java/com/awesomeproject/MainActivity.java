package com.awesomeproject;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.awesomeproject.module.ModuleUtil;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
    private static final String TAG = "MainActivity";

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private IntentFilter receiveFilter;
    private SmsReciver messageReceiver;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "MM";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查询权限，包含了接收短信
        queryAuthority();
        //监控短信发送状态的监听器
    }



    //回调函数，不论用户在授权对话框同意还是拒绝，activity的onRequestPermissionsResult会被回调来通知结果（通过第三个参数）
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //git  test >>>>>>>>>>>>>>>>>
                    queryAuthority();
                } else {
                    // Permission Denied
                    Log.d(TAG, "onRequestPermissionsResult:获取信息失败 ");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //查询权限
    private void queryAuthority() {
        int hasReadContactsPermission = 0;
        //Android Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ModuleUtil.isMIUI()){
                final String SMS_URI_INBOX = "content://sms/inbox";
                try {
                    // 盲调获取手机内部短信，如果出错则没有权限，则打开权限面板
                    Uri uri = Uri.parse(SMS_URI_INBOX);
                    String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
                    Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");
                }catch(Exception e){
                    Toast.makeText(this.getBaseContext(), "请授予读取短信权限和读取通知类短信权限！", Toast.LENGTH_LONG).show();
                    jumpPermissionActivity();
                }
            }else{
                hasReadContactsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            }
        }

        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                请求授权对话框
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.RECEIVE_SMS},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }

            return;
        }
        //执行查询操作
        registerReceiver();
    }

    //为短信监听器注册（动态）
    public void registerReceiver(){
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver = new SmsReciver();
        registerReceiver(messageReceiver,receiveFilter);
    }


    private void jumpPermissionActivity() {
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            String miuiVersion = getMIUIVersion();
            String permissionDetailActivityNameV6 = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";
            String permissionDetailActivityNameV8 = "com.miui.permcenter.permissions.PermissionsEditorActivity";
            if (!TextUtils.isEmpty(miuiVersion)) {
                switch (miuiVersion) {
                    case "V5":
                        jumpOriginalPermissionActivity();
                        break;
                    case "V6":
                    case "V7":
                        jumpXimiPermissionActivity(permissionDetailActivityNameV6);
                        break;
                    case "V8":
                    case "V9":
                        jumpXimiPermissionActivity(permissionDetailActivityNameV8);
                        break;
                    default:
                        jumpOriginalPermissionActivity();
                        break;
                }
            } else {
                Toast.makeText(this.getBaseContext(), "应用权限页跳转失败，请手动在设置中进行更改", Toast.LENGTH_SHORT).show();
            }

        } else {
            jumpOriginalPermissionActivity();
        }
    }

    /**
     * 根据MIUI系统，判断需要跳转的页面
     *
     * @param permissionDetailActivityName
     */
    private void jumpXimiPermissionActivity(String permissionDetailActivityName) {
        try {

            Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
            ComponentName componentName = new ComponentName("com.miui.securitycenter", permissionDetailActivityName);
            i.setComponent(componentName);
            i.putExtra("extra_pkgname", getPackageName());
            startActivity(i);
        } catch (Exception e) {
            jumpOriginalPermissionActivity();
        }
    }

    /**
     * 普通Android手机通用的权限设置页面跳转
     */
    private void jumpOriginalPermissionActivity() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);

    }

    private static String getMIUIVersion() {
        return ModuleUtil.getSystemProperty("ro.miui.ui.version.name");
    }
}
