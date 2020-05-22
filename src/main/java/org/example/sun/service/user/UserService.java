package org.example.sun.service.user;

import org.example.sun.model.user.User;

import java.util.List;


public interface UserService {

    List<User> getAll();

    User get(User user);
}
