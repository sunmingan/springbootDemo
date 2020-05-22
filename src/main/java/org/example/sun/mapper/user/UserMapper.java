package org.example.sun.mapper.user;

import org.example.sun.model.user.User;

import java.util.List;

public interface UserMapper {

    List<User> getAll();

    User getOne(long id);
}
