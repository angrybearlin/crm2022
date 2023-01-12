package com.study.crm.commons.domain;

public class RetValue {
    private String code;

    private String msg;

    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public RetValue() {
    }

    public RetValue(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
