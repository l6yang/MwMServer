package com.mwm.loyal.model;

public class AccountBean {
    private String account;
    private String password;
    private String nickname;
    private byte[] icon;
    private String sign;
    private int locked = 0;
    private String oldPassword;

    public AccountBean() {
    }

    public AccountBean(String account) {
        this.account = account;
    }

    public AccountBean(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public AccountBean(String account, String password, String nickname, byte[] icon, String sign, int locked, String oldPassword) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.icon = icon;
        this.sign = sign;
        this.locked = locked;
        this.oldPassword = oldPassword;
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

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
