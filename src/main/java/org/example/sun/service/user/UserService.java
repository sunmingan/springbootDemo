package org.example.sun.service.user;

import org.example.sun.model.user.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    List<User> getAll();

    Map get(User user);
}
