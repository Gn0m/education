package com.example.demo.json_viewer.controller;

import com.example.demo.Configuration;
import com.example.demo.json_viewer.model.User;
import com.example.demo.json_viewer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = Configuration.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User userPost;
    private User userPatch;
    @Autowired
    private UserService service;

    @BeforeEach
    void setUp() {
        userPost = new User("Некрасов", "necrasov@gmail.com");
        userPatch = new User("Есенин", "es@edu.com");
        userPatch.setId(1);
    }

    @Order(1)
    @Test
    void getOneSummary() throws Exception {
        initMethod();

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("http://localhost:8080/api/summary/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                        "id": 1,
                                        "name": "Стрыкало",
                                        "email": "strykalo@mail.ru"
                                   }
                                """)
                );
    }

    @Order(2)
    @Test
    void getOneDetails() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("http://localhost:8080/api/details/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                        "id": 1,
                                        "name": "Стрыкало",
                                        "email": "strykalo@mail.ru",
                                        "orders": [
                                            {
                                                "id": 1,
                                                "sum": 30.00,
                                                "products": [
                                                    {
                                                        "id": 1,
                                                        "name": "Шляпа",
                                                        "price": 10.00
                                                    },
                                                    {
                                                        "id": 2,
                                                        "name": "Вторая шляпа",
                                                        "price": 20.00
                                                    }
                                                ],
                                                "status": "SENT"
                                            },
                                            {
                                                "id": 2,
                                                "sum": 80.00,
                                                "products": [
                                                    {
                                                        "id": 3,
                                                        "name": "Игрушечный паровоз",
                                                        "price": 30.00
                                                    },
                                                    {
                                                        "id": 4,
                                                        "name": "Зонтик",
                                                        "price": 50.00
                                                    }
                                                ],
                                                "status": "CREATED"
                                            }
                                        ]
                                   }
                                """)
                );
    }

    @Order(3)
    @Test
    void getAll() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("http://localhost:8080/api/all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                  [
                                             {
                                                 "id": 1,
                                                 "name": "Стрыкало",
                                                 "email": "strykalo@mail.ru"
                                             },
                                             {
                                                 "id": 2,
                                                 "name": "Якубович",
                                                 "email": "ykub@gmail.ru"
                                             }
                                         ]
                                """)
                );
    }

    @Order(4)
    @Test
    void post() throws Exception {
        String json = objectMapper.writeValueAsString(userPost);
        System.out.println(json);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("http://localhost:8080/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                            "id": 3,
                                            "name": "Некрасов",
                                            "email": "necrasov@gmail.com"
                                   }
                                """)
                );
    }

    @Order(5)
    @Test
    void delete() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/2"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().string(Boolean.TRUE.toString())
                );
    }

    @Order(6)
    @Test
    void update() throws Exception {
        String json = objectMapper.writeValueAsString(userPatch);

        this.mockMvc
                .perform(MockMvcRequestBuilders.patch("http://localhost:8080/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                            "id": 1,
                                            "name": "Есенин",
                                            "email": "es@edu.com"
                                   }
                                """)
                );
    }

    private void initMethod() {
        service.init();
    }
}