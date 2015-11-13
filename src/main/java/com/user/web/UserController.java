package com.user.web;

import com.user.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index(Model model, @PageableDefault(size = 1) Pageable pageable) throws Exception {
        model.addAttribute("page", userRepository.findAll(pageable));
        return "users/index";
    }

}
