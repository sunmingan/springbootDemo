package org.example.sun.configuration.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Http状态码异常, 扩展了原来CommException,除了可以设置code属性之外还可以设置状态码 CommException状态码为200
 * Created by IssacChow on 18/3/6.
 */
public class StatusCodeException extends CommException {

    private int httpStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public StatusCodeException(int code, String message) {
        super(code, message);
    }

    public StatusCodeException(int httpStatusCode, int code, String message) {
        this(code, message);
        this.httpStatusCode = httpStatusCode;
    }
}
