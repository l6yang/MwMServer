package com.mwm.loyal.beans;

public class FeedBackBean {
    private String time;
    private String account;
    private String content;

    public FeedBackBean(String time, String account, String content) {
        this.time = time;
        this.account = account;
        this.content = content;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{\"time\":" + (time == null ? null : "\"" + time + "\"") +
                ",\"account\":" + (account == null ? null : "\"" + account + "\"") +
                ",\"content\":" + (content == null ? null : "\"" + content + "\"") +
                "}";
    }
}
