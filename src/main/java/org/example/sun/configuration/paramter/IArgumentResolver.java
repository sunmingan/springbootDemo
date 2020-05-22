package org.example.sun.configuration.paramter;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @desc:
 * @author: suanjin
 * @create: 18/12/2017
 *
 */
public interface IArgumentResolver {

    /**
     * 映射对象
     *
     * @param methodParameter 方法参数
     * @param nativeWebRequest http请求
     * @return parameterType对象实例
     */
    Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception;

}
