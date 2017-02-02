package com.mwm.loyal.beans;

import java.sql.Blob;

public class LoginBean {
    private String account;
    private String password;
    private String nickname;
    private Blob icon;
    private String signature;
    private String mac;
    private String device;
    private String lock;//-1表示帐号被锁

    public LoginBean() {
    }

    public LoginBean(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public LoginBean(String account, String password, String nickname, Blob icon, String signature) {
        this.account = account;
        this.nickname = nickname;
        this.password = password;
        this.icon = icon;
        this.signature = signature;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Blob getIcon() {
        return icon;
    }

    public void setIcon(Blob icon) {
        this.icon = icon;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
