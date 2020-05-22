package org.example.sun.configuration.exception;

/**
 * @desc:: 错误码定义
 * @author: suanjin
 * @date: 18/12/2017 10:35 AM
 *
 */
public enum CommErrors {
	
	API_SUB_WEIXIN(100888, "重复关注"),
	API_OPENID_NULL(100889, "此用户没有人居openId存在库里"),

    //COMM常用的定义
    API_SUCCESS(1, "成功"),
    API_ERROR(-1, "系统繁忙，请稍后重试"),
    API_NOT_EXIST(10001, "请求接口(%s)不存在"),
    LOGIN_AUTH_FAIR(401, "登录已过期"),
    MISSING_PARAM(80000,"缺少参数"),
    ;

    private int errorCode;
    private String errorMsg;

    CommErrors(int code, String message) {
        this.errorCode = code;
        this.errorMsg = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
