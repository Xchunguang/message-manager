package com.awesomeproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.awesomeproject.module.MailModule;
import com.awesomeproject.module.ModulePackage;
import com.facebook.react.bridge.ReactApplicationContext;

public class SmsReciver extends BroadcastReceiver {
    private static final String TAG = "MainActivity";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Object []pdus = (Object[]) bundle.get("pdus");
        String format = intent.getStringExtra("format");
        SmsMessage[]messages = new SmsMessage[pdus.length];
        Log.d(TAG, "onReceive format is "+format+" !!!!!! ");
        for(int i=0;i<messages.length;i++){
//                createFromPdu(byte []pdu)方法已被废弃
//                原因是为了同时支持3GPP和3GPP2
//                他们是移动系统通信标准的拟定组织分别拟定的GSM/UMTS/LTE标准和CDMA/LTE标准
//                因此推荐是用的方法是createFromPdu(byte[] pdu, String format)
//                其中fotmat可以是SmsConstants.FORMAT_3GPP或者SmsConstants.FORMAT_3GPP2
            byte[] sms = (byte[])pdus[i];
            messages[i]  = SmsMessage.createFromPdu(sms,format);
        }
        //获取发送方手机号码
        String address = messages[0].getOriginatingAddress();
        String fullMessage = "";
        for(SmsMessage message:messages){
            //获取短信内容（短信内容太长会被分段）
            fullMessage += message.getMessageBody();
        }
        Log.d(TAG, "消息内容: "+fullMessage);
        Log.d(TAG, "发送人: "+address);
        String mailInfo = getHtmlByMessage(fullMessage,address);

        // @ TODO 使用短信指令来操作手机发送的短信类型
        //通过定义好的几个操作设置相应的指令，通过给当前手机发送指令即可调用程序发送相应的短信


        ModulePackage curModulePackage = MainApplication.getModule();
        //发送消息
        curModulePackage.getMailModule().sendMail(mailInfo,"您有新的短消息");
        //发送消息的总数加一
        curModulePackage.getMailModule().addNewReceiveMessage();
    }

    private String getHtmlByMessage(String message,String address){
        StringBuilder sb = new StringBuilder("<p><h2>");
        sb.append("发件人：<span style='color:blue'>");
        sb.append(address);
        sb.append("</span></h2></p>");
        sb.append("<p><h4>内容：<span style='color:blue'>");
        sb.append(message);
        sb.append("</span></h4></p>");
        return sb.toString();
    }
}