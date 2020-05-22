package org.example.sun.configuration;


import org.example.sun.configuration.exception.CommErrors;
import org.example.sun.configuration.exception.CommException;
import io.jsonwebtoken.Claims;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luoluo
 * @version 1.0
 * @email 15360801546@163.com
 * @date 创建时间：2018-1-16 16:44:16
 */
public class JWTAuthorize implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return authorized(request, request.getHeader("Authorization"));
    }

    public static boolean authorized(HttpServletRequest request, String auth) {
        if (authorizedNoException(request, auth)) {
            return true;
        }
        throw new CommException(CommErrors.LOGIN_AUTH_FAIR);
//        throw new StatusCodeException(HttpServletResponse.SC_UNAUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED, "登录验证失败");
    }

    public static boolean authorizedNoException(HttpServletRequest request, String auth) {


        String path = request.getRequestURI();
        boolean b = false;
        for(String item:WebMvcConfiguration.loginOptionalPathList){
            if(path.contains(item)){
                b = true;
                break;
            }
        }

        if(b == true && StringHelper.isNullOrEmptyString(auth)){
            return true;
        }

        if ((auth != null) && (auth.length() > 7)) {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0) {
                auth = auth.substring(7, auth.length());
                Claims claims = JwtHelper.parseToken(auth);
                if (claims != null) {
                    if (claims.get("userId") == null) {
                        return false;
                    }
                    request.setAttribute("userId", claims.get("userId"));
                    // 可以加入更多的参数 包括版本等
                    return true;
                }
            }
        }
        return false;
    }

}