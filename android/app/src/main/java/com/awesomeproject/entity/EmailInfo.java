package com.awesomeproject.entity;

import java.util.List;

/**
 * 邮件发送信息
 */
public class EmailInfo {
    public String sendEamil;

    public String sendEmailPassword;

    public List<String> emailList;

    public String smtpHost;

    public String sendUserName;

    public String receiveUserName;

    public String subject;

    public String content;

    public String getSendEamil() {
        return sendEamil;
    }

    public void setSendEamil(String sendEamil) {
        this.sendEamil = sendEamil;
    }

    public String getSendEmailPassword() {
        return sendEmailPassword;
    }

    public void setSendEmailPassword(String sendEmailPassword) {
        this.sendEmailPassword = sendEmailPassword;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}