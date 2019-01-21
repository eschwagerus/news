package com.upday.news.controller;

import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.upday.news.error.ArticleNotFoundException;
import com.upday.news.error.ErrorDetails;
import com.upday.news.model.Article;
import com.upday.news.service.ArticleService;

@RestController
@RequestMapping(value = "/article")
@Api(name = "Articles", description = "This Rest base API lets you create, modify, and search for articles.")
public class ArticleController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;

    @ApiMethod(description = "This method stores a new article in the database."
            + "The newly created article is returned on success. "
            + "Note that the result contains the generated articleId.")
    @ApiErrors(apierrors = {
            @ApiError(code = "400 - Bad Request", description = "The given article is incomplete or invalid. E.g. missing field."),
            @ApiError(code = "400 - Bad Request", description = "The given article json has a syntax error.")
    })
    @PostMapping(value = "/create")
    public Article create(@RequestBody Article article) {

        log.debug("Creating new article: {}", article);
        return articleService.create(article);
    }

    @ApiMethod(description = "This method updates an existing article. Identifier is the articleId.")
    @ApiErrors(apierrors = {
            @ApiError(code = "400 - Bad Request", description = "The given article is incomplete or invalid. E.g. missing field."),
            @ApiError(code = "400 - Bad Request", description = "The given article json has a syntax error.")
    })
    @PutMapping(value = "/update")
    public Article update(@RequestBody Article article) {

        log.debug("Updating existing article: {}", article);
        return articleService.update(article);
    }

    @ApiMethod(description = "This method deletes an existing article.")
    @ApiErrors(apierrors = {
            @ApiError(code = "404 - Not Found", description = "The requested article was not found.")
    })
    @DeleteMapping(value = "/delete")
    public Article delete(@ApiQueryParam(description = "The id of the article to delete.", name = "articleId")
                          @RequestParam
                          String articleId) {

        log.debug("Deleting existing article: {}", articleId);
        return articleService.delete(articleId);
    }

    @ApiMethod(description = "This method shows all details of a certain article.")
    @ApiErrors(apierrors = {
            @ApiError(code = "404 - Not Found", description = "The requested article was not found.")
    })
    @GetMapping(value = "/display")
    public Article display(@ApiQueryParam(description = "The id of the article to display.", name = "articleId")
                           @RequestParam
                           String articleId) {

        log.debug("Displaying article with id: {}", articleId);
        return articleService.findById(articleId);
    }

    @ApiMethod(description = "This method returns all articles for this author.")
    @GetMapping(value = "/listForAuthor")
    public List<Article> listForAuthor(@ApiQueryParam(description = "The name of the author.", name = "author")
                                       @RequestParam
                                       String author) {

        log.debug("Listing articles for author {}", author);
        return articleService.findByAuthor(author);
    }

    @ApiMethod(description = "This method returns all articles for this author.")
    @GetMapping(value = "/listForPeriod")
    public List<Article> listForPeriod(@ApiQueryParam(description = "Startdate of period, included.", name = "from")
                                       @RequestParam
                                       Long from,
                                       @ApiQueryParam(description = "Enddate of period, excluded.", name = "to")
                                       @RequestParam
                                       Long to) {
        return articleService.findByPublishDateBetween(new Date(from), new Date(to));
    }

    @ApiMethod(description = "This method returns all articles with a certain keyword.")
    @GetMapping(value = "/findByKeyword")
    public List<Article> findByKeyword(@ApiQueryParam(description = "The keyword to look for.", name = "keyword")
                                       @RequestParam String keyword) {
        return articleService.findByKeyword(keyword);
    }

    /**
     * An ExceptionHandler for this controller. All validation Exceptions will return a simplified ErrorDetails
     * message. HTTP status code is 400 - Bad request.
     *
     * @param ex      the thrown Exception
     * @param request information on the original request
     * @return ErrorDetails as json String
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetails> handleInputValidationException(ValidationException ex, WebRequest request) {

        return new ResponseEntity<>(new ErrorDetails(
                new Date(),
                ex.getMessage(),
                "The given data is incomplete or invalid.",
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * An ExceptionHandler for this controller. All validation JsonProcessingException will return a simplified
     * ErrorDetails message. HTTP status code is 400 - Bad request.
     *
     * @param ex      the thrown Exception
     * @param request information on the original request
     * @return ErrorDetails as json String
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorDetails> handleJsonSyntaxException(JsonProcessingException ex, WebRequest request) {

        return new ResponseEntity<>(new ErrorDetails(
                new Date(),
                ex.getMessage(),
                "The given data is no valid Json.",
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * An ExceptionHandler for this controller. All ArticleNotFoundException will return a simplified
     * ErrorDetails message. HTTP status code is 404 - Not found.
     *
     * @param ex      the thrown Exception
     * @param request information on the original request
     * @return ErrorDetails as json String
     */
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleArticleNotFoundException(ArticleNotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(new ErrorDetails(
                new Date(),
                ex.getMessage(),
                "No article with this id found in database.",
                request.getDescription(false)),
                HttpStatus.NOT_FOUND);
    }

}
