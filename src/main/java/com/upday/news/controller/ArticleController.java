package com.upday.news.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.upday.news.error.ArticleNotFoundException;
import com.upday.news.error.ErrorDetails;
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
    @ApiErrors(apierrors = {
            @ApiError(code = "400 - Bad Request", description = "The given article is incomplete or invalid. E.g. missing field."),
            @ApiError(code = "400 - Bad Request", description = "The given article json has a syntax error.")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Article create(@RequestBody Article article) {

        log.debug("Creating new article: %", article);
        return articleRepository.insert(article);
    }

    @ApiMethod(description = "This method updates an existing article. Identifier is the articleId.")
    @ApiErrors(apierrors = {
            @ApiError(code = "400 - Bad Request", description = "The given article is incomplete or invalid. E.g. missing field."),
            @ApiError(code = "400 - Bad Request", description = "The given article json has a syntax error.")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Article update(@RequestBody Article article) {

        log.debug("Updating existing article: %", article);
        return articleRepository.save(article);
    }

    @ApiMethod(description = "This method deletes an existing article.")
    @ApiErrors(apierrors = {
            @ApiError(code = "404 - Not Found", description = "The requested article was not found.")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@ApiQueryParam(description = "The id of the article to display.", name = "articleId")
                         @RequestParam
                         String articleId) {

        log.debug("Deleting existing article: %", articleId);
        articleRepository.deleteById(articleId);
        return "";
    }

    @ApiMethod(description = "This method shows all details of a certain article.")
    @ApiErrors(apierrors = {
            @ApiError(code = "404 - Not Found", description = "The requested article was not found.")
    })
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public Article display(@ApiQueryParam(description = "The id of the article to display.", name = "articleId")
                           @RequestParam
                           String articleId) {

        Optional<Article> articleById = articleRepository.findById(articleId);
        if (articleById.isPresent()) {
            log.debug("Displaying article: %", articleById.get());
            return articleById.get();
        } else {
            throw new ArticleNotFoundException("ArticleId not found in DB: " + articleId);
        }
    }

    @ApiMethod(description = "This method returns all articles for this author.")
    @RequestMapping(value = "/listForAuthor", method = RequestMethod.GET)
    public List<Article> listForAuthor(@ApiQueryParam(description = "The name of the author.", name = "author")
                                       @RequestParam
                                       String author) {
        return articleRepository.findByAuthors(author);
    }

    @ApiMethod(description = "This method returns all articles for this author.")
    @RequestMapping(value = "/listForPeriod", method = RequestMethod.GET)
    public List<Article> listForPeriod(@ApiQueryParam(description = "Startdate of period, included.", name = "from")
                                       @RequestParam
                                       Date from,
                                       @ApiQueryParam(description = "Enddate of period, excluded.", name = "to")
                                       @RequestParam
                                       Date to) {
        return articleRepository.findByPublishDateBetween(from, to);
    }

    @ApiMethod(description = "This method returns all articles with a certain keyword.")
    @RequestMapping(value = "/findByKeyword", method = RequestMethod.GET)
    public List<Article> findByKeyword(@ApiQueryParam(description = "The keyword to look for.", name = "keyword")
                                       @RequestParam String keyword) {
        return articleRepository.findByKeywords(keyword);
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

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setDetails("The given data is incomplete or invalid.");
        errorDetails.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setDetails("The given data is no valid Json.");
        errorDetails.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleArticleNotFoundException(ArticleNotFoundException ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setDetails("No article with this id found in database.");
        errorDetails.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}