package com.upday.news.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upday.news.error.ArticleNotFoundException;
import com.upday.news.model.Article;
import com.upday.news.model.ArticleRepository;

@Service
public class ArticleService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ArticleRepository articleRepository;

    /**
     * This method stores a new article in the database. The newly created article is returned on success.
     * Note that the result contains the generated articleId.
     *
     * @param article a valid article
     * @return the saved article including a generated articleId
     */
    public Article create(@NotNull Article article) {
        return articleRepository.insert(article);
    }

    /**
     * This method updates an existing article. Identifier is the articleId.
     *
     * @param article a valid article
     * @return the updated article
     */
    public Article update(@NotNull Article article) {

        Optional<Article> currentArticle = articleRepository.findById(article.getArticleId());

        if (currentArticle.isPresent()) {
            return articleRepository.save(article);
        } else {
            throw new ArticleNotFoundException(("The article was not found and therefore not be deleted: ")
                    + article.getArticleId());
        }
    }

    /**
     * This method deletes an existing article.
     *
     * @param articleId The id of the article to display.
     * @return the deleted article
     */
    public Article delete(@NotBlank String articleId) {

        Optional<Article> article = articleRepository.findById(articleId);

        if (article.isPresent()) {
            articleRepository.delete(article.get());
            return article.get();
        } else {
            throw new ArticleNotFoundException(("The article was not found and therefore not be deleted: ")
                    + articleId);
        }
    }

    /**
     * This method finds an article by its id.
     *
     * @param articleId The id of the article to display.
     * @return article with specified id
     */
    public Article findById(@NotBlank String articleId) {

        Optional<Article> articleById = articleRepository.findById(articleId);
        if (articleById.isPresent()) {
            log.debug("Displaying article: {}", articleById.get());
            return articleById.get();
        } else {
            throw new ArticleNotFoundException("ArticleId not found in DB: " + articleId);
        }
    }

    /**
     * This method returns all articles for this author.
     *
     * @param author The name of the author.
     * @return list of articles for this author
     */
    public List<Article> findByAuthor(@NotBlank String author) {
        return articleRepository.findByAuthors(author);
    }

    /**
     * This method returns all articles for this author.
     *
     * @param from Startdate of period, included
     * @param to   Enddate of period, excluded
     * @return list of articles including this author
     */
    public List<Article> findByPublishDateBetween(@NotNull Date from, @NotNull Date to) {
        return articleRepository.findByPublishDateBetween(from, to);
    }

    /**
     * This method returns all articles with a certain keyword.
     *
     * @param keyword The keyword to look for.
     * @return list of articles including this keyword
     */
    public List<Article> findByKeyword(@NotBlank String keyword) {
        return articleRepository.findByKeywords(keyword);
    }

}
