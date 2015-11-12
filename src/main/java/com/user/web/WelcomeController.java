package com.user.web;

import com.google.common.collect.Lists;
import com.user.persistence.entity.User;
import com.user.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

@Slf4j
@Controller
@RequestMapping("/")
public class WelcomeController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index() throws Exception {
        return "welcome/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() throws Exception {
        return "users/login";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public String generate() throws Exception {
        User admin = User.builder()
                .email("admin@gmail.com")
                .password("123456")
                .firstName("Firstname")
                .lastName("lastname")
                .roles(Collections.singletonList(User.Roles.ROLE_ADMIN))
                .build();
        User user = User.builder()
                .email("user@gmail.com")
                .password("123456")
                .firstName("User")
                .lastName("lastname")
                .roles(Collections.singletonList(User.Roles.ROLE_USER))
                .build();
        userRepository.deleteAll();
        log.debug("Delete all users");
        userRepository.save(Lists.newArrayList(user, admin));
        log.debug("Create 2 users 'admin' and 'user'");
        return "welcome/index";
    }

}
