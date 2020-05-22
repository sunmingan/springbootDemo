package org.example.sun.controller.user;


import org.example.sun.configuration.JwtHelper;
import org.example.sun.configuration.exception.CommException;
import org.example.sun.model.user.User;
import org.example.sun.service.user.UserService;
import org.example.sun.utils.AccessToken;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/public/1.0/user")
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public Object login(@RequestBody User user){
        if(user == null){
            throw new CommException("缺少参数");
        }
        if(!"admin".equals(user.getUserName())){
            throw new CommException("没有当前账号！");
        }

        long expiredTime = DateTime.now().plusDays(7).getMillis();
        Map<String,Object> tokenData = new HashMap<>();
        tokenData.put("userId","111234");
        String token = JwtHelper.createToken(tokenData,expiredTime);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return map;
    }


}
