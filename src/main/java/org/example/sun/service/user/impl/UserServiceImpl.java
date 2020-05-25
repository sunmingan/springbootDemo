package org.example.sun.service.user.impl;

import org.example.sun.configuration.JwtHelper;
import org.example.sun.configuration.exception.CommException;
import org.example.sun.mapper.user.UserMapper;
import org.example.sun.model.user.User;
import org.example.sun.service.user.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }

    @Override
    public Map<String, Object> get(User user) {
        List<User> userList = userMapper.getUserList(user);

        if(userList == null || userList.size() < 1){
            throw new CommException("不存在当前用户！");
        }

        User u = userList.get(0);
        long expiredTime = DateTime.now().plusDays(7).getMillis();
        Map<String,Object> tokenData = new HashMap<>();
        tokenData.put("userId",u.getId());
        String token = JwtHelper.createToken(tokenData,expiredTime);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return map;
    }
}
