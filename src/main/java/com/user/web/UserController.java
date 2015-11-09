package com.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index() throws Exception {
        return "users/login";
    }

}
