package com.genius.primavera.interfaces;

import com.genius.primavera.application.article.WriteArticleService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WriteArticleService writeArticleService;

    @Test
    @Order(1)
    @DisplayName("게시판 리스트 화면 접근")
    @WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
    public void articles() throws Exception {
        mockMvc.perform(get("/articles").accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(view().name("article/list"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    @DisplayName("1번 게시글 상세 접근")
    @WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
    public void detail() throws Exception {
        mockMvc.perform(get("/articles/1").accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(view().name("article/detail"))
                .andExpect(status().is2xxSuccessful());
    }
}