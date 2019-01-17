package com.upday.news.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upday.news.model.Article;
import com.upday.news.model.ArticleRepository;

@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ArticleRepository articleRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Article create(@RequestBody Article article) {

        log.debug("Creating new article: %", article);
        return articleRepository.insert(article);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Article update(@RequestBody Article article) {

        log.debug("Updating existing article: %", article);
        return articleRepository.save(article);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String articleId) {

        log.debug("Deleting existing article: %", articleId);
        articleRepository.deleteById(articleId);
        return "";
    }

    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public Article display(String articleId) {

        Optional<Article> articleById = articleRepository.findById(articleId);
        if(articleById.isPresent()) {
            log.debug("Displaying article: %", articleById.get());
            return articleById.get();
        } else {
            // TODO: return 404 / message
            return new Article();
        }
    }

    @RequestMapping(value = "/listForAuthor", method = RequestMethod.GET)
    public List<Article> listForAuthor() {
        return null;
    }

    @RequestMapping(value = "/listForPeriod", method = RequestMethod.GET)
    public List<Article> listForPeriod() {
        return null;
    }

    @RequestMapping(value = "/findByKeyword", method = RequestMethod.GET)
    public List<Article> findByKeyword() {
        return null;
    }

}