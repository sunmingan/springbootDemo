package org.example.sun.service.user.impl;

import org.example.sun.mapper.user.UserMapper;
import org.example.sun.model.user.User;
import org.example.sun.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }

    @Override
    public User get(User user) {
        return userMapper.getOne(1l);
    }
}
