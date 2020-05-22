package org.example.sun.configuration.http;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.example.sun.configuration.exception.CommErrors;
import org.example.sun.configuration.exception.CommException;
import org.example.sun.configuration.exception.StatusCodeException;
import org.example.sun.utils.JacksonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * @desc:
 * @author: suanjin
 * @create: 18/12/2017
 *
 */
@RestControllerAdvice
public class GlobalRequestBodyAdvice extends RequestBodyAdviceAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GlobalRequestBodyAdvice.class);

    /**
     * 针对外呼模块的请求参数缓存
     */
    static final ThreadLocal<Map<String, Object>> CALL_REQUEST_COMMON_PARAM = new ThreadLocal<>();

    @Autowired
    private HttpServletRequest req;

    public GlobalRequestBodyAdvice() {
    }

    /**
     * 格式化请求参数，如果是话机请求，去除多余的公共参数
     *
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, //
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (!req.getMethod().equalsIgnoreCase("OPTIONS")) {
            byte[] bytes = ArrayUtils.clone(StreamUtils.copyToByteArray(inputMessage.getBody()));
            Map requestParam = JacksonUtils.getObjectMapper().readValue(ArrayUtils.clone(bytes), Map.class);

                    
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    if (requestParam.get("request_data") != null) {
                        return IOUtils.toInputStream(JacksonUtils.toJson(requestParam.get("request_data")), "UTF-8");
                    }
                    
                    return IOUtils.toInputStream(JacksonUtils.toJson(requestParam), "UTF-8");
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        }
        return inputMessage;
    }

    /**
     * 请求参数注入支持
     *
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
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
