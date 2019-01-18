package com.upday.news.error;

import java.util.Date;

import lombok.Data;

/**
 * This class contains information returned by tha API in case of an Exception.
 * (Invalid data, article not found, etc).
 * It's meant to contain "userfriendly" readable information about the issue.
 * No technical details like stacktraces should be included.
 */
@Data
public class ErrorDetails {

    /**
     * When did the error occur.
     */
    private Date timestamp;

    /**
     * Details: Short text describing the error.
     */
    private String message;

    /**
     * More details on error and how to fix it if available.
     */
    private String details;

    /**
     * The rest method URL that produced the error.
     */
    private String path;
}