package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.DemoApplication;
import com.example.demo.config.SecurityConfig;
import com.example.demo.persistence.model.Book;
import com.example.demo.persistence.repo.BookRepository;
import com.example.demo.web.BookController;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;

import java.nio.charset.Charset;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes={SecurityConfig.class, DemoApplication.class})
public class BookControllerIntegrationTest {

    @MockBean
    private BookRepository bookRepository;
    
    @Autowired
    BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToBooksAndValidBook_thenCorrectResponse() throws Exception {
        String bookStr = "{\"title\": \"bob\", \"author\" : \"bob@domain.com\"}";
        Book bookModel = new Book("bob", "bob@domain.com");

        when(bookRepository.save(bookModel)).thenReturn(bookModel);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
            .content(bookStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON));
    }
    
}