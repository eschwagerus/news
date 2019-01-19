package com.upday.news.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.ValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This collection of tests verifies the behaviour of the article repository which
 * includes all data access methods for articles (CRUD & additional finder methods)
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class ArticleRepositoryIntegrationTest {

    @Autowired
    ArticleRepository articleRepository;

    /**
     * This method removes all data from the storage before each test is executed.
     */
    @Before
    public void clearRepository() {
        articleRepository.deleteAll();
    }

    /**
     * This test creates a valid article.
     */
    @Test
    public void createArticle_success() {

        // prepare
        Article article = createIndexedDummyArticle(1L);

        // test
        Article articleSaved = articleRepository.insert(article);

        // verify
        Assert.assertNotNull(articleSaved.getArticleId());
        Assert.assertEquals("This is the main text #1", articleSaved.getText());
        Assert.assertEquals(1, articleRepository.count());
    }

    /**
     * This test verifies, that incomplete data is not stored (no authors) and
     * a meaningful response is given.
     */
    @Test
    public void createArticle_failureNoAuthor() {

        // prepare
        Article article = createIndexedDummyArticle(2L);
        article.setAuthors(null);

        // test
        try {
            articleRepository.save(article);
        } catch (ValidationException e) {
            // verify
            Assert.assertEquals("authors: At least one author must be set.", e.getMessage());
            Assert.assertEquals(0, articleRepository.count());
        }
    }

    /**
     * This test first stores an article and then reads it again from the db.
     */
    @Test
    public void updateAndGetArticle_success() {

        // prepare
        Article article = createIndexedDummyArticle(3L);
        article = articleRepository.insert(article);

        // test
        article.setAuthors(Arrays.asList("author7", "author8", "author9"));
        article = articleRepository.save(article);

        // verify
        Article articleById = articleRepository.findById(article.getArticleId()).get();
        Assert.assertEquals(article, articleById);
    }

    /**
     * This test first stores an article and deletes it in the next step.
     */
    @Test
    public void createAndDeleteArticle_success() {

        // prepare
        Article article = createIndexedDummyArticle(4L);
        article = articleRepository.insert(article);
        Long numberOfArticlesAfterInsert = articleRepository.count();

        // test
        articleRepository.deleteById(article.getArticleId());

        // verify
        Assert.assertEquals(1L, numberOfArticlesAfterInsert.longValue());
        Assert.assertEquals(0, articleRepository.count());
    }

    /**
     * This test checks the findByAuthor method.
     */
    @Test
    public void findArticleByAuthor_success() {

        // prepare
        articleRepository.save(createIndexedDummyArticle(1L));
        articleRepository.save(createIndexedDummyArticle(2L));
        articleRepository.save(createIndexedDummyArticle(5L));

        // test
        List<Article> articlesOfAuthor = articleRepository.findByAuthors("author2");

        // verify
        Assert.assertEquals(2, articlesOfAuthor.size());
    }

    /**
     * This test checks the findByKeywords method.
     */
    @Test
    public void findArticleByKeyword_success() {

        // prepare
        articleRepository.save(createIndexedDummyArticle(5L));
        articleRepository.save(createIndexedDummyArticle(6L));
        articleRepository.save(createIndexedDummyArticle(9L));

        // test
        List<Article> articlesOfAuthor = articleRepository.findByKeywords("keyword6");

        // verify
        Assert.assertEquals(2, articlesOfAuthor.size());
    }

    /**
     * This test creates some dummy articles and verifies the findByPublishDate method.
     */
    @Test
    public void findByPublishDate_success() {

        // prepare
        Article article1 = createIndexedDummyArticle(1L);
        Article article2 = createIndexedDummyArticle(2L);
        Article article3 = createIndexedDummyArticle(3L);
        Article article4 = createIndexedDummyArticle(4L);

        article1.setPublishDate(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        article2.setPublishDate(new GregorianCalendar(2019, Calendar.JANUARY, 2).getTime());
        article3.setPublishDate(new GregorianCalendar(2019, Calendar.JANUARY, 3).getTime());
        article4.setPublishDate(new GregorianCalendar(2019, Calendar.JANUARY, 4).getTime());

        articleRepository.saveAll(Arrays.asList(article1, article2, article3, article4));

        // test
        List<Article> articlesByPublishDate = articleRepository.findByPublishDateBetween(
                new GregorianCalendar(2018, Calendar.DECEMBER, 24).getTime(),
                new GregorianCalendar(2019, Calendar.JANUARY, 3).getTime());

        // verify
        Assert.assertEquals(2, articlesByPublishDate.size());
    }

    /**
     * A simple helper method to create a dummy article.
     *
     * @param index an index added to each field so you can distinguish the objects
     * @return a valid article
     */
    private Article createIndexedDummyArticle(Long index) {

        Article article = new Article();
        article.setHeader("Header #" + index);
        article.setShortDescription("A short description of the article #" + index);
        article.setText("This is the main text #" + index);
        article.setPublishDate(new Date());
        article.setAuthors(Arrays.asList("author" + index, "author" + (index + 1)));
        article.setKeywords(Arrays.asList("keyword" + index, "keyword" + (index + 1)));
        return article;
    }

}