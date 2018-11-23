package com.awesomeproject.module;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.util.Log;

import com.awesomeproject.entity.EmailInfo;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailModule extends ReactContextBaseJavaModule {

    public MailModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * 返回的名称由前端调用
     *
     * @return
     */
    @Override
    public String getName() {
        return "MailModule";
    }

    @ReactMethod
    public void sendMail(String message, Callback callback) {
        sendMail("<h1>hello world</h1>", "明天上班");
        callback.invoke("I receivce message!!!");
    }

    public void sendMail(String content, String subject) {
        EmailInfo emailInfo = getEmailInfo(content, subject);
        sendSimpleEmail(emailInfo);
    }

    @ReactMethod
    public void getMessage(Callback callback) {
        callback.invoke(getAllMessage());
    }

    /**
     * 获得所有收件箱的信息
     *
     * @return
     */
    public String getAllMessage() {
        final String SMS_URI_ALL = "content://sms/";//所有短信
        final String SMS_URI_INBOX = "content://sms/inbox";//收件箱
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getCurrentActivity().getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {
            Log.d("TAG", ex.getMessage());
        }
        return smsBuilder.toString();
    }

    /**
     * 插入发送列表数据
     *
     * @param emailList
     * @param callback
     */
    @ReactMethod
    public void insertEmailValue(String emailList, Callback callback) {
        insertValue("emailList", emailList);
        callback.invoke("success");
    }

    /**
     * 保存发送邮件信息
     * sendEmail,sendEmailPassword
     *
     * @param info
     * @param callback
     */
    @ReactMethod
    public void insertSendInfo(String info, Callback callback) {
        if (info.indexOf(",") > 0) {
            String sendEmail = info.split(",")[0];
            String sendEmailPassword = info.split(",")[1];
            insertValue("sendEmail", sendEmail);
            insertValue("sendEmailPassword", sendEmailPassword);
            callback.invoke("success");
        }
    }

    /**
     * 读取发送列表数据
     *
     * @param callback
     */
    @ReactMethod
    public void getEmailValue(Callback callback) {
        String data = getValue("emailList");
        callback.invoke(data);
    }

    /**
     * 读取所有发送的短信数量
     *
     * @param callback
     */
    @ReactMethod
    public void getTotalReceiveNum(Callback callback) {
        Integer total = getIntValue("total");
        callback.invoke(total);
    }

    /**
     * 发送一个新的消息
     */
    public void addNewReceiveMessage() {
        Integer total = getIntValue("total");
        insertIntegerValue("total", total + 1);
    }

    /**
     * 插入数据
     *
     * @param key
     * @param value
     */
    private void insertValue(String key, String value) {
        SharedPreferences sp = getCurrentActivity().getSharedPreferences("store_info", Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        //以key-value形式保存数据
        editor.putString(key, value);
        //apply()是异步写入数据
        editor.apply();
        //commit()是同步写入数据
        //editor.commit();
    }

    /**
     * 插入数据
     *
     * @param key
     * @param value
     */
    private void insertIntegerValue(String key, Integer value) {
        SharedPreferences sp = getCurrentActivity().getSharedPreferences("store_info", Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        //以key-value形式保存数据
        editor.putInt(key, value);
        //apply()是异步写入数据
        editor.apply();
        //commit()是同步写入数据
        //editor.commit();
    }

    /**
     * 读取数据
     *
     * @param key
     * @return
     */
    private String getValue(String key) {
        //获得SharedPreferences的实例
        SharedPreferences sp = getCurrentActivity().getSharedPreferences("store_info", Context.MODE_PRIVATE);
        //通过key值获取到相应的data，如果没取到，则返回后面的默认值
        String data = sp.getString(key, "null");
        return data;
    }

    private Integer getIntValue(String key) {
        //获得SharedPreferences的实例
        SharedPreferences sp = getCurrentActivity().getSharedPreferences("store_info", Context.MODE_PRIVATE);
        //通过key值获取到相应的data，如果没取到，则返回后面的默认值
        Integer data = sp.getInt(key, 0);
        return data;
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @return
     * @throws Exception
     */
    private static MimeMessage createMimeMessage(Session session, EmailInfo emailInfo) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(emailInfo.getSendEamil(), "邮件服务", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        if (emailInfo.getEmailList().size() > 0) {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailInfo.getEmailList().get(0), emailInfo.getEmailList().get(0), "UTF-8"));
            for (int i = 1; i < emailInfo.getEmailList().size(); i++) {
                message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailInfo.getEmailList().get(i), emailInfo.getEmailList().get(i), "UTF-8"));
            }
        }


        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(emailInfo.getSubject(), "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(emailInfo.getContent(), "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
//        message.saveChanges();

        return message;
    }

    /**
     * 发送简单电子邮件
     */
    private void sendSimpleEmail(EmailInfo emailInfo) {
        final EmailInfo curEmailInfo = emailInfo;
        // Android 4.0 之后不能在主线程中请求HTTP请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
                // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
                //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
                String myEmailAccount = curEmailInfo.getSendEamil();
                String myEmailPassword = curEmailInfo.getSendEmailPassword();

                // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
                // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
                String myEmailSMTPHost = curEmailInfo.getSmtpHost();

                // 收件人邮箱（替换为自己知道的有效邮箱）
                // 1. 创建参数配置, 用于连接邮件服务器的参数配置
                Properties props = new Properties();                    // 参数配置
                props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
                props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
                props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

                // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
                //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
                //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
                /*
                // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
                //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
                //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
                final String smtpPort = "465";
                props.setProperty("mail.smtp.port", smtpPort);
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.smtp.socketFactory.fallback", "false");
                props.setProperty("mail.smtp.socketFactory.port", smtpPort);
                */

                // 2. 根据配置创建会话对象, 用于和邮件服务器交互
                Session session = Session.getInstance(props);
                session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

                // 3. 创建一封邮件
                try {
                    MimeMessage curMessage = createMimeMessage(session, curEmailInfo);
                    // 4. 根据 Session 获取邮件传输对象
                    Transport transport = session.getTransport();
                    // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
                    //
                    //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
                    //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
                    //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
                    //
                    //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
                    //           (1) 邮箱没有开启 SMTP 服务;
                    //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
                    //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
                    //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
                    //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
                    //
                    //    PS_03: 仔细看日志。

                    transport.connect(myEmailAccount, myEmailPassword);
                    // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                    transport.sendMessage(curMessage, curMessage.getAllRecipients());
                    // 7. 关闭连接
                    transport.close();


                } catch (Exception e) {

                }
            }
        }).start();
    }


    private EmailInfo getEmailInfo(String content, String subject) {
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setSendEamil(getValue("sendEmail"));
        emailInfo.setSendEmailPassword(getValue("sendEmailPassword"));
        String emailListStr = getValue("emailList");
        String[] emailListArr = emailListStr.split(",");
        List<String> emailList = new ArrayList<>();
        for (String str : emailListArr) {
            emailList.add(str);
        }
        emailInfo.setEmailList(emailList);
        String smtpHost = "";
        if (emailInfo.getSendEamil().indexOf("qq.com") >= 0) {
            smtpHost = "smtp.qq.com";
        } else if (emailInfo.getSendEamil().indexOf("163.com") >= 0) {
            smtpHost = "smtp.163.com";
        }
        emailInfo.setSmtpHost(smtpHost);
        emailInfo.setContent(content);
        emailInfo.setSubject(subject);
        emailInfo.setSendUserName("服务邮箱");
        emailInfo.setReceiveUserName("接收邮箱");
        return emailInfo;
    }
}