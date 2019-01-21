package com.upday.news.controller;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.upday.news.model.ArticleRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This tests verify the behaviour of the actual REST- Api for articles.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleRepository articleRepository;

    /**
     * This method removes all data from the storage before each test is executed.
     */
    @Before
    public void clearRepository() {
        articleRepository.deleteAll();
    }

    @Test
    public void createUpdateDeleteArticle() throws Exception {

        // prepare
        String article = "{\n" +
                "    \"header\": \"header1111\",\n" +
                "    \"shortDescription\": \"A short description of the article\",\n" +
                "    \"text\": \"Here it comes - the actual article\",\n" +
                "    \"publishDate\": 1547733791804,\n" +
                "    \"authors\": [\n" +
                "        \"author1\",\n" +
                "        \"author2\",\n" +
                "        \"author3\"\n" +
                "    ],\n" +
                "    \"keywords\": [\n" +
                "        \"keyword1\",\n" +
                "        \"keyword2\"\n" +
                "    ]\n" +
                "}";

        // test and verify
        ResultActions createdArticleResult = mockMvc.perform(
                post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(article))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header").value("header1111"));

        String articleId = Document.parse(createdArticleResult.andReturn()
                .getResponse().getContentAsString()).get("articleId").toString();
        String articleUpdate = "{\n" +
                "    \"articleId\": \"" + articleId + "\",\n" +
                "    \"header\": \"header3333\",\n" +
                "    \"shortDescription\": \"A short description of the article\",\n" +
                "    \"text\": \"Here it comes - the actual article\",\n" +
                "    \"publishDate\": 1547733791804,\n" +
                "    \"authors\": [\n" +
                "        \"author1\",\n" +
                "        \"author2\",\n" +
                "        \"author3\"\n" +
                "    ],\n" +
                "    \"keywords\": [\n" +
                "        \"keyword1\",\n" +
                "        \"keyword2\"\n" +
                "    ]\n" +
                "}";

        mockMvc.perform(
                put("/article/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header").value("header3333"));

        mockMvc.perform(
                delete("/article/delete")
                        .param("articleId", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header").value("header3333"));
    }

    @Test
    public void createArticle_incompleteData() throws Exception {

        // prepare
        String invalidArticle = "{\n" +
                "    \"shortDescription\": \"A short description of the article\",\n" +
                "    \"text\": \"Here it comes - the actual article\",\n" +
                "    \"publishDate\": 1547733791804,\n" +
                "    \"authors\": [\n" +
                "        \"author1\",\n" +
                "        \"author2\",\n" +
                "        \"author3\"\n" +
                "    ],\n" +
                "    \"keywords\": [\n" +
                "        \"keyword1\",\n" +
                "        \"keyword2\"\n" +
                "    ]\n" +
                "}";

        // test and verify
        mockMvc.perform(
                post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidArticle))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("header: The header must not be blank."))
                .andExpect(jsonPath("$.details")
                        .value("The given data is incomplete or invalid."));
    }

    @Test
    public void displayArticle_notFound() throws Exception {

        // prepare

        // test and verify
        mockMvc.perform(
                get("/article/display")
                        .param("articleId", "1234567890ABCDEFG"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details")
                        .value("No article with this id found in database."));
    }

    @Test
    public void listForAuthor() throws Exception {

        // prepare
        generateSomeTestData();

        // test and verify
        mockMvc.perform(
                get("/article/listForAuthor")
                        .param("author", "author6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(3));
    }

    @Test
    public void listForPeriod() throws Exception {

        // prepare
        generateSomeTestData();

        // test and verify
        mockMvc.perform(
                get("/article/listForPeriod")
                        .param("from", "1547078400000")
                        .param("to", "1547424000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(3));
    }

    @Test
    public void listForKeyword() throws Exception {

        // prepare
        generateSomeTestData();

        // test and verify
        mockMvc.perform(
                get("/article/findByKeyword")
                        .param("keyword", "keywordx"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(3));
    }

    private void generateSomeTestData() throws Exception {

        List<String> articles = Arrays.asList(
                "{\n" +
                        "    \"header\": \"header1111\",\n" +
                        "    \"shortDescription\": \"A short description of the article\",\n" +
                        "    \"text\": \"Here it comes - the actual article\",\n" +
                        "    \"publishDate\": 1547078400000,\n" +
                        "    \"authors\": [\n" +
                        "        \"author1\",\n" +
                        "        \"author2\",\n" +
                        "        \"author6\"\n" +
                        "    ],\n" +
                        "    \"keywords\": [\n" +
                        "        \"keyword1\",\n" +
                        "        \"keywordx\"\n" +
                        "    ]\n" +
                        "}",
                "{\n" +
                        "    \"header\": \"header2222\",\n" +
                        "    \"shortDescription\": \"A short description of the article\",\n" +
                        "    \"text\": \"Here it comes - the actual article\",\n" +
                        "    \"publishDate\": 1547164800000,\n" +
                        "    \"authors\": [\n" +
                        "        \"author1\",\n" +
                        "        \"author2\",\n" +
                        "        \"author6\"\n" +
                        "    ],\n" +
                        "    \"keywords\": [\n" +
                        "        \"keyword1\",\n" +
                        "        \"keywordx\"\n" +
                        "    ]\n" +
                        "}",
                "{\n" +
                        "    \"header\": \"header3333\",\n" +
                        "    \"shortDescription\": \"A short description of the article\",\n" +
                        "    \"text\": \"Here it comes - the actual article\",\n" +
                        "    \"publishDate\": 1547251200000,\n" +
                        "    \"authors\": [\n" +
                        "        \"author1\",\n" +
                        "        \"author2\",\n" +
                        "        \"author3\"\n" +
                        "    ],\n" +
                        "    \"keywords\": [\n" +
                        "        \"keyword1\",\n" +
                        "        \"keyword2\"\n" +
                        "    ]\n" +
                        "}",
                "{\n" +
                        "    \"header\": \"header4444\",\n" +
                        "    \"shortDescription\": \"A short description of the article\",\n" +
                        "    \"text\": \"Here it comes - the actual article\",\n" +
                        "    \"publishDate\": 1547337600000,\n" +
                        "    \"authors\": [\n" +
                        "        \"author1\",\n" +
                        "        \"author2\",\n" +
                        "        \"author6\"\n" +
                        "    ],\n" +
                        "    \"keywords\": [\n" +
                        "        \"keyword1\",\n" +
                        "        \"keyword2\"\n" +
                        "    ]\n" +
                        "}",
                "{\n" +
                        "    \"header\": \"header5555\",\n" +
                        "    \"shortDescription\": \"A short description of the article\",\n" +
                        "    \"text\": \"Here it comes - the actual article\",\n" +
                        "    \"publishDate\": 1547424000000,\n" +
                        "    \"authors\": [\n" +
                        "        \"author1\",\n" +
                        "        \"author2\",\n" +
                        "        \"author3\"\n" +
                        "    ],\n" +
                        "    \"keywords\": [\n" +
                        "        \"keyword1\",\n" +
                        "        \"keywordx\"\n" +
                        "    ]\n" +
                        "}"
        );

        articles.forEach(article -> {
            try {
                mockMvc.perform(
                        post("/article/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(article));
            } catch (Exception e) {
            }
        });
    }

}