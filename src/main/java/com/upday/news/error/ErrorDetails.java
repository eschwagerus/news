package com.upday.news.error;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
    private String path;
}