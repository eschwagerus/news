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

/**
 * The main data structure of an article.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@ApiObject(name = "Article", description = "Datastructure to represent an article.")
public class Article {

    @Id
    @ApiObjectField(description = "The unique identifier of an article. Generated automatically by db - do not set!.")
    private String articleId;

    @NotBlank(message = "The header must not be blank.")
    @ApiObjectField(description = "The header of the article.", required = true)
    private String header;

    @NotBlank(message = "The short description must not be blank.")
    @ApiObjectField(description = "A short text describing the article.", required = true)
    private String shortDescription;

    @NotBlank
    @ApiObjectField(description = "The article / content itself.", required = true)
    private String text;

    @NotNull(message = "The publish date must be set.")
    @ApiObjectField(description = "The publishDate of the article.", required = true)
    private Date publishDate;

    @NotEmpty(message = "At least one author must be set.")
    @ApiObjectField(description = "A list of authors. At least one must be set.", required = true)
    private List<String> authors;

    @NotEmpty(message = "At least one keyword must be set.")
    @ApiObjectField(description = "A list of keywords for this article. At least one must be set", required = true)
    private List<String> keywords;
}
