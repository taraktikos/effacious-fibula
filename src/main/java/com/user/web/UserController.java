package com.user.web;

import com.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index() throws Exception {
//        userRepository.save(User.builder()
//                .email("admin@gmail.com")
//                .password("123456")
//                .firstName("Firstname")
//                .lastName("lastname")
//                .roles(Collections.singletonList(User.Roles.ROLE_ADMIN))
//                .build());
        return "users/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() throws Exception {
        return "users/login";
    }

}
