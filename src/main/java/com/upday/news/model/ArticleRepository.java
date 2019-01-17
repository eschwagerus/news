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

    List<Article> findByAuthors(String author);

    List<Article> findByKeywords(String keyword);

    List<Article> findByPublishDateBetween(Date from, Date to);
}