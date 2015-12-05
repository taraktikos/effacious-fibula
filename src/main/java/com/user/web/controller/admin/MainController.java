package com.user.web.controller.admin;

import com.user.service.MediaService;
import com.user.service.UserService;
import com.user.web.mapping.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/admin")
public class MainController {

    @Autowired
    Mapper mapper;
    @Autowired
    UserService userService;
    @Autowired
    MediaService mediaService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index() {
        return "admin/main/index";
    }

    @RequestMapping(value = "/cache/clear", method = RequestMethod.GET)
    public String cacheClear() throws IOException {
        mediaService.cacheClear();
        return "redirect:/admin";
    }
}
