package com.upday.news.error;

/**
 * Business Exception. Indicates, that an article could not be found.
 */
public class ArticleNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * @param message a meaningful message about the Exception
     */
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
