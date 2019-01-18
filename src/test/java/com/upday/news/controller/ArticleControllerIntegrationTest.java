package com.upday.news.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void createArticle() throws Exception {

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
        mockMvc.perform(
                post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(article))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header").value("header1111"));
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

}