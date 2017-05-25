package com.yihleego.pano.pojo;

import java.io.Serializable;

/**
 * Created by YihLeego on 17-5-17.
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -9158305920472927444L;
    private T data;
    private boolean success = false;
    private String msg;
    private String code;
    private String exception;

    public Result() {
    }

    public static <T> Result<T> createSucc(String code, String msg, T data) {
        Result<T> result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> createSucc(T data) {
        return createSucc("0", "success", data);
    }

    public static <T> Result<T> createFail(String code, String msg, String exception) {
        Result<T> result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setException(exception);
        return result;
    }

    public static <T> Result<T> createFail(String exception) {
        return createFail("1", "failed", exception);
    }


    public Result<T> buildSucc(String code, String msg, T data) {
        this.data = data;
        this.success = true;
        this.msg = msg;
        this.code = code;
        return this;
    }

    public Result<T> buildSucc(T data) {
        return buildSucc("0", "success", data);
    }

    public Result<T> buildFail(String code, String msg, String exception) {
        this.success = false;
        this.msg = msg;
        this.code = code;
        this.exception = exception;
        return this;
    }

    public Result<T> buildFail(T data) {
        return buildFail("1", "failed", exception);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
