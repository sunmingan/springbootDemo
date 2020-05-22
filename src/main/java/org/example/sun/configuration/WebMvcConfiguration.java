package org.example.sun.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;

/**
 * @author : sunmignan
 * @date : 2020
 *
 */
@Configuration
@Profile(value = {"dev"})
public class WebMvcConfiguration implements WebMvcConfigurer {


    public static final List<String> loginRequiredPathList = new ArrayList<>();
    public static final List<String> loginOptionalPathList = new ArrayList<>();

    static{
        loginRequiredPathList.add("/*/api/private/**");
        loginRequiredPathList.add("/api/private/**");

        loginOptionalPathList.add("/*/api/public/**");
        loginOptionalPathList.add("/api/public/**");
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
       // String[] excludes = new String[]{"/","/static/**","/service/**","/articlepicture/**","/headpicture/**","/institution/**","/photo/**"};

        registry.addInterceptor(new JWTAuthorize()).addPathPatterns(loginRequiredPathList).excludePathPatterns(loginOptionalPathList);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // 允许跨域访问的路径
                .allowedOrigins("*")    // 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")    // 允许请求方法
                .maxAge(168000)    // 预检间隔时间
                .allowedHeaders("*")  // 允许头部设置
                .allowCredentials(true);    // 是否发送cookie
    }

    /**
     * 处理返回string 数据时候 出现转换的问题
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }



}