package com.mwm.loyal.beans;

public class ResultBean<T> {
    private String code;
    private String message;
    private T obj;

    public ResultBean() {
    }

    public ResultBean(T obj) {
        this.code = "1";
        this.obj = obj;
    }

    public ResultBean(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultBean(String code, String message, T obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(int code) {
        setCode(String.valueOf(code));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
