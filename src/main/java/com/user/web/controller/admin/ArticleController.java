package com.user.web.controller.admin;

import com.user.persistence.entity.Article;
import com.user.service.ArticleService;
import com.user.web.dto.ArticleDto;
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
@RequestMapping("/admin/articles")
public class ArticleController {

    @Autowired
    Mapper mapper;
    @Autowired
    ArticleService articleService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable) throws Exception {
        model.addAttribute("page", mapper.mapAsPage(articleService.findAll(pageable), ArticleDto.class));
        return "admin/articles/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("articleDto", new ArticleDto());
        return "admin/articles/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@Valid ArticleDto articleDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            Article article = mapper.map(articleDto, Article.class);
            try {
                articleService.save(article);
                return "redirect:/admin/articles";
            } catch (DuplicateKeyException e) {
                bindingResult.rejectValue("uri", "", "Uri must be unique");
            }
        }
        model.addAttribute("articleDto", articleDto);
        return "admin/articles/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable String id, Model model) {
        return articleService.findOne(id).map(article -> {
            model.addAttribute("articleDto", mapper.map(article, ArticleDto.class));
            return "admin/articles/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable String id, @Valid ArticleDto articleDto, BindingResult bindingResult, Model model) throws IOException {
        return articleService.findOne(id).map(article -> {
            if (!bindingResult.hasErrors()) {
                mapper.map(articleDto, article);
                try {
                    articleService.save(article);
                    return "redirect:/admin/articles";
                } catch (DuplicateKeyException e) {
                    bindingResult.rejectValue("uri", "", "Uri must be unique");
                }
            }
            model.addAttribute("userDto", articleDto);
            return "admin/articles/edit";
        }).orElse("errors/404");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        return articleService.findOne(id).map(article -> {
            articleService.delete(article);
            return "redirect:/admin/articles";
        }).orElse("errors/404");
    }
}
