package com.user.web.controller;

import com.user.persistence.repository.UserRepository;
import com.user.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/")
public class WelcomeController {

    @Autowired
    MediaService mediaService;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index() throws Exception {
        return "welcome/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() throws Exception {
        return "welcome/login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() throws Exception {
        return "welcome/profile";
    }

    @RequestMapping(value = "/profile/test", method = RequestMethod.GET)
    public String profileTest() throws Exception {
        return "welcome/profileTest";
    }

    @ResponseBody
    @RequestMapping(value = "/media/cache/{param}/**", method = RequestMethod.GET)
    public byte[] media(@PathVariable String param, HttpServletRequest request) throws Exception {
        String requestURI = request.getRequestURI();
        String path = requestURI.split(param)[1];
        Path generated = mediaService.generate(param, path);
        if (Objects.isNull(generated)) {
            return null;
        }
        return Files.readAllBytes(generated);
    }

    @ResponseBody
    @RequestMapping(value = "/media/cache/{param}/{entity}/{id}/{name:.*}", method = RequestMethod.GET)
    public byte[] mediaCache(@PathVariable String param, @PathVariable String entity, @PathVariable String id, @PathVariable String name) throws IOException {
        Path generated = mediaService.generate(param, entity, id, name);
        if (Objects.isNull(generated)) {
            return null;
        }
        return Files.readAllBytes(generated);
    }

}
