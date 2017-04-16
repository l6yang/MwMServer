package com.mwm.loyal.model;

public class FeedBackBean {
    private String account;
    private String content;
    private String time;

    public FeedBackBean() {
    }

    public FeedBackBean(String account) {
        this.account = account;
    }

    public FeedBackBean(String account, String content, String time) {
        this.account = account;
        this.content = content;
        this.time = time;
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
