package org.example.sun.configuration.http;


import org.example.sun.configuration.exception.CommErrors;
import org.example.sun.configuration.exception.CommException;
import org.example.sun.configuration.exception.StatusCodeException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServletServerHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: suanjin
 * @create: 18/12/2017
 *
 */
@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GlobalResponseBodyAdvice.class);


    public GlobalResponseBodyAdvice() {
    }

    /**
     * 响应参数注入支持
     *
     * @param methodParameter
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class clazz) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType,
            Class clazz, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Object response = null;
        if (methodParameter.hasMethodAnnotation(ExceptionHandler.class)) {
            response = (HttpResponse) obj;
        } else {
            response = HttpResponse.setSuccess(obj);
        }
//        LOGGER.debug("response {}", response);
        System.out.println(response);
        return response != null ? response : HttpResponse.setFail();
    }

    /**
     * @desc: 接口不存在；404 处理
     * @date: 19/12/2017 9:43 AM
     * @param: NoHandlerFoundException
     *
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public HttpResponse noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        LOGGER.error("noHandlerFoundException...");
        CommErrors errors = CommErrors.API_NOT_EXIST;
        return HttpResponse.setFail(errors.getErrorCode(), String.format(errors.getErrorMsg(), ex.getRequestURL()));
    }

    /**
     * @desc: 捕捉异常处理
     * @date: 19/12/2017 9:52 AM
     * @param: Exception
     *
     */
    @ExceptionHandler(value = Exception.class)
    public HttpResponse commExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception ex) {

        if (ex instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException) ex;
            response.setStatus(statusCodeException.getHttpStatusCode());
            //LOGGER.error("StatusCodeException: {}", statusCodeException.toString());
            LOGGER.error("StatusCodeException: {}", statusCodeException);
            return HttpResponse.setFail(statusCodeException.getCode(), statusCodeException.getMessage());
        }

        if (ex instanceof CommException) {
            CommException commEx = null;
            commEx = (CommException) ex;
            //LOGGER.error("CommException: {}", commEx.toString());
            LOGGER.error("CommException: {}", commEx);
            return HttpResponse.setFail(commEx.getCode(), commEx.getMessage());
        }

        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e = (MissingServletRequestParameterException) ex;
            //LOGGER.error("MissingServletRequestParameterException: {}", e.toString());
            LOGGER.error("MissingServletRequestParameterException: {}", e);
            return HttpResponse.setFail(CommErrors.MISSING_PARAM.getErrorCode(), CommErrors.MISSING_PARAM.getErrorMsg() + ":" + e.getParameterName());
        }

        //LOGGER.error("Exception:{}", ex.toString());
        LOGGER.error("Exception:{}", ex);
        return HttpResponse.setFail();  //系统错误，请稍后重试

    }

}
