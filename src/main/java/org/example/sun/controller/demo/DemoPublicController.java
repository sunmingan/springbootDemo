package org.example.sun.controller.demo;


import org.example.sun.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/api/public/1.0/demo")
public class DemoPublicController {

    @Resource
    RedisUtil redisUtil;

    @RequestMapping("/redis")
    @ResponseBody
    public String redis(){
        String key = "rediskey1";

        //redisTemplate.opsForValue().set(key,value);
        //redisTemplate.opsForValue().get("sunmingan");

        if(!redisUtil.hasKey(key)){
            redisUtil.set(key, "你好我是心来的",1000);
        }
        return redisUtil.get(key).toString();

    }
}
