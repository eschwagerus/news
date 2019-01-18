package com.upday.news.error;

/**
 * Business Exception. Indicates, that an article could not be found.
 */
public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String message) {
        super(message);
    }
}