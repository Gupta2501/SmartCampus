package com.teach.javafxclient.request;

import java.util.Map;
/**
 * DataResponse 后端HTTP请求返回数据对象
 * Integer code 返回代码 0 正确返回 1 错误返回信息
 * Object data 返回数据对象
 * String msg 返回正确错误信息
 */
public class DataResponse<T> {
    private Integer code;
    private T data;
    private String msg;

    public DataResponse(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
