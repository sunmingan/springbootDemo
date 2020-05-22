package org.example.sun.controller.user;


import org.example.sun.model.user.User;
import org.example.sun.service.user.UserService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/private/1.0/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/getAll")
    @ResponseBody
    public String getAll(){
        List<User> list = userService.getAll();

        JSONArray jsonarray = JSONArray.fromObject(list);
        String jsonstr = jsonarray.toString();
        return jsonstr;
    }
}
