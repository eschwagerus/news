package com.upday.news.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The mongo repository encapsulates all actual data access like crud and finder methods.
 * Note that the basic crud and finder methods are already implemented in the base classes.
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    /**
     * Returns all articles where the author is included in the list of authors.
     *
     * @param author name of the author to look for
     * @return a list of authors - empty if no match
     */
    List<Article> findByAuthors(String author);

    /**
     * Returns all articles containing a certain keyword.
     *
     * @param keyword one keyword to search for
     * @return a list of matching articles - empty if no match
     */
    List<Article> findByKeywords(String keyword);

    /**
     * Returns all articles within a certain time period.
     *
     * @param from the start date (included)
     * @param to the end date (excluded)
     * @return a list of matching articles - empty if no match
     */
    List<Article> findByPublishDateBetween(Date from, Date to);
}
