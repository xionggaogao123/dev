package com.sys.utils;

import java.io.Serializable;

import com.sys.constants.Constant;

/**
 * 一个返回类，表示处理结果正确与否
 *
 * @author fourer
 */
public class RespObj implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 60683785108752137L;

    /**
     * 处理成功
     */
    public static final RespObj SUCCESS = new RespObj(Constant.SUCCESS_CODE);
    /**
     * 处理失败
     */
    public static final RespObj FAILD = new RespObj(Constant.FAILD_CODE);

    private String code;
    private Object message;
    private String errorMessage;

    public RespObj(String code) {
        super();
        this.code = code;
    }

    public RespObj(String code, Object message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static RespObj SUCCESS(Object message) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(message);
        return respObj;
    }

    public static RespObj FAILD(Object message) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        respObj.setMessage(message);
        return respObj;
    }

    public static RespObj FAILDWithErrorMsg(String errorMsg) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        respObj.setErrorMessage(errorMsg);
        return respObj;
    }

    @Override
    public String toString() {
        return "RespObj{" +
                "code='" + code + '\'' +
                ", message=" + message +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}