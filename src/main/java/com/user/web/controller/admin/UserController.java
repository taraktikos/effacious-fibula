package com.user.web.controller.admin;

import com.user.persistence.entity.User;
import com.user.service.UserService;
import com.user.web.dto.UserDto;
import com.user.web.mapping.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    Mapper mapper;
    @Autowired
    UserService userService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable) throws Exception {
        model.addAttribute("page", mapper.mapAsPage(userService.findAll(pageable), UserDto.class));
        return "admin/users/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", User.Roles.values());
        return "admin/users/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            User user = mapper.map(userDto, User.class);
            try {
                userService.save(user);
                if (!userDto.getPhotoVirtual().isEmpty()) {
                    user.setPhoto(userService.savePicture(user, userDto.getPhotoVirtual()));
                    userService.save(user);
                }
                return "redirect:/admin/users";
            } catch (DuplicateKeyException e) {
                bindingResult.rejectValue("email", "", "Email must be unique");
            }
        }
        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", User.Roles.values());
        return "admin/users/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable String id, Model model) {
        return userService.findOne(id).map(user -> {
            model.addAttribute("userDto", mapper.map(user, UserDto.class));
            model.addAttribute("roles", User.Roles.values());
            return "admin/users/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable String id, @Valid UserDto userDto, BindingResult bindingResult, Model model) throws IOException {
        return userService.findOne(id).map(user -> {
            if (!bindingResult.hasErrors()) {
                mapper.map(userDto, user);
                if (!userDto.getPhotoVirtual().isEmpty()) {
                    userService.deletePicture(user);
                    user.setPhoto(userService.savePicture(user, userDto.getPhotoVirtual()));
                }
                try {
                    userService.save(user);
                    return "redirect:/admin/users";
                } catch (DuplicateKeyException e) {
                    bindingResult.rejectValue("email", "", "Email must be unique");
                }
            }
            model.addAttribute("userDto", userDto);
            model.addAttribute("roles", User.Roles.values());
            return "admin/users/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        return userService.findOne(id).map(user -> {
            userService.delete(user);
            return "redirect:/admin/users";
        }).orElse("errors/404");
    }

}
