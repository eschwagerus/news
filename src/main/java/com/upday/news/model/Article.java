package com.upday.news.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Article {

    @Id
    String articleId;

    @NotBlank(message = "The header must not be blank.")
    String header;

    @NotBlank(message = "The short description must not be blank.")
    String shortDescription;

    @NotBlank
    String text;

    @NotNull(message = "The publish date must be set.")
    Date publishDate;

    @NotEmpty(message = "At least one author must be set.")
    List<String> authors;

    @NotEmpty(message = "At least one keyword must be set.")
    List<String> keywords;
}