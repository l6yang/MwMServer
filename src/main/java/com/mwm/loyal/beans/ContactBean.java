package com.mwm.loyal.beans;

public class ContactBean {
    private String account;
    private String contact;
    private String time;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ContactBean(String account, String contact, String time) {
        this.account = (account);
        this.contact = (contact);
        this.time = (time);
    }

    public ContactBean() {
    }

    @Override
    public String toString() {
        return "{\"account\":" + (account == null ? null : "\"" + account + "\"") +
                ",\"contact\":" + (contact == null ? null : "\"" + contact + "\"") +
                ",\"time\":" + (time == null ? null : "\"" + time + "\"") +
                "}";
    }
}