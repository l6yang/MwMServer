package com.mwm.loyal.model;

import com.mwm.loyal.imp.Contact;
import com.mwm.loyal.utils.TimeUtil;

import java.sql.Timestamp;
import java.util.Date;

public class FeedBackBean {
    private String account;
    private String content;
    private Timestamp time;

    public FeedBackBean(String account, String content, Timestamp time) {
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{\"time\":" + (time == null ? null : "\"" + TimeUtil.getDate(new Date(time.getTime()), Contact.Str.TIME_ALL) + "\"") +
                ",\"account\":" + (account == null ? null : "\"" + account + "\"") +
                ",\"content\":" + (content == null ? null : "\"" + content + "\"") +
                "}";
    }
}
