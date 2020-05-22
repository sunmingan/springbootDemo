package org.example.sun.configuration.paramter;

import org.example.sun.configuration.annotation.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: suanjin
 * @create: 18/12/2017
 *
 */
public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodArgumentResolver.class);

    private Map<MediaType, IArgumentResolver> resolverMap = new HashMap<>();

    /**
     * addResolver
     */
    public void addResolver(MediaType mediaType, IArgumentResolver resolver) {
        if (null != mediaType) {
            resolverMap.put(mediaType, resolver);
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ArgumentResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest request = new ServletServerHttpRequest(nativeRequest);
        MediaType contentType = request.getHeaders().getContentType();
        LOGGER.info("contentType: {}", contentType);
        IArgumentResolver argumentResolver = resolverMap.get(contentType);
        if (argumentResolver != null) {
            return argumentResolver.resolveArgument(methodParameter, nativeWebRequest);
        }
        throw new RuntimeException("unsupported media type " + contentType);
    }
}
