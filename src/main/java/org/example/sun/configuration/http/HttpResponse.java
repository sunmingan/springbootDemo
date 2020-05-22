package org.example.sun.configuration.http;

/**
 *
 *
 * @author luoluo
 * @email 15360801546@163.com
 * @version 1.0
 * @date 创建时间：2018-1-19 10:49:01
 */
public class HttpResponse<T> {

    public static final int SUCCESS = 1;
    public static final String SUCCESS_MESSAGE = "成功";
    public static final int ERROR = -1;
    public static final String ERROR_MESSAGE = "系统错误，请稍后重试";

    private Integer code = ERROR;
    private String message = ERROR_MESSAGE;
    private T data = null;

    /**
     * @desc: 设置成功
     * @date: 18/12/2017 7:53 PM
     * @param: null
     *
     */
    public static HttpResponse setSuccess() {
        return new HttpResponse(SUCCESS, SUCCESS_MESSAGE);
    }

    public static <T> HttpResponse setSuccess(T data) {
        return new HttpResponse<T>(SUCCESS, SUCCESS_MESSAGE, data);
    }

    /**
     * @desc: 设置失败
     * @date: 18/12/2017 7:54 PM
     * @param: null
     *
     */
    public static HttpResponse setFail() {
        return setFail(ERROR, ERROR_MESSAGE);
    }

    public static HttpResponse setFail(int errCode, String errMsg) {
        return new HttpResponse(errCode, errMsg);
    }

    private HttpResponse(int errCode, String errMsg) {
        this.code = errCode;
        this.message = errMsg;
    }

    private HttpResponse(int errCode, String errMsg, T data) {
        this.code = errCode;
        this.message = errMsg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "code=" + code + ",message=" + message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
}
