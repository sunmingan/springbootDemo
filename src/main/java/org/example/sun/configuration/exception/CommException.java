package org.example.sun.configuration.exception;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @desc: 异常处理
 * @author: suanjin
 * @date: 18/12/2017 10:35 AM
 *
 */
public class CommException extends RuntimeException implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(CommException.class);

    private static final long serialVersionUID = 7771642631672762140L;

    private static final int ERROR_CODE = -1;
    private static final String ERROR_MESSAGE = "未知异常";
    private final Integer code;
    private final String message;

    public CommException(Throwable cause) {
        this.code = ERROR_CODE;
        this.message = ERROR_MESSAGE;

        logger.error(message,cause);
    }

    public CommException(Throwable cause,String msg) {
        this.code = ERROR_CODE;

        if(StringUtils.isNotBlank(msg)){
            message = msg;
        }else{
            this.message = ERROR_MESSAGE;
        }

        logger.error(message,cause);
    }

    public CommException() {
        this.code = ERROR_CODE;
        this.message = ERROR_MESSAGE;
    }

    public CommException(String errMsg) {
        this.code = ERROR_CODE;
        this.message = errMsg;
    }

    public CommException(Integer errCode, String errMsg) {
        this.code = errCode;
        this.message = errMsg;
    }

    public CommException(CommException params) {
        this(params.getCode(), params.getMessage());
    }

    public CommException(CommErrors params) {
        this(params.getErrorCode(), params.getErrorMsg());
    }

    public CommException(CommErrors error, String exMsg) {
        this.code = error.getErrorCode();
        this.message = error.getErrorMsg() + "," + exMsg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "异常编码：" + this.code + "，异常信息：" + this.message;
    }
}
