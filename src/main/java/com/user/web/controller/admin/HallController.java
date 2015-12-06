package com.user.web.controller.admin;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import com.user.persistence.entity.Hall;
import com.user.persistence.entity.Hall.Image;
import com.user.service.HallService;
import com.user.service.MediaService;
import com.user.web.dto.HallDto;
import com.user.web.mapping.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/admin/halls")
public class HallController {

    @Autowired
    Mapper mapper;
    @Autowired
    HallService hallService;
    @Autowired
    MediaService mediaService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable) throws Exception {
        model.addAttribute("page", mapper.mapAsPage(hallService.findAll(pageable), HallDto.class));
        return "admin/halls/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("hallDto", new HallDto());
        return "admin/halls/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@Valid HallDto hallDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            Hall hall = mapper.map(hallDto, Hall.class);
            try {
                hallService.save(hall);
                return "redirect:/admin/halls";
            } catch (DuplicateKeyException e) {
                bindingResult.rejectValue("uri", "", "Uri must be unique");
            }
        }
        model.addAttribute("hallDto", hallDto);
        return "admin/halls/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable String id, Model model) {
        return hallService.findOne(id).map(hall -> {
            model.addAttribute("hallDto", mapper.map(hall, HallDto.class));
            return "admin/halls/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable String id, @Valid HallDto hallDto, BindingResult bindingResult, Model model) {
        return hallService.findOne(id).map(hall -> {
            if (!bindingResult.hasErrors()) {
                mapper.map(hallDto, hall);
                try {
                    hallService.save(hall);
                    return "redirect:/admin/halls";
                } catch (DuplicateKeyException e) {
                    bindingResult.rejectValue("uri", "", "Uri must be unique");
                }
            }
            model.addAttribute("hallDto", hallDto);
            return "admin/halls/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    public String images(@PathVariable String id, Model model) {
        return hallService.findOne(id).map(hall -> {
            model.addAttribute("hall", hall);
            return "admin/halls/images";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.POST)
    public String saveImages(@PathVariable String id, @RequestParam("images") List<MultipartFile> images, Model model) {
        return hallService.findOne(id).map(hall -> {
            List<Object> errors = Lists.newArrayList();
            if (images.size() == 1 && images.get(0).getOriginalFilename().isEmpty()) {
                images.clear();
            }
            if (!images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (image.getSize() > 3 * 1024 * 1024) { //3Mb todo use validator
                        errors.add(image.getOriginalFilename() + " greater than 3Mb");
                    }
                    if (!Lists.newArrayList("image/jpeg", "image/png").contains(image.getContentType().toLowerCase())) {
                        errors.add(image.getOriginalFilename() + " has incorrect type");
                    }
                }
            } else {
                errors.add("Please select image");
            }
            if (errors.isEmpty()) {
                for (MultipartFile image : images) {
                    DBObject metadata = new BasicDBObject();
                    metadata.put("entity", "hall");
                    metadata.put("id", id);
                    try {
                        GridFSFile file = mediaService.storeWithRandomName(
                                image.getInputStream(),
                                image.getOriginalFilename(),
                                image.getContentType(),
                                metadata
                        );
                        Image hallImage = Image.builder().id((ObjectId) file.getId()).name(file.getFilename()).build();
                        hall.getImages().add(hallImage);
                    } catch (IOException e) {
                        errors.add(e.getMessage());
                    }
                }
                hallService.save(hall);
                return "redirect:/admin/halls/images/" + id;
            }
            model.addAttribute("hall", hall);
            model.addAttribute("errors", errors);
            return "admin/halls/images";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/{id}/images/delete/{imageId}", method = RequestMethod.GET)
    public String deleteImage(@PathVariable String id, @PathVariable String imageId) {
        return hallService.findOne(id).map(hall -> {
            hall.getImages().removeIf(image -> image.getId().toString().equals(imageId));
            mediaService.delete(imageId);
            hallService.save(hall);
            return "redirect:/admin/halls/images/" + id;
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/{id}/images/main/{imageId}", method = RequestMethod.GET)
    public String setMainImage(@PathVariable String id, @PathVariable String imageId) {
        return hallService.findOne(id).map(hall -> {
            hall
                .getImages()
                .stream()
                .filter(image -> image.getId().toString().equals(imageId))
                .findFirst()
                .ifPresent(image -> {
                    hall.getImages().forEach(i -> i.setMain(false));
                    image.setMain(true);
                    hallService.save(hall);
                });
            return "redirect:/admin/halls/images/" + id;
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        return hallService.findOne(id).map(hall -> {
            hallService.delete(hall);
            return "redirect:/admin/halls";
        }).orElse("errors/404");
    }
}
