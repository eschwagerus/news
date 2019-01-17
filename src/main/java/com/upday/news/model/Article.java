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
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@ApiObject(name = "Article", description = "Datastructure to represent an article.")
public class Article {

    @Id
    @ApiObjectField(description = "The unique identifier of an article. Generated automatically by db - do not set!.")
    String articleId;

    @NotBlank(message = "The header must not be blank.")
    @ApiObjectField(description = "The header of the article.", required = true)
    String header;

    @NotBlank(message = "The short description must not be blank.")
    @ApiObjectField(description = "A short text describing the article.", required = true)
    String shortDescription;

    @NotBlank
    @ApiObjectField(description = "The article / content itself.", required = true)
    String text;

    @NotNull(message = "The publish date must be set.")
    @ApiObjectField(description = "The publishDate of the article.", required = true)
    Date publishDate;

    @NotEmpty(message = "At least one author must be set.")
    @ApiObjectField(description = "A list of authors. At least one must be set.", required = true)
    List<String> authors;

    @NotEmpty(message = "At least one keyword must be set.")
    @ApiObjectField(description = "A list of keywords for this article. At least one must be set", required = true)
    List<String> keywords;
}