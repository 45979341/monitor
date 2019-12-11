package com.zhuzhou.framework.utils.previous;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class ResponseInfo<T> implements Serializable {

    protected static final long serialVersionUID = 1L;

    //返回码
    protected String code;

    //返回描述
    protected String message;

    //返回内容
    protected T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCodeAndDesc(CloudResultCode cloudResultCode) {
        this.code = cloudResultCode.getCode();
        this.message = cloudResultCode.getValue();
    }

    public void setCodeAndDesc(CloudResultCode cloudResultCode,String mas) {
        this.code = cloudResultCode.getCode();
        this.message = cloudResultCode.getValue()+",["+mas+"]";
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
