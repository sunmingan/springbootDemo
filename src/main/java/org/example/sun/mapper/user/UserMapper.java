package org.example.sun.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.example.sun.model.user.User;

import java.util.List;

public interface UserMapper {

    List<User> getAll();

    User getOne(User user);

    List<User> getUserList( User user);
}
