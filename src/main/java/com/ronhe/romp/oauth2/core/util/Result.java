package com.ronhe.romp.oauth2.core.util;

import java.io.Serializable;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/7/12
 */
public class Result<R> implements Serializable {

    private boolean success;
    private int code;
    private String msg;
    private R data;

    private static final String successMsg = "操作成功.";
    private static final String failMsg = "操作失败.";

    public static <R> Result<R> ofSuccess() {
        return new Result<R>().setSuccess(true).setMsg(successMsg).setData(null);
    }

    public static <R> Result<R> ofSuccess(R data) {
        return new Result<R>().setSuccess(true).setMsg(successMsg).setData(data);
    }

    public static <R> Result<R> ofSuccessMsg(String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg);
    }

    public static <R> Result<R> ofSuccessWithMsg(R data, String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg).setData(data);
    }

    public static <R> Result<R> ofSuccess(int code, String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg).setData(null).setCode(code);
    }

    public static <R> Result<R> ofFail() {
        return new Result<R>().setSuccess(false).setMsg(failMsg);
    }

    public static <R> Result<R> ofFail(int code, String msg) {
        Result<R> ResponseResult = new Result<>();
        ResponseResult.setSuccess(false);
        ResponseResult.setCode(code);
        ResponseResult.setMsg(msg);
        return ResponseResult;
    }

    public static <R> Result<R> ofFail(R data) {
        return new Result<R>().setSuccess(false).setMsg(failMsg).setData(data);
    }

    public static <R> Result<R> ofFail(String msg) {
        Result<R> ResponseResult = new Result<>();
        ResponseResult.setSuccess(false);
        ResponseResult.setMsg(msg);
        return ResponseResult;
    }

    public static <R> Result<R> ofFail(R data, String msg) {
        Result<R> ResponseResult = new Result<>();
        ResponseResult.setData(data);
        ResponseResult.setSuccess(false);
        ResponseResult.setMsg(msg);
        return ResponseResult;
    }

    public static <R> Result<R> ofFail(int code, String msg, R data) {
        return new Result<R>().setSuccess(false).setMsg(msg).setCode(code).setData(data);
    }

    public static <R> Result<R> ofThrowable(Throwable throwable) {
        Result<R> ResponseResult = new Result<>();
        ResponseResult.setSuccess(false);
        ResponseResult.setMsg(throwable.getMessage());
        return ResponseResult;
    }

    public static <R> Result<R> ofThrowable(Throwable throwable, R data) {
        Result<R> ResponseResult = new Result<>();
        ResponseResult.setSuccess(false);
        ResponseResult.setData(data);
        ResponseResult.setMsg(throwable.getMessage());
        return ResponseResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result<R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public R getData() {
        return data;
    }

    public Result<R> setData(R data) {
        this.data = data;
        return this;
    }
}
