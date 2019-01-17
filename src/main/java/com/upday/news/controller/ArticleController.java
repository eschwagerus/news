package com.upday.news.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upday.news.model.Article;
import com.upday.news.model.ArticleRepository;

@RestController
@RequestMapping(value = "/article")
@Api(name = "Articles", description = "This Rest base API lets you create, modify, and search for articles.")
public class ArticleController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ArticleRepository articleRepository;

    @ApiMethod(description = "This method stores a new article in the database." +
            "The newly created article is returned on success. " +
            "Note that the result contains the generated articleId.")
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

    @ApiMethod(description = "This method shows all details of a certain article.")
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public Article display(@ApiQueryParam(description = "The id of the article to display.", name = "articleId")
                           @RequestParam
                           String articleId) {

        Optional<Article> articleById = articleRepository.findById(articleId);
        if (articleById.isPresent()) {
            log.debug("Displaying article: %", articleById.get());
            return articleById.get();
        } else {
            // TODO: return 404 / message
            return new Article();
        }
    }

    @ApiMethod(description = "This method returns all articles for this author.")
    @RequestMapping(value = "/listForAuthor", method = RequestMethod.GET)
    public List<Article> listForAuthor(@ApiQueryParam(description = "The name of the author.", name = "author")
                                       @RequestParam String author) {
        return articleRepository.findByAuthors(author);
    }

    @RequestMapping(value = "/listForPeriod", method = RequestMethod.GET)
    public List<Article> listForPeriod(Date from, Date to) {
        return articleRepository.findByPublishDateBetween(from, to);
    }

    @ApiMethod(description = "This method returns all articles with a certain keyword.")
    @RequestMapping(value = "/findByKeyword", method = RequestMethod.GET)
    public List<Article> findByKeyword(@ApiQueryParam(description = "The keyword to look for.", name = "keyword")
                                       @RequestParam String keyword) {
        return articleRepository.findByKeywords(keyword);
    }

}